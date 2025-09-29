package br.com.fiap.universidade_fiap.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

// EnumTipoOperacao removido - usando String agora
import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Operacao;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.OperacaoRepository;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;

@Controller
public class OperacaoController {

    @Autowired
    private OperacaoRepository operacaoRepository;
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/operacoes")
    public ModelAndView operacoes() {
        ModelAndView mv = new ModelAndView("/operacoes/lista");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        usuarioRepository.findByEmail(auth.getName())
            .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
        
        List<Operacao> operacoes = operacaoRepository.findAll();
        mv.addObject("operacoes", operacoes);
        
        return mv;
    }

    @GetMapping("/operacoes/nova")
    public ModelAndView novaOperacao() {
        try {
            ModelAndView mv = new ModelAndView("/operacoes/cadastro");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
            } else {
                return new ModelAndView("redirect:/login");
            }
            
            mv.addObject("operacao", new Operacao());
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("tiposOperacao", new String[]{"CHECK_IN", "CHECK_OUT"});
            
            return mv;
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("error/500");
            mv.addObject("error", "Erro interno: " + e.getMessage());
            return mv;
        }
    }

    @PostMapping("/operacoes/salvar")
    public ModelAndView salvarOperacao(Operacao operacao) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        // Buscar a moto pelo ID
                        if (operacao.getMoto() != null && operacao.getMoto().getId() != null) {
                            Long motoId = operacao.getMoto().getId();
                            
                            motoRepository.findById(motoId).ifPresent(moto -> {
                                operacao.setMoto(moto);
                                operacao.setUsuario(usuario);
                                
                                operacaoRepository.save(operacao);
                            });
                        }
                    });
            } else {
                return new ModelAndView("redirect:/login");
            }
            
            return new ModelAndView("redirect:/operacoes?sucesso=true");
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("/operacoes/cadastro");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
            }
            
            mv.addObject("operacao", operacao);
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("tiposOperacao", new String[]{"CHECK_IN", "CHECK_OUT"});
            mv.addObject("erro", "Erro ao salvar operação: " + e.getMessage());
            return mv;
        }
    }
}
