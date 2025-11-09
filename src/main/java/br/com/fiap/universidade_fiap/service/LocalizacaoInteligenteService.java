package br.com.fiap.universidade_fiap.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;

/**
 * Serviço de Localização Inteligente com ESP32
 * Simula localização GPS/Bluetooth via ESP32 e otimiza posicionamento usando IA
 */
@Service
public class LocalizacaoInteligenteService {

    private static final Logger logger = LoggerFactory.getLogger(LocalizacaoInteligenteService.class);
    
    @Autowired(required = false)
    private AIService aiService;
    
    @Autowired(required = false)
    private AIServiceFallback aiServiceFallback;
    
    private final Random random = new Random();
    
    // Simular dados do ESP32 (GPS + Bluetooth)
    private final Map<Long, Map<String, Object>> dadosESP32 = new HashMap<>();

    /**
     * Obtém localização otimizada das motos usando ESP32 e IA
     */
    public Map<String, Object> obterLocalizacaoOtimizada(List<Moto> motos, List<StatusMoto> statusMotos) {
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, Object>> localizacoes = new ArrayList<>();
        
        // Garantir que as listas não são null
        if (motos == null) {
            motos = new ArrayList<>();
        }
        if (statusMotos == null) {
            statusMotos = new ArrayList<>();
        }
        
        // Criar mapa de status mais recente por moto
        Map<Long, StatusMoto> statusMaisRecente = new HashMap<>();
        for (StatusMoto status : statusMotos) {
            if (status != null && status.getMoto() != null) {
                Long motoId = status.getMoto().getId();
                if (motoId != null) {
                    StatusMoto statusAtual = statusMaisRecente.get(motoId);
                    if (statusAtual == null || 
                        (status.getDataCriacao() != null && statusAtual.getDataCriacao() != null &&
                         status.getDataCriacao().isAfter(statusAtual.getDataCriacao()))) {
                        statusMaisRecente.put(motoId, status);
                    }
                }
            }
        }
        
        // Simular dados do ESP32 para cada moto
        for (Moto moto : motos) {
            try {
                if (moto == null || moto.getId() == null) {
                    logger.warn("Moto inválida encontrada, pulando...");
                    continue;
                }
                
                Map<String, Object> localizacao = obterDadosESP32(moto.getId());
                if (localizacao == null) {
                    logger.warn("Erro ao obter dados ESP32 para moto {}", moto.getId());
                    continue;
                }
                
                StatusMoto status = statusMaisRecente.get(moto.getId());
                String statusAtual = status != null && status.getStatus() != null ? status.getStatus() : "PENDENTE";
                
                // Calcular melhor posição no pátio usando algoritmo inteligente
                Map<String, Integer> melhorPosicao = calcularMelhorPosicao(moto, statusAtual, localizacoes);
                if (melhorPosicao == null) {
                    logger.warn("Erro ao calcular melhor posição para moto {}", moto.getId());
                    continue;
                }
                
                // Calcular distância até entrada (para otimização)
                int posX = melhorPosicao.getOrDefault("x", -1);
                int posY = melhorPosicao.getOrDefault("y", -1);
                double distanciaEntrada = posX >= 0 && posY >= 0 ? calcularDistanciaEntrada(posX, posY) : 0.0;
                
                // Score de otimização (quanto melhor a posição, maior o score)
                double scoreOtimizacao = calcularScoreOtimizacao(moto, statusAtual, melhorPosicao, distanciaEntrada);
                
                Map<String, Object> loc = new HashMap<>();
                loc.put("moto", moto);
                loc.put("status", statusAtual);
                loc.put("area", status != null && status.getArea() != null ? status.getArea() : "Não definida");
                loc.put("posicaoX", posX);
                loc.put("posicaoY", posY);
                
                Object lat = localizacao.get("latitude");
                Object lon = localizacao.get("longitude");
                Object sinalBT = localizacao.get("sinalBluetooth");
                Object sinalGPS = localizacao.get("sinalGPS");
                
                loc.put("latitude", lat != null ? lat : 0.0);
                loc.put("longitude", lon != null ? lon : 0.0);
                loc.put("sinalBluetooth", sinalBT != null ? sinalBT : 0.0);
                loc.put("sinalGPS", sinalGPS != null ? sinalGPS : 0.0);
                loc.put("distanciaEntrada", Math.round(distanciaEntrada * 100.0) / 100.0);
                loc.put("scoreOtimizacao", Math.round(scoreOtimizacao * 100.0) / 100.0);
                loc.put("timestamp", System.currentTimeMillis());
                loc.put("esp32Id", "ESP32-" + String.format("%04d", moto.getId()));
                
                localizacoes.add(loc);
            } catch (Exception e) {
                logger.error("Erro ao processar moto {}: {}", moto != null ? moto.getId() : "null", e.getMessage());
                // Continuar processando outras motos
            }
        }
        
        // Ordenar por score de otimização (melhores posições primeiro)
        try {
            localizacoes.sort(Comparator.comparing((Map<String, Object> loc) -> {
                Object score = loc.get("scoreOtimizacao");
                if (score == null) {
                    return 0.0;
                }
                if (score instanceof Double) {
                    return (Double) score;
                }
                if (score instanceof Number) {
                    return ((Number) score).doubleValue();
                }
                return 0.0;
            }).reversed());
        } catch (Exception e) {
            logger.warn("Erro ao ordenar localizações: {}", e.getMessage());
            // Continuar sem ordenação se houver erro
        }
        
        resultado.put("localizacoes", localizacoes);
        resultado.put("totalMotos", localizacoes.size());
        resultado.put("timestamp", System.currentTimeMillis());
        
        // Análise inteligente usando IA
        try {
            String analiseIA = gerarAnaliseLocalizacao(localizacoes);
            resultado.put("analiseIA", analiseIA != null ? analiseIA : "Sistema de localização inteligente com ESP32.");
        } catch (Exception e) {
            logger.warn("Erro ao gerar análise IA: {}", e.getMessage());
            resultado.put("analiseIA", "Sistema de localização inteligente com ESP32.");
        }
        
        logger.info("Localização inteligente: {} motos localizadas via ESP32", localizacoes.size());
        
        return resultado;
    }
    
