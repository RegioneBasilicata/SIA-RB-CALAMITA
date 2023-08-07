package it.csi.nembo.nemboconf.dto;

import java.util.ArrayList;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class RigaAmbitoTematicoDTO implements ILoggable
{
  private static final long                serialVersionUID = 9104726695231817976L;

  private String                           tipoOperazione;
  private String                           descrTipoOperazione;

  // id= id_focus_area , codiceS/N , descrizione = ""
  private ArrayList<DecodificaDTO<String>> elencoFocusArea;

  public RigaAmbitoTematicoDTO()
  {

  }

  public String getTipoOperazione()
  {
    return tipoOperazione;
  }

  public void setTipoOperazione(String tipoOperazione)
  {
    this.tipoOperazione = tipoOperazione;
  }

  public ArrayList<DecodificaDTO<String>> getElencoFocusArea()
  {
    return elencoFocusArea;
  }

  public void setElencoFocusArea(
      ArrayList<DecodificaDTO<String>> elencoFocusArea)
  {
    this.elencoFocusArea = elencoFocusArea;
  }

  public String getDescrTipoOperazione()
  {
    return descrTipoOperazione;
  }

  public void setDescrTipoOperazione(String descrTipoOperazione)
  {
    this.descrTipoOperazione = descrTipoOperazione;
  }

}
