import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Main {

    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {
        blockchain.add(new Block("First block", "0"));
        blockchain.add(new Block("Second block", blockchain.get(blockchain.size() - 1).hash));
        blockchain.add(new Block("Third block", blockchain.get(blockchain.size() - 1).hash));

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }

    public static boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            final Block currentBlock = blockchain.get(i);
            final Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(previousBlock.hash)) {
                System.out.println("Current hashes not equal");
                return false;
            }

            if (!currentBlock.previousHash.equals(previousBlock.previousHash)) {
                System.out.println("Previous hashes not equal");
                return false;
            }
        }
        
        return true;
    }
}