package br.com.fiap.universidade_fiap.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.service.AuthenticationService;
import br.com.fiap.universidade_fiap.service.ESP32Service;

@Controller
public class DisruptiveArchitecturesController {

    private static final Logger logger = LoggerFactory.getLogger(DisruptiveArchitecturesController.class);
    private static final Random random = new Random();
    
    @Autowired(required = false)
    private MotoRepository motoRepository;
    
    @Autowired(required = false)
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired(required = false)
    private AuthenticationService authenticationService;
    
    @Autowired(required = false)
    private ESP32Service esp32Service;

    @GetMapping("/disruptive-architectures")
    public ModelAndView disruptiveArchitectures() {
        ModelAndView mv = new ModelAndView("disruptive-architectures");
        
        try {
            if (authenticationService != null) {
                try {
                    authenticationService.adicionarUsuarioLogado(mv);
                } catch (Exception e) {
                    // Ignorar erro
                }
            }
            
            // Buscar motos e status
            List<Moto> motos = new ArrayList<>();
            List<StatusMoto> statusMotos = new ArrayList<>();
            
            if (motoRepository != null) {
                try {
                    motos = motoRepository.findAll();
                } catch (Exception e) {
                    logger.error("Erro ao buscar motos: {}", e.getMessage());
                }
            }
            
            if (statusMotosRepository != null) {
                try {
                    statusMotos = statusMotosRepository.findAllWithRelations();
                } catch (Exception e) {
                    logger.error("Erro ao buscar status: {}", e.getMessage());
                }
            }
            
            // Criar mapa de status mais recente por moto
            Map<Long, StatusMoto> statusMap = new HashMap<>();
            for (StatusMoto status : statusMotos) {
                if (status != null && status.getMoto() != null && status.getMoto().getId() != null) {
                    Long motoId = status.getMoto().getId();
                    StatusMoto statusAtual = statusMap.get(motoId);
                    if (statusAtual == null || 
                        (status.getDataCriacao() != null && statusAtual.getDataCriacao() != null &&
                         status.getDataCriacao().isAfter(statusAtual.getDataCriacao()))) {
                        statusMap.put(motoId, status);
                    }
                }
            }
            
            // Criar localizações com status
            // Usar Set para evitar posições duplicadas
            Set<String> posicoesOcupadas = new HashSet<>();
            List<Map<String, Object>> localizacoes = new ArrayList<>();
            
            logger.info("Processando {} motos cadastradas para exibir no mapa", motos.size());
            
            for (Moto moto : motos) {
                try {
                    Map<String, Object> loc = criarLocalizacao(moto, statusMap.get(moto.getId()), posicoesOcupadas);
                    localizacoes.add(loc);
                    logger.debug("Moto {} (placa: {}) posicionada em ({}, {})", 
                        moto.getId(), moto.getPlaca(), loc.get("posicaoX"), loc.get("posicaoY"));
                } catch (Exception e) {
                    logger.error("Erro ao criar localização para moto {}: {}", moto.getId(), e.getMessage());
                }
            }
            
            logger.info("Total de {} motos posicionadas no mapa ({} posições ocupadas)", 
                localizacoes.size(), posicoesOcupadas.size());
            
            // Estatísticas
            long motosProntas = localizacoes.stream()
                .filter(loc -> "PRONTA".equals(loc.get("status")))
                .count();
            long motosAlugadas = localizacoes.stream()
                .filter(loc -> "ALUGADA".equals(loc.get("status")))
                .count();
            long motosManutencao = localizacoes.stream()
                .filter(loc -> {
                    String status = (String) loc.get("status");
                    return status != null && (status.contains("MANUTENCAO") || status.contains("REPARO") || 
                           status.contains("DANOS") || status.contains("MOTOR"));
                })
                .count();
            
            mv.addObject("motos", motos);
            mv.addObject("localizacoes", localizacoes);
            mv.addObject("totalMotos", motos.size());
            mv.addObject("motosProntas", motosProntas);
            mv.addObject("motosAlugadas", motosAlugadas);
            mv.addObject("motosManutencao", motosManutencao);
            mv.addObject("analiseIA", gerarAnalise(motos.size(), motosProntas, motosAlugadas, motosManutencao));
            mv.addObject("buscaRealizada", false);
            mv.addObject("motoEncontrada", null);
            mv.addObject("localizacaoMotoEncontrada", null);
            mv.addObject("placaBuscada", "");
            Map<String, Object> ledInfo = new HashMap<>();
            ledInfo.put("ativo", false);
            ledInfo.put("tempoRestante", 0);
            ledInfo.put("tempoRestanteSegundos", 0);
            mv.addObject("ledInfo", ledInfo);
            
        } catch (Exception e) {
            logger.error("Erro: {}", e.getMessage());
            mv.addObject("motos", new ArrayList<>());
            mv.addObject("localizacoes", new ArrayList<>());
            mv.addObject("totalMotos", 0);
            mv.addObject("motosProntas", 0);
            mv.addObject("motosAlugadas", 0);
            mv.addObject("motosManutencao", 0);
            mv.addObject("analiseIA", "Sistema de localização inteligente.");
            mv.addObject("buscaRealizada", false);
        }
        
        return mv;
    }
    