    /**
     * Simula dados do ESP32 (GPS + Bluetooth)
     */
    private Map<String, Object> obterDadosESP32(Long motoId) {
        // Se já temos dados para esta moto, retornar
        if (dadosESP32.containsKey(motoId)) {
            return dadosESP32.get(motoId);
        }
        
        // Simular coordenadas GPS (São Paulo - região FIAP)
        double latitude = -23.5505 + (random.nextDouble() - 0.5) * 0.01;
        double longitude = -46.6333 + (random.nextDouble() - 0.5) * 0.01;
        
        // Simular sinal Bluetooth (0-100%)
        double sinalBluetooth = 70 + random.nextDouble() * 30;
        
        // Simular sinal GPS (0-100%)
        double sinalGPS = 80 + random.nextDouble() * 20;
        
        Map<String, Object> dados = new HashMap<>();
        dados.put("latitude", Math.round(latitude * 1000000.0) / 1000000.0);
        dados.put("longitude", Math.round(longitude * 1000000.0) / 1000000.0);
        dados.put("sinalBluetooth", Math.round(sinalBluetooth * 100.0) / 100.0);
        dados.put("sinalGPS", Math.round(sinalGPS * 100.0) / 100.0);
        dados.put("bateriaESP32", 85 + random.nextInt(15));
        
        dadosESP32.put(motoId, dados);
        return dados;
    }
    
