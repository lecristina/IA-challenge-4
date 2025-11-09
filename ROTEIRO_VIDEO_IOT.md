# 🎬 Roteiro Técnico - IoT e Controle de LED no TrackZone
## Apresentação para 3 Pessoas

---

## 🎯 INTRODUÇÃO (40 segundos)

**Pessoa 1:**
> "Apresento a arquitetura IoT do TrackZone: sistema de rastreamento e localização de motos em tempo real usando ESP8266/ESP32. Integração completa entre backend Spring Boot e hardware embarcado via HTTP REST. O sistema reduz tempo de busca de 10-15 minutos para 30 segundos, com ROI de 1.186% e payback de 28 dias. Tecnologias: Spring Boot, RestTemplate, ESP8266, Arduino, comunicação HTTP REST assíncrona e controle de LED inteligente."

---

## 📐 PARTE 1: ARQUITETURA (1 minuto)

### 1.1. Componentes (20 segundos)

**Pessoa 1:**
> "Arquitetura em três camadas: Controller recebe requisições, Service controla LED, hardware ESP8266 executa comandos."

### 1.2. Modo Simulado vs Físico (20 segundos)

**Pessoa 2:**
> "Sistema funciona em dois modos: simulado quando `esp32.enabled=false`, físico quando `esp32.enabled=true` com URL do ESP8266."

### 1.3. Comunicação HTTP REST (20 segundos)

**Pessoa 3:**
> "Comunicação via RestTemplate: Java envia POST para `/led/ativar`, ESP8266 responde e controla LED físico."

---

## 🔄 PARTE 2: FLUXO DE FUNCIONAMENTO (1 minuto)

### 2.1. Busca de Moto (30 segundos)

**Pessoa 1:**
> "Fluxo: usuário busca placa, Controller busca no banco, chama Service para ativar LED."

### 2.2. Ativação de LED (30 segundos)

**Pessoa 2:**
> "Service verifica `esp32.enabled`, se true envia HTTP POST, se false simula. Rastreia LEDs ativos em ConcurrentHashMap com timestamp."

---

## 🛠️ PARTE 3: IMPLEMENTAÇÃO TÉCNICA (1 minuto)

### 3.1. ESP32Service (30 segundos)

**Pessoa 3:**
> "Service usa `@Value` para ler configuração, RestTemplate para HTTP, ConcurrentHashMap thread-safe para rastreamento. Desativação automática após 30 segundos."

### 3.2. Dados ESP32 Simulados (30 segundos)

**Pessoa 1:**
> "Geração de dados: Bluetooth 70-99%, Bateria 85-99%, ESP32 ID formatado. Alertas automáticos para bateria baixa."

---

## 🔌 PARTE 4: HARDWARE ESP8266/ESP32 (1 minuto)

### 4.1. Código Arduino (30 segundos)

**Pessoa 2:**
> "Arduino: servidor HTTP na porta 80, endpoint POST `/led/ativar` recebe JSON com placa, controla LED no GPIO 2, pisca por 30 segundos, desativa automaticamente."

### 4.2. Comunicação HTTP (30 segundos)

**Pessoa 3:**
> "Requisição: POST com JSON `{"placa": "ABC1234"}`. Resposta: JSON com sucesso e tempo restante. ESP8266 processa e ativa LED físico."

---

## 💻 PARTE 5: DEMONSTRAÇÃO PRÁTICA (1 minuto)

### 5.1. Busca de Moto (30 segundos)

**Pessoa 1:**
> "Demonstração: buscar placa ABC1234, sistema encontra moto, ativa LED, mostra dados ESP32."

### 5.2. Dashboard Completo (30 segundos)

**Pessoa 2:**
> "Dashboard mostra todas as motos: tabela com placa, modelo, status, Bluetooth, ESP32 ID, bateria, alertas, botão LED."

---

## ⚙️ PARTE 6: CONFIGURAÇÃO (20 segundos)

**Pessoa 3:**
> "Configuração: `esp32.enabled=true` e `esp32.base-url=http://192.168.1.100` no `application.properties`. Código Arduino precisa SSID e senha WiFi."

---

## 🎯 CONCLUSÃO (40 segundos)

### 7.1. Resumo Técnico (15 segundos)

**Pessoa 1:**
> "Arquitetura IoT completa: Controller, Service, Hardware. Modo simulado e físico. HTTP REST. Controle de LED via ESP8266."

### 7.2. Economia e ROI (25 segundos)

**Pessoa 2:**
> "Custo: R$ 9,35/moto com ESP8266 - 87% mais barato que ESP32. ROI: 1.186% no primeiro ano. Payback: 28 dias."

**Pessoa 3:**
> "Economia: R$ 12.024,00/ano para 100 motos. Redução de tempo de busca de 10-15 minutos para 30 segundos. Consumo de energia: R$ 3,00/mês para 100 motos."

---

## ⏱️ DURAÇÃO TOTAL

- **Introdução**: 40 segundos
- **Parte 1 - Arquitetura**: 1 minuto
- **Parte 2 - Fluxo**: 1 minuto
- **Parte 3 - Implementação**: 1 minuto
- **Parte 4 - Hardware**: 1 minuto
- **Parte 5 - Demonstração**: 1 minuto
- **Parte 6 - Configuração**: 20 segundos
- **Conclusão**: 40 segundos

**Total**: ~6 minutos e 20 segundos

---

## 📋 DISTRIBUIÇÃO DE FALAS

### Pessoa 1:
- Introdução
- Parte 1.1 (Componentes)
- Parte 2.1 (Busca de Moto)
- Parte 3.2 (Dados ESP32)
- Parte 5.1 (Busca de Moto - Demo)
- Conclusão 7.1 (Resumo Técnico)

### Pessoa 2:
- Parte 1.2 (Modo Simulado vs Físico)
- Parte 2.2 (Ativação de LED)
- Parte 4.1 (Código Arduino)
- Parte 5.2 (Dashboard Completo)
- Conclusão 7.2 (Economia - primeira parte)

### Pessoa 3:
- Parte 1.3 (Comunicação HTTP REST)
- Parte 3.1 (ESP32Service)
- Parte 4.2 (Comunicação HTTP)
- Parte 6 (Configuração)
- Conclusão 7.2 (Economia - segunda parte)

---

**Roteiro técnico, objetivo e direto ao ponto.**
