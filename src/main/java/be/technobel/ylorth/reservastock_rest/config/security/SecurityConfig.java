package be.technobel.ylorth.reservastock_rest.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {

        http.csrf().disable();

        http.httpBasic().disable();

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests(
            registry -> registry

                    .requestMatchers("/auth/login").anonymous()
                    .requestMatchers("/auth/studentRegister").anonymous()
                    .requestMatchers("/auth/sendPasswordMail").anonymous()
                    .requestMatchers("/auth/newPassword").anonymous()
                    .requestMatchers("/auth/register").hasRole("ADMIN")
                    .requestMatchers("/auth/toValidate").hasRole("ADMIN")
                    .requestMatchers("/auth/validate/{id:[0-9]+}").hasRole("ADMIN")
                    .requestMatchers("/auth/unValidate/{id:[0-9]+}").hasRole("ADMIN")


                    .requestMatchers(HttpMethod.POST, "/room/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/room/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/room/**").authenticated()

                    .requestMatchers(HttpMethod.POST, "/material/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/material/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/material/**").authenticated()

                    .requestMatchers(HttpMethod.GET,"/request/allUnconfirmed").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/request/{id:[0-9]+}").authenticated()
                    .requestMatchers(HttpMethod.PATCH,"/request/{id:[0-9]+}/confirm").hasRole("ADMIN")
                    .requestMatchers("/request/**").authenticated()


                    .requestMatchers( request -> request.getRequestURI().length() > 500 ).denyAll()

                    // Si pas permitAll, swagger ne fonctionne pas
                    .anyRequest().permitAll()

        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
