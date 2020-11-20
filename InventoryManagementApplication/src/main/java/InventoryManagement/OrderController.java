package InventoryManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class OrderController {
    // DECLARE VARIABLES
    @FXML private ImageView backButton;
    @FXML private Button addButton;
    @FXML private Button applyButton;
    @FXML private Button clearButton;
    @FXML private Button logoutButton;
    
    // ComboBoxes
    @FXML private ComboBox supplierChoice;
    @FXML private ComboBox userChoice;
    @FXML private ComboBox statusChoice;
    @FXML private ComboBox productChoice;
    @FXML private ComboBox dateChoice;
    
    // date picker
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    
    // table element
    @FXML private TableView<Order> allOrders;
    @FXML private TableColumn<Order, Integer> idColumn;
    @FXML private TableColumn<Order, Supplier> supplierColumn;
    @FXML private TableColumn<Order, Status> statusColumn;
    @FXML private TableColumn<Order, String> dateColumn;
    @FXML private TableColumn<Order, String> totalColumn;
    
    //slider 
    @FXML private Slider priceSlider;
    
    // text
    @FXML private Text minText;
    @FXML private Text maxText;
    @FXML private Text priceText;
    @FXML private Text loginUserText;
    
    // radio
    @FXML private RadioButton priceRadio;
    
    // imageview
    @FXML private ImageView addIcon;
    
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
    
    // HANDLE FUNCTIONALITITES
    @FXML
    private void handleBack() throws IOException {
        moveToDashboard();
    }
    
    @FXML
    private void handleAdd()throws IOException, SQLException {
        int order_id = DatabaseManager.getNewOrderId();
        Common.movePage("editorder", order_id);
    }
    
    @FXML private void handleRadioPrice() {
        if (priceRadio.isSelected()) {
            priceSlider.setDisable(false);
            priceText.setVisible(true);
        } else {
            priceSlider.setDisable(true);
            priceText.setVisible(false);
        }
    }
    
    @FXML
    private void handleApply() throws SQLException {
        // Collect Filter Values
        int supplier = -1, user = -1, product = -1;
        KeyValPair Supplier = ((KeyValPair) supplierChoice.getValue());
        KeyValPair User = ((KeyValPair) userChoice.getValue());
        KeyValPair Product = ((KeyValPair) productChoice.getValue());
        Status status = ((Status) statusChoice.getValue());
            if (Supplier != null) { 
            supplier = Supplier.getKey();
            } else {
            }
            if (User != null) {
            user = User.getKey();
            }
            if (Product != null) {
            product = Product.getKey();
            }
        
        // Collect Date Filter
        String date = ((String) dateChoice.getValue());
        ZoneId defaultZone = ZoneId.systemDefault();
        LocalDate Start = startDate.getValue();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date start, end;
        String start_date = "", end_date= "";
        if (Start != null) {
            start = Date.from(Start.atStartOfDay(defaultZone).toInstant());
            start_date = (formatter.format(start));
        }
        
        LocalDate End = endDate.getValue();
        if (End != null) {
            end = Date.from(End.atStartOfDay(defaultZone).toInstant());
            end_date = (formatter.format(end));
        }
        
        // Collect Price Filter
        double maxPrice;
        if (priceRadio.isSelected()) {
            maxPrice = Float.parseFloat(priceText.getText());
        } else {
            maxPrice = 0.0;
        }
        
        // process the results
        System.out.println("Supplier: " + supplier);
        System.out.println("User: " + user);
        System.out.println("Product: " + product);
        System.out.println("Status: " + status);
        System.out.println("Filter date by: " + date);
        System.out.println("Start date: " + start_date);
        System.out.println("End date: " + end_date);
        System.out.println("Current date: " + Common.currentDate());
        System.out.println("Maximum price: " + Common.doubleStr(maxPrice));
        
        String keyDate = null;
        if (date == "Date Placed") {
            keyDate = "place_date";
        } else if (date == "Date Received") {
            keyDate = "receive_date";
        } else if (date == "Date Packed") {
            keyDate = "packed_date";
        } else if (date == "Date Shipped") {
            keyDate = "shipped_date";
        } else if (date == "Date Delivered") {
            keyDate = "delivered_date";
        }
        
        ObservableList<Order> displayOrder = DatabaseManager.getAllOrdersByFilter(supplier, user, 
                product, status, keyDate, start_date, end_date, maxPrice);
        
        fillTableFilter(displayOrder);
    }
    
    @FXML
    private void handleClear() throws SQLException, IOException {
        Common.resetPage();
    }
    
    @FXML 
    private void handleDateDropdown() {
        startDate.setDisable(false);
        endDate.setDisable(false);
    }
    
    // ADDITIONAL HELPER FUNCTIONS
    @FXML
    private void moveToAdd(int order_id) throws IOException, SQLException {
        DatabaseManager.addOrder(order_id);
        Common.setPage("addorder-" + order_id);
        App.setRoot("addorder");
    }
    
    private void moveToDetail(int order_id) throws IOException {
        Common.setPage("detailorder-" + order_id);
        App.setRoot("detailorder");
    }
    
    private void moveToDashboard() throws IOException {
        Common.movePage("managerdashboard");
    }
    
    //INITIALIZE PAGE 
    public void initialize() throws SQLException {
    loginUserText.setText("Logged in as: " + Common.getUser().getUsername());
    menuBarDisplay();
    // SETUP ROW LISTENER
    allOrders.setRowFactory(tv -> {
        TableRow<Order> row = new TableRow<>();
        row.setOnMouseClicked(event-> {
            if (event.getClickCount() == 2 && (!row.isEmpty())) {
                Order rowData = row.getItem();
                int order_id = rowData.getOrderId();
                try {
                    Common.movePage("detailorder", order_id);
                } catch (IOException ex) {
                    System.out.println("CANNOT MOVE TO ORDER DETAIL");
                    ex.printStackTrace();
                }
            }
        });
        return row;
    });
    
    // DATE FORMAT
    startDate.setConverter(new StringConverter<LocalDate>() {
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
    
    // DATE FORMAT
    endDate.setConverter(new StringConverter<LocalDate>() {
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
        
    // COMMON ACTIONS
    ItemList.emptyList();
    fillDropDownOptions();
    startDate.setDisable(true);
    endDate.setDisable(true);
    priceSlider.valueProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue <?extends Number>observable, Number oldValue, Number newValue){
            priceText.setText(Common.doubleStr(newValue.doubleValue()));
        }
    });
    
    priceSlider.setDisable(true);
    priceText.setVisible(false);
    if((Common.getUser().getStatus()) == Position.MANAGER) {
            fillTableUser();
            fillSliderInfo();
        } else if ((Common.getUser().getStatus()) == Position.EMPLOYEE) {
            fillTableUser();
            fillSliderInfo();
        } else {
            fillTableSupplier();
            fillSliderInfo(Common.getUser().getSuppId());
        }
    }
    
    public void fillTableSupplier() throws SQLException {
        ObservableList<Order> data = FXCollections.observableArrayList();
        data = DatabaseManager.getAllOrdersBySupplier((Common.getUser()).getSuppId());
        
        idColumn.setCellValueFactory(new PropertyValueFactory("orderId"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory("supplier"));
        statusColumn.setCellValueFactory(new PropertyValueFactory("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("editDate"));
        totalColumn.setCellValueFactory(new PropertyValueFactory("PriceString"));
        
        allOrders.setItems(data);
    }
    
    public void fillTableUser() throws SQLException {
        ObservableList<Order> data = DatabaseManager.getAllOrders();
        
        idColumn.setCellValueFactory(new PropertyValueFactory("orderId"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory("supplier"));
        statusColumn.setCellValueFactory(new PropertyValueFactory("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("editDate"));
        totalColumn.setCellValueFactory(new PropertyValueFactory("PriceString"));
        
        allOrders.setItems(data);
    }
    
    public void fillTableFilter(ObservableList<Order> data) throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory("orderId"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory("supplier"));
        statusColumn.setCellValueFactory(new PropertyValueFactory("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory("editDate"));
        totalColumn.setCellValueFactory(new PropertyValueFactory("PriceString"));
        
        allOrders.setItems(data);
    }
    
    public void fillDropDownOptions() throws SQLException {
        // SUPPLIER DROPDOWN OPTION
        if (Common.getUser().getStatus() == Position.SUPPLIER) {
            ObservableList<KeyValPair> currentSupplier = FXCollections.observableArrayList();
            Supplier current = DatabaseManager.getSupplierById(Common.getUser().getSuppId());
            KeyValPair currentSupp = new KeyValPair(current.getId(), current.getName());
            currentSupplier.add(currentSupp);
            supplierChoice.setItems(currentSupplier);
            supplierChoice.setValue(currentSupp);
            supplierChoice.setDisable(true);
        } else {
            ObservableList<KeyValPair> supplierChoices = DatabaseManager.getSuppliers();
            supplierChoice.setItems(supplierChoices);
        }
        
        // USER DROPDOWN
        ObservableList<KeyValPair> userChoices = DatabaseManager.getUsers();
        userChoice.setItems(userChoices);
        
        // STATUS DROPDOWN
        ObservableList<Status> statusChoices = FXCollections.observableArrayList(Status.PLACED, Status.RECEIVED, Status.PACKED, Status.SHIPPED, Status.DELIVERED);
        statusChoice.setItems(statusChoices);
        
        // PRODUCT DROPDOWN
        ObservableList<KeyValPair> productChoices = DatabaseManager.getProducts();
        productChoice.setItems(productChoices);
        
        // FILTER DROPDOWN
        ObservableList<String> filterChoices = FXCollections.observableArrayList("Date Placed", "Date Received", "Date Packed", "Date Shipped", "Date Delivered");
        dateChoice.setItems(filterChoices);
    }
    
    public void fillSliderInfo() throws SQLException {
        double min = DatabaseManager.minOrderPrice();
        double max = DatabaseManager.maxOrderPrice();
        
        
        priceSlider.setMin(min);
        priceSlider.setMax(max);
        
        minText.setText(String.valueOf(Common.doubleStr(min)));
        maxText.setText(String.valueOf(Common.doubleStr(max)));
    }
    
    public void fillSliderInfo(int supp_id) throws SQLException {
        double min = DatabaseManager.minOrderPrice(supp_id);
        double max = DatabaseManager.maxOrderPrice(supp_id);
        
        
        priceSlider.setMin(min);
        priceSlider.setMax(max);
        
        minText.setText(String.valueOf(Common.doubleStr(min)));
        maxText.setText(String.valueOf(Common.doubleStr(max)));
    }
}
