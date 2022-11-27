package searchengine.message;

public final class MyMessage {
    private Boolean result;
    private String error;

    public MyMessage(Boolean status) {
        this.result = status;
    }

    public MyMessage(Boolean status, String message) {
        this.result = status;
        this.error = message;
    }

    @Override
    public String toString() {
        return "result: " + result + "\n" +
                "error: " + error;
    }
}
