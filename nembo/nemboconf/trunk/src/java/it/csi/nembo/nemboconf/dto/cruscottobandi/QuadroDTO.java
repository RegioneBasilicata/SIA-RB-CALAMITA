package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class QuadroDTO implements ILoggable
{

  private static final long  serialVersionUID = 4601339650061084885L;

  private long               idGruppoOggetto;
  private long               idOggetto;
  private long               idQuadro;
  private long               idQuadroOggettoMaster;
  private long               idQuadroOggetto;
  private long               idQuadroOggettoNew;
  private long               idBandoOggetto;
  private long               idBandoOggettoMaster;
  private long               countControlli;
  private String             flagObbligatorio;
  private String             codGruppo;
  private String             descrGruppo;
  private String             codOggetto;
  private String             descrOggetto;
  private String             flagAttivo;
  private String             codice;
  private String             descrizione;
  private Date               dataInizio;
  private Date               dataFine;
  private boolean            quadroEsistente;
  private String             descrizioneEstesa;
  // private String nomeFile;
  private byte[]             immagine;
  private int                dimensioneImmagine;
  // usato per gestire il rowspan nella sezione 'controlli'
  private List<ControlloDTO> controlli;

  public long getIdGruppoOggetto()
  {
    return idGruppoOggetto;
  }

  public void setIdGruppoOggetto(long idGruppoOggetto)
  {
    this.idGruppoOggetto = idGruppoOggetto;
  }

  public long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(long idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public long getIdQuadro()
  {
    return idQuadro;
  }

  public void setIdQuadro(long idQuadro)
  {
    this.idQuadro = idQuadro;
  }

  public String getFlagObbligatorio()
  {
    return flagObbligatorio;
  }

  public void setFlagObbligatorio(String flagObbligatorio)
  {
    this.flagObbligatorio = flagObbligatorio;
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

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public boolean isQuadroEsistente()
  {
    return quadroEsistente;
  }

  public void setQuadroEsistente(boolean quadroEsistente)
  {
    this.quadroEsistente = quadroEsistente;
  }

  public String getCodGruppo()
  {
    return codGruppo;
  }

  public void setCodGruppo(String codGruppo)
  {
    this.codGruppo = codGruppo;
  }

  public String getDescrGruppo()
  {
    return descrGruppo;
  }

  public void setDescrGruppo(String descrGruppo)
  {
    this.descrGruppo = descrGruppo;
  }

  public String getFlagAttivo()
  {
    return flagAttivo;
  }

  public void setFlagAttivo(String flagAttivo)
  {
    this.flagAttivo = flagAttivo;
  }

  public long getIdQuadroOggettoMaster()
  {
    return idQuadroOggettoMaster;
  }

  public void setIdQuadroOggettoMaster(long idQuadroOggettoMaster)
  {
    this.idQuadroOggettoMaster = idQuadroOggettoMaster;
  }

  public long getIdQuadroOggetto()
  {
    return idQuadroOggetto;
  }

  public void setIdQuadroOggetto(long idQuadroOggetto)
  {
    this.idQuadroOggetto = idQuadroOggetto;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public long getIdQuadroOggettoNew()
  {
    return idQuadroOggettoNew;
  }

  public void setIdQuadroOggettoNew(long idQuadroOggettoNew)
  {
    this.idQuadroOggettoNew = idQuadroOggettoNew;
  }

  public List<ControlloDTO> getControlli()
  {
    return controlli;
  }

  public void setControlli(List<ControlloDTO> controlli)
  {
    this.controlli = controlli;
  }

  public long getCountControlli()
  {
    return countControlli;
  }

  public void setCountControlli(long countControlli)
  {
    this.countControlli = countControlli;
  }

  public String getDescrizioneEstesa()
  {
    return descrizioneEstesa;
  }

  public void setDescrizioneEstesa(String descrizioneEstesa)
  {
    this.descrizioneEstesa = descrizioneEstesa;
  }

  public byte[] getImmagine()
  {
    return immagine;
  }

  public void setImmagine(byte[] immagine)
  {
    this.immagine = immagine;
  }

  public int getDimensioneImmagine()
  {
    return dimensioneImmagine;
  }

  public void setDimensioneImmagine(int dimensioneImmagine)
  {
    this.dimensioneImmagine = dimensioneImmagine;
  }

  public long getIdBandoOggettoMaster()
  {
    return idBandoOggettoMaster;
  }

  public void setIdBandoOggettoMaster(long idBandoOggettoMaster)
  {
    this.idBandoOggettoMaster = idBandoOggettoMaster;
  }

}
