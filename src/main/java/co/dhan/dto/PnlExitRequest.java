package co.dhan.dto;

import co.dhan.constant.ProductType;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for configuring P&L-based exit rules. Warning: In case of profitValue set below
 * the current Profit in P&L, then the P&L based exit will be triggered immediately. This applies to
 * lossValue set above the current Loss in P&L as well.
 */
@Data
@Builder
public class PnlExitRequest {
  /** User-defined target profit amount for the P&L exit */
  private BigDecimal profitValue;

  /** User-defined target loss amount for the P&L exit */
  private BigDecimal lossValue;

  /** Indicates if the kill switch is enabled for this P&L exit */
  private boolean enableKillSwitch;

  /** Product types applicable for the P&L exit are only one of [INTRADAY, DELIVERY] */
  private ProductType productType;
}
