package br.com.fiap.universidade_fiap.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.model.Usuario;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotosRepository;
import br.com.fiap.universidade_fiap.repository.UsuarioRepository;

@Service
@ConditionalOnProperty(value = "app.data.init.enabled", havingValue = "true")
public class DataInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MotoRepository motoRepository;
    
    @Autowired
    private StatusMotosRepository statusMotosRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.data.init.enabled:false}")
    private boolean dataInitEnabled;

    @Override
    public void run(String... args) throws Exception {
        if (!dataInitEnabled) {
            return;
        }

        // Limpar dados existentes em ordem segura
        statusMotosRepository.deleteAll();
        motoRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Criar dados mínimos
        createUsers();
        createMotos();
        createStatusMotos();
    }
    
    private void createUsers() {
        
        List<Usuario> usuarios = Arrays.asList(
            new Usuario(null, "Admin TrackZone", "admin@teste.com", 
                passwordEncoder.encode("Admin123!"), "12.345.678/0001-99", 
                "Rua das Flores, 123 - São Paulo/SP", "(11) 99999-9999", "ADMIN"),
            
            new Usuario(null, "Filial Centro", "gerente@teste.com", 
                passwordEncoder.encode("Gerente123!"), "98.765.432/0001-88", 
                "Av. Paulista, 456 - São Paulo/SP", "(11) 88888-8888", "GERENTE"),
            
            new Usuario(null, "Operador Filial", "operador@teste.com", 
                passwordEncoder.encode("Operador123!"), "11.111.111/0001-11", 
                "Rua da Consolação, 789 - São Paulo/SP", "(11) 77777-7777", "OPERADOR"),
            
            new Usuario(null, "Filial Norte", "norte@teste.com", 
                passwordEncoder.encode("Gerente123!"), "22.222.222/0002-22", 
                "Av. Tietê, 1000 - São Paulo/SP", "(11) 66666-6666", "GERENTE"),
            
            new Usuario(null, "Filial Sul", "sul@teste.com", 
                passwordEncoder.encode("Operador123!"), "33.333.333/0003-33", 
                "Rua Augusta, 2000 - São Paulo/SP", "(11) 55555-5555", "OPERADOR")
        );
        
        for (Usuario usuario : usuarios) {
            usuarioRepository.save(usuario);
        }
        logger.info("Usuários criados: {}", usuarios.size());
    }
    
    private void createMotos() {
        logger.debug("Criando motos...");
        
        Usuario admin = usuarioRepository.findByEmail("admin@teste.com").orElse(null);
        if (admin == null) {
            logger.error("Usuário admin não encontrado! Não é possível criar motos.");
            return;
        }
        
        List<Moto> motos = Arrays.asList(
            new Moto(null, "ABC1234", "9BWZZZZZZZZZZZZZZ1", "Honda CG 160", admin),
            new Moto(null, "DEF5678", "9BWZZZZZZZZZZZZZZ2", "Yamaha Fazer 250", admin),
            new Moto(null, "GHI9012", "9BWZZZZZZZZZZZZZZ3", "Honda CB 600", admin),
            new Moto(null, "JKL3456", "9BWZZZZZZZZZZZZZZ4", "Kawasaki Ninja 300", admin),
            new Moto(null, "MNO7890", "9BWZZZZZZZZZZZZZZ5", "Suzuki GSX 750", admin),
            new Moto(null, "PQR2468", "9BWZZZZZZZZZZZZZZ6", "Honda PCX 160", admin),
            new Moto(null, "STU1357", "9BWZZZZZZZZZZZZZZ7", "Yamaha NMAX 160", admin),
            new Moto(null, "VWX9753", "9BWZZZZZZZZZZZZZZ8", "Honda Biz 125", admin),
            new Moto(null, "YZA8642", "9BWZZZZZZZZZZZZZZ9", "Kawasaki Z400", admin),
            new Moto(null, "BCD1111", "9BWZZZZZZZZZZZZZZA", "Suzuki Burgman 200", admin)
        );
        
        motoRepository.saveAll(motos);
        logger.info("Motos criadas: {}", motos.size());
    }
    
    private void createStatusMotos() {
        logger.debug("Criando status das motos...");
        
        Usuario admin = usuarioRepository.findByEmail("admin@teste.com").orElse(null);
        if (admin == null) {
            logger.error("Usuário admin não encontrado! Não é possível criar status.");
            return;
        }
        
        List<Moto> motos = motoRepository.findAll();
        if (motos.isEmpty()) {
            logger.warn("Nenhuma moto encontrada! Não é possível criar status.");
            return;
        }
        
        List<StatusMoto> statusMotos = Arrays.asList(
            new StatusMoto(null, motos.get(0), "PRONTA", "Garagem Principal", admin),
            new StatusMoto(null, motos.get(1), "ALUGADA", "Em Uso", admin),
            new StatusMoto(null, motos.get(2), "MANUTENCAO_AGENDADA", "Oficina", admin),
            new StatusMoto(null, motos.get(3), "REPARO_SIMPLES", "Oficina", admin),
            new StatusMoto(null, motos.get(4), "DANOS_ESTRUTURAIS", "Oficina", admin),
            new StatusMoto(null, motos.get(5), "MOTOR_DEFEITUOSO", "Oficina", admin),
            new StatusMoto(null, motos.get(6), "AGUARDANDO_ALUGUEL", "Garagem Secundária", admin),
            new StatusMoto(null, motos.get(7), "SEM_PLACA", "Garagem Principal", admin),
            new StatusMoto(null, motos.get(8), "PENDENTE", "Garagem Principal", admin),
            new StatusMoto(null, motos.get(9), "PRONTA", "Garagem Secundária", admin)
        );
        
        statusMotosRepository.saveAll(statusMotos);
        logger.info("Status das motos criados: {}", statusMotos.size());
    }
}
