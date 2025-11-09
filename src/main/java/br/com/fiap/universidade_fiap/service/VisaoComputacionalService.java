package br.com.fiap.universidade_fiap.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;

/**
 * Serviço de Visão Computacional com IA
 * Simula detecção e análise de motos no pátio usando IA
 */
@Service
public class VisaoComputacionalService {

    private static final Logger logger = LoggerFactory.getLogger(VisaoComputacionalService.class);
    
    @Autowired(required = false)
    private AIService aiService;
    
    @Autowired(required = false)
    private AIServiceFallback aiServiceFallback;
    
    private final Random random = new Random();

    /**
     * Detecta motos no pátio usando visão computacional simulada
     */
    public Map<String, Object> detectarMotosNoPatio(List<Moto> motos, List<StatusMoto> statusMotos) {
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> deteccoes = new ArrayList<>();
        
        // Criar mapa de status mais recente por moto
        Map<Long, StatusMoto> statusMaisRecente = new HashMap<>();
        for (StatusMoto status : statusMotos) {
            if (status.getMoto() != null) {
                Long motoId = status.getMoto().getId();
                StatusMoto statusAtual = statusMaisRecente.get(motoId);
                if (statusAtual == null || 
                    status.getDataCriacao().isAfter(statusAtual.getDataCriacao())) {
                    statusMaisRecente.put(motoId, status);
                }
            }
        }
        
        // Simular detecção de cada moto com IA
        for (Moto moto : motos) {
            Map<String, Object> deteccao = new HashMap<>();
            
            StatusMoto status = statusMaisRecente.get(moto.getId());
            String statusAtual = status != null ? status.getStatus() : "PENDENTE";
            
            // Simular coordenadas no pátio (baseado em ID para consistência)
            int x = (int) (moto.getId() % 10);
            int y = (int) (moto.getId() / 10);
            
            // Simular confiança da detecção (85-99%)
            double confianca = 85 + random.nextDouble() * 14;
            
            // Simular análise de estado visual
            String analiseVisual = analisarEstadoVisual(statusAtual, moto);
            
            // Detecção de anomalias usando IA
            Map<String, Object> anomalias = detectarAnomalias(moto, statusAtual, status);
            
            deteccao.put("moto", moto);
            deteccao.put("status", statusAtual);
            deteccao.put("area", status != null ? status.getArea() : "Não definida");
            deteccao.put("posicaoX", x);
            deteccao.put("posicaoY", y);
            deteccao.put("confianca", Math.round(confianca * 100.0) / 100.0);
            deteccao.put("analiseVisual", analiseVisual);
            deteccao.put("anomalias", anomalias);
            deteccao.put("timestamp", System.currentTimeMillis());
            
            deteccoes.add(deteccao);
        }
        
        resultado.put("deteccoes", deteccoes);
        resultado.put("totalDetectado", deteccoes.size());
        resultado.put("timestamp", System.currentTimeMillis());
        
        // Análise agregada usando IA
        String analiseAgregada = gerarAnaliseAgregada(deteccoes);
        resultado.put("analiseIA", analiseAgregada);
        
        logger.info("Visão computacional: {} motos detectadas no pátio", deteccoes.size());
        
        return resultado;
    }
    
    /**
     * Analisa o estado visual da moto usando IA
     */
    private String analisarEstadoVisual(String status, Moto moto) {
        String contexto = String.format(
            "Moto placa %s, status: %s. Analise o estado visual desta moto no pátio.",
            moto.getPlaca(), status
        );
        
        String pergunta = "Descreva brevemente o estado visual desta moto baseado no status atual.";
        
        try {
            if (aiService != null) {
                return aiService.obterSugestao(contexto, pergunta);
            } else if (aiServiceFallback != null) {
                return aiServiceFallback.obterSugestao(contexto, pergunta);
            }
        } catch (Exception e) {
            logger.warn("Erro ao usar IA para análise visual: {}", e.getMessage());
        }
        
        // Fallback sem IA
        return gerarAnaliseVisualFallback(status);
    }
    
