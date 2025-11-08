# 🎥 Guia para Apresentação em Vídeo - TrackZone

## 📋 Informações Gerais

- **Duração Máxima**: 15 minutos
- **Participantes**: Todos os membros devem participar
- **Formato**: Demonstração técnica + narrativa da solução

---

## 👥 Integrantes e Participação

### Integrantes
1. **André Rogério Vieira Pavanela Altobelli Antunes** - RM: 554764
2. **Enrico Figueiredo Del Guerra** - RM: 558604
3. **Leticia Cristina Dos Santos Passos** - RM: 555241

### ⚠️ IMPORTANTE: Todos devem falar no vídeo!

**Sugestão de Distribuição:**
- **Integrante 1**: Introdução + Problema da Mottu + Dashboard (3-4 min)
- **Integrante 2**: Funcionalidades principais + Tecnologias (4-5 min)
- **Integrante 3**: IA + Integração Multidisciplinar + Conclusão (3-4 min)
- **Todos**: Aparecer juntos na abertura e fechamento

---

## 🎬 Roteiro da Apresentação

### 1. **Abertura** (1 minuto)
- [ ] Apresentação da equipe (todos aparecem)
- [ ] Nome do projeto: **TrackZone**
- [ ] Problema da Mottu (contexto)
- [ ] Objetivo da solução

**Fala Sugerida:**
> "Olá! Somos o grupo [Nome do Grupo] e desenvolvemos o TrackZone, uma solução completa de gestão de frota de motos para a Mottu. Nosso sistema resolve o problema de [descrever problema] através de [descrever solução]."

---

### 2. **Demonstração Técnica - Parte 1** (4-5 minutos)

#### 2.1. Login e Autenticação
- [ ] Acessar aplicação (mostrar URL se deploy, ou localhost:8081)
- [ ] Fazer login com usuário ADMIN
- [ ] Mostrar que há 3 perfis (ADMIN, GERENTE, OPERADOR)
- [ ] Explicar Spring Security implementado

**Fala Sugerida:**
> "Vamos começar demonstrando o sistema. Primeiro, fazemos login com um usuário ADMIN. O sistema utiliza Spring Security para autenticação e autorização, com três perfis de acesso diferentes: ADMIN, GERENTE e OPERADOR."

#### 2.2. Dashboard
- [ ] Mostrar dashboard com estatísticas
- [ ] Explicar métricas apresentadas
- [ ] Destacar visualização moderna

**Fala Sugerida:**
> "Aqui temos o dashboard principal, que oferece uma visão geral do sistema com estatísticas em tempo real. A interface foi desenvolvida com Thymeleaf e Bootstrap 5 para garantir uma experiência moderna e responsiva."

#### 2.3. Gestão de Motos
- [ ] Cadastrar uma nova moto
- [ ] Mostrar validações (placa única, chassi único)
- [ ] Listar motos cadastradas
- [ ] Editar uma moto
- [ ] Explicar Spring Data JPA

**Fala Sugerida:**
> "Agora vamos cadastrar uma nova moto. O sistema valida se a placa e chassi são únicos antes de salvar. Utilizamos Spring Data JPA para persistência, com repositórios que abstraem a complexidade do acesso a dados. Veja que após salvar, a moto aparece na lista."

**Pontos a Destacar:**
- ✅ Validações Bean Validation
- ✅ Repository Pattern
- ✅ Transações do banco

---

### 3. **Demonstração Técnica - Parte 2** (4-5 minutos)

#### 3.1. Status e Operações
- [ ] Mostrar status de motos
- [ ] Atualizar status de uma moto
- [ ] Explicar ENUM de status
- [ ] Mostrar operações

**Fala Sugerida:**
> "O sistema permite gerenciar o status de cada moto. Temos vários status disponíveis, como PRONTA, PENDENTE, REPARO_SIMPLES, entre outros. Cada mudança de status é registrada no histórico, utilizando Flyway para controlar as migrações do banco."

#### 3.2. Relatórios
- [ ] Mostrar tela de relatórios
- [ ] Filtrar por período
- [ ] Filtrar por status
- [ ] Explicar queries customizadas

**Fala Sugerida:**
> "Os relatórios permitem análise detalhada dos dados. Podemos filtrar por período, status, ou moto específica. Isso é possível graças a queries customizadas no Spring Data JPA."

#### 3.3. Assistente IA (DESTAQUE!)
- [ ] Acessar `/ai/chat`
- [ ] Fazer uma pergunta ao assistente
- [ ] Mostrar resposta da IA
- [ ] Explicar Spring AI implementado

