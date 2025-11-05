package br.com.fiap.universidade_fiap.control;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.fiap.universidade_fiap.model.Dashboard;
import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.repository.DashboardRepository;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;
import br.com.fiap.universidade_fiap.service.AuthenticationService;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @Autowired
    private DashboardRepository dashboardRepository;
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/dashboard")
    public ModelAndView dashboard() {
        logger.debug("Carregando dashboard");
        ModelAndView mv = new ModelAndView("/home/dashboard");
        
        try {
            authenticationService.adicionarUsuarioLogado(mv);
            
            // Calcular contadores dinamicamente
            List<Moto> motos = motoRepository.findAll();
            List<StatusMoto> statusMotos = statusMotosRepository.findAll();
            
            // Contar por status
            long motosProntas = statusMotos.stream()
                .filter(s -> s.getStatus().equals("PRONTA"))
                .count();
                
            long motosAlugadas = statusMotos.stream()
                .filter(s -> s.getStatus().equals("ALUGADA"))
                .count();
                
            long motosEmManutencao = statusMotos.stream()
                .filter(s -> s.getStatus().equals("REPARO_SIMPLES") || 
                           s.getStatus().equals("DANOS_ESTRUTURAIS") ||
                           s.getStatus().equals("MOTOR_DEFEITUOSO") ||
                           s.getStatus().equals("MANUTENCAO_AGENDADA"))
                .count();
                
            long motosPendentes = statusMotos.stream()
                .filter(s -> s.getStatus().equals("PENDENTE"))
                .count();
                
            long motosSemPlaca = statusMotos.stream()
                .filter(s -> s.getStatus().equals("SEM_PLACA"))
                .count();
                
            long motosAguardandoAluguel = statusMotos.stream()
                .filter(s -> s.getStatus().equals("AGUARDANDO_ALUGUEL"))
                .count();
            
            logger.debug("Estatísticas do dashboard - Prontas: {}, Alugadas: {}, Manutenção: {}", 
                motosProntas, motosAlugadas, motosEmManutencao);
            
            // Adicionar contadores ao modelo
            mv.addObject("totalMotos", motos.size());
            mv.addObject("motosProntas", motosProntas);
            mv.addObject("motosAlugadas", motosAlugadas);
            mv.addObject("motosEmManutencao", motosEmManutencao);
            mv.addObject("motosPendentes", motosPendentes);
            mv.addObject("motosSemPlaca", motosSemPlaca);
            mv.addObject("motosAguardandoAluguel", motosAguardandoAluguel);
            mv.addObject("totalStatus", statusMotos.size());
            
            return mv;
        } catch (Exception e) {
            logger.error("Erro ao carregar dashboard: {}", e.getMessage(), e);
            throw e;
        }
    }
}
