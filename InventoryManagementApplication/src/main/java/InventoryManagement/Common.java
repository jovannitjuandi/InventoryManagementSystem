package InventoryManagement;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Common {
    private static User currentUser;
    private static String currentPage;
    private static String prevPage;
    private static final DecimalFormat df = new DecimalFormat("#.#");
    
    //GETTER AND SETTER FUNCTIONS
    public static void setUser (User user) {
        currentUser = user;
    }
    public static User getUser () {
        return currentUser;
    }
    public static void setPage(String page) {
        currentPage = page;
    }
    public static String getPage() {
        return currentPage;
    }
    private static void setPrev(String page) {
        prevPage = page;
    }
    
    public static String getPrev() {
        if (prevPage == null) {
            return null;
        }
        return prevPage.replaceAll("[-0123456789]", "");
    }
    
    public static String clean (String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        input = input.replace("'", "''");
        return ((input.trim()).toLowerCase());
    }
    
    public static String currentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
        Date date = new Date();
        return((formatter.format(date)).replaceAll("\\.", ""));
    }
    
    public static String displayDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateForm = formatter.parse(date);
        
        SimpleDateFormat fullForm = new SimpleDateFormat("dd MMM yyyy");
        String stringForm = fullForm.format(dateForm);
        String displayString = stringForm.replace(".", "").trim();
        
        return displayString;
    }
    
    public static LocalDate getDate(String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
    
    public static void resetPage() throws IOException {
        String current = getPage();
        String root = current.replaceAll("[-0123456789]", "");
        
        Common.setPage(current);
        App.setRoot(root);
    }
    
    public static void movePage(String newPage, int id) throws IOException {
        setPrev(currentPage);
        setPage(newPage + "-" + id);
        System.out.println(currentPage);
        App.setRoot(newPage);
    }
    
    public static void movePage(String newPage) throws IOException {
        setPrev(currentPage);
        setPage(newPage);
        System.out.println(currentPage);
        App.setRoot(newPage);
    }
    
    public static String doubleStr(double display) {
        return df.format(display);
    }
    
    public static int getIdFromPage() {
        String page = currentPage;
        page = page.replaceAll("[^\\d]", ""); 
        page = page.trim();
        if (page.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(page);
    }
    
    public static boolean isNumber(String input) {
        boolean isNumber = true;
        try {
          Double.parseDouble(input);
        } catch(NumberFormatException e) {
          isNumber = false;
        }
        return isNumber;
    }
    
    public static boolean isInt(String input) {
        boolean isNumber = true;
        try {
          Integer.parseInt(input);
        } catch(NumberFormatException e) {
          isNumber = false;
        }
        return isNumber;
    }
    
    public static void showDashboard() throws IOException {
        switch (currentUser.getStatus()) {
            case MANAGER: {
                movePage("managerdashboard");
                break;
            }
            case EMPLOYEE: {
                movePage("managerdashboard");
                break;
            }
            case SUPPLIER: {
                movePage("managerdashboard");
            }
        }
    }
}
