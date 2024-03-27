package intol.dti.imp.server;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import intol.dti.objects.BFTCoinMessage;
import intol.dti.objects.BFTNftMessage;
import intol.dti.objects.Coin;
import intol.dti.objects.NFT;

import java.io.*;
import java.util.TreeMap;

public class DTIServer<K> extends DefaultSingleRecoverable {

    private final ServiceReplica replica;
    private CoinLogic coinLogic = new CoinLogic();
    private NFTLogic nftLogic = new NFTLogic();

    public DTIServer(int id) {
        coinLogic = new CoinLogic();
        nftLogic = new NFTLogic();
        replica = new ServiceReplica(id, this, this);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java BFTMapServer <server id>");
            System.exit(-1);
        }
        new DTIServer<Integer>(Integer.parseInt(args[0]));
    }


    @Override
    public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {
        BFTCoinMessage coinMessage = isCoin(command);
        BFTNftMessage nftMessage = isNFT(command);
        try {

            if (coinMessage != null) {
                return coinLogic.executeOrderedCoin(coinMessage, msgCtx.getSender());

            } else if (nftMessage != null) {
                return nftLogic.executeOrderedNFT(nftMessage);
            }
            return null;

        } catch (IOException ex) {
            //logger.error("Failed to process ordered request", ex);
            return new byte[0];
        }
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        BFTCoinMessage coinMessage = isCoin(command);
        BFTNftMessage nftMessage = isNFT(command);
        try {

            if (coinMessage != null) {
                return coinLogic.executeUnorderedCoin(coinMessage, msgCtx.getSender());

            } else if (nftMessage != null) {
                return nftLogic.executeUnorderedNFT(nftMessage, msgCtx.getSender());
            }
            return null;

        } catch (IOException ex) {
            //logger.error("Failed to process ordered request", ex);
            return new byte[0];
        }
    }

    @Override
    public void installSnapshot(byte[] state) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(state);
             ObjectInput in = new ObjectInputStream(bis)) {
            coinLogic = (CoinLogic) in.readObject();
            nftLogic = (NFTLogic) in.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace(); //debug instruction
        }
    }

    @Override
    public byte[] getSnapshot() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(coinLogic);
            out.writeObject(nftLogic);
            out.flush();
            bos.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(); //debug instruction
            return new byte[0];
        }
    }

    public BFTNftMessage isNFT(byte[] command) {
        try {
            return BFTNftMessage.fromBytes(command);
        } catch (Exception e) {
            return null;
        }
    }

    public BFTCoinMessage isCoin(byte[] command) {
        try {
            return BFTCoinMessage.fromBytes(command);
        } catch (Exception e) {
            return null;
        }
    }
}
