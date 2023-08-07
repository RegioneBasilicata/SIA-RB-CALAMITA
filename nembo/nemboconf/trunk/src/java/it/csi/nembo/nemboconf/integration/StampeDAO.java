package it.csi.nembo.nemboconf.integration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.nembo.nemboconf.dto.cruscottobandi.DettaglioInfoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.dto.stampa.IconaStampa;
import it.csi.nembo.nemboconf.dto.stampa.InfoHeader;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class StampeDAO extends BaseDAO
{
  private static final String THIS_CLASS = StampeDAO.class.getSimpleName();

  public List<String> getElencoCodiciQuadroInOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoCodiciQuadroInOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    final String QUERY = " SELECT                              \n"
        + "   Q.CODICE                                         \n"
        + " FROM                                               \n"
        + "   NEMBO_R_BANDO_OGGETTO_QUADRO BOQ,                  \n"
        + "   NEMBO_R_QUADRO_OGGETTO QO,                         \n"
        + "   NEMBO_D_QUADRO Q                                   \n"
        + " WHERE                                              \n"
        + "   BOQ.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO         \n"
        + "   AND BOQ.ID_QUADRO_OGGETTO = QO.ID_QUADRO_OGGETTO \n"
        + "   AND QO.ID_QUADRO = Q.ID_QUADRO                   \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForList(QUERY, mapParameterSource,
          String.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBandoOggetto", idBandoOggetto) },
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

  public String getDenominazioneBandoByIdBandoOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDenominazioneBandoByIdBandoOggetto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    final String QUERY = " SELECT                         \n"
        + "   B.ANNO_CAMPAGNA || ' - ' || B.DENOMINAZIONE \n"
        + " FROM                                          \n"
        + "   NEMBO_R_BANDO_OGGETTO BO,                     \n"
        + "   NEMBO_D_BANDO B                               \n"
        + " WHERE                                         \n"
        + "   BO.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO     \n"
        + "   AND BO.ID_BANDO = B.ID_BANDO                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.queryForObject(QUERY,
          mapParameterSource, String.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBandoOggetto", idBandoOggetto) },
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

  public InfoHeader getInfoHeader(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getInfoHeader";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    final String QUERY = " SELECT                                                         \n"
        + "   B.ANNO_CAMPAGNA || ' - ' || B.DENOMINAZIONE AS DESC_BANDO,                  \n"
        + "   O.DESCRIZIONE AS DESC_OGGETTO,                                              \n"
        + "   (SELECT                                                                     \n"
        + "       LISTAGG(L.CODICE,' - ')                                                 \n"
        + "       WITHIN GROUP(ORDER BY L.ORDINAMENTO)                                    \n"
        + "    FROM                                                                       \n"
        + "      NEMBO_R_LIVELLO_BANDO LB,                                                  \n"
        + "      NEMBO_D_LIVELLO L                                                          \n"
        + "    WHERE                                                                      \n"
        + "      LB.ID_BANDO = B.ID_BANDO                                                 \n"
        + "      AND LB.ID_LIVELLO = L.ID_LIVELLO                                         \n"
        + "    GROUP BY LB.ID_BANDO                                                       \n"
        + "   ) AS LISTA_MISURE,                                                          \n"
        + "   (SELECT                                                                     \n"
        + "       LISTAGG(AC.DESCRIZIONE,CHR(13)||CHR(10))                                \n"
        + "       WITHIN GROUP(ORDER BY AC.ORDINAMENTO_TIPO_AMMINISTRAZ,  AC.DESCRIZIONE) \n"
        + "    FROM                                                                       \n"
        + "      NEMBO_D_BANDO_AMM_COMPETENZA BAC,                                          \n"
        + "      SMRCOMUNE_V_AMM_COMPETENZA AC                                            \n"
        + "    WHERE                                                                      \n"
        + "      BAC.ID_BANDO = B.ID_BANDO                                                \n"
        + "      AND BAC.EXT_ID_AMM_COMPETENZA = AC.ID_AMM_COMPETENZA                     \n"
        + "    GROUP BY BAC.ID_BANDO                                                      \n"
        + "   ) AS LISTA_AMM_COMPETENZA                                                   \n"
        + " FROM                                                                          \n"
        + "   NEMBO_R_BANDO_OGGETTO BO,                                                     \n"
        + "   NEMBO_D_BANDO B,                                                              \n"
        + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,                                            \n"
        + "   NEMBO_D_OGGETTO O                                                             \n"
        + " WHERE                                                                         \n"
        + "   BO.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                                     \n"
        + "   AND BO.ID_BANDO = B.ID_BANDO                                                \n"
        + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO              \n"
        + "   AND LGO.ID_OGGETTO = O.ID_OGGETTO                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
        Types.NUMERIC);
    try
    {
      return queryForObject(QUERY, mapParameterSource, InfoHeader.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBandoOggetto", idBandoOggetto) },
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

  public List<GruppoInfoDTO> getDichiarazioni(long idBandoOggetto,
      String codice) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDichiarazioni";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    final String QUERY = " SELECT                                            \n"
        + "   G.ID_GRUPPO_INFO,                                              \n"
        + "   G.DESCRIZIONE,                                                 \n"
        + "   D.ID_DETTAGLIO_INFO,                                           \n"
        + "   D.DESCRIZIONE AS DESCRIZIONE_INFO,                             \n"
        + "   D.FLAG_OBBLIGATORIO,                                           \n"
        + "   D.FLAG_GESTIONE_FILE,                                          \n"
        + "   D.EXT_ID_TIPO_DOCUMENTO  AS EXT_ID_TIPO_DOCUMENTO,             \n"
        + "   D.ID_LEGAME_INFO,                                              \n"
        + "   F.TIPO_VINCOLO                                                 \n"
        + " FROM                                                             \n"
        + "   NEMBO_D_GRUPPO_INFO G,                                           \n"
        + "   NEMBO_D_DETTAGLIO_INFO D,                                        \n"
        + "   NEMBO_D_LEGAME_INFO E,                                           \n"
        + "   NEMBO_D_VINCOLO_INFO F,                                          \n"
        + "   NEMBO_R_QUADRO_OGGETTO QO,                                       \n"
        + "   NEMBO_D_QUADRO Q                                                 \n"
        + " WHERE                                                            \n"
        + "   G.ID_GRUPPO_INFO = D.ID_GRUPPO_INFO                            \n"
        + "   AND E.ID_LEGAME_INFO(+) = D.ID_LEGAME_INFO                     \n"
        + "   AND E.ID_VINCOLO_DICHIARAZIONE = F.ID_VINCOLO_DICHIARAZIONE(+) \n"
        + "   AND G.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                     \n"
        + "   AND G.ID_QUADRO_OGGETTO = QO.ID_QUADRO_OGGETTO                 \n"
        + "   AND QO.ID_QUADRO = Q.ID_QUADRO                                 \n"
        + "   AND Q.CODICE = :CODICE                                         \n"
        + " ORDER BY                                                         \n"
        + "   G.ORDINE,                                                      \n"
        + "   D.ORDINE                                                       \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
        Types.NUMERIC);
    mapParameterSource.addValue("CODICE", codice, Types.VARCHAR);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoInfoDTO>>()
          {

            @Override
            public List<GruppoInfoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoInfoDTO> list = new ArrayList<GruppoInfoDTO>();
              Long lastKey = null;
              List<DettaglioInfoDTO> info = null;
              while (rs.next())
              {
                long idGruppoInfo = rs.getLong("ID_GRUPPO_INFO");
                if (lastKey == null || lastKey != idGruppoInfo)
                {
                  GruppoInfoDTO gruppoInfoDTO = null;
                  gruppoInfoDTO = new GruppoInfoDTO();
                  gruppoInfoDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                  gruppoInfoDTO.setIdGruppoInfo(rs.getLong("ID_GRUPPO_INFO"));
                  info = new ArrayList<DettaglioInfoDTO>();
                  gruppoInfoDTO.setElencoDettagliInfo(info);
                  list.add(gruppoInfoDTO);
                  lastKey = idGruppoInfo;
                }

                DettaglioInfoDTO infoDTO = new DettaglioInfoDTO();
                infoDTO.setDescrizione(rs.getString("DESCRIZIONE_INFO"));
                infoDTO.setFlagGestioneFile(rs.getString("FLAG_GESTIONE_FILE"));
                infoDTO.setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                infoDTO.setIdDettaglioInfo(rs.getLong("ID_DETTAGLIO_INFO"));
                infoDTO.setIdLegameInfo(rs.getLong("ID_LEGAME_INFO"));
                infoDTO.setTipoVincolo(rs.getString("TIPO_VINCOLO"));
                info.add(infoDTO);
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBandoOggetto", idBandoOggetto) },
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

  public Map<Long, List<IconaStampa>> getMapCuStampePerBandoOggettoByIdBando(
      long idBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getMapCuStampePerBandoOggettoByIdBando";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    final String QUERY = " SELECT                             \n"
        + "   BO.ID_BANDO_OGGETTO,                                \n"
        + "   EC.CODICE_CDU,                                      \n"
        + "   TD.DESC_TIPO_DOCUMENTO,                             \n"
        + "   I.NOME_ICONA                                        \n"
        + " FROM                                                  \n"
        + "   NEMBO_R_BANDO_OGGETTO BO,                             \n"
        + "   NEMBO_R_BANDO_OGGETTO_ICONA ROI,                      \n"
        + "   NEMBO_R_OGGETTO_ICONA OI,                             \n"
        + "   NEMBO_D_ELENCO_CDU EC,                                \n"
        + "   DOQUIAGRI_V_TIPO_DOC TD,                            \n"
        + "   NEMBO_D_ICONA I,									  \n"
        + "	  NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,					  \n"
        + "   NEMBO_D_OGGETTO O                                     \n"
        + " WHERE                                                 \n"
        + "   BO.ID_BANDO = :ID_BANDO                             \n"
        + "   AND BO.ID_BANDO_OGGETTO = ROI.ID_BANDO_OGGETTO      \n"
        + "   AND ROI.ID_OGGETTO_ICONA = OI.ID_OGGETTO_ICONA      \n"
        + "   AND OI.EXT_ID_TIPO_DOCUMENTO IS NOT NULL            \n"
        + "   AND OI.ID_ELENCO_CDU = EC.ID_ELENCO_CDU             \n"
        + "   AND EC.CODICE_CDU<>'CU-NEMBO-126-I'                 \n"
        + "   AND TD.ID_TIPO_DOCUMENTO = OI.EXT_ID_TIPO_DOCUMENTO \n"
        // non visualizzo stampe di doc che NON sono di tipo istanza
        + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO"
        + "   AND O.ID_OGGETTO = LGO.ID_OGGETTO					  \n"
        + "   AND O.FLAG_ISTANZA = 'S'							  \n"

        + "   AND OI.ID_ICONA = I.ID_ICONA                        \n"
        + " ORDER BY                                              \n"
        + "   OI.ORDINE                                           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<Long, List<IconaStampa>>>()
          {

            @Override
            public Map<Long, List<IconaStampa>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<Long, List<IconaStampa>> map = new HashMap<Long, List<IconaStampa>>();
              while (rs.next())
              {
                long idBandoOggetto = rs.getLong("ID_BANDO_OGGETTO");
                List<IconaStampa> list = map.get(idBandoOggetto);
                if (list == null)
                {
                  list = new ArrayList<IconaStampa>();
                  map.put(idBandoOggetto, list);
                }
                IconaStampa icona = new IconaStampa();
                icona.setCodiceCdu(rs.getString("CODICE_CDU"));
                icona.setDescTipoDocumento(rs.getString("DESC_TIPO_DOCUMENTO"));
                icona.setNomeIcona(rs.getString("NOME_ICONA"));
                list.add(icona);
              }
              return map;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBando", idBando) },
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

  public Map<String, List<String>> getTestiStampeIstruttoria(String codiceCdu,
      long idBandoOggetto)
      throws InternalUnexpectedException
  {
    final String THIS_METHOD = "getTestiStampeIstruttoria";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + "." + THIS_METHOD + " BEGIN.");
    }
    String QUERY = " SELECT                                            \n"
        + "   GTV.TIPO_COLLOCAZIONE_TESTO,                             \n"
        + "   TV.DESCRIZIONE                                           \n"
        + " FROM                                                       \n"
        + "   NEMBO_D_GRUPPO_TESTO_VERBALE GTV,                          \n"
        + "   NEMBO_D_TESTO_VERBALE TV,                                  \n"
        + "   NEMBO_D_ELENCO_CDU EC                                      \n"
        + " WHERE                                                      \n"
        + "   GTV.ID_GRUPPO_TESTO_VERBALE = TV.ID_GRUPPO_TESTO_VERBALE \n"
        + "   AND GTV.ID_ELENCO_CDU = EC.ID_ELENCO_CDU                 \n"
        + "   AND EC.CODICE_CDU = :CODICE_CDU                          \n"
        + "   AND GTV.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO             \n"
        + " ORDER BY                                                   \n"
        + "   GTV.TIPO_COLLOCAZIONE_TESTO,                             \n"
        + "   TV.ORDINE ASC                                            \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("CODICE_CDU", codiceCdu, Types.VARCHAR);
    mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
        Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<String, List<String>>>()
          {
            @Override
            public Map<String, List<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, List<String>> result = new HashMap<String, List<String>>();
              while (rs.next())
              {
                final String tipoCollocazioneTesto = rs
                    .getString("TIPO_COLLOCAZIONE_TESTO");
                List<String> list = result.get(tipoCollocazioneTesto);
                if (list == null)
                {
                  list = new ArrayList<String>();
                  result.put(tipoCollocazioneTesto, list);
                }
                list.add(NemboUtils.STRING
                    .escapeSpecialsChar(rs.getString("DESCRIZIONE")));
              }
              return result;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("codiceCdu", codiceCdu),
              new LogParameter("idBandoOggetto", idBandoOggetto)
          },
          new LogVariable[]
          {
          // new LogParameter("idVariabile", idVariabile),
          }, QUERY, mapParameterSource);
      logInternalUnexpectedException(e, THIS_METHOD);
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

  public Map<String, String> getValoriDefaultSegnaposto()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getValoriDefaultSegnaposto";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    final String QUERY = " SELECT                             \n"
        + "   NOME,		                                		  \n"
        + "   VALORE_DEFAULT                                      \n"
        + " FROM                                                  \n"
        + "   NEMBO_D_SEGNAPOSTO			                          \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<Map<String, String>>()
          {
            @Override
            public Map<String, String> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              Map<String, String> mappa = new HashMap<String, String>();
              while (rs.next())
              {
                mappa.put(rs.getString("NOME"), rs.getString("VALORE_DEFAULT"));
              }
              return mappa;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
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

  public String getCodiceTipoBando(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getCodiceTipoBando]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT 												\n"
        + "	TL.CODICE										\n"
        + "FROM 												\n"
        + "	NEMBO_D_TIPO_LIVELLO TL,							\n"
        + "	NEMBO_D_BANDO B,									\n"
        + "	 NEMBO_R_BANDO_OGGETTO  BO 						\n"
        + "WHERE 												\n"
        + "	BO.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO			\n"
        + "	AND BO.ID_BANDO = B.ID_BANDO					\n"
        + "	AND B.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO		\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
                return rs.getString("CODICE");
              return "";

            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto)
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

  public String getCodiceOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getCodiceOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT 																\n"
        + "	O.CODICE														\n"
        + "FROM 																\n"
        + "	 NEMBO_D_OGGETTO O,												\n"
        + "	 NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,								\n"
        + "	 NEMBO_R_BANDO_OGGETTO  BO 										\n"
        + "WHERE 																\n"
        + "	BO.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO							\n"
        + "	AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO	\n"
        + "	AND LGO.ID_OGGETTO = O.ID_OGGETTO								\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
                return rs.getString("CODICE");
              return "";

            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto)
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