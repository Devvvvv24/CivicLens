package CivicLens.Backend.config;

import CivicLens.Backend.user.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // We are using JWT, so disable CSRF for our API.
                .csrf(csrf -> csrf.disable())


                // Enable CORS configuration below.
                .cors(Customizer.withDefaults())


                // JWT means we don't use normal server sessions.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // Authorization rules.
                .authorizeHttpRequests(auth -> auth


                        // =========================
                        // PUBLIC AUTH API
                        // =========================

                        .requestMatchers(
                                "/api/auth/**"
                        )
                        .permitAll()


                        // =========================
                        // PUBLIC MAP API
                        // =========================

                        .requestMatchers(
                                "/api/public/**"
                        )
                        .permitAll()


                        // =========================
                        // UPLOADED IMAGES
                        // =========================

                        // Anyone can view uploaded images.
                        .requestMatchers(
                                "/uploads/**"
                        )
                        .permitAll()


                        // Only logged-in users can upload.
                        .requestMatchers(
                                "/api/uploads/**"
                        )
                        .authenticated()


                        // =========================
                        // ADMIN ISSUE APIs
                        // =========================

                        // Only admin can see ALL issues.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/issues"
                        )
                        .hasRole("ADMIN")


                        // Only admin can update status.
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/issues/*/status"
                        )
                        .hasRole("ADMIN")


                        // Only admin can delete issues.
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/issues/*"
                        )
                        .hasRole("ADMIN")


                        // =========================
                        // CITIZEN ISSUE APIs
                        // =========================

                        // Logged-in user can see their own reports.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/issues/my"
                        )
                        .authenticated()


                        // Logged-in user can create reports.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/issues"
                        )
                        .authenticated()


                        // Logged-in users can access one specific issue.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/issues/*"
                        )
                        .authenticated()


                        // =========================
                        // WEBSITE PAGES
                        // =========================

                        // Homepage, login, register,
                        // CSS, JS, Thymeleaf pages etc.
                        .anyRequest()
                        .permitAll()
                )


                // Check JWT before Spring's normal
                // username/password authentication filter.
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }


    // =========================
    // PASSWORD ENCODER
    // =========================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================
    // CORS CONFIGURATION
    // =========================

    @Bean
    public CorsConfigurationSource corsConfigurationSource(

            @Value("${app.cors.allowed-origins}")
            String allowedOrigins) {


        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(

                Arrays
                        .stream(
                                allowedOrigins.split(",")
                        )
                        .map(String::trim)
                        .toList()
        );


        configuration.setAllowedMethods(

                List.of(
                        "GET",
                        "POST",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );


        configuration.setAllowedHeaders(

                List.of(
                        "Authorization",
                        "Content-Type"
                )
        );


        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }
}