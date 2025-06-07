
package model;

public class Supply {
    private final int id;
    private final String componentType;
    private final String componentName;
    private final int quantity;
    private final String supplyDate;

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