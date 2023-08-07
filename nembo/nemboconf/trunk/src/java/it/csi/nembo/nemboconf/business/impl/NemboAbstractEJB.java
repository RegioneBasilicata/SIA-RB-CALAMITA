package it.csi.nembo.nemboconf.business.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.csi.nembo.nemboconf.business.INemboAbstractEJB;
import it.csi.nembo.nemboconf.dto.ComuneDTO;
import it.csi.nembo.nemboconf.dto.FileAllegatoParametro;
import it.csi.nembo.nemboconf.exception.DatabaseAutomationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.integration.BaseDAO;
import it.csi.nembo.nemboconf.util.NemboConstants;

public abstract class NemboAbstractEJB<TDAO extends BaseDAO>
    implements INemboAbstractEJB
{
  final String                  THIS_CLASS = NemboAbstractEJB.class
      .getSimpleName();
  protected static final Logger logger     = Logger
      .getLogger(NemboConstants.LOGGIN.LOGGER_NAME + ".business");
  @Resource
  protected EJBContext          context;
  protected TDAO                dao;

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public <T> T queryForObject(SqlParameterSource parameters, Class<T> objClass)
      throws DatabaseAutomationException
  {
    return dao.queryForObject(parameters, objClass);
  }

  @Override
  @TransactionAttribute(value = TransactionAttributeType.SUPPORTS)
  public <T> List<T> queryForList(SqlParameterSource parameters,
      Class<T> objClass) throws DatabaseAutomationException
  {
    return dao.queryForList(parameters, objClass);
  }

  @Autowired
  public void setDao(TDAO dao)
  {
    this.dao = dao;
  }

  @Override
  public Date getSysDate() throws InternalUnexpectedException
  {
    return dao.getSysDate();
  }

  @Override
  public List<String[]> getStatoDatabase() throws InternalUnexpectedException
  {
    return dao.getStatoDatabase();
  }

  public FileAllegatoParametro getFileAllegatoParametro(String codice)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getFileAllegatoParametro";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getFileAllegatoParametro(codice);
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
  public Map<String, String> getParametriComune(String... idParametro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametriComune]";
    final boolean debugLevel = logger.isDebugEnabled();
    if (debugLevel)
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getParametriComune(idParametro);
    }
    finally
    {
      if (debugLevel)
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  @Override
  public Map<String, String> getParametri(String[] paramNames)
      throws InternalUnexpectedException
  {
    return dao.getParametri(paramNames);
  }
  
  @Override
  public List<ComuneDTO> getComuni(String idRegione, String istatProvincia,
      String flagEstinto, String flagEstero) throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getComuni";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    try
    {
      return dao.getComuni(idRegione, istatProvincia, flagEstinto, flagEstero);
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " END.");
      }
    }
  }
}