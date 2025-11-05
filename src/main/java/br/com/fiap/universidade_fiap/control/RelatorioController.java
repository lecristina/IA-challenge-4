package br.com.fiap.universidade_fiap.control;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Operacao;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.OperacaoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;
import br.com.fiap.universidade_fiap.service.AuthenticationService;

@Controller
public class RelatorioController {

    private static final Logger logger = LoggerFactory.getLogger(RelatorioController.class);
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired
    private OperacaoRepository operacaoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/relatorios")
    public ModelAndView relatorios() {
        ModelAndView mv = new ModelAndView("/relatorios/lista");
        authenticationService.adicionarUsuarioLogado(mv);
        return mv;
    }

    @GetMapping("/relatorios/motos")
    public ModelAndView relatorioMotos() {
        logger.debug("Gerando relatório de motos");
        try {
            if (!authenticationService.isAuthenticated()) {
                logger.warn("Usuário não autenticado tentando acessar relatório");
                return new ModelAndView("redirect:/login");
            }
            
            ModelAndView mv = new ModelAndView("/relatorios/motos");
            authenticationService.adicionarUsuarioLogado(mv);
            
            List<Moto> motos = motoRepository.findAll();
            logger.debug("Total de motos encontradas: {}", motos.size());
            mv.addObject("motos", motos);
            
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório de motos: {}", e.getMessage(), e);
            ModelAndView mv = new ModelAndView("error/500");
            mv.addObject("error", "Erro interno: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/relatorios/status")
    public ModelAndView relatorioStatus() {
        logger.debug("Gerando relatório de status");
        try {
            if (!authenticationService.isAuthenticated()) {
                logger.warn("Usuário não autenticado tentando acessar relatório");
                return new ModelAndView("redirect:/login");
            }
            
            ModelAndView mv = new ModelAndView("/relatorios/status");
            authenticationService.adicionarUsuarioLogado(mv);
            
            List<StatusMoto> statusMotos = statusMotosRepository.findAll();
            logger.debug("Total de status encontrados: {}", statusMotos.size());
            
            // Se não há dados, criar uma lista vazia
            if (statusMotos == null || statusMotos.isEmpty()) {
                statusMotos = new java.util.ArrayList<>();
            }
            
            mv.addObject("statusMotos", statusMotos);
            
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório de status: {}", e.getMessage(), e);
            ModelAndView mv = new ModelAndView("error/500");
            mv.addObject("error", "Erro interno: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/relatorios/operacoes")
    public ModelAndView relatorioOperacoes() {
        logger.debug("Gerando relatório de operações");
        ModelAndView mv = new ModelAndView("/relatorios/operacoes");
        authenticationService.adicionarUsuarioLogado(mv);
        
        List<Operacao> operacoes = operacaoRepository.findAll();
        logger.debug("Total de operações encontradas: {}", operacoes.size());
        mv.addObject("operacoes", operacoes);
        
        return mv;
    }

}
