import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    private static int sequence;
    private String transactionId;
    private final PublicKey sender;
    private final PublicKey recipient;
    private final float value;
    private byte[] signature;

    private final ArrayList<TransactionInput> inputs;
    private final ArrayList<TransactionOutput> outputs = new ArrayList<>();

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public float getValue() {
        return value;
    }

    public ArrayList<TransactionInput> getInputs() {
        return inputs;
    }

    public ArrayList<TransactionOutput> getOutputs() {
        return outputs;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value + sequence);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {

        if (!verifySignature()) {
            System.out.println("# Transaction Signature is not valid (verification failed)");
        }

        for (TransactionInput i : this.inputs) {
            i.setUTXO(Main.UTXOs.get(i.getTransactionOutputId()));
        }

        if (getInputsValue() < Main.minimumTransaction) {
            System.out.println("# Transaction Inputs too small: " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - this.value;
        this.transactionId = calculateHash();
        this.outputs.add(new TransactionOutput(this.recipient, this.value, this.transactionId));
        this.outputs.add(new TransactionOutput(this.sender, leftOver, this.transactionId));

        for (TransactionOutput o : this.outputs) {
            Main.UTXOs.put(o.getId(), o);
        }

        for (TransactionInput i : this.inputs) {
            if (i.getUTXO() == null) continue;
            Main.UTXOs.remove(i.getUTXO().getId());
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : this.inputs) {
            if (i.getUTXO() == null) continue;
            total += i.getUTXO().getValue();
        }

        return total;
    }

    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : this.outputs) {
            total += o.getValue();
        }

        return total;
    }
}
