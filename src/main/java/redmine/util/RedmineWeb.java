package redmine.util;

import com.riocard.ferramentas.data.TratamentoDeDados;
import com.riocard.ferramentas.webInteraction.Web;
import io.github.bonigarcia.wdm.WebDriverManager;
import redmine.interfaceGrafica.InterfaceUtil;
import redmine.interfaceGrafica.RedmineGUI;
import redmine.model.Linha;
import redmine.model.Msg;
import redmine.pages.LoginPage;
import redmine.pages.MinhaPaginaPage;
import redmine.pages.ProjetosPage;
import redmine.pages.TempoGastoPage;
import lombok.Data;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.*;

@Data
public class RedmineWeb {

    private final String URL = "https://redmine.riocardti.com.br/login";
    private WebDriver driver;
    private LoginPage loginPage;
    private MinhaPaginaPage minhaPaginaPage;
    private ProjetosPage projetoPage;
    private TempoGastoPage tempoGastoPage;

    private ManipuladorExcel manipuladorExcel;
    private RedmineLog log;
    private int totalErros;
    private String msgResultado;
    private ArrayList<String> msgsErros;
    private ArrayList<String> msgsSucesso = new ArrayList<String>();
    private boolean lancouAlguma;
    private Set<String> valoresCombo = new HashSet<String>();
    private Map<String, String> mapComboProjeto = new HashMap<String, String>();

    public RedmineWeb(RedmineLog log, ManipuladorExcel manipuladorExcel) {
        this.log = log;
        this.manipuladorExcel = manipuladorExcel;
        this.totalErros = manipuladorExcel.getTotalErros();
        this.msgsErros = manipuladorExcel.getMsgsErros();
        lancouAlguma = false;
    }

    private void inicializarDriver() throws IOException {
        log.escrever(Msg.INICIOLANCAMENTO.getValor());
        //WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public void inicializarPages() {
        loginPage = new LoginPage(driver);
        minhaPaginaPage = new MinhaPaginaPage(driver);
        projetoPage = new ProjetosPage(driver);
        tempoGastoPage = new TempoGastoPage(driver);
    }

    public void realizarLancamentoHoras() throws Exception {
        if (manipuladorExcel.getLinhasTimeSheet().size() > 0) {
            inicializarDriver();
            inicializarPages();
            logarRedmine();
            acessarFormularioHoras();
            mapearValoresCombo();
            preencherValoresFormulario();
            driver.close();
            if (totalErros <= 0) {
                msgResultado += Msg.RESULTADOLINHASLANCADAS.getValor();
            } else {
                if (lancouAlguma) {
                    msgResultado = Msg.RESULTADOALGUMALANCADA.getValor();
                } else {
                    msgResultado += Msg.RESULTADONENHUMALINHALANCADA.getValor();
                }
            }
        } else {
            msgResultado += Msg.RESULTADONENHUMALINHALANCADA.getValor();
        }
    }

    public void logarRedmine() throws Exception {
        try {

            driver.get(URL);

            boolean logou = false;
            do {
                Web.clickClearAndSendKeys(loginPage.campoLogin, DadosConfig.loginRedmine);
                Web.clickClearAndSendKeys(loginPage.campoSenha, DadosConfig.senhaRedmine);
                loginPage.botaoEntrar.click();

                try {
                    loginPage.msgErroLoginSenhaInvalido.getText();

                    RedmineGUI redmineGUI = new RedmineGUI("Preencha novamente");
                    redmineGUI.setVisible(true);

                    if (InterfaceUtil.isInterfaceClosed(redmineGUI)) {
                        log.escrever(Msg.NOVATENTATIVALOGIN.getValor());
                    }
                } catch (Exception e) {
                    try {
                        minhaPaginaPage.campoProjetos.getText();
                        logou = true;
                    } catch (Exception ignored) {
                    }
                }

            } while (!logou);

        } catch (Exception e) {
            log.escreverLogENotificar(Msg.NAOFOIPOSSIVELLOGAR.getValor());
            driver.close();
            throw e;
        }
    }

    public void acessarFormularioHoras() throws Exception {
        clicarProjetos();
        clicarTempoGasto();
        clicarTempoTrabalho();
    }

