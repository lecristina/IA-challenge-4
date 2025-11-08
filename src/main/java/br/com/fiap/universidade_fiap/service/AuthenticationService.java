package br.com.fiap.universidade_fiap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;

/**
 * Serviço para gerenciar autenticação e usuário logado
 * Evita duplicação de código em todos os controllers
 */
@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtém o usuário autenticado
     * @return Usuario logado ou null se não autenticado
     */
    public Usuario getUsuarioLogado() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                return usuarioRepository.findByEmail(auth.getName()).orElse(null);
            }
        } catch (Exception e) {
            logger.warn("Erro ao obter usuário logado: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Adiciona o usuário logado ao ModelAndView
     * @param mv ModelAndView para adicionar o usuário
     */
    public void adicionarUsuarioLogado(ModelAndView mv) {
        Usuario usuario = getUsuarioLogado();
        if (usuario != null) {
            mv.addObject("usuario_logado", usuario);
        }
    }

    /**
     * Verifica se o usuário está autenticado
     * @return true se autenticado, false caso contrário
     */
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser");
    }

    /**
     * Obtém o email do usuário autenticado
     * @return Email do usuário ou null
     */
    public String getEmailUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
            return auth.getName();
        }
        return null;
    }
}




