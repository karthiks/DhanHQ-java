package co.dhan.dto;

import co.dhan.constant.KillSwitchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Data Transfer Object representing the Kill Switch status response from Dhan API. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KillSwitchStatusResponse {
  private String dhanClientId;
  private KillSwitchStatus status;
}
