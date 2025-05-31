package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:wandshop.db");
            createTables();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ошибка подключения к базе данных: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            connection = null;
        }
    }

    private void createTables() throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        String createWandsTable = "CREATE TABLE IF NOT EXISTS Wands (" +
                "wand_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "core TEXT NOT NULL, " +
                "wood TEXT NOT NULL, " +
                "status TEXT NOT NULL)";
        String createCustomersTable = "CREATE TABLE IF NOT EXISTS Customers (" +
                "customer_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL)";
        String createPurchasesTable = "CREATE TABLE IF NOT EXISTS Purchases (" +
                "purchase_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "wand_id INTEGER, " +
                "customer_id INTEGER, " +
                "purchase_date TEXT, " +
                "FOREIGN KEY (wand_id) REFERENCES Wands(wand_id), " +
                "FOREIGN KEY (customer_id) REFERENCES Customers(customer_id))";
        String createSuppliesTable = "CREATE TABLE IF NOT EXISTS Supplies (" +
                "supply_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "component_type TEXT NOT NULL, " +
                "component_name TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "supply_date TEXT NOT NULL)";
        String createComponentsTable = "CREATE TABLE IF NOT EXISTS Components (" +
                "component_type TEXT NOT NULL, " +
                "component_name TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "PRIMARY KEY (component_type, component_name))";

        Statement statement = connection.createStatement();
        statement.execute(createWandsTable);
        statement.execute(createCustomersTable);
        statement.execute(createPurchasesTable);
        statement.execute(createSuppliesTable);
        statement.execute(createComponentsTable);
        statement.close();
    }

    private boolean checkAndUpdateStock(String core, String wood) throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }

        String checkCoreSql = "SELECT quantity FROM Components WHERE component_type = ? AND component_name = ?";
        PreparedStatement coreStmt = connection.prepareStatement(checkCoreSql);
        coreStmt.setString(1, "сердцевина");
        coreStmt.setString(2, core);
        ResultSet coreRs = coreStmt.executeQuery();
        int coreQuantity = coreRs.next() ? coreRs.getInt("quantity") : 0;
        coreRs.close();
        coreStmt.close();

        String checkWoodSql = "SELECT quantity FROM Components WHERE component_type = ? AND component_name = ?";
        PreparedStatement woodStmt = connection.prepareStatement(checkWoodSql);
        woodStmt.setString(1, "древесина");
        woodStmt.setString(2, wood);
        ResultSet woodRs = woodStmt.executeQuery();
        int woodQuantity = woodRs.next() ? woodRs.getInt("quantity") : 0;
        woodRs.close();
        woodStmt.close();

        if (coreQuantity < 1 || woodQuantity < 1) {
            return false;
        }

        String updateCoreSql = "UPDATE Components SET quantity = quantity - 1 WHERE component_type = ? AND component_name = ?";
        PreparedStatement updateCoreStmt = connection.prepareStatement(updateCoreSql);
        updateCoreStmt.setString(1, "сердцевина");
        updateCoreStmt.setString(2, core);
        updateCoreStmt.executeUpdate();
        updateCoreStmt.close();

        String updateWoodSql = "UPDATE Components SET quantity = quantity - 1 WHERE component_type = ? AND component_name = ?";
        PreparedStatement updateWoodStmt = connection.prepareStatement(updateWoodSql);
        updateWoodStmt.setString(1, "древесина");
        updateWoodStmt.setString(2, wood);
        updateWoodStmt.executeUpdate();
        updateWoodStmt.close();

        return true;
    }

    public void addWand(String core, String wood) throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }

        if (!checkAndUpdateStock(core, wood)) {
            throw new SQLException("Недостаточно ингредиентов на складе: требуется 1 единица " + core + " и 1 единица " + wood);
        }

        String sql = "INSERT INTO Wands (core, wood, status) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, core);
        ps.setString(2, wood);
        ps.setString(3, "В наличии");
        ps.executeUpdate();
        ps.close();
    }

    public void addCustomer(String firstName, String lastName) throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        String sql = "INSERT INTO Customers (first_name, last_name) VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.executeUpdate();
        ps.close();
    }

    public void addPurchase(int wandId, int customerId, String purchaseDate) throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        String updateWand = "UPDATE Wands SET status = ? WHERE wand_id = ?";
        PreparedStatement ps1 = connection.prepareStatement(updateWand);
        ps1.setString(1, "Продана");
        ps1.setInt(2, wandId);
        ps1.executeUpdate();
        ps1.close();

        String sql = "INSERT INTO Purchases (wand_id, customer_id, purchase_date) VALUES (?, ?, ?)";
        PreparedStatement ps2 = connection.prepareStatement(sql);
        ps2.setInt(1, wandId);
        ps2.setInt(2, customerId);
        ps2.setString(3, purchaseDate);
        ps2.executeUpdate();
        ps2.close();
    }

    public void addSupply(String componentType, String componentName, int quantity, String supplyDate) throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        // Добавляем запись в Supplies
        String supplySql = "INSERT INTO Supplies (component_type, component_name, quantity, supply_date) VALUES (?, ?, ?, ?)";
        PreparedStatement supplyPs = connection.prepareStatement(supplySql);
        supplyPs.setString(1, componentType);
        supplyPs.setString(2, componentName);
        supplyPs.setInt(3, quantity);
        supplyPs.setString(4, supplyDate);
        supplyPs.executeUpdate();
        supplyPs.close();

        String checkComponentSql = "SELECT quantity FROM Components WHERE component_type = ? AND component_name = ?";
        PreparedStatement checkPs = connection.prepareStatement(checkComponentSql);
        checkPs.setString(1, componentType);
        checkPs.setString(2, componentName);
        ResultSet rs = checkPs.executeQuery();
        if (rs.next()) {
            int currentQuantity = rs.getInt("quantity");
            String updateSql = "UPDATE Components SET quantity = ? WHERE component_type = ? AND component_name = ?";
            PreparedStatement updatePs = connection.prepareStatement(updateSql);
            updatePs.setInt(1, currentQuantity + quantity);
            updatePs.setString(2, componentType);
            updatePs.setString(3, componentName);
            updatePs.executeUpdate();
            updatePs.close();
        } else {
            String insertSql = "INSERT INTO Components (component_type, component_name, quantity) VALUES (?, ?, ?)";
            PreparedStatement insertPs = connection.prepareStatement(insertSql);
            insertPs.setString(1, componentType);
            insertPs.setString(2, componentName);
            insertPs.setInt(3, quantity);
            insertPs.executeUpdate();
            insertPs.close();
        }
        rs.close();
        checkPs.close();
    }

    public List<Wand> getAllWands() throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        List<Wand> wands = new ArrayList<>();
        String sql = "SELECT * FROM Wands";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            wands.add(new Wand(
                    rs.getInt("wand_id"),
                    rs.getString("core"),
                    rs.getString("wood"),
                    rs.getString("status")
            ));
        }
        rs.close();
        statement.close();
        return wands;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            customers.add(new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
            ));
        }
        rs.close();
        statement.close();
        return customers;
    }

    public List<Purchase> getAllPurchases() throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM Purchases";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            purchases.add(new Purchase(
                    rs.getInt("purchase_id"),
                    rs.getInt("wand_id"),
                    rs.getInt("customer_id"),
                    rs.getString("purchase_date")
            ));
        }
        rs.close();
        statement.close();
        return purchases;
    }

    public List<Supply> getAllSupplies() throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        List<Supply> supplies = new ArrayList<>();
        String sql = "SELECT * FROM Supplies";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            supplies.add(new Supply(
                    rs.getInt("supply_id"),
                    rs.getString("component_type"),
                    rs.getString("component_name"),
                    rs.getInt("quantity"),
                    rs.getString("supply_date")
            ));
        }
        rs.close();
        statement.close();
        return supplies;
    }
    
    public List<Component> getAllComponents() throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        List<Component> components = new ArrayList<>();
        String sql = "SELECT * FROM Components";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            components.add(new Component(
                    rs.getString("component_type"),
                    rs.getString("component_name"),
                    rs.getInt("quantity")
            ));
        }
        rs.close();
        statement.close();
        return components;
    }

    public List<String> getAvailableComponents(String componentType) throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        List<String> components = new ArrayList<>();
        String sql = "SELECT component_name FROM Components WHERE component_type = ? AND quantity > 0";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, componentType);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            components.add(rs.getString("component_name"));
        }
        rs.close();
        ps.close();
        return components;
    }

    public void clearAllData() throws SQLException {
        if (connection == null) {
            throw new SQLException("Подключение к базе данных не установлено");
        }
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM Purchases");
        statement.execute("DELETE FROM Wands");
        statement.execute("DELETE FROM Customers");
        statement.execute("DELETE FROM Supplies");
        statement.execute("DELETE FROM Components");
        statement.close();
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}