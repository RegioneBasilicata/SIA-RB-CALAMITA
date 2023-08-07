package it.csi.nembo.nemboconf.dto;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class Parametro implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -4004770506470757595L;
  protected String          codice;
  protected String          valore;

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getValore()
  {
    return valore;
  }

  public void setValore(String valore)
  {
    this.valore = valore;
  }
}
