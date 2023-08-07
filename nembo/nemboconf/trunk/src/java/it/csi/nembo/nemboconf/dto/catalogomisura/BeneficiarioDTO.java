package it.csi.nembo.nemboconf.dto.catalogomisura;

import java.io.Serializable;

public class BeneficiarioDTO implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  private long              idLivello;
  private long              idFgTipologia;
  private String            descTipologiaAzienda;
  private String            descFormaGiuridica;

  public BeneficiarioDTO()
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

  public String getDescTipologiaAzienda()
  {
    return descTipologiaAzienda;
  }

  public void setDescTipologiaAzienda(String descTipologiaAzienda)
  {
    this.descTipologiaAzienda = descTipologiaAzienda;
  }

  public String getDescFormaGiuridica()
  {
    return descFormaGiuridica;
  }

  public String getDescrizione()
  {
    return descFormaGiuridica;
  }

  public void setDescFormaGiuridica(String descFormaGiuridica)
  {
    this.descFormaGiuridica = descFormaGiuridica;
  }

  public long getIdFgTipologia()
  {
    return idFgTipologia;
  }

  public void setIdFgTipologia(long idFgTipologia)
  {
    this.idFgTipologia = idFgTipologia;
  }

}
