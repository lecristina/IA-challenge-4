package br.com.fiap.universidade_fiap.control;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.service.AuthenticationService;

/**
 * Controller para gerenciar Status de Motos
 * Extraído do MotoController para seguir Single Responsibility Principle
 */
@Controller
public class StatusMotoController {

    private static final Logger logger = LoggerFactory.getLogger(StatusMotoController.class);
    
    @Autowired
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    private static final String[] STATUS_DISPONIVEIS = {
        "PENDENTE", "REPARO_SIMPLES", "DANOS_ESTRUTURAIS", "MOTOR_DEFEITUOSO", 
        "MANUTENCAO_AGENDADA", "PRONTA", "SEM_PLACA", "ALUGADA", "AGUARDANDO_ALUGUEL"
    };

    @GetMapping("/motos/status/novo")
    public ModelAndView novoStatusMoto() {
        logger.debug("Novo cadastro de status de moto");
        try {
            ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
            authenticationService.adicionarUsuarioLogado(mv);
            
            mv.addObject("statusMoto", new StatusMoto());
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("status", STATUS_DISPONIVEIS);
            mv.addObject("editando", false);
            
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao criar novo status de moto: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/motos/status/salvar")
    public ModelAndView salvarStatusMoto(StatusMoto statusMoto) {
        logger.info("Salvando status de moto: motoId={}, status={}", 
            statusMoto.getMoto() != null ? statusMoto.getMoto().getId() : null, 
            statusMoto.getStatus());
        
        try {
            Usuario usuario = authenticationService.getUsuarioLogado();
            
            if (usuario == null) {
                logger.warn("Tentativa de salvar status sem autenticação");
                return criarModelViewComErro(statusMoto, "Usuário não autenticado", false);
            }
            
            // Buscar a moto pelo ID
            if (statusMoto.getMoto() != null && statusMoto.getMoto().getId() != null) {
                Long motoId = statusMoto.getMoto().getId();
                
                motoRepository.findById(motoId).ifPresent(moto -> {
                    logger.debug("Moto encontrada: placa={}", moto.getPlaca());
                    statusMoto.setMoto(moto);
                    statusMoto.setUsuario(usuario);
                    statusMoto.setDataCriacao(LocalDateTime.now());
                    
                    StatusMoto statusSalvo = statusMotosRepository.save(statusMoto);
                    logger.info("Status salvo com sucesso: id={}", statusSalvo.getId());
                });
            } else {
                logger.warn("ID da moto não fornecido ao salvar status");
            }
            
            return new ModelAndView("redirect:/motos?sucesso=true");
        } catch (Exception e) {
            logger.error("Erro ao salvar status de moto: {}", e.getMessage(), e);
            return criarModelViewComErro(statusMoto, "Erro ao salvar status: " + e.getMessage(), false);
        }
    }

    @GetMapping("/motos/status/editar/{id}")
    public ModelAndView editarStatusMoto(@PathVariable Long id) {
        logger.debug("Editando status de moto: id={}", id);
        
        try {
            ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
            authenticationService.adicionarUsuarioLogado(mv);
            
            statusMotosRepository.findById(id).ifPresent(statusMoto -> {
                logger.debug("Status encontrado: status={}", statusMoto.getStatus());
                mv.addObject("statusMoto", statusMoto);
                mv.addObject("editando", true);
            });
            
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("status", STATUS_DISPONIVEIS);
            
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao editar status de moto: id={}, erro={}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/motos/status/atualizar")
    public ModelAndView atualizarStatusMoto(StatusMoto statusMoto) {
        logger.info("Atualizando status de moto: motoId={}, status={}", 
            statusMoto.getMoto() != null ? statusMoto.getMoto().getId() : null, 
            statusMoto.getStatus());
        
        try {
            Usuario usuario = authenticationService.getUsuarioLogado();
            
            if (usuario == null) {
                logger.warn("Tentativa de atualizar status sem autenticação");
                return criarModelViewComErro(statusMoto, "Usuário não autenticado", true);
            }
            
            // Buscar a moto pelo ID
            if (statusMoto.getMoto() != null && statusMoto.getMoto().getId() != null) {
                Long motoId = statusMoto.getMoto().getId();
                
                motoRepository.findById(motoId).ifPresent(moto -> {
                    logger.debug("Moto encontrada: placa={}", moto.getPlaca());
                    statusMoto.setMoto(moto);
                    statusMoto.setUsuario(usuario);
                    statusMoto.setDataCriacao(LocalDateTime.now());
                    
                    StatusMoto statusAtualizado = statusMotosRepository.save(statusMoto);
                    logger.info("Status atualizado com sucesso: id={}", statusAtualizado.getId());
                });
            } else {
                logger.warn("ID da moto não fornecido ao atualizar status");
            }
            
            return new ModelAndView("redirect:/motos/operacoes?sucesso=true");
        } catch (Exception e) {
            logger.error("Erro ao atualizar status de moto: {}", e.getMessage(), e);
            return criarModelViewComErro(statusMoto, "Erro ao atualizar status: " + e.getMessage(), true);
        }
    }

    @GetMapping("/motos/status/excluir/{id}")
    public ModelAndView excluirStatusMoto(@PathVariable Long id) {
        logger.info("Excluindo status de moto: id={}", id);
        statusMotosRepository.deleteById(id);
        return new ModelAndView("redirect:/motos/operacoes");
    }

    @GetMapping("/motos/operacoes")
    public ModelAndView listarStatusMotos() {
        logger.debug("Listando status das motos");
        try {
            if (!authenticationService.isAuthenticated()) {
                logger.warn("Usuário não autenticado tentando listar status");
                return new ModelAndView("redirect:/login");
            }
            
            ModelAndView mv = new ModelAndView("/motos/operacoes");
            authenticationService.adicionarUsuarioLogado(mv);
            
            // Buscar status das motos
            List<StatusMoto> statusMotos = statusMotosRepository.findAll();
            logger.debug("Total de status encontrados: {}", statusMotos != null ? statusMotos.size() : 0);
            
            // Se não há dados, criar uma lista vazia
            if (statusMotos == null) {
                statusMotos = new java.util.ArrayList<>();
            }
            
            mv.addObject("statusMotos", statusMotos);
            
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao listar status das motos: {}", e.getMessage(), e);
            
            ModelAndView mv = new ModelAndView("/motos/operacoes");
            mv.addObject("statusMotos", new java.util.ArrayList<>());
            mv.addObject("erro", "Erro ao carregar status das motos: " + e.getMessage());
            authenticationService.adicionarUsuarioLogado(mv);
            
            return mv;
        }
    }
    
    /**
     * Método auxiliar para criar ModelAndView com erro
     * Aplica o princípio DRY (Don't Repeat Yourself)
     */
    private ModelAndView criarModelViewComErro(StatusMoto statusMoto, String mensagemErro, boolean editando) {
        ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
        mv.addObject("statusMoto", statusMoto != null ? statusMoto : new StatusMoto());
        mv.addObject("motos", motoRepository.findAll());
        mv.addObject("status", STATUS_DISPONIVEIS);
        mv.addObject("editando", editando);
        mv.addObject("erro", mensagemErro);
        authenticationService.adicionarUsuarioLogado(mv);
        return mv;
    }
}


