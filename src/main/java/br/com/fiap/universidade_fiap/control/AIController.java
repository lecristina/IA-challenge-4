package br.com.fiap.universidade_fiap.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.service.AIService;
import br.com.fiap.universidade_fiap.service.AIServiceFallback;
import br.com.fiap.universidade_fiap.service.AuthenticationService;

@Controller
public class AIController {

    private static final Logger logger = LoggerFactory.getLogger(AIController.class);

    @Autowired(required = false)
    private AIService aiService;

    @Autowired(required = false)
    private AIServiceFallback aiServiceFallback;

    @Autowired(required = false)
    private AuthenticationService authenticationService;

    @GetMapping("/ai/chat")
    public ModelAndView chat() {
        logger.debug("Acessando página do chatbot");
        ModelAndView mv = new ModelAndView("ai/chat");
        
        try {
            // Adicionar objetos básicos primeiro
            mv.addObject("titulo", "Assistente IA - TrackZone");
            mv.addObject("aiDisponivel", aiService != null);
            
            // Tentar adicionar usuário logado (opcional)
            if (authenticationService != null) {
                try {
                    authenticationService.adicionarUsuarioLogado(mv);
                } catch (Exception e) {
                    logger.debug("Não foi possível adicionar usuário logado: {}", e.getMessage());
                    // Adicionar null para evitar erro no template
                    mv.addObject("usuario_logado", null);
                }
            } else {
                // Se não houver serviço, adicionar null explicitamente
                mv.addObject("usuario_logado", null);
            }
            
            logger.debug("Página do chatbot carregada com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao carregar página do chatbot: {}", e.getMessage(), e);
            // Em caso de erro, retornar página básica mesmo assim
            mv = new ModelAndView("ai/chat");
            mv.addObject("titulo", "Assistente IA - TrackZone");
            mv.addObject("aiDisponivel", false);
            mv.addObject("usuario_logado", null);
        }
        
        return mv;
    }

    @PostMapping("/ai/perguntar")
    @ResponseBody
    public String perguntar(@RequestParam String pergunta, @RequestParam(required = false) String contexto) {
        logger.debug("Pergunta recebida: {}", pergunta);
        
        try {
            String contextoFinal = contexto != null ? contexto : "Sistema de gestão de motos TrackZone";
            
            // Tentar usar AIService se disponível
            if (aiService != null) {
                try {
                    return aiService.obterSugestao(contextoFinal, pergunta);
                } catch (Exception e) {
                    logger.warn("Erro ao usar AIService, tentando fallback: {}", e.getMessage());
                }
            }
            
            // Usar fallback se disponível
            if (aiServiceFallback != null) {
                try {
                    return aiServiceFallback.obterSugestao(contextoFinal, pergunta);
                } catch (Exception e) {
                    logger.warn("Erro ao usar AIServiceFallback: {}", e.getMessage());
                }
            }
            
            // Resposta padrão se nada funcionar
            return "💡 **Assistente TrackZone**\n\nDesculpe, o serviço de IA não está disponível no momento. " +
                   "Tente novamente mais tarde ou use as perguntas rápidas disponíveis na página.";
        } catch (Exception e) {
            logger.error("Erro ao processar pergunta: {}", e.getMessage(), e);
            return "Erro ao processar sua pergunta. Tente novamente.";
        }
    }

    @PostMapping("/ai/analisar-operacao")
    @ResponseBody
    public String analisarOperacao(@RequestParam String dadosOperacao) {
        logger.debug("Análise de operação solicitada");
        
        try {
            // Tentar usar AIService se disponível
            if (aiService != null) {
                try {
                    return aiService.analisarOperacao(dadosOperacao);
                } catch (Exception e) {
                    logger.warn("Erro ao usar AIService para análise, tentando fallback: {}", e.getMessage());
                }
            }
            
            // Usar fallback se disponível
            if (aiServiceFallback != null) {
                try {
                    return aiServiceFallback.analisarOperacao(dadosOperacao);
                } catch (Exception e) {
                    logger.warn("Erro ao usar AIServiceFallback para análise: {}", e.getMessage());
                }
            }
            
            // Resposta padrão
            return "Análise não disponível no momento. Verifique manualmente os dados da operação.";
        } catch (Exception e) {
            logger.error("Erro ao analisar operação: {}", e.getMessage(), e);
            return "Erro ao analisar operação. Tente novamente.";
        }
    }
}

