package it.csi.nembo.nemboconf.dto.reportistica;

import java.io.Serializable;
import java.util.List;

import it.csi.nembo.nemboconf.dto.AttoreVO;

public class GraficoVO implements Serializable
{
  private static final long serialVersionUID = 9104726695231817976L;

  private String            descrBreve;
  private String            descrEstesa;
  private String            nomeTabella;
  private String            istruzioneSQL;
  private long              idTipoVisualizzazione;
  private long              idElencoQuery;
  private String            descrTipoVisualizzazione;
  private String            tipoTema;
  private String            flagVisibile;
  private Object            jsonData;
  private boolean           excelTemplate;
  private ReportVO          reportVO;
  private List<AttoreVO>    attori;

  public String getDescrAttori()
  {
    StringBuilder sb = new StringBuilder();
    if (attori != null)
    {
      for (AttoreVO att : attori)
      {
        sb.append(att.getDescrizione() + ", ");
      }
      sb.replace(sb.length() - 2, sb.length(), "");
    }
    return sb.toString();
  }

  public String getDescrBreve()
  {
    return descrBreve;
  }

  public void setDescrBreve(String descrBreve)
  {
    this.descrBreve = descrBreve;
  }

  public String getDescrEstesa()
  {
    return descrEstesa;
  }

  public void setDescrEstesa(String descrEstesa)
  {
    this.descrEstesa = descrEstesa;
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

  public String getDescrTipoVisualizzazione()
  {
    return descrTipoVisualizzazione;
  }

  public void setDescrTipoVisualizzazione(String descrTipoVisualizzazione)
  {
    this.descrTipoVisualizzazione = descrTipoVisualizzazione;
  }

  public String getTipoTema()
  {
    return tipoTema;
  }

  public void setTipoTema(String tipoTema)
  {
    this.tipoTema = tipoTema;
  }

  public Object getJsonData()
  {
    return jsonData;
  }

  public void setJsonData(Object jsonData)
  {
    this.jsonData = jsonData;
  }

  public ReportVO getReportVO()
  {
    return reportVO;
  }

  public void setReportVO(ReportVO reportVO)
  {
    this.reportVO = reportVO;
  }

  public String getDescrCompleta()
  {
    return descrBreve + "\n" + descrEstesa;
  }

  public String getNomeTabella()
  {
    return nomeTabella;
  }

  public void setNomeTabella(String nomeTabella)
  {
    this.nomeTabella = nomeTabella;
  }

  public long getIdElencoQuery()
  {
    return idElencoQuery;
  }

  public void setIdElencoQuery(long idElencoQuery)
  {
    this.idElencoQuery = idElencoQuery;
  }

  public String getFlagVisibile()
  {
    return flagVisibile;
  }

  public void setFlagVisibile(String flagVisibile)
  {
    this.flagVisibile = flagVisibile;
  }

  public List<AttoreVO> getAttori()
  {
    return attori;
  }

  public void setAttori(List<AttoreVO> attori)
  {
    this.attori = attori;
  }

  public boolean isExcelTemplate()
  {
    return excelTemplate;
  }

  public void setExcelTemplate(boolean hasQueryReport)
  {
    this.excelTemplate = hasQueryReport;
  }

}
