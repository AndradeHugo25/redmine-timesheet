package redmine.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class TempoGastoPage {

    public TempoGastoPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Tempo de trabalho")
    public WebElement campoTempoTrabalho;
    @FindBy(how = How.ID, using = "time_entry_project_id")
    public WebElement comboProjeto;
    @FindBy(how = How.ID, using = "time_entry_issue_id")
    public WebElement campoTarefa;
    @FindBy(how = How.ID, using = "time_entry_spent_on")
    public WebElement campoData;
    @FindBy(how = How.ID, using = "time_entry_hours")
    public WebElement campoHoras;
    @FindBy(how = How.ID, using = "time_entry_comments")
    public WebElement campoComentario;
    @FindBy(how = How.ID, using = "time_entry_activity_id")
    public WebElement comboAtividade;
    @FindBy(how = How.ID, using = "time_entry_custom_field_values_42")
    public WebElement comboEquipe;
    @FindBy(how = How.NAME, using = "commit")
    public WebElement botaoCriar;
    @FindBy(how = How.NAME, using = "continue")
    public WebElement botaoCriarContinuar;
    @FindBy(how = How.ID, using = "errorExplanation")
    public WebElement msgErroTarefaInvalida;
    @FindBy(how = How.ID, using = "flash_notice")
    public WebElement msgSucessoLancamento;



}
