package it.csi.nembo.nemboconf.business.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.nembo.nemboconf.business.IEconomieEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.ContributoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.EconomiaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.RisorsaAttivataDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.TipoImportoDTO;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.integration.EconomieDAO;

@Stateless()
@EJB(name = "java:app/Economie", beanInterface = IEconomieEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class EconomieiEJB extends NemboAbstractEJB<EconomieDAO>
    implements IEconomieEJB
{
  private SessionContext sessionContext;

  @Resource
  private void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<ContributoDTO> getElencoContributi(long idBando,
      List<Long> idLivelli) throws InternalUnexpectedException
  {
    return dao.getElencoContributi(idBando, idLivelli);
  }

  @Override
  public void updateContributoLivelli(List<ContributoDTO> lContributi)
      throws InternalUnexpectedException
  {
    if (lContributi != null)
    {
      for (ContributoDTO contributo : lContributi)
      {
        dao.updatePercentualiContributoLivello(contributo.getIdBando(),
            contributo.getIdLivello(), contributo.getPercMinima(),
            contributo.getPercMassima(), contributo.getMassimaleSpesa());
      }
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<TipoImportoDTO> getElencoTipiImportoDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    return dao.getElencoTipiImportoDisponibili(idBando);
  }

  @Override
  public void insertTipoFondo(long idBando, long idTipoImporto,
      long idUtenteLogin) throws InternalUnexpectedException
  {
    dao.insertTipoFondo(idBando, idTipoImporto, idUtenteLogin);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<TipoImportoDTO> getElencoFondiByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    List<TipoImportoDTO> importoDTOList = dao.getElencoFondiByIdBando(idBando);
    if (importoDTOList != null && importoDTOList.size() > 0)
    {
      for (TipoImportoDTO item : importoDTOList)
      {
        item.setRisorseAttivateList(
            dao.getElencoRisorse(item.getIdBando(), item.getIdTipoImporto()));
        if (item.getRisorseAttivateList() != null)
        {
          for (RisorsaAttivataDTO ris : item.getRisorseAttivateList())
          {
            ris.setElencoEconomie(
                dao.getelencoEconomie(ris.getIdRisorseLivelloBando()));
          }
        }
      }
    }
    return importoDTOList;
  }

  @Override
  public void updateRisorseTipoImporto(long idBando, long idTipoImporto,
      long idUtenteAggiornamennto, List<RisorsaAttivataDTO> risorse)
      throws InternalUnexpectedException, ApplicationException
  {
    final String THIS_METHOD = "updateRisorseTipoImporto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      dao.lockPianoFinanziario();

      if (risorse != null)
      {
        long idRisorseLivelloBando;
        for (RisorsaAttivataDTO risorsa : risorse)
        {
          Long id = dao.select("NEMBO_T_RISORSE_LIVELLO_BANDO",
              "ID_RISORSE_LIVELLO_BANDO", risorsa.getIdRisorseLivelloBando());
          if (id != null && id.longValue() > 0)
          {
            idRisorseLivelloBando = risorsa.getIdRisorseLivelloBando();
            dao.updateRisorseLivelloBando(risorsa.getIdRisorseLivelloBando(),
                risorsa.getDescrizione(), risorsa.getRisorseAttivate(),
                risorsa.getIdLivello(), risorsa.getDataInizio(),
                risorsa.getDataFine(), risorsa.getFlagBloccato(),
                risorsa.getFlagAmmCompetenza(), risorsa.getRaggruppamento());
          }
          else
          {
            idRisorseLivelloBando = dao.insertRisorseLivelloBando(
                risorsa.getDescrizione(), risorsa.getRisorseAttivate(),
                risorsa.getIdLivello(), risorsa.getDataInizio(),
                risorsa.getDataFine(), risorsa.getFlagBloccato(),
                risorsa.getFlagAmmCompetenza(), idBando, idTipoImporto,
                risorsa.getRaggruppamento());
          }

          dao.delete(" NEMBO_R_RISOR_LIV_BANDO_AMM_CO",
              "ID_RISORSE_LIVELLO_BANDO", idRisorseLivelloBando);
          if ("N".equals(risorsa.getFlagAmmCompetenza())
              && risorsa.getElencoAmmCompetenza() != null)
          {
            for (AmmCompetenzaDTO amm : risorsa.getElencoAmmCompetenza())
            {
              dao.insertRisorsaLivelloBando(idRisorseLivelloBando,
                  amm.getIdAmmCompetenza());
            }
          }
        }

        /*
         * Alla conferma occorre verificare che non vengano
         * inseriti più rek sulla tabella NEMBO_T_RISORSE_LIVELLO_BANDO con
         * DATA_FINE a NULL aventi lo stesso ID_BANDO, ID_TIPO_IMPORTO,
         * ID_LIVELLO, RAGGRUPPAMENTO, e se è presente un rek
         * FLAG_AMM_COMPETENZA = 'S' non possono essercene uno con
         * FLAG_AMM_COMPETENZA = 'N' Se sono presenti più rek con
         * FLAG_AMM_COMPETENZA = 'N' non devono esserci collegati dei rek su
         *  NEMBO_R_RISOR_LIV_BANDO_AMM_CO con EXT_ID_AMM_COMPETENZA doppi
         */

        if (!dao.isFlagAmmCompetenzaCorrette(idBando, idTipoImporto))
        {
          throw new ApplicationException(
              "Non è possibile inserire più di un  fondo attivo per la stessa operazione, specializzazione e amministrazione");
        }

        if (!dao.isAmmCompetenzeCorrette(idBando, idTipoImporto))
        {
          throw new ApplicationException(
              "Non è possibile inserire più di un  fondo attivo per la stessa operazione, specializzazione e amministrazione");
        }

        if (dao.isPresentiRecordDoppi(idBando, idTipoImporto))
        {
          throw new ApplicationException(
              "Non è possibile inserire più di un  fondo attivo per la stessa operazione, specializzazione e amministrazione");
        }

        dao.updateUltimaModificaFondo(idBando, idTipoImporto,
            idUtenteAggiornamennto);
      }
    }
    catch (InternalUnexpectedException e)
    {
      sessionContext.setRollbackOnly();
      throw e;
    }
    catch (ApplicationException e)
    {
      sessionContext.setRollbackOnly();
      throw e;
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e, new LogParameter[]
      { new LogParameter("idBando", idBando) }, new LogVariable[]
      { new LogVariable("idTipoImporto", idTipoImporto) });
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
  public void deleteRisorsa(long idRisorseLivelloBando)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "deleteRisorsa";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      dao.delete(" NEMBO_R_RISOR_LIV_BANDO_AMM_CO", "ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando);
      dao.delete("NEMBO_T_RISORSE_LIVELLO_BANDO", "ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando);
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new InternalUnexpectedException(e, new LogParameter[]
      { new LogParameter("idRisorseLivelloBando", idRisorseLivelloBando) },
          null);
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
  public void inserisciEconomie(long idBando, long idTipoImporto,
      long idUtenteAggiornamennto, long idRisorseLivelloBando,
      List<EconomiaDTO> economie)
      throws InternalUnexpectedException, ApplicationException
  {
    final String THIS_METHOD = "inserisciEconomie";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      dao.delete("NEMBO_T_ECONOMIA", "ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando);
      if (economie != null)
      {
        for (EconomiaDTO economia : economie)
        {
          dao.insertEconomia(economia.getDescrizione(),
              economia.getImportoEconomia(),
              economia.getIdRisorseLivelloBando());
        }
      }

      if (!dao.validateEconomie(idRisorseLivelloBando))
      {
        throw new ApplicationException(
            "Il totale delle economie non può superare l'importo delle risorse attivate sottratto dagli importi già inseriti in liste di liquidazione");
      }

      dao.updateUltimaModificaFondo(idBando, idTipoImporto,
          idUtenteAggiornamennto);
    }
    catch (InternalUnexpectedException e)
    {
      sessionContext.setRollbackOnly();
      throw e;
    }
    catch (ApplicationException e)
    {
      sessionContext.setRollbackOnly();
      throw e;
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
  public void cleanFondiVuoti(long idBando) throws InternalUnexpectedException
  {
    dao.cleanFondiVuoti(idBando);
  }

  @Override
  public BigDecimal getTotaleImportoLiquidatoEconomia(
      long idRisorseLivelloBando) throws InternalUnexpectedException
  {
    BigDecimal totale = BigDecimal.ZERO;
    totale = totale.add(dao.getTotaleImportoLiquidato(idRisorseLivelloBando));
    totale = totale.add(dao.getTotaleEconomie(idRisorseLivelloBando));
    return totale;
  }

  @Override
  public RisorsaAttivataDTO getRisorsa(long idRisorseLivelloBando)
      throws InternalUnexpectedException
  {
    return dao.getRisorsa(idRisorseLivelloBando);
  }

}