    /**
     * Calcula a melhor posição no pátio usando algoritmo inteligente
     */
    private Map<String, Integer> calcularMelhorPosicao(Moto moto, String status, 
                                                         List<Map<String, Object>> localizacoesExistentes) {
        Map<String, Integer> posicao = new HashMap<>();
        
        // Garantir que status não é null
        if (status == null) {
            status = "PENDENTE";
        }
        
        // Se a moto está alugada, não está no pátio
        if ("ALUGADA".equals(status)) {
            posicao.put("x", -1);
            posicao.put("y", -1);
            return posicao;
        }
        
        // Algoritmo de otimização: distribuir motos de forma inteligente
        // Motos prontas perto da entrada, motos em manutenção em área reservada
        
        int x, y;
        
        if ("PRONTA".equals(status)) {
            // Motos prontas: perto da entrada (lado esquerdo, frente)
            x = random.nextInt(5); // 0-4 (perto da entrada)
            y = random.nextInt(3); // 0-2 (frente do pátio)
        } else if (status.contains("MANUTENCAO") || status.contains("REPARO") || 
                   status.contains("DANOS") || status.contains("MOTOR")) {
            // Motos em manutenção: área reservada (lado direito, fundo)
            x = 7 + random.nextInt(3); // 7-9 (área reservada)
            y = 7 + random.nextInt(3); // 7-9 (fundo do pátio)
        } else {
            // Outros status: distribuição aleatória
            x = random.nextInt(10);
            y = random.nextInt(10);
        }
        
        // Evitar sobreposição (se possível)
        final int finalX = x;
        final int finalY = y;
        boolean sobreposicao = localizacoesExistentes != null && localizacoesExistentes.stream()
            .anyMatch(loc -> {
                if (loc == null) return false;
                Object posX = loc.get("posicaoX");
                Object posY = loc.get("posicaoY");
                if (posX == null || posY == null) return false;
                if (posX instanceof Number && posY instanceof Number) {
                    return ((Number) posX).intValue() == finalX && ((Number) posY).intValue() == finalY;
                }
                return posX.equals(finalX) && posY.equals(finalY);
            });
        
        int posX = x;
        int posY = y;
        if (sobreposicao && !"ALUGADA".equals(status)) {
            // Tentar posição adjacente
            posX = (x + 1) % 10;
            posY = (y + 1) % 10;
        }
        
        posicao.put("x", posX);
        posicao.put("y", posY);
        return posicao;
    }
    
    /**
     * Calcula distância até a entrada do pátio
     */
    private double calcularDistanciaEntrada(int x, int y) {
        // Entrada está em (0, 0)
        return Math.sqrt(x * x + y * y);
    }
    
    /**
     * Calcula score de otimização (0-100)
     */
    private double calcularScoreOtimizacao(Moto moto, String status, 
                                           Map<String, Integer> posicao, double distanciaEntrada) {
        double score = 50.0; // Base
        
        // Garantir que status não é null
        if (status == null) {
            status = "PENDENTE";
        }
        
        // Motos prontas perto da entrada = score alto
        if ("PRONTA".equals(status) && distanciaEntrada < 3) {
            score += 30;
        }
        
        // Motos em manutenção longe da entrada = score médio
        if (status.contains("MANUTENCAO") || status.contains("REPARO")) {
            score += 20;
        }
        
        // Penalizar distância muito longa
        if (distanciaEntrada > 7) {
            score -= 20;
        }
        
        // Bônus por organização
        if (posicao != null) {
            Object xObj = posicao.get("x");
            Object yObj = posicao.get("y");
            if (xObj instanceof Number && yObj instanceof Number) {
                int x = ((Number) xObj).intValue();
                int y = ((Number) yObj).intValue();
                if (x >= 0 && y >= 0) {
                    score += 10;
                }
            }
        }
        
        return Math.max(0, Math.min(100, score));
    }
    
    /**
     * Gera análise de localização usando IA
     */
    private String gerarAnaliseLocalizacao(List<Map<String, Object>> localizacoes) {
        if (localizacoes == null || localizacoes.isEmpty()) {
            return "Sistema de localização inteligente com ESP32. Nenhuma moto localizada no momento.";
        }
        
        long motosProntas = 0;
        long motosManutencao = 0;
        double scoreMedio = 0.0;
        
        try {
            motosProntas = localizacoes.stream()
                .filter(loc -> loc != null && "PRONTA".equals(loc.get("status")))
                .count();
            
            motosManutencao = localizacoes.stream()
                .filter(loc -> {
                    if (loc == null || loc.get("status") == null) {
                        return false;
                    }
                    String status = (String) loc.get("status");
                    return status != null && (status.contains("MANUTENCAO") || status.contains("REPARO") || 
                           status.contains("DANOS") || status.contains("MOTOR"));
                })
                .count();
            
            scoreMedio = localizacoes.stream()
                .filter(loc -> loc != null && loc.get("scoreOtimizacao") != null)
                .mapToDouble(loc -> {
                    Object score = loc.get("scoreOtimizacao");
                    if (score instanceof Double) {
                        return (Double) score;
                    }
                    if (score instanceof Number) {
                        return ((Number) score).doubleValue();
                    }
                    return 0.0;
                })
                .average()
                .orElse(0.0);
        } catch (Exception e) {
            logger.warn("Erro ao calcular estatísticas: {}", e.getMessage());
        }
        
        String contexto = String.format(
            "Análise de localização do pátio via ESP32: Total de %d motos. " +
            "Prontas: %d, Em manutenção: %d. Score médio de otimização: %.2f. " +
            "Forneça recomendações inteligentes para melhorar a organização do pátio.",
            localizacoes.size(), motosProntas, motosManutencao, scoreMedio
        );
        
        String pergunta = "Analise a distribuição das motos no pátio e forneça recomendações práticas para otimização.";
        
        try {
            if (aiService != null) {
                return aiService.obterSugestao(contexto, pergunta);
            } else if (aiServiceFallback != null) {
                return aiServiceFallback.obterSugestao(contexto, pergunta);
            }
        } catch (Exception e) {
            logger.warn("Erro ao usar IA para análise de localização: {}", e.getMessage());
        }
        
        // Fallback
        return String.format(
            "Análise do pátio: %d motos localizadas via ESP32. " +
            "%d prontas para uso, %d em manutenção. Score médio de otimização: %.2f/100. " +
            "Recomendação: Organizar motos prontas próximas à entrada para facilitar acesso.",
            localizacoes.size(), motosProntas, motosManutencao, scoreMedio
        );
    }
    
