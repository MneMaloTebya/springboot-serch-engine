package searchengine.dto.indexing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartIndexingResponse {
    private boolean result;

    public void setResult(boolean result) {
        this.result = result;
    }
}
