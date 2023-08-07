package it.csi.nembo.nemboconf.dto.catalogomisura;

import java.util.Date;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class SettoriDiProduzioneDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -8588871550254625010L;
  /**
   * 
   */
  private long              idSettore;
  private String            descrizione;
  private Date              dataInizio;
  private Date              dataFine;

  public long getIdSettore()
  {
    return idSettore;
  }

  public void setIdSettore(long idSettore)
  {
    this.idSettore = idSettore;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public String getDataInizioStr()
  {
    return NemboUtils.DATE.formatDate(dataInizio);
  }

  public String getDataFineStr()
  {
    return NemboUtils.DATE.formatDate(dataFine);
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
