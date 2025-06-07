
package model;

public class Purchase {
    private final int id;
    private final int wandId;
    private final int customerId;
    private final String purchaseDate;

    public Purchase(int id, int wandId, int customerId, String purchaseDate) {
        this.id = id;
        this.wandId = wandId;
        this.customerId = customerId;
        this.purchaseDate = purchaseDate;
    }

    public int getId() {
        return id;
    }

    public int getWandId() {
        return wandId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }
}
