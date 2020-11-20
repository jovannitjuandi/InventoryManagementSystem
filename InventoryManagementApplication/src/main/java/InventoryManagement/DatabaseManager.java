package InventoryManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class DatabaseManager {
    // CURRENT CONNECTION
    private static Connection conn;
    
    // DATABASE NAMES
    private static final String SHOP_INFO = "Store";
    private static final String ALL_USERS = "Users";
    private static final String SUPPLIERS = "Suppliers";
    private static final String ALL_PRODS = "Products";
    private static final String PROD_TYPE = "ProductType";
    private static final String ALL_ORDER = "Orders";
    private static final String PROD_ORDS = "ProductOrderJoin";
    private static final String DB_NAME = "Database";
    
    // OPEN CONNECTION TO SPECIFIED DATABASE
    public static void openConnection(String DBname) throws SQLException {
        String query = "jdbc:sqlite:" + DBname + ".db";
        DatabaseManager.conn = DriverManager.getConnection(query);
        conn.createStatement().execute("PRAGMA foreign_keys = ON");
    }
    
    // CREATE TABLES 
    private static void createStoreTable() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String createStatement = "CREATE TABLE IF NOT EXISTS " + SHOP_INFO + 
                "(shop_id INTEGER PRIMARY KEY autoincrement, " + 
                "name TEXT NOT NULL, " +
                "manager_id INTEGER, " +
                "FOREIGN KEY (manager_id) REFERENCES " + ALL_USERS + "(user_id) ON DELETE SET NULL);";
        
        st.execute(createStatement);
        
        st.close();
        conn.close();
    }
    
    private static void createSuppTable() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String createStatement = "CREATE TABLE IF NOT EXISTS " + SUPPLIERS + 
                "(supp_id INTEGER PRIMARY KEY autoincrement, " + 
                "name TEXT NOT NULL, " +
                "contact TEXT NOT NULL, " +
                "street TEXT NOT NULL, " +
                "suburb TEXT NOT NULL, " +
                "state TEXT NOT NULL, " +
                "postcode TEXT NOT NULL, " +
                "country TEXT NOT NULL);";
        st.execute(createStatement);
        
        st.close();
        conn.close();
    }
    
    private static void createUserTable() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String createStatement = "CREATE TABLE IF NOT EXISTS " + ALL_USERS + 
                "(user_id INTEGER PRIMARY KEY autoincrement, " + 
                "name TEXT NOT NULL, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "supp_id INTEGER DEFAULT NULL, " +
                "approved INTEGER DEFAULT 0 CHECK (approved IN (0,1)), " +
                "FOREIGN KEY(supp_id) REFERENCES " + SUPPLIERS + "(supp_id) ON DELETE CASCADE);";
        st.execute(createStatement);
        
        st.close();
        conn.close();
    }
    
    private static void createProdTable() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String createStatement = "CREATE TABLE IF NOT EXISTS " + PROD_TYPE + 
                "(type_id INTEGER PRIMARY KEY autoincrement, " + 
                "type TEXT NOT NULL);";
        st.execute(createStatement);
        
        openConnection(DB_NAME);
        st = conn.createStatement();
        
        createStatement = "CREATE TABLE IF NOT EXISTS " + ALL_PRODS + 
                "(prod_id INTEGER PRIMARY KEY autoincrement, " + 
                "name TEXT NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "price DECIMAL NOT NULL, " +
                "type_id INTEGER NOT NULL, " +
                "available INTEGER DEFAULT 1 CHECK (available IN (0,1)), " +
                "FOREIGN KEY(type_id) REFERENCES " + PROD_TYPE + "(type_id) ON DELETE CASCADE);";
        st.execute(createStatement);
        
        st.close();
        conn.close();
    }
    
    private static void createOrderTable() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String createStatement = "CREATE TABLE IF NOT EXISTS " + ALL_ORDER + 
                "(order_id INTEGER PRIMARY KEY autoincrement NOT NULL, " + 
                "supp_id INTEGER, " +
                "status TEXT DEFAULT 'PLACED', " +
                "place_date TEXT, " + "place_user INTEGER, " +
                "receive_date TEXT, " + "receive_user INTEGER, " +
                "packed_date TEXT, " + "packed_user INTEGER, " +
                "shipped_date TEXT, " + "shipped_user INTEGER, " +
                "delivered_date TEXT, " + "delivered_user INTEGER, " +
                "price DECIMAL DEFAULT 0.0, " + "edit_date TEXT, " +
                "FOREIGN KEY (place_user) REFERENCES " + ALL_USERS + "(user_id) ON DELETE SET NULL, " +
                "FOREIGN KEY (receive_user) REFERENCES " + ALL_USERS + "(user_id) ON DELETE SET NULL, " +
                "FOREIGN KEY (packed_user) REFERENCES " + ALL_USERS + "(user_id) ON DELETE SET NULL, " +
                "FOREIGN KEY (shipped_user) REFERENCES " + ALL_USERS + "(user_id) ON DELETE SET NULL, " +
                "FOREIGN KEY (delivered_user) REFERENCES " + ALL_USERS + "(user_id) ON DELETE SET NULL, " +
                "FOREIGN KEY(supp_id) REFERENCES " + SUPPLIERS + "(supp_id) ON DELETE SET NULL);";
        st.execute(createStatement);
        
        st.close();
        conn.close();
    }
    
    private static void createJoinTable() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String createStatement = "CREATE TABLE IF NOT EXISTS " + PROD_ORDS + 
                "(join_id INTEGER PRIMARY KEY autoincrement, " + 
                "order_id INTEGER NOT NULL, " +
                "prod_id INTEGER NOT NULL, " +
                "prod_qty INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(order_id) REFERENCES " + ALL_ORDER + "(order_id) ON DELETE CASCADE, " +
                "FOREIGN KEY(prod_id) REFERENCES " + ALL_PRODS + "(prod_id) ON DELETE CASCADE);";
        st.execute(createStatement);
        
        st.close();
        conn.close();
    }
    
    public static void createTables() throws SQLException {
        createStoreTable();
        createSuppTable();
        createUserTable();
        createProdTable();
        createOrderTable();
        createJoinTable();
    }
    
    public static void populateTables() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        ArrayList<String> insertStatements = new ArrayList<String>();
        String insertStatement;
        
        // INSERTING SUPPLIERS
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('bonds', '1800 806 906', '190 dunmore street', 'wentworthville', 'nsw', '2145', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('bundaberg', '1800 777 097', '147 wharf street', 'spring hill', 'qld', '4000', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('kellogg''s', '1800 000 474', '41 wentworth avenue', 'pagewood', 'nsw', '2035', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('unilever', '1800 888 449', '17 park street', 'sydney', 'nsw', '2000', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('greenspoon', '1800 987 075', '6 sarton road', 'clayton', 'vic', '3168', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('heinz', '1800 037 058', '99 beacon road', 'port melbourne', 'vic', '3207', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('arnott''s', '1800 994 824', '240 bunda street', 'canberra', 'act', '2601', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('p&g', '1800 554 345', '90 douglas avenue', 'south perth', 'wa', '6151', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('masterfoods', '1800 085 368', '1 hindley street', 'adelaide', 'sa', '5000', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('sara lee', '1800 235 784', '37 ryde road', 'pymble', 'nsw', '2073', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('cadbury', '1800 359 857', '323 canterbury road', 'ringwood', 'vic', '3134', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('markets', '1800 772 557', '26 holthouse road', 'hungerford', 'sa', '5222', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('boost', '1800 357 219', '59 barker street', 'piesseville', 'wa', '6315', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, suburb, state, postcode, country) " + "VALUES ('twinning', '1800 472 480', '79 quay street', 'haymarket', 'nsw', '2000', 'australia');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // INSERTING USERS
        // manager
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, approved) " + "VALUES ('manager', 'manager', 'manager', 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        // shop users
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, approved) " + "VALUES ('devesh mitra', 'devesh', 'devesh', 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, approved) " + "VALUES ('sarah parkson', 'sarah', 'sarah', 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, approved) " + "VALUES ('jake jakie', 'jake', 'jake', 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, approved) " + "VALUES ('employee', 'employee', 'employee', 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, approved) " + "VALUES ('sheldon cooper', 'sheldon', 'sheldon', 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password) " + "VALUES ('bernard stewart', 'bernard', 'bernard');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password) " + "VALUES ('white icebear', 'icebear', 'icebear');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password) " + "VALUES ('amy fowler', 'amy', 'amy');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password) " + "VALUES ('jai croll', 'jai', 'jai');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        // supplier users
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('poppie reman', 'pop', 'pop', 1, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('king kong', 'king', 'king', 1, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('harry potter', 'harry', 'harry', 1, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('bonds', 'bonds', 'bonds', 1, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('bruce wayne', 'batman', 'batman', 6, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('dick grayson', 'robin', 'robin', 6, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('alfred buttler', 'alfred', 'alfred', 6, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('heinz', 'heinz', 'heinz', 6, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('ron weasley', 'ron', 'rom', 13, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('luna lovegood', 'luna', 'luna', 13, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('bilbo baggins', 'bilbo', 'bilbo', 13, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('boost', 'boost', 'boost', 13, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('cadbury', 'cadbury', 'cadbury', 11, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('farmers', 'farmers', 'farmers', 11, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('twinning', 'twinning', 'twinning', 14, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id, approved) " + "VALUES ('twg', 'twg', 'twg', 14, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // INSERTING PRODUCT TYPE
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('fruits');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('poultry');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('vegetables');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('dairy');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('lunch');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('containers');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('breakfast');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) " + "VALUES ('snacks');";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // INSERTING PRODUCT
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('avocado', 'ea', 3.2, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('red grapes', 'pkg', 10.0, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('green grapes', 'pkg', 12.4, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('black grapes', 'pkg', 11.2, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('bananas', 'kg', 2.2, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('apples', 'kg', 4.3, 1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('yoghurt', 'tb', 1.8, 4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('milk', 'ltr', 4.6, 4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('cheese', 'pkg', 5.2, 4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('pork', 'kg', 13.2, 2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('beef', 'kg', 11.5, 2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('chicken', 'kg', 10.4, 2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('green tea', 'box', 4.5, 7);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('black tea', 'box', 4.3, 7);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('pasta', 'pkg', 6.7, 5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('rice', 'kg', 7.3, 5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('spinach', 'pkg', 3.1, 3);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('carrot', 'kg', 1.4, 3);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('popcorn', 'bag', 7.9, 8);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) " + "VALUES ('chips', 'ea', 7.1, 8);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // INSERTING ORDERS
        // placed order, user has to be approved, and place_user working in shop not supplier
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (1, 'PLACED', '2020-11-08', 1, '2020-11-08', 352.2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (1, 'PLACED', '2020-10-03', 1, '2020-10-03', 607.0);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (6, 'PLACED', '2020-10-05', 2, '2020-10-05', 538.8);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (6, 'PLACED', '2020-09-08', 2, '2020-09-08', 379.3);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (11, 'PLACED', '2020-09-03', 3, '2020-09-03', 286.4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (13, 'PLACED', '2020-08-20', 3, '2020-08-20', 388.1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (13, 'PLACED', '2020-08-15', 4, '2020-08-15', 345.6);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (13, 'PLACED', '2020-07-08', 4, '2020-07-08', 244.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, edit_date, price) " + "VALUES (14, 'PLACED', '2020-06-08', 5, '2020-06-08', 226.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // received, receive_user has to be from the same supplier as the supp_id, date has to be after place_date
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (1, 'RECEIVED', '2020-11-08', 1, '2020-11-09', 13, '2020-11-09', 509.1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (1, 'RECEIVED', '2020-10-03', 1, '2020-10-04', 13, '2020-10-04', 627.2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (6, 'RECEIVED', '2020-10-05', 3, '2020-10-07', 18, '2020-10-07', 418.4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (6, 'RECEIVED', '2020-09-08', 2, '2020-09-09', 18, '2020-09-09', 391.8);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (11, 'RECEIVED', '2020-09-03', 4, '2020-09-04', 23, '2020-09-04', 419.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (13, 'RECEIVED', '2020-08-20', 2, '2020-08-21', 20, '2020-08-21', 270.2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (13, 'RECEIVED', '2020-08-15', 2, '2020-08-16', 19, '2020-08-16', 291.4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (13, 'RECEIVED', '2020-07-08', 1, '2020-07-09', 20, '2020-07-09', 416.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, edit_date, price) " + "VALUES (14, 'RECEIVED', '2020-06-08', 2, '2020-06-09', 25, '2020-06-09', 478.3);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // packed, packed_user has to be from same supplier as supp_id and date has to be after received
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (1, 'PACKED', '2020-11-08', 1, '2020-11-09', 13, '2020-11-10', 14, '2020-11-10', 385.4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (1, 'PACKED', '2020-10-03', 1, '2020-10-04', 13, '2020-10-05', 14, '2020-10-05', 472);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (6, 'PACKED', '2020-10-05', 2, '2020-10-07', 18, '2020-10-18', 15, '2020-10-18', 617.2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (11, 'PACKED', '2020-09-08', 3, '2020-09-09', 23, '2020-09-11', 24, '2020-09-11', 370.8);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (13, 'PACKED', '2020-09-03', 1, '2020-09-04', 19, '2020-09-05', 22, '2020-09-05', 448.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (13, 'PACKED', '2020-08-20', 2, '2020-08-21', 20, '2020-08-22', 22, '2020-08-22', 321.4);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (13, 'PACKED', '2020-08-15', 1, '2020-08-16', 19, '2020-08-17', 22, '2020-08-17', 203.6);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (13, 'PACKED', '2020-07-08', 2, '2020-07-09', 20, '2020-07-10', 22, '2020-07-10', 340.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, edit_date, price) " + "VALUES (14, 'PACKED', '2020-06-08', 3, '2020-06-09', 25, '2020-06-10', 26, '2020-06-10', 611.3);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // shipped, shipped date must be after packed date and user has to be a supplier from the same supp_id
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (1, 'SHIPPED', '2020-11-08', 13, '2020-11-09', 14, '2020-11-10', 12, '2020-11-11', 11, '2020-11-11', 492.7);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (1, 'SHIPPED', '2020-10-03', 13, '2020-10-04', 14, '2020-10-05', 12, '2020-10-05', 11, '2020-10-05', 699.2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (6, 'SHIPPED', '2020-10-05', 2, '2020-10-07', 15, '2020-10-18', 6, '2020-10-19', 6, '2020-10-19', 636);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (11, 'SHIPPED', '2020-09-08', 5, '2020-09-09', 23, '2020-09-11', 24, '2020-09-13', 23, '2020-09-13', 447.1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (13, 'SHIPPED', '2020-09-03', 5, '2020-09-04', 19, '2020-09-05', 22, '2020-09-06', 21, '2020-09-06', 317.0);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (13, 'SHIPPED', '2020-08-20', 5, '2020-08-21', 19, '2020-08-22', 22, '2020-08-23', 21, '2020-08-23', 305);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (13, 'SHIPPED', '2020-08-15', 2, '2020-08-16', 20, '2020-08-17', 22, '2020-08-18', 21, '2020-08-18', 193.2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (13, 'SHIPPED', '2020-07-08', 2, '2020-07-09', 20, '2020-07-10', 22, '2020-07-11', 21, '2020-07-11', 474.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, edit_date, price) " + "VALUES (14, 'SHIPPED', '2020-06-08', 1, '2020-06-09', 25, '2020-06-10', 26, '2020-06-11', 25, '2020-06-11', 548.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // delivered, delivered date must be after shipped date and user has to be a shop worker or manager
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (1, 'DELIVERED', '2020-11-08', 2, '2020-11-09', 13, '2020-11-10', 14, '2020-11-11', 12, '2020-11-14', 1, '2020-11-14', 598.7);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (1, 'DELIVERED', '2020-10-03', 2, '2020-10-04', 13, '2020-10-05', 14, '2020-10-05', 12, '2020-10-10', 1, '2020-10-10', 550.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (6, 'DELIVERED', '2020-10-05', 1, '2020-10-07', 18, '2020-10-18', 16, '2020-10-19', 16, '2020-10-21', 2, '2020-10-21', 209.6);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (11, 'DELIVERED', '2020-09-08', 4, '2020-09-09', 23, '2020-09-11', 24, '2020-09-13', 23, '2020-09-14', 2, '2020-09-14', 588.3);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (13, 'DELIVERED', '2020-09-03', 6, '2020-09-04', 20, '2020-09-05', 22, '2020-09-06', 21, '2020-09-08', 3, '2020-09-08', 237.1);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (13, 'DELIVERED', '2020-08-20', 4, '2020-08-21', 20, '2020-08-22', 22, '2020-08-23', 21, '2020-08-27', 3, '2020-08-27', 444.8);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (13, 'DELIVERED', '2020-08-15', 2, '2020-08-16', 19, '2020-08-17', 22, '2020-08-18', 21, '2020-08-30', 6, '2020-08-30', 601.5);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (13, 'DELIVERED', '2020-07-08', 1, '2020-07-09', 19, '2020-07-10', 22, '2020-07-11', 21, '2020-07-19', 6, '2020-07-19', 227.2);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + ALL_ORDER + " (supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, edit_date, price) " + "VALUES (14, 'DELIVERED', '2020-06-08', 2, '2020-06-09', 25, '2020-06-10', 26, '2020-06-11', 26, '2020-06-18', 5, '2020-06-18', 273.9);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // INSERTING FIRST ITEM TO EAXH ORDER
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (1, 1, 23);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (2, 2, 21);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (3, 3, 22);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (4, 4, 23);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (5, 5, 15);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (6, 6, 19);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (7, 7, 42);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (8, 8, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (9, 9, 29);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (10, 10, 23);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (11, 10, 35);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (12, 9, 22);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (13, 8, 23);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (14, 7, 15);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (15, 6, 19);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (16, 5, 42);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (17, 4, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (18, 3, 29);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (19, 2, 23);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (20, 1, 35);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (21, 11, 22);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (22, 12, 23);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (23, 13, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (24, 6, 19);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (25, 5, 42);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (26, 13, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (27, 12, 29);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (28, 11, 23);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (29, 11, 35);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (30, 10, 22);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (31, 9, 23);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (32, 6, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (33, 4, 19);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (34, 1, 42);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (35, 2, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (36, 12, 29);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (37, 12, 23);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (38, 14, 35);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (39, 18, 22);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (40, 10, 23);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (41, 20, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (42, 12, 19);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (43, 15, 42);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (44, 18, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (45, 15, 29);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        
        // INSERTING SECOND ITEM TO EAXH ORDER
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (1, 10, 14);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (2, 11, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (3, 12, 22);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (4, 13, 23);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (5, 14, 13);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (6, 15, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (7, 16, 24);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (8, 17, 35);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (9, 18, 12);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (10, 19, 14);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (11, 20, 12);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (12, 11, 16);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (13, 12, 12);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (14, 13, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (15, 14, 29);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (16, 15, 22);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (17, 16, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (18, 17, 19);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (19, 18, 33);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (20, 19, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (21, 20, 22);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (22, 1, 13);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (23, 2, 35);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (24, 14, 29);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (25, 15, 12);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (26, 16, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (27, 17, 19);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (28, 18, 13);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (29, 19, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (30, 20, 12);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (31, 20, 13);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (32, 19, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (33, 18, 29);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (34, 17, 12);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (35, 16, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (36, 15, 19);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (37, 14, 13);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (38, 13, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (39, 12, 12);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (40, 11, 13);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (41, 1, 25);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (42, 2, 19);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (43, 3, 22);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (44, 4, 15);";
        insertStatements.add(insertStatement);st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (45, 5, 19);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        
        // INSERTING THIRD ITEM TO EAXH ORDER
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (1, 15, 14);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (2, 16, 15);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (3, 17, 12);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (4, 18, 13);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (5, 19, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (6, 20, 29);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (7, 19, 12);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (8, 18, 15);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (9, 17, 19);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (10, 16, 13);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (11, 1, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (12, 2, 12);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (13, 3, 13);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (14, 4, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (15, 5, 29);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (16, 6, 12);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (17, 7, 15);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (18, 8, 13);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (19, 9, 21);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (20, 11, 21);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (21, 12, 20);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (22, 13, 20);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (23, 17, 10);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (24, 11, 10);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (25, 18, 22);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (26, 19, 15);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (27, 10, 19);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (28, 2, 21);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (29, 1, 31);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (30, 3, 21);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (31, 4, 21);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (32, 5, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (33, 6, 12);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (34, 7, 12);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (35, 8, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (36, 9, 23);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (37, 10, 23);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (38, 11, 25);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (39, 13, 12);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (40, 12, 13);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (41, 8, 11);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (42, 9, 11);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (43, 6, 11);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (44, 5, 11);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + "VALUES (45, 7, 21);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        
        // INSERTING STORE INFORMATION
        insertStatement = "INSERT INTO " + SHOP_INFO + " (name, manager_id) " + "VALUES ('shop', 1);";
        insertStatements.add(insertStatement);
        st.execute(insertStatement);
        
//        for (String query : insertStatements) {
//            st.execute(query);
//        }
        
        st.close();
        conn.close();
    }
    
    public static void emptyTables() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String deleteStatement;
        
        deleteStatement = "DELETE FROM " + SHOP_INFO + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DELETE FROM " + PROD_ORDS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DELETE FROM " + ALL_USERS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DELETE FROM " + SUPPLIERS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DELETE FROM " + PROD_TYPE + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DELETE FROM " + ALL_PRODS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DELETE FROM " + ALL_ORDER + ";";
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
    
    public static void dropTables() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String deleteStatement;
        
        deleteStatement = "DROP TABLE IF EXISTS " + SHOP_INFO + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DROP TABLE IF EXISTS " + PROD_ORDS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DROP TABLE IF EXISTS " + ALL_USERS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DROP TABLE IF EXISTS " + SUPPLIERS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DROP TABLE IF EXISTS " + PROD_TYPE + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DROP TABLE IF EXISTS " + ALL_PRODS + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DROP TABLE IF EXISTS " + ALL_ORDER + ";";
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
    // MANAGE STORE FUNCTIONS
    public static void startStore(String name) throws SQLException { // for debugging purposes
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        String getStatement = "SELECT * FROM " + SHOP_INFO + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        if (rs.next() == false) {
            insertStatement = "INSERT INTO " + SHOP_INFO + " (name) VALUES ('" + name + "');";
            st.execute(insertStatement);
        }
        
        st.close();
        conn.close();
        rs.close();
    }
    
    public static void setManager(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement = "UPDATE " + SHOP_INFO + " SET manager_id = " + id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static int getManagerId() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT manager_id FROM " + SHOP_INFO + ";";
        ResultSet rs = st.executeQuery(getStatement);
        int manager_id = -1;
        while (rs.next()) {
        if (rs.getString(1) != null) {
            manager_id = Integer.parseInt(rs.getString(1));
        }
        }
        
        st.close();
        rs.close();
        conn.close();
        return manager_id;
    }
    
    // ORDER RELATED FUNCTION
    public static void addOrder(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        insertStatement = "INSERT INTO " + ALL_ORDER + " (order_id) " + 
            "VALUES (" + order_id + ");";
        
        System.out.println(insertStatement);
        
        
        st.execute(insertStatement);
        
        st.close();
        conn.close();
    }
    
    public static boolean orderExist(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT order_id FROM " + ALL_ORDER + " WHERE order_id = " + order_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        if (rs.next()) {
            st.close();
            conn.close();
            rs.close();
            return true;
        }
        
        st.close();
        conn.close();
        rs.close();
        return false;
    }
    
    public static void updateOrderPrice (int order_id, double price) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_ORDER + " SET price = " + price + 
                " WHERE order_id = " + order_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static double getOrderPrice(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT price FROM " + ALL_ORDER + " WHERE order_id = " + order_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        double price = rs.getFloat(1);
        st.close();
        conn.close();
        rs.close();
        return price;
    }
    
    public static String orderTotalString(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT prod_id, prod_qty FROM " + PROD_ORDS + " WHERE order_id = " + order_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        double totalPrice = 0.0;
        
        while (rs.next()) {
            Product product = getProductById(Integer.parseInt(rs.getString(1)));
            totalPrice = totalPrice + (product.getPrice()*rs.getInt(2));
        }
        
        st.close();
        conn.close();
        rs.close();
        return Common.doubleStr(totalPrice);
    }
    
    public static double orderTotal(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT prod_id, prod_qty FROM " + PROD_ORDS + " WHERE order_id = " + order_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        double totalPrice = 0.0;
        
        while (rs.next()) {
            Product product = getProductById(Integer.parseInt(rs.getString(1)));
            totalPrice = totalPrice + (product.getPrice()*rs.getInt(2));
        }
        
        st.close();
        conn.close();
        rs.close();
        return totalPrice;
    }
    
    public static void addItemToOrder(int order_id, Product product, int qty) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        insertStatement = "INSERT INTO " + PROD_ORDS + " (order_id, prod_id, prod_qty) " + 
            "VALUES (" + order_id + ", " + product.getProdId() + ", " + qty + ");";
        st.execute(insertStatement);
        
        st.close();
        conn.close();
        
        //double price = Float.parseFloat(orderTotal(order_id));
        //updateOrderPrice(order_id, price);
    }
    
    public static void assignOrder(int order_id, int supp_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_ORDER + " SET supp_id = " + supp_id + 
                " WHERE order_id = " + order_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void placeOrder(int order_id, String date, int user, double price) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_ORDER + " SET place_date = '" + date + "', place_user = "
                + user  + ", status = '" + Status.PLACED + "', price = " + price + ", edit_date = '" + date + "' WHERE order_id = " + order_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void receiveOrder(int order_id, String date, int user) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_ORDER + " SET receive_date = '" + date + "', receive_user = "
                + user  + ", status = '" + Status.RECEIVED + "', edit_date = '" + date + "' WHERE order_id = " + order_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void packedOrder(int order_id, String date, int user) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_ORDER + " SET packed_date = '" + date + "', packed_user = "
                + user  + ", status = '" + Status.PACKED + "', edit_date = '" + date + "' WHERE order_id = " + order_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void shippedOrder(int order_id, String date, int user) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_ORDER + " SET shipped_date = '" + date + "', shipped_user = "
                + user  + ", status = '" + Status.SHIPPED + "', edit_date = '" + date + "' WHERE order_id = " + order_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void deliveredOrder(int order_id, String date, int user) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_ORDER + " SET delivered_date = '" + date + "', delivered_user = "
                + user  + ", status = '" + Status.DELIVERED + "', edit_date = '" + date + "' WHERE order_id = " + order_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void cleanOrderTable() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String deleteStatement = "DELETE FROM " + ALL_ORDER + " WHERE status IS NULL;";
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
    
    public static void removeOrder(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String deleteStatement = "DELETE FROM " + ALL_ORDER + " WHERE order_id = " + order_id + ";";
        st.execute(deleteStatement);
        
        deleteStatement = "DELETE FROM " + PROD_ORDS + " WHERE order_id = " + order_id + ";";
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
    // ADD TO DATABASE FUNCTIONS
    public static void addUser(String name, String username, String password, int supplier) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password, supp_id) " + 
            "VALUES ('" + name + "', '" + username + "', '" + password + "', '" + supplier + "');";
        st.execute(insertStatement);
        
        st.close();
        conn.close();
    }
    
    public static void addUser(String name, String username, String password) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        insertStatement = "INSERT INTO " + ALL_USERS + " (name, username, password) " + 
            "VALUES ('" + name + "', '" + username + "', '" + password + "');";
        st.execute(insertStatement);
        
        st.close();
        conn.close();
    }
    
    public static void addSupplier(String name, String contact, String street, 
            String suburb, String state, String postcode, String country) 
            throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        insertStatement = "INSERT INTO " + SUPPLIERS + " (name, contact, street, "
            + "suburb, state, postcode, country) " + "VALUES ('" + name + "', '" 
            + contact + "', '" + street + "', '" + suburb + "', '" + state 
            + "', '" + postcode + "', '" + country + "');";
        st.execute(insertStatement);
        
        st.close();
        conn.close();
    }
    
    // READ FROM DATABASE FUNCTIONS
    public static User getUserByUsername(String username) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_USERS + " WHERE username = '" + username + "';";
        ResultSet rs = st.executeQuery(getStatement);
        
        int ui = Integer.parseInt(rs.getString(1));
        String n = rs.getString(2);
        String u = rs.getString(3);
        String p = rs.getString(4);
        Position s;
        String supp_id = rs.getString(5);
        boolean approved = false;
        if (rs.getInt(6) == 1) {
            approved = true;
        }
        int si;
        if (supp_id == null) {
            si = 0;
        } else {
            si = Integer.parseInt(rs.getString(5));
        }
        
        if (ui == getManagerId()) {
            s = Position.MANAGER;
        } else if (si == 0) {
            s = Position.EMPLOYEE;
        } else {
            s = Position.SUPPLIER;
        }
        
        User current = new User(ui, n, u, p, s, approved, si);
        
        st.close();
        conn.close();
        rs.close();
        return current;
    }
    
    public static ObservableList<PieChart.Data> pieChartData(User user) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement;
        if (user.getStatus() == Position.SUPPLIER) {
            getStatement = "SELECT status FROM " + ALL_ORDER + " WHERE supp_id = " + user.getSuppId() + ";";
        } else {
            getStatement = "SELECT status FROM " + ALL_ORDER + ";";
        }
        ResultSet rs = st.executeQuery(getStatement);
        
        int[] statusTally = new int[6];
        ArrayList<PieChart.Data> statTally = new ArrayList<>(); 
        while(rs.next()) {
            Status status = Status.valueOf(rs.getString(1));
            statusTally[status.getValue()] = statusTally[status.getValue()] + 1;
        }
        
        Status stats[] = Status.values();
        for (Status s : stats) {
            if (statusTally[s.getValue()] > 0) {
                PieChart.Data data = new PieChart.Data(String.valueOf(s), statusTally[s.getValue()]);
                statTally.add(data);
            }
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(statTally);
        
//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//            new PieChart.Data(String.valueOf(Status.PLACED), statusTally[Status.PLACED.getValue()]), 
//            new PieChart.Data(String.valueOf(Status.RECEIVED), statusTally[Status.RECEIVED.getValue()]), 
//            new PieChart.Data(String.valueOf(Status.PACKED), statusTally[Status.PACKED.getValue()]),  
//            new PieChart.Data(String.valueOf(Status.SHIPPED), statusTally[Status.SHIPPED.getValue()]), 
//            new PieChart.Data(String.valueOf(Status.DELIVERED), statusTally[Status.DELIVERED.getValue()]) 
//        ); 
        
        return pieChartData;
    }
    
    public static boolean userApproved(String username) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT approved FROM " + ALL_USERS + " WHERE username = '" + username + "';";
        ResultSet rs = st.executeQuery(getStatement);
        
        boolean approved = false;
        
        if (rs.getInt(1) == 1) {
            approved = true;
        }
        
        st.close();
        conn.close();
        rs.close();
        return approved;
    }
    
    public static boolean usernameExists(String username) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT username FROM " + ALL_USERS + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        while (rs.next()) {
            if ((username.toLowerCase()).equals(rs.getString(1).toLowerCase())) {
                st.close();
                conn.close();
                return true;
            }
        }
        
        st.close();
        conn.close();
        rs.close();
        return false;
    }
    
    public static ObservableList<KeyValPair> getUsers() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT user_id, username FROM " + ALL_USERS + " ORDER BY username ASC;";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<KeyValPair> users = FXCollections.observableArrayList();
        while (rs.next()) {
            KeyValPair n = new KeyValPair(Integer.parseInt(rs.getString(1)), rs.getString(2));
            users.add(n);
        }
        
        st.close();
        conn.close();
        rs.close();
        return users;
    }
    
    public static String getPassword(String username) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT password FROM " + ALL_USERS + " WHERE username = '" + username + "';";
        ResultSet rs = st.executeQuery(getStatement);
        String pass = rs.getString(1);
        
        
        st.close();
        conn.close();
        rs.close();
        return pass;
    }
    
    public static ObservableList<KeyValPair> getSuppliers() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT supp_id, name FROM " + SUPPLIERS + " ORDER BY name ASC;";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<KeyValPair> suppliers = FXCollections.observableArrayList();
        while (rs.next()) {
            KeyValPair n = new KeyValPair(Integer.parseInt(rs.getString(1)), rs.getString(2));
            suppliers.add(n);
        }
        
        st.close();
        conn.close();
        rs.close();
        return suppliers;
    }
    
    public static Supplier getSupplierById(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + SUPPLIERS + " WHERE supp_id = " + id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        Address address = new Address(rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
        Supplier supplier = new Supplier(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), address);
        
        st.close();
        conn.close();
        rs.close();
        return supplier;
    }
    
    public static ObservableList<Supplier> getAllSuppliers() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + SUPPLIERS + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        
        while(rs.next()) {
            Address address = new Address(rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
            Supplier supplier = new Supplier(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), address);
            suppliers.add(supplier);
        }
        
        st.close();
        conn.close();
        rs.close();
        return suppliers;
    }
    
    public static ObservableList<Order> getAllOrders() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_ORDER + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Order> orders = FXCollections.observableArrayList();
        while(rs.next()) {
            int order_id = Integer.parseInt(rs.getString(1));
            Supplier supplier = getSupplierById(Integer.parseInt(rs.getString(2)));
            Status stat = Status.valueOf(rs.getString(3));
            String place_date = rs.getString(4);
            int place_user = Integer.parseInt(rs.getString(5));
            String receive_date = rs.getString(6);
            int receive_user = -1;
            String packed_date = rs.getString(8);
            int packed_user = -1;
            String shipped_date = rs.getString(10);
            int shipped_user = -1;
            String delivered_date = rs.getString(12);
            int delivered_user = -1;
            double price = Float.parseFloat(rs.getString(14));
            
            // only fill if order is received
            if (stat == Status.RECEIVED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));
            }

            // only fill if order is packed
            else if (stat == Status.PACKED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));
            }

            // only fill if order is shipped
            else if (stat == Status.SHIPPED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));

                shipped_date = rs.getString(10);
                shipped_user = Integer.parseInt(rs.getString(11));
            }

            // only fill if order is delivered
            else if (stat == Status.DELIVERED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));

                shipped_date = rs.getString(10);
                shipped_user = Integer.parseInt(rs.getString(11));

                delivered_date = rs.getString(12);
                delivered_user = Integer.parseInt(rs.getString(13));
            }
        
        Order order = new Order(order_id, supplier, stat, place_date, place_user, 
            receive_date, receive_user, packed_date, packed_user, shipped_date, 
            shipped_user, delivered_date, delivered_user, price);
            orders.add(order);
        }
        
        st.close();
        conn.close();
        rs.close();
        return orders;
    }
    
        public static ObservableList<Order> get10RecentOrders(User user) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement;
        if (user.getStatus() == Position.SUPPLIER) {
            getStatement = "SELECT * FROM " + ALL_ORDER + " WHERE supp_id = " + user.getSuppId() + " AND status = 'PLACED' ORDER BY edit_date DESC LIMIT 10";
        } else {
            getStatement = "SELECT * FROM " + ALL_ORDER + " ORDER BY edit_date DESC, CASE status WHEN 'DELIVERED' THEN 0 WHEN 'SHIPPED' THEN 1 WHEN 'PACKED' THEN 2 WHEN 'RECEIVED' THEN 3 WHEN 'PLACED' THEN 4 END LIMIT 10";
        }
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Order> orders = FXCollections.observableArrayList();
        while(rs.next()) {
            int order_id = Integer.parseInt(rs.getString(1));
            Supplier supplier = getSupplierById(Integer.parseInt(rs.getString(2)));
            Status stat = Status.valueOf(rs.getString(3));
            String place_date = rs.getString(4);
            int place_user = Integer.parseInt(rs.getString(5));
            String receive_date = rs.getString(6);
            int receive_user = -1;
            String packed_date = rs.getString(8);
            int packed_user = -1;
            String shipped_date = rs.getString(10);
            int shipped_user = -1;
            String delivered_date = rs.getString(12);
            int delivered_user = -1;
            double price = Float.parseFloat(rs.getString(14));
            
            // only fill if order is received
            if (stat == Status.RECEIVED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));
            }

            // only fill if order is packed
            else if (stat == Status.PACKED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));
            }

            // only fill if order is shipped
            else if (stat == Status.SHIPPED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));

                shipped_date = rs.getString(10);
                shipped_user = Integer.parseInt(rs.getString(11));
            }

            // only fill if order is delivered
            else if (stat == Status.DELIVERED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));

                shipped_date = rs.getString(10);
                shipped_user = Integer.parseInt(rs.getString(11));

                delivered_date = rs.getString(12);
                delivered_user = Integer.parseInt(rs.getString(13));
            }
        
        Order order = new Order(order_id, supplier, stat, place_date, place_user, 
            receive_date, receive_user, packed_date, packed_user, shipped_date, 
            shipped_user, delivered_date, delivered_user, price);
            orders.add(order);
        }
        
        st.close();
        conn.close();
        rs.close();
        return orders;
    }
    
    public static ObservableList<Order> getAllOrdersBySupplier(int supplier_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_ORDER + " WHERE supp_id = " + supplier_id +  ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Order> orders = FXCollections.observableArrayList();
        // CODE GOES HERE
        while(rs.next()) {
            int order_id = Integer.parseInt(rs.getString(1));
            Supplier supplier = getSupplierById(Integer.parseInt(rs.getString(2)));
            Status stat = Status.valueOf(rs.getString(3));
            String place_date = rs.getString(4);
            int place_user = Integer.parseInt(rs.getString(5));
            String receive_date = rs.getString(6);
            int receive_user = -1;
            String packed_date = rs.getString(8);
            int packed_user = -1;
            String shipped_date = rs.getString(10);
            int shipped_user = -1;
            String delivered_date = rs.getString(12);
            int delivered_user = -1;
            double price = Float.parseFloat(rs.getString(14));
            
            // only fill if order is received
            // only fill if order is received
        if (stat == Status.RECEIVED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
        }

        // only fill if order is packed
        else if (stat == Status.PACKED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
            
            packed_date = rs.getString(8);
            packed_user = Integer.parseInt(rs.getString(9));
        }

        // only fill if order is shipped
        else if (stat == Status.SHIPPED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
            
            packed_date = rs.getString(8);
            packed_user = Integer.parseInt(rs.getString(9));
            
            shipped_date = rs.getString(10);
            shipped_user = Integer.parseInt(rs.getString(11));
        }

        // only fill if order is delivered
        else if (stat == Status.DELIVERED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
            
            packed_date = rs.getString(8);
            packed_user = Integer.parseInt(rs.getString(9));
            
            shipped_date = rs.getString(10);
            shipped_user = Integer.parseInt(rs.getString(11));
            
            delivered_date = rs.getString(12);
            delivered_user = Integer.parseInt(rs.getString(13));
        }
        
        Order order = new Order(order_id, supplier, stat, place_date, place_user, 
            receive_date, receive_user, packed_date, packed_user, shipped_date, 
            shipped_user, delivered_date, delivered_user, price);
            orders.add(order);
        }
        
        st.close();
        conn.close();
        rs.close();
        return orders;
    }
    
    public static ObservableList<Order> getAllOrdersByFilter(int supplier_id, int user_id, int prod_id, Status status, String keyDate, String startDate, String endDate, double maxPrice) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String getStatement = "SELECT * FROM " + ALL_ORDER + " WHERE order_id >= 0";
        if (prod_id != -1) {
            getStatement = "SELECT " + ALL_ORDER + ".order_id, supp_id, status, place_date, place_user, receive_date, receive_user, packed_date, packed_user, shipped_date, shipped_user, delivered_date, delivered_user, price, prod_id FROM " + PROD_ORDS + " INNER JOIN " + ALL_ORDER + " ON " + PROD_ORDS + ".order_id = " + ALL_ORDER + ".order_id WHERE prod_id = " + prod_id;
        }
        
        if (supplier_id != -1) {
            getStatement = getStatement + " AND supp_id = " + supplier_id;
        }
        
        if (user_id != -1) {
            getStatement = getStatement + " AND (place_user = " + user_id + 
                    " OR receive_user = " + user_id + 
                    " OR packed_user = " + user_id +
                    " OR shipped_user = " + user_id + 
                    " OR delivered_user = " + user_id + ")";
        }
        
        if (status != null) {
            getStatement = getStatement + " AND status = '" + status + "'";
        }
        
        if (keyDate != null) {
            if (startDate.isEmpty() == false) {
                getStatement = getStatement + " AND " + keyDate + " >= '" + startDate + "'";
            }

            if (endDate.isEmpty() == false) {
                getStatement = getStatement + " AND " + keyDate + " <= '" + endDate + "'";
            }
        }
        if (maxPrice != 0.0) {
            getStatement = getStatement + " AND price <= " + maxPrice;
        }
        
        getStatement = getStatement + ";";
        System.out.println(getStatement);
        ResultSet rs = st.executeQuery(getStatement);

        // TURN RESULTS INTO ORDER LIST
        ObservableList<Order> orders = FXCollections.observableArrayList();
        // CODE GOES HERE
        while(rs.next()) {
            int order_id = Integer.parseInt(rs.getString(1));
            Supplier supplier = getSupplierById(Integer.parseInt(rs.getString(2)));
            Status stat = Status.valueOf(rs.getString(3));
            String place_date = rs.getString(4);
            int place_user = Integer.parseInt(rs.getString(5));
            String receive_date = rs.getString(6);
            int receive_user = -1;
            String packed_date = rs.getString(8);
            int packed_user = -1;
            String shipped_date = rs.getString(10);
            int shipped_user = -1;
            String delivered_date = rs.getString(12);
            int delivered_user = -1;
            double price = Float.parseFloat(rs.getString(14));
            
            // only fill if order is received
            // only fill if order is received
            if (stat == Status.RECEIVED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));
            }

            // only fill if order is packed
            else if (stat == Status.PACKED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));
            }

            // only fill if order is shipped
            else if (stat == Status.SHIPPED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));

                shipped_date = rs.getString(10);
                shipped_user = Integer.parseInt(rs.getString(11));
            }

            // only fill if order is delivered
            else if (stat == Status.DELIVERED) {
                receive_date = rs.getString(6);
                receive_user = Integer.parseInt(rs.getString(7));

                packed_date = rs.getString(8);
                packed_user = Integer.parseInt(rs.getString(9));

                shipped_date = rs.getString(10);
                shipped_user = Integer.parseInt(rs.getString(11));

                delivered_date = rs.getString(12);
                delivered_user = Integer.parseInt(rs.getString(13));
            }
        
        Order order = new Order(order_id, supplier, stat, place_date, place_user, 
            receive_date, receive_user, packed_date, packed_user, shipped_date, 
            shipped_user, delivered_date, delivered_user, price);
            orders.add(order);
        }
        
        st.close();
        conn.close();
        rs.close();
        return orders;
    }
    
    public static ObservableList<OrderItem> getAllOrderItem(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + PROD_ORDS + " WHERE order_id = " + order_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<OrderItem> data = FXCollections.observableArrayList();
        
        while(rs.next()) {
            Product product = getProductById(rs.getInt(3));
            OrderItem item = new OrderItem(product, rs.getInt(4), order_id);
            data.add(item);
        }
        st.close();
        conn.close();
        rs.close();
        return data;
    }
    
    public static void deleteOrderItem (int order_id, int prod_id, int prod_qty) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String deleteStatement = "DELETE FROM " + PROD_ORDS + " WHERE order_id = " + order_id + ", prod_id = " + prod_id + ", prod_qty = " + prod_qty + ";";
        st.executeQuery(deleteStatement);
        
        st.close();
        conn.close();
    }
    
    public static int getOrderItemCount(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + PROD_ORDS + " WHERE order_id = " + order_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        int count = 0;
        while(rs.next()) {
            count = count + 1;
        }
        st.close();
        conn.close();
        rs.close();
        return count;
    }
    
    public static int getNewOrderId() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT MAX(order_id) FROM " + ALL_ORDER + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        int newId = 1; 
        if (rs.getString(1) != null) {
            newId = rs.getInt(1) + 1;
        }
        st.close();
        conn.close();
        rs.close();
        return newId;
    }
    
    //All user accounts 
        public static ObservableList<User> getAllUsers() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_USERS + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<User> users = FXCollections.observableArrayList();
        
        while(rs.next()){
            int user_id = Integer.parseInt(rs.getString(1));
            String supp_id = rs.getString(5);
            Position status;
            boolean approved = false;
            User user;
            if (supp_id != null) {
                status = Position.SUPPLIER;
                if (rs.getInt(6) == 1) {
                    approved = true;
                }
                user = new User(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), status, approved, Integer.parseInt(rs.getString(5)));
            } else {
                if (user_id == getManagerId()) {
                    status = Position.MANAGER;
                } else {
                    status = Position.EMPLOYEE;
                }
                if (rs.getInt(6) == 1) {
                    approved = true;
                }
                user = new User(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), rs.getString(4), status, approved);
            }
           users.add(user);
            
            
        }
        
        st.close();
        conn.close();
        rs.close();
        return users;
    }
        
    public static ObservableList<KeyValPair> getProducts() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT prod_id, name FROM " + ALL_PRODS + " ORDER BY name ASC;";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<KeyValPair> products = FXCollections.observableArrayList();
        while (rs.next()) {
            KeyValPair n = new KeyValPair(Integer.parseInt(rs.getString(1)), rs.getString(2));
            products.add(n);
        }
        
        st.close();
        conn.close();
        rs.close();
        return products;
    }
    
    
    
    // PRODUCT FILTER
    public static ObservableList<Product> getProductByFilter(int type_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_PRODS + " WHERE available = 1 AND type_id = " + type_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Product> products = FXCollections.observableArrayList();
        while (rs.next()) {
            ProductType type = getProductTypeById(rs.getInt(5));
            boolean available = true;
            if (rs.getInt(6) == 0) available = false;
            
            Product product = new Product(rs.getInt(1), rs.getString(2), 
                    rs.getString(3), rs.getDouble(4), type, available, false);
            products.add(product);
        }
        
        st.close();
        conn.close();
        rs.close();
        return products;
    }
    
    public static ObservableList<Product> getProductByFilter(String name) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_PRODS + " WHERE available = 1 AND name LIKE '%" + name + "%';";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Product> products = FXCollections.observableArrayList();
        while (rs.next()) {
            ProductType type = getProductTypeById(rs.getInt(5));
            boolean available = true;
            if (rs.getInt(6) == 0) available = false;
            Product product = new Product(rs.getInt(1), rs.getString(2), 
                    rs.getString(3), rs.getDouble(4), type, available, false);
            products.add(product);
        }
        
        st.close();
        conn.close();
        rs.close();
        return products;
    }
    
    private static ProductType getProductTypeById(int type_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + PROD_TYPE + " WHERE type_id = " + type_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ProductType type = new ProductType(rs.getInt(1), rs.getString(2));
        rs.close();
        st.close();
        return type;
    }
    
    public static ObservableList<Product> getProductByFilter(int type_id, String name) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_PRODS + " WHERE available = 1 AND name LIKE '%" + name + "%' AND type_id = " + type_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Product> products = FXCollections.observableArrayList();
        while (rs.next()) {
            ProductType type = getProductTypeById(rs.getInt(5));
            boolean available = true;
            if (rs.getInt(6) == 0) available = false;
            Product product = new Product(rs.getInt(1), rs.getString(2), 
                    rs.getString(3), rs.getDouble(4), type, available, false);
            products.add(product);
        }
        
        st.close();
        conn.close();
        rs.close();
        return products;
    }
    
    public static ObservableList<Product> getAllProducts() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_PRODS  + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Product> products = FXCollections.observableArrayList();
        while (rs.next()) {
            ProductType type = getProductTypeById(rs.getInt(5));
            boolean available = true;
            if (rs.getInt(6) == 0) available = false;
            Product product = new Product(rs.getInt(1), rs.getString(2), 
                    rs.getString(3), rs.getDouble(4), type, available, false);
            products.add(product);
        }
        
        st.close();
        conn.close();
        rs.close();
        return products;
    }
    
    public static ObservableList<Product> getAllAvailableProducts() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_PRODS  + " WHERE available = 1;";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Product> products = FXCollections.observableArrayList();
        while (rs.next()) {
            ProductType type = getProductTypeById(rs.getInt(5));
            boolean available = true;
            if (rs.getInt(6) == 0) available = false;
            Product product = new Product(rs.getInt(1), rs.getString(2), 
                    rs.getString(3), rs.getDouble(4), type, available, false);
            products.add(product);
        }
        
        st.close();
        conn.close();
        rs.close();
        return products;
    }
        
    public static ObservableList<String> getProductsTypes() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT type FROM " + PROD_TYPE;
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<String> productTypes = FXCollections.observableArrayList();
        
        while(rs.next()) {
            String type = rs.getString(1);
            productTypes.add(type);
        }
        
        st.close();
        conn.close();
        rs.close();
        return productTypes;     
    
    }
    
    public static XYChart.Series<String, Number> getLineGraph(User user, int year, int month) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement;
        XYChart.Series<String, Number> lineGraphData = new XYChart.Series<>();
        ResultSet rs;
        String prev1, prev, now;
        double last1, last, current;
        
        switch(month) {
            case 1: {
                prev1 = (year-1) + "-" + "11";
                prev = (year-1) + "-" + "12";
                now = year + "-" + "01";
                break;
            }
            case 2: {
                prev1 = (year-1) + "-" + "12";
                prev = year + "-" + "01";
                now = year + "-" + "02";
                break;
            }
            case 3: {
                prev1 = year + "-" + "01";
                prev = year + "-" + "02";
                now = year + "-" + "03";
                break;
            }
            case 4: {
                prev1 = year + "-" + "02";
                prev = year + "-" + "03";
                now = year + "-" + "04";
                break;
            }
            case 5: {
                prev1 = year + "-" + "03";
                prev = year + "-" + "04";
                now = year + "-" + "05";
                break;
            }
            case 6: {
                prev1 = year + "-" + "04";
                prev = year + "-" + "05";
                now = year + "-" + "06";
                break;
            }
            case 7: {
                prev1 = year + "-" + "05";
                prev = year + "-" + "06";
                now = year + "-" + "07";
                break;
            }
            case 8: {
                prev1 = year + "-" + "06";
                prev = year + "-" + "07";
                now = year + "-" + "08";
                break;
            }
            case 9: {
                prev1 = year + "-" + "07";
                prev = year + "-" + "08";
                now = year + "-" + "09";
                break;
            }
            case 10: {
                prev1 = year + "-" + "08";
                prev = year + "-" + "09";
                now = year + "-" + "10";
                break;
            }
            case 11: {
                prev1 = year + "-" + "09";
                prev = year + "-" + "10";
                now = year + "-" + "11";
                break;
            }
            case 12: {
                prev1 = year + "-" + "10";
                prev = year + "-" + "11";
                now = year + "-" + "12";
                break;
            }
            default: {
                prev1 = year + "-" + "00";
                prev = year + "-" + "00";
                now = year + "-" + "00";
                break;
            }
        }
        
        if (user.getStatus() == Position.SUPPLIER) {
            getStatement = "SELECT SUM(price) FROM " + ALL_ORDER + " WHERE supp_id = " + user.getSuppId() + " AND SUBSTR(delivered_date, 1, 7) = '" + prev1 + "';";
        } else {
            getStatement = "SELECT SUM(price) FROM " + ALL_ORDER + " WHERE SUBSTR(delivered_date, 1, 7) = '" + prev1 + "';";
        }
        rs = st.executeQuery(getStatement);
        if (rs.next()) {
            last1 = rs.getDouble(1);
            lineGraphData.getData().add(new XYChart.Data<>(prev1, last1));
        }
       
        if (user.getStatus() == Position.SUPPLIER) {
            getStatement = "SELECT SUM(price) FROM " + ALL_ORDER + " WHERE supp_id = " + user.getSuppId() + " AND SUBSTR(delivered_date, 1, 7) = '" + prev + "';";
        } else {
            getStatement = "SELECT SUM(price) FROM " + ALL_ORDER + " WHERE SUBSTR(delivered_date, 1, 7) = '" + prev + "';";
        }
        rs = st.executeQuery(getStatement);
        if (rs.next()) {
            last = rs.getDouble(1);
            lineGraphData.getData().add(new XYChart.Data<>(prev, last));
        }
        
        if (user.getStatus() == Position.SUPPLIER) {
            getStatement = "SELECT SUM(price) FROM " + ALL_ORDER + " WHERE supp_id = " + user.getSuppId() + " AND SUBSTR(delivered_date, 1, 7) = '" + now + "';";
        } else {
            getStatement = "SELECT SUM(price) FROM " + ALL_ORDER + " WHERE SUBSTR(delivered_date, 1, 7) = '" + now + "';";
        }
        rs = st.executeQuery(getStatement);
        if (rs.next()) {
            current = rs.getDouble(1);
            lineGraphData.getData().add(new XYChart.Data<>(now, current));
        }
        
        return lineGraphData;
    }
    
    public static ObservableList<KeyValPair> getTypes() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + PROD_TYPE + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<KeyValPair> types = FXCollections.observableArrayList();
        while (rs.next()) {
            KeyValPair n = new KeyValPair(Integer.parseInt(rs.getString(1)), rs.getString(2));
            types.add(n);
        }
        
        st.close();
        conn.close();
        rs.close();
        return types;     
    
    }
    
    public static ObservableList<Product> getProductsByOrderId(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT prod_id FROM " + ALL_PRODS + " WHERE order_id = " + id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ObservableList<Product> products = FXCollections.observableArrayList();
        
        while(rs.next()) {
            Product product = getProductById(Integer.parseInt(rs.getString(1)));
            products.add(product);
        }
        
        st.close();
        conn.close();
        rs.close();
        return products;
    }
    
    public static void addProduct(String name, String unit, double price, int type_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        insertStatement = "INSERT INTO " + ALL_PRODS + " (name, unit, price, type_id) "
                + "VALUES ('" + name + "', '" + unit + "', '" + price + "', '" + type_id + "');";
        st.execute(insertStatement);
        
        st.close();
        conn.close();         
    }
    
    public static void addProductType(String type) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String insertStatement;
        
        insertStatement = "INSERT INTO " + PROD_TYPE + " (type) VALUES ('" + type + "');";
        st.execute(insertStatement);
        
        st.close();
        conn.close();       
    }
    
    public static int getProductTypeId(String type) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT type_id FROM " + PROD_TYPE + " WHERE LOWER(type) = '" + type.toLowerCase() + "';";
        ResultSet rs = st.executeQuery(getStatement);
        
        int typeId = rs.getInt(1);
        st.close();
        conn.close();
        rs.close();
        
        return typeId;
    }
    
    public static String getProductTypeFromId(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT type FROM " + PROD_TYPE + " WHERE type_id = " + id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        String typeName = rs.getString(1);
        
        st.close();
        conn.close();
        rs.close();
        return typeName;
    }
    
    public static void deleteProductById (int id) throws SQLException {
        openConnection(DB_NAME);   
        String sqlString = "UPDATE " + ALL_PRODS + " SET available = 0 WHERE prod_id = ?";
        PreparedStatement psmt = conn.prepareStatement(sqlString);
          
        psmt.setInt(1, id);
        psmt.execute();        

        conn.close();
    }
    
    private static Supplier getSupplierByIdHelper(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + SUPPLIERS + " WHERE supp_id = " + id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        Address address = new Address(rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
        Supplier supplier = new Supplier(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), address);
        
        st.close();
        rs.close();
        return supplier;
    }
    
    public static Order getOrderById(int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_ORDER + " WHERE order_id = " + order_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        
        Supplier supplier = getSupplierByIdHelper(Integer.parseInt(rs.getString(2)));
        Status stat = Status.valueOf(rs.getString(3));
        String place_date = rs.getString(4);
        int place_user = Integer.parseInt(rs.getString(5));
        String receive_date = rs.getString(6);
        int receive_user = -1;
        String packed_date = rs.getString(8);
        int packed_user = -1;
        String shipped_date = rs.getString(10);
        int shipped_user = -1;
        String delivered_date = rs.getString(12);
        int delivered_user = -1;
        double price = Float.parseFloat(rs.getString(14));

        // only fill if order is received
        if (stat == Status.RECEIVED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
        }

        // only fill if order is packed
        else if (stat == Status.PACKED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
            
            packed_date = rs.getString(8);
            packed_user = Integer.parseInt(rs.getString(9));
        }

        // only fill if order is shipped
        else if (stat == Status.SHIPPED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
            
            packed_date = rs.getString(8);
            packed_user = Integer.parseInt(rs.getString(9));
            
            shipped_date = rs.getString(10);
            shipped_user = Integer.parseInt(rs.getString(11));
        }

        // only fill if order is delivered
        else if (stat == Status.DELIVERED) {
            receive_date = rs.getString(6);
            receive_user = Integer.parseInt(rs.getString(7));
            
            packed_date = rs.getString(8);
            packed_user = Integer.parseInt(rs.getString(9));
            
            shipped_date = rs.getString(10);
            shipped_user = Integer.parseInt(rs.getString(11));
            
            delivered_date = rs.getString(12);
            delivered_user = Integer.parseInt(rs.getString(13));
        }
        
        Order order = new Order(order_id, supplier, stat, place_date, place_user, 
            receive_date, receive_user, packed_date, packed_user, shipped_date, 
            shipped_user, delivered_date, delivered_user, price);
        
        st.close();
        conn.close();
        rs.close();
        return order;
    }
    
    public static double maxOrderPrice(int supp_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT MAX(price) FROM " + ALL_ORDER + " WHERE supp_id = " + supp_id + ";";
        
        ResultSet rs = st.executeQuery(getStatement);
        
        double max = 0.0;
        if (rs.getString(1) != null) {
            max = Float.parseFloat(rs.getString(1));
        }
        
        st.close();
        conn.close();
        rs.close();
        return max;
    }
    
    public static double maxOrderPrice() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT MAX(price) FROM " + ALL_ORDER + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        double max = 0.0;
        if (rs.getString(1) != null) {
            max = Float.parseFloat(rs.getString(1));
        }
        
        st.close();
        conn.close();
        rs.close();
        return max;
    }
    
    public static double minOrderPrice(int supp_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT MIN(price) FROM " + ALL_ORDER + " WHERE supp_id = " + supp_id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        double min = 0.0;
        if (rs.getString(1) != null) {
            min = Float.parseFloat(rs.getString(1));
        }
        
        st.close();
        conn.close();
        rs.close();
        return min;
    }
    
    public static double minOrderPrice() throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT MIN(price) FROM " + ALL_ORDER + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        double min = 0.0;
        if (rs.getString(1) != null) {
            min = Float.parseFloat(rs.getString(1));
        }
        
        st.close();
        conn.close();
        rs.close();
        return min;
    }
    
    public static Product getProductById(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + ALL_PRODS + " WHERE prod_id = " + id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        ProductType type = getProductTypeById(rs.getInt(5));
        boolean available = true;
        if (rs.getInt(6) == 0) available = false;
        Product product = new Product(id, rs.getString(2), rs.getString(3), 
            Float.parseFloat(rs.getString(4)), type, available, false);
        
        st.close();
        conn.close();
        rs.close();
        return product;
    }
    
    public static User getUserById(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String getStatement = "SELECT manager_id FROM " + SHOP_INFO + ";";
        ResultSet rs = st.executeQuery(getStatement);
        int manager_id = -1;
        if (rs.next()) {
            manager_id = rs.getInt(1);
        }
        
        getStatement = "SELECT * FROM " + ALL_USERS + " WHERE user_id = " + id + ";";
        rs = st.executeQuery(getStatement);
        
        
        User user;
        if (rs.next()) {
            int user_id = Integer.parseInt(rs.getString(1));
            String name = rs.getString(2);
            String username = rs.getString(3);
            String password = rs.getString(4);
            String supp_id = rs.getString(5);
            boolean approved = false;
            if (rs.getInt(6) == 1) {
                approved = true;
            }
            Position status;

            if (supp_id != null) {
                status = Position.SUPPLIER;
                user = new User(user_id, name, username, password, status, approved, Integer.parseInt(supp_id));
            } else {
                if (user_id == manager_id) {
                    status = Position.MANAGER;
                } else {
                    status = Position.EMPLOYEE;
                }
                user = new User(user_id, name, username, password, status, approved);
            }
        } else {
            user = new User(-1, "User not Available", "N/A", "", null, false);
        }
        
        st.close();
        conn.close();
        rs.close();
        return user;
    } 
    
    public static String getPriceByOrderId(int id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String getStatement = "SELECT * FROM " + PROD_ORDS + " WHERE order_id = " + id + ";";
        ResultSet rs = st.executeQuery(getStatement);
        
        double total = 0.0;
        
        while(rs.next()) {
            double price = getProductById(Integer.parseInt(rs.getString(3))).getPrice();
            int qty = Integer.parseInt(rs.getString(4));
            total = total + (price*qty);
        }
        
        st.close();
        conn.close();
        rs.close();
        return Common.doubleStr(total);
    }
    
    // UPDATE ENTRIES IN DATABASE
    public static void editSupplier(int supp_id, String name, String contact, 
        String street, String suburb, String state, String postcode, 
        String country) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + SUPPLIERS + " SET name = '" + name + "', contact = '"
                + contact + "', street = '" + street + "', suburb = '" + suburb + "', state = '"
                + state + "', postcode = '" + postcode + "', country = '" + country + "' WHERE supp_id = "
                + supp_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void editUser( int user_id, String name, String username) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_USERS + " SET name = '" + name + "', username = '"
                + username  + "' WHERE user_id = "
                + user_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void approveUser(int user_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_USERS + " SET approved = 1" + " WHERE user_id = "
                + user_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    public static void unapproveUser(int user_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String updateStatement;
        
        updateStatement = "UPDATE " + ALL_USERS + " SET approved = 0" + " WHERE user_id = "
                + user_id + ";";
        st.execute(updateStatement);
        
        st.close();
        conn.close();
    }
    
    // DELETE FROM DATABASE FUNCTIONS
    public static void removeSupplier(int supp_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String deleteStatement = "DELETE FROM " + SUPPLIERS + " WHERE supp_id = " + supp_id + ";";
        
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
    
    public static void removeUser(int user_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String deleteStatement = "DELETE FROM " + ALL_USERS + " WHERE user_id = " + user_id + ";";
        
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
    
    public static void removeAllOrderItem (int order_id) throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        String deleteStatement = "DELETE FROM " + PROD_ORDS + " WHERE order_id = " + order_id + ";";
        
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
    
    // DELETE TABLES (FOR DEBUGGING AND RESETTING PURPOSES)
    public static void deleteTableContent(String DBname)throws SQLException {
        openConnection(DB_NAME);
        Statement st = conn.createStatement();
        
        String deleteStatement = "DELETE FROM " + DBname;
        st.execute(deleteStatement);
        
        st.close();
        conn.close();
    }
}
