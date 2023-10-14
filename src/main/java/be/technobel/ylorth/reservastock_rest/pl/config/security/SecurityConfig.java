package be.technobel.ylorth.reservastock_rest.pl.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {

        http.cors();

        http.csrf(AbstractHttpConfigurer::disable);

        http.httpBasic().disable();

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests(
            registry -> registry


                    .requestMatchers(HttpMethod.POST,"/auth/login").anonymous()
                    .requestMatchers(HttpMethod.POST,"/auth/studentRegister").anonymous()
                    .requestMatchers(HttpMethod.POST,"/auth/sendPasswordMail").anonymous()
                    .requestMatchers(HttpMethod.POST,"/auth/newPassword").anonymous()
                    .requestMatchers(HttpMethod.POST,"/auth/register").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/auth/toValidate").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/auth/validate/{id:[0-9]+}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/auth/unValidate/{id:[0-9]+}").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.POST, "/room/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/room/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/room/**").authenticated()

                    .requestMatchers(HttpMethod.POST, "/material/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/material/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/material/**").authenticated()

                    .requestMatchers(HttpMethod.GET,"/request/allUnconfirmed").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/request/freeRooms/{id:[0-9]+}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/request/{id:[0-9]+}").authenticated()
                    .requestMatchers(HttpMethod.PATCH,"/request/{id:[0-9]+}/confirm").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET,"/request/**").authenticated()
                    .requestMatchers(HttpMethod.POST,"/request/**").authenticated()

                    .requestMatchers(HttpMethod.POST, "/api/upload").authenticated()

                    .requestMatchers(CorsUtils::isPreFlightRequest).authenticated()

                    .requestMatchers( request -> request.getRequestURI().length() > 500 ).denyAll()

                    // Si pas permitAll, swagger ne fonctionne pas
                    .anyRequest().denyAll()


        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
