package it.csi.nembo.nemboconf.dto;

import java.io.Serializable;

public class AttoreVO implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  private String            codice;
  private String            descrizione;

  public AttoreVO()
  {

  }

  public AttoreVO(String cod, String descr)
  {
    this.codice = cod;
    this.descrizione = descr;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
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
