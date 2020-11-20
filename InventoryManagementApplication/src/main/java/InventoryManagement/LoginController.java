package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {
    // DECLARE DESIGN COMPONENTS
    @FXML private Button signupButton;
    @FXML private TextField usernameInput;
    @FXML private TextField passwordInput;
    @FXML private Text errorText;
    
    //HELPER FUNCTIONS
    @FXML
    private void switchToSecondary() throws IOException{
        App.setRoot("signup");
    }
    
    @FXML
    private void shopUserLoggedIn() throws IOException{
        App.setRoot("supplier");
    }
    
    private void setCommon(String username, String page) throws SQLException {
        Common.setUser(DatabaseManager.getUserByUsername(username));
        Common.setPage(page);
    }
    
    private void moveToDashboard() throws IOException {
        Common.movePage("managerdashboard");
    }
    
    //HANDLE FUNCTIONS
    public void handleLogin() throws SQLException, IOException {
        String username = Common.clean(usernameInput.getText());
        boolean userExist = DatabaseManager.usernameExists(username);
        errorText.setVisible(false);
        
        if (userExist) {
            String correctPass = DatabaseManager.getPassword(username);
            if (correctPass.equals(passwordInput.getText())) {
                if (DatabaseManager.userApproved(username)) {
                    setCommon(username, "login");
                    moveToDashboard();
                } else {
                    errorText.setText("get Manager Approval");
                    errorText.setVisible(true);
                }
            } else {
                errorText.setText("Username or Password is incorrect");
                errorText.setVisible(true);
            }
        } else {
            errorText.setText("Username or Password is incorrect");
            errorText.setVisible(true);
        }
    }
    
    //INITIALIZE PAGE 
    public void initialize() {
        Common.setUser(new User());
        Common.setPage("login");
        errorText.setVisible(false);
    }
}
