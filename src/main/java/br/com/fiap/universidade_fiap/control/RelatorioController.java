package br.com.fiap.universidade_fiap.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Operacao;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.OperacaoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;

@Controller
public class RelatorioController {

    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired
    private OperacaoRepository operacaoRepository;
    
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/relatorios")
    public ModelAndView relatorios() {
        ModelAndView mv = new ModelAndView("/relatorios/lista");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        usuarioRepository.findByEmail(auth.getName())
            .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
        
        return mv;
    }

    @GetMapping("/relatorios/motos")
    public ModelAndView relatorioMotos() {
        try {
            ModelAndView mv = new ModelAndView("/relatorios/motos");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
            } else {
                return new ModelAndView("redirect:/login");
            }
            
            List<Moto> motos = motoRepository.findAll();
            mv.addObject("motos", motos);
            
            return mv;
        } catch (Exception e) {
            System.out.println("ERRO no relatorioMotos: " + e.getMessage());
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("error/500");
            mv.addObject("error", "Erro interno: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/relatorios/status")
    public ModelAndView relatorioStatus() {
        try {
            System.out.println("=== RELATÓRIO DE STATUS ===");
            ModelAndView mv = new ModelAndView("/relatorios/status");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        System.out.println("Usuário logado: " + usuario.getNomeFilial());
                        mv.addObject("usuario_logado", usuario);
                    });
            } else {
                return new ModelAndView("redirect:/login");
            }
            
            System.out.println("Buscando status de motos...");
            List<StatusMoto> statusMotos = statusMotosRepository.findAll();
            System.out.println("Total de status encontrados: " + statusMotos.size());
            
            // Se não há dados, criar uma lista vazia
            if (statusMotos == null || statusMotos.isEmpty()) {
                System.out.println("Nenhum status encontrado, criando lista vazia");
                statusMotos = new java.util.ArrayList<>();
            }
            
            mv.addObject("statusMotos", statusMotos);
            
            System.out.println("ModelAndView criado com sucesso");
            return mv;
        } catch (Exception e) {
            System.out.println("ERRO no relatorioStatus: " + e.getMessage());
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("error/500");
            mv.addObject("error", "Erro interno: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/relatorios/operacoes")
    public ModelAndView relatorioOperacoes() {
        ModelAndView mv = new ModelAndView("/relatorios/operacoes");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        usuarioRepository.findByEmail(auth.getName())
            .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
        
        List<Operacao> operacoes = operacaoRepository.findAll();
        mv.addObject("operacoes", operacoes);
        
        return mv;
    }

}
