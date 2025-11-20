package com.gs.fiap.jobfitscore.infra.security;

import com.gs.fiap.jobfitscore.domain.autenticacao.Role;
import com.gs.fiap.jobfitscore.domain.autenticacao.UsuarioEmpresaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final UsuarioEmpresaUserDetailsService userDetailsService;
	private final FiltroTokenAcesso filtroTokenAcesso;
	
	public SecurityConfig(UsuarioEmpresaUserDetailsService userDetailsService,
	                      FiltroTokenAcesso filtroTokenAcesso) {
		this.userDetailsService = userDetailsService;
		this.filtroTokenAcesso = filtroTokenAcesso;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> {}) // Habilita CORS
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/autenticacao/**").permitAll()
						.requestMatchers("/api/usuarios/cadastrar").permitAll()
						.requestMatchers("/api/empresas/cadastrar").permitAll()
						.requestMatchers(
								"/",
								"/index.html",
								"/style.css",
								"/logo.jpeg",
								"/images/**",
								"/static/**"
						).permitAll()
						.requestMatchers(
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/swagger-resources/**",
								"/webjars/**"
						).permitAll()
						.requestMatchers("/api/usuarios/atualizar", "/api/usuarios/deletar")
							.hasRole(Role.USUARIO.name())
						.requestMatchers("/api/empresas/atualizar", "/api/empresas/deletar")
							.hasRole(Role.EMPRESA.name())
						.requestMatchers("/api/empresas/buscar-por-email").permitAll()
						.requestMatchers("/api/usuarios/buscar-por-email").permitAll()
						.requestMatchers(
								"/api/cursos/**",
								"/api/vagas/**",
								"/api/habilidades/**",
								"/api/vaga-habilidade/**",
								"/api/usuario-habilidade/**",
								"/api/candidaturas/**"
						).hasAnyRole(Role.USUARIO.name(), Role.EMPRESA.name())
						.anyRequest().authenticated()
				)
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(filtroTokenAcesso, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		hierarchy.setHierarchy(
				"ROLE_ADMIN > ROLE_USUARIO\n" +
						"ROLE_ADMIN > ROLE_EMPRESA"
		);
		return hierarchy;
	}
	
	@Bean
	public SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchy());
		return handler;
	}

	@Bean
		public CorsFilter corsFilter() {
			CorsConfiguration config = new CorsConfiguration();
			config.addAllowedOriginPattern("*");
			config.addAllowedHeader("*");
			config.addAllowedMethod("*");
			config.setAllowCredentials(true);

			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", config);

			return new CorsFilter(source);
		}

}