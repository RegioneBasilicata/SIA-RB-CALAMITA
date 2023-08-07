package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class Ricevuta implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;
  private String            oggettoMail;
  private String            corpoMail;

  public String getOggettoMail()
  {
    return oggettoMail;
  }

  public void setOggettoMail(String oggettoMail)
  {
    this.oggettoMail = oggettoMail;
  }

  public String getCorpoMail()
  {
    return corpoMail;
  }

  public void setCorpoMail(String corpoMail)
  {
    this.corpoMail = corpoMail;
  }
}
