package intol.dti.imp.client;

import bftsmart.tom.ServiceProxy;
import intol.dti.objects.coin.BFTCoinMessage;
import intol.dti.objects.coin.BFTCoinRequestType;
import intol.dti.objects.coin.CoinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static intol.dti.objects.coin.BFTCoinRequestType.MINT;
import static intol.dti.objects.coin.BFTCoinRequestType.MY_COINS;

public class BFTCoin {
    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    private final ServiceProxy serviceProxy;

    public BFTCoin(ServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }

    public CoinDTO[] my_coins() {
        byte[] rep;
        try {
            BFTCoinMessage request = new BFTCoinMessage();
            request.setType(MY_COINS);
            //invokes BFT-SMaRt Unordered since this is a read-only operation
            rep = serviceProxy.invokeUnordered(BFTCoinMessage.toBytes(request));
            return BFTCoinMessage.fromBytes(rep).getCoinDTOS();
        } catch (Exception e) {
            logger.error("Failed to send MY_COINS request");
            return null;
        }
    }

    public int mint(float value) {
        byte[] rep;
        try {
            BFTCoinMessage request = new BFTCoinMessage();
            request.setType(MINT);
            request.setValue(value);
            //invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(BFTCoinMessage.toBytes(request));
            BFTCoinMessage response = BFTCoinMessage.fromBytes(rep);
            if (response.getValue() == -1) {
                logger.error("You dont have permission to mint coins");
                return -1;
            }
            return BFTCoinMessage.fromBytes(rep).getCoins()[0];
        } catch (Exception e) {
            logger.error("Failed to send MINT request");
        }
        return -1;
    }

    public int spend(int[] coins, int receiver, float value) {
        byte[] rep;
        try {
            BFTCoinMessage request = new BFTCoinMessage();
            request.setType(BFTCoinRequestType.SPEND);
            request.setCoins(coins);
            request.setReceiver(receiver);
            request.setValue(value);
            //invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(BFTCoinMessage.toBytes(request));
            return (int) BFTCoinMessage.fromBytes(rep).getValue();
        } catch (Exception e) {
            logger.error("Failed to send SPEND request");
        }
        return -1;
    }


}
