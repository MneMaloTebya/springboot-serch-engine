package searchengine.dto.indexing;

import lombok.Data;

@Data
public class ErrorIndexingResponse extends IndexingResponse {
    private String error;

    public ErrorIndexingResponse() {

    }

    public ErrorIndexingResponse(String error) {
        super(false);
        this.error = error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