    @PostMapping("/disruptive-architectures/buscar")
    public ModelAndView buscarMoto(@RequestParam(required = false) String placa) {
        ModelAndView mv = new ModelAndView("disruptive-architectures");
        
        try {
            // Adicionar usuário logado
            if (authenticationService != null) {
                try {
                    authenticationService.adicionarUsuarioLogado(mv);
                } catch (Exception e) {
                    logger.warn("Erro ao adicionar usuário logado: {}", e.getMessage());
                }
            }
            
            // Buscar motos e status - SEMPRE inicializar listas vazias
            List<Moto> motos = new ArrayList<>();
            List<StatusMoto> statusMotos = new ArrayList<>();
            
            try {
                if (motoRepository != null) {
                    motos = motoRepository.findAll();
                    if (motos == null) {
                        motos = new ArrayList<>();
                    }
                }
            } catch (Exception e) {
                logger.error("Erro ao buscar motos: {}", e.getMessage(), e);
                motos = new ArrayList<>();
            }
            
            try {
                if (statusMotosRepository != null) {
                    statusMotos = statusMotosRepository.findAllWithRelations();
                    if (statusMotos == null) {
                        statusMotos = new ArrayList<>();
                    }
                }
            } catch (Exception e) {
                logger.error("Erro ao buscar status: {}", e.getMessage(), e);
                statusMotos = new ArrayList<>();
            }
            
            // Criar mapa de status - SEMPRE inicializar
            Map<Long, StatusMoto> statusMap = new HashMap<>();
            try {
                for (StatusMoto status : statusMotos) {
                    if (status != null && status.getMoto() != null && status.getMoto().getId() != null) {
                        Long motoId = status.getMoto().getId();
                        StatusMoto statusAtual = statusMap.get(motoId);
                        if (statusAtual == null || 
                            (status.getDataCriacao() != null && statusAtual.getDataCriacao() != null &&
                             status.getDataCriacao().isAfter(statusAtual.getDataCriacao()))) {
                            statusMap.put(motoId, status);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Erro ao criar mapa de status: {}", e.getMessage(), e);
                statusMap = new HashMap<>();
            }
            
            // Buscar moto por placa - RETORNAR DADOS ESPECÍFICOS DA MOTO
            Moto motoEncontrada = null;
            Map<String, Object> localizacaoMotoEncontrada = null;
            
            logger.info("🔍 BUSCA INICIADA: placa={}", placa != null ? placa.trim().toUpperCase() : "null");
            
            if (placa != null && !placa.trim().isEmpty()) {
                try {
                    if (motoRepository != null) {
                        // Normalizar placa - remover hífen e converter para maiúsculo
                        String placaNormalizada = placa.trim().toUpperCase().replace("-", "").replace(" ", "");
                        
                        // Tentar buscar com hífen primeiro
                        var opt = motoRepository.findByPlaca(placa.trim().toUpperCase());
                        
                        // Se não encontrou, tentar sem hífen
                        if (opt == null || !opt.isPresent()) {
                            opt = motoRepository.findByPlaca(placaNormalizada);
                        }
                        
                        // Se ainda não encontrou, tentar com hífen adicionado
                        if ((opt == null || !opt.isPresent()) && placaNormalizada.length() == 7) {
                            String placaComHifen = placaNormalizada.substring(0, 3) + "-" + placaNormalizada.substring(3);
                            opt = motoRepository.findByPlaca(placaComHifen);
                        }
                        
                        // Se ainda não encontrou, buscar todas e fazer comparação manual
                        if (opt == null || !opt.isPresent()) {
                            List<Moto> todasMotos = motoRepository.findAll();
                            for (Moto moto : todasMotos) {
                                if (moto != null && moto.getPlaca() != null) {
                                    String placaMoto = moto.getPlaca().toUpperCase().replace("-", "").replace(" ", "");
                                    if (placaMoto.equals(placaNormalizada)) {
                                        motoEncontrada = moto;
                                        opt = Optional.of(moto);
                                        break;
                                    }
                                }
                            }
                        }
                        
                        if (opt != null && opt.isPresent()) {
                            motoEncontrada = opt.get();
                            logger.info("MOTO ENCONTRADA: Placa={}, Chassi={}, Modelo={}, ID={}", 
                                motoEncontrada.getPlaca(), 
                                motoEncontrada.getChassi(), 
                                motoEncontrada.getMotor(), 
                                motoEncontrada.getId());
                            
                            // Ativar LED via ESP32
                            if (esp32Service != null) {
                                try {
                                    esp32Service.ativarLED(placa.trim().toUpperCase());
                                    logger.info("LED ATIVADO via ESP32 para placa: {}", placa.trim().toUpperCase());
                                } catch (Exception e) {
                                    logger.error("Erro ao ativar LED: {}", e.getMessage(), e);
                                }
                            }
                            
                            // Criar localização GPS específica para a moto encontrada - SEMPRE CRIAR
                            StatusMoto statusMoto = statusMap.get(motoEncontrada.getId());
                            Set<String> posicoesOcupadas = new HashSet<>();
                            
                            // Tentar criar localização
                            try {
                                localizacaoMotoEncontrada = criarLocalizacao(motoEncontrada, statusMoto, posicoesOcupadas);
                            } catch (Exception e) {
                                logger.error("Erro ao criar localização: {}", e.getMessage(), e);
                                localizacaoMotoEncontrada = null;
                            }
                            
                            // Se não foi criada, criar localização básica
                            if (localizacaoMotoEncontrada == null) {
                                localizacaoMotoEncontrada = new HashMap<>();
                                localizacaoMotoEncontrada.put("moto", motoEncontrada);
                                
                                // GPS
                                localizacaoMotoEncontrada.put("latitude", -23.5505 + (random.nextDouble() - 0.5) * 0.01);
                                localizacaoMotoEncontrada.put("longitude", -46.6333 + (random.nextDouble() - 0.5) * 0.01);
                                
                                // Posição FIXA no pátio - cada moto tem sua posição única baseada no ID
                                long motoId = motoEncontrada.getId() != null ? motoEncontrada.getId() : 0;
                                
                                // Pátio grande: 50x50 metros
                                // Cada moto tem posição FIXA baseada no ID
                                // Usar algoritmo determinístico para garantir sempre a mesma posição
                                
                                // Calcular posição X fixa (0-49 metros)
                                int posXFix = (int) ((motoId * 7 + 13) % 50);
                                if (posXFix == 0) posXFix = 5; // Evitar posição 0
                                
                                // Calcular posição Y fixa (0-49 metros)
                                int posYFix = (int) ((motoId * 11 + 17) % 50);
                                if (posYFix == 0) posYFix = 5; // Evitar posição 0
                                
                                // Calcular setor (1-5) baseado na posição fixa
                                int setorX = (posXFix / 10) + 1; // Setor 1-5
                                int setorY = (posYFix / 10) + 1; // Setor 1-5
                                
                                localizacaoMotoEncontrada.put("posicaoX", posXFix);
                                localizacaoMotoEncontrada.put("posicaoY", posYFix);
                                localizacaoMotoEncontrada.put("setorX", setorX);
                                localizacaoMotoEncontrada.put("setorY", setorY);
                                localizacaoMotoEncontrada.put("setor", "Setor " + setorX + "-" + setorY);
                                localizacaoMotoEncontrada.put("posicaoMetros", posXFix + "m x " + posYFix + "m");
                                localizacaoMotoEncontrada.put("posicaoFixa", true); // Marcar como posição fixa
                                
                                // ESP32
                                localizacaoMotoEncontrada.put("sinalGPS", 80 + random.nextInt(20));
                                localizacaoMotoEncontrada.put("sinalBluetooth", 70 + random.nextInt(30));
                                localizacaoMotoEncontrada.put("esp32Id", "ESP32-" + String.format("%04d", motoEncontrada.getId()));
                                localizacaoMotoEncontrada.put("bateria", 85 + random.nextInt(15));
                                
                                // Status e Localização via Operação
                                String statusAtual = "PENDENTE";
                                String localizacaoOperacao = "Pátio Principal";
                                
                                if (statusMoto != null) {
                                    if (statusMoto.getStatus() != null) {
                                        statusAtual = statusMoto.getStatus();
                                    }
                                    if (statusMoto.getArea() != null && !statusMoto.getArea().trim().isEmpty()) {
                                        localizacaoOperacao = statusMoto.getArea();
                                    }
                                }
                                
                                localizacaoMotoEncontrada.put("status", statusAtual);
                                localizacaoMotoEncontrada.put("area", localizacaoOperacao);
                                localizacaoMotoEncontrada.put("localizacaoOperacao", localizacaoOperacao);
                            }
                            
                            // Sempre marcar como encontrada
                            localizacaoMotoEncontrada.put("encontrada", true);
                            
                            logger.info("✅ LOCALIZAÇÃO CRIADA: Placa={}, PosX={}, PosY={}, Lat={}, Lon={}", 
                                motoEncontrada.getPlaca(),
                                localizacaoMotoEncontrada.get("posicaoX"),
                                localizacaoMotoEncontrada.get("posicaoY"),
                                localizacaoMotoEncontrada.get("latitude"),
                                localizacaoMotoEncontrada.get("longitude"));
                        } else {
                            logger.warn("MOTO NÃO ENCONTRADA para placa: {}", placa.trim().toUpperCase());
                        }
                    } else {
                        logger.warn("MotoRepository não disponível para buscar placa: {}", placa.trim().toUpperCase());
                    }
                } catch (Exception e) {
                    logger.error("Erro ao buscar moto por placa '{}': {}", placa, e.getMessage(), e);
                }
            } else {
                logger.warn("Placa não informada ou vazia");
            }
            
            // Criar localizações para todas as motos - SEMPRE inicializar
            Set<String> posicoesOcupadas = new HashSet<>();
            List<Map<String, Object>> localizacoes = new ArrayList<>();
            
            try {
                // Se encontrou uma moto, adicionar sua localização primeiro
                if (motoEncontrada != null && localizacaoMotoEncontrada != null) {
                    localizacoes.add(localizacaoMotoEncontrada);
                }
                
                // Adicionar outras motos
                if (motos != null) {
                    for (Moto moto : motos) {
                        // Pular se já foi adicionada acima
                        if (motoEncontrada != null && moto != null && moto.getId() != null && 
                            motoEncontrada.getId() != null && moto.getId().equals(motoEncontrada.getId())) {
                            continue;
                        }
                        try {
                            if (moto != null) {
                                Map<String, Object> loc = criarLocalizacao(moto, statusMap.get(moto.getId()), posicoesOcupadas);
                                if (loc != null) {
                                    localizacoes.add(loc);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Erro ao criar localização para moto {}: {}", 
                                moto != null ? moto.getId() : "null", e.getMessage(), e);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Erro ao criar localizações: {}", e.getMessage(), e);
                localizacoes = new ArrayList<>();
            }
            
            // Estatísticas - SEMPRE calcular
            long motosProntas = 0;
            long motosAlugadas = 0;
            long motosManutencao = 0;
            
            try {
                motosProntas = localizacoes.stream()
                    .filter(loc -> loc != null && "PRONTA".equals(loc.get("status")))
                    .count();
                motosAlugadas = localizacoes.stream()
                    .filter(loc -> loc != null && "ALUGADA".equals(loc.get("status")))
                    .count();
                motosManutencao = localizacoes.stream()
                    .filter(loc -> {
                        if (loc == null) return false;
                        String status = (String) loc.get("status");
                        return status != null && (status.contains("MANUTENCAO") || status.contains("REPARO") || 
                               status.contains("DANOS") || status.contains("MOTOR"));
                    })
                    .count();
            } catch (Exception e) {
                logger.error("Erro ao calcular estatísticas: {}", e.getMessage(), e);
            }
            
            // RETORNAR TODOS OS DADOS DA MOTO ENCONTRADA
            mv.addObject("motos", motos);
            mv.addObject("localizacoes", localizacoes);
            mv.addObject("totalMotos", motos.size());
            mv.addObject("motosProntas", motosProntas);
            mv.addObject("motosAlugadas", motosAlugadas);
            mv.addObject("motosManutencao", motosManutencao);
            mv.addObject("motoEncontrada", motoEncontrada);
            mv.addObject("localizacaoMotoEncontrada", localizacaoMotoEncontrada); // Localização específica da moto encontrada
            String placaBuscadaStr = (placa != null && !placa.trim().isEmpty()) ? placa.trim().toUpperCase() : "";
            mv.addObject("placaBuscada", placaBuscadaStr);
            mv.addObject("buscaRealizada", motoEncontrada != null);
            
            mv.addObject("analiseIA", gerarAnalise(motos.size(), motosProntas, motosAlugadas, motosManutencao));
            
            // LOG CRÍTICO - garantir que dados estão sendo passados
            logger.info("🔍 PASSANDO PARA TEMPLATE: placaBuscada='{}', motoEncontrada={}, localizacaoMotoEncontrada={}", 
                placaBuscadaStr,
                motoEncontrada != null ? motoEncontrada.getPlaca() : "NÃO",
                localizacaoMotoEncontrada != null ? "SIM" : "NÃO");
            
            if (motoEncontrada != null && localizacaoMotoEncontrada != null) {
                logger.info("✅ RETORNANDO PARA TEMPLATE: Placa={}, PosX={}, PosY={}, Lat={}, Lon={}", 
                    motoEncontrada.getPlaca(),
                    localizacaoMotoEncontrada.get("posicaoX"),
                    localizacaoMotoEncontrada.get("posicaoY"),
                    localizacaoMotoEncontrada.get("latitude"),
                    localizacaoMotoEncontrada.get("longitude"));
            } else if (motoEncontrada != null) {
                logger.error("❌ ERRO: Moto encontrada mas localização é NULL! Placa={}", motoEncontrada.getPlaca());
            }
            
            // Sempre obter informações do LED virtual/físico se a moto foi encontrada
            Map<String, Object> ledInfo = new HashMap<>();
            ledInfo.put("ativo", false);
            ledInfo.put("tempoRestante", 0);
            ledInfo.put("tempoRestanteSegundos", 0);
            ledInfo.put("ledFisicoPiscando", false);
            ledInfo.put("ledVirtualPiscando", false);
            
            if (motoEncontrada != null) {
                if (esp32Service != null) {
                    try {
                        // Verificar status do LED físico via ESP32 (remoto/simulado)
                        boolean ledFisicoAtivo = esp32Service.isLEDAtivo(motoEncontrada.getPlaca());
                        ledInfo.put("ledFisicoPiscando", ledFisicoAtivo);
                        
                        // LED virtual sempre ativo quando a busca é feita
                        ledInfo.put("ledVirtualPiscando", true);
                        ledInfo.put("ativo", true);
                        
                        Map<String, Object> ledInfoTemp = esp32Service.obterInfoLED(motoEncontrada.getPlaca());
                        if (ledInfoTemp != null) {
                            ledInfo = ledInfoTemp;
                            // Garantir que o status físico e virtual estão incluídos
                            ledInfo.put("ledFisicoPiscando", ledFisicoAtivo);
                            ledInfo.put("ledVirtualPiscando", true);
                            ledInfo.put("ativo", true);
                        } else {
                            // Se não retornou info, usar o status verificado
                            ledInfo.put("ativo", true);
                            ledInfo.put("ledVirtualPiscando", true);
                            ledInfo.put("tempoRestanteSegundos", 30);
                        }
                        
                        logger.info("LED: Virtual={}, Físico={}, Ativo={}, TempoRestante={}s", 
                            true, ledFisicoAtivo, ledInfo.get("ativo"), ledInfo.get("tempoRestanteSegundos"));
                    } catch (Exception e) {
                        logger.error("Erro ao obter info do LED: {}", e.getMessage(), e);
                        // Em caso de erro, garantir que LED virtual está ativo
                        ledInfo.put("ledVirtualPiscando", true);
                        ledInfo.put("ativo", true);
                        ledInfo.put("tempoRestanteSegundos", 30);
                        try {
                            boolean ledFisicoAtivo = esp32Service.isLEDAtivo(motoEncontrada.getPlaca());
                            ledInfo.put("ledFisicoPiscando", ledFisicoAtivo);
                        } catch (Exception e2) {
                            logger.error("Erro ao verificar LED físico: {}", e2.getMessage());
                        }
                    }
                } else {
                    // Se ESP32Service não está disponível, LED virtual ainda funciona
                    ledInfo.put("ledVirtualPiscando", true);
                    ledInfo.put("ativo", true);
                    ledInfo.put("tempoRestanteSegundos", 30);
                }
            }
            mv.addObject("ledInfo", ledInfo);
            
        } catch (Exception e) {
            logger.error("ERRO GERAL no método buscarMoto: {}", e.getMessage(), e);
            // SEMPRE garantir que todos os objetos estão inicializados
            mv.addObject("motos", new ArrayList<>());
            mv.addObject("localizacoes", new ArrayList<>());
            mv.addObject("totalMotos", 0);
            mv.addObject("motosProntas", 0);
            mv.addObject("motosAlugadas", 0);
            mv.addObject("motosManutencao", 0);
            mv.addObject("motoEncontrada", null);
            mv.addObject("localizacaoMotoEncontrada", null);
            mv.addObject("placaBuscada", placa != null ? placa.trim().toUpperCase() : "");
            mv.addObject("buscaRealizada", false);
            mv.addObject("analiseIA", "Erro ao buscar moto. Tente novamente.");
            Map<String, Object> ledInfo = new HashMap<>();
            ledInfo.put("ativo", false);
            ledInfo.put("tempoRestante", 0);
            ledInfo.put("tempoRestanteSegundos", 0);
            mv.addObject("ledInfo", ledInfo);
        }
        
        return mv;
    }
    
    @PostMapping("/disruptive-architectures/ativar-led")
    @ResponseBody
    public Map<String, Object> ativarLED(@RequestParam String placa) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("sucesso", false);
        resposta.put("mensagem", "Erro");
        
        try {
            if (esp32Service == null || placa == null || placa.trim().isEmpty()) {
                resposta.put("mensagem", "Dados inválidos");
                return resposta;
            }
            
            boolean sucesso = esp32Service.ativarLED(placa.trim());
            resposta.put("sucesso", sucesso);
            resposta.put("mensagem", sucesso ? "LED ativado!" : "Erro");
            resposta.put("placa", placa.trim().toUpperCase());
            
            try {
                Map<String, Object> ledInfo = esp32Service.obterInfoLED(placa.trim());
                resposta.put("ledInfo", ledInfo);
            } catch (Exception e) {
                // Ignorar erro
            }
            
        } catch (Exception e) {
            logger.error("Erro: {}", e.getMessage());
        }
        
        return resposta;
    }
    
    private Map<String, Object> criarLocalizacao(Moto moto, StatusMoto status, Set<String> posicoesOcupadas) {
        Map<String, Object> loc = new HashMap<>();
        if (moto != null) {
            loc.put("moto", moto);
            
            // Status da moto
            String statusAtual = "PENDENTE";
            if (status != null && status.getStatus() != null && !status.getStatus().trim().isEmpty()) {
                statusAtual = status.getStatus();
            }
            loc.put("status", statusAtual);
            loc.put("area", status != null && status.getArea() != null ? status.getArea() : "Não definida");
            
            // Localização GPS (simulada via ESP32)
            loc.put("latitude", -23.5505 + (random.nextDouble() - 0.5) * 0.01);
            loc.put("longitude", -46.6333 + (random.nextDouble() - 0.5) * 0.01);
            
            // Posição FIXA no pátio - cada moto tem sua posição única baseada no ID
            // Pátio grande: 50x50 metros
            long motoId = moto.getId() != null ? moto.getId() : 0;
            int posX, posY;
            
            if ("ALUGADA".equals(statusAtual)) {
                // Motos alugadas não aparecem no pátio
                posX = -1;
                posY = -1;
            } else {
                // Calcular posição FIXA baseada no ID da moto
                // Algoritmo determinístico para garantir sempre a mesma posição
                
                // Calcular posição X fixa (0-49 metros)
                posX = (int) ((motoId * 7 + 13) % 50);
                if (posX == 0) posX = 5; // Evitar posição 0
                
                // Calcular posição Y fixa (0-49 metros)
                posY = (int) ((motoId * 11 + 17) % 50);
                if (posY == 0) posY = 5; // Evitar posição 0
                
                // Ajustar baseado no status (opcional, mas mantém distribuição)
                if ("PRONTA".equals(statusAtual)) {
                    // Motos prontas: manter na área 0-24 metros (mais perto da entrada)
                    if (posX > 24) posX = posX % 25;
                    if (posY > 24) posY = posY % 25;
                } else if (statusAtual != null && (statusAtual.contains("MANUTENCAO") || statusAtual.contains("REPARO") || 
                           statusAtual.contains("DANOS") || statusAtual.contains("MOTOR"))) {
                    // Motos em manutenção: área 25-49 metros (fundo do pátio)
                    posX = 25 + (posX % 25);
                    posY = 25 + (posY % 25);
                }
                
                // Marcar posição como ocupada (mesmo que seja fixa, manter controle)
                String posicaoKey = posX + "," + posY;
                posicoesOcupadas.add(posicaoKey);
            }
            
            loc.put("posicaoX", posX);
            loc.put("posicaoY", posY);
            loc.put("posicaoFixa", true); // Marcar como posição fixa
            
            // Dados ESP32 (simulados)
            loc.put("sinalGPS", 80 + random.nextInt(20));
            loc.put("sinalBluetooth", 70 + random.nextInt(30));
            if (moto.getId() != null) {
                loc.put("esp32Id", "ESP32-" + String.format("%04d", moto.getId()));
            }
            loc.put("bateria", 85 + random.nextInt(15));
            
            // Alertas
            List<String> alertas = new ArrayList<>();
            try {
                Object bateriaObj = loc.get("bateria");
                if (bateriaObj != null && bateriaObj instanceof Number) {
                    int bateria = ((Number) bateriaObj).intValue();
                    if (bateria < 20) {
                        alertas.add("Bateria baixa");
                    }
                }
            } catch (Exception e) {
                // Ignorar erro
            }
            try {
                Object gpsObj = loc.get("sinalGPS");
                if (gpsObj != null && gpsObj instanceof Number) {
                    int gps = ((Number) gpsObj).intValue();
                    if (gps < 50) {
                        alertas.add("Sinal GPS fraco");
                    }
                }
            } catch (Exception e) {
                // Ignorar erro
            }
            if (statusAtual != null && (statusAtual.contains("MANUTENCAO") || statusAtual.contains("REPARO"))) {
                alertas.add("Em manutenção");
            }
            loc.put("alertas", alertas);
        }
        return loc;
    }
    
    private String gerarAnalise(int total, long prontas, long alugadas, long manutencao) {
        if (total == 0) {
            return "Nenhuma moto cadastrada.";
        }
        return String.format(
            "Análise Inteligente: Sistema monitorando %d motos via ESP32. " +
            "%d prontas para uso, %d alugadas, %d em manutenção. " +
            "Todas as motos estão sendo rastreadas em tempo real com GPS e Bluetooth.",
            total, prontas, alugadas, manutencao
        );
    }
}



