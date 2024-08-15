package redmine.runner;

import redmine.interfaceGrafica.ConfigGUI;
import redmine.interfaceGrafica.InterfaceUtil;
import redmine.model.Msg;
import redmine.util.DadosConfig;
import redmine.util.ManipuladorExcel;
import redmine.util.RedmineLog;
import redmine.util.RedmineWeb;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class main {

    private static RedmineLog log;

    public static void main(String[] args) throws Exception {
        try {
            String msgResultado = "";
            ArrayList<String> msgsErros = new ArrayList<String>();
            ArrayList<String> msgsSucesso = new ArrayList<String>();

            final JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            ConfigGUI configGUI = new ConfigGUI(panel);
            configGUI.setVisible(true);

            if (InterfaceUtil.isInterfaceClosed(configGUI)) {
                log = new RedmineLog(DadosConfig.pathPlanilha);

                ManipuladorExcel manipuladorExcel = new ManipuladorExcel(DadosConfig.pathPlanilha, log);
                manipuladorExcel.armazenarLinhasPlanilhaTimeSheet();

                RedmineWeb web = new RedmineWeb(log, manipuladorExcel);
                web.realizarLancamentoHoras();
                msgResultado = web.getMsgResultado();
                msgsSucesso = web.getMsgsSucesso();
                msgsErros = web.getMsgsErros();
            }

            log.escreverResultado(msgResultado, msgsErros, msgsSucesso);

        } catch (Exception e) {
            e.printStackTrace();
            log.notificar(Msg.ERROVERIRIFCARLOG.getValor());
            log.escreverMensagemExcecao(e.getMessage());
            log.escreverStackTrace(e.getStackTrace());
            throw e;
        }
    }

}
