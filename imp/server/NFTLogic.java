package intol.dti.imp.server;

import intol.dti.objects.nft.BFTNftMessage;
import intol.dti.objects.nft.NFT;
import intol.dti.objects.nft.NFTDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;


public class NFTLogic implements java.io.Serializable {

    private int idGenerator = 1;
    private TreeMap<Integer, NFT> nftMap;
    private transient CoinLogic coinLogic;

    public NFTLogic(CoinLogic coinLogic) {
        nftMap = new TreeMap<>();
        this.coinLogic = coinLogic;
    }

    public byte[] executeOrderedNFT(BFTNftMessage nftMessage, int sender) throws IOException {
        System.out.println("Executing Message: " + nftMessage);
        switch (nftMessage.getType()) {
            case MY_NFTS:
                return myNFTs(nftMessage, sender);
            case MINT_NFT:
                return mintNFT(nftMessage, sender);
            case SET_PRICE:
                return setNFTPrice(nftMessage, sender);
            case SEARCH_NFT:
                return search_nft(nftMessage, sender);
            case BUY_NFT:
                return buyNFT(nftMessage, sender);
            default:
                System.out.println("Unknown nft request type");
                return new byte[0];
        }
    }

    public byte[] executeUnorderedNFT(BFTNftMessage nftMessage, int sender) throws IOException {
        switch (nftMessage.getType()) {
            case MY_NFTS:
                return myNFTs(nftMessage, sender);
            case SEARCH_NFT:
                return search_nft(nftMessage, sender);
            default:
                System.out.println("Unknown nft request type");
                return new byte[0];
        }
    }

    private byte[] myNFTs(BFTNftMessage nftMessage, int sender) throws IOException {
        BFTNftMessage response = new BFTNftMessage();
        ArrayList<NFTDto> nftDtos = new ArrayList<>();
        for (NFT nft : nftMap.values()) {
            if (nft.getOwner() == sender) {
                nftDtos.add(new NFTDto(nft.getId(), nft.getOwner(), nft.getValue(), nft.getURI(), nft.getName()));
            }
        }
        response.setNfts(nftDtos.toArray(new NFTDto[0]));
        return BFTNftMessage.toBytes(response);
    }

    private byte[] mintNFT(BFTNftMessage nftMessage, int sender) throws IOException {
        BFTNftMessage response = new BFTNftMessage();
        if (nftMessage.getName() == null || nftMessage.getUri() == null || nftMessage.getValue() <= 0) {
            response.setValue(-1);
            return BFTNftMessage.toBytes(response);
        }

        for (NFT nft : nftMap.values()) {
            if (nft.getName().equals(nftMessage.getName())) {
                response.setValue(-2);
                return BFTNftMessage.toBytes(response);
            }
        }

        NFT createdNFT = createNFT(sender, nftMessage.getName(), nftMessage.getUri(), nftMessage.getValue());
        System.out.println("Created NFT: " + createdNFT);
        response.setNfts(new NFTDto[]{new NFTDto(createdNFT.getId(), createdNFT.getOwner(),
                createdNFT.getValue(), createdNFT.getURI(), createdNFT.getName())});
        return BFTNftMessage.toBytes(response);
    }

    private byte[] setNFTPrice(BFTNftMessage nftMessage, int sender) throws IOException {
        BFTNftMessage response = new BFTNftMessage();
        if (nftMessage.getValue() <= 0) {
            response.setValue(-1);
            return BFTNftMessage.toBytes(response);
        }
        NFT nft = nftMap.get(nftMessage.getId());
        if (nft == null || nft.getOwner() != sender) {
            response.setValue(-2);
            return BFTNftMessage.toBytes(response);
        }
        nft.setValue(nftMessage.getValue());
        response.setNfts(new NFTDto[]{new NFTDto(nft.getId(), nft.getOwner(), nft.getValue(), nft.getURI(), nft.getName())});
        return BFTNftMessage.toBytes(response);
    }

    private byte[] search_nft(BFTNftMessage nftMessage, int sender) throws IOException {
        BFTNftMessage response = new BFTNftMessage();
        if (nftMessage.getText() == null) {
            response.setValue(-1);
            return BFTNftMessage.toBytes(response);
        }

        ArrayList<NFTDto> nftDtos = new ArrayList<>();
        for (NFT nft : nftMap.values()) {
            String name = nft.getName();
            name = name.toLowerCase();
            if (name.contains(nftMessage.getText().toLowerCase())) {
                nftDtos.add(new NFTDto(nft.getId(), nft.getOwner(), nft.getValue(), nft.getURI(), nft.getName()));
            }
        }
        response.setNfts(nftDtos.toArray(new NFTDto[0]));
        return BFTNftMessage.toBytes(response);
    }

    private byte[] buyNFT(BFTNftMessage nftMessage, int sender) throws IOException {
        BFTNftMessage response = new BFTNftMessage();
        if (nftMap.get(nftMessage.getId()) == null || nftMap.get(nftMessage.getId()).getOwner() == sender) {
            response.setValue(-3);
            return BFTNftMessage.toBytes(response);
        }
        if (nftMessage.getCoins() == null || nftMessage.getCoins().length == 0) {
            response.setValue(-2);
            return BFTNftMessage.toBytes(response);
        }

        int transaction = coinLogic.executeTransaction(nftMessage.getCoins(), nftMap.get(nftMessage.getId()).getOwner(),
                sender, nftMap.get(nftMessage.getId()).getValue());
        if (transaction == -1) {
            response.setValue(-1);
            return BFTNftMessage.toBytes(response);
        }
        nftMap.get(nftMessage.getId()).setOwner(sender);
        response.setCoins(new int[]{transaction});
        return BFTNftMessage.toBytes(response);
    }

    private NFT createNFT(int sender, String name, String uri, float value) {
        NFT nft = new NFT(idGenerator, sender, name, uri, value);
        nftMap.put(idGenerator, nft);
        idGenerator++;
        return nft;
    }

    public void setCoinLogic(CoinLogic coinLogic) {
        this.coinLogic = coinLogic;
    }

}
