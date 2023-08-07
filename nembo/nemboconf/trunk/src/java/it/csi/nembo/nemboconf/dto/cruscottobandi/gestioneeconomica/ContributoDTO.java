package it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica;

import java.math.BigDecimal;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class ContributoDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idBando;
  private long              idLivello;
  private String            codLivello;
  private String            descrLivello;
  private BigDecimal        percMinima;
  private BigDecimal        percMassima;
  private BigDecimal        massimaleSpesa;

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public BigDecimal getPercMinima()
  {
    return percMinima;
  }

  public void setPercMinima(BigDecimal percMinima)
  {
    this.percMinima = percMinima;
  }

  public BigDecimal getPercMassima()
  {
    return percMassima;
  }

  public void setPercMassima(BigDecimal percMassima)
  {
    this.percMassima = percMassima;
  }

  public BigDecimal getMassimaleSpesa()
  {
    return massimaleSpesa;
  }

  public void setMassimaleSpesa(BigDecimal massimaleSpesa)
  {
    this.massimaleSpesa = massimaleSpesa;
  }

  public String getDescrLivello()
  {
    return descrLivello;
  }

  public void setDescrLivello(String descrLivello)
  {
    this.descrLivello = descrLivello;
  }

  /* UTILITY GETTER */

  public String getCodLivello()
  {
    return codLivello;
  }

  public void setCodLivello(String codLivello)
  {
    this.codLivello = codLivello;
  }

  public String getPercMinimaStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(percMinima, 2, false);
  }

  public String getPercMassimaStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(percMassima, 2, false);
  }

  public String getMassimaleSpesaStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(massimaleSpesa, 2, false);
  }

}
