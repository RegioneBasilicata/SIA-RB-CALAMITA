package it.csi.nembo.nemboconf.business.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.commons.validator.GenericValidator;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.ComuneBandoDTO;
import it.csi.nembo.nemboconf.dto.ComuneDTO;
import it.csi.nembo.nemboconf.dto.CriterioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.ElencoQueryBandoDTO;
import it.csi.nembo.nemboconf.dto.FilieraDTO;
import it.csi.nembo.nemboconf.dto.SoluzioneDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.ControlloAmministrativoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.ControlloDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.CruscottoInterventiDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.DettaglioInfoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.DocumentiRichiestiDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.EsitoCallPckDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.FileAllegatoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GraduatoriaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoTestoVerbaleDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.LivelloDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.LogRecordDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.OggettiIstanzeDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.QuadroDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.QuadroOggettoVO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.Ricevuta;
import it.csi.nembo.nemboconf.dto.cruscottobandi.TestoVerbaleDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.dto.reportistica.GraficoVO;
import it.csi.nembo.nemboconf.dto.reportistica.ParametriQueryReportVO;
import it.csi.nembo.nemboconf.dto.reportistica.ReportVO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.integration.CruscottoBandiDAO;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;

@Stateless()
@EJB(name = "java:app/CruscottoBandi", beanInterface = ICruscottoBandiEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class CruscottoBandiEJB extends NemboAbstractEJB<CruscottoBandiDAO>
    implements ICruscottoBandiEJB
{
  private SessionContext sessionContext;

  @Resource
  private void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<BandoDTO> getElencoBandiDisponibili(boolean isMaster,
      boolean isBandoGal, List<Long> idsBandiGal)
      throws InternalUnexpectedException
  {
    return dao.getElencoBandiDisponibili(isBandoGal, idsBandiGal);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<BandoDTO> getDettaglioBandiMaster()
      throws InternalUnexpectedException
  {
    return dao.getDettaglioBandiMaster();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<AmmCompetenzaDTO> getAmmCompetenzaAssociate(long idBando,
      boolean isMaster) throws InternalUnexpectedException
  {
    return dao.getAmmCompetenzaAssociate(idBando, isMaster);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<AmmCompetenzaDTO> getAmmCompetenzaDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getAmmCompetenzaDisponibili(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<AmmCompetenzaDTO> getAmmCompetenzaDisponibiliFondi(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getAmmCompetenzaDisponibiliFondi(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<TipoOperazioneDTO> getTipiOperazioniAssociati(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getTipiOperazioniAssociati(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<TipoOperazioneDTO> getTipiOperazioniDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getTipiOperazioniDisponibili(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getInformazioniBando(idBando);
  }

  @Override
  public boolean isBandoModificabile(long idBando)
      throws InternalUnexpectedException
  {
    return dao.isBandoModificabile(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<AmmCompetenzaDTO> getDettagliAmmCompetenza(
      Vector<Long> vIdAmmCompetenza) throws InternalUnexpectedException
  {
    return dao.getDettagliAmmCompetenza(vIdAmmCompetenza);
  }

  @Override
  public long aggiornaDatiIdentificativi(BandoDTO bando)
      throws InternalUnexpectedException
  {
    long idBando = bando.getIdBando();
    if (dao.isBandoEsistente(bando.getIdBando()))
    {
      dao.updateDBando(bando);
    }
    else
    {
      idBando = dao.insertDBando(bando);
      dao.insertRBandoMaster(idBando, bando.getIdBandoMaster());
    }

    Vector<Long> livelliInseritiDaUtente = bando.getvIdTipiOperazioni();
    Vector<Long> livelliPrecedenti = new Vector<Long>();
    List<TipoOperazioneDTO> tipiOperazioniPresenti = getTipiOperazioniAssociati(
        idBando);
    long idLivelloEsistente;


      if (tipiOperazioniPresenti != null)
      {
        // Cancello eventuali livelli deselezionati dall'utente
        for (TipoOperazioneDTO livelloPrec : tipiOperazioniPresenti)
        {
          idLivelloEsistente = livelloPrec.getIdLivello();
          livelliPrecedenti.add(idLivelloEsistente);
          if (!livelliInseritiDaUtente.contains(idLivelloEsistente))
          {
            // Sto cercando di cancellare un livello
            try
            {
              dao.deleteRLivelloBando(idBando, idLivelloEsistente);
            }
            catch (Exception e)
            {
              // Sto cercando di cancellare un livelloche ha già interventi
              // abbinati
              sessionContext.setRollbackOnly();
              throw new InternalUnexpectedException(
                  "Non puoi cancellare l'operazione "
                      + livelloPrec.getDescrizioneEstesa()
                      + " in quanto risultano esserci interventi configurati!",
                  e);
            }
          }
        }
      }

      for (Long idLivello : bando.getvIdTipiOperazioni())
      {
        if (!livelliPrecedenti.contains(idLivello))
        {
          dao.insertRLivelloBando(idBando, idLivello);
        }
      }
    

    try
    {
      dao.deleteDBandoAmmCompetenza(idBando);
      for (Long idAmm : bando.getvIdAmministrazioni())
      {
        dao.insertDBandoAmmCompetenza(idBando, idAmm);
      }

      // dao.deleteRElencoQueryBando(idBando, bando.getIdBandoMaster());
      // dao.insertRElencoQueryBando(idBando, bando.getIdBandoMaster());
    }
    catch (Exception e)
    {
      context.setRollbackOnly();
      throw e;
    }
    return idBando;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FileAllegatoDTO> getElencoAllegati(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoAllegati(idBando);
  }

  @Override
  public void insertFileAllegato(FileAllegatoDTO fileDTO)
      throws InternalUnexpectedException
  {
    dao.insertFileAllegato(fileDTO);
  }

  @Override
  public void deleteFileAllegato(long idAllegatiBando)
      throws InternalUnexpectedException
  {
    dao.deleteFileAllegato(idAllegatiBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getElencoFiltriSelezioneAziende()
      throws InternalUnexpectedException
  {
    return dao.getElencoFiltriSelezioneAziende();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getElencoTipiVincoli()
      throws InternalUnexpectedException
  {
    return dao.getElencoTipiVincoli();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public void updateFiltroBando(long idBando, String istruzioneSql,
      String descrizioneFiltro) throws InternalUnexpectedException
  {
    dao.updateFiltroBando(idBando, istruzioneSql, descrizioneFiltro);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<CruscottoInterventiDTO> getInterventi(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getInterventi(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<CruscottoInterventiDTO> getElencoInterventiSelezionati(
      long idBando) throws InternalUnexpectedException
  {
    return dao.getElencoInterventiSelezionati(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<CruscottoInterventiDTO> getElencoInterventiSelezionabili(
      long idBando) throws InternalUnexpectedException
  {
    return dao.getElencoInterventiSelezionabili(idBando);
  }

  @Override
  public void insertInterventi(long idBando, Vector<DecodificaDTO<Long>> valori)
      throws InternalUnexpectedException
  {

    dao.deleteRLivBandoIntervento(idBando);
    for (DecodificaDTO<Long> val : valori)
    {
      dao.insertRLivBandoIntervento(idBando, val.getId(),
          Long.parseLong(val.getCodice()));
    }
  }

  @Override
  public void eliminaIntervento(long idBando, long idLivello,
      long idDescrizioneIntervento) throws InternalUnexpectedException
  {
    dao.deleteRLivBandoIntervento(idBando, idLivello, idDescrizioneIntervento);
  }

  @Override
  public void updateIntervento(long idBando, long idDescrizioneIntervento,
      long idLivello, BigDecimal costoUnitMin, BigDecimal costoUnitMax)
      throws InternalUnexpectedException
  {
    dao.updateRLivBandoIntervento(idBando, idDescrizioneIntervento, idLivello,
        costoUnitMin, costoUnitMax);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoGruppiOggettiBandoMaster(long idBando,
      long idGruppoOggetto) throws InternalUnexpectedException
  {
    return dao.getElencoGruppiOggettiBandoMaster(idBando, idGruppoOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoGruppiOggetti(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoGruppiOggetti(idBando, null);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoGruppiOggetti(long idBando,
      String flagIstanza) throws InternalUnexpectedException
  {
    return dao.getElencoGruppiOggetti(idBando, flagIstanza);
  }

  @Override
  public int aggiornaSelezioneOggettiBando(long idBando,
      List<OggettiIstanzeDTO> elenco) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::aggiornaSelezioneOggettiBando]";
    int returnValue = 0;
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      for (OggettiIstanzeDTO item : elenco)
      {
        // caso 1: Se è stato deselezionato il checkbutton su un rek che era già
        // presente su NEMBO_R_BANDO_OGGETTO occorre cancellare nell’ordine i
        // rek delle tabelle legate
        if ("S".equals(item.getStatoPrecedente())
            && "N".equals(item.getStatoAttuale()))
        {
    	  boolean canLog = dao.canLogAttivitaBandoOggetto(item.getIdBandoOggetto());
    	  if(canLog)
    	  {
    	    returnValue = -1;
    	  }
    	  else
    	  {
    		  List<Long> listIdLegameInfo = dao
    				  .getIdLegameInfo(item.getIdBandoOggetto());
    		  dao.deleteDDettaglioInfo(item.getIdBandoOggetto());
    		  if (listIdLegameInfo != null)
    		  {
    			  dao.deteteDLegameInfo(listIdLegameInfo);
    		  }
    		  
    		  // elimino gruppi verbali
    		  List<GruppoTestoVerbaleDTO> gruppi = dao
    				  .getGruppiTestoVerbale(item.getIdBandoOggetto());
    		  for (GruppoTestoVerbaleDTO g : gruppi)
    		  {
    			  
    			  for (TestoVerbaleDTO t : g.getTestoVerbale())
    			  {
    				  dao.delete("NEMBO_D_TESTO_VERBALE", "ID_TESTO_VERBALE",
    						  t.getIdTestoVerbale());
    			  }
    			  dao.delete("NEMBO_D_GRUPPO_TESTO_VERBALE",
    					  "ID_GRUPPO_TESTO_VERBALE", g.getIdGruppoTestoVerbale());
    		  }
    		  
    		  dao.deleteByIdBAndoOggetto("NEMBO_D_GRUPPO_INFO",
    				  item.getIdBandoOggetto());
    		  dao.deleteByIdBAndoOggetto("NEMBO_T_VALORI_PARAMETRI",
    				  item.getIdBandoOggetto());
    		  dao.deleteByIdBAndoOggetto("NEMBO_R_BANDO_OGGETTO_CONTROLL",
    				  item.getIdBandoOggetto());
    		  dao.deleteByIdBAndoOggetto("NEMBO_R_BANDO_OGGETTO_QUADRO",
    				  item.getIdBandoOggetto());
    		  dao.deleteByIdBAndoOggetto("NEMBO_R_BANDO_OGGETTO_ICONA",
    				  item.getIdBandoOggetto());
    		  dao.deleteByIdBAndoOggetto("NEMBO_R_BAND_OG_QUAD_CONTR_AMM",
    				  item.getIdBandoOggetto());
    		  dao.deleteByIdBAndoOggetto("NEMBO_R_BANDO_OGGETTO",
    				  item.getIdBandoOggetto());
    	  }
    	}

        // caso 2: Se è stato selezionato il checkbutton su un rek che non era
        // presente occorre inserire un rek su NEMBO_R_BANDO_OGGETTO.
        else
          if ("N".equals(item.getStatoPrecedente())
              && "S".equals(item.getStatoAttuale()))
          {
            long idBandoOggetto = dao.insertRBandoOggetto(item.getIdBando(),
                item.getIdLegameGruppoOggetto(), item.getDataInizio(),
                item.getDataFine(), item.getDataRitardo());
            dao.insertRBandoOggettoIconaFromMaster(item.getIdBandoMaster(),
                idBandoOggetto, item.getIdBandoOggettoMaster());

            List<GruppoTestoVerbaleDTO> gruppi = dao
                .getGruppiTestoVerbale(item.getIdBandoOggettoMaster());

            for (GruppoTestoVerbaleDTO g : gruppi)
            {
              Long idGruppoTestoVerbale = dao.insertGruppoTestoVerbale(
                  idBandoOggetto, g.getIdElencoCdu(),
                  g.getTipoCollocazioneTesto());
              for (TestoVerbaleDTO t : g.getTestoVerbale())
              {
                if (t.getDescrizione() != null && t.getOrdine() != 0
                    && t.getFlagCatalogo() != null)
                  dao.insertTestiVerbali(idBandoOggetto, idGruppoTestoVerbale,
                      t.getDescrizione(), t.getOrdine(), t.getFlagCatalogo());
              }
            }

          }

          // caso 3: Sui checkbutton selezionati che erano riferiti a rek già
          // presenti occorre aggiornare i seguenti campi su
          // NEMBO_R_BANDO_OGGETTO
          else
            if ("S".equals(item.getStatoPrecedente())
                && "S".equals(item.getStatoAttuale()))
            {
              dao.updateRBandoOggetto(item.getIdBandoOggetto(),
                  item.getDataInizio(), item.getDataFine(),
                  item.getDataRitardo());
            }
      }
      return returnValue;
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<QuadroDTO> getElencoQuadriDisponibili(long idBando,
      long idGruppoOggetto, long idOggetto) throws InternalUnexpectedException
  {
    return dao.getElencoQuadriDisponibili(idBando, idGruppoOggetto, idOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<QuadroDTO> getElencoQuadriDisponibili(long idOggetto)
      throws InternalUnexpectedException
  {
    return dao.getElencoQuadriDisponibili(idOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<QuadroDTO> getElencoQuadriSelezionati(long idOggetto)
      throws InternalUnexpectedException
  {
    return dao.getElencoQuadriSelezionati(idOggetto);
  }

  @Override
  public void aggiornaQuadri(List<QuadroDTO> elenco)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::aggiornaQuadri]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      for (QuadroDTO quadro : elenco)
      {
        if ("N".equals(quadro.getFlagAttivo()))
        {
          /*
           * Per quelli che non hanno una corrispondenza sulla tabella
           * NEMBO_R_BANDO_OGGETTO_QUADRO occorre inserire il rek
           */
          if (quadro.getIdQuadroOggetto() == 0
              && quadro.getIdQuadroOggettoNew() != 0)
          {
            dao.insertRBandoOggettoQuadro(quadro.getIdBandoOggetto(),
                quadro.getIdQuadroOggettoNew());

            if (NemboConstants.QUADRO.CODICE.CONTROLLI_LOCO
                .equals(quadro.getCodice()))
            {
              dao.importaControlliInLoco(quadro.getIdBandoOggettoMaster(),
                  quadro.getIdBandoOggetto(), quadro.getIdQuadroOggettoNew());
            }
          }
          /*
           * Per quelli che sono deselezionati ed esiste un rek corrispondente
           * sulla tabella NEMBO_R_BANDO_OGGETTO_QUADRO occorre cancellare il
           * rek previa cancellazione anche dei rek delle tabelle collegate
           */
          else
            if (quadro.getIdQuadroOggetto() != 0
                && quadro.getIdQuadroOggettoNew() == 0)
            {
              if (NemboConstants.QUADRO.CODICE.CONTROLLI_LOCO
                  .equals(quadro.getCodice()))
              {
                dao.deleteControlliInLoco(quadro.getIdBandoOggetto());
              }

              dao.deleteTValoriParametri(quadro.getIdBandoOggetto(),
                  quadro.getIdQuadroOggetto());
              dao.deleteRBandoOggettoControlloByIdQuadro(
                  quadro.getIdBandoOggetto(), quadro.getIdQuadroOggetto());

              List<Long> listIdLegameInfo = dao.getIdLegameInfo(
                  quadro.getIdBandoOggetto(), quadro.getIdQuadroOggetto());
              dao.deleteDDettaglioInfo(quadro.getIdBandoOggetto(),
                  quadro.getIdQuadroOggetto());
              if (listIdLegameInfo != null)
              {
                dao.deteteDLegameInfo(listIdLegameInfo);
              }
              dao.deleteByIdBAndoQuadroOggetto("NEMBO_D_GRUPPO_INFO",
                  quadro.getIdBandoOggetto(), quadro.getIdQuadroOggetto());
              dao.deleteByIdBAndoQuadroOggetto("NEMBO_R_BANDO_OGGETTO_QUADRO",
                  quadro.getIdBandoOggetto(), quadro.getIdQuadroOggetto());
            }
        }
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e.getCause());
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getElencoGruppiControlliDisponibili(
      long idBando, String codiceQuadro) throws InternalUnexpectedException
  {
    return dao.getElencoGruppiControlliDisponibili(idBando, codiceQuadro);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoGruppiControlliDisponibili(
      long idBando) throws InternalUnexpectedException
  {
    return dao.getElencoGruppiControlliDisponibili(idBando);
  }
  
  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoGruppiControlliDisponibili(
      String flagIstanza, long idBando) throws InternalUnexpectedException
  {
    return dao.getElencoGruppiControlliDisponibili(flagIstanza,idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ControlloDTO> getElencoControlliDisponibili(long idBando,
      long idGruppoOggetto, long idOggetto) throws InternalUnexpectedException
  {
    return dao.getElencoControlliDisponibili(idBando, idGruppoOggetto,
        idOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean esisteValoreParametro(long idBandoOggetto, long idControllo)
      throws InternalUnexpectedException
  {
    return dao.esisteValoreParametro(idBandoOggetto, idControllo);
  }

  @Override
  public void updateControlli(List<ControlloDTO> controlli, Long idUtente,
      long idBandoOggetto, String msgLogFinal)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateControlli]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      for (ControlloDTO item : controlli)
      {
        /*
         * Per quelli che risultano selezionati, oppure sono protetti
         * (obbligatori) e non hanno una corrispondenza sulla tabella
         * NEMBO_R_BANDO_OGGETTO_CONTROLL
         */

        if (item.getIdControlloInserito() == 0
            && !GenericValidator.isBlankOrNull(item.getGravitaInserita()))
        {
          dao.insertRBandoOggettoControllo(item.getIdBandoOggetto(),
              item.getIdControllo(), item.getGravitaInserita());
        }
        /*
         * Per quelli che risultano non protetti e sono deselezionati ed esiste
         * un rek corrispondente sulla tabella NEMBO_R_BANDO_OGGETTO_CONTROLL
         * occorre cancellare il rek previa cancellazione anche dei rek della
         * tabella figlia NEMBO_T_VALORI_PARAMETRI
         */
        else
          if (item.getIdControlloInserito() != 0
              && GenericValidator.isBlankOrNull(item.getGravitaInserita()))
          {
            dao.deleteValoreParametro(item.getIdBandoOggetto(),
                item.getIdControllo());
            dao.deleteRBandoOggettoControllo(item.getIdBandoOggetto(),
                item.getIdControllo());

          }
          // se ho aggiornato solo la gravità, aggiorno su DB il valore, senza
          // toccare i paramentri aggiuntivi
          // else if("N".equals(item.getFlagObbligatorio()) &&
          // item.getIdControlloInserito() != 0 &&
          // !GenericValidator.isBlankOrNull(item.getGravitaInserita()))
          else
            if (item.getIdControlloInserito() != 0
                && !GenericValidator.isBlankOrNull(item.getGravitaInserita()))
            {
              dao.updateGravitaControllo(item.getIdBandoOggetto(),
                  item.getIdControllo(), item.getGravitaInserita());
            }

        // se il controllo è OBBLIGATORIO ed è la prima volta che si preme
        // conferma
        /*
         * copia tutti (di tutti i controlli obbligatori) i valori di
         * FLAG_GIUSTIFICABILE dalla NEMBO_R_QUADRO_OGGETTO_CONTROL alla
         * NEMBO_R_BANDO_OGGETTO_CONTROLL
         */
        if (item.getIdControlloInserito() == 0
            && item.getFlagObbligatorio().compareTo("S") == 0)
        {
          dao.updateGiustificazioneControllo(item.getIdBandoOggetto(),
              item.getIdControllo(), item.getFlagGiustificabileQuadro());
        }
        // se il controllo è OBBLIGATORIO e NON è la prima volta che si preme
        // conferma scrivo il flag che ho inserito a mano
        else
          if (item.getIdControlloInserito() != 0
              && item.getFlagObbligatorio().compareTo("S") == 0)
          {
            dao.updateGiustificazioneControllo(item.getIdBandoOggetto(),
                item.getIdControllo(), item.getFlagGiustificabileBando());
          }
          // se il controllo è FACOLTATIVO copio il flag inserito (se il record
          // è stato eliminato prima, no problem, non aggiorna nulla)
          else
            if (item.getFlagObbligatorio().compareTo("N") == 0)
            {
              dao.updateGiustificazioneControllo(item.getIdBandoOggetto(),
                  item.getIdControllo(), item.getFlagGiustificabileBando());
            }

      }
      if (idBandoOggetto != -1)
        if (msgLogFinal != "")
        {
          dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
              NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.CONTROLLI,
              msgLogFinal);
        }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }

  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<Long>> getParametriControllo(long idBandoOggetto,
      long idControllo) throws InternalUnexpectedException
  {
    return dao.getParametriControllo(idBandoOggetto, idControllo);
  }

  @Override
  public void aggiornaValoriParametri(long idBandoOggetto, long idControllo,
      List<String> valoriParametri) throws InternalUnexpectedException
  {
    dao.deleteValoreParametro(idBandoOggetto, idControllo);
    for (String valore : valoriParametri)
    {
      if (!GenericValidator.isBlankOrNull(valore))
      {
        dao.insertValoreParametro(idBandoOggetto, idControllo, valore);
      }
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoInfoDTO> getElencoDettagliInfo(long idBando,
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getElencoDettagliInfo(idBando, idQuadroOggetto, idBandoOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoInfoDTO> getElencoDettagliInfoCatalogo(long idBando,
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getElencoDettagliInfoCatalogo(idBando, idQuadroOggetto,
        idBandoOggetto);
  }

  @Override
  public void inserisciDichiarazioni(long idBando, long idBandoOggetto,
      long idQuadroOggetto, List<GruppoInfoDTO> dichiarazioni)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::inserisciDichiarazioni]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      final boolean isProcEsistente = dao.isProcedimentoOggettoQuadroInserito(
          idBando, idBandoOggetto, idQuadroOggetto);
      /*
       * Devo controllare se ci sono procedimenti oggetto inseriti per questa
       * configurazione. In caso positivo posso solamente aggiornare i rek
       * presenti o inserire quelli nuovi
       */
      if (!isProcEsistente)
      {
        /*
         * In questo caso il quadro oggetto è privo di procediemnti e posso
         * cancellare tutto e reinserite
         */
        List<Long> listIdLegameInfo = dao.getIdLegameInfo(idBandoOggetto,
            idQuadroOggetto);
        dao.deleteDDettaglioInfo(idBandoOggetto, idQuadroOggetto);
        if (listIdLegameInfo != null)
        {
          dao.deteteDLegameInfo(listIdLegameInfo);
        }
        dao.deleteByIdBAndoQuadroOggetto("NEMBO_D_GRUPPO_INFO", idBandoOggetto,
            idQuadroOggetto);
      }

      int ordineGruppo = 1;
      int ordineDett = 1;
      long idGruppoInfo;

      // Mappa contenente il mapping tra il legameInfo del catalogo e quello
      // creato per la nostra dichiarazione
      HashMap<Long, Long> mapLegamiInfoOldNew = new HashMap<Long, Long>();
      Long idLegameInfo = null;

      for (GruppoInfoDTO gruppoInfo : dichiarazioni)
      {
        if (isProcEsistente && gruppoInfo.getIdGruppoInfo() > 0)
        {
          idGruppoInfo = gruppoInfo.getIdGruppoInfo();
          dao.updateDescrGruppoInfo(idGruppoInfo, gruppoInfo.getDescrizione());
        }
        else
        {
          idGruppoInfo = dao.insertDGruppoInfo(idBandoOggetto,
              idQuadroOggetto,
              gruppoInfo.getDescrizione(),
              ordineGruppo,
              ((!GenericValidator
                  .isBlankOrNull(gruppoInfo.getFlagInfoCatalogo())
                  && "S".equals(gruppoInfo.getFlagInfoCatalogo())) ? "S"
                      : "N"));
        }

        ordineGruppo++;
        for (DettaglioInfoDTO dettInfo : gruppoInfo.getElencoDettagliInfo())
        {
          idLegameInfo = null;
          if (dettInfo.getIdVincolo() > 0)
          {
            idLegameInfo = dettInfo.getIdVincolo();
            if (mapLegamiInfoOldNew.containsKey(idLegameInfo))
            {
              idLegameInfo = mapLegamiInfoOldNew.get(idLegameInfo);
            }
            else
            {
              idLegameInfo = null;
              if (dettInfo.getIdVincoloDichiarazione() != 0)
              {
                idLegameInfo = dao.insertDLegameInfoTemp(
                    dettInfo.getIdVincoloDichiarazione());
                mapLegamiInfoOldNew.put(dettInfo.getIdVincolo(), idLegameInfo);
              }
            }
          }

          if (isProcEsistente && dettInfo.getIdDettaglioInfo() > 0)
          {
            dao.updateDDettaglioInfo(dettInfo.getIdDettaglioInfo(),
                dettInfo.getDescrizione(),
                ordineDett,
                ((!GenericValidator
                    .isBlankOrNull(dettInfo.getFlagObbligatorio())
                    && "S".equals(dettInfo.getFlagObbligatorio())) ? "S" : "N"),
                idLegameInfo,
                ((!GenericValidator
                    .isBlankOrNull(dettInfo.getFlagGestioneFile()))
                        ? dettInfo.getFlagGestioneFile()
                        : "N"),
                dettInfo.getExtIdTipoDocumento(),
                dettInfo.getCodiceInfo());
          }
          else
          {
            dao.insertDDettaglioInfo(idGruppoInfo,
                dettInfo.getDescrizione(),
                ordineDett,
                ((!GenericValidator
                    .isBlankOrNull(dettInfo.getFlagObbligatorio())
                    && "S".equals(dettInfo.getFlagObbligatorio())) ? "S" : "N"),
                idLegameInfo,
                ((!GenericValidator
                    .isBlankOrNull(dettInfo.getFlagGestioneFile()))
                        ? dettInfo.getFlagGestioneFile()
                        : "N"),
                dettInfo.getExtIdTipoDocumento(),
                dettInfo.getCodiceInfo());
          }
          ordineDett++;
        }
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean verificaDichiarazioni(long idBando, long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    boolean ret = dao.isDichiarazionePresente(idBandoOggetto, idQuadroOggetto);
    if (ret)
    {
      boolean find = false;
      List<GruppoInfoDTO> elencoGruppiInfoCatalogo = getElencoDettagliInfoCatalogo(
          idBando, idQuadroOggetto, idBandoOggetto);
      if (elencoGruppiInfoCatalogo != null)
      {
        for (GruppoInfoDTO item : elencoGruppiInfoCatalogo)
        {
          if ("S".equals(item.getFlagInfoObbligatoria()))
          {
            find = true;
            break;
          }
        }
        if (find)
        {
          ret = dao.isDichObbligatorieInserite(idBandoOggetto, idQuadroOggetto);
        }
      }
    }
    return ret;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public long getIdBandoOggetto(long idBando,
      long idOggetto, long idGruppoOggetto) throws InternalUnexpectedException
  {
    return dao.getIdBandoOggetto(idBando, idOggetto, idGruppoOggetto);
  }
  

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public DecodificaDTO<Long> getIdBandoOggetto(long idBando,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    return dao.getIdBandoOggetto(idBando, idQuadroOggetto);
  }


  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean isDichiarazionePresente(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    return dao.isDichiarazionePresente(idBandoOggetto, idQuadroOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getElencoTipiFileDisponibili()
      throws InternalUnexpectedException
  {
    return dao.getElencoTipiFileDisponibili();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> elencoQueryBando(long idBando,
      boolean flagElenco, String attore) throws InternalUnexpectedException
  {
    return dao.elencoQueryBando(idBando, flagElenco, attore);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public GraficoVO getGrafico(long idElencoQuery, ParametriQueryReportVO params)
      throws InternalUnexpectedException
  {
    GraficoVO graficoVO = dao.getDatiGrafico(idElencoQuery);

    params.setIstruzioneSQL(graficoVO.getIstruzioneSQL());
    params.setIdTipoVisualizzazione(graficoVO.getIdTipoVisualizzazione());
    ReportVO reportVO = dao.getReportBando(params);

    graficoVO.setReportVO(reportVO);
    graficoVO.setJsonData(reportVO.getJSON());
    return graficoVO;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public void updateAllegato(long idAllegatiBando,	
      String descrizione) throws InternalUnexpectedException
  {
    dao.updateAllegato(idAllegatiBando, descrizione);
  }

  @Override
  public void updateOrdineAllegato(long idAllegatiBandoPartenza,
      long idAllegatiBandoArrivo) throws InternalUnexpectedException
  {
    FileAllegatoDTO filePartenza = dao.getAllegato(idAllegatiBandoPartenza);
    FileAllegatoDTO fileArrivo = dao.getAllegato(idAllegatiBandoArrivo);

    String ordinePartenzaTmp = filePartenza.getOrdine();
    String ordineArrivoTmp = fileArrivo.getOrdine();

    dao.updateOrdineAllegato(idAllegatiBandoPartenza, ordineArrivoTmp);
    dao.updateOrdineAllegato(idAllegatiBandoArrivo, ordinePartenzaTmp);

  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<DecodificaDTO<String>> getIdLegameInfo(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    return dao.getDLegameInfo(idBandoOggetto, idQuadroOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean graficiTabellariPresenti(long idBando, String attore)
      throws InternalUnexpectedException
  {
    return dao.graficiTabellariPresenti(idBando, attore);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean isProcedimentoOggettoQuadroInserito(long idBando,
      long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    return dao.isProcedimentoOggettoQuadroInserito(idBando, idBandoOggetto,
        idQuadroOggetto);
  }

  @Override
  public byte[] getImmagineByIdQuadro(long idQuadro)
      throws InternalUnexpectedException
  {
    return dao.getImmagineByIdQuadro(idQuadro);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<AmmCompetenzaDTO> getAmmCompetenzaRisorsa(
      long idRisorseLivelloBando) throws InternalUnexpectedException
  {
    return dao.getAmmCompetenzaRisorsa(idRisorseLivelloBando);
  }

  @Override
  public List<GraduatoriaDTO> getGraduatorieBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getGraduatorieBando(idBando);

  }

  @Override
  public List<FileAllegatoDTO> getAllegatiGraduatoria(long idGraduatoria)
      throws InternalUnexpectedException
  {
    return dao.getAllegatiGraduatoria(idGraduatoria);
  }

  @Override
  public FileAllegatoDTO getFileAllegatoGraduatoria(long idAllegatiGraduatoria)
      throws InternalUnexpectedException
  {
    return dao.getFileAllegatoGraduatoria(idAllegatiGraduatoria);

  }

  @Override
  public List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException
  {
    return dao.getElencoLivelli();
  }

  @Override
  public List<LivelloDTO> getLivelliBando(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getLivelliBando(idBando);
  }

  @Override
  public List<ControlloAmministrativoDTO> getElencoControlliAmministrativiByIdQuadroOggetto(
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getElencoControlliAmministrativiByIdQuadroOggetto(
        idQuadroOggetto, idBandoOggetto);
  }

  @Override
  public void updateControlliTecnici(long idBandoOggetto, long idQuadroOggetto,
      List<ControlloAmministrativoDTO> elencoTmp)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateControlliTecnici]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      if (elencoTmp != null)
      {
        for (ControlloAmministrativoDTO controllo : elencoTmp)
        {
          for (ControlloAmministrativoDTO figlio : controllo
              .getControlliFigli())
          {
            if ("S".equals(figlio.getFlagSelezionato())
                || "S".equals(figlio.getFlagObbligatorio()))
            {
              if (dao.existRBandoOggQuadContrAmmLiv(idBandoOggetto,
                  figlio.getIdQuadroOggControlloAmm()))
              {
                continue;
              }

              dao.deleteRBandoOggContrAmm(idBandoOggetto,
                  figlio.getIdQuadroOggControlloAmm());
              if (figlio.getIdQuadroOggControlloAmm() <= 0)
              {
                long idControlloAmministrativo = dao
                    .insertControlloAmministrativo(
                        figlio.getIdControlloAmministratPadre(),
                        figlio.getCodice(), figlio.getDescrizione());
                long idQuadroOggControlloAmm = dao
                    .insertQuadroOggettoControlloAmministrativo(
                        idControlloAmministrativo, idQuadroOggetto);
                dao.insertBandoOggettoControlloAmministrazivo(
                    idQuadroOggControlloAmm, idBandoOggetto);
              }
              else
              {
                dao.insertBandoOggettoControlloAmministrazivo(
                    figlio.getIdQuadroOggControlloAmm(), idBandoOggetto);
              }
              // Se non l'ho già fatto, inserisco anche il padre
              if (!dao.existRBandoOggQuadContrAmm(idBandoOggetto,
                  controllo.getIdQuadroOggControlloAmm()))
              {
                dao.insertBandoOggettoControlloAmministrazivo(
                    controllo.getIdQuadroOggControlloAmm(), idBandoOggetto);
              }
            }
            else
            {
              dao.deleteRBandoOggContrAmmLiv(idBandoOggetto,
                  figlio.getIdQuadroOggControlloAmm());
              dao.deleteRBandoOggContrAmm(idBandoOggetto,
                  figlio.getIdQuadroOggControlloAmm());
            }
          }
        }
        dao.deleteControlliPadriSenzaFigli(idBandoOggetto);
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public void disattivaBandoOggetto(long idBandoOggetto, Long idUtente,
      String descrizione, String note)
      throws InternalUnexpectedException
  {

    try
    {
      dao.disattivaBandoOggetto(idBandoOggetto);
      dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente, descrizione, note);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
  }

  @Override
  public void attivaBandoOggetto(long idBandoOggetto, Long idUtente,
      String descrizione, String note)
      throws InternalUnexpectedException
  {

    try
    {
      dao.attivaBandoOggetto(idBandoOggetto);
      /*
       * controllo che non ci sia la stringa "ATTIVATO" nella tabella del log
       * /*per tutti gli idBandoOggetto relativi ad oggetti con flagIstanza='S'
       * /*lo faccio solo però nel caso in cui il flag istanza sia 'S'
       */
      if (dao.isOggettoIstanza(idBandoOggetto))
        if (dao.canSendMailAttivazione(idBandoOggetto))
        {
          // invio la mail
          String[] parametri = new String[1];
          parametri[0] = "MAIL_ASSIST_BANDO";
          Map<String, String> params = dao.getParametri(parametri);
          String indirizziMail = params.get("MAIL_ASSIST_BANDO");
          String[] indSplitted = null;
          if (indirizziMail != null)
          {
            indSplitted = indirizziMail.split("#");
            if (indSplitted != null)
            {
              Long idBando = dao.getIdBando(idBandoOggetto);
              BandoDTO bando = dao.getInformazioniBando(idBando);
              DateTimeFormatter format = DateTimeFormatter
                  .ofPattern("dd/MM/yyyy");
              String testoMail = "Con la presente si comunica che il bando \""
                  + bando.getDenominazione() +
                  "\" è stato attivato in data "
                  + LocalDate.now().format(format)
                  + ", con data inizio validità "
                  + bando.getDataInizioStrNoSec() + " e data fine validità "
                  + bando.getDataFineStrNoSec() + " dal referente "
                  + bando.getReferenteBando() + ".";
              NemboUtils.MAIL.postMail(
                  NemboConstants.MAIL.SERVIZI_AGRICOLTURA, indSplitted,
                  null,
                  "Attivazione del bando: \"" + bando.getDenominazione() + "\"",
                  testoMail, null);
            }
          }
        }
      dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente, "ATTIVATO", note);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
  }

  @Override
  public boolean canLogAttivitaBandoOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.canLogAttivitaBandoOggetto(idBandoOggetto);
  }

  @Override
  public void logAttivitaBandoOggetto(Long idBandoOggetto, Long idUtente,
      String descrizione, String note)
      throws InternalUnexpectedException
  {

    if (descrizione.compareTo("DUPLICA BANDO") == 0)
    {
      dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente, descrizione, note);
      return;
    }

    if (descrizione.compareTo("DISATTIVATO") != 0
        && descrizione.compareTo("ATTIVATO") != 0)
    {
      if (descrizione.compareTo(
          NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.ATTI_PUBBLICATI) != 0)
      {
        boolean can = dao.canLogAttivitaBandoOggetto(idBandoOggetto);
        if (can)
          dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente, descrizione,
              note);
      }
      else
      {
        boolean can = dao
            .canLogAttivitaBandoOggettoForAttiPubblicati(idBandoOggetto);
        if (can)
          dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente, descrizione,
              note);
      }
    }
    else
      dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente, descrizione, note);
  }

  @Override
  public Long getFirstIdBandoOggetto(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getFirstIdBandoOggetto(idBando);
  }

  @Override
  public FileAllegatoDTO getAllegato(long idAllegatoBando)
      throws InternalUnexpectedException
  {
    return dao.getAllegato(idAllegatoBando);
  }

  @Override
  public Boolean checkQuadroInserito(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.checkQuadroInserito(idBandoOggetto);
  }

  @Override
  public Boolean checkControlliObbligatoriInseriti(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.checkControlliObbligatoriInseriti(idBandoOggetto);
  }

  @Override
  public String getDescrizioneFiltro(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getDescrizioneFiltro(idBando);

  }

  @Override
  public List<LogRecordDTO> readLog(long idBando)
      throws InternalUnexpectedException
  {
    return dao.readLog(idBando);
  }

  @Override
  public String getDescrizioneOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getDescrizioneOggetto(idBandoOggetto);
  }

  @Override
  public String getHelpCdu(String codCdu) throws InternalUnexpectedException
  {
    return dao.getHelpCdu(codCdu);
  }

  @Override
  public List<FocusAreaDTO> getElencoFocusAreaBandi()
      throws InternalUnexpectedException
  {
    return dao.getElencoFocusAreaBandi();
  }

  @Override
  public List<SettoriDiProduzioneDTO> getElencoSettoriBandi()
      throws InternalUnexpectedException
  {
    return dao.getElencoSettoriBandi();
  }

  @Override
  public boolean checkParametriControlliInseriti(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.checkParametriControlliInseriti(idBandoOggetto);
  }

  @Override
  public Ricevuta getDatiRicevutaMail(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getDatiRicevutaMail(idBandoOggetto);
  }

  @Override
  public void updateRicevutaMail(long idUtenteAggiornamento,
      long idBandoOggetto, String oggettoMail, String corpoMail)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateRicevutaMail]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      dao.updateRBandoOggetto(idBandoOggetto, oggettoMail, corpoMail);
      String descroggetto = getDescrizioneOggetto(idBandoOggetto);
      logAttivitaBandoOggetto(idBandoOggetto, idUtenteAggiornamento,
          NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.RICEVUTA,
          descroggetto + " - Inseriti i seguenti dati: \n Oggetto:"
              + oggettoMail + "\n Corpo:" + corpoMail);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public GraficoVO getDescTabella(String nomeTabella)
      throws InternalUnexpectedException
  {
    GraficoVO graficoVO = new GraficoVO();
    ReportVO reportVO = dao.getDescTabella(nomeTabella);
    graficoVO.setReportVO(reportVO);
    graficoVO.setJsonData(reportVO.getJSON());
    return graficoVO;
  }

  @Override
  public void insertRow(String nomeTabella,
      LinkedHashMap<String, String> mapValues)
      throws InternalUnexpectedException
  {
    dao.insertRow(nomeTabella, mapValues);
  }

  @Override
  public void deleteRowByID(String nomeTabella, String nomeColonnaId, long id)
      throws InternalUnexpectedException
  {
    dao.deleteRowByID(nomeTabella, nomeColonnaId, id);
  }

  @Override
  public List<OggettiIstanzeDTO> getElencoOggettiNemboconf()
      throws InternalUnexpectedException
  {
    return dao.getElencoOggettiNemboconf();
  }

  @Override
  public void updateQuadroOggetto(long idOggetto, String[] aIdQuadri)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateQuadroOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      // Prelevo la situazione attuale
      List<QuadroDTO> lAttuale = getElencoQuadriSelezionati(idOggetto);
      List<String> idQuadriNew = (aIdQuadri == null) ? new ArrayList<String>()
          : Arrays.asList(aIdQuadri);
      List<String> idQuadriPrev = new ArrayList<String>();
      if (lAttuale != null)
      {
        for (QuadroDTO quadroAtt : lAttuale)
        {
          if (!idQuadriNew.contains(String.valueOf(quadroAtt.getIdQuadro())))
          {
            // Cancello il record
            dao.deleteRQuadroOggetto(quadroAtt.getIdQuadro(), idOggetto);
          }
          idQuadriPrev.add(String.valueOf(quadroAtt.getIdQuadro()));
        }
      }
      for (String newId : idQuadriNew)
      {
        if (!idQuadriPrev.contains(newId))
        {
          dao.insertRQuadroOggetto(idOggetto, Long.parseLong(newId));
        }
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public String getDescrizioneOggettoById(long idOggetto)
      throws InternalUnexpectedException
  {
    return dao.getDescrizioneOggettoById(idOggetto);
  }

  @Override
  public void updateOrdineQuadroOggetto(long idOggetto, long idQuadroPartenza,
      long idQuadroArrivo) throws InternalUnexpectedException
  {
    long partenza = dao.getOrdineQuadroOggetto(idOggetto, idQuadroPartenza);
    long arrivo = dao.getOrdineQuadroOggetto(idOggetto, idQuadroArrivo);

    dao.updateOrdineQuadroOggetto(idOggetto, idQuadroPartenza, arrivo);
    dao.updateOrdineQuadroOggetto(idOggetto, idQuadroArrivo, partenza);
  }

  @Override
  public List<BeneficiarioDTO> getBeneficiari(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getBeneficiari(idBando);
  }

  @Override
  public List<BeneficiarioDTO> getElencoBeneficiariDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoBeneficiariDisponibili(idBando);
  }

  @Override
  public List<BeneficiarioDTO> getElencoBeneficiariSelezionati(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoBeneficiariSelezionati(idBando);
  }

  @Override
  public void updateFormeGiuridiche(long idBando, String[] aIdFgTipologia)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateFormeGiuridiche]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      List<LivelloDTO> livelli = dao.getLivelliBandoFormeGiuridiche(idBando);
      dao.deleteRLivBandoBeneficiario(idBando);
      if (livelli != null)
      {
        for (LivelloDTO liv : livelli)
        {
          if (aIdFgTipologia != null)
          {
            for (String id : aIdFgTipologia)
            {
              dao.inserRLivBandoBeneficiario(idBando, liv.getIdLivello(),
                  Long.parseLong(id));
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public List<QuadroOggettoVO> getElencoQuadroOggettoNemboconf()
      throws InternalUnexpectedException
  {
    return dao.getElencoQuadroOggettoNemboconf();
  }

  @Override
  public List<ControlloDTO> getElencoQuadriControlliDisponibili(
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    return dao.getElencoQuadriControlliDisponibili(idQuadroOggetto);
  }

  @Override
  public List<ControlloDTO> getElencoQuadriControlliSelezionati(
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    return dao.getElencoQuadriControlliSelezionati(idQuadroOggetto);
  }

  @Override
  public void updateQuadroOggettoControllo(long idQuadroOggetto,
      String[] aIdControlli) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateQuadroOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      // Prelevo la situazione attuale
      List<ControlloDTO> lAttuale = getElencoQuadriControlliSelezionati(
          idQuadroOggetto);

      List<String> idQuadriNew = (aIdControlli == null)
          ? new ArrayList<String>()
          : Arrays.asList(aIdControlli);
      List<String> idQuadriPrev = new ArrayList<String>();
      if (lAttuale != null)
      {
        for (ControlloDTO quadroAtt : lAttuale)
        {
          if (!idQuadriNew.contains(String.valueOf(quadroAtt.getIdControllo())))
          {
            // Cancello il record
            dao.deleteRQuadroOggettoControllo(quadroAtt.getIdControllo(),
                idQuadroOggetto);
          }
          idQuadriPrev.add(String.valueOf(quadroAtt.getIdControllo()));
        }
      }

      for (String newId : idQuadriNew)
      {
        if (!idQuadriPrev.contains(newId))
        {
          dao.insertRQuadroOggettoControllo(idQuadroOggetto,
              Long.parseLong(newId));
        }
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public void updateQuadroOggettoControlli(
      List<ControlloDTO> vQuadroOggControlli) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateQuadroOggettoControlli]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      for (ControlloDTO item : vQuadroOggControlli)
      {
        dao.updateRQuadroOggettoControllu(item.getIdQuadroOggettoControllo(),
            item.getFlagObbligatorio(), item.getGravita());
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public List<LivelloDTO> getElencoLivelliDisponibili(long idBandoOggetto,
      long idQuadroOggControlloAmm) throws InternalUnexpectedException
  {
    List<LivelloDTO> list = dao.getElencoLivelliDisponibili(idBandoOggetto,
        idQuadroOggControlloAmm);
    if (list == null)
    {
      list = new ArrayList<LivelloDTO>();
    }
    return list;
  }

  @Override
  public List<LivelloDTO> getElencoLivelliSelezionati(long idBandoOggetto,
      long idQuadroOggControlloAmm) throws InternalUnexpectedException
  {
    List<LivelloDTO> list = dao.getElencoLivelliSelezionati(idBandoOggetto,
        idQuadroOggControlloAmm);
    if (list == null)
    {
      list = new ArrayList<LivelloDTO>();
    }
    return list;
  }

  @Override
  public void updateControlliLivelli(long idBandoOggetto,
      long idQuadroOggControlloAmm, String[] aIdControlli)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateControlliLivelli]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      // Prelevo la situazione attuale
      List<LivelloDTO> lAttuale = getElencoLivelliSelezionati(idBandoOggetto,
          idQuadroOggControlloAmm);

      List<String> idQuadriNew = (aIdControlli == null)
          ? new ArrayList<String>()
          : Arrays.asList(aIdControlli);
      List<String> idQuadriPrev = new ArrayList<String>();
      if (lAttuale != null)
      {
        for (LivelloDTO quadroAtt : lAttuale)
        {
          if (!idQuadriNew.contains(String.valueOf(quadroAtt.getIdLivello())))
          {
            // Cancello il record
            dao.deleteLivelloContrAmm(idBandoOggetto, idQuadroOggControlloAmm,
                quadroAtt.getIdLivello());
          }
          idQuadriPrev.add(String.valueOf(quadroAtt.getIdLivello()));
        }
      }

      for (String newId : idQuadriNew)
      {
        if (!idQuadriPrev.contains(newId))
        {
          dao.insertLivelloContrAmm(idBandoOggetto, idQuadroOggControlloAmm,
              Long.parseLong(newId));
        }
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public List<DecodificaDTO<String>> getElencoPlaceholder()
      throws InternalUnexpectedException
  {
    return dao.getElencoPlaceholder();
  }

  @Override
  public Boolean isOggettoIstanza(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.isOggettoIstanza(idBandoOggetto);
  }

  @Override
  public Boolean isOggettoIstanzaPagamento(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.isOggettoIstanzaPagamento(idBandoOggetto);
  }

  @Override
  public List<DecodificaDTO<Long>> getElencoDocumentiBandoOggetto(long idBando,
      long idBandoMaster, long idBandoOggetto, boolean getFromMaster)
      throws InternalUnexpectedException
  {
    long idBandoOggettoMaster = dao.getIdBandoOggettoMaster(idBando,
        idBandoMaster, idBandoOggetto);

    final String THIS_METHOD = "getElencoDocumentiBandoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      if (getFromMaster)
        return dao.getElencoDocumentiBandoOggetto(idBandoOggettoMaster);
      else
        return dao.getElencoDocumentiBandoOggetto(idBandoOggetto);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public List<GruppoTestoVerbaleDTO> getGruppiTestoVerbale(long idBandoOggetto,
      long idElencoCdu, String flagVisibile, String flagCatalogo)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getGruppiTestoVerbale";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getGruppiTestoVerbale(idBandoOggetto, idElencoCdu,
          flagVisibile, flagCatalogo);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public List<TestoVerbaleDTO> updateTestiVerbali(long idBandoOggetto,
      long idElencoCdu,
      List<DecodificaDTO<Long>> testi, Long idUtente, String msgLog)
      throws InternalUnexpectedException, ApplicationException
  {
    try
    {
      List<TestoVerbaleDTO> testiInseriti = new ArrayList<>();
      if (dao.isBandoOggettoModificabile(idBandoOggetto))
      {
        dao.deleteTestiVerbaleModificabili(idBandoOggetto, idElencoCdu);
        testiInseriti = dao.insertTestiVerbali(testi);

        if (msgLog != "")
          dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
              NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.TESTI_VERBALI,
              msgLog);

        return testiInseriti;
      }
      else
      {
        throw new ApplicationException("Oggetto non modificabile");
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
  }

  @Override
  public List<DecodificaDTO<String>> getElencoPlaceholderNemboconf()
      throws InternalUnexpectedException
  {
    return dao.getElencoPlaceholderNemboconf();
  }

  @Override
  public List<GraficoVO> elencoQueryBando(long idBando, boolean flagElenco)
      throws InternalUnexpectedException
  {
    return dao.elencoQueryBando(idBando, flagElenco);
  }

  @Override
  public void aggiornaGraficiBando(long idBando, Long[] idsElencoQuery,
      Long idUtente, String msgLog) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::aggiornaGraficiBando]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      dao.updateRElencoQueryBando(idBando, "N");
      if (idsElencoQuery != null)
        dao.updateRElencoQueryBando(idBando, "S", idsElencoQuery);
      // LOG
      Long idBandoOggetto = dao.getFirstIdBandoOggetto(idBando);
      if (idBandoOggetto != null)
        dao.logAttivitaBandoOggetto(idBandoOggetto, idUtente,
            NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.GRAFICI, msgLog);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public List<BandoDTO> getIdElencoBandiDisponibili()
      throws InternalUnexpectedException
  {
    return dao.getIdElencoBandiDisponibili();
  }

  @Override
  public List<TipoOperazioneDTO> getTipiOperazioniDisponibiliGAL(long idBando,
      long idAmmComp) throws InternalUnexpectedException
  {
    return dao.getTipiOperazioniDisponibiliGAL(idBando, idAmmComp);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FilieraDTO> getTipiFiliereAssociate(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getTipiFiliereAssociate(idBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FilieraDTO> getTipiFiliereDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getTipiFiliereDisponibili(idBando);
  }

  @Override
  public void eliminaTipoFiliera(long idBando, long idTipoFiliera)
      throws InternalUnexpectedException
  {
    dao.eliminaTipoFiliera(idBando, idTipoFiliera);
  }

  @Override
  public void updateFiliere(long idBando, Vector<Long> idsTipiFiliera)
      throws InternalUnexpectedException
  {
    dao.delete("NEMBO_R_BANDO_FILIERA", "ID_BANDO", idBando);
    for (Long l : idsTipiFiliera)
      dao.insertRBandoFiliera(idBando, l);
  }

  @Override
  public void updateOrdineTestiGruppo(GruppoTestoVerbaleDTO gr)
      throws InternalUnexpectedException
  {
    try
    {
      for (TestoVerbaleDTO testo : gr.getTestoVerbale())
      {
        if (testo.getIdTestoVerbale() != null)
        {
          dao.delete("NEMBO_D_TESTO_VERBALE", "ID_TESTO_VERBALE",
              testo.getIdTestoVerbale());
          dao.insertTestiVerbaliOrdine(gr.getIdGruppoTestoVerbale(),
              testo.getIdTestoVerbale(), testo.getDescrizione(),
              testo.getFlagCatalogo(), testo.getOrdine());
        }
      }
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
  }

  @Override
  public List<TestoVerbaleDTO> getTestoVerbali(long idBandoOggetto,
      long idElencoCdu, long idGruppoTestoVerbaliPartenza)
      throws InternalUnexpectedException
  {

    Long tipoCollocazioneTesto = dao
        .getTipoCollocazioneTesto(idGruppoTestoVerbaliPartenza);
    Long cntTipiDestinazione = dao
        .countTipiCollocazioneTesti(idGruppoTestoVerbaliPartenza);
    Long cntTipiPartenza = dao.countTipiCollocazioneTesti(idElencoCdu,
        idBandoOggetto);

    return dao.getTestoVerbali(idBandoOggetto, idElencoCdu,
        tipoCollocazioneTesto, cntTipiDestinazione, cntTipiPartenza);
  }

  @Override
  public void duplicaTestoVerbali(long idBandoOggetto, long idElencoCdu,
      List<TestoVerbaleDTO> testiNonInCatalogo, long idGruppoTestoDestinazione,
      String msgLog, Long idUtenteLogin)
      throws InternalUnexpectedException
  {
    try
    {
      for (TestoVerbaleDTO testo : testiNonInCatalogo)
      {
        Long cntTipiPartenza = testo.getCntTipoCollPartenza();
        Long cntTipiArrivo = testo.getCntTipoCollAvvio();
        Long tipoCollocazioneTesto = dao
            .getTipoCollocazioneTesto(idGruppoTestoDestinazione);

        if (cntTipiPartenza < cntTipiArrivo)
        {
          // da doc con 5 gruppi a 6
          if (tipoCollocazioneTesto != 1)
          {
            tipoCollocazioneTesto++;
            dao.duplicaTestoVerbali(testo, idElencoCdu, idBandoOggetto,
                idGruppoTestoDestinazione);
          }
          else
          {
            // se no COPIO SIA nel grupo con collocazione 1 CHE 2
            dao.duplicaTestoVerbali(testo, idElencoCdu, idBandoOggetto,
                idGruppoTestoDestinazione);
            tipoCollocazioneTesto += 1;
            testo.setIdTipoCollocazioneGruppo(tipoCollocazioneTesto);
            // Long newIdGruppo =
            // dao.getNextIdGruppoTesto(idGruppoTestoDestinazione,
            // tipoCollocazioneTesto);
            // dao.duplicaTestoVerbali(testo, idElencoCdu,
            // idBandoOggetto,newIdGruppo);
          }

        }
        else
          if (cntTipiArrivo < cntTipiPartenza)
          {
            // da doc con 6 gruppi a 5
            if (tipoCollocazioneTesto != 1)
            {
              tipoCollocazioneTesto--;
            }
            dao.duplicaTestoVerbali(testo, idElencoCdu, idBandoOggetto,
                idGruppoTestoDestinazione);
          }
          else
          {
            // da 6 a 6 o da 5 a 5 - copia normale
            dao.duplicaTestoVerbali(testo, idElencoCdu, idBandoOggetto,
                idGruppoTestoDestinazione);
          }

      }
      dao.logAttivitaBandoOggetto(idBandoOggetto, idUtenteLogin,
          NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.TESTI_VERBALI, msgLog);

    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
  }

  @Override
  public void importaTestiDaCatalogo(long idBando, long idBandoMaster,
      long idBandoOggetto, long idElencoCdu, String msgLog, Long idUtenteLogin)
      throws InternalUnexpectedException
  {

    try
    {

      // leggo tutti i gruppi attualmente esistenti ed elimino (dalla
      // collection) tutti i testi in catalogo
      // cosi mi restano quelli in catalogo da riaggiungere poi al fondo
      List<GruppoTestoVerbaleDTO> gruppiConTestiNonInCatalogo = dao
          .getGruppiTestoVerbale(idBandoOggetto, idElencoCdu, null, "N");

      // elimino tutti i testi
      dao.deleteTestiVerbaliPerImportazione(idBandoOggetto, idElencoCdu);

      // get idBandoOggetto del bando master da cui dovrò copiare i testi
      long idBandoOggettoMaster = dao.getIdBandoOggettoMaster(idBando,
          idBandoMaster, idBandoOggetto);

      List<GruppoTestoVerbaleDTO> gruppi = dao
          .getGruppiTestoVerbalePerImportazioneCatalogo(idBandoOggettoMaster,
              idElencoCdu, null, "S");
      int ordine = 0;
      int maxOrdine = 0;
      if (gruppi != null && !gruppi.isEmpty())
      {
        boolean hasTesti = false;
        for (GruppoTestoVerbaleDTO g : gruppi)
        {
          if (g.getTestoVerbale() != null && !g.getTestoVerbale().isEmpty()
              && g.getTestoVerbale().get(0).getDescrizione() != null)
            hasTesti = true;
        }

        if (hasTesti)
        {
          for (GruppoTestoVerbaleDTO g : gruppi)
          {
            maxOrdine = 0;
            long idGruppoTestoVerbale = 0;
            for (GruppoTestoVerbaleDTO gr : gruppiConTestiNonInCatalogo)
            {
              if (g.getTipoCollocazioneTesto() == gr.getTipoCollocazioneTesto())
              {
                idGruppoTestoVerbale = gr.getIdGruppoTestoVerbale();
                break;
              }
            }

            if (g.getTestoVerbale() != null && !g.getTestoVerbale().isEmpty()
                && idGruppoTestoVerbale != 0)
              for (TestoVerbaleDTO t : g.getTestoVerbale())
              {
                if (t.getFlagCatalogo() != null)// &&
                // t.getFlagCatalogo().compareTo("S")==0)
                {
                  // copio tutti i testi del master
                  dao.insertTestiVerbali(idBandoOggetto, idGruppoTestoVerbale,
                      t.getDescrizione(),
                      t.getOrdine(), "N");
                  ordine = t.getOrdine();
                  if (ordine > maxOrdine)
                    maxOrdine = ordine;
                }
              }

            ordine = maxOrdine + 10;
            // inserisco tutti i testi NON in catalogo che mi sono
            // salvato prima
            for (GruppoTestoVerbaleDTO gr : gruppiConTestiNonInCatalogo)
            {
              if (g.getTipoCollocazioneTesto() == gr.getTipoCollocazioneTesto())
              {
                if (gr.getTestoVerbale() != null
                    && !gr.getTestoVerbale().isEmpty())
                  for (TestoVerbaleDTO t : gr.getTestoVerbale())
                  {
                    ordine += 10;
                    dao.insertTestiVerbali(idBandoOggetto,
                        gr.getIdGruppoTestoVerbale(),
                        t.getDescrizione(), ordine, "N");
                  }
              }
            }
          }
        }
        else
        {
          for (GruppoTestoVerbaleDTO gr : gruppiConTestiNonInCatalogo)
          {

            if (gr.getTestoVerbale() != null && !gr.getTestoVerbale().isEmpty())
              for (TestoVerbaleDTO t : gr.getTestoVerbale())
              {
                ordine += 10;
                dao.insertTestiVerbali(idBandoOggetto,
                    gr.getIdGruppoTestoVerbale(), t.getDescrizione(),
                    ordine, "N");
              }
          }
        }
      }

      dao.logAttivitaBandoOggetto(idBandoOggetto, idUtenteLogin,
          NemboConstants.LOGGIN.LOG_ATTIVITA_OGGETTI.TESTI_VERBALI, msgLog);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e);
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean isDatiIdentificativiModificabili(long idBando)
      throws InternalUnexpectedException
  {
    return dao.isDatiIdentificativiModificabili(idBando);
  }

  @Override
  public boolean hasGruppiDaImportare(long idBandoMaster, long idBando,
      long idBandoOggetto, long idElencoCdu)
      throws InternalUnexpectedException
  {
    long idBandoOggettoMaster = dao.getIdBandoOggettoMaster(idBando,
        idBandoMaster, idBandoOggetto);
    List<GruppoTestoVerbaleDTO> gruppi = dao
        .getGruppiTestoVerbalePerImportazioneCatalogo(idBandoOggettoMaster,
            idElencoCdu, null, "S");

    boolean hasTesti = false;

    if (gruppi != null && !gruppi.isEmpty())
    {
      for (GruppoTestoVerbaleDTO g : gruppi)
      {
        if (g.getTestoVerbale() != null && !g.getTestoVerbale().isEmpty()
            && g.getTestoVerbale().get(0).getDescrizione() != null)
          hasTesti = true;
      }

    }
    return hasTesti;
  }

  @Override
  public void creaGruppiTestoVerbali(long idBando, long idBandoOggetto,
      long lIdElencoCdu, long idBandoMaster)
      throws InternalUnexpectedException
  {
    long idBandoOggettoMaster = dao.getIdBandoOggettoMaster(idBando,
        idBandoMaster, idBandoOggetto);
    dao.creaGruppiTestoVerbali(idBandoOggetto, lIdElencoCdu,
        idBandoOggettoMaster);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public DecodificaDTO<Long> getInfoBandoOggetto(long idBando,
      Long idQuadroOggetto, Long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getInfoBandoOggetto(idBando, idQuadroOggetto, idBandoOggetto);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public ControlloDTO getControlloById(String idControllo)
      throws InternalUnexpectedException
  {
    return dao.getControlloById(idControllo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<SoluzioneDTO> getSoluzioniControllo(String idControllo)
      throws InternalUnexpectedException
  {
    return dao.getSoluzioniControllo(idControllo);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<BandoDTO> getElencoBandiByDenominazione(String denominazione,
      Long idBandoSelezionato) throws InternalUnexpectedException
  {
    return dao.getElencoBandiByDenominazione(denominazione, idBandoSelezionato);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean verificaUnivocitaNomeBando(String nomeNuovoBando)
      throws InternalUnexpectedException
  {
    return dao.verificaUnivocitaNomeBando(nomeNuovoBando);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<GruppoOggettoDTO> getElencoGruppiOggettiAttivi(Long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoGruppiOggettiAttivi(idBando, null);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean haQualcosaDiVuoto(long idBando, long idGruppoOggetto,
      long idOggetto, String flagIstanza) throws InternalUnexpectedException
  {

    if ((/* dao.hasQuadro(idBando, idOggetto, idGruppoOggetto, "IMPEG") && */ !dao
        .hasQualcosaInQuadro(idBando, idGruppoOggetto, idOggetto, "IMPEG"))
        || (/* dao.hasQuadro(idBando, idOggetto, idGruppoOggetto, "ALLEG") && */ !dao
            .hasQualcosaInQuadro(idBando, idGruppoOggetto, idOggetto, "ALLEG"))
        || (/* dao.hasQuadro(idBando, idOggetto, idGruppoOggetto,"DICH") && */ !dao
            .hasQualcosaInQuadro(idBando, idGruppoOggetto, idOggetto, "DICH"))
        || ("N".equals(flagIstanza)
            && !dao.hasTestiVerbali(idBando, idGruppoOggetto, idOggetto)))
      return true;
    return false;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean hasQualcosaInQuadro(long idBando, long idGruppoOggetto,
      long idOggetto, String codiceQuadro) throws InternalUnexpectedException
  {
    return (dao.hasQuadro(idBando, idGruppoOggetto, idOggetto, codiceQuadro)
        && dao.hasQualcosaInQuadro(idBando, idGruppoOggetto, idOggetto,
            codiceQuadro));
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean hasTestiVerbali(long idBando, long idGruppoOggetto,
      long idOggetto)
      throws InternalUnexpectedException
  {
    return dao.hasTestiVerbali(idBando, idGruppoOggetto, idOggetto);
  }

  @Override
  public EsitoCallPckDTO callDuplicaBando(Long idBandoSelezionato,
      Long idBandoObiettivo,
      List<GruppoOggettoDTO> gruppi, String nomeNuovoBando)
      throws InternalUnexpectedException
  {
    StringBuilder listToPass = new StringBuilder("");
    for (GruppoOggettoDTO gruppo : gruppi)
    {
      if (gruppo.isDaImportare())
        for (OggettiIstanzeDTO oggetto : gruppo.getOggetti())
        {
          if (oggetto.isDaImportare())
          {
            listToPass.append(
                String.valueOf(oggetto.getIdLegameGruppoOggetto()) + "!");
            listToPass.append(
                "on".equals(oggetto.getImportDichiarazioni()) ? "S!" : "N!");
            listToPass
                .append("on".equals(oggetto.getImportImpegni()) ? "S!" : "N!");
            listToPass
                .append("on".equals(oggetto.getImportAllegati()) ? "S!" : "N!");
            listToPass.append(
                "on".equals(oggetto.getImportTestiVerbali()) ? "S!" : "N!");
            listToPass.append("#");
          }
        }
    }

    return dao.callDuplicaBando(idBandoSelezionato, idBandoObiettivo,
        listToPass, nomeNuovoBando);
  }

  @Override
  public boolean hasQuadro(Long idBando, long idGruppoOggetto, long idOggetto,
      String codiceQuadro)
      throws InternalUnexpectedException
  {
    return dao.hasQuadro(idBando, idGruppoOggetto, idOggetto, codiceQuadro);
  }

  @Override
  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezione(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoCriteriSelezione(idBando);
  }

  @Override
  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezioneDisponibili(
      long idBando) throws InternalUnexpectedException
  {
    return dao.getElencoCriteriSelezioneDisponibili(idBando);
  }

  @Override
  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezioneSelezionati(
      long idBando) throws InternalUnexpectedException
  {
    return dao.getElencoCriteriSelezioneSelezionati(idBando);
  }

  @Override
  public void updateCriteri(long idBando, List<String> idsCriteri)
      throws InternalUnexpectedException
  {
    List<CriterioDiSelezioneDTO> criteriDB = dao
        .getElencoCriteriSelezione(idBando);

    // elimino gli id che sono stati deselezionati
    if (criteriDB != null && !criteriDB.isEmpty())
    {
      for (CriterioDiSelezioneDTO item : criteriDB)
      {
        if (idsCriteri == null || idsCriteri.isEmpty()
            || !idsCriteri.contains(item.getIdLivelloCriterio().toString()))
        {
          dao.delete("NEMBO_R_BANDO_LIVELLO_CRITERIO",
              "ID_BANDO_LIVELLO_CRITERIO", item.getIdBandoLivelloCriterio());
        }
      }

    }

    // inserisco gli id che non c'erano ancora su db
    if (idsCriteri != null && !idsCriteri.isEmpty())
    {
      boolean insert = true;
      for (String id : idsCriteri)
      {
        insert = true;
        if (criteriDB != null && !criteriDB.isEmpty())
        {
          for (CriterioDiSelezioneDTO item : criteriDB)
          {
            if (id.equals(item.getIdLivelloCriterio().toString()))
            {
              insert = false;
              break;
            }
          }
        }

        if (insert && !GenericValidator.isBlankOrNull(id))
        {
          dao.insertRBandoLivelloCriterio(idBando, Long.valueOf(id));
        }
      }
    }
  }

  public boolean checkCriteriConPunteggio(long idBando, List<String> ids)
      throws InternalUnexpectedException
  {
    for (String item : ids)
    {
      if (dao.checkCriteriConPunteggio(idBando, Long.parseLong(item)))
      {
        return true;
      }
    }
    return false;
  }

  public OggettiIstanzeDTO getParametriDefaultRicevuta(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getParametriDefaultRicevuta(idBandoOggetto);
  }

  @Override
  public List<ElencoQueryBandoDTO> getElencoReport(String extCodAttore)
      throws InternalUnexpectedException
  {
    return dao.getElencoReport(extCodAttore);
  }

  @Override
  public List<ElencoQueryBandoDTO> getElencoGrafici(String extCodAttore)
      throws InternalUnexpectedException
  {
    return dao.getElencoGrafici(extCodAttore);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public byte[] getExcelParametroDiElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException
  {
    return dao.getExcelParametroDiElencoQuery(idElencoQuery);
  }

  @Override
  public EsitoCallPckDTO callDuplicaBandoCopiaOggettiStessoBando(
      Long idBandoOggettoOld, Long idBandoOggettoNuovo,
      int idQuadro) throws InternalUnexpectedException
  {
    return dao.callDuplicaBandoCopiaOggettiStessoBando(idBandoOggettoOld,
        idBandoOggettoNuovo, idQuadro);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public boolean isOggettoDomandaPagamentoGAL(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.isOggettoDomandaPagamentoGAL(idBandoOggetto);
  }

  @Override
  public BigDecimal getBudget(long idBando, long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getBudget(idBando, idLivello);
  }

  @Override
  public long getIdBando(long idBandoOggetto) throws InternalUnexpectedException
  {
    return dao.getIdBando(idBandoOggetto);
  }

  @Override
  public boolean controlloInfoCatalogo(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.controlloInfoCatalogo(idBandoOggetto,
        dao.getIdBando(idBandoOggetto));
  }

  @Override
  public boolean isPrimoOggettoAttivato(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.isPrimoOggettoAttivato(idBandoOggetto);
  }

  @Override
  public BigDecimal getRisorsePubblichePianoFinanziarioGal(long idBando,
      long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getRisorsePubblichePianoFinanziarioGal(idBando, idLivello);
  }

@Override
public int inserisciComuniBando(long idBando, String[] arrayComune) throws InternalUnexpectedException {
	dao.lockBando(idBando);
	for(String istatComune : arrayComune)
	{
		dao.inserisciComuniBando(idBando,istatComune,"N");
	}
	return arrayComune.length;
}

@Override
public List<ComuneDTO> getComuniNonSelezionatiPerBando(String idRegione, String istatProvincia, String flagEstinto,
		String flagEstero, long idBando) throws InternalUnexpectedException {
	return dao.getComuniNonSelezionatiPerBando(idRegione, istatProvincia, flagEstinto, flagEstero, idBando);
}

@Override
public List<ComuneBandoDTO> getListComuniBando(long idBando) throws InternalUnexpectedException {
	List<ComuneBandoDTO> list = dao.getListComuniBando(idBando);
	if(list==null)
	{
		list = new ArrayList<ComuneBandoDTO>();
	}
	return list;
}

@Override
public List<Integer> getListFogliBandoComune(long idBandoComune) throws InternalUnexpectedException {
	List<Integer> list = dao.getListFogliBandoComune(idBandoComune);
	if(list == null)
	{
		list = new ArrayList<Integer>();
	}
	return list;
}

	@Override
	public List<Integer> getFogliValidiByIdBandoComune(long idBandoComune) throws InternalUnexpectedException 
	{
		List<Integer> lista = dao.getFogliValidiByIdBandoComune(idBandoComune);
		if(lista == null)
		{
			lista = new ArrayList<Integer>();
		}
		return lista;
	}

	@Override
	public void inserisciFogliComuneBando(long idBandoComune, Integer[] arrayFogliInseritiInteger)
			throws InternalUnexpectedException 
	{
		long idBando = dao.getBandoComune(idBandoComune);
		dao.lockBando(idBando);
		dao.eliminaFogliBandoComune(new long[]{idBandoComune});
		dao.aggiornaBandoComune(idBandoComune, "S");
		for(Integer foglio : arrayFogliInseritiInteger)
		{
			dao.inserisciFoglioComuneBando(idBandoComune,foglio);
		}
	}

	@Override
	public int inserisciTuttiFogliComuneComune(long idBandoComune) throws InternalUnexpectedException {
		long idBando = dao.getBandoComune(idBandoComune);
		dao.lockBando(idBando);
		dao.eliminaFogliBandoComune(new long[]{idBandoComune});
		return dao.aggiornaBandoComune(idBandoComune, "N");
	}

	@Override
	public int eliminaComuniBando(long idBando, long[] arrayIdBandoComune) throws InternalUnexpectedException {
		dao.lockBando(idBando);
		int n=0;
		dao.eliminaFogliBandoComune(arrayIdBandoComune);
		n += dao.eliminaComuneBando(idBando,arrayIdBandoComune);
		return n;
	}
	
	@Override
	public List<DocumentiRichiestiDTO> getDocumentiRichiesti(long idBando, long idOggetto, long idGruppoOggetto) throws InternalUnexpectedException
	{
		List<DocumentiRichiestiDTO> lista = dao.getDocumentiRichiesti(idBando,idOggetto, idGruppoOggetto);
		if(lista == null)
		{
			lista = new ArrayList<DocumentiRichiestiDTO>();
		}
		return lista;
	}
	
	
	@Override
	public boolean deleteInsertTipoDocRicBandoOgg(long idBando, long idOggetto, long idGruppoOggetto, long[] idsTipoDocRicOggetto) throws InternalUnexpectedException
	{
		dao.lockBando(idBando);
		 
		long nDocumentiNonEliminabili = dao.countNDocRichiestiInseritiBandoOggettoCheDevonoEssereEliminati(idBando, idOggetto, idGruppoOggetto, idsTipoDocRicOggetto);
		long idBandoOggetto = dao.getIdBandoOggetto(idBando, idOggetto, idGruppoOggetto);
		
		if(nDocumentiNonEliminabili > 0L)
		{
			return false;
		}
		else
		{
			dao.deleteTipoDocricBandoOgg(idBandoOggetto);
			if(idsTipoDocRicOggetto != null && idsTipoDocRicOggetto.length > 0)
			{
				dao.insertTipoDocRicBandoOgg(idBandoOggetto, idsTipoDocRicOggetto);
			}
			return true;
		}
	
	}


}
