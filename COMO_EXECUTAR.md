# 🚀 Como Executar o Projeto Corretamente

## ✅ Correções Aplicadas

1. **Spring AI Desabilitado por Padrão**: O Spring AI estava tentando inicializar sem configuração, causando erro. Agora está desabilitado por padrão no `application.properties`.

2. **Scripts de Execução Atualizados**: Os scripts `executar.bat` e `executar.ps1` foram atualizados para:
   - Verificar se Maven está instalado
   - Compilar o projeto corretamente
   - Usar `mvn spring-boot:run` (mais confiável que executar JAR diretamente)
   - Exibir mensagens mais claras

## 📋 Requisitos

- **Java 17+** (verificado: Java 21 instalado ✅)
- **Maven 3.6+** (verificado: Maven 3.9.9 instalado ✅)

## 🎯 Como Executar

### Opção 1: Usando PowerShell (Recomendado)

```powershell
.\executar.ps1
```

### Opção 2: Usando Batch (Windows)

```cmd
executar.bat
```

### Opção 3: Manualmente

```powershell
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

## 🌐 Acessar a Aplicação

Após iniciar, aguarde alguns segundos (15-30 segundos) e acesse:

- **Aplicação Principal**: http://localhost:8081
- **H2 Console** (banco de dados): http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:trackzone`
  - Usuário: `sa`
  - Senha: (vazio)
- **Chatbot IA**: http://localhost:8081/ai/chat

## 🔐 Credenciais de Acesso

| Perfil | Email | Senha |
|--------|-------|-------|
| **ADMIN** | admin@teste.com | Admin123! |
| **GERENTE** | gerente@teste.com | Gerente123! |
| **OPERADOR** | operador@teste.com | Operador123! |

## ⚙️ Configurações

### Banco de Dados

O projeto está configurado para usar **H2 Database** (em memória) por padrão. Não é necessário configuração adicional.

### Spring AI (Opcional)

O Spring AI está **desabilitado por padrão**. Para habilitar:

1. **Ollama (Local, Gratuito)**:
   - Instale Ollama: https://ollama.ai/
   - Execute: `ollama pull llama2`
   - No `application.properties`, descomente:
     ```properties
     spring.ai.ollama.base-url=http://localhost:11434
     spring.ai.ollama.chat.options.model=llama2
     spring.ai.ollama.chat.options.temperature=0.7
     ```
   - E comente as linhas de desabilitação:
     ```properties
     # spring.ai.openai.chat.enabled=false
     # spring.ai.ollama.chat.enabled=false
     ```

2. **OpenAI (Pago)**:
   - Obtenha API Key: https://platform.openai.com/api-keys
   - No `application.properties`, descomente e configure:
     ```properties
     spring.ai.openai.api-key=sua-api-key-aqui
     spring.ai.openai.chat.options.model=gpt-3.5-turbo
     spring.ai.openai.chat.options.temperature=0.7
     ```

## 🐛 Solução de Problemas

### Porta 8081 em Uso

```powershell
# Verificar o que está usando a porta
netstat -ano | findstr :8081

# Matar o processo (substitua <PID> pelo ID do processo)
taskkill /PID <PID> /F
```

### Erro de Compilação

```powershell
# Limpar e recompilar
mvn clean compile
```

### Aplicação não Inicia

1. Verifique os logs no console
2. Verifique se Java e Maven estão no PATH
3. Verifique se a porta 8081 está livre
4. Tente executar manualmente: `mvn spring-boot:run`

## 📝 Notas Importantes

- A aplicação usa **H2 Database em memória**, então os dados são perdidos ao reiniciar
- O Spring AI é **opcional** - a aplicação funciona normalmente sem ele (usa fallback)
- A primeira inicialização pode demorar mais (download de dependências)
- Aguarde 15-30 segundos após iniciar para a aplicação estar totalmente pronta

## ✅ Status da Correção

- ✅ Spring AI desabilitado por padrão
- ✅ Scripts de execução atualizados
- ✅ Configurações corrigidas
- ✅ Compilação funcionando
- ✅ Projeto pronto para execução

---

**Última atualização**: 2025-11-05
**Status**: ✅ Pronto para execução

