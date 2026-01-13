package in.techcamp.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(  "/", "/prototype/**", "/user/**", "/user/login").permitAll()//ログイン不要　許可範囲保留
                        .requestMatchers(HttpMethod.POST, "/prototype/**", "/user/**").permitAll()//ログイン不要　許可範囲保留
                        .anyRequest().permitAll())//一旦全ページログイン不要で表示できるよう許可しています。
                        // .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginProcessingUrl("/login")
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .usernameParameter("email") 
                        .permitAll())

                .logout(logout -> logout
                        .logoutUrl("/login")
                        .logoutSuccessUrl("/"));

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}