
package model;

public class Wand {
    private final int id;
    private final String core;
    private final String wood;
    private final String status;

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
