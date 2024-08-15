package redmine.interfaceGrafica;

import com.riocard.ferramentas.data.Encriptador;
import com.riocard.ferramentas.file.ManipuladorArquivo;
import redmine.runner.ThreadB;
import redmine.util.DadosConfig;
import lombok.Data;
import lombok.SneakyThrows;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Data
public class ConfigGUI extends DefaultGUI {

    private JPanel painelMain;

    private JLabel loginRedmineLabel;
    private JLabel senhaRedmineLabel;
    private JLabel equipeLabel;
    private JLabel pathLabel;

    private JTextField campoLoginRedmine;
    private JPasswordField campoSenhaRedmine;
    private JComboBox comboEquipe;

    private JButton botaoExecutar;
    private JButton botaoAbrirArq;
    private JLabel msgLoginLabel;
    private JLabel msgSenhaLabel;
    private JLabel msgCaminhoLabel;
    private JLabel msgEquipeLabel;
    private JTextField campoPathPlanilha;

    private Encriptador encriptador;

    public ConfigGUI(JPanel panel) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        super("Timesheet Automation");
        painelMain = panel;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(painelMain);
        this.pack();
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);

        encriptador = new Encriptador();

        String pathProjeto = new File("").getAbsolutePath() + "\\";
        String pathFile = pathProjeto + "dadosLoginTimesheet.txt";
        File file = new File(pathFile);
        if (file.exists()) {
            ManipuladorArquivo.armazenarConteudoArquivoEmLinhas(pathFile);
            List<String> linhas = ManipuladorArquivo.getLinhasLidas();
            for (String linhaAtual : linhas) {
                if (linhaAtual.contains("senha")) {
                    int posPrimeiroIgual = linhaAtual.indexOf('=');
                    String dadoAntigo = linhaAtual.substring(posPrimeiroIgual + 1);
                    String senhaDecrypt = encriptador.decifrar(dadoAntigo);
                    campoSenhaRedmine.setText(senhaDecrypt);
                } else {
                    String[] split = linhaAtual.split("=");
                    String dadoAntigo = split[1];
                    if (linhaAtual.contains("login")) {
                        campoLoginRedmine.setText(dadoAntigo);
                    } else if (linhaAtual.contains("equipe")) {
                        comboEquipe.setSelectedItem(dadoAntigo);
                    } else {
                        break;
                    }
                }
            }
        }

        Border border = BorderFactory.createLineBorder(Color.GRAY);
        campoPathPlanilha.setBorder(border);
        campoPathPlanilha.setBackground(Color.WHITE);

        botaoAbrirArq.setPreferredSize(new Dimension(64, 1));

        comboEquipe.setBackground(Color.WHITE);
        comboEquipe.setPreferredSize(new Dimension(158, 1));

        msgLoginLabel.setVisible(false);
        msgSenhaLabel.setVisible(false);
        msgCaminhoLabel.setVisible(false);
        msgEquipeLabel.setVisible(false);

        botaoAbrirArq.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Selecione a planilha");
                chooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter(".xls, .xlsx", "xls", "xlsx");
                chooser.addChoosableFileFilter(filter);

                int returnValue = chooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    campoPathPlanilha.setText(selectedFile.getAbsolutePath());
                }

                DadosConfig.pathPlanilha = campoPathPlanilha.getText().replace('\\', '/');
            }
        });

        botaoExecutar.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean loginVazio = campoLoginRedmine.getText().equals("") || campoLoginRedmine == null;
                boolean senhaVazia = String.valueOf(campoSenhaRedmine.getPassword()).equals("") || campoSenhaRedmine == null;
                boolean caminhoVazio = campoPathPlanilha.getText().equals("") || campoPathPlanilha == null;
                boolean equipeVazia = String.valueOf(comboEquipe.getSelectedItem()).equals("") || comboEquipe == null;

                msgLoginLabel.setVisible(loginVazio);
                msgSenhaLabel.setVisible(senhaVazia);
                msgCaminhoLabel.setVisible(caminhoVazio);
                msgEquipeLabel.setVisible(equipeVazia);

                if (!loginVazio && !senhaVazia && !caminhoVazio && !equipeVazia) {
                    threadB = new ThreadB();
                    threadB.start();

                    String login = campoLoginRedmine.getText();
                    String senha = String.valueOf(campoSenhaRedmine.getPassword());
                    String equipe = String.valueOf(comboEquipe.getSelectedItem());

                    String path = new File("").getAbsolutePath() + "\\";
                    String nomeArquivo = "dadosLoginTimesheet.txt";
                    File file = new File(path + nomeArquivo);
                    if (!file.exists()) {
                        ManipuladorArquivo.criarDiretorio(path);
                    }

                    String senhaCriptografada = encriptador.cifrar(senha);
                    String novosDados = "login=" + login + "\n" +
                            "senha=" + senhaCriptografada + "\n" +
                            "equipe=" + equipe;
                    ManipuladorArquivo.salvarTextoEmArquivo(novosDados, nomeArquivo, path);

                    DadosConfig.loginRedmine = login;
                    DadosConfig.senhaRedmine = senha;
                    DadosConfig.equipe = equipe;

                    dispose();
                }

            }
        });
    }
}
