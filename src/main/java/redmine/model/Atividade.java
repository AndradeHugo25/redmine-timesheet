package redmine.model;

public enum Atividade {

    ACOMPANHAMENTOIMPLANT("Acompanhamento da implantação"),
    APOIOTEC("Apoio técnico"),
    APOIOTECOUTRASAREAS("Apoio técnico a outras áreas"),
    ARQUITSOFT("Arquitetura de software"),
    ATIVIDADEAREA("Atividades da área"),
    ATIVIDADEEMPRESA("Atividades da empresa"),
    CODEREVIEW("Code review"),
    CODIFICACAOTESTESUNITARIOS("Codificação e testes unitários"),
    CODIFICANDO("Codificando"),
    DESENVOLVERDVI("Desenvolver DVI"),
    DESENVOLVERWIREFRAME("Desenvolver wireframe"),
    ENTENDIMENTODEMANDA("Entendimento demanda"),
    ESPECIFICACAO("Especificação"),
    EXECTAREFA("Execução de tarefa"),
    ESPECIFICANDOCASOTESTE("Especificando caso de teste"),
    ESPECIFICARREQUISITOS("Especificar requisitos"),
    ESTIMATIVADEESFORCOS("Estimativa de esforços"),
    GERENCIARDESENV("Gerenciar desenvolvimento"),
    HOMOLOGHARDWARE("Homologação de hardware"),
    HOMOLOGSOFTWARE("Homologação de software"),
    LEVANTAMENTOREQUISITOS("Levantamento requisitos"),
    MERGESOFT("Merge de software"),
    MODELAGEMDADOS("Modelagem de dados"),
    PLANEJAMENTOIMPLANT("Planejamento da implantação"),
    REUNIAOEXTERNA("Reunião externa"),
    REUNIAOINTERNA("Reunião interna"),
    VALIDANDO("Validando");

    private final String valor;

    Atividade(String valorOpcao) {
        valor = valorOpcao;
    }

    public String getValor() {
        return valor;
    }

    public static boolean contem(String projeto) {
        for (Atividade aux : values()) {
            if (projeto.equals(aux.getValor())) {
                return true;
            }
        } return false;
    }

}