    /**
     * Detecta anomalias na moto usando IA
     */
    private Map<String, Object> detectarAnomalias(Moto moto, String status, StatusMoto statusMoto) {
        Map<String, Object> anomalias = new HashMap<>();
        List<String> problemas = new ArrayList<>();
        boolean temAnomalia = false;
        
        // Análise baseada em regras + IA
        if (status.contains("DANOS") || status.contains("MOTOR_DEFEITUOSO")) {
            problemas.add("Danos estruturais detectados");
            temAnomalia = true;
        }
        
        if (status.equals("SEM_PLACA")) {
            problemas.add("Placa não identificada pela visão computacional");
            temAnomalia = true;
        }
        
        // Usar IA para análise mais profunda
        String contexto = String.format(
            "Moto placa %s, status: %s, área: %s. Analise se há anomalias que precisam atenção.",
            moto.getPlaca(), 
            status,
            statusMoto != null ? statusMoto.getArea() : "Não definida"
        );
        
        String pergunta = "Identifique anomalias ou problemas que precisam de atenção imediata.";
        
        try {
            String analiseIA = null;
            if (aiService != null) {
                analiseIA = aiService.obterSugestao(contexto, pergunta);
            } else if (aiServiceFallback != null) {
                analiseIA = aiServiceFallback.obterSugestao(contexto, pergunta);
            }
            
            if (analiseIA != null && !analiseIA.isEmpty()) {
                // Extrair problemas da análise da IA
                if (analiseIA.toLowerCase().contains("atenção") || 
                    analiseIA.toLowerCase().contains("problema") ||
                    analiseIA.toLowerCase().contains("anomalia")) {
                    problemas.add("Análise IA detectou necessidade de verificação");
                    temAnomalia = true;
                }
            }
        } catch (Exception e) {
            logger.warn("Erro ao usar IA para detecção de anomalias: {}", e.getMessage());
        }
        
        anomalias.put("temAnomalia", temAnomalia);
        anomalias.put("problemas", problemas);
        anomalias.put("severidade", temAnomalia ? (problemas.size() > 1 ? "ALTA" : "MÉDIA") : "NENHUMA");
        
        return anomalias;
    }
    
    /**
     * Gera análise agregada usando IA
     */
    private String gerarAnaliseAgregada(List<Map<String, Object>> deteccoes) {
        long motosProntas = deteccoes.stream()
            .filter(d -> d.get("status").equals("PRONTA"))
            .count();
        long motosAlugadas = deteccoes.stream()
            .filter(d -> d.get("status").equals("ALUGADA"))
            .count();
        long motosManutencao = deteccoes.stream()
            .filter(d -> {
                String status = (String) d.get("status");
                return status.contains("REPARO") || status.contains("DANOS") || 
                       status.contains("MOTOR") || status.contains("MANUTENCAO");
            })
            .count();
        
        long motosComAnomalias = deteccoes.stream()
            .filter(d -> {
                @SuppressWarnings("unchecked")
                Map<String, Object> anomalias = (Map<String, Object>) d.get("anomalias");
                return anomalias != null && (Boolean) anomalias.get("temAnomalia");
            })
            .count();
        
        String contexto = String.format(
            "Análise do pátio: Total de %d motos detectadas. " +
            "Prontas: %d, Em uso: %d, Em manutenção: %d, Com anomalias: %d. " +
            "Forneça uma análise inteligente e recomendações.",
            deteccoes.size(), motosProntas, motosAlugadas, motosManutencao, motosComAnomalias
        );
        
        String pergunta = "Analise a situação do pátio e forneça recomendações práticas.";
        
        try {
            if (aiService != null) {
                return aiService.obterSugestao(contexto, pergunta);
            } else if (aiServiceFallback != null) {
                return aiServiceFallback.obterSugestao(contexto, pergunta);
            }
        } catch (Exception e) {
            logger.warn("Erro ao usar IA para análise agregada: {}", e.getMessage());
        }
        
        // Fallback sem IA
        return gerarAnaliseAgregadaFallback(motosProntas, motosAlugadas, motosManutencao, motosComAnomalias);
    }
    
    /**
     * Fallback para análise visual sem IA
     */
    private String gerarAnaliseVisualFallback(String status) {
        switch (status) {
            case "PRONTA":
                return "Moto em bom estado visual, pronta para uso.";
            case "ALUGADA":
                return "Moto em uso, não presente no pátio no momento.";
            case "REPARO_SIMPLES":
            case "MANUTENCAO_AGENDADA":
                return "Moto em manutenção, estado visual dentro do esperado.";
            case "DANOS_ESTRUTURAIS":
            case "MOTOR_DEFEITUOSO":
                return "Moto com danos visíveis, requer atenção imediata.";
            default:
                return "Estado visual a ser verificado.";
        }
    }
    
    /**
     * Fallback para análise agregada sem IA
     */
    private String gerarAnaliseAgregadaFallback(long prontas, long alugadas, long manutencao, long anomalias) {
        StringBuilder analise = new StringBuilder();
        analise.append("Análise do pátio: ");
        
        if (prontas > 0) {
            analise.append(String.format("%d moto(s) pronta(s) para uso. ", prontas));
        }
        
        if (alugadas > 0) {
            analise.append(String.format("%d moto(s) em uso. ", alugadas));
        }
        
        if (manutencao > 0) {
            analise.append(String.format("%d moto(s) em manutenção. ", manutencao));
        }
        
        if (anomalias > 0) {
            analise.append(String.format("⚠️ %d moto(s) com anomalias detectadas requerem atenção imediata.", anomalias));
        } else {
            analise.append("Nenhuma anomalia crítica detectada.");
        }
        
        return analise.toString();
    }
}


