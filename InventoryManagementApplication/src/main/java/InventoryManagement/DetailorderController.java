package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class DetailorderController {
    // ATTRIBUTE DECLARATIONS
    @FXML private Text idText;
    @FXML private Text supplierText;
    @FXML private Text statusText;
    @FXML private Text priceText;
    @FXML private Text placeUserText;
    @FXML private Text receiveUserText;
    @FXML private Text packedUserText;
    @FXML private Text shippedUserText;
    @FXML private Text deliveredUserText;
    @FXML private Text placeDateText;
    @FXML private Text receiveDateText;
    @FXML private Text packedDateText;
    @FXML private Text shippedDateText;
    @FXML private Text deliveredDateText;
    @FXML private Text loginUserText;
    @FXML private Button editButton;
    @FXML private ImageView backButton;
    @FXML private Button deleteButton;
    @FXML private Button logoutButton;
    @FXML private TableView productTable;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn qtyColumn;
    @FXML private TableColumn unitPriceColumn;
    @FXML private TableColumn unitColumn;
    @FXML private TableColumn totalColumn;
    @FXML private ProgressBar orderStatus;
    
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
    
    // INITIALIZE PAGE
    public void initialize() throws SQLException, IOException, ParseException {
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        setDisplay();
    }
    
    // HANDLE FUNCTIONS
    public void handleBack() throws IOException {
        moveToOrder();
    } 
    
    public void handleEdit() throws IOException {
        Common.movePage("editorder", Common.getIdFromPage());
    }
    
    public void handleDelete() throws SQLException, IOException {
        deleteOrder();
    }
    
    // HELPER FUNCTIONS
    private void displayOrderDetail() throws SQLException, IOException, ParseException {
        int order_id = Common.getIdFromPage();
        Order currentOrder = DatabaseManager.getOrderById(order_id);
        String id = String.valueOf(currentOrder.getOrderId());
        String supplier = currentOrder.getSupplier().getName();
        String status = String.valueOf(currentOrder.getStatus());
        String price = currentOrder.getPriceString();
        
        idText.setText(id);
        supplierText.setText(supplier);
        statusText.setText(status);
        priceText.setText(price);
        
        setUserDateText(currentOrder);
        setProgressBar(currentOrder.getStatus());
    }
    
    private void setUserDateText(Order order) throws SQLException, IOException, ParseException {
        Status status = order.getStatus();
        if (status == Status.PLACED) {
            placeUserText.setText(DatabaseManager.getUserById(order.getPlaceUser()).getUsername());
            placeDateText.setText(order.getPlaceDate());
            
            placeUserText.setVisible(true);
            placeDateText.setVisible(true);
        } else if (status == Status.RECEIVED) {
            placeUserText.setText(DatabaseManager.getUserById(order.getPlaceUser()).getUsername());
            placeDateText.setText(order.getPlaceDate());
            receiveUserText.setText(DatabaseManager.getUserById(order.getReceivedUser()).getUsername());
            receiveDateText.setText(order.getReceivedDate());
            
            placeUserText.setVisible(true);
            placeDateText.setVisible(true);
            receiveUserText.setVisible(true);
            receiveDateText.setVisible(true);
        } else if (status == Status.PACKED) {
            placeUserText.setText(DatabaseManager.getUserById(order.getPlaceUser()).getUsername());
            placeDateText.setText(order.getPlaceDate());
            receiveUserText.setText(DatabaseManager.getUserById(order.getReceivedUser()).getUsername());
            receiveDateText.setText(order.getReceivedDate());
            packedUserText.setText(DatabaseManager.getUserById(order.getPackedUser()).getUsername());
            packedDateText.setText(order.getPackedDate());
            
            placeUserText.setVisible(true);
            placeDateText.setVisible(true);
            receiveUserText.setVisible(true);
            receiveDateText.setVisible(true);
            packedUserText.setVisible(true);
            packedDateText.setVisible(true);
        } else if (status == Status.SHIPPED) {
            placeUserText.setText(DatabaseManager.getUserById(order.getPlaceUser()).getUsername());
            placeDateText.setText(order.getPlaceDate());
            receiveUserText.setText(DatabaseManager.getUserById(order.getReceivedUser()).getUsername());
            receiveDateText.setText(order.getReceivedDate());
            packedUserText.setText(DatabaseManager.getUserById(order.getPackedUser()).getUsername());
            packedDateText.setText(order.getPackedDate());
            shippedUserText.setText(DatabaseManager.getUserById(order.getShippedUser()).getUsername());
            shippedDateText.setText(order.getShippedDate());
            
            placeUserText.setVisible(true);
            placeDateText.setVisible(true);
            receiveUserText.setVisible(true);
            receiveDateText.setVisible(true);
            packedUserText.setVisible(true);
            packedDateText.setVisible(true);
            shippedUserText.setVisible(true);
            shippedDateText.setVisible(true);
        } else if (status == Status.DELIVERED) {
            placeUserText.setText(DatabaseManager.getUserById(order.getPlaceUser()).getUsername());
            placeDateText.setText(order.getPlaceDate());
            receiveUserText.setText(DatabaseManager.getUserById(order.getReceivedUser()).getUsername());
            receiveDateText.setText(order.getReceivedDate());
            packedUserText.setText(DatabaseManager.getUserById(order.getPackedUser()).getUsername());
            packedDateText.setText(order.getPackedDate());
            shippedUserText.setText(DatabaseManager.getUserById(order.getShippedUser()).getUsername());
            shippedDateText.setText(order.getShippedDate());
            deliveredUserText.setText(DatabaseManager.getUserById(order.getDeliveredUser()).getUsername());
            deliveredDateText.setText(order.getDeliveredDate());
            
            placeUserText.setVisible(true);
            placeDateText.setVisible(true);
            receiveUserText.setVisible(true);
            receiveDateText.setVisible(true);
            packedUserText.setVisible(true);
            packedDateText.setVisible(true);
            shippedUserText.setVisible(true);
            shippedDateText.setVisible(true);
            deliveredUserText.setVisible(true);
            deliveredDateText.setVisible(true);
        } else {
            DatabaseManager.removeOrder(Common.getIdFromPage());
            moveToOrder();
        }
    }
    
    private void setProgressBar(Status status) throws SQLException, IOException {
        switch(status) {
            case PLACED: orderStatus.setProgress(0); break;
            case RECEIVED: orderStatus.setProgress(0.25); break;
            case PACKED: orderStatus.setProgress(0.5); break;
            case SHIPPED: orderStatus.setProgress(0.75); break;
            case DELIVERED: orderStatus.setProgress(1); break;
            default: {
                DatabaseManager.removeOrder(Common.getIdFromPage());
                moveToOrder();
                break;
            }
        }
    }
    
    private void displayItemTable() throws SQLException {
        ObservableList<OrderItem> orderItems = DatabaseManager.getAllOrderItem(Common.getIdFromPage());
        
        nameColumn.setCellValueFactory(new PropertyValueFactory("ProductName"));
        unitColumn.setCellValueFactory(new PropertyValueFactory("Unit"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory("Quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory("PriceString"));
        totalColumn.setCellValueFactory(new PropertyValueFactory("TotalString"));
        
        productTable.setItems(orderItems);
    }
    
    private void setDisplay() throws SQLException, IOException, ParseException {
        displayOrderDetail();
        displayItemTable();
    }
    
    private void deleteOrder() throws SQLException, IOException {
        DatabaseManager.removeOrder(Common.getIdFromPage());
        DatabaseManager.removeAllOrderItem(Common.getIdFromPage());
        moveToOrder();
    }
    
    private void moveToOrder() throws IOException {
        Common.movePage("order");
    }
}
