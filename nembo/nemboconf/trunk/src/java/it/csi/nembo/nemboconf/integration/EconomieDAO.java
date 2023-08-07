package it.csi.nembo.nemboconf.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.ContributoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.EconomiaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.RisorsaAttivataDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.TipoImportoDTO;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;

public class EconomieDAO extends BaseDAO
{

  private static final String THIS_CLASS = EconomieDAO.class.getSimpleName();

  public EconomieDAO()
  {
  }

  public List<ContributoDTO> getElencoContributi(long idBando,
      List<Long> idLivelli) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoContributi";
    StringBuilder SELECT = new StringBuilder();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT.append(" SELECT                                             \n"
          + "   DL.ID_LIVELLO,                                     \n"
          + "   LB.ID_BANDO,                                       \n"
          + "   DL.CODICE AS COD_LIVELLO,                  		   \n"
          + "   DL.DESCRIZIONE AS DESCR_LIVELLO,                   \n"
          + "   LB.PERCENTUALE_CONTRIBUTO_MINIMA AS PERC_MINIMA,   \n"
          + "   LB.PERCENTUALE_CONTRIBUTO_MASSIMA AS PERC_MASSIMA, \n"
          + "   LB.MASSIMALE_SPESA		                           \n"
          + " FROM                                                 \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,                            \n"
          + "   NEMBO_D_LIVELLO DL                                   \n"
          + " WHERE                                                \n"
          + "   LB.ID_BANDO = :ID_BANDO                            \n"
          + "   AND LB.ID_LIVELLO = DL.ID_LIVELLO                  \n");
      if (idLivelli != null && idLivelli.size() > 0)
      {
        Vector<Long> vIds = new Vector<Long>();
        for (Long id : idLivelli)
        {
          vIds.add(id);
        }
        SELECT.append(" " + getInCondition("DL.ID_LIVELLO", vIds));
      }
      SELECT.append(" ORDER BY DL.ORDINAMENTO           \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return queryForList(SELECT.toString(), parameterSource,
          ContributoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT.toString());
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

