package it.csi.nembo.nemboconf.dto.cruscottobandi;

import org.apache.commons.validator.GenericValidator;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class AmmCompetenzaDTO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idBando;
  private long              idBandoMaster;
  private long              idAmmCompetenza;
  private String            descBreveTipoAmministraz;
  private String            descEstesaTipoAmministraz;
  private String            descrizione;
  private String            denominazioneuno;

  public AmmCompetenzaDTO()
  {
    super();
  }

  public AmmCompetenzaDTO(long idAmmCompetenza, String descrizione)
  {
    super();
    this.idAmmCompetenza = idAmmCompetenza;
    this.descrizione = descrizione;
  }

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdAmmCompetenza()
  {
    return idAmmCompetenza;
  }

  public void setIdAmmCompetenza(long idAmmCompetenza)
  {
    this.idAmmCompetenza = idAmmCompetenza;
  }

  public String getDescBreveTipoAmministraz()
  {
    return descBreveTipoAmministraz;
  }

  public void setDescBreveTipoAmministraz(String descBreveTipoAmministraz)
  {
    this.descBreveTipoAmministraz = descBreveTipoAmministraz;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getDescrizioneEstesa()
  {
    return ((GenericValidator.isBlankOrNull(descBreveTipoAmministraz) ? ""
        : descBreveTipoAmministraz + " - ") + descrizione).trim();
  }

  public long getIdBandoMaster()
  {
    return idBandoMaster;
  }

  public void setIdBandoMaster(long idBandoMaster)
  {
    this.idBandoMaster = idBandoMaster;
  }

  public String getDescEstesaTipoAmministraz()
  {
    return descEstesaTipoAmministraz;
  }

  public void setDescEstesaTipoAmministraz(String descEstesaTipoAmministraz)
  {
    this.descEstesaTipoAmministraz = descEstesaTipoAmministraz;
  }

  public String getDenominazioneuno()
  {
    return denominazioneuno;
  }

  public void setDenominazioneuno(String denominazioneuno)
  {
    this.denominazioneuno = denominazioneuno;
  }

}
