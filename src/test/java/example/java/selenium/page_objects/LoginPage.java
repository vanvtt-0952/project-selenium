package example.java.selenium.page_objects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage {
    @FindBy(how = How.XPATH, using = "//*[@id=\"name\"]")
    private WebElement emailAddressField;

    @FindBy(how = How.XPATH,using = "//*[@id=\"password\"]")
    private WebElement passwordField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"header-navbar\"]/ul/li/button")
    private WebElement btnSignin;

    @FindBy(how = How.XPATH,using = "//*[@id=\"signinForm\"]/div[2]/div[4]/div/button")
    private WebElement btnLogin;

    @FindBy(how = How.XPATH,using = "//*[@id=\"signinForm\"]/p")
    private WebElement generalErrorField;

    @FindBy(how = How.XPATH, using = "//*[@id=\"navbarOrganizationSetting\"]")
    private WebElement myAccount;

    @FindBy(how = How.XPATH, using = "//*[@id=\"main\"]/div[2]/div[2]/div[3]/div[2]/a[4]")
    private WebElement logout;


    public void login(String emailAddress, String password){
        emailAddressField.clear();
        emailAddressField.sendKeys(emailAddress);

        passwordField.clear();
        passwordField.sendKeys(password);
        btnLogin.click();
    }

    public String getGeneralError(){
        return generalErrorField.getText();
    }

    public void myAccountLogout(){
        myAccount.click();
    }

    public WebElement getLogout(){
        return  logout;
    }

    public WebElement getEmailInput(){
        return  emailAddressField;
    }
}
