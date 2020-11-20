package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;


public class ProductController {
    
    ObservableList<Product> data = FXCollections.observableArrayList();
    
    //ATTRIBUTE DECLARATIONS
    @FXML private AnchorPane deleteConfirmation;
    @FXML private ImageView backButton;
    @FXML private ImageView addIcon;
    @FXML private ImageView binIcon;
    @FXML private Button deleteProductBtn;
    @FXML private Button yesButton;
    @FXML private Button addProductBtn;
    @FXML private Button noButton;
    @FXML private Button logoutButton;
    @FXML private TableView<Product> allProducts;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> unitColumn;   
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> typeIdColumn;
    @FXML private Text confirmText;
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
    
    public void handleAddProduct() throws IOException {
        switchToProducts();
    }
    
        public void handleYesButton(ActionEvent e) throws IOException, SQLException {
        deleteSelectedRows(e);
        Common.resetPage();
    }

    public void handleNoButton() {
        deleteConfirmation.setVisible(false);
        allProducts.setVisible(true);
        deleteProductBtn.setVisible(true);
        addProductBtn.setVisible(true);
        binIcon.setVisible(true);
        addIcon.setVisible(true);
    }

    public void handleDelete() {
        allProducts.setVisible(false);
        deleteConfirmation.setVisible(true);
        deleteProductBtn.setVisible(false);
        addProductBtn.setVisible(false);
        binIcon.setVisible(false);
        addIcon.setVisible(false);
    }
    
    //HELPER FUNCTIONS
    @FXML
    private void switchToProducts() throws IOException {
        Common.movePage("addproduct");
    }
  
    @FXML
    private void displayTable() throws SQLException {
        
        TableColumn actionColumn = new TableColumn("Action");
        
        data = FXCollections.observableArrayList();
        data = DatabaseManager.getAllAvailableProducts();
        
        idColumn.setCellValueFactory(new PropertyValueFactory("prodId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        unitColumn.setCellValueFactory(new PropertyValueFactory("unit"));
        priceColumn.setCellValueFactory(new PropertyValueFactory("price"));
        typeIdColumn.setCellValueFactory(new PropertyValueFactory("typeName"));

        actionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Product, CheckBox> arg0) {
            Product product = arg0.getValue();
                CheckBox checkBox = new CheckBox();
                checkBox.selectedProperty().setValue(product.getSelected());
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                        Boolean old_val, Boolean new_val) {
                        product.setSelected(new_val);
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });
        allProducts.getColumns().addAll(actionColumn);
        allProducts.setItems(data);
    }
    
    @FXML
    private void deleteSelectedRows(ActionEvent e) throws SQLException {
        
        ObservableList<Product> dataListRemove = FXCollections.observableArrayList();
        
        for (Product p : data) {
            if (p.getSelected() == true) {
                dataListRemove.add(p);
                DatabaseManager.deleteProductById(p.getProdId());
            }
        }
        data.removeAll(dataListRemove);
    }
    
    private void moveToDashboard() throws IOException {
        Common.showDashboard();
    }   
    
    public void initialize () throws SQLException {
        allProducts.setVisible(true);
        deleteConfirmation.setVisible(false);
        displayTable();
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        menuBarDisplay();
    }   
    
}