**Fala Sugerida:**
> "Aqui está uma das funcionalidades mais inovadoras do nosso sistema: o Assistente IA. Utilizamos Spring AI 1.0 para integrar inteligência artificial ao sistema. O assistente pode responder perguntas sobre o sistema, fornecer sugestões e até analisar operações. Se a IA não estiver configurada, o sistema utiliza um fallback inteligente que ainda fornece respostas úteis."

**Pontos a Destacar:**
- ✅ Spring AI 1.0.0 (versão estável)
- ✅ Integração com Ollama (local) ou OpenAI
- ✅ Fallback inteligente
- ✅ Carregamento condicional (@ConditionalOnClass)

---

### 4. **Narrativa da Solução** (3-4 minutos)

#### 4.1. Decisões de Design
- [ ] Explicar por que Spring Boot
- [ ] Justificar escolha de Thymeleaf
- [ ] Explicar Flyway
- [ ] Destacar Spring AI como diferencial

**Fala Sugerida:**
> "Agora vamos explicar algumas decisões de design. Escolhemos Spring Boot pela produtividade e integração nativa com todo o ecossistema Spring. Thymeleaf foi escolhido por sua integração perfeita com Spring Boot e pela capacidade de criar fragmentos reutilizáveis, seguindo o princípio DRY. Flyway garante versionamento do banco de dados e reprodutibilidade. E o Spring AI foi nossa escolha para inovação, oferecendo um diferencial competitivo ao sistema."

#### 4.2. Arquitetura e Padrões
- [ ] Explicar arquitetura MVC
- [ ] Mencionar SOLID
- [ ] Destacar Repository Pattern
- [ ] Falar sobre Exception Handling

**Fala Sugerida:**
> "A arquitetura segue o padrão MVC do Spring, com separação clara de responsabilidades. Aplicamos princípios SOLID, especialmente Single Responsibility, e utilizamos o Repository Pattern para abstração de dados. Temos também um Exception Handler global para tratamento centralizado de erros."

---

### 5. **Integração Multidisciplinar** (2-3 minutos)

#### 5.1. Disciplinas Integradas
- [ ] Design Thinking (UI/UX)
- [ ] Metodologias Ágeis (Sprints)
- [ ] Banco de Dados (SQL, migrações)
- [ ] Engenharia de Software (arquitetura)
- [ ] Segurança (Spring Security)
- [ ] IA (Spring AI)

**Fala Sugerida:**
> "O projeto integra várias disciplinas do semestre. Aplicamos Design Thinking no desenvolvimento da interface, garantindo uma experiência intuitiva. Utilizamos metodologias ágeis com sprints ao longo do semestre. Em Banco de Dados, criamos 5 migrações Flyway com scripts SQL completos, incluindo triggers de auditoria. A arquitetura segue princípios de Engenharia de Software com código limpo e bem estruturado. Implementamos segurança robusta com Spring Security e criptografia de senhas. E por fim, integramos IA através do Spring AI."

**Evidências a Mencionar:**
- ✅ Scripts SQL em `db/migration/`
- ✅ Arquitetura em camadas
- ✅ Interface moderna e responsiva
- ✅ Commits no Git (histórico de desenvolvimento)

---

### 6. **Conclusão** (1 minuto)
- [ ] Resumir funcionalidades principais
- [ ] Destacar inovação (IA)
- [ ] Agradecer

**Fala Sugerida:**
> "Em conclusão, o TrackZone é uma solução completa que integra tecnologias modernas do ecossistema Spring, com destaque para a integração de IA através do Spring AI. O sistema está funcional, bem documentado e pronto para uso. Obrigado pela atenção!"

---

## ✅ Checklist Antes da Gravação

### Preparação Técnica
- [ ] Aplicação rodando e testada
- [ ] Todos os fluxos funcionando
- [ ] Login de teste funcionando
- [ ] Dados de exemplo cadastrados
- [ ] IA configurada (se possível) ou fallback funcionando
- [ ] Navegador com extensões desnecessárias desativadas
- [ ] Tela compartilhada configurada (se necessário)

### Preparação da Apresentação
- [ ] Roteiro revisado por todos
- [ ] Distribuição de falas definida
- [ ] Tempo de cada seção cronometrado
- [ ] Slides preparados (se houver)
- [ ] Evidências prontas (screenshots, diagramas)

### Testes
- [ ] Testar todos os fluxos antes de gravar
- [ ] Verificar se não há erros visuais
- [ ] Confirmar que todos os links funcionam
- [ ] Testar áudio e vídeo

---

## 🎯 Pontos-Chave para Demonstrar

### 1. Demonstração Técnica (40 pontos)
- ✅ **Navegar pelos principais fluxos**: Login → Dashboard → Cadastro → Listagem → Edição → IA
- ✅ **Aplicar conceitos da disciplina**: Mencionar Spring Boot, Security, JPA, Thymeleaf, Flyway, AI
- ✅ **Interface moderna**: Mostrar design responsivo e intuitivo

