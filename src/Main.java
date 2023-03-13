public class Main {
    public static void main(String[] args) {
        Block genesisBlock = new Block("I'm the first block", "0");
        System.out.println("Hash for block 1: " + genesisBlock.hash);

        Block secondBlock = new Block("Well hello, I'm the second block", genesisBlock.previousHash);
        System.out.println("Hash for block 2: " + secondBlock.hash);

        Block thirdBlock = new Block("My, greetings, I'm the third block", secondBlock.previousHash);
        System.out.println("Hash for block 3: " + thirdBlock.hash);
    }
}