
package model;

public class Component { 
    private String componentType; 
    private String componentName; 
    private int quantity;
    
    public Component(String componentType, String componentName, int quantity) {
        this.componentType = componentType;
        this.componentName = componentName;
        this.quantity = quantity;
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
}
