# 🤖 Spring AI - Assistente Inteligente

## ✅ O que foi implementado:

### **Spring AI** - Assistente Inteligente
- ✅ Chat interativo com IA
- ✅ Sugestões inteligentes para o sistema
- ✅ Análise de operações
- ✅ Integração com Ollama (local) ou OpenAI
- ✅ Fallback inteligente quando IA não está disponível

---

## 🚀 Como usar:

### Configuração do Spring AI

#### Opção 1: Ollama (Local, Gratuito - Recomendado)

1. **Instalar Ollama**: https://ollama.ai/
2. **Baixar modelo**:
   ```bash
   ollama pull llama2
   ```
3. **Iniciar Ollama** (geralmente roda automaticamente após instalação)
4. **Editar `application.properties`** e descomentar:
   ```properties
   spring.ai.ollama.base-url=http://localhost:11434
   spring.ai.ollama.chat.options.model=llama2
   spring.ai.ollama.chat.options.temperature=0.7
   ```

#### Opção 2: OpenAI (Pago, precisa API Key)

1. **Obter API Key**: https://platform.openai.com/api-keys
2. **Editar `application.properties`** e descomentar:
   ```properties
   spring.ai.openai.api-key=sua-api-key-aqui
   spring.ai.openai.chat.options.model=gpt-3.5-turbo
   spring.ai.openai.chat.options.temperature=0.7
   ```
3. **Comentar as linhas do Ollama**

**Nota**: O Spring AI está configurado como dependência opcional no `pom.xml`. A aplicação funciona normalmente mesmo sem Spring AI configurado (usa fallback inteligente).

---

## 🎯 Funcionalidades:

### Chat com IA
- Acesse: `/ai/chat`
- Faça perguntas sobre o sistema
- Receba sugestões inteligentes
- Use perguntas rápidas pré-definidas

### Análise de Operações
- Análise automática de operações de motos
- Sugestões inteligentes baseadas no contexto
- Respostas em português brasileiro

---

## 📁 Arquivos Criados/Modificados:

### Arquivos Principais:
- `src/main/java/.../service/AIService.java` - Serviço de IA
- `src/main/java/.../control/AIController.java` - Controller do Chat IA
- `src/main/resources/templates/ai/chat.html` - Interface do Chat

### Arquivos Modificados:
- `pom.xml` - Dependências Spring AI (opcional)
- `application.properties` - Configurações Spring AI (comentadas)

---

## 🎨 Design Moderno:

### Interface do Chat:
- Design moderno com gradientes
- Mensagens estilizadas (usuário vs bot)
- Animações suaves
- Perguntas rápidas pré-definidas
- Loading spinner elegante

---

## ⚠️ Notas Importantes:

1. **Spring AI Opcional**: Se não tiver Ollama ou OpenAI configurado, o sistema usa um fallback inteligente que ainda fornece respostas úteis.

2. **Fallback Inteligente**: O `AIService` detecta automaticamente se o Spring AI está disponível e usa fallback quando necessário.

3. **Modo Desenvolvimento**: Para desenvolvimento local sem Spring AI, a aplicação funciona normalmente. O chat simplesmente não terá acesso à IA.

---

## 🔧 Troubleshooting:

### Erro ao conectar com Ollama
- Verifique se Ollama está rodando: `ollama list`
- Confirme a URL: `http://localhost:11434`
- Verifique se o modelo foi baixado: `ollama pull llama2`

### Chat não funciona
- Verifique os logs para erros
- Teste o fallback local primeiro
- Confirme que a rota `/ai/chat` está acessível
- Verifique se o Spring AI está no classpath (é opcional)

---

**Desenvolvido com ❤️ integrando Spring AI de forma elegante!**




