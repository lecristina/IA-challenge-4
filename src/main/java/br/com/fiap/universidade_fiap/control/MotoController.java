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

// EnumStatus removido - usando String agora
import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.service.AuthenticationService;
import br.com.fiap.universidade_fiap.service.MotoService;

/**
 * Controller para gerenciar Motos
 * Segue Single Responsibility Principle - apenas gerencia motos
 * Status de motos foi movido para StatusMotoController
 */
@Controller
public class MotoController {

    private static final Logger logger = LoggerFactory.getLogger(MotoController.class);
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private MotoService motoService;
    

    @GetMapping("/teste")
    public String teste() {
        return "teste";
    }

    @GetMapping("/motos")
    public ModelAndView listarMotos() {
        ModelAndView mv = new ModelAndView("/motos/lista");
        
        authenticationService.adicionarUsuarioLogado(mv);
        
        List<Moto> motos = motoRepository.findAll();
        mv.addObject("motos", motos);
        
        return mv;
    }

    @GetMapping("/motos/novo")
    public ModelAndView novoCadastroMoto() {
        logger.debug("Novo cadastro de moto");
        try {
            if (!authenticationService.isAuthenticated()) {
                logger.warn("Usuário não autenticado tentando cadastrar moto");
                return new ModelAndView("redirect:/login");
            }
            
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            authenticationService.adicionarUsuarioLogado(mv);
            
            mv.addObject("moto", new Moto());
            mv.addObject("editando", false);
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao criar novo cadastro de moto: {}", e.getMessage(), e);
            ModelAndView mv = new ModelAndView("error/500");
            mv.addObject("error", "Erro interno: " + e.getMessage());
            return mv;
        }
    }

    @PostMapping("/motos/salvar")
    public ModelAndView salvarMoto(Moto moto) {
        logger.info("Salvando moto: placa={}, chassi={}", moto.getPlaca(), moto.getChassi());
        
        try {
            // Obter usuário logado
            Usuario usuarioLogado = authenticationService.getUsuarioLogado();

            // Validações básicas
            if (moto.getPlaca() == null || moto.getPlaca().trim().isEmpty()) {
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Placa é obrigatória!");
                if (usuarioLogado != null) {
                    mv.addObject("usuario_logado", usuarioLogado);
                }
                return mv;
            }

            if (moto.getChassi() == null || moto.getChassi().trim().isEmpty()) {
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Chassi é obrigatório!");
                if (usuarioLogado != null) {
                    mv.addObject("usuario_logado", usuarioLogado);
                }
                return mv;
            }

            // Verificar se placa já existe
            if (motoService.placaExiste(moto.getPlaca())) {
                logger.warn("Tentativa de cadastrar placa já existente: {}", moto.getPlaca());
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Placa já cadastrada no sistema!");
                authenticationService.adicionarUsuarioLogado(mv);
                return mv;
            }

            // Verificar se chassi já existe
            if (motoService.chassiExiste(moto.getChassi())) {
                logger.warn("Tentativa de cadastrar chassi já existente: {}", moto.getChassi());
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Chassi já cadastrado no sistema!");
                authenticationService.adicionarUsuarioLogado(mv);
                return mv;
            }

            // Verificar autenticação
            if (usuarioLogado == null) {
                logger.warn("Tentativa de salvar moto sem autenticação");
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Usuário não autenticado");
                return mv;
            }

            // Salvar moto
            moto.setDataCriacao(LocalDateTime.now());
            motoService.salvarMoto(moto, usuarioLogado);
            
            return new ModelAndView("redirect:/motos?sucesso=true");
            
        } catch (Exception e) {
            logger.error("Erro ao salvar moto: {}", e.getMessage(), e);
            
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            mv.addObject("moto", moto);
            mv.addObject("editando", false);
            mv.addObject("erro", "Erro interno do servidor: " + e.getMessage());
            authenticationService.adicionarUsuarioLogado(mv);
            
            return mv;
        }
    }

    @GetMapping("/motos/auditoria")
    public String auditoriaMotos() {
        return "redirect:/auditoria";
    }

