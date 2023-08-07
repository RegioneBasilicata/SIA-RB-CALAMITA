package it.csi.nembo.nemboconf.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;

import it.csi.nembo.nemboconf.dto.DecodificaImportoDTO;
import it.csi.nembo.nemboconf.dto.PrioritaFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.dto.pianofinanziario.GerarchiaLivelloDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.ImportoFocusAreaDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.IterPianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.PianoFinanziarioDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.SottoMisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class PianoFinanziarioNemboDAO extends BaseDAO
{
  private static final String THIS_CLASS = PianoFinanziarioNemboDAO.class
      .getSimpleName();

  public void lockPianoFinanziario(String codice)
  {
    // Faccio il lock sulla tabella NEMBO_D_TIPO_PIANO_FINANZIARIO e non sulla
    // NEMBO_T_PIANO_FINANZIARIO
    // perchè la prima ha sempre almeno un record su cui fare lock, la seconda
    // (se è la prima volta
    // che si inserisce un pinao finanziario) potrebbe non averne.
    final String QUERY = " SELECT 1 FROM NEMBO_D_TIPO_PIANO_FINANZIARIO WHERE CODICE = :CODICE FOR UPDATE ";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("CODICE", codice);
    namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
  }

  public List<PianoFinanziarioDTO> getElencoPianiFinanziari(String codice)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoPianiFinanziari]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                                                 \n"
        + "   PF.ID_PIANO_FINANZIARIO,                                            \n"
        + "   PF.DESCRIZIONE AS PF_DESCRIZIONE,                                   \n"
        + "   PF.DATA_ULTIMO_AGGIORNAMENTO AS PF_DATA_ULTIMO_AGGIORNAMENTO,       \n"
        + "   PF.EXT_ID_UTENTE_AGGIORNAMENTO AS PF_EXT_ID_UTENTE_AGGIORNAMENTO,   \n"
        + "   TPF.ID_TIPO_PIANO_FINANZIARIO,                                      \n"
        + "   TPF.DESCRIZIONE AS TPF_DESCRIZIONE,                                 \n"
        + "   TPF.CODICE AS TPF_CODICE,                                           \n"
        + "   IPF.DATA_FINE AS IPF_DATA_FINE,                                     \n"
        + "   IPF.DATA_INIZIO AS IPF_DATA_INIZIO,                                 \n"
        + "   IPF.EXT_ID_UTENTE_AGGIORNAMENTO AS IPF_EXT_ID_UTENTE_AGGIORNAMENT,  \n"
        + "   IPF.ID_ITER_PIANO_FINANZIARIO,                                      \n"
        + "   SPF.ID_STATO_PIANO_FINANZIARIO,                                     \n"
        + "   SPF.DESCRIZIONE AS SPF_DESCRIZIONE                                  \n"
        + " FROM                                                                  \n"
        + "   NEMBO_T_PIANO_FINANZIARIO PF,                                         \n"
        + "   NEMBO_T_ITER_PIANO_FINANZIARIO IPF,                                   \n"
        + "   NEMBO_D_STATO_PIANO_FINANZIARI SPF,                                  \n"
        + "   NEMBO_D_TIPO_PIANO_FINANZIARIO TPF                                    \n"
        + " WHERE                                                                 \n"
        + "   PF.ID_PIANO_FINANZIARIO = IPF.ID_PIANO_FINANZIARIO                  \n"
        + "   AND IPF.ID_STATO_PIANO_FINANZIARIO = SPF.ID_STATO_PIANO_FINANZIARIO \n"
        + "   AND PF.ID_TIPO_PIANO_FINANZIARIO = TPF.ID_TIPO_PIANO_FINANZIARIO    \n"
        + "   AND TPF.CODICE = :CODICE                                            \n"
        + " ORDER BY                                                              \n"
        + "   SPF.ID_STATO_PIANO_FINANZIARIO , IPF.DATA_INIZIO DESC               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("CODICE", codice);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<PianoFinanziarioDTO>>()
          {

            @Override
            public List<PianoFinanziarioDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<PianoFinanziarioDTO> list = new ArrayList<PianoFinanziarioDTO>();
              PianoFinanziarioDTO pianoFinanziarioDTO = null;
              Long lastIdPianoFinanziario = null;
              long idPianoFinanziario = 0;
              while (rs.next())
              {
                idPianoFinanziario = rs.getLong("ID_PIANO_FINANZIARIO");
                if (lastIdPianoFinanziario == null
                    || lastIdPianoFinanziario != idPianoFinanziario)
                {
                  pianoFinanziarioDTO = new PianoFinanziarioDTO();
                  pianoFinanziarioDTO.setIdPianoFinanziario(idPianoFinanziario);
                  pianoFinanziarioDTO.setDataUltimoAggiornamento(
                      rs.getTimestamp("PF_DATA_ULTIMO_AGGIORNAMENTO"));
                  pianoFinanziarioDTO
                      .setDescrizione(rs.getString("PF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setExtIdUtenteAggiornamento(
                      rs.getLong("PF_EXT_ID_UTENTE_AGGIORNAMENTO"));
                  pianoFinanziarioDTO.setIdTipoPianoFinanziario(
                      rs.getLong("ID_TIPO_PIANO_FINANZIARIO"));
                  pianoFinanziarioDTO
                      .setDescTipo(rs.getString("TPF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setCodiceTipoPianoFinanziario(
                      rs.getString("TPF_CODICE"));
                  pianoFinanziarioDTO
                      .setIter(new ArrayList<IterPianoFinanziarioDTO>());
                  list.add(pianoFinanziarioDTO);
                  lastIdPianoFinanziario = idPianoFinanziario;
                }
                IterPianoFinanziarioDTO iter = new IterPianoFinanziarioDTO();
                iter.setDataFine(rs.getTimestamp("IPF_DATA_FINE"));
                iter.setDataInizio(rs.getTimestamp("IPF_DATA_INIZIO"));
                iter.setExtIdUtenteAggiornamento(
                    rs.getLong("IPF_EXT_ID_UTENTE_AGGIORNAMENT"));
                iter.setIdIterPianoFinanziario(
                    rs.getLong("ID_ITER_PIANO_FINANZIARIO"));
                iter.setIdPianoFinanziario(
                    pianoFinanziarioDTO.getIdPianoFinanziario());
                iter.setIdStatoPianoFinanziario(
                    rs.getLong("ID_STATO_PIANO_FINANZIARIO"));
                iter.setDescStato(rs.getString("SPF_DESCRIZIONE"));
                pianoFinanziarioDTO.getIter().add(iter);
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("codice", codice) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public PianoFinanziarioDTO getPianoFinanziarioCorrente(String codice)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getLastPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " WITH                                                       \n"
        + "   PIANI AS                                                                \n"
        + "   (                                                                       \n"
        + "     SELECT                                                                \n"
        + "       PF.ID_PIANO_FINANZIARIO,                                            \n"
        + "       PF.DESCRIZIONE                 AS PF_DESCRIZIONE,                   \n"
        + "       PF.DATA_ULTIMO_AGGIORNAMENTO   AS PF_DATA_ULTIMO_AGGIORNAMENTO,     \n"
        + "       PF.EXT_ID_UTENTE_AGGIORNAMENTO AS PF_EXT_ID_UTENTE_AGGIORNAMENTO,   \n"
        + "       TPF.ID_TIPO_PIANO_FINANZIARIO,                                      \n"
        + "       TPF.DESCRIZIONE                 AS TPF_DESCRIZIONE,                 \n"
        + "       TPF.CODICE                      AS TPF_CODICE,                      \n"
        + "       IPF.DATA_FINE                   AS IPF_DATA_FINE,                   \n"
        + "       IPF.DATA_INIZIO                 AS IPF_DATA_INIZIO,                 \n"
        + "       IPF.EXT_ID_UTENTE_AGGIORNAMENTO AS IPF_EXT_ID_UTENTE_AGGIORNAMENT,  \n"
        + "       IPF.ID_ITER_PIANO_FINANZIARIO,                                      \n"
        + "       SPF.ID_STATO_PIANO_FINANZIARIO,                                     \n"
        + "       SPF.DESCRIZIONE AS SPF_DESCRIZIONE                                  \n"
        + "     FROM                                                                  \n"
        + "       NEMBO_T_PIANO_FINANZIARIO PF,                                         \n"
        + "       NEMBO_T_ITER_PIANO_FINANZIARIO IPF,                                   \n"
        + "       NEMBO_D_STATO_PIANO_FINANZIARI SPF,                                  \n"
        + "       NEMBO_D_TIPO_PIANO_FINANZIARIO TPF                                    \n"
        + "     WHERE                                                                 \n"
        + "       PF.ID_PIANO_FINANZIARIO            = IPF.ID_PIANO_FINANZIARIO       \n"
        + "       AND IPF.ID_STATO_PIANO_FINANZIARIO = SPF.ID_STATO_PIANO_FINANZIARIO \n"
        + "       AND PF.ID_TIPO_PIANO_FINANZIARIO   = TPF.ID_TIPO_PIANO_FINANZIARIO  \n"
        + "       AND TPF.CODICE                     = :CODICE                        \n"
        + "   )                                                                       \n"
        + " SELECT                                                                    \n"
        + "   ID_PIANO_FINANZIARIO,                                                   \n"
        + "   PF_DESCRIZIONE,                                                         \n"
        + "   PF_DATA_ULTIMO_AGGIORNAMENTO,                                           \n"
        + "   PF_EXT_ID_UTENTE_AGGIORNAMENTO,                                         \n"
        + "   ID_TIPO_PIANO_FINANZIARIO,                                              \n"
        + "   TPF_DESCRIZIONE,                                                        \n"
        + "   TPF_CODICE,                                                             \n"
        + "   IPF_DATA_FINE,                                                          \n"
        + "   IPF_DATA_INIZIO,                                                        \n"
        + "   IPF_EXT_ID_UTENTE_AGGIORNAMENT,                                         \n"
        + "   ID_ITER_PIANO_FINANZIARIO,                                              \n"
        + "   ID_STATO_PIANO_FINANZIARIO,                                             \n"
        + "   SPF_DESCRIZIONE                                                         \n"
        + " FROM                                                                      \n"
        + "   PIANI PF                                                                \n"
        + " WHERE                                                                     \n"
        + "   PF.ID_PIANO_FINANZIARIO =                                               \n"
        + "   (                                                                       \n"
        + "     SELECT                                                                \n"
        + "       MAX(PF2.ID_PIANO_FINANZIARIO)                                       \n"
        + "     FROM                                                                  \n"
        + "       PIANI PF2                                                           \n"
        + "   )                                                                       \n"
        + " ORDER BY                                                                  \n"
        + "   IPF_DATA_INIZIO DESC                                                    \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("CODICE", codice);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<PianoFinanziarioDTO>()
          {

            @Override
            public PianoFinanziarioDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              PianoFinanziarioDTO pianoFinanziarioDTO = null;
              while (rs.next())
              {
                if (pianoFinanziarioDTO == null)
                {
                  pianoFinanziarioDTO = new PianoFinanziarioDTO();
                  pianoFinanziarioDTO.setIdPianoFinanziario(
                      rs.getLong("ID_PIANO_FINANZIARIO"));
                  pianoFinanziarioDTO.setDataUltimoAggiornamento(
                      rs.getTimestamp("PF_DATA_ULTIMO_AGGIORNAMENTO"));
                  pianoFinanziarioDTO
                      .setDescrizione(rs.getString("PF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setExtIdUtenteAggiornamento(
                      rs.getLong("PF_EXT_ID_UTENTE_AGGIORNAMENTO"));
                  pianoFinanziarioDTO.setIdTipoPianoFinanziario(
                      rs.getLong("ID_TIPO_PIANO_FINANZIARIO"));
                  pianoFinanziarioDTO
                      .setDescTipo(rs.getString("TPF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setCodiceTipoPianoFinanziario(
                      rs.getString("TPF_CODICE"));
                  pianoFinanziarioDTO
                      .setIter(new ArrayList<IterPianoFinanziarioDTO>());
                }
                IterPianoFinanziarioDTO iter = new IterPianoFinanziarioDTO();
                iter.setDataFine(rs.getTimestamp("IPF_DATA_FINE"));
                iter.setDataInizio(rs.getTimestamp("IPF_DATA_INIZIO"));
                iter.setExtIdUtenteAggiornamento(
                    rs.getLong("IPF_EXT_ID_UTENTE_AGGIORNAMENT"));
                iter.setIdIterPianoFinanziario(
                    rs.getLong("ID_ITER_PIANO_FINANZIARIO"));
                iter.setIdPianoFinanziario(
                    pianoFinanziarioDTO.getIdPianoFinanziario());
                iter.setIdStatoPianoFinanziario(
                    rs.getLong("ID_STATO_PIANO_FINANZIARIO"));
                iter.setDescStato(rs.getString("SPF_DESCRIZIONE"));
                pianoFinanziarioDTO.getIter().add(iter);
              }
              return pianoFinanziarioDTO;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("codice", codice) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public PianoFinanziarioDTO getPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                                                 \n"
        + "   PF.ID_PIANO_FINANZIARIO,                                            \n"
        + "   PF.DESCRIZIONE                 AS PF_DESCRIZIONE,                   \n"
        + "   PF.DATA_ULTIMO_AGGIORNAMENTO   AS PF_DATA_ULTIMO_AGGIORNAMENTO,     \n"
        + "   PF.EXT_ID_UTENTE_AGGIORNAMENTO AS PF_EXT_ID_UTENTE_AGGIORNAMENTO,   \n"
        + "   TPF.ID_TIPO_PIANO_FINANZIARIO,                                      \n"
        + "   TPF.DESCRIZIONE                 AS TPF_DESCRIZIONE,                 \n"
        + "   TPF.CODICE                      AS TPF_CODICE,                      \n"
        + "   IPF.DATA_FINE                   AS IPF_DATA_FINE,                   \n"
        + "   IPF.DATA_INIZIO                 AS IPF_DATA_INIZIO,                 \n"
        + "   IPF.EXT_ID_UTENTE_AGGIORNAMENTO AS IPF_EXT_ID_UTENTE_AGGIORNAMENT,  \n"
        + "   IPF.ID_ITER_PIANO_FINANZIARIO,                                      \n"
        + "   SPF.ID_STATO_PIANO_FINANZIARIO,                                     \n"
        + "   SPF.DESCRIZIONE AS SPF_DESCRIZIONE                                  \n"
        + " FROM                                                                  \n"
        + "   NEMBO_T_PIANO_FINANZIARIO PF,                                         \n"
        + "   NEMBO_T_ITER_PIANO_FINANZIARIO IPF,                                   \n"
        + "   NEMBO_D_STATO_PIANO_FINANZIARI SPF,                                  \n"
        + "   NEMBO_D_TIPO_PIANO_FINANZIARIO TPF                                    \n"
        + " WHERE                                                                 \n"
        + "   PF.ID_PIANO_FINANZIARIO            = IPF.ID_PIANO_FINANZIARIO       \n"
        + "   AND IPF.ID_STATO_PIANO_FINANZIARIO = SPF.ID_STATO_PIANO_FINANZIARIO \n"
        + "   AND PF.ID_TIPO_PIANO_FINANZIARIO   = TPF.ID_TIPO_PIANO_FINANZIARIO  \n"
        + "   AND PF.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO                 \n"
        + " ORDER BY                                                              \n"
        + "   IPF.DATA_INIZIO DESC                                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<PianoFinanziarioDTO>()
          {

            @Override
            public PianoFinanziarioDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              PianoFinanziarioDTO pianoFinanziarioDTO = null;
              while (rs.next())
              {
                if (pianoFinanziarioDTO == null)
                {
                  pianoFinanziarioDTO = new PianoFinanziarioDTO();
                  pianoFinanziarioDTO.setIdPianoFinanziario(
                      rs.getLong("ID_PIANO_FINANZIARIO"));
                  pianoFinanziarioDTO.setDataUltimoAggiornamento(
                      rs.getTimestamp("PF_DATA_ULTIMO_AGGIORNAMENTO"));
                  pianoFinanziarioDTO
                      .setDescrizione(rs.getString("PF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setExtIdUtenteAggiornamento(
                      rs.getLong("PF_EXT_ID_UTENTE_AGGIORNAMENTO"));
                  pianoFinanziarioDTO.setIdTipoPianoFinanziario(
                      rs.getLong("ID_TIPO_PIANO_FINANZIARIO"));
                  pianoFinanziarioDTO
                      .setDescTipo(rs.getString("TPF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setCodiceTipoPianoFinanziario(
                      rs.getString("TPF_CODICE"));
                  pianoFinanziarioDTO
                      .setIter(new ArrayList<IterPianoFinanziarioDTO>());
                }
                IterPianoFinanziarioDTO iter = new IterPianoFinanziarioDTO();
                iter.setDataFine(rs.getTimestamp("IPF_DATA_FINE"));
                iter.setDataInizio(rs.getTimestamp("IPF_DATA_INIZIO"));
                iter.setExtIdUtenteAggiornamento(
                    rs.getLong("IPF_EXT_ID_UTENTE_AGGIORNAMENT"));
                iter.setIdIterPianoFinanziario(
                    rs.getLong("ID_ITER_PIANO_FINANZIARIO"));
                iter.setIdPianoFinanziario(
                    pianoFinanziarioDTO.getIdPianoFinanziario());
                iter.setIdStatoPianoFinanziario(
                    rs.getLong("ID_STATO_PIANO_FINANZIARIO"));
                iter.setDescStato(rs.getString("SPF_DESCRIZIONE"));
                pianoFinanziarioDTO.getIter().add(iter);
              }
              return pianoFinanziarioDTO;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idPianoFinanziario", idPianoFinanziario) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public PianoFinanziarioDTO insertPianoFinanziario(
      PianoFinanziarioDTO pianoFinanziarioDTO)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT            \n"
        + " INTO                              \n"
        + "   NEMBO_T_PIANO_FINANZIARIO         \n"
        + "   (                               \n"
        + "     ID_PIANO_FINANZIARIO,         \n"
        + "     DESCRIZIONE,                  \n"
        + "     DATA_ULTIMO_AGGIORNAMENTO,    \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,  \n"
        + "     ID_TIPO_PIANO_FINANZIARIO     \n"
        + "   )                               \n"
        + "   VALUES                          \n"
        + "   (                               \n"
        + "     :ID_PIANO_FINANZIARIO,        \n"
        + "     :DESCRIZIONE,                 \n"
        + "     SYSDATE,                      \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO, \n"
        + "     :ID_TIPO_PIANO_FINANZIARIO    \n"
        + "   )                               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      pianoFinanziarioDTO.setIdPianoFinanziario(getNextSequenceValue(
          NemboConstants.SQL.SEQUENCE.NEMBO_T_PIANO_FINANZIARIO));
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO",
          pianoFinanziarioDTO.getIdPianoFinanziario(), Types.NUMERIC);
      // Concateno nella descrizione anche l'id piano finanziario
      pianoFinanziarioDTO.setDescrizione(pianoFinanziarioDTO.getDescrizione()
          + " " + pianoFinanziarioDTO.getIdPianoFinanziario());
      mapParameterSource.addValue("DESCRIZIONE",
          pianoFinanziarioDTO.getDescrizione(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          pianoFinanziarioDTO.getExtIdUtenteAggiornamento(), Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_PIANO_FINANZIARIO",
          pianoFinanziarioDTO.getIdTipoPianoFinanziario(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return pianoFinanziarioDTO;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("pianoFinanziarioDTO", pianoFinanziarioDTO) },
          new LogVariable[]
          {}, INSERT, mapParameterSource);
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

  public void insertIterPianoFinanziario(
      IterPianoFinanziarioDTO iterPianoFinanziarioDTO)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertIterPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                            \n"
        + " INTO                                              \n"
        + "   NEMBO_T_ITER_PIANO_FINANZIARIO                    \n"
        + "   (                                               \n"
        + "     ID_ITER_PIANO_FINANZIARIO,                    \n"
        + "     ID_PIANO_FINANZIARIO,                         \n"
        + "     ID_STATO_PIANO_FINANZIARIO,                   \n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,                  \n"
        + "     DATA_INIZIO,                                  \n"
        + "     DATA_FINE                                     \n"
        + "   )                                               \n"
        + "   VALUES                                          \n"
        + "   (                                               \n"
        + "     SEQ_NEMBO_T_ITER_PIANO_FINANZI.NEXTVAL,   	  \n"
        + "     :ID_PIANO_FINANZIARIO,                        \n"
        + "     :ID_STATO_PIANO_FINANZIARIO,                  \n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO,                 \n"
        + "     SYSDATE,                                      \n"
        + "     NULL                                          \n"
        + "   )                                               \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO",
          iterPianoFinanziarioDTO.getIdPianoFinanziario(), Types.NUMERIC);
      mapParameterSource.addValue("ID_STATO_PIANO_FINANZIARIO",
          iterPianoFinanziarioDTO.getIdStatoPianoFinanziario(), Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          iterPianoFinanziarioDTO.getExtIdUtenteAggiornamento(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("iterPianoFinanziarioDTO",
              iterPianoFinanziarioDTO) },
          new LogVariable[]
          {}, INSERT, mapParameterSource);
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

  public void copyPianoFinanziario(long idPianoFinanziarioNew,
      long idPianoFinanziarioOld) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::[" + THIS_CLASS
        + "::copyPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String COPY = " INSERT                                                 \n"
        + " INTO                                                                 \n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA                                           \n"
        + "   (                                                                  \n"
        + "     ID_LIVELLO_FOCUS_AREA,                                           \n"
        + "     ID_FOCUS_AREA,                                                   \n"
        + "     ID_LIVELLO,                                                      \n"
        + "     IMPORTO,                                                         \n"
        + "     IMPORTO_TRASCINATO,                                              \n"
        + "     ID_PIANO_FINANZIARIO                                             \n"
        + "   )                                                                  \n"
        + "     SELECT                                                           \n"
        + "       SEQ_NEMBO_R_LIVELLO_FOCUS_AREA.NEXTVAL,                          \n"
        + "       LFA.ID_FOCUS_AREA,                                             \n"
        + "       LFA.ID_LIVELLO,                                                \n"
        + "       LFA.IMPORTO,                                                   \n"
        + "       LFA.IMPORTO_TRASCINATO,                                        \n"
        + "       :ID_PIANO_FINANZIARIO_NEW                                      \n"
        + "     FROM                                                             \n"
        + "       NEMBO_R_LIVELLO_FOCUS_AREA LFA,                                  \n"
        + "       NEMBO_D_LIVELLO L                                                \n"
        + "     WHERE                                                            \n"
        + "       LFA.ID_LIVELLO               = L.ID_LIVELLO                    \n"
        + "       AND LFA.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO_OLD       \n"
        + "       AND SYSDATE BETWEEN L.DATA_INIZIO AND NVL(L.DATA_FINE, SYSDATE)\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO_NEW",
          idPianoFinanziarioNew, Types.NUMERIC);
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO_OLD",
          idPianoFinanziarioOld, Types.NUMERIC);
      namedParameterJdbcTemplate.update(COPY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idPianoFinanziarioNew", idPianoFinanziarioNew),
              new LogParameter("idPianoFinanziarioNew",
                  idPianoFinanziarioOld) },
          new LogVariable[]
          {}, COPY, mapParameterSource);
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

  public void chiudiIterCorrentePianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::chiudiIterCorrentePianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                         \n"
        + "   NEMBO_T_ITER_PIANO_FINANZIARIO                 \n"
        + " SET                                            \n"
        + "   DATA_FINE = NULL                             \n"
        + " WHERE                                          \n"
        + "   ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO \n"
        + "   AND DATA_FINE IS NULL                        \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idPianoFinanziario", idPianoFinanziario) },
          null, UPDATE, mapParameterSource);
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

  public PianoFinanziarioDTO getUltimoPianoFinanziarioInStato(
      long idStatoPianoFinanziario) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getLastPianoFinanziarioInStato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                                                   \n"
        + "   ROWNUM AS IDX,                                                            \n"
        + "   A.*                                                                       \n"
        + " FROM                                                                        \n"
        + "   (                                                                         \n"
        + "     SELECT                                                                  \n"
        + "       PF.ID_PIANO_FINANZIARIO,                                              \n"
        + "       PF.DESCRIZIONE                 AS PF_DESCRIZIONE,                     \n"
        + "       PF.DATA_ULTIMO_AGGIORNAMENTO   AS PF_DATA_ULTIMO_AGGIORNAMENTO,       \n"
        + "       PF.EXT_ID_UTENTE_AGGIORNAMENTO AS PF_EXT_ID_UTENTE_AGGIORNAMENTO,     \n"
        + "       TPF.ID_TIPO_PIANO_FINANZIARIO,                                        \n"
        + "       TPF.DESCRIZIONE                 AS TPF_DESCRIZIONE,                   \n"
        + "       TPF.CODICE                      AS TPF_CODICE,                        \n"
        + "       IPF.DATA_FINE                   AS IPF_DATA_FINE,                     \n"
        + "       IPF.DATA_INIZIO                 AS IPF_DATA_INIZIO,                   \n"
        + "       IPF.EXT_ID_UTENTE_AGGIORNAMENTO AS IPF_EXT_ID_UTENTE_AGGIORNAMENT,    \n"
        + "       IPF.ID_ITER_PIANO_FINANZIARIO,                                        \n"
        + "       SPF.ID_STATO_PIANO_FINANZIARIO,                                       \n"
        + "       SPF.DESCRIZIONE AS SPF_DESCRIZIONE                                    \n"
        + "     FROM                                                                    \n"
        + "       NEMBO_T_PIANO_FINANZIARIO PF,                                           \n"
        + "       NEMBO_T_ITER_PIANO_FINANZIARIO IPF,                                     \n"
        + "       NEMBO_D_STATO_PIANO_FINANZIARI SPF,                                    \n"
        + "       NEMBO_D_TIPO_PIANO_FINANZIARIO TPF                                      \n"
        + "     WHERE                                                                   \n"
        + "       PF.ID_PIANO_FINANZIARIO            = IPF.ID_PIANO_FINANZIARIO         \n"
        + "       AND IPF.ID_STATO_PIANO_FINANZIARIO = SPF.ID_STATO_PIANO_FINANZIARIO   \n"
        + "       AND PF.ID_TIPO_PIANO_FINANZIARIO   = TPF.ID_TIPO_PIANO_FINANZIARIO    \n"
        + "       AND PF.ID_PIANO_FINANZIARIO       IN                                  \n"
        + "       (                                                                     \n"
        + "         SELECT                                                              \n"
        + "           PF2.ID_PIANO_FINANZIARIO                                          \n"
        + "         FROM                                                                \n"
        + "           NEMBO_T_PIANO_FINANZIARIO PF2,                                      \n"
        + "           NEMBO_T_ITER_PIANO_FINANZIARIO IPF2                                 \n"
        + "         WHERE                                                               \n"
        + "           PF2.ID_PIANO_FINANZIARIO            = IPF2.ID_PIANO_FINANZIARIO   \n"
        + "           AND IPF2.DATA_FINE                 IS NULL                        \n"
        + "           AND IPF2.ID_STATO_PIANO_FINANZIARIO = :ID_STATO_PIANO_FINANZIARIO \n"
        + "       )                                                                     \n"
        + "     ORDER BY                                                                \n"
        + "       IPF.DATA_INIZIO DESC                                                  \n"
        + "   )                                                                         \n"
        + "   A                                                                         \n"
        + " WHERE                                                                       \n"
        + "   ROWNUM = 1                                                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_STATO_PIANO_FINANZIARIO",
          idStatoPianoFinanziario);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<PianoFinanziarioDTO>()
          {

            @Override
            public PianoFinanziarioDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              PianoFinanziarioDTO pianoFinanziarioDTO = null;
              while (rs.next())
              {
                if (pianoFinanziarioDTO == null)
                {
                  pianoFinanziarioDTO = new PianoFinanziarioDTO();
                  pianoFinanziarioDTO.setIdPianoFinanziario(
                      rs.getLong("ID_PIANO_FINANZIARIO"));
                  pianoFinanziarioDTO.setDataUltimoAggiornamento(
                      rs.getTimestamp("PF_DATA_ULTIMO_AGGIORNAMENTO"));
                  pianoFinanziarioDTO
                      .setDescrizione(rs.getString("PF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setExtIdUtenteAggiornamento(
                      rs.getLong("PF_EXT_ID_UTENTE_AGGIORNAMENTO"));
                  pianoFinanziarioDTO.setIdTipoPianoFinanziario(
                      rs.getLong("ID_TIPO_PIANO_FINANZIARIO"));
                  pianoFinanziarioDTO
                      .setDescTipo(rs.getString("TPF_DESCRIZIONE"));
                  pianoFinanziarioDTO.setCodiceTipoPianoFinanziario(
                      rs.getString("TPF_CODICE"));
                  pianoFinanziarioDTO
                      .setIter(new ArrayList<IterPianoFinanziarioDTO>());
                }
                IterPianoFinanziarioDTO iter = new IterPianoFinanziarioDTO();
                iter.setDataFine(rs.getTimestamp("IPF_DATA_FINE"));
                iter.setDataInizio(rs.getTimestamp("IPF_DATA_INIZIO"));
                iter.setExtIdUtenteAggiornamento(
                    rs.getLong("IPF_EXT_ID_UTENTE_AGGIORNAMENT"));
                iter.setIdIterPianoFinanziario(
                    rs.getLong("ID_ITER_PIANO_FINANZIARIO"));
                iter.setIdPianoFinanziario(
                    pianoFinanziarioDTO.getIdPianoFinanziario());
                iter.setIdStatoPianoFinanziario(
                    rs.getLong("ID_STATO_PIANO_FINANZIARIO"));
                iter.setDescStato(rs.getString("SPF_DESCRIZIONE"));
                pianoFinanziarioDTO.getIter().add(iter);
              }
              return pianoFinanziarioDTO;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idPianoFinanziario", idStatoPianoFinanziario) },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public List<PrioritaFocusAreaDTO> getElencoPrioritaFocusArea(
      long idPianoFinanziario) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoPrioritaFocusArea]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                                      \n"
        + "   P.ID_PRIORITA AS ID_PRIORITA,                            \n"
        + "   P.CODICE      AS P_CODICE,                               \n"
        + "   P.DESCRIZIONE AS P_DESCRIZIONE,                          \n"
        + "   FA.ID_FOCUS_AREA,                                        \n"
        + "   FA.CODICE      AS FA_CODICE,                             \n"
        + "   FA.DESCRIZIONE AS FA_DESCRIZIONE,                        \n"
        + "   (                                                        \n"
        + "     SELECT                                                 \n"
        + "       NVL(SUM(NVL(LFA.IMPORTO,0)),0)                       \n"
        + "     FROM                                                   \n"
        + "       NEMBO_R_LIVELLO_FOCUS_AREA LFA                         \n"
        + "     WHERE                                                  \n"
        + "       LFA.ID_FOCUS_AREA            = FA.ID_FOCUS_AREA      \n"
        + "       AND LFA.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO \n"
        + "   ) AS TOTALE_IMPORTI                                      \n"
        + " FROM                                                       \n"
        + "   NEMBO_D_PRIORITA P,                                        \n"
        + "   NEMBO_D_FOCUS_AREA FA                                      \n"
        + " WHERE                                                      \n"
        + "   P.ID_PRIORITA = FA.ID_PRIORITA                           \n"
        + " ORDER BY                                                   \n"
        + "   P.CODICE,                                                \n"
        + "   FA.CODICE                                                \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<PrioritaFocusAreaDTO>>()
          {

            @Override
            public List<PrioritaFocusAreaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<PrioritaFocusAreaDTO> list = new ArrayList<PrioritaFocusAreaDTO>();
              PrioritaFocusAreaDTO priorita = null;
              Long lastIdPriorita = null;
              long idPriorita = 0;
              while (rs.next())
              {
                idPriorita = rs.getLong("ID_PRIORITA");
                if (lastIdPriorita == null || lastIdPriorita != idPriorita)
                {
                  priorita = new PrioritaFocusAreaDTO();
                  priorita.setCodice(rs.getString("P_CODICE"));
                  priorita.setDescrizione(rs.getString("P_DESCRIZIONE"));
                  list.add(priorita);
                  lastIdPriorita = idPriorita;
                }
                DecodificaImportoDTO<String> focusArea = new DecodificaImportoDTO<String>();
                focusArea.setId(rs.getString("ID_FOCUS_AREA"));
                focusArea.setCodice(rs.getString("FA_CODICE"));
                focusArea.setDescrizione(rs.getString("FA_DESCRIZIONE"));
                focusArea.setImporto(rs.getBigDecimal("TOTALE_IMPORTI"));
                priorita.getElencoFocusArea().add(focusArea);
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, null,
          null, QUERY, (MapSqlParameterSource) null);
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

//TODO: FIXME: maintain
  
  public List<MisuraDTO> getElencoLivelli(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getElencoLivelli]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " WITH                                             \n"
        + "   RISORSE_BANDO AS                                                  \n"
        + "   (                                                                 \n"
        + "     SELECT                                                          \n"
        + "       RLB.ID_LIVELLO,                                               \n"
        + "       SUM(RLB.RISORSE_ATTIVATE) AS RISORSE_ATTIVATE,                \n"
        + "       SUM(IMPORTO_ECONOMIA)     AS IMPORTO_ECONOMIA                 \n"
        + "     FROM                                                            \n"
        + "       NEMBO_T_RISORSE_LIVELLO_BANDO RLB                               \n"
        + "     LEFT JOIN NEMBO_T_ECONOMIA E                                      \n"
        + "     ON                                                              \n"
        + "       RLB.ID_RISORSE_LIVELLO_BANDO = E.ID_RISORSE_LIVELLO_BANDO     \n"
        + "     GROUP BY                                                        \n"
        + "       RLB.ID_LIVELLO                                                \n"
        + "   )                                                                 \n"
        + " SELECT                                                              \n"
        + "   L.ID_LIVELLO,                                                     \n"
        + "   L.CODICE,                                                         \n"
        + "   L.DESCRIZIONE,                                                    \n"
        + "   L.ID_TIPOLOGIA_LIVELLO,                                           \n"
        + "   L.ID_TIPO_LIVELLO,   		                                        \n"
        + "   L.ID_SETTORE,                                                     \n"
        + "   S.CODICE AS COD_SETTORE,                                          \n"
        + "   LP.ID_LIVELLO_PADRE,                                              \n"
        + "   LFA.ID_FOCUS_AREA,                                                \n"
        + "   LFA.ID_PRIORITA,                                         	        \n"
        + "   LFA.IMPORTO,                                                      \n"
        + "   LFA.IMPORTO_TRASCINATO,                                           \n"
        + "   LFA.CODICE AS FA_CODICE,                                          \n"
        + "   RB.RISORSE_ATTIVATE,                                              \n"
        + "   ( SELECT COUNT(*)                                             	\n"
        + "		FROM NEMBO_T_STORICO_LIVELLO_FA FF, NEMBO_R_LIVELLO_FOCUS_AREA LFA2	\n"
        + "		WHERE FF.ID_LIVELLO_FOCUS_AREA = LFA2.ID_LIVELLO_FOCUS_AREA		\n"
        + "		AND LFA2.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO			\n"
        + "		AND LFA2.ID_LIVELLO = L.ID_LIVELLO								\n"
        + "		AND LFA2.ID_FOCUS_AREA = LFA.ID_FOCUS_AREA						\n"
        + "		AND DATA_CONSOLIDAMENTO IS NULL) AS NUMERO_RECORD_STORICO, 		\n"
        /* Aggiunta colonna Liquidato */
        + "   ( SELECT SUM(IL.IMPORTO_LIQUIDATO)								\n"
        + "		FROM 															\n"
        + "			NEMBO_T_IMPORTI_LIQUIDATI IL, 								\n"
        + "			NEMBO_T_LISTA_LIQUIDAZIONE LIS, 								\n"
        + "			NEMBO_R_LISTA_LIQUIDAZ_IMP_LIQ RLL							\n"
        + "		WHERE															\n"
        + "			RLL.FLAG_ESITO_LIQUIDAZIONE = 'S'							\n"
        + "			AND IL.ID_IMPORTI_LIQUIDATI= RLL.ID_IMPORTI_LIQUIDATI		\n"
        + "			AND LIS.ID_LISTA_LIQUIDAZIONE = RLL.ID_LISTA_LIQUIDAZIONE	\n"
        + "			AND LIS.FLAG_STATO_LISTA IN ('A','T')						\n"
        + "			AND IL.ID_LIVELLO = L.ID_LIVELLO) AS PAGATO,                \n"
        /* fine */
        + "   RB.IMPORTO_ECONOMIA                                               \n"
        + " FROM                                                                \n"
        + "   NEMBO_D_LIVELLO L,                                                  \n"
        + "   NEMBO_R_LIV_PIANO_FINANZIARIO LPF,                                  \n"
        + "   NEMBO_R_LIVELLO_PADRE LP,                                           \n"
        + "   NEMBO_D_TIPO_PIANO_FINANZIARIO TPF,                                 \n"
        + "   NEMBO_T_PIANO_FINANZIARIO PF,                                       \n"
        + "   NEMBO_D_SETTORE S,                                                  \n"
        + "   (                                                                 \n"
        + "     SELECT                                                          \n"
        + "       L_FA.ID_LIVELLO,                                              \n"
        + "       L_FA.ID_FOCUS_AREA,                                           \n"
        + "       L_FA.IMPORTO,                                                 \n"
        + "       L_FA.IMPORTO_TRASCINATO,                                      \n"
        + "       FA.CODICE,                                                    \n"
        + "       FA.ID_PRIORITA                                                \n"
        + "     FROM                                                            \n"
        + "       NEMBO_D_FOCUS_AREA FA,                                          \n"
        + "       NEMBO_R_LIVELLO_FOCUS_AREA L_FA                                 \n"
        + "     WHERE                                                           \n"
        + "       L_FA.ID_FOCUS_AREA            = FA.ID_FOCUS_AREA              \n"
        + "       AND L_FA.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO         \n"
        + "   )                                                                 \n"
        + "   LFA,                                                              \n"
        + "   RISORSE_BANDO RB                                                  \n"
        + " WHERE                                                               \n"
        + "   PF.ID_PIANO_FINANZIARIO           = :ID_PIANO_FINANZIARIO         \n"
        + "   AND PF.ID_TIPO_PIANO_FINANZIARIO  = TPF.ID_TIPO_PIANO_FINANZIARIO \n"
        + "   AND L.ID_LIVELLO                  = LPF.ID_LIVELLO                \n"
        + "   AND L.ID_SETTORE                  = S.ID_SETTORE(+)               \n"
        + "   AND LPF.ID_TIPO_PIANO_FINANZIARIO = TPF.ID_TIPO_PIANO_FINANZIARIO \n"
        + "   AND L.ID_LIVELLO                  = LP.ID_LIVELLO(+)              \n"
        + "   AND L.ID_EDIZIONE_PSR             = 1                             \n"
        + "   AND L.ID_LIVELLO                  = LFA.ID_LIVELLO(+)             \n"
        + "   AND L.ID_LIVELLO                  = RB.ID_LIVELLO(+)              \n"
        + " ORDER BY                                                            \n"
        + "   L.ORDINAMENTO,                                                    \n"
        + "   LFA.ID_FOCUS_AREA                                                 \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
        Types.INTEGER);
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
              while (rs.next())
              {
                long idLivello = rs.getLong("ID_LIVELLO");
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
                  livelloCorrente
                      .setIdTipoLivello(rs.getLong("ID_TIPO_LIVELLO"));

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
                    importo.setIdPriorita(rs.getLong("ID_PRIORITA"));
                    importo.setNumeroRecordStorico(
                        rs.getLong("NUMERO_RECORD_STORICO"));
                    importo.setCodice(rs.getString("FA_CODICE"));
                    importo.setImporto(rs.getBigDecimal("IMPORTO"));
                    importo.setImportoTrascinato(
                        rs.getBigDecimal("IMPORTO_TRASCINATO"));
                    importo.setImportoPagato(
                        NemboUtils.NUMBERS.nvl(rs.getBigDecimal("PAGATO")));
                    // L'elenco è sicuramente != null in quanto l'oggetto
                    // TipoOperazione crea la lista nel costruttore
                    operazione.getElenco().add(importo);
                    operazione.setRisorseAttivate(NemboUtils.NUMBERS
                        .nvl(rs.getBigDecimal("RISORSE_ATTIVATE")));
                    operazione.setImportoPagato(
                        NemboUtils.NUMBERS.nvl(rs.getBigDecimal("PAGATO")));
                    operazione.setImportoEconomia(NemboUtils.NUMBERS
                        .nvl(rs.getBigDecimal("IMPORTO_ECONOMIA")));
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
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("idPianoFinanziario", idPianoFinanziario) }, null,
          QUERY, mapParameterSource);
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

  public void insertRisorsa(long idPianoFinanziario, long idLivello,
      long idFocusArea, BigDecimal importo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::insertRisorsa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT                                                                                   \n"
        + " INTO                                                                                                     \n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA                                                                               \n"
        + "   (                                                                                                      \n"
        + "     ID_LIVELLO_FOCUS_AREA,                                                                               \n"
        + "     ID_FOCUS_AREA,                                                                                       \n"
        + "     ID_LIVELLO,                                                                                          \n"
        + "     IMPORTO,                                                                                             \n"
        + "     ID_PIANO_FINANZIARIO                                                                                 \n"
        + "   )                                                                                                      \n"
        + "   VALUES                                                                                                 \n"
        + "   (                                                                                                      \n"
        + "     SEQ_NEMBO_R_LIVELLO_FOCUS_AREA.NEXTVAL,                                                                \n"
        + "     :ID_FOCUS_AREA,                                                                                      \n"
        + "     (SELECT L.ID_LIVELLO FROM NEMBO_D_LIVELLO L WHERE L.ID_LIVELLO = :ID_LIVELLO AND L.DATA_FINE IS NULL), \n"
        + "     :IMPORTO,                                                                                            \n"
        + "     :ID_PIANO_FINANZIARIO                                                                                \n"
        + "   )                                                                                                      \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_FOCUS_AREA", idFocusArea, Types.NUMERIC);
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idLivello", idLivello),
              new LogParameter("idFocusArea", idFocusArea),
              new LogParameter("importo", importo)
          },
          new LogVariable[]
          {}, INSERT, mapParameterSource);
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

  public void updateRisorsa(long idLivelloFocusArea, BigDecimal importo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateRisorsa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                           \n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA                       \n"
        + " SET                                              \n"
        + "   IMPORTO = :IMPORTO                             \n"
        + " WHERE                                            \n"
        + "   ID_LIVELLO_FOCUS_AREA = :ID_LIVELLO_FOCUS_AREA \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_LIVELLO_FOCUS_AREA", idLivelloFocusArea,
          Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idLivelloFocusArea", idLivelloFocusArea),
              new LogParameter("importo", importo)
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
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

  public Long getIdLivelloFocusArea(long idPianoFinanziario, long idLivello,
      long idFocusArea)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIdLivelloFocusArea]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                              \n"
        + "   LFA.ID_LIVELLO_FOCUS_AREA                        \n"
        + " FROM                                               \n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA LFA                     \n"
        + " WHERE                                              \n"
        + "   LFA.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO \n"
        + "   AND LFA.ID_FOCUS_AREA    = :ID_FOCUS_AREA        \n"
        + "   AND LFA.ID_LIVELLO       = :ID_LIVELLO           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_FOCUS_AREA", idFocusArea, Types.NUMERIC);
      return queryForLong(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idLivello", idLivello),
              new LogParameter("idFocusArea", idFocusArea)
          },
          new LogVariable[]
          {}, QUERY, mapParameterSource);
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

  public TipoOperazioneDTO getTipoOperazionePianoFinanziario(
      long idPianoFinanziario, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::getTipoOperazionePianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                                               \n"
        + "   L.ID_LIVELLO,                                                     \n"
        + "   L.CODICE,                                                         \n"
        + "   L.DESCRIZIONE,                                                    \n"
        + "   L.ID_TIPOLOGIA_LIVELLO,                                           \n"
        + "   L.ID_SETTORE,                                                     \n"
        + "   S.CODICE AS COD_SETTORE,                                          \n"
        + "   LP.ID_LIVELLO_PADRE,                                              \n"
        + "   LFA.ID_FOCUS_AREA,                                                \n"
        + "   LFA.IMPORTO,                                                      \n"
        + "   LFA.CODICE AS FA_CODICE                                           \n"
        + " FROM                                                                \n"
        + "   NEMBO_D_LIVELLO L,                                                  \n"
        + "   NEMBO_R_LIV_PIANO_FINANZIARIO LPF,                                  \n"
        + "   NEMBO_R_LIVELLO_PADRE LP,                                           \n"
        + "   NEMBO_D_TIPO_PIANO_FINANZIARIO TPF,                                 \n"
        + "   NEMBO_T_PIANO_FINANZIARIO PF,                                       \n"
        + "   NEMBO_D_SETTORE S,                                                  \n"
        + "   (                                                                 \n"
        + "     SELECT                                                          \n"
        + "       L_FA.ID_LIVELLO,                                              \n"
        + "       L_FA.ID_FOCUS_AREA,                                           \n"
        + "       L_FA.IMPORTO,                                                 \n"
        + "       FA.CODICE                                                     \n"
        + "     FROM                                                            \n"
        + "       NEMBO_D_FOCUS_AREA FA,                                          \n"
        + "       NEMBO_R_LIVELLO_FOCUS_AREA L_FA                                 \n"
        + "     WHERE                                                           \n"
        + "       L_FA.ID_FOCUS_AREA            = FA.ID_FOCUS_AREA              \n"
        + "       AND L_FA.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO         \n"
        + "   )                                                                 \n"
        + "   LFA                                                               \n"
        + " WHERE                                                               \n"
        + "   PF.ID_PIANO_FINANZIARIO           = :ID_PIANO_FINANZIARIO         \n"
        + "   AND PF.ID_TIPO_PIANO_FINANZIARIO  = TPF.ID_TIPO_PIANO_FINANZIARIO \n"
        + "   AND L.ID_LIVELLO                  = LPF.ID_LIVELLO                \n"
        + "   AND L.ID_SETTORE                  = S.ID_SETTORE(+)               \n"
        + "   AND LPF.ID_TIPO_PIANO_FINANZIARIO = TPF.ID_TIPO_PIANO_FINANZIARIO \n"
        + "   AND L.ID_LIVELLO                  = LP.ID_LIVELLO(+)              \n"
        + "   AND L.ID_EDIZIONE_PSR             = 1                             \n"
        + "   AND L.ID_LIVELLO                  = LFA.ID_LIVELLO(+)             \n"
        + "   AND L.ID_LIVELLO                  = :ID_LIVELLO                   \n"
        + " ORDER BY                                                            \n"
        + "   L.ORDINAMENTO,                                                    \n"
        + "   LFA.ID_FOCUS_AREA                                                 \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
    try
    {
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<TipoOperazioneDTO>()
          {
            @Override
            public TipoOperazioneDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              TipoOperazioneDTO livelloCorrente = null;
              while (rs.next())
              {
                long idLivello = rs.getLong("ID_LIVELLO");
                if (livelloCorrente == null)
                {
                  livelloCorrente = new TipoOperazioneDTO();
                  livelloCorrente.setIdLivello(idLivello);
                  livelloCorrente.setCodice(rs.getString("CODICE"));
                  livelloCorrente.setDescrizione(rs.getString("DESCRIZIONE"));
                  livelloCorrente.setIdTipologiaLivello(
                      rs.getLong("ID_TIPOLOGIA_LIVELLO"));
                  Long idSettore = getLongNull(rs, "ID_SETTORE");
                  livelloCorrente
                      .setIdLivelloPadre(rs.getLong("ID_LIVELLO_PADRE"));
                  if (idSettore != null)
                  {
                    livelloCorrente.setIdSettore(idSettore);
                    livelloCorrente
                        .setCodiceSettore(rs.getString("COD_SETTORE"));
                  }
                }
                Long idFocusArea = getLongNull(rs, "ID_FOCUS_AREA");
                if (idFocusArea != null)
                {
                  ImportoFocusAreaDTO importo = new ImportoFocusAreaDTO();
                  importo.setIdFocusArea(idFocusArea);
                  importo.setCodice(rs.getString("FA_CODICE"));
                  importo.setImporto(rs.getBigDecimal("IMPORTO"));
                  // L'elenco è sicuramente != null in quanto l'oggetto
                  // TipoOperazione crea la lista nel costruttore
                  livelloCorrente.getElenco().add(importo);
                }
              }
              return livelloCorrente;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idLivello", idLivello)
          }, null, QUERY, mapParameterSource);
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

  public ImportoFocusAreaDTO getImportoPianoFinanziario(long idPianoFinanziario,
      long idLivello) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                              \n"
        + "   LFA.ID_LIVELLO,                                      \n"
        + "   LFA.ID_FOCUS_AREA,                                   \n"
        + "   LFA.IMPORTO,                                         \n"
        + "   LFA.IMPORTO_TRASCINATO,                              \n"
        + "   FA.CODICE                                            \n"
        + " FROM                                                   \n"
        + "   NEMBO_D_FOCUS_AREA FA,                                 \n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA LFA                         \n"
        + " WHERE                                                  \n"
        + "   LFA.ID_FOCUS_AREA            = FA.ID_FOCUS_AREA      \n"
        + "   AND LFA.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO \n"
        + "   AND LFA.ID_LIVELLO = :ID_LIVELLO                     \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
    try
    {
      return queryForObject(QUERY, mapParameterSource,
          ImportoFocusAreaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idLivello", idLivello),
          }, null, QUERY, mapParameterSource);
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

  public void deleteRisorsa(long idPianoFinanziario, long idLivello,
      long idFocusArea) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::deleteRisorsa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String DELETE = " DELETE                        \n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA                    \n"
        + " WHERE                                         \n"
        + "   ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO\n"
        + "  AND ID_LIVELLO  = :ID_LIVELLO                \n"
        + "  AND ID_FOCUS_AREA = :ID_FOCUS_AREA           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_FOCUS_AREA", idFocusArea, Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idLivello", idLivello),
              new LogParameter("idFocusArea", idFocusArea)
          },
          new LogVariable[]
          {}, DELETE, mapParameterSource);
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

  public void updateRigaPianoFinanziario(long idPianoFinanziario,
      long idLivello, BigDecimal importo, BigDecimal importoTrascinato)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::updateRigaPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                         \n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA                     \n"
        + " SET                                            \n"
        + "   IMPORTO            = :IMPORTO,               \n"
        + "   IMPORTO_TRASCINATO = :TRASCINATO             \n"
        + " WHERE                                          \n"
        + "   ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO \n"
        + "   AND ID_LIVELLO       = :ID_LIVELLO           \n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);
      mapParameterSource.addValue("TRASCINATO", importoTrascinato,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idLivello", idLivello),
              new LogParameter("importo", importo),
              new LogParameter("importoTrascinato", importoTrascinato),
          },
          new LogVariable[]
          {}, UPDATE, mapParameterSource);
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

  public long insertPianoFinanziarioStoricizzato(
      PianoFinanziarioDTO pianoFinanziarioDTO)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::insertPianoFinanziarioStoricizzato]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = " INSERT              \n"
        + " INTO                              	\n"
        + "   NEMBO_T_PIANO_FINANZIARIO         	\n"
        + "   (                               	\n"
        + "     ID_PIANO_FINANZIARIO,         	\n"
        + "     DESCRIZIONE,                  	\n"
        + "     DATA_ULTIMO_AGGIORNAMENTO,    	\n"
        + "     EXT_ID_UTENTE_AGGIORNAMENTO,  	\n"
        + "     PIANO_STORICIZZATO,  			\n"
        + "     ID_TIPO_PIANO_FINANZIARIO     	\n"
        + "   )                               	\n"
        + "   VALUES                          	\n"
        + "   (                               	\n"
        + "     :ID_PIANO_FINANZIARIO,        	\n"
        + "     :DESCRIZIONE,                 	\n"
        + "     SYSDATE,                      	\n"
        + "     :EXT_ID_UTENTE_AGGIORNAMENTO, 	\n"
        + "     :PIANO_STORICIZZATO, 			\n"
        + "     :ID_TIPO_PIANO_FINANZIARIO    	\n"
        + "   )                               	\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    long idPiano;
    try
    {
      idPiano = getNextSequenceValue(
          NemboConstants.SQL.SEQUENCE.NEMBO_T_PIANO_FINANZIARIO);
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPiano,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE",
          pianoFinanziarioDTO.getDescrizione(), Types.VARCHAR);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          pianoFinanziarioDTO.getExtIdUtenteAggiornamento(), Types.NUMERIC);
      mapParameterSource.addValue("PIANO_STORICIZZATO",
          new SqlLobValue(pianoFinanziarioDTO.getPianoStoricizzato()),
          Types.BLOB);
      mapParameterSource.addValue("ID_TIPO_PIANO_FINANZIARIO",
          pianoFinanziarioDTO.getIdTipoPianoFinanziario(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idPiano;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          { new LogParameter("pianoFinanziarioDTO", pianoFinanziarioDTO) },
          new LogVariable[]
          {}, INSERT, mapParameterSource);
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

  public byte[] getExcelPianoFinanziario(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getExcelPianoFinanziario";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                            		\n"
          + "   PIANO_STORICIZZATO                                 	\n"
          + " FROM                                            		\n"
          + "   NEMBO_T_PIANO_FINANZIARIO Q                             \n"
          + " WHERE                                            		\n"
          + "  Q.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO			\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<byte[]>()
          {
            @Override
            public byte[] extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              rs.next();
              byte[] img = rs.getBytes("PIANO_STORICIZZATO");
              return img;
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

  public void updateDataConsolidamento(long idPianoFinanziario)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateDataConsolidamento";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                                   	\n"
          + "   NEMBO_T_STORICO_LIVELLO_FA                               	\n"
          + " SET                                                      	\n"
          + "   DATA_CONSOLIDAMENTO = SYSDATE                         	\n"
          + " WHERE                                                    	\n"
          + "   DATA_CONSOLIDAMENTO IS NULL				           		\n"
          + "	AND ID_LIVELLO_FOCUS_AREA IN (			           		\n"
          + "			SELECT 			           						\n"
          + "				ID_LIVELLO_FOCUS_AREA			           	\n"
          + "			FROM			           						\n"
          + "				NEMBO_R_LIVELLO_FOCUS_AREA 			        \n"
          + "			WHERE 			           						\n"
          + "				ID_PIANO_FINANZIARIO=:ID_PIANO_FINANZIARIO) \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PIANO_FINANZIARIO", idPianoFinanziario)
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

  public void updateTStoricoLivelloFA(long idPianoFinanziario, long idLivello,
      BigDecimal importo,
      BigDecimal importoTrascinato, String motivazioni,
      long extIdUtenteAggiornamento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateTStoricoLivelloFA";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      final String INSERT = " INSERT              								\n"
          + " INTO                              							\n"
          + "   NEMBO_T_STORICO_LIVELLO_FA         							\n"
          + "   (                               							\n"
          + "     ID_STORICO_LIVELLO_FA,         							\n"
          + "     ID_LIVELLO_FOCUS_AREA,          						\n"
          + "     IMPORTO, 					   							\n"
          + "     IMPORTO_TRASCINATO,			   							\n"
          + "     DATA_INSERIMENTO, 			 							\n"
          + "     DATA_CONSOLIDAMENTO,  									\n"
          + "     MOTIVAZIONE,			     							\n"
          + "     EXT_ID_UTENTE_AGGIORNAMENTO    							\n"
          + "   )                               							\n"
          + "   VALUES                          							\n"
          + "   (                               							\n"
          + "     SEQ_NEMBO_T_STORICO_LIVELLO_FA.NEXTVAL,					\n"
          + "     (	SELECT 												\n"
          + "				ID_LIVELLO_FOCUS_AREA 							\n"
          + "			FROM												\n"
          + "				NEMBO_R_LIVELLO_FOCUS_AREA 						\n"
          + "			WHERE 												\n"
          + "				ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO 	\n"
          + "				AND ID_LIVELLO=:ID_LIVELLO) ,       			\n"
          + "     :IMPORTO, 					   							\n"
          + "     :IMPORTO_TRASCINATO,			   						\n"
          + "     SYSDATE, 			 									\n"
          + "     NULL,  													\n"
          + "     :MOTIVAZIONI,			     							\n"
          + "     :EXT_ID_UTENTE_AGGIORNAMENTO    						\n"
          + "   )                               							\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_UTENTE_AGGIORNAMENTO",
          extIdUtenteAggiornamento, Types.NUMERIC);
      mapParameterSource.addValue("MOTIVAZIONI", motivazioni, Types.VARCHAR);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO_TRASCINATO", importoTrascinato,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_PIANO_FINANZIARIO", idPianoFinanziario),
              new LogParameter("ID_LIVELLO", idLivello),
              new LogParameter("EXT_ID_UTENTE_AGGIORNAMENTO",
                  extIdUtenteAggiornamento),
              new LogParameter("IMPORTO", importo),
              new LogParameter("IMPORTO_TRASCINATO", importoTrascinato),

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

  public List<ImportoFocusAreaDTO> getStoricoImportiPianoFinanziario(
      long idPianoFinanziario, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getImportoPianoFinanziario]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    final String QUERY = " SELECT                              			\n"
        + "   LFA.ID_LIVELLO,                                      			\n"
        + "   LFA.ID_FOCUS_AREA,                                   			\n"
        + "   SLFA.IMPORTO,                                         		\n"
        + "   SLFA.IMPORTO_TRASCINATO,                              		\n"
        + "   SLFA.MOTIVAZIONE AS MOTIVAZIONI,                    			\n"
        + "   SLFA.DATA_INSERIMENTO,							            \n"
        + "   SLFA.EXT_ID_UTENTE_AGGIORNAMENTO,					            \n"
        + "   FA.CODICE                                            			\n"
        + " FROM                                                   			\n"
        + "   NEMBO_D_FOCUS_AREA FA,                                 			\n"
        + "   NEMBO_R_LIVELLO_FOCUS_AREA LFA,	                       			\n"
        + "   NEMBO_T_STORICO_LIVELLO_FA	SLFA                       			\n"
        + " WHERE                                                  			\n"
        + "   LFA.ID_FOCUS_AREA = FA.ID_FOCUS_AREA      					\n"
        + "   AND SLFA.ID_LIVELLO_FOCUS_AREA = LFA.ID_LIVELLO_FOCUS_AREA	\n"
        + "   AND SLFA.DATA_CONSOLIDAMENTO IS NULL							\n"
        + "   AND LFA.ID_PIANO_FINANZIARIO = :ID_PIANO_FINANZIARIO 			\n"
        + "   AND LFA.ID_LIVELLO = :ID_LIVELLO                     			\n"
        + "	ORDER BY SLFA.DATA_INSERIMENTO DESC								\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_PIANO_FINANZIARIO", idPianoFinanziario,
        Types.NUMERIC);
    mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
    try
    {
      return queryForList(QUERY, mapParameterSource, ImportoFocusAreaDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idPianoFinanziario", idPianoFinanziario),
              new LogParameter("idLivello", idLivello),
          }, null, QUERY, mapParameterSource);
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
  
  public void setFlagConsolidaS(int anno, int trimestreCorrente)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::setFlagConsolidaS]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                         	\n"
        + "   NEMBO_T_DICHIARAZIONE_SPESA	                 	\n"
        + " SET                                            	\n"
        + "   FLAG_STATO_DICHIARAZIONE = 'N'                \n"
        + " WHERE                                         	\n"
        + "   ANNO = :ANNO               					\n"
        + "	  AND PROGRESSIVO = :PROGRESSIVO				\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {

      mapParameterSource.addValue("ANNO", anno, Types.NUMERIC);
      mapParameterSource.addValue("PROGRESSIVO", trimestreCorrente,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
          null, UPDATE, mapParameterSource);
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

  public void updateImportoDichiarazioneSpesa(Long idLivello, Long ID, int anno,
      Integer trimestre, BigDecimal importo, boolean hasPriorita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::updateImportoDichiarazioneSpesa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String colonna = "";
    if (hasPriorita)
    {
      mapParameterSource.addValue("ID", ID, Types.NUMERIC);
      colonna = "ID_PRIORITA";
    }
    else
    {
      mapParameterSource.addValue("ID", ID, Types.NUMERIC);
      colonna = "ID_FOCUS_AREA";
    }

    String UPDATE = " UPDATE                         			\n"
        + "   NEMBO_T_IMPORTO_DICHIARAZIONE                 		\n"
        + " SET                                            		\n"
        + "   IMPORTO_SPESA = :IMPORTO			                \n"
        + " WHERE                                         		\n"
        + "   " + colonna + " = :ID           	     			\n"
        + "   AND ID_LIVELLO = :ID_LIVELLO  	              	\n"
        + "	  AND ID_DICHIARAZIONE_SPESA = (	              	\n"
        + "		SELECT ID_DICHIARAZIONE_SPESA 	              	\n"
        + "		FROM NEMBO_T_DICHIARAZIONE_SPESA	              	\n"
        + " 	WHERE                                         	\n"
        + "   " + colonna + " = :ID           	     			\n"
        + "   		AND ID_LIVELLO = :ID_LIVELLO  	            \n"
        + "   		AND FLAG_STATO_DICHIARAZIONE = 'N'          \n"
        // + " AND ANNO = :ANNO \n"
        + "   		AND PROGRESSIVO = :PROGRESSIVO)  	        \n";

    try
    {

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("PROGRESSIVO", trimestre, Types.NUMERIC);
      mapParameterSource.addValue("ANNO", anno, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
          null, UPDATE, mapParameterSource);
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

  public boolean existsImportoLivFA(Long idLivello, Long ID, int anno,
      Integer trimestre, BigDecimal importo, boolean hasPriorita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "existsImportoLivFA";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      String colonna = "";
      if (hasPriorita)
      {
        mapParameterSource.addValue("ID", ID, Types.NUMERIC);
        colonna = "ID.ID_PRIORITA";
      }
      else
      {
        mapParameterSource.addValue("ID", ID, Types.NUMERIC);
        colonna = "ID.ID_FOCUS_AREA";
      }

      SELECT = " SELECT 																	\n"
          + "		ID.ID_DICHIARAZIONE_SPESA 	           								\n"
          + "	FROM 																	\n"
          + "		NEMBO_T_DICHIARAZIONE_SPESA DS,										\n"
          + "		NEMBO_T_IMPORTO_DICHIARAZIONE ID										\n"
          + " WHERE                                       							\n"
          // + " ANNO = :ANNO \n"
          + "   	 FLAG_STATO_DICHIARAZIONE = 'N'										\n"
          + "     AND ID.ID_LIVELLO = :ID_LIVELLO										\n"
          + "     AND ID.ID_DICHIARAZIONE_SPESA  (+)= DS.ID_DICHIARAZIONE_SPESA     	\n"
          + "     AND " + colonna + " (+)= :ID					 						\n";

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("PROGRESSIVO", trimestre, Types.NUMERIC);
      mapParameterSource.addValue("ANNO", anno, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                return true;
              }
              return false;
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

  public void insertImportoDichiarazioneSpesa(Long idLivello, Long ID, int anno,
      Integer trimestre,
      BigDecimal importo, boolean hasPriorita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS
        + "::insertImportoDichiarazioneSpesa]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }

    String UPDATE = " INSERT INTO    	                		\n"
        + "   NEMBO_T_IMPORTO_DICHIARAZIONE                 		\n"
        + " (	ID_IMPORTO_DICHIARAZIONE,                  		\n"
        + "		ID_DICHIARAZIONE_SPESA,                   		\n"
        + "		ID_LIVELLO, 									\n"
        + "		ID_FOCUS_AREA,                  				\n"
        + "		ID_PRIORITA,        			          		\n"
        + "		IMPORTO_SPESA)                  				\n"
        + "	VALUES(    						              		\n"
        + "		SEQ_NEMBO_T_IMPORTO_DICHIARAZI.NEXTVAL,       	\n"
        + "		(	SELECT ID_DICHIARAZIONE_SPESA 	            \n"
        + "			FROM NEMBO_T_DICHIARAZIONE_SPESA	            \n"
        + " 		WHERE                                       \n"
        // + " ANNO = :ANNO \n"
        + "   		 FLAG_STATO_DICHIARAZIONE = 'N'),		\n"
        + " 	:ID_LIVELLO, 									\n";

    if (!hasPriorita)
      UPDATE += "		:ID_FOCUS_AREA,							\n"
          + "		NULL,									\n";
    else
      UPDATE += "		NULL,									\n"
          + "		:ID_PRIORITA,							\n";

    UPDATE += "		:IMPORTO)                               \n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      if (hasPriorita)
        mapParameterSource.addValue("ID_PRIORITA", ID, Types.NUMERIC);
      else
        mapParameterSource.addValue("ID_FOCUS_AREA", ID, Types.NUMERIC);

      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("PROGRESSIVO", trimestre, Types.NUMERIC);
      mapParameterSource.addValue("ANNO", anno, Types.NUMERIC);
      mapParameterSource.addValue("IMPORTO", importo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
          null, UPDATE, mapParameterSource);
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

  public BigDecimal getDatiVistaSigop(int anno, long trimestre,
      String codLivello, long idFocusArea, boolean hasPriorita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDatiVistaSigop";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                  	\n"
          + " 	SUM(IMPORTO_QUOTA_NETTO) AS TOT  						\n"
          + " FROM                                                      \n"
          + "   SIGOP.SIGOP_V_PAGAMENTI                                 \n"
          + " WHERE                                                    	\n"
          + "   DATA_DECRETO BETWEEN :DATA_INIZIO AND :DATA_FINE		\n"
          + "	AND QUOTA = '1 - UE'									\n"
          + " 	AND MISURA = :COD_MISURA							   	\n";
      if (!hasPriorita)
        SELECT += " AND FOCUS_AREA = (SELECT CODICE FROM NEMBO_D_FOCUS_AREA WHERE ID_FOCUS_AREA =:ID_FOCUS_AREA)				   		\n";
      else
        SELECT += " AND FOCUS_AREA IN (SELECT CODICE FROM NEMBO_D_FOCUS_AREA WHERE ID_PRIORITA =:ID_PRIORITA)				   			\n";

      Date inizioTrimestre1 = new GregorianCalendar(anno, Calendar.JANUARY, 01)
          .getTime();
      Date fineTrimestre1 = new GregorianCalendar(anno, Calendar.MARCH, 31)
          .getTime();
      Date inizioTrimestre2 = new GregorianCalendar(anno, Calendar.APRIL, 01)
          .getTime();
      Date fineTrimestre2 = new GregorianCalendar(anno, Calendar.JUNE, 30)
          .getTime();
      Date inizioTrimestre3 = new GregorianCalendar(anno, Calendar.JULY, 01)
          .getTime();
      Date fineTrimestre3 = new GregorianCalendar(anno, Calendar.OCTOBER, 15)
          .getTime();
      Date inizioTrimestre4 = new GregorianCalendar(anno, Calendar.OCTOBER, 06)
          .getTime();
      Date fineTrimestre4 = new GregorianCalendar(anno, Calendar.DECEMBER, 31)
          .getTime();
      Date dataInizio = null;
      Date dataFine = null;
      if (trimestre == 1)
      {
        dataInizio = inizioTrimestre1;
        dataFine = fineTrimestre1;
      }
      else
        if (trimestre == 2)
        {
          dataInizio = inizioTrimestre2;
          dataFine = fineTrimestre2;
        }
        else
          if (trimestre == 3)
          {
            dataInizio = inizioTrimestre3;
            dataFine = fineTrimestre3;
          }
          else
          {
            dataInizio = inizioTrimestre4;
            dataFine = fineTrimestre4;
          }

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("DATA_INIZIO", dataInizio, Types.DATE);
      parameterSource.addValue("DATA_FINE", dataFine, Types.DATE);

      if (codLivello.length() == 1)
        codLivello = "0" + codLivello;

      parameterSource.addValue("COD_MISURA", codLivello, Types.VARCHAR);

      if (!hasPriorita)
        parameterSource.addValue("ID_FOCUS_AREA", idFocusArea, Types.NUMERIC);
      else
        parameterSource.addValue("ID_PRIORITA", idFocusArea, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BigDecimal>()
          {
            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getBigDecimal("TOT");
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
  
 public void resetImporti(int anno, int trimestreCorrente)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::resetImporti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String UPDATE = " UPDATE                         	\n"
        + "   NEMBO_T_IMPORTO_DICHIARAZIONE		            \n"
        + " SET                                            	\n"
        + "   IMPORTO_SPESA = -1                			\n"
        + " WHERE 											\n"
        + "		ID_DICHIARAZIONE_SPESA =         			\n"
        + " (SELECT 										\n"
        + "		ID_DICHIARAZIONE_SPESA          			\n"
        + "	 FROM											\n"
        + "		 NEMBO_T_DICHIARAZIONE_SPESA           		\n"
        + "	 WHERE                                   		\n"
        + "   	ANNO = :ANNO               					\n"
        + "	  	AND PROGRESSIVO = :PROGRESSIVO	)			\n";
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      if (trimestreCorrente == 4)
      {
        trimestreCorrente = 1;
        anno++;
      }
      mapParameterSource.addValue("ANNO", anno, Types.NUMERIC);
      mapParameterSource.addValue("PROGRESSIVO", trimestreCorrente,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {},
          null, UPDATE, mapParameterSource);
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
