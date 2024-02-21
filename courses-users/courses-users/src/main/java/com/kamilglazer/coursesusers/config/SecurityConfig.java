package com.kamilglazer.coursesusers.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select username, password, active from user_profiles where username=?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select username,role from roles where username=?");

        return jdbcUserDetailsManager;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/courses").permitAll()
                                .requestMatchers("/courseToFavorites/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/favorites").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/courseFromFavorites/{id}").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/addCourse").hasRole("ADMIN")
                                .requestMatchers("/course/**").hasRole("ADMIN")
                                .requestMatchers("/deleteCourse/**").hasRole("ADMIN")
                                .requestMatchers("/register").permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf->csrf.disable());

        return http.build();

    }
}
