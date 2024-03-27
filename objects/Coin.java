package intol.dti.objects;

import java.util.Objects;

public class Coin {
    private final int id;//no two coins can have the same id
    private int owner;

    private float value;

    public Coin(int id, int owner, float value) {
        this.id = id;
        this.owner = owner;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return id == coin.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

}
