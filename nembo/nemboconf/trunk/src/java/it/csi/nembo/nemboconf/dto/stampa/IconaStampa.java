package it.csi.nembo.nemboconf.dto.stampa;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class IconaStampa implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -5258657295575208912L;
  protected String          codiceCdu;
  protected String          descTipoDocumento;
  protected String          nomeIcona;

  public String getCodiceCdu()
  {
    return codiceCdu;
  }

  public void setCodiceCdu(String codiceCdu)
  {
    this.codiceCdu = codiceCdu;
  }

  public String getDescTipoDocumento()
  {
    return descTipoDocumento;
  }

  public void setDescTipoDocumento(String descTipoDocumento)
  {
    this.descTipoDocumento = descTipoDocumento;
  }

  public String getNomeIcona()
  {
    return nomeIcona;
  }

  public void setNomeIcona(String nomeIcona)
  {
    this.nomeIcona = nomeIcona;
  }
}
