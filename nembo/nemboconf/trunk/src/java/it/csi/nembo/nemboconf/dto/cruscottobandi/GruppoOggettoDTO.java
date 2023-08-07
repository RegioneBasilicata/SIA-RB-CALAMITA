package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class GruppoOggettoDTO implements ILoggable
{

  private static final long       serialVersionUID = 4601339650061084885L;

  private long                    idBando;
  private long                    idBandoMaster;
  private long                    idGruppoOggetto;
  private long                    idOggetto;
  private long                    countQuadri;
  private String                  codGruppo;
  private String                  descrGruppo;
  private String                  descrOggetto;
  private List<OggettiIstanzeDTO> oggetti;
  private boolean                 daImportare;

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

  public long getIdGruppoOggetto()
  {
    return idGruppoOggetto;
  }

  public void setIdGruppoOggetto(long idGruppoOggetto)
  {
    this.idGruppoOggetto = idGruppoOggetto;
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

  public List<OggettiIstanzeDTO> getOggetti()
  {
    return oggetti;
  }

  public void setOggetti(List<OggettiIstanzeDTO> oggetti)
  {
    this.oggetti = oggetti;
  }

  public long getCountQuadri()
  {
    return countQuadri;
  }

  public void setCountQuadri(long countQuadri)
  {
    this.countQuadri = countQuadri;
  }

  public long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(long idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public String getDescrOggetto()
  {
    return descrOggetto;
  }

  public void setDescrOggetto(String descrOggetto)
  {
    this.descrOggetto = descrOggetto;
  }

  public String getDescrizioneEstesa()
  {
    return descrGruppo + " - " + descrOggetto;
  }

  public String getIdUnivoco()
  {
    return idGruppoOggetto + "&&" + idOggetto;
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
