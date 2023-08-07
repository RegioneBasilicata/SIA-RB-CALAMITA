package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class TipoOperazioneDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idLivello;
  private String            codice;
  private String            descrizione;

  public TipoOperazioneDTO()
  {
    super();
  }

  public TipoOperazioneDTO(long idLivello, String descrizione)
  {
    super();
    this.idLivello = idLivello;
    this.descrizione = descrizione;
    this.codice = "";
  }

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

  public String getDescrizioneEstesa()
  {
    return (codice + " " + descrizione).trim();
  }
}
