package InventoryManagement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ManagerdashboardController {
    @FXML private Button supplierButton;
    @FXML private Button userButton;
    @FXML private PieChart orderStatusPie;
    @FXML private Label caption;
    @FXML private Text titleText;
    @FXML private Text loginUserText;
    @FXML private Text tableTitle;
    @FXML private Text monthlyStatHeading;
    @FXML private TableView recentOrders;
    @FXML private TableColumn orderIdCol;
    @FXML private TableColumn suppIdCol;
    @FXML private TableColumn statusCol;
    @FXML private TableColumn editDateCol;
    @FXML private TableColumn totalCol;
    @FXML private LineChart<String, Number> monthlyOrder;
    
    public void initialize () throws SQLException, IOException {
        User currentUser = Common.getUser();
        String user = "Logged in as: " + currentUser.getUsername();
        loginUserText.setText(user);
        displayPieChart();
        displayRecentOrders();
        menuBarDisplay();
        displayLineGraph();

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
            titleText.setText("MANAGER DASHBOARD");
            tableTitle.setText("Recent Updated");
            monthlyStatHeading.setText("Recent Expense From Order Placed");
        } else if (currentUser.getStatus() == Position.EMPLOYEE) {
            titleText.setText("EMPLOYEE DASHBOARD");
            tableTitle.setText("Recently Updated");
            monthlyStatHeading.setText("Recent Expense From Order Placed");
            userButton.setVisible(false);
        } else if (currentUser.getStatus() == Position.SUPPLIER) {
            titleText.setText("SUPPLIER DASHBOARD: " + DatabaseManager.getSupplierById(currentUser.getSuppId()).getName().toUpperCase());
            tableTitle.setText("Recently Assigned");
            monthlyStatHeading.setText("Recent Revenue From Order Delivered");
            userButton.setVisible(false);
            supplierButton.setVisible(false);
        }
    }
    // END MENU BAR HANDLE
    
    // HELPER FUNCTIONS
    private void displayPieChart() throws IOException, SQLException {
        ObservableList<PieChart.Data> pieChartData = DatabaseManager.pieChartData(Common.getUser());
        orderStatusPie.setData(pieChartData);
        orderStatusPie.setLabelsVisible(true);
        
        for (PieChart.Data data : orderStatusPie.getData()) {
            System.out.println(data.getPieValue());
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
            e -> {
                double total = 0.0;
                for (PieChart.Data d : orderStatusPie.getData()) {
                        total += d.getPieValue();
                    }
                caption.setLayoutX(e.getSceneX());
                caption.setLayoutY(e.getSceneY());
                String text = String.format("%.1f%%", 100*data.getPieValue()/total) ;
                caption.setText(text);
            });
        }
    }
    
    private void displayRecentOrders() throws SQLException {
        ObservableList<Order> data = DatabaseManager.get10RecentOrders(Common.getUser());
        
        orderIdCol.setCellValueFactory(new PropertyValueFactory("orderId"));
        suppIdCol.setCellValueFactory(new PropertyValueFactory("supplier"));
        statusCol.setCellValueFactory(new PropertyValueFactory("status"));
        editDateCol.setCellValueFactory(new PropertyValueFactory("editDate"));
        totalCol.setCellValueFactory(new PropertyValueFactory("PriceString"));
        
        recentOrders.setItems(data);
    }
    
    private void displayLineGraph() throws SQLException {
        String current = Common.currentDate();
        int year = Integer.parseInt(current.substring(0, 4));
        int month = Integer.parseInt(current.substring(5, 7));
        System.out.println(year + "-" + month);
        
        XYChart.Series<String, Number> test = DatabaseManager.getLineGraph(Common.getUser(), year, month);
        monthlyOrder.getData().add(test);
        monthlyOrder.setLegendVisible(false);
    }
}
