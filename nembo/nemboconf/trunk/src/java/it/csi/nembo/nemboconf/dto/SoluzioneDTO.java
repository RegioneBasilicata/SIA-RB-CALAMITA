package it.csi.nembo.nemboconf.dto;

import java.io.Serializable;

public class SoluzioneDTO implements Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 4001162295524696513L;

  private String            descrizione;
  private String            flagNoteObbligatorie;
  private String            flagFileObbligatorio;
  private boolean           antimafia;
  private String            codiceIdentificativo;

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getFlagNoteObbligatorie()
  {
    if (flagNoteObbligatorie.equals("N"))
      return "NO";
    else
      return "SI";
  }

  public void setFlagNoteObbligatorie(String flagNoteObbligatorie)
  {
    this.flagNoteObbligatorie = flagNoteObbligatorie;
  }

  public boolean isAntimafia()
  {
    return antimafia;
  }

  public void setAntimafia(boolean antimafia)
  {
    this.antimafia = antimafia;
  }

  public String getFlagFileObbligatorio()
  {
    if (flagFileObbligatorio.equals("N"))
      return "NO";
    else
      return "SI";
  }

  public void setFlagFileObbligatorio(String flagFileObbligatorio)
  {
    this.flagFileObbligatorio = flagFileObbligatorio;
  }

  public String getCodiceIdentificativo()
  {
    return codiceIdentificativo;
  }

  public void setCodiceIdentificativo(String codiceIdentificativo)
  {
    this.codiceIdentificativo = codiceIdentificativo;
  }

}
