package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class EditorderController {
    // ATTRIBUTE DECLARATIONS
    @FXML private Label prevTotalLabel;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Button placeButton;
    @FXML private Button logoutButton;
    @FXML private ImageView deleteItemIcon;
    @FXML private ImageView addItemIcon;
    @FXML private Text orderNumberText;
    @FXML private Text prevTotalText;
    @FXML private Text newTotalText;
    @FXML private Text errorText;
    @FXML private Text loginUserText;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn qtyColumn;
    @FXML private TableColumn unitColumn;
    @FXML private TableColumn unitPriceColumn;
    @FXML private TableColumn totalColumn;
    @FXML private TableView itemTable;
    @FXML private ToggleButton statusButton;
    @FXML private ComboBox supplierChoice;
    @FXML private DatePicker statusDate;
    
    private ObservableList<OrderItem> allItems;
    
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
    
    //INITIALIZE PAGE 
    @FXML
    public void initialize() throws SQLException {
        loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
        menuBarDisplay();
        if (messageFound()) {
            retreiveMessage();
        }
        
        statusDate.setVisible(false);
        
        // DATE FORMAT
        statusDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

            @Override 
            public String toString(LocalDate date) {
                if (date != null) {
                    return stringFormatter.format(date).replace(".", "");
                } else {
                    return "";
                }
            }

            @Override 
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        
        if (isAddOrder()) {
            displayItemTable(getAddOrderTableData());
            displayAddOrderTexts();
            displayAddOrderButtons();
            displayAddDropdown();
            displayDatePicker();
        } else {
            displayItemTable(getEditOrderTableData());
            displayEditOrderTexts();
            displayEditOrderButtons();
            displayEditDropdown();
        }
    }
    
    //HANDLE FUNCTIONS
    @FXML
    private void handleBack() throws IOException {
        Common.movePage("order");
    }
    
    @FXML
    private void handleAdd() throws IOException {
        Common.movePage("additem", Common.getIdFromPage());
    }
    
    @FXML
    private void handleCancel() throws IOException, SQLException {
        Common.movePage("order");
    }
    
    @FXML
    private void handlePlace() throws IOException, SQLException {
        if (!errorExist()) {
            placeOrder();
            Common.movePage("order");
        }
    }
    
    @FXML
    private void handleUpdate() throws IOException, SQLException, ParseException {
        if (!errorExist()) {
           if (updateOrder()) {
               Common.movePage("order");
           }
        }
    }
    
    @FXML
    private void handleDelete() throws IOException, SQLException {
        
        deleteItem();
    }
    
    @FXML
    private void handleStatus() {
        if (statusButton.isSelected()) {
            statusDate.setDisable(false);
        } else {
            statusDate.setDisable(true);
        }
    }
    
    // HELPER FUNCTIONS
    private void deleteItem() throws IOException, SQLException {
        OrderItem item = (OrderItem) itemTable.getSelectionModel().getSelectedItem();
        
        if (item != null) {
            if (isAddOrder()) {
                ItemList.deleteFromList(item);
            } else {
                ItemList.deleteItem(item);
            }
        } else {
            return;
        }
        
        Common.resetPage();
    }
    
    private String calculatePrice() {
        double totalPrice = 0.0;
        for (OrderItem entry : allItems) {
            totalPrice = totalPrice + entry.getTotal();
        }
        return Common.doubleStr(totalPrice);
    }
    
    private boolean errorExist() {
        errorText.setVisible(false);
        KeyValPair output = (KeyValPair) supplierChoice.getValue();
        if (output == null) {
            errorText.setText("select a supplier!");
            errorText.setVisible(true);
            return true;
        } 
        return false;
    }
    
    private void placeOrder() throws SQLException {
        int order_id = Common.getIdFromPage();
        DatabaseManager.addOrder(order_id);
        for (OrderItem item : allItems) {
            DatabaseManager.addItemToOrder(order_id, item.getProduct(), item.getQuantity());
        }
        double newPrice = DatabaseManager.orderTotal(order_id);
        
        KeyValPair supplier = (KeyValPair) supplierChoice.getValue();
        DatabaseManager.assignOrder(Common.getIdFromPage(), supplier.getKey());
        DatabaseManager.placeOrder(Common.getIdFromPage(), Common.currentDate(), Common.getUser().getUserId(), newPrice);
    }
    
    private boolean updateOrder() throws SQLException {
        int order_id = Common.getIdFromPage();
        DatabaseManager.removeAllOrderItem(order_id);
        for (OrderItem item : allItems) {
            DatabaseManager.addItemToOrder(order_id, item.getProduct(), item.getQuantity());
        }
        double newPrice = DatabaseManager.orderTotal(order_id);
        DatabaseManager.updateOrderPrice (order_id, newPrice);
        
        KeyValPair supplier = (KeyValPair) supplierChoice.getValue();
        DatabaseManager.assignOrder(Common.getIdFromPage(), supplier.getKey());
        
        if (statusButton.isSelected()) {
            Order current = DatabaseManager.getOrderById(Common.getIdFromPage());
            if (current.getStatus() == Status.PLACED) {
                if (DatabaseManager.getOrderItemCount(order_id) == 0) {
                    errorText.setText("do not take on empty orders");
                    errorText.setVisible(true);
                    return false;
                } else {
                    DatabaseManager.receiveOrder(order_id, Common.currentDate(), Common.getUser().getUserId());
                }
            } else if (current.getStatus() == Status.RECEIVED) {
                DatabaseManager.packedOrder(order_id, Common.currentDate(), Common.getUser().getUserId());
            } else if (current.getStatus() == Status.PACKED) {
                DatabaseManager.shippedOrder(order_id, Common.currentDate(), Common.getUser().getUserId());
            } else if (current.getStatus() == Status.SHIPPED) {
                DatabaseManager.deliveredOrder(order_id, Common.currentDate(), Common.getUser().getUserId());
            } else {
                statusButton.setVisible(false);
            }
        }
        return true;
    }
    
    private boolean updateOrderWithDate() throws SQLException, ParseException {
        int order_id = Common.getIdFromPage();
        DatabaseManager.removeAllOrderItem(order_id);
        for (OrderItem item : allItems) {
            DatabaseManager.addItemToOrder(order_id, item.getProduct(), item.getQuantity());
        }
        double newPrice = DatabaseManager.orderTotal(order_id);
        DatabaseManager.updateOrderPrice (order_id, newPrice);
        
        KeyValPair supplier = (KeyValPair) supplierChoice.getValue();
        DatabaseManager.assignOrder(Common.getIdFromPage(), supplier.getKey());
        
        // format input date
        LocalDate inputDate = statusDate.getValue();
        ZoneId defaultZone = ZoneId.systemDefault();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date input;
        String input_date= "";
        if (inputDate != null) {
            input = Date.from(inputDate.atStartOfDay(defaultZone).toInstant());
            input_date = (formatter.format(input));
        } 
        Order current = DatabaseManager.getOrderById(Common.getIdFromPage());
        
        if (input_date.compareTo(Common.currentDate()) < 0) {
            errorText.setText("status cannot be changed to before the current date");
            errorText.setVisible(true);
            return false; // input date must be larger
        }
        
        if (statusButton.isSelected()) {
            if (current.getStatus() == Status.PLACED) {
                if (input_date.compareTo(current.getPlaceDateForm()) < 0) {
                    errorText.setText("status cannot be changed to before the placed date");
                    errorText.setVisible(true);
                    statusDate.setValue(Common.getDate(current.getPlaceDateForm()));
                    return false;
                } else {
                    DatabaseManager.receiveOrder(order_id, input_date, Common.getUser().getUserId());
                }
            } else if (current.getStatus() == Status.RECEIVED) {
                if (input_date.compareTo(current.getReceivedDateForm()) < 0) {
                    errorText.setText("status cannot be changed to before the received date");
                    errorText.setVisible(true);
                    statusDate.setValue(Common.getDate(current.getReceivedDateForm()));
                    return false;
                } else {
                    DatabaseManager.packedOrder(order_id, input_date, Common.getUser().getUserId());
                }
            } else if (current.getStatus() == Status.PACKED) {
                if (input_date.compareTo(current.getPackedDateForm()) < 0) {
                    errorText.setText("status cannot be changed to before the packed date");
                    errorText.setVisible(true);
                    statusDate.setValue(Common.getDate(current.getPackedDateForm()));
                    return false;
                } else {
                    DatabaseManager.shippedOrder(order_id, input_date, Common.getUser().getUserId());
                }
            } else if (current.getStatus() == Status.SHIPPED) {
                if (input_date.compareTo(current.getShippedDateForm()) < 0) {
                    errorText.setText("status cannot be changed to before the delivered date");
                    errorText.setVisible(true);
                    statusDate.setValue(Common.getDate(current.getShippedDateForm()));
                    return false;
                } else {
                    DatabaseManager.deliveredOrder(order_id, input_date, Common.getUser().getUserId());
                }
            } else {
                statusButton.setVisible(false);
            }
        }
        return true;
    }
    
    private boolean messageFound() {
        if (Message.messageExist()) {
            return true;
        } else {
            return false;
        }
    }
    
    private void retreiveMessage() {
        OrderItem item = (OrderItem) Message.receiveMessage();
        addOrderToList(item);
    }
    
    private void addOrderToList(OrderItem item) {
        ItemList.addItem(item);
    }
    
    private boolean isAddOrder() throws SQLException {
        if (DatabaseManager.orderExist(Common.getIdFromPage())) {
            return false;
        }
        return true;
    }
    
    private void displayItemTable(ObservableList<OrderItem> orderItems) {
        nameColumn.setCellValueFactory(new PropertyValueFactory("ProductName"));
        unitColumn.setCellValueFactory(new PropertyValueFactory("Unit"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory("Quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory("TotalString"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory("PriceString"));
        itemTable.setItems(orderItems);
    }
    
    private ObservableList<OrderItem> getAddOrderTableData() {
        allItems = FXCollections.observableArrayList();
        ArrayList<OrderItem> newItems = new ArrayList<OrderItem>();
        newItems = ItemList.getAddList();

        for (OrderItem entry : newItems) {
            boolean added = false;
            for (OrderItem item : allItems) {
                if (item.productEquals(entry)) {
                    item.setQuantity(item.getQuantity() + entry.getQuantity());
                    added = true;
                }
            }
            if (!added) {
                allItems.add(entry);
            }
        }
        return allItems;
    }
    
    private ObservableList<OrderItem> getEditOrderTableData() throws SQLException {
        allItems = FXCollections.observableArrayList();
        allItems = DatabaseManager.getAllOrderItem(Common.getIdFromPage());
        
//        ArrayList<OrderItem> delItems = new ArrayList<OrderItem>();
//        delItems = ItemList.getDelList();
        
        ArrayList<OrderItem> newItems = new ArrayList<OrderItem>();
        newItems = ItemList.getAddList();
        
        for (OrderItem entry : newItems) {
            boolean found = false;
            for (OrderItem item : allItems) {
                if (item.productEquals(entry)) {
                    item.setQuantity(item.getQuantity() + entry.getQuantity());
                    found = true;
                } 
            }
            if (!found) {
                allItems.add(entry);
            }
        }
        
//        for (OrderItem entry : delItems) {
//            boolean reduced = false;
//            for (OrderItem item : allItems) {
//                if (item.productEquals(entry)) {
//                    item.setQuantity(item.getQuantity() - entry.getQuantity());
//                    reduced = true;
//                    break;
//                } 
//            }
//        }
        
        allItems.removeIf(entry->entry.getQuantity() == 0);
        return allItems;
    }
    
    private void displayAddOrderTexts() {
        String orderNo = String.valueOf(Common.getIdFromPage());
        orderNumberText.setText(orderNo);
        String price = calculatePrice();;
        newTotalText.setText(price);
        prevTotalText.setVisible(false);
        prevTotalLabel.setVisible(false);
    }
    
    private void displayEditOrderTexts() throws SQLException {
        String orderNo = String.valueOf(Common.getIdFromPage());
        orderNumberText.setText(orderNo);
        
        Order current = DatabaseManager.getOrderById(Common.getIdFromPage());
        if (current.getStatus() != Status.PLACED) {
            prevTotalText.setVisible(false);
            prevTotalLabel.setVisible(false);
        }
        String oldPrice = DatabaseManager.orderTotalString(Common.getIdFromPage());
        String newPrice = calculatePrice();
        prevTotalText.setText(oldPrice);
        newTotalText.setText(newPrice);
    }
    
    private void displayAddOrderButtons() {
        updateButton.setVisible(false);
        statusButton.setVisible(false);
        statusButton.setSelected(false);
        if (Common.getUser().getStatus() == Position.SUPPLIER) {
            addButton.setVisible(false);
            addItemIcon.setVisible(false);
            deleteButton.setVisible(false);
            deleteItemIcon.setVisible(false);
        }
    }
    
    private void displayEditOrderButtons() throws SQLException {
        placeButton.setVisible(false);
        statusButton.setVisible(false);
        statusButton.setSelected(false);
        deleteButton.setVisible(false);
        deleteItemIcon.setVisible(false);
        
        Order current = DatabaseManager.getOrderById(Common.getIdFromPage());
        boolean isSupplier = Common.getUser().getStatus() == Position.SUPPLIER;
        
        if (current.getStatus() == Status.PLACED && isSupplier) {
            statusButton.setText("RECEIVED");
            statusButton.setVisible(true);
        } else if (current.getStatus() == Status.RECEIVED && isSupplier) {
            statusButton.setText("PACKED");
            statusButton.setVisible(true);
        } else if (current.getStatus() == Status.PACKED && isSupplier) {
            statusButton.setText("SHIPPED");
            statusButton.setVisible(true);
        } else if (current.getStatus() == Status.SHIPPED && !(isSupplier)) {
            statusButton.setText("DELIVERED");
            statusButton.setVisible(true);
        } else {
            statusButton.setVisible(false);
        }
        if (isSupplier) {
            addButton.setVisible(false);
            addItemIcon.setVisible(false);
            //deleteButton.setVisible(false);
            //deleteItemIcon.setVisible(false);
        } else if (current.getStatus() == Status.PLACED && !(isSupplier)) {
            addButton.setVisible(true);
            addItemIcon.setVisible(true);
            //deleteButton.setVisible(true);
            //deleteItemIcon.setVisible(false);
        } else {
            addButton.setVisible(false);
            addItemIcon.setVisible(false);
            //deleteButton.setVisible(false);
            //deleteItemIcon.setVisible(false);
        }
    }
    
    private void displayAddDropdown() throws SQLException {
        ObservableList<KeyValPair> supplierChoices = DatabaseManager.getSuppliers();
        supplierChoice.setItems(supplierChoices);
    }
    
    private void displayEditDropdown() throws SQLException {
        Order current = DatabaseManager.getOrderById(Common.getIdFromPage());
        Supplier supplier = current.getSupplier();
        
        ObservableList<KeyValPair> supplierChoices = DatabaseManager.getSuppliers();
        supplierChoice.setItems(supplierChoices);
        
        KeyValPair currentSupplier = new KeyValPair(supplier.getId(), supplier.getName());
        supplierChoice.setValue(currentSupplier);
        
        if (current.getStatus() != Status.PLACED || Common.getUser().getStatus() == Position.SUPPLIER) {
            supplierChoice.setDisable(true);
        }
    }
    
    // ONLY SHOWS UP AT EDIT 
    private void displayDatePicker() {
        if(statusButton.isVisible()) {
            statusDate.setVisible(true);
        } else {
            statusDate.setVisible(false);
        }
        
        if (statusButton.isSelected()) {
            statusDate.setDisable(false);
        } else {
            statusDate.setDisable(true);
        }
        statusDate.setValue(LocalDate.now());
    }
}
