package intol.dti.objects.nft;

import java.util.Objects;

public class NFT {
    private final int id; //no two NFTs can have the same id
    private int owner;
    private final String name;
    private final String URI; //URI to the image of the NFT
    private float value;

    public NFT(int id, int owner, String name, String URI, float value) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.URI = URI;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NFT nft = (NFT) o;
        return id == nft.id && Objects.equals(name, nft.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getURI() {
        return URI;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NFT{" +
                "id=" + id +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", URI='" + URI + '\'' +
                ", value=" + value +
                '}';
    }
}
