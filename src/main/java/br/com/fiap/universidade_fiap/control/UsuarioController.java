package br.com.fiap.universidade_fiap.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

// EnumPerfil removido - usando String agora
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;
import br.com.fiap.universidade_fiap.service.AuthenticationService;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
	
	private final PasswordEncoder encoder;
	private final UsuarioRepository repU;
	private final AuthenticationService authenticationService;

	public UsuarioController(PasswordEncoder encoder, UsuarioRepository repU, AuthenticationService authenticationService) {
		this.encoder = encoder;
		this.repU = repU;
		this.authenticationService = authenticationService;
	}

	@GetMapping("/usuario/novo")
	public ModelAndView retornarCadUsuario() {
		ModelAndView mv = new ModelAndView("/usuario/novo");
		authenticationService.adicionarUsuarioLogado(mv);

		mv.addObject("usuario", new Usuario());
		mv.addObject("perfis", new String[]{"ADMIN", "GERENTE", "OPERADOR"});
		return mv;
	}

    @PostMapping("/insere_usuario")
    public ModelAndView inserirUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        logger.info("Cadastrando usuário: email={}, nomeFilial={}", usuario.getEmail(), usuario.getNomeFilial());

        try {
            authenticationService.adicionarUsuarioLogado(mv);

            // Verificar erros de validação
            if (bindingResult.hasErrors()) {
                logger.warn("Erros de validação ao cadastrar usuário: {}", bindingResult.getAllErrors());
                mv.setViewName("/usuario/novo");
                mv.addObject("usuario", usuario);
                mv.addObject("perfis", new String[]{"ADMIN", "GERENTE", "OPERADOR"});
                mv.addObject("erro", "Por favor, corrija os erros no formulário.");
                return mv;
            }

            // Normalizar CNPJ (remover caracteres especiais para comparação)
            String cnpjLimpo = usuario.getCnpj().replaceAll("[^0-9]", "");
            
            // Verificar se email já existe
            if (repU.findByEmail(usuario.getEmail()).isPresent()) {
                logger.warn("Tentativa de cadastrar email já existente: {}", usuario.getEmail());
                return criarModelViewComErro(mv, usuario, "E-mail já cadastrado no sistema!");
            }

            // Verificar se CNPJ já existe (comparar apenas números)
            boolean cnpjExiste = repU.findAll().stream()
                    .anyMatch(u -> u.getCnpj().replaceAll("[^0-9]", "").equals(cnpjLimpo));
            
            if (cnpjExiste) {
                logger.warn("Tentativa de cadastrar CNPJ já existente: {}", usuario.getCnpj());
                return criarModelViewComErro(mv, usuario, "CNPJ já cadastrado no sistema!");
            }

            // Validar senha
            if (usuario.getSenhaHash() == null || usuario.getSenhaHash().trim().isEmpty()) {
                logger.warn("Tentativa de cadastrar usuário sem senha");
                return criarModelViewComErro(mv, usuario, "Senha é obrigatória!");
            }

            // Criptografar senha e salvar usuário
            usuario.setSenhaHash(encoder.encode(usuario.getSenhaHash()));
            Usuario usuarioSalvo = repU.save(usuario);
            logger.info("Usuário cadastrado com sucesso: id={}, email={}", usuarioSalvo.getId(), usuarioSalvo.getEmail());

            mv.setViewName("redirect:/usuario/novo?sucesso=true");
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao cadastrar usuário: {}", e.getMessage(), e);
            return criarModelViewComErro(mv, usuario, "Erro interno do servidor: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/lista")
    public ModelAndView listarUsuarios() {
        logger.debug("Listando usuários");
        ModelAndView mv = new ModelAndView("/usuario/lista");
        try {
            authenticationService.adicionarUsuarioLogado(mv);
            
            List<Usuario> usuarios = repU.findAll();
            logger.debug("Total de usuários encontrados: {}", usuarios.size());
            mv.addObject("usuarios", usuarios);
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao listar usuários: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/usuario/excluir/{id}")
    public ModelAndView excluirUsuario(@PathVariable Long id) {
        logger.info("Excluindo usuário: id={}", id);
        
        try {
            // Verificar se o usuário existe
            Usuario usuario = repU.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
            
            logger.debug("Usuário encontrado: email={}", usuario.getEmail());
            
            // Excluir o usuário
            repU.deleteById(id);
            logger.info("Usuário excluído com sucesso: id={}", id);
            
            return new ModelAndView("redirect:/usuario/lista?sucesso=true");
        } catch (Exception e) {
            logger.error("Erro ao excluir usuário: id={}, erro={}", id, e.getMessage(), e);
            return new ModelAndView("redirect:/usuario/lista?erro=" + e.getMessage());
        }
    }

    /**
     * Método auxiliar para criar ModelAndView com erro
     * Aplica o princípio DRY (Don't Repeat Yourself)
     */
    private ModelAndView criarModelViewComErro(ModelAndView mv, Usuario usuario, String mensagemErro) {
        mv.setViewName("/usuario/novo");
        mv.addObject("erro", mensagemErro);
        mv.addObject("usuario", usuario);
        mv.addObject("perfis", new String[]{"ADMIN", "GERENTE", "OPERADOR"});
        return mv;
    }

}
