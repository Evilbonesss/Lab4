package view;

import controller.WandShopController;
import model.Customer;
import model.Purchase;
import model.Supply;
import model.Wand;
import model.Component;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class WandShopView {
    private WandShopController controller;
    public JFrame mainFrame;

    public WandShopView() {
        mainFrame = new JFrame("Магазин волшебных палочек Олливандера");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        mainFrame.setLocationRelativeTo(null);
    }

    public void setController(WandShopController controller) {
        this.controller = controller;
    }

    public void start() {
        JMenuBar menuBar = new JMenuBar();
        JMenu addMenu = new JMenu("Добавить");
        JMenu viewMenu = new JMenu("Просмотреть");
        JMenu clearMenu = new JMenu("Очистить");

        JMenuItem addWandItem = new JMenuItem("Добавить палочку");
        JMenuItem addCustomerItem = new JMenuItem("Добавить покупателя");
        JMenuItem addPurchaseItem = new JMenuItem("Зарегистрировать покупку");
        JMenuItem addSupplyItem = new JMenuItem("Добавить поставку");
        addMenu.add(addWandItem);
        addMenu.add(addCustomerItem);
        addMenu.add(addPurchaseItem);
        addMenu.add(addSupplyItem);

        JMenuItem viewWandsItem = new JMenuItem("Склад");
        JMenuItem viewCustomersItem = new JMenuItem("Покупатели");
        JMenuItem viewPurchasesItem = new JMenuItem("Покупки");
        JMenuItem viewSuppliesItem = new JMenuItem("Поставки");
        JMenuItem viewComponentsItem = new JMenuItem("Компоненты");
        viewMenu.add(viewWandsItem);
        viewMenu.add(viewCustomersItem);
        viewMenu.add(viewPurchasesItem);
        viewMenu.add(viewSuppliesItem);
        viewMenu.add(viewComponentsItem);

        JMenuItem clearDataItem = new JMenuItem("Очистить все данные");
        clearMenu.add(clearDataItem);

        menuBar.add(addMenu);
        menuBar.add(viewMenu);
        menuBar.add(clearMenu);
        mainFrame.setJMenuBar(menuBar);

        addWandItem.addActionListener(e -> showAddWandDialog());
        addCustomerItem.addActionListener(e -> showAddCustomerDialog());
        addPurchaseItem.addActionListener(e -> showAddPurchaseDialog());
        addSupplyItem.addActionListener(e -> showAddSupplyDialog());
        viewWandsItem.addActionListener(e -> showWands());
        viewCustomersItem.addActionListener(e -> showCustomers());
        viewPurchasesItem.addActionListener(e -> showPurchases());
        viewSuppliesItem.addActionListener(e -> showSupplies());
        viewComponentsItem.addActionListener(e -> showComponents());
        clearDataItem.addActionListener(e -> clearData());

        mainFrame.setVisible(true);
    }

    private void showAddWandDialog() {
        JDialog dialog = new JDialog(mainFrame, "Добавить палочку", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(mainFrame);

        JLabel coreLabel = new JLabel("Сердцевина:");
        JComboBox<String> coreCombo = new JComboBox<>();
        JLabel woodLabel = new JLabel("Древесина:");
        JComboBox<String> woodCombo = new JComboBox<>();
        JButton addButton = new JButton("Добавить");

        try {
            List<String> cores = controller.getAvailableComponents("сердцевина");
            List<String> woods = controller.getAvailableComponents("древесина");
            for (String core : cores) {
                coreCombo.addItem(core);
            }
            for (String wood : woods) {
                woodCombo.addItem(wood);
            }

            addButton.setEnabled(coreCombo.getItemCount() > 0 && woodCombo.getItemCount() > 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Ошибка загрузки компонентов: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            return;
        }

        addButton.addActionListener(e -> {
            try {
                String selectedCore = (String) coreCombo.getSelectedItem();
                String selectedWood = (String) woodCombo.getSelectedItem();
                if (selectedCore == null || selectedWood == null) {
                    throw new IllegalArgumentException("Выберите сердцевину и древесину!");
                }
                controller.addWand(selectedCore, selectedWood);
                JOptionPane.showMessageDialog(dialog, "Палочка добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(coreLabel);
        dialog.add(coreCombo);
        dialog.add(woodLabel);
        dialog.add(woodCombo);
        dialog.add(new JLabel());
        dialog.add(addButton);
        dialog.setVisible(true);
    }

    private void showAddCustomerDialog() {
        JDialog dialog = new JDialog(mainFrame, "Добавить покупателя", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(mainFrame);

        JLabel firstNameLabel = new JLabel("Имя:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Фамилия:");
        JTextField lastNameField = new JTextField();
        JButton addButton = new JButton("Добавить");

        addButton.addActionListener(e -> {
            try {
                controller.addCustomer(firstNameField.getText(), lastNameField.getText());
                JOptionPane.showMessageDialog(dialog, "Покупатель добавлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(firstNameLabel);
        dialog.add(firstNameField);
        dialog.add(lastNameLabel);
        dialog.add(lastNameField);
        dialog.add(new JLabel());
        dialog.add(addButton);
        dialog.setVisible(true);
    }

    private void showAddPurchaseDialog() {
        JDialog dialog = new JDialog(mainFrame, "Зарегистрировать покупку", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(mainFrame);

        JLabel wandIdLabel = new JLabel("ID палочки:");
        JTextField wandIdField = new JTextField();
        JLabel customerIdLabel = new JLabel("ID покупателя:");
        JTextField customerIdField = new JTextField();
        JButton addButton = new JButton("Зарегистрировать");

        addButton.addActionListener(e -> {
            try {
                int wandId = Integer.parseInt(wandIdField.getText());
                int customerId = Integer.parseInt(customerIdField.getText());
                controller.addPurchase(wandId, customerId);
                JOptionPane.showMessageDialog(dialog, "Покупка зарегистрирована!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(wandIdLabel);
        dialog.add(wandIdField);
        dialog.add(customerIdLabel);
        dialog.add(customerIdField);
        dialog.add(new JLabel());
        dialog.add(addButton);
        dialog.setVisible(true);
    }

    private void showAddSupplyDialog() {
        JDialog dialog = new JDialog(mainFrame, "Добавить поставку", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(mainFrame);

        JLabel typeLabel = new JLabel("Тип:");
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"сердцевина", "древесина"});
        JLabel nameLabel = new JLabel("Название:");
        JTextField nameField = new JTextField();
        JLabel quantityLabel = new JLabel("Количество:");
        JTextField quantityField = new JTextField();
        JButton addButton = new JButton("Добавить");

        addButton.addActionListener(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                String selectedType = (String) typeCombo.getSelectedItem();
                controller.addSupply(selectedType, nameField.getText(), quantity);
                JOptionPane.showMessageDialog(dialog, "Поставка добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(typeLabel);
        dialog.add(typeCombo);
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(quantityLabel);
        dialog.add(quantityField);
        dialog.add(new JLabel());
        dialog.add(addButton);
        dialog.setVisible(true);
    }

    private void showWands() {
        try {
            List<Wand> wands = controller.getAllWands();
            String[] columnNames = {"ID", "Сердцевина", "Древесина", "Статус"};
            Object[][] data = new Object[wands.size()][4];
            for (int i = 0; i < wands.size(); i++) {
                Wand wand = wands.get(i);
                data[i][0] = wand.getId();
                data[i][1] = wand.getCore();
                data[i][2] = wand.getWood();
                data[i][3] = wand.getStatus();
            }
            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            JDialog dialog = new JDialog(mainFrame, "Палочки на складе", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.setSize(500, 300);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCustomers() {
        try {
            List<Customer> customers = controller.getAllCustomers();
            String[] columnNames = {"ID", "Имя", "Фамилия"};
            Object[][] data = new Object[customers.size()][3];
            for (int i = 0; i < customers.size(); i++) {
                Customer customer = customers.get(i);
                data[i][0] = customer.getId();
                data[i][1] = customer.getFirstName();
                data[i][2] = customer.getLastName();
            }
            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            JDialog dialog = new JDialog(mainFrame, "Покупатели", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPurchases() {
        try {
            List<Purchase> purchases = controller.getAllPurchases();
            String[] columnNames = {"ID покупки", "ID палочки", "ID покупателя", "Дата"};
            Object[][] data = new Object[purchases.size()][4];
            for (int i = 0; i < purchases.size(); i++) {
                Purchase purchase = purchases.get(i);
                data[i][0] = purchase.getId();
                data[i][1] = purchase.getWandId();
                data[i][2] = purchase.getCustomerId();
                data[i][3] = purchase.getPurchaseDate();
            }
            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            JDialog dialog = new JDialog(mainFrame, "Покупки", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.setSize(500, 300);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSupplies() {
        try {
            List<Supply> supplies = controller.getAllSupplies();
            String[] columnNames = {"ID", "Тип", "Название", "Количество", "Дата"};
            Object[][] data = new Object[supplies.size()][5];
            for (int i = 0; i < supplies.size(); i++) {
                Supply supply = supplies.get(i);
                data[i][0] = supply.getId();
                data[i][1] = supply.getComponentType();
                data[i][2] = supply.getComponentName();
                data[i][3] = supply.getQuantity();
                data[i][4] = supply.getSupplyDate();
            }
            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            JDialog dialog = new JDialog(mainFrame, "Поставки", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.setSize(600, 300);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showComponents() {
        try {
            List<Component> components = controller.getAllComponents();
            String[] columnNames = {"Тип", "Название", "Количество"};
            Object[][] data = new Object[components.size()][3];
            for (int i = 0; i < components.size(); i++) {
                Component component = components.get(i);
                data[i][0] = component.getComponentType();
                data[i][1] = component.getComponentName();
                data[i][2] = component.getQuantity();
            }
            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            JDialog dialog = new JDialog(mainFrame, "Компоненты на складе", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(scrollPane, BorderLayout.CENTER);
            dialog.setSize(500, 300);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearData() {
        int result = JOptionPane.showConfirmDialog(mainFrame, "Вы уверены, что хотите очистить все данные?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                controller.clearAllData();
                JOptionPane.showMessageDialog(mainFrame, "Все данные очищены!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void close() {
        mainFrame.dispose();
    }
}