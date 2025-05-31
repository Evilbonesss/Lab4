
package model;

public class Wand {
    private int id;
    private String core;
    private String wood;
    private String status;

    public Wand(int id, String core, String wood, String status) {
        this.id = id;
        this.core = core;
        this.wood = wood;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCore() {
        return core;
    }

    public String getWood() {
        return wood;
    }

    public String getStatus() {
        return status;
    }
}
