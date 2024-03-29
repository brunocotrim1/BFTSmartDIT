package intol.dti.objects.nft;

public class NFTDto implements java.io.Serializable{
    public NFTDto(int id, int owner, float value, String uri, String name) {
        this.id = id;
        this.owner = owner;
        this.value = value;
        this.uri = uri;
        this.name = name;
    }
    private int id;
    private int owner;
    private float value;
    private String uri;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NFTDto{" +
                "id=" + id +
                ", owner=" + owner +
                ", value=" + value +
                ", uri='" + uri + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
