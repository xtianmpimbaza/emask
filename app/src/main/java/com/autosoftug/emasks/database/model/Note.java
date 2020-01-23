package com.autosoftug.emasks.database.model;

public class Note {
    public static final String TABLE_NAME = "transactions";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_METHOD = "method";
    public static final String COLUMN_CURRENCY = "currency";
    public static final String COLUMN_RECEIPIENT = "receipient";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TIMESTAMP = "time";

    private int id;
    private String note;
    private String txid;
    private String amount;
    private String timestamp;
    private String receipient;
    private String method;
    private String currency;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " VARCHAR(100) PRIMARY KEY,"
                    + COLUMN_METHOD + " VARCHAR(100),"
                    + COLUMN_CURRENCY + " VARCHAR(100),"
                    + COLUMN_RECEIPIENT + " VARCHAR(100),"
                    + COLUMN_AMOUNT + " VARCHAR(100),"
                    + COLUMN_TIMESTAMP + " TEXT"
                    + ")";

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceipient() {
        return receipient;
    }

    public void setReceipient(String receipient) {
        this.receipient = receipient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


}
