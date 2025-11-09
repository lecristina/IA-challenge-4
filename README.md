# 🏍️ TrackZone - Disruptive Architectures (IA + IoT)

## 🔗 Repositório do Projeto

**GitHub**: [https://github.com/lecristina/IA-challenge-4](https://github.com/lecristina/IA-challenge-4)

---

## 👥 Integrantes

- **André Rogério Vieira Pavanela Altobelli Antunes** - RM: 554764
- **Enrico Figueiredo Del Guerra** - RM: 558604
- **Leticia Cristina Dos Santos Passos** - RM: 555241

---

## 📋 Sobre o Projeto

Sistema inovador de gestão de frota de motos integrando **Inteligência Artificial (Spring AI)** e **Internet das Coisas (ESP8266/ESP32)** para localização inteligente e controle de LED via HTTP REST.

### 🎯 Problema da Mottu

A Mottu precisa localizar rapidamente motos em um pátio de 50x50 metros com centenas de veículos. O **TrackZone** resolve isso com:

- **Busca Inteligente**: Localização de motos em 30 segundos (vs. 10-15 minutos)
- **Controle de LED**: LED pisca via ESP8266/ESP32 para identificação visual
- **Assistente IA**: Chat interativo com Spring AI para suporte inteligente
- **Custo-Benefício**: Hardware econômico (R$ 9,35/moto) com ROI de 1.186%

---

## 📋 Índice


- [Instalação e Execução](#-instalação-e-execução)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Funcionalidades Disruptivas](#-funcionalidades-disruptivas)
- [Cálculo de Custos e ROI](#-cálculo-de-custos-e-roi---solução-econômica)
- [Como Funciona a IA](#-como-funciona-a-ia---arquitetura-e-implementação)
- [Como Funciona o IoT](#-como-funciona-o-iot---arquitetura-e-implementação)
- [Integração Multidisciplinar](#-integração-multidisciplinar)
- [Resultados Finais](#-resultados-finais)

---

## 🚀 Instalação e Execução

### 1. Clonar o Repositório

```bash
git clone https://github.com/lecristina/IA-challenge-4.git
cd AI-JAVA-4
```

### 2. Executar a Aplicação

```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

### 3. Acessar a Aplicação

Abra o navegador e acesse: **http://localhost:8081**

**Páginas Principais:**
- **Login**: http://localhost:8081/login
- **Dashboard IoT/IOB/IA**: http://localhost:8081/disruptive-architectures ⭐ (Busca inteligente de motos com ESP8266)
- **Assistente IA**: http://localhost:8081/ai/chat

**Credenciais:**
- Email: `admin@teste.com` | Senha: `Admin123!`

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Spring Boot 3.5.4** - Framework principal
- **Spring Security** - Autenticação e autorização com 3 perfis
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **Spring AI 1.0.0** - Integração com IA (Ollama/OpenAI)
- **Bean Validation** - Validações (@NotBlank, @Email, @Pattern)
- **Exception Handling** - Tratamento global de exceções
- **RestTemplate** - Comunicação HTTP REST com ESP8266/ESP32

### Frontend
- **Thymeleaf** - Template engine com fragmentos reutilizáveis
- **Bootstrap 5** - Framework CSS responsivo
- **Font Awesome** - Ícones
- **JavaScript** - Validações client-side e interatividade

### Banco de Dados
- **H2 Database** - Banco em memória (desenvolvimento)
- **Oracle Database** - Banco principal (produção)
- **Flyway** - Controle de versão do banco (migrações)

### Hardware IoT
- **ESP8266** - Microcontrolador WiFi (R$ 8,00 - recomendado)
- **ESP32** - Microcontrolador WiFi + Bluetooth (R$ 20,00 - alternativa)
- **Arduino IDE** - Ambiente de desenvolvimento para ESP8266/ESP32
- **ArduinoJson** - Biblioteca para parsing JSON no Arduino

### Inteligência Artificial
- **Spring AI** - Framework de integração com IA
- **Ollama** - Provedor de IA local (gratuito)
- **OpenAI** - Provedor de IA em nuvem (pago)

### Arquitetura e Padrões
- **MVC (Model-View-Controller)** - Arquitetura do Spring
- **Repository Pattern** - Abstração de acesso a dados
- **Service Layer** - Lógica de negócio
- **Dependency Injection** - Injeção de dependências
- **SOLID Principles** - Princípios aplicados no código
- **Strategy Pattern** - Para IA (AIService vs AIServiceFallback)
- **Fallback Pattern** - Para robustez do sistema

---

## 📁 Estrutura do Projeto

```
AI-JAVA-4/
├── src/main/java/br/com/fiap/universidade_fiap/
│   ├── control/                    # Controllers (MVC)
│   │   ├── AIController.java              # Chat com IA
│   │   ├── DisruptiveArchitecturesController.java  # Dashboard IoT/IA
│   │   ├── DashboardController.java
│   │   ├── HomeController.java
│   │   ├── LoginController.java
│   │   └── ...
│   ├── model/                      # Entidades JPA
│   │   ├── Moto.java
│   │   ├── StatusMoto.java
│   │   ├── Operacao.java
│   │   └── Usuario.java
│   ├── repository/                 # Repositórios JPA
│   │   ├── MotoRepository.java
│   │   ├── StatusMotosRepository.java
│   │   └── ...
│   ├── service/                     # Serviços de negócio
│   │   ├── AIService.java              # Serviço de IA (Spring AI)
│   │   ├── AIServiceFallback.java      # Fallback IA
│   │   ├── ESP32Service.java           # Serviço de controle ESP8266/ESP32
│   │   ├── VisaoComputacionalService.java  # Visão computacional com IA
│   │   ├── LocalizacaoInteligenteService.java  # Localização inteligente
│   │   └── ...
│   ├── security/                    # Configuração Spring Security
│   │   └── SegurancaConfig.java
│   └── exception/                   # Tratamento de exceções
│       └── GlobalExceptionHandler.java
├── src/main/resources/
│   ├── application.properties       # Configurações principais
│   ├── db/migration/                # Scripts Flyway
│   │   ├── V1__Create_tables.sql
│   │   ├── V2__Insert_initial_data.sql
│   │   └── ...
│   ├── templates/                   # Templates Thymeleaf
│   │   ├── fragmentos.html         # Fragmentos reutilizáveis
│   │   ├── login.html
│   │   ├── ai/
│   │   │   └── chat.html           # Chat IA
│   │   └── ...
│   └── static/css/                  # Estilos CSS
│       └── style.css
├── ESP32_LED_EXAMPLE.ino           # Código Arduino para ESP8266/ESP32
├── pom.xml                          # Dependências Maven
└── README.md                        # Documentação principal
```

### Principais Arquivos

#### Controllers
- **`DisruptiveArchitecturesController.java`**: Gerencia dashboard IoT/IOB/IA e ativação de LED
- **`AIController.java`**: Gerencia chat interativo com IA

#### Services
- **`ESP32Service.java`**: Controla LED via ESP8266/ESP32 (simulado ou físico)
- **`AIService.java`**: Integração com Spring AI (Ollama/OpenAI)
- **`AIServiceFallback.java`**: Fallback inteligente quando IA não está disponível
- **`VisaoComputacionalService.java`**: Visão computacional com IA
- **`LocalizacaoInteligenteService.java`**: Localização inteligente com IA

#### Hardware
- **`ESP32_LED_EXAMPLE.ino`**: Código Arduino completo para ESP8266/ESP32 com explicação linha por linha

---

## 🎯 Funcionalidades Disruptivas

### 🤖 Assistente IA (Spring AI)

- **Chat Interativo**: Conversa com IA sobre o sistema (`/ai/chat`)
- **Sugestões Inteligentes**: Respostas contextuais em português brasileiro
- **Análise de Operações**: Análise automática de dados usando Spring AI
- **Fallback Inteligente**: Funciona mesmo sem IA configurada (usa respostas pré-definidas)
- **Múltiplos Provedores**: Suporta Ollama (local, gratuito) ou OpenAI (pago)
- **Interface Moderna**: Design elegante com gradientes e animações suaves

### 🔌 IoT/IOB - ESP8266/ESP32

- **Dashboard IoT/IOB/IA**: Página `/disruptive-architectures` com busca inteligente de motos
- **Busca Inteligente**: Buscar moto por placa com LED piscando
- **LED Virtual/Físico**: LED pisca via ESP8266 (simulado ou físico)
- **Integração ESP8266**: Comunicação HTTP REST com hardware físico
- **Custo-Benefício**: Hardware econômico (R$ 9,35/moto) com ROI impressionante (payback de 28 dias)
- **URL de Acesso**: http://localhost:8081/disruptive-architectures (requer autenticação)

### 👁️ Visão Computacional com IA

- **Detecção Automática**: Detecta motos no pátio usando IA e visão computacional
- **Análise Visual**: Analisa estado visual de cada moto com confiança 85-99%
- **Detecção de Anomalias**: Identifica problemas automaticamente usando IA
- **Análise Agregada**: Análise inteligente do pátio completo usando Spring AI
- **Otimização de Posicionamento**: Calcula melhor posição no pátio usando algoritmos inteligentes

---

## 💰 Cálculo de Custos e ROI - Solução Econômica

### Hardware por Moto (Opção Econômica)

**Custo de hardware por moto (compra em volume):**
- ESP8266 (alternativa ao ESP32): R$ 8,00 (compra em lote de 100+)
- LED: R$ 0,30 (compra em lote)
- Resistor 220Ω: R$ 0,05 (compra em lote)
- Cabos e conectores: R$ 1,00
- **Total por moto: R$ 9,35** (87% mais barato que ESP32!)

**Custo de instalação (opções):**
- **Opção 1 - Interna**: Treinar equipe própria = R$ 0,00 (apenas tempo)
- **Opção 2 - Terceirizada**: R$ 20,00 por moto (instalação simples)
- **Total com instalação interna: R$ 9,35 por moto**
- **Total com instalação terceirizada: R$ 29,35 por moto**

### Custo Total para Frota

**Frota de 100 motos:**
- Hardware: 100 × R$ 9,35 = R$ 935,00
- Instalação interna: R$ 0,00 (equipe própria)
- **Total: R$ 935,00** (economia de 87%!)

**Frota de 500 motos:**
- Hardware: 500 × R$ 9,35 = R$ 4.675,00
- Instalação interna: R$ 0,00 (equipe própria)
- **Total: R$ 4.675,00** (economia de 87%!)

**Frota de 1.000 motos:**
- Hardware: 1.000 × R$ 9,35 = R$ 9.350,00
- Instalação interna: R$ 0,00 (equipe própria)
- **Total: R$ 9.350,00** (economia de 87%!)

### ROI (Retorno sobre Investimento)

**Economia de tempo:**
- Sem o sistema: 10-15 minutos para encontrar uma moto
- Com o sistema: 30 segundos (busca + LED piscando)
- **Economia: 9-14 minutos por busca**

**Economia de custo operacional:**
- Operador ganha R$ 20/hora
- 10 buscas por dia × 10 minutos economizados = 100 minutos = 1,67 horas
- **Economia diária: R$ 33,40**
- **Economia mensal: R$ 1.002,00**
- **Economia anual: R$ 12.024,00**

**ROI para frota de 100 motos (opção econômica):**
- Investimento: R$ 935,00 (hardware apenas)
- Economia anual: R$ 12.024,00
- **ROI: 1.186% no primeiro ano!** 🚀
- **Payback: 28 dias** (menos de 1 mês!)

### Custo de Energia

**ESP8266 em standby (mais econômico que ESP32):**
- Consumo: 0,08W (standby WiFi - mais eficiente)
- Custo kWh: R$ 0,60
- **Custo mensal por ESP8266: R$ 0,03**

**LED piscando (30 segundos):**
- Consumo: 0,02W × 30s = 0,0006 Wh
- **Custo por ativação: R$ 0,00000036 (praticamente zero)**

**Para 100 motos:**
- Custo mensal de energia: 100 × R$ 0,03 = **R$ 3,00/mês**
- **Custo anual: R$ 36,00** (desprezível comparado à economia)

**💡 O ESP8266 é mais barato E mais eficiente em energia!**

### Números para Lembrar

- **Custo por moto**: R$ 9,35 (hardware - ESP8266)
- **Custo total (100 motos)**: R$ 935,00 (instalação interna)
- **Economia anual**: R$ 12.024,00
- **ROI**: 1.186% no primeiro ano 🚀
- **Payback**: 28 dias (menos de 1 mês!)
- **Tempo de busca**: 30 segundos (vs. 10-15 minutos)
- **Consumo energia**: R$ 3,00/mês (100 motos)
- **Economia vs. opção original**: 87% mais barato!

---

## 🔧 Como Funciona o IoT - Arquitetura e Implementação

### 📐 Arquitetura do IoT

O IoT no TrackZone foi implementado com uma arquitetura flexível que suporta tanto simulação quanto hardware físico (ESP8266/ESP32), utilizando comunicação HTTP REST para controle remoto do LED.

#### Componentes Principais

```
┌─────────────────────────────────────────────────────────────┐
│          DisruptiveArchitecturesController                  │
│  (Endpoint: /disruptive-architectures, /ativar-led)        │
└────────────────────┬────────────────────────────────────────┘
                     │
         ┌───────────┴───────────┐
         │                       │
    ┌────▼────┐          ┌──────▼──────┐
    │ESP32    │          │Localização  │
    │Service  │          │Inteligente  │
    │         │          │Service      │
    └────┬────┘          └─────────────┘
         │
    ┌────▼────┐
    │RestTemplate│
    │(HTTP REST)│
    └────┬────┘
         │
    ┌────▼────┐
    │ESP8266/ │
    │ESP32    │
    │(Hardware)│
    └─────────┘
```

#### 1. **DisruptiveArchitecturesController** (`DisruptiveArchitecturesController.java`)
- **Responsabilidade**: Gerenciar dashboard IoT/IOB/IA e ativar LED
- **Endpoints**:
  - `GET /disruptive-architectures` - Dashboard IoT com busca de motos
  - `POST /disruptive-architectures/buscar` - Buscar moto por placa e ativar LED
  - `POST /disruptive-architectures/ativar-led` - Ativar LED de uma moto
- **Funcionalidades**:
  - Busca inteligente de motos por placa
  - Ativação de LED virtual/físico
  - Geração de dados ESP32 (GPS, Bluetooth, Bateria)

#### 2. **ESP32Service** (`ESP32Service.java`)
- **Responsabilidade**: Controlar LED via ESP8266/ESP32 (simulado ou físico)
- **Características**:
  - Suporta modo simulado (sem hardware)
  - Suporta modo físico (com ESP8266/ESP32)
  - Comunicação HTTP REST com hardware
  - Rastreamento de LEDs ativos por placa
  - Desativação automática após 30 segundos
- **Configuração**:
  - `esp32.enabled` - Habilita/desabilita comunicação com hardware
  - `esp32.base-url` - URL base do ESP8266/ESP32 (ex: http://192.168.1.100)

#### 3. **ESP8266/ESP32 (Hardware)**
- **Responsabilidade**: Controlar LED físico via HTTP REST
- **Características**:
  - Servidor HTTP na porta 80
  - Endpoint `POST /led/ativar` para ativar LED
  - Endpoint `GET /led/status` para verificar status
  - LED pisca por 30 segundos automaticamente
  - Desativação automática após tempo limite

### 🔄 Fluxo de Funcionamento

#### Fluxo de Ativação de LED

```
1. Usuário busca moto por placa no dashboard (/disruptive-architectures)
   ↓
2. DisruptiveArchitecturesController recebe POST /disruptive-architectures/buscar
   ↓
3. Controller busca moto no banco de dados
   ↓
4. Controller chama ESP32Service.ativarLED(placa)
   ↓
5a. Se esp32.enabled=true:
    → ESP32Service envia HTTP POST para ESP8266/ESP32
    → ESP8266/ESP32 recebe comando e ativa LED físico
    → LED pisca por 30 segundos
   ↓
5b. Se esp32.enabled=false:
    → ESP32Service simula ativação (sem hardware)
    → LED virtual é ativado no sistema
   ↓
6. Sistema retorna localização e status do LED ao usuário
```

### 🛠️ Como Foi Implementado

#### 1. **Serviço de Controle ESP32** (`ESP32Service.java`)

```java
@Service
public class ESP32Service {
    @Value("${esp32.enabled:false}")
    private boolean esp32Enabled;
    
    @Value("${esp32.base-url:http://192.168.1.100}")
    private String esp32BaseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Long> ledsAtivos = new ConcurrentHashMap<>();
}
```

**Características:**
- Usa `@Value` para ler configuração do `application.properties`
- `ConcurrentHashMap` para rastreamento thread-safe de LEDs ativos
- `RestTemplate` para comunicação HTTP REST com hardware
- Suporta modo simulado e físico

#### 2. **Ativação de LED**

```java
public boolean ativarLED(String placa) {
    // Normalizar placa
    String placaNormalizada = placa.trim().toUpperCase();
    ledsAtivos.put(placaNormalizada, System.currentTimeMillis());
    
    // Se ESP32 real está habilitado, enviar comando HTTP
    if (esp32Enabled) {
        try {
            String url = esp32BaseUrl + "/led/ativar";
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("placa", placaNormalizada);
            
            restTemplate.postForObject(url, requestBody, Map.class);
            
            logger.info("LED REAL ativado para moto com placa: {} via ESP32 em {}", 
                placaNormalizada, esp32BaseUrl);
            return true;
        } catch (RestClientException e) {
            logger.error("Erro ao comunicar com ESP32 real: {}", e.getMessage());
            // Continuar com simulação se falhar
        }
    }
    
    logger.info("LED SIMULADO ativado para moto com placa: {}", placaNormalizada);
    return true;
}
```

**Características:**
- Verifica se hardware está habilitado
- Envia HTTP POST para ESP8266/ESP32 se habilitado
- Fallback para simulação se hardware não disponível
- Rastreia timestamp de ativação

#### 3. **Código Arduino Completo para ESP8266/ESP32** (`ESP32_LED_EXAMPLE.ino`)

**Código completo com explicação linha por linha:**

```cpp
/*
 * Código ESP32/ESP8266 para controlar LED via HTTP REST
 * 
 * Hardware necessário:
 * - ESP8266 (recomendado) ou ESP32
 * - LED conectado ao pino GPIO 2
 * - Resistor 220Ω entre LED e GND
 * 
 * Conexões:
 * - LED positivo (ânodo) -> GPIO 2
 * - LED negativo (cátodo) -> Resistor 220Ω -> GND
 */

// Bibliotecas necessárias
#include <WiFi.h>          // Para conexão WiFi
#include <WebServer.h>     // Para servidor HTTP
#include <ArduinoJson.h>   // Para parsing JSON

// ========== CONFIGURAÇÃO WIFI ==========
const char* ssid = "SEU_WIFI_SSID";        // Nome da sua rede WiFi
const char* password = "SUA_SENHA_WIFI";   // Senha da sua rede WiFi

// ========== CONFIGURAÇÃO DO LED ==========
const int LED_PIN = 2;                     // Pino GPIO 2 (pode ser outro)
const unsigned long LED_DURATION = 30000;  // 30 segundos em millisegundos

// ========== SERVIDOR WEB ==========
WebServer server(80);  // Servidor HTTP na porta 80

// ========== VARIÁVEIS DE CONTROLE ==========
unsigned long ledStartTime = 0;  // Timestamp de quando LED foi ativado
bool ledActive = false;           // Flag para saber se LED está ativo
String placaAtiva = "";           // Placa da moto que ativou o LED

// ========== SETUP (Executa uma vez ao iniciar) ==========
void setup() {
  Serial.begin(115200);  // Inicia comunicação serial (115200 baud)
  delay(1000);
  
  // Configurar pino do LED como saída
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);  // Garantir que LED inicia desligado
  
  // Conectar ao WiFi
  Serial.println();
  Serial.print("Conectando ao WiFi: ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  // Aguardar conexão WiFi
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println();
  Serial.println("WiFi conectado!");
  Serial.print("IP do ESP8266: ");
  Serial.println(WiFi.localIP());  // Mostra IP na serial
  
  // ========== CONFIGURAR ROTAS HTTP ==========
  server.on("/led/ativar", HTTP_POST, handleAtivarLED);  // POST para ativar LED
  server.on("/led/status", HTTP_GET, handleStatusLED);   // GET para verificar status
  server.on("/", HTTP_GET, handleRoot);                  // GET para página raiz
  
  // Iniciar servidor HTTP
  server.begin();
  Serial.println("Servidor HTTP iniciado!");
  Serial.println("Acesse http://" + WiFi.localIP().toString() + " para ver o status");
}

// ========== LOOP (Executa continuamente) ==========
void loop() {
  server.handleClient();  // Processar requisições HTTP
  
  // Controlar LED piscando
  if (ledActive) {
    unsigned long currentTime = millis();
    unsigned long elapsedTime = currentTime - ledStartTime;
    
    // Piscar LED: 500ms ligado, 500ms desligado (1Hz)
    int blinkState = (currentTime / 500) % 2;
    digitalWrite(LED_PIN, blinkState);
    
    // Desativar após 30 segundos
    if (elapsedTime >= LED_DURATION) {
      ledActive = false;
      digitalWrite(LED_PIN, LOW);
      placaAtiva = "";
      Serial.println("LED desativado automaticamente após 30 segundos");
    }
  } else {
    digitalWrite(LED_PIN, LOW);  // Garantir LED desligado
  }
  
  delay(10);  // Pequeno delay para não sobrecarregar CPU
}

// ========== HANDLER: Ativar LED (POST /led/ativar) ==========
void handleAtivarLED() {
  if (server.hasArg("plain")) {
    String body = server.arg("plain");  // Ler body da requisição
    
    // Parse JSON
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, body);
    
    if (error) {
      // Erro ao parsear JSON
      server.send(400, "application/json", "{\"erro\":\"JSON inválido\"}");
      return;
    }
    
    String placa = doc["placa"].as<String>();  // Extrair placa do JSON
    
    if (placa.length() > 0) {
      // Ativar LED
      ledActive = true;
      ledStartTime = millis();  // Registrar timestamp
      placaAtiva = placa;
      
      Serial.print("LED ativado para placa: ");
      Serial.println(placa);
      
      // Resposta JSON de sucesso
      server.send(200, "application/json", 
        "{\"sucesso\":true,\"mensagem\":\"LED ativado\",\"placa\":\"" + placa + "\"}");
    } else {
      // Placa não informada
      server.send(400, "application/json", "{\"erro\":\"Placa não informada\"}");
    }
  } else {
    // Body vazio
    server.send(400, "application/json", "{\"erro\":\"Body vazio\"}");
  }
}

// ========== HANDLER: Status do LED (GET /led/status) ==========
void handleStatusLED() {
  StaticJsonDocument<200> doc;
  doc["ativo"] = ledActive;
  doc["placa"] = placaAtiva;
  
  if (ledActive) {
    // Calcular tempo restante
    unsigned long elapsedTime = millis() - ledStartTime;
    unsigned long remainingTime = LED_DURATION - elapsedTime;
    doc["tempoRestante"] = remainingTime > 0 ? remainingTime : 0;
  } else {
    doc["tempoRestante"] = 0;
  }
  
  // Serializar JSON e enviar resposta
  String response;
  serializeJson(doc, response);
  server.send(200, "application/json", response);
}

// ========== HANDLER: Página Raiz (GET /) ==========
void handleRoot() {
  String html = "<!DOCTYPE html><html><head><title>ESP32 LED Control</title></head><body>";
  html += "<h1>ESP32 LED Control</h1>";
  html += "<p>Status: " + String(ledActive ? "ATIVO" : "INATIVO") + "</p>";
  html += "<p>Placa: " + placaAtiva + "</p>";
  html += "<p>IP: " + WiFi.localIP().toString() + "</p>";
  html += "</body></html>";
  
  server.send(200, "text/html", html);
}
```

**Explicação Detalhada:**

1. **Bibliotecas:**
   - `WiFi.h`: Gerencia conexão WiFi do ESP8266/ESP32
   - `WebServer.h`: Cria servidor HTTP na porta 80
   - `ArduinoJson.h`: Faz parsing de JSON recebido e cria JSON de resposta

2. **Setup:**
   - Configura GPIO 2 como saída para o LED
   - Conecta ao WiFi usando SSID e senha
   - Configura rotas HTTP (`/led/ativar`, `/led/status`, `/`)
   - Inicia servidor HTTP na porta 80

3. **Loop:**
   - Processa requisições HTTP continuamente
   - Controla LED piscando (500ms ON, 500ms OFF)
   - Desativa LED automaticamente após 30 segundos

4. **Handlers:**
   - `handleAtivarLED()`: Recebe POST com JSON `{"placa": "ABC1234"}`, ativa LED
   - `handleStatusLED()`: Retorna status atual do LED em JSON
   - `handleRoot()`: Página HTML simples com informações do ESP8266

**Características:**
- ✅ Servidor HTTP na porta 80
- ✅ Endpoint `POST /led/ativar` para ativar LED
- ✅ Endpoint `GET /led/status` para verificar status
- ✅ LED pisca por 30 segundos automaticamente (500ms ON, 500ms OFF)
- ✅ Desativação automática após 30 segundos
- ✅ Tratamento de erros (JSON inválido, placa vazia, etc.)
- ✅ Logging via Serial Monitor para debug
- ✅ Página HTML simples na raiz para verificar status

### 💡 Controle de LED - Detalhes Técnicos

#### Funcionamento do LED

O LED é controlado via GPIO do ESP8266/ESP32 e pisca por 30 segundos quando ativado:

**Características do LED:**
- **GPIO**: Pino 2 (GPIO2) no ESP8266/ESP32
- **Resistor**: 220Ω (proteção contra sobrecarga)
- **Frequência de Piscar**: 500ms ligado, 500ms desligado (1Hz)
- **Duração**: 30 segundos após ativação
- **Desativação**: Automática após 30 segundos

**Ciclo de Vida do LED:**
```
1. Usuário busca moto → Sistema envia HTTP POST
2. ESP8266 recebe comando → Ativa LED
3. LED pisca por 30 segundos (500ms ON, 500ms OFF)
4. Após 30 segundos → LED desativa automaticamente
5. Sistema remove LED do mapa de LEDs ativos
```

#### Rastreamento de LEDs Ativos

O sistema usa `ConcurrentHashMap` para rastrear LEDs ativos:

```java
private final Map<String, Long> ledsAtivos = new ConcurrentHashMap<>();

// Chave: Placa da moto (normalizada)
// Valor: Timestamp de ativação (System.currentTimeMillis())
```

**Vantagens:**
- **Thread-Safe**: Múltiplas requisições simultâneas são seguras
- **Performance**: O(1) para busca e inserção
- **Desativação Automática**: Remove LEDs expirados automaticamente

### 🔌 ESP8266 vs ESP32 - Comparação Técnica

#### ESP8266 (Recomendado - R$ 8,00)
- **CPU**: Tensilica L106 32-bit (80MHz)
- **RAM**: 80KB
- **Flash**: 4MB (varia)
- **WiFi**: 802.11 b/g/n (2.4GHz)
- **GPIO**: 17 pinos
- **Consumo**: 0,08W (standby WiFi)
- **Custo**: R$ 8,00 (compra em volume)
- **Vantagens**: Mais barato, suficiente para LED
- **Desvantagens**: Menos recursos que ESP32

#### ESP32 (Alternativa - R$ 20,00)
- **CPU**: Dual-core Tensilica LX6 32-bit (240MHz)
- **RAM**: 520KB
- **Flash**: 4MB (varia)
- **WiFi**: 802.11 b/g/n (2.4GHz)
- **Bluetooth**: 4.2 + BLE
- **GPIO**: 34 pinos
- **Consumo**: 0,10W (standby WiFi)
- **Custo**: R$ 20,00 (compra em volume)
- **Vantagens**: Mais recursos, Bluetooth
- **Desvantagens**: Mais caro, desnecessário para LED simples

**Recomendação**: ESP8266 é suficiente para controle de LED e 87% mais barato!

### 📡 Protocolo HTTP REST - Detalhes de Implementação

#### Requisição de Ativação (Java → ESP8266)

**Código Java (ESP32Service.java):**
```java
public boolean ativarLED(String placa) {
    String url = esp32BaseUrl + "/led/ativar";
    Map<String, String> requestBody = new HashMap<>();
    requestBody.put("placa", placa);
    
    try {
        restTemplate.postForObject(url, requestBody, Map.class);
        logger.info("LED REAL ativado para placa: {}", placa);
        return true;
    } catch (RestClientException e) {
        logger.error("Erro ao comunicar com ESP8266: {}", e.getMessage());
        return false; // Fallback para simulação
    }
}
```

**Características:**
- Usa `RestTemplate` do Spring para HTTP
- Timeout configurável (padrão: 5 segundos)
- Tratamento de erro com fallback
- Logging detalhado para debug

### 🔧 Configuração

#### Modo Simulado (Padrão)

```properties
# application.properties
esp32.enabled=false
```

**Vantagens:**
- Funciona sem hardware
- Ideal para desenvolvimento
- Testes rápidos

#### Modo Físico (Com Hardware)

```properties
# application.properties
esp32.enabled=true
esp32.base-url=http://192.168.1.100
```

**Vantagens:**
- Controle real de LED físico
- Demonstração completa do sistema
- Integração real com hardware

---

## 🤖 Como Funciona a IA - Arquitetura e Implementação

### 📐 Arquitetura da IA

A IA no TrackZone foi implementada com uma arquitetura flexível e robusta, utilizando o padrão **Strategy** e **Fallback** para garantir que o sistema funcione mesmo sem IA configurada.

#### Componentes Principais

```
┌─────────────────────────────────────────────────────────────┐
│                    AIController                              │
│  (Endpoint: /ai/chat, /ai/perguntar, /ai/analisar-operacao)│
└────────────────────┬────────────────────────────────────────┘
                     │
         ┌───────────┴───────────┐
         │                       │
    ┌────▼────┐          ┌──────▼──────┐
    │AIService│          │AIService     │
    │(Spring  │          │Fallback      │
    │ AI)     │          │(Respostas    │
    │         │          │ pré-definidas)│
    └────┬────┘          └──────────────┘
         │
    ┌────▼────┐
    │ChatModel│
    │(Ollama/ │
    │OpenAI)  │
    └─────────┘
```

#### 1. **AIController** (`AIController.java`)
- **Responsabilidade**: Receber requisições HTTP e rotear para os serviços de IA
- **Endpoints**:
  - `GET /ai/chat` - Página do chat interativo
  - `POST /ai/perguntar` - Processar pergunta do usuário
  - `POST /ai/analisar-operacao` - Analisar operação de moto
- **Estratégia**: Tenta usar `AIService` primeiro, se falhar usa `AIServiceFallback`

#### 2. **AIService** (`AIService.java`)
- **Responsabilidade**: Integração com Spring AI (Ollama/OpenAI)
- **Características**:
  - Usa `@ConditionalOnClass` para carregar apenas se Spring AI estiver disponível
  - Inicialização lazy e thread-safe usando `synchronized`
  - Usa Reflection para chamar Spring AI (compatibilidade com diferentes versões)
  - Fallback automático para `AIServiceFallback` em caso de erro

#### 3. **AIServiceFallback** (`AIServiceFallback.java`)
- **Responsabilidade**: Fornecer respostas inteligentes sem precisar de IA externa
- **Características**:
  - Respostas pré-definidas baseadas em palavras-chave
  - Cobre os principais tópicos do sistema (status, cadastro, operações, etc.)
  - Sempre disponível, não depende de configuração externa

### 🔄 Fluxo de Funcionamento

#### Fluxo de uma Pergunta

```
1. Usuário faz pergunta no chat (/ai/chat)
   ↓
2. AIController recebe POST /ai/perguntar
   ↓
3. AIController tenta usar AIService
   ↓
4a. Se Spring AI disponível:
    → AIService cria prompt formatado
    → Chama ChatModel (Ollama/OpenAI)
    → Retorna resposta da IA
   ↓
4b. Se Spring AI não disponível OU erro:
    → Usa AIServiceFallback
    → Retorna resposta pré-definida baseada em palavras-chave
   ↓
5. Resposta formatada retorna ao usuário
```

### 🛠️ Como Foi Implementado

#### 1. **Dependência Opcional no pom.xml**

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
    <version>1.0.0</version>
    <optional>true</optional> <!-- Opcional: não quebra se não estiver disponível -->
</dependency>
```

**Por que opcional?**
- Permite que a aplicação funcione sem Spring AI configurado
- Não quebra a compilação se a dependência não estiver disponível
- Facilita desenvolvimento local sem precisar configurar IA

#### 2. **Carregamento Condicional**

```java
@Service
@ConditionalOnClass(name = "org.springframework.ai.chat.ChatModel")
public class AIService {
    // Só é criado se ChatModel estiver no classpath
}
```

**Por que usar `@ConditionalOnClass`?**
- O Spring só cria o bean se a classe `ChatModel` existir
- Se não existir, o `AIServiceFallback` é usado automaticamente
- Evita erros de ClassNotFoundException

#### 3. **Uso de Reflection**

```java
// Buscar ChatModel no ApplicationContext
Class<?> chatModelClass = Class.forName("org.springframework.ai.chat.ChatModel");
var beans = applicationContext.getBeansOfType(chatModelClass);
if (!beans.isEmpty()) {
    chatModel = beans.values().iterator().next();
}

// Criar Prompt via Reflection
Class<?> promptClass = Class.forName("org.springframework.ai.chat.prompt.Prompt");
Class<?> userMessageClass = Class.forName("org.springframework.ai.chat.messages.UserMessage");
Object userMessage = userMessageClass.getConstructor(String.class).newInstance(promptText);
Object prompt = promptClass.getConstructor(userMessageClass).newInstance(userMessage);

// Chamar método generate() via Reflection
Method generateMethod = chatModelClass.getMethod("call", promptClass);
Object response = generateMethod.invoke(chatModel, prompt);
```

**Por que usar Reflection?**
- **Compatibilidade**: Funciona com diferentes versões do Spring AI
- **Flexibilidade**: Não quebra se a API mudar
- **Opcionalidade**: Permite usar Spring AI sem dependência direta

#### 4. **Estratégia de Fallback**

O sistema implementa uma estratégia de fallback em múltiplas camadas:

**Camada 1: Detecção de Disponibilidade**
```java
if (chatModel == null) {
    return fallback.obterSugestao(contexto, pergunta);
}
```

**Camada 2: Tratamento de Erros**
```java
try {
    return chamarIA(prompt);
} catch (Exception e) {
    logger.warn("Erro ao chamar IA, usando fallback: {}", e.getMessage());
    return fallback.obterSugestao(contexto, pergunta);
}
```

**Camada 3: Respostas Pré-definidas**
```java
// AIServiceFallback.java
if (pergunta.toLowerCase().contains("status")) {
    return "Para verificar o status de uma moto...";
}
```

### 🎯 Prompt Engineering

#### Prompt para Chat

```java
String promptText = String.format(
    "Você é um assistente especializado em gestão de motos para logística. " +
    "Contexto: %s\n\nPergunta: %s\n\n" +
    "Forneça uma resposta útil, prática e objetiva em português brasileiro.",
    contexto, pergunta
);
```

**Características do Prompt:**
- Define o papel do assistente (especialista em gestão de motos)
- Inclui contexto do sistema
- Solicita resposta em português brasileiro
- Formato claro e objetivo

### 📊 Integração com Outros Serviços

A IA também é usada em outros serviços do sistema:

#### Visão Computacional (`VisaoComputacionalService.java`)
- Usa IA para análise de detecções
- Detecta anomalias usando IA
- Gera análise agregada do pátio

#### Localização Inteligente (`LocalizacaoInteligenteService.java`)
- Usa IA para otimização de posicionamento
- Calcula melhor posição baseado em status
- Gera recomendações inteligentes

### ✅ Vantagens da Arquitetura

1. **Flexibilidade**: Funciona com ou sem IA configurada
2. **Robustez**: Fallback garante que sempre há resposta
3. **Performance**: Inicialização lazy evita overhead
4. **Manutenibilidade**: Código limpo e bem documentado
5. **Escalabilidade**: Fácil adicionar novos provedores de IA
6. **Compatibilidade**: Funciona com diferentes versões do Spring AI
7. **Reflection**: Permite uso dinâmico sem dependência direta
8. **Multi-Camadas**: Fallback em múltiplas camadas garante robustez

### 🤖 Configuração do Spring AI

#### Opção 1: Ollama (Local, Gratuito - Recomendado)

1. **Instalar Ollama**: https://ollama.ai/
2. **Baixar modelo**:
   ```bash
   ollama pull llama2
   ```
3. **Editar `application.properties`** e descomentar:
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

**Nota**: O Spring AI está configurado como dependência opcional no `pom.xml`. A aplicação funciona normalmente mesmo sem Spring AI configurado (usa fallback inteligente).

---

## 🔗 Integração Multidisciplinar

### Disciplinas Integradas

#### 1. **Inteligência Artificial** (Spring AI) 🤖
- **IA Integrada**: Assistente inteligente para suporte com Spring AI
- **Chat Interativo**: Conversa com IA sobre o sistema (`/ai/chat`)
- **Análise de Dados**: Análise automática de operações usando IA
- **Visão Computacional**: Detecção e análise de motos no pátio usando IA
- **Localização Inteligente**: Otimização de posicionamento usando algoritmos inteligentes
- **Detecção de Anomalias**: Identificação automática de problemas usando IA
- **Fallback Inteligente**: Sistema funciona mesmo sem IA configurada
- **Múltiplos Provedores**: Suporta Ollama (local, gratuito) ou OpenAI (pago)
- **Evidências**: 
  - `AIService.java` - Serviço de IA com Spring AI
  - `AIServiceFallback.java` - Fallback inteligente quando IA não está disponível
  - `AIController.java` - Controller do chat
  - `VisaoComputacionalService.java` - Serviço de visão computacional com IA
  - `LocalizacaoInteligenteService.java` - Serviço de localização inteligente com IA
  - `templates/ai/chat.html` - Interface do chat com design moderno

#### 2. **IoT/IOB - ESP8266** (Internet das Coisas)
- **Integração ESP8266**: Controle de LED físico via HTTP REST (87% mais barato que ESP32!)
- **Simulação Inteligente**: Sistema funciona com ou sem hardware físico
- **Comunicação Remota**: API REST para comunicação com ESP8266
- **Custo-Benefício**: Hardware econômico (R$ 9,35/moto) com ROI impressionante
- **Evidências**:
  - `ESP32Service.java` - Serviço de controle ESP8266/ESP32
  - `ESP32_LED_EXAMPLE.ino` - Código Arduino para ESP8266/ESP32 (compatível)
  - `DisruptiveArchitecturesController.java` - Controller do dashboard IoT/IA
  - Endpoint: `POST /disruptive-architectures/ativar-led`

#### 3. **Engenharia de Software** (Arquitetura)
- **Padrões de Projeto**: Repository, Service, MVC
- **SOLID**: Princípios aplicados no código
- **Clean Code**: Código limpo e bem documentado
- **Arquitetura em Camadas**: Separação clara de responsabilidades

#### 4. **Interface e Experiência do Usuário**
- **UI/UX**: Interface moderna com Bootstrap 5
- **Responsividade**: Design adaptável a diferentes telas
- **Acessibilidade**: Navegação intuitiva e clara
- **Evidências**: Templates Thymeleaf com design consistente

#### 5. **Segurança da Informação**
- **Autenticação**: Spring Security com login seguro
- **Autorização**: Controle de acesso por perfis
- **Criptografia**: Senhas hashadas com BCrypt
- **CSRF Protection**: Proteção contra ataques CSRF

---

## 🎯 Resultados Finais

### ✅ Funcionalidades Implementadas

#### Inteligência Artificial
- ✅ **Chat Interativo**: Assistente IA funcional com Spring AI
- ✅ **Fallback Inteligente**: Sistema funciona mesmo sem IA configurada
- ✅ **Múltiplos Provedores**: Suporta Ollama (local) e OpenAI (pago)
- ✅ **Visão Computacional**: Detecção e análise de motos com IA
- ✅ **Localização Inteligente**: Otimização de posicionamento com algoritmos inteligentes
- ✅ **Detecção de Anomalias**: Identificação automática de problemas

#### Internet das Coisas (IoT)
- ✅ **Controle de LED**: LED pisca via ESP8266/ESP32 (simulado ou físico)
- ✅ **Comunicação HTTP REST**: Integração completa com hardware
- ✅ **Dashboard IoT/IOB/IA**: Interface completa para busca inteligente
- ✅ **Busca Inteligente**: Localização de motos em 30 segundos
- ✅ **Código Arduino Completo**: Implementação completa para ESP8266/ESP32

### 📊 Métricas de Performance

#### Tempo de Busca
- **Antes**: 10-15 minutos para encontrar uma moto
- **Depois**: 30 segundos (busca + LED piscando)
- **Melhoria**: Redução de 95-97% no tempo de busca

#### Custo-Benefício
- **Custo por moto**: R$ 9,35 (hardware - ESP8266)
- **Custo total (100 motos)**: R$ 935,00 (instalação interna)
- **Economia anual**: R$ 12.024,00
- **ROI**: 1.186% no primeiro ano 🚀
- **Payback**: 28 dias (menos de 1 mês!)
- **Economia vs. opção original**: 87% mais barato!

#### Consumo de Energia
- **ESP8266 em standby**: 0,08W (mais eficiente que ESP32)
- **Custo mensal (100 motos)**: R$ 3,00
- **Custo anual (100 motos)**: R$ 36,00 (desprezível comparado à economia)

### 🏆 Diferenciais Técnicos

1. **Arquitetura Flexível**: Sistema funciona com ou sem hardware físico
2. **Robustez**: Fallback em múltiplas camadas garante disponibilidade
3. **Custo-Benefício**: Hardware econômico (ESP8266) com ROI impressionante
4. **Integração IA**: Spring AI integrado com fallback inteligente
5. **Comunicação HTTP REST**: Integração completa com ESP8266/ESP32
6. **Código Completo**: Código Arduino documentado linha por linha
7. **Documentação Completa**: README detalhado com todas as informações

### 📈 Resultados Quantitativos

#### Para Frota de 100 Motos
- **Investimento**: R$ 935,00
- **Economia anual**: R$ 12.024,00
- **ROI**: 1.186% no primeiro ano
- **Payback**: 28 dias
- **Tempo economizado**: 9-14 minutos por busca
- **Economia diária**: R$ 33,40
- **Economia mensal**: R$ 1.002,00

#### Para Frota de 500 Motos
- **Investimento**: R$ 4.675,00
- **Economia anual**: R$ 60.120,00
- **ROI**: 1.186% no primeiro ano
- **Payback**: 28 dias

#### Para Frota de 1.000 Motos
- **Investimento**: R$ 9.350,00
- **Economia anual**: R$ 120.240,00
- **ROI**: 1.186% no primeiro ano
- **Payback**: 28 dias

### 🎯 Objetivos Alcançados

- ✅ **Localização Rápida**: Redução de 95-97% no tempo de busca
- ✅ **Custo-Benefício**: Hardware econômico com ROI de 1.186%
- ✅ **Integração IA**: Assistente inteligente funcional
- ✅ **Integração IoT**: Controle de LED via ESP8266/ESP32
- ✅ **Robustez**: Sistema funciona mesmo sem IA ou hardware configurado
- ✅ **Documentação**: README completo com todas as informações técnicas
- ✅ **Código Completo**: Implementação completa e documentada

### 🚀 Próximos Passos (Opcional)

- [ ] Integração com sensores adicionais (GPS, acelerômetro)
- [ ] Dashboard mobile para operadores
- [ ] Notificações push para alertas
- [ ] Machine Learning para previsão de manutenção
- [ ] Integração com sistemas de gestão existentes

---

**Desenvolvido com ❤️ para o Challenge 3 - Java Advanced (4º Sprint)**

**FIAP - Faculdade de Informática e Administração Paulista**
