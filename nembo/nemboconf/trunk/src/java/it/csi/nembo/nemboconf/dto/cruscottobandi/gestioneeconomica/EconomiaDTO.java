package it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class EconomiaDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idEconomia;
  private long              idRisorseLivelloBando;
  private String            descrizione;
  private BigDecimal        importoEconomia;

  public long getIdEconomia()
  {
    return idEconomia;
  }

  public void setIdEconomia(long idEconomia)
  {
    this.idEconomia = idEconomia;
  }

  public long getIdRisorseLivelloBando()
  {
    return idRisorseLivelloBando;
  }

  public void setIdRisorseLivelloBando(long idRisorseLivelloBando)
  {
    this.idRisorseLivelloBando = idRisorseLivelloBando;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public BigDecimal getImportoEconomia()
  {
    return importoEconomia;
  }

  public String getImportoEconomiaStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(importoEconomia, 2, true);
  }

  public String getImportoEconomiaFormatted()
  {
    BigDecimal ret = (importoEconomia == null) ? new BigDecimal(0)
        : importoEconomia;
    DecimalFormat formatter = new DecimalFormat("###,###.00");
    return formatter.format(ret.setScale(2, RoundingMode.HALF_UP));
  }

  public void setImportoEconomia(BigDecimal importoEconomia)
  {
    this.importoEconomia = importoEconomia;
  }

}
