package intol.dti.imp.client;

import bftsmart.tom.ServiceProxy;
import intol.dti.objects.nft.BFTNftMessage;
import intol.dti.objects.nft.BFTNftRequestType;
import intol.dti.objects.nft.NFTDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static intol.dti.objects.nft.BFTNftRequestType.*;

public class BFTNft {
    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    private final ServiceProxy serviceProxy;

    public BFTNft(ServiceProxy serviceProxy) {
        this.serviceProxy = serviceProxy;
    }


    public NFTDto[] myNFTs() {
        byte[] rep;
        try {
            BFTNftMessage request = new BFTNftMessage();
            request.setType(MY_NFTS);
            //invokes BFT-SMaRt Unordered since this is a read-only operation
            rep = serviceProxy.invokeUnordered(BFTNftMessage.toBytes(request));
            return BFTNftMessage.fromBytes(rep).getNfts();
        } catch (Exception e) {
            logger.error("\tFailed to send MY_NFTS request");
            return null;
        }
    }

    public NFTDto mint_nft(float value, String name, String uri) {
        byte[] rep;
        try {
            BFTNftMessage request = new BFTNftMessage();
            request.setType(MINT_NFT);
            request.setValue(value);
            request.setName(name);
            request.setUri(uri);
            //invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(BFTNftMessage.toBytes(request));
            BFTNftMessage response = BFTNftMessage.fromBytes(rep);
            if (response.getValue() == -1) {
                logger.error("\t Bad NFT Mint Request");
                return null;
            } else if (response.getValue() == -2) {
                logger.error("\t NFT with same name already exists");
                return null;
            }
            return response.getNfts()[0];
        } catch (Exception e) {
            logger.error("Failed to send MINT request");
        }
        return null;
    }

    public NFTDto set_price(int id, float price) {
        byte[] rep;
        try {
            BFTNftMessage request = new BFTNftMessage();
            request.setType(BFTNftRequestType.SET_PRICE);
            request.setId(id);
            request.setValue(price);
            //invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(BFTNftMessage.toBytes(request));
            BFTNftMessage response = BFTNftMessage.fromBytes(rep);
            if (response.getValue() == -1) {
                logger.error("\t Cannot Input a negative value");
                return null;
            } else if (response.getValue() == -2) {
                logger.error("\t You do not own this NFT");
                return null;
            }
            return response.getNfts()[0];
        } catch (Exception e) {
            logger.error("Failed to send SET_PRICE request");
        }
        return null;
    }

    public NFTDto[] search_nft(String text) {
        byte[] rep;
        try {
            BFTNftMessage request = new BFTNftMessage();
            request.setType(BFTNftRequestType.SEARCH_NFT);
            request.setText(text);
            //invokes BFT-SMaRt Unordered since this is a read-only operation
            rep = serviceProxy.invokeUnordered(BFTNftMessage.toBytes(request));
            BFTNftMessage response = BFTNftMessage.fromBytes(rep);
            if (response.getValue() == -1) {
                logger.error("\t Empty text field");
                return null;
            }
            return response.getNfts();
        } catch (Exception e) {
            logger.error("Failed to send SEARCH_NFT request");
            return null;
        }
    }

    public int buy_nft(int id, int[] coins) {
        byte[] rep;
        try {
            BFTNftMessage request = new BFTNftMessage();
            request.setType(BFTNftRequestType.BUY_NFT);
            request.setId(id);
            request.setCoins(coins);
            //invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(BFTNftMessage.toBytes(request));
            BFTNftMessage response = BFTNftMessage.fromBytes(rep);
            if (response.getValue() == -3) {
                logger.error("\t NFT does not exist or you own it already");
                return -1;
            } else if (response.getValue() == -2) {
                logger.error("\t Problem with coins parameter");
                return -1;
            } else if (response.getValue() == -1) {
                logger.error("\t Error processing coin transaction");
                return -1;
            }
            return response.getCoins()[0];
        } catch (Exception e) {
            logger.error("Failed to send BUY_NFT request");
            return -1;
        }
    }
}
