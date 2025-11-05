package br.com.fiap.universidade_fiap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Serviço de IA usando Spring AI (quando disponível)
 * Usa fallback quando Spring AI não está configurado
 * 
 * IMPORTANTE: Este serviço só será criado se a classe ChatModel estiver no classpath
 * Se não estiver, o AIServiceFallback será usado automaticamente
 */
@Service
@ConditionalOnClass(name = "org.springframework.ai.chat.ChatModel")
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    
    @Autowired(required = false)
    private ApplicationContext applicationContext;
    
    @Autowired(required = false)
    private AIServiceFallback fallback;
    
    private Object chatModel;
    private volatile boolean initialized = false;
    
    private void initChatModel() {
        if (initialized) {
            return;
        }
        
        synchronized (this) {
            if (initialized) {
                return;
            }
            
            if (applicationContext == null) {
                logger.warn("ApplicationContext não disponível para inicializar Spring AI");
                initialized = true;
                return;
            }
            
            try {
                Class<?> chatModelClass = Class.forName("org.springframework.ai.chat.ChatModel");
                var beans = applicationContext.getBeansOfType(chatModelClass);
                if (!beans.isEmpty()) {
                    chatModel = beans.values().iterator().next();
                    logger.info("Spring AI ChatModel inicializado: {}", chatModel.getClass().getSimpleName());
                    initialized = true;
                    return;
                }
            } catch (ClassNotFoundException e) {
                logger.debug("Spring AI ChatModel não encontrado no classpath");
            } catch (Exception e) {
                logger.warn("Erro ao buscar ChatModel: {}", e.getMessage());
            }
            
            chatModel = null;
            initialized = true;
        }
    }

    public String obterSugestao(String contexto, String pergunta) {
        initChatModel();
        
        if (chatModel == null) {
            if (fallback != null) {
                return fallback.obterSugestao(contexto, pergunta);
            }
            return "Serviço de IA não disponível no momento. Use as perguntas rápidas ou consulte o menu de ajuda.";
        }

        try {
            String promptText = String.format(
                "Você é um assistente especializado em gestão de motos para logística. " +
                "Contexto: %s\n\nPergunta: %s\n\n" +
                "Forneça uma resposta útil, prática e objetiva em português brasileiro.",
                contexto, pergunta
            );

            Class<?> promptClass = Class.forName("org.springframework.ai.chat.prompt.Prompt");
            Class<?> userMessageClass = Class.forName("org.springframework.ai.chat.messages.UserMessage");
            
            Object userMessage = userMessageClass.getConstructor(String.class).newInstance(promptText);
            java.util.List<Object> messages = java.util.Arrays.asList(userMessage);
            Object prompt = promptClass.getConstructor(java.util.List.class).newInstance(messages);
            
            java.lang.reflect.Method callMethod = chatModel.getClass().getMethod("call", promptClass);
            Object chatResponse = callMethod.invoke(chatModel, prompt);
            
            java.lang.reflect.Method getResultMethod = chatResponse.getClass().getMethod("getResult");
            Object result = getResultMethod.invoke(chatResponse);
            
            java.lang.reflect.Method getOutputMethod = result.getClass().getMethod("getOutput");
            Object output = getOutputMethod.invoke(result);
            
            java.lang.reflect.Method getContentMethod = output.getClass().getMethod("getContent");
            String content = (String) getContentMethod.invoke(output);
            
            return content != null && !content.isEmpty() ? content : 
                   (fallback != null ? fallback.obterSugestao(contexto, pergunta) : "Resposta não disponível.");
        } catch (Exception e) {
            logger.error("Erro ao chamar IA: {}", e.getMessage(), e);
            return fallback != null ? fallback.obterSugestao(contexto, pergunta) : 
                   "Erro ao processar pergunta. Tente novamente.";
        }
    }

    public String analisarOperacao(String dadosOperacao) {
        initChatModel();
        
        if (chatModel == null) {
            return fallback != null ? fallback.analisarOperacao(dadosOperacao) : 
                   "Análise não disponível no momento.";
        }

        try {
            String promptText = String.format(
                "Analise a seguinte operação de moto e forneça sugestões em português brasileiro:\n%s",
                dadosOperacao
            );

            Class<?> promptClass = Class.forName("org.springframework.ai.chat.prompt.Prompt");
            Class<?> userMessageClass = Class.forName("org.springframework.ai.chat.messages.UserMessage");
            
            Object userMessage = userMessageClass.getConstructor(String.class).newInstance(promptText);
            java.util.List<Object> messages = java.util.Arrays.asList(userMessage);
            Object prompt = promptClass.getConstructor(java.util.List.class).newInstance(messages);
            
            java.lang.reflect.Method callMethod = chatModel.getClass().getMethod("call", promptClass);
            Object chatResponse = callMethod.invoke(chatModel, prompt);
            
            java.lang.reflect.Method getResultMethod = chatResponse.getClass().getMethod("getResult");
            Object result = getResultMethod.invoke(chatResponse);
            
            java.lang.reflect.Method getOutputMethod = result.getClass().getMethod("getOutput");
            Object output = getOutputMethod.invoke(result);
            
            java.lang.reflect.Method getContentMethod = output.getClass().getMethod("getContent");
            String content = (String) getContentMethod.invoke(output);
            
            return content != null && !content.isEmpty() ? content : 
                   (fallback != null ? fallback.analisarOperacao(dadosOperacao) : "Análise não disponível.");
        } catch (Exception e) {
            logger.error("Erro ao analisar operação: {}", e.getMessage(), e);
            return fallback != null ? fallback.analisarOperacao(dadosOperacao) : 
                   "Erro ao analisar operação. Tente novamente.";
        }
    }
}

