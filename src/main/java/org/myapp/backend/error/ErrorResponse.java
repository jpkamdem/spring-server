package org.myapp.backend.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ErrorResponse {

  private HttpStatus status;
  private Map<String, String> error;

  public ErrorResponse(HttpStatus status, String error) {

    this.status = status;
    this.error = new HashMap<>();
    this.error.put("message", error);
  }

}