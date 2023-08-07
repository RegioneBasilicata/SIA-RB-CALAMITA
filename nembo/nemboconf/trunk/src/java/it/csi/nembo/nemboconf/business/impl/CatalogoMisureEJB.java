package it.csi.nembo.nemboconf.business.impl;

import java.util.HashMap;
import java.util.List;
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

import it.csi.nembo.nemboconf.business.ICatalogoMisureEJB;
import it.csi.nembo.nemboconf.dto.CriterioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.PrincipioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.InterventiDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.integration.CatalogoMisureDAO;

@Stateless()
@EJB(name = "java:app/CatalogoMisure", beanInterface = ICatalogoMisureEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class CatalogoMisureEJB extends NemboAbstractEJB<CatalogoMisureDAO>
    implements ICatalogoMisureEJB
{
  private SessionContext sessionContext;

  @Resource
  private void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<BeneficiarioDTO> getBeneficiari(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getBeneficiari(idLivello);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<BeneficiarioDTO> getElencoBeneficiariSelezionabili(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getElencoBeneficiariSelezionabili(idLivello);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<BeneficiarioDTO> getElencoBeneficiariSelezionati(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getElencoBeneficiariSelezionati(idLivello);
  }

  @Override
  public void insertBeneficiari(long idLivello, Vector<String> vctIdBeneficiari)
      throws InternalUnexpectedException, ApplicationException
  {
    /*
     * Il sistema verifica se esiste almeno un record su
     * NEMBO_R_LIV_BANDO_BENEFICIARIO con stesso ID_LIVELLO e
     * EXT_ID_FG_TIPOLOGIA dei record esclusi dalla selezione precedente. Nel
     * caso in cui esista, il sistema emette un messaggio per indicare
     * l’impossibilità di eliminare l’associazione perché già utilizzata ed
     * interrompe l’operazione, esplicitando i beneficiari che non possono
     * essere eliminati.
     */
    String msgError = "";
    List<BeneficiarioDTO> beneList = dao.getBeneficiari(idLivello);
    List<DecodificaDTO<String>> beneAssociatiList = dao
        .getBeneficiariAssociati(idLivello);
    if ((beneList != null && beneList.size() > 0)
        && (beneAssociatiList != null && beneAssociatiList.size() > 0))
    {
      for (BeneficiarioDTO item : beneList)
      {
        if (!vctIdBeneficiari.contains(String.valueOf(item.getIdFgTipologia())))
        {
          for (DecodificaDTO<String> dec : beneAssociatiList)
          {
            if (dec.getId().equals(String.valueOf(item.getIdFgTipologia())))
              msgError = msgError + " " + item.getDescrizione() + "<br/>";
          }
        }
      }
    }
    if (!GenericValidator.isBlankOrNull(msgError))
    {
      throw new ApplicationException(
          "Impossibile eliminare i seguenti beneficiari poichè risultano in uso:<br/>"
              + msgError);
    }

    dao.eliminaBeneficiari(idLivello);
    if (vctIdBeneficiari != null)
    {
      boolean trovato = false;
      for (String idFgTipologia : vctIdBeneficiari)
      {
        trovato = false;
        if (beneAssociatiList != null && beneAssociatiList.size() > 0)
        {
          for (DecodificaDTO<String> dec : beneAssociatiList)
          {
            if (dec.getId().equals(idFgTipologia))
            {
              trovato = true;
            }
          }
        }
        if (!trovato)
        {
          dao.insertBeneficiario(idLivello, Long.parseLong(idFgTipologia));
        }
      }
    }
  }

  @Override
  public void eliminaBeneficiario(long idFgTipologia, long idLivello)
      throws InternalUnexpectedException, ApplicationException
  {
    String msgError = "";
    List<DecodificaDTO<String>> beneAssociatiList = dao
        .getBeneficiariAssociati(idLivello);
    if (beneAssociatiList != null && beneAssociatiList.size() > 0)
    {
      for (DecodificaDTO<String> dec : beneAssociatiList)
      {
        if (dec.getId().equals(String.valueOf(idFgTipologia)))
          msgError = msgError + " "
              + dao.getBeneficiario(idLivello, idFgTipologia).getDescrizione()
              + "<br/>";
      }
    }
    if (!GenericValidator.isBlankOrNull(msgError))
    {
      throw new ApplicationException(
          "Impossibile eliminare i seguenti beneficiari poichè risultano in uso:<br/>"
              + msgError);
    }

    dao.eliminaBeneficiario(idFgTipologia, idLivello);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public TipoOperazioneDTO getAlberaturaOperazione(long idLivello)
      throws InternalUnexpectedException
  {
    TipoOperazioneDTO tipoOperazioneDTO = new TipoOperazioneDTO();

    // mapLivello conterrà CODICE/DESCRIZIONE/ID_LIVELLO_PADRE
    HashMap<String, String> mapLivello = dao.getDettaglioLivello(idLivello);
    tipoOperazioneDTO.setCodTipoOperazione(mapLivello.get("CODICE"));
    tipoOperazioneDTO.setDescrTipoOperazione(mapLivello.get("DESCRIZIONE"));

    mapLivello = dao.getDettaglioLivello(
        Long.parseLong(mapLivello.get("ID_LIVELLO_PADRE")));
    tipoOperazioneDTO.setCodSottomisura(mapLivello.get("CODICE"));
    tipoOperazioneDTO.setDescrSottomisura(mapLivello.get("DESCRIZIONE"));

    mapLivello = dao.getDettaglioLivello(
        Long.parseLong(mapLivello.get("ID_LIVELLO_PADRE")));
    tipoOperazioneDTO.setCodMisura(mapLivello.get("CODICE"));
    tipoOperazioneDTO.setDescrMisura(mapLivello.get("DESCRIZIONE"));

    return tipoOperazioneDTO;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<InterventiDTO> getInterventi(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getInterventi(idLivello);
  }

  @Override
  public void eliminaIntervento(long idLivello, long idDescrizioneIntervento)
      throws InternalUnexpectedException, ApplicationException
  {
    String msgError = "";
    Vector<String> vDescr = new Vector<String>();
    String descr = "";
    List<DecodificaDTO<String>> intAssociatiList = dao
        .getInterventiAssociati(idLivello);
    if (intAssociatiList != null && intAssociatiList.size() > 0)
    {
      for (DecodificaDTO<String> dec : intAssociatiList)
      {
        if (dec.getId().equals(String.valueOf(idDescrizioneIntervento)))
        {
          descr = dao.getIntervento(idLivello, idDescrizioneIntervento)
              .getDescIntervento();
          if (!vDescr.contains(descr))
          {
            vDescr.add(descr);
            msgError = msgError + " " + descr + "<br/>";
          }
        }
      }
    }
    if (!GenericValidator.isBlankOrNull(msgError))
    {
      throw new ApplicationException(
          "Impossibile eliminare i seguenti interventi poichè risultano in uso:<br/>"
              + msgError);
    }

    dao.eliminaIntervento(idLivello, idDescrizioneIntervento);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<InterventiDTO> getElencoInterventiSelezionabili(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getElencoInterventiSelezionabili(idLivello);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<InterventiDTO> getElencoInterventiSelezionati(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getElencoInterventiSelezionati(idLivello);
  }

  @Override
  public void insertInterventi(long idLivello,
      Vector<String> vctIdIDescrIntervento)
      throws InternalUnexpectedException, ApplicationException
  {
    /*
     * Il sistema verifica se esiste almeno un record su
     * NEMBO_R_LIV_BANDO_INTERVENTO con stesso ID_LIVELLO e
     * ID_DESCRIZIONE_INTERVENTO dei record esclusi dalla selezione precedente.
     * Nel caso in cui esista, il sistema emette un messaggio per indicare
     * l’impossibilità di eliminare l’associazione perché già utilizzata ed
     * interrompe l’operazione, esplicitando gli interventi che non possono
     * essere eliminati
     */
    String msgError = "";
    Vector<String> vDescr = new Vector<String>();
    String descr = "";
    List<InterventiDTO> interventiList = dao.getInterventi(idLivello);
    List<DecodificaDTO<String>> intAssociatiList = dao
        .getInterventiAssociati(idLivello);
    if ((interventiList != null && interventiList.size() > 0)
        && (intAssociatiList != null && intAssociatiList.size() > 0))
    {
      for (InterventiDTO item : interventiList)
      {
        if (!vctIdIDescrIntervento
            .contains(String.valueOf(item.getIdDescrizioneIntervento())))
        {
          for (DecodificaDTO<String> dec : intAssociatiList)
          {
            if (dec.getId()
                .equals(String.valueOf(item.getIdDescrizioneIntervento())))
            {

              descr = item.getDescIntervento();
              if (!vDescr.contains(descr))
              {
                vDescr.add(descr);
                msgError = msgError + " " + descr + "<br/>";
              }
            }
          }
        }
      }
    }
    if (!GenericValidator.isBlankOrNull(msgError))
    {
      throw new ApplicationException(
          "Impossibile eliminare i seguenti interventi poichè risultano in uso:<br/>"
              + msgError);
    }

    dao.eliminaInterventi(idLivello);

    if (vctIdIDescrIntervento != null)
    {
      boolean trovato = false;
      for (String idDescrIntervento : vctIdIDescrIntervento)
      {
        trovato = false;
        if (intAssociatiList != null && intAssociatiList.size() > 0)
        {
          for (DecodificaDTO<String> dec : intAssociatiList)
          {
            if (dec.getId().equals(idDescrIntervento))
            {
              trovato = true;
            }
          }
        }
        if (!trovato)
        {
          dao.insertIntervento(idLivello, Long.parseLong(idDescrIntervento));
        }
      }
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FocusAreaDTO> getFocusArea(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getFocusArea(idLivello);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<FocusAreaDTO> getElencoFocusArea(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getElencoFocusArea(idLivello);
  }

  @Override
  public String abbinaFocusArea(long idLivello, String idFaPrincipale,
      String[] idFaSecondari) throws InternalUnexpectedException
  {
    String retMesage = null;
    Long faPrincipaleAttuale = dao.getFocusAreaPrincipale(idLivello);
    dao.deleteFocusAreaSecondarie(idLivello);

    if (faPrincipaleAttuale != null && faPrincipaleAttuale != 0)
    {
      // se esisteva già un abbinamento allora devo vedere se esiste un premio
      // abbinato
      if (dao.isPremioAssegnato(idLivello, faPrincipaleAttuale))
      {
        retMesage = "“La focus area principale non può essere variata perché, in corrispondenza del tipo operazione selezionato, risulta un budget ad essa assegnato.";
        sessionContext.setRollbackOnly();
      }
      else
      {
        dao.deleteFocusAreaPrincipale(idLivello,
            String.valueOf(faPrincipaleAttuale));
        if (idFaPrincipale != null)
        {
          dao.insertFocusAreaPrincipale(idLivello, idFaPrincipale);
        }
      }
    }
    else
    {
      // se non esisteva l'abbinamento allora devo inseririre la nuova focus
      // area
      if (idFaPrincipale != null)
      {
        dao.insertFocusAreaPrincipale(idLivello, idFaPrincipale);
      }
    }

    // inserisco Focus Area secondarie
    if (retMesage == null && idFaSecondari != null)
    {
      for (String idFaSecondaria : idFaSecondari)
      {
        dao.insertFocusAreaSecondaria(idLivello, idFaSecondaria);
      }
    }

    return retMesage;
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public DecodificaDTO<String> getInfoTipoLivello(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getInfoTipoLivello(idLivello);
  }

  @Override
  public List<SettoriDiProduzioneDTO> getSettoriDiProduzione(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getSettoriDiProduzione(idLivello);
  }

  @Override
  public List<SettoriDiProduzioneDTO> getElencoSettoriSelezionabili(
      long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getElencoSettoriSelezionabili(idLivello);

  }

  @Override
  public List<SettoriDiProduzioneDTO> getElencoSettoriSelezionati(
      long idLivello) throws InternalUnexpectedException
  {
    return dao.getElencoSettoriSelezionati(idLivello);

  }

  @Override
  public void insertSettori(long idLivello, Vector<String> vctSettori)
      throws InternalUnexpectedException, ApplicationException
  {

    dao.eliminaSettoriSelezionati(idLivello);// elimino tutti quelli presenti in
                                             // precedenza e li reinserisco in
                                             // seguito uno per uno
    if (vctSettori != null)
      for (String idSettore : vctSettori)
        dao.insertSettoreSelezionato(idLivello, idSettore);
  }

  @Override
  public void eliminaSettore(long idSettore, long idLivello)
      throws InternalUnexpectedException
  {
    dao.eliminaSettoreSelezionato(idLivello, idSettore);
  }

  public List<MisuraDTO> getCatalogoMisure() throws InternalUnexpectedException
  {
    return dao.getCatalogoMisure();
  }

  public List<DecodificaDTO<String>> getElencoMisureNemboconf(
      long idTipologiaLivello) throws InternalUnexpectedException
  {
    return dao.getElencoMisureNemboconf(idTipologiaLivello);
  }

  public List<DecodificaDTO<String>> getElencoSettoriNemboconf()
      throws InternalUnexpectedException
  {
    return dao.getElencoSettoriNemboconf();
  }

  public List<DecodificaDTO<String>> getElencoFocusAreaNemboconf()
      throws InternalUnexpectedException
  {
    return dao.getElencoFocusAreaNemboconf();
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public List<PrincipioDiSelezioneDTO> getCriteriDiSelezione(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getCriteriDiSelezione(idLivello);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public PrincipioDiSelezioneDTO getPrincipioDiSelezioneById(long idLivello,
      long idPrincipioSelezione)
      throws InternalUnexpectedException
  {
    return dao.getPrincipioDiSelezioneById(idLivello, idPrincipioSelezione);
  }

  @Override
  public String eliminaPrincipioDiSelezione(long idPrincipioSelezione)
      throws InternalUnexpectedException
  {
    try
    {
      dao.deleteRLivelloCriterio(idPrincipioSelezione);
      dao.delete("NEMBO_D_CRITERIO", "ID_PRINCIPIO_SELEZIONE",
          idPrincipioSelezione);
      dao.delete("NEMBO_D_PRINCIPIO_SELEZIONE", "ID_PRINCIPIO_SELEZIONE",
          idPrincipioSelezione);
    }
    catch (Exception e)
    {
      context.setRollbackOnly();
      return "ERROR";
    }
    return "OK";
  }

  @Override
  public String updatePrincipioDiSelezione(PrincipioDiSelezioneDTO principio,
      long idLivello) throws InternalUnexpectedException
  {

    try
    {
      if (principio.getIdPrincipioSelezione() != -1)
      {
        dao.deleteRLivelloCriterio(principio.getIdPrincipioSelezione());
        dao.delete("NEMBO_D_CRITERIO", "ID_PRINCIPIO_SELEZIONE",
            principio.getIdPrincipioSelezione());
        dao.delete("NEMBO_D_PRINCIPIO_SELEZIONE", "ID_PRINCIPIO_SELEZIONE",
            principio.getIdPrincipioSelezione());
      }

      Long idNuovoPrincipio = dao.insertPrincipioDiSelezione(principio);
      for (CriterioDiSelezioneDTO criterio : principio.getCriteri())
      {
        Long idCriterioInserito = dao.insertCriteriDiSelezione(criterio,
            idNuovoPrincipio, idLivello);
        dao.insertRCriteriLivello(idCriterioInserito, idLivello);

      }
      return "OK";
    }
    catch (Exception e)
    {
      context.setRollbackOnly();
      return "ERROR"; // children = criterio già associato ad un bando
    }
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public String getMaxCodiceCriterio(long idLivello)
      throws InternalUnexpectedException
  {
    return dao.getMaxCodiceCriterio(idLivello);
  }
}
