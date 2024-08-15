package redmine.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    //TELA LOGIN
    @FindBy(how = How.ID, using = "username")
    public WebElement campoLogin;
    @FindBy(how = How.ID, using = "password")
    public WebElement campoSenha;
    @FindBy(how = How.ID, using = "login-submit")
    public WebElement botaoEntrar;
    @FindBy(how = How.ID, using = "flash_error")
    public WebElement msgErroLoginSenhaInvalido;



}
