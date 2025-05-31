package mephi.b23902.i.laba4;

import controller.WandShopController;
import model.DatabaseManager;
import view.WandShopView;
import javax.swing.*;
import java.sql.SQLException;

public class LABA4 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseManager dbManager = new DatabaseManager();
            WandShopView view = new WandShopView();
            WandShopController controller = new WandShopController(dbManager, view);
            controller.start();

            view.mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    try {
                        controller.close();
                    } catch (SQLException ex) {
                        System.err.println("Ошибка при закрытии: " + ex.getMessage());
                    }
                }
            });
        });
    }
}