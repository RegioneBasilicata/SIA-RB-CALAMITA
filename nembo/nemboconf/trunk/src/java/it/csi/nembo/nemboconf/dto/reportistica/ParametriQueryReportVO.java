package it.csi.nembo.nemboconf.dto.reportistica;

public class ParametriQueryReportVO
{

  private long   idBando;
  private String codEnteCaa;
  private String istruzioneSQL;
  private long   idTipoVisualizzazione;

  public Long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public String getCodEnteCaa()
  {
    return codEnteCaa;
  }

  public void setCodEnteCaa(String codEnteCaa)
  {
    this.codEnteCaa = codEnteCaa;
  }

  public String getIstruzioneSQL()
  {
    return istruzioneSQL;
  }

  public void setIstruzioneSQL(String istruzioneSQL)
  {
    this.istruzioneSQL = istruzioneSQL;
  }

  public long getIdTipoVisualizzazione()
  {
    return idTipoVisualizzazione;
  }

  public void setIdTipoVisualizzazione(long idTipoVisualizzazione)
  {
    this.idTipoVisualizzazione = idTipoVisualizzazione;
  }

}
