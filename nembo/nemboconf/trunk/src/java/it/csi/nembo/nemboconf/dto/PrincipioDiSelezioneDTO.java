package it.csi.nembo.nemboconf.dto;

import java.io.Serializable;
import java.util.List;

public class PrincipioDiSelezioneDTO implements Serializable
{

  /**
   * 
   */
  private static final long            serialVersionUID = -5225630313116622745L;
  private Long                         idPrincipioSelezione;
  private String                       descrizione;
  private List<CriterioDiSelezioneDTO> criteri;
  private Long                         ordine;

  public Long getIdPrincipioSelezione()
  {
    return idPrincipioSelezione;
  }

  public void setIdPrincipioSelezione(Long idPrincipioSelezione)
  {
    this.idPrincipioSelezione = idPrincipioSelezione;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public List<CriterioDiSelezioneDTO> getCriteri()
  {
    return criteri;
  }

  public void setCriteri(List<CriterioDiSelezioneDTO> criteri)
  {
    this.criteri = criteri;
  }

  public String getMaxCodice()
  {
    if (criteri != null)
      return criteri.stream().filter(c -> c != null && c.getCodice() != null)
          .max((c1, c2) -> c1.getCodice().compareTo(c2.getCodice())).get()
          .getCodice();
    return null;
  }

  public Long getOrdine()
  {
    return ordine;
  }

  public void setOrdine(Long ordine)
  {
    this.ordine = ordine;
  }

}
