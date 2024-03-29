package intol.dti.imp.client;

import bftsmart.tom.ServiceProxy;
import intol.dti.objects.coin.CoinDTO;
import intol.dti.objects.nft.NFTDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BFTDit {
    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    private final ServiceProxy serviceProxy;
    private final BFTNft bftNft;
    private final BFTCoin bftCoin;


    public BFTDit(int id) {
        this.serviceProxy = new ServiceProxy(id);
        this.bftNft = new BFTNft(serviceProxy);
        this.bftCoin = new BFTCoin(serviceProxy);
    }


    public CoinDTO[] my_coins() {
        return bftCoin.my_coins();
    }

    public int mint(float value) {
        return bftCoin.mint(value);
    }

    public int spend(int[] coins, int receiver, float value) {
        return bftCoin.spend(coins, receiver, value);
    }

    public NFTDto[] myNFTs() {
        return bftNft.myNFTs();
    }

    public NFTDto mint_nft(float value, String name, String uri) {
        return bftNft.mint_nft(value, name, uri);
    }

    public NFTDto set_price(int id, float price) {
        return bftNft.set_price(id, price);
    }

    public NFTDto[] search_nft(String text) {
        return bftNft.search_nft(text);
    }

    public int buy_nft(int id, int[] coins) {
        return bftNft.buy_nft(id, coins);
    }
}
