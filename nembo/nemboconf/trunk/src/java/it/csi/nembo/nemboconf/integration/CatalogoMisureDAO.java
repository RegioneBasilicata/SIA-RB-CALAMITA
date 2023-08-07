package it.csi.nembo.nemboconf.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.nembo.nemboconf.dto.CriterioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.PrincipioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.InfoMisurazioneIntervento;
import it.csi.nembo.nemboconf.dto.catalogomisura.InterventiDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.dto.pianofinanziario.GerarchiaLivelloDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.ImportoFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.SottoMisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class CatalogoMisureDAO extends BaseDAO
{

  private static final String THIS_CLASS = CatalogoMisureDAO.class
      .getSimpleName();

  public CatalogoMisureDAO()
  {
  }

  public List<BeneficiarioDTO> getBeneficiari(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getBeneficiari";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                            \n"
          + "   RL.ID_LIVELLO,   	   											\n"
          + "   RL.EXT_ID_FG_TIPOLOGIA AS ID_FG_TIPOLOGIA,   					\n"
          + "   TA.DESC_TIPOLOGIA_AZIENDA,            						\n"
          + "   TA.DESC_FORMA_GIURIDICA      									\n"
          + " FROM 															\n"
          + "   NEMBO_R_LIVELLO_FG_TIPOLOGIA RL,                                \n"
          + "   SMRGAA_V_TIPOLOGIA_AZIENDA TA                                 \n"
          + " WHERE 															\n"
          + "   RL.ID_LIVELLO = :ID_LIVELLO                             		\n"
          + "   AND RL.EXT_ID_FG_TIPOLOGIA = TA.ID_FG_TIPOLOGIA  				\n"
          + " ORDER BY TA.DESC_TIPOLOGIA_AZIENDA , TA.DESC_FORMA_GIURIDICA    \n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource, BeneficiarioDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public BeneficiarioDTO getBeneficiario(long idLivello, long idFgTipologia)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getBeneficiari";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                            	\n"
          + "   RL.ID_LIVELLO,   	   												\n"
          + "   RL.EXT_ID_FG_TIPOLOGIA AS ID_FG_TIPOLOGIA,   						\n"
          + "   TA.DESC_TIPOLOGIA_AZIENDA,            							\n"
          + "   TA.DESC_FORMA_GIURIDICA      										\n"
          + " FROM 																\n"
          + "   NEMBO_R_LIVELLO_FG_TIPOLOGIA RL,                                  	\n"
          + "   SMRGAA_V_TIPOLOGIA_AZIENDA TA                                   	\n"
          + " WHERE 																\n"
          + "   RL.ID_LIVELLO = :ID_LIVELLO                             			\n"
          + "   AND RL.EXT_ID_FG_TIPOLOGIA = :EXT_ID_FG_TIPOLOGIA          		\n"
          + "   AND RL.EXT_ID_FG_TIPOLOGIA = TA.ID_FG_TIPOLOGIA  					\n"
          + " ORDER BY TA.DESC_TIPOLOGIA_AZIENDA , TA.DESC_FORMA_GIURIDICA      	\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      mapParameterSource.addValue("EXT_ID_FG_TIPOLOGIA", idFgTipologia);
      return queryForObject(QUERY, mapParameterSource, BeneficiarioDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello),
              new LogParameter("idFgTipologia", idFgTipologia) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public List<DecodificaDTO<String>> getBeneficiariAssociati(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getBeneficiari";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                            	\n"
          + "   A.EXT_ID_FG_TIPOLOGIA 			\n"
          + " FROM 								\n"
          + "   NEMBO_R_LIV_BANDO_BENEFICIARIO A 	\n"
          + " WHERE 								\n"
          + "   A.ID_LIVELLO = :ID_LIVELLO  		\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DecodificaDTO<String>>>()
          {
            @Override
            public List<DecodificaDTO<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DecodificaDTO<String>> elenco = new Vector<DecodificaDTO<String>>();
              while (rs.next())
              {
                elenco.add(new DecodificaDTO<String>(
                    rs.getString("EXT_ID_FG_TIPOLOGIA")));
              }
              return elenco;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<BeneficiarioDTO> getElencoBeneficiariSelezionabili(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoBeneficiariSelezionabili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                            		\n"
          + "   TA.ID_FG_TIPOLOGIA,            										\n"
          + "   TA.DESC_TIPOLOGIA_AZIENDA,            								\n"
          + "   TA.DESC_FORMA_GIURIDICA      											\n"
          + " FROM 																	\n"
          + "   SMRGAA_V_TIPOLOGIA_AZIENDA TA                                      	\n"
          + " WHERE 																	\n"
          + "   TA.ID_FG_TIPOLOGIA NOT IN                                           	\n"
          + "     ( SELECT A.EXT_ID_FG_TIPOLOGIA FROM NEMBO_R_LIVELLO_FG_TIPOLOGIA A	\n"
          + "		WHERE A.ID_LIVELLO = :ID_LIVELLO)  									\n"
          + "	AND TA.DATA_FINE_FORMA_GIURIDICA IS NULL								\n"
          + " ORDER BY TA.DESC_TIPOLOGIA_AZIENDA , TA.DESC_FORMA_GIURIDICA  			\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource, BeneficiarioDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<BeneficiarioDTO> getElencoBeneficiariSelezionati(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoBeneficiariSelezionati";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                            		\n"
          + "   TA.ID_FG_TIPOLOGIA,            										\n"
          + "   TA.DESC_TIPOLOGIA_AZIENDA,            								\n"
          + "   TA.DESC_FORMA_GIURIDICA      											\n"
          + " FROM 																	\n"
          + "   SMRGAA_V_TIPOLOGIA_AZIENDA TA                                      	\n"
          + " WHERE 																	\n"
          + "   TA.ID_FG_TIPOLOGIA IN                                           		\n"
          + "     ( SELECT A.EXT_ID_FG_TIPOLOGIA FROM NEMBO_R_LIVELLO_FG_TIPOLOGIA A	\n"
          + "		WHERE A.ID_LIVELLO = :ID_LIVELLO)  									\n"
          + " ORDER BY TA.DESC_TIPOLOGIA_AZIENDA , TA.DESC_FORMA_GIURIDICA     		\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource, BeneficiarioDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public void insertBeneficiario(long idLivello, long idFgTipologia)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertBeneficiario";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT                           \n"
          + " INTO                            \n"
          + "   NEMBO_R_LIVELLO_FG_TIPOLOGIA    \n"
          + "   (                             \n"
          + "     ID_LIVELLO,      			\n"
          + "     EXT_ID_FG_TIPOLOGIA    		\n"
          + "   )                             \n"
          + "   VALUES                        \n"
          + "   (                             \n"
          + "     :ID_LIVELLO,      			\n"
          + "     :EXT_ID_FG_TIPOLOGIA    	\n"
          + "   )                           	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_FG_TIPOLOGIA", idFgTipologia,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello),
              new LogParameter("EXT_ID_FG_TIPOLOGIA", idFgTipologia) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public void deleteBeneficiari(long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteBeneficiari";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                           \n"
          + " FROM                            \n"
          + "   NEMBO_R_LIVELLO_FG_TIPOLOGIA    \n"
          + " WHERE                       	\n"
          + "   ID_LIVELLO = :ID_LIVELLO      \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello), }, new LogVariable[]
          {}, DELETE,
          new MapSqlParameterSource());
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

  public void eliminaBeneficiario(long idFgTipologia, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "eliminaBeneficiario";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                          						\n"
          + "	 NEMBO_R_LIVELLO_FG_TIPOLOGIA    						\n"
          + " WHERE 												\n"
          + "	 EXT_ID_FG_TIPOLOGIA = :EXT_ID_FG_TIPOLOGIA 	    \n"
          + "	 AND ID_LIVELLO = :ID_LIVELLO 						\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("EXT_ID_FG_TIPOLOGIA", idFgTipologia,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("EXT_ID_FG_TIPOLOGIA", idFgTipologia),
              new LogParameter("ID_LIVELLO", idLivello) },
          new LogVariable[]
          {}, DELETE, new MapSqlParameterSource());
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

  public HashMap<String, String> getDettaglioLivello(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getDettaglioLivello";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                          \n"
          + "  LP.ID_LIVELLO_PADRE,                         \n"
          + "  DL.ID_LIVELLO,                               \n"
          + "  DL.CODICE,                                   \n"
          + "  DL.DESCRIZIONE                               \n"
          + " FROM                                          \n"
          + "  NEMBO_D_EDIZIONE_Nemboconf DEP,                      \n"
          + "  NEMBO_D_LIVELLO DL,                            \n"
          + "  NEMBO_R_LIVELLO_PADRE LP                       \n"
          + " WHERE                                         \n"
          + "  DEP.ANNO_INIZIO = 2014                       \n"
          + "  AND DEP.ANNO_FINE = 2020                     \n"
          + "  AND DL.ID_EDIZIONE_PSR = DEP.ID_EDIZIONE_PSR \n"
          + "  AND LP.ID_LIVELLO(+) = DL.ID_LIVELLO         \n"
          + "  AND DL.ID_LIVELLO = :ID_LIVELLO              \n"
          + " ORDER BY DL.ORDINAMENTO                       \n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<HashMap<String, String>>()
          {
            @Override
            public HashMap<String, String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              HashMap<String, String> retMap = new HashMap<String, String>();
              while (rs.next())
              {
                retMap.put("CODICE", rs.getString("CODICE"));
                retMap.put("DESCRIZIONE", rs.getString("DESCRIZIONE"));
                retMap.put("ID_LIVELLO_PADRE",
                    rs.getString("ID_LIVELLO_PADRE"));
              }
              return retMap;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<InterventiDTO> getInterventi(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getInterventi";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = "  SELECT                                                             \n"
          + "    A.ID_LIVELLO,                                                  \n"
          + "    A.ID_DESCRIZIONE_INTERVENTO,                                   \n"
          + "    D.DESCRIZIONE DESC_TIPO_INTERVENTO,                            \n"
          + "    B.DESCRIZIONE DESC_INTERVENTO,                                 \n"
          + "    MI.DESCRIZIONE AS DESC_MISURAZIONE,                            \n"
          + "    UM.CODICE AS CODICE_MISURA                                     \n"
          + "  FROM                                                             \n"
          + "    NEMBO_R_LIVELLO_INTERVENTO A,                                    \n"
          + "    NEMBO_D_DESCRIZIONE_INTERVENTO B,                                \n"
          + "    NEMBO_R_AGGREGAZIONE_INTERVENT C,                               \n"
          + "    NEMBO_D_TIPO_AGGREGAZIONE D,                                     \n"
          + "    NEMBO_R_MISURAZIONE_INTERVENTO MI,                               \n"
          + "    NEMBO_D_UNITA_MISURA UM                                          \n"
          + "  WHERE                                                            \n"
          + "    A.ID_LIVELLO = :ID_LIVELLO                                     \n"
          + "    AND B.ID_DESCRIZIONE_INTERVENTO = A.ID_DESCRIZIONE_INTERVENTO  \n"
          + "    AND C.ID_DESCRIZIONE_INTERVENTO = B.ID_DESCRIZIONE_INTERVENTO  \n"
          + "    AND D.ID_TIPO_AGGREGAZIONE = C.ID_TIPO_AGGREGAZIONE_PRIMO_LIV  \n"
          + "    AND C.ID_DESCRIZIONE_INTERVENTO = MI.ID_DESCRIZIONE_INTERVENTO \n"
          + "    AND MI.ID_UNITA_MISURA = UM.ID_UNITA_MISURA(+)                 \n"
          + "  ORDER BY                                                         \n"
          + "    D.DESCRIZIONE ASC,                                             \n"
          + "    B.DESCRIZIONE ASC                                              \n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<InterventiDTO>>()
          {
            @Override
            public List<InterventiDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<InterventiDTO> list = new ArrayList<InterventiDTO>();
              InterventiDTO intervento = null;
              InfoMisurazioneIntervento info = null;
              long idDescrInterventoLast = -1;
              long idDescrIntervento = -1;

              while (rs.next())
              {
                idDescrInterventoLast = rs.getLong("ID_DESCRIZIONE_INTERVENTO");
                if (idDescrIntervento != idDescrInterventoLast)
                {
                  idDescrIntervento = idDescrInterventoLast;
                  intervento = new InterventiDTO();
                  intervento.setIdLivello(rs.getLong("ID_LIVELLO"));
                  intervento.setIdDescrizioneIntervento(
                      rs.getLong("ID_DESCRIZIONE_INTERVENTO"));
                  intervento.setDescIntervento(rs.getString("DESC_INTERVENTO"));
                  intervento.setDescTipoIntervento(
                      rs.getString("DESC_TIPO_INTERVENTO"));
                  intervento.setInfoMisurazioni(
                      new ArrayList<InfoMisurazioneIntervento>());
                  list.add(intervento);
                }

                info = new InfoMisurazioneIntervento();
                info.setDescMisurazione(rs.getString("DESC_MISURAZIONE"));
                info.setCodiceUnitaMisura(rs.getString("CODICE_MISURA"));
                intervento.getInfoMisurazioni().add(info);
              }
              return list;
            }
          });

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public InterventiDTO getIntervento(long idLivello,
      long idDescrizioneintervento)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getIntervento";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                          	\n"
          + "   A.ID_LIVELLO,                                                 \n"
          + "   A.ID_DESCRIZIONE_INTERVENTO,                                  \n"
          + "   D.DESCRIZIONE DESC_TIPO_INTERVENTO,                           \n"
          + "   B.DESCRIZIONE DESC_INTERVENTO                                 \n"
          + " FROM                                                            \n"
          + "   NEMBO_R_LIVELLO_INTERVENTO A,                                   \n"
          + "   NEMBO_D_DESCRIZIONE_INTERVENTO B,                               \n"
          + "   NEMBO_R_AGGREGAZIONE_INTERVENT C,                              \n"
          + "   NEMBO_D_TIPO_AGGREGAZIONE D                                     \n"
          + " WHERE                                                           \n"
          + "   A.ID_LIVELLO = :ID_LIVELLO                                    \n"
          + "   AND A.ID_DESCRIZIONE_INTERVENTO = :ID_DESCRIZIONE_INTERVENTO  \n"
          + "   AND B.ID_DESCRIZIONE_INTERVENTO = A.ID_DESCRIZIONE_INTERVENTO \n"
          + "   AND C.ID_DESCRIZIONE_INTERVENTO = B.ID_DESCRIZIONE_INTERVENTO \n"
          + "   AND D.ID_TIPO_AGGREGAZIONE = C.ID_TIPO_AGGREGAZIONE_PRIMO_LIV \n"
          + " ORDER BY                                                        \n"
          + "   D.DESCRIZIONE ASC,                                            \n"
          + "   B.DESCRIZIONE ASC                                             \n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      mapParameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          idDescrizioneintervento);
      return queryForObject(QUERY, mapParameterSource, InterventiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello),
              new LogParameter("idDescrizioneintervento",
                  idDescrizioneintervento) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public void eliminaIntervento(long idLivello, long idDescrizioneIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "eliminaIntervento";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                          								\n"
          + "	 NEMBO_R_LIVELLO_INTERVENTO    								\n"
          + " WHERE 														\n"
          + "	 ID_LIVELLO = :ID_LIVELLO 									\n"
          + "	 AND ID_DESCRIZIONE_INTERVENTO = :ID_DESCRIZIONE_INTERVENTO \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          idDescrizioneIntervento, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello),
              new LogParameter("ID_DESCRIZIONE_INTERVENTO",
                  idDescrizioneIntervento) },
          new LogVariable[]
          {}, DELETE, new MapSqlParameterSource());
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

  public List<InterventiDTO> getElencoInterventiSelezionabili(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoInterventiSelezionabili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = "  SELECT                                                                      \n"
          + "    C.ID_DESCRIZIONE_INTERVENTO,                                            \n"
          + "    C.ID_TIPO_AGGREGAZIONE,                                                 \n"
          + "    D.DESCRIZIONE DESC_TIPO_INTERVENTO,                                     \n"
          + "    B.DESCRIZIONE DESC_INTERVENTO,                                          \n"
          + "    E.DESCRIZIONE DESC_CATEGORIA                                            \n"
          + "  FROM                                                                      \n"
          + "    NEMBO_D_DESCRIZIONE_INTERVENTO B,                                         \n"
          + "    NEMBO_D_CATEGORIA_INTERVENTO E,                                           \n"
          + "    NEMBO_R_AGGREGAZIONE_INTERVENT C,                                        \n"
          + "    NEMBO_D_TIPO_AGGREGAZIONE D                                               \n"
          + "  WHERE                                                                     \n"
          + "        C.ID_DESCRIZIONE_INTERVENTO = B.ID_DESCRIZIONE_INTERVENTO           \n"
          + "    AND D.ID_TIPO_AGGREGAZIONE = C.ID_TIPO_AGGREGAZIONE_PRIMO_LIV           \n"
          + "    AND E.ID_CATEGORIA_INTERVENTO(+) = B.ID_CATEGORIA_INTERVENTO            \n"
          + "    AND (E.FLAG_ESCLUDI_CATALOGO IS NULL OR E.FLAG_ESCLUDI_CATALOGO <> 'S') \n"
          + "    AND B.ID_DESCRIZIONE_INTERVENTO NOT IN                                  \n"
          + "         ( SELECT Z.ID_DESCRIZIONE_INTERVENTO                               \n"
          + "              FROM NEMBO_R_LIVELLO_INTERVENTO Z                               \n"
          + "            WHERE Z.ID_LIVELLO = :ID_LIVELLO)                               \n"
          + "    AND B.DATA_CESSAZIONE IS NULL                                           \n"
          + "   ORDER BY                                                                 \n"
          + "    B.DESCRIZIONE ASC                                                       \n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource, InterventiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<InterventiDTO> getElencoInterventiSelezionati(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoInterventiSelezionati";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT        												    \n"
          + "  C.ID_DESCRIZIONE_INTERVENTO,                                   \n"
          + "  C.ID_TIPO_AGGREGAZIONE,                                        \n"
          + "   C.ID_AGGREGAZIONE_INTERVENTI AS ID_AGGREZIONE_INTERVENTI,     \n"
          + "   D.DESCRIZIONE AS DESC_TIPO_INTERVENTO,                        \n"
          + "   B.DESCRIZIONE AS DESC_INTERVENTO                              \n"
          + " FROM                                                            \n"
          + "   NEMBO_D_DESCRIZIONE_INTERVENTO B,                               \n"
          + "   NEMBO_R_AGGREGAZIONE_INTERVENT C,                              \n"
          + "   NEMBO_D_TIPO_AGGREGAZIONE D                                     \n"
          + " WHERE                                                           \n"
          + "   C.ID_DESCRIZIONE_INTERVENTO = B.ID_DESCRIZIONE_INTERVENTO 	\n"
          + "   AND D.ID_TIPO_AGGREGAZIONE = C.ID_TIPO_AGGREGAZIONE_PRIMO_LIV \n"
          + "   AND B.ID_DESCRIZIONE_INTERVENTO IN	 						\n"
          + "        ( SELECT Z.ID_DESCRIZIONE_INTERVENTO 					\n"
          + "             FROM NEMBO_R_LIVELLO_INTERVENTO Z                     \n"
          + "           WHERE Z.ID_LIVELLO = :ID_LIVELLO)					  	\n"
          + " ORDER BY                                                        \n"
          + "   B.DESCRIZIONE ASC                                             \n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource, InterventiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public void insertIntervento(long idLivello, long idIDescrIntervento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertIntervento";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT                          	\n"
          + " INTO                            \n"
          + "   NEMBO_R_LIVELLO_INTERVENTO    	\n"
          + "   (                             \n"
          + "     ID_LIVELLO,      			\n"
          + "     ID_DESCRIZIONE_INTERVENTO   \n"
          + "   )                             \n"
          + "   VALUES                        \n"
          + "   (                             \n"
          + "     :ID_LIVELLO,      			\n"
          + "     :ID_DESCRIZIONE_INTERVENTO  \n"
          + "   )                           	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          idIDescrIntervento, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello),
              new LogParameter("ID_DESCRIZIONE_INTERVENTO",
                  idIDescrIntervento) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public List<FocusAreaDTO> getFocusArea(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getFocusArea";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                           	  		\n"
          + "   A.ID_LIVELLO,                                   		\n"
          + "   A.ID_FOCUS_AREA ID_FA_PRINC,                    		\n"
          + "   FA_PRINCIP.CODICE CODICE_FA_PRINC,              		\n"
          + "   FA_PRINCIP.DESCRIZIONE DESCR_FA_PRINC,          		\n"
          + "   B.ID_FOCUS_AREA ID_FA_SECOND,                   		\n"
          + "   FA_SECONDAR.CODICE CODICE_FA_SECOND,            		\n"
          + "   FA_SECONDAR.DESCRIZIONE DESCR_FA_SECOND         		\n"
          + " FROM                                              		\n"
          + "   NEMBO_D_LIV_FOCUS_AREA_PRINCIP A,                 		\n"
          + "   NEMBO_R_LIV_FOCUS_AREA_SECONDA B,                		\n"
          + "   NEMBO_D_FOCUS_AREA FA_PRINCIP,                    		\n"
          + "   NEMBO_D_FOCUS_AREA FA_SECONDAR                    		\n"
          + " WHERE                                             		\n"
          + "   A.ID_LIVELLO = :ID_LIVELLO                      		\n"
          + "   AND A.ID_LIVELLO = B.ID_LIVELLO(+)              		\n"
          + "   AND A.ID_FOCUS_AREA = FA_PRINCIP.ID_FOCUS_AREA  		\n"
          + "   AND B.ID_FOCUS_AREA = FA_SECONDAR.ID_FOCUS_AREA(+) 	\n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<FocusAreaDTO>>()
          {
            @Override
            public List<FocusAreaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<FocusAreaDTO> list = new ArrayList<FocusAreaDTO>();
              FocusAreaDTO faDTO = null;
              while (rs.next())
              {
                if (rs.isFirst())
                {
                  faDTO = new FocusAreaDTO();
                  faDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
                  faDTO.setIdFocusArea(rs.getLong("ID_FA_PRINC"));
                  faDTO.setCodice(rs.getString("CODICE_FA_PRINC"));
                  faDTO.setDescrizione(rs.getString("DESCR_FA_PRINC"));
                  faDTO.setTipo("Principale");
                  list.add(faDTO);
                }
                long idSec = rs.getLong("ID_FA_SECOND");
                if (idSec != 0)
                {
                  faDTO = new FocusAreaDTO();
                  faDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
                  faDTO.setIdFocusArea(idSec);
                  faDTO.setCodice(rs.getString("CODICE_FA_SECOND"));
                  faDTO.setDescrizione(rs.getString("DESCR_FA_SECOND"));
                  faDTO.setTipo("Secondaria");
                  list.add(faDTO);
                }
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<FocusAreaDTO> getElencoFocusArea(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoFocusArea";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                      	\n"
          + "   FA.ID_FOCUS_AREA,                         \n"
          + "   FA.CODICE,                                \n"
          + "   FA.DESCRIZIONE,                           \n"
          + "   A.ID_FOCUS_AREA PRIMARIO,                 \n"
          + "   B.ID_FOCUS_AREA SECONDARIO                \n"
          + " FROM                                        \n"
          + "   NEMBO_D_FOCUS_AREA FA,                      \n"
          + "   NEMBO_D_LIV_FOCUS_AREA_PRINCIP A,           \n"
          + "   NEMBO_R_LIV_FOCUS_AREA_SECONDA B           \n"
          + " WHERE                                       \n"
          + "   FA.ID_FOCUS_AREA = A.ID_FOCUS_AREA(+)     \n"
          + "   AND FA.ID_FOCUS_AREA = B.ID_FOCUS_AREA(+) \n"
          + "   AND A.ID_LIVELLO(+) = :ID_LIVELLO         \n"
          + "   AND B.ID_LIVELLO(+) = :ID_LIVELLO         \n"
          + "ORDER BY FA.CODICE, FA.DESCRIZIONE           \n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<FocusAreaDTO>>()
          {
            @Override
            public List<FocusAreaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<FocusAreaDTO> list = new ArrayList<FocusAreaDTO>();
              FocusAreaDTO faDTO = null;
              while (rs.next())
              {
                faDTO = new FocusAreaDTO();
                faDTO.setIdFocusArea(rs.getLong("ID_FOCUS_AREA"));
                faDTO.setCodice(rs.getString("CODICE"));
                faDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                if (rs.getLong("PRIMARIO") != 0)
                {
                  faDTO.setPrimaria(true);
                }
                else
                  if (rs.getLong("SECONDARIO") != 0)
                  {
                    faDTO.setSecondaria(true);
                  }
                list.add(faDTO);
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public Long getFocusAreaPrincipale(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getFocusAreaPrincipale";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                            	\n"
          + "   A.ID_FOCUS_AREA   	   			\n"
          + " FROM 								\n"
          + "   NEMBO_D_LIV_FOCUS_AREA_PRINCIP A  	\n"
          + " WHERE 								\n"
          + "   A.ID_LIVELLO = :ID_LIVELLO      	\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public void insertFocusAreaPrincipale(long idLivello, String idFaPrincipale)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertFocusAreaPrincipale";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT                          	\n"
          + " INTO                            \n"
          + "   NEMBO_D_LIV_FOCUS_AREA_PRINCIP  \n"
          + "   (                             \n"
          + "     ID_LIVELLO,    				\n"
          + "     ID_FOCUS_AREA      			\n"
          + "   )                             \n"
          + "   VALUES                        \n"
          + "   (                             \n"
          + "     :ID_LIVELLO,   				\n"
          + "     :ID_FOCUS_AREA      		\n"
          + "   )                           	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_FOCUS_AREA", idFaPrincipale,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_FOCUS_AREA", idFaPrincipale),
              new LogParameter("ID_LIVELLO", idLivello) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public void deleteFocusAreaPrincipale(long idLivello, String idFaPrincipale)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteFocusAreaPrincipale";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE FROM                           	\n"
          + "   NEMBO_D_LIV_FOCUS_AREA_PRINCIP  		\n"
          + " WHERE                        			\n"
          + "   ID_LIVELLO = :ID_LIVELLO   			\n"
          + "   AND ID_FOCUS_AREA = :ID_FOCUS_AREA	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_FOCUS_AREA", idFaPrincipale,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_FOCUS_AREA", idFaPrincipale),
              new LogParameter("ID_LIVELLO", idLivello) },
          new LogVariable[]
          {}, DELETE, new MapSqlParameterSource());
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

  public void insertFocusAreaSecondaria(long idLivello, String idFaSecondaria)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertFocusAreaSecondaria";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT                          	\n"
          + " INTO                            \n"
          + "   NEMBO_R_LIV_FOCUS_AREA_SECONDA \n"
          + "   (                             \n"
          + "     ID_LIVELLO,    				\n"
          + "     ID_FOCUS_AREA      			\n"
          + "   )                             \n"
          + "   VALUES                        \n"
          + "   (                             \n"
          + "     :ID_LIVELLO,   				\n"
          + "     :ID_FOCUS_AREA      		\n"
          + "   )                           	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_FOCUS_AREA", idFaSecondaria,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_FOCUS_AREA", idFaSecondaria),
              new LogParameter("ID_LIVELLO", idLivello) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public void deleteFocusAreaSecondarie(long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteFocusAreaSecondarie";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE FROM                           	\n"
          + "   NEMBO_R_LIV_FOCUS_AREA_SECONDA  		\n"
          + " WHERE                        			\n"
          + "   ID_LIVELLO = :ID_LIVELLO   			\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello) }, new LogVariable[]
          {}, DELETE,
          new MapSqlParameterSource());
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

  public boolean isPremioAssegnato(long idLivello, long idFocusArea)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "isPremioAssegnato";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                        		\n"
          + "   A.ID_LIV_FOCUS_AREA_LEADER						\n"
          + " FROM 												\n"
          + "   NEMBO_R_LIV_FOCUS_AREA_LEADER A                    	\n"
          + " WHERE 												\n"
          + "   A.ID_LIV_FOCUS_AREA_LEADER = :ID_FOCUS_AREA    	\n"
          + "	AND A.ID_LIVELLO = :ID_LIVELLO						\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      mapParameterSource.addValue("ID_FOCUS_AREA", idFocusArea);

      Long ret = queryForLong(QUERY, mapParameterSource);
      if (ret != null)
        return true;
      else
        return false;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello),
              new LogParameter("idFocusArea", idFocusArea) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public void eliminaInterventi(long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "eliminaInterventi";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                   							\n"
          + "	 NEMBO_R_LIVELLO_INTERVENTO							\n"
          + " WHERE 												\n"
          + "	 ID_LIVELLO = :ID_LIVELLO 							\n"
          + "	AND ID_DESCRIZIONE_INTERVENTO NOT IN ( 				\n"
          + "    				SELECT A.ID_DESCRIZIONE_INTERVENTO 	\n"
          + " 				FROM 								\n"
          + "   					NEMBO_R_LIV_BANDO_INTERVENTO A    \n"
          + " 				WHERE 								\n"
          + "   					A.ID_LIVELLO = :ID_LIVELLO )    \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello) }, new LogVariable[]
          {}, DELETE,
          new MapSqlParameterSource());
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

  public void eliminaBeneficiari(long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "eliminaBeneficiari";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                          						\n"
          + "	 NEMBO_R_LIVELLO_FG_TIPOLOGIA    						\n"
          + " WHERE 												\n"
          + "	 ID_LIVELLO = :ID_LIVELLO 							\n"
          + "	AND EXT_ID_FG_TIPOLOGIA NOT IN ( 					\n"
          + "    				SELECT A.EXT_ID_FG_TIPOLOGIA 		\n"
          + " 				FROM 								\n"
          + "   					NEMBO_R_LIV_BANDO_BENEFICIARIO A  \n"
          + " 				WHERE 								\n"
          + "   					A.ID_LIVELLO = :ID_LIVELLO )    \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello) }, new LogVariable[]
          {}, DELETE,
          new MapSqlParameterSource());
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

  public List<DecodificaDTO<String>> getInterventiAssociati(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getInterventiAssociati";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                            \n"
          + "   A.ID_DESCRIZIONE_INTERVENTO 	\n"
          + " FROM 							\n"
          + "   NEMBO_R_LIV_BANDO_INTERVENTO A 	\n"
          + " WHERE 							\n"
          + "   A.ID_LIVELLO = :ID_LIVELLO  	\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DecodificaDTO<String>>>()
          {
            @Override
            public List<DecodificaDTO<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DecodificaDTO<String>> elenco = new Vector<DecodificaDTO<String>>();
              while (rs.next())
              {
                elenco.add(new DecodificaDTO<String>(
                    rs.getString("ID_DESCRIZIONE_INTERVENTO")));
              }
              return elenco;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public DecodificaDTO<String> getInfoTipoLivello(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getInfoTipoLivello";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                       						\n"
          + "   A1.CODICE AS ID,              				\n"
          + "   A1.CODICE,                    				\n"
          + "   A1.DESCRIZIONE                				\n"
          + " FROM                         					\n"
          + "   NEMBO_D_TIPO_LIVELLO A1,         				\n"
          + "   NEMBO_D_LIVELLO A2         						\n"
          + " WHERE                        					\n"
          + "  A2.ID_LIVELLO = :ID_LIVELLO   					\n"
          + "  AND A1.ID_TIPO_LIVELLO = A2.ID_TIPO_LIVELLO   	\n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      return queryForSingleDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<SettoriDiProduzioneDTO> getSettoriDiProduzione(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getSettoriDiProduzione";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                         	                    \n"
          + "   SP.ID_SETTORE_PRODUZIONE AS ID_SETTORE,   	   					\n"
          + "   SP.DESCRIZIONE AS DESCRIZIONE, 							  		\n"
          + "   SP.DATA_INIZIO AS DATA_INIZIO,  			          				\n"
          + "   SP.DATA_FINE	AS DATA_FINE			     						\n"
          + " FROM 																\n"
          + "   NEMBO_D_SETTORE_PRODUZIONE SP,                                      \n"
          + "   NEMBO_R_LIVELLO_SETTORE_PROD L                                      \n"
          + " WHERE 															    \n"
          + "   L.ID_LIVELLO = :ID_LIVELLO                            	 		\n"
          + "   AND L.ID_SETTORE_PRODUZIONE = SP.ID_SETTORE_PRODUZIONE			\n"
          + " ORDER BY DESCRIZIONE												\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource,
          SettoriDiProduzioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<SettoriDiProduzioneDTO> getElencoSettoriSelezionabili(
      long idLivello) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoSettoriSelezionabili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                            \n"
          + "   SP.ID_SETTORE_PRODUZIONE AS ID_SETTORE,   	   					\n"
          + "   SP.DESCRIZIONE AS DESCRIZIONE,									\n"
          + "	SP.DATA_INIZIO AS DATA_INIZIO,									\n"
          + "	SP.DATA_FINE AS DATA_FINE 								  		\n"
          + " FROM 																\n"
          + "   NEMBO_D_SETTORE_PRODUZIONE SP                                     \n"
          + " WHERE 															\n"
          + "   SP.DATA_FINE IS NULL                							\n"
          + "	AND NOT EXISTS (												\n"
          + "            SELECT SP.ID_SETTORE_PRODUZIONE,   					\n"
          + "  			   	    SP.DESCRIZIONE,									\n"
          + "		   	    	SP.DATA_INIZIO,									\n"
          + "	   	    		SP.DATA_FINE 									\n"
          + "         	 	FROM 												\n"
          + "					NEMBO_R_LIVELLO_SETTORE_PROD L					\n"
          + "			 	WHERE 												\n"
          + "			   	 L.ID_SETTORE_PRODUZIONE=SP.ID_SETTORE_PRODUZIONE	\n"
          + "				AND 												\n"
          + "					L.ID_LIVELLO=:ID_LIVELLO ) 						\n"
          + "ORDER BY DESCRIZIONE												\n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource,
          SettoriDiProduzioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idLivello", idLivello) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public List<SettoriDiProduzioneDTO> getElencoSettoriSelezionati(
      long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoSettoriSelezionati";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                            \n"
          + "   SP.ID_SETTORE_PRODUZIONE AS ID_SETTORE,   	   				\n"
          + "   SP.DESCRIZIONE AS DESCRIZIONE,								\n"
          + "	  SP.DATA_INIZIO AS DATA_INIZIO,								\n"
          + "	  SP.DATA_FINE AS DATA_FINE							  			\n"
          + " FROM 															\n"
          + "   NEMBO_D_SETTORE_PRODUZIONE SP,                                  \n"
          + "   NEMBO_R_LIVELLO_SETTORE_PROD L                                  \n"
          + " WHERE 															\n"
          + "   L.ID_LIVELLO = :ID_LIVELLO                            	 	\n"
          + "   AND                            	 							\n"
          + "   SP.ID_SETTORE_PRODUZIONE = L.ID_SETTORE_PRODUZIONE	 		\n"
          + "	ORDER BY DESCRIZIONE											\n";
      mapParameterSource.addValue("ID_LIVELLO", idLivello);
      return queryForList(QUERY, mapParameterSource,
          SettoriDiProduzioneDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public void eliminaSettoriSelezionati(long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "eliminaSettoriSelezionati";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                          \n"
          + " FROM                            \n"
          + "   NEMBO_R_LIVELLO_SETTORE_PROD    \n"
          + " WHERE                       	\n"
          + "   ID_LIVELLO = :ID_LIVELLO      \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello), }, new LogVariable[]
          {}, DELETE,
          new MapSqlParameterSource());
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

  public void insertSettoreSelezionato(long idLivello, String idSettore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertSettoreSelezionato";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT  									\n"
          + "   INTO                         			    \n"
          + "   NEMBO_R_LIVELLO_SETTORE_PROD  (ID_LIVELLO,	\n"
          + "		 ID_SETTORE_PRODUZIONE)  				\n"
          + "   VALUES                        			\n"
          + "   (:ID_LIVELLO,      						\n"
          + "     :ID_SETTORE) 							\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_SETTORE", idSettore, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello),
              new LogParameter("ID_SETTORE", idSettore) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public void eliminaSettoreSelezionato(long idLivello, long idSettore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "eliminaSettoreSelezionato";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                          		\n"
          + " FROM                            		\n"
          + "   NEMBO_R_LIVELLO_SETTORE_PROD    		\n"
          + " WHERE                       			\n"
          + "   ID_LIVELLO = :ID_LIVELLO  			\n"
          + " AND										\n"
          + "	   ID_SETTORE_PRODUZIONE = :ID_SETTORE 	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_SETTORE", idSettore, Types.NUMERIC);

      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("ID_LIVELLO", idLivello), }, new LogVariable[]
          {}, DELETE,
          new MapSqlParameterSource());
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

  public List<MisuraDTO> getCatalogoMisure() throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getCatalogoMisure]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = "  SELECT                                                             	\n"
        + "    L.ID_LIVELLO,                                                    	\n"
        + "    L.CODICE,                                                        	\n"
        + "    L.DESCRIZIONE,                                                   	\n"
        + "    L.ID_TIPOLOGIA_LIVELLO,                                          	\n"
        + "    L.ID_SETTORE,                                                    	\n"
        + "   LFA.ID_FOCUS_AREA,                                               	\n"
        + "   LFA.CODICE AS FA_CODICE,                                          	\n"
        + "    LP.ID_LIVELLO_PADRE,							                  	\n"
        + "    S.CODICE AS COD_SETTORE                                          	\n"
        + "  FROM                                                               	\n"
        + "    NEMBO_D_LIVELLO L,                                                 	\n"
        + "    NEMBO_R_LIVELLO_PADRE LP,                                          	\n"
        + "   (                                                                 	\n"
        + "     SELECT                                                          	\n"
        + "       L_FA.ID_LIVELLO,                                              	\n"
        + "       L_FA.ID_FOCUS_AREA,                                           	\n"
        + "       L_FA.IMPORTO,                                                 	\n"
        + "       L_FA.IMPORTO_TRASCINATO,                                      	\n"
        + "       FA.CODICE                                                     	\n"
        + "     FROM                                                            	\n"
        + "       NEMBO_D_FOCUS_AREA FA,                                          	\n"
        + "       NEMBO_R_LIVELLO_FOCUS_AREA L_FA                                 	\n"
        + "     WHERE                                                           	\n"
        + "       L_FA.ID_FOCUS_AREA            = FA.ID_FOCUS_AREA              	\n"
        + "   )                                                                 	\n"
        + "   LFA,                                                              	\n"
        + "    NEMBO_D_SETTORE S                                  					\n"
        + "  WHERE                                              					\n"
        + "    L.ID_SETTORE                  = S.ID_SETTORE(+)  					\n"
        + "    AND L.ID_LIVELLO              = LP.ID_LIVELLO(+) 					\n"
        + "   AND L.ID_LIVELLO                  = LFA.ID_LIVELLO(+)             	\n"
        + "    AND L.ID_EDIZIONE_PSR         = 1                					\n"
        + "  ORDER BY                                           					\n"
        + "    L.ORDINAMENTO,                                    					\n"
        + "   LFA.ID_FOCUS_AREA                                                 	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<MisuraDTO>>()
          {
            @Override
            public List<MisuraDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<MisuraDTO> list = new ArrayList<MisuraDTO>();
              HashMap<Long, GerarchiaLivelloDTO<?>> mapLivelli = new HashMap<Long, GerarchiaLivelloDTO<?>>();
              GerarchiaLivelloDTO<?> livelloCorrente = null;
              GerarchiaLivelloDTO<?> padre = null;
              long idLivello = 0;
              long idLivelloLast = 0;
              while (rs.next())
              {
                idLivelloLast = rs.getLong("ID_LIVELLO");

                if (idLivello != idLivelloLast)
                {
                  idLivello = idLivelloLast;
                  livelloCorrente = mapLivelli.get(idLivello);
                  if (livelloCorrente == null)
                  {
                    int idTipologiaLivello = rs.getInt("ID_TIPOLOGIA_LIVELLO");
                    switch (idTipologiaLivello)
                    {
                      case NemboConstants.LIVELLO.TIPOLOGIA.ID.MISURA:
                        MisuraDTO misuraDTO = new MisuraDTO();
                        livelloCorrente = misuraDTO;
                        list.add(misuraDTO);
                        break;
                      case NemboConstants.LIVELLO.TIPOLOGIA.ID.SOTTOMISURA:
                        livelloCorrente = new SottoMisuraDTO();
                        break;
                      case NemboConstants.LIVELLO.TIPOLOGIA.ID.OPERAZIONE:
                        livelloCorrente = new TipoOperazioneDTO();
                        break;
                    }
                    livelloCorrente
                        .setIdLivelloPadre(getLongNull(rs, "ID_LIVELLO_PADRE"));
                    livelloCorrente.setIdLivello(idLivello);
                    livelloCorrente.setCodice(rs.getString("CODICE"));
                    livelloCorrente.setDescrizione(rs.getString("DESCRIZIONE"));
                    livelloCorrente.setIdTipologiaLivello(
                        rs.getLong("ID_TIPOLOGIA_LIVELLO"));
                    Long idSettore = getLongNull(rs, "ID_SETTORE");
                    // Se id_settore<>NULL DEVE essere un tipo operazione
                    // altrimenti il db è sbagliato
                    if (idSettore != null
                        && livelloCorrente instanceof TipoOperazioneDTO)
                    {
                      TipoOperazioneDTO operazione = (TipoOperazioneDTO) livelloCorrente;
                      operazione.setIdSettore(idSettore);
                      operazione.setCodiceSettore(rs.getString("COD_SETTORE"));
                    }
                    mapLivelli.put(idLivello, livelloCorrente);
                    Long idLivelloPadre = getLongNull(rs, "ID_LIVELLO_PADRE");
                    if (idLivelloPadre != null)
                    {
                      padre = mapLivelli.get(idLivelloPadre);
                      if (padre != null)
                      {
                        if (padre instanceof MisuraDTO
                            && livelloCorrente instanceof SottoMisuraDTO)
                        {
                          ((MisuraDTO) padre).getElenco()
                              .add((SottoMisuraDTO) livelloCorrente);
                        }
                        else
                        {
                          // Il padre (dato che la gerarchia si basa su soli tre
                          // livelli, se non è una Misura
                          // DEVE essere una sottomisura e allora l'elemento
                          // corrente (figlio) DEVE essere una operazione
                          if (padre instanceof SottoMisuraDTO
                              && livelloCorrente instanceof TipoOperazioneDTO)
                          {
                            ((SottoMisuraDTO) padre).getElenco()
                                .add((TipoOperazioneDTO) livelloCorrente);
                          }
                          else
                          {
                            // Non esiste un terzo caso ==> Errore di
                            // configurazione di DB
                            throw new DataRetrievalFailureException(
                                "Errore di gerarchia nei livelli: trovato elemento figlio (id #"
                                    + livelloCorrente.getIdLivello()
                                    + ") con id_tipologia_livello = "
                                    + livelloCorrente.getIdTipologiaLivello() +
                                    " il cui livello padre (id #"
                                    + padre.getIdLivello()
                                    + ") ha id_tipologia_livello = "
                                    + padre.getIdTipologiaLivello());
                          }
                        }
                      }
                    }
                  }
                  Long idFocusArea = getLongNull(rs, "ID_FOCUS_AREA");
                  if (idFocusArea != null)
                  {
                    // Se id_focus_area<>NULL DEVE essere un tipo operazione
                    // altrimenti il db è sbagliato
                    if (livelloCorrente instanceof TipoOperazioneDTO)
                    {
                      TipoOperazioneDTO operazione = (TipoOperazioneDTO) livelloCorrente;
                      ImportoFocusAreaDTO importo = new ImportoFocusAreaDTO();
                      importo.setIdFocusArea(idFocusArea);
                      importo.setCodice(rs.getString("FA_CODICE"));
                      // L'elenco è sicuramente != null in quanto l'oggetto
                      // TipoOperazione crea la lista nel costruttore
                      operazione.getElenco().add(importo);
                    }
                    else
                    {
                      throw new DataRetrievalFailureException(
                          "Errore di gerarchia nei livelli: trovato elemento (id #"
                              + livelloCorrente.getIdLivello()
                              + ") con id_tipologia_livello = "
                              + livelloCorrente.getIdTipologiaLivello()
                              + " che ha dati di focus area/importo");
                    }
                  }

                }
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, null, QUERY, mapParameterSource);
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

  public List<DecodificaDTO<String>> getElencoMisureNemboconf(
      long idTipologiaLivello) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoMisureNemboconf";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                               \n"
          + "   A.ID_LIVELLO AS ID,                                \n"
          + "   A.CODICE AS CODICE,                                \n"
          + "   A.DESCRIZIONE AS DESCRIZIONE                       \n"
          + " FROM                                                 \n"
          + "   NEMBO_D_LIVELLO A                                    \n"
          + " WHERE                                                \n"
          + "   A.ID_EDIZIONE_PSR = 1                              \n"
          + "   AND A.ID_TIPOLOGIA_LIVELLO = :ID_TIPOLOGIA_LIVELLO \n"
          + " ORDER BY A.ORDINAMENTO                               \n";
      mapParameterSource.addValue("ID_TIPOLOGIA_LIVELLO", idTipologiaLivello,
          Types.NUMERIC);
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<DecodificaDTO<String>> getElencoSettoriNemboconf()
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoSettoriNemboconf";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                                                                          \n"
          + "   A.ID_SETTORE AS ID,                                                                                           \n"
          + "   A.CODICE AS CODICE,                                                                                           \n"
          + "   A.DESCRIZIONE AS DESCRIZIONE                                                                                  \n"
          + " FROM                                                                                                            \n"
          + "   NEMBO_D_SETTORE A                                                                                               \n"
          + " WHERE                                                                                                           \n"
          + "   EXISTS (SELECT B.ID_LIVELLO FROM NEMBO_D_LIVELLO B WHERE B.ID_EDIZIONE_PSR = 1 AND B.ID_SETTORE = A.ID_SETTORE) \n"
          + " ORDER BY A.CODICE                                                                                               \n";
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<DecodificaDTO<String>> getElencoFocusAreaNemboconf()
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoFocusAreaNemboconf";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                                                                     \n"
          + "   A.ID_FOCUS_AREA AS ID,                                                                                   \n"
          + "   A.CODICE AS CODICE,                                                                                      \n"
          + "   A.DESCRIZIONE AS DESCRIZIONE                                                                             \n"
          + " FROM                                                                                                       \n"
          + "   NEMBO_D_FOCUS_AREA A                                                                                       \n"
          + " WHERE                                                                                                      \n"
          + "   EXISTS (SELECT B.ID_FOCUS_AREA FROM NEMBO_R_LIVELLO_FOCUS_AREA B WHERE  B.ID_FOCUS_AREA = A.ID_FOCUS_AREA) \n"
          + " ORDER BY A.CODICE                                                                                          \n";
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {}, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public List<PrincipioDiSelezioneDTO> getCriteriDiSelezione(long idLivello)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getCriteriDiSelezione";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                        						\n"
          + "   PS.DESCRIZIONE, 													\n"
          + "	  PS.ORDINE,                										\n"
          + "	  PS.ID_PRINCIPIO_SELEZIONE,   										\n"
          + "   C.CODICE,				                    						\n"
          + "   C.CRITERIO_SELEZIONE,                     						\n"
          + "   C.ID_CRITERIO,                									\n"
          + "   C.SPECIFICHE,			                    						\n"
          + "   C.PUNTEGGIO_MIN,    		                						\n"
          + "   C.PUNTEGGIO_MAX,   		                						\n"
          + "   C.FLAG_ELABORAZIONE,                      						\n"
          + "   C.ORDINE 					                						\n"
          + " FROM                                        						\n"
          + "   NEMBO_D_PRINCIPIO_SELEZIONE PS,             						\n"
          + "   NEMBO_D_CRITERIO C,				            						\n"
          + "   NEMBO_R_LIVELLO_CRITERIO LC		            						\n"
          + " WHERE                                       						\n"
          + "   LC.ID_LIVELLO=:ID_LIVELLO			        						\n"
          + "   AND LC.ID_CRITERIO = C.ID_CRITERIO								\n"
          + "   AND C.ID_PRINCIPIO_SELEZIONE = PS.ID_PRINCIPIO_SELEZIONE          \n"
          + "ORDER BY PS.ORDINE, C.ORDINE     	        					    \n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<PrincipioDiSelezioneDTO>>()
          {
            @Override
            public List<PrincipioDiSelezioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<PrincipioDiSelezioneDTO> list = new ArrayList<PrincipioDiSelezioneDTO>();
              PrincipioDiSelezioneDTO principio = new PrincipioDiSelezioneDTO();
              CriterioDiSelezioneDTO criterioDiSelezione = null;

              Long idPrincipioOld = -1l;
              while (rs.next())
              {

                if (idPrincipioOld != rs.getLong("ID_PRINCIPIO_SELEZIONE"))
                {
                  idPrincipioOld = rs.getLong("ID_PRINCIPIO_SELEZIONE");
                  principio = new PrincipioDiSelezioneDTO();
                  principio.setIdPrincipioSelezione(idPrincipioOld);
                  principio.setDescrizione(rs.getString("DESCRIZIONE"));
                  principio.setCriteri(new ArrayList<CriterioDiSelezioneDTO>());
                  criterioDiSelezione = new CriterioDiSelezioneDTO();
                  list.add(principio);
                }

                criterioDiSelezione = new CriterioDiSelezioneDTO();
                criterioDiSelezione.setCodice(rs.getString("CODICE"));
                criterioDiSelezione
                    .setCriterioDiSelezione(rs.getString("CRITERIO_SELEZIONE"));
                criterioDiSelezione
                    .setFlagElaborazione(rs.getString("FLAG_ELABORAZIONE"));
                criterioDiSelezione
                    .setIdCriterioDiSelezione(rs.getLong("ID_CRITERIO"));
                criterioDiSelezione
                    .setPunteggioMax(rs.getLong("PUNTEGGIO_MAX"));
                criterioDiSelezione
                    .setPunteggioMin(rs.getLong("PUNTEGGIO_MIN"));
                criterioDiSelezione.setSpecifiche(rs.getString("SPECIFICHE"));

                principio.getCriteri().add(criterioDiSelezione);
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public PrincipioDiSelezioneDTO getPrincipioDiSelezioneById(long idLivello,
      long idPrincipioSelezione) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getCriteriDiSelezione";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                        						\n"
          + "   PS.DESCRIZIONE, 													\n"
          + "	  PS.ORDINE,                										\n"
          + "	  PS.ID_PRINCIPIO_SELEZIONE,   										\n"
          + "   C.CODICE,				                    						\n"
          + "   C.CRITERIO_SELEZIONE,                     						\n"
          + "   C.ID_CRITERIO,                									\n"
          + "   C.SPECIFICHE,			                    						\n"
          + "   C.PUNTEGGIO_MIN,    		                						\n"
          + "   C.PUNTEGGIO_MAX,   		                						\n"
          + "   C.FLAG_ELABORAZIONE,                      						\n"
          + "   C.ORDINE 					                						\n"
          + " FROM                                        						\n"
          + "   NEMBO_D_PRINCIPIO_SELEZIONE PS,             						\n"
          + "   NEMBO_D_CRITERIO C,				            						\n"
          + "   NEMBO_R_LIVELLO_CRITERIO LC		            						\n"
          + " WHERE                                       						\n"
          + "   LC.ID_LIVELLO=:ID_LIVELLO			       							\n"
          + "   AND LC.ID_CRITERIO = C.ID_CRITERIO								\n"
          + "   AND C.ID_PRINCIPIO_SELEZIONE = PS.ID_PRINCIPIO_SELEZIONE          \n"
          + "   AND C.ID_PRINCIPIO_SELEZIONE = :ID_PRINCIPIO_SELEZIONE            \n"
          + "ORDER BY PS.ORDINE, C.ORDINE     	        					    \n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_PRINCIPIO_SELEZIONE",
          idPrincipioSelezione, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<PrincipioDiSelezioneDTO>()
          {
            @Override
            public PrincipioDiSelezioneDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              PrincipioDiSelezioneDTO principio = new PrincipioDiSelezioneDTO();
              CriterioDiSelezioneDTO criterioDiSelezione = null;

              Long idPrincipioOld = -1l;
              while (rs.next())
              {

                if (idPrincipioOld != rs.getLong("ID_PRINCIPIO_SELEZIONE"))
                {
                  idPrincipioOld = rs.getLong("ID_PRINCIPIO_SELEZIONE");
                  principio = new PrincipioDiSelezioneDTO();
                  principio.setIdPrincipioSelezione(idPrincipioOld);
                  principio.setDescrizione(rs.getString("DESCRIZIONE"));
                  principio.setCriteri(new ArrayList<CriterioDiSelezioneDTO>());
                  criterioDiSelezione = new CriterioDiSelezioneDTO();
                }

                criterioDiSelezione = new CriterioDiSelezioneDTO();
                criterioDiSelezione.setCodice(rs.getString("CODICE"));
                criterioDiSelezione
                    .setCriterioDiSelezione(rs.getString("CRITERIO_SELEZIONE"));
                criterioDiSelezione
                    .setFlagElaborazione(rs.getString("FLAG_ELABORAZIONE"));
                criterioDiSelezione
                    .setIdCriterioDiSelezione(rs.getLong("ID_CRITERIO"));
                criterioDiSelezione
                    .setPunteggioMax(rs.getLong("PUNTEGGIO_MAX"));
                criterioDiSelezione
                    .setPunteggioMin(rs.getLong("PUNTEGGIO_MIN"));
                criterioDiSelezione.setSpecifiche(rs.getString("SPECIFICHE"));

                principio.getCriteri().add(criterioDiSelezione);
              }
              return principio;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idLivello", idLivello) }, new LogVariable[]
          {}, QUERY,
          mapParameterSource);
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

  public Long insertCriteriDiSelezione(CriterioDiSelezioneDTO criterio,
      long idPrincipioSelezione, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertCriteriDiSelezione";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT                          														\n"
          + " INTO                            													\n"
          + "   NEMBO_D_CRITERIO			    													\n"
          + "   (                             													\n"
          + "     ID_CRITERIO,	   																\n"
          + "     ID_PRINCIPIO_SELEZIONE,															\n"
          + "     CODICE,			   																\n"
          + "     CRITERIO_SELEZIONE,																\n"
          + "     SPECIFICHE,																		\n"
          + "     PUNTEGGIO_MAX,   																\n"
          + "     PUNTEGGIO_MIN,   																\n"
          + "     FLAG_ELABORAZIONE,    															\n"
          + "     ORDINE			    															\n"
          + "   )                             													\n"
          + "   VALUES                        													\n"
          + "   (                             													\n"
          + "     :ID_CRITERIO,  																	\n"
          + "     :ID_PRINCIPIO_SELEZIONE,  														\n"
          + "     :CODICE_CRITERIO,  																\n"
          + "     :DESCRIZIONE,  																	\n"
          + "     :SPECIFICHE,  																	\n"
          + "     :PUNTEGGIO_MAX,  																\n"
          + "     :PUNTEGGIO_MIN,  																\n"
          + "     :FLAG_ELABORAZIONE,    															\n"
          + "     (SELECT NVL(MAX(C.ORDINE)+1,1)													\n"
          + " 		FROM                                        								\n"
          + "   			NEMBO_D_PRINCIPIO_SELEZIONE PS,             								\n"
          + "   			NEMBO_D_CRITERIO C,				            							\n"
          + "   			NEMBO_R_LIVELLO_CRITERIO LC		            							\n"
          + " 		WHERE                                       								\n"
          + "   			LC.ID_LIVELLO=:ID_LIVELLO			        							\n"
          + "   			AND LC.ID_CRITERIO = C.ID_CRITERIO										\n"
          + "   			AND C.ID_PRINCIPIO_SELEZIONE = PS.ID_PRINCIPIO_SELEZIONE          		\n"
          + "			)  																			\n"
          + "   )                           	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      Long idCriterio = getNextSequenceValue("SEQ_NEMBO_D_CRITERIO");
      mapParameterSource.addValue("ID_CRITERIO", idCriterio, Types.NUMERIC);
      mapParameterSource.addValue("CODICE_CRITERIO", criterio.getCodice(),
          Types.VARCHAR);
      mapParameterSource.addValue("DESCRIZIONE",
          criterio.getCriterioDiSelezione(), Types.VARCHAR);
      mapParameterSource.addValue("SPECIFICHE", criterio.getSpecifiche(),
          Types.VARCHAR);
      mapParameterSource.addValue("PUNTEGGIO_MAX", criterio.getPunteggioMax(),
          Types.NUMERIC);
      mapParameterSource.addValue("PUNTEGGIO_MIN", criterio.getPunteggioMin(),
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_ELABORAZIONE",
          criterio.getIdFlagElaborazione(), Types.VARCHAR);
      mapParameterSource.addValue("ID_PRINCIPIO_SELEZIONE",
          idPrincipioSelezione, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idCriterio;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("CODICE_CRITERIO", criterio.getCodice()) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public Long insertPrincipioDiSelezione(PrincipioDiSelezioneDTO principio)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertPrincipioDiSelezione";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT                          	\n"
          + " INTO                            \n"
          + "   NEMBO_D_PRINCIPIO_SELEZIONE	    \n"
          + "   (                             \n"
          + "     ID_PRINCIPIO_SELEZIONE,	 	\n"
          + "     DESCRIZIONE,	   			\n"
          + "     ORDINE		   				\n"
          + "   )                             \n"
          + "   VALUES                        \n"
          + "   (                             \n"
          + "     :ID_PRINCIPIO_SELEZIONE,	\n"
          + "     :DESCRIZIONE,	  			\n"
          + "     (SELECT NVL(MAX(ORDINE)+1,1) FROM NEMBO_D_PRINCIPIO_SELEZIONE)		  				\n"
          + "   )                           	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      Long idPrincipio = getNextSequenceValue(
          "SEQ_NEMBO_D_PRINCIPIO_SELEZION");
      mapParameterSource.addValue("ID_PRINCIPIO_SELEZIONE", idPrincipio,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", principio.getDescrizione(),
          Types.VARCHAR);
      mapParameterSource.addValue("ORDINE", 1, Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idPrincipio;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("CODICE_CRITERIO", principio.getDescrizione()) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public void deleteRLivelloCriterio(Long idPrincipioSelezione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRLivelloCriterio";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      DELETE = " DELETE                          												\n"
          + " FROM                            											\n"
          + "   NEMBO_R_LIVELLO_CRITERIO	    											\n"
          + " WHERE                       												\n"
          + "   ID_CRITERIO IN (SELECT     												\n"
          + "   					CR.ID_CRITERIO										    \n"
          + "   				 FROM 	     												\n"
          + "   				 	NEMBO_R_LIVELLO_CRITERIO 	LC,    							\n"
          + "   				 	NEMBO_D_CRITERIO CR	 	     							\n"
          + "   				 WHERE					 	     							\n"
          + "   				 	CR.ID_PRINCIPIO_SELEZIONE=:idPrincipioSelezione  	   	\n"
          + "   				 	AND CR.ID_CRITERIO=LC.ID_CRITERIO					    \n"
          + "   )																			\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("idPrincipioSelezione", idPrincipioSelezione,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idPrincipioSelezione", idPrincipioSelezione), },
          new LogVariable[]
          {}, DELETE,
          new MapSqlParameterSource());
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

  public void insertRCriteriLivello(Long idCriterioInserito, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRCriteriLivello";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      INSERT = " INSERT                          	\n"
          + " INTO                            \n"
          + "   NEMBO_R_LIVELLO_CRITERIO	    \n"
          + "   (                             \n"
          + "     ID_LIVELLO_CRITERIO,	 	\n"
          + "     ID_LIVELLO,		   			\n"
          + "     ID_CRITERIO	   				\n"
          + "   )                             \n"
          + "   VALUES                        \n"
          + "   (                             \n"
          + "     (SELECT NVL(MAX(id_LIVELLO_CRITERIO)+1,1) from NEMBO_R_LIVELLO_CRITERIO),  		\n"
          + "     :ID_LIVELLO,  				\n"
          + "     :ID_CRITERIO  				\n"
          + "   )                           	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      Long idLivCriterio = getNextSequenceValue(
          "SEQ_NEMBO_D_PRINCIPIO_SELEZION");
      mapParameterSource.addValue("ID_LIVELLO_CRITERIO", idLivCriterio,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_CRITERIO", idCriterioInserito,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idCriterioInserito", idCriterioInserito) },
          new LogVariable[]
          {}, INSERT, new MapSqlParameterSource());
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

  public String getMaxCodiceCriterio(long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getMaxCodiceCriterio]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT "
        + " 'M'||C.CODICE_LIVELLO||'-CR'||MAX(NVL(SUBSTR(a.CODICE,10,2)+1, '01'))||'A' AS CODICE_CRITERIO ,		\n"
        + " A.CODICE AS LAST_COD_ESISTENTE,																		\n"
        + " C.CODICE_LIVELLO  AS COD_LIVELLO	 																\n"
        + "FROM 																								\n"
        + "	NEMBO_D_CRITERIO A,																					\n"
        + " NEMBO_D_PRINCIPIO_SELEZIONE B,																		\n"
        + "	NEMBO_D_LIVELLO C, 																					\n"
        + " NEMBO_R_LIVELLO_CRITERIO LC																			\n"
        + "WHERE 																								\n"
        + " A.ID_PRINCIPIO_SELEZIONE=B.ID_PRINCIPIO_SELEZIONE (+)												\n"
        + " AND C.ID_LIVELLO  = LC.ID_LIVELLO (+)																\n"
        + " AND LC.ID_CRITERIO = A.ID_CRITERIO	(+)																\n"
        + " AND C.ID_LIVELLO = :ID_LIVELLO																		\n"
        + "GROUP BY A.ID_PRINCIPIO_SELEZIONE,A.CRITERIO_SELEZIONE,A.ORDINE,C.CODICE_LIVELLO, A.CODICE			\n"
        + "ORDER BY A.CODICE DESC, A.ID_PRINCIPIO_SELEZIONE,A.CRITERIO_SELEZIONE,A.ORDINE,C.CODICE_LIVELLO		\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                return rs.getString("CODICE_CRITERIO");
              }
              return "";
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idLivello", idLivello)
          },
          new LogVariable[]
          {}, SELECT, mapParameterSource);
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

}
