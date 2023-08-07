package it.csi.nembo.nemboconf.dto.pianofinanziario;

import java.util.Date;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.integration.annotation.DBTable;
import it.csi.nembo.nemboconf.integration.annotation.DBUpdateColumn;
import it.csi.nembo.nemboconf.util.NemboConstants;

@DBTable("NEMBO_T_ITER_PIANO_FINANZIARIO")
public class IterPianoFinanziarioDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 920469693489227433L;
  @DBUpdateColumn(value = "ID_ITER_PIANO_FINANZIARIO", defaultInsertValue = "SEQ_NEMBO_T_ITER_PIANO_FINANZI.NEXTVAL")
  private long              idIterPianoFinanziario;
  @DBUpdateColumn(value = "ID_PIANO_FINANZIARIO", whereClause = true)
  private long              idPianoFinanziario;
  @DBUpdateColumn("ID_STATO_PIANO_FINANZIARIO")
  private long              idStatoPianoFinanziario;
  @DBUpdateColumn("EXT_ID_UTENTE_AGGIORNAMENTO")
  private long              extIdUtenteAggiornamento;
  @DBUpdateColumn("DATA_FINE")
  private Date              dataFine;
  @DBUpdateColumn(value = "DATA_INIZIO", defaultInsertValue = "SYSDATE")
  private Date              dataInizio;
  private String            descStato;
  private String            descUtente;

  public long getIdIterPianoFinanziario()
  {
    return idIterPianoFinanziario;
  }

  public void setIdIterPianoFinanziario(long idIterPianoFinanziario)
  {
    this.idIterPianoFinanziario = idIterPianoFinanziario;
  }

  public long getIdPianoFinanziario()
  {
    return idPianoFinanziario;
  }

  public void setIdPianoFinanziario(long idPianoFinanziario)
  {
    this.idPianoFinanziario = idPianoFinanziario;
  }

  public long getIdStatoPianoFinanziario()
  {
    return idStatoPianoFinanziario;
  }

  public void setIdStatoPianoFinanziario(long idStatoPianoFinanziario)
  {
    this.idStatoPianoFinanziario = idStatoPianoFinanziario;
  }

  public long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public String getDescStato()
  {
    return descStato;
  }

  public void setDescStato(String descStato)
  {
    this.descStato = descStato;
  }

  public String getDescUtente()
  {
    return descUtente;
  }

  public void setDescUtente(String descUtente)
  {
    this.descUtente = descUtente;
  }

  public boolean isInBozza()
  {
    return idStatoPianoFinanziario == NemboConstants.PIANO_FINANZIARIO.STATO.ID.BOZZA;
  }

  public boolean isConsolidato()
  {
    return idStatoPianoFinanziario == NemboConstants.PIANO_FINANZIARIO.STATO.ID.CONSOLIDATO;
  }

  public boolean isStoricizzato()
  {
    return idStatoPianoFinanziario == NemboConstants.PIANO_FINANZIARIO.STATO.ID.STORICIZZATO;
  }
}