  public void updatePercentualiContributoLivello(long idBando, long idLIvello,
      BigDecimal percMinima, BigDecimal percMassima, BigDecimal massimaleSpesa)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updatePercentualiContributoLivello";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                                                \n"
          + "   NEMBO_R_LIVELLO_BANDO L                                               \n"
          + " SET                                                                   \n"
          + "   L.PERCENTUALE_CONTRIBUTO_MINIMA = :PERCENTUALE_CONTRIBUTO_MINIMA,   \n"
          + "   L.PERCENTUALE_CONTRIBUTO_MASSIMA = :PERCENTUALE_CONTRIBUTO_MASSIMA, \n"
          + "   L.MASSIMALE_SPESA = :MASSIMALE_SPESA                                \n"
          + " WHERE                                                                 \n"
          + "   L.ID_LIVELLO = :ID_LIVELLO                                          \n"
          + "   AND L.ID_BANDO = :ID_BANDO                                          \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLIvello, Types.NUMERIC);
      mapParameterSource.addValue("PERCENTUALE_CONTRIBUTO_MINIMA", percMinima,
          Types.NUMERIC);
      mapParameterSource.addValue("PERCENTUALE_CONTRIBUTO_MASSIMA", percMassima,
          Types.NUMERIC);
      mapParameterSource.addValue("MASSIMALE_SPESA", massimaleSpesa,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_LIVELLO", idLIvello),
              new LogParameter("PERCENTUALE_CONTRIBUTO_MINIMA", percMinima),
              new LogParameter("PERCENTUALE_CONTRIBUTO_MASSIMA", percMassima),
              new LogParameter("MASSIMALE_SPESA", massimaleSpesa)
          },
          new LogVariable[]
          {}, UPDATE, new MapSqlParameterSource());
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

  public List<TipoImportoDTO> getElencoTipiImportoDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoTipiImportoDisponibili";
    StringBuilder SELECT = new StringBuilder();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT.append(" SELECT                             \n"
          + "   IM.ID_TIPO_IMPORTO,              \n"
          + "   IM.DESCRIZIONE,                  \n"
          + "   IM.FONDO_PAGAMENTO               \n"
          + " FROM                               \n"
          + "   NEMBO_D_TIPO_IMPORTO IM            \n"
          + " WHERE                              \n"
          + "   IM.ID_TIPO_IMPORTO NOT IN (      \n"
          + "       SELECT F.ID_TIPO_IMPORTO     \n"
          + "       FROM NEMBO_R_FONDO F           \n"
          + "       WHERE F.ID_BANDO = :ID_BANDO \n"
          + "   )                                \n"
          + " ORDER BY IM.DESCRIZIONE            \n");
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return queryForList(SELECT.toString(), parameterSource,
          TipoImportoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT.toString());
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

  public void insertTipoFondo(long idBando, long idTipoImporto,
      long idUtenteLogin) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertTipoFondo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT INTO                    \n"
          + "   NEMBO_R_FONDO                  \n"
          + " (                              \n"
          + "   ID_BANDO,                    \n"
          + "   ID_TIPO_IMPORTO,             \n"
          + "   EXT_ID_UTENTE_AGGIORNAMENTO, \n"
          + "   DATA_ULTIMO_AGGIORNAMENTO    \n"
          + " )                              \n"
          + " VALUES(                        \n"
          + "   :ID_BANDO,                   \n"
          + "   :ID_TIPO_IMPORTO,            \n"
          + "   :ID_UTENTE_AGGIORNAMENTO,    \n"
          + "   SYSDATE                      \n"
          + " )                              \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_UTENTE_AGGIORNAMENTO", idUtenteLogin,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBando", idBando),
              new LogParameter("idUtenteLogin", idUtenteLogin),
              new LogParameter("idTipoImporto", idTipoImporto) },
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

  public List<TipoImportoDTO> getElencoFondiByIdBando(long idBando)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoFondiByIdBando";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                      \n"
          + "   I.ID_TIPO_IMPORTO,                                        \n"
          + "   I.DESCRIZIONE,                                            \n"
          + "   I.FONDO_PAGAMENTO,                                        \n"
          + "   F.EXT_ID_UTENTE_AGGIORNAMENTO AS ID_UTENTE_AGGIORNAMENTO, \n"
          + "   F.DATA_ULTIMO_AGGIORNAMENTO ,                             \n"
          + "   F.ID_BANDO				                                  \n"
          + " FROM                                                        \n"
          + "   NEMBO_R_FONDO F,                                            \n"
          + "   NEMBO_D_TIPO_IMPORTO I                                      \n"
          + " WHERE                                                       \n"
          + "   F.ID_BANDO = :ID_BANDO                                    \n"
          + "   AND F.ID_TIPO_IMPORTO = I.ID_TIPO_IMPORTO                 \n";

      mapParameterSource.addValue("ID_BANDO", idBando);
      return queryForList(QUERY, mapParameterSource, TipoImportoDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBando", idBando) }, new LogVariable[]
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

  public List<RisorsaAttivataDTO> getElencoRisorse(long idBando,
      long idTipoImporto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoRisorse";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                                              \n"
          + "   L.ID_RISORSE_LIVELLO_BANDO,                                                       \n"
          + "   L.DESCRIZIONE,                                                                    \n"
          + "   L.RISORSE_ATTIVATE,                                                               \n"
          + "   L.DATA_INIZIO,                                                                    \n"
          + "   L.DATA_FINE,                                                                      \n"
          + "   L.FLAG_BLOCCATO,                                                                  \n"
          + "   L.ID_LIVELLO,                                                                     \n"
          + "   D.CODICE AS CODICE_LIVELLO,                                                       \n"
          + "   L.RAGGRUPPAMENTO,                                                                 \n"
          + "   L.FLAG_AMM_COMPETENZA,                                                            \n"
          + "   M.EXT_ID_AMM_COMPETENZA,                                                          \n"
          + "   AC.DESCRIZIONE AS DESCR_AMM,                                                      \n"
          + "   AC.DESC_BREVE_TIPO_AMMINISTRAZ,                                                   \n"
          + "   AC.DESC_ESTESA_TIPO_AMMINISTRAZ,                                                  \n"
          + "   (SELECT COUNT(*) FROM NEMBO_R_RISOR_LIV_BAND_IMP_LIQ Q                            \n"
          + "   WHERE Q.ID_RISORSE_LIVELLO_BANDO = L.ID_RISORSE_LIVELLO_BANDO) AS IMP_LIQ		  \n"
          + "  FROM                                                                               \n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO L,                                                    \n"
          + "   NEMBO_D_LIVELLO D,                                                                  \n"
          + "    NEMBO_R_RISOR_LIV_BANDO_AMM_CO M,                                                 \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA AC                                                     \n"
          + "  WHERE                                                                              \n"
          + "   L.ID_BANDO = :ID_BANDO                                                            \n"
          + "   AND L.ID_TIPO_IMPORTO = :ID_TIPO_IMPORTO                                          \n"
          + "   AND D.ID_LIVELLO = L.ID_LIVELLO                                                   \n"
          + "   AND M.ID_RISORSE_LIVELLO_BANDO(+) = L.ID_RISORSE_LIVELLO_BANDO                    \n"
          + "   AND AC.ID_AMM_COMPETENZA(+) = M.EXT_ID_AMM_COMPETENZA                             \n"
          + " ORDER BY L.ID_RISORSE_LIVELLO_BANDO, D.CODICE, AC.DESCRIZIONE                       \n";

      mapParameterSource.addValue("ID_BANDO", idBando);
      mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<RisorsaAttivataDTO>>()
          {
            @Override
            public List<RisorsaAttivataDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              long idRisorseLivelloBando = 0;
              long idAmmCompetenza = 0;
              long idRisorseLivelloBandoLast = 0;
              List<RisorsaAttivataDTO> elenco = null;
              List<AmmCompetenzaDTO> elencoAmmCompetenza = null;
              AmmCompetenzaDTO ammCompetenza = null;
              RisorsaAttivataDTO risorsa;

              while (rs.next())
              {
                if (elenco == null)
                {
                  elenco = new ArrayList<RisorsaAttivataDTO>();
                }
                idRisorseLivelloBandoLast = rs
                    .getLong("ID_RISORSE_LIVELLO_BANDO");
                idAmmCompetenza = rs.getLong("EXT_ID_AMM_COMPETENZA");
                if (idRisorseLivelloBandoLast != idRisorseLivelloBando)
                {
                  idRisorseLivelloBando = idRisorseLivelloBandoLast;
                  risorsa = new RisorsaAttivataDTO();
                  risorsa.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                  risorsa.setDataFine(rs.getDate("DATA_FINE"));
                  risorsa.setDataInizio(rs.getDate("DATA_INIZIO"));
                  risorsa.setDescrizione(rs.getString("DESCRIZIONE"));
                  risorsa.setFlagAmmCompetenza(
                      rs.getString("FLAG_AMM_COMPETENZA"));
                  risorsa.setFlagBloccato(rs.getString("FLAG_BLOCCATO"));
                  risorsa.setIdLivello(rs.getLong("ID_LIVELLO"));
                  risorsa.setIdRisorseLivelloBando(idRisorseLivelloBandoLast);
                  risorsa.setRaggruppamento(rs.getString("RAGGRUPPAMENTO"));
                  risorsa
                      .setRisorseAttivate(rs.getBigDecimal("RISORSE_ATTIVATE"));
                  risorsa.setRisorsaEliminabile(rs.getLong("IMP_LIQ") <= 0);

                  if (idAmmCompetenza > 0)
                  {
                    elencoAmmCompetenza = new ArrayList<AmmCompetenzaDTO>();
                    risorsa.setElencoAmmCompetenza(elencoAmmCompetenza);
                  }
                  elenco.add(risorsa);
                }
                if (idAmmCompetenza > 0)
                {
                  ammCompetenza = new AmmCompetenzaDTO();
                  ammCompetenza
                      .setIdAmmCompetenza(rs.getLong("EXT_ID_AMM_COMPETENZA"));
                  ammCompetenza.setDescrizione(rs.getString("DESCR_AMM"));
                  ammCompetenza.setDescBreveTipoAmministraz(
                      rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                  ammCompetenza.setDescEstesaTipoAmministraz(
                      rs.getString("DESC_ESTESA_TIPO_AMMINISTRAZ"));
                  elencoAmmCompetenza.add(ammCompetenza);
                }
              }

              return elenco;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBando", idBando) }, new LogVariable[]
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

  public void updateRisorseLivelloBando(long idRisorseLivelloBando,
      String descrizione, BigDecimal risorseAttivate,
      long idLivello, Date dataInizio, Date dataFine, String flagBloccato,
      String flagAmmCompetenza, String raggruppamento)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateRisorseLivelloBando";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      UPDATE = " UPDATE                                                   \n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO A                          \n"
          + " SET                                                      \n"
          + "   A.DESCRIZIONE = :DESCRIZIONE,                          \n"
          + "   A.RISORSE_ATTIVATE = :RISORSE_ATTIVATE,                \n"
          + "   A.DATA_INIZIO = :DATA_INIZIO,                          \n"
          + "   A.DATA_FINE = :DATA_FINE,                              \n"
          + "   A.ID_LIVELLO = :ID_LIVELLO,                            \n"
          + "   A.FLAG_BLOCCATO = :FLAG_BLOCCATO,                      \n"
          + "   A.RAGGRUPPAMENTO = :RAGGRUPPAMENTO,                    \n"
          + "   A.FLAG_AMM_COMPETENZA = :FLAG_AMM_COMPETENZA           \n"
          + " WHERE                                                    \n"
          + "   A.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("RAGGRUPPAMENTO", raggruppamento,
          Types.VARCHAR);
      mapParameterSource.addValue("RISORSE_ATTIVATE", risorseAttivate,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_INIZIO", dataInizio, Types.DATE);
      mapParameterSource.addValue("DATA_FINE", dataFine, Types.DATE);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_BLOCCATO", flagBloccato, Types.VARCHAR);
      mapParameterSource.addValue("FLAG_AMM_COMPETENZA", flagAmmCompetenza,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idRisorseLivelloBando", idRisorseLivelloBando),
              new LogParameter("descrizione", descrizione),
              new LogParameter("risorseAttivate", risorseAttivate),
              new LogParameter("raggruppamento", raggruppamento),
              new LogParameter("dataInizio", dataInizio),
              new LogParameter("dataFine", dataFine),
              new LogParameter("flagBloccato", flagBloccato),
              new LogParameter("flagAmmCompetenza", flagAmmCompetenza),
              new LogParameter("idLivello", idLivello) },
          new LogVariable[]
          {}, UPDATE, new MapSqlParameterSource());
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

  public long insertRisorseLivelloBando(String descrizione,
      BigDecimal risorseAttivate,
      long idLivello, Date dataInizio, Date dataFine, String flagBloccato,
      String flagAmmCompetenza, long idBando, long idTipoImporto,
      String raggruppamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRisorseLivelloBando";
    String INSERT = "";
    long newId = 0;
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT INTO                   \n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO \n"
          + " (                             \n"
          + "   ID_RISORSE_LIVELLO_BANDO,   \n"
          + "   DESCRIZIONE,                \n"
          + "   RISORSE_ATTIVATE,           \n"
          + "   DATA_INIZIO,                \n"
          + "   DATA_FINE,                  \n"
          + "   ID_LIVELLO,                 \n"
          + "   ID_BANDO,                   \n"
          + "   ID_TIPO_IMPORTO,            \n"
          + "   FLAG_BLOCCATO,              \n"
          + "   RAGGRUPPAMENTO,             \n"
          + "   FLAG_AMM_COMPETENZA         \n"
          + " )                             \n"
          + " VALUES(                       \n"
          + "   :ID_RISORSE_LIVELLO_BANDO,  \n"
          + "   :DESCRIZIONE,               \n"
          + "   :RISORSE_ATTIVATE,          \n"
          + "   :DATA_INIZIO,               \n"
          + "   :DATA_FINE,                 \n"
          + "   :ID_LIVELLO,                \n"
          + "   :ID_BANDO,                  \n"
          + "   :ID_TIPO_IMPORTO,           \n"
          + "   :FLAG_BLOCCATO,       		\n"
          + "   :RAGGRUPPAMENTO,       		\n"
          + "   :FLAG_AMM_COMPETENZA  		\n"
          + " )                             \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      newId = getNextSequenceValue("SEQ_NEMBO_T_RISORSE_LIVEL_BAND");
      mapParameterSource.addValue("ID_RISORSE_LIVELLO_BANDO", newId,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("RAGGRUPPAMENTO", raggruppamento,
          Types.VARCHAR);
      mapParameterSource.addValue("RISORSE_ATTIVATE", risorseAttivate,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_INIZIO", dataInizio, Types.DATE);
      mapParameterSource.addValue("DATA_FINE", dataFine, Types.DATE);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_BLOCCATO", flagBloccato, Types.VARCHAR);
      mapParameterSource.addValue("FLAG_AMM_COMPETENZA", flagAmmCompetenza,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("descrizione", descrizione),
              new LogParameter("raggruppamento", raggruppamento),
              new LogParameter("risorseAttivate", risorseAttivate),
              new LogParameter("dataInizio", dataInizio),
              new LogParameter("idBando", idBando),
              new LogParameter("idTipoImporto", idTipoImporto),
              new LogParameter("dataFine", dataFine),
              new LogParameter("flagBloccato", flagBloccato),
              new LogParameter("idLivello", idLivello) },
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
    return newId;
  }

  public void insertRisorsaLivelloBando(long idRisorseLivelloBando,
      long idAmmCompetenza) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRisorsaLivelloBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT INTO                      \n"
          + "    NEMBO_R_RISOR_LIV_BANDO_AMM_CO \n"
          + " (                                \n"
          + "   ID_RISORSE_LIVELLO_BANDO,      \n"
          + "   EXT_ID_AMM_COMPETENZA          \n"
          + " )                                \n"
          + " VALUES(                          \n"
          + "   :ID_RISORSE_LIVELLO_BANDO,     \n"
          + "   :EXT_ID_AMM_COMPETENZA         \n"
          + " )                                \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_AMM_COMPETENZA", idAmmCompetenza,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idRisorseLivelloBando", idRisorseLivelloBando),
              new LogParameter("idAmmCompetenza", idAmmCompetenza) },
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

  public List<EconomiaDTO> getelencoEconomie(long idRisorseLivelloBando)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getelencoEconomie";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                   \n"
          + "   A.ID_ECONOMIA,                                         \n"
          + "   A.DESCRIZIONE,                                         \n"
          + "   A.IMPORTO_ECONOMIA                                     \n"
          + " FROM                                                     \n"
          + "   NEMBO_T_ECONOMIA A                                       \n"
          + " WHERE                                                    \n"
          + "   A.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO \n"
          + " ORDER BY A.ID_ECONOMIA \n";

      mapParameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, EconomiaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idRisorseLivelloBando", idRisorseLivelloBando) },
          new LogVariable[]
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

  public void insertEconomia(String descrizione, BigDecimal importoEconomia,
      long idRisorseLivelloBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertEconomia";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " INSERT INTO                       \n"
          + "   NEMBO_T_ECONOMIA 					\n"
          + " (                                 \n"
          + "   ID_ECONOMIA,      				\n"
          + "   ID_RISORSE_LIVELLO_BANDO,		\n"
          + "   DESCRIZIONE,					\n"
          + "   IMPORTO_ECONOMIA          		\n"
          + " )                                	\n"
          + " VALUES(                          	\n"
          + "   :ID_ECONOMIA,      				\n"
          + "   :ID_RISORSE_LIVELLO_BANDO,		\n"
          + "   :DESCRIZIONE,					\n"
          + "   :IMPORTO_ECONOMIA          		\n"
          + " )                                	\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_ECONOMIA",
          getNextSequenceValue("SEQ_NEMBO_T_ECONOMIA"), Types.NUMERIC);
      mapParameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_ECONOMIA", importoEconomia,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idRisorseLivelloBando", idRisorseLivelloBando),
              new LogParameter("importoEconomia", importoEconomia),
              new LogParameter("descrizione", descrizione) },
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

  public void cleanFondiVuoti(long idBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "cleanFondiVuoti";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      INSERT = " DELETE                                          \n"
          + "   NEMBO_R_FONDO A                                 \n"
          + " WHERE                                           \n"
          + "   A.ID_BANDO = :ID_BANDO                        \n"
          + "   AND NOT EXISTS (                              \n"
          + "       SELECT                                    \n"
          + "           R.ID_TIPO_IMPORTO                     \n"
          + "        FROM                                     \n"
          + "           NEMBO_T_RISORSE_LIVELLO_BANDO R         \n"
          + "        WHERE                                    \n"
          + "           R.ID_TIPO_IMPORTO = A.ID_TIPO_IMPORTO \n"
          + "           AND R.ID_BANDO = A.ID_BANDO			  \n"
          + "   )                                             \n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idBando", idBando) },
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

  public boolean isFlagAmmCompetenzaCorrette(long idBando, long idTipoImporto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isFlagAmmCompetenzaCorrette";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                           \n"
          + "   COUNT(*) AS INCOERENZE                          \n"
          + " FROM                                              \n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO A                   \n"
          + " WHERE                                             \n"
          + "   A.ID_BANDO = :ID_BANDO                          \n"
          + "   AND A.ID_TIPO_IMPORTO = :ID_TIPO_IMPORTO        \n"
          + "   AND A.FLAG_AMM_COMPETENZA =  'S'                \n"
          + "   AND A.DATA_FINE IS NULL			                \n"
          + "   AND EXISTS (                                    \n"
          + "       SELECT B.ID_RISORSE_LIVELLO_BANDO           \n"
          + "       FROM                                        \n"
          + "         NEMBO_T_RISORSE_LIVELLO_BANDO B             \n"
          + "       WHERE                                       \n"
          + "         B.ID_BANDO = A.ID_BANDO                   \n"
          + "         AND B.ID_TIPO_IMPORTO = A.ID_TIPO_IMPORTO \n"
          + "         AND B.ID_LIVELLO = A.ID_LIVELLO           \n"
          + "  		  AND B.DATA_FINE IS NULL			        \n"
          + "         AND B.RAGGRUPPAMENTO = A.RAGGRUPPAMENTO   \n"
          + "         AND B.FLAG_AMM_COMPETENZA = 'N'           \n"
          + "   )                                               \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
                return rs.getLong("INCOERENZE") <= 0;
              return true;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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

  public boolean isPresentiRecordDoppi(long idBando, long idTipoImporto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isPresentiRecordDoppi";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                          \n"
          + "   COUNT(*) AS INCOERENZE                                                        \n"
          + "  FROM                                                                           \n"
          + "    NEMBO_T_RISORSE_LIVELLO_BANDO A                                                \n"
          + "  WHERE                                                                          \n"
          + "    A.ID_BANDO = :ID_BANDO                                                       \n"
          + "    AND A.ID_TIPO_IMPORTO = :ID_TIPO_IMPORTO                                     \n"
          + "    AND A.DATA_FINE IS NULL                                                      \n"
          + "    AND A.FLAG_AMM_COMPETENZA = 'S'                                              \n"
          + "    AND EXISTS (                                                                 \n"
          + "        SELECT B.ID_RISORSE_LIVELLO_BANDO                                        \n"
          + "        FROM                                                                     \n"
          + "          NEMBO_T_RISORSE_LIVELLO_BANDO B                                          \n"
          + "        WHERE                                                                    \n"
          + "          B.ID_BANDO = A.ID_BANDO                                                \n"
          + "          AND B.ID_TIPO_IMPORTO = A.ID_TIPO_IMPORTO                              \n"
          + "          AND B.ID_LIVELLO = A.ID_LIVELLO                                        \n"
          + "         AND B.DATA_FINE IS NULL                                                 \n"
          + "         AND B.FLAG_AMM_COMPETENZA = 'S'                                         \n"
          + "         AND A.ID_RISORSE_LIVELLO_BANDO <> B.ID_RISORSE_LIVELLO_BANDO            \n"
          + "         AND nvl(B.RAGGRUPPAMENTO,'0') = nvl(A.RAGGRUPPAMENTO,'0')  	     	  \n"
          + "    )                                                                            \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
                return rs.getLong("INCOERENZE") > 0;
              return true;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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

  public boolean validateEconomie(final long idRisorseLivelloBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "validateEconomie";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                                                                                 \n"
          + "   (SELECT SUM(A.IMPORTO_ECONOMIA) AS TOT_ECONOMIE FROM NEMBO_T_ECONOMIA A WHERE A.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO) AS TOT_ECONOMIE, \n"
          + "   (SELECT RISORSE_ATTIVATE FROM NEMBO_T_RISORSE_LIVELLO_BANDO B WHERE B.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO) AS TOT_RISORSE             \n"
          + " FROM                                                                                                                                                   \n"
          + "   DUAL                                                                                                                                                 \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                BigDecimal economie = rs.getBigDecimal("TOT_ECONOMIE");
                BigDecimal risorse = rs.getBigDecimal("TOT_RISORSE");
                BigDecimal impLiq = BigDecimal.ZERO;

                try
                {
                  impLiq = getTotaleImportoLiquidato(idRisorseLivelloBando);
                }
                catch (InternalUnexpectedException e)
                {
                  impLiq = BigDecimal.ZERO;
                }

                if (economie != null
                    && economie.compareTo(risorse.subtract(impLiq)) == 1)
                {
                  return false;
                }
              }
              return true;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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

  public BigDecimal getTotaleEconomie(long idRisorseLivelloBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getTotaleEconomie";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                   \n"
          + "   NVL(SUM(NVL(B.IMPORTO_ECONOMIA,0)), 0) AS TOTALE       \n"
          + " FROM                                                     \n"
          + "   NEMBO_T_ECONOMIA B                                       \n"
          + " WHERE                                                    \n"
          + "   B.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BigDecimal>()
          {
            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getBigDecimal("TOTALE");
              }
              return BigDecimal.ZERO;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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

  public BigDecimal getTotaleImportoLiquidato(long idRisorseLivelloBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getTotaleImportoLiquidato";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                   \n"
          + "   NVL(SUM(NVL(A.IMPORTO_LIQUIDATO,0)), 0) AS TOTALE      \n"
          + " FROM                                                     \n"
          + "   NEMBO_T_IMPORTI_LIQUIDATI A,                             \n"
          + "   NEMBO_R_RISOR_LIV_BAND_IMP_LIQ B                       \n"
          + " WHERE                                                    \n"
          + "   B.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO \n"
          + "   AND A.ID_IMPORTI_LIQUIDATI = B.ID_IMPORTI_LIQUIDATI    \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BigDecimal>()
          {
            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getBigDecimal("TOTALE");
              }
              return BigDecimal.ZERO;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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

  public boolean isAmmCompetenzeCorrette(long idBando, long idTipoImporto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isAmmCompetenzeCorrette";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                           \n"
          + "   A.ID_BANDO,                                                    \n"
          + "   A.ID_TIPO_IMPORTO,                                             \n"
          + "   A.ID_LIVELLO,                                                  \n"
          + "   A.RAGGRUPPAMENTO,                                              \n"
          + "   B.EXT_ID_AMM_COMPETENZA,                                       \n"
          + "   COUNT(*) AS TOTALI                                             \n"
          + " FROM                                                             \n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO A,                                 \n"
          + "    NEMBO_R_RISOR_LIV_BANDO_AMM_CO B                               \n"
          + " WHERE                                                            \n"
          + "   A.ID_BANDO = :ID_BANDO                                         \n"
          + "   AND A.ID_TIPO_IMPORTO = :ID_TIPO_IMPORTO                       \n"
          + "   AND A.FLAG_AMM_COMPETENZA =  'N'                               \n"
          + "   AND B.ID_RISORSE_LIVELLO_BANDO(+) = A.ID_RISORSE_LIVELLO_BANDO \n"
          + "   AND A.DATA_FINE IS NULL			        					   \n"
          + " GROUP BY                                                         \n"
          + "   A.ID_BANDO,                                                    \n"
          + "   A.ID_TIPO_IMPORTO,                                             \n"
          + "   A.ID_LIVELLO,                                                  \n"
          + "   A.RAGGRUPPAMENTO,                                              \n"
          + "   B.EXT_ID_AMM_COMPETENZA                                        \n"
          + "                                                                  \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                if (rs.getLong("TOTALI") > 1)
                {
                  return false;
                }
              }

              return true;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          SELECT);
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

  public void updateUltimaModificaFondo(long idBando, long idTipoImporto,
      long idUtenteAggiornamennto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateUltimaModificaFondo";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                                                \n"
          + "   NEMBO_R_FONDO L                                               		\n"
          + " SET                                                                   \n"
          + "   L.EXT_ID_UTENTE_AGGIORNAMENTO = :EXT_ID_UTENTE_AGGIORNAMENTO,   	\n"
          + "   L.DATA_ULTIMO_AGGIORNAMENTO = SYSDATE 								\n"
          + " WHERE                                                                 \n"
          + "   L.ID_TIPO_IMPORTO = :ID_TIPO_IMPORTO                                \n"
          + "   AND L.ID_BANDO = :ID_BANDO                                          \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_IMPORTO", idTipoImporto,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          idUtenteAggiornamennto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("idTipoImporto", idTipoImporto),
              new LogParameter("idUtenteAggiornamennto", idUtenteAggiornamennto)
          },
          new LogVariable[]
          {}, UPDATE, new MapSqlParameterSource());
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

  public RisorsaAttivataDTO getRisorsa(long idRisorseLivelloBando)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getRisorsa";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                                              \n"
          + "   L.ID_RISORSE_LIVELLO_BANDO,                                                       \n"
          + "   L.DESCRIZIONE,                                                                    \n"
          + "   L.RISORSE_ATTIVATE,                                                               \n"
          + "   L.DATA_INIZIO,                                                                    \n"
          + "   L.DATA_FINE,                                                                      \n"
          + "   L.FLAG_BLOCCATO,                                                                  \n"
          + "   L.ID_LIVELLO,                                                                     \n"
          + "   L.RAGGRUPPAMENTO,                                                                 \n"
          + "   L.ID_BANDO,                                                                 	  \n"
          + "   L.ID_TIPO_IMPORTO,                                                                \n"
          + "   L.FLAG_AMM_COMPETENZA                                                             \n"
          + "  FROM                                                                               \n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO L                                                     \n"
          + "  WHERE                                                                              \n"
          + "   L.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO                            \n";

      mapParameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<RisorsaAttivataDTO>()
          {
            @Override
            public RisorsaAttivataDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              RisorsaAttivataDTO risorsa = null;

              while (rs.next())
              {
                risorsa = new RisorsaAttivataDTO();
                risorsa.setDataFine(rs.getDate("DATA_FINE"));
                risorsa.setDataInizio(rs.getDate("DATA_INIZIO"));
                risorsa.setDescrizione(rs.getString("DESCRIZIONE"));
                risorsa
                    .setFlagAmmCompetenza(rs.getString("FLAG_AMM_COMPETENZA"));
                risorsa.setFlagBloccato(rs.getString("FLAG_BLOCCATO"));
                risorsa.setIdLivello(rs.getLong("ID_LIVELLO"));
                risorsa.setIdRisorseLivelloBando(
                    rs.getLong("ID_RISORSE_LIVELLO_BANDO"));
                risorsa.setRaggruppamento(rs.getString("RAGGRUPPAMENTO"));
                risorsa.setIdBando(rs.getLong("ID_BANDO"));
                risorsa.setIdTipoImporto(rs.getLong("ID_TIPO_IMPORTO"));
              }

              return risorsa;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idRisorseLivelloBando", idRisorseLivelloBando) },
          new LogVariable[]
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

  public void lockPianoFinanziario()
  {
    final String QUERY = " SELECT 1 FROM NEMBO_T_PIANO_FINANZIARIO WHERE ID_PIANO_FINANZIARIO = 1 FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
  }
}
