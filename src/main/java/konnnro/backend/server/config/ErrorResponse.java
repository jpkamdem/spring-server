package konnnro.backend.server.config;

import java.time.LocalDateTime;

import lombok.Getter;

public class ErrorResponse {
  @Getter
  private Integer status;

  @Getter
  private String message;

  @Getter
  private LocalDateTime timestamp;

  public ErrorResponse(Integer status, LocalDateTime timestamp, String message) {
    this.status = status;
    this.timestamp = timestamp;
    this.message = message;
  }
}