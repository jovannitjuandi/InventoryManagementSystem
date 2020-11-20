package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


public class UserController {
    //ATTRIBUTE DECLARATIONS
    @FXML private TableView allUsers;
    @FXML private Button editbutton;
    @FXML private Button logoutButton;
    @FXML private ComboBox userChoice;
    @FXML private Text errortext;
    @FXML private Text loginUserText;
    @FXML private TableColumn userID;
    @FXML private TableColumn name;
    @FXML private TableColumn userName;
    @FXML private TableColumn status;
    @FXML private TableColumn suppID;
    @FXML private TableColumn approveColumn;
    
    // MENU BAR HANDLES
    @FXML private Button userButton;
    @FXML private Button supplierButton;
    public void handleLogout() throws IOException {
        Common.movePage("login");
    }
    
    public void handleHome() throws IOException {
        Common.movePage("managerdashboard");
    }
    
    public void handleSupplier() throws IOException {
        Common.movePage("Supplier");
    }
    
    public void handleAbout() throws IOException {
        Common.movePage("about");
    }
    
    public void handleProduct() throws IOException {
        Common.movePage("product");
    }
    
    public void handleOrder() throws IOException {
        Common.movePage("order");
    }
    
    public void handleUser() throws IOException {
        Common.movePage("user");
    }
    
    private void menuBarDisplay() throws SQLException {
        User currentUser = Common.getUser();
        if (currentUser.getStatus() == Position.MANAGER) {
        } else if (currentUser.getStatus() == Position.EMPLOYEE) {
            userButton.setVisible(false);
        } else if (currentUser.getStatus() == Position.SUPPLIER) {
            userButton.setVisible(false);
            supplierButton.setVisible(false);
        }
    }
    
    //HANDLE FUNCTIONS
    @FXML
    private void handleBack() throws IOException {
        moveToDashboard();
    }
    
    @FXML
    private void handleEditUser() throws IOException{
        KeyValPair output = (KeyValPair) userChoice.getValue();
        if (output == null){
            errortext.setVisible(true);
        } else {
            errortext.setVisible(false);
            switchToEditUsers(output.getKey());
        }
    }
    
    @FXML
    private void moveToDashboard() throws IOException {
        if((Common.getUser().getStatus()) == Position.MANAGER) {
            Common.setPage("managerdashboard");
            App.setRoot("managerdashboard");
        } else if ((Common.getUser().getStatus()) == Position.EMPLOYEE) {
            Common.setPage("employeedashboard");
            App.setRoot("employeedashboard");
        } else {
            Common.setPage("supplierdashboard-" + Common.getUser().getSuppId());
            App.setRoot("supplierdashboard");
        }
    }
    
    @FXML
    //HELPER FUNCTIONS
    private void switchToEditUsers(int userId) throws IOException{
        Common.setPage("edituser-" + userId);
        App.setRoot("edituser");
    }



    //DISPLAY FUNCTIONS
    public void displayTable() throws SQLException{
        ObservableList<User> data = FXCollections.observableArrayList();
        data = DatabaseManager.getAllUsers();
        
       userID.setCellValueFactory(new PropertyValueFactory("userId"));
       name.setCellValueFactory(new PropertyValueFactory("name"));
       userName.setCellValueFactory(new PropertyValueFactory("username"));
       status.setCellValueFactory(new PropertyValueFactory("status"));
       suppID.setCellValueFactory(new PropertyValueFactory("suppId"));
       approveColumn.setCellValueFactory(new PropertyValueFactory("approvedString"));
       
       allUsers.setItems(data);
    }
    
    //INITIALIZE PAGE
    public void initialize() throws SQLException{
        errortext.setVisible(false);
        displayTable();
        menuBarDisplay();
        
        ObservableList<KeyValPair> userChoices = DatabaseManager.getUsers();
        userChoice.setItems(userChoices);
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
    }
}