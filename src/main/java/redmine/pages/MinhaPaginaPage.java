package redmine.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class MinhaPaginaPage {

    public MinhaPaginaPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.PARTIAL_LINK_TEXT, using = "Projetos")
    public WebElement campoProjetos;
}