    private void clicarProjetos() throws Exception {
        try {
            minhaPaginaPage.campoProjetos.click();
        } catch (Exception e) {
            log.escreverLogENotificar(Msg.NAOCLICOU.getValor() + "Projetos!");
            driver.close();
            throw e;
        }
    }

    private void clicarTempoGasto() throws Exception {
        try {
            Thread.sleep(2000);
            projetoPage.campoTempoGasto.click();
        } catch (Exception e) {
            log.escreverLogENotificar(Msg.NAOCLICOU.getValor() + "Tempo Gasto!");
            driver.close();
            throw e;
        }
    }

    private void clicarTempoTrabalho() throws Exception {
        try {
            Thread.sleep(3000);
            tempoGastoPage.campoTempoTrabalho.click();
        } catch (Exception e) {
            log.escreverLogENotificar(Msg.NAOCLICOU.getValor() + "Tempo de Trabalho!");
            driver.close();
            throw e;
        }
    }

    private void mapearValoresCombo() {
        setValoresCombo(Web.getValoresCombo(tempoGastoPage.comboProjeto));
        for (String valor : valoresCombo) {
            String chave = "";
            chave = TratamentoDeDados.formatar(valor);
            chave = TratamentoDeDados.removerNumeros(chave);
            chave = TratamentoDeDados.removerAcentuacao(chave).toUpperCase();
            mapComboProjeto.put(chave, valor);
        }
    }

    public void preencherValoresFormulario() throws Exception {

        ArrayList<Linha> linhasSeremLancadas = manipuladorExcel.getLinhasTimeSheet();

        for (Linha linhaAtual : linhasSeremLancadas) {
            try {
                String projeto = linhaAtual.getProjeto().toUpperCase();
                projeto = TratamentoDeDados.formatar(projeto);
                projeto = TratamentoDeDados.removerNumeros(projeto);
                projeto = TratamentoDeDados.removerAcentuacao(projeto);

                if (mapComboProjeto.containsKey(projeto)) {
                    Web.selecionarItemDaCombo(tempoGastoPage.comboProjeto, mapComboProjeto.get(projeto));
                    Web.clickClearAndSendKeys(tempoGastoPage.campoTarefa, linhaAtual.getTarefa());
                    Web.clickClearAndSendKeys(tempoGastoPage.campoData, linhaAtual.getData());
                    Web.clickClearAndSendKeys(tempoGastoPage.campoHoras, linhaAtual.getHoras());
                    Web.clickClearAndSendKeys(tempoGastoPage.campoComentario, linhaAtual.getObservacao());

                    try {
                        String atividade = linhaAtual.getAtividade();
                        atividade = atividade.substring(0, 1).toUpperCase() + atividade.substring(1);
                        Web.selecionarItemDaCombo(tempoGastoPage.comboAtividade, atividade);

                        Web.selecionarItemDaCombo(tempoGastoPage.comboEquipe, DadosConfig.equipe);
                        tempoGastoPage.botaoCriarContinuar.click();

                        try {
                            tempoGastoPage.msgSucessoLancamento.getText();
                            manipuladorExcel.confirmarLancamento(linhaAtual);
                            lancouAlguma = true;
                            msgsSucesso.add("> Linha " + linhaAtual.getNumLinha() + "-" + Msg.LINHALANCADASUCESSO.getValor());
                        } catch (Exception e) {
                            try {
                                tempoGastoPage.msgErroTarefaInvalida.getText();
                                msgsErros.add("> Linha " + linhaAtual.getNumLinha() + "-" + Msg.TAREFAINVALIDA.getValor());
                            } catch (Exception ignored) {
                            }
                        }
                    } catch (Exception e) {
                        msgsErros.add("> Linha " + linhaAtual.getNumLinha() + "-" + Msg.ATIVIDADEINVALIDAPROJETO.getValor());
                    }
                } else {
                    msgsErros.add("> Linha " + linhaAtual.getNumLinha() + "-" + Msg.PROJETOINVALIDO.getValor());
                }
            } catch (Exception e) {
                log.escreverLogENotificar(Msg.NAOFOIPOSSIVELPREENCHERDADOS.getValor());
            }
        }
        log.escrever(Msg.FIMLANCAMENTO.getValor());
    }
}
