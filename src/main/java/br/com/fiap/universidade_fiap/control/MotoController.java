package br.com.fiap.universidade_fiap.control;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

// EnumStatus removido - usando String agora
import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;

@Controller
public class MotoController {

    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    

    @GetMapping("/teste")
    public String teste() {
        return "teste";
    }

    @GetMapping("/motos")
    public ModelAndView listarMotos() {
        ModelAndView mv = new ModelAndView("/motos/lista");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        usuarioRepository.findByEmail(auth.getName())
            .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
        
        List<Moto> motos = motoRepository.findAll();
        mv.addObject("motos", motos);
        
        return mv;
    }

    @GetMapping("/motos/novo")
    public ModelAndView novoCadastroMoto() {
        System.out.println("=== NOVO CADASTRO DE MOTO ===");
        try {
            System.out.println("Criando ModelAndView...");
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            
            System.out.println("Obtendo autenticação...");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + auth);
            System.out.println("Auth name: " + (auth != null ? auth.getName() : "null"));
            
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
            } else {
                return new ModelAndView("redirect:/login");
            }
            
            mv.addObject("moto", new Moto());
            mv.addObject("editando", false);
            return mv;
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("error/500");
            mv.addObject("error", "Erro interno: " + e.getMessage());
            return mv;
        }
    }

    @PostMapping("/motos/salvar")
    public ModelAndView salvarMoto(Moto moto) {
        System.out.println("=== SALVANDO MOTO ===");
        System.out.println("Moto recebida: " + moto);
        
        try {
            // Obter usuário logado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioLogado = null;
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioLogado = usuarioRepository.findByEmail(auth.getName()).orElse(null);
            }

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
            if (motoRepository.findByPlaca(moto.getPlaca()).isPresent()) {
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Placa já cadastrada no sistema!");
                if (usuarioLogado != null) {
                    mv.addObject("usuario_logado", usuarioLogado);
                }
                return mv;
            }

            // Verificar se chassi já existe
            if (motoRepository.findByChassi(moto.getChassi()).isPresent()) {
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Chassi já cadastrado no sistema!");
                if (usuarioLogado != null) {
                    mv.addObject("usuario_logado", usuarioLogado);
                }
                return mv;
            }

            // Verificar autenticação
            if (usuarioLogado == null) {
                ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
                mv.addObject("moto", moto);
                mv.addObject("editando", false);
                mv.addObject("erro", "Usuário não autenticado");
                return mv;
            }

            // Salvar moto
            moto.setUsuario(usuarioLogado);
            moto.setDataCriacao(LocalDateTime.now());
            
            System.out.println("Salvando moto: " + moto);
            Moto motoSalva = motoRepository.save(moto);
            System.out.println("Moto salva com sucesso! ID: " + motoSalva.getId());
            
            return new ModelAndView("redirect:/motos?sucesso=true");
            
        } catch (Exception e) {
            System.err.println("Erro ao salvar moto: " + e.getMessage());
            e.printStackTrace();
            
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            mv.addObject("moto", moto);
            mv.addObject("editando", false);
            mv.addObject("erro", "Erro interno do servidor: " + e.getMessage());
            
            // Adicionar usuário logado no catch também
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
            }
            
            return mv;
        }
    }

    @GetMapping("/motos/status/novo")
    public ModelAndView novoStatusMoto() {
        System.out.println("=== NOVO STATUS DE MOTO ===");
        try {
            System.out.println("Criando ModelAndView...");
            ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
            
            System.out.println("Obtendo autenticação...");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + auth);
            System.out.println("Auth name: " + (auth != null ? auth.getName() : "null"));
            
            if (auth != null && auth.getName() != null) {
                System.out.println("Buscando usuário no banco...");
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        System.out.println("Usuário encontrado: " + usuario.getNomeFilial());
                        mv.addObject("usuario_logado", usuario);
                    });
            }
            
            System.out.println("Buscando motos...");
            mv.addObject("statusMoto", new StatusMoto());
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("status", new String[]{"PENDENTE", "REPARO_SIMPLES", "DANOS_ESTRUTURAIS", "MOTOR_DEFEITUOSO", "MANUTENCAO_AGENDADA", "PRONTA", "SEM_PLACA", "ALUGADA", "AGUARDANDO_ALUGUEL"});
            mv.addObject("editando", false);
            
            System.out.println("ModelAndView criado com sucesso");
            System.out.println("Objetos no ModelAndView: " + mv.getModel());
            return mv;
        } catch (Exception e) {
            System.out.println("ERRO no novoStatusMoto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/motos/status/salvar")
    public ModelAndView salvarStatusMoto(StatusMoto statusMoto) {
        System.out.println("=== SALVANDO STATUS DE MOTO ===");
        System.out.println("StatusMoto recebido: " + statusMoto);
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Auth: " + auth);
            
            if (auth != null && auth.getName() != null) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        System.out.println("Usuário encontrado: " + usuario.getNomeFilial());
                        
                        // Buscar a moto pelo ID
                        if (statusMoto.getMoto() != null && statusMoto.getMoto().getId() != null) {
                            Long motoId = statusMoto.getMoto().getId();
                            System.out.println("Buscando moto com ID: " + motoId);
                            
                            motoRepository.findById(motoId).ifPresent(moto -> {
                                System.out.println("Moto encontrada: " + moto.getPlaca());
                                statusMoto.setMoto(moto);
                                statusMoto.setUsuario(usuario);
                                statusMoto.setDataCriacao(LocalDateTime.now());
                                
                                System.out.println("Salvando status no banco...");
                                StatusMoto statusSalvo = statusMotosRepository.save(statusMoto);
                                System.out.println("Status salvo com ID: " + statusSalvo.getId());
                            });
                        } else {
                            System.out.println("ID da moto não fornecido!");
                        }
                    });
            } else {
                System.out.println("Usuário não autenticado!");
                ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
                mv.addObject("statusMoto", new StatusMoto());
                mv.addObject("motos", motoRepository.findAll());
                mv.addObject("status", new String[]{"PENDENTE", "REPARO_SIMPLES", "DANOS_ESTRUTURAIS", "MOTOR_DEFEITUOSO", "MANUTENCAO_AGENDADA", "PRONTA", "SEM_PLACA", "ALUGADA", "AGUARDANDO_ALUGUEL"});
                mv.addObject("editando", false);
                mv.addObject("erro", "Usuário não autenticado");
                return mv;
            }
            
            return new ModelAndView("redirect:/motos?sucesso=true");
        } catch (Exception e) {
            System.out.println("ERRO ao salvar status: " + e.getMessage());
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
            mv.addObject("statusMoto", new StatusMoto());
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("status", new String[]{"PENDENTE", "REPARO_SIMPLES", "DANOS_ESTRUTURAIS", "MOTOR_DEFEITUOSO", "MANUTENCAO_AGENDADA", "PRONTA", "SEM_PLACA", "ALUGADA", "AGUARDANDO_ALUGUEL"});
            mv.addObject("editando", false);
            mv.addObject("erro", "Erro ao salvar status: " + e.getMessage());
            return mv;
        }
    }


    @GetMapping("/motos/auditoria")
    public String auditoriaMotos() {
        return "redirect:/auditoria";
    }

    @GetMapping("/motos/editar/{id}")
    public ModelAndView editarMoto(@PathVariable Long id) {
        System.out.println("=== EDITANDO MOTO ===");
        System.out.println("ID da moto: " + id);
        
        try {
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        System.out.println("Usuário encontrado: " + usuario.getNomeFilial());
                        mv.addObject("usuario_logado", usuario);
                    });
            }
            
            motoRepository.findById(id).ifPresent(moto -> {
                System.out.println("Moto encontrada: " + moto.getPlaca());
                mv.addObject("moto", moto);
                mv.addObject("editando", true);
            });
            
            System.out.println("ModelAndView criado com sucesso");
            return mv;
        } catch (Exception e) {
            System.out.println("ERRO ao editar moto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/motos/atualizar")
    public ModelAndView atualizarMoto(Moto moto) {
        System.out.println("=== ATUALIZANDO MOTO ===");
        System.out.println("Moto recebida: " + moto);
        System.out.println("ID da moto: " + moto.getId());
        
        try {
            // Verificar se a moto existe
            if (moto.getId() == null) {
                throw new RuntimeException("ID da moto não fornecido");
            }
            
            // Buscar a moto existente
            Moto motoExistente = motoRepository.findById(moto.getId())
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com ID: " + moto.getId()));
            
            System.out.println("Moto existente encontrada: " + motoExistente.getPlaca());
            
            // Verificar se a placa está sendo alterada e se já existe
            if (!motoExistente.getPlaca().equals(moto.getPlaca())) {
                if (motoRepository.findByPlaca(moto.getPlaca()).isPresent()) {
                    throw new RuntimeException("Placa já cadastrada no sistema!");
                }
            }
            
            // Verificar se o chassi está sendo alterado e se já existe
            if (!motoExistente.getChassi().equals(moto.getChassi())) {
                if (motoRepository.findByChassi(moto.getChassi()).isPresent()) {
                    throw new RuntimeException("Chassi já cadastrado no sistema!");
                }
            }
            
            // Atualizar apenas os campos permitidos
            motoExistente.setPlaca(moto.getPlaca());
            motoExistente.setChassi(moto.getChassi());
            motoExistente.setMotor(moto.getMotor());
            
            // Manter o usuário e data de criação originais
            System.out.println("Salvando moto atualizada...");
            motoRepository.save(motoExistente);
            System.out.println("Moto atualizada com sucesso!");
            
            return new ModelAndView("redirect:/motos?sucesso=true");
        } catch (Exception e) {
            System.out.println("ERRO ao atualizar moto: " + e.getMessage());
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("/motos/cadastroMotos");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
            }
            
            mv.addObject("moto", moto);
            mv.addObject("editando", true);
            mv.addObject("erro", "Erro ao atualizar moto: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/motos/teste")
    public ModelAndView testeCriacao() {
        System.out.println("=== TESTE DE CRIAÇÃO ===");
        try {
            // Criar uma moto de teste
            Moto motoTeste = new Moto();
            motoTeste.setPlaca("TEST123");
            motoTeste.setChassi("CHASSI123456789");
            motoTeste.setMotor("Motor Teste");
            
            // Obter usuário logado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        motoTeste.setUsuario(usuario);
                        motoTeste.setDataCriacao(LocalDateTime.now());
                        
                        try {
                            Moto motoSalva = motoRepository.save(motoTeste);
                            System.out.println("Moto de teste criada com sucesso! ID: " + motoSalva.getId());
                            
                            // Criar status de teste para a moto
                            StatusMoto statusTeste = new StatusMoto();
                            statusTeste.setMoto(motoSalva);
                            statusTeste.setStatus("PRONTA");
                            statusTeste.setArea("Garagem Principal");
                            statusTeste.setUsuario(usuario);
                            statusTeste.setDataCriacao(LocalDateTime.now());
                            
                            StatusMoto statusSalvo = statusMotosRepository.save(statusTeste);
                            System.out.println("Status de teste criado com sucesso! ID: " + statusSalvo.getId());
                            
                        } catch (Exception e) {
                            System.err.println("Erro ao criar moto de teste: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
            }
            
            return new ModelAndView("redirect:/motos?sucesso=true");
        } catch (Exception e) {
            System.err.println("Erro no teste: " + e.getMessage());
            e.printStackTrace();
            return new ModelAndView("redirect:/motos?erro=Erro no teste");
        }
    }

    @GetMapping("/motos/excluir/{id}")
    public ModelAndView excluirMoto(@PathVariable Long id) {
        System.out.println("=== EXCLUINDO MOTO ===");
        System.out.println("ID da moto a ser excluída: " + id);
        
        try {
            // Verificar se a moto existe
            Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com ID: " + id));
            
            System.out.println("Moto encontrada: " + moto.getPlaca());
            
            // Excluir status relacionados primeiro (se houver)
            List<StatusMoto> statusRelacionados = statusMotosRepository.findByMotoId(id);
            if (!statusRelacionados.isEmpty()) {
                System.out.println("Excluindo " + statusRelacionados.size() + " status relacionados...");
                statusMotosRepository.deleteAll(statusRelacionados);
            }
            
            // Excluir a moto
            System.out.println("Excluindo moto...");
            motoRepository.deleteById(id);
            System.out.println("Moto excluída com sucesso!");
            
            return new ModelAndView("redirect:/motos?excluido=true");
        } catch (Exception e) {
            System.out.println("ERRO ao excluir moto: " + e.getMessage());
            e.printStackTrace();
            return new ModelAndView("redirect:/motos?erro=Erro ao excluir moto: " + e.getMessage());
        }
    }

    @GetMapping("/motos/status/editar/{id}")
    public ModelAndView editarStatusMoto(@PathVariable Long id) {
        System.out.println("=== EDITANDO STATUS DE MOTO ===");
        System.out.println("ID do status: " + id);
        
        try {
            ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        System.out.println("Usuário encontrado: " + usuario.getNomeFilial());
                        mv.addObject("usuario_logado", usuario);
                    });
            }
            
            statusMotosRepository.findById(id).ifPresent(statusMoto -> {
                System.out.println("Status encontrado: " + statusMoto.getStatus());
                mv.addObject("statusMoto", statusMoto);
                mv.addObject("editando", true);
            });
            
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("status", new String[]{"PENDENTE", "REPARO_SIMPLES", "DANOS_ESTRUTURAIS", "MOTOR_DEFEITUOSO", "MANUTENCAO_AGENDADA", "PRONTA", "SEM_PLACA", "ALUGADA", "AGUARDANDO_ALUGUEL"});
            
            System.out.println("ModelAndView criado com sucesso");
            return mv;
        } catch (Exception e) {
            System.out.println("ERRO ao editar status: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/motos/status/atualizar")
    public ModelAndView atualizarStatusMoto(StatusMoto statusMoto) {
        System.out.println("=== ATUALIZANDO STATUS DE MOTO ===");
        System.out.println("StatusMoto recebido: " + statusMoto);
        
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        System.out.println("Usuário encontrado: " + usuario.getNomeFilial());
                        
                        // Buscar a moto pelo ID
                        if (statusMoto.getMoto() != null && statusMoto.getMoto().getId() != null) {
                            Long motoId = statusMoto.getMoto().getId();
                            System.out.println("Buscando moto com ID: " + motoId);
                            
                            motoRepository.findById(motoId).ifPresent(moto -> {
                                System.out.println("Moto encontrada: " + moto.getPlaca());
                                statusMoto.setMoto(moto);
                                statusMoto.setUsuario(usuario);
                                statusMoto.setDataCriacao(LocalDateTime.now());
                                
                                System.out.println("Atualizando status no banco...");
                                StatusMoto statusAtualizado = statusMotosRepository.save(statusMoto);
                                System.out.println("Status atualizado com ID: " + statusAtualizado.getId());
                            });
                        } else {
                            System.out.println("ID da moto não fornecido!");
                        }
                    });
            } else {
                System.out.println("Usuário não autenticado!");
                ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
                mv.addObject("statusMoto", statusMoto);
                mv.addObject("motos", motoRepository.findAll());
                mv.addObject("status", new String[]{"PENDENTE", "REPARO_SIMPLES", "DANOS_ESTRUTURAIS", "MOTOR_DEFEITUOSO", "MANUTENCAO_AGENDADA", "PRONTA", "SEM_PLACA", "ALUGADA", "AGUARDANDO_ALUGUEL"});
                mv.addObject("editando", true);
                mv.addObject("erro", "Usuário não autenticado");
                return mv;
            }
            
            return new ModelAndView("redirect:/motos/operacoes?sucesso=true");
        } catch (Exception e) {
            System.out.println("ERRO ao atualizar status: " + e.getMessage());
            e.printStackTrace();
            ModelAndView mv = new ModelAndView("/motos/cadastroStatusMoto");
            mv.addObject("statusMoto", statusMoto);
            mv.addObject("motos", motoRepository.findAll());
            mv.addObject("status", new String[]{"PENDENTE", "REPARO_SIMPLES", "DANOS_ESTRUTURAIS", "MOTOR_DEFEITUOSO", "MANUTENCAO_AGENDADA", "PRONTA", "SEM_PLACA", "ALUGADA", "AGUARDANDO_ALUGUEL"});
            mv.addObject("editando", true);
            mv.addObject("erro", "Erro ao atualizar status: " + e.getMessage());
            return mv;
        }
    }

    @GetMapping("/motos/status/excluir/{id}")
    public ModelAndView excluirStatusMoto(@PathVariable Long id) {
        statusMotosRepository.deleteById(id);
        return new ModelAndView("redirect:/motos/operacoes");
    }

    @GetMapping("/motos/operacoes")
    public ModelAndView listarStatusMotos() {
        System.out.println("=== LISTANDO STATUS DAS MOTOS ===");
        try {
            ModelAndView mv = new ModelAndView("/motos/operacoes");
            
            // Obter usuário logado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {
                        System.out.println("Usuário logado: " + usuario.getNomeFilial());
                        mv.addObject("usuario_logado", usuario);
                    });
            } else {
                System.out.println("Usuário não autenticado, redirecionando para login");
                return new ModelAndView("redirect:/login");
            }
            
            // Buscar status das motos
            System.out.println("Buscando status das motos...");
            List<StatusMoto> statusMotos = statusMotosRepository.findAll();
            System.out.println("Total de status encontrados: " + (statusMotos != null ? statusMotos.size() : 0));
            
            // Se não há dados, criar uma lista vazia
            if (statusMotos == null) {
                System.out.println("Lista de status é null, criando lista vazia");
                statusMotos = new java.util.ArrayList<>();
            }
            
            mv.addObject("statusMotos", statusMotos);
            
            System.out.println("ModelAndView criado com sucesso");
            return mv;
        } catch (Exception e) {
            System.err.println("ERRO ao listar status das motos: " + e.getMessage());
            e.printStackTrace();
            
            ModelAndView mv = new ModelAndView("/motos/operacoes");
            mv.addObject("statusMotos", new java.util.ArrayList<>());
            mv.addObject("erro", "Erro ao carregar status das motos: " + e.getMessage());
            
            // Adicionar usuário logado mesmo em caso de erro
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null && !auth.getName().equals("anonymousUser")) {
                usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> mv.addObject("usuario_logado", usuario));
            }
            
            return mv;
        }
    }
}
