package com.auronbox.auronbox.config;

import com.auronbox.auronbox.security.FilterToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    @Autowired
    private FilterToken filter;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests(authorizeConfig -> {

                    // Arquivos estáticos — libera tudo
                    authorizeConfig.requestMatchers(
                            "/", "/index.html", "/menuGestao.html", "/loginAdmin.html",
                            "/gestaoProdutos.html", "/gestaoPedidos.html",
                            "/gestaoUsuarios.html",          // ← sua nova página
                            "/css/**", "/js/**", "/icon/**", "/img/**", "/fonts/**"
                    ).permitAll();

                    // Rotas de API abertas
                    authorizeConfig.requestMatchers(HttpMethod.POST, "/usuarios/email/**").permitAll();
                    authorizeConfig.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
                    authorizeConfig.requestMatchers(HttpMethod.GET, "/produtos/**").permitAll();

                    // Rotas protegidas
                    authorizeConfig.requestMatchers("/produtos/**").authenticated();
                    authorizeConfig.requestMatchers("/usuarios/**").authenticated();
                    authorizeConfig.requestMatchers("/carrinhos/**").authenticated();
                    authorizeConfig.requestMatchers("/pedidos/**").authenticated();

                    authorizeConfig.anyRequest().permitAll();
                })
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
