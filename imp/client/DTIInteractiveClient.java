package intol.dti.imp.client;

import intol.dti.objects.coin.CoinDTO;
import intol.dti.objects.nft.NFTDto;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class DTIInteractiveClient {
    public static void main(String[] args) {
        int clientId = (args.length > 0) ? Integer.parseInt(args[0]) : 1001;
        BFTDit bftDit = new BFTDit(clientId);
        Console console = System.console();
        System.out.println("\nCommands:\n");
        System.out.println("\tCoin Related Commands:");
        System.out.println("\t\tCOINS: get the ids and values of the coins associated with this user.");
        System.out.println("\t\tMINT(value): Mint Coins with the specified value with permission." +
                " for this one value is requested after typing mint");
        System.out.println("\t\tSPEND(coins, receiver, value): Sends value from the coins " +
                "to the receiver.PS: Insert this format SPEND 1,5,6 2 3");
        System.out.println("\n\t NFT Related Commands:");
        System.out.println("\t\tMY_NFTS: get the ids and values of the NFTs associated with this user.");
        System.out.println("\t\tMINT_NFT(name, uri, value): Mint NFTs with the specified value, name and uri");
        System.out.println("\t\tSET_PRICE(id, price): Set the price of the NFT with the specified id");
        System.out.println("\t\tSEARCH_NFT(text): Search for NFTs with the specified name");
        System.out.println("\t\tBUY_NFT(id, coins): Buy the NFT with the specified id with the given coins");
        while (true) {
            String cmd = console.readLine("\n  > ");
            if (cmd.equalsIgnoreCase("COINS")) {
                CoinDTO[] coins = bftDit.my_coins();
                if (coins != null) {
                    if (coins.length == 0) {
                        System.out.println("\tNo coins available");
                        continue;
                    }
                    System.out.println("\tCoins: ");
                    float total = 0;
                    for (CoinDTO coin : coins) {
                        System.out.println("\t\t Coin:" + coin.getId() + " : " + coin.getValue());
                        total += coin.getValue();
                    }
                    System.out.println("\t\tTotal value of coins: " + total);
                } else {
                    System.out.println("\tFailed to retrieve coins");
                }
            } else if (cmd.equalsIgnoreCase("MINT")) {
                try {
                    float value = Float.parseFloat(console.readLine("\tEnter the float value to be minted: "));
                    System.out.println("\tId of the minted Coin: " + bftDit.mint(value));
                } catch (Exception e) {
                    System.out.println("\tThe value is not a number. Please try again.");
                }

            } else if (cmd.toLowerCase().contains("spend")) {
                try {
                    String[] parts = cmd.split("\\s+");

                    if (parts.length != 4 || !parts[0].contains("spend")) {
                        throw new IllegalArgumentException("\tInvalid input format. Expected format: SPEND <coins> <receiver> <value>");
                    }

                    String[] coinsStr = parts[1].split(",");
                    List<Integer> coins = new ArrayList<>();
                    for (String coinStr : coinsStr) {
                        coins.add(Integer.parseInt(coinStr));
                    }

                    int receiver = Integer.parseInt(parts[2]);
                    float value = Float.parseFloat(parts[3]);
                    System.out.println("Result of spend " + bftDit.spend(coins.stream().mapToInt(i -> i).toArray()
                            , receiver, value));


                } catch (Exception e) {
                    System.out.println("\tEither the coin id, receiver id or value is not a number. Please try again. with format" +
                            "SPEND 1,5,6 2 3");
                }

            } else if (cmd.equalsIgnoreCase("MY_NFTS")) {
                NFTDto[] nfts = bftDit.myNFTs();
                if (nfts != null) {
                    if (nfts.length == 0) {
                        System.out.println("\tNo NFTs available");
                        continue;
                    }
                    System.out.println("\tNFTs: ");
                    for (NFTDto nft : nfts) {
                        System.out.println("\t\t NFT:" + nft.getId() + " : " + nft.getName() + " : "+ nft.getUri() + " : " + nft.getValue());
                    }
                } else {
                    System.out.println("\tFailed to retrieve NFTs");
                }

            } else if (cmd.toLowerCase().contains("mint_nft")) {
                try {
                    String[] parts = cmd.split("\\s+");

                    if (parts.length != 4) {
                        throw new IllegalArgumentException("\tInvalid input format. Expected format: MINT_NFT <name> <uri> <value>");
                    }

                    String name = parts[1];
                    String uri = parts[2];
                    float value = Float.parseFloat(parts[3]);
                    NFTDto nft = bftDit.mint_nft(value, name, uri);
                    if (nft != null) {
                        System.out.println("\tId of the minted NFT: " + nft.getId() + " with name: " + nft.getName()
                                + " and uri: " + nft.getUri() + " and value: " + nft.getValue());
                    } else {
                        System.out.println("\tFailed to mint NFT");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("\tEither the value is not a number or the name or uri is not a string. Please try again.");
                }

            } else if (cmd.toLowerCase().contains("set_price")) {
                try {
                    String[] parts = cmd.split("\\s+");

                    if (parts.length != 3 || !parts[0].contains("set_price")) {
                        throw new IllegalArgumentException("\tInvalid input format. Expected format: SET_PRICE <id> <price>");
                    }

                    int id = Integer.parseInt(parts[1]);
                    float price = Float.parseFloat(parts[2]);
                    NFTDto nft = bftDit.set_price(id, price);
                    if (nft != null) {
                        System.out.println("\tId of the NFT: " + nft.getId() + " with name: " + nft.getName()
                                + " and uri: " + nft.getUri() + " and value: " + nft.getValue());
                    } else {
                        System.out.println("\tFailed to set price for NFT");
                    }
                } catch (Exception e) {
                    System.out.println("\tEither the id is not a number or the price is not a number. Please try again.");
                }

            } else if (cmd.toLowerCase().contains("search_nft")) {
                try {
                    String[] parts = cmd.split("\\s+");

                    if (parts.length != 2 || !parts[0].contains("search_nft")) {
                        throw new IllegalArgumentException("\tInvalid input format. Expected format: SEARCH_NFT <text>");
                    }

                    String text = parts[1];
                    NFTDto[] nfts = bftDit.search_nft(text);
                    if (nfts != null) {
                        if (nfts.length == 0) {
                            System.out.println("\tNo NFTs available");
                            continue;
                        }
                        System.out.println("\tNFTs: ");
                        for (NFTDto nft : nfts) {
                            System.out.println("\t\t NFT:" + nft.getId() + " : " + nft.getName() + " : " +
                                    nft.getUri() + " : " + nft.getValue());
                        }
                    } else {
                        System.out.println("\tFailed to search NFTs");
                    }
                } catch (Exception e) {
                    System.out.println("\tEither the text is not a string. Please try again.");
                }

            } else if (cmd.toLowerCase().contains("buy_nft")) {
                try {
                    String[] parts = cmd.split("\\s+");

                    if (parts.length != 3 || !parts[0].contains("buy_nft")) {
                        throw new IllegalArgumentException("\tInvalid input format. Expected format: BUY_NFT <id> <coins>");
                    }

                    int id = Integer.parseInt(parts[1]);
                    String[] coinsStr = parts[2].split(",");
                    List<Integer> coins = new ArrayList<>();
                    for (String coinStr : coinsStr) {
                        coins.add(Integer.parseInt(coinStr));
                    }
                    int transactionResult = bftDit.buy_nft(id, coins.stream().mapToInt(i -> i).toArray());
                    if (transactionResult == -1) {
                        System.out.println("\tFailed to buy NFT");
                    } else {
                        System.out.println("\tTransaction id: " + transactionResult);
                    }
                } catch (Exception e) {
                    System.out.println("\tEither the id is not a number or the coins are not numbers. Please try again.");
                }

            } else if (cmd.equalsIgnoreCase("Help")) {
                System.out.println("\nCommands:\n");
                System.out.println("\tCoin Related Commands:");
                System.out.println("\t\tCOINS: get the ids and values of the coins associated with this user.");
                System.out.println("\t\tMINT(value): Mint Coins with the specified value with permission." +
                        " for this one value is requested after typing mint");
                System.out.println("\t\tSPEND(coins, receiver, value): Sends value from the coins " +
                        "to the receiver.PS: Insert this format SPEND 1,5,6 2 3");
                System.out.println("\n\t NFT Related Commands:");
                System.out.println("\t\tMY_NFTS: get the ids and values of the NFTs associated with this user.");
                System.out.println("\t\tMINT_NFT(name, uri, value): Mint NFTs with the specified value, name and uri");
                System.out.println("\t\tSET_PRICE(id, price): Set the price of the NFT with the specified id");
                System.out.println("\t\tSEARCH_NFT(text): Search for NFTs with the specified name");
                System.out.println("\t\tBUY_NFT(id, coins): Buy the NFT with the specified id with the given coins");
            } else {
                System.out.println("\tUnknown command");
            }

        }
    }
}
