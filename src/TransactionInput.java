public class TransactionInput {
    private final String transactionOutputId;
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return this.transactionOutputId;
    }

    public TransactionOutput getUTXO() {
        return this.UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }
}
