package intol.dti.objects.nft;

import java.io.*;
import java.util.Arrays;

public class BFTNftMessage implements Serializable {
    private BFTNftRequestType type;
    private NFTDto[] nfts;
    private String name;
    private String uri;
    private float value;
    private int[] coins;
    private int id;
    private String text;


    public static byte[] toBytes(BFTNftMessage message) throws java.io.IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(message);

        objOut.flush();
        byteOut.flush();

        return byteOut.toByteArray();
    }

    public static BFTNftMessage fromBytes(byte[] rep) throws Exception {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(rep);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (BFTNftMessage) objIn.readObject();
    }

    public BFTNftRequestType getType() {
        return type;
    }

    public void setType(BFTNftRequestType type) {
        this.type = type;
    }

    public NFTDto[] getNfts() {
        return nfts;
    }

    public void setNfts(NFTDto[] nfts) {
        this.nfts = nfts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int[] getCoins() {
        return coins;
    }

    public void setCoins(int[] coins) {
        this.coins = coins;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "BFTNftMessage{" +
                "type=" + type +
                ", nfts=" + Arrays.toString(nfts) +
                ", name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                ", value=" + value +
                ", coins=" + Arrays.toString(coins) +
                ", id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
