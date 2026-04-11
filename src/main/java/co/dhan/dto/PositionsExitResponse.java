package co.dhan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Response model for exiting all positions. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionsExitResponse {
  private String status;
  private String message;
}
