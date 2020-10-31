package dev.a360.springAuth.exceptions;
 import com.fasterxml.jackson.annotation.JsonInclude;
 import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorModel {
    private HttpStatus status;
    private String timestamp;
    private String message;
    private String debugMessage;

    private ApiErrorModel() {
        LocalDateTime dateTime = LocalDateTime.now();
        timestamp = dateTime.toString();
    }

    public ApiErrorModel(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiErrorModel(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }
}

