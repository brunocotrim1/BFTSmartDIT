package intol.dti.objects;

import java.io.*;

public class BFTCoinMessage implements Serializable {
    private BFTCoinRequestType type;
    private float value;
    private int[] coins;
    private CoinDTO[] coinDTOS;
    private int receiver;
    public static byte[] toBytes(BFTCoinMessage message) throws java.io.IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(message);

        objOut.flush();
        byteOut.flush();

        return byteOut.toByteArray();
    }

    public static BFTCoinMessage fromBytes(byte[] rep) throws Exception {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(rep);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (BFTCoinMessage) objIn.readObject();
    }

    public BFTCoinRequestType getType() {
        return type;
    }

    public void setType(BFTCoinRequestType type) {
        this.type = type;
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

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public CoinDTO[] getCoinDTOS() {
        return coinDTOS;
    }

    public void setCoinDTOS(CoinDTO[] coinDTOS) {
        this.coinDTOS = coinDTOS;
    }

}
