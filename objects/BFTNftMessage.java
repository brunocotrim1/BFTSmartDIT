package intol.dti.objects;

import java.io.*;

public class BFTNftMessage implements Serializable {

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
}
