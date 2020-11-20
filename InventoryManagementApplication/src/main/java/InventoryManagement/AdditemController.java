package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class AdditemController {
    // ATTRIBUTE DECLARATIONS
    @FXML private Text errorText;
    @FXML private Text loginUserText;
    @FXML private ComboBox typeChoice;
    @FXML private TextField searchInput;
    @FXML private TextField qtyInput;
    @FXML private Button supplierButton;
    @FXML private Button userButton;
    @FXML private TableView productTable;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn typeColumn;
    @FXML private TableColumn priceColumn;
    
    // INITIALIZE PAGE
    public void initialize() throws SQLException {
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        displayTable(DatabaseManager.getAllAvailableProducts());
        setTypeOptions();
        menuBarDisplay();
    }
    
    // MENU BAR HANDLES
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
    
    // HANDLE FUNCTIONS
    public void handleSearch() throws SQLException {
        applyFilter();
    }
    
    public void handleAdd() throws IOException {
        addItem();
    }
    
    public void handleBack() throws IOException {
        Common.movePage("editorder", Common.getIdFromPage());
    }
    
    public void handleClear() throws IOException {
        Common.resetPage();
    }
    
    // HELPER FUNCTIONS
    private void applyFilter() throws SQLException {
        KeyValPair output = (KeyValPair) typeChoice.getValue();
        String query =  Common.clean(searchInput.getText());
        
        displayTable(getTableData(output, query));
    }
    
    private void addItem() throws IOException {
        Product product = (Product) productTable.getSelectionModel().getSelectedItem();
        String quantity = Common.clean(qtyInput.getText());
        
        if (!errorExist(product, quantity)) {
            OrderItem item = new OrderItem(product, Integer.parseInt(quantity), Common.getIdFromPage());
            Message.createMessage(item, (Common.getPrev() + "-" + Common.getIdFromPage()));
            Common.movePage("editorder", Common.getIdFromPage());
        }
    }
    
    private ObservableList<Product> getTableData(KeyValPair output, String query) throws SQLException {
        ObservableList<Product> data;
        if ((output != null) && (!query.isEmpty())) {
            data = DatabaseManager.getProductByFilter(output.getKey(), query);
        } else {
            if (output == null) {
                data = DatabaseManager.getProductByFilter(query);
            } else {
                data = DatabaseManager.getProductByFilter(output.getKey());
            }
        }
        return data;
    }
    
    private boolean errorExist(Product product, String quantity) {
        errorText.setVisible(false);
        
        if (product == null) {
            errorText.setText("select a product!");
            errorText.setVisible(true);
            return true;
        } 
        
        if (quantity.isEmpty()) {
            errorText.setText("enter a quantity!");
            errorText.setVisible(true);
            return true;
        }
        
        if (!Common.isNumber(quantity)) {
            errorText.setText("enter a valid quantity!");
            errorText.setVisible(true);
            return true;
        }
        
        if (!Common.isInt(quantity)) {
            errorText.setText("quantity must be whole numbers!");
            errorText.setVisible(true);
            return true;
        }
        
        if (!(Message.messageFree())) {
            Message.deleteMessage();
        }
        return false;
    }
    
    private void setTypeOptions() throws SQLException {
        ObservableList<KeyValPair> typeChoices = DatabaseManager.getTypes();
        typeChoice.setItems(typeChoices);
    }
    
    private void displayTable(ObservableList<Product> data) throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory("ProdId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory("Name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("TypeName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory("PriceString"));
        
        productTable.setItems(data);
    }
}
