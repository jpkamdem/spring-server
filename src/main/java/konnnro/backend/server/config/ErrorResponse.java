package konnnro.backend.server.config;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;

public class ErrorResponse {
  @Getter(AccessLevel.PUBLIC)
  private Integer status;

  @Getter(AccessLevel.PUBLIC)
  private String message;

  @Getter(AccessLevel.PUBLIC)
  private LocalDateTime timestamp;

  public ErrorResponse(Integer status, LocalDateTime timestamp, String message) {
    this.status = status;
    this.timestamp = timestamp;
    this.message = message;
  }
}