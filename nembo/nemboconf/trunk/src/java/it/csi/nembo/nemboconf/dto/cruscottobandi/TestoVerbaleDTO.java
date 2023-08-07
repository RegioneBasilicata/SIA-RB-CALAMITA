package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class TestoVerbaleDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 2132431469642801352L;
  protected Long            idTestoVerbale;
  protected Long            idGruppo;
  protected String          descrizione;
  protected String          flagCatalogo;
  protected int             ordine;
  protected Long            idTipoCollocazioneGruppo;
  protected Long            cntTipoCollPartenza;
  protected Long            cntTipoCollAvvio;

  public Long getCntTipoCollPartenza()
  {
    return cntTipoCollPartenza;
  }

  public void setCntTipoCollPartenza(Long cntTipoCollPartenza)
  {
    this.cntTipoCollPartenza = cntTipoCollPartenza;
  }

  public Long getCntTipoCollAvvio()
  {
    return cntTipoCollAvvio;
  }

  public void setCntTipoCollAvvio(Long cntTipoCollAvvio)
  {
    this.cntTipoCollAvvio = cntTipoCollAvvio;
  }

  public Long getIdTestoVerbale()
  {
    return idTestoVerbale;
  }

  public void setIdTestoVerbale(long idTestoVerbale)
  {
    this.idTestoVerbale = idTestoVerbale;
  }

  public Long getIdGruppo()
  {
    return idGruppo;
  }

  public void setIdGruppo(long idGruppo)
  {
    this.idGruppo = idGruppo;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getFlagCatalogo()
  {
    return flagCatalogo;
  }

  public void setFlagCatalogo(String flagCatalogo)
  {
    this.flagCatalogo = flagCatalogo;
  }

  public int getOrdine()
  {
    return ordine;
  }

  public void setOrdine(int ordine)
  {
    this.ordine = ordine;
  }

  public boolean isDisabled()
  {
    return NemboConstants.FLAGS.SI.equals(flagCatalogo);
  }

  public String getDisabledText()
  {
    return NemboConstants.FLAGS.SI.equals(flagCatalogo)
        ? "disabled=\"disabled\""
        : "";
  }

  public Long getIdTipoCollocazioneGruppo()
  {
    return idTipoCollocazioneGruppo;
  }

  public void setIdTipoCollocazioneGruppo(Long idTipoCollocazioneGruppo)
  {
    this.idTipoCollocazioneGruppo = idTipoCollocazioneGruppo;
  }

}
