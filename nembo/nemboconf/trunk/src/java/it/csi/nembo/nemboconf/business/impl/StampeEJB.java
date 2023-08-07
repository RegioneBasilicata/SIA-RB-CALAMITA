package it.csi.nembo.nemboconf.business.impl;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import it.csi.nembo.nemboconf.business.IStampeEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.dto.stampa.IconaStampa;
import it.csi.nembo.nemboconf.dto.stampa.InfoHeader;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.integration.StampeDAO;

@Stateless()
@EJB(name = "java:app/Stampe", beanInterface = IStampeEJB.class)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class StampeEJB extends NemboAbstractEJB<StampeDAO>
    implements IStampeEJB
{
  private final String THIS_CLASS = StampeEJB.class.getCanonicalName();

  @Override
  public List<String> getElencoCodiciQuadroInOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getElencoCodiciQuadroInOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getElencoCodiciQuadroInOggetto(idBandoOggetto);
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
  public InfoHeader getInfoHeader(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getInfoHeader";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getInfoHeader(idBandoOggetto);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }

  public List<GruppoInfoDTO> getDichiarazioni(long idBandoOggetto,
      String codice) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getDichiarazioni";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getDichiarazioni(idBandoOggetto, codice);
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
  public Map<Long, List<IconaStampa>> getMapCuStampePerBandoOggettoByIdBando(
      long idBando) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getMapCuStampePerBandoOggettoByIdBando";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getMapCuStampePerBandoOggettoByIdBando(idBando);
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
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public Map<String, List<String>> getTestiStampeIstruttoria(String codiceCdu,
      long idBandoOggetto)
      throws InternalUnexpectedException
  {
    Map<String, List<String>> testi = dao.getTestiStampeIstruttoria(codiceCdu,
        idBandoOggetto);
    Map<String, String> valoriDefault = dao.getValoriDefaultSegnaposto();

    for (Map.Entry<String, String> entry : valoriDefault.entrySet())
    {

      String segnaposto = entry.getKey();
      String defaultValue = entry.getValue();

      for (Map.Entry<String, List<String>> testo : testi.entrySet())
      {

        List<String> listaTesti = testo.getValue();

        ListIterator<String> iter = listaTesti.listIterator();
        while (iter.hasNext())
        {
          String s = iter.next();
          if (s.contains(segnaposto))
          {
            if (defaultValue == null)
              defaultValue = "";
            s = s.replace(segnaposto, defaultValue);
            iter.set(s);
          }
        }
      }
    }

    return testi;
  }

  @Override
  public String getCodiceTipoBando(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getCodiceTipoBando(idBandoOggetto);
  }

  @Override
  public String getCodiceOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    return dao.getCodiceOggetto(idBandoOggetto);
  }
}
