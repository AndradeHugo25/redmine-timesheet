package redmine.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Linha {

    private String data;
    private String projeto;
    private String atividade;
    private String tarefa;
    private String observacao;
    private String horas;
    private String equipe;
    private String situacao;
    private int numLinha;

    public ArrayList<String> getCamposObrigatorios() {
        ArrayList<String> a = new ArrayList<String>();
        a.add(data);
        a.add(projeto);
        a.add(atividade);
        a.add(horas);
        return a;
    }

}
