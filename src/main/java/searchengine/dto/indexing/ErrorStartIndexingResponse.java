package searchengine.dto.indexing;

import lombok.Data;

@Data
public class ErrorStartIndexingResponse extends StartIndexingResponse {
    private String error;

    public ErrorStartIndexingResponse() {

    }

    public ErrorStartIndexingResponse(String error) {
        super(false);
        this.error = error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
