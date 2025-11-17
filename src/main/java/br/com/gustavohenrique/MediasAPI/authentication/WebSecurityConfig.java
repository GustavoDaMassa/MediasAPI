package br.com.gustavohenrique.MediasAPI.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(2)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/web/**", "/login", "/logout")
                .csrf(AbstractHttpConfigurer::disable) // Simplificando para o exemplo, considere habilitar em produção
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/web/login", "/web/register", "/css/**", "/web/").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> {
                    form.loginPage("/web/login");
                    form.loginProcessingUrl("/login"); // URL que o Spring Security vai interceptar
                    form.usernameParameter("email"); // Especifica que o parâmetro de usuário é 'email'
                    form.defaultSuccessUrl("/web/courses", true); // Redireciona após o login
                    form.permitAll();
                })
                .logout(logout -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                    logout.logoutSuccessUrl("/web/login?logout");
                    logout.permitAll();
                });

        return http.build();
    }
}
