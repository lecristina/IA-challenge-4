package br.com.fiap.universidade_fiap.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@Configuration
public class SegurancaConfig {

	@Bean
	public SecurityFilterChain chain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(request -> request
				// Rotas públicas
				.requestMatchers("/login", "/usuario/novo", "/insere_usuario", "/css/**", "/js/**", "/img/**", "/error/**")
				.permitAll()
				// Rotas específicas por perfil
				.requestMatchers("/usuario/lista", "/usuario/excluir/**").hasRole("ADMIN")
				.requestMatchers("/dashboard", "/relatorios/**").hasAnyRole("ADMIN", "GERENTE")
				.requestMatchers("/motos/**", "/operacoes/**").hasAnyRole("ADMIN", "GERENTE", "OPERADOR")
				.requestMatchers("/ai/**").authenticated()
				.requestMatchers("/acesso_negado").permitAll()
				// Todas as outras rotas requerem autenticação
				.anyRequest().authenticated())
				.formLogin(
						login -> login.loginPage("/login").usernameParameter("username").passwordParameter("password")
								.defaultSuccessUrl("/", true).failureUrl("/login?falha=true").permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true").permitAll())
				.exceptionHandling(exception -> exception.accessDeniedHandler((request, response, ex) -> {
					response.sendRedirect("/acesso_negado");
				}))
				.csrf(csrf -> csrf.ignoringRequestMatchers("/logout", "/ai/perguntar", "/ai/analisar-operacao"))
				.sessionManagement(session -> session
					.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
					.maximumSessions(1)
					.maxSessionsPreventsLogin(false)
					.sessionRegistry(sessionRegistry()));

		return http.build();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
}
