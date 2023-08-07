package it.csi.nembo.nemboconf.dto;

import java.io.Serializable;

public class CriterioDiSelezioneDTO implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = -3837427681517146769L;
  private Long              idCriterioDiSelezione;
  private Long              idBandoLivelloCriterio;
  private Long              idLivelloCriterio;
  private String            codice;
  private String            codOperazione;
  private String            criterioDiSelezione;
  private String            criterioDiSelEsteso;
  private String            descrPrincipiSelezione;
  private String            specifiche;
  private String            tipoControllo;
  private Long              punteggioMin;
  private Long              punteggioMax;
  private String            flagElaborazione;

  public Long getIdLivelloCriterio()
  {
    return idLivelloCriterio;
  }

  public void setIdLivelloCriterio(Long idLivelloCriterio)
  {
    this.idLivelloCriterio = idLivelloCriterio;
  }

  public Long getIdBandoLivelloCriterio()
  {
    return idBandoLivelloCriterio;
  }

  public void setIdBandoLivelloCriterio(Long idBandoLivelloCriterio)
  {
    this.idBandoLivelloCriterio = idBandoLivelloCriterio;
  }

  public String getCriterioDiSelEsteso()
  {
    return criterioDiSelEsteso;
  }

  public void setCriterioDiSelEsteso(String criterioDiSelEsteso)
  {
    this.criterioDiSelEsteso = criterioDiSelEsteso;
  }

  public String getCodOperazione()
  {
    return codOperazione;
  }

  public void setCodOperazione(String codOperazione)
  {
    this.codOperazione = codOperazione;
  }

  public String getDescrPrincipiSelezione()
  {
    return descrPrincipiSelezione;
  }

  public void setDescrPrincipiSelezione(String descrPrincipiSelezione)
  {
    this.descrPrincipiSelezione = descrPrincipiSelezione;
  }

  public String getTipoControllo()
  {
    return tipoControllo;
  }

  public void setTipoControllo(String tipoControllo)
  {
    this.tipoControllo = tipoControllo;
  }

  public Long getIdCriterioDiSelezione()
  {
    return idCriterioDiSelezione;
  }

  public void setIdCriterioDiSelezione(Long idCriterioDiSelezione)
  {
    this.idCriterioDiSelezione = idCriterioDiSelezione;
  }

  public String getCriterioDiSelezione()
  {
    return criterioDiSelezione;
  }

  public void setCriterioDiSelezione(String criterioDiSelezione)
  {
    this.criterioDiSelezione = criterioDiSelezione;
  }

  public String getSpecifiche()
  {
    return specifiche;
  }

  public void setSpecifiche(String specifiche)
  {
    this.specifiche = specifiche;
  }

  public Long getPunteggioMin()
  {
    return punteggioMin;
  }

  public void setPunteggioMin(Long punteggioMin)
  {
    this.punteggioMin = punteggioMin;
  }

  public Long getPunteggioMax()
  {
    return punteggioMax;
  }

  public void setPunteggioMax(Long punteggioMax)
  {
    this.punteggioMax = punteggioMax;
  }

  public String getFlagElaborazione()
  {
    if (flagElaborazione != null)
      if (flagElaborazione.compareTo("A") == 0)
        return "Automatico";
    if (flagElaborazione.compareTo("M") == 0)
      return "Manuale";

    return flagElaborazione;
  }

  public void setFlagElaborazione(String flagElaborazione)
  {
    this.flagElaborazione = flagElaborazione;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getIdFlagElaborazione()
  {

    return flagElaborazione;
  }

}
