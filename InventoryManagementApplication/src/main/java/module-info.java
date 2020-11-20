module InventoryManagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens InventoryManagement to javafx.fxml;
    exports InventoryManagement;
}
