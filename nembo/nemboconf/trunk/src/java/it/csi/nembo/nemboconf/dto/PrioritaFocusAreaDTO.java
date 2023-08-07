package it.csi.nembo.nemboconf.dto;

import java.util.ArrayList;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class PrioritaFocusAreaDTO implements ILoggable
{
  private static final long                serialVersionUID = 9104726695231817976L;

  private String                           codice;
  private String                           descrizione;

  private ArrayList<DecodificaDTO<String>> elencoFocusArea;

  public PrioritaFocusAreaDTO()
  {

  }

  public PrioritaFocusAreaDTO(String codice, String descrizione)
  {
    this.descrizione = descrizione;
    this.codice = codice;
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

  public ArrayList<DecodificaDTO<String>> getElencoFocusArea()
  {
    if (elencoFocusArea == null)
      elencoFocusArea = new ArrayList<DecodificaDTO<String>>();
    return elencoFocusArea;
  }

  public void setElencoFocusArea(
      ArrayList<DecodificaDTO<String>> elencoFocusArea)
  {
    this.elencoFocusArea = elencoFocusArea;
  }

}
