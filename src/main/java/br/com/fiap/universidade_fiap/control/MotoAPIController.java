package br.com.fiap.universidade_fiap.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.service.ESP32Service;
import br.com.fiap.universidade_fiap.service.LocalizacaoInteligenteService;

/**
 * API REST para integração com Mobile App e .NET
 * Endpoints JSON para consumo externo
 */
@RestController
@RequestMapping("/api/v1")
public class MotoAPIController {

    private static final Logger logger = LoggerFactory.getLogger(MotoAPIController.class);
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired(required = false)
    private ESP32Service esp32Service;
    
    @Autowired(required = false)
    private LocalizacaoInteligenteService localizacaoService;
    
    /**
     * Lista todas as motos (JSON)
     * GET /api/v1/motos
     */
    @GetMapping("/motos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> listarMotos() {
        Map<String, Object> resposta = new HashMap<>();
        
        try {
            List<Moto> motos = motoRepository.findAll();
            
            List<Map<String, Object>> motosJson = motos.stream()
                .map(moto -> {
                    Map<String, Object> motoJson = new HashMap<>();
                    motoJson.put("id", moto.getId());
                    motoJson.put("placa", moto.getPlaca());
                    motoJson.put("chassi", moto.getChassi());
                    motoJson.put("motor", moto.getMotor());
                    motoJson.put("dataCriacao", moto.getDataCriacao());
                    return motoJson;
                })
                .collect(Collectors.toList());
            
            resposta.put("sucesso", true);
            resposta.put("total", motos.size());
            resposta.put("motos", motosJson);
            
            logger.info("API: {} motos listadas", motos.size());
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            logger.error("Erro ao listar motos via API: {}", e.getMessage());
            resposta.put("sucesso", false);
            resposta.put("erro", "Erro ao listar motos");
            return ResponseEntity.status(500).body(resposta);
        }
    }
    
    /**
     * Busca localização de uma moto por placa (JSON)
     * GET /api/v1/motos/{placa}/localizacao
     */
    @GetMapping("/motos/{placa}/localizacao")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarLocalizacao(@PathVariable String placa) {
        Map<String, Object> resposta = new HashMap<>();
        
        try {
            // Normalizar placa (remover hífen e espaços)
            String placaNormalizada = placa.replaceAll("[\\s-]", "").toUpperCase();
            
            // Buscar moto
            Moto moto = motoRepository.findByPlaca(placaNormalizada).orElse(null);
            
            if (moto == null) {
                // Tentar sem hífen
                moto = motoRepository.findAll().stream()
                    .filter(m -> m.getPlaca() != null && 
                        m.getPlaca().replaceAll("[\\s-]", "").equalsIgnoreCase(placaNormalizada))
                    .findFirst()
                    .orElse(null);
            }
            
            if (moto == null) {
                resposta.put("sucesso", false);
                resposta.put("erro", "Moto não encontrada");
                return ResponseEntity.status(404).body(resposta);
            }
            
            // Buscar status mais recente
            List<StatusMoto> statusList = statusMotosRepository.findByMotoId(moto.getId());
            StatusMoto statusMaisRecente = statusList.stream()
                .max((s1, s2) -> s1.getDataCriacao().compareTo(s2.getDataCriacao()))
                .orElse(null);
            
            // Obter localização
            Map<String, Object> localizacao = new HashMap<>();
            if (localizacaoService != null) {
                localizacao = localizacaoService.obterLocalizacaoOtimizada(
                    List.of(moto), 
                    statusList
                );
            } else {
                // Fallback básico
                localizacao.put("posicaoX", -1);
                localizacao.put("posicaoY", -1);
                localizacao.put("area", statusMaisRecente != null ? statusMaisRecente.getArea() : "Não definida");
            }
            
            // Verificar LED
            boolean ledAtivo = false;
            if (esp32Service != null) {
                ledAtivo = esp32Service.isLEDAtivo(placa);
            }
            
            resposta.put("sucesso", true);
            resposta.put("moto", Map.of(
                "id", moto.getId(),
                "placa", moto.getPlaca(),
                "chassi", moto.getChassi(),
                "motor", moto.getMotor()
            ));
            resposta.put("status", statusMaisRecente != null ? statusMaisRecente.getStatus() : "PENDENTE");
            resposta.put("area", statusMaisRecente != null ? statusMaisRecente.getArea() : "Não definida");
            resposta.put("localizacao", Map.of(
                "posicaoX", localizacao.getOrDefault("posicaoX", -1),
                "posicaoY", localizacao.getOrDefault("posicaoY", -1),
                "area", localizacao.getOrDefault("area", "Não definida")
            ));
            resposta.put("led", Map.of(
                "ativo", ledAtivo,
                "virtual", true
            ));
            
            logger.info("API: Localização da moto {} obtida", placa);
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            logger.error("Erro ao buscar localização via API: {}", e.getMessage());
            resposta.put("sucesso", false);
            resposta.put("erro", "Erro ao buscar localização");
            return ResponseEntity.status(500).body(resposta);
        }
    }
    
