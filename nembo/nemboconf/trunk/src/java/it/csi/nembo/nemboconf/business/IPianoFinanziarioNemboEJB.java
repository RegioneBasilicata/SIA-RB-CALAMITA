package it.csi.nembo.nemboconf.business;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import it.csi.nembo.nemboconf.dto.PrioritaFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.ImportoFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.PianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;

@Local
public interface IPianoFinanziarioNemboEJB extends INemboAbstractEJB
{
  public List<PianoFinanziarioDTO> getElencoPianoFinanziari()
      throws InternalUnexpectedException;

  public PianoFinanziarioDTO getUltimoPianoFinanziarioInStato(
      long idStatoPianoFinanziario) throws InternalUnexpectedException;

  public PianoFinanziarioDTO getPianoFinanziarioCorrente(String codice)
      throws InternalUnexpectedException;

  public void inserisciPianoFinanziario(long idUtenteAggiornamento)
      throws InternalUnexpectedException, ApplicationException;

  public List<PrioritaFocusAreaDTO> getElencoPrioritaFocusArea(
      long idPianoFinanziario) throws InternalUnexpectedException;

  //TODO: remove
  public List<MisuraDTO> getElencoLivelli(long idPianoFinanziario)
      throws InternalUnexpectedException;

  public TipoOperazioneDTO insertRisorsa(long idPianoFinanziario,
      long idLivello, long idFocusArea, BigDecimal importo)
      throws InternalUnexpectedException, ApplicationException;

  public ImportoFocusAreaDTO getImportoPianoFinanziario(long idPianoFinanziario,
      long idLivello) throws InternalUnexpectedException;

  public TipoOperazioneDTO deleteRisorsa(long idPianoFinanziario,
      long idLivello, long idFocusArea)
      throws InternalUnexpectedException, ApplicationException;

  public void eliminaPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException, ApplicationException;

  public void updateRigaPianoFinanziario(long idPianoFinanziario,
      long idLivello, BigDecimal importo, BigDecimal importoTrascinato,
      String motivazioni, long extIdUtenteAggiornamento,
      BigDecimal importoPrimaDellaModifica,
      BigDecimal importoTrascinatoPrimaDellaModifica)
      throws InternalUnexpectedException, ApplicationException;

  public void consolidaPianoFinanziario(long id, String nomePianoStoricizzato,
      byte[] fileExcel, long idUtenteLogin)
      throws InternalUnexpectedException, ApplicationException;

  public byte[] getExcelPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException;

  public PianoFinanziarioDTO getPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException;

  public List<ImportoFocusAreaDTO> getStoricoImportiPianoFinanziario(
      long idPianoFinanziario, long idLivello)
      throws InternalUnexpectedException;
}
