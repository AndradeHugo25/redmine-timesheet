package redmine.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class ProjetosPage {

    public ProjetosPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(how = How.XPATH, using = "/html/body/div/div[2]/div[1]/div[2]/div[2]/ul/li[4]/a")
    public WebElement campoTempoGasto;

}
