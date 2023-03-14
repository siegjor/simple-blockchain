import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String merkleRoot;
    private final ArrayList<Transaction> transactions = new ArrayList<>();
    private final long timeStamp;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        merkleRoot
        );
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace("\0", "0");
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
//            System.out.println("substring: " + hash.substring(0, difficulty));
            hash = calculateHash();
        }

        System.out.println("Block mined!!! : " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) return false;

        if (previousHash != "0") {
            if (!transaction.processTransaction()) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction successfully added to block");
        return true;
    }
}
