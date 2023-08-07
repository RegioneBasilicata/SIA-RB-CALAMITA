package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class GruppoInfoDTO implements ILoggable
{

  private static final long      serialVersionUID = 4601339650061084885L;

  private long                   idBando;
  private long                   idQuadroOggetto;
  private long                   idBandoOggetto;
  private long                   idGruppoInfo;
  private String                 descrizione;
  private String                 flagInfoObbligatoria;
  private String                 flagInfoCatalogo;
  private List<DettaglioInfoDTO> elencoDettagliInfo;

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

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public List<DettaglioInfoDTO> getElencoDettagliInfo()
  {
    return elencoDettagliInfo;
  }

  public void setElencoDettagliInfo(List<DettaglioInfoDTO> elencoDettagliInfo)
  {
    this.elencoDettagliInfo = elencoDettagliInfo;
  }

  public String getFlagInfoObbligatoria()
  {
    return flagInfoObbligatoria;
  }

  public void setFlagInfoObbligatoria(String flagInfoObbligatoria)
  {
    this.flagInfoObbligatoria = flagInfoObbligatoria;
  }

  public String getFlagInfoCatalogo()
  {
    return flagInfoCatalogo;
  }

  public void setFlagInfoCatalogo(String flagInfoCatalogo)
  {
    this.flagInfoCatalogo = flagInfoCatalogo;
  }

}
