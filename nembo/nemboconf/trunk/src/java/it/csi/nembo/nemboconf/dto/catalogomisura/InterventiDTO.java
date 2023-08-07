package it.csi.nembo.nemboconf.dto.catalogomisura;

import java.io.Serializable;
import java.util.List;

public class InterventiDTO implements Serializable
{
  private static final long               serialVersionUID = 9104726695231817976L;

  private long                            idLivello;
  private long                            idDescrizioneIntervento;
  private long                            idTipoAggregazione;
  private String                          descTipoIntervento;
  private String                          descIntervento;
  private String                          descCategoria;
  private List<InfoMisurazioneIntervento> infoMisurazioni;

  public InterventiDTO()
  {

  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getDescTipoIntervento()
  {
    return descTipoIntervento;
  }

  public void setDescTipoIntervento(String descTipoIntervento)
  {
    this.descTipoIntervento = descTipoIntervento;
  }

  public String getDescIntervento()
  {
    return descIntervento;
  }

  public void setDescIntervento(String descIntervento)
  {
    this.descIntervento = descIntervento;
  }

  public long getIdDescrizioneIntervento()
  {
    return idDescrizioneIntervento;
  }

  public void setIdDescrizioneIntervento(long idDescrizioneIntervento)
  {
    this.idDescrizioneIntervento = idDescrizioneIntervento;
  }

  public long getIdTipoAggregazione()
  {
    return idTipoAggregazione;
  }

  public void setIdTipoAggregazione(long idTipoAggregazione)
  {
    this.idTipoAggregazione = idTipoAggregazione;
  }

  public String getDescCategoria()
  {
    return descCategoria;
  }

  public void setDescCategoria(String descCategoria)
  {
    this.descCategoria = descCategoria;
  }

  public List<InfoMisurazioneIntervento> getInfoMisurazioni()
  {
    return infoMisurazioni;
  }

  public void setInfoMisurazioni(
      List<InfoMisurazioneIntervento> infoMisurazioni)
  {
    this.infoMisurazioni = infoMisurazioni;
  }

}
