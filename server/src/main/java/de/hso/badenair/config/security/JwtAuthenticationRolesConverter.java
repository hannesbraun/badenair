package de.hso.badenair.config.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthenticationRolesConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLES_CLAIM = "roles";
    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private static Collection<? extends GrantedAuthority> extractRoles(final Jwt jwt) {
        if (!jwt.containsClaim(ROLES_CLAIM)) {
            return Collections.emptyList();
        }

        Collection<String> resourceAccess = jwt.getClaim(ROLES_CLAIM);
        return resourceAccess.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        Collection<GrantedAuthority> authorities = Stream.concat(
            defaultGrantedAuthoritiesConverter.convert(source).stream(),
            extractRoles(source).stream()
        ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(source, authorities);
    }
}
