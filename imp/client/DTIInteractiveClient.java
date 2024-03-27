package intol.dti.imp.client;

import intol.dti.objects.CoinDTO;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class DTIInteractiveClient {
    public static void main(String[] args) {
        int clientId = (args.length > 0) ? Integer.parseInt(args[0]) : 1001;
        BFTCoin bftCoin = new BFTCoin(clientId);

        Console console = System.console();
        System.out.println("\nCommands:\n");
        System.out.println("\tCoin Related Commands:");
        System.out.println("\t\tCOINS: get the ids and values of the coins associated with this user.");
        System.out.println("\t\tMINT(value): Mint Coins with the specified value with permission.");
        System.out.println("\t\tSPEND(coins, receiver, value): Sends value from the coins " +
                "to the receiver.PS: Insert this format SPEND 1,5,6 2 3");
        while (true) {
            String cmd = console.readLine("\n  > ");
            if (cmd.equalsIgnoreCase("COINS")) {
                CoinDTO[] coins = bftCoin.my_coins();
                if (coins != null) {
                    if (coins.length == 0) {
                        System.out.println("No coins available");
                        continue;
                    }
                    System.out.println("Coins: ");
                    for (CoinDTO coin : coins) {
                        System.out.println("\t" + coin.getId() + " : " + coin.getValue());
                    }
                } else {
                    System.out.println("Failed to retrieve coins");
                }
            } else if (cmd.equalsIgnoreCase("MINT")) {
                try {
                    float value = Float.parseFloat(console.readLine("Enter the float value to be minted: "));
                    System.out.println("Result of Mint: " + bftCoin.mint(value));
                } catch (Exception e) {
                    System.out.println("The value is not a number. Please try again.");
                    continue;
                }

            } else if (cmd.toLowerCase().contains("spend")) {
                try {
                    String[] parts = cmd.split("\\s+");

                    if (parts.length != 4 || !parts[0].contains("spend")) {
                        throw new IllegalArgumentException("Invalid input format. Expected format: SPEND <coins> <receiver> <value>");
                    }

                    String[] coinsStr = parts[1].split(",");
                    List<Integer> coins = new ArrayList<>();
                    for (String coinStr : coinsStr) {
                        coins.add(Integer.parseInt(coinStr));
                    }

                    int receiver = Integer.parseInt(parts[2]);
                    float value = Float.parseFloat(parts[3]);
                    System.out.println("Result of spend " + bftCoin.spend(coins.stream().mapToInt(i -> i).toArray()
                            , receiver, value));


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Either the coin id, receiver id or value is not a number. Please try again.");
                    continue;
                }

            } else {
                System.out.println("Unknown command");
            }

        }
    }
}
