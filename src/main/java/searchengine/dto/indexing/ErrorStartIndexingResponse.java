package searchengine.dto.indexing;

public class ErrorStartIndexingResponse extends StartIndexingResponse {
    private String error;

    public void setError(String error) {
        this.error = error;
    }
}
