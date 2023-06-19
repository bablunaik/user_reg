package com.UserRegistraion.security;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/users/**").permitAll()//** is access by all
                .anyRequest().authenticated()
                .and()
                .csrf().disable(); // Disable CSRF protection for simplicity, you may configure it properly for your application
    }

    // Other configuration methods...
}

