package it.csi.nembo.nemboconf.dto.catalogomisura;

import java.io.Serializable;
import java.math.BigDecimal;

public class InfoMisurazioneIntervento implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  private String            descMisurazione;
  private BigDecimal        valore;
  private String            codiceUnitaMisura;

  public String getDescMisurazione()
  {
    return descMisurazione;
  }

  public void setDescMisurazione(String descMisurazione)
  {
    this.descMisurazione = descMisurazione;
  }

  public BigDecimal getValore()
  {
    return valore;
  }

  public void setValore(BigDecimal valore)
  {
    this.valore = valore;
  }

  public String getCodiceUnitaMisura()
  {
    return codiceUnitaMisura;
  }

  public void setCodiceUnitaMisura(String codiceUnitaMisura)
  {
    this.codiceUnitaMisura = codiceUnitaMisura;
  }

}
