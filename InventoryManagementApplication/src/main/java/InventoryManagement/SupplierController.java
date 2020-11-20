package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class SupplierController {
    //ATTRIBUTE DECLARATIONS
    @FXML private Button addSupplier;
    @FXML private Button editButton;
    @FXML private Button logoutButton;
    @FXML private Button aboutButton;
    @FXML private ComboBox supplierChoice;
    @FXML private TableView<Supplier> allSuppliers;
    @FXML private TableColumn supplierID;
    @FXML private TableColumn supplierName;
    @FXML private TableColumn supplierContact;
    @FXML private TableColumn supplierAddress;
    @FXML private Text errorText;
    @FXML private Text loginUserText;

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
    public void handleBack() throws IOException {
        moveToDashboard();
    }
    
    public void handleAddSupplier() throws IOException {
        switchToAddSuppliers();
    }
    
    public void handleEditSupplier() throws IOException {
        KeyValPair output = (KeyValPair) supplierChoice.getValue();
        if (output == null) {
            errorText.setVisible(true);
        } else {
            errorText.setVisible(false);
            switchToEditSuppliers(output.getKey());
        }
    }
    
    //MOVE FUNCTION
    private void moveToDashboard() throws IOException {
        Common.movePage("managerdashboard");
    }
    
    //HELPER FUNCTIONS
    @FXML
    private void switchToAddSuppliers() throws IOException {
        App.setRoot("addsupplier");
    }
    
    @FXML
    private void switchToEditSuppliers(int id) throws IOException {
        Common.setPage("editsupplier-" + id);
        App.setRoot("editsupplier");
    }
    
    @FXML
    public void displayTable() throws SQLException {
        ObservableList<Supplier> data = FXCollections.observableArrayList();
        data = DatabaseManager.getAllSuppliers();
        
        supplierID.setCellValueFactory(new PropertyValueFactory("id"));
        supplierName.setCellValueFactory(new PropertyValueFactory("name"));
        supplierContact.setCellValueFactory(new PropertyValueFactory("contact"));
        supplierAddress.setCellValueFactory(new PropertyValueFactory("address"));
        
        allSuppliers.setItems(data);
    }
    //INITIALIZE PAGE
    public void initialize () throws SQLException {
        errorText.setVisible(false);
        displayTable();
        menuBarDisplay();
        
        ObservableList<KeyValPair> suppliers = DatabaseManager.getSuppliers();
        supplierChoice.setItems(suppliers);
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
    }
}
