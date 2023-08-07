package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class DettaglioInfoDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idBando;
  private long              idQuadroOggetto;
  private long              idBandoOggetto;
  private long              idGruppoInfo;
  private long              idDettaglioInfo;
  private long              idVincolo;
  private String            descrizione;
  private String            codiceInfo;
  private String            tipoVincolo;
  private String            flagObbligatorio;
  private String            flagGestioneFile;
  private Long              extIdTipoDocumento;
  private long              idLegameInfo;
  private long              idVincoloDichiarazione;

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
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

  public long getIdGruppoInfo()
  {
    return idGruppoInfo;
  }

  public void setIdGruppoInfo(long idGruppoInfo)
  {
    this.idGruppoInfo = idGruppoInfo;
  }

  public long getIdDettaglioInfo()
  {
    return idDettaglioInfo;
  }

  public void setIdDettaglioInfo(long idDettaglioInfo)
  {
    this.idDettaglioInfo = idDettaglioInfo;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getCodiceInfo()
  {
    return codiceInfo;
  }

  public void setCodiceInfo(String codiceInfo)
  {
    this.codiceInfo = codiceInfo;
  }

  public String getFlagObbligatorio()
  {
    return flagObbligatorio;
  }

  public void setFlagObbligatorio(String flagObbligatorio)
  {
    this.flagObbligatorio = flagObbligatorio;
  }

  public String getFlagGestioneFile()
  {
    return flagGestioneFile == null ? "N" : flagGestioneFile;
  }

  public void setFlagGestioneFile(String flagGestioneFile)
  {
    this.flagGestioneFile = flagGestioneFile;
  }

  public Long getExtIdTipoDocumento()
  {
    return extIdTipoDocumento;
  }

  public void setExtIdTipoDocumento(Long extIdTipoDocumento)
  {
    this.extIdTipoDocumento = extIdTipoDocumento;
  }

  public long getIdLegameInfo()
  {
    return idLegameInfo;
  }

  public String getIdLegameInfoStr()
  {
    return String.valueOf(idLegameInfo);
  }

  public void setIdLegameInfo(long idLegameInfo)
  {
    this.idLegameInfo = idLegameInfo;
  }

  public String getTipoVincolo()
  {
    return tipoVincolo;
  }

  public void setTipoVincolo(String tipoVincolo)
  {
    this.tipoVincolo = tipoVincolo;
  }

  public long getIdVincoloDichiarazione()
  {
    return idVincoloDichiarazione;
  }

  public void setIdVincoloDichiarazione(long idVincoloDichiarazione)
  {
    this.idVincoloDichiarazione = idVincoloDichiarazione;
  }

  public long getIdVincolo()
  {
    return idVincolo;
  }

  public void setIdVincolo(long idVincolo)
  {
    this.idVincolo = idVincolo;
  }

}
