package it.csi.nembo.nemboconf.dto;

import java.math.BigDecimal;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class DecodificaImportoDTO<T> extends DecodificaDTO<T>
    implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -2015295107524298131L;
  private BigDecimal        importo;

  public BigDecimal getImporto()
  {
    return importo;
  }

  public String getImportoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(importo);
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }
}