### 2. Narrativa (20 pontos)
- ✅ **Explicar solução**: Problema da Mottu → Solução TrackZone
- ✅ **Decisões de design**: Por que cada tecnologia
- ✅ **Originalidade**: Destaque para IA integrada

### 3. Integração Multidisciplinar (20 pontos)
- ✅ **Mencionar disciplinas**: Design Thinking, Ágeis, BD, Eng. Software, Segurança, IA
- ✅ **Evidências**: Scripts SQL, commits, arquitetura

### 4. Apresentação Oral (10 pontos)
- ✅ **Todos participam**: Distribuir falas igualmente
- ✅ **Clareza**: Falar pausadamente e com clareza
- ✅ **Domínio**: Demonstrar conhecimento técnico

### 5. Organização (10 pontos)
- ✅ **Estrutura clara**: Seguir roteiro
- ✅ **Transições suaves**: Entre seções
- ✅ **Profissionalismo**: Apresentação polida

---

## ⚠️ Pontos de Atenção (Evitar Penalidades)

### Código e Boas Práticas
- ✅ **Não mencionar código duplicado** (se houver, já foi refatorado)
- ✅ **Destacar uso de fragmentos Thymeleaf** (DRY)
- ✅ **Mencionar SOLID aplicado**

### Colaboração
- ✅ **Todos devem falar** (evitar que uma pessoa domine)
- ✅ **Mencionar trabalho em equipe** (se relevante)

### Usabilidade
- ✅ **Testar fluxos antes** (evitar erros durante apresentação)
- ✅ **Mostrar interface responsiva** (se possível, mudar tamanho de tela)

### Alinhamento com Mottu
- ✅ **Sempre mencionar o problema da Mottu** no início
- ✅ **Conectar funcionalidades com o problema** ao longo da apresentação

---

## 📝 Dicas de Gravação

### Áudio
- [ ] Usar microfone de qualidade (evitar ruído)
- [ ] Falar pausadamente e com clareza
- [ ] Testar áudio antes de gravar

### Vídeo
- [ ] Bom enquadramento (todos visíveis)
- [ ] Iluminação adequada
- [ ] Fundo neutro (se necessário)

### Tela
- [ ] Zoom adequado (100-125%)
- [ ] Resolução adequada (1920x1080 recomendado)
- [ ] Cursor visível mas não distraindo

### Edição
- [ ] Cortar pausas longas
- [ ] Adicionar transições suaves
- [ ] Verificar áudio sincronizado

---

## 🎬 Exemplo de Script Completo (15 minutos)

### Minuto 0-1: Abertura
> "Olá! Somos [Nomes] e desenvolvemos o TrackZone para a Mottu. Vamos demonstrar nosso sistema completo."

### Minuto 1-5: Demonstração Técnica 1
> "Vamos começar fazendo login. O sistema usa Spring Security... Agora o dashboard... Vamos cadastrar uma moto usando Spring Data JPA..."

### Minuto 5-9: Demonstração Técnica 2
> "Agora vamos mostrar o status das motos... E aqui está nosso diferencial: o Assistente IA usando Spring AI..."

### Minuto 9-12: Narrativa e Integração
> "Escolhemos Spring Boot pela produtividade... Aplicamos Design Thinking... Criamos 5 migrações SQL..."

### Minuto 12-15: Conclusão
> "Em resumo, o TrackZone integra tecnologias modernas com IA. Obrigado!"

---

## 📊 Cronograma Sugerido

| Tempo | Seção | Responsável | Pontos-Chave |
|-------|-------|-------------|--------------|
| 0-1 min | Abertura | Todos | Apresentação da equipe |
| 1-5 min | Demo Técnica 1 | Integrante 1 | Login, Dashboard, CRUD |
| 5-9 min | Demo Técnica 2 | Integrante 2 | Status, Relatórios, IA |
| 9-12 min | Narrativa | Integrante 3 | Decisões, Integração |
| 12-15 min | Conclusão | Todos | Resumo e agradecimento |

---

## ✅ Checklist Final Antes de Enviar

- [ ] Vídeo com duração máxima de 15 minutos
- [ ] Todos os integrantes aparecem e falam
- [ ] Todos os fluxos principais demonstrados
- [ ] Conceitos da disciplina mencionados
- [ ] Decisões de design explicadas
- [ ] Integração multidisciplinar abordada
- [ ] Sem erros visuais ou de fluxo
- [ ] Áudio claro e vídeo de qualidade
- [ ] Link de acesso à aplicação mencionado (se houver)
- [ ] README completo e atualizado

---

**Boa sorte na apresentação! 🚀**

**Lembre-se**: O objetivo é demonstrar conhecimento técnico, trabalho em equipe e uma solução completa e funcional!




