package intol.dti.imp.server;

import intol.dti.objects.coin.BFTCoinMessage;
import intol.dti.objects.coin.Coin;
import intol.dti.objects.coin.CoinDTO;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class CoinLogic implements Serializable {
    private TreeMap<Integer, Coin> coinMap;
    private int idGenerator = 1;

    public CoinLogic() {
        coinMap = new TreeMap<>();
    }

    public byte[] executeOrderedCoin(BFTCoinMessage message, int sender) throws IOException {
        System.out.println("Executing Message: " + message);
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
        if (sender != 4 || message.getValue() <= 0) {
            response.setValue(-1);
            return BFTCoinMessage.toBytes(response);
        }
        Coin coin = createCoin(sender, message.getValue());
        response.setCoins(new int[]{coin.getId()});
        return BFTCoinMessage.toBytes(response);
    }


    private byte[] spend(BFTCoinMessage message, int sender) throws IOException {
        BFTCoinMessage response = new BFTCoinMessage();
        if (message.getCoins() == null || message.getCoins().length == 0 || message.getValue() <= 0) {
            response.setValue(-1);
            return BFTCoinMessage.toBytes(response);
        }
        response.setValue(executeTransaction(message.getCoins(), message.getReceiver(), sender, message.getValue()));
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
            }else {
                return -1;
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
    public int executeTransaction(int[] coins, int receiver, int sender, float value) {
        float valueFromAllCoins = valueFromAllOwnerCoins(sender, coins);
        if (valueFromAllCoins == -1 || value <= 0) {
            return -1;
        }
        if (valueFromAllCoins < value || sender == receiver) {
            return -1;
        }
        consumeCoinsOwner(sender, coins);
        if (valueFromAllCoins - value == 0) {
            Coin receiverCoin = createCoin(receiver, value);
            return 0;
        } else {
            Coin issuer = createCoin(sender, valueFromAllCoins - value);
            Coin receiverCoin = createCoin(receiver, value);
            return issuer.getId();
        }
    }
}
