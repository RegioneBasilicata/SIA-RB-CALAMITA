package it.csi.nembo.nemboconf.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.csi.nembo.nemboconf.dto.ComuneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.FileAllegatoParametro;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.exception.DatabaseAutomationException;
import it.csi.nembo.nemboconf.exception.InternalException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.integration.annotation.DBQuery;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class BaseDAO
{
  private static final String          THIS_CLASS = BaseDAO.class
      .getSimpleName();
  protected static final Logger        logger     = Logger
      .getLogger(NemboConstants.LOGGIN.LOGGER_NAME + ".integration");
  protected final int                  PASSO      = 500;

  @Autowired
  protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Autowired
  protected ApplicationContext         appContext;

  public BaseDAO()
  {
  }

  public <T> T queryForObject(String query, SqlParameterSource parameters,
      Class<T> objClass, ResultSetExtractor<T> re)
  {
    return namedParameterJdbcTemplate.query(query, parameters, re);
  }

  public <T> T queryForObject(String query, SqlParameterSource parameters,
      Class<T> objClass)
  {
    ResultSetExtractor<T> re = new GenericObjectExtractor<T>(objClass);
    return namedParameterJdbcTemplate.query(query, parameters, re);
  }

  public <T> List<T> queryForList(String query, SqlParameterSource parameters,
      Class<T> objClass)
  {
    ResultSetExtractor<List<T>> re = new GenericListEstractor<T>(objClass);
    return namedParameterJdbcTemplate.query(query, parameters, re);
  }

  public <T> T queryForObject(SqlParameterSource parameters, Class<T> objClass)
      throws DatabaseAutomationException
  {
    DBQuery dbQueryAnnotation = objClass.getAnnotation(DBQuery.class);
    if (dbQueryAnnotation == null)
    {
      throw new DatabaseAutomationException(
          "DBQuery annotation mancante su classe " + objClass.getName());
    }
    ResultSetExtractor<T> re = new GenericObjectExtractor<T>(objClass);
    return namedParameterJdbcTemplate.query(dbQueryAnnotation.value(),
        parameters, re);
  }

  public <T> List<T> queryForList(SqlParameterSource parameters,
      Class<T> objClass) throws DatabaseAutomationException
  {
    DBQuery dbQueryAnnotation = objClass.getAnnotation(DBQuery.class);
    if (dbQueryAnnotation == null)
    {
      throw new DatabaseAutomationException(
          "DBQuery annotation mancante su classe " + objClass.getName());
    }
    ResultSetExtractor<List<T>> re = new GenericListEstractor<T>(objClass);
    return namedParameterJdbcTemplate.query(dbQueryAnnotation.value(),
        parameters, re);
  }

  public boolean update(IPersistent... objUpdate) throws InternalException
  {
    DatabaseUpdater bdu = new DatabaseUpdater(namedParameterJdbcTemplate);
    bdu.update(objUpdate);
    return true;
  }

  public boolean insert(IPersistent... objUpdate) throws InternalException
  {
    DatabaseUpdater bdu = new DatabaseUpdater(namedParameterJdbcTemplate);
    bdu.insert(objUpdate);
    return true;
  }

  public int update(String query, MapSqlParameterSource parameters)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "chiudiUltimoStato";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      return namedParameterJdbcTemplate.update(query, parameters);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, query,
          parameters);
      logInternalUnexpectedException(e,
          "[" + THIS_CLASS + ":: " + THIS_METHOD + "]");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] END.");
      }
    }

  }

  public String getInCondition(String campo, Vector<? extends Number> vId)
  {
    int cicli = vId.size() / PASSO;
    if (vId.size() % PASSO != 0)
      cicli++;

    StringBuffer condition = new StringBuffer(" AND ( ");
    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.size(); i++)
      {
        if (primo)
        {
          condition.append(" " + campo + " IN (" + getIdFromvId(vId, i));
          primo = false;
        }
        else
        {
          condition.append("," + getIdFromvId(vId, i));
        }
      }
      condition.append(")");
    }
    condition.append(")");

    return condition.toString();

  }

  public String getInCondition(String campo, List<? extends Number> vId)
  {
    int cicli = vId.size() / PASSO;
    if (vId.size() % PASSO != 0)
      cicli++;

    StringBuffer condition = new StringBuffer(" AND ( ");
    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.size(); i++)
      {
        if (primo)
        {
          condition.append(" " + campo + " IN (" + getIdFromvId(vId, i));
          primo = false;
        }
        else
        {
          condition.append("," + getIdFromvId(vId, i));
        }
      }
      condition.append(")");
    }
    condition.append(")");

    return condition.toString();

  }
  
  protected String getInCondition(String column, long[] ids)
  {
    int cicli = ids.length / PASSO;
    if (ids.length % PASSO != 0)
      cicli++;

    StringBuffer condition = new StringBuffer(" AND ( ");
    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < ids.length; i++)
      {
        if (primo)
        {
          condition.append(" ").append(column).append(" IN (").append(ids[i]);
          primo = false;
        }
        else
        {
          condition.append(",").append(ids[i]);
        }
      }
      condition.append(")");
    }
    condition.append(")");

    return condition.toString();
  }

  protected String getIdFromvId(List<?> vId, int idx)
  {

    Object o = vId.get(idx);

    if (o instanceof String)
    {
      return "'" + (String) o + "'";
    }
    else
      return o.toString();
  }

  public String getInCondition(String campo, Vector<? extends Number> vId, boolean andClause)
  {
    int cicli = vId.size() / PASSO;
    if (vId.size() % PASSO != 0)
      cicli++;
    StringBuffer condition = new StringBuffer("  ");

    if (andClause)
      condition.append(" AND ( ");

    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.size(); i++)
      {
        if (primo)
        {
          condition.append(" " + campo + " IN (" + getIdFromvId(vId, i));
          primo = false;
        }
        else
        {
          condition.append("," + getIdFromvId(vId, i));
        }
      }
      condition.append(")");
    }

    if (andClause)
      condition.append(")");

    return condition.toString();

  }

  public String getNotInCondition(String campo, Vector<? extends Number> vId)
  {
    int cicli = vId.size() / PASSO;
    if (vId.size() % PASSO != 0)
      cicli++;

    StringBuffer condition = new StringBuffer(" AND ( ");
    for (int j = 0; j < cicli; j++)
    {
      if (j != 0)
      {
        condition.append(" OR ");
      }
      boolean primo = true;
      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.size(); i++)
      {
        if (primo)
        {
          condition.append(" " + campo + " NOT IN (" + getIdFromvId(vId, i));
          primo = false;
        }
        else
        {
          condition.append("," + getIdFromvId(vId, i));
        }
      }
      condition.append(")");
    }
    condition.append(")");
    return condition.toString();
  }

  public String getNotInCondition(String campo, long[] vId)
  {
	    int cicli = vId.length / PASSO;
	    if (vId.length % PASSO != 0)
	      cicli++;

	    StringBuffer condition = new StringBuffer(" AND ( ");
	    for (int j = 0; j < cicli; j++)
	    {
	      if (j != 0)
	      {
	        condition.append(" OR ");
	      }
	      boolean primo = true;
	      for (int i = j * PASSO; i < ((j + 1) * PASSO) && i < vId.length; i++)
	      {
	        if (primo)
	        {
	          condition.append(" " + campo + " NOT IN (" + Long.toString(vId[i]));
	          primo = false;
	        }
	        else
	        {
	          condition.append("," + Long.toString(vId[i]));
	        }
	      }
	      condition.append(")");
	    }
	    condition.append(")");
	    return condition.toString();
  }
  
  protected String getIdFromvId(Vector<?> vId, int idx)
  {

    Object o = vId.get(idx);

    if (o instanceof String)
    {
      return "'" + (String) o + "'";
    }
    else
      if (o instanceof Long)
      {
        return ((Long) o).toString();
      }
      else
        if (o instanceof BigDecimal)
        {
          return ((BigDecimal) o).toString();
        }
        else
        {
          return o.toString();
        }
  }

  public long getNextSequenceValue(String sequenceName)
  {
    String query = " SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
    return namedParameterJdbcTemplate.queryForLong(query,
        (SqlParameterSource) null);
  }

  protected void logInternalUnexpectedException(InternalUnexpectedException e,
      String logHeader)
  {
    logger.error(logHeader
        + " *********************************** INIZIO DUMP ECCEZIONE  ***********************************");
    logger.error(logHeader + " Query:\n" + e.getQuery() + "\n");
    logger.error(
        logHeader + " Parametri del metodo:\n" + e.getParameters() + "\n");
    logger.error(
        logHeader + " Variabili del metodo:\n" + e.getVariables() + "\n");
    logger.error(
        logHeader + " Stacktrace:\n" + e.getExceptionStackTrace() + "\n");
    logger.error(logHeader
        + " ************************************ FINE DUMP ECCEZIONE *************************************");

  }

  public Long getLongNull(ResultSet rs, String name) throws SQLException
  {
    String value = rs.getString(name);
    if (value != null)
    {
      return new Long(value);
    }
    return null;
  }

  public Integer getIntegerNull(ResultSet rs, String name) throws SQLException
  {
    String value = rs.getString(name);
    if (value != null)
    {
      return new Integer(value);
    }
    return null;
  }

  public Long queryForLong(String query,
      MapSqlParameterSource mapParameterSource)
  {
    List<Long> list = namedParameterJdbcTemplate.query(query,
        mapParameterSource, new RowMapper<Long>()
        {

          @Override
          public Long mapRow(ResultSet rs, int index) throws SQLException
          {
            String value = rs.getString(1);
            if (value != null)
            {
              return new Long(value);
            }
            return null;
          }
        });
    return list == null || list.isEmpty() ? null : list.get(0);
  }

  public BigDecimal queryForBigDecimal(String query,
      MapSqlParameterSource mapParameterSource)
  {
    List<BigDecimal> list = namedParameterJdbcTemplate.query(query,
        mapParameterSource, new RowMapper<BigDecimal>()
        {

          @Override
          public BigDecimal mapRow(ResultSet rs, int index) throws SQLException
          {
            return rs.getBigDecimal(1);
          }
        });
    return list == null || list.isEmpty() ? null : list.get(0);
  }

  public int delete(String table, String idName, long idValue)
  {
    StringBuilder query = new StringBuilder("DELETE FROM ").append(table)
        .append(" WHERE ").append(idName).append(" = ").append(idValue);
    return namedParameterJdbcTemplate.update(query.toString(),
        (SqlParameterSource) null);
  }

  public Long select(String table, String idName, long idValue)
  {
    StringBuilder query = new StringBuilder("SELECT ").append(idName)
        .append(" FROM ").append(table).append(" WHERE ").append(idName)
        .append(" = ").append(idValue);
    List<Long> list = namedParameterJdbcTemplate.query(query.toString(),
        (SqlParameterSource) null, new RowMapper<Long>()
        {

          @Override
          public Long mapRow(ResultSet rs, int index) throws SQLException
          {
            return rs.getLong(1);
          }
        });
    return list == null || list.isEmpty() ? null : list.get(0);
  }

  public Date getSysDate() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getSysDate]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT SYSDATE FROM DUAL \n";
    try
    {
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          (MapSqlParameterSource) null, Date.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<String[]> getStatoDatabase() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getStatoDatabase]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                                                 \n"
        + "   DISTINCT OBJECT_NAME,                                                \n"
        + "   STATUS                                                               \n"
        + " FROM                                                                   \n"
        + "   ALL_OBJECTS                                                          \n"
        + " WHERE                                                                  \n"
        + "   OWNER            = 'Nemboconf'                                             \n"
        + "   AND OBJECT_TYPE IN ('PACKAGE','PACKAGE BODY','PROCEDURE','FUNCTION') \n"
        + " ORDER BY                                                               \n"
        + "   OBJECT_NAME ASC                                                      \n";
    try
    {
      return namedParameterJdbcTemplate.query(QUERY,
          (MapSqlParameterSource) null, new ResultSetExtractor<List<String[]>>()
          {

            @Override
            public List<String[]> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<String[]> list = new ArrayList<String[]>();
              while (rs.next())
              {
                list.add(new String[]
                { rs.getString("OBJECT_NAME"), rs.getString("STATUS"),
                    null /*
                          * Verrà riempito dalla controller con il nome
                          * dell'icona da visualizzare
                          */ });
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public List<DecodificaDTO<String>> queryForDecodificaString(String query,
      SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<List<DecodificaDTO<String>>>()
        {
          @Override
          public List<DecodificaDTO<String>> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            List<DecodificaDTO<String>> list = new ArrayList<DecodificaDTO<String>>();
            while (rs.next())
            {
              DecodificaDTO<String> d = new DecodificaDTO<String>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              d.setId(rs.getString("ID"));
              list.add(d);
            }
            return list;
          }
        });
  }

  public DecodificaDTO<String> queryForSingleDecodificaString(String query,
      SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<DecodificaDTO<String>>()
        {
          @Override
          public DecodificaDTO<String> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            if (rs.next())
            {
              DecodificaDTO<String> d = new DecodificaDTO<String>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              d.setId(rs.getString("ID"));
              return d;
            }
            return null;
          }
        });
  }

  public List<DecodificaDTO<Long>> queryForDecodificaLong(String query,
      SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<List<DecodificaDTO<Long>>>()
        {
          @Override
          public List<DecodificaDTO<Long>> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            List<DecodificaDTO<Long>> list = new ArrayList<DecodificaDTO<Long>>();
            while (rs.next())
            {
              DecodificaDTO<Long> d = new DecodificaDTO<Long>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              d.setId(rs.getLong("ID"));
              list.add(d);
            }
            return list;
          }
        });
  }

  public List<DecodificaDTO<Integer>> queryForDecodificaInteger(String query,
      SqlParameterSource parameters)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<List<DecodificaDTO<Integer>>>()
        {
          @Override
          public List<DecodificaDTO<Integer>> extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            List<DecodificaDTO<Integer>> list = new ArrayList<DecodificaDTO<Integer>>();
            while (rs.next())
            {
              DecodificaDTO<Integer> d = new DecodificaDTO<Integer>();
              d.setCodice(rs.getString("CODICE"));
              d.setDescrizione(rs.getString("DESCRIZIONE"));
              d.setId(rs.getInt("ID"));
              list.add(d);
            }
            return list;
          }
        });
  }

  public FileAllegatoParametro getFileAllegatoParametro(String codice)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getFileAllegatoParametro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT CODICE, VALORE, FILE_ALLEGATO FROM NEMBO_D_PARAMETRO P WHERE P.CODICE = :CODICE";
    MapSqlParameterSource params = new MapSqlParameterSource();
    try
    {
      params.addValue("CODICE", codice, Types.VARCHAR);
      return namedParameterJdbcTemplate.query(QUERY, params,
          new ResultSetExtractor<FileAllegatoParametro>()
          {
            @Override
            public FileAllegatoParametro extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              FileAllegatoParametro fileAllegato = null;
              if (rs.next())
              {
                fileAllegato = new FileAllegatoParametro();
                fileAllegato.setCodice(rs.getString("CODICE"));
                fileAllegato.setValore(rs.getString("VALORE"));
                fileAllegato.setFileAllegato(rs.getBytes("FILE_ALLEGATO"));
              }
              return fileAllegato;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          { new LogVariable("codice", codice) }, QUERY,
          (MapSqlParameterSource) params);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public Map<String, String> getParametriComune(String... idParametro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametroComune]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    if (idParametro == null)
    {
      logger.error(
          THIS_METHOD + " idParametro è null! NullPointerException in arrivo!");
    }
    StringBuilder queryBuilder = new StringBuilder(
        "SELECT ID_PARAMETRO, VALORE FROM DB_PARAMETRO WHERE ID_PARAMETRO IN (");
    int i = 1;
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    for (String id : idParametro)
    {
      if (i > 1)
      {
        queryBuilder.append(",");
      }
      String paramName = "PARAMETRO" + i;
      mapParameterSource.addValue(paramName, id, Types.VARCHAR);
      queryBuilder.append(":" + paramName);
      ++i;
    }
    queryBuilder.append(")");
    final String QUERY = queryBuilder.toString();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<String, String>>()
          {
            @Override
            public Map<String, String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, String> map = new HashMap<String, String>();
              while (rs.next())
              {
                map.put(rs.getString("ID_PARAMETRO"), rs.getString("VALORE"));
              }
              return map;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idParametro", idParametro)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public Map<String, String> getParametri(String[] paramNames)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getParametri]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    StringBuilder query = new StringBuilder();
    query.append(
        " SELECT CODICE, VALORE FROM NEMBO_D_PARAMETRO P WHERE P.CODICE IN (");
    boolean needComma = false;
    try
    {
      int len = paramNames.length;
      MapSqlParameterSource params = new MapSqlParameterSource();
      for (int i = 0; i < len; ++i)
      {
        if (needComma)
        {
          query.append(",");
        }
        else
        {
          needComma = true;
        }
        String name = "PARAM_" + i;
        query.append(':').append(name);
        params.addValue(name, paramNames[i], Types.VARCHAR);
      }
      query.append(")");
      return namedParameterJdbcTemplate.query(query.toString(), params,
          new ResultSetExtractor<Map<String, String>>()
          {

            @Override
            public Map<String, String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, String> map = new HashMap<String, String>();
              while (rs.next())
              {
                map.put(rs.getString("CODICE"), rs.getString("VALORE"));
              }
              return map;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, query.toString(), (MapSqlParameterSource) null);
      logInternalUnexpectedException(e, THIS_METHOD + "");
      throw e;
    }
    finally
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(THIS_METHOD + " END.");
      }
    }
  }

  public String queryForString(String query, SqlParameterSource parameters,
      final String field)
  {
    return namedParameterJdbcTemplate.query(query, parameters,
        new ResultSetExtractor<String>()
        {
          @Override
          public String extractData(ResultSet rs)
              throws SQLException, DataAccessException
          {
            String sql = "";
            while (rs.next())
            {
              sql = rs.getString(field);
            }
            return sql;
          }
        });
  }
  
  private String getQueryComuni(String idRegione, String istatProvincia, String flagEstinto, String flagEstero, String extra) {
		StringBuilder queryBuilder = new StringBuilder(
		    " SELECT                       \n"
		        + "   DG.ID_REGIONE,                  \n"
		        + "   DG.DESCRIZIONE_REGIONE,         \n"
		        + "   DG.ISTAT_PROVINCIA,             \n"
		        + "   DG.SIGLA_PROVINCIA,             \n"
		        + "   DG.DESCRIZIONE_PROVINCIA,       \n"
		        + "   DG.ISTAT_COMUNE,                \n"
		        + "   DG.DESCRIZIONE_COMUNE,          \n"
		        + "   DG.CAP,                         \n"
		        + "   DG.FLAG_ESTERO                  \n"
		        + " FROM                              \n"
		        + "   SMRGAA_V_DATI_AMMINISTRATIVI DG \n");
		boolean firstCondition = true;
		if (!GenericValidator.isBlankOrNull(idRegione))
		{
		  queryBuilder.append(" WHERE DG.ID_REGIONE = :ID_REGIONE \n");
		  firstCondition = false;
		}
		if (!GenericValidator.isBlankOrNull(istatProvincia))
		{
		  if (firstCondition)
		  {
		    firstCondition = false;
		    queryBuilder.append(" WHERE \n");
		  }
		  else
		  {
		    queryBuilder.append(" AND ");
		  }
		  queryBuilder.append(" DG.ISTAT_PROVINCIA = :ISTAT_PROVINCIA \n");
		}
		if (!GenericValidator.isBlankOrNull(flagEstinto))
		{
		  if (firstCondition)
		  {
		    firstCondition = false;
		    queryBuilder.append(" WHERE \n");
		  }
		  else
		  {
		    queryBuilder.append(" AND ");
		  }
		  queryBuilder.append(" DG.FLAG_ESTINTO = :FLAG_ESTINTO \n");
		}
		if (!GenericValidator.isBlankOrNull(flagEstero))
		{
		  if (firstCondition)
		  {
		    firstCondition = false;
		    queryBuilder.append(" WHERE \n");
		  }
		  else
		  {
		    queryBuilder.append(" AND ");
		  }
		  queryBuilder.append(" DG.FLAG_ESTERO = :FLAG_ESTERO \n");
		}
		
		if(extra !=null && !extra.equals(""))
		{
			queryBuilder.append(extra);
		}
		
		queryBuilder.append(" ORDER BY DG.DESCRIZIONE_COMUNE ASC");
		final String QUERY = queryBuilder.toString();
		return QUERY;
	}
  
  public List<ComuneDTO> getComuniNonSelezionatiPerBando(String idRegione, String istatProvincia, String flagEstinto, String flagEstero, long idBando)
  	throws InternalUnexpectedException
  {
	    String THIS_METHOD = "[" + THIS_CLASS + "::getComuniNonSelezionatiPerBando]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
		String extra = 
			  " AND DG.ISTAT_COMUNE NOT IN (			\r\n"
			  + " SELECT EXT_ISTAT_COMUNE 				\r\n"
			  + " FROM NEMBO_R_BANDO_COMUNE 			\r\n"
			  + " WHERE ID_BANDO = :ID_BANDO			\r\n"
			  + ")";
	    
	    final String QUERY = getQueryComuni(idRegione, istatProvincia, flagEstinto, flagEstero, extra);
	    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
	    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
	    mapSqlParameterSource.addValue("ISTAT_PROVINCIA", istatProvincia,Types.VARCHAR);
	    mapSqlParameterSource.addValue("FLAG_ESTINTO", flagEstinto, Types.VARCHAR);
	    mapSqlParameterSource.addValue("FLAG_ESTERO", flagEstero, Types.VARCHAR);
	    mapSqlParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
	    try
	    {
	      ((JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations())
	          .setMaxRows(10000);
	      return (List<ComuneDTO>) queryForList(QUERY, mapSqlParameterSource,
	          ComuneDTO.class);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          (LogParameter[]) null,
	          new LogVariable[]
	          {}, QUERY, (MapSqlParameterSource) null);
	      logInternalUnexpectedException(e, THIS_METHOD + "");
	      throw e;
	    }
	    finally
	    {
	      if (logger.isDebugEnabled())
	      {
	        logger.debug(THIS_METHOD + " END.");
	      }
	    }
  }
  
  public List<ComuneDTO> getComuni(String idRegione, String istatProvincia,
	      String flagEstinto, String flagEstero)
	      throws InternalUnexpectedException
	  {
	    String THIS_METHOD = "[" + THIS_CLASS + "::getComuni]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String QUERY = getQueryComuni(idRegione, istatProvincia, flagEstinto, flagEstero, null);
	    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
	    mapSqlParameterSource.addValue("ID_REGIONE", idRegione, Types.VARCHAR);
	    mapSqlParameterSource.addValue("ISTAT_PROVINCIA", istatProvincia,
	        Types.VARCHAR);
	    mapSqlParameterSource.addValue("FLAG_ESTINTO", flagEstinto, Types.VARCHAR);
	    mapSqlParameterSource.addValue("FLAG_ESTERO", flagEstero, Types.VARCHAR);
	    try
	    {
	      ((JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations())
	          .setMaxRows(10000);
	      return (List<ComuneDTO>) queryForList(QUERY, mapSqlParameterSource,
	          ComuneDTO.class);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          (LogParameter[]) null,
	          new LogVariable[]
	          {}, QUERY, (MapSqlParameterSource) null);
	      logInternalUnexpectedException(e, THIS_METHOD + "");
	      throw e;
	    }
	    finally
	    {
	      if (logger.isDebugEnabled())
	      {
	        logger.debug(THIS_METHOD + " END.");
	      }
	    }
	  }



}
