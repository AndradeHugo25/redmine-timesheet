package redmine.model;

public enum Msg {

    INICIO(">>> TIMESHEET AUTOMATION <<<"),
    NOVATENTATIVALOGIN("Nova tentativa de login."),
    ERROVERIRIFCARLOG("ERRO!!! Verificar arquivo de log."),
    NOVAMENSAGEM("Nova Mensagem"),
    LOGINNULO("Campo login obrigatório!"),
    SENHANULA("Campo senha obrigatório!"),
    PATHNULO("Campo caminho da planilha obrigatório!"),
    EQUIPENULA("Campo equipe obrigatório!"),

    INICIOLEITURAPLANILHA("\n...Iniciando validação de dados da planilha..."),
    DATAMAIORQUEATUAL("Data não pode ser maior do que a data atual!"),
    DATAINVALIDA("Data inválida! Não foi possível realizar a conversão da data para o formato dd/MM/yyyy!"),
    PROJETOINVALIDO("Projeto inválido!"),
    ATIVIDADEINVALIDA("Atividade inválida!"),
    CAMPONULO("Campo não pode ser nulo!"),
    FIMPROCESSAMENTO("Fim do processamento da planilha."),

    INICIOLANCAMENTO("...Iniciando lançamento de horas no redmine..."),
    TAREFAINVALIDA("Tarefa não pertence ao projeto!"),
    ATIVIDADEINVALIDAPROJETO("Atividade inválida para este projeto!"),
    NAOCLICOU("Não foi possível clicar em "),
    NAOFOIPOSSIVELLOGAR("Não foi possível realizar login!"),
    NAOFOIPOSSIVELPREENCHERDADOS("Não foi possível preencher os dados!"),
    FIMLANCAMENTO("Fim do lançamento de horas no redmine."),

    CONTEMERROS("\n>> ERROS ENCONTRADOS!!\n>> Confira o arquivo de log gerado na mesma pasta da planilha!"),
    TOTALERROS("\nTOTAL DE LINHAS COM ERRO: "),
    TOTALLANCADAS("\nTOTAL DE LINHAS LANÇADAS: "),
    LINHALANCADASUCESSO("Lançada com sucesso."),
    RESULTADONENHUMALINHALANCADA("\nRESULTADO: Nenhuma linha lançada!"),
    RESULTADOALGUMALANCADA("\nRESULTADO: Algumas linhas lançadas, outras com erros!"),
    RESULTADOLINHASLANCADAS("\nRESULTADO: Linha(s) lançada(s) com sucesso!");

    private final String valor;

    Msg(String valorOpcao) {
        valor = valorOpcao;
    }

    public String getValor() {
        return valor;
    }

    public static boolean contem(String projeto) {
        for (Msg aux : values()) {
            if (projeto.equals(aux.getValor())) {
                return true;
            }
        } return false;
    }

}
