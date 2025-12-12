package dev.cong.v.springcomereme.security;

import dev.cong.v.springcomereme.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilter {

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        HttpMethod [] authorizedMethods =  new HttpMethod[]{HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        http.cors(cors -> cors
                .configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3500"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
                    config.setAllowCredentials(true);
                    return config;
                }));

        http.authorizeHttpRequests(configure -> configure

                .requestMatchers("/order/all").hasRole("ADMIN")
                .requestMatchers("/product/all").hasRole("ADMIN")
                .requestMatchers("/auth/admin/**").hasRole("ADMIN")


                .requestMatchers("/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/category/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/brand/**").permitAll()


                .requestMatchers(HttpMethod.GET,"/product/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/color/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/file/**").permitAll()

                .requestMatchers(HttpMethod.GET,"/size/**").permitAll()

                // forgot password

                .requestMatchers("/password/**").permitAll()

                .requestMatchers("/product-item/**").permitAll()

                .requestMatchers("/file/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/momo/create/**").permitAll()
                // admin

                .requestMatchers(Arrays.toString(authorizedMethods),"/product/**").hasRole("ADMIN")
                .requestMatchers(Arrays.toString(authorizedMethods),"/product-item/**").hasRole("ADMIN")
                .requestMatchers(Arrays.toString(authorizedMethods),"/size/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/order/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/order/**").hasRole("ADMIN")







                .anyRequest()
                .authenticated()

        );

        http.authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);

        // disable CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
