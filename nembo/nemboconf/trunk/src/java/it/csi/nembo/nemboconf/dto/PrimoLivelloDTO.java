package it.csi.nembo.nemboconf.dto;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class PrimoLivelloDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = 9104726695231817976L;
  private Long              misura;
  private Long              idLivello;
  private String            descrizione;

  private SecondoLivelloDTO secondoLivello[];

  public PrimoLivelloDTO()
  {

  }

  public PrimoLivelloDTO(Long misura, String descrizione)
  {
    this.misura = misura;
    this.descrizione = descrizione;
  }

  public Long getMisura()
  {
    return misura;
  }

  public Long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(Long idLivello)
  {
    this.idLivello = idLivello;
  }

  public void setMisura(Long misura)
  {
    this.misura = misura;
  }

  public SecondoLivelloDTO[] getSecondoLivello()
  {
    return secondoLivello;
  }

  public void setSecondoLivello(SecondoLivelloDTO[] secondoLivello)
  {
    this.secondoLivello = secondoLivello;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

}
