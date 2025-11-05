package br.com.fiap.universidade_fiap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan
@ComponentScan
@SpringBootApplication(exclude = {
	// Excluir autoconfiguração do Spring AI quando não configurado
	// Isso previne erros de inicialização quando não há API key configurada
	org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration.class,
	org.springframework.ai.autoconfigure.ollama.OllamaAutoConfiguration.class
})
public class UniversidadeFiapApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversidadeFiapApplication.class, args);
	}

}
