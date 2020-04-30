package Models;

public class BaseObject {
    private Integer id;

    public BaseObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toCSV() {
        return id.toString();
    }

}
