package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class OggettiIstanzeDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idBando;
  private long              idBandoMaster;
  private long              idOggetto;
  private long              idBandoOggetto;
  private long              idBandoOggettoMaster;
  private long              idLegameGruppoOggetto;
  private long              countQuadri;
  private String            codOggetto;
  private String            descrOggetto;
  private String            descrGruppoOggetto;
  private String            flagIstanza;
  private String            flagAttivo;
  private String            flagMasterScaduto;
  private Date              dataInizio;
  private Date              dataFine;
  private Date              dataRitardo;

  private Date              dataInizioPrec;
  private Date              dataFinePrec;
  private Date              dataRitardoPrec;
  // info per duplica bandi
  private boolean           hasImpegni;
  private boolean           hasDichiarazioni;
  private boolean           hasAllegati;
  private boolean           hasTestiVerbali;
  private String            importImpegni;
  private String            importDichiarazioni;
  private String            importAllegati;
  private String            importTestiVerbali;
  private boolean           daImportare      = false;
  private boolean           hasQuadroImpegni;
  private boolean           hasQuadroDichiarazioni;
  private boolean           hasQuadroAllegati;

  private String            oggettoRicevutaDefault;
  private String            corpoRicevutaDefault;

  public String getOggettoRicevutaDefault()
  {
    return oggettoRicevutaDefault;
  }

  public void setOggettoRicevutaDefault(String oggettoRicevutaDefault)
  {
    this.oggettoRicevutaDefault = oggettoRicevutaDefault;
  }

  public String getCorpoRicevutaDefault()
  {
    return corpoRicevutaDefault;
  }

  public void setCorpoRicevutaDefault(String corpoRicevutaDefault)
  {
    this.corpoRicevutaDefault = corpoRicevutaDefault;
  }

  public boolean isHasQuadroImpegni()
  {
    return hasQuadroImpegni;
  }

  public void setHasQuadroImpegni(boolean hasQuadroImpegni)
  {
    this.hasQuadroImpegni = hasQuadroImpegni;
  }

  public boolean isHasQuadroDichiarazioni()
  {
    return hasQuadroDichiarazioni;
  }

  public void setHasQuadroDichiarazioni(boolean hasQuadroDichiarazioni)
  {
    this.hasQuadroDichiarazioni = hasQuadroDichiarazioni;
  }

  public boolean isHasQuadroAllegati()
  {
    return hasQuadroAllegati;
  }

  public void setHasQuadroAllegati(boolean hasQuadroAllegati)
  {
    this.hasQuadroAllegati = hasQuadroAllegati;
  }

  public Date getDataInizioPrec()
  {
    return dataInizioPrec;
  }

  public void setDataInizioPrec(Date dataInizioPrec)
  {
    this.dataInizioPrec = dataInizioPrec;
  }

  public Date getDataFinePrec()
  {
    return dataFinePrec;
  }

  public void setDataFinePrec(Date dataFinePrec)
  {
    this.dataFinePrec = dataFinePrec;
  }

  public Date getDataRitardoPrec()
  {
    return dataRitardoPrec;
  }

  public void setDataRitardoPrec(Date dataRitardoPrec)
  {
    this.dataRitardoPrec = dataRitardoPrec;
  }

  // usato per gestire il rowspan nella sezione "quadri"
  private List<QuadroDTO> quadri;

  // usato per vedere se gli oggetti erano già selezionati o sono stati
  // selezionati/deselezionati dopo
  private String          statoPrecedente;
  private String          statoAttuale;

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdBandoMaster()
  {
    return idBandoMaster;
  }

  public void setIdBandoMaster(long idBandoMaster)
  {
    this.idBandoMaster = idBandoMaster;
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

  public String getFlagIstanza()
  {
    return flagIstanza;
  }

  public String getFlagIstanzaDescr()
  {
    return (flagIstanza != null && "S".equals(flagIstanza)) ? "SI" : "NO";
  }

  public void setFlagIstanza(String flagIstanza)
  {
    this.flagIstanza = flagIstanza;
  }

  public String getFlagAttivo()
  {
    return flagAttivo;
  }

  public void setFlagAttivo(String flagAttivo)
  {
    this.flagAttivo = flagAttivo;
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

  public Date getDataRitardo()
  {
    return dataRitardo;
  }

  public void setDataRitardo(Date dataRitardo)
  {
    this.dataRitardo = dataRitardo;
  }

  public String getStatoPrecedente()
  {
    return statoPrecedente;
  }

  public void setStatoPrecedente(String statoPrecedente)
  {
    this.statoPrecedente = statoPrecedente;
  }

  public String getStatoAttuale()
  {
    return statoAttuale;
  }

  public void setStatoAttuale(String statoAttuale)
  {
    this.statoAttuale = statoAttuale;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public long getIdLegameGruppoOggetto()
  {
    return idLegameGruppoOggetto;
  }

  public void setIdLegameGruppoOggetto(long idLegameGruppoOggetto)
  {
    this.idLegameGruppoOggetto = idLegameGruppoOggetto;
  }

  public String getDataInizioStr()
  {
    return NemboUtils.DATE.formatDateTime(dataInizio);
  }

  public String getDataFineStr()
  {
    return NemboUtils.DATE.formatDateTime(dataFine);
  }

  public String getDataRitardoStr()
  {
    return NemboUtils.DATE.formatDateTime(dataRitardo);
  }

  public String getDataInizioPrecStr()
  {
    return NemboUtils.DATE.formatDateTime(dataInizioPrec);
  }

  public String getDataFinePrecStr()
  {
    return NemboUtils.DATE.formatDateTime(dataFinePrec);
  }

  public String getDataRitardoPrecStr()
  {
    return NemboUtils.DATE.formatDateTime(dataRitardoPrec);
  }

  public List<QuadroDTO> getQuadri()
  {
    return quadri;
  }

  public void setQuadri(List<QuadroDTO> quadri)
  {
    this.quadri = quadri;
  }

  public long getCountQuadri()
  {
    return countQuadri;
  }

  public void setCountQuadri(long countQuadri)
  {
    this.countQuadri = countQuadri;
  }

  public String getFlagMasterScaduto()
  {
    return flagMasterScaduto;
  }

  public void setFlagMasterScaduto(String flagMasterScaduto)
  {
    this.flagMasterScaduto = flagMasterScaduto;
  }

  public long getIdBandoOggettoMaster()
  {
    return idBandoOggettoMaster;
  }

  public void setIdBandoOggettoMaster(long idBandoOggettoMaster)
  {
    this.idBandoOggettoMaster = idBandoOggettoMaster;
  }

  public String getDescrGruppoOggetto()
  {
    return descrGruppoOggetto;
  }

  public void setDescrGruppoOggetto(String descrGruppoOggetto)
  {
    this.descrGruppoOggetto = descrGruppoOggetto;
  }

  public String getImportImpegni()
  {
    return importImpegni;
  }

  public void setImportImpegni(String importImpegni)
  {
    this.importImpegni = importImpegni;
  }

  public String getImportDichiarazioni()
  {
    return importDichiarazioni;
  }

  public void setImportDichiarazioni(String importDichiarazioni)
  {
    this.importDichiarazioni = importDichiarazioni;
  }

  public String getImportAllegati()
  {
    return importAllegati;
  }

  public void setImportAllegati(String importAllegati)
  {
    this.importAllegati = importAllegati;
  }

  public String getImportTestiVerbali()
  {
    return importTestiVerbali;
  }

  public void setImportTestiVerbali(String importTestiVerbali)
  {
    this.importTestiVerbali = importTestiVerbali;
  }

  public boolean isHasImpegni()
  {
    return hasImpegni;
  }

  public void setHasImpegni(boolean hasImpegni)
  {
    this.hasImpegni = hasImpegni;
  }

  public boolean isHasDichiarazioni()
  {
    return hasDichiarazioni;
  }

  public void setHasDichiarazioni(boolean hasDichiarazioni)
  {
    this.hasDichiarazioni = hasDichiarazioni;
  }

  public boolean isHasAllegati()
  {
    return hasAllegati;
  }

  public void setHasAllegati(boolean hasAllegati)
  {
    this.hasAllegati = hasAllegati;
  }

  public boolean isHasTestiVerbali()
  {
    return hasTestiVerbali;
  }

  public void setHasTestiVerbali(boolean hasTestiVerbali)
  {
    this.hasTestiVerbali = hasTestiVerbali;
  }

  public boolean isDaImportare()
  {
    return daImportare;
  }

  public void setDaImportare(boolean daImportare)
  {
    this.daImportare = daImportare;
  }

}