    /**
     * Busca moto mais próxima de uma posição específica
     */
    public Map<String, Object> buscarMotoMaisProxima(double latitude, double longitude, 
                                                      List<Moto> motos, List<StatusMoto> statusMotos) {
        try {
            Map<String, Object> resultado = obterLocalizacaoOtimizada(motos, statusMotos);
            
            if (resultado == null) {
                return null;
            }
            
            Object locsObj = resultado.get("localizacoes");
            if (!(locsObj instanceof List)) {
                return null;
            }
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> localizacoes = (List<Map<String, Object>>) locsObj;
            
            if (localizacoes == null || localizacoes.isEmpty()) {
                return null;
            }
            
            // Filtrar apenas motos disponíveis (PRONTA)
            List<Map<String, Object>> motosDisponiveis = localizacoes.stream()
                .filter(loc -> loc != null && "PRONTA".equals(loc.get("status")))
                .collect(Collectors.toList());
            
            if (motosDisponiveis.isEmpty()) {
                return null;
            }
            
            // Calcular distância para cada moto
            motosDisponiveis.forEach(loc -> {
                try {
                    Object latObj = loc.get("latitude");
                    Object lonObj = loc.get("longitude");
                    
                    if (latObj instanceof Number && lonObj instanceof Number) {
                        double lat = ((Number) latObj).doubleValue();
                        double lon = ((Number) lonObj).doubleValue();
                        double distancia = calcularDistanciaGPS(latitude, longitude, lat, lon);
                        loc.put("distanciaUsuario", Math.round(distancia * 1000.0) / 1000.0); // em km
                    }
                } catch (Exception e) {
                    logger.warn("Erro ao calcular distância: {}", e.getMessage());
                }
            });
            
            // Ordenar por distância
            try {
                motosDisponiveis.sort(Comparator.comparing((Map<String, Object> loc) -> {
                    Object dist = loc.get("distanciaUsuario");
                    if (dist instanceof Double) {
                        return (Double) dist;
                    }
                    if (dist instanceof Number) {
                        return ((Number) dist).doubleValue();
                    }
                    return Double.MAX_VALUE;
                }));
            } catch (Exception e) {
                logger.warn("Erro ao ordenar por distância: {}", e.getMessage());
            }
            
            Map<String, Object> resultadoBusca = new HashMap<>();
            resultadoBusca.put("motoMaisProxima", motosDisponiveis.get(0));
            resultadoBusca.put("outrasOpcoes", motosDisponiveis.size() > 1 ? 
                motosDisponiveis.subList(1, Math.min(4, motosDisponiveis.size())) : new ArrayList<>());
            resultadoBusca.put("totalEncontradas", motosDisponiveis.size());
            
            return resultadoBusca;
        } catch (Exception e) {
            logger.error("Erro ao buscar moto mais próxima: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Calcula distância entre duas coordenadas GPS (Haversine)
     */
    private double calcularDistanciaGPS(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raio da Terra em km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}

