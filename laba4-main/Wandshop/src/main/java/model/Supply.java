
package model;

public class Supply {
    private int id;
    private String componentType;
    private String componentName;
    private int quantity;
    private String supplyDate;

    public Supply(int id, String componentType, String componentName, int quantity, String supplyDate) {
        this.id = id;
        this.componentType = componentType;
        this.componentName = componentName;
        this.quantity = quantity;
        this.supplyDate = supplyDate;
    }

    public int getId() {
        return id;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getComponentName() {
        return componentName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplyDate() {
        return supplyDate;
    }
}