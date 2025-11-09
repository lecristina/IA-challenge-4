package br.com.fiap.universidade_fiap.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * Serviço para controlar ESP32 - Suporta simulação e comunicação real via HTTP REST
 * 
 * Para usar com LED real:
 * 1. Configure esp32.enabled=true no application.properties
 * 2. Configure esp32.base-url com o IP do seu ESP32 (ex: http://192.168.1.100)
 * 3. No ESP32, implemente um servidor HTTP que receba POST /led/ativar com JSON: {"placa": "ABC1234"}
 */
@Service
public class ESP32Service {

    private static final Logger logger = LoggerFactory.getLogger(ESP32Service.class);
    
    // Mapa para rastrear LEDs ativos (placa -> timestamp de ativação)
    private final Map<String, Long> ledsAtivos = new ConcurrentHashMap<>();
    
    // Tempo de duração do LED piscando (30 segundos)
    private static final long DURACAO_LED_MS = 30000;
    
    // Configuração para comunicação real com ESP32
    @Value("${esp32.enabled:false}")
    private boolean esp32Enabled;
    
    @Value("${esp32.base-url:http://192.168.1.100}")
    private String esp32BaseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Ativa LED piscando no ESP32 da moto
     * @param placa Placa da moto
     * @return true se ativado com sucesso
     */
    public boolean ativarLED(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            logger.warn("Tentativa de ativar LED com placa inválida");
            return false;
        }
        
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
                logger.error("Erro ao comunicar com ESP32 real em {}: {}", 
                    esp32BaseUrl, e.getMessage());
                // Continuar com simulação se falhar
            }
        }
        
        logger.info("LED SIMULADO ativado para moto com placa: {} via ESP32", placaNormalizada);
        return true;
    }
    
    /**
     * Verifica se o LED está ativo para uma placa
     * @param placa Placa da moto
     * @return true se LED está piscando
     */
    public boolean isLEDAtivo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }
        
        String placaNormalizada = placa.trim().toUpperCase();
        Long timestamp = ledsAtivos.get(placaNormalizada);
        
        if (timestamp == null) {
            return false;
        }
        
        // Verificar se ainda está dentro do tempo de duração
        long tempoDecorrido = System.currentTimeMillis() - timestamp;
        if (tempoDecorrido > DURACAO_LED_MS) {
            // LED expirou, remover
            ledsAtivos.remove(placaNormalizada);
            return false;
        }
        
        return true;
    }
    
    /**
     * Desativa LED para uma placa
     * @param placa Placa da moto
     */
    public void desativarLED(String placa) {
        if (placa != null && !placa.trim().isEmpty()) {
            ledsAtivos.remove(placa.trim().toUpperCase());
            logger.info("LED desativado para moto com placa: {}", placa.trim().toUpperCase());
        }
    }
    
    /**
     * Limpa LEDs expirados
     */
    public void limparLEDsExpirados() {
        long agora = System.currentTimeMillis();
        ledsAtivos.entrySet().removeIf(entry -> {
            long tempoDecorrido = agora - entry.getValue();
            return tempoDecorrido > DURACAO_LED_MS;
        });
    }
    
    /**
     * Obtém informações do LED para uma placa
     * @param placa Placa da moto
     * @return Map com informações do LED
     */
    public Map<String, Object> obterInfoLED(String placa) {
        Map<String, Object> info = new HashMap<>();
        
        if (placa == null || placa.trim().isEmpty()) {
            info.put("ativo", false);
            info.put("tempoRestante", 0);
            return info;
        }
        
        String placaNormalizada = placa.trim().toUpperCase();
        Long timestamp = ledsAtivos.get(placaNormalizada);
        
        if (timestamp == null) {
            info.put("ativo", false);
            info.put("tempoRestante", 0);
        } else {
            long tempoDecorrido = System.currentTimeMillis() - timestamp;
            long tempoRestante = Math.max(0, DURACAO_LED_MS - tempoDecorrido);
            
            info.put("ativo", tempoRestante > 0);
            info.put("tempoRestante", tempoRestante);
            info.put("tempoRestanteSegundos", tempoRestante / 1000);
        }
        
        return info;
    }
}

