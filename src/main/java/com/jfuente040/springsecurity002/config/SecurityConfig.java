package com.jfuente040.springsecurity002.config;

import com.jfuente040.springsecurity002.config.filter.JwtTokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration 
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtTokenValidator jwtTokenValidator;

    public SecurityConfig(UserDetailsService userDetailsService, JwtTokenValidator jwtTokenValidator) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/auth/hello", "/auth/login", "/auth/signup").permitAll()
                        // Documentación de API
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // Endpoints que requieren autenticación
                        .requestMatchers("/auth/hello-secured", "/auth/others").authenticated()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Agregar el filtro JWT antes del BasicAuthenticationFilter
                .addFilterBefore(jwtTokenValidator, BasicAuthenticationFilter.class)
                .build();
    }



    // -- BEANS FOR AUTHENTICATION AND AUTHORIZATION USING AUTHENTICATION MANAGER --
   
    //1- AuthenticationManager bean for authentication purposes
    //El metodo que usa AuthenticationConfiguration para obtener el AuthenticationManager
    //es el recomendado en las versiones recientes de Spring Security.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //2- AuthenticationProvider bean for authentication
    //By default, we are using the default authentication provider provided by Spring Security
    // No need to define a custom AuthenticationProvider as we are using InMemoryUserDetailsManager by default
    // Next, we will define a custom AuthenticationProvider to demonstrate how to use it
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // You can define a custom AuthenticationProvider here if needed
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider; // Returning the custom provider
    }

    //3- PasswordEncoder bean for encoding passwords
    // BCryptPasswordEncoder is a strong password encoder that uses the BCrypt hashing function
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Using BCryptPasswordEncoder for password encoding
    }

    //4- UserDetailsService bean for user details management
    // InMemoryUserDetailsManager is used to store user details in memory for testing purposes
    // In a real application, you would typically use a database or another persistent storage
    // to store user details.
    /*
    @Bean
    public UserDetailsService userDetailsService() {
       UserDetails user = User.withUsername("jfuente")
                // asign password using BCryptPasswordEncoder
                //emule the password encoding process when the user is created in the database
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .authorities("READ", "CREATE")
                .build();
        return new InMemoryUserDetailsManager(user);        
    }
    */

  
}
