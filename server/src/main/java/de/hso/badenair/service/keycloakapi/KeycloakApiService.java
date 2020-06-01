package de.hso.badenair.service.keycloakapi;

import de.hso.badenair.config.keycloakapi.KeycloakApiConfig;
import de.hso.badenair.exception.GetRolesException;
import de.hso.badenair.exception.KeycloakApiAuthenticationException;
import de.hso.badenair.exception.UserNotFoundException;
import de.hso.badenair.service.keycloakapi.dto.CredentialRepresentation;
import de.hso.badenair.service.keycloakapi.dto.RoleRepresentation;
import de.hso.badenair.service.keycloakapi.dto.UserRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
    private static final String CUSTOMER_ROLE_NAME = "badenair_customer";
    private final KeycloakApiConfig config;
    private final RestTemplate restTemplate = new RestTemplate();
    private String accessToken;
    private Map<EmployeeRole, RoleRepresentation> employeeRoles;
    private RoleRepresentation customerRole;

    /**
     * Retrieves access token from the keyloak server with the given credentials
     */
    @PostConstruct
    private void getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "admin-cli");
        map.add("username", config.getUsername());
        map.add("password", config.getPassword());
        map.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        final String url = "http://" + config.getHost() + "/auth/realms/master/protocol/openid-connect/token";

        final ResponseEntity<KeycloakAccessToken> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, KeycloakAccessToken.class);
        final KeycloakAccessToken tokenBody = Optional.ofNullable(exchange.getBody())
            .orElseThrow(KeycloakApiAuthenticationException::new);

        accessToken = tokenBody.getAccess_token();
        getRolesForInit();
    }

    /**
     * Creates a new employee user account
     * @param username Name of the user to create (must be unique)
     * @param role Role the user should have
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
     * @param roleRepresentations Roles the user will be given
     * @param userId ID of the user
     */
    private void addRoleToUser(RoleRepresentation[] roleRepresentations, String userId) {
        HttpEntity<RoleRepresentation[]> entity = new HttpEntity<>(roleRepresentations, getAuthHeader());

        final String url = "http://" + config.getHost() + "/auth/admin/realms/badenair/users/" + userId + "/role-mappings/realm";
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    /**
     * Removes the specified roles to the user
     * @param roleRepresentations Roles the user will be given
     * @param userId ID of the user
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
