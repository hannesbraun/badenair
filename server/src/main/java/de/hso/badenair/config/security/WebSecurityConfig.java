package de.hso.badenair.config.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .authorizeRequests()
            .mvcMatchers("/api/customer/public/**").permitAll()
            .mvcMatchers("/api/customer/**").hasAuthority("SCOPE_customers")
            .mvcMatchers("/api/employee/**").hasAuthority("SCOPE_employees")
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(new JwtAuthenticationRolesConverter());
    }
}
