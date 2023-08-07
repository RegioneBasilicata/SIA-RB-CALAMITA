package it.csi.nembo.nemboconf.dto;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class SecondoLivelloDTO extends PrimoLivelloDTO implements ILoggable
{
  /**
   * 
   */
  private static final long serialVersionUID = -9030727656018205714L;
  private String            sottomisura;
  private TerzoLivelloDTO   terzoLivello[];
  private String            idUnivoco;

  public SecondoLivelloDTO()
  {
  }

  public SecondoLivelloDTO(Long misura, String descrizione)
  {
    super(misura, descrizione);
  }

  public SecondoLivelloDTO(Long misura, String sottomisura, String descrizione)
  {
    super(misura, descrizione);
    this.sottomisura = sottomisura;
  }

  public String getSottomisura()
  {
    return sottomisura;
  }

  public void setSottomisura(String sottomisura)
  {
    this.sottomisura = sottomisura;
  }

  public TerzoLivelloDTO[] getTerzoLivello()
  {
    return terzoLivello;
  }

  public void setTerzoLivello(TerzoLivelloDTO[] terzoLivello)
  {
    this.terzoLivello = terzoLivello;
  }

  public String getIdUnivoco()
  {
    return idUnivoco;
  }

  public void setIdUnivoco(String idUnivoco)
  {
    this.idUnivoco = idUnivoco;
  }

}
