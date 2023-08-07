package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.Date;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class LogRecordDTO implements ILoggable
{

  /**
   * 
   */
  private static final long serialVersionUID = -3900089285744336415L;

  /**
   * 
   */

  private long              idBando;
  private long              idBandoOggetto;
  private String            descrOggetto;
  private long              idOggetto;
  private long              extIdUtenteAggiornamento;
  private Date              dataUltimoAggiornamento;
  private String            descrizioneAttivita;
  private String            note;
  private String            utente;

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public String getDescrizioneAttivita()
  {
    return descrizioneAttivita;
  }

  public void setDescrizioneAttivita(String descrizioneAttivita)
  {
    this.descrizioneAttivita = descrizioneAttivita;
  }

  public String getNote()
  {
    return note;
  }

  public void setNote(String note)
  {
    this.note = note;
  }

  public String getDataUltimoAggiornamentoStr()
  {
    return NemboUtils.DATE.formatDateTime(dataUltimoAggiornamento);
  }

  public String getUtente()
  {
    return utente;
  }

  public void setUtente(String utente)
  {
    this.utente = utente;
  }

  public String getDescrOggetto()
  {
    return descrOggetto;
  }

  public void setDescrOggetto(String descrOggetto)
  {
    this.descrOggetto = descrOggetto;
  }

  public long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(long idOggetto)
  {
    this.idOggetto = idOggetto;
  }
}
