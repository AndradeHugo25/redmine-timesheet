package redmine.util;

import com.riocard.ferramentas.configuration.Log;
import com.riocard.ferramentas.file.ManipuladorArquivo;
import redmine.model.Msg;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static redmine.model.Msg.*;

public class RedmineLog extends Log {

    public RedmineLog(String caminho) throws IOException {
        String caminhoCompleto = montarCaminhoArqLogRedmine(caminho);
        setCaminhoCompleto(caminhoCompleto);
        ManipuladorArquivo.deletarArquivo(caminhoCompleto);
        escrever(INICIO.getValor());
    }

    private String montarCaminhoArqLogRedmine(String caminhoPlanilha) {
        int ultimaBarra = caminhoPlanilha.lastIndexOf('/');
        return caminhoPlanilha.substring(0, ultimaBarra + 1) + "logTimesheet.txt";
    }

    public void escreverLogENotificar(String txt) throws IOException {
        escrever(txt);
        notificar(txt);
    }

    public void notificar(String txt) {
        JOptionPane.showMessageDialog(null, txt);
    }

    public void notificarErro(String txt) {
        JOptionPane.showMessageDialog(null, txt, NOVAMENSAGEM.getValor(), JOptionPane.WARNING_MESSAGE);
    }

    public void escreverResultado(String msgResultado, ArrayList<String> msgsErros, ArrayList<String> msgsSucesso) throws IOException {
        escrever(TOTALERROS.getValor() + msgsErros.size());
        escreverLista(msgsErros);
        escrever(TOTALLANCADAS.getValor() + msgsSucesso.size());
        escreverLista(msgsSucesso);
        escrever(msgResultado);
        if (msgsErros.size() > 0) {
            notificarErro(msgResultado + Msg.CONTEMERROS.getValor());
        } else {
            notificar(msgResultado);
        }
    }

    private void escreverLista(ArrayList<String> lista) throws IOException {
        Collections.sort(lista);
        for (String aux : lista) {
            escrever(aux);
        }
    }

}
