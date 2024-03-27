package intol.dti.objects;

public class CoinDTO implements java.io.Serializable {
    private final int id;
    private float value;

    public CoinDTO(int id, float value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }


    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
