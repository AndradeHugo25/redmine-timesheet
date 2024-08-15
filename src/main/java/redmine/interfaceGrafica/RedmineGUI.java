package redmine.interfaceGrafica;

import com.riocard.ferramentas.data.Encriptador;
import com.riocard.ferramentas.file.ManipuladorArquivo;
import lombok.SneakyThrows;
import redmine.runner.ThreadB;
import redmine.util.DadosConfig;
import lombok.Data;

import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.NoSuchAlgorithmException;

@Data
public class RedmineGUI extends DefaultGUI {

    private JLabel loginRedLabel;
    private JTextField campoLoginRed;
    private JLabel senhaRedLabel;
    private JButton botaoOk;
    private JPanel painelMain;
    private JPasswordField campoSenhaRed;
    private JLabel msgLoginLabel;
    private JLabel msgSenhaLabel;

    private Encriptador encriptador;

    public RedmineGUI(String titulo) throws NoSuchPaddingException, NoSuchAlgorithmException {
        super(titulo);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(painelMain);
        this.pack();
        this.setSize(350, 200);
        this.setLocationRelativeTo(null);

        encriptador = new Encriptador();

        msgLoginLabel.setVisible(false);
        msgSenhaLabel.setVisible(false);

        botaoOk.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean loginVazio = campoLoginRed.getText().equals("") || campoLoginRed == null;
                boolean senhaVazia = String.valueOf(campoSenhaRed.getPassword()).equals("") || campoSenhaRed == null;

                msgLoginLabel.setVisible(loginVazio);
                msgSenhaLabel.setVisible(senhaVazia);

                if (!loginVazio && !senhaVazia) {
                    threadB = new ThreadB();
                    threadB.start();

                    String login = campoLoginRed.getText();
                    String senha = String.valueOf(campoSenhaRed.getPassword());

                    String path = new File("").getAbsolutePath() + "\\";
                    String nomeArquivo = "dadosLoginTimesheet.txt";
                    File file = new File(path + nomeArquivo);
                    if (!file.exists()) {
                        ManipuladorArquivo.criarDiretorio(path);
                    }

                    String senhaCriptografada = encriptador.cifrar(senha);
                    String novosDados = "login=" + login + "\n" +
                            "senha=" + senhaCriptografada + "\n" +
                            "equipe=" + DadosConfig.equipe;
                    ManipuladorArquivo.salvarTextoEmArquivo(novosDados, nomeArquivo, path);

                    DadosConfig.loginRedmine = login;
                    DadosConfig.senhaRedmine = senha;

                    dispose();
                }

            }
        });
    }

}
