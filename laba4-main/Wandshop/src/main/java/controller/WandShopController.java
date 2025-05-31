package controller;

import model.DatabaseManager;
import model.Wand;
import model.Customer;
import model.Purchase;
import model.Component;
import model.Supply;
import view.WandShopView;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Component;

public class WandShopController {
    private DatabaseManager dbManager;
    private WandShopView view;

    public WandShopController(DatabaseManager dbManager, WandShopView view) {
        this.dbManager = dbManager;
        this.view = view;
        this.view.setController(this);
    }

    public void start() {
        view.start();
    }

    public void addWand(String core, String wood) throws SQLException {
        if (core.trim().isEmpty() || wood.trim().isEmpty()) {
            throw new IllegalArgumentException("Все поля должны быть заполнены!");
        }
        dbManager.addWand(core, wood);
    }

    public void addCustomer(String firstName, String lastName) throws SQLException {
        if (firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Все поля должны быть заполнены!");
        }
        dbManager.addCustomer(firstName, lastName);
    }

    public void addPurchase(int wandId, int customerId) throws SQLException {
        if (wandId <= 0 || customerId <= 0) {
            throw new IllegalArgumentException("ID должны быть положительными числами!");
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dbManager.addPurchase(wandId, customerId, date);
    }

    public void addSupply(String componentType, String componentName, int quantity) throws SQLException {
        if (componentType.trim().isEmpty() || componentName.trim().isEmpty() || quantity <= 0) {
            throw new IllegalArgumentException("Все поля должны быть заполнены, количество должно быть положительным!");
        }
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dbManager.addSupply(componentType, componentName, quantity, date);
    }

    public List<Wand> getAllWands() throws SQLException {
        return dbManager.getAllWands();
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return dbManager.getAllCustomers();
    }

    public List<Purchase> getAllPurchases() throws SQLException {
        return dbManager.getAllPurchases();
    }

    public List<Supply> getAllSupplies() throws SQLException {
        return dbManager.getAllSupplies();
    }
    
    public List<Component> getAllComponents() throws SQLException {
        return dbManager.getAllComponents();
    }
    
    public List<String> getAvailableComponents(String componentType) throws SQLException {
        return dbManager.getAvailableComponents(componentType);
    }

    public void clearAllData() throws SQLException {
        dbManager.clearAllData();
    }

    public void close() throws SQLException {
        dbManager.close();
        view.close();
    }
}