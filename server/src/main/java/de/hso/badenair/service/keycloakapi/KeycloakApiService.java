package de.hso.badenair.service.keycloakapi;

import de.hso.badenair.config.keycloakapi.KeycloakApiConfig;
import de.hso.badenair.exception.GetRolesException;
import de.hso.badenair.exception.KeycloakApiAuthenticationException;
import de.hso.badenair.exception.UserNotFoundException;
import de.hso.badenair.service.keycloakapi.dto.CredentialRepresentation;
import de.hso.badenair.service.keycloakapi.dto.KeycloakRole;
import de.hso.badenair.service.keycloakapi.dto.RoleRepresentation;
import de.hso.badenair.service.keycloakapi.dto.UserRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakApiService {

    private static final UserRepresentation[] EMPTY_USER_LIST = {};
    private static final KeycloakRole[] EMPTY_ROLE_LIST = {};
    private static final String CUSTOMER_ROLE_NAME = "badenair_customer";
    private final KeycloakApiConfig config;
    private final TaskScheduler taskScheduler;
    private final RestTemplate restTemplate = new RestTemplate();
    private String accessToken;
    private String refreshToken;
    private Map<EmployeeRole, RoleRepresentation> employeeRoles;
    private RoleRepresentation customerRole;

    /**
     * Retrieves access token from the keyloak server with the given credentials
     */
    @PostConstruct
    private void getToken() {
        final KeycloakAccessToken tokenBody = getKeycloakAccessToken();

        final int refreshDelayInMs = (int) ((tokenBody.getExpires_in() * 0.75) * 1000);

        // Try to get a new access token after 75% of the tokens lifespan has passed
        taskScheduler.scheduleWithFixedDelay(this::refreshAccessToken, refreshDelayInMs);
        getRolesForInit();
    }

    /**
     * @return Returns the needed attributes of the access token request as an object
     */
    private KeycloakAccessToken getKeycloakAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "admin-cli");
        body.add("username", config.getUsername());
        body.add("password", config.getPassword());
        body.add("grant_type", "password");

        return getAccessToken(body);
    }

    private void refreshAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "admin-cli");
        body.add("refresh_token", refreshToken);
        body.add("grant_type", "refresh_token");

        getAccessToken(body);
    }

    /**
     * @param body Body of the access token request
     * @return Returns the needed attributes of the access token request as an object
     */
    private KeycloakAccessToken getAccessToken(MultiValueMap<String, String> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        final String url = "http://" + config.getHost() + "/auth/realms/master/protocol/openid-connect/token";

        final ResponseEntity<KeycloakAccessToken> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, KeycloakAccessToken.class);
        final KeycloakAccessToken tokenBody = Optional.ofNullable(exchange.getBody())
            .orElseThrow(KeycloakApiAuthenticationException::new);

        accessToken = tokenBody.getAccess_token();
        refreshToken = tokenBody.getRefresh_token();

        return tokenBody;
    }

    /**
     * Creates a new employee user account
     *
     * @param username Name of the user to create (must be unique)
     * @param role     Role the user should have
     */
    public void createEmployeeUser(String username, EmployeeRole role) {
        final UserRepresentation user = createUser(username);

        HttpEntity<UserRepresentation> entity = new HttpEntity<>(user, getAuthHeader());

        final String url = getBaseUrl() + "users";

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

            RoleRepresentation[] roles = {employeeRoles.get(EmployeeRole.DEFAULT), employeeRoles.get(role)};
            final UserRepresentation userRepresentation = getUser(username)
                .orElseThrow(() -> new UserNotFoundException(username));
            addRoleToUser(roles, userRepresentation.getId());
            RoleRepresentation[] rolesToRemove = {customerRole};
            removeRoleFromUser(rolesToRemove, userRepresentation.getId());
        } catch (HttpClientErrorException e) {
            log.warn("User {} could not be created: {}", username, e.getMessage());
        }

    }

    /**
     * Creates a new customer user account
     *
     * @param username Name of the user to create (must be unique)
     */
    public void createCustomerUser(String username) {
        final UserRepresentation user = createUser(username);

        HttpEntity<UserRepresentation> entity = new HttpEntity<>(user, getAuthHeader());

        final String url = getBaseUrl() + "users";

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
        } catch (HttpClientErrorException e) {
            log.warn("User {} could not be created: {}", username, e.getMessage());
        }

    }

    /**
     * @return Returns all customer user accounts
     */
    public List<UserRepresentation> getCustomerUsers() {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(getAuthHeader());

        final String url = getBaseUrl() + "roles/" + CUSTOMER_ROLE_NAME + "/users?max=2000";
        final ResponseEntity<UserRepresentation[]> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, UserRepresentation[].class);

        final UserRepresentation[] body = Optional.ofNullable(exchange.getBody()).orElse(EMPTY_USER_LIST);

        return Arrays.asList(body);
    }

    /**
     * @return Returns all employee user accounts
     */
    public List<UserRepresentation> getEmployeeUsers() {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(getAuthHeader());

        final String url = getBaseUrl() + "roles/" + EmployeeRole.DEFAULT.getName() + "/users?max=1000";
        final ResponseEntity<UserRepresentation[]> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, UserRepresentation[].class);

        final UserRepresentation[] body = Optional.ofNullable(exchange.getBody()).orElse(EMPTY_USER_LIST);

        return Arrays.asList(body);
    }


    /**
     * @param role Role of the employees
     * @return Returns all employee user accounts with the specified role
     */
    public List<UserRepresentation> getEmployeeUsersWithRole(EmployeeRole role) {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(getAuthHeader());

        final String url = getBaseUrl() + "roles/" + role.getName() + "/users?max=1000";
        final ResponseEntity<UserRepresentation[]> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, UserRepresentation[].class);

        final UserRepresentation[] body = Optional.ofNullable(exchange.getBody()).orElse(EMPTY_USER_LIST);

        return Arrays.asList(body);
    }

    /**
     * @param username Name of the user to seach for
     * @return Returns an {@link Optional} with a user
     */
    public Optional<UserRepresentation> getUser(String username) {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(getAuthHeader());

        final String url = getBaseUrl() + "users?username=" + username;
        final ResponseEntity<UserRepresentation[]> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, UserRepresentation[].class);

        final UserRepresentation[] body = Optional.ofNullable(exchange.getBody()).orElse(EMPTY_USER_LIST);

        if (body.length > 0) {
            return Optional.of(body[0]);
        }

        return Optional.empty();
    }


    /**
     * @param id Id of the user
     * @return Returns whether the user is a pilot or not
     */
    public boolean isPilot(String id) {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(getAuthHeader());

        final String url = getBaseUrl() + "users/" + id + "/role-mappings/realm";
        final ResponseEntity<KeycloakRole[]> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, KeycloakRole[].class);

        final KeycloakRole[] body = Optional.ofNullable(exchange.getBody()).orElse(EMPTY_ROLE_LIST);

        return Arrays.stream(body)
            .map(KeycloakRole::getName)
            .anyMatch(name -> name.equals(EmployeeRole.DASH_PILOT.getName()) || name.equals(EmployeeRole.JET_PILOT.getName()));
    }

    /**
     * Retrieves the roles with ids needed for user creation
     */
    private void getRolesForInit() {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(getAuthHeader());

        final String url = getBaseUrl() + "roles";
        final ResponseEntity<RoleRepresentation[]> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, RoleRepresentation[].class);

        final RoleRepresentation[] roles = Optional.ofNullable(exchange.getBody()).orElseThrow(GetRolesException::new);
        final List<String> employeeRolesAsString = Arrays.stream(EmployeeRole.values()).map(EmployeeRole::getName).collect(Collectors.toList());

        employeeRoles = Arrays.stream(roles).filter(role -> employeeRolesAsString.contains(role.getName()))
            .collect(Collectors.toMap(
                role -> EmployeeRole.fromString(role.getName()),
                Function.identity()
            ));

        customerRole = Arrays.stream(roles).filter(role -> role.getName().equals(CUSTOMER_ROLE_NAME))
            .findFirst().orElseThrow(GetRolesException::new);
    }

    /**
     * @param username Name of the user
     * @return Returns a user with the default password '1234'
     */
    private UserRepresentation createUser(String username) {
        return UserRepresentation.builder()
            .username(username)
            .firstName(username)
            .lastName(username)
            .email(username + "@mail.de")
            .enabled(true)
            .credentials(List.of(CredentialRepresentation.builder()
                .type("password")
                .temporary(false)
                .value("1234")
                .build()))
            .build();
    }

    /**
     * Adds the specified rules to the user
     *
     * @param roleRepresentations Roles the user will be given
     * @param userId              ID of the user
     */
    private void addRoleToUser(RoleRepresentation[] roleRepresentations, String userId) {
        HttpEntity<RoleRepresentation[]> entity = new HttpEntity<>(roleRepresentations, getAuthHeader());

        final String url = "http://" + config.getHost() + "/auth/admin/realms/badenair/users/" + userId + "/role-mappings/realm";
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    /**
     * Removes the specified roles to the user
     *
     * @param roleRepresentations Roles the user will be given
     * @param userId              ID of the user
     */
    private void removeRoleFromUser(RoleRepresentation[] roleRepresentations, String userId) {
        HttpEntity<RoleRepresentation[]> entity = new HttpEntity<>(roleRepresentations, getAuthHeader());

        final String url = "http://" + config.getHost() + "/auth/admin/realms/badenair/users/" + userId + "/role-mappings/realm";
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    /**
     * @return Returns a header with the 'Authorization' field set to a valid token
     */
    private HttpHeaders getAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    /**
     * @return Returns the base URL of the keycloak API
     */
    private String getBaseUrl() {
        return "http://" + config.getHost() + "/auth/admin/realms/badenair/";
    }
}