    @GetMapping("/motos/editar/{id}")
    public ModelAndView editarMoto(@PathVariable Long id) {
        logger.debug("Editando moto: id={}", id);
        
        try {
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            authenticationService.adicionarUsuarioLogado(mv);
            
            motoRepository.findById(id).ifPresent(moto -> {
                logger.debug("Moto encontrada: placa={}", moto.getPlaca());
                mv.addObject("moto", moto);
                mv.addObject("editando", true);
            });
            
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao editar moto: id={}, erro={}", id, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/motos/atualizar")
    public ModelAndView atualizarMoto(Moto moto) {
        logger.info("Atualizando moto: id={}, placa={}", moto.getId(), moto.getPlaca());
        
        try {
            // Verificar se a moto existe
            if (moto.getId() == null) {
                throw new RuntimeException("ID da moto não fornecido");
            }
            
            // Buscar a moto existente
            Moto motoExistente = motoRepository.findById(moto.getId())
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com ID: " + moto.getId()));
            
            logger.debug("Moto existente encontrada: placa={}", motoExistente.getPlaca());
            
            // Verificar se a placa está sendo alterada e se já existe
            if (!motoExistente.getPlaca().equals(moto.getPlaca())) {
                if (!motoService.podeAlterarPlaca(moto.getId(), moto.getPlaca())) {
                    throw new RuntimeException("Placa já cadastrada no sistema!");
                }
            }
            
            // Verificar se o chassi está sendo alterado e se já existe
            if (!motoExistente.getChassi().equals(moto.getChassi())) {
                if (!motoService.podeAlterarChassi(moto.getId(), moto.getChassi())) {
                    throw new RuntimeException("Chassi já cadastrado no sistema!");
                }
            }
            
            // Atualizar apenas os campos permitidos
            motoService.atualizarMoto(motoExistente, moto);
            
            return new ModelAndView("redirect:/motos?sucesso=true");
        } catch (Exception e) {
            logger.error("Erro ao atualizar moto: id={}, erro={}", moto.getId(), e.getMessage(), e);
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            authenticationService.adicionarUsuarioLogado(mv);
            
            mv.addObject("moto", moto);
            mv.addObject("editando", true);
            mv.addObject("erro", "Erro ao atualizar moto: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/motos/teste")
    public ModelAndView testeCriacao() {
        logger.debug("Criando moto de teste");
        try {
            Usuario usuario = authenticationService.getUsuarioLogado();
            
            if (usuario == null) {
                logger.warn("Tentativa de criar moto de teste sem autenticação");
                return new ModelAndView("redirect:/motos?erro=Usuário não autenticado");
            }
            
            // Criar uma moto de teste
            Moto motoTeste = new Moto();
            motoTeste.setPlaca("TEST123");
            motoTeste.setChassi("CHASSI123456789");
            motoTeste.setMotor("Motor Teste");
            motoTeste.setUsuario(usuario);
            motoTeste.setDataCriacao(LocalDateTime.now());
            
            try {
                Moto motoSalva = motoRepository.save(motoTeste);
                logger.info("Moto de teste criada com sucesso: id={}", motoSalva.getId());
            } catch (Exception e) {
                logger.error("Erro ao criar moto de teste: {}", e.getMessage(), e);
            }
            
            return new ModelAndView("redirect:/motos?sucesso=true");
        } catch (Exception e) {
            logger.error("Erro no teste de criação: {}", e.getMessage(), e);
            return new ModelAndView("redirect:/motos?erro=Erro no teste");
        }
    }

    @GetMapping("/motos/excluir/{id}")
    public ModelAndView excluirMoto(@PathVariable Long id) {
        logger.info("Excluindo moto: id={}", id);
        
        try {
            // Verificar se a moto existe
            Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com ID: " + id));
            
            logger.debug("Moto encontrada: placa={}", moto.getPlaca());
            
            // Excluir a moto
            // Nota: Status relacionados serão excluídos automaticamente via cascade ou manualmente via StatusMotoController
            motoRepository.deleteById(id);
            logger.info("Moto excluída com sucesso: id={}", id);
            
            return new ModelAndView("redirect:/motos?excluido=true");
        } catch (Exception e) {
            logger.error("Erro ao excluir moto: id={}, erro={}", id, e.getMessage(), e);
            return new ModelAndView("redirect:/motos?erro=Erro ao excluir moto: " + e.getMessage());
        }
    }

}
