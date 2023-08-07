//TODO: remove
package it.csi.nembo.nemboconf.business.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.nembo.nemboconf.business.IPianoFinanziarioNemboEJB;
import it.csi.nembo.nemboconf.dto.PrioritaFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.dto.pianofinanziario.ImportoFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.IterPianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.PianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.exception.NemboAssertionException;
import it.csi.nembo.nemboconf.integration.PianoFinanziarioNemboDAO;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;

@Stateless()
@EJB(name = "java:app/PianoFinanziarioNemboconf", beanInterface = IPianoFinanziarioNemboEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class PianoFinanziarioNemboEJB
    extends NemboAbstractEJB<PianoFinanziarioNemboDAO>
    implements IPianoFinanziarioNemboEJB
{
  final String THIS_CLASS = PianoFinanziarioNemboEJB.class.getSimpleName();

  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<PianoFinanziarioDTO> getElencoPianoFinanziari()
      throws InternalUnexpectedException
  {
    return dao.getElencoPianiFinanziari(
        NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE);
  }

  @Override
  public void inserisciPianoFinanziario(long idUtenteAggiornamento)
      throws InternalUnexpectedException, ApplicationException
  {
    final String CODICE_PIANO_Nemboconf = NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE;
    PianoFinanziarioDTO pianoDTO = null;
    IterPianoFinanziarioDTO iter = null;
    PianoFinanziarioDTO last = null;
    IterPianoFinanziarioDTO currentIter = null;
    try
    {
      // Lock
      dao.lockPianoFinanziario(CODICE_PIANO_Nemboconf);
      // Lock ottenuto, ho diritto di accesso esclusivo in inserimento del piano
      // finanziario
      // Devo capire se c'è già un piano finanziario e in che stato si trova
      last = dao.getPianoFinanziarioCorrente(CODICE_PIANO_Nemboconf);
      if (last != null)
      {
        // Esiste un piano finanziario ==> vediamo lo stato corrente
        currentIter = last.getCurrentIter();
        // Utils.ASSERT.notNull(currentIter, "iter corrente piano finanziario");
        long idStato = currentIter.getIdStatoPianoFinanziario();
        if (idStato != NemboConstants.PIANO_FINANZIARIO.STATO.ID.CONSOLIDATO)
        {
          throw new ApplicationException(
              NemboConstants.PIANO_FINANZIARIO.ERROR.INSERIMENTO_CON_VERSIONE_CORRENTE_IN_BOZZA);
        }
      }
      // Se sono arrivato qui vuol dire che ho superato tutti i controlli di
      // valitità, e l'eventuale piano finanziario quindi posso inserire il
      // nuovo piano
      // finanziario
      pianoDTO = new PianoFinanziarioDTO();
      pianoDTO.setDescrizione(" Versione ");
      pianoDTO.setExtIdUtenteAggiornamento(idUtenteAggiornamento);
      pianoDTO.setIdTipoPianoFinanziario(1);
      iter = new IterPianoFinanziarioDTO();
      iter.setIdPianoFinanziario(pianoDTO.getIdPianoFinanziario());
      iter.setExtIdUtenteAggiornamento(idUtenteAggiornamento);
      iter.setIdStatoPianoFinanziario(
          NemboConstants.PIANO_FINANZIARIO.STATO.ID.BOZZA);
      dao.insertPianoFinanziario(pianoDTO);
      iter.setIdPianoFinanziario(pianoDTO.getIdPianoFinanziario());
      dao.insertIterPianoFinanziario(iter);
      if (last != null)
      {
        dao.copyPianoFinanziario(pianoDTO.getIdPianoFinanziario(),
            last.getIdPianoFinanziario());
      }
    }
    catch (ApplicationException e)
    {
      context.setRollbackOnly();
      throw e;
    }
    catch (InternalUnexpectedException e)
    {
      context.setRollbackOnly();
      throw e;
    }
    catch (Exception e)
    {
      context.setRollbackOnly();
      throw new InternalUnexpectedException(e, new LogParameter[]
      { new LogParameter("idUtenteAggiornamento", idUtenteAggiornamento) },
          new LogVariable[]
          { new LogVariable("pianoDTO", pianoDTO),
              new LogVariable("iter", iter),
              new LogVariable("currentIter", currentIter),
              new LogVariable("last", last) });
    }
  }

  @Override
  public void consolidaPianoFinanziario(long idPianoFinanziario,
      String nomePianoStoricizzato, byte[] fileExcel,
      long idUtenteAggiornamento)
      throws InternalUnexpectedException, ApplicationException
  {
    final String CODICE_PIANO_Nemboconf = NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE;
    PianoFinanziarioDTO pianoFinanziarioDTO = null;
    PianoFinanziarioDTO pianoFinanziarioRiferimentoDTO = null;
    IterPianoFinanziarioDTO currentIter = null;
    IterPianoFinanziarioDTO newIter = null;
    IterPianoFinanziarioDTO iterStoricizzato = null;
    PianoFinanziarioDTO pianoConsolidato = null;
    try
    {
      // Lock
      dao.lockPianoFinanziario(CODICE_PIANO_Nemboconf);
      // Lock ottenuto, ho diritto di accesso esclusivo in inserimento del piano
      // finanziario
      pianoFinanziarioRiferimentoDTO = dao
          .getPianoFinanziario(idPianoFinanziario);
      pianoFinanziarioDTO = new PianoFinanziarioDTO();
      pianoFinanziarioDTO.setIdTipoPianoFinanziario(
          pianoFinanziarioRiferimentoDTO.getIdTipoPianoFinanziario());
      pianoFinanziarioDTO.setDescrizione(nomePianoStoricizzato);
      pianoFinanziarioDTO.setExtIdUtenteAggiornamento(idUtenteAggiornamento);
      pianoFinanziarioDTO.setPianoStoricizzato(fileExcel);
      long idPiano = dao
          .insertPianoFinanziarioStoricizzato(pianoFinanziarioDTO);

      // Inserisco il nuovo iter con stato STORICIZZATO
      newIter = new IterPianoFinanziarioDTO();
      newIter.setIdPianoFinanziario(idPiano);
      newIter.setExtIdUtenteAggiornamento(idUtenteAggiornamento);
      newIter.setIdStatoPianoFinanziario(
          NemboConstants.PIANO_FINANZIARIO.STATO.ID.STORICIZZATO);
      dao.insertIterPianoFinanziario(newIter);

      // Aggiorno a SYSDATE la DATA_CONSOLIDAMENTO della
      // NEMBO_R_LIVELLO_FOCUS_AREA
      dao.updateDataConsolidamento(idPianoFinanziario);
    }
    catch (NemboAssertionException e)
    {
      context.setRollbackOnly();
      e.setLogParameters(new LogParameter[]
      {
          new LogParameter("idPianoFinanziario", idPianoFinanziario),
          new LogParameter("idUtenteAggiornamento", idUtenteAggiornamento)
      });
      e.setLogVariables(new LogVariable[]
      {
          new LogVariable("iter", newIter),
          new LogVariable("currentIter", currentIter),
          new LogVariable("pianoConsolidato", pianoConsolidato),
          new LogVariable("iterStoricizzato", iterStoricizzato),
          new LogVariable("pianoFinanziarioDTO", pianoFinanziarioDTO)
      });
      throw e;
    }
    catch (InternalUnexpectedException e)
    {
      context.setRollbackOnly();
      throw e;
    }
    catch (Exception e)
    {
      context.setRollbackOnly();
      throw new InternalUnexpectedException(e,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idUtenteAggiornamento", idUtenteAggiornamento)
          },
          new LogVariable[]
          {
              new LogVariable("iter", newIter),
              new LogVariable("currentIter", currentIter),
              new LogVariable("pianoConsolidato", pianoConsolidato),
              new LogVariable("iterStoricizzato", iterStoricizzato),
              new LogVariable("pianoFinanziarioDTO", pianoFinanziarioDTO)
          });
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public PianoFinanziarioDTO getUltimoPianoFinanziarioInStato(
      long idStatoPianoFinanziario) throws InternalUnexpectedException
  {
    return dao.getUltimoPianoFinanziarioInStato(idStatoPianoFinanziario);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public PianoFinanziarioDTO getPianoFinanziarioCorrente(String codice)
      throws InternalUnexpectedException
  {
    return dao.getPianoFinanziarioCorrente(codice);
  }

  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<PrioritaFocusAreaDTO> getElencoPrioritaFocusArea(
      long idPianoFinanziario) throws InternalUnexpectedException
  {
    return dao.getElencoPrioritaFocusArea(idPianoFinanziario);
  }



  @Override
  public TipoOperazioneDTO insertRisorsa(long idPianoFinanziario,
      long idLivello, long idFocusArea, BigDecimal importo)
      throws InternalUnexpectedException, ApplicationException
  {
    dao.lockPianoFinanziario(
        NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE);
    PianoFinanziarioDTO pianoFinanziario = dao
        .getPianoFinanziario(idPianoFinanziario);
    if (pianoFinanziario == null)
    {
      throw new ApplicationException(
          "Il piano finanziario indicato non esiste. E' possibile che sia stato cancellato da qualche altro utente");
    }
    IterPianoFinanziarioDTO iterCorrente = pianoFinanziario.getCurrentIter();
    NemboUtils.ASSERT.notNull(iterCorrente,
        "Iter Corrente Piano Finanziario ");
    if (!iterCorrente.isInBozza())
    {
      throw new ApplicationException(
          "Il piano finanziario indicato non è più in bozza e non può essere modificato");
    }
    Long idLivelloFocusArea = dao.getIdLivelloFocusArea(idPianoFinanziario,
        idLivello, idFocusArea);
    if (idLivelloFocusArea == null)
    {
      // Non ho trovato un valore su db ==> cella vuota ==> la inserisco
      dao.insertRisorsa(idPianoFinanziario, idLivello, idFocusArea, importo);
    }
    else
    {
      // Trovato un valore preesistente ==> modifica di una cella ==> aggiorno
      // solo l'importo
      dao.updateRisorsa(idLivelloFocusArea, importo);
    }
    return dao.getTipoOperazionePianoFinanziario(idPianoFinanziario, idLivello);
  }

  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public ImportoFocusAreaDTO getImportoPianoFinanziario(long idPianoFinanziario,
      long idLivello) throws InternalUnexpectedException
  {
    return dao.getImportoPianoFinanziario(idPianoFinanziario, idLivello);
  }

  @Override
  public TipoOperazioneDTO deleteRisorsa(long idPianoFinanziario,
      long idLivello, long idFocusArea)
      throws InternalUnexpectedException, ApplicationException
  {
    dao.lockPianoFinanziario(
        NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE);
    PianoFinanziarioDTO pianoFinanziario = dao
        .getPianoFinanziario(idPianoFinanziario);
    if (pianoFinanziario == null)
    {
      throw new ApplicationException(
          "Il piano finanziario indicato non esiste. E' possibile che sia stato cancellato da qualche altro utente");
    }
    IterPianoFinanziarioDTO iterCorrente = pianoFinanziario.getCurrentIter();
    NemboUtils.ASSERT.notNull(iterCorrente,
        "Iter Corrente Piano Finanziario ");
    if (!iterCorrente.isInBozza())
    {
      throw new ApplicationException(
          "Il piano finanziario indicato non è più in bozza e non può essere modificato");
    }
    dao.deleteRisorsa(idPianoFinanziario, idLivello, idFocusArea);
    return dao.getTipoOperazionePianoFinanziario(idPianoFinanziario, idLivello);
  }

  @Override
  public void eliminaPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException, ApplicationException
  {
    final String CODICE_PIANO_Nemboconf = NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE;
    PianoFinanziarioDTO pianoFinanziarioDTO = null;
    IterPianoFinanziarioDTO currentIter = null;
    try
    {
      // Lock
      dao.lockPianoFinanziario(CODICE_PIANO_Nemboconf);
      // Lock ottenuto, ho diritto di accesso esclusivo in inserimento del piano
      // finanziario
      // Devo capire se c'è già un piano finanziario e in che stato si trova
      pianoFinanziarioDTO = dao.getPianoFinanziario(idPianoFinanziario);
      if (pianoFinanziarioDTO == null)
      {
        throw new ApplicationException(
            NemboConstants.PIANO_FINANZIARIO.ERROR.PIANO_FINANZIARIO_NON_TROVATO);
      }
      // Esiste un piano finanziario ==> vediamo lo stato corrente
      currentIter = pianoFinanziarioDTO.getCurrentIter();
      NemboUtils.ASSERT.notNull(currentIter,
          "iter corrente piano finanziario");
      long idStato = currentIter.getIdStatoPianoFinanziario();
      if (idStato != NemboConstants.PIANO_FINANZIARIO.STATO.ID.BOZZA)
      {
        throw new ApplicationException(
            NemboConstants.PIANO_FINANZIARIO.ERROR.ELIMINAZIONE_CON_VERSIONE_CORRENTE_NON_IN_BOZZA);
      }
      dao.delete("NEMBO_R_LIVELLO_FOCUS_AREA", "ID_PIANO_FINANZIARIO",
          idPianoFinanziario);
      dao.delete("NEMBO_T_ITER_PIANO_FINANZIARIO", "ID_PIANO_FINANZIARIO",
          idPianoFinanziario);
      dao.delete("NEMBO_T_PIANO_FINANZIARIO", "ID_PIANO_FINANZIARIO",
          idPianoFinanziario);
    }
    catch (ApplicationException e)
    {
      context.setRollbackOnly();
      throw e;
    }
    catch (NemboAssertionException e)
    {
      context.setRollbackOnly();
      e.setLogParameters(new LogParameter[]
      {
          new LogParameter("idPianoFinanziario", idPianoFinanziario)
      });
      e.setLogVariables(new LogVariable[]
      {
          new LogVariable("currentIter", currentIter),
          new LogVariable("pianoFinanziarioDTO", pianoFinanziarioDTO)
      });
      throw e;
    }
    catch (InternalUnexpectedException e)
    {
      context.setRollbackOnly();
      throw e;
    }
    catch (Exception e)
    {
      context.setRollbackOnly();
      throw new InternalUnexpectedException(e,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
          },
          new LogVariable[]
          {
              new LogVariable("currentIter", currentIter),
              new LogVariable("pianoFinanziarioDTO", pianoFinanziarioDTO)
          });
    }
  }

  @Override
  public void updateRigaPianoFinanziario(long idPianoFinanziario,
      long idLivello, BigDecimal importo, BigDecimal importoTrascinato,
      String motivazioni, long extIdUtenteAggiornamento,
      BigDecimal importoPrimaDellaModifica,
      BigDecimal importoTrascinatoPrimaDellaModifica)
      throws InternalUnexpectedException, ApplicationException
  {
    final String THIS_METHOD = "updateRigaPianoFinanziario";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      dao.lockPianoFinanziario(
          NemboConstants.PIANO_FINANZIARIO.TIPO.CODICE_REGIONALE);
      dao.updateTStoricoLivelloFA(idPianoFinanziario, idLivello,
          importoPrimaDellaModifica, importoTrascinatoPrimaDellaModifica,
          motivazioni, extIdUtenteAggiornamento);
      dao.updateRigaPianoFinanziario(idPianoFinanziario, idLivello, importo,
          importoTrascinato);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public byte[] getExcelPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    return dao.getExcelPianoFinanziario(idPianoFinanziario);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public PianoFinanziarioDTO getPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    return dao.getPianoFinanziario(idPianoFinanziario);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ImportoFocusAreaDTO> getStoricoImportiPianoFinanziario(
      long idPianoFinanziario, long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getStoricoImportiPianoFinanziario(idPianoFinanziario, idLivello);
  }

 
  //TODO: FIXME: maintain
  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<MisuraDTO> getElencoLivelli(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    return dao.getElencoLivelli(idPianoFinanziario);
  }


}
