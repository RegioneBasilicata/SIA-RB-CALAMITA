package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class ControlloDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idOggetto;
  private long              idQuadro;
  private long              idQuadroOggettoControllo;
  private long              idBandoOggetto;
  private String            codOggetto;
  private String            descrOggetto;
  private String            codQuadro;
  private String            descrQuadro;
  private String            flagAttivo;
  private long              idControllo;
  private String            codice;
  private String            descrizione;
  private String            descrEstesa;
  private String            descrParametro;
  private String            flagObbligatorio;
  private String            gravita;
  private long              idControlloInserito;
  private String            gravitaInserita;
  private String            flagGiustificabileBando;
  private String            flagGiustificabileQuadro;
  private int               numeroSoluzioniAnomalie;

  public String getFlagGiustificabileBando()
  {
    return flagGiustificabileBando;
  }

  public void setFlagGiustificabileBando(String flagGiustificabileBando)
  {
    this.flagGiustificabileBando = flagGiustificabileBando;
  }

  public String getFlagGiustificabileQuadro()
  {
    return flagGiustificabileQuadro;
  }

  public void setFlagGiustificabileQuadro(String flagGiustificabileQuadro)
  {
    this.flagGiustificabileQuadro = flagGiustificabileQuadro;
  }

  public long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(long idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public String getCodOggetto()
  {
    return codOggetto;
  }

  public void setCodOggetto(String codOggetto)
  {
    this.codOggetto = codOggetto;
  }

  public String getDescrOggetto()
  {
    return descrOggetto;
  }

  public void setDescrOggetto(String descrOggetto)
  {
    this.descrOggetto = descrOggetto;
  }

  public String getFlagAttivo()
  {
    return flagAttivo;
  }

  public void setFlagAttivo(String flagAttivo)
  {
    this.flagAttivo = flagAttivo;
  }

  public long getIdControllo()
  {
    return idControllo;
  }

  public String getIdUnivoco()
  {
    return idControllo + "" + idBandoOggetto;
  }

  public void setIdControllo(long idControllo)
  {
    this.idControllo = idControllo;
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

  public void setDescrEstesa(String descrEstesa)
  {
    this.descrEstesa = descrEstesa;
  }

  public String getDescrParametro()
  {
    return descrParametro;
  }

  public void setDescrParametro(String descrParametro)
  {
    this.descrParametro = descrParametro;
  }

  public String getFlagObbligatorio()
  {
    return flagObbligatorio;
  }

  public void setFlagObbligatorio(String flagObbligatorio)
  {
    this.flagObbligatorio = flagObbligatorio;
  }

  public String getGravita()
  {
    return gravita;
  }

  public void setGravita(String gravita)
  {
    this.gravita = gravita;
  }

  public long getIdControlloInserito()
  {
    return idControlloInserito;
  }

  public void setIdControlloInserito(long idControlloInserito)
  {
    this.idControlloInserito = idControlloInserito;
  }

  public String getGravitaInserita()
  {
    return gravitaInserita;
  }

  public void setGravitaInserita(String gravitaInserita)
  {
    this.gravitaInserita = gravitaInserita;
  }

  public long getIdQuadro()
  {
    return idQuadro;
  }

  public void setIdQuadro(long idQuadro)
  {
    this.idQuadro = idQuadro;
  }

  public String getCodQuadro()
  {
    return codQuadro;
  }

  public void setCodQuadro(String codQuadro)
  {
    this.codQuadro = codQuadro;
  }

  public String getDescrQuadro()
  {
    return descrQuadro;
  }

  public void setDescrQuadro(String descrQuadro)
  {
    this.descrQuadro = descrQuadro;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public long getIdQuadroOggettoControllo()
  {
    return idQuadroOggettoControllo;
  }

  public void setIdQuadroOggettoControllo(long idQuadroOggettoControllo)
  {
    this.idQuadroOggettoControllo = idQuadroOggettoControllo;
  }

  public int getNumeroSoluzioniAnomalie()
  {
    return numeroSoluzioniAnomalie;
  }

  public void setNumeroSoluzioniAnomalie(int numeroSoluzioniAnomalie)
  {
    this.numeroSoluzioniAnomalie = numeroSoluzioniAnomalie;
  }

}
