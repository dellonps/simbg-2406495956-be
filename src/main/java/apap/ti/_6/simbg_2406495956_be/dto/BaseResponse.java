package apap.ti._6.simbg_2406495956_be.dto;

import java.time.OffsetDateTime;

public class BaseResponse<T> {
    private int status;
    private String message;
    private OffsetDateTime timestamp;
    private T data;

    public BaseResponse(int status, String message, OffsetDateTime timestamp, T data) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.data = data;
    }

    public static <T> BaseResponse<T> of(int status, String message, T data) {
        return new BaseResponse<>(status, message, OffsetDateTime.now(), data);
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public T getData() { return data; }
}