package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class TipoInterventoDTO implements ILoggable
{

  private static final long            serialVersionUID = 4601339650061084885L;

  private long                         idTipoAggregazione;
  private long                         countInterventi;
  private String                       tipoIntervento;
  private List<CruscottoInterventiDTO> interventi;

  public long getIdTipoAggregazione()
  {
    return idTipoAggregazione;
  }

  public void setIdTipoAggregazione(long idTipoAggregazione)
  {
    this.idTipoAggregazione = idTipoAggregazione;
  }

  public String getTipoIntervento()
  {
    return tipoIntervento;
  }

  public void setTipoIntervento(String tipoIntervento)
  {
    this.tipoIntervento = tipoIntervento;
  }

  public List<CruscottoInterventiDTO> getInterventi()
  {
    return interventi;
  }

  public void setInterventi(List<CruscottoInterventiDTO> interventi)
  {
    this.interventi = interventi;
  }

  public long getCountInterventi()
  {
    return countInterventi;
  }

  public void setCountInterventi(long countInterventi)
  {
    this.countInterventi = countInterventi;
  }

}
