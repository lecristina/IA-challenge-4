/*
 * Código ESP32 para controlar LED via HTTP REST
 * 
 * Este código deve ser carregado no seu ESP32 para que o LED real funcione.
 * 
 * Hardware necessário:
 * - ESP32 (qualquer modelo)
 * - LED conectado ao pino GPIO 2 (ou outro pino de sua escolha)
 * - Resistor 220Ω entre LED e GND
 * 
 * Conexões:
 * - LED positivo (ânodo) -> GPIO 2
 * - LED negativo (cátodo) -> Resistor 220Ω -> GND
 * 
 * Configuração:
 * 1. Configure o SSID e senha do WiFi
 * 2. Carregue este código no ESP32
 * 3. Anote o IP que aparece no Serial Monitor
 * 4. Configure esse IP no application.properties como esp32.base-url
 * 5. Configure esp32.enabled=true no application.properties
 */

#include <WiFi.h>
#include <WebServer.h>
#include <ArduinoJson.h>

// Configuração WiFi
const char* ssid = "SEU_WIFI_SSID";        // Altere para o nome do seu WiFi
const char* password = "SUA_SENHA_WIFI";   // Altere para a senha do seu WiFi

// Configuração do LED
const int LED_PIN = 2;  // Pino do LED (GPIO 2 no ESP32)
const unsigned long LED_DURATION = 30000;  // 30 segundos em millisegundos

// Servidor Web na porta 80
WebServer server(80);

// Variáveis para controlar o LED
unsigned long ledStartTime = 0;
bool ledActive = false;
String placaAtiva = "";

void setup() {
  Serial.begin(115200);
  delay(1000);
  
  // Configurar pino do LED
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);
  
  // Conectar ao WiFi
  Serial.println();
  Serial.print("Conectando ao WiFi: ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  
  Serial.println();
  Serial.println("WiFi conectado!");
  Serial.print("IP do ESP32: ");
  Serial.println(WiFi.localIP());
  
  // Configurar rotas do servidor
  server.on("/led/ativar", HTTP_POST, handleAtivarLED);
  server.on("/led/status", HTTP_GET, handleStatusLED);
  server.on("/", HTTP_GET, handleRoot);
  
  // Iniciar servidor
  server.begin();
  Serial.println("Servidor HTTP iniciado!");
  Serial.println("Acesse http://" + WiFi.localIP().toString() + " para ver o status");
}

void loop() {
  server.handleClient();
  
  // Controlar LED piscando
  if (ledActive) {
    unsigned long currentTime = millis();
    unsigned long elapsedTime = currentTime - ledStartTime;
    
    // Piscar LED (500ms ligado, 500ms desligado)
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
    digitalWrite(LED_PIN, LOW);
  }
  
  delay(10);
}

// Handler para ativar LED
void handleAtivarLED() {
  if (server.hasArg("plain")) {
    String body = server.arg("plain");
    
    // Parse JSON
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, body);
    
    if (error) {
      server.send(400, "application/json", "{\"erro\":\"JSON inválido\"}");
      return;
    }
    
    String placa = doc["placa"].as<String>();
    
    if (placa.length() > 0) {
      ledActive = true;
      ledStartTime = millis();
      placaAtiva = placa;
      
      Serial.print("LED ativado para placa: ");
      Serial.println(placa);
      
      server.send(200, "application/json", 
        "{\"sucesso\":true,\"mensagem\":\"LED ativado\",\"placa\":\"" + placa + "\"}");
    } else {
      server.send(400, "application/json", "{\"erro\":\"Placa não informada\"}");
    }
  } else {
    server.send(400, "application/json", "{\"erro\":\"Body vazio\"}");
  }
}

// Handler para verificar status do LED
void handleStatusLED() {
  StaticJsonDocument<200> doc;
  doc["ativo"] = ledActive;
  doc["placa"] = placaAtiva;
  
  if (ledActive) {
    unsigned long elapsedTime = millis() - ledStartTime;
    unsigned long remainingTime = LED_DURATION - elapsedTime;
    doc["tempoRestante"] = remainingTime > 0 ? remainingTime : 0;
  } else {
    doc["tempoRestante"] = 0;
  }
  
  String response;
  serializeJson(doc, response);
  server.send(200, "application/json", response);
}

// Handler para página raiz
void handleRoot() {
  String html = "<!DOCTYPE html><html><head><title>ESP32 LED Control</title></head><body>";
  html += "<h1>ESP32 LED Control</h1>";
  html += "<p>Status: " + String(ledActive ? "ATIVO" : "INATIVO") + "</p>";
  html += "<p>Placa: " + placaAtiva + "</p>";
  html += "<p>IP: " + WiFi.localIP().toString() + "</p>";
  html += "</body></html>";
  
  server.send(200, "text/html", html);
}

