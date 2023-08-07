package it.csi.nembo.nemboconf.dto;

import java.util.Date;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class FilieraDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -2756063455328019902L;

  private Long              idFiliera;
  private Long              idTipoFiliera;
  private String            denominazioneFiliera;
  private String            descrizioneTipoFiliera;
  private Date              dataFineTipo;
  private String            descAltraFiliera;
  private String            descFiliera;
  private String            capofila;
  private String            azienda;
  private String            sedeLegale;
  private Long              idAzienda;

  public Long getIdFiliera()
  {
    return idFiliera;
  }

  public void setIdFiliera(Long idFiliera)
  {
    this.idFiliera = idFiliera;
  }

  public Long getIdTipoFiliera()
  {
    return idTipoFiliera;
  }

  public void setIdTipoFiliera(Long idTipoFiliera)
  {
    this.idTipoFiliera = idTipoFiliera;
  }

  public String getDescrizioneTipoFiliera()
  {
    return descrizioneTipoFiliera;
  }

  public void setDescrizioneTipoFiliera(String descrizioneTipoFiliera)
  {
    this.descrizioneTipoFiliera = descrizioneTipoFiliera;
  }

  public String getDescAltraFiliera()
  {
    return descAltraFiliera;
  }

  public void setDescAltraFiliera(String descAltraFiliera)
  {
    this.descAltraFiliera = descAltraFiliera;
  }

  public String getDescFiliera()
  {
    return descFiliera;
  }

  public void setDescFiliera(String descFiliera)
  {
    this.descFiliera = descFiliera;
  }

  public String getCapofila()
  {
    return capofila;
  }

  public void setCapofila(String capofila)
  {
    this.capofila = capofila;
  }

  public String getAzienda()
  {
    return azienda;
  }

  public void setAzienda(String azienda)
  {
    this.azienda = azienda;
  }

  public String getSedeLegale()
  {
    return sedeLegale;
  }

  public void setSedeLegale(String sedeLegale)
  {
    this.sedeLegale = sedeLegale;
  }

  public String getDenominazioneFiliera()
  {
    return denominazioneFiliera;
  }

  public void setDenominazioneFiliera(String denominazioneFiliera)
  {
    this.denominazioneFiliera = denominazioneFiliera;
  }

  public Long getIdAzienda()
  {
    return idAzienda;
  }

  public void setIdAzienda(Long idAzienda)
  {
    this.idAzienda = idAzienda;
  }

  public String getDataFineValiditaTipoFilieraStr()
  {
    return dataFineTipo != null ? NemboUtils.DATE.formatDate(dataFineTipo)
        : "";
  }

  public Date getDataFineTipo()
  {
    return dataFineTipo;
  }

  public void setDataFineTipo(Date dataFineTipo)
  {
    this.dataFineTipo = dataFineTipo;
  }

}
