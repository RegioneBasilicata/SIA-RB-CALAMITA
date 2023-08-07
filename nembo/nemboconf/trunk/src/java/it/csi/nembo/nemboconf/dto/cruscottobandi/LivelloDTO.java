package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class LivelloDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 4601339650061084885L;

  private long              idLivello;
  private String            codice;
  private String            codiceLivello;
  private String            codiceMisura;
  private String            codiceSottoMisura;
  private String            descrizione;
  private String            descrEstesa;

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getDescrEstesa()
  {
    return descrEstesa;
  }

  public String getCodiceDescrizione()
  {
    return codice + " - " + descrizione;
  }

  public void setDescrEstesa(String descrEstesa)
  {
    this.descrEstesa = descrEstesa;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public String getCodiceMisura()
  {
    return codiceMisura;
  }

  public void setCodiceMisura(String codiceMisura)
  {
    this.codiceMisura = codiceMisura;
  }

  public String getCodiceSottoMisura()
  {
    return codiceSottoMisura;
  }

  public void setCodiceSottoMisura(String codiceSottoMisura)
  {
    this.codiceSottoMisura = codiceSottoMisura;
  }

}
