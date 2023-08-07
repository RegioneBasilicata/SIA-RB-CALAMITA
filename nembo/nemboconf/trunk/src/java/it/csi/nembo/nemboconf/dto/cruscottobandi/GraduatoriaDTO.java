package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class GraduatoriaDTO implements ILoggable
{

  /**
   * 
   */
  private static final long     serialVersionUID = 3927178443882118949L;
  private long                  idGraduatoria;
  private String                descrizione;
  private List<FileAllegatoDTO> elencoAllegati;
  private Date                  dataApprovazione;

  public long getIdGraduatoria()
  {
    return idGraduatoria;
  }

  public void setIdGraduatoria(long idGraduatoria)
  {
    this.idGraduatoria = idGraduatoria;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public List<FileAllegatoDTO> getElencoAllegati()
  {
    return elencoAllegati;
  }

  public void setElencoAllegati(List<FileAllegatoDTO> elencoAllegati)
  {
    this.elencoAllegati = elencoAllegati;
  }

  public Date getDataApprovazione()
  {
    return dataApprovazione;
  }

  public void setDataApprovazione(Date dataApprovazione)
  {
    this.dataApprovazione = dataApprovazione;
  }

  public String getDataApprovazioneStr()
  {
    return NemboUtils.DATE.formatDateTime(dataApprovazione);
  }

}
