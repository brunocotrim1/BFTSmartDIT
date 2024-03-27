package intol.dti.imp.server;

import intol.dti.objects.BFTCoinMessage;
import intol.dti.objects.Coin;
import intol.dti.objects.CoinDTO;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import static org.bouncycastle.util.Arrays.contains;

public class CoinLogic implements Serializable {
    private TreeMap<Integer, Coin> coinMap;
    private int idGenerator = 1;

    public CoinLogic() {
        coinMap = new TreeMap<>();
    }

    public byte[] executeOrderedCoin(BFTCoinMessage message, int sender) throws IOException {
        switch (message.getType()) {
            case MY_COINS:
                return myCoins(message, sender);
            case MINT:
                return mint(message, sender);
            case SPEND:
                return spend(message, sender);
            default:
                System.out.println("Unknown coin request type");
                return new byte[0];
        }
    }

    public byte[] executeUnorderedCoin(BFTCoinMessage message, int sender) throws IOException {
        switch (message.getType()) {
            case MY_COINS:
                return myCoins(message, sender);
            default:
                System.out.println("Unknown coin request type");
                return new byte[0];
        }
    }

    private byte[] myCoins(BFTCoinMessage message, int sender) throws IOException {
        BFTCoinMessage response = new BFTCoinMessage();
        ArrayList<CoinDTO> coinDTOS = new ArrayList<>();
        for (Coin coin : coinMap.values()) {
            if (coin.getOwner() == sender) {
                coinDTOS.add(new CoinDTO(coin.getId(), coin.getValue()));
            }
        }
        response.setCoinDTOS(coinDTOS.toArray(new CoinDTO[0]));
        return BFTCoinMessage.toBytes(response);
    }

    private byte[] mint(BFTCoinMessage message, int sender) throws IOException {
        BFTCoinMessage response = new BFTCoinMessage();
        if (sender != 4) {
            response.setValue(-1);
            return BFTCoinMessage.toBytes(response);
        }
        Coin coin = createCoin(sender, message.getValue());
        response.setCoins(new int[]{coin.getId()});
        return BFTCoinMessage.toBytes(response);
    }


    private byte[] spend(BFTCoinMessage message, int sender) throws IOException {
        BFTCoinMessage response = new BFTCoinMessage();

        float valueFromAllCoins = valueFromAllOwnerCoins(sender,message.getCoins());
        if (valueFromAllCoins == -1) {
            response.setValue(-1);
            return BFTCoinMessage.toBytes(response);
        }
        int receiver = message.getReceiver();
        float value = message.getValue();

        if (valueFromAllCoins < value || sender == receiver) {
            response.setValue(-1);
            return BFTCoinMessage.toBytes(response);
        }
        consumeCoinsOwner(sender,message.getCoins());
        if(valueFromAllCoins - value == 0){
            response.setValue(0);
        }else {
            Coin issuer = createCoin(sender, valueFromAllCoins - value);
            System.out.println("COINS AFTER SPEND:" + coinMap);
            response.setValue(issuer.getId());
        }
        Coin receiverCoin = createCoin(receiver, value);
        return BFTCoinMessage.toBytes(response);
    }

    private float valueFromAllOwnerCoins(int owner, int[] coins) {
        float value = 0;
        for (int coinId : coins) {
            Coin coin = coinMap.get(coinId);
            if (coin == null) {
                return -1;
            }
            if (coin.getOwner() == owner) {
                value += coin.getValue();
            }
        }
        return value;
    }

    private void consumeCoinsOwner(int owner, int[] coins) {
        for (int coinId : coins) {
            Coin coin = coinMap.get(coinId);
            if (coin != null && coin.getOwner() == owner) {
                coinMap.remove(coinId);
            }
        }
    }

    private Coin createCoin(int owner, float value) {
        Coin coin = new Coin(idGenerator, owner, value);
        idGenerator += 1;
        coinMap.put(coin.getId(), coin);
        return coin;
    }

}