    /**
     * Ativa LED de uma moto por placa (JSON)
     * POST /api/v1/motos/{placa}/ativar-led
     */
    @PostMapping("/motos/{placa}/ativar-led")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> ativarLED(@PathVariable String placa) {
        Map<String, Object> resposta = new HashMap<>();
        
        try {
            if (esp32Service == null) {
                resposta.put("sucesso", false);
                resposta.put("erro", "Serviço ESP32 não disponível");
                return ResponseEntity.status(503).body(resposta);
            }
            
            boolean ativado = esp32Service.ativarLED(placa);
            
            resposta.put("sucesso", ativado);
            resposta.put("placa", placa);
            resposta.put("ledAtivo", ativado);
            resposta.put("mensagem", ativado ? "LED ativado com sucesso" : "Erro ao ativar LED");
            
            logger.info("API: LED ativado para moto {}", placa);
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            logger.error("Erro ao ativar LED via API: {}", e.getMessage());
            resposta.put("sucesso", false);
            resposta.put("erro", "Erro ao ativar LED");
            return ResponseEntity.status(500).body(resposta);
        }
    }
    
    /**
     * Busca status de uma moto por placa (JSON)
     * GET /api/v1/motos/{placa}/status
     */
    @GetMapping("/motos/{placa}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buscarStatus(@PathVariable String placa) {
        Map<String, Object> resposta = new HashMap<>();
        
        try {
            String placaNormalizada = placa.replaceAll("[\\s-]", "").toUpperCase();
            
            Moto moto = motoRepository.findByPlaca(placaNormalizada).orElse(null);
            
            if (moto == null) {
                moto = motoRepository.findAll().stream()
                    .filter(m -> m.getPlaca() != null && 
                        m.getPlaca().replaceAll("[\\s-]", "").equalsIgnoreCase(placaNormalizada))
                    .findFirst()
                    .orElse(null);
            }
            
            if (moto == null) {
                resposta.put("sucesso", false);
                resposta.put("erro", "Moto não encontrada");
                return ResponseEntity.status(404).body(resposta);
            }
            
            List<StatusMoto> statusList = statusMotosRepository.findByMotoId(moto.getId());
            StatusMoto statusMaisRecente = statusList.stream()
                .max((s1, s2) -> s1.getDataCriacao().compareTo(s2.getDataCriacao()))
                .orElse(null);
            
            resposta.put("sucesso", true);
            resposta.put("moto", Map.of(
                "id", moto.getId(),
                "placa", moto.getPlaca()
            ));
            resposta.put("status", statusMaisRecente != null ? statusMaisRecente.getStatus() : "PENDENTE");
            resposta.put("area", statusMaisRecente != null ? statusMaisRecente.getArea() : "Não definida");
            resposta.put("dataStatus", statusMaisRecente != null ? statusMaisRecente.getDataCriacao() : null);
            
            return ResponseEntity.ok(resposta);
            
        } catch (Exception e) {
            logger.error("Erro ao buscar status via API: {}", e.getMessage());
            resposta.put("sucesso", false);
            resposta.put("erro", "Erro ao buscar status");
            return ResponseEntity.status(500).body(resposta);
        }
    }
}

