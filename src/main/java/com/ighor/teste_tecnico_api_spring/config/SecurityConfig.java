package com.ighor.teste_tecnico_api_spring.config;

import jakarta.servlet.DispatcherType;
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

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter){
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                //Desativa o CSRF
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                //Configura CORS
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configure(http))
                //Define a sessão como STATELESS
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Permite algumas rotas sem autenticação
                //Isso é necessário porque precisamos dessas rotas para conseguir pegar o token.
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        //Libera erros internos do Spring
                        //Isso evita que páginas de erro 403/404 criem loops de autenticação.
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        //User pode acessar POST /auth/login (fazer login)
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        //User pode acessar POST /auth/register (criar conta)
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        //Parece errado deixa essa linha, mas deixei por ser teste, pra facilitar o fluxo de uso do app
                        .requestMatchers(HttpMethod.POST, "/account/register").permitAll()
                        //Exige autenticação em qualquer outra rota
                        .anyRequest().authenticated())
                //Adiciona o meu filtro JWT
                //Isso coloca o meu SecurityFilter (o filtro que valida tokens) ANTES do filtro padrão de login do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    //cria e registra um bean de PasswordEncoder usando BCrypt
    //Permite que o use BCrypt para criptografar senhas
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
