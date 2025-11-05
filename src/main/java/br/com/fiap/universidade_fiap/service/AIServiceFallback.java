package br.com.fiap.universidade_fiap.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Serviço de fallback para IA quando Spring AI não está configurado
 * Fornece respostas básicas sem precisar de API key
 */
@Service
public class AIServiceFallback {

    private static final Logger logger = LoggerFactory.getLogger(AIServiceFallback.class);

    public String obterSugestao(String contexto, String pergunta) {
        logger.debug("Usando fallback para pergunta: {}", pergunta);
        
        String perguntaLower = pergunta.toLowerCase();
        
        // Respostas pré-definidas baseadas em palavras-chave
        if (perguntaLower.contains("status") || perguntaLower.contains("estado")) {
            return "💡 **Status Disponíveis para Motos:**\n\n" +
                   "• PRONTA - Moto pronta para uso\n" +
                   "• PENDENTE - Aguardando ação\n" +
                   "• REPARO_SIMPLES - Reparo básico necessário\n" +
                   "• DANOS_ESTRUTURAIS - Danos graves na estrutura\n" +
                   "• MOTOR_DEFEITUOSO - Problemas no motor\n" +
                   "• MANUTENCAO_AGENDADA - Manutenção já agendada\n" +
                   "• SEM_PLACA - Moto sem placa cadastrada\n" +
                   "• ALUGADA - Moto atualmente alugada\n" +
                   "• AGUARDANDO_ALUGUEL - Pronta para aluguel\n\n" +
                   "Você pode atualizar o status de uma moto na página 'Status das Motos'.";
        }
        
        if (perguntaLower.contains("cadastrar") || perguntaLower.contains("cadastro")) {
            return "📝 **Como Cadastrar uma Moto:**\n\n" +
                   "1. Acesse o menu 'Motos' no header\n" +
                   "2. Clique em 'Cadastrar Nova Moto'\n" +
                   "3. Preencha os campos obrigatórios:\n" +
                   "   • Placa (única, obrigatória)\n" +
                   "   • Chassi (único, obrigatório)\n" +
                   "   • Motor (obrigatório)\n" +
                   "4. Clique em 'Salvar'\n\n" +
                   "⚠️ **Importante:** A placa e o chassi devem ser únicos no sistema.";
        }
        
        if (perguntaLower.contains("excluir") || perguntaLower.contains("deletar") || perguntaLower.contains("remover")) {
            return "🗑️ **Como Excluir uma Moto:**\n\n" +
                   "1. Acesse a lista de motos\n" +
                   "2. Localize a moto desejada\n" +
                   "3. Clique no botão 'Excluir'\n" +
                   "4. Confirme a exclusão\n\n" +
                   "⚠️ **Atenção:** A exclusão é permanente e não pode ser desfeita. " +
                   "Verifique se a moto não possui operações relacionadas antes de excluir.";
        }
        
        if (perguntaLower.contains("operacao") || perguntaLower.contains("operacoes")) {
            return "🔄 **Operações Disponíveis:**\n\n" +
                   "• CHECK_IN - Entrada da moto no sistema\n" +
                   "• CHECK_OUT - Saída da moto do sistema\n" +
                   "• MANUTENCAO - Registro de manutenção\n" +
                   "• ALUGUEL - Registro de aluguel\n" +
                   "• TRANSFERENCIA - Movimentação entre áreas\n\n" +
                   "Acesse o menu 'Operações' para gerenciar as operações do sistema.";
        }
        
        if (perguntaLower.contains("relatorio") || perguntaLower.contains("relatórios")) {
            return "📊 **Relatórios Disponíveis:**\n\n" +
                   "• Relatório por Período - Operações em período específico\n" +
                   "• Relatório por Status - Motos agrupadas por status\n" +
                   "• Relatório por Moto - Histórico individual de cada moto\n\n" +
                   "Acesse o menu 'Relatórios' para visualizar os relatórios. " +
                   "Disponível para perfis ADMIN e GERENTE.";
        }
        
        if (perguntaLower.contains("dashboard") || perguntaLower.contains("painel")) {
            return "📈 **Dashboard:**\n\n" +
                   "O Dashboard oferece uma visão geral do sistema com:\n" +
                   "• Estatísticas de motos\n" +
                   "• Métricas de operações\n" +
                   "• Status em tempo real\n\n" +
                   "Acesse o menu 'Dashboard' para visualizar. " +
                   "Disponível para perfis ADMIN e GERENTE.";
        }
        
        if (perguntaLower.contains("usuario") || perguntaLower.contains("usuário") || perguntaLower.contains("perfil")) {
            return "👤 **Perfis de Usuário:**\n\n" +
                   "• **ADMIN** - Acesso total ao sistema\n" +
                   "• **GERENTE** - Gestão de operações e relatórios\n" +
                   "• **OPERADOR** - Operações básicas\n\n" +
                   "Cada perfil tem permissões específicas. " +
                   "Apenas ADMIN pode gerenciar usuários.";
        }
        
        // Resposta padrão
        return "💡 **Bem-vindo ao Assistente do TrackZone!**\n\n" +
               "Posso ajudar você com:\n" +
               "• Status de motos\n" +
               "• Cadastro e exclusão de motos\n" +
               "• Operações do sistema\n" +
               "• Relatórios\n" +
               "• Dashboard\n" +
               "• Perfis de usuário\n\n" +
               "Faça uma pergunta específica ou use as perguntas rápidas disponíveis na página.";
    }

    public String analisarOperacao(String dadosOperacao) {
        logger.debug("Analisando operação com fallback: {}", dadosOperacao);
        
        return "📋 **Análise da Operação:**\n\n" +
               "Os dados da operação parecem estar corretos. " +
               "Verifique se todas as informações necessárias foram preenchidas.\n\n" +
               "💡 **Dicas:**\n" +
               "• Certifique-se de que a moto está cadastrada\n" +
               "• Verifique se o status está atualizado\n" +
               "• Adicione observações relevantes quando necessário";
    }
}

