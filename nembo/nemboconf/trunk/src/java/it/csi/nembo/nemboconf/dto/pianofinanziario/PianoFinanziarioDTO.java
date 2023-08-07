package it.csi.nembo.nemboconf.dto.pianofinanziario;

import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class PianoFinanziarioDTO implements ILoggable
{

  /** serialVersionUID */
  private static final long             serialVersionUID = 839938191045544215L;
  private long                          idPianoFinanziario;
  private String                        descrizione;
  private Date                          dataUltimoAggiornamento;
  private long                          extIdUtenteAggiornamento;
  private long                          idTipoPianoFinanziario;
  private String                        descTipo;
  private String                        codiceTipoPianoFinanziario;
  private byte[]                        pianoStoricizzato;

  private List<IterPianoFinanziarioDTO> iter;

  public long getIdPianoFinanziario()
  {
    return idPianoFinanziario;
  }

  public void setIdPianoFinanziario(long idPianoFinanziario)
  {
    this.idPianoFinanziario = idPianoFinanziario;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public long getIdTipoPianoFinanziario()
  {
    return idTipoPianoFinanziario;
  }

  public void setIdTipoPianoFinanziario(long idTipoPianoFinanziario)
  {
    this.idTipoPianoFinanziario = idTipoPianoFinanziario;
  }

  public List<IterPianoFinanziarioDTO> getIter()
  {
    return iter;
  }

  public void setIter(List<IterPianoFinanziarioDTO> iter)
  {
    this.iter = iter;
  }

  public String getDescTipo()
  {
    return descTipo;
  }

  public void setDescTipo(String descTipo)
  {
    this.descTipo = descTipo;
  }

  public String getCodiceTipoPianoFinanziario()
  {
    return codiceTipoPianoFinanziario;
  }

  public void setCodiceTipoPianoFinanziario(String codiceTipoPianoFinanziario)
  {
    this.codiceTipoPianoFinanziario = codiceTipoPianoFinanziario;
  }

  public IterPianoFinanziarioDTO getCurrentIter()
  {
    if (iter != null)
    {
      for (IterPianoFinanziarioDTO current : iter)
      {
        if (current.getDataFine() == null)
        {
          return current;
        }
      }
    }
    return null;
  }

  public boolean isModificabile()
  {
    IterPianoFinanziarioDTO currentIter = getCurrentIter();
    return currentIter != null && currentIter
        .getIdStatoPianoFinanziario() == NemboConstants.PIANO_FINANZIARIO.STATO.ID.BOZZA;
  }

  public byte[] getPianoStoricizzato()
  {
    return pianoStoricizzato;
  }

  public void setPianoStoricizzato(byte[] pianoStoricizzato)
  {
    this.pianoStoricizzato = pianoStoricizzato;
  }

}
