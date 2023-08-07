package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class EsitoCallPckDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;
  private int               risultato;
  private String            messaggio;
  private Long              idBandoNew;

  public int getRisultato()
  {
    return risultato;
  }

  public void setRisultato(int risultato)
  {
    this.risultato = risultato;
  }

  public String getMessaggio()
  {
    return messaggio;
  }

  public void setMessaggio(String messaggio)
  {
    this.messaggio = messaggio;
  }

  public Long getIdBandoNew()
  {
    return idBandoNew;
  }

  public void setIdBandoNew(Long idBandoNew)
  {
    this.idBandoNew = idBandoNew;
  }

}
