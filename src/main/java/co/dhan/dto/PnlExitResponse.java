package co.dhan.dto;

import co.dhan.constant.PnLExitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Response object for P&L exit configuration. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PnlExitResponse {

  /** P&L based exit configured successfully - ACTIVE or INACTIVE */
  private PnLExitStatus pnlExitStatus;

  /** Response message from API - Status of Conditional Trigger */
  private String message;
}
