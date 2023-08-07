package it.csi.nembo.nemboconf.integration;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.SqlLobValue;

import it.csi.nembo.nemboconf.dto.AttoreVO;
import it.csi.nembo.nemboconf.dto.ComuneBandoDTO;
import it.csi.nembo.nemboconf.dto.CriterioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.ElencoQueryBandoDTO;
import it.csi.nembo.nemboconf.dto.FilieraDTO;
import it.csi.nembo.nemboconf.dto.SoluzioneDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.InfoMisurazioneIntervento;
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
import it.csi.nembo.nemboconf.dto.internal.LogParameter;
import it.csi.nembo.nemboconf.dto.internal.LogVariable;
import it.csi.nembo.nemboconf.dto.reportistica.CellReportVO;
import it.csi.nembo.nemboconf.dto.reportistica.ColReportVO;
import it.csi.nembo.nemboconf.dto.reportistica.GraficoVO;
import it.csi.nembo.nemboconf.dto.reportistica.ParametriQueryReportVO;
import it.csi.nembo.nemboconf.dto.reportistica.ReportVO;
import it.csi.nembo.nemboconf.dto.reportistica.RowsReportVO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class CruscottoBandiDAO extends BaseDAO
{

  private static final String THIS_CLASS = CruscottoBandiDAO.class
      .getSimpleName();

  public CruscottoBandiDAO()
  {
  }

  public List<BandoDTO> getElencoBandiDisponibili(boolean isBandoGal,
      List<Long> idsBandiGal) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoBandiDisponibili";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                                                             \n"
          + "   A.ID_BANDO,                                                                                                                      \n"
          + "   A.DATA_INIZIO,                                                                                                                   \n"
          + "   A.DATA_FINE,                                                                                                                     \n"
          + "   A.DENOMINAZIONE,                                                                                                                 \n"
          + "   A.ANNO_CAMPAGNA,                                                                                                                 \n"
          + "   EC.ID_EVENTO_CALAMITOSO,                                                                                                                     \n"
          + "   EC.DATA_EVENTO,                                                                                                                     \n"
          + "   EC.DESCRIZIONE AS DESC_EVENTO_CALAMITOSO,                                                                                                                     \n"
          + "   CE.ID_CATEGORIA_EVENTO,                                                                                                                     \n"
          + "   CE.DESCRIZIONE AS DESC_CAT_EVENTO,                                                                                                                     \n"
          + "   DECODE(A.FLAG_TITOLARITA_REGIONALE,'S','Si','N','No','') AS FLAG_TITOLARITA_REGIONALE,                                           \n"
          + "   B.DESCRIZIONE AS DESCR_TIPO_BANDO,                                                                                               \n"
          + "   DECODE((SELECT COUNT(*) FROM NEMBO_R_BANDO_OGGETTO BO WHERE BO.ID_BANDO=A.ID_BANDO AND BO.FLAG_ATTIVO = 'S'),0,'N','S') AS ATTIVO, \n"
          + "   L.ID_LIVELLO,			                                                                                                         \n"
          + "   L.DESCRIZIONE,                                                                                                                   \n"
          + "   L.CODICE,                                                                                                                        \n"
          + "   L.CODICE_LIVELLO,                                                                                                                \n"

          + "   ALLEG.ID_ALLEGATI_BANDO,                                                                                                         \n"
          + "   ALLEG.DESCRIZIONE DESCR_FILE,                                                                                                    \n"
          + "   ALLEG.NOME_FILE,                                                                                                                 \n"
          + "   ALLEG.ORDINE,                                                                                                                    \n"

          + "   VISTAAMM.ID_AMM_COMPETENZA,                                                                                                      \n"
          + "   VISTAAMM.DESC_BREVE_TIPO_AMMINISTRAZ ,                                                                                           \n"
          + "   VISTAAMM.DESCRIZIONE AS DESCR_AMM,                                                                                               \n"
          + "   VISTAAMM.DENOMINAZIONE_1		                                                                                                 \n"

          + " FROM                                                                                                                               \n"
          + "   NEMBO_D_BANDO A,                                                                                                                   \n"
          + "   NEMBO_D_TIPO_LIVELLO B,                                                                                                            \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,                                                                                                          \n"
          + "   NEMBO_R_BANDO_MASTER BM,                                                                                                           \n"
          + "   NEMBO_D_LIVELLO L,                                                                                                                 \n"
          + "   NEMBO_D_ALLEGATI_BANDO ALLEG,                                                                                                      \n"
          + "   NEMBO_D_BANDO_AMM_COMPETENZA AMMCOMP,                                                                                              \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA VISTAAMM,                                                                                              \n"
          + "   NEMBO_D_EVENTO_CALAMITOSO EC,                                                                                              \n"
          + "   NEMBO_D_CATEGORIA_EVENTO CE                                                                                              \n"

          + " WHERE                                                                                                                              \n"
          + "   B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO                                                                                            \n"
          + "   AND A.FLAG_MASTER = 'N'                                                                                                          \n"
          + "   AND LB.ID_BANDO = A.ID_BANDO                                                                                                     \n"
          + "   AND BM.ID_BANDO = A.ID_BANDO                                                                                                     \n"
          + "   AND L.ID_LIVELLO  = LB.ID_LIVELLO                                                                                                \n"
          + "   AND ALLEG.ID_BANDO (+)= A.ID_BANDO																								 \n"
          + "   AND AMMCOMP.EXT_ID_AMM_COMPETENZA = VISTAAMM.ID_AMM_COMPETENZA																	 \n"
          + "   AND AMMCOMP.ID_BANDO = A.ID_BANDO																								 \n"
      	  + "   AND A.ID_EVENTO_CALAMITOSO = EC.ID_EVENTO_CALAMITOSO (+)																								 \n"
      	  + "   AND EC.ID_CATEGORIA_EVENTO = CE.ID_CATEGORIA_EVENTO (+)																								 \n"
      	  ;

      if (isBandoGal && idsBandiGal != null && !idsBandiGal.isEmpty())
        SELECT += getInCondition("A.ID_BANDO", idsBandiGal);
      SELECT += " ORDER BY A.DATA_FINE, A.DENOMINAZIONE, L.CODICE, ALLEG.ORDINE, ALLEG.NOME_FILE, A.ID_BANDO , VISTAAMM.DESC_BREVE_TIPO_AMMINISTRAZ          \n";

      return namedParameterJdbcTemplate.query(SELECT,
          (MapSqlParameterSource) null, new ResultSetExtractor<List<BandoDTO>>()
          {
            @Override
            public List<BandoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<BandoDTO> list = new ArrayList<BandoDTO>();
              List<LivelloDTO> livelli = null;
              List<FileAllegatoDTO> allegati = null;
              List<AmmCompetenzaDTO> ammComp = null;

              LivelloDTO livello = null;
              long idBando;
              long idBandoLast = -1;
              long idLivello;
              long idLivelloLast = -1;
              BandoDTO bandoDTO = null;
              String misura = "";
              String sottoMisura = "";
              while (rs.next())
              {
                idBando = rs.getLong("ID_BANDO");
                if (idBando != idBandoLast)
                {
                  idBandoLast = idBando;
                  idLivelloLast = -1;
                  bandoDTO = new BandoDTO();
                  bandoDTO.setIdBando(idBando);
                  bandoDTO.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                  bandoDTO.setDataFine(rs.getTimestamp("DATA_FINE"));
                  bandoDTO.setDataEvento(rs.getTimestamp("DATA_EVENTO"));
                  bandoDTO.setIdEventoCalamitoso(rs.getLong("ID_EVENTO_CALAMITOSO"));
                  bandoDTO.setDescEventoCalamitoso(rs.getString("DESC_EVENTO_CALAMITOSO"));
                  bandoDTO.setIdCategoriaEvento(rs.getLong("ID_CATEGORIA_EVENTO"));
                  bandoDTO.setDescCatEvento(rs.getString("DESC_CAT_EVENTO"));
                  bandoDTO.setDenominazione(rs.getString("DENOMINAZIONE"));
                  bandoDTO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                  bandoDTO.setDescrTipoBando(rs.getString("DESCR_TIPO_BANDO"));
                  bandoDTO.setFlagTitolaritaRegionale(
                      rs.getString("FLAG_TITOLARITA_REGIONALE"));
                  bandoDTO.setBandoAttivo("S".equals(rs.getString("ATTIVO")));
                  try
                  {
                    bandoDTO.setElencoFocusArea(getElencoFocusArea(idBando));
                    bandoDTO.setElencoSettori(getElencoSettori(idBando));
                  }
                  catch (InternalUnexpectedException e)
                  {
                    logger.error(
                        "[" + THIS_CLASS + ":: " + e.getMessage() + "] END.");
                  }
                  livelli = new ArrayList<LivelloDTO>();
                  allegati = new ArrayList<FileAllegatoDTO>();
                  ammComp = new ArrayList<AmmCompetenzaDTO>();
                  bandoDTO.setLivelli(livelli);
                  bandoDTO.setAllegati(allegati);
                  bandoDTO.setAmministrazioniCompetenza(ammComp);
                  list.add(bandoDTO);
                }

                idLivello = rs.getLong("ID_LIVELLO");

                if (idLivello != idLivelloLast)
                {
                  idLivelloLast = idLivello;
                  livello = new LivelloDTO();
                  misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                  if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                  {
                    sottoMisura = rs.getString("CODICE_LIVELLO")
                        .split("\\.")[1];
                    livello.setCodiceSottoMisura(misura + "." + sottoMisura);
                  }

                  livello.setIdLivello(rs.getLong("ID_LIVELLO"));
                  livello.setCodice(rs.getString("CODICE"));
                  livello.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                  livello.setCodiceMisura(misura);
                  livello.setDescrizione("DESCRIZIONE");
                  livelli.add(livello);
                }

                // aggiungo allegati
                FileAllegatoDTO file = new FileAllegatoDTO();
                if (rs.getString("NOME_FILE") != null)
                {
                  file.setDescrizione(rs.getString("DESCR_FILE"));
                  file.setIdAllegatiBando(rs.getLong("ID_ALLEGATI_BANDO"));
                  file.setNomeFile(rs.getString("NOME_FILE"));
                  boolean exists = false;
                  for (FileAllegatoDTO f : allegati)
                  {
                    if (f.getIdAllegatiBando() == rs
                        .getLong("ID_ALLEGATI_BANDO"))
                    {
                      exists = true;
                    }
                  }
                  if (!exists)
                    allegati.add(file);
                }

                // aggiungo amm comp
                AmmCompetenzaDTO amm = new AmmCompetenzaDTO();
                if (rs.getString("DESCR_AMM") != null)
                {
                  amm.setIdAmmCompetenza(rs.getLong("ID_AMM_COMPETENZA"));
                  amm.setDescBreveTipoAmministraz(
                      rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                  amm.setDescrizione(rs.getString("DESCR_AMM"));
                  amm.setDenominazioneuno(rs.getString("DENOMINAZIONE_1"));

                  boolean exists = false;
                  for (AmmCompetenzaDTO a : ammComp)
                  {
                    if (a.getIdAmmCompetenza() == rs
                        .getLong("ID_AMM_COMPETENZA"))
                    {
                      exists = true;
                    }
                  }
                  if (!exists)
                    ammComp.add(amm);
                }

              }
              return list.isEmpty() ? null : list;
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

  public List<FocusAreaDTO> getElencoFocusArea(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoFocusArea";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                  \n"
          + "   F.ID_FOCUS_AREA,                      \n"
          + "   D.CODICE                              \n"
          + " FROM                                    \n"
          + "   NEMBO_D_BANDO B,                        \n"
          + "   NEMBO_R_LIVELLO_BANDO L,                \n"
          + "   NEMBO_R_LIVELLO_FOCUS_AREA F,           \n"
          + "   NEMBO_D_FOCUS_AREA D                    \n"
          + " WHERE                                   \n"
          + "   B.ID_BANDO = L.ID_BANDO               \n"
          + "   AND F.ID_LIVELLO = L.ID_LIVELLO       \n"
          + "   AND D.ID_FOCUS_AREA = F.ID_FOCUS_AREA \n"
          + "   AND F.ID_PIANO_FINANZIARIO = 1        \n"
          + "   AND B.ID_BANDO = :ID_BANDO            \n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<FocusAreaDTO>>()
          {
            @Override
            public List<FocusAreaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<FocusAreaDTO> list = new ArrayList<FocusAreaDTO>();
              FocusAreaDTO item = null;
              while (rs.next())
              {
                item = new FocusAreaDTO();
                item.setIdFocusArea(rs.getLong("ID_FOCUS_AREA"));
                item.setCodice(rs.getString("CODICE"));
                list.add(item);
              }
              return list.isEmpty() ? null : list;
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

  public List<SettoriDiProduzioneDTO> getElencoSettori(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoSettori";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                   \n"
          + "   S.CODICE,                       \n"
          + "   S.ID_SETTORE                    \n"
          + " FROM                              \n"
          + "   NEMBO_D_BANDO B,                  \n"
          + "   NEMBO_R_LIVELLO_BANDO L,          \n"
          + "   NEMBO_D_LIVELLO D,                \n"
          + "   NEMBO_D_SETTORE S                 \n"
          + " WHERE                             \n"
          + "   B.ID_BANDO = L.ID_BANDO         \n"
          + "   AND L.ID_LIVELLO = D.ID_LIVELLO \n"
          + "   AND D.ID_SETTORE = S.ID_SETTORE \n"
          + "   AND B.ID_BANDO = :ID_BANDO      \n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<SettoriDiProduzioneDTO>>()
          {
            @Override
            public List<SettoriDiProduzioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<SettoriDiProduzioneDTO> list = new ArrayList<SettoriDiProduzioneDTO>();
              SettoriDiProduzioneDTO item = null;
              while (rs.next())
              {
                item = new SettoriDiProduzioneDTO();
                item.setIdSettore(rs.getLong("ID_SETTORE"));
                item.setDescrizione(rs.getString("CODICE"));
                list.add(item);
              }
              return list.isEmpty() ? null : list;
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

  public List<BandoDTO> getDettaglioBandiMaster()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDettaglioBandiMaster";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                 													\n"
          + "  A.ID_BANDO,                           													\n"
          + "  A.DATA_INIZIO,                        													\n"
          + "  A.DATA_FINE,                          													\n"
          + "  A.DENOMINAZIONE,                      													\n"
          + "  A.ANNO_CAMPAGNA,                       													\n"
          + "  DECODE(A.FLAG_TITOLARITA_REGIONALE,'S','Si','N','No','') AS FLAG_TITOLARITA_REGIONALE,  	\n"
          + "  B.DESCRIZIONE AS DESCR_TIPO_BANDO                     		 							\n"
          + " FROM                                   													\n"
          + "  NEMBO_D_BANDO A,                         													\n"
          + "  NEMBO_D_TIPO_LIVELLO B                         											\n"
          + " WHERE          																			\n"
          + "   B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO                        							\n"
          + "   AND A.FLAG_MASTER = 'S'                  												\n"
          + "   AND SYSDATE BETWEEN A.DATA_INIZIO AND NVL(A.DATA_FINE,SYSDATE) 							\n"
          + " ORDER BY A.DATA_FINE, A.DENOMINAZIONE  													\n";

      return namedParameterJdbcTemplate.query(SELECT,
          (MapSqlParameterSource) null, new ResultSetExtractor<List<BandoDTO>>()
          {
            @Override
            public List<BandoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<BandoDTO> list = new ArrayList<BandoDTO>();
              BandoDTO bandoDTO = null;
              while (rs.next())
              {
                bandoDTO = new BandoDTO();
                bandoDTO.setIdBando(rs.getLong("ID_BANDO"));
                bandoDTO.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                bandoDTO.setDataFine(rs.getTimestamp("DATA_FINE"));
                bandoDTO.setDenominazione(rs.getString("DENOMINAZIONE"));
                bandoDTO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                bandoDTO.setDescrTipoBando(rs.getString("DESCR_TIPO_BANDO"));
                bandoDTO.setFlagTitolaritaRegionale(
                    rs.getString("FLAG_TITOLARITA_REGIONALE"));
                list.add(bandoDTO);
              }
              return list.isEmpty() ? null : list;
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

  public List<AmmCompetenzaDTO> getAmmCompetenzaAssociate(long idBando,
      boolean isMaster) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAmmCompetenzaAssociate";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   A.ID_BANDO,                                        \n"
          + "   B.ID_AMM_COMPETENZA,                               \n"
          + "   B.DESC_BREVE_TIPO_AMMINISTRAZ,                     \n"
          + "   B.DESCRIZIONE || '-' || B.DENOMINAZIONE_1 AS DESCRIZIONE		   \n"
          + " FROM                                                 \n"
          + "   NEMBO_D_BANDO_AMM_COMPETENZA A,                      \n"
          + "   NEMBO_D_BANDO C,				                       \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA B                       \n"
          + " WHERE                                                \n"
          + "   A.EXT_ID_AMM_COMPETENZA = B.ID_AMM_COMPETENZA      \n"
          + "   AND A.ID_BANDO = :ID_BANDO                         \n"
          + "   AND A.ID_BANDO = C.ID_BANDO                        \n";
      if (!isMaster)
        SELECT += "   AND C.FLAG_MASTER = 'N'               \n";
      else
        SELECT += "   AND C.FLAG_MASTER = 'S'               \n";
      SELECT += " ORDER BY B.DESC_BREVE_TIPO_AMMINISTRAZ,B.DESCRIZIONE \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<AmmCompetenzaDTO>>()
          {
            @Override
            public List<AmmCompetenzaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AmmCompetenzaDTO> list = new ArrayList<AmmCompetenzaDTO>();
              AmmCompetenzaDTO ammDTO = null;
              while (rs.next())
              {
                ammDTO = new AmmCompetenzaDTO();
                ammDTO.setIdBando(rs.getLong("ID_BANDO"));
                ammDTO.setIdAmmCompetenza(rs.getLong("ID_AMM_COMPETENZA"));
                ammDTO.setDescBreveTipoAmministraz(
                    rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                ammDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(ammDTO);
              }
              return list.isEmpty() ? null : list;
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

  public List<AmmCompetenzaDTO> getDettagliAmmCompetenza(
      Vector<Long> vIdAmmCompetenza) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAmmCompetenzaAssociate";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   B.ID_AMM_COMPETENZA,                               \n"
          + "   B.DESC_BREVE_TIPO_AMMINISTRAZ,                     \n"
          + "   B.DESCRIZIONE                                      \n"
          + " FROM                                                 \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA B                       \n"
          + " WHERE                                                \n"
          + getInCondition("B.ID_AMM_COMPETENZA", vIdAmmCompetenza, false)
          + " ORDER BY B.DESC_BREVE_TIPO_AMMINISTRAZ,B.DESCRIZIONE \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<AmmCompetenzaDTO>>()
          {
            @Override
            public List<AmmCompetenzaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AmmCompetenzaDTO> list = new ArrayList<AmmCompetenzaDTO>();
              AmmCompetenzaDTO ammDTO = null;
              while (rs.next())
              {
                ammDTO = new AmmCompetenzaDTO();
                ammDTO.setIdAmmCompetenza(rs.getLong("ID_AMM_COMPETENZA"));
                ammDTO.setDescBreveTipoAmministraz(
                    rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                ammDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(ammDTO);
              }
              return list.isEmpty() ? null : list;
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

  public List<AmmCompetenzaDTO> getAmmCompetenzaDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAmmCompetenzaDisponibili";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " WITH BANDO_MASTER AS (                                         \n"
          + " SELECT NVL(A1.ID_BANDO_MASTER, B2.ID_BANDO) AS ID_BANDO_MASTER \n"
          + " FROM                                                           \n"
          + "   NEMBO_R_BANDO_MASTER A1,                                       \n"
          + "   NEMBO_D_BANDO B2                                               \n"
          + " WHERE                                                          \n"
          + "   A1.ID_BANDO(+) = B2.ID_BANDO                                 \n"
          + "   AND B2.ID_BANDO = :ID_BANDO                                  \n"
          + " )                                                              \n"
          + " SELECT                                                         \n"
          + "    A.ID_BANDO,                                                 \n"
          + "    BM.ID_BANDO_MASTER,                                         \n"
          + "    A.EXT_ID_AMM_COMPETENZA,                                    \n"
          + "    B.DESC_BREVE_TIPO_AMMINISTRAZ,                              \n"
          + "    B.DESC_ESTESA_TIPO_AMMINISTRAZ,                             \n"
          + "    B.DESCRIZIONE,                                              \n"
          + "    B.DENOMINAZIONE_1                                           \n"
          + "  FROM                                                          \n"
          + "    NEMBO_D_BANDO_AMM_COMPETENZA A,                               \n"
          + "    SMRCOMUNE_V_AMM_COMPETENZA B,                               \n"
          + "    BANDO_MASTER BM                                             \n"
          + "  WHERE                                                         \n"
          + "    A.EXT_ID_AMM_COMPETENZA = B.ID_AMM_COMPETENZA               \n"
          + "    AND A.ID_BANDO = BM.ID_BANDO_MASTER                         \n"
          + "    AND A.EXT_ID_AMM_COMPETENZA NOT IN (                        \n"
          + "      SELECT                                                    \n"
          + "        C.EXT_ID_AMM_COMPETENZA                                 \n"
          + "      FROM                                                      \n"
          + "        NEMBO_D_BANDO_AMM_COMPETENZA C,                           \n"
          + "        NEMBO_D_BANDO D                                           \n"
          + "      WHERE                                                     \n"
          + "        C.ID_BANDO = :ID_BANDO                                  \n"
          + "        AND D.ID_BANDO = C.ID_BANDO                             \n"
          + "        AND D.FLAG_MASTER = 'N'                                 \n"
          + "    )                                                           \n"
          + "  ORDER BY B.DESC_BREVE_TIPO_AMMINISTRAZ,B.DESCRIZIONE          \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<AmmCompetenzaDTO>>()
          {
            @Override
            public List<AmmCompetenzaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AmmCompetenzaDTO> list = new ArrayList<AmmCompetenzaDTO>();
              AmmCompetenzaDTO ammDTO = null;
              while (rs.next())
              {
                ammDTO = new AmmCompetenzaDTO();
                ammDTO.setIdBando(rs.getLong("ID_BANDO"));
                ammDTO.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                ammDTO.setIdAmmCompetenza(rs.getLong("EXT_ID_AMM_COMPETENZA"));
                ammDTO.setDescBreveTipoAmministraz(
                    rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                ammDTO.setDenominazioneuno(rs.getString("DENOMINAZIONE_1"));
                ammDTO.setDescEstesaTipoAmministraz(
                    rs.getString("DESC_ESTESA_TIPO_AMMINISTRAZ"));
                ammDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(ammDTO);
              }
              return list.isEmpty() ? null : list;
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

  public List<TipoOperazioneDTO> getTipiOperazioniAssociati(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getTipiOperazioniAssociati";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                        \n"
          + "   B.ID_LIVELLO,               \n"
          + "   B.CODICE,                   \n"
          + "   B.DESCRIZIONE               \n"
          + " FROM                          \n"
          + "   NEMBO_R_LIVELLO_BANDO A,      \n"
          + "   NEMBO_D_LIVELLO B,            \n"
          + "   NEMBO_D_BANDO C               \n"
          + " WHERE                         \n"
          + "   A.ID_LIVELLO = B.ID_LIVELLO \n"
          + "   AND C.ID_BANDO = A.ID_BANDO \n"
          + "   AND C.FLAG_MASTER = 'N'     \n"
          + "   AND A.ID_BANDO = :ID_BANDO  \n"
          + " ORDER BY B.ORDINAMENTO        \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<TipoOperazioneDTO>>()
          {
            @Override
            public List<TipoOperazioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<TipoOperazioneDTO> list = new ArrayList<TipoOperazioneDTO>();
              TipoOperazioneDTO toDTO = null;
              while (rs.next())
              {
                toDTO = new TipoOperazioneDTO();
                toDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
                toDTO.setCodice(rs.getString("CODICE"));
                toDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(toDTO);
              }
              return list.isEmpty() ? null : list;
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

  public List<TipoOperazioneDTO> getTipiOperazioniDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getTipiOperazioniDisponibili";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = "   WITH BANDO_MASTER AS (                                         \n"
          + "   SELECT NVL(A1.ID_BANDO_MASTER, B2.ID_BANDO) AS ID_BANDO_MASTER \n"
          + "   FROM                                                           \n"
          + "    NEMBO_R_BANDO_MASTER A1,                                        \n"
          + "    NEMBO_D_BANDO B2                                                \n"
          + "   WHERE                                                          \n"
          + "    A1.ID_BANDO(+) = B2.ID_BANDO                                  \n"
          + "    AND B2.ID_BANDO = :ID_BANDO                                   \n"
          + "   )                                                              \n"
          + "                                                                  \n"
          + "   SELECT                                                         \n"
          + "     B.ID_LIVELLO,                                                \n"
          + "     B.CODICE,                                                    \n"
          + "     B.DESCRIZIONE                                                \n"
          + "   FROM                                                           \n"
          + "     NEMBO_R_LIVELLO_BANDO A,                                       \n"
          + "     NEMBO_D_LIVELLO B,                                             \n"
          + "     BANDO_MASTER BM                                              \n"
          + "   WHERE                                                          \n"
          + "     A.ID_LIVELLO = B.ID_LIVELLO                                  \n"
          + "     AND A.ID_BANDO = BM.ID_BANDO_MASTER                          \n"
          + "     AND A.ID_LIVELLO NOT IN (                                    \n"
          + "       SELECT                                                     \n"
          + "         C.ID_LIVELLO                                             \n"
          + "       FROM                                                       \n"
          + "         NEMBO_R_LIVELLO_BANDO C,                                   \n"
          + "         NEMBO_D_BANDO D                                            \n"
          + "       WHERE                                                      \n"
          + "         C.ID_BANDO = :ID_BANDO                                   \n"
          + "         AND D.ID_BANDO = C.ID_BANDO                              \n"
          + "         AND D.FLAG_MASTER = 'N'                                  \n"
          + "     )                                                            \n"
          + "   ORDER BY B.ORDINAMENTO                                         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<TipoOperazioneDTO>>()
          {
            @Override
            public List<TipoOperazioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<TipoOperazioneDTO> list = new ArrayList<TipoOperazioneDTO>();
              TipoOperazioneDTO toDTO = null;
              while (rs.next())
              {
                toDTO = new TipoOperazioneDTO();
                toDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
                toDTO.setCodice(rs.getString("CODICE"));
                toDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(toDTO);
              }
              return list.isEmpty() ? null : list;
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

  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getInformazioniBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                               \n"
          + "   B.ID_BANDO,                                                        \n"
          + "   B.ID_TIPO_LIVELLO,                                                 \n"
          + "   B.REFERENTE_BANDO,                                                 \n"
          + "   B.EMAIL_REFERENTE_BANDO,                                           \n"
          + "   B.FLAG_RIBASSO_INTERVENTI,                                         \n"
          + "   B.FLAG_MASTER,                                                     \n"
          + "   B.FLAG_DOMANDA_MULTIPLA,                                           \n"
          + "   B.ISTRUZIONE_SQL_FILTRO,                                           \n"
          + "   B.DESCRIZIONE_FILTRO,                                              \n"
          + "   DECODE(B.FLAG_MASTER,'S',NULL,B.DENOMINAZIONE) AS DENOMINAZIONE,   \n"
          + "   B.DATA_INIZIO,        											   \n"
          + "   B.DATA_FINE,        											   \n"
          + "   B.NUM_RANGE_ANNI_EXPOST,										   \n"
          + "	B.ID_PROCEDIMENTO_AGRICOLO,											\n"
          + "   C.CODICE AS COD_TIPOLOGIA,                                         \n"
          + "   C.DESCRIZIONE AS TIPOLOGIA,                                        \n"
          + "   NVL(B.ANNO_CAMPAGNA,TO_CHAR(SYSDATE, 'YYYY')) AS ANNO_CAMPAGNA,    \n"
          + "   NVL(B.FLAG_TITOLARITA_REGIONALE, 'N') AS FLAG_TITOLARITA_REGIONALE, \n"
          + "   NVL(D.ID_BANDO_MASTER, B.ID_BANDO) AS ID_BANDO_MASTER,              \n"
          + "   EC.ID_EVENTO_CALAMITOSO,       										\n"
          + "   EC.DESCRIZIONE AS DESC_EVENTO_CALAMITOSO,       					\n"
          + "   CE.ID_CATEGORIA_EVENTO,       										\n"
          + "   CE.DESCRIZIONE AS DESC_CAT_EVENTO              						\n"
          + " FROM                                                                 	\n"
          + "   NEMBO_D_BANDO B,                                                    \n"
          + "   NEMBO_D_TIPO_LIVELLO C,                                             \n"
          + "   NEMBO_R_BANDO_MASTER D,                                             \n"
          + "   NEMBO_D_EVENTO_CALAMITOSO EC,                                       \n"
          + "   NEMBO_D_CATEGORIA_EVENTO CE                                         \n"
          + " WHERE                                                                \n"
          + "   C.ID_TIPO_LIVELLO = B.ID_TIPO_LIVELLO                              \n"
          + "   AND D.ID_BANDO(+) = B.ID_BANDO		                               \n"
          + "   AND B.ID_EVENTO_CALAMITOSO = EC.ID_EVENTO_CALAMITOSO(+)			   \n"
          + "   AND EC.ID_CATEGORIA_EVENTO = CE.ID_CATEGORIA_EVENTO(+)                                         \n"
          + "   AND B.ID_BANDO = :ID_BANDO                                         \n"
          
          ;

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BandoDTO>()
          {
            @Override
            public BandoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              BandoDTO bando = new BandoDTO();
              while (rs.next())
              {
                String flagMaster = rs.getString("FLAG_MASTER");
                bando.setIdBando(rs.getLong("ID_BANDO"));
                bando.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                bando.setIdTipoLivello(rs.getLong("ID_TIPO_LIVELLO"));
                bando.setFlagTitolaritaRegionale(
                    rs.getString("FLAG_TITOLARITA_REGIONALE"));
                bando.setFlagDomandaMultipla(
                    rs.getString("FLAG_DOMANDA_MULTIPLA"));
                bando.setDenominazione(rs.getString("DENOMINAZIONE"));
                bando.setDescrTipoBando(rs.getString("TIPOLOGIA"));
                bando.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                bando.setCodiceTipoBando(rs.getString("COD_TIPOLOGIA"));
                bando.setDescrizioneFiltro(rs.getString("DESCRIZIONE_FILTRO"));
                bando.setIstruzioneSqlFiltro(
                    rs.getString("ISTRUZIONE_SQL_FILTRO"));
                bando.setReferenteBando(rs.getString("REFERENTE_BANDO"));
                bando.setEmailReferenteBando(
                    rs.getString("EMAIL_REFERENTE_BANDO"));
                bando.setFlagRibassoInterventi(
                    rs.getString("FLAG_RIBASSO_INTERVENTI"));
                bando.setNumeroAnniMantenimento(
                    rs.getLong("NUM_RANGE_ANNI_EXPOST"));
                bando.setIdProcedimentoAgricolo(rs.getInt("ID_PROCEDIMENTO_AGRICOLO"));

                bando.setFlagMaster(flagMaster);
                if (flagMaster.equals("N"))
                {
                  bando.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                  bando.setDataFine(rs.getTimestamp("DATA_FINE"));
                  bando.setIdEventoCalamitoso(rs.getLong("ID_EVENTO_CALAMITOSO"));
                  bando.setIdCategoriaEvento(rs.getLong("ID_CATEGORIA_EVENTO"));
                  bando.setDescCatEvento(rs.getString("DESC_CAT_EVENTO"));
                  bando.setDescEventoCalamitoso(rs.getString("DESC_EVENTO_CALAMITOSO"));
                }
              }
              return bando;
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

  public boolean isBandoModificabile(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isBandoModificabile";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                               \n"
          + "   A.ID_BANDO,                                                        \n"
          + "   A.ID_BANDO_OGGETTO,                                                \n"
          + "   A.FLAG_ATTIVO                                                      \n"
          + " FROM                                                                 \n"
          + "   NEMBO_R_BANDO_OGGETTO A                                              \n"
          + " WHERE                                                                \n"
          + "   A.ID_BANDO = :ID_BANDO                                             \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                String flagAttivo = rs.getString("FLAG_ATTIVO");
                if (flagAttivo.equals("S"))
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

  public boolean isBandoEsistente(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isBandoEsistente";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                         \n"
          + "   A.ID_BANDO                   \n"
          + " FROM                           \n"
          + "   NEMBO_D_BANDO A                \n"
          + " WHERE                          \n"
          + "   A.ID_BANDO = :ID_BANDO       \n"
          + "   AND A.FLAG_MASTER = 'N'      \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
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

  public void insertRBandoMaster(long idBando, long idBandoMaster)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRBandoMaster";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                       \n"
          + " INTO                       \n"
          + "   NEMBO_R_BANDO_MASTER       \n"
          + "   (                        \n"
          + "     ID_BANDO,      		   \n"
          + "     ID_BANDO_MASTER    	   \n"
          + "   )                        \n"
          + "   VALUES                   \n"
          + "   (                        \n"
          + "     :ID_BANDO,      	   \n"
          + "     :ID_BANDO_MASTER       \n"
          + "   )                        \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando);
      mapParameterSource.addValue("ID_BANDO_MASTER", idBandoMaster);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_BANDO_MASTER", idBandoMaster)
          },
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

  public void updateDBando(BandoDTO bando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateDBando";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                                   \n"
          + "   NEMBO_D_BANDO                                            \n"
          + " SET                                                      \n"
          + "   DENOMINAZIONE = :DENOMINAZIONE,                        \n"
          + "   REFERENTE_BANDO = :REFERENTE_BANDO,                    \n"
          + "   EMAIL_REFERENTE_BANDO = :EMAIL_REFERENTE_BANDO,        \n"
          + "   FLAG_RIBASSO_INTERVENTI = :FLAG_RIBASSO_INTERVENTI,    \n"
          + "   DATA_INIZIO = :DATA_INIZIO,                            \n"
          + "   DATA_FINE = :DATA_FINE,                            	   \n"
          + "   ID_EVENTO_CALAMITOSO = :ID_EVENTO_CALAMITOSO,      	   \n"
          + "   ANNO_CAMPAGNA = :ANNO_CAMPAGNA,                        \n"
          + "   FLAG_DOMANDA_MULTIPLA = :FLAG_DOMANDA_MULTIPLA, 	   \n"
          + "   FLAG_TITOLARITA_REGIONALE = :FLAG_TITOLARITA_REGIONALE,\n"
          + "   NUM_RANGE_ANNI_EXPOST = :NUM_RANGE_ANNI_EXPOST,		   \n"
          + "   ID_PROCEDIMENTO_AGRICOLO = :ID_PROCEDIMENTO_AGRICOLO		   \n"
          + " WHERE                                                    \n"
          + "   ID_BANDO = :ID_BANDO                                   \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", bando.getIdBando(),
          Types.NUMERIC);
      mapParameterSource.addValue("DENOMINAZIONE", bando.getDenominazione(),
          Types.VARCHAR);
      mapParameterSource.addValue("EMAIL_REFERENTE_BANDO",
          bando.getEmailReferenteBando(), Types.VARCHAR);
      mapParameterSource.addValue("REFERENTE_BANDO", bando.getReferenteBando(),
          Types.VARCHAR);
      mapParameterSource.addValue("FLAG_RIBASSO_INTERVENTI",
          bando.getFlagRibassoInterventi(), Types.VARCHAR);
      mapParameterSource.addValue("DATA_INIZIO", bando.getDataInizio(),
          Types.TIMESTAMP);
      mapParameterSource.addValue("DATA_FINE", bando.getDataFine(),
          Types.TIMESTAMP);
      mapParameterSource.addValue("ID_EVENTO_CALAMITOSO", bando.getIdEventoCalamitoso(),Types.NUMERIC);
      mapParameterSource.addValue("ANNO_CAMPAGNA", bando.getAnnoCampagna(),
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_TITOLARITA_REGIONALE",
          bando.getFlagTitolaritaRegionale(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_DOMANDA_MULTIPLA",
          bando.getFlagDomandaMultipla(), Types.VARCHAR);
      mapParameterSource.addValue("NUM_RANGE_ANNI_EXPOST",
          bando.getNumeroAnniMantenimento(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO",
              bando.getIdProcedimentoAgricolo(), Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", bando.getIdBando()),
              new LogParameter("DENOMINAZIONE", bando.getDenominazione()),
              new LogParameter("DATA_INIZIO", bando.getDataInizio()),
              new LogParameter("DATA_FINE", bando.getDataFine()),
              new LogParameter("ID_EVENTO_CALAMITOSO", bando.getIdEventoCalamitoso()),
              new LogParameter("ANNO_CAMPAGNA", bando.getAnnoCampagna()),
              new LogParameter("FLAG_TITOLARITA_REGIONALE",
                  bando.getFlagTitolaritaRegionale()),
              new LogParameter("FLAG_DOMANDA_MULTIPLA",
                  bando.getFlagDomandaMultipla()),
              new LogParameter("ID_PROCEDIMENTO_AGRICOLO",
                      bando.getIdProcedimentoAgricolo())
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

  public long insertDBando(BandoDTO bando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertDBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                       			 \n"
          + " INTO                       			 \n"
          + "   NEMBO_D_BANDO       				 \n"
          + "   (                        			 \n"
          + "     ID_BANDO,      		   			 \n"
          + "     DENOMINAZIONE,    	   			 \n"
          + "     REFERENTE_BANDO,    	   		 \n"
          + "     EMAIL_REFERENTE_BANDO,    	   	 \n"
          + "     FLAG_RIBASSO_INTERVENTI,    	 \n"
          + "     DATA_INIZIO,    	   			 \n"
          + "     DATA_FINE,    	   			 \n"
          + "	  ID_EVENTO_CALAMITOSO,		"	
          + "     ID_TIPO_LIVELLO,    	   		 \n"
          + "     FLAG_MASTER,    	   			 \n"
          + "     ANNO_CAMPAGNA,    	   			 \n"
          + "     FLAG_TITOLARITA_REGIONALE,    	 \n"
          + "     FLAG_DOMANDA_MULTIPLA,    		 \n"
          + "     ID_RANGE_IDENTIFICATIVO,   		 \n"
          + "     FLAG_RENDICONTAZIONE_DOC_SPESA,  \n"
          + "     NUM_RANGE_ANNI_EXPOST,   		 \n"
          + "     ID_PROCEDIMENTO_AGRICOLO   		 \n"
          + "   )                        			 \n"
          + "   VALUES                   			 \n"
          + "   (                        			 \n"
          + "     :ID_BANDO,      		   		 \n"
          + "     :DENOMINAZIONE,    	   			 \n"
          + "     :REFERENTE_BANDO,    	   		 \n"
          + "     :EMAIL_REFERENTE_BANDO,    	   	 \n"
          + "     :FLAG_RIBASSO_INTERVENTI,    	 \n"
          + "     :DATA_INIZIO,    	   			 \n"
          + "     :DATA_FINE,    	   				 \n"
          + "     :ID_EVENTO_CALAMITOSO,   				 \n"
          + "     :ID_TIPO_LIVELLO,    	   	  	 \n"
          + "     'N',    	   					 \n"
          + "     :ANNO_CAMPAGNA,    	   			 \n"
          + "     :FLAG_TITOLARITA_REGIONALE ,   	 \n"
          + "     :FLAG_DOMANDA_MULTIPLA,    		 \n"
          + "     (SELECT ID_RANGE_IDENTIFICATIVO  \n"
          + "			FROM NEMBO_D_BANDO 			 \n"
          + "			WHERE 						 \n"
          + "			ID_BANDO = :ID_BANDO_MASTER  \n"
          + "			AND FLAG_MASTER = 'S'),		 \n"
          + "     (SELECT FLAG_RENDICONTAZIONE_DOC_SPESA  \n"
          + "			FROM NEMBO_D_BANDO 			 \n"
          + "			WHERE 						 \n"
          + "			ID_BANDO = :ID_BANDO_MASTER  \n"
          + "			AND FLAG_MASTER = 'S'),		 \n"
          + "     :NUM_RANGE_ANNI_EXPOST,   		 \n"
          + "     :ID_PROCEDIMENTO_AGRICOLO   		 \n"
          + "   )                        			 \n";
      long idBando = getNextSequenceValue("SEQ_NEMBO_D_BANDO");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_MASTER", bando.getIdBandoMaster(),
          Types.NUMERIC);
      mapParameterSource.addValue("DENOMINAZIONE", bando.getDenominazione(),
          Types.VARCHAR);
      mapParameterSource.addValue("REFERENTE_BANDO", bando.getReferenteBando(),
          Types.VARCHAR);
      mapParameterSource.addValue("EMAIL_REFERENTE_BANDO",
          bando.getEmailReferenteBando(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_RIBASSO_INTERVENTI",
          bando.getFlagRibassoInterventi(), Types.VARCHAR);
      mapParameterSource.addValue("DATA_INIZIO", bando.getDataInizio(),
          Types.TIMESTAMP);
      mapParameterSource.addValue("DATA_FINE", bando.getDataFine(),
          Types.TIMESTAMP);
           mapParameterSource.addValue("ID_EVENTO_CALAMITOSO", bando.getIdEventoCalamitoso(),Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_LIVELLO", bando.getIdTipoLivello(),Types.NUMERIC);
      mapParameterSource.addValue("ANNO_CAMPAGNA", bando.getAnnoCampagna(),
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_TITOLARITA_REGIONALE",
          bando.getFlagTitolaritaRegionale(), Types.VARCHAR);
      mapParameterSource.addValue("FLAG_DOMANDA_MULTIPLA",
          bando.getFlagDomandaMultipla(), Types.VARCHAR);
      mapParameterSource.addValue("NUM_RANGE_ANNI_EXPOST",
          bando.getNumeroAnniMantenimento(), Types.NUMERIC);
      mapParameterSource.addValue("ID_PROCEDIMENTO_AGRICOLO",
              bando.getIdProcedimentoAgricolo(), Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBando;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_MASTER", bando.getIdBandoMaster()),
              new LogParameter("DENOMINAZIONE", bando.getDenominazione()),
              new LogParameter("DATA_INIZIO", bando.getDataInizioStr()),
              new LogParameter("ID_EVENTO_CALAMITOSO", bando.getIdEventoCalamitoso()),
              new LogParameter("ID_TIPO_LIVELLO", bando.getIdTipoLivello()),
              new LogParameter("REFERENTE_BANDO", bando.getReferenteBando()),
              new LogParameter("EMAIL_REFERENTE_BANDO",
                  bando.getEmailReferenteBando()),
              new LogParameter("FLAG_RIBASSO_INTERVENTI",
                  bando.getFlagRibassoInterventi()),
              new LogParameter("ANNO_CAMPAGNA", bando.getAnnoCampagna()),
              new LogParameter("FLAG_TITOLARITA_REGIONALE",
                  bando.getFlagTitolaritaRegionale()),
              new LogParameter("FLAG_DOMANDA_MULTIPLA",
                  bando.getFlagDomandaMultipla()),
              new LogParameter("ID_PROCEDIMENTO_AGRICOLO",
                      bando.getIdProcedimentoAgricolo())
          },
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

  public void deleteDBandoAmmCompetenza(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteDBandoAmmCompetenza";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       	\n"
          + "   NEMBO_D_BANDO_AMM_COMPETENZA	\n"
          + "WHERE                   		\n"
          + "  ID_BANDO = :ID_BANDO       	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
          },
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

  public void insertRLivelloBando(long idBando, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRLivelloBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    \n"
          + " INTO                    \n"
          + "   NEMBO_R_LIVELLO_BANDO   \n"
          + "   (                     \n"
          + "     ID_BANDO,      		\n"
          + "     ID_LIVELLO    	   	\n"
          + "   )                     \n"
          + "   VALUES                \n"
          + "   (                     \n"
          + "     :ID_BANDO,      	\n"
          + "     :ID_LIVELLO    	   	\n"
          + "   )            			\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_LIVELLO", idLivello)
          },
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

  public void insertDBandoAmmCompetenza(long idBando, long idAmmCompetenza)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertDBandoAmmCompetenza";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    		\n"
          + " INTO                    		\n"
          + "   NEMBO_D_BANDO_AMM_COMPETENZA    \n"
          + "   (                     		\n"
          + "     ID_BANDO_AMM_COMPETENZA,    \n"
          + "     ID_BANDO,    	   			\n"
          + "     EXT_ID_AMM_COMPETENZA    	\n"
          + "   )                     		\n"
          + "   VALUES                		\n"
          + "   (                     		\n"
          + "     :ID_BANDO_AMM_COMPETENZA,   \n"
          + "     :ID_BANDO,    	   			\n"
          + "     :EXT_ID_AMM_COMPETENZA  	\n"
          + "   )            					\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_AMM_COMPETENZA",
          getNextSequenceValue("SEQ_NEMBO_D_BANDO_AMM_COMPETEN"),
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_AMM_COMPETENZA", idAmmCompetenza,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("EXT_ID_AMM_COMPETENZA", idAmmCompetenza)
          },
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

  public void deleteRLivelloBando(long idBando, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRLivelloBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       	\n"
          + "   NEMBO_R_LIVELLO_BANDO			\n"
          + "WHERE                   		\n"
          + "  ID_BANDO = :ID_BANDO       	\n"
          + "  AND ID_LIVELLO = :ID_LIVELLO \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_LIVELLO", idLivello)
          },
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

  public void deleteRLivelloBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRLivelloBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       	\n"
          + "   NEMBO_R_LIVELLO_BANDO			\n"
          + "WHERE                   		\n"
          + "  ID_BANDO = :ID_BANDO       	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
          },
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

  public List<FileAllegatoDTO> getElencoAllegati(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoAllegati";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 							\n"
          + " 	ID_ALLEGATI_BANDO,  			\n"
          + "	DESCRIZIONE,  					\n"
          + "	NOME_FILE,  					\n"
          + "	ID_BANDO,ORDINE  				\n"
          + " FROM 								\n"
          + "	NEMBO_D_ALLEGATI_BANDO		    \n"
          + " WHERE 							\n"
          + "   ID_BANDO = :ID_BANDO          	\n"
          + "   AND FLAG_VISIBILE = 'S'       	\n"
          + "ORDER BY ORDINE, NOME_FILE			\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return queryForList(SELECT, parameterSource, FileAllegatoDTO.class);
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

  public void insertFileAllegato(FileAllegatoDTO fileDTO)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertFileAllegato";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    													\n"
          + " INTO                    													\n"
          + "   NEMBO_D_ALLEGATI_BANDO    													\n"
          + "   (                     													\n"
          + "     ID_ALLEGATI_BANDO,    													\n"
          + "     DESCRIZIONE,    	   													\n"
          + "     NOME_FILE, 																\n"
          + "     FILE_ALLEGATO,              											\n"
          + "     ID_BANDO,    															\n"
          + "     ORDINE	    															\n"
          + "   )                     													\n"
          + "   VALUES                													\n"
          + "   (                     													\n"
          + "     SEQ_NEMBO_D_ALLEGATI_BANDO.NEXTVAL,   									\n"
          + "     :DESCRIZIONE,    	   													\n"
          + "     :NOME_FILE, 															\n"
          + "     :FILE_ALLEGATO,            												\n"
          + "     :ID_BANDO,    															\n"
          + "     (SELECT NVL( MAX(ORDINE)+1 ,1) FROM NEMBO_D_ALLEGATI_BANDO WHERE ID_BANDO = :ID_BANDO)   \n"
          + "   )            																\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("DESCRIZIONE", fileDTO.getDescrizione(),
          Types.VARCHAR);
      mapParameterSource.addValue("NOME_FILE", fileDTO.getNomeFile(),
          Types.VARCHAR);
      mapParameterSource.addValue("FILE_ALLEGATO",
          new SqlLobValue(fileDTO.getFileAllegato()), Types.BLOB);
      mapParameterSource.addValue("ID_BANDO", fileDTO.getIdBando(),
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("DESCRIZIONE", fileDTO.getDescrizione()),
              new LogParameter("NOME_FILE", fileDTO.getNomeFile()),
              new LogParameter("FILE_ALLEGATO", fileDTO.getFileAllegato()),
              new LogParameter("ID_BANDO", fileDTO.getIdBando()),
          },
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

  public void deleteFileAllegato(long idAllegatiBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteFileAllegato";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       					\n"
          + "   NEMBO_D_ALLEGATI_BANDO						\n"
          + "WHERE                   						\n"
          + "  ID_ALLEGATI_BANDO = :ID_ALLEGATI_BANDO    	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_ALLEGATI_BANDO", idAllegatiBando,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ALLEGATI_BANDO", idAllegatiBando)
          },
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

  public List<DecodificaDTO<String>> getElencoFiltriSelezioneAziende()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoFiltriSelezioneAziende";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                       \n"
          + "   A.ISTRUZIONE_SQL_FILTRO,   \n"
          + "   A.DESCRIZIONE_FILTRO       \n"
          + " FROM                         \n"
          + "   NEMBO_C_QUERY_FILTRO A       \n"
          + "ORDER BY A.DESCRIZIONE_FILTRO \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<DecodificaDTO<String>>>()
          {
            @Override
            public List<DecodificaDTO<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DecodificaDTO<String>> elenco = new Vector<DecodificaDTO<String>>();
              elenco.add(new DecodificaDTO<String>("0", "", "--- nessuno ---"));

              int c = 1;
              while (rs.next())
              {
                elenco.add(new DecodificaDTO<String>(String.valueOf(c),
                    rs.getString("ISTRUZIONE_SQL_FILTRO").replace("\n",
                        "<br/>"),
                    rs.getString("DESCRIZIONE_FILTRO").replace("\n", "<br/>")));
                c++;
              }
              return elenco;
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

  public List<DecodificaDTO<String>> getElencoTipiVincoli()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoTipiVincoli";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                       					\n"
          + "   A.ID_VINCOLO_DICHIARAZIONE AS ID,   		\n"
          + "   A.ID_VINCOLO_DICHIARAZIONE AS CODICE,       \n"
          + "   A.TIPO_VINCOLO AS DESCRIZIONE       		\n"
          + " FROM                         					\n"
          + "   NEMBO_D_VINCOLO_INFO A      					\n"
          + "ORDER BY A.ID_VINCOLO_DICHIARAZIONE 			\n";

      return queryForDecodificaString(SELECT, null);
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

  public void updateFiltroBando(long idBando, String istruzioneSql,
      String descrizioneFiltro) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateFiltroBando";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                                   \n"
          + "   NEMBO_D_BANDO                                            \n"
          + " SET                                                      \n"
          + "   ISTRUZIONE_SQL_FILTRO = :ISTRUZIONE_SQL_FILTRO,        \n"
          + "   DESCRIZIONE_FILTRO = :DESCRIZIONE_FILTRO               \n"
          + " WHERE                                                    \n"
          + "   ID_BANDO = :ID_BANDO                                   \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ISTRUZIONE_SQL_FILTRO", istruzioneSql,
          Types.CLOB);
      mapParameterSource.addValue("DESCRIZIONE_FILTRO", descrizioneFiltro,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ISTRUZIONE_SQL_FILTRO", istruzioneSql),
              new LogParameter("DESCRIZIONE_FILTRO", descrizioneFiltro)
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

  public List<CruscottoInterventiDTO> getInterventi(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getInterventi";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                          	\n"
          + "   A.ID_BANDO,				                                      	\n"
          + "   C.ID_TIPO_AGGREGAZIONE,                                       	\n"
          + "   B.ID_LIVELLO,			                                    	\n"
          + "   C.DESCRIZIONE AS TIPO_INTERVENTO,                             	\n"
          + "   E.ID_DESCRIZIONE_INTERVENTO,                                  	\n"
          + "   E.DESCRIZIONE AS DESCR_INTERVENTO,                            	\n"
          + "   F.ID_TIPO_LOCALIZZAZIONE,                                     	\n"
          + "   F.DESCRIZIONE AS LOCALIZZAZIONE,                              	\n"
          + "   E.FLAG_GESTIONE_COSTO_UNITARIO,                               	\n"
          + "   G.CODICE AS OPERAZIONE,                                       	\n"
          + "   A.COSTO_UNITARIO_MINIMO,                                      	\n"
          + "   A.COSTO_UNITARIO_MASSIMO,                                      	\n"
          + "   MI.DESCRIZIONE AS DESC_MISURAZIONE,                             \n"
          + "   UM.CODICE AS CODICE_MISURA                                      \n"
          + " FROM                                                            	\n"
          + "   NEMBO_R_LIV_BANDO_INTERVENTO A,                                 	\n"
          + "   NEMBO_R_LIVELLO_BANDO B,                                        	\n"
          + "   NEMBO_D_TIPO_AGGREGAZIONE C,                                    	\n"
          + "   NEMBO_R_AGGREGAZIONE_INTERVENT D,                              	\n"
          + "   NEMBO_D_DESCRIZIONE_INTERVENTO E,                               	\n"
          + "   NEMBO_D_TIPO_LOCALIZZAZIONE F,                                  	\n"
          + "   NEMBO_D_LIVELLO G,                                              	\n"
          + "   NEMBO_R_MISURAZIONE_INTERVENTO MI,                              	\n"
          + "   NEMBO_D_UNITA_MISURA UM                                         	\n"
          + " WHERE                                                          	\n"
          + "   A.ID_LIVELLO = B.ID_LIVELLO                                   	\n"
          + "   AND B.ID_BANDO = A.ID_BANDO                                   	\n"
          + "   AND A.ID_DESCRIZIONE_INTERVENTO = D.ID_DESCRIZIONE_INTERVENTO 	\n"
          + "   AND D.ID_TIPO_AGGREGAZIONE_PRIMO_LIV = C.ID_TIPO_AGGREGAZIONE 	\n"
          + "   AND E.ID_DESCRIZIONE_INTERVENTO = A.ID_DESCRIZIONE_INTERVENTO 	\n"
          + "   AND F.ID_TIPO_LOCALIZZAZIONE = E.ID_TIPO_LOCALIZZAZIONE       	\n"
          + "   AND D.ID_DESCRIZIONE_INTERVENTO = MI.ID_DESCRIZIONE_INTERVENTO 	\n"
          + "   AND MI.ID_UNITA_MISURA = UM.ID_UNITA_MISURA(+)                 	\n"
          + "   AND G.ID_LIVELLO = A.ID_LIVELLO                                 \n"
          + "   AND B.ID_BANDO = :ID_BANDO                                    	\n"
          + "ORDER BY C.DESCRIZIONE, E.DESCRIZIONE                            	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<CruscottoInterventiDTO>>()
          {
            @Override
            public List<CruscottoInterventiDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<CruscottoInterventiDTO> list = new ArrayList<CruscottoInterventiDTO>();
              CruscottoInterventiDTO interventoDTO = null;
              InfoMisurazioneIntervento info = null;
              long idDescrInterventoLast = -1;
              long idDescrIntervento = -1;
              while (rs.next())
              {
                idDescrInterventoLast = rs.getLong("ID_DESCRIZIONE_INTERVENTO");
                if (idDescrIntervento != idDescrInterventoLast)
                {
                  idDescrIntervento = idDescrInterventoLast;
                  interventoDTO = new CruscottoInterventiDTO();
                  interventoDTO.setCostoUnitMassimo(
                      rs.getBigDecimal("COSTO_UNITARIO_MASSIMO"));
                  interventoDTO.setCostoUnitMinimo(
                      rs.getBigDecimal("COSTO_UNITARIO_MINIMO"));
                  interventoDTO
                      .setDescrIntervento(rs.getString("DESCR_INTERVENTO"));
                  interventoDTO.setIdBando(rs.getLong("ID_BANDO"));
                  interventoDTO.setIdDescrizioneIntervento(
                      rs.getLong("ID_DESCRIZIONE_INTERVENTO"));
                  interventoDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
                  interventoDTO.setIdTipoAggregazione(
                      rs.getLong("ID_TIPO_AGGREGAZIONE"));
                  interventoDTO.setIdTipoLocalizzazione(
                      rs.getLong("ID_TIPO_LOCALIZZAZIONE"));
                  interventoDTO
                      .setLocalizzazione(rs.getString("LOCALIZZAZIONE"));
                  interventoDTO.setOperazione(rs.getString("OPERAZIONE"));
                  interventoDTO.setFlagGestioneCostoUnitario(
                      rs.getString("FLAG_GESTIONE_COSTO_UNITARIO"));
                  interventoDTO
                      .setTipoIntervento(rs.getString("TIPO_INTERVENTO"));
                  interventoDTO.setInfoMisurazioni(
                      new ArrayList<InfoMisurazioneIntervento>());
                  list.add(interventoDTO);
                }
                info = new InfoMisurazioneIntervento();
                info.setDescMisurazione(rs.getString("DESC_MISURAZIONE"));
                info.setCodiceUnitaMisura(rs.getString("CODICE_MISURA"));
                interventoDTO.getInfoMisurazioni().add(info);
              }
              return list.isEmpty() ? null : list;
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

  public List<CruscottoInterventiDTO> getElencoInterventiSelezionabili(
      long idBando) throws InternalUnexpectedException
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
      QUERY = " SELECT                                                          \n"
          + "   C.ID_DESCRIZIONE_INTERVENTO,                                  \n"
          + "   F.ID_LIVELLO,                                                 \n"
          + "   F.CODICE AS OPERAZIONE,                                       \n"
          + "   C.ID_TIPO_AGGREGAZIONE,                                       \n"
          + "   D.DESCRIZIONE AS TIPO_INTERVENTO,                             \n"
          + "   B.DESCRIZIONE AS DESCR_INTERVENTO                             \n"
          + " FROM                                                            \n"
          + "   NEMBO_D_LIVELLO F,                                              \n"
          + "   NEMBO_R_LIVELLO_BANDO A,                                        \n"
          + "   NEMBO_R_LIVELLO_INTERVENTO E,                                   \n"
          + "   NEMBO_D_DESCRIZIONE_INTERVENTO B,                               \n"
          + "   NEMBO_R_AGGREGAZIONE_INTERVENT C,                              \n"
          + "   NEMBO_D_BANDO F,                                                \n"
          + "   NEMBO_D_TIPO_AGGREGAZIONE D                                     \n"
          + " WHERE                                                           \n"
          + "   A.ID_LIVELLO = E.ID_LIVELLO                                   \n"
          + "   AND F.ID_LIVELLO = E.ID_LIVELLO                               \n"
          + "   AND E.ID_DESCRIZIONE_INTERVENTO = C.ID_DESCRIZIONE_INTERVENTO \n"
          + "   AND C.ID_DESCRIZIONE_INTERVENTO = B.ID_DESCRIZIONE_INTERVENTO \n"
          + "   AND D.ID_TIPO_AGGREGAZIONE = C.ID_TIPO_AGGREGAZIONE_PRIMO_LIV \n"
          + "   AND C.ID_DESCRIZIONE_INTERVENTO  NOT IN                       \n"
          + "       ( SELECT                                                  \n"
          + "            Z.ID_DESCRIZIONE_INTERVENTO                          \n"
          + "         FROM                                                    \n"
          + "            NEMBO_R_LIV_BANDO_INTERVENTO Z                         \n"
          + "          WHERE                                                  \n"
          + "                Z.ID_BANDO = :ID_BANDO                           \n"
          + "                AND Z.ID_LIVELLO = F.ID_LIVELLO)                 \n"

          + "   AND (B.DATA_CESSAZIONE IS NULL                                \n"
          + "         OR TRUNC(B.DATA_CESSAZIONE) >= TRUNc(SYSDATE))          \n"

          + "   AND A.ID_BANDO = F.ID_BANDO                                   \n"
          + "   AND F.FLAG_MASTER = 'N'                                       \n"
          + "   AND A.ID_BANDO = :ID_BANDO                                    \n"
          + " ORDER BY                                                        \n"
          + "       B.DESCRIZIONE ASC                                         \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      return queryForList(QUERY, mapParameterSource,
          CruscottoInterventiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando) },
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

  public List<CruscottoInterventiDTO> getElencoInterventiSelezionati(
      long idBando) throws InternalUnexpectedException
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
      QUERY = " SELECT                                                          \n"
          + "   C.ID_DESCRIZIONE_INTERVENTO,                                  \n"
          + "   F.ID_LIVELLO,                                                 \n"
          + "   F.CODICE AS OPERAZIONE,                                       \n"
          + "   C.ID_TIPO_AGGREGAZIONE,                                       \n"
          + "   D.DESCRIZIONE AS TIPO_INTERVENTO,                             \n"
          + "   B.DESCRIZIONE AS DESCR_INTERVENTO                             \n"
          + " FROM                                                            \n"
          + "   NEMBO_D_LIVELLO F,                                              \n"
          + "   NEMBO_R_LIVELLO_BANDO A,                                        \n"
          + "   NEMBO_R_LIVELLO_INTERVENTO E,                                   \n"
          + "   NEMBO_D_DESCRIZIONE_INTERVENTO B,                               \n"
          + "   NEMBO_R_AGGREGAZIONE_INTERVENT C,                              \n"
          + "   NEMBO_D_BANDO F,                                                \n"
          + "   NEMBO_D_TIPO_AGGREGAZIONE D                                     \n"
          + " WHERE                                                           \n"
          + "   A.ID_LIVELLO = E.ID_LIVELLO                                   \n"
          + "   AND F.ID_LIVELLO = E.ID_LIVELLO                               \n"
          + "   AND E.ID_DESCRIZIONE_INTERVENTO = C.ID_DESCRIZIONE_INTERVENTO \n"
          + "   AND C.ID_DESCRIZIONE_INTERVENTO = B.ID_DESCRIZIONE_INTERVENTO \n"
          + "   AND D.ID_TIPO_AGGREGAZIONE = C.ID_TIPO_AGGREGAZIONE_PRIMO_LIV \n"
          + "   AND C.ID_DESCRIZIONE_INTERVENTO  IN                       	  \n"
          + "       ( SELECT                                                  \n"
          + "            Z.ID_DESCRIZIONE_INTERVENTO                          \n"
          + "         FROM                                                    \n"
          + "            NEMBO_R_LIV_BANDO_INTERVENTO Z                         \n"
          + "          WHERE                                                  \n"
          + "                Z.ID_BANDO = :ID_BANDO                           \n"
          + "                AND Z.ID_LIVELLO = F.ID_LIVELLO)                 \n"
          + "   AND B.DATA_CESSAZIONE IS NULL                                 \n"
          + "   AND A.ID_BANDO = F.ID_BANDO                                   \n"
          + "   AND F.FLAG_MASTER = 'N'                                       \n"
          + "   AND A.ID_BANDO = :ID_BANDO                                    \n"
          + " ORDER BY                                                        \n"
          + "       B.DESCRIZIONE ASC                                         \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      return queryForList(QUERY, mapParameterSource,
          CruscottoInterventiDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando) },
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

  public boolean isInterventoEsistente(long idBando,
      long idDescrizioneIntervento) throws InternalUnexpectedException
  {
    String THIS_METHOD = "isInterventoEsistente";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                               \n"
          + "   A.ID_BANDO                                                         \n"
          + " FROM                                                                 \n"
          + "   NEMBO_R_LIV_BANDO_INTERVENTO A                                       \n"
          + " WHERE                                                                \n"
          + "   A.ID_BANDO = :ID_BANDO                                             \n"
          + "   AND A.ID_DESCRIZIONE_INTERVENTO = :ID_DESCRIZIONE_INTERVENTO       \n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          idDescrizioneIntervento, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
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

  public void insertRLivBandoIntervento(long idBando,
      long idDescrizioneIntervento, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRLivBandoIntervento";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    		\n"
          + " INTO                    		\n"
          + "   NEMBO_R_LIV_BANDO_INTERVENTO    \n"
          + "   (                     		\n"
          + "     ID_BANDO,    				\n"
          + "     ID_DESCRIZIONE_INTERVENTO,  \n"
          + "     ID_LIVELLO    		        \n"
          + "   )                     		\n"
          + "   VALUES                		\n"
          + "   (                     		\n"
          + "     :ID_BANDO,    				\n"
          + "     :ID_DESCRIZIONE_INTERVENTO, \n"
          + "     :ID_LIVELLO    		        \n"
          + "   )            					\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          idDescrizioneIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_DESCRIZIONE_INTERVENTO",
                  idDescrizioneIntervento),
              new LogParameter("ID_LIVELLO", idLivello)
          },
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

  public void deleteRLivBandoIntervento(long idBando,
      long idDescrizioneIntervento, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRLivBandoIntervento";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       									\n"
          + "   NEMBO_R_LIV_BANDO_INTERVENTO									\n"
          + "WHERE                   										\n"
          + "  ID_BANDO = :ID_BANDO       									\n"
          + "  AND ID_DESCRIZIONE_INTERVENTO = :ID_DESCRIZIONE_INTERVENTO   \n"
          + "  AND ID_LIVELLO = :ID_LIVELLO       							\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          idDescrizioneIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_DESCRIZIONE_INTERVENTO",
                  idDescrizioneIntervento),
              new LogParameter("ID_LIVELLO", idLivello)
          },
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

  public void deleteRLivBandoIntervento(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRLivBandoIntervento";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       									\n"
          + "   NEMBO_R_LIV_BANDO_INTERVENTO									\n"
          + "WHERE                   										\n"
          + "  ID_BANDO = :ID_BANDO       									\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
          },
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

  public void updateRLivBandoIntervento(long idBando,
      long idDescrizioneIntervento,
      long idLivello, BigDecimal costoUnitMin, BigDecimal costoUnitMax)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateRLivBandoIntervento";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " UPDATE                       									\n"
          + "   NEMBO_R_LIV_BANDO_INTERVENTO									\n"
          + "SET 															\n"
          + " COSTO_UNITARIO_MINIMO = :COSTO_UNITARIO_MINIMO,        		\n"
          + " COSTO_UNITARIO_MASSIMO = :COSTO_UNITARIO_MASSIMO              \n"
          + "WHERE                   										\n"
          + "  ID_BANDO = :ID_BANDO       									\n"
          + "  AND ID_DESCRIZIONE_INTERVENTO = :ID_DESCRIZIONE_INTERVENTO   \n"
          + "  AND ID_LIVELLO = :ID_LIVELLO       							\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_DESCRIZIONE_INTERVENTO",
          idDescrizioneIntervento, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("COSTO_UNITARIO_MINIMO", costoUnitMin,
          Types.DECIMAL);
      mapParameterSource.addValue("COSTO_UNITARIO_MASSIMO", costoUnitMax,
          Types.DECIMAL);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_DESCRIZIONE_INTERVENTO",
                  idDescrizioneIntervento),
              new LogParameter("ID_LIVELLO", idLivello),
              new LogParameter("COSTO_UNITARIO_MINIMO", costoUnitMin),
              new LogParameter("COSTO_UNITARIO_MASSIMO", costoUnitMax)
          },
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

  public List<GruppoOggettoDTO> getElencoGruppiOggettiBandoMaster(long idBando,
      long idGruppoOggetto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoGruppiOggettiBandoMaster";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                                 \n"
          + "     A.ID_BANDO_MASTER,                                                     \n"
          + "     A.ID_BANDO,                                                            \n"
          + "     E.ID_GRUPPO_OGGETTO,                                                   \n"
          + "     C.ID_BANDO_OGGETTO AS ID_BANDO_OGG_MASTER,                             \n"
          + "     E.CODICE AS COD_GRUPPO,                                                \n"
          + "     E.DESCRIZIONE AS DESCR_GRUPPO,                                         \n"
          + "     D.ID_OGGETTO,                                                          \n"
          + "     D.CODICE AS COD_OGGETTO,                                               \n"
          + "     D.DESCRIZIONE AS DESCR_OGGETTO,                                        \n"
          + "     D.FLAG_ISTANZA,                                                        \n"
          + "     NVL(MIO_BANDO.FLAG_ATTIVO,'N') AS FLAG_ATTIVO,                         \n"
          + "     MIO_BANDO.ID_BANDO_OGGETTO,                                            \n"
          + "     MIO_BANDO.DATA_INIZIO,                                                 \n"
          + "     MIO_BANDO.DATA_FINE,                                                   \n"
          + "     MIO_BANDO.DATA_RITARDO,                                                \n"
          + "     B.ID_LEGAME_GRUPPO_OGGETTO,                                            \n"
          + "     (TRUNC(NVL(C.DATA_FINE,SYSDATE))) AS DATA_FINE_MASTER                  \n"
          + "   FROM                                                                     \n"
          + "     NEMBO_R_BANDO_MASTER A,                                                  \n"
          + "     NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                                         \n"
          + "     NEMBO_R_BANDO_OGGETTO C,                                                 \n"
          + "     NEMBO_D_OGGETTO D,                                                       \n"
          + "     NEMBO_D_GRUPPO_OGGETTO E,                                                \n"
          + "     (SELECT                                                                \n"
          + "        F2.ID_OGGETTO,                                                      \n"
          + "        F.DATA_INIZIO,                                                      \n"
          + "        F.DATA_FINE,                                                        \n"
          + "        F.DATA_RITARDO,                                                     \n"
          + "        F.ID_BANDO_OGGETTO,                                                 \n"
          + "        F.FLAG_ATTIVO,                                                      \n"
          + "        F.ID_LEGAME_GRUPPO_OGGETTO                                          \n"
          + "      FROM                                                                  \n"
          + "        NEMBO_R_BANDO_OGGETTO F,                                              \n"
          + "        NEMBO_R_LEGAME_GRUPPO_OGGETTO F2                                      \n"
          + "      WHERE                                                                 \n"
          + "        F.ID_BANDO = :ID_BANDO                                              \n"
          + "        AND F.ID_LEGAME_GRUPPO_OGGETTO = F2.ID_LEGAME_GRUPPO_OGGETTO        \n"
          + "     ) MIO_BANDO                                                            \n"
          + "   WHERE                                                                    \n"
          + "     A.ID_BANDO_MASTER = C.ID_BANDO                                         \n"
          + "     AND B.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO            \n"
          + "     AND B.ID_OGGETTO = D.ID_OGGETTO                                        \n"
          + "     AND B.ID_GRUPPO_OGGETTO = E.ID_GRUPPO_OGGETTO                          \n"
          + "     AND MIO_BANDO.ID_LEGAME_GRUPPO_OGGETTO(+) = B.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "     AND C.FLAG_ATTIVO = 'S'                                                \n"
          + "     AND E.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO                           \n"
          + "     AND TRUNC(C.DATA_INIZIO) <= TRUNC(SYSDATE)                             \n"
          + "     AND A.ID_BANDO = :ID_BANDO                                             \n"
          + "   ORDER BY                                                                 \n"
          + "     E.ORDINE, B.ORDINE                                                     \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      mapParameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoOggettoDTO>>()
          {
            @Override
            public List<GruppoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoOggettoDTO> list = new ArrayList<GruppoOggettoDTO>();
              ArrayList<OggettiIstanzeDTO> oggetti = null;
              GruppoOggettoDTO gruppoDTO = null;
              long idGruppoOggetto = 0;
              long lastIdGruppoOggetto;
              while (rs.next())
              {
                lastIdGruppoOggetto = rs.getLong("ID_GRUPPO_OGGETTO");
                if (idGruppoOggetto != lastIdGruppoOggetto)
                {
                  idGruppoOggetto = lastIdGruppoOggetto;
                  gruppoDTO = new GruppoOggettoDTO();
                  gruppoDTO.setIdBando(rs.getLong("ID_BANDO"));
                  gruppoDTO.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                  gruppoDTO.setIdGruppoOggetto(lastIdGruppoOggetto);
                  gruppoDTO.setCodGruppo(rs.getString("COD_GRUPPO"));
                  gruppoDTO.setDescrGruppo(rs.getString("DESCR_GRUPPO"));
                  oggetti = new ArrayList<OggettiIstanzeDTO>();
                  gruppoDTO.setOggetti(oggetti);
                  list.add(gruppoDTO);
                }

                Date dataFineMaster = rs.getTimestamp("DATA_FINE_MASTER");
                Date sysdate = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(sysdate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                sysdate = cal.getTime();

                OggettiIstanzeDTO oggetto = new OggettiIstanzeDTO();
                oggetto.setIdBando(rs.getLong("ID_BANDO"));
                oggetto.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                oggetto.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                oggetto
                    .setIdBandoOggettoMaster(rs.getLong("ID_BANDO_OGG_MASTER"));
                oggetto.setIdLegameGruppoOggetto(
                    rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));
                oggetto.setIdOggetto(rs.getLong("ID_OGGETTO"));
                oggetto.setCodOggetto(rs.getString("COD_OGGETTO"));
                oggetto.setDescrOggetto(rs.getString("DESCR_OGGETTO"));
                oggetto.setFlagIstanza(rs.getString("FLAG_ISTANZA"));
                oggetto.setFlagAttivo(rs.getString("FLAG_ATTIVO"));
                oggetto.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                oggetto.setDataFine(rs.getTimestamp("DATA_FINE"));
                oggetto.setDataRitardo(rs.getTimestamp("DATA_RITARDO"));

                if (sysdate.compareTo(dataFineMaster) > 0)
                {
                  oggetto.setFlagMasterScaduto("S");
                }
                else
                {
                  oggetto.setFlagMasterScaduto("N");
                }

                oggetti.add(oggetto);
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("idGruppoOggetto", idGruppoOggetto) },
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

  public List<GruppoOggettoDTO> getElencoGruppiOggetti(long idBando,
      String flagIstanza) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoGruppiOggetti";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                    \n"
          + "   C.ID_BANDO,                                                 \n"
          + "   E.ID_GRUPPO_OGGETTO,                                        \n"
          + "   E.CODICE AS COD_GRUPPO,                                     \n"
          + "   E.DESCRIZIONE AS DESCR_GRUPPO,                              \n"
          + "   D.ID_OGGETTO,                                           	\n"
          + "   D.CODICE AS COD_OGGETTO,                                    \n"
          + "   D.DESCRIZIONE AS DESCR_OGGETTO,                             \n"
          + "   D.FLAG_ISTANZA,                                             \n"
          + "   C.FLAG_ATTIVO,                                              \n"
          + "   F.ID_BANDO_OGGETTO,                                         \n"
          + "   F.DATA_INIZIO,                                            	\n"
          + "   F.DATA_FINE,                                            	\n"
          + "   F.DATA_RITARDO,                                           	\n"
          + "   B.ID_LEGAME_GRUPPO_OGGETTO                                  \n"
          + " FROM                                                          \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                              \n"
          + "   NEMBO_R_BANDO_OGGETTO C,                                      \n"
          + "   NEMBO_D_OGGETTO D,                                            \n"
          + "   NEMBO_D_GRUPPO_OGGETTO E,                                     \n"
          + "   NEMBO_R_BANDO_OGGETTO F                                       \n"
          + " WHERE                                                         \n"
          + "   F.ID_BANDO(+) = C.ID_BANDO                  				\n"
          + "   AND B.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND B.ID_OGGETTO = D.ID_OGGETTO                             \n"
          + "   AND B.ID_GRUPPO_OGGETTO = E.ID_GRUPPO_OGGETTO               \n"
          + "   AND F.ID_BANDO_OGGETTO(+) = C.ID_BANDO_OGGETTO              \n"
          + "   AND C.ID_BANDO = :ID_BANDO                                  \n"
          + ((flagIstanza == null) ? ""
              : " AND D.FLAG_ISTANZA = :FLAG_ISTANZA   \n")
          + " ORDER BY                                                      \n"
          + "   E.ORDINE,B.ORDINE                                           \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      mapParameterSource.addValue("FLAG_ISTANZA", flagIstanza);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoOggettoDTO>>()
          {
            @Override
            public List<GruppoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoOggettoDTO> list = new ArrayList<GruppoOggettoDTO>();
              ArrayList<OggettiIstanzeDTO> oggetti = null;
              GruppoOggettoDTO gruppoDTO = null;
              long idGruppoOggetto = 0;
              long lastIdGruppoOggetto;
              while (rs.next())
              {
                lastIdGruppoOggetto = rs.getLong("ID_GRUPPO_OGGETTO");
                if (idGruppoOggetto != lastIdGruppoOggetto)
                {
                  idGruppoOggetto = lastIdGruppoOggetto;
                  gruppoDTO = new GruppoOggettoDTO();
                  gruppoDTO.setIdBando(rs.getLong("ID_BANDO"));
                  gruppoDTO.setIdGruppoOggetto(lastIdGruppoOggetto);
                  gruppoDTO.setCodGruppo(rs.getString("COD_GRUPPO"));
                  gruppoDTO.setDescrGruppo(rs.getString("DESCR_GRUPPO"));
                  oggetti = new ArrayList<OggettiIstanzeDTO>();
                  gruppoDTO.setOggetti(oggetti);
                  list.add(gruppoDTO);
                }

                OggettiIstanzeDTO oggetto = new OggettiIstanzeDTO();
                oggetto.setIdBando(rs.getLong("ID_BANDO"));
                oggetto.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                oggetto.setIdLegameGruppoOggetto(
                    rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));
                oggetto.setIdOggetto(rs.getLong("ID_OGGETTO"));
                oggetto.setCodOggetto(rs.getString("COD_OGGETTO"));
                oggetto.setDescrOggetto(rs.getString("DESCR_OGGETTO"));
                oggetto.setDescrGruppoOggetto(gruppoDTO.getDescrGruppo() + "-"
                    + rs.getString("DESCR_OGGETTO"));
                oggetto.setFlagIstanza(rs.getString("FLAG_ISTANZA"));
                oggetto.setFlagAttivo(rs.getString("FLAG_ATTIVO"));
                oggetto.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                oggetto.setDataFine(rs.getTimestamp("DATA_FINE"));
                oggetto.setDataRitardo(rs.getTimestamp("DATA_RITARDO"));
                oggetti.add(oggetto);
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando) },
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

  public void deleteByIdBAndoOggetto(String nomeTabella, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteByIdBAndoOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       									\n"
          + "   " + nomeTabella + "		    									\n"
          + "WHERE                   										\n"
          + "  ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO							\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
          },
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

  public void deleteByIdBAndoQuadroOggetto(String nomeTabella,
      long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteByIdBAndoOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       									\n"
          + "   " + nomeTabella + "		    									\n"
          + "WHERE                   										\n"
          + "  ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO							\n"
          + "  AND ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO		    		\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public List<Long> getIdLegameInfo(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getIdLegameInfo";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT DISTINCT                              \n"
          + "  A.ID_LEGAME_INFO                            \n"
          + " FROM                                         \n"
          + "   NEMBO_D_DETTAGLIO_INFO A,                    \n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO B,              \n"
          + "   NEMBO_D_GRUPPO_INFO C                        \n"
          + " WHERE                                        \n"
          + "   B.ID_QUADRO_OGGETTO = C.ID_QUADRO_OGGETTO  \n"
          + "   AND B.ID_BANDO_OGGETTO = C.ID_BANDO_OGGETTO \n"
          + "   AND C.ID_GRUPPO_INFO = A.ID_GRUPPO_INFO    \n"
          + "   AND A.ID_LEGAME_INFO IS NOT NULL           \n"
          + "   AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n";

      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<Long>>()
          {
            @Override
            public List<Long> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<Long> list = new ArrayList<Long>();
              while (rs.next())
              {
                list.add(rs.getLong("ID_LEGAME_INFO"));
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto) },
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

  public List<Long> getIdLegameInfo(long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getIdLegameInfo";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT DISTINCT                              		\n"
          + "  A.ID_LEGAME_INFO                            		\n"
          + " FROM                                         		\n"
          + "   NEMBO_D_DETTAGLIO_INFO A,                    		\n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO B,              		\n"
          + "   NEMBO_D_GRUPPO_INFO C                        		\n"
          + " WHERE                                        		\n"
          + "   B.ID_QUADRO_OGGETTO = C.ID_QUADRO_OGGETTO  		\n"
          + "   AND B.ID_BANDO_OGGETTO = C.ID_BANDO_OGGETTO  	\n"
          + "   AND C.ID_GRUPPO_INFO = A.ID_GRUPPO_INFO    		\n"
          + "   AND A.ID_LEGAME_INFO IS NOT NULL           		\n"
          + "   AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO 		\n"
          + "   AND B.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO 	\n";

      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<Long>>()
          {
            @Override
            public List<Long> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<Long> list = new ArrayList<Long>();
              while (rs.next())
              {
                list.add(rs.getLong("ID_LEGAME_INFO"));
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public void deleteDDettaglioInfo(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteDDettaglioInfo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                                           \n"
          + "   NEMBO_D_DETTAGLIO_INFO X                         \n"
          + " WHERE                                            \n"
          + "   X.ID_DETTAGLIO_INFO IN (                       \n"
          + "     SELECT                                       \n"
          + "      A.ID_DETTAGLIO_INFO                         \n"
          + "     FROM                                         \n"
          + "       NEMBO_D_DETTAGLIO_INFO A,                    \n"
          + "       NEMBO_R_BANDO_OGGETTO_QUADRO B,              \n"
          + "       NEMBO_D_GRUPPO_INFO C                        \n"
          + "     WHERE                                        \n"
          + "       B.ID_QUADRO_OGGETTO = C.ID_QUADRO_OGGETTO  \n"
          + "       AND B.ID_BANDO_OGGETTO = C.ID_BANDO_OGGETTO \n"
          + "       AND C.ID_GRUPPO_INFO = A.ID_GRUPPO_INFO    \n"
          + "       AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n"
          + "   )                                              \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
          },
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

  public void deleteDDettaglioInfo(long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteDDettaglioInfo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                                           		\n"
          + "   NEMBO_D_DETTAGLIO_INFO X                         		\n"
          + " WHERE                                            		\n"
          + "   X.ID_DETTAGLIO_INFO IN (                       		\n"
          + "     SELECT                                       		\n"
          + "      A.ID_DETTAGLIO_INFO                         		\n"
          + "     FROM                                         		\n"
          + "       NEMBO_D_DETTAGLIO_INFO A,                    		\n"
          + "       NEMBO_R_BANDO_OGGETTO_QUADRO B,              		\n"
          + "       NEMBO_D_GRUPPO_INFO C                        		\n"
          + "     WHERE                                        		\n"
          + "       B.ID_QUADRO_OGGETTO = C.ID_QUADRO_OGGETTO  		\n"
          + "       AND B.ID_BANDO_OGGETTO = C.ID_BANDO_OGGETTO  	\n"
          + "       AND C.ID_GRUPPO_INFO = A.ID_GRUPPO_INFO    		\n"
          + "       AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO 		\n"
          + "       AND B.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO 	\n"
          + "   )                                              		\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public void deteteDLegameInfo(List<Long> listIdLegameInfo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deteteDLegameInfo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                                           	\n"
          + "   NEMBO_D_LEGAME_INFO                          		\n"
          + " WHERE                                            	\n"
          + getInCondition("ID_LEGAME_INFO", new Vector<Long>(listIdLegameInfo),
              false);

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
          },
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

  public long insertRBandoOggetto(long idBando, long idLegameGruppoOggetto,
      Date dataApertura, Date dataChiusura, Date dataProroga)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRBandoOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    		\n"
          + " INTO                    		\n"
          + "   NEMBO_R_BANDO_OGGETTO		    \n"
          + "   (                     		\n"
          + "     ID_BANDO_OGGETTO,    		\n"
          + "     ID_BANDO,  					\n"
          + "     ID_LEGAME_GRUPPO_OGGETTO,   \n"
          + "     FLAG_ATTIVO,    		    \n"
          + "     DATA_INIZIO,    		    \n"
          + "     DATA_FINE,    		        \n"
          + "     DATA_RITARDO    		    \n"
          + "   )                     		\n"
          + "   VALUES                		\n"
          + "   (                     		\n"
          + "     :ID_BANDO_OGGETTO,    		\n"
          + "     :ID_BANDO,  				\n"
          + "     :ID_LEGAME_GRUPPO_OGGETTO,  \n"
          + "     'N',  						\n"
          + "     :DATA_INIZIO,    		    \n"
          + "     :DATA_FINE,    		        \n"
          + "     :DATA_RITARDO    		    \n"
          + "   )            					\n";

      long idBandoOggetto = getNextSequenceValue("SEQ_NEMBO_R_BANDO_OGGETTO");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_LEGAME_GRUPPO_OGGETTO",
          idLegameGruppoOggetto, Types.NUMERIC);
      mapParameterSource.addValue("DATA_INIZIO", dataApertura, Types.TIMESTAMP);
      mapParameterSource.addValue("DATA_FINE", dataChiusura, Types.TIMESTAMP);
      mapParameterSource.addValue("DATA_RITARDO", dataProroga, Types.TIMESTAMP);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_LEGAME_GRUPPO_OGGETTO",
                  idLegameGruppoOggetto),
              new LogParameter("DATA_INIZIO", dataApertura),
              new LogParameter("DATA_FINE", dataChiusura),
              new LogParameter("DATA_RITARDO", dataProroga)
          },
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

  public void insertRBandoOggettoIconaFromMaster(long idBandoMaster,
      long idBandoOggetto, long idBandoOggettoMaster)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRBandoOggettoIconaFromMaster";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    		          			\n"
          + " INTO                    		          			\n"
          + "   NEMBO_R_BANDO_OGGETTO_ICONA		          			\n"
          + "   (                     		          			\n"
          + "     ID_BANDO_OGGETTO,    		   	      			\n"
          + "     ID_OGGETTO_ICONA  					  			\n"
          + "   )                     		          			\n"
          + " SELECT                                    			\n"
          + "   " + idBandoOggetto + " AS ID_BANDO_OGGETTO, 			\n"
          + "   A.ID_OGGETTO_ICONA                      			\n"
          + " FROM                                      			\n"
          + "  NEMBO_R_BANDO_OGGETTO_ICONA A,             			\n"
          + "  NEMBO_R_BANDO_OGGETTO B                    			\n"
          + " WHERE                                     			\n"
          + "  B.ID_BANDO_OGGETTO = A.ID_BANDO_OGGETTO  			\n"
          + "  AND B.ID_BANDO = :ID_BANDO_MASTER        			\n"
          + "  AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO_MASTER  \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_MASTER", idBandoMaster,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO_MASTER",
          idBandoOggettoMaster, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBandoMaster),
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_BANDO_OGGETTO_MASTER", idBandoOggettoMaster),
          },
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

  public void updateRBandoOggetto(long idBandoOggetto, Date dataInizio,
      Date dataFine, Date dataRitardo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateRBandoOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " UPDATE                       			\n"
          + "   NEMBO_R_BANDO_OGGETTO					\n"
          + "SET 									\n"
          + " DATA_INIZIO  = :DATA_INIZIO,        	\n"
          + " DATA_FINE    = :DATA_FINE,            \n"
          + " DATA_RITARDO = :DATA_RITARDO          \n"
          + "WHERE                   				\n"
          + "  ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("DATA_INIZIO", dataInizio, Types.TIMESTAMP);
      mapParameterSource.addValue("DATA_FINE", dataFine, Types.TIMESTAMP);
      mapParameterSource.addValue("DATA_RITARDO", dataRitardo, Types.TIMESTAMP);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("DATA_INIZIO", dataInizio),
              new LogParameter("DATA_FINE", dataFine),
              new LogParameter("DATA_RITARDO", dataRitardo)
          },
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

  public List<QuadroDTO> getElencoQuadriDisponibili(long idBando,
      long idGruppoOggetto, long idOggetto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoQuadriDisponibili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                             \n"
          + "    A.FLAG_OBBLIGATORIO,                                            \n"
          + "    H.ID_GRUPPO_OGGETTO,                                            \n"
          + "    H.CODICE AS COD_GRUPPO,                                         \n"
          + "    H.DESCRIZIONE AS DESCR_GRUPPO,                                  \n"
          + "    F.ID_OGGETTO,                                                   \n"
          + "    F.CODICE AS COD_OGGETTO,                                        \n"
          + "    F.DESCRIZIONE AS DESCR_OGGETTO,                                 \n"
          + "    E.ID_QUADRO,                                                    \n"
          + "    E.CODICE AS COD_QUADRO,                                         \n"
          + "    E.DESCRIZIONE AS DESCR_QUADRO,                                  \n"
          + "    E.DATA_INIZIO,                                                  \n"
          + "    E.DATA_FINE,                                                    \n"
          + "    B.ID_QUADRO_OGGETTO,                                            \n"
          + "    C.ID_BANDO_OGGETTO,                                             \n"
          + "    M.FLAG_ATTIVO,                                            		 \n"
          + "	NVL( (                                                           \n"
          + "	    SELECT                                       				 \n"
          + "	      I.ID_QUADRO_OGGETTO    									 \n"
          + "	    FROM                                    					 \n"
          + "	      NEMBO_R_BANDO_OGGETTO_QUADRO I,              				 \n"
          + "	      NEMBO_R_BANDO_OGGETTO L   									 \n"
          + "	    WHERE                                       				 \n"
          + "	      I.ID_BANDO_OGGETTO(+) = L.ID_BANDO_OGGETTO  				 \n"
          + "	      AND L.ID_BANDO = :ID_BANDO  								 \n"
          + "	      and I.ID_QUADRO_OGGETTO = A.ID_QUADRO_OGGETTO				 \n"
          + "	      and I.ID_BANDO_OGGETTO = M.ID_BANDO_OGGETTO				 \n"
          + "	      ),0)  ID_QUADRO_ESIST,	  								 \n"
          + "    M.ID_BANDO_OGGETTO AS ID_BANDO_OGG_ESIST,						 \n"
          + " 	 E.descrizione_estesa AS DESCRIZIONE_ESTESA,					 \n"
          + "    NVL(length(immagine),0) AS	DIMENSIONE_IMMAGINE		             \n"
          + "  FROM                                                              \n"
          + "    NEMBO_R_QUADRO_OGGETTO A,                                         \n"
          + "    NEMBO_R_BANDO_OGGETTO_QUADRO B,                                   \n"
          + "    NEMBO_R_BANDO_OGGETTO C,                                          \n"
          + "    NEMBO_R_BANDO_MASTER D,                                           \n"
          + "    NEMBO_D_QUADRO E,                                                 \n"
          + "    NEMBO_D_OGGETTO F,                                                \n"
          + "    NEMBO_R_LEGAME_GRUPPO_OGGETTO G,                                  \n"
          + "    NEMBO_D_GRUPPO_OGGETTO H,                                         \n"
          + "    NEMBO_R_BANDO_OGGETTO M                                           \n"
          + "  WHERE                                                             \n"
          + "    A.ID_QUADRO_OGGETTO = B.ID_QUADRO_OGGETTO                       \n"
          + "    AND B.ID_BANDO_OGGETTO = C.ID_BANDO_OGGETTO                     \n"
          + "    AND C.ID_BANDO = D.ID_BANDO_MASTER                              \n"
          + "    AND E.ID_QUADRO = A.ID_QUADRO                                   \n"
          + "    AND F.ID_OGGETTO = A.ID_OGGETTO                                 \n"
          + "    AND G.ID_OGGETTO = F.ID_OGGETTO                                 \n"
          + "    AND G.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO     \n"
          + "    AND H.ID_GRUPPO_OGGETTO =G.ID_GRUPPO_OGGETTO                    \n"
          + "    AND M.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO     \n"
          + "    AND M.ID_BANDO = :ID_BANDO                                      \n"
          + "    AND D.ID_BANDO = :ID_BANDO                                      \n"
          + "    AND H.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO                    \n"
          + "    AND F.ID_OGGETTO = :ID_OGGETTO                    				 \n"
          + "    AND C.ID_LEGAME_GRUPPO_OGGETTO IN (                             \n"
          + "        SELECT ID_LEGAME_GRUPPO_OGGETTO                             \n"
          + "        FROM NEMBO_R_BANDO_OGGETTO                                    \n"
          + "        WHERE ID_BANDO = :ID_BANDO                                  \n"
          + "    )                                                               \n"
          + "  ORDER BY H.ORDINE, G.ORDINE, A.ORDINE                             \n";

      mapParameterSource.addValue("ID_BANDO", idBando);
      mapParameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto);
      mapParameterSource.addValue("ID_OGGETTO", idOggetto);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<QuadroDTO>>()
          {
            @Override
            public List<QuadroDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<QuadroDTO> list = new ArrayList<QuadroDTO>();
              QuadroDTO quadroDTO = null;
              while (rs.next())
              {
                quadroDTO = new QuadroDTO();
                quadroDTO.setIdGruppoOggetto(rs.getLong("ID_GRUPPO_OGGETTO"));
                quadroDTO.setIdOggetto(rs.getLong("ID_OGGETTO"));
                quadroDTO.setIdQuadro(rs.getLong("ID_QUADRO"));
                quadroDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGG_ESIST"));
                quadroDTO
                    .setIdBandoOggettoMaster(rs.getLong("ID_BANDO_OGGETTO"));
                quadroDTO.setIdQuadroOggetto(rs.getLong("ID_QUADRO_ESIST"));
                quadroDTO
                    .setIdQuadroOggettoMaster(rs.getLong("ID_QUADRO_OGGETTO"));
                quadroDTO.setCodGruppo(rs.getString("COD_GRUPPO"));
                quadroDTO.setCodice(rs.getString("COD_QUADRO"));
                quadroDTO.setCodOggetto(rs.getString("COD_OGGETTO"));
                quadroDTO.setDescrGruppo(rs.getString("DESCR_GRUPPO"));
                quadroDTO.setDescrizione(rs.getString("DESCR_QUADRO"));
                quadroDTO.setDescrOggetto(rs.getString("DESCR_OGGETTO"));
                quadroDTO.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                quadroDTO.setDataFine(rs.getTimestamp("DATA_FINE"));
                quadroDTO
                    .setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                quadroDTO.setQuadroEsistente(rs.getLong("ID_QUADRO_ESIST") > 0);
                quadroDTO.setFlagAttivo(rs.getString("FLAG_ATTIVO"));
                quadroDTO
                    .setDescrizioneEstesa(rs.getString("DESCRIZIONE_ESTESA"));
                quadroDTO
                    .setDimensioneImmagine(rs.getInt("DIMENSIONE_IMMAGINE"));
                list.add(quadroDTO);
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idGruppoOggetto", idGruppoOggetto),
              new LogParameter("idOggetto", idOggetto),
              new LogParameter("ID_BANDO", idBando)
          },
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

  public long insertRBandoOggettoQuadro(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRBandoOggettoQuadro";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    		\n"
          + " INTO                    		\n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO    \n"
          + "   (                     		\n"
          + "     ID_BANDO_OGGETTO,    		\n"
          + "     ID_QUADRO_OGGETTO  	    	\n"
          + "   )                     		\n"
          + "   VALUES                		\n"
          + "   (                     		\n"
          + "     :ID_BANDO_OGGETTO,    		\n"
          + "     :ID_QUADRO_OGGETTO   		\n"
          + "   )            					\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public void deleteTValoriParametri(long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteControlliQuadri";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                                         \n"
          + "   NEMBO_T_VALORI_PARAMETRI X                     \n"
          + " WHERE        									 \n"
          + "   X.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO       \n"
          + "   AND X.ID_CONTROLLO IN (                    	 \n"
          + "     SELECT                                     \n"
          + "      A.ID_CONTROLLO                         	 \n"
          + "     FROM                                       \n"
          + "       NEMBO_R_QUADRO_OGGETTO_CONTROL A         \n"
          + "     WHERE                                      \n"
          + "       A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO \n"
          + "   )                                            \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public void deleteValoreParametro(long idBandoOggetto, long idControllo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteControlliQuadri";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                                        \n"
          + "   NEMBO_T_VALORI_PARAMETRI                      \n"
          + " WHERE        									\n"
          + "   ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO       	\n"
          + "   AND ID_CONTROLLO = :ID_CONTROLLO       		\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_CONTROLLO", idControllo)
          },
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

  public List<DecodificaDTO<String>> getElencoGruppiControlliDisponibili(
      long idBando, String codiceQuadro) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoGruppiControlliDisponibili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                    \n"
          + "   E.ID_QUADRO_OGGETTO || '_' || A.ID_BANDO_OGGETTO AS ID, \n"
          + "   C.CODICE,                                               \n"
          + "   C.DESCRIZIONE,                                          \n"
          + "   G.DESCRIZIONE AS DESCR_OGGETTO                          \n"
          + " FROM                                                      \n"
          + "   NEMBO_R_BANDO_OGGETTO a,                                  \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                          \n"
          + "   NEMBO_D_GRUPPO_OGGETTO C,                                 \n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO D,                           \n"
          + "   NEMBO_R_QUADRO_OGGETTO E,                                 \n"
          + "   NEMBO_D_QUADRO F,                                         \n"
          + "   NEMBO_D_OGGETTO G,                                        \n"
          + "   NEMBO_D_BANDO H                                  			\n"
          + " WHERE                                                     \n"
          + "   A.ID_LEGAME_GRUPPO_OGGETTO = B.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND B.ID_GRUPPO_OGGETTO = C.ID_GRUPPO_OGGETTO           \n"
          + "   AND A.ID_BANDO_OGGETTO = D.ID_BANDO_OGGETTO             \n"
          + "   AND D.ID_QUADRO_OGGETTO = E.ID_QUADRO_OGGETTO           \n"
          + "   AND E.ID_OGGETTO = G.ID_OGGETTO				            \n"
          + "   AND E.ID_QUADRO = F.ID_QUADRO                           \n"
          + "   AND F.CODICE = :CODICE                                  \n"
          + "   AND A.ID_BANDO = H.ID_BANDO                             \n"
          + "   AND H.FLAG_MASTER = 'N'	                                \n"
          + "   AND A.ID_BANDO = :ID_BANDO                              \n"
          + "ORDER BY C.ORDINE,B.ORDINE									\n";

      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("CODICE", codiceQuadro, Types.VARCHAR);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DecodificaDTO<String>>>()
          {
            @Override
            public List<DecodificaDTO<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<DecodificaDTO<String>> list = new ArrayList<DecodificaDTO<String>>();
              DecodificaDTO<String> item = null;
              while (rs.next())
              {
                item = new DecodificaDTO<String>();
                item.setId(rs.getString("ID"));
                item.setCodice(rs.getString("CODICE"));
                item.setDescrizione(rs.getString("DESCRIZIONE"));
                item.setInfoAggiuntiva(rs.getString("DESCR_OGGETTO"));
                list.add(item);
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("CODICE", codiceQuadro)
          },
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

  public List<GruppoOggettoDTO> getElencoGruppiControlliDisponibili(
	      String flagIstanza, long idBando) throws InternalUnexpectedException
  {
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    String THIS_METHOD = "getElencoGruppiControlliDisponibili";
	    String QUERY = "";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
	    }
	    try
	    {
	      QUERY = " SELECT                                                           \n"
	          + "   DGO.ID_GRUPPO_OGGETTO,                                         \n"
	          + "   DGO.CODICE,                                                    \n"
	          + "   DGO.DESCRIZIONE,                                               \n"
	          + "   DO.ID_OGGETTO,                                                 \n"
	          + "   DO.DESCRIZIONE AS DESCR_OGGETTO                                \n"
	          + " FROM                                                             \n"
	          + "   NEMBO_R_BANDO_OGGETTO BO,                                        \n"
	          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,                               \n"
	          + "   NEMBO_D_GRUPPO_OGGETTO DGO,                                      \n"
	          + "   NEMBO_D_OGGETTO DO                                               \n"
	          + " WHERE                                                            \n"
	          + "   BO.ID_BANDO = :ID_BANDO                                        \n"
	          + "   AND LGO.ID_LEGAME_GRUPPO_OGGETTO = BO.ID_LEGAME_GRUPPO_OGGETTO \n"
	          + "   AND DGO.ID_GRUPPO_OGGETTO = LGO.ID_GRUPPO_OGGETTO              \n"
	          + "   AND DO.ID_OGGETTO = LGO.ID_OGGETTO                             \n"
	          ;
	      if(flagIstanza != null)
	      {
	    	  QUERY = QUERY +
	    			  "   AND DO.FLAG_ISTANZA = :FLAG_ISTANZA                             \n"
	    			  ;
	      }
	      QUERY = QUERY
	          + " ORDER BY DGO.ORDINE,LGO.ORDINE                                   \n";

	      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
	      mapParameterSource.addValue("FLAG_ISTANZA", flagIstanza, Types.VARCHAR);
	      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
	          new ResultSetExtractor<List<GruppoOggettoDTO>>()
	          {
	            @Override
	            public List<GruppoOggettoDTO> extractData(ResultSet rs)
	                throws SQLException, DataAccessException
	            {
	              ArrayList<GruppoOggettoDTO> list = new ArrayList<GruppoOggettoDTO>();
	              GruppoOggettoDTO item = null;
	              long idGruppoLast;
	              long idGruppo = 0;
	              long idOggettoLast;
	              long idOggetto = 0;
	              while (rs.next())
	              {
	                idGruppoLast = rs.getLong("ID_GRUPPO_OGGETTO");
	                idOggettoLast = rs.getLong("ID_OGGETTO");
	                if (idGruppoLast != idGruppo || idOggettoLast != idOggetto)
	                {
	                  idGruppo = idGruppoLast;
	                  idOggetto = idOggettoLast;
	                  item = new GruppoOggettoDTO();
	                  item.setIdGruppoOggetto(idGruppoLast);
	                  item.setIdOggetto(idOggettoLast);
	                  item.setCodGruppo(rs.getString("CODICE"));
	                  item.setDescrGruppo(rs.getString("DESCRIZIONE"));
	                  item.setDescrOggetto(rs.getString("DESCR_OGGETTO"));
	                  list.add(item);
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
	          {
	              new LogParameter("ID_BANDO", idBando),
	              new LogParameter("FLAG_ISTANZA", flagIstanza)
	          },
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
  
  public List<GruppoOggettoDTO> getElencoGruppiControlliDisponibili(
      long idBando) throws InternalUnexpectedException
  {
	  return getElencoGruppiControlliDisponibili(null, idBando);
  }

  public List<ControlloDTO> getElencoControlliDisponibili(long idBando,
      long idGruppoOggetto, long idOggetto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoControlliDisponibili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = "  SELECT                                                                     \n"
          + "   C.ID_OGGETTO,                                                             \n"
          + "   C.CODICE AS COD_OGGETTO,                                                  \n"
          + "   C.DESCRIZIONE AS DESCR_OGGETTO,                                           \n"
          + "   (SELECT COUNT(SC.ID_TIPO_SOLUZIONE)		 					  		      \n"
          + " FROM 																		  \n"
          + "	NEMBO_R_SOLUZIONE_CONTROLLO SC											  \n"
          + " WHERE 																	  \n"
          + "   SC.ID_CONTROLLO = G.ID_CONTROLLO 										  \n"
          + "		) AS NUMERO_SOLUZIONI_ANOMALIE,                                       \n"
          + "   A.ID_BANDO_OGGETTO,                                                       \n"
          + "   A.FLAG_ATTIVO,                                                            \n"
          + "   G.ID_CONTROLLO,                                                           \n"
          + "   G.CODICE,                                                                 \n"
          + "   G.DESCRIZIONE,                                                            \n"
          + "   G.DESCRIZIONE_ESTESA AS DESCR_ESTESA,                                     \n"
          + "   G.DESC_PARAMETRO AS DESCR_PARAMETRO,                                      \n"
          + "   F.FLAG_OBBLIGATORIO,                                                      \n"
          + "   F.GRAVITA,                                                                \n"
          + "   L.CODICE AS COD_QUADRO,                                                   \n"
          + "   L.ID_QUADRO,                                                              \n"
          + "   L.DESCRIZIONE AS DESCR_QUADRO,                                            \n"
          + "   (SELECT H.ID_CONTROLLO                                                    \n"
          + "      FROM NEMBO_R_BANDO_OGGETTO_CONTROLL H,                                  \n"
          + "           NEMBO_R_BANDO_OGGETTO I                                             \n"
          + "      WHERE                                                                  \n"
          + "           I.ID_BANDO_OGGETTO = H.ID_BANDO_OGGETTO                           \n"
          + "           and h.id_controllo = g.id_controllo                               \n"
          + "           AND H.ID_BANDO_OGGETTO =  D.ID_BANDO_OGGETTO                      \n"
          + "           AND I.ID_BANDO = :ID_BANDO ) AS ID_CONTROLLO_INSERITO,            \n"
          + "      (SELECT H.GRAVITA                                                      \n"
          + "      FROM NEMBO_R_BANDO_OGGETTO_CONTROLL H,                                  \n"
          + "           NEMBO_R_BANDO_OGGETTO I                                             \n"
          + "      WHERE                                                                  \n"
          + "           I.ID_BANDO_OGGETTO = H.ID_BANDO_OGGETTO                           \n"
          + "           AND H.ID_CONTROLLO = G.ID_CONTROLLO                               \n"
          + "           AND H.ID_BANDO_OGGETTO =  D.ID_BANDO_OGGETTO                      \n"
          + "           AND I.ID_BANDO = :ID_BANDO ) AS GRAVITA_INSERITA,                 \n"

          + "   (SELECT H.FLAG_GIUSTIFICABILE                                             \n"
          + "      FROM NEMBO_R_BANDO_OGGETTO_CONTROLL H,                                  \n"
          + "           NEMBO_R_BANDO_OGGETTO I                                             \n"
          + "      WHERE                                                                  \n"
          + "           I.ID_BANDO_OGGETTO = H.ID_BANDO_OGGETTO                           \n"
          + "           and h.id_controllo = g.id_controllo                               \n"
          + "           AND H.ID_BANDO_OGGETTO =  D.ID_BANDO_OGGETTO                      \n"
          + "           AND I.ID_BANDO = :ID_BANDO ) AS FLAG_GIUSTIFICABILE_BANDO,     	  \n"
          + "   (SELECT H.FLAG_GIUSTIFICABILE                                             \n"
          + "      FROM NEMBO_R_QUADRO_OGGETTO_CONTROL H,                                 \n"
          + "           NEMBO_R_QUADRO_OGGETTO I                                            \n"
          + "      WHERE                                                                  \n"
          + "           I.ID_QUADRO_OGGETTO = H.ID_QUADRO_OGGETTO                         \n"
          + "           and h.id_controllo = g.id_controllo                               \n"
          + "           AND H.ID_QUADRO_OGGETTO =  D.ID_QUADRO_OGGETTO                    \n"
          + "           AND I.ID_OGGETTO = :ID_OGGETTO ) AS FLAG_GIUSTIFICABILE_QUADRO    \n"

          + "  FROM                                                                       \n"
          + "    NEMBO_R_BANDO_OGGETTO A,                                                   \n"
          + "    NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                                           \n"
          + "    NEMBO_D_OGGETTO C,                                                         \n"
          + "    NEMBO_R_BANDO_OGGETTO_QUADRO D,                                            \n"
          + "    NEMBO_R_QUADRO_OGGETTO E,                                                  \n"
          + "    NEMBO_R_QUADRO_OGGETTO_CONTROL F,                                        \n"
          + "    NEMBO_D_CONTROLLO G,                                                       \n"
          + "    NEMBO_D_QUADRO L                                                           \n"
          + "  WHERE                                                                      \n"
          + "    A.ID_LEGAME_GRUPPO_OGGETTO = B.ID_LEGAME_GRUPPO_OGGETTO                  \n"
          + "    AND B.ID_OGGETTO = C.ID_OGGETTO                                          \n"
          + "    AND D.ID_BANDO_OGGETTO = A.ID_BANDO_OGGETTO                              \n"
          + "    AND E.ID_QUADRO_OGGETTO = D.ID_QUADRO_OGGETTO                            \n"
          + "    AND E.ID_QUADRO_OGGETTO = F.ID_QUADRO_OGGETTO                            \n"
          + "    AND G.ID_CONTROLLO = F.ID_CONTROLLO                                      \n"
          + "    AND L.ID_QUADRO = E.ID_QUADRO                                            \n"
          + "    AND G.DATA_INIZIO <= NVL(A.DATA_FINE,TO_DATE('31/12/9999','DD/MM/YYYY')) \n"
          + "    AND NVL(G.DATA_FINE,TO_DATE('31/12/9999','DD/MM/YYYY')) >= A.DATA_INIZIO \n"
          + "    AND B.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO                             \n"
          + "    AND C.ID_OGGETTO = :ID_OGGETTO                             			  \n"
          + "    AND A.ID_BANDO = :ID_BANDO                                               \n"
          + "  ORDER BY B.ORDINE, E.ORDINE, G.CODICE    	                              \n";

      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      return queryForList(QUERY, mapParameterSource, ControlloDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("idGruppoOggetto", idGruppoOggetto),
              new LogParameter("idOggetto", idOggetto) },
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

  public boolean esisteValoreParametro(long idBandoOggetto, long idControllo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "esisteValoreParametro";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                     \n"
          + "   A.ID_VALORI_PARAMETRI                    \n"
          + " FROM                                       \n"
          + "   NEMBO_T_VALORI_PARAMETRI A                 \n"
          + " WHERE                                      \n"
          + "   A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO   \n"
          + "   AND A.ID_CONTROLLO = :ID_CONTROLLO       \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                String temp = rs.getString("ID_VALORI_PARAMETRI");
                if (temp != null)
                {
                  return true;
                }
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

  public long insertRBandoOggettoControllo(long idBandoOggetto,
      long idControllo, String gravita) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRBandoOggettoControllo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    		\n"
          + " INTO                    		\n"
          + "   NEMBO_R_BANDO_OGGETTO_CONTROLL \n"
          + "   (                     		\n"
          + "     ID_BANDO_OGGETTO,    		\n"
          + "     ID_CONTROLLO,  	    		\n"
          + "     GRAVITA  	    			\n"
          + "   )                     		\n"
          + "   VALUES                		\n"
          + "   (                     		\n"
          + "     :ID_BANDO_OGGETTO,    		\n"
          + "     :ID_CONTROLLO,   			\n"
          + "     :GRAVITA   					\n"
          + "   )            					\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      mapParameterSource.addValue("GRAVITA", gravita.toUpperCase(),
          Types.VARCHAR);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_CONTROLLO", idControllo),
              new LogParameter("GRAVITA", gravita)
          },
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

  public long deleteRBandoOggettoControllo(long idBandoOggetto,
      long idControllo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRBandoOggettoControllo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                    			 \n"
          + "   NEMBO_R_BANDO_OGGETTO_CONTROLL 		 \n"
          + " WHERE    								 \n"
          + "  ID_BANDO_OGGETTO= :ID_BANDO_OGGETTO   \n"
          + "  AND ID_CONTROLLO = :ID_CONTROLLO   	 \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_CONTROLLO", idControllo)
          },
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

  public List<DecodificaDTO<Long>> getParametriControllo(long idBandoOggetto,
      long idControllo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getParametriControllo";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                   \n"
          + "   A.ID_VALORI_PARAMETRI,                 \n"
          + "   A.ID_BANDO_OGGETTO,                    \n"
          + "   A.ID_CONTROLLO,       				   \n"
          + "   A.VALORE       						   \n"
          + " FROM                         			   \n"
          + "   NEMBO_T_VALORI_PARAMETRI A       		   \n"
          + " WHERE                         		   \n"
          + "   A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n"
          + "   AND A.ID_CONTROLLO = :ID_CONTROLLO     \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<DecodificaDTO<Long>>>()
          {
            @Override
            public List<DecodificaDTO<Long>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DecodificaDTO<Long>> elenco = new Vector<DecodificaDTO<Long>>();
              while (rs.next())
              {
                elenco.add(new DecodificaDTO<Long>(
                    rs.getLong("ID_VALORI_PARAMETRI"), rs.getString("VALORE")));
              }
              return elenco;
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

  public long insertValoreParametro(long idBandoOggetto, long idControllo,
      String valoreParametro) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertValoreParametro";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                       	\n"
          + " INTO                       	\n"
          + "   NEMBO_T_VALORI_PARAMETRI    \n"
          + "   (                        	\n"
          + "     ID_VALORI_PARAMETRI,    \n"
          + "     ID_BANDO_OGGETTO,    	\n"
          + "     ID_CONTROLLO,    	   	\n"
          + "     VALORE    	   			\n"
          + "   )                        	\n"
          + "   VALUES                   	\n"
          + "   (                        	\n"
          + "     :ID_VALORI_PARAMETRI,   \n"
          + "     :ID_BANDO_OGGETTO,    	\n"
          + "     :ID_CONTROLLO,    	   	\n"
          + "     :VALORE    	   			\n"
          + "   )                        	\n";
      long idParametro = getNextSequenceValue("SEQ_NEMBO_T_VALORI_PARAMETRI");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_VALORI_PARAMETRI", idParametro,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      mapParameterSource.addValue("VALORE", valoreParametro, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idParametro;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_CONTROLLO", idControllo),
              new LogParameter("VALORE", valoreParametro)
          },
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

  public List<GruppoInfoDTO> getElencoDettagliInfo(long idBando,
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoDettagliInfo";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = "  SELECT                                                                                 \n"
          + "    D.ID_BANDO,                                                                          \n"
          + "    D.ID_BANDO_OGGETTO,                                                                  \n"
          + "    B.ID_GRUPPO_INFO,                                                                    \n"
          + "    A.ID_QUADRO_OGGETTO,                                                                 \n"
          + "    B.DESCRIZIONE AS DESCR_GRUPPO,                                                       \n"
          + "    B.FLAG_INFO_CATALOGO,                                                                \n"
          + "    C.ID_DETTAGLIO_INFO,                                                                 \n"
          + "    C.ID_LEGAME_INFO,                                                                    \n"
          + "    C.DESCRIZIONE as DESCR_DETT,                                                         \n"
          + "    C.FLAG_OBBLIGATORIO,                                                                 \n"
          + "    C.CODICE_INFO,                                                                       \n"
          + "    C.FLAG_GESTIONE_FILE,                                                                \n"
          + "    F.ID_VINCOLO_DICHIARAZIONE,                                                          \n"
          + "    F.TIPO_VINCOLO,                                                                      \n"
          + "    C.EXT_ID_TIPO_DOCUMENTO                                                              \n"
          + "  FROM                                                                                   \n"
          + "    NEMBO_R_BANDO_OGGETTO_QUADRO A ,                                                       \n"
          + "    NEMBO_D_GRUPPO_INFO B,                                                                 \n"
          + "    NEMBO_D_DETTAGLIO_INFO C,                                                              \n"
          + "    NEMBO_R_BANDO_OGGETTO D,                                                               \n"
          + "    NEMBO_D_LEGAME_INFO E,                                                                 \n"
          + "    NEMBO_D_VINCOLO_INFO F                                                                 \n"
          + "  WHERE                                                                                  \n"
          + "    A.ID_BANDO_OGGETTO = B.ID_BANDO_OGGETTO                                              \n"
          + "    AND A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO                                         \n"
          + "    AND A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO        	                                  \n"
          + "    AND A.ID_QUADRO_OGGETTO = B.ID_QUADRO_OGGETTO                                        \n"
          + "    AND B.ID_GRUPPO_INFO = C.ID_GRUPPO_INFO                                              \n"
          + "    AND A.ID_BANDO_OGGETTO = D.ID_BANDO_OGGETTO                                          \n"
          + "    AND C.ID_LEGAME_INFO = E.ID_LEGAME_INFO(+)                                           \n"
          + "    AND E.ID_VINCOLO_DICHIARAZIONE = F.ID_VINCOLO_DICHIARAZIONE(+)                       \n"
          + "    AND A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO                                         \n"
          + "    AND D.ID_BANDO = :ID_BANDO                                                           \n"
          + "  ORDER BY DECODE(B.FLAG_INFO_CATALOGO,'S',1,'N',2,null), B.ORDINE, C.ORDINE, F.ID_VINCOLO_DICHIARAZIONE                                \n";

      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoInfoDTO>>()
          {
            @Override
            public List<GruppoInfoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<GruppoInfoDTO> elenco = new Vector<GruppoInfoDTO>();
              List<DettaglioInfoDTO> dettagli = null;
              GruppoInfoDTO gruppoDTO = null;
              DettaglioInfoDTO dettaglio = null;
              long idGruppoInfoLast = 0;
              long idGruppoInfo = 0;

              while (rs.next())
              {
                idGruppoInfo = rs.getLong("ID_GRUPPO_INFO");

                if (idGruppoInfo != idGruppoInfoLast)
                {
                  idGruppoInfoLast = idGruppoInfo;
                  gruppoDTO = new GruppoInfoDTO();
                  gruppoDTO.setIdBando(rs.getLong("ID_BANDO"));
                  gruppoDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                  gruppoDTO.setIdGruppoInfo(rs.getLong("ID_GRUPPO_INFO"));
                  gruppoDTO.setIdQuadroOggetto(rs.getLong("ID_QUADRO_OGGETTO"));
                  gruppoDTO
                      .setFlagInfoCatalogo(rs.getString("FLAG_INFO_CATALOGO"));
                  gruppoDTO.setDescrizione(rs.getString("DESCR_GRUPPO"));
                  dettagli = new ArrayList<DettaglioInfoDTO>();
                  gruppoDTO.setElencoDettagliInfo(dettagli);
                  elenco.add(gruppoDTO);
                }
                dettaglio = new DettaglioInfoDTO();
                dettaglio.setIdBando(rs.getLong("ID_BANDO"));
                dettaglio.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                dettaglio.setIdGruppoInfo(rs.getLong("ID_GRUPPO_INFO"));
                dettaglio.setIdQuadroOggetto(rs.getLong("ID_QUADRO_OGGETTO"));
                dettaglio.setIdLegameInfo(rs.getLong("ID_LEGAME_INFO"));
                dettaglio.setIdVincoloDichiarazione(
                    rs.getLong("ID_VINCOLO_DICHIARAZIONE"));
                dettaglio.setIdDettaglioInfo(rs.getLong("ID_DETTAGLIO_INFO"));
                dettaglio
                    .setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                dettaglio
                    .setFlagGestioneFile(rs.getString("FLAG_GESTIONE_FILE"));
                dettaglio.setDescrizione(rs.getString("DESCR_DETT"));
                dettaglio
                    .setExtIdTipoDocumento(rs.getLong("EXT_ID_TIPO_DOCUMENTO"));
                dettaglio.setCodiceInfo(rs.getString("CODICE_INFO"));
                dettaglio.setTipoVincolo(rs.getString("TIPO_VINCOLO"));
                dettagli.add(dettaglio);
              }
              return elenco;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto),
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
          },
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

  public List<GruppoInfoDTO> getElencoDettagliInfoCatalogo(long idBando,
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoDettagliInfoCatalogo";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = "  SELECT DISTINCT                                                  \n"
          + "    C.ID_GRUPPO_INFO_CATALOGO,                                     \n"
          + "    C.DESCRIZIONE AS DESCR_GRUPPO,                                 \n"
          + "    C.FLAG_INFO_OBBLIGATORIA,                                      \n"
          + "    D.ID_DETTAGLIO_INFO_CATALOGO,                                  \n"
          + "    D.DESCRIZIONE AS DESCR_DETT,                                   \n"
          + "    D.FLAG_OBBLIGATORIO,                                           \n"
          + "    F.TIPO_VINCOLO, 												\n"
          + "    E.ID_VINCOLO_DICHIARAZIONE,                                    \n"
          + "    G.ID_BANDO_OGGETTO,                                            \n"
          + "    G.ID_QUADRO_OGGETTO,                                           \n"
          + "    D.CODICE_INFO,                                                 \n"
          + "    D.ID_LEGAME_INFO,                                              \n"
          + "    D.EXT_ID_TIPO_DOCUMENTO,                                       \n"
          + "    D.FLAG_GESTIONE_FILE,	                                        \n"
          + "    C.ORDINE,                                                      \n"
          + "    D.ORDINE,                                                      \n"
          + "    F.ID_VINCOLO_DICHIARAZIONE										\n"
          + "  FROM                                                             \n"
          + "    NEMBO_R_INFO_CATALOGO A,                                         \n"
          + "    NEMBO_R_LIVELLO_BANDO B,                                         \n"
          + "    NEMBO_R_BANDO_OGGETTO H,                                         \n"
          + "    NEMBO_R_BANDO_OGGETTO_QUADRO  G,                                 \n"
          + "    NEMBO_D_GRUPPO_INFO_CATALOGO C,                                  \n"
          + "    NEMBO_D_DETTAGLIO_INFO_CATALOG D,                               \n"
          + "    NEMBO_D_LEGAME_INFO E,                                           \n"
          + "    NEMBO_D_VINCOLO_INFO F                                           \n"
          + "  WHERE                                                            \n"
          + "    A.ID_LIVELLO = B.ID_LIVELLO                                    \n"
          + "    AND A.ID_GRUPPO_INFO_CATALOGO = C.ID_GRUPPO_INFO_CATALOGO      \n"
          + "    AND C.ID_GRUPPO_INFO_CATALOGO = D.ID_GRUPPO_INFO_CATALOGO      \n"
          + "    AND A.ID_QUADRO_OGGETTO = G.ID_QUADRO_OGGETTO                  \n"
          + "    AND H.ID_BANDO = B.ID_BANDO                                    \n"
          + "    AND H.ID_BANDO_OGGETTO = G.ID_BANDO_OGGETTO                    \n"
          + "    AND D.ID_LEGAME_INFO = E.ID_LEGAME_INFO(+)                     \n"
          + "    AND E.ID_VINCOLO_DICHIARAZIONE = F.ID_VINCOLO_DICHIARAZIONE(+) \n"
          + "    AND B.ID_BANDO = :ID_BANDO                                     \n"
          + "    AND A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO      				\n"
          + "    AND H.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO      				\n"
          + "   GROUP BY                        								\n"
          + "     C.ID_GRUPPO_INFO_CATALOGO,    								\n"
          + "     C.DESCRIZIONE,                								\n"
          + "     C.FLAG_INFO_OBBLIGATORIA,     								\n"
          + "     D.ID_DETTAGLIO_INFO_CATALOGO, 								\n"
          + "     D.DESCRIZIONE,                								\n"
          + "     D.FLAG_OBBLIGATORIO,          								\n"
          + "     F.TIPO_VINCOLO,               								\n"
          + "     E.ID_VINCOLO_DICHIARAZIONE,                                   \n"
          + "     G.ID_BANDO_OGGETTO,           								\n"
          + "     G.ID_QUADRO_OGGETTO,          								\n"
          + "     D.CODICE_INFO,                								\n"
          + "     D.EXT_ID_TIPO_DOCUMENTO,                                      \n"
          + "     D.FLAG_GESTIONE_FILE,	                                        \n"
          + "     D.ID_LEGAME_INFO,                								\n"
          + "     C.ORDINE,                     								\n"
          + "     D.ORDINE,                      								\n"
          + "     F.ID_VINCOLO_DICHIARAZIONE									\n"
          + "  ORDER BY C.ORDINE, D.ORDINE, F.ID_VINCOLO_DICHIARAZIONE          \n";

      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoInfoDTO>>()
          {
            @Override
            public List<GruppoInfoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<GruppoInfoDTO> elenco = new Vector<GruppoInfoDTO>();
              List<DettaglioInfoDTO> dettagli = null;
              GruppoInfoDTO gruppoDTO = null;
              DettaglioInfoDTO dettaglio = null;
              long idGruppoInfoLast = 0;
              long idGruppoInfo = 0;

              while (rs.next())
              {
                idGruppoInfo = rs.getLong("ID_GRUPPO_INFO_CATALOGO");
                if (idGruppoInfo != idGruppoInfoLast)
                {
                  idGruppoInfoLast = idGruppoInfo;
                  gruppoDTO = new GruppoInfoDTO();
                  gruppoDTO
                      .setIdGruppoInfo(rs.getLong("ID_GRUPPO_INFO_CATALOGO"));
                  gruppoDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                  gruppoDTO.setDescrizione(rs.getString("DESCR_GRUPPO"));
                  gruppoDTO.setFlagInfoObbligatoria(
                      rs.getString("FLAG_INFO_OBBLIGATORIA"));
                  gruppoDTO.setFlagInfoCatalogo("S");
                  dettagli = new ArrayList<DettaglioInfoDTO>();
                  gruppoDTO.setElencoDettagliInfo(dettagli);
                  elenco.add(gruppoDTO);
                }
                dettaglio = new DettaglioInfoDTO();
                dettaglio
                    .setIdGruppoInfo(rs.getLong("ID_GRUPPO_INFO_CATALOGO"));
                dettaglio.setIdDettaglioInfo(
                    rs.getLong("ID_DETTAGLIO_INFO_CATALOGO"));
                dettaglio
                    .setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                dettaglio.setDescrizione(rs.getString("DESCR_DETT"));
                dettaglio.setCodiceInfo(rs.getString("CODICE_INFO"));
                dettaglio.setIdLegameInfo(rs.getLong("ID_LEGAME_INFO"));
                dettaglio
                    .setExtIdTipoDocumento(rs.getLong("EXT_ID_TIPO_DOCUMENTO"));
                dettaglio
                    .setFlagGestioneFile(rs.getString("FLAG_GESTIONE_FILE"));
                dettaglio.setTipoVincolo(rs.getString("TIPO_VINCOLO"));
                dettaglio.setIdVincoloDichiarazione(
                    rs.getLong("ID_VINCOLO_DICHIARAZIONE"));
                dettagli.add(dettaglio);
              }
              return elenco;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto),
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
          },
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

  public long insertDGruppoInfo(long idBandoOggetto, long idQuadroOggetto,
      String descrizione, int ordineGruppo, String flagInfoCatalogo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertDGruppoInfo";
    String INSERT = "";
    long idGruppoInfo = 0;
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    \n"
          + " INTO                    \n"
          + "   NEMBO_D_GRUPPO_INFO     \n"
          + "   (                     \n"
          + "     ID_GRUPPO_INFO,     \n"
          + "     DESCRIZIONE,    	\n"
          + "     ORDINE,    	   		\n"
          + "     ID_QUADRO_OGGETTO,  \n"
          + "     ID_BANDO_OGGETTO,   \n"
          + "     FLAG_INFO_CATALOGO  \n"
          + "   )                     \n"
          + "   VALUES                \n"
          + "   (                     \n"
          + "     :ID_GRUPPO_INFO,    \n"
          + "     :DESCRIZIONE,    	\n"
          + "     :ORDINE,    	   	\n"
          + "     :ID_QUADRO_OGGETTO, \n"
          + "     :ID_BANDO_OGGETTO,  \n"
          + "     :FLAG_INFO_CATALOGO \n"
          + "   )            			\n";

      idGruppoInfo = getNextSequenceValue("SEQ_NEMBO_D_GRUPPO_INFO");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_GRUPPO_INFO", idGruppoInfo,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("ORDINE", ordineGruppo, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_INFO_CATALOGO", flagInfoCatalogo,
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idGruppoInfo;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_GRUPPO_INFO", idGruppoInfo),
              new LogParameter("DESCRIZIONE", descrizione),
              new LogParameter("ORDINE", ordineGruppo),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto),
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("FLAG_INFO_CATALOGO", flagInfoCatalogo),
          },
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

  public void insertDDettaglioInfo(long idGruppoInfo, String descrizione,
      int ordine, String flagObbligatorio, Long idLegameInfo,
      String flagGestioneFile, Long extIdTipoDocumento, String codiceInfo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertDGruppoInfo";
    String INSERT = "";
    long idDettInfo = 0;
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    	\n"
          + " INTO                    	\n"
          + "   NEMBO_D_DETTAGLIO_INFO      \n"
          + "   (                     	\n"
          + "     ID_DETTAGLIO_INFO,      \n"
          + "     ID_GRUPPO_INFO,    		\n"
          + "     DESCRIZIONE,    	   	\n"
          + "     ORDINE,  				\n"
          + "     FLAG_OBBLIGATORIO,    	\n"
          + "     ID_LEGAME_INFO,    		\n"
          + "     FLAG_GESTIONE_FILE,     \n"
          + "     EXT_ID_TIPO_DOCUMENTO,  \n"
          + "     CODICE_INFO    			\n"
          + "   )                     	\n"
          + "   VALUES                	\n"
          + "   (                     	\n"
          + "     :ID_DETTAGLIO_INFO,     \n"
          + "     :ID_GRUPPO_INFO,    	\n"
          + "     :DESCRIZIONE,    	   	\n"
          + "     :ORDINE,  				\n"
          + "     :FLAG_OBBLIGATORIO,    	\n"
          + "     :ID_LEGAME_INFO,    	\n"
          + "     :FLAG_GESTIONE_FILE,    \n"
          + "     :EXT_ID_TIPO_DOCUMENTO,	\n"
          + "     :CODICE_INFO     		\n"
          + "   )            				\n";

      idDettInfo = getNextSequenceValue("SEQ_NEMBO_D_DETTAGLIO_INFO");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DETTAGLIO_INFO", idDettInfo,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_GRUPPO_INFO", idGruppoInfo,
          Types.VARCHAR);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("ORDINE", ordine, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_OBBLIGATORIO", flagObbligatorio,
          Types.VARCHAR);
      mapParameterSource.addValue("ID_LEGAME_INFO", idLegameInfo,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_TIPO_DOCUMENTO", extIdTipoDocumento,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_GESTIONE_FILE", flagGestioneFile,
          Types.VARCHAR);
      mapParameterSource.addValue("CODICE_INFO", codiceInfo, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DETTAGLIO_INFO", idDettInfo),
              new LogParameter("ID_GRUPPO_INFO", idGruppoInfo),
              new LogParameter("DESCRIZIONE", descrizione),
              new LogParameter("ORDINE", ordine),
              new LogParameter("FLAG_OBBLIGATORIO", flagObbligatorio),
              new LogParameter("ID_LEGAME_INFO", idLegameInfo),
              new LogParameter("EXT_ID_TIPO_DOCUMENTO", extIdTipoDocumento),
              new LogParameter("FLAG_GESTIONE_FILE", flagGestioneFile),
              new LogParameter("CODICE_INFO", codiceInfo)
          },
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

  public boolean isDichiarazionePresente(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "isDichiarazionePresente";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                        		     \n"
          + "   COUNT(*) AS COUNT_DICH      			 \n"
          + " FROM                          			 \n"
          + "   NEMBO_D_GRUPPO_INFO           			 \n"
          + " WHERE                         			 \n"
          + "   ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO   \n"
          + "   AND ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                long countDich = rs.getLong("COUNT_DICH");
                if (countDich <= 0)
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

  public boolean isDichObbligatorieInserite(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "isDichObbligatorieInserite";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                      \n"
          + "   A.ID_QUADRO_OGGETTO,                                      \n"
          + "   D.ID_GRUPPO_INFO_CATALOGO,                                \n"
          + "   D.DESCRIZIONE,                                            \n"
          + "   E.ID_GRUPPO_INFO                                          \n"
          + " FROM                                                        \n"
          + "   NEMBO_R_INFO_CATALOGO A,                                    \n"
          + "   NEMBO_R_BANDO_OGGETTO B,                                    \n"
          + "   NEMBO_R_LIVELLO_BANDO C,                                    \n"
          + "   NEMBO_D_GRUPPO_INFO_CATALOGO D,                             \n"
          + "   NEMBO_D_GRUPPO_INFO E                                       \n"
          + " WHERE                                                       \n"
          + "   A.ID_LIVELLO = C.ID_LIVELLO                               \n"
          + "   AND C.ID_BANDO = B.ID_BANDO                               \n"
          + "   AND D.ID_GRUPPO_INFO_CATALOGO = A.ID_GRUPPO_INFO_CATALOGO \n"
          + "   AND E.ID_QUADRO_OGGETTO(+) = A.ID_QUADRO_OGGETTO          \n"
          + "   AND D.FLAG_INFO_OBBLIGATORIA = 'S'                        \n"
          + "   AND E.FLAG_INFO_CATALOGO = 'S'                        	  \n"
          + "   AND E.ID_BANDO_OGGETTO = B.ID_BANDO_OGGETTO               \n"
          + "   AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                \n"
          + "   AND A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO              \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              long idGruppoInserito;
              while (rs.next())
              {
                idGruppoInserito = rs.getLong("ID_GRUPPO_INFO");
                if (idGruppoInserito > 0)
                {
                  return true;
                }
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

  public DecodificaDTO<Long> getIdBandoOggetto(long idBando,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getIdBandoOggetto";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                    		\n"
          + "   B.ID_BANDO_OGGETTO,                      		\n"
          + "   B.FLAG_ATTIVO                      				\n"
          + " FROM                                      		\n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO A,           		\n"
          + "   NEMBO_R_BANDO_OGGETTO B                   		\n"
          + " WHERE                                     		\n"
          + "   A.ID_BANDO_OGGETTO = B.ID_BANDO_OGGETTO 		\n"
          + "   AND A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO   	\n"
          + "   AND B.ID_BANDO = :ID_BANDO                     	\n";

      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DecodificaDTO<Long>>()
          {
            @Override
            public DecodificaDTO<Long> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              DecodificaDTO<Long> decod = new DecodificaDTO<Long>();
              while (rs.next())
              {
                decod.setId(rs.getLong("ID_BANDO_OGGETTO"));
                decod.setDescrizione(rs.getString("FLAG_ATTIVO"));
              }

              return decod;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public void attivaBandoOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "attivaBandoOggetto";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                 \n"
          + "   NEMBO_R_BANDO_OGGETTO                  \n"
          + " SET                                    \n"
          + "   FLAG_ATTIVO = 'S' 					 \n"
          + " WHERE                                  \n"
          + "   ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
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

  public long updateGravitaControllo(long idBandoOggetto, long idControllo,
      String gravita) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateGravitaControllo";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      UPDATE = " UPDATE                    		         \n"
          + "   NEMBO_R_BANDO_OGGETTO_CONTROLL          \n"
          + " SET  									 \n"
          + "    GRAVITA = :GRAVITA  	    			 \n"
          + " WHERE               					 \n"
          + "   ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO   \n"
          + "   AND ID_CONTROLLO = :ID_CONTROLLO   	 \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      mapParameterSource.addValue("GRAVITA", gravita.toUpperCase(),
          Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_CONTROLLO", idControllo),
              new LogParameter("GRAVITA", gravita)
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

  public long deleteRBandoOggettoControlloByIdQuadro(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRBandoOggettoControlloByIdQuadro";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                    			 		 \n"
          + "   NEMBO_R_BANDO_OGGETTO_CONTROLL 		 		 \n"
          + " WHERE    								 		 \n"
          + "  ID_BANDO_OGGETTO= :ID_BANDO_OGGETTO   		 \n"
          + "  AND ID_CONTROLLO IN (                    	 \n"
          + "     SELECT                                     \n"
          + "      A.ID_CONTROLLO                         	 \n"
          + "     FROM                                       \n"
          + "       NEMBO_R_QUADRO_OGGETTO_CONTROL A         \n"
          + "     WHERE                                      \n"
          + "       A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO \n"
          + "   )                                            \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public List<DecodificaDTO<String>> getElencoTipiFileDisponibili()
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoTipiFileDisponibili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                         \n"
          + "   A.ID_TIPO_DOCUMENTO,         \n"
          + "   A.DESC_TIPO_DOCUMENTO        \n"
          + " FROM                           \n"
          + "   DOQUIAGRI_V_TIPO_DOC A       \n"
          + " WHERE                          \n"
          + "   A.ID_CATEGORIA_DOC = 2003    \n"
          + " ORDER BY A.DESC_TIPO_DOCUMENTO \n";

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<DecodificaDTO<String>>>()
          {
            @Override
            public List<DecodificaDTO<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<DecodificaDTO<String>> list = new ArrayList<DecodificaDTO<String>>();
              DecodificaDTO<String> item = null;
              while (rs.next())
              {
                item = new DecodificaDTO<String>();
                item.setId(rs.getString("ID_TIPO_DOCUMENTO"));
                item.setDescrizione(rs.getString("DESC_TIPO_DOCUMENTO"));
                list.add(item);
              }
              return list.isEmpty() ? null : list;
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

  public long insertDLegameInfoTemp(long idVincoloDichiarazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertDLegameInfoTemp";
    String INSERT = "";
    long idLegameInfoNew = 0;
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    					\n"
          + " INTO                    					\n"
          + "   NEMBO_D_LEGAME_INFO     					\n"
          + "   (                     					\n"
          + "     ID_LEGAME_INFO,     					\n"
          + "     ID_VINCOLO_DICHIARAZIONE  				\n"
          + "   )                     					\n"
          + "   VALUES                					\n"
          + "   (                     					\n"
          + "     :ID_LEGAME_INFO,  						\n"
          + "     :ID_VINCOLO_DICHIARAZIONE   			\n"
          + "   )            								\n";

      idLegameInfoNew = getNextSequenceValue("SEQ_NEMBO_D_LEGAME_INFO");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_VINCOLO_DICHIARAZIONE",
          idVincoloDichiarazione, Types.NUMERIC);
      mapParameterSource.addValue("ID_LEGAME_INFO", idLegameInfoNew,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idLegameInfoNew;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_VINCOLO_DICHIARAZIONE",
                  idVincoloDichiarazione),
              new LogParameter("ID_LEGAME_INFO", idLegameInfoNew)
          },
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

  public ReportVO getReportBando(ParametriQueryReportVO parametri)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getReportBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = parametri.getIstruzioneSQL().toUpperCase();

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      if (SELECT.contains(":ID_BANDO"))
        mapParameterSource.addValue("ID_BANDO", parametri.getIdBando(),
            Types.NUMERIC);
      if (SELECT.contains(":COD_ENTE_CAA"))
        mapParameterSource.addValue("COD_ENTE_CAA", parametri.getCodEnteCaa(),
            Types.VARCHAR);

      final long idTipoVisualizzazione = parametri.getIdTipoVisualizzazione();

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<ReportVO>()
          {
            @Override
            public ReportVO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              if (idTipoVisualizzazione == 4)
              {
                return elabReportProiezione(rs);
              }
              else
                if (idTipoVisualizzazione == 6)
                {
                  return elabReportIstogrammaStacked(rs);
                }
                else
                {
                  return elabReport(rs);
                }
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

  private ReportVO elabReportProiezione(ResultSet rs) throws SQLException
  {
    ReportVO reportVO = new ReportVO();
    ColReportVO colReportVO = null;
    CellReportVO cellReportVO = null;
    List<CellReportVO> listRow = null;
    RowsReportVO rowsReportVO = null;
    String valRiga;
    String valColonna;
    List<String> asseX = new ArrayList<String>();
    List<String> asseY = new ArrayList<String>();

    Vector<ColReportVO> colsDefinitions = new Vector<ColReportVO>();
    Vector<RowsReportVO> rowValues = new Vector<RowsReportVO>();

    // Creo la lista delle colonne ed elaboro le righe
    ResultSetMetaData rsmd = rs.getMetaData();
    // la prima colonna la leggo normalmente
    colReportVO = new ColReportVO();
    colReportVO.setId(rsmd.getColumnName(1));
    colReportVO.setLabel(rsmd.getColumnName(1));
    colReportVO.setType(getColumnTypeStr(rsmd.getColumnType(1)));
    colsDefinitions.add(colReportVO);

    while (rs.next())
    {

      // Gestisco righe
      valRiga = rs.getString(1);
      if (!asseX.contains(valRiga))
      {
        // In questo caso creo la riga
        asseX.add(valRiga);
        rowsReportVO = new RowsReportVO();
        listRow = new Vector<CellReportVO>();
        cellReportVO = new CellReportVO();
        cellReportVO.setV(rs.getObject(1));
        listRow.add(cellReportVO);
        rowsReportVO.addRowReport(listRow);
        rowValues.add(rowsReportVO);
      }
      cellReportVO = new CellReportVO();
      cellReportVO.setV(rs.getObject(3));
      listRow.add(cellReportVO);

      // Gestisco colonne
      valColonna = rs.getString(2);
      if (asseY.contains(valColonna))
      {
        // non devo fare nulla
      }
      else
      {
        asseY.add(valColonna);
        colReportVO = new ColReportVO();
        colReportVO.setId(rs.getString(2));
        colReportVO.setLabel(rs.getString(2));
        colReportVO.setType(ColReportVO.TYPE_NUMBER);
        colsDefinitions.add(colReportVO);
      }
    }

    reportVO.setColsDefinitions(colsDefinitions);
    reportVO.setRowValues(rowValues);
    return reportVO;
  }

  private ReportVO elabReportIstogrammaStacked(ResultSet rs) throws SQLException
  {
    ReportVO reportVO = new ReportVO();
    ColReportVO colReportVO = null;
    ColReportVO colReportVORef = null;
    CellReportVO cellReportVO = null;
    List<CellReportVO> listRow = null;
    RowsReportVO rowsReportVO = null;
    String valRiga;
    String valColonna;

    TreeMap<String, Long> mapColsIndex = new TreeMap<String, Long>();
    TreeMap<Long, CellReportVO> mapRowIndex = new TreeMap<Long, CellReportVO>();

    List<String> asseX = new ArrayList<String>();
    List<String> asseY = new ArrayList<String>();

    List<ColReportVO> colsDefinitions = new ArrayList<ColReportVO>();
    Vector<RowsReportVO> rowValues = new Vector<RowsReportVO>();

    // Creo la lista delle colonne ed elaboro le righe
    ResultSetMetaData rsmd = rs.getMetaData();
    // la prima colonna la leggo normalmente
    colReportVORef = new ColReportVO();
    colReportVORef.setId(rsmd.getColumnName(1));
    colReportVORef.setLabel(rsmd.getColumnName(1));
    colReportVORef.setType(getColumnTypeStr(rsmd.getColumnType(1)));

    long count = 1;

    while (rs.next())
    {
      // Gestisco colonne
      valColonna = rs.getString(2);
      if (asseY.contains(valColonna))
      {
        // non devo fare nulla
      }
      else
      {
        asseY.add(valColonna);
        mapColsIndex.put(rs.getString(2), count);
        count++;
        colReportVO = new ColReportVO();
        colReportVO.setId(rs.getString(2));
        colReportVO.setLabel(rs.getString(2));
        colReportVO.setType(ColReportVO.TYPE_NUMBER);
        colsDefinitions.add(colReportVO);
        Collections.sort(colsDefinitions, new Comparator<ColReportVO>()
        {
          @Override
          public int compare(ColReportVO o1, ColReportVO o2)
          {
            return o1.getLabel().compareTo(o2.getLabel());
          }
        });
      }

      // Gestisco righe
      valRiga = rs.getString(1);
      if (!asseX.contains(valRiga))
      {
        // Salvo i valori precedenti
        if (listRow != null)
        {
          Set<Entry<String, Long>> colSet = mapColsIndex.entrySet();
          Iterator<Entry<String, Long>> colI = colSet.iterator();
          while (colI.hasNext())
          {
            Entry<String, Long> me = (Entry<String, Long>) colI.next();
            if (mapRowIndex.containsKey(me.getValue()))
            {
              listRow.add(mapRowIndex.get(me.getValue()));
            }
          }
        }

        // In questo caso creo la riga
        asseX.add(valRiga);
        rowsReportVO = new RowsReportVO();
        listRow = new Vector<CellReportVO>();
        cellReportVO = new CellReportVO();
        cellReportVO.setV(rs.getObject(1));
        listRow.add(cellReportVO);
        rowsReportVO.addRowReport(listRow);
        rowValues.add(rowsReportVO);
      }

      cellReportVO = new CellReportVO();
      cellReportVO.setV(rs.getObject(3));
      mapRowIndex.put(mapColsIndex.get(rs.getString(2)), cellReportVO);
    }

    colsDefinitions.add(0, colReportVORef);
    reportVO.setColsDefinitions(colsDefinitions);
    reportVO.setRowValues(rowValues);
    return reportVO;
  }

  private String getColumnTypeStr(int type)
  {
    switch (type)
    {
      case Types.VARCHAR:
        return ColReportVO.TYPE_STRING;
      case Types.BOOLEAN:
        return ColReportVO.TYPE_BOOLEAN;
      case Types.DATE:
        return ColReportVO.TYPE_DATE;
      case Types.TIMESTAMP:
        return ColReportVO.TYPE_DATETIME;
      case Types.NUMERIC:
        return ColReportVO.TYPE_NUMBER;
      case Types.BLOB:
        return ColReportVO.TYPE_BLOB;
      default:
        return ColReportVO.TYPE_STRING;
    }
  }

  private ReportVO elabReport(ResultSet rs) throws SQLException
  {
    ReportVO reportVO = new ReportVO();
    ColReportVO colReportVO = null;
    CellReportVO cellReportVO = null;
    List<CellReportVO> listRow = null;
    RowsReportVO rowsReportVO = null;

    Vector<ColReportVO> colsDefinitions = new Vector<ColReportVO>();
    Vector<RowsReportVO> rowValues = new Vector<RowsReportVO>();

    // Creo la lista delle colonne
    ResultSetMetaData rsmd = rs.getMetaData();

    for (int i = 1; i <= rsmd.getColumnCount(); i++)
    {
      colReportVO = new ColReportVO();
      colReportVO.setId(rsmd.getColumnName(i));
      colReportVO.setLabel(rsmd.getColumnName(i));
      colReportVO
          .setNullable(rsmd.isNullable(i) == ResultSetMetaData.columnNullable);
      colReportVO.setMaxSize(rsmd.getColumnDisplaySize(i));
      colReportVO.setType(getColumnTypeStr(rsmd.getColumnType(i)));
      colsDefinitions.add(colReportVO);
    }

    while (rs.next())
    {
      rowsReportVO = new RowsReportVO();
      listRow = new Vector<CellReportVO>();

      for (int i = 1; i <= colsDefinitions.size(); i++)
      {
        cellReportVO = new CellReportVO();
        cellReportVO.setV(rs.getObject(i));
        listRow.add(cellReportVO);
      }

      rowsReportVO.addRowReport(listRow);
      rowValues.add(rowsReportVO);
    }

    reportVO.setColsDefinitions(colsDefinitions);
    reportVO.setRowValues(rowValues);
    return reportVO;
  }

  public List<DecodificaDTO<String>> elencoQueryBando(long idBando,
      boolean flagElenco, String attore) throws InternalUnexpectedException
  {
    String THIS_METHOD = "elencoQueryBando";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT.append(
          " SELECT                                                     \n"
              + "  A.ID_ELENCO_QUERY AS ID,                                  \n"
              + "  B.DESCRIZIONE_BREVE AS CODICE,                            \n"
              + "  B.DESCRIZIONE_ESTESA AS DESCRIZIONE                       \n"
              + " FROM                                                       \n"
              + "  NEMBO_R_ELENCO_QUERY_BANDO A,                               \n"
              + "  NEMBO_D_ELENCO_QUERY B,                                     \n"
              + "  NEMBO_D_TIPO_VISUALIZZAZIONE C                              \n"
              + " WHERE                                                      \n"
              + "  A.ID_BANDO = :ID_BANDO                                    \n"
              + "  AND A.FLAG_VISIBILE = 'S'				                 \n"
              + "  AND A.EXT_COD_ATTORE = :ATTORE                            \n"
              + "  AND A.ID_ELENCO_QUERY = B.ID_ELENCO_QUERY                 \n"
              + "  AND B.ID_TIPO_VISUALIZZAZIONE = C.ID_TIPO_VISUALIZZAZIONE \n");
      if (flagElenco)
      {
        SELECT.append("  AND C.FLAG_ELENCO = 'S'                   \n");
      }
      else
      {
        SELECT.append("  AND C.FLAG_ELENCO = 'N'                   \n");
      }
      SELECT.append(" ORDER BY A.ORDINAMENTO                         \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ATTORE", attore, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<DecodificaDTO<String>>>()
          {
            @Override
            public List<DecodificaDTO<String>> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<DecodificaDTO<String>> res = null;
              while (rs.next())
              {
                if (res == null)
                {
                  res = new Vector<DecodificaDTO<String>>();
                }
                res.add(new DecodificaDTO<String>(rs.getString("ID"),
                    rs.getString("CODICE"), rs.getString("DESCRIZIONE")));
              }
              return res;
            }
          });
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

  public GraficoVO getDatiGrafico(long idElencoQuery)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getDatiGrafico";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                  \n"
          + "   A.DESCRIZIONE_BREVE,                                  \n"
          + "   A.DESCRIZIONE_ESTESA,                                 \n"
          + "   A.ISTRUZIONE_SQL,                                     \n"
          + "   A.ID_TIPO_VISUALIZZAZIONE,                            \n"
          + "   LENGTH(A.EXCEL_TEMPLATE) AS LENGTH_EXCEL_TEMPLATE,    \n"
          + "   B.DESCRIZIONE                                         \n"
          + " FROM                                                    \n"
          + "   NEMBO_D_ELENCO_QUERY A,                                 \n"
          + "   NEMBO_D_TIPO_VISUALIZZAZIONE B                          \n"
          + " WHERE                                                   \n"
          + "   A.ID_TIPO_VISUALIZZAZIONE = B.ID_TIPO_VISUALIZZAZIONE \n"
          + "   AND A.ID_ELENCO_QUERY = :ID_ELENCO_QUERY              \n";

      mapParameterSource.addValue("ID_ELENCO_QUERY", idElencoQuery,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<GraficoVO>()
          {
            @Override
            public GraficoVO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              GraficoVO graficoVO = new GraficoVO();
              graficoVO.setIdElencoQuery(idElencoQuery);
              while (rs.next())
              {
                graficoVO.setDescrBreve(rs.getString("DESCRIZIONE_BREVE"));
                graficoVO.setDescrEstesa(rs.getString("DESCRIZIONE_ESTESA"));
                graficoVO.setIstruzioneSQL(rs.getString("ISTRUZIONE_SQL"));
                graficoVO.setIdTipoVisualizzazione(
                    rs.getLong("ID_TIPO_VISUALIZZAZIONE"));
                graficoVO
                    .setDescrTipoVisualizzazione(rs.getString("DESCRIZIONE"));
                graficoVO
                    .setExcelTemplate(rs.getLong("LENGTH_EXCEL_TEMPLATE") > 0);
              }
              return graficoVO;
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

  public void updateAllegato(long idAllegatiBando,
      String descrizione) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateAllegato";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                   \n"
          + "   NEMBO_D_ALLEGATI_BANDO                   \n"
          + " SET                                      \n"
          + "   DESCRIZIONE = :DESCRIZIONE             \n"
          + " WHERE                                    \n"
          + "   ID_ALLEGATI_BANDO = :ID_ALLEGATI_BANDO \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_ALLEGATI_BANDO", idAllegatiBando,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ALLEGATI_BANDO", idAllegatiBando),
              new LogParameter("DESCRIZIONE", descrizione)
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

  public void updateOrdineAllegato(long idAllegatiBando, String ordine)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateOrdineAllegato";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                   	\n"
          + "   NEMBO_D_ALLEGATI_BANDO                   	\n"
          + " SET                                      	\n"
          + "   ORDINE = :ORDINE                		\n"
          + " WHERE                                    	\n"
          + "   ID_ALLEGATI_BANDO = :ID_ALLEGATI_BANDO 	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_ALLEGATI_BANDO", idAllegatiBando,
          Types.NUMERIC);
      mapParameterSource.addValue("ORDINE", ordine, Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_ALLEGATI_BANDO", idAllegatiBando),
              new LogParameter("ORDINE", ordine)
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

  public FileAllegatoDTO getAllegato(long idAllegatoBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAllegato";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 										\n"
          + " 	*  											\n"
          + " FROM 											\n"
          + "	NEMBO_D_ALLEGATI_BANDO		    			\n"
          + " WHERE 										\n"
          + "   ID_ALLEGATI_BANDO = :ID_ALLEGATI_BANDO  	\n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ALLEGATI_BANDO", idAllegatoBando,
          Types.NUMERIC);

      return queryForObject(SELECT, parameterSource, FileAllegatoDTO.class);
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

  public List<DecodificaDTO<String>> getDLegameInfo(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getDLegameInfo";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT   DISTINCT                    		   \n"
          + "  D.ID_LEGAME_INFO AS ID,                     \n"
          + "  D.ID_VINCOLO_DICHIARAZIONE AS CODICE,       \n"
          + "  E.TIPO_VINCOLO AS DESCRIZIONE   			   \n"
          + " FROM                                         \n"
          + "   NEMBO_D_DETTAGLIO_INFO A,                    \n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO B,              \n"
          + "   NEMBO_D_GRUPPO_INFO C,                       \n"
          + "   NEMBO_D_LEGAME_INFO D,                       \n"
          + "   NEMBO_D_VINCOLO_INFO E                       \n"
          + " WHERE                                        \n"
          + "   B.ID_QUADRO_OGGETTO = C.ID_QUADRO_OGGETTO  \n"
          + "   AND B.ID_BANDO_OGGETTO = C.ID_BANDO_OGGETTO  \n"
          + "   AND C.ID_GRUPPO_INFO = A.ID_GRUPPO_INFO    \n"
          + "   AND A.ID_LEGAME_INFO IS NOT NULL           \n"
          + "   AND A.ID_LEGAME_INFO = D.ID_LEGAME_INFO    \n"
          + "   AND D.ID_VINCOLO_DICHIARAZIONE = E.ID_VINCOLO_DICHIARAZIONE    \n"
          + "   AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n"
          + "   AND B.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO \n"
          + "     ORDER BY D.ID_VINCOLO_DICHIARAZIONE\n";

      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto);
      return queryForDecodificaString(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public boolean graficiTabellariPresenti(long idBando, String attore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "graficiTabellariPresenti";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                      \n"
          + "   A.ID_ELENCO_QUERY                                         \n"
          + " FROM                                                        \n"
          + "   NEMBO_R_ELENCO_QUERY_BANDO A,                               \n"
          + "   NEMBO_D_ELENCO_QUERY B,                                     \n"
          + "   NEMBO_D_TIPO_VISUALIZZAZIONE C                              \n"
          + " WHERE                                                       \n"
          + "   A.ID_BANDO = :ID_BANDO 									  \n"
          + "   AND A.FLAG_VISIBILE = 'S'				                  \n"
          + "   AND A.EXT_COD_ATTORE = :ATTORE                            \n"
          + "   AND A.ID_ELENCO_QUERY = B.ID_ELENCO_QUERY                 \n"
          + "   AND B.ID_TIPO_VISUALIZZAZIONE = C.ID_TIPO_VISUALIZZAZIONE \n"
          + "   AND C.FLAG_ELENCO = 'S'                                   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ATTORE", attore, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
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

  public boolean isProcedimentoOggettoQuadroInserito(long idBando,
      long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isProcedimentoOggettoQuadroInserito";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                          \n"
          + "   COUNT(*) AS COUNT_DICH                                        \n"
          + " FROM                                                            \n"
          + "   NEMBO_T_PROCEDIMENTO P,                                         \n"
          + "   NEMBO_T_PROCEDIMENTO_OGGETTO PO,                                \n"
          + "   NEMBO_R_BANDO_OGGETTO BO,                                       \n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO BOQ,                               \n"
          + "   NEMBO_D_GRUPPO_INFO GI                                          \n"
          + " WHERE                                                           \n"
          + "   P.ID_PROCEDIMENTO = PO.ID_PROCEDIMENTO                    	  \n"
          + "   AND BO.ID_BANDO = P.ID_BANDO                                  \n"
          + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND BOQ.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO                \n"
          + "   AND GI.ID_QUADRO_OGGETTO = BOQ.ID_QUADRO_OGGETTO              \n"
          + "   AND GI.ID_BANDO_OGGETTO = BOQ.ID_BANDO_OGGETTO                \n"
          + "   AND P.ID_BANDO = :ID_BANDO                                    \n"
          + "   AND GI.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO                 \n"
          + "   AND GI.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                long countDich = rs.getLong("COUNT_DICH");
                if (countDich <= 0)
                {
                  return false;
                }
                else
                {
                  return true;
                }
              }
              else
              {
                return false;
              }
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

  public void updateDescrGruppoInfo(long idGruppoInfo, String descrizione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateDescrGruppoInfo";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                \n"
          + "   NEMBO_D_GRUPPO_INFO                   \n"
          + " SET                                   \n"
          + "   DESCRIZIONE = :DESCRIZIONE          \n"
          + " WHERE                                 \n"
          + "   ID_GRUPPO_INFO = :ID_GRUPPO_INFO	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_GRUPPO_INFO", idGruppoInfo,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_GRUPPO_INFO", idGruppoInfo),
              new LogParameter("DESCRIZIONE", descrizione)
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

  public void updateDDettaglioInfo(long idDettaglioInfo, String descrizione,
      int ordine, String flagObbligatorio, Long idLegameInfo,
      String flagGestioneFile, Long extIdTipoDocumento, String codiceInfo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateDDettaglioInfo";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      UPDATE = " UPDATE                                            \n"
          + "   NEMBO_D_DETTAGLIO_INFO                            \n"
          + " SET                                               \n"
          + "   DESCRIZIONE = :DESCRIZIONE,                     \n"
          + "   ORDINE = :ORDINE,                               \n"
          + "   FLAG_OBBLIGATORIO = :FLAG_OBBLIGATORIO,         \n"
          + "   ID_LEGAME_INFO = :ID_LEGAME_INFO,               \n"
          + "   FLAG_GESTIONE_FILE = :FLAG_GESTIONE_FILE,       \n"
          + "   EXT_ID_TIPO_DOCUMENTO = :EXT_ID_TIPO_DOCUMENTO, \n"
          + "   CODICE_INFO = :CODICE_INFO                      \n"
          + " WHERE                                             \n"
          + "   ID_DETTAGLIO_INFO= :ID_DETTAGLIO_INFO           \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_DETTAGLIO_INFO", idDettaglioInfo,
          Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("ORDINE", ordine, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_OBBLIGATORIO", flagObbligatorio,
          Types.VARCHAR);
      mapParameterSource.addValue("ID_LEGAME_INFO", idLegameInfo,
          Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_TIPO_DOCUMENTO", extIdTipoDocumento,
          Types.NUMERIC);
      mapParameterSource.addValue("FLAG_GESTIONE_FILE", flagGestioneFile,
          Types.VARCHAR);
      mapParameterSource.addValue("CODICE_INFO", codiceInfo, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_DETTAGLIO_INFO", idDettaglioInfo),
              new LogParameter("DESCRIZIONE", descrizione),
              new LogParameter("ORDINE", ordine),
              new LogParameter("FLAG_OBBLIGATORIO", flagObbligatorio),
              new LogParameter("ID_LEGAME_INFO", idLegameInfo),
              new LogParameter("EXT_ID_TIPO_DOCUMENTO", extIdTipoDocumento),
              new LogParameter("FLAG_GESTIONE_FILE", flagGestioneFile),
              new LogParameter("CODICE_INFO", codiceInfo)
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

  public byte[] getImmagineByIdQuadro(long idQuadro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getImmagineByIdQuadro";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                            \n"
          + "   IMMAGINE                                 \n"
          + " FROM                                             \n"
          + "   NEMBO_D_QUADRO Q                                 \n"
          + " WHERE                                            \n"
          + "  Q.ID_QUADRO = :ID_QUADRO					  	 \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<byte[]>()
          {
            @Override
            public byte[] extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              rs.next();
              byte[] img = rs.getBytes("IMMAGINE");
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

  public List<AmmCompetenzaDTO> getAmmCompetenzaRisorsa(
      long idRisorseLivelloBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAmmCompetenzaRisorsa";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   B.ID_AMM_COMPETENZA,                               \n"
          + "   B.DESC_BREVE_TIPO_AMMINISTRAZ,                     \n"
          + "   B.DESCRIZIONE                                      \n"
          + " FROM                                                 \n"
          + "    NEMBO_R_RISOR_LIV_BANDO_AMM_CO A,                      \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA B                       \n"
          + " WHERE                                                \n"
          + "   A.EXT_ID_AMM_COMPETENZA = B.ID_AMM_COMPETENZA      \n"
          + "   AND A.ID_RISORSE_LIVELLO_BANDO = :ID_RISORSE_LIVELLO_BANDO      \n"
          + " ORDER BY B.DESC_BREVE_TIPO_AMMINISTRAZ,B.DESCRIZIONE \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_RISORSE_LIVELLO_BANDO",
          idRisorseLivelloBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<AmmCompetenzaDTO>>()
          {
            @Override
            public List<AmmCompetenzaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AmmCompetenzaDTO> list = new ArrayList<AmmCompetenzaDTO>();
              AmmCompetenzaDTO ammDTO = null;
              while (rs.next())
              {
                ammDTO = new AmmCompetenzaDTO();
                ammDTO.setIdAmmCompetenza(rs.getLong("ID_AMM_COMPETENZA"));
                ammDTO.setDescBreveTipoAmministraz(
                    rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                ammDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(ammDTO);
              }
              return list.isEmpty() ? null : list;
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

  public List<GraduatoriaDTO> getGraduatorieBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getGraduatorieBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   G.ID_GRADUATORIA,                                  \n"
          + "   G.DATA_APPROVAZIONE,                    		   \n"
          + "   G.DESCRIZIONE                                      \n"
          + " FROM                                                 \n"
          + "   NEMBO_t_graduatoria G,                   			   \n"
          + "   NEMBO_r_liv_bando_graduatoria BG                     \n"
          + " WHERE                                                \n"
          + "   bg.id_bando = :ID_BANDO							   \n"
          + "   AND bg.id_graduatoria = g.id_graduatoria     	   \n"
          + "	AND g.flag_approvata= 'S'							\n"
          + " ORDER BY G.DESCRIZIONE \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<GraduatoriaDTO>>()
          {
            @Override
            public List<GraduatoriaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GraduatoriaDTO> list = new ArrayList<GraduatoriaDTO>();
              GraduatoriaDTO grad = null;
              while (rs.next())
              {
                grad = new GraduatoriaDTO();
                grad.setIdGraduatoria(rs.getLong("ID_GRADUATORIA"));
                grad.setDescrizione(rs.getString("DESCRIZIONE"));
                grad.setDataApprovazione(rs.getTimestamp("DATA_APPROVAZIONE"));
                list.add(grad);
              }
              return list.isEmpty() ? null : list;
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

  public List<FileAllegatoDTO> getAllegatiGraduatoria(long idGraduatoria)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAllegatiGraduatoria";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   G.ID_ALLEGATI_GRADUATORIA,                         \n"
          + "   G.NOME_FILE,         			           		   \n"
          + "   G.DESCRIZIONE                                      \n"
          + " FROM                                                 \n"
          + "   NEMBO_t_allegati_graduatoria G                   	   \n"
          + " WHERE                                                \n"
          + "   G.id_graduatoria = :ID_GRADUATORIA				   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_GRADUATORIA", idGraduatoria, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<FileAllegatoDTO>>()
          {
            @Override
            public List<FileAllegatoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<FileAllegatoDTO> list = new ArrayList<FileAllegatoDTO>();
              FileAllegatoDTO all = null;
              while (rs.next())
              {
                all = new FileAllegatoDTO();
                all.setIdAllegatiBando(rs.getLong("ID_ALLEGATI_GRADUATORIA"));
                all.setDescrizione(rs.getString("DESCRIZIONE"));
                all.setNomeFile(rs.getString("NOME_FILE"));
                list.add(all);
              }
              return list.isEmpty() ? null : list;
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

  public FileAllegatoDTO getFileAllegatoGraduatoria(long idAllegatiGraduatoria)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getFileAllegatoGraduatoria";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   FILE_ALLEGATO, NOME_FILE                         \n"
          + " FROM                                                 \n"
          + "   NEMBO_T_ALLEGATI_GRADUATORIA G                   	   \n"
          + " WHERE                                                \n"
          + "   G.ID_ALLEGATI_GRADUATORIA = :ID_ALLEGATI_GRADUATORIA				   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ALLEGATI_GRADUATORIA", idAllegatiGraduatoria,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<FileAllegatoDTO>()
          {
            @Override
            public FileAllegatoDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              FileAllegatoDTO f = new FileAllegatoDTO();
              while (rs.next())
              {
                f.setFileAllegato(rs.getBytes("FILE_ALLEGATO"));
                f.setNomeFile(rs.getString("NOME_FILE"));

              }
              return f;
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

  public List<LivelloDTO> getLivelliBando(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getLivelliBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                                  \n"
          + "   L.ID_LIVELLO ID_LIVELLO,"
          + "   L.DESCRIZIONE  DESCRIZIONE,"
          + "   L.CODICE CODICE              \n"
          + " FROM                                      \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,					\n"
          + "	NEMBO_D_LIVELLO L                  	    \n"
          + " WHERE                                     \n"
          + "   LB.ID_BANDO = :ID_BANDO					\n"
          + " AND 										\n"
          + "	L.ID_LIVELLO  = LB.ID_LIVELLO			\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              while (rs.next())
              {
                l = new LivelloDTO();

                l.setIdLivello(rs.getLong("ID_LIVELLO"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
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

  public List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoLivelli";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                          		    	\n"
          + " L.DESCRIZIONE AS  DESCRIZIONE,				\n"
          + " L.ID_LIVELLO AS ID,							\n"
          + " L.CODICE AS CODICE, 							\n"
          + " L.CODICE_LIVELLO  							\n"
          + " FROM    										\n"
          + "	NEMBO_D_BANDO A,								\n"
          + "   NEMBO_D_TIPO_LIVELLO B,   					\n"
          + "   NEMBO_R_LIVELLO_BANDO LB,					 	\n"
          + "	NEMBO_D_LIVELLO L                  	   		\n"
          + " WHERE  										\n"
          + "    B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO 		\n"
          + "   AND LB.ID_BANDO = A.ID_BANDO				\n"
          + "   AND L.ID_LIVELLO  = LB.ID_LIVELLO   		\n"
          + "	AND A.FLAG_MASTER <> 'S'                   	\n"
          + "	AND L.ID_TIPOLOGIA_LIVELLO = 3             	\n"
          + " ORDER BY ORDINAMENTO                         	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              String misura = "";
              String sottoMisura = "";
              while (rs.next())
              {
                misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                l = new LivelloDTO();
                if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                {
                  sottoMisura = rs.getString("CODICE_LIVELLO").split("\\.")[1];
                  l.setCodiceSottoMisura(misura + "." + sottoMisura);
                }

                l.setIdLivello(rs.getLong("ID"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                l.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                l.setCodiceMisura(misura);

                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
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

  public List<ControlloAmministrativoDTO> getElencoControlliAmministrativiByIdQuadroOggetto(
      long idQuadroOggetto, final long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoControlliAmministrativiByIdQuadroOggetto";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                                                      \n"
          + "     DECODE(CA.ID_CONTROLLO_AMMINISTRAT_PADRE,NULL, CA.ORDINAMENTO*1000,CA_PADRE.ORDINAMENTO*1000+CA.ORDINAMENTO) AS ORDINE, \n"
          + "     DECODE(BOQCA.ID_QUADRO_OGG_CONTROLLO_AMM,NULL, 'N', 'S') AS FLAG_SELEZIONATO,                                           \n"
          + "     CA.ID_CONTROLLO_AMMINISTRAT_PADRE,                                                                                      \n"
          + "     CA.CODICE,                                                                                                              \n"
          + "     CA.ID_CONTROLLO_AMMINISTRATIVO,                                                                                         \n"
          + "     CA.DESCRIZIONE,                                                                                                         \n"
          + "     QOCA.FLAG_OBBLIGATORIO,                                                                                                 \n"
          + "     QOCA.DATA_FINE_VALIDITA,                                                                                                \n"
          + "     QOCA.ID_QUADRO_OGG_CONTROLLO_AMM,                                                                                       \n"
          + "      (SELECT FLAG_ATTIVO FROM NEMBO_R_BANDO_OGGETTO WHERE ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO) AS FLAG_ATTIVO,               \n"
          + "     QAL.ID_LIVELLO,                                                                                                         \n"
          + "     DL.CODICE AS COD_LIVELLO,                                                                                               \n"
          + "     DL.DESCRIZIONE AS DESCR_LIVELLO                                                                                         \n"
          + "   FROM                                                                                                                      \n"
          + "     NEMBO_D_CONTROLLO_AMMINISTRATI CA                                                                                       \n"
          + "     LEFT JOIN                                                                                                               \n"
          + "     NEMBO_D_CONTROLLO_AMMINISTRATI CA_PADRE                                                                                 \n"
          + "     ON                                                                                                                      \n"
          + "     CA.ID_CONTROLLO_AMMINISTRAT_PADRE = CA_PADRE.ID_CONTROLLO_AMMINISTRATIVO,                                               \n"
          + "     NEMBO_R_QUADRO_OGG_CONTROL_AMM QOCA                                                                                     \n"
          + "     LEFT OUTER JOIN                                                                                                         \n"
          + "     NEMBO_R_BAND_OG_QUAD_CONTR_AMM BOQCA                                                                                    \n"
          + "     ON QOCA.ID_QUADRO_OGG_CONTROLLO_AMM = BOQCA.ID_QUADRO_OGG_CONTROLLO_AMM                                                 \n"
          + "     AND BOQCA.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                                                                          \n"
          + "     LEFT OUTER JOIN                                                                                                         \n"
          + "     NEMBO_R_BAN_OG_QUA_CON_AMM_LIV QAL                                                                                      \n"
          + "     ON QAL.ID_BANDO_OGGETTO = BOQCA.ID_BANDO_OGGETTO                                                                        \n"
          + "     AND QAL.ID_QUADRO_OGG_CONTROLLO_AMM = QOCA.ID_QUADRO_OGG_CONTROLLO_AMM                                                  \n"
          + "     LEFT OUTER JOIN                                                                                                         \n"
          + "     NEMBO_D_LIVELLO DL                                                                                                        \n"
          + "     ON DL.ID_LIVELLO = QAL.ID_LIVELLO                                                                                       \n"
          + "   WHERE                                                                                                                     \n"
          + "     CA.ID_CONTROLLO_AMMINISTRATIVO   = QOCA.ID_CONTROLLO_AMMINISTRATIVO                                                     \n"
          + "     AND QOCA.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO                                                                         \n"
          + "     AND NVL(QOCA.DATA_FINE_VALIDITA,SYSDATE) >= SYSDATE                                                                     \n"
          + "   ORDER BY                                                                                                                  \n"
          + "         ORDINE ASC, DL.ORDINAMENTO                                                                                          \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<ControlloAmministrativoDTO>>()
          {
            @Override
            public List<ControlloAmministrativoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<ControlloAmministrativoDTO> list = new ArrayList<ControlloAmministrativoDTO>();
              List<ControlloAmministrativoDTO> listFigli = null;
              ControlloAmministrativoDTO controlloPadre = null;
              ControlloAmministrativoDTO controlloFiglio = null;
              long idControlloAmministrativoPadre = -1;
              long idControlloLast = 0;
              long idControllo = 0;
              List<LivelloDTO> elencoLivelli = null;

              while (rs.next())
              {
                idControlloAmministrativoPadre = rs
                    .getLong("ID_CONTROLLO_AMMINISTRAT_PADRE");
                if (idControlloAmministrativoPadre <= 0)
                {
                  controlloPadre = new ControlloAmministrativoDTO();
                  listFigli = new ArrayList<ControlloAmministrativoDTO>();
                  controlloPadre.setCodice(rs.getString("CODICE"));
                  controlloPadre
                      .setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
                  controlloPadre.setDescrizione(rs.getString("DESCRIZIONE"));
                  controlloPadre.setFlagAttivo(rs.getString("FLAG_ATTIVO"));
                  controlloPadre
                      .setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                  controlloPadre
                      .setFlagSelezionato(rs.getString("FLAG_SELEZIONATO"));
                  controlloPadre.setIdControlloAmministrativo(
                      rs.getLong("ID_CONTROLLO_AMMINISTRATIVO"));
                  controlloPadre.setIdControlloAmministratPadre(
                      idControlloAmministrativoPadre);
                  controlloPadre.setIdQuadroOggControlloAmm(
                      rs.getLong("ID_QUADRO_OGG_CONTROLLO_AMM"));
                  controlloPadre.setControlliFigli(listFigli);
                  list.add(controlloPadre);
                }
                else
                {
                  idControlloLast = rs.getLong("ID_CONTROLLO_AMMINISTRATIVO");

                  if (idControlloLast != idControllo)
                  {
                    idControllo = idControlloLast;
                    controlloFiglio = new ControlloAmministrativoDTO();
                    controlloFiglio.setCodice(rs.getString("CODICE"));
                    controlloFiglio
                        .setDataFineValidita(rs.getDate("DATA_FINE_VALIDITA"));
                    controlloFiglio.setDescrizione(rs.getString("DESCRIZIONE"));
                    controlloFiglio.setIdBandoOggetto(idBandoOggetto);
                    controlloFiglio.setFlagAttivo(rs.getString("FLAG_ATTIVO"));
                    controlloFiglio
                        .setFlagObbligatorio(rs.getString("FLAG_OBBLIGATORIO"));
                    controlloFiglio
                        .setFlagSelezionato(rs.getString("FLAG_SELEZIONATO"));
                    controlloFiglio.setIdControlloAmministrativo(
                        rs.getLong("ID_CONTROLLO_AMMINISTRATIVO"));
                    controlloFiglio.setIdControlloAmministratPadre(
                        idControlloAmministrativoPadre);
                    controlloFiglio.setIdQuadroOggControlloAmm(
                        rs.getLong("ID_QUADRO_OGG_CONTROLLO_AMM"));
                    elencoLivelli = new ArrayList<LivelloDTO>();
                    controlloFiglio.setElencoLivelli(elencoLivelli);
                    listFigli.add(controlloFiglio);
                  }

                  if (rs.getString("COD_LIVELLO") != null)
                  {
                    LivelloDTO liv = new LivelloDTO();
                    liv.setCodice(rs.getString("COD_LIVELLO"));
                    liv.setDescrizione(rs.getString("DESCR_LIVELLO"));
                    elencoLivelli.add(liv);
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

  public long insertControlloAmministrativo(Long idControlloAmministratPadre,
      String codice, String descrizione) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertControlloAmministrativo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    long idControlloAmministrativo = 0;
    try
    {

      INSERT = " INSERT                       		\n"
          + " INTO                       			\n"
          + "   NEMBO_D_CONTROLLO_AMMINISTRATI    \n"
          + "   (                        			\n"
          + "     ID_CONTROLLO_AMMINISTRATIVO,    \n"
          + "     ID_CONTROLLO_AMMINISTRAT_PADRE,	\n"
          + "     CODICE,		    	   			\n"
          + "     DESCRIZIONE,    	   			\n"
          + "     ORDINAMENTO      	   			\n"
          + "   )                        			\n"
          + "   VALUES                   			\n"
          + "   (                        			\n"
          + "     :ID_CONTROLLO_AMMINISTRATIVO,   \n"
          + "     :ID_CONTROLLO_AMMINISTRAT_PADRE, \n"
          + "     :CODICE,		    	   		\n"
          + "     :DESCRIZIONE,    	   			\n";
      if (idControlloAmministratPadre == null)
      {
        INSERT = INSERT
            + "     (SELECT MAX(ORDINAMENTO)+1 FROM  NEMBO_D_CONTROLLO_AMMINISTRATI WHERE ID_CONTROLLO_AMMINISTRAT_PADRE IS NULL)  \n";
      }
      else
      {
        INSERT = INSERT
            + "     (SELECT NVL(MAX(ORDINAMENTO)+1,1) FROM  NEMBO_D_CONTROLLO_AMMINISTRATI WHERE ID_CONTROLLO_AMMINISTRAT_PADRE = :ID_CONTROLLO_AMMINISTRAT_PADRE)  \n";
      }

      INSERT = INSERT + "   )                        \n";
      idControlloAmministrativo = getNextSequenceValue(
          "SEQ_NEMBO_D_CONTROLLO_AMMINIST");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_CONTROLLO_AMMINISTRATIVO",
          idControlloAmministrativo, Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO_AMMINISTRAT_PADRE",
          idControlloAmministratPadre, Types.NUMERIC);
      mapParameterSource.addValue("CODICE", codice, Types.VARCHAR);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idControlloAmministratPadre",
                  idControlloAmministratPadre),
              new LogParameter("codice", codice),
              new LogParameter("descrizione", descrizione),
              new LogParameter("codice", codice),
              new LogParameter("idControlloAmministrativo",
                  idControlloAmministrativo),
          },
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
    return idControlloAmministrativo;
  }

  public long insertQuadroOggettoControlloAmministrativo(
      long idControlloAmministrativo, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertQuadroOggettoControlloAmministrativo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    long idQuadroOggControlloAmm = 0;
    try
    {

      INSERT = " INSERT                       		\n"
          + " INTO                       			\n"
          + "   NEMBO_R_QUADRO_OGG_CONTROL_AMM    \n"
          + "   (                        			\n"
          + "     ID_QUADRO_OGG_CONTROLLO_AMM,    \n"
          + "     ID_QUADRO_OGGETTO,				\n"
          + "     ID_CONTROLLO_AMMINISTRATIVO,	\n"
          + "     FLAG_OBBLIGATORIO,    	   		\n"
          + "     DATA_FINE_VALIDITA      	   	\n"
          + "   )                        			\n"
          + "   VALUES                   			\n"
          + "   (                        			\n"
          + "     :ID_QUADRO_OGG_CONTROLLO_AMM,   \n"
          + "     :ID_QUADRO_OGGETTO,				\n"
          + "     :ID_CONTROLLO_AMMINISTRATIVO,	\n"
          + "     'N',    	   					\n"
          + "     NULL      	   					\n"
          + "   )                        			\n";

      idQuadroOggControlloAmm = getNextSequenceValue(
          "SEQ_NEMBO_R_QUAD_OGG_CONTR_AMM");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_CONTROLLO_AMMINISTRATIVO",
          idControlloAmministrativo, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggControlloAmm, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idControlloAmministrativo",
                  idControlloAmministrativo),
              new LogParameter("idQuadroOggetto", idQuadroOggetto),
              new LogParameter("idQuadroOggControlloAmm",
                  idQuadroOggControlloAmm)
          },
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
    return idQuadroOggControlloAmm;
  }

  public void insertBandoOggettoControlloAmministrazivo(
      long idQuadroOggControlloAmm, long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertBandoOggettoControlloAmministrazivo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                       		\n"
          + " INTO                       			\n"
          + "   NEMBO_R_BAND_OG_QUAD_CONTR_AMM    \n"
          + "   (                        			\n"
          + "     ID_BANDO_OGGETTO,    			\n"
          + "     ID_QUADRO_OGG_CONTROLLO_AMM		\n"
          + "   )                        			\n"
          + "   VALUES                   			\n"
          + "   (                        			\n"
          + "     :ID_BANDO_OGGETTO,    			\n"
          + "     :ID_QUADRO_OGG_CONTROLLO_AMM	\n"
          + "   )                        			\n";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggControlloAmm, Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadroOggControlloAmm",
                  idQuadroOggControlloAmm),
              new LogParameter("idBandoOggetto", idBandoOggetto)
          },
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

  public void disattivaBandoOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "disattivaBandoOggetto";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                 \n"
          + "   NEMBO_R_BANDO_OGGETTO                  \n"
          + " SET                                    \n"
          + "   FLAG_ATTIVO = 'N' 					 \n"
          + " WHERE                                  \n"
          + "   ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
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

  public void logAttivitaBandoOggetto(long idBandoOggetto, Long idUtente,
      String descrizione, String note)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::logAttivitaBandoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String INSERT = "INSERT INTO NEMBO_t_attivita_bando_oggetto 	\n"
        + " (ID_ATTIVITA_BANDO_OGGETTO,			\n"
        + "	 ID_BANDO_OGGETTO, 					\n"
        + "	 EXT_ID_UTENTE_AGGIORNAMENTO, 		\n"
        + "	 DATA_ULTIMO_AGGIORNAMENTO,			\n"
        + "	 DESC_ATTIVITA,						\n"
        + "	 NOTE 								\n"
        + ") 									\n"
        + "VALUES 								\n"
        + " (:ID_ATTIVITA_BANDO_OGGETTO,		\n"
        + "	 :ID_BANDO_OGGETTO, 				\n"
        + "	 :ID_UTENTE,						\n"
        + "	 SYSDATE, 							\n"
        + "	 :DESCRIZIONE, 						\n"
        + "	 :NOTE 								\n"
        + ") 									\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      long id = getNextSequenceValue("seq_NEMBO_t_attivita_bando_ogg");
      mapParameterSource.addValue("ID_ATTIVITA_BANDO_OGGETTO", id,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_UTENTE", idUtente, Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);

      if (note.length() > 4000)
      {
        note = note.substring(0, 4000);
      }
      mapParameterSource.addValue("NOTE", note, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_UTENTE", idUtente),
              new LogParameter("DESCRIZIONE", descrizione),
              new LogParameter("NOTE", note)
          },
          new LogVariable[]
          {}, INSERT, mapParameterSource);
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

  public boolean canLogAttivitaBandoOggetto(Long idBandoOggetto)
      throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::canLogAttivitaBandoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = "SELECT DESC_ATTIVITA FROM  NEMBO_t_attivita_bando_oggetto		\n"
        + "WHERE 	ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO					\n"
        + "ORDER BY DATA_ULTIMO_AGGIORNAMENTO DESC					\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      Boolean result = namedParameterJdbcTemplate.query(SELECT,
          mapParameterSource, new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
              {

                if (rs.getString("DESC_ATTIVITA").compareTo("ATTIVATO") == 0)
                  return Boolean.FALSE;

                if (rs.getString("DESC_ATTIVITA").compareTo("DISATTIVATO") == 0)
                  return Boolean.TRUE;

              }

              return Boolean.FALSE;
            }
          });

      return result.booleanValue();
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
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

  public Long getFirstIdBandoOggetto(long idBando)
      throws InternalUnexpectedException
  {

    String THIS_METHOD = "[" + THIS_CLASS + "::getFirstIdBandoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT * FROM (													\n"
        + "SELECT ID_BANDO_OGGETTO										\n"
        + "FROM															\n"
        + " NEMBO_R_BANDO_OGGETTO A,										\n"
        + " NEMBO_R_LEGAME_GRUPPO_OGGETTO B,								\n"
        + " NEMBO_D_GRUPPO_OGGETTO C										\n"
        + "WHERE 															\n"
        + "	A.ID_BANDO=:ID_BANDO										\n"
        + "AND															\n"
        + "	A.ID_LEGAME_GRUPPO_OGGETTO = B.ID_LEGAME_GRUPPO_OGGETTO		\n"
        + "AND															\n"
        + "	B.ID_GRUPPO_OGGETTO = C.ID_GRUPPO_OGGETTO					\n"
        + "ORDER BY C.ORDINE, B.ORDINE									\n"
        + ")																\n"
        + "WHERE ROWNUM= 1												\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return queryForLong(SELECT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
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

  public Boolean checkControlliObbligatoriInseriti(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "checkControlliObbligatoriInseriti";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = "   SELECT                                                                           \n"
          + "     C.ID_OGGETTO,                                                                  \n"
          + "     C.DESCRIZIONE,                                                                 \n"
          + "     G.ID_CONTROLLO,                                                                \n"
          + "     F.FLAG_OBBLIGATORIO,                                                           \n"
          + "     F.GRAVITA,                                                                     \n"
          + "     (SELECT H.ID_CONTROLLO                                                         \n"
          + "        FROM NEMBO_R_BANDO_OGGETTO_CONTROLL H,                                       \n"
          + "             NEMBO_R_BANDO_OGGETTO I                                                  \n"
          + "        WHERE                                                                       \n"
          + "             I.ID_BANDO_OGGETTO = H.ID_BANDO_OGGETTO                                \n"
          + "             and h.id_controllo = g.id_controllo                                    \n"
          + "             AND H.ID_BANDO_OGGETTO =  D.ID_BANDO_OGGETTO                           \n"
          + "             AND I.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO  ) AS ID_CONTROLLO_INSERITO \n"
          + "    FROM                                                                            \n"
          + "      NEMBO_R_BANDO_OGGETTO A,                                                        \n"
          + "      NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                                                \n"
          + "      NEMBO_D_OGGETTO C,                                                              \n"
          + "      NEMBO_R_BANDO_OGGETTO_QUADRO D,                                                 \n"
          + "      NEMBO_R_QUADRO_OGGETTO E,                                                       \n"
          + "      NEMBO_R_QUADRO_OGGETTO_CONTROL F,                                             \n"
          + "      NEMBO_D_CONTROLLO G,                                                            \n"
          + "      NEMBO_D_QUADRO L                                                                \n"
          + "    WHERE                                                                           \n"
          + "      A.ID_LEGAME_GRUPPO_OGGETTO = B.ID_LEGAME_GRUPPO_OGGETTO                       \n"
          + "      AND B.ID_OGGETTO = C.ID_OGGETTO                                               \n"
          + "      AND D.ID_BANDO_OGGETTO = A.ID_BANDO_OGGETTO                                   \n"
          + "      AND E.ID_QUADRO_OGGETTO = D.ID_QUADRO_OGGETTO                                 \n"
          + "      AND E.ID_QUADRO_OGGETTO = F.ID_QUADRO_OGGETTO                                 \n"
          + "      AND G.ID_CONTROLLO = F.ID_CONTROLLO                                           \n"
          + "      AND L.ID_QUADRO = E.ID_QUADRO                                                 \n"
          + "      AND G.DATA_INIZIO <= NVL(A.DATA_FINE,TO_DATE('31/12/9999','DD/MM/YYYY'))      \n"
          + "      AND NVL(G.DATA_FINE,TO_DATE('31/12/9999','DD/MM/YYYY')) >= A.DATA_INIZIO      \n"
          + "      AND A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                                    \n"
          + "      AND F.FLAG_OBBLIGATORIO = 'S'                                                 \n"
          + "   ORDER BY B.ORDINE, E.ORDINE, G.CODICE                                            \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              Long idControlloInserito = null;
              while (rs.next())
              {
                idControlloInserito = rs.getLong("ID_CONTROLLO_INSERITO");

                if (idControlloInserito == null
                    || idControlloInserito.longValue() == 0)
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

  public String getDescrizioneFiltro(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDescrizioneFiltro]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT DESCRIZIONE_FILTRO		\n"
        + "FROM NEMBO_D_BANDO				\n"
        + "WHERE ID_BANDO = :ID_BANDO		\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
                return rs.getString("DESCRIZIONE_FILTRO");
              return "";

            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
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

  public List<LogRecordDTO> readLog(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::readLog]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT 											\n"
        + "	A.ID_BANDO_OGGETTO,							\n"
        + "	(SELECT 									\n"
        + "		O.DESCRIZIONE 							\n"
        + "	FROM 										\n"
        + "		NEMBO_D_OGGETTO O, 						\n"
        + "		NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO			\n"
        + "	WHERE 										\n"
        + "		LGO.ID_OGGETTO = O.ID_OGGETTO			\n"
        + "		AND B.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO) AS DESCR_OGGETTO,				\n"
        + "	A.EXT_ID_UTENTE_AGGIORNAMENTO,				\n"
        + "	A.DATA_ULTIMO_AGGIORNAMENTO,				\n"
        + "	A.DESC_ATTIVITA,							\n"
        + "	A.NOTE										\n"
        + "FROM NEMBO_t_attivita_bando_oggetto A,			\n"
        + "NEMBO_R_BANDO_OGGETTO B							\n"
        + "WHERE B.ID_BANDO = :ID_BANDO					\n"
        + "AND B.ID_BANDO_OGGETTO = A.ID_BANDO_OGGETTO	\n"
        + "ORDER BY DATA_ULTIMO_AGGIORNAMENTO DESC		\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      return (LinkedList<LogRecordDTO>) namedParameterJdbcTemplate.query(SELECT,
          mapParameterSource, new ResultSetExtractor<LinkedList<LogRecordDTO>>()
          {
            @Override
            public LinkedList<LogRecordDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              LinkedList<LogRecordDTO> records = new LinkedList<LogRecordDTO>();
              LogRecordDTO toDTO = null;
              while (rs.next())
              {
                toDTO = new LogRecordDTO();
                toDTO.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                toDTO.setDescrOggetto(rs.getString("DESCR_OGGETTO"));
                toDTO.setExtIdUtenteAggiornamento(
                    rs.getLong("EXT_ID_UTENTE_AGGIORNAMENTO"));
                toDTO.setDataUltimoAggiornamento(
                    rs.getTimestamp("DATA_ULTIMO_AGGIORNAMENTO"));
                toDTO.setDescrizioneAttivita(rs.getString("DESC_ATTIVITA"));
                toDTO.setNote(
                    NemboUtils.STRING.safeHTMLText(rs.getString("NOTE")));
                records.add(toDTO);
              }

              return records;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
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

  public String getDescrizioneOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDescrizioneOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT DESCRIZIONE												\n"
        + "FROM NEMBO_D_OGGETTO A,											\n"
        + "NEMBO_R_LEGAME_GRUPPO_OGGETTO B,									\n"
        + "NEMBO_R_BANDO_OGGETTO C											\n"
        + "WHERE A.ID_OGGETTO = B.ID_OGGETTO								\n"
        + "AND															\n"
        + "B.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO		\n"
        + "AND															\n"
        + "C.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO							\n";

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
                return rs.getString("DESCRIZIONE");
              return "";

            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
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

  public String getHelpCdu(String codcdu) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getHelpCdu]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                \n"
        + "   HELP_CDU 	          \n"
        + " FROM                  \n"
        + "   NEMBO_D_ELENCO_CDU "
        + " WHERE "
        + "	 CODICE_CDU = :CODICE_CDU    \n";
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("CODICE_CDU", codcdu, Types.VARCHAR);
      return queryForString(QUERY, mapParameterSource, "HELP_CDU");
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          (LogParameter[]) null,
          new LogVariable[]
          {}, QUERY, (MapSqlParameterSource) null);
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

  public List<FocusAreaDTO> getElencoFocusAreaBandi()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoFocusAreaBandi";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT   DISTINCT                               \n"
          + "   F.ID_FOCUS_AREA,                      \n"
          + "   D.CODICE                              \n"
          + " FROM                                    \n"
          + "   NEMBO_D_BANDO B,                        \n"
          + "   NEMBO_R_LIVELLO_BANDO L,                \n"
          + "   NEMBO_R_LIVELLO_FOCUS_AREA F,           \n"
          + "   NEMBO_D_FOCUS_AREA D                    \n"
          + " WHERE                                   \n"
          + "   B.ID_BANDO = L.ID_BANDO               \n"
          + "   AND F.ID_LIVELLO = L.ID_LIVELLO       \n"
          + "   AND D.ID_FOCUS_AREA = F.ID_FOCUS_AREA \n"
          + "   AND F.ID_PIANO_FINANZIARIO = 1        \n"
          + "   ORDER BY D.CODICE            \n";

      return namedParameterJdbcTemplate.query(SELECT,
          (MapSqlParameterSource) null,
          new ResultSetExtractor<List<FocusAreaDTO>>()
          {
            @Override
            public List<FocusAreaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<FocusAreaDTO> list = new ArrayList<FocusAreaDTO>();
              FocusAreaDTO item = null;
              while (rs.next())
              {
                item = new FocusAreaDTO();
                item.setIdFocusArea(rs.getLong("ID_FOCUS_AREA"));
                item.setCodice(rs.getString("CODICE"));
                list.add(item);
              }
              return list.isEmpty() ? null : list;
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

  public List<SettoriDiProduzioneDTO> getElencoSettoriBandi()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoSettoriBandi";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                   \n"
          + "   S.CODICE,                       \n"
          + "   S.ID_SETTORE                    \n"
          + " FROM                              \n"
          + "   NEMBO_D_BANDO B,                  \n"
          + "   NEMBO_R_LIVELLO_BANDO L,          \n"
          + "   NEMBO_D_LIVELLO D,                \n"
          + "   NEMBO_D_SETTORE S                 \n"
          + " WHERE                             \n"
          + "   B.ID_BANDO = L.ID_BANDO         \n"
          + "   AND L.ID_LIVELLO = D.ID_LIVELLO \n"
          + "   AND D.ID_SETTORE = S.ID_SETTORE \n"
          + "   ORDER BY S.CODICE      \n";

      return namedParameterJdbcTemplate.query(SELECT,
          (MapSqlParameterSource) null,
          new ResultSetExtractor<List<SettoriDiProduzioneDTO>>()
          {
            @Override
            public List<SettoriDiProduzioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<SettoriDiProduzioneDTO> list = new ArrayList<SettoriDiProduzioneDTO>();
              SettoriDiProduzioneDTO item = null;
              while (rs.next())
              {
                item = new SettoriDiProduzioneDTO();
                item.setIdSettore(rs.getLong("ID_SETTORE"));
                item.setDescrizione(rs.getString("CODICE"));
                list.add(item);
              }
              return list.isEmpty() ? null : list;
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

  public Boolean checkParametriControlliInseriti(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "checkParametriControlliInseriti";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   H.ID_CONTROLLO                                     \n"
          + " FROM                                                 \n"
          + "    NEMBO_R_BANDO_OGGETTO_CONTROLL H,                  \n"
          + "    NEMBO_R_BANDO_OGGETTO I,                            \n"
          + "    NEMBO_D_CONTROLLO C                                 \n"
          + " WHERE                                                \n"
          + "    I.ID_BANDO_OGGETTO = H.ID_BANDO_OGGETTO           \n"
          + "    AND C.ID_CONTROLLO = H.ID_CONTROLLO               \n"
          + "    AND C.DESC_PARAMETRO IS NOT NULL                  \n"
          + "    AND I.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO        \n"
          + "    AND NOT EXISTS (                                  \n"
          + "            SELECT                                    \n"
          + "                  V.ID_VALORI_PARAMETRI               \n"
          + "            FROM                                      \n"
          + "              NEMBO_T_VALORI_PARAMETRI V                \n"
          + "            WHERE                                     \n"
          + "              V.ID_BANDO_OGGETTO = H.ID_BANDO_OGGETTO \n"
          + "              AND V.ID_CONTROLLO = H.ID_CONTROLLO     \n"
          + "            )                                         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return false;
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

  public Ricevuta getDatiRicevutaMail(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDatiRicevutaMail";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 									\n"
          + " 	A.OGGETTO_RICEVUTA AS OGGETTO_MAIL,  	\n"
          + " 	A.CORPO_RICEVUTA AS CORPO_MAIL			\n"
          + " FROM 										\n"
          + "	NEMBO_R_BANDO_OGGETTO A		    		\n"
          + " WHERE 									\n"
          + "   A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO 	\n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return queryForObject(SELECT, parameterSource, Ricevuta.class);
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

  public void updateRBandoOggetto(long idBandoOggetto, String oggettoRicevuta,
      String corpoRicevuta) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateRBandoOggetto";
    StringBuffer UPDATE = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      UPDATE.append(" UPDATE                                    \n"
          + "   NEMBO_R_BANDO_OGGETTO A        SET        \n");

      if (oggettoRicevuta != null)
      {
        UPDATE.append("    A.OGGETTO_RICEVUTA = :OGGETTO_RICEVUTA \n");
      }
      else
      {
        UPDATE.append("    A.OGGETTO_RICEVUTA = NULL \n");
      }

      if (corpoRicevuta != null)
      {
        UPDATE.append("    , A.CORPO_RICEVUTA = :CORPO_RICEVUTA \n");
      }
      else
      {
        UPDATE.append("    , A.CORPO_RICEVUTA = NULL \n");
      }

      UPDATE.append(" WHERE                                     \n"
          + "    A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n");

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto);
      if (oggettoRicevuta != null)
      {
        mapParameterSource.addValue("OGGETTO_RICEVUTA", oggettoRicevuta);
      }
      if (corpoRicevuta != null)
      {
        mapParameterSource.addValue("CORPO_RICEVUTA", corpoRicevuta);
      }
      namedParameterJdbcTemplate.update(UPDATE.toString(), mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("oggettoRicevuta", oggettoRicevuta),
              new LogParameter("corpoRicevuta", corpoRicevuta)
          },
          new LogVariable[]
          {}, UPDATE.toString(), new MapSqlParameterSource());
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

  public Boolean checkQuadroInserito(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "checkQuadroInserito";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = "   SELECT ID_BANDO_OGGETTO FROM NEMBO_R_BANDO_OGGETTO_QUADRO WHERE   ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO  ";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
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

  public boolean canLogAttivitaBandoOggettoForAttiPubblicati(
      Long idBandoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::canLogAttivitaBandoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = "SELECT DESC_ATTIVITA FROM  NEMBO_t_attivita_bando_oggetto		\n"
        + "WHERE 	ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO					\n"
        + "ORDER BY DATA_ULTIMO_AGGIORNAMENTO DESC					\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      Boolean result = namedParameterJdbcTemplate.query(SELECT,
          mapParameterSource, new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
              {

                if (rs.getString("DESC_ATTIVITA").compareTo("ATTIVATO") == 0)
                  return Boolean.TRUE;

                if (rs.getString("DESC_ATTIVITA").compareTo("DISATTIVATO") == 0)
                  return Boolean.TRUE;

              }

              return Boolean.FALSE;
            }
          });

      return result.booleanValue();
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
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

  public ReportVO getDescTabella(String nomeTabella)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getDescTabella";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = "SELECT * FROM " + nomeTabella + " order by 1 desc";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<ReportVO>()
          {
            @Override
            public ReportVO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              return elabReport(rs);
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

  @SuppressWarnings("rawtypes")
  public void insertRow(String nomeTabella,
      LinkedHashMap<String, String> mapValues)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRow";
    StringBuffer INSERT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      int count = 0;
      INSERT.append(" INSERT                       \n"
          + " INTO                       \n"
          + nomeTabella + "			       \n"
          + "   (                        \n");

      Iterator it = mapValues.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry pair = (Map.Entry) it.next();
        INSERT.append(pair.getKey());
        if (count < mapValues.size() - 1)
        {
          INSERT.append(",");
        }
        count++;
      }
      INSERT.append(" )  VALUES     (              \n");
      count = 0;
      it = mapValues.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry pair = (Map.Entry) it.next();
        if (count > 0)
        {
          INSERT.append(":" + pair.getKey());
        }
        else
        {
          INSERT.append(
              "(SELECT MAX(" + pair.getKey() + ")+1 FROM " + nomeTabella + ")");
        }

        if (count < mapValues.size() - 1)
        {
          INSERT.append(",");
        }
        count++;
      }

      INSERT.append(")");

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      it = mapValues.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry pair = (Map.Entry) it.next();
        mapParameterSource.addValue((String) pair.getKey(),
            (pair.getKey().toString().indexOf("DATA") >= 0
                ? NemboUtils.DATE.parseDate((String) pair.getValue())
                : pair.getValue()));
      }
      namedParameterJdbcTemplate.update(INSERT.toString(), mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          null,
          new LogVariable[]
          {}, INSERT.toString(), new MapSqlParameterSource());
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

  public void deleteRowByID(String nomeTabella, String nomeColonnaId, long id)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRowByID";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " delete from " + nomeTabella + " where " + nomeColonnaId
          + " = :ID ";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID", id);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID", id)
          },
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

  public List<OggettiIstanzeDTO> getElencoOggettiNemboconf()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoOggettiNemboconf";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = "SELECT ID_OGGETTO, CODICE AS COD_OGGETTO, DESCRIZIONE AS DESCR_OGGETTO,FLAG_ISTANZA FROM NEMBO_D_OGGETTO order by codice";
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      return queryForList(SELECT, mapParameterSource, OggettiIstanzeDTO.class);
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

  public List<QuadroDTO> getElencoQuadriDisponibili(long idOggetto)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoQuadriDisponibili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = "SELECT "
          + "  A.ID_QUADRO,"
          + "  A.DESCRIZIONE,"
          + "  A.CODICE "
          + "FROM "
          + "	NEMBO_D_QUADRO A"
          + " WHERE "
          + " NOT EXISTS (SELECT B.ID_QUADRO FROM NEMBO_R_quadro_OGGETTO B WHERE B.ID_QUADRO = A.ID_QUADRO and B.ID_OGGETTO = :ID_OGGETTO) "
          + " ORDER BY DESCRIZIONE	 \n";

      mapParameterSource.addValue("ID_OGGETTO", idOggetto);
      return queryForList(QUERY, mapParameterSource, QuadroDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          null,
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

  public List<QuadroDTO> getElencoQuadriSelezionati(long idOggetto)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoQuadriSelezionati";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT "
          + "  A.ID_QUADRO,"
          + "  A.DESCRIZIONE,"
          + "  A.CODICE "
          + " FROM "
          + "	 NEMBO_R_QUADRO_OGGETTO B,"
          + "	 NEMBO_D_QUADRO A"
          + " WHERE"
          + "	 A.ID_QUADRO = B.ID_QUADRO"
          + "	AND B.ID_OGGETTO = :ID_OGGETTO"
          + " ORDER BY B.ORDINE";

      mapParameterSource.addValue("ID_OGGETTO", idOggetto);
      return queryForList(QUERY, mapParameterSource, QuadroDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          null,
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

  public void deleteRQuadroOggetto(long idQuadro, long idOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRQuadroOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                    			 \n"
          + "   NEMBO_R_QUADRO_OGGETTO		 		 \n"
          + " WHERE    								 \n"
          + "  ID_QUADRO= :ID_QUADRO   \n"
          + "  AND ID_OGGETTO = :ID_OGGETTO   	 \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
      mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadro", idQuadro),
              new LogParameter("idOggetto", idOggetto)
          },
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

  public void insertRQuadroOggetto(long idOggetto, long idQuadro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRQuadroOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT INTO                    			 \n"
          + "   NEMBO_R_QUADRO_OGGETTO	(ID_QUADRO, ID_OGGETTO, ID_QUADRO_OGGETTO, FLAG_OBBLIGATORIO, ORDINE)	 		 \n"
          + " VALUES    								 \n"
          + "  ( :ID_QUADRO,   \n"
          + "    :ID_OGGETTO,"
          + "    (select max(ID_QUADRO_OGGETTO)+1 from NEMBO_R_QUADRO_OGGETTO ),"
          + "  'N',"
          + " (select max(ORDINE)+1 from NEMBO_R_QUADRO_OGGETTO where ID_OGGETTO = :ID_OGGETTO) )  	 \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
      mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadro", idQuadro),
              new LogParameter("idOggetto", idOggetto)
          },
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

  public String getDescrizioneOggettoById(long idOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getDescrizioneOggettoById]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT A.DESCRIZIONE											\n"
        + "FROM NEMBO_D_OGGETTO A											\n"
        + "WHERE A.ID_OGGETTO = :ID_OGGETTO								\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<String>()
          {
            @Override
            public String extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
                return rs.getString("DESCRIZIONE");
              return "";

            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idOggetto", idOggetto)
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

  public Long getOrdineQuadroOggetto(long idOggetto, long idQuadro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getOrdineQuadroOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT A.ORDINE					\n"
        + "FROM NEMBO_R_QUADRO_OGGETTO A			\n"
        + "WHERE A.ID_OGGETTO = :ID_OGGETTO	\n"
        + " AND A.ID_QUADRO = :ID_QUADRO 		\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<Long>()
          {
            @Override
            public Long extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
              {
                return rs.getLong("ORDINE");
              }
              return new Long(0);

            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idOggetto", idOggetto)
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

  public void updateOrdineQuadroOggetto(long idOggetto, long idQuadro,
      long ordine) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateOrdineQuadroOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " UPDATE"
          + "	 NEMBO_R_QUADRO_OGGETTO"
          + " SET "
          + "		ORDINE = :ORDINE"
          + " WHERE "
          + "	  ID_QUADRO = :ID_QUADRO "
          + "	  AND ID_OGGETTO = :ID_OGGETTO	 \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_QUADRO", idQuadro, Types.NUMERIC);
      mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      mapParameterSource.addValue("ORDINE", ordine, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadro", idQuadro),
              new LogParameter("idOggetto", idOggetto),
              new LogParameter("ordine", ordine)
          },
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

  public List<BeneficiarioDTO> getBeneficiari(long idBando)
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
      QUERY = " SELECT                                                       \n"
          + "  RL.ID_LIVELLO,                                              \n"
          + "  RL.EXT_ID_FG_TIPOLOGIA AS ID_FG_TIPOLOGIA,                  \n"
          + "  TA.DESC_TIPOLOGIA_AZIENDA,                                  \n"
          + "  TA.DESC_FORMA_GIURIDICA                                     \n"
          + " FROM                                                         \n"
          + "  NEMBO_R_LIV_BANDO_BENEFICIARIO RL,                            \n"
          + "  SMRGAA_V_TIPOLOGIA_AZIENDA TA                               \n"
          + " WHERE                                                        \n"
          + "  RL.ID_BANDO = :ID_BANDO                                     \n"
          + "  AND RL.EXT_ID_FG_TIPOLOGIA = TA.ID_FG_TIPOLOGIA             \n"
          + " ORDER BY TA.DESC_TIPOLOGIA_AZIENDA , TA.DESC_FORMA_GIURIDICA \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      return queryForList(QUERY, mapParameterSource, BeneficiarioDTO.class);
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

  public List<BeneficiarioDTO> getElencoBeneficiariDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoBeneficiariDisponibili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                     	\n"
          + "   RL.ID_LIVELLO,                                              \n"
          + "   RL.EXT_ID_FG_TIPOLOGIA AS ID_FG_TIPOLOGIA,                  \n"
          + "   TA.DESC_TIPOLOGIA_AZIENDA,                                  \n"
          + "   TA.DESC_FORMA_GIURIDICA                                     \n"
          + "  FROM                                                         \n"
          + "   NEMBO_R_LIVELLO_BANDO RB,                                     \n"
          + "   NEMBO_R_LIVELLO_FG_TIPOLOGIA RL,                              \n"
          + "   SMRGAA_V_TIPOLOGIA_AZIENDA TA                               \n"
          + "  WHERE                                                        \n"
          + "   RB.ID_BANDO = :ID_BANDO                                     \n"
          + "   AND RL.ID_LIVELLO = RB.ID_LIVELLO                           \n"
          + "   AND RL.EXT_ID_FG_TIPOLOGIA = TA.ID_FG_TIPOLOGIA             \n"
          + "   AND NOT EXISTS (                                            \n"
          + "       SELECT                                                  \n"
          + "           A.EXT_ID_FG_TIPOLOGIA                               \n"
          + "       FROM                                                    \n"
          + "           NEMBO_R_LIV_BANDO_BENEFICIARIO A                      \n"
          + "       WHERE                                                   \n"
          + "           A.ID_BANDO = RB.ID_BANDO                            \n"
          + "           AND A.ID_LIVELLO = RL.ID_LIVELLO                    \n"
          + "           AND A.EXT_ID_FG_TIPOLOGIA =  RL.EXT_ID_FG_TIPOLOGIA \n"
          + "   )                                                           \n"
          + "  ORDER BY TA.DESC_TIPOLOGIA_AZIENDA , TA.DESC_FORMA_GIURIDICA \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      return queryForList(QUERY, mapParameterSource, BeneficiarioDTO.class);
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

  public List<BeneficiarioDTO> getElencoBeneficiariSelezionati(long idBando)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoBeneficiariSelezioanti";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                        \n"
          + "   RL.ID_LIVELLO,                                              \n"
          + "   RL.EXT_ID_FG_TIPOLOGIA AS ID_FG_TIPOLOGIA,                  \n"
          + "   TA.DESC_TIPOLOGIA_AZIENDA,                                  \n"
          + "   TA.DESC_FORMA_GIURIDICA                                     \n"
          + "  FROM                                                         \n"
          + "   NEMBO_R_LIVELLO_BANDO RB,                                     \n"
          + "   NEMBO_R_LIVELLO_FG_TIPOLOGIA RL,                              \n"
          + "   SMRGAA_V_TIPOLOGIA_AZIENDA TA                               \n"
          + "  WHERE                                                        \n"
          + "   RB.ID_BANDO = :ID_BANDO                                     \n"
          + "   AND RL.ID_LIVELLO = RB.ID_LIVELLO                           \n"
          + "   AND RL.EXT_ID_FG_TIPOLOGIA = TA.ID_FG_TIPOLOGIA             \n"
          + "   AND  EXISTS (                                            	\n"
          + "       SELECT                                                  \n"
          + "           A.EXT_ID_FG_TIPOLOGIA                               \n"
          + "       FROM                                                    \n"
          + "           NEMBO_R_LIV_BANDO_BENEFICIARIO A                      \n"
          + "       WHERE                                                   \n"
          + "           A.ID_BANDO = RB.ID_BANDO                            \n"
          + "           AND A.ID_LIVELLO = RL.ID_LIVELLO                    \n"
          + "           AND A.EXT_ID_FG_TIPOLOGIA =  RL.EXT_ID_FG_TIPOLOGIA \n"
          + "   )                                                           \n"
          + "  ORDER BY TA.DESC_TIPOLOGIA_AZIENDA , TA.DESC_FORMA_GIURIDICA \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      return queryForList(QUERY, mapParameterSource, BeneficiarioDTO.class);
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

  public void inserRLivBandoBeneficiario(long idBando, long idLivello,
      long idFgTipologia) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRQuadroOggetto";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT INTO                    												\n"
          + "   NEMBO_R_LIV_BANDO_BENEFICIARIO	(ID_LIVELLO, ID_BANDO, EXT_ID_FG_TIPOLOGIA)	\n"
          + " VALUES    								 									\n"
          + "  ( :ID_LIVELLO,   															\n"
          + "    :ID_BANDO, 																\n"
          + "    :EXT_ID_FG_TIPOLOGIA) 														\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("EXT_ID_FG_TIPOLOGIA", idFgTipologia,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBando", idBando),
              new LogParameter("idLivello", idLivello),
              new LogParameter("idFgTipologia", idFgTipologia)
          },
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

  public List<LivelloDTO> getLivelliBandoFormeGiuridiche(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getLivelliBandoFormeGiuridiche";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                                  \n"
          + "   L.ID_LIVELLO ID_LIVELLO,"
          + "   L.DESCRIZIONE  DESCRIZIONE,"
          + "   L.CODICE CODICE              \n"
          + " FROM                                      \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,					\n"
          + "   NEMBO_R_LIVELLO_FG_TIPOLOGIA LT,					\n"
          + "	NEMBO_D_LIVELLO L                  	    \n"
          + " WHERE                                     \n"
          + "   LB.ID_BANDO = :ID_BANDO					\n"
          + "  AND LT.ID_LIVELLO = LB.ID_LIVELLO					\n"
          + " AND 										\n"
          + "	L.ID_LIVELLO  = LB.ID_LIVELLO			\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<LivelloDTO>>()
          {
            @Override
            public List<LivelloDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              List<LivelloDTO> livelli = new LinkedList<LivelloDTO>();
              LivelloDTO l = null;
              while (rs.next())
              {
                l = new LivelloDTO();

                l.setIdLivello(rs.getLong("ID_LIVELLO"));
                l.setDescrizione(rs.getString("DESCRIZIONE"));
                l.setCodice(rs.getString("CODICE"));
                livelli.add(l);
              }
              if (livelli.isEmpty())
                return null;

              return livelli;
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

  public void deleteRLivBandoBeneficiario(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRLivBandoBeneficiario";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                   			\n"
          + "   NEMBO_R_LIV_BANDO_BENEFICIARIO	\n"
          + " WHERE 							\n"
          + "   ID_BANDO = :ID_BANDO    		\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBando", idBando)
          },
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

  public List<QuadroOggettoVO> getElencoQuadroOggettoNemboconf()
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoQuadroOggettoNemboconf";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                            \n"
          + "   A.ID_QUADRO_OGGETTO,            \n"
          + "   A.ID_QUADRO,                    \n"
          + "   A.ID_OGGETTO,                   \n"
          + "   A.FLAG_OBBLIGATORIO,            \n"
          + "   B.CODICE AS COD_QUADRO,         \n"
          + "   B.DESCRIZIONE AS DESCR_QUADRO,  \n"
          + "   C.CODICE AS COD_OGGETTO,        \n"
          + "   C.DESCRIZIONE AS DESCR_OGGETTO  \n"
          + " FROM                              \n"
          + "   NEMBO_R_QUADRO_OGGETTO A,         \n"
          + "   NEMBO_D_QUADRO B,                 \n"
          + "   NEMBO_D_OGGETTO C                 \n"
          + " WHERE                             \n"
          + "   A.ID_QUADRO = B.ID_QUADRO       \n"
          + "   AND A.ID_OGGETTO = C.ID_OGGETTO \n"
          + " ORDER BY                          \n"
          + "       B.DESCRIZIONE,              \n"
          + "       C.DESCRIZIONE               \n"
          + "                                   \n";
      return queryForList(QUERY, mapParameterSource, QuadroOggettoVO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, null,
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

  public List<ControlloDTO> getElencoQuadriControlliDisponibili(
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoQuadriControlliDisponibili";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = "SELECT "
          + "  A.ID_CONTROLLO,"
          + "  A.CODICE,"
          + "  A.DESCRIZIONE, "
          + "  A.CODICE || ' - ' || A.DESCRIZIONE AS DESCR_ESTESA "
          + "FROM "
          + "	NEMBO_D_CONTROLLO A"
          + " WHERE "
          + " NOT EXISTS (SELECT B.ID_QUADRO_OGGETTO FROM NEMBO_R_QUADRO_OGGETTO_CONTROL B WHERE B.ID_CONTROLLO = A.ID_CONTROLLO AND B.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO) "
          + " ORDER BY A. DESCRIZIONE	 \n";

      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto);
      return queryForList(QUERY, mapParameterSource, ControlloDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          null,
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

  public List<ControlloDTO> getElencoQuadriControlliSelezionati(
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoQuadriControlliSelezionati";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT "
          + "  B.ID_QUADRO_OGGETTO_CONTROLLO,"
          + "  A.ID_CONTROLLO,"
          + "  A.CODICE,"
          + "  A.DESCRIZIONE, "
          + "  B.GRAVITA, "
          + "  B.FLAG_OBBLIGATORIO, "
          + "  A.CODICE || ' - ' || A.DESCRIZIONE AS DESCR_ESTESA"
          + " FROM "
          + "	 NEMBO_R_QUADRO_OGGETTO_CONTROL B,"
          + "	 NEMBO_D_CONTROLLO A"
          + " WHERE"
          + "	 A.ID_CONTROLLO = B.ID_CONTROLLO"
          + "	AND B.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO"
          + " ORDER BY A.CODICE";

      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto);
      return queryForList(QUERY, mapParameterSource, ControlloDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          null,
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

  public void deleteRQuadroOggettoControllo(long idControllo,
      long idQuadroOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRQuadroOggettoControllo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                    			 \n"
          + "   NEMBO_R_QUADRO_OGGETTO_CONTROL		 		 \n"
          + " WHERE    								 \n"
          + "  ID_QUADRO_OGGETTO= :ID_QUADRO_OGGETTO   \n"
          + "  AND ID_CONTROLLO = :ID_CONTROLLO   	 \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadroOggetto", idQuadroOggetto),
              new LogParameter("idControllo", idControllo)
          },
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

  public void insertRQuadroOggettoControllo(long idQuadroOggetto,
      long idControllo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRQuadroOggettoControllo";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT INTO                    			 \n"
          + "   NEMBO_R_QUADRO_OGGETTO_CONTROL	(ID_CONTROLLO, FLAG_OBBLIGATORIO, GRAVITA, ID_QUADRO_OGGETTO, ID_QUADRO_OGGETTO_CONTROLLO)	 		 \n"
          + " VALUES    								 \n"
          + "  ( :ID_CONTROLLO, 'N', 'W',   \n"
          + "    :ID_QUADRO_OGGETTO,"
          + "    (select NVL(max(ID_QUADRO_OGGETTO_CONTROLLO),0)+1 from NEMBO_R_QUADRO_OGGETTO_CONTROL ) )\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadroOggetto", idQuadroOggetto),
              new LogParameter("idControllo", idControllo)
          },
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

  public void updateRQuadroOggettoControllu(long idQuadroOggettoControllo,
      String flagObbligatorio, String gravita)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateRQuadroOggettoControllu";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " UPDATE                    			 	\n"
          + "   NEMBO_R_QUADRO_OGGETTO_CONTROL		 	\n"
          + " SET     								 	\n"
          + "  FLAG_OBBLIGATORIO = :FLAG_OBBLIGATORIO, 	\n"
          + "  GRAVITA = :GRAVITA 						\n"
          + " WHERE  									\n"
          + "  ID_QUADRO_OGGETTO_CONTROLLO = :ID_QUADRO_OGGETTO_CONTROLLO \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_QUADRO_OGGETTO_CONTROLLO",
          idQuadroOggettoControllo, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_OBBLIGATORIO", flagObbligatorio,
          Types.VARCHAR);
      mapParameterSource.addValue("GRAVITA", gravita, Types.VARCHAR);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idQuadroOggettoControllo",
                  idQuadroOggettoControllo),
              new LogParameter("flagObbligatorio", flagObbligatorio),
              new LogParameter("gravita", gravita)
          },
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

  public List<LivelloDTO> getElencoLivelliDisponibili(long idBandoOggetto,
      long idQuadroOggControlloAmm) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoLivelliDisponibili";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DL.CODICE, DL.ID_LIVELLO, DL.DESCRIZIONE, DL.CODICE_LIVELLO         \n"
          + "   FROM NEMBO_D_LIVELLO DL , NEMBO_R_LIVELLO_BANDO LB, NEMBO_R_BANDO_OGGETTO BO   \n"
          + "  WHERE 																	 \n"
          + "  BO.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO									 \n"
          + "  AND BO.ID_BANDO = LB.ID_BANDO 											 \n"
          + "  AND LB.ID_LIVELLO = DL.ID_LIVELLO 										 \n"
          + " AND NOT EXISTS                                                          	 \n"
          + "  (SELECT ID_LIVELLO                                                        \n"
          + "           FROM NEMBO_R_BAN_OG_QUA_CON_AMM_LIV                              \n"
          + "          WHERE ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                        \n"
          + "            AND ID_QUADRO_OGG_CONTROLLO_AMM = :ID_QUADRO_OGG_CONTROLLO_AMM  \n"
          + "			 AND ID_LIVELLO = DL.ID_LIVELLO) 								 \n"
          + "  ORDER BY DL.ORDINAMENTO                                                   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggControlloAmm, Types.NUMERIC);

      return queryForList(SELECT, parameterSource, LivelloDTO.class);
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

  public List<LivelloDTO> getElencoLivelliSelezionati(long idBandoOggetto,
      long idQuadroOggControlloAmm) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoLivelliSelezionati";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DL.CODICE, DL.ID_LIVELLO, DL.DESCRIZIONE, DL.CODICE_LIVELLO         \n"
          + "   FROM NEMBO_D_LIVELLO DL                                                    \n"
          + "  WHERE EXISTS                                                              \n"
          + "  (SELECT ID_LIVELLO                                                        \n"
          + "           FROM NEMBO_R_BAN_OG_QUA_CON_AMM_LIV                              \n"
          + "          WHERE ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                        \n"
          + "            AND ID_QUADRO_OGG_CONTROLLO_AMM = :ID_QUADRO_OGG_CONTROLLO_AMM  \n"
          + "			 AND ID_LIVELLO = DL.ID_LIVELLO) 								 \n"
          + "  ORDER BY DL.ORDINAMENTO                                                   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggControlloAmm, Types.NUMERIC);

      return queryForList(SELECT, parameterSource, LivelloDTO.class);
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

  public void insertLivelloContrAmm(long idBandoOggetto,
      long idQuadroOggControlloAmm, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertLivelloContrAmm";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT INTO                    			 													\n"
          + "   NEMBO_R_BAN_OG_QUA_CON_AMM_LIV	(ID_BANDO_OGGETTO, ID_QUADRO_OGG_CONTROLLO_AMM, ID_LIVELLO)	\n"
          + " VALUES    								 													\n"
          + "  ( :ID_BANDO_OGGETTO,   																		\n"
          + "    :ID_QUADRO_OGG_CONTROLLO_AMM, 																\n"
          + "    :ID_LIVELLO) 																				\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggControlloAmm, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("idLivello", idLivello),
              new LogParameter("idQuadroOggControlloAmm",
                  idQuadroOggControlloAmm)
          },
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

  public void deleteLivelloContrAmm(long idBandoOggetto,
      long idQuadroOggControlloAmm, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteLivelloContrAmm";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE	                   			 							\n"
          + "   NEMBO_R_BAN_OG_QUA_CON_AMM_LIV 									\n"
          + " WHERE    								 							\n"
          + "  ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO   							\n"
          + "  AND ID_QUADRO_OGG_CONTROLLO_AMM = :ID_QUADRO_OGG_CONTROLLO_AMM 	\n"
          + "  AND ID_LIVELLO = :ID_LIVELLO 									\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggControlloAmm, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("idLivello", idLivello),
              new LogParameter("idQuadroOggControlloAmm",
                  idQuadroOggControlloAmm)
          },
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

  public List<DecodificaDTO<String>> getElencoPlaceholder()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoPlaceholder";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " WITH SEGNAPOSTI AS                                                \n"
          + " (                                                                 \n"
          + "   SELECT                                                          \n"
          + "   A.ID_SEGNAPOSTO AS ID,                                          \n"
          + "   REPLACE(REPLACE(A.COD_SEGNAPOSTO,'<<','$$'),'>>','') AS CODICE, \n"
          + "   A.DESCRIZIONE                                                   \n"
          + " FROM                                                              \n"
          + "   DB_D_SEGNAPOSTO A                                               \n"
          + " UNION                                                             \n"
          + " SELECT                                                            \n"
          + "   B.ID_SEGNAPOSTO AS ID,                                          \n"
          + "   B.NOME AS CODICE,                                               \n"
          + "   B.DESCRIZIONE AS DESCRIZIONE                                    \n"
          + " FROM                                                              \n"
          + "   NEMBO_D_SEGNAPOSTO B                                              \n"
          + " )                                                                 \n"
          + " SELECT * FROM SEGNAPOSTI ORDER BY DESCRIZIONE                     \n";

      return queryForDecodificaString(SELECT, null);
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

  public void deleteRBandoOggContrAmmLiv(long idBandoOggetto,
      long idQuadroOggettoContrAmm) throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRBandoOggContrAmmLiv";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       												\n"
          + "   NEMBO_R_BAN_OG_QUA_CON_AMM_LIV											\n"
          + "WHERE                   													\n"
          + "  ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO 									\n"
          + "  AND ID_QUADRO_OGG_CONTROLLO_AMM = :ID_QUADRO_OGG_CONTROLLO_AMM        	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggettoContrAmm, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("idQuadroOggettoContrAmm",
                  idQuadroOggettoContrAmm)
          },
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

  public void deleteRBandoOggContrAmm(long idBandoOggetto,
      long idQuadroOggettoContrAmm) throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRBandoOggContrAmm";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       												\n"
          + "   NEMBO_R_BAND_OG_QUAD_CONTR_AMM											\n"
          + "WHERE                   													\n"
          + "  ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO 									\n"
          + "  AND ID_QUADRO_OGG_CONTROLLO_AMM = :ID_QUADRO_OGG_CONTROLLO_AMM        	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          idQuadroOggettoContrAmm, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("idQuadroOggettoContrAmm",
                  idQuadroOggettoContrAmm)
          },
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

  public Boolean existRBandoOggQuadContrAmmLiv(long idBandoOggetto,
      long IdQuadroOggControlloAmm) throws InternalUnexpectedException
  {
    String THIS_METHOD = "existRBandoOggQuadContrAmmLiv";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                            \n"
          + "  A.ID_BANDO_OGGETTO                                               \n"
          + " FROM                                                              \n"
          + "  NEMBO_R_BAN_OG_QUA_CON_AMM_LIV A                                 \n"
          + " WHERE                                                             \n"
          + "  A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                           \n"
          + "  AND A.ID_QUADRO_OGG_CONTROLLO_AMM = :ID_QUADRO_OGG_CONTROLLO_AMM \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          IdQuadroOggControlloAmm, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return Boolean.TRUE;
              }
              return Boolean.FALSE;
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

  public Boolean existRBandoOggQuadContrAmm(long idBandoOggetto,
      long IdQuadroOggControlloAmm) throws InternalUnexpectedException
  {
    String THIS_METHOD = "existRBandoOggQuadContrAmm";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                            \n"
          + "  A.ID_BANDO_OGGETTO                                               \n"
          + " FROM                                                              \n"
          + "  NEMBO_R_BAND_OG_QUAD_CONTR_AMM A                                 \n"
          + " WHERE                                                             \n"
          + "  A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                           \n"
          + "  AND A.ID_QUADRO_OGG_CONTROLLO_AMM = :ID_QUADRO_OGG_CONTROLLO_AMM \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("ID_QUADRO_OGG_CONTROLLO_AMM",
          IdQuadroOggControlloAmm, Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return Boolean.TRUE;
              }
              return Boolean.FALSE;
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

  public void deleteControlliPadriSenzaFigli(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteControlliPadriSenzaFigli";
    String DELETE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      DELETE = " DELETE                                                                               \n"
          + "   NEMBO_R_BAND_OG_QUAD_CONTR_AMM CA                                                  \n"
          + " WHERE                                                                                \n"
          + "   CA.ID_QUADRO_OGG_CONTROLLO_AMM IN (                                                \n"
          + "  SELECT                                                                              \n"
          + "      E.ID_QUADRO_OGG_CONTROLLO_AMM                                                   \n"
          + "  FROM                                                                                \n"
          + "      NEMBO_R_BAND_OG_QUAD_CONTR_AMM E,                                               \n"
          + "      NEMBO_R_QUADRO_OGG_CONTROL_AMM F,                                               \n"
          + "      NEMBO_D_CONTROLLO_AMMINISTRATI D                                                \n"
          + "   WHERE                                                                              \n"
          + "       E.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                                         \n"
          + "      AND E.ID_QUADRO_OGG_CONTROLLO_AMM = F.ID_QUADRO_OGG_CONTROLLO_AMM               \n"
          + "      AND D.ID_CONTROLLO_AMMINISTRATIVO = F.ID_CONTROLLO_AMMINISTRATIVO               \n"
          + "      AND D.ID_CONTROLLO_AMMINISTRAT_PADRE IS NULL                                    \n"
          + "      AND NOT EXISTS                                                                  \n"
          + "      (                                                                               \n"
          + "            SELECT                                                                    \n"
          + "                D1.ID_CONTROLLO_AMMINISTRATIVO                                        \n"
          + "            FROM                                                                      \n"
          + "                NEMBO_R_BAND_OG_QUAD_CONTR_AMM E1,                                    \n"
          + "                NEMBO_R_QUADRO_OGG_CONTROL_AMM F1,                                    \n"
          + "                NEMBO_D_CONTROLLO_AMMINISTRATI D1                                     \n"
          + "             WHERE                                                                    \n"
          + "                 E1.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                              \n"
          + "                AND E1.ID_QUADRO_OGG_CONTROLLO_AMM = F1.ID_QUADRO_OGG_CONTROLLO_AMM   \n"
          + "                AND D1.ID_CONTROLLO_AMMINISTRATIVO = F1.ID_CONTROLLO_AMMINISTRATIVO   \n"
          + "                AND D1.ID_CONTROLLO_AMMINISTRAT_PADRE = D.ID_CONTROLLO_AMMINISTRATIVO \n"
          + "      )                                                                               \n"
          + " )                                                                                    \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto)
          },
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

  public long importaControlliInLoco(long idBandoOggettoMaster,
      long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "importaControlliInLoco";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT INTO NEMBO_R_BAND_OG_QUAD_CONTR_AMM                          \n"
          + "      (ID_BANDO_OGGETTO,ID_QUADRO_OGG_CONTROLLO_AMM )                \n"
          + " SELECT                                                              \n"
          + "   :ID_BANDO_OGGETTO, A.ID_QUADRO_OGG_CONTROLLO_AMM                  \n"
          + " FROM                                                                \n"
          + "   NEMBO_R_QUADRO_OGG_CONTROL_AMM A,                                 \n"
          + "   NEMBO_R_BAND_OG_QUAD_CONTR_AMM B                                  \n"
          + " WHERE                                                               \n"
          + "   A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO                          \n"
          + "   AND A.ID_QUADRO_OGG_CONTROLLO_AMM = B.ID_QUADRO_OGG_CONTROLLO_AMM \n"
          + "   AND B.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO_MASTER                 \n"
          + "   AND SYSDATE <= NVL(A.DATA_FINE_VALIDITA,SYSDATE)                  \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO_MASTER",
          idBandoOggettoMaster, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_BANDO_OGGETTO_MASTER", idBandoOggettoMaster),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto)
          },
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

  public long deleteControlliInLoco(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteControlliInLoco";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                    			 		 \n"
          + "   NEMBO_R_BAND_OG_QUAD_CONTR_AMM 		 		 \n"
          + " WHERE    								 		 \n"
          + "  ID_BANDO_OGGETTO= :ID_BANDO_OGGETTO   		 \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
      return idBandoOggetto;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
          },
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

  public Boolean isOggettoIstanza(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isOggettoIstanza";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                        \n"
          + "   C.ID_OGGETTO                                                \n"
          + " FROM                                                          \n"
          + "   NEMBO_R_BANDO_OGGETTO A,                                      \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                              \n"
          + "   NEMBO_D_OGGETTO C                                             \n"
          + " WHERE                                                         \n"
          + "   A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                      \n"
          + "   AND A.ID_LEGAME_GRUPPO_OGGETTO = B.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND B.ID_OGGETTO = C.ID_OGGETTO                             \n"
          + "   AND C.FLAG_ISTANZA = 'S'                                    \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return Boolean.TRUE;
              }
              return Boolean.FALSE;
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

  public void insertRElencoQueryBando(long idBando, long idBandoMaster)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRElencoQueryBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT INTO                                \n"
          + "   NEMBO_R_ELENCO_QUERY_BANDO                 \n"
          + "   (ID_ELENCO_QUERY, ID_BANDO, ORDINAMENTO, \n"
          + "   EXT_COD_ATTORE, FLAG_VISIBILE)           \n"
          + " SELECT                                     \n"
          + "   A.ID_ELENCO_QUERY,                       \n"
          + "   :ID_BANDO,                               \n"
          + "   A.ORDINAMENTO,                           \n"
          + "   A.EXT_COD_ATTORE,                        \n"
          + "   'N'				                         \n"
          + " FROM                                       \n"
          + "   NEMBO_R_ELENCO_QUERY_BANDO A               \n"
          + " WHERE                                      \n"
          + "   A.ID_BANDO = :ID_BANDO_MASTER            \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_MASTER", idBandoMaster,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_BANDO_MASTER", idBandoMaster)
          },
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

  public void deleteRElencoQueryBando(long idBando, long idBandoMaster)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRElencoQueryBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                                           \n"
          + "   NEMBO_R_ELENCO_QUERY_BANDO A                     \n"
          + " WHERE                                            \n"
          + "   A.ID_BANDO = :ID_BANDO                         \n"
          + "   AND EXISTS (                                   \n"
          + "       SELECT                                     \n"
          + "         B.ID_ELENCO_QUERY                        \n"
          + "       FROM                                       \n"
          + "        NEMBO_R_ELENCO_QUERY_BANDO B                \n"
          + "       WHERE                                      \n"
          + "        B.ID_BANDO = :ID_BANDO_MASTER             \n"
          + "        AND A.EXT_COD_ATTORE = B.EXT_COD_ATTORE   \n"
          + "        AND A.ID_ELENCO_QUERY = B.ID_ELENCO_QUERY \n"
          + "   )                                              \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_MASTER", idBandoMaster,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_BANDO_MASTER", idBandoMaster)
          },
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

  //TODO: FIXME: è stata rimossa la DESCR_ATTORE in quanto presa da PAPUA_D_ATTORE
  public List<GraficoVO> elencoQueryBando(long idBando, boolean flagElenco)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "elencoQueryBando";
    StringBuffer SELECT = new StringBuffer();
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT.append(
          "  SELECT                                                     \n"
              + "   A.ID_ELENCO_QUERY,                                        \n"
              + "   B.DESCRIZIONE_BREVE,                                      \n"
              + "   B.DESCRIZIONE_ESTESA,                                     \n"
              + "   LENGTH(B.EXCEL_TEMPLATE) AS LENGTH_EXCEL_TEMPLATE,      \n"
              + "   A.EXT_COD_ATTORE,                                         \n"
              + "   C.DESCRIZIONE AS DESCR_TIPO_GRAFICO,                      \n"
              + "   A.FLAG_VISIBILE,                                          \n"
              + "   '' AS DESCR_ATTORE"
            //  + "   ATT.DESC_ATTORE AS DESCR_ATTORE                           \n"
              + "  FROM                                                       \n"
              + "   NEMBO_R_ELENCO_QUERY_BANDO A,                               \n"
              + "   NEMBO_D_ELENCO_QUERY B,                                     \n"
              + "   NEMBO_D_TIPO_VISUALIZZAZIONE C                            \n"
             // + "   PAPUA_D_ATTORE ATT                                        \n"
              + "  WHERE                                                      \n"
              + "   A.ID_BANDO = :ID_BANDO                                    \n"
              + "   AND  A.ID_ELENCO_QUERY = B.ID_ELENCO_QUERY                \n"
              + "   AND B.ID_TIPO_VISUALIZZAZIONE = C.ID_TIPO_VISUALIZZAZIONE \n")
              //+ "   AND ATT.COD_ATTORE = A.EXT_COD_ATTORE                      \n")
              ;
      if (flagElenco)
      {
        SELECT.append("  AND C.FLAG_ELENCO = 'S'                        \n");
      }
      else
      {
        SELECT.append("  AND C.FLAG_ELENCO = 'N'                        \n");
      }
      SELECT.append(" ORDER BY A.ID_ELENCO_QUERY, A.ORDINAMENTO            \n");

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT.toString(),
          parameterSource, new ResultSetExtractor<List<GraficoVO>>()
          {
            @Override
            public List<GraficoVO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<GraficoVO> res = null;
              List<AttoreVO> attori = null;
              GraficoVO item = null;
              long idquery = 0;
              long idqueryLast = 0;
              while (rs.next())
              {
                if (res == null)
                {
                  res = new Vector<GraficoVO>();
                }
                idqueryLast = rs.getLong("ID_ELENCO_QUERY");
                if (idquery != idqueryLast)
                {
                  idquery = idqueryLast;
                  item = new GraficoVO();
                  item.setIdElencoQuery(idqueryLast);
                  item.setExcelTemplate(
                      rs.getLong("LENGTH_EXCEL_TEMPLATE") > 0);
                  item.setDescrBreve(rs.getString("DESCRIZIONE_BREVE"));
                  item.setDescrEstesa(rs.getString("DESCRIZIONE_ESTESA"));
                  item.setFlagVisibile(rs.getString("FLAG_VISIBILE"));
                  item.setDescrTipoVisualizzazione(
                      rs.getString("DESCR_TIPO_GRAFICO"));
                  attori = new ArrayList<AttoreVO>();
                  item.setAttori(attori);
                  res.add(item);
                }
                attori.add(new AttoreVO(rs.getString("EXT_COD_ATTORE"),
                    rs.getString("DESCR_ATTORE")));
              }
              return res;
            }
          });
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

  public void updateRElencoQueryBando(long idBando, String flagVisibile)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateRElencoQueryBando";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      UPDATE = " UPDATE                                                   \n"
          + "   NEMBO_R_ELENCO_QUERY_BANDO                               \n"
          + " SET                                                      \n"
          + "   FLAG_VISIBILE = :FLAG_VISIBILE                         \n"
          + " WHERE                                                    \n"
          + "   ID_BANDO = :ID_BANDO                                   \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_VISIBILE", flagVisibile, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("FLAG_VISIBILE", flagVisibile),
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

  public void updateRElencoQueryBando(long idBando, String flagVisibile,
      Long[] idsElencoQuery) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateRElencoQueryBando";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      UPDATE = " UPDATE                                                   \n"
          + "   NEMBO_R_ELENCO_QUERY_BANDO                               \n"
          + " SET                                                      \n"
          + "   FLAG_VISIBILE = :FLAG_VISIBILE                         \n"
          + " WHERE                                                    \n"
          + "   ID_BANDO = :ID_BANDO                                   \n"
          + getInCondition("ID_ELENCO_QUERY",
              new Vector<Long>(Arrays.asList(idsElencoQuery)));

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_VISIBILE", flagVisibile, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("FLAG_VISIBILE", flagVisibile),
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

  public List<DecodificaDTO<Long>> getElencoDocumentiBandoOggetto(
      long idBandoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoDocumentiBandoOggetto";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT                                \n"
          + "   EC.ID_ELENCO_CDU      AS ID,                 \n"
          + "   EC.NOME_DOCUMENTO_CDU AS DESCRIZIONE,        \n"
          + "   EC.CODICE_CDU         AS CODICE              \n"
          + " FROM                                           \n"
          + "   NEMBO_D_GRUPPO_TESTO_VERBALE GTV,              \n"
          + "   NEMBO_D_ELENCO_CDU EC                          \n"
          + " WHERE                                          \n"
          + "   GTV.ID_ELENCO_CDU        = EC.ID_ELENCO_CDU  \n"
          + "   AND GTV.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO \n"
          + " ORDER BY                                       \n"
          + "   EC.NOME_DOCUMENTO_CDU                        \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return queryForDecodificaLong(SELECT, parameterSource);
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

  public List<GruppoTestoVerbaleDTO> getGruppiTestoVerbale(long idBandoOggetto,
      long idElencoCdu, String flagVisible, String flagCatalogo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getGruppiTestoVerbale";
    String QUERY = " SELECT                                                       \n"
        + "   GTV.ID_GRUPPO_TESTO_VERBALE,                                            \n"
        + "   GTV.TIPO_COLLOCAZIONE_TESTO,                                            \n"
        + "   STV.DESCRIZIONE AS DESC_GRUPPO_TESTO_VERBALE,                           \n"
        + "   STV.FLAG_VISIBILE,                                                      \n"
        + "   TV.ID_TESTO_VERBALE,                                                    \n"
        + "   TV.DESCRIZIONE AS DESC_TESTO_VERBALE,                                   \n"
        + "   TV.FLAG_CATALOGO,                                                       \n"
        + "   TV.ORDINE                                                               \n"
        + " FROM                                                                      \n"
        + "   NEMBO_D_STRUTT_TESTO_VERBALE STV,                                         \n"
        + "   NEMBO_D_GRUPPO_TESTO_VERBALE GTV                                          \n"
        + " LEFT OUTER JOIN NEMBO_D_TESTO_VERBALE TV                                    \n"
        + " ON                                                                        \n"
        + "   GTV.ID_GRUPPO_TESTO_VERBALE = TV.ID_GRUPPO_TESTO_VERBALE                \n"
        + "   AND TV.FLAG_CATALOGO  = NVL(:FLAG_CATALOGO,TV.FLAG_CATALOGO)            \n"
        + " WHERE                                                                     \n"
        + "   GTV.ID_BANDO_OGGETTO            = :ID_BANDO_OGGETTO                     \n"
        + "   AND GTV.ID_ELENCO_CDU           = :ID_ELENCO_CDU                        \n"
        + "   AND STV.ID_ELENCO_CDU           = GTV.ID_ELENCO_CDU                     \n"
        + "   AND STV.TIPO_COLLOCAZIONE_TESTO = GTV.TIPO_COLLOCAZIONE_TESTO           \n"
        + "   AND STV.FLAG_VISIBILE           = NVL(:FLAG_VISIBILE, STV.FLAG_VISIBILE)\n"
        + " ORDER BY                                                                  \n"
        + "   GTV.TIPO_COLLOCAZIONE_TESTO,                                            \n"
        + "   TV.ORDINE                                                               \n";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_ELENCO_CDU", idElencoCdu, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_VISIBILE", flagVisible, Types.VARCHAR);
      mapParameterSource.addValue("FLAG_CATALOGO", flagCatalogo, Types.VARCHAR);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoTestoVerbaleDTO>>()
          {
            @Override
            public List<GruppoTestoVerbaleDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<GruppoTestoVerbaleDTO> list = new ArrayList<GruppoTestoVerbaleDTO>();
              long lastIdGruppoTestoVerbale = Long.MIN_VALUE;
              GruppoTestoVerbaleDTO gruppoTestoVerbaleDTO = null;
              while (rs.next())
              {
                long idGruppoTestoVerbale = rs
                    .getLong("ID_GRUPPO_TESTO_VERBALE");
                if (lastIdGruppoTestoVerbale != idGruppoTestoVerbale)
                {
                  gruppoTestoVerbaleDTO = new GruppoTestoVerbaleDTO();
                  gruppoTestoVerbaleDTO
                      .setIdGruppoTestoVerbale(idGruppoTestoVerbale);
                  gruppoTestoVerbaleDTO.setDescGruppoTestoVerbale(
                      rs.getString("DESC_GRUPPO_TESTO_VERBALE"));
                  gruppoTestoVerbaleDTO
                      .setFlagVisibile(rs.getString("FLAG_VISIBILE"));
                  gruppoTestoVerbaleDTO.setTipoCollocazioneTesto(
                      rs.getInt("TIPO_COLLOCAZIONE_TESTO"));
                  list.add(gruppoTestoVerbaleDTO);
                  lastIdGruppoTestoVerbale = idGruppoTestoVerbale;
                }
                BigDecimal idTestoVerbale = rs
                    .getBigDecimal("ID_TESTO_VERBALE");
                // Devo verificare che ci sia effettivamente del testo, possono
                // esserci gruppi senza alcun testo e quindi verifico che i dati
                // del testo non
                // siano dovuti all'outer join
                if (idTestoVerbale != null)
                {
                  // ID_TESTO_VERBALE è valorizzato, quindi è un dato valido,
                  // non proveniente da una outer join
                  TestoVerbaleDTO testoVerbaleDTO = new TestoVerbaleDTO();
                  testoVerbaleDTO
                      .setDescrizione(rs.getString("DESC_TESTO_VERBALE"));
                  testoVerbaleDTO
                      .setFlagCatalogo(rs.getString("FLAG_CATALOGO"));
                  testoVerbaleDTO.setIdTestoVerbale(idTestoVerbale.intValue());
                  testoVerbaleDTO.setOrdine(rs.getInt("ORDINE"));
                  gruppoTestoVerbaleDTO.addTestoVerbale(testoVerbaleDTO);
                }
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, QUERY);
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

  public List<TestoVerbaleDTO> insertTestiVerbali(
      List<DecodificaDTO<Long>> testi) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertTestiVerbali";
    final String INSERT = " INSERT                                                        \n"
        + " INTO                                                          \n"
        + "   NEMBO_D_TESTO_VERBALE                                         \n"
        + "   (                                                           \n"
        + "     ID_TESTO_VERBALE,                                         \n"
        + "     ID_GRUPPO_TESTO_VERBALE,                                  \n"
        + "     DESCRIZIONE,                                              \n"
        + "     FLAG_CATALOGO,                                            \n"
        + "     ORDINE                                                    \n"
        + "   )                                                           \n"
        + "   VALUES                                                      \n"
        + "   (                                                           \n"
        + "     :ID_TESTO_VERBALE,      			                      \n"
        + "     :ID_GRUPPO_TESTO_VERBALE,                                 \n"
        + "     :DESCRIZIONE,                                             \n"
        + "     'N',                                                      \n"
        + "     (                                                         \n"
        + "       SELECT                                                  \n"
        + "         NVL(MAX(TV.ORDINE),0)+:ORDINE                         \n"
        + "       FROM                                                    \n"
        + "         NEMBO_D_TESTO_VERBALE TV                                \n"
        + "       WHERE                                                   \n"
        + "         TV.ID_GRUPPO_TESTO_VERBALE = :ID_GRUPPO_TESTO_VERBALE \n"
        + "     )                                                         \n"
        + "   )                                                           \n";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    MapSqlParameterSource[] batchParameters = null;
    try
    {
      int size = testi.size();
      batchParameters = new MapSqlParameterSource[size];
      List<TestoVerbaleDTO> testiInseriti = new ArrayList<>();
      for (int i = 0; i < size; ++i)
      {

        MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
        DecodificaDTO<Long> testo = testi.get(i);
        batchParameters[i] = mapParameterSource;
        Long idTestoVerbale = getNextSequenceValue("SEQ_NEMBO_D_TESTO_VERBALE");
        mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE", testo.getId(),
            Types.NUMERIC);
        mapParameterSource.addValue("ID_TESTO_VERBALE", idTestoVerbale,
            Types.NUMERIC);

        mapParameterSource.addValue("DESCRIZIONE", testo.getDescrizione(),
            Types.VARCHAR);
        Integer ordine = Integer.valueOf(10 * i);
        mapParameterSource.addValue("ORDINE", ordine, Types.VARCHAR);

        TestoVerbaleDTO t = new TestoVerbaleDTO();
        t.setIdGruppo(testo.getId());
        t.setDescrizione(testo.getDescrizione());
        t.setIdTestoVerbale(idTestoVerbale);
        t.setFlagCatalogo("N");
        t.setOrdine(ordine);
        testiInseriti.add(t);
      }
      namedParameterJdbcTemplate.batchUpdate(INSERT, batchParameters);
      return testiInseriti;
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("testi", testi)
          },
          new LogVariable[]
          {}, INSERT, batchParameters);
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

  public void deleteTestiVerbaleModificabili(long idBandoOggetto,
      long idElencoCdu) throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteTestiVerbaleModificabili";
    String QUERY = " DELETE                                                              \n"
        + " FROM                                                                \n"
        + "   NEMBO_D_TESTO_VERBALE TV1                                           \n"
        + " WHERE                                                               \n"
        + "   TV1.ID_TESTO_VERBALE IN                                           \n"
        + "   (                                                                 \n"
        + "     SELECT                                                          \n"
        + "       TV.ID_TESTO_VERBALE                                           \n"
        + "     FROM                                                            \n"
        + "       NEMBO_D_GRUPPO_TESTO_VERBALE GTV,                               \n"
        + "       NEMBO_D_TESTO_VERBALE TV,                                       \n"
        + "       NEMBO_D_STRUTT_TESTO_VERBALE STV                                \n"
        + "     WHERE                                                           \n"
        + "       TV.ID_GRUPPO_TESTO_VERBALE      = GTV.ID_GRUPPO_TESTO_VERBALE \n"
        + "       AND GTV.ID_BANDO_OGGETTO        = :ID_BANDO_OGGETTO           \n"
        + "       AND GTV.ID_ELENCO_CDU           = :ID_ELENCO_CDU              \n"
        + "       AND STV.ID_ELENCO_CDU           = GTV.ID_ELENCO_CDU           \n"
        + "       AND STV.TIPO_COLLOCAZIONE_TESTO = GTV.TIPO_COLLOCAZIONE_TESTO \n"
        // + " AND STV.FLAG_VISIBILE = 'S' \n"
        + "       AND TV.FLAG_CATALOGO            = 'N'                         \n"
        + "   )                                                                 \n";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_ELENCO_CDU", idElencoCdu, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, QUERY);
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

  public boolean isBandoOggettoModificabile(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isBandoOggettoModificabile";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                               \n"
          + "   COUNT(*)                                                           \n"
          + " FROM                                                                 \n"
          + "   NEMBO_R_BANDO_OGGETTO A                                              \n"
          + " WHERE                                                                \n"
          + "   A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                             \n"
          + "   AND A.FLAG_ATTIVO <> 'S'                                            \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.queryForLong(SELECT,
          parameterSource) != 0;
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

  public List<DecodificaDTO<String>> getElencoPlaceholderNemboconf()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoPlaceholderNemboconf";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                            \n"
          + "   B.ID_SEGNAPOSTO AS ID,                                          \n"
          + "   B.NOME AS CODICE,                                               \n"
          + "   B.DESCRIZIONE AS DESCRIZIONE                                    \n"
          + " FROM                                                              \n"
          + "   NEMBO_D_SEGNAPOSTO B                                              \n"
          + " ORDER BY DESCRIZIONE                                              \n";

      return queryForDecodificaString(SELECT, null);
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

  public List<GruppoTestoVerbaleDTO> getGruppiTestoVerbale(
      long idBandoOggettoMaster) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getGruppiTestoVerbale";
    String QUERY = " SELECT  distinct                                       \n"
        + "   GTV.ID_GRUPPO_TESTO_VERBALE,                                  \n"
        + "   GTV.TIPO_COLLOCAZIONE_TESTO,                                  \n"
        + "   GTV.ID_ELENCO_CDU,		                                    \n"
        + "   TV.ID_TESTO_VERBALE,                                          \n"
        + "   TV.DESCRIZIONE AS DESC_TESTO_VERBALE,                         \n"
        + "   TV.FLAG_CATALOGO,                                             \n"
        + "   TV.ORDINE                                                     \n"
        + " FROM                                                            \n"
        + "   NEMBO_D_GRUPPO_TESTO_VERBALE GTV,                               \n"
        + "   NEMBO_D_TESTO_VERBALE TV                                        \n"
        + " WHERE                                                           \n"
        + "   TV.ID_GRUPPO_TESTO_VERBALE (+)= GTV.ID_GRUPPO_TESTO_VERBALE   \n"
        + "   AND GTV.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO    				\n"
        + " ORDER BY GTV.ID_GRUPPO_TESTO_VERBALE			                \n";

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggettoMaster,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoTestoVerbaleDTO>>()
          {
            @Override
            public List<GruppoTestoVerbaleDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<GruppoTestoVerbaleDTO> list = new ArrayList<GruppoTestoVerbaleDTO>();
              long lastIdGruppoTestoVerbale = Long.MIN_VALUE;
              GruppoTestoVerbaleDTO gruppoTestoVerbaleDTO = null;
              while (rs.next())
              {
                long idGruppoTestoVerbale = rs
                    .getLong("ID_GRUPPO_TESTO_VERBALE");
                if (lastIdGruppoTestoVerbale != idGruppoTestoVerbale)
                {
                  gruppoTestoVerbaleDTO = new GruppoTestoVerbaleDTO();
                  gruppoTestoVerbaleDTO
                      .setIdGruppoTestoVerbale(idGruppoTestoVerbale);
                  gruppoTestoVerbaleDTO.setTipoCollocazioneTesto(
                      rs.getInt("TIPO_COLLOCAZIONE_TESTO"));
                  gruppoTestoVerbaleDTO
                      .setIdElencoCdu(rs.getLong("ID_ELENCO_CDU"));
                  list.add(gruppoTestoVerbaleDTO);
                  lastIdGruppoTestoVerbale = idGruppoTestoVerbale;
                }
                TestoVerbaleDTO testoVerbaleDTO = new TestoVerbaleDTO();
                testoVerbaleDTO
                    .setDescrizione(rs.getString("DESC_TESTO_VERBALE"));
                testoVerbaleDTO.setFlagCatalogo(rs.getString("FLAG_CATALOGO"));
                testoVerbaleDTO
                    .setIdTestoVerbale(rs.getInt("ID_TESTO_VERBALE"));
                testoVerbaleDTO.setOrdine(rs.getInt("ORDINE"));
                gruppoTestoVerbaleDTO.addTestoVerbale(testoVerbaleDTO);
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, QUERY);
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

  public Long insertGruppoTestoVerbale(long idBandoOggetto, Long idElencoCdu,
      int tipoCollocazioneTesto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertGruppoTestoVerbale";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    Long idGruppoTestoVerbale = null;

    try
    {

      INSERT = " INSERT                       						\n"
          + " INTO                      						\n"
          + "   NEMBO_D_GRUPPO_TESTO_VERBALE       				\n"
          + "   (                        						\n"
          + "     ID_GRUPPO_TESTO_VERBALE,      				\n"
          + "     ID_ELENCO_CDU,      		   				\n"
          + "     ID_BANDO_OGGETTO,      		   				\n"
          + "     TIPO_COLLOCAZIONE_TESTO    	   				\n"
          + "   )                        						\n"
          + "   VALUES                   						\n"
          + "   (                       			 			\n"
          + "     :ID_GRUPPO_TESTO_VERBALE,				 	\n"
          + "     :ID_ELENCO_CDU,      	   					\n"
          + "     :ID_BANDO_OGGETTO,      	   				\n"
          + "     :TIPO_COLLOCAZIONE_TESTO       				\n"
          + "   )                        						\n";

      idGruppoTestoVerbale = getNextSequenceValue(
          "SEQ_NEMBO_D_GRUPPO_TESTO_VERBA");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE",
          idGruppoTestoVerbale);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto);
      mapParameterSource.addValue("ID_ELENCO_CDU", idElencoCdu);
      mapParameterSource.addValue("TIPO_COLLOCAZIONE_TESTO",
          tipoCollocazioneTesto);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("idElencoCdu", idElencoCdu),
              new LogParameter("tipoCollocazioneTesto", tipoCollocazioneTesto)
          },
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
    return idGruppoTestoVerbale;
  }

  public void insertTestiVerbali(long idBandoOggetto, long idGruppoTestoVerbale,
      String descrizione, int ordine,
      String flagCatalogo) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertTestiVerbali";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      INSERT = " INSERT                       						\n"
          + " INTO                      						\n"
          + "   NEMBO_D_TESTO_VERBALE       					\n"
          + "   (                        						\n"
          + "     ID_TESTO_VERBALE,      						\n"
          + "     ID_GRUPPO_TESTO_VERBALE,      				\n"
          + "     DESCRIZIONE,	      		   				\n"
          + "     ORDINE,			      		   				\n"
          + "     FLAG_CATALOGO		    	   				\n"
          + "   )                        						\n"
          + "   VALUES                   						\n"
          + "   (                       			 			\n"
          + "     SEQ_NEMBO_D_TESTO_VERBALE.NEXTVAL, 			\n"
          + "     :ID_GRUPPO_TESTO_VERBALE,      	   			\n"
          + "     :DESCRIZIONE,      	   						\n"
          + "     :ORDINE,      	   							\n"
          + "     :FLAG_CATALOGO       						\n"
          + "   )                        						\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE",
          idGruppoTestoVerbale);
      mapParameterSource.addValue("DESCRIZIONE", descrizione);
      mapParameterSource.addValue("ORDINE", ordine);
      // flagCatalogo="S";
      mapParameterSource.addValue("FLAG_CATALOGO", flagCatalogo);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idGruppoTestoVerbale", idGruppoTestoVerbale),
              new LogParameter("descrizione", descrizione),
              new LogParameter("ordine", ordine),
              new LogParameter("flagCatalogo", flagCatalogo)
          },
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

  public void updateGiustificazioneControllo(long idBandoOggetto,
      long idControllo,
      String flagGiustificabileQuadro) throws InternalUnexpectedException
  {
    String THIS_METHOD = "updateGiustificazioneControllo";
    String UPDATE = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      UPDATE = " UPDATE                    		         						\n"
          + "   NEMBO_R_BANDO_OGGETTO_CONTROLL          						\n"
          + " SET  									 						\n"
          + "    FLAG_GIUSTIFICABILE = :FLAG_GIUSTIFICABILE  	    			\n"
          + " WHERE               					 						\n"
          + "   ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO   						\n"
          + "   AND ID_CONTROLLO = :ID_CONTROLLO   	 						\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_GIUSTIFICABILE",
          flagGiustificabileQuadro, Types.VARCHAR);
      namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
              new LogParameter("ID_CONTROLLO", idControllo),
              new LogParameter("FLAG_GIUSTIFICABILE", flagGiustificabileQuadro)
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

  public List<BandoDTO> getIdElencoBandiDisponibili()
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getIdElencoBandiDisponibili";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                                                             \n"
          + "   A.ID_BANDO                                                                                                                      \n"
          + " FROM                                                                                                                               \n"
          + "   NEMBO_D_BANDO A,                                                                                                                   \n"
          + "   NEMBO_D_TIPO_LIVELLO B,                                                                                                            \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,                                                                                                          \n"
          + "   NEMBO_R_BANDO_MASTER BM,                                                                                                           \n"
          + "   NEMBO_D_LIVELLO L                                                                                                                  \n"
          + " WHERE                                                                                                                              \n"
          + "   B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO                                                                                            \n"
          + "   AND A.FLAG_MASTER = 'N'                                                                                                          \n"
          + "   AND LB.ID_BANDO = A.ID_BANDO                                                                                                     \n"
          + "   AND BM.ID_BANDO = A.ID_BANDO                                                                                                     \n"
          + "   AND L.ID_LIVELLO  = LB.ID_LIVELLO                                                                                                \n"
          + " ORDER BY A.DATA_FINE, A.DENOMINAZIONE, L.CODICE                                                                                              \n";

      return namedParameterJdbcTemplate.query(SELECT,
          (MapSqlParameterSource) null, new ResultSetExtractor<List<BandoDTO>>()
          {
            @Override
            public List<BandoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<BandoDTO> list = new ArrayList<BandoDTO>();
              long idBando;
              long idBandoLast = -1;
              BandoDTO bandoDTO = null;
              while (rs.next())
              {
                idBando = rs.getLong("ID_BANDO");
                if (idBando != idBandoLast)
                {
                  idBandoLast = idBando;
                  bandoDTO = new BandoDTO();
                  bandoDTO.setIdBando(idBando);

                  list.add(bandoDTO);
                }

              }
              return list.isEmpty() ? null : list;
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

  public List<TipoOperazioneDTO> getTipiOperazioniDisponibiliGAL(long idBando,
      long idAmmComp) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getTipiOperazioniDisponibiliGAL";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = "SELECT DISTINCT																\n"
          + "		D1.CODICE,																\n"
          + "		D1.ID_LIVELLO,															\n"
          + "		D1.DESCRIZIONE															\n"
          + "	 FROM 																		\n"
          + "		NEMBO_T_PIANO_FINANZIARIO_LEAD A,										\n"
          + "		NEMBO_R_LIV_FOCUS_AREA_LEADER B,											\n"
          + "		Nemboconf.NEMBO_T_PROCEDIMENTO_OGGETTO C,										\n"
          + "		NEMBO_D_LIVELLO D1,NEMBO_D_LIVELLO D2,										\n"
          + "		Nemboconf.NEMBO_D_ESITO E,														\n"
          + "		NEMBO_R_LIVELLO_PADRE F 													\n"
          + " WHERE 																		\n"
          + "		C.ID_PROCEDIMENTO_OGGETTO=A.ID_PROCEDIMENTO_OGGETTO 					\n"
          + " 		AND  B.ID_PIANO_FINANZIARIO_LEADER=A.ID_PIANO_FINANZIARIO_LEADER 		\n"
          + " 		AND (C.DATA_FINE IN (													\n"
          + "				SELECT 															\n"
          + "					MAX(DATA_FINE)  											\n"
          + "              FROM 															\n"
          + "					NEMBO_T_PROCEDIMENTO_OGGETTO  								\n"
          + "              WHERE 															\n"
          + "					ID_PROCEDIMENTO=C.ID_PROCEDIMENTO  							\n"
          + "                  AND ID_LEGAME_GRUPPO_OGGETTO IN (2,43) 						\n"
          + "                  AND ID_ESITO=4 												\n"
          + "              GROUP BY ID_PROCEDIMENTO										\n"
          + "				) 																\n"
          + "	    OR (C.DATA_FINE IS NULL AND C.ID_LEGAME_GRUPPO_OGGETTO IN (2,43)))		\n"
          + " AND A.EXT_ID_AZIENDA= (														\n"
          + "					SELECT 														\n"
          + "						EXT_ID_AZIENDA 											\n"
          + "					FROM 														\n"
          + "						SMRCOMUNE.DB_AMM_COMPETENZA_AZIENDA_PROC 				\n"
          + "					WHERE 														\n"
          + "						ID_AMM_COMPETENZA=:ID_AMM_COMP 							\n"
          + "						AND DATA_FINE_VALIDITA IS NULL 							\n"
          + "					)															\n"
          + " AND D1.ID_LIVELLO=B.ID_LIVELLO												\n"
          + " AND D1.ID_LIVELLO=F.ID_LIVELLO												\n"
          + " AND F.ID_LIVELLO=D2.ID_LIVELLO												\n"
          + " AND F.ID_LIVELLO_PADRE=58													\n"
          + " AND D1.ID_LIVELLO NOT IN (1000,1001,1006,1004,1007,1008,1009)";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_AMM_COMP", idAmmComp, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<TipoOperazioneDTO>>()
          {
            @Override
            public List<TipoOperazioneDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<TipoOperazioneDTO> list = new ArrayList<TipoOperazioneDTO>();
              TipoOperazioneDTO toDTO = null;
              while (rs.next())
              {
                toDTO = new TipoOperazioneDTO();
                toDTO.setIdLivello(rs.getLong("ID_LIVELLO"));
                toDTO.setCodice(rs.getString("CODICE"));
                toDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(toDTO);
              }
              return list.isEmpty() ? null : list;
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

  public void insertRLivelloBandoAmbito(long idBando, Long idAmbitoTematico,
      long idLivello) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRLivelloBando";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                    \n"
          + " INTO                    \n"
          + "   NEMBO_R_LIV_BANDO_AMBITO\n"
          + "   (                     \n"
          + "     ID_BANDO,      		\n"
          + "     ID_LIVELLO,    	   	\n"
          + "     ID_AMBITO_TEMATICO  \n"
          + "   )                     \n"
          + "   VALUES                \n"
          + "   (                     \n"
          + "     :ID_BANDO,      	\n"
          + "     :ID_LIVELLO,    	\n"
          + "     :ID_AMBITO_TEMATICO \n"
          + "   )            			\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      mapParameterSource.addValue("ID_AMBITO_TEMATICO", idAmbitoTematico,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_LIVELLO", idLivello),
              new LogParameter("ID_AMBITO_TEMATICO", idAmbitoTematico)
          },
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

  public void deleteRLivelloBandoAmbito(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteRLivelloBandoAmbito";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       	\n"
          + "   NEMBO_R_LIV_BANDO_AMBITO		\n"
          + "WHERE                   		\n"
          + "  ID_BANDO = :ID_BANDO       	\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
          },
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

  public EsitoCallPckDTO callDuplicaBando(long idBandoSelezionato,
      Long idBandoObiettivo, String flagTestiVerbali,
      String idsLegameGruppoOggetto, String nomeNuovoBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "callDuplicaBando";
    StringBuffer CALL = new StringBuffer("PCK_NEMBO_DUPLICA_BANDO.Main");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("bando_selezionato", idBandoSelezionato,
          Types.NUMERIC);
      if (idBandoObiettivo != null)
        parameterSource.addValue("bando_obiettivo", idBandoObiettivo,
            Types.NUMERIC);
      else
        parameterSource.addValue("bando_obiettivo", null, Types.NUMERIC);

      if (nomeNuovoBando != null)
        parameterSource.addValue("nome_bando", nomeNuovoBando, Types.VARCHAR);
      else
        parameterSource.addValue("nome_bando", null, Types.VARCHAR);

      parameterSource.addValue("flagTestiV", flagTestiVerbali, Types.VARCHAR);
      parameterSource.addValue("ListaOggetti", idsLegameGruppoOggetto,
          Types.VARCHAR);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_NEMBO_DUPLICA_BANDO")
              .withProcedureName("Main")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlParameter("bando_selezionato", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlInOutParameter("bando_obiettivo", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("nome_bando", java.sql.Types.VARCHAR));
      call.addDeclaredParameter(
          new SqlParameter("flagTestiV", java.sql.Types.VARCHAR));
      call.addDeclaredParameter(
          new SqlParameter("ListaOggetti", java.sql.Types.VARCHAR));

      call.addDeclaredParameter(
          new SqlOutParameter("pRisultato", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("pMessaggio", java.sql.Types.VARCHAR));
      Map<String, Object> results = call.execute(parameterSource);

      EsitoCallPckDTO dto = new EsitoCallPckDTO();
      dto.setRisultato(((BigDecimal) results.get("pRisultato")).intValue());
      dto.setMessaggio((String) results.get("pMessaggio"));
      dto.setIdBandoNew(
          ((BigDecimal) results.get("bando_obiettivo")).longValue());
      return dto;

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          CALL.toString());
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

  public List<FilieraDTO> getTipiFiliereDisponibili(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getTipiFiliereDisponibili]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                    		\n"
        + "   T.ID_TIPO_FILIERA,  				     		\n"
        + "   T.DESCRIZIONE AS DESCRIZIONE_TIPO_FILIERA,		\n"
        + "   T.DATA_FINE AS DATA_FINE_TIPO					\n"
        + " FROM                                      		\n"
        + "   NEMBO_D_TIPO_FILIERA T		             		\n"
        + " WHERE                    				 		\n"
        + "  (T.DATA_FINE > SYSDATE				 			\n"
        + "  OR T.DATA_FINE IS NULL)				 			\n"
        + " AND T.ID_TIPO_FILIERA NOT IN (					\n"
        + "SELECT                                    		\n"
        + "   T2.ID_TIPO_FILIERA  				     		\n"
        + " FROM                                      		\n"
        + "   NEMBO_D_TIPO_FILIERA T2,		             		\n"
        + "   NEMBO_R_BANDO_FILIERA B			         		\n"
        + " WHERE                    				 		\n"
        + "	T2.ID_TIPO_FILIERA = B.ID_TIPO_FILIERA    		\n"
        + "	AND B.ID_BANDO=:ID_BANDO			     		\n"
        + "  AND (T2.DATA_FINE > SYSDATE			     		\n"
        + "		OR T2.DATA_FINE IS NULL )) 					\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

    try
    {
      return queryForList(QUERY, mapParameterSource, FilieraDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
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

  public List<FilieraDTO> getTipiFiliereAssociate(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getTipiFiliereAssociate]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String QUERY = " SELECT                                    			\n"
        + "   T.ID_TIPO_FILIERA,  				     			\n"
        + "   T.DESCRIZIONE AS DESCRIZIONE_TIPO_FILIERA,			\n"
        + "   T.DATA_FINE AS DATA_FINE_TIPO						\n"
        + " FROM                                      			\n"
        + "   NEMBO_D_TIPO_FILIERA T,		             			\n"
        + "   NEMBO_R_BANDO_FILIERA B			         			\n"
        + " WHERE                    				 			\n"
        + "	T.ID_TIPO_FILIERA = B.ID_TIPO_FILIERA    			\n"
        + "	AND B.ID_BANDO=:ID_BANDO			     			\n"
        // + " AND (T.DATA_FINE > SYSDATE OR T.DATA_FINE IS NULL) \n"
        + " ORDER BY T.DESCRIZIONE								\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

    try
    {
      return queryForList(QUERY, mapParameterSource, FilieraDTO.class);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando)
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

  public void eliminaTipoFiliera(long idBando, long idTipoFiliera)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "eliminaTipoFiliera";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " DELETE                       					\n"
          + "   NEMBO_R_BANDO_FILIERA						 	\n"
          + "WHERE                   						\n"
          + "  ID_TIPO_FILIERA = :ID_TIPO_FILIERA       	\n"
          + "  AND ID_BANDO = :ID_BANDO       				\n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_TIPO_FILIERA", idTipoFiliera,
          Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("idTipoFiliera", idTipoFiliera)
          },
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

  public void insertRBandoFiliera(long idBando, long idTipoFiliera)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRBandoFiliera";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                       \n"
          + " INTO                       \n"
          + "   NEMBO_R_BANDO_FILIERA      \n"
          + "   (                        \n"
          + "     ID_BANDO,      		   \n"
          + "     ID_TIPO_FILIERA    	   \n"
          + "   )                        \n"
          + "   VALUES                   \n"
          + "   (                        \n"
          + "     :ID_BANDO,      	   \n"
          + "     :ID_TIPO_FILIERA       \n"
          + "   )                        \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando);
      mapParameterSource.addValue("ID_TIPO_FILIERA", idTipoFiliera);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_TIPO_FILIERA", idTipoFiliera)
          },
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

  public void insertTestiVerbaliOrdine(long idGruppoTestoVerbale,
      Long idTestoVerbale, String descrizione,
      String flagCatalogo, int ordine) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertTestiVerbaliOrdine";
    final String INSERT = " INSERT                                    \n"
        + " INTO                                                          \n"
        + "   NEMBO_D_TESTO_VERBALE                                         \n"
        + "   (                                                           \n"
        + "     ID_TESTO_VERBALE,                                         \n"
        + "     ID_GRUPPO_TESTO_VERBALE,                                  \n"
        + "     DESCRIZIONE,                                              \n"
        + "     FLAG_CATALOGO,                                            \n"
        + "     ORDINE                                                    \n"
        + "   )                                                           \n"
        + "   VALUES                                                      \n"
        + "   (                                                           \n"
        + "     :ID_TESTO_VERBALE,                          			  \n"
        + "     :ID_GRUPPO_TESTO_VERBALE,                                 \n"
        + "     :DESCRIZIONE,                                             \n"
        + "     :FLAG_CATALOGO,                                           \n"
        + "     :ORDINE                                          		  \n"
        + "   )                                                           \n";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    MapSqlParameterSource[] batchParameters = null;
    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE",
          idGruppoTestoVerbale, Types.NUMERIC);
      if (idTestoVerbale != 0)
        mapParameterSource.addValue("ID_TESTO_VERBALE", idTestoVerbale,
            Types.NUMERIC);
      else
        mapParameterSource.addValue("ID_TESTO_VERBALE",
            getNextSequenceValue("SEQ_NEMBO_D_TESTO_VERBALE"), Types.NUMERIC);

      mapParameterSource.addValue("FLAG_CATALOGO", flagCatalogo, Types.VARCHAR);
      mapParameterSource.addValue("DESCRIZIONE", descrizione, Types.VARCHAR);
      mapParameterSource.addValue("ORDINE", ordine, Types.VARCHAR);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idGruppoTestoVerbale", idGruppoTestoVerbale),
              new LogParameter("idTestoVerbale", idTestoVerbale),
              new LogParameter("flagCatalogo", flagCatalogo),
              new LogParameter("descrizione", descrizione),
              new LogParameter("ordine", ordine)

          },
          new LogVariable[]
          {}, INSERT, batchParameters);
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

  public List<TestoVerbaleDTO> getTestoVerbali(long idBandoOggetto,
      long idElencoCdu, long tipoCollocazioneTesto, Long cntTipiDestinazione,
      Long cntTipiPartenzaa) throws InternalUnexpectedException
  {

    final Long cntDest = cntTipiDestinazione;
    final Long cntPart = cntTipiPartenzaa;
    if (cntTipiPartenzaa < cntTipiDestinazione)
    {
      // da 5 a 6
      if (tipoCollocazioneTesto != 1)
        tipoCollocazioneTesto--;
    }
    else
      if (cntTipiDestinazione < cntTipiPartenzaa)
      {
        // da 6 a 5
        if (tipoCollocazioneTesto != 1)
          tipoCollocazioneTesto++;
      }

    String THIS_METHOD = "getTestoVerbaliByIdGruppo";
    String QUERY = " SELECT                                                       \n"
        + "   GTV.TIPO_COLLOCAZIONE_TESTO,                                            \n"
        + "   TV.DESCRIZIONE AS DESC_TESTO_VERBALE,                                   \n"
        + "   TV.FLAG_CATALOGO,                                                       \n"
        + "   TV.ID_TESTO_VERBALE,                                                    \n"
        + "   TV.ORDINE                                                               \n"
        + " FROM                                                                      \n"
        + "   NEMBO_D_GRUPPO_TESTO_VERBALE GTV,                                         \n"
        + "   NEMBO_D_TESTO_VERBALE TV                                    			  \n"
        + " WHERE                                                                     \n"
        + "   TV.ID_GRUPPO_TESTO_VERBALE = GTV.ID_GRUPPO_TESTO_VERBALE            	  \n"
        + "   AND GTV.ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO							  \n"
        + "   AND TV.FLAG_CATALOGO='N'												  \n"
        + "   AND GTV.ID_ELENCO_CDU=:ID_ELENCO_CDU									  \n";
    if (cntTipiDestinazione < cntTipiPartenzaa)
    {
      // da 6 a 5
      if (tipoCollocazioneTesto == 1)
        QUERY += "   AND GTV.TIPO_COLLOCAZIONE_TESTO in (1,2)			    	   \n";
      else
        QUERY += "   AND GTV.TIPO_COLLOCAZIONE_TESTO = :TIPO_COLLOCAZIONE_TESTO      \n";
    }
    else
      QUERY += "   AND GTV.TIPO_COLLOCAZIONE_TESTO = :TIPO_COLLOCAZIONE_TESTO      \n";

    QUERY += " ORDER BY                                                              \n"
        + "   GTV.TIPO_COLLOCAZIONE_TESTO,TV.ORDINE                                \n";

    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

      mapParameterSource.addValue("TIPO_COLLOCAZIONE_TESTO",
          tipoCollocazioneTesto, Types.NUMERIC);

      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_ELENCO_CDU", idElencoCdu, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<TestoVerbaleDTO>>()
          {
            @Override
            public List<TestoVerbaleDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<TestoVerbaleDTO> list = new ArrayList<TestoVerbaleDTO>();

              while (rs.next())
              {
                TestoVerbaleDTO testoVerbaleDTO = new TestoVerbaleDTO();
                testoVerbaleDTO
                    .setDescrizione(rs.getString("DESC_TESTO_VERBALE"));
                testoVerbaleDTO.setFlagCatalogo(rs.getString("FLAG_CATALOGO"));
                testoVerbaleDTO
                    .setIdTestoVerbale(rs.getLong("ID_TESTO_VERBALE"));
                testoVerbaleDTO.setIdTipoCollocazioneGruppo(
                    rs.getLong("TIPO_COLLOCAZIONE_TESTO"));
                testoVerbaleDTO.setOrdine(rs.getInt("ORDINE"));
                testoVerbaleDTO.setCntTipoCollPartenza(cntPart);
                testoVerbaleDTO.setCntTipoCollAvvio(cntDest);
                list.add(testoVerbaleDTO);
              }

              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, QUERY);
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

  public void duplicaTestoVerbali(TestoVerbaleDTO testo, long lIdElencoCdu,
      long idBandoOggetto, long idGruppoTestoDestinazione)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "duplicaTestoVerbali";
    final String INSERT = " INSERT                                    \n"
        + " INTO                                                          \n"
        + "   NEMBO_D_TESTO_VERBALE                                         \n"
        + "   (                                                           \n"
        + "     ID_TESTO_VERBALE,                                         \n"
        + "     ID_GRUPPO_TESTO_VERBALE,                                  \n"
        + "     DESCRIZIONE,                                              \n"
        + "     FLAG_CATALOGO,                                            \n"
        + "     ORDINE                                                    \n"
        + "   )                                                           \n"
        + "   VALUES                                                      \n"
        + "   (                                                           \n"
        + "     :ID_TESTO_VERBALE,      			                      \n"
        + "     :ID_GRUPPO_TESTO_VERBALE,      		                      \n"
        + "     :DESCRIZIONE,                                             \n"
        + "     'N',                                                      \n"
        + "     (                                                         \n"
        + "       SELECT                                                  \n"
        + "         NVL(MAX(TV.ORDINE),0)+:ORDINE                         \n"
        + "       FROM                                                    \n"
        + "         NEMBO_D_TESTO_VERBALE TV                                \n"
        + "       WHERE                                                   \n"
        + "         TV.ID_GRUPPO_TESTO_VERBALE = :ID_GRUPPO_TESTO_VERBALE	\n"
        + "     )                                                         \n"
        + "   )                                                           \n";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();

    try
    {
      Long idTestoVerbale = getNextSequenceValue("SEQ_NEMBO_D_TESTO_VERBALE");
      mapParameterSource.addValue("ID_TESTO_VERBALE", idTestoVerbale,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("TIPO_COLLOCAZIONE_TESTO",
          testo.getIdTipoCollocazioneGruppo(), Types.NUMERIC);
      mapParameterSource.addValue("ID_ELENCO_CDU", lIdElencoCdu, Types.NUMERIC);
      mapParameterSource.addValue("DESCRIZIONE", testo.getDescrizione(),
          Types.VARCHAR);
      mapParameterSource.addValue("ORDINE", 10, Types.NUMERIC);
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE",
          idGruppoTestoDestinazione, Types.NUMERIC);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("lIdElencoCdu", lIdElencoCdu),
              new LogParameter("TIPO_COLLOCAZIONE_TESTO",
                  testo.getIdTipoCollocazioneGruppo())
          },
          new LogVariable[]
          {}, INSERT, mapParameterSource);
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

  public Long getTipoCollocazioneTesto(long idGruppoTestoVerbali)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getFirstIdBandoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT TIPO_COLLOCAZIONE_TESTO										\n"
        + " FROM NEMBO_D_GRUPPO_TESTO_VERBALE									\n"
        + " WHERE ID_GRUPPO_TESTO_VERBALE = :ID_GRUPPO_TESTO_VERBALE			\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE",
          idGruppoTestoVerbali, Types.NUMERIC);
      return queryForLong(SELECT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idGruppoTestoVerbali", idGruppoTestoVerbali)
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

  public Long countTipiCollocazioneTesti(long idGruppoTestoVerbali)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::countTipiCollocazioneTesti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT MAX(TIPO_COLLOCAZIONE_TESTO)									\n"
        + " FROM NEMBO_D_GRUPPO_TESTO_VERBALE									\n"
        + " WHERE 															\n"
        + "	ID_ELENCO_CDU = (SELECT ID_ELENCO_CDU FROM NEMBO_D_GRUPPO_TESTO_VERBALE WHERE ID_GRUPPO_TESTO_VERBALE=:ID_GRUPPO_TESTO_VERBALE)		"
        + "   AND ID_BANDO_OGGETTO = (SELECT ID_BANDO_OGGETTO FROM NEMBO_D_GRUPPO_TESTO_VERBALE WHERE ID_GRUPPO_TESTO_VERBALE=:ID_GRUPPO_TESTO_VERBALE)	\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE",
          idGruppoTestoVerbali, Types.NUMERIC);
      return queryForLong(SELECT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idGruppoTestoVerbali", idGruppoTestoVerbali)
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

  public Long countTipiCollocazioneTesti(long idElencoCdu, Long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::countTipiCollocazioneTesti]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT MAX(TIPO_COLLOCAZIONE_TESTO)									\n"
        + " FROM NEMBO_D_GRUPPO_TESTO_VERBALE									\n"
        + " WHERE 															\n"
        + "	ID_ELENCO_CDU = :ID_ELENCO_CDU 								    \n"
        + "   AND ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO						\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_ELENCO_CDU", idElencoCdu, Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return queryForLong(SELECT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idElencoCdu", idElencoCdu),
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

  public Long getNextIdGruppoTesto(long idGruppo, long tipoCollocazioneTesto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getNextIdGruppoTesto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT ID_GRUPPO_TESTO_VERBALE										\n"
        + " FROM NEMBO_D_GRUPPO_TESTO_VERBALE									\n"
        + " WHERE 															\n"
        + "	ID_ELENCO_CDU = (SELECT ID_ELENCO_CDU FROM NEMBO_D_GRUPPO_TESTO_VERBALE WHERE ID_GRUPPO_TESTO_VERBALE=:ID_GRUPPO_TESTO_VERBALE)		\n"
        + "   AND ID_BANDO_OGGETTO = (SELECT ID_BANDO_OGGETTO FROM NEMBO_D_GRUPPO_TESTO_VERBALE WHERE ID_GRUPPO_TESTO_VERBALE=:ID_GRUPPO_TESTO_VERBALE)"
        + "   AND TIPO_COLLOCAZIONE_TESTO=:TIPO_COLLOCAZIONE_TESTO			\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("TIPO_COLLOCAZIONE_TESTO",
          tipoCollocazioneTesto, Types.NUMERIC);
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE", idGruppo,
          Types.NUMERIC);

      return queryForLong(SELECT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("tipoCollocazioneTesto", tipoCollocazioneTesto),
              new LogParameter("idGruppo", idGruppo)
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

  public void deleteTestiVerbaliPerImportazione(long idBandoOggetto,
      long idElencoCdu) throws InternalUnexpectedException
  {
    String THIS_METHOD = "deleteTestiVerbaliPerImportazione";
    String QUERY = " DELETE                                                 \n"
        + " FROM                                                                \n"
        + "   NEMBO_D_TESTO_VERBALE TV2                                           \n"
        + " WHERE                                                               \n"
        + "   TV2.ID_GRUPPO_TESTO_VERBALE IN                                    \n"
        + "   (                                                                 \n"
        + "     SELECT                                                          \n"
        + "       GTV.ID_GRUPPO_TESTO_VERBALE                                   \n"
        + "     FROM                                                            \n"
        + "       NEMBO_D_GRUPPO_TESTO_VERBALE GTV                                \n"
        + "     WHERE                                                           \n"
        + "       GTV.ID_BANDO_OGGETTO        = :ID_BANDO_OGGETTO           	\n"
        + "       AND GTV.ID_ELENCO_CDU           = :ID_ELENCO_CDU              \n"
        + "   )                                                                 \n";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_ELENCO_CDU", idElencoCdu, Types.NUMERIC);
      namedParameterJdbcTemplate.update(QUERY, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, QUERY);
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

  public long getIdBandoOggettoMaster(long idBando, long idBandoMaster,
      long idBandoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIdBandoOggettoMaster]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = "SELECT 														\n"
        + "	ID_BANDO_OGGETTO											\n"
        + "FROM 														\n"
        + "	NEMBO_R_BANDO_OGGETTO											\n"
        + "WHERE														\n"
        + "	ID_BANDO = :ID_BANDO_MASTER									\n"
        + "AND  											            \n"
        + " ID_LEGAME_GRUPPO_OGGETTO = ( 					            \n"
        + "SELECT ID_LEGAME_GRUPPO_OGGETTO 					            \n"
        + "FROM															\n"
        + " NEMBO_R_BANDO_OGGETTO 	A									\n"
        + "WHERE 														\n"
        + "	A.ID_BANDO=:ID_BANDO										\n"
        + "AND															\n"
        + "	A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO )					\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_MASTER", idBandoMaster,
          Types.NUMERIC);

      return queryForLong(SELECT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_BANDO_MASTER", idBandoMaster),
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
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

  public List<GruppoTestoVerbaleDTO> getGruppiTestoVerbalePerImportazioneCatalogo(
      long idBandoOggetto,
      long idElencoCdu, String flagVisible, String flagCatalogo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getGruppiTestoVerbalePerImportazioneCatalogo";
    String QUERY = " SELECT                                                       	\n"
        + "    DISTINCT                                                     			\n"
        + "     GTV.ID_GRUPPO_TESTO_VERBALE,                                    		\n"
        + "     GTV.TIPO_COLLOCAZIONE_TESTO,                                    		\n"
        + "     GTV.ID_ELENCO_CDU,		                                 				\n"
        // + " TV.ID_TESTO_VERBALE, \n"
        + "     TV.DESCRIZIONE AS DESC_TESTO_VERBALE,                                   \n"
        + "     TV.FLAG_CATALOGO,                                       				\n"
        + "     TV.ORDINE                                             					\n"
        + "    FROM                                                   					\n"
        + "      NEMBO_D_GRUPPO_TESTO_VERBALE GTV,                                     	\n"
        + "      NEMBO_D_TESTO_VERBALE TV                                      			\n"
        + "   WHERE                                                     				\n"
        + "     TV.ID_GRUPPO_TESTO_VERBALE (+)= GTV.ID_GRUPPO_TESTO_VERBALE             \n"
        + " 	AND GTV.ID_ELENCO_CDU = :ID_ELENCO_CDU                        			\n"
        // + " AND TV.FLAG_CATALOGO = :FLAG_CATALOGO \n"
        + "     AND GTV.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                         	\n"
        + "    ORDER BY GTV.ID_GRUPPO_TESTO_VERBALE                                     \n";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_ELENCO_CDU", idElencoCdu, Types.NUMERIC);
      mapParameterSource.addValue("FLAG_VISIBILE", flagVisible, Types.VARCHAR);
      mapParameterSource.addValue("FLAG_CATALOGO", flagCatalogo, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoTestoVerbaleDTO>>()
          {
            @Override
            public List<GruppoTestoVerbaleDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              List<GruppoTestoVerbaleDTO> list = new ArrayList<GruppoTestoVerbaleDTO>();
              long lastIdGruppoTestoVerbale = Long.MIN_VALUE;
              GruppoTestoVerbaleDTO gruppoTestoVerbaleDTO = null;
              while (rs.next())
              {
                long idGruppoTestoVerbale = rs
                    .getLong("ID_GRUPPO_TESTO_VERBALE");
                if (lastIdGruppoTestoVerbale != idGruppoTestoVerbale)
                {
                  gruppoTestoVerbaleDTO = new GruppoTestoVerbaleDTO();
                  gruppoTestoVerbaleDTO
                      .setIdGruppoTestoVerbale(idGruppoTestoVerbale);
                  gruppoTestoVerbaleDTO.setTipoCollocazioneTesto(
                      rs.getInt("TIPO_COLLOCAZIONE_TESTO"));
                  gruppoTestoVerbaleDTO
                      .setIdElencoCdu(rs.getLong("ID_ELENCO_CDU"));
                  list.add(gruppoTestoVerbaleDTO);
                  lastIdGruppoTestoVerbale = idGruppoTestoVerbale;
                }
                TestoVerbaleDTO testoVerbaleDTO = new TestoVerbaleDTO();
                testoVerbaleDTO
                    .setDescrizione(rs.getString("DESC_TESTO_VERBALE"));
                testoVerbaleDTO.setFlagCatalogo(rs.getString("FLAG_CATALOGO"));
                // testoVerbaleDTO.setIdTestoVerbale(rs.getInt("ID_TESTO_VERBALE"));
                testoVerbaleDTO.setOrdine(rs.getInt("ORDINE"));
                gruppoTestoVerbaleDTO.addTestoVerbale(testoVerbaleDTO);
              }
              return list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t, QUERY);
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

  public boolean isDatiIdentificativiModificabili(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isDatiIdentificativiModificabili";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                        \n"
          + "   A.ID_BANDO_OGGETTO,                                         \n"
          + "   A.FLAG_ATTIVO                                               \n"
          + " FROM                                                          \n"
          + "   NEMBO_R_BANDO_OGGETTO A,                                      \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                              \n"
          + "   NEMBO_D_GRUPPO_OGGETTO C                                      \n"
          + " WHERE                                                         \n"
          + "   A.ID_BANDO = :ID_BANDO                                      \n"
          + "   AND A.ID_LEGAME_GRUPPO_OGGETTO = B.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND B.ID_GRUPPO_OGGETTO = C.ID_GRUPPO_OGGETTO               \n"
          + " ORDER BY C.ORDINE, B.ORDINE                                   \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                String flagAttivo = rs.getString("FLAG_ATTIVO");
                if (flagAttivo.equals("S"))
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

  public void creaGruppiTestoVerbali(long idBandoOggetto, long lIdElencoCdu,
      long idBandoOggettoMaster) throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertGruppoTestoVerbale";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    Long idGruppoTestoVerbale = null;

    try
    {

      INSERT = " INSERT                       						\n"
          + " INTO                      						\n"
          + "   NEMBO_D_GRUPPO_TESTO_VERBALE       				\n"
          + "   (                        						\n"
          + "     ID_GRUPPO_TESTO_VERBALE,      				\n"
          + "     ID_ELENCO_CDU,      		   				\n"
          + "     ID_BANDO_OGGETTO,      		   				\n"
          + "     TIPO_COLLOCAZIONE_TESTO    	   				\n"
          + "   )                        						\n"
          + "   SELECT									    \n"
          + "		SEQ_NEMBO_D_GRUPPO_TESTO_VERBA.NEXTVAL,		\n"
          + "    	ID_ELENCO_CDU,      	   					\n"
          + "     :ID_BANDO_OGGETTO,                   		\n"
          + "		TIPO_COLLOCAZIONE_TESTO      	   			\n"
          + "    FROM                   						\n"
          + "		NEMBO_D_GRUPPO_TESTO_VERBALE                  \n"
          + "	   WHERE                    					\n"
          + "		ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO_MASTER   \n"
          + "		AND ID_ELENCO_CDU=:ID_ELENCO_CDU		    \n";

      idGruppoTestoVerbale = getNextSequenceValue(
          "SEQ_NEMBO_D_GRUPPO_TESTO_VERBA");
      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_GRUPPO_TESTO_VERBALE",
          idGruppoTestoVerbale);
      mapParameterSource.addValue("ID_BANDO_OGGETTO_MASTER",
          idBandoOggettoMaster);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto);

      mapParameterSource.addValue("ID_ELENCO_CDU", lIdElencoCdu);

      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("idBandoOggetto", idBandoOggetto),
              new LogParameter("idElencoCdu", lIdElencoCdu)
          },
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

  public DecodificaDTO<Long> getInfoBandoOggetto(long idBando,
      Long idQuadroOggetto, Long idBandoOggetto)
      throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getInfoBandoOggetto";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                    		\n"
          + "   B.ID_BANDO_OGGETTO,                      		\n"
          + "   B.FLAG_ATTIVO                      				\n"
          + " FROM                                      		\n"
          + "   NEMBO_R_BANDO_OGGETTO_QUADRO A,           		\n"
          + "   NEMBO_R_BANDO_OGGETTO B                   		\n"
          + " WHERE                                     		\n"
          + "   A.ID_BANDO_OGGETTO = B.ID_BANDO_OGGETTO 		\n"
          + "   AND A.ID_QUADRO_OGGETTO = :ID_QUADRO_OGGETTO   	\n"
          + "   AND A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO   	\n"
          + "   AND B.ID_BANDO = :ID_BANDO                     	\n";

      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_QUADRO_OGGETTO", idQuadroOggetto,
          Types.NUMERIC);
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<DecodificaDTO<Long>>()
          {
            @Override
            public DecodificaDTO<Long> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              DecodificaDTO<Long> decod = new DecodificaDTO<Long>();
              while (rs.next())
              {
                decod.setId(rs.getLong("ID_BANDO_OGGETTO"));
                decod.setDescrizione(rs.getString("FLAG_ATTIVO"));
              }

              return decod;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_QUADRO_OGGETTO", idQuadroOggetto),
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
          },
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

  public List<SoluzioneDTO> getSoluzioniControllo(String idControllo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getSoluzioniControllo";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 															\n"
          + " 	TS.DESCRIZIONE,													\n"
          + "	TS.FLAG_NOTE_OBBLIGATORIE,										\n"
          + "	TS.FLAG_FILE_OBBLIGATORIO,										\n"
          + "	TS.CODICE_IDENTIFICATIVO										\n"
          + " FROM 																\n"
          + "	NEMBO_D_TIPO_SOLUZIONE TS,										\n"
          + "	NEMBO_R_SOLUZIONE_CONTROLLO SC									\n"
          + " WHERE 															\n"
          + "   SC.ID_CONTROLLO = :ID_CONTROLLO 								\n"
          + "   AND TS.ID_TIPO_SOLUZIONE = SC.ID_TIPO_SOLUZIONE		  	    	\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);

      return queryForList(SELECT, parameterSource, SoluzioneDTO.class);
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

  public ControlloDTO getControlloById(String idControllo)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getControlloById";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 							\n"
          + " 	CODICE, DESCRIZIONE				\n"
          + " FROM 								\n"
          + "	NEMBO_D_CONTROLLO		    		\n"
          + " WHERE 							\n"
          + "   ID_CONTROLLO = :ID_CONTROLLO  	\n";
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_CONTROLLO", idControllo, Types.NUMERIC);

      return queryForObject(SELECT, parameterSource, ControlloDTO.class);
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

  public List<BandoDTO> getElencoBandiByDenominazione(String denominazione,
      Long idBandoSelezionato) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoBandiByDenominazione";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                                                             \n"
          + "   A.ID_BANDO,                                                                                                                      \n"
          + "   A.DATA_INIZIO,                                                                                                                   \n"
          + "   A.DATA_FINE,                                                                                                                     \n"                                                                                                                           
          + "   A.DENOMINAZIONE,                                                                                                                 \n"
          + "   A.ANNO_CAMPAGNA,                                                                                                                 \n"
          + "   EC.ID_EVENTO_CALAMITOSO,                                                                                                                 \n"
          + "   EC.DESCRIZIONE AS DESC_EVENTO_CALAMITOSO,                                                                                                                 \n"
          + "   EC.DATA_EVENTO,                                                                                                                 \n"
          + "   CE.ID_CATEGORIA_EVENTO,                                                                                                                 \n"
          + "   CE.DESCRIZIONE AS DESC_CAT_EVENTO,                                                                                                                 \n"
          + "   DECODE(A.FLAG_TITOLARITA_REGIONALE,'S','Si','N','No','') AS FLAG_TITOLARITA_REGIONALE,                                           \n"
          + "   B.DESCRIZIONE AS DESCR_TIPO_BANDO,                                                                                               \n"
          + "   DECODE((SELECT COUNT(*) FROM NEMBO_R_BANDO_OGGETTO BO WHERE BO.ID_BANDO=A.ID_BANDO AND BO.FLAG_ATTIVO = 'S'),0,'N','S') AS ATTIVO, \n"
          + "   L.ID_LIVELLO,			                                                                                                         \n"
          + "   L.DESCRIZIONE,                                                                                                                   \n"
          + "   L.CODICE,                                                                                                                        \n"
          + "   L.CODICE_LIVELLO,                                                                                                                \n"
          + "   ALLEG.ID_ALLEGATI_BANDO,                                                                                                         \n"
          + "   ALLEG.DESCRIZIONE DESCR_FILE,                                                                                                    \n"
          + "   ALLEG.NOME_FILE,                                                                                                                 \n"
          + "   ALLEG.ORDINE,                                                                                                                    \n"
          + "   VISTAAMM.ID_AMM_COMPETENZA,                                                                                                      \n"
          + "   VISTAAMM.DESC_BREVE_TIPO_AMMINISTRAZ ,                                                                                           \n"
          + "   VISTAAMM.DESCRIZIONE AS DESCR_AMM,                                                                                               \n"
          + "   VISTAAMM.DENOMINAZIONE_1		                                                                                                 \n"

          + " FROM                                                                                                                               \n"
          + "   NEMBO_D_BANDO A,                                                                                                                   \n"
          + "   NEMBO_D_TIPO_LIVELLO B,                                                                                                            \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,                                                                                                          \n"
          + "   NEMBO_R_BANDO_MASTER BM,                                                                                                           \n"
          + "   NEMBO_D_LIVELLO L,                                                                                                                 \n"
          + "   NEMBO_D_ALLEGATI_BANDO ALLEG,                                                                                                      \n"
          + "   NEMBO_D_BANDO_AMM_COMPETENZA AMMCOMP,                                                                                              \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA VISTAAMM,                                                                                              \n"
          + "   NEMBO_D_EVENTO_CALAMITOSO EC,                                                                                              \n"
          + "   NEMBO_D_CATEGORIA_EVENTO CE                                                                                              \n"

          + " WHERE                                                                                                                              \n"
          + "   B.ID_TIPO_LIVELLO = A.ID_TIPO_LIVELLO                                                                                            \n"
          + "   AND UPPER(A.DENOMINAZIONE) LIKE UPPER(:DENOMINAZIONE)                                                                                                          \n"

          + "   AND A.FLAG_MASTER = 'N'                                                                                                          \n"
          + "   AND LB.ID_BANDO = A.ID_BANDO                                                                                                     \n"
          + "   AND BM.ID_BANDO = A.ID_BANDO                                                                                                     \n"
          + "   AND L.ID_LIVELLO  = LB.ID_LIVELLO                                                                                                \n"
          + "   AND ALLEG.ID_BANDO (+)= A.ID_BANDO																								 \n"
          + "   AND AMMCOMP.EXT_ID_AMM_COMPETENZA = VISTAAMM.ID_AMM_COMPETENZA																	 \n"
          + "   AND AMMCOMP.ID_BANDO = A.ID_BANDO																								 \n"
          + "   AND A.ID_BANDO <> :ID_BANDO_SELEZIONATO																							 \n"
          //FIXME: capire se devono essere messe in outer join. Secondo me no perché prendo solo i bandi non master 
          + "   AND A.ID_EVENTO_CALAMITOSO = EC.ID_EVENTO_CALAMITOSO (+)																							 \n"
          + "   AND EC.ID_CATEGORIA_EVENTO = CE.ID_CATEGORIA_EVENTO	(+)																						 \n"
          ;

      SELECT += " ORDER BY A.DATA_FINE, A.DENOMINAZIONE, L.CODICE, ALLEG.ORDINE, ALLEG.NOME_FILE, A.ID_BANDO , VISTAAMM.DESC_BREVE_TIPO_AMMINISTRAZ          \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("DENOMINAZIONE", "%" + denominazione + "%",
          Types.VARCHAR);
      parameterSource.addValue("ID_BANDO_SELEZIONATO", idBandoSelezionato,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<BandoDTO>>()
          {
            @Override
            public List<BandoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<BandoDTO> list = new ArrayList<BandoDTO>();
              List<LivelloDTO> livelli = null;
              List<FileAllegatoDTO> allegati = null;
              List<AmmCompetenzaDTO> ammComp = null;

              LivelloDTO livello = null;
              long idBando;
              long idBandoLast = -1;
              long idLivello;
              long idLivelloLast = -1;
              BandoDTO bandoDTO = null;
              String misura = "";
              String sottoMisura = "";
              while (rs.next())
              {
                idBando = rs.getLong("ID_BANDO");
                if (idBando != idBandoLast)
                {
                  idBandoLast = idBando;
                  idLivelloLast = -1;
                  bandoDTO = new BandoDTO();
                  bandoDTO.setIdBando(idBando);
                  bandoDTO.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                  bandoDTO.setDataFine(rs.getTimestamp("DATA_FINE"));
                  bandoDTO.setIdEventoCalamitoso(rs.getLong("ID_EVENTO_CALAMITOSO"));
                  bandoDTO.setIdCategoriaEvento(rs.getLong("ID_CATEGORIA_EVENTO"));
                  bandoDTO.setDescEventoCalamitoso(rs.getString("DESC_EVENTO_CALAMITOSO"));
                  bandoDTO.setDescCatEvento(rs.getString("DESC_CAT_EVENTO"));
                  bandoDTO.setDataEvento(rs.getTimestamp("DATA_EVENTO"));
                  bandoDTO.setDenominazione(rs.getString("DENOMINAZIONE"));
                  bandoDTO.setAnnoCampagna(rs.getString("ANNO_CAMPAGNA"));
                  bandoDTO.setDescrTipoBando(rs.getString("DESCR_TIPO_BANDO"));
                  bandoDTO.setFlagTitolaritaRegionale(
                      rs.getString("FLAG_TITOLARITA_REGIONALE"));
                  bandoDTO.setBandoAttivo("S".equals(rs.getString("ATTIVO")));
                  try
                  {
                    bandoDTO.setElencoFocusArea(getElencoFocusArea(idBando));
                    bandoDTO.setElencoSettori(getElencoSettori(idBando));
                  }
                  catch (InternalUnexpectedException e)
                  {
                    logger.error(
                        "[" + THIS_CLASS + ":: " + e.getMessage() + "] END.");
                  }
                  livelli = new ArrayList<LivelloDTO>();
                  allegati = new ArrayList<FileAllegatoDTO>();
                  ammComp = new ArrayList<AmmCompetenzaDTO>();
                  bandoDTO.setLivelli(livelli);
                  bandoDTO.setAllegati(allegati);
                  bandoDTO.setAmministrazioniCompetenza(ammComp);
                  list.add(bandoDTO);
                }

                idLivello = rs.getLong("ID_LIVELLO");

                if (idLivello != idLivelloLast)
                {
                  idLivelloLast = idLivello;
                  livello = new LivelloDTO();
                  misura = rs.getString("CODICE_LIVELLO").split("\\.")[0];
                  if (rs.getString("CODICE_LIVELLO").split("\\.").length > 1)
                  {
                    sottoMisura = rs.getString("CODICE_LIVELLO")
                        .split("\\.")[1];
                    livello.setCodiceSottoMisura(misura + "." + sottoMisura);
                  }

                  livello.setIdLivello(rs.getLong("ID_LIVELLO"));
                  livello.setCodice(rs.getString("CODICE"));
                  livello.setCodiceLivello(rs.getString("CODICE_LIVELLO"));
                  livello.setCodiceMisura(misura);
                  livello.setDescrizione("DESCRIZIONE");
                  livelli.add(livello);
                }

                // aggiungo allegati
                FileAllegatoDTO file = new FileAllegatoDTO();
                if (rs.getString("NOME_FILE") != null)
                {
                  file.setDescrizione(rs.getString("DESCR_FILE"));
                  file.setIdAllegatiBando(rs.getLong("ID_ALLEGATI_BANDO"));
                  file.setNomeFile(rs.getString("NOME_FILE"));
                  boolean exists = false;
                  for (FileAllegatoDTO f : allegati)
                  {
                    if (f.getIdAllegatiBando() == rs
                        .getLong("ID_ALLEGATI_BANDO"))
                    {
                      exists = true;
                    }
                  }
                  if (!exists)
                    allegati.add(file);
                }

                // aggiungo amm comp
                AmmCompetenzaDTO amm = new AmmCompetenzaDTO();
                if (rs.getString("DESCR_AMM") != null)
                {
                  amm.setIdAmmCompetenza(rs.getLong("ID_AMM_COMPETENZA"));
                  amm.setDescBreveTipoAmministraz(
                      rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                  amm.setDescrizione(rs.getString("DESCR_AMM"));
                  amm.setDenominazioneuno(rs.getString("DENOMINAZIONE_1"));

                  boolean exists = false;
                  for (AmmCompetenzaDTO a : ammComp)
                  {
                    if (a.getIdAmmCompetenza() == rs
                        .getLong("ID_AMM_COMPETENZA"))
                    {
                      exists = true;
                    }
                  }
                  if (!exists)
                    ammComp.add(amm);
                }

              }
              return list.isEmpty() ? null : list;
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

  public boolean verificaUnivocitaNomeBando(String nomeNuovoBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "verificaUnivocitaNomeBando";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
        //TODO: FIXME qua la data evento non serve?
    	SELECT = " SELECT                        		     		\n"
          + "   COUNT(*) AS CNT_DEN		      			 		\n"
          + " FROM                          			 		\n"
          + "   NEMBO_D_BANDO		             			 		\n"
          + " WHERE                         			 		\n"
          + "   DENOMINAZIONE = :NOME_BANDO				 		\n"
          + "   AND (DATA_FINE IS NULL OR DATA_FINE < SYSDATE)	\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("NOME_BANDO", nomeNuovoBando, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                long cnt = rs.getLong("CNT_DEN");
                if (cnt <= 0)
                {
                  return true;
                }
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

  public List<GruppoOggettoDTO> getElencoGruppiOggettiAttivi(long idBando,
      String flagIstanza) throws InternalUnexpectedException
  {
    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    String THIS_METHOD = "getElencoGruppiOggettiAttivi";
    String QUERY = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      QUERY = " SELECT                                                    \n"
          + "   C.ID_BANDO,                                                 \n"
          + "   E.ID_GRUPPO_OGGETTO,                                        \n"
          + "   E.CODICE AS COD_GRUPPO,                                     \n"
          + "   E.DESCRIZIONE AS DESCR_GRUPPO,                              \n"
          + "   D.ID_OGGETTO,                                           	\n"
          + "   D.CODICE AS COD_OGGETTO,                                    \n"
          + "   D.DESCRIZIONE AS DESCR_OGGETTO,                             \n"
          + "   D.FLAG_ISTANZA,                                             \n"
          + "   C.FLAG_ATTIVO,                                              \n"
          + "   F.ID_BANDO_OGGETTO,                                         \n"
          + "   F.DATA_INIZIO,                                            	\n"
          + "   F.DATA_FINE,                                            	\n"
          + "   F.DATA_RITARDO,                                           	\n"
          + "   B.ID_LEGAME_GRUPPO_OGGETTO                                  \n"
          + " FROM                                                          \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                              \n"
          + "   NEMBO_R_BANDO_OGGETTO C,                                      \n"
          + "   NEMBO_D_OGGETTO D,                                            \n"
          + "   NEMBO_D_GRUPPO_OGGETTO E,                                     \n"
          + "   NEMBO_R_BANDO_OGGETTO F                                       \n"
          + " WHERE                                                         \n"
          + "   F.ID_BANDO(+) = C.ID_BANDO                  				\n"
          + "   AND B.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO \n"
          // + " AND ( C.DATA_FINE IS NULL OR C.DATA_FINE < SYSDATE ) \n"
          + "   AND B.ID_OGGETTO = D.ID_OGGETTO                             \n"
          + "   AND B.ID_GRUPPO_OGGETTO = E.ID_GRUPPO_OGGETTO               \n"
          + "   AND F.ID_BANDO_OGGETTO(+) = C.ID_BANDO_OGGETTO              \n"
          + "   AND C.ID_BANDO = :ID_BANDO                                  \n"
          + ((flagIstanza == null) ? ""
              : " AND D.FLAG_ISTANZA = :FLAG_ISTANZA   \n")
          + " ORDER BY                                                      \n"
          + "   E.ORDINE,B.ORDINE                                           \n";
      mapParameterSource.addValue("ID_BANDO", idBando);
      mapParameterSource.addValue("FLAG_ISTANZA", flagIstanza);
      return namedParameterJdbcTemplate.query(QUERY, mapParameterSource,
          new ResultSetExtractor<List<GruppoOggettoDTO>>()
          {
            @Override
            public List<GruppoOggettoDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<GruppoOggettoDTO> list = new ArrayList<GruppoOggettoDTO>();
              ArrayList<OggettiIstanzeDTO> oggetti = null;
              GruppoOggettoDTO gruppoDTO = null;
              long idGruppoOggetto = 0;
              long lastIdGruppoOggetto;
              while (rs.next())
              {
                lastIdGruppoOggetto = rs.getLong("ID_GRUPPO_OGGETTO");
                if (idGruppoOggetto != lastIdGruppoOggetto)
                {
                  idGruppoOggetto = lastIdGruppoOggetto;
                  gruppoDTO = new GruppoOggettoDTO();
                  gruppoDTO.setIdBando(rs.getLong("ID_BANDO"));
                  gruppoDTO.setIdGruppoOggetto(lastIdGruppoOggetto);
                  gruppoDTO.setCodGruppo(rs.getString("COD_GRUPPO"));
                  gruppoDTO.setDescrGruppo(rs.getString("DESCR_GRUPPO"));
                  oggetti = new ArrayList<OggettiIstanzeDTO>();
                  gruppoDTO.setOggetti(oggetti);
                  list.add(gruppoDTO);
                }

                OggettiIstanzeDTO oggetto = new OggettiIstanzeDTO();
                oggetto.setIdBando(rs.getLong("ID_BANDO"));
                oggetto.setIdBandoOggetto(rs.getLong("ID_BANDO_OGGETTO"));
                oggetto.setIdLegameGruppoOggetto(
                    rs.getLong("ID_LEGAME_GRUPPO_OGGETTO"));
                oggetto.setIdOggetto(rs.getLong("ID_OGGETTO"));
                oggetto.setCodOggetto(rs.getString("COD_OGGETTO"));
                oggetto.setDescrOggetto(rs.getString("DESCR_OGGETTO"));
                oggetto.setDescrGruppoOggetto(gruppoDTO.getDescrGruppo() + "-"
                    + rs.getString("DESCR_OGGETTO"));
                oggetto.setFlagIstanza(rs.getString("FLAG_ISTANZA"));
                oggetto.setFlagAttivo(rs.getString("FLAG_ATTIVO"));
                oggetto.setDataInizio(rs.getTimestamp("DATA_INIZIO"));
                oggetto.setDataFine(rs.getTimestamp("DATA_FINE"));
                oggetto.setDataRitardo(rs.getTimestamp("DATA_RITARDO"));
                oggetti.add(oggetto);
              }
              return list.isEmpty() ? null : list;
            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando) },
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

  public boolean canSendMailAttivazione(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "canSendMailAttivazione";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                          	\n"
          + "   COUNT(*) AS CNT		                                          	\n"
          + " FROM                                                            	\n"
          + "   NEMBO_T_ATTIVITA_BANDO_OGGETTO A,                               	\n"
          + "   NEMBO_R_BANDO_OGGETTO BO,                                		  	\n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,                              	\n"
          + "   NEMBO_D_OGGETTO O		                                          	\n"
          + " WHERE                                                           	\n"
          + "   O.ID_OGGETTO = LGO.ID_OGGETTO  		                           	\n"
          + "   AND O.FLAG_ISTANZA = 'S'									  	\n"
          + "   AND A.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO                  	\n"
          + "   AND A.DESC_ATTIVITA = 'ATTIVATO'			                  	\n"
          + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO	\n"
          + "   AND BO.ID_BANDO = (                  	                  		\n"
          + "		SELECT                   	                  	            \n"
          + "			BOG.ID_BANDO                   	                  		\n"
          + "		FROM                   	                  	               	\n"
          + "			NEMBO_R_BANDO_OGGETTO BOG                                 \n"
          + "		WHERE                   	                                \n"
          + "			BOG.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO)				\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                long countDich = rs.getLong("CNT");
                if (countDich <= 0)
                {
                  return true;
                }
                else
                {
                  return false;
                }
              }
              else
              {
                return false;
              }
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

  public Long getIdBando(long idBandoOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getIdBando]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = "SELECT ID_BANDO								\n"
        + " FROM NEMBO_R_BANDO_OGGETTO A					\n"
        + "WHERE 											\n"
        + " ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO 			\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<Long>()
          {
            @Override
            public Long extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
              {
                return rs.getLong("ID_BANDO");
              }
              return new Long(0);

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

  public OggettiIstanzeDTO getInfoOggetto(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "[" + THIS_CLASS + "::getInfoOggetto]";
    if (logger.isDebugEnabled())
    {
      logger.debug(THIS_METHOD + " BEGIN.");
    }
    final String SELECT = " SELECT 															\n"
        + "	A.DESCRIZIONE,												\n"
        + "	C.DATA_INIZIO,												\n"
        + "	C.DATA_FINE													\n"
        + "FROM 															\n"
        + "	NEMBO_D_OGGETTO A,											\n"
        + "	NEMBO_R_LEGAME_GRUPPO_OGGETTO B,								\n"
        + "	NEMBO_R_BANDO_OGGETTO C										\n"
        + "WHERE 															\n"
        + "	A.ID_OGGETTO = B.ID_OGGETTO									\n"
        + "AND															\n"
        + "	B.ID_LEGAME_GRUPPO_OGGETTO = C.ID_LEGAME_GRUPPO_OGGETTO		\n"
        + "AND															\n"
        + "	C.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO						\n";

    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
    try
    {
      mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, mapParameterSource,
          new ResultSetExtractor<OggettiIstanzeDTO>()
          {
            @Override
            public OggettiIstanzeDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {

              while (rs.next())
              {

                OggettiIstanzeDTO oggetto = new OggettiIstanzeDTO();
                oggetto.setDescrOggetto(rs.getString("DESCRIZIONE"));
                oggetto.setDataFine(rs.getDate("DATA_FINE"));
                oggetto.setDataInizio(rs.getDate("DATA_INIZIO"));

                return oggetto;
              }

              return null;

            }
          });
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto)
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

  public List<AmmCompetenzaDTO> getAmmCompetenzaDisponibiliFondi(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getAmmCompetenzaDisponibiliFondi";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " WITH BANDO_MASTER AS (                                         \n"
          + " SELECT NVL(A1.ID_BANDO_MASTER, B2.ID_BANDO) AS ID_BANDO_MASTER \n"
          + " FROM                                                           \n"
          + "   NEMBO_R_BANDO_MASTER A1,                                       \n"
          + "   NEMBO_D_BANDO B2                                               \n"
          + " WHERE                                                          \n"
          + "   A1.ID_BANDO(+) = B2.ID_BANDO                                 \n"
          + "   AND B2.ID_BANDO = :ID_BANDO                                  \n"
          + " )                                                              \n"
          + " SELECT                                                         \n"
          + "    A.ID_BANDO,                                                 \n"
          + "    BM.ID_BANDO_MASTER,                                         \n"
          + "    A.EXT_ID_AMM_COMPETENZA,                                    \n"
          + "    B.DESC_BREVE_TIPO_AMMINISTRAZ,                              \n"
          + "    B.DESC_ESTESA_TIPO_AMMINISTRAZ,                             \n"
          + "    B.DESCRIZIONE,                                              \n"
          + "    B.DENOMINAZIONE_1                                           \n"
          + "  FROM                                                          \n"
          + "    NEMBO_D_BANDO_AMM_COMPETENZA A,                               \n"
          + "    SMRCOMUNE_V_AMM_COMPETENZA B,                               \n"
          + "    BANDO_MASTER BM                                             \n"
          + "  WHERE                                                         \n"
          + "    A.EXT_ID_AMM_COMPETENZA = B.ID_AMM_COMPETENZA               \n"
          + "    AND A.ID_BANDO = BM.ID_BANDO_MASTER                         \n"
          + "    AND A.EXT_ID_AMM_COMPETENZA NOT IN (                        \n"
          + " SELECT                                               			 \n"
          + "   B.ID_AMM_COMPETENZA                               			 \n"
          + " FROM                                                 			 \n"
          + "    NEMBO_R_RISOR_LIV_BANDO_AMM_CO A,                      	 	 \n"
          + "   SMRCOMUNE_V_AMM_COMPETENZA B,								 \n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO C                      			 \n"
          + " WHERE                                                			 \n"
          + "   A.EXT_ID_AMM_COMPETENZA = B.ID_AMM_COMPETENZA      			 \n"
          + "   AND C.ID_BANDO = :ID_BANDO      							 \n"
          + "   AND A.ID_RISORSE_LIVELLO_BANDO = C.ID_RISORSE_LIVELLO_BANDO  \n"
          + "    )                                                           \n"
          + "  ORDER BY B.DESC_BREVE_TIPO_AMMINISTRAZ,B.DESCRIZIONE          \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<List<AmmCompetenzaDTO>>()
          {
            @Override
            public List<AmmCompetenzaDTO> extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              ArrayList<AmmCompetenzaDTO> list = new ArrayList<AmmCompetenzaDTO>();
              AmmCompetenzaDTO ammDTO = null;
              while (rs.next())
              {
                ammDTO = new AmmCompetenzaDTO();
                ammDTO.setIdBando(rs.getLong("ID_BANDO"));
                ammDTO.setIdBandoMaster(rs.getLong("ID_BANDO_MASTER"));
                ammDTO.setIdAmmCompetenza(rs.getLong("EXT_ID_AMM_COMPETENZA"));
                ammDTO.setDescBreveTipoAmministraz(
                    rs.getString("DESC_BREVE_TIPO_AMMINISTRAZ"));
                ammDTO.setDenominazioneuno(rs.getString("DENOMINAZIONE_1"));
                ammDTO.setDescEstesaTipoAmministraz(
                    rs.getString("DESC_ESTESA_TIPO_AMMINISTRAZ"));
                ammDTO.setDescrizione(rs.getString("DESCRIZIONE"));
                list.add(ammDTO);
              }
              return list.isEmpty() ? null : list;
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

  public boolean hasQualcosaInQuadro(long idBando, long idGruppoOggetto,
      long idOggetto, String codiceQuadro) throws InternalUnexpectedException
  {
    String THIS_METHOD = "hasQualcosaInQuadro";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      SELECT = " SELECT *                                                             	\n"
          + " FROM                                                                 	\n"
          + "   NEMBO_D_DETTAGLIO_INFO A,                                            	\n"
          + "   NEMBO_D_GRUPPO_INFO B,                                                \n"
          + "   NEMBO_D_QUADRO Q,                                                     \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,                                    \n"
          + "   NEMBO_R_BANDO_OGGETTO BO,			                                    \n"
          + "   NEMBO_R_QUADRO_OGGETTO QO                                            	\n"
          + " WHERE                                                                	\n"
          + "   B.ID_GRUPPO_INFO = A.ID_GRUPPO_INFO                                	\n"
          + "   AND BO.ID_BANDO = :ID_BANDO                    						\n"
          + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO		\n"
          + "   AND B.ID_BANDO_OGGETTO=BO.ID_BANDO_OGGETTO                        	\n"
          + "   AND QO.ID_QUADRO=Q.ID_QUADRO        								\n"
          + "   AND QO.ID_QUADRO_OGGETTO = B.ID_QUADRO_OGGETTO        				\n"
          + "   AND Q.CODICE=:CODICE_QUADRO   			     						\n"
          + "   AND LGO.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO						\n"
          + "   AND LGO.ID_OGGETTO = :ID_OGGETTO									\n"
      // + " AND B.FLAG_INFO_CATALOGO = 'N' \n"
      ;

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      parameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto,
          Types.NUMERIC);
      parameterSource.addValue("CODICE_QUADRO", codiceQuadro, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
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
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_GRUPPO_OGGETTO", idGruppoOggetto),
              new LogParameter("CODICE_QUADRO", codiceQuadro),
              new LogParameter("ID_OGGETTO", idOggetto)
          },
          new LogVariable[]
          {}, SELECT, new MapSqlParameterSource());
      logInternalUnexpectedException(e, THIS_METHOD);
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

  public boolean hasTestiVerbali(long idBando, long idGruppoOggetto,
      long idOggetto) throws InternalUnexpectedException
  {
    String THIS_METHOD = "hasTestiVerbali";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      SELECT = " SELECT *                                                             	\n"
          + " FROM                                                                 	\n"
          + "   NEMBO_D_GRUPPO_TESTO_VERBALE A,                                       \n"
          + "   NEMBO_D_TESTO_VERBALE B,                                              \n"
          + "   NEMBO_D_QUADRO Q,                                                     \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,                                    \n"
          + "   NEMBO_R_BANDO_OGGETTO BO,			                                    \n"
          + "   NEMBO_R_QUADRO_OGGETTO QO                                            	\n"
          + " WHERE                                                                	\n"
          + "   B.ID_GRUPPO_TESTO_VERBALE  = A.ID_GRUPPO_TESTO_VERBALE              \n"
          + "   AND BO.ID_BANDO = :ID_BANDO                    						\n"
          + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO		\n"
          + "   AND A.ID_BANDO_OGGETTO=BO.ID_BANDO_OGGETTO                        	\n"
          + "   AND QO.ID_QUADRO=Q.ID_QUADRO        								\n"
          + "   AND LGO.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO						\n"
          + "   AND LGO.ID_OGGETTO = :ID_OGGETTO									\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      parameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
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
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_GRUPPO_OGGETTO", idGruppoOggetto),
              new LogParameter("ID_OGGETTO", idOggetto)
          },
          new LogVariable[]
          {}, SELECT, new MapSqlParameterSource());
      logInternalUnexpectedException(e, THIS_METHOD);
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

  public EsitoCallPckDTO callDuplicaBando(Long idBandoSelezionato,
      Long idBandoObiettivo, StringBuilder listToPass,
      String nomeNuovoBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "callDuplicaBando";
    StringBuffer CALL = new StringBuffer("PCK_NEMBO_DUPLICA_BANDO.Main");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("bando_selezionato", idBandoSelezionato,
          Types.NUMERIC);
      if (idBandoObiettivo != null)
        parameterSource.addValue("bando_obiettivo", idBandoObiettivo,
            Types.NUMERIC);
      else
        parameterSource.addValue("bando_obiettivo", null, Types.NUMERIC);

      if (nomeNuovoBando != null)
        parameterSource.addValue("nome_bando", nomeNuovoBando, Types.VARCHAR);
      else
        parameterSource.addValue("nome_bando", null, Types.VARCHAR);

      parameterSource.addValue("ListaOggetti", listToPass.toString(),
          Types.VARCHAR);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_NEMBO_DUPLICA_BANDO")
              .withProcedureName("Main")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlParameter("bando_selezionato", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlInOutParameter("bando_obiettivo", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("nome_bando", java.sql.Types.VARCHAR));
      call.addDeclaredParameter(
          new SqlParameter("ListaOggetti", java.sql.Types.VARCHAR));

      call.addDeclaredParameter(
          new SqlOutParameter("pRisultato", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("pMessaggio", java.sql.Types.VARCHAR));
      Map<String, Object> results = call.execute(parameterSource);

      EsitoCallPckDTO dto = new EsitoCallPckDTO();
      dto.setRisultato(((BigDecimal) results.get("pRisultato")).intValue());
      dto.setMessaggio((String) results.get("pMessaggio"));
      dto.setIdBandoNew(
          ((BigDecimal) results.get("bando_obiettivo")).longValue());
      return dto;

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          CALL.toString());
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

  public boolean hasQuadro(long idBando, long idGruppoOggetto, long idOggetto,
      String codiceQuadro) throws InternalUnexpectedException
  {
    String THIS_METHOD = "hasQualcosaInQuadro";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      SELECT = " SELECT *                                                             	\n"
          + " FROM                                                                 	\n"
          + "   NEMBO_D_QUADRO Q,                                                     \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,                                    \n"
          + "   NEMBO_R_BANDO_OGGETTO BO,			                                    \n"
          + "   NEMBO_R_QUADRO_OGGETTO QO                                            	\n"
          + " WHERE                                                                	\n"
          + "   BO.ID_BANDO = :ID_BANDO                    							\n"
          + "   AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO		\n"
          + "   AND LGO.ID_OGGETTO = :ID_OGGETTO									\n"
          + "   AND LGO.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO						\n"
          + "   AND LGO.ID_OGGETTO = QO.ID_OGGETTO									\n"
          + "   AND QO.ID_QUADRO=Q.ID_QUADRO        								\n"
          + "   AND Q.CODICE=:CODICE_QUADRO   			     						\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
      parameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto,
          Types.NUMERIC);

      parameterSource.addValue("CODICE_QUADRO", codiceQuadro, Types.VARCHAR);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
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
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_OGGETTO", idOggetto)
          },
          new LogVariable[]
          {}, SELECT, new MapSqlParameterSource());
      logInternalUnexpectedException(e, THIS_METHOD);
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

  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezione(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoCriteriSelezione";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                             \n"
          + "   A.ID_CRITERIO AS ID_CRITERIO_DI_SELEZIONE,                                                       \n"
          + "   D.DESCRIZIONE AS DESCR_PRINCIPI_SELEZIONE,                                                       \n"
          + "   E.CODICE AS COD_OPERAZIONE,                                                                      \n"
          + "   A.CODICE,                                                                      					 \n"
          + "   C.ID_BANDO_LIVELLO_CRITERIO,                                                   					 \n"
          + "   A.CRITERIO_SELEZIONE AS CRITERIO_DI_SELEZIONE,                                                   \n"
          + "   B.ID_LIVELLO_CRITERIO,                                                   \n"
          + "   A.SPECIFICHE ,                                                                                   \n"
          + "   NVL(A.PUNTEGGIO_MIN,0) AS PUNTEGGIO_MIN,                                                         \n"
          + "   NVL(A.PUNTEGGIO_MAX,0) AS PUNTEGGIO_MAX,                                                         \n"
          + "   DECODE(A.FLAG_ELABORAZIONE,'A','Automatico','M','Manuale',A.FLAG_ELABORAZIONE) AS TIPO_CONTROLLO \n"
          + " FROM                                                                                               \n"
          + "   NEMBO_D_CRITERIO A,                                                                                \n"
          + "   NEMBO_R_LIVELLO_CRITERIO B,                                                                        \n"
          + "   NEMBO_R_BANDO_LIVELLO_CRITERIO C,                                                                  \n"
          + "   NEMBO_D_PRINCIPIO_SELEZIONE D,                                                                     \n"
          + "   NEMBO_D_LIVELLO E                                                                                  \n"
          + " WHERE                                                                                              \n"
          + "   B.ID_CRITERIO = A.ID_CRITERIO                                                                    \n"
          + "   AND C.ID_LIVELLO_CRITERIO = B.ID_LIVELLO_CRITERIO                                                \n"
          + "   AND D.ID_PRINCIPIO_SELEZIONE = A.ID_PRINCIPIO_SELEZIONE                                          \n"
          + "   AND E.ID_LIVELLO = B.ID_LIVELLO                                                                  \n"
          + "   AND C.ID_BANDO=:ID_BANDO                                                                         \n"
          + " ORDER BY D.DESCRIZIONE,A.CODICE                                                                    \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return queryForList(SELECT, parameterSource,
          CriterioDiSelezioneDTO.class);
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

  public byte[] getExcelParametroDiElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getExcelParametroDiElencoQuery";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                               \n"
          + "   EXCEL_TEMPLATE                                   \n"
          + " FROM                                                \n"
          + "   NEMBO_D_ELENCO_QUERY Q                             \n"
          + " WHERE                                               \n"
          + "  Q.ID_ELENCO_QUERY = :ID_ELENCO_QUERY     \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_ELENCO_QUERY", idElencoQuery, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<byte[]>()
          {
            @Override
            public byte[] extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return rs.getBytes("EXCEL_TEMPLATE");
              }
              else
              {
                return null;
              }
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

  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezioneDisponibili(
      long idBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoCriteriSelezioneDisponibili";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                  \n"
          + "   A.ID_CRITERIO ID_CRITERIO_DI_SELEZIONE,                                               \n"
          + "   D.DESCRIZIONE AS DESCR_PRINCIPI_SELEZIONE,                                            \n"
          + "   E.CODICE || ' ' || A.CODICE || ' ' ||A.CRITERIO_SELEZIONE  AS CRITERIO_DI_SEL_ESTESO, \n"
          + "   A.CODICE ,                                                                            \n"
          + "   B.ID_LIVELLO_CRITERIO,                                                   \n"
          + "   A.CRITERIO_SELEZIONE AS CRITERIO_DI_SELEZIONE                                         \n"
          + " FROM                                                                                    \n"
          + "   NEMBO_D_CRITERIO A,                                                                     \n"
          + "   NEMBO_R_LIVELLO_CRITERIO B,                                                             \n"
          + "   NEMBO_D_PRINCIPIO_SELEZIONE D,                                                          \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,                                                          \n"
          + "   NEMBO_D_LIVELLO E                                                                       \n"
          + " WHERE                                                                                   \n"
          + "   B.ID_CRITERIO = A.ID_CRITERIO                                                         \n"
          + "   AND D.ID_PRINCIPIO_SELEZIONE = A.ID_PRINCIPIO_SELEZIONE                               \n"
          + "   AND E.ID_LIVELLO = B.ID_LIVELLO                                                       \n"
          + "   AND LB.ID_LIVELLO = B.ID_LIVELLO                                                       \n"
          + "   AND LB.ID_BANDO=:ID_BANDO                                                       \n"
          + "   AND NOT EXISTS (                                                                          \n"
          + "       SELECT C.ID_LIVELLO_CRITERIO                                                      \n"
          + "       FROM NEMBO_R_BANDO_LIVELLO_CRITERIO C                                               \n"
          + "       WHERE C.ID_LIVELLO_CRITERIO = B.ID_LIVELLO_CRITERIO                               \n"
          + "       AND C.ID_BANDO=:ID_BANDO                                                          \n"
          + "    )                                                                                    \n"
          + " ORDER BY D.ORDINE,A.ORDINE                                                         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return queryForList(SELECT, parameterSource,
          CriterioDiSelezioneDTO.class);
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

  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezioneSelezionati(
      long idBando) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoCriteriSelezioneSelezionati";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                  \n"
          + "   A.ID_CRITERIO ID_CRITERIO_DI_SELEZIONE,                                               \n"
          + "   D.DESCRIZIONE AS DESCR_PRINCIPI_SELEZIONE,                                            \n"
          + "   E.CODICE || ' ' || A.CODICE || ' ' ||A.CRITERIO_SELEZIONE  AS CRITERIO_DI_SEL_ESTESO, \n"
          + "   A.CODICE ,                                                                            \n"
          + "   B.ID_LIVELLO_CRITERIO,                                                   \n"
          + "   A.CRITERIO_SELEZIONE AS CRITERIO_DI_SELEZIONE                                         \n"
          + " FROM                                                                                    \n"
          + "   NEMBO_D_CRITERIO A,                                                                     \n"
          + "   NEMBO_R_LIVELLO_CRITERIO B,                                                             \n"
          + "   NEMBO_D_PRINCIPIO_SELEZIONE D,                                                          \n"
          + "   NEMBO_R_LIVELLO_BANDO LB,                                                          \n"
          + "   NEMBO_D_LIVELLO E                                                                       \n"
          + " WHERE                                                                                   \n"
          + "   B.ID_CRITERIO = A.ID_CRITERIO                                                         \n"
          + "   AND D.ID_PRINCIPIO_SELEZIONE = A.ID_PRINCIPIO_SELEZIONE                               \n"
          + "   AND E.ID_LIVELLO = B.ID_LIVELLO                                                       \n"
          + "   AND LB.ID_LIVELLO = B.ID_LIVELLO                                                       \n"
          + "   AND LB.ID_BANDO=:ID_BANDO                                                       \n"
          + "   AND EXISTS (                                                                          \n"
          + "       SELECT C.ID_LIVELLO_CRITERIO                                                      \n"
          + "       FROM NEMBO_R_BANDO_LIVELLO_CRITERIO C                                               \n"
          + "       WHERE C.ID_LIVELLO_CRITERIO = B.ID_LIVELLO_CRITERIO                               \n"
          + "       AND C.ID_BANDO=:ID_BANDO                                                          \n"
          + "    )                                                                                    \n"
          + " ORDER BY D.ORDINE,A.ORDINE                                                         \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return queryForList(SELECT, parameterSource,
          CriterioDiSelezioneDTO.class);
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

  public void insertRBandoLivelloCriterio(long idBando, long idLivelloCriterio)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "insertRBandoLivelloCriterio";
    String INSERT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {

      INSERT = " INSERT                       \n"
          + " INTO                       \n"
          + "   NEMBO_R_BANDO_LIVELLO_CRITERIO       \n"
          + "   (                        \n"
          + "     ID_BANDO_LIVELLO_CRITERIO,      		   \n"
          + "     ID_LIVELLO_CRITERIO,    	   \n"
          + "     ID_BANDO    	   \n"
          + "   )                        \n"
          + "   VALUES                   \n"
          + "   (                        \n"
          + "     SEQ_NEMBO_R_BANDO_LIVELLO_CRIT.NEXTVAL,      	   \n"
          + "     :ID_LIVELLO_CRITERIO,      	   \n"
          + "     :ID_BANDO       \n"
          + "   )                        \n";

      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      mapParameterSource.addValue("ID_LIVELLO_CRITERIO", idLivelloCriterio,
          Types.NUMERIC);
      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          new LogParameter[]
          {
              new LogParameter("ID_BANDO", idBando),
              new LogParameter("ID_LIVELLO_CRITERIO", idLivelloCriterio)
          },
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

  public boolean checkCriteriConPunteggio(long idBando, long idLivelloCriterio)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "checkCriteriConPunteggio";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                                                                                                                   \n"
          + "   A.ID_LIVELLO_CRITERIO                                                                                                                                  \n"
          + " from                                                                                                                                                     \n"
          + "   NEMBO_R_BANDO_LIVELLO_CRITERIO A                                                                                                                         \n"
          + " WHERE                                                                                                                                                    \n"
          + "   A.ID_BANDO = :ID_BANDO                                                                                                                                 \n"
          + "   AND A.ID_LIVELLO_CRITERIO = :ID_LIVELLO_CRITERIO                                                                                                                                 \n"
          + "   AND (                                                                                                                                                  \n"
          + "       EXISTS (SELECT B.ID_BANDO_LIVELLO_CRITERIO FROM NEMBO_T_PUNTEGGIO_CALCOLATO B WHERE A.ID_BANDO_LIVELLO_CRITERIO = B.ID_BANDO_LIVELLO_CRITERIO)       \n"
          + "       OR  EXISTS (SELECT B.ID_BANDO_LIVELLO_CRITERIO FROM NEMBO_T_PUNTEGGIO_ISTRUTTORIA B WHERE A.ID_BANDO_LIVELLO_CRITERIO = B.ID_BANDO_LIVELLO_CRITERIO) \n"
          + "       )                                                                                                                                                  \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_LIVELLO_CRITERIO", idLivelloCriterio,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
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

  public Boolean isOggettoIstanzaPagamento(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isOggettoIstanzaPagamento";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                        \n"
          + "   C.ID_OGGETTO                                                \n"
          + " FROM                                                          \n"
          + "   NEMBO_R_BANDO_OGGETTO A,                                      \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO B,                              \n"
          + "   NEMBO_D_OGGETTO C                                             \n"
          + " WHERE                                                         \n"
          + "   A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                      \n"
          + "   AND A.ID_LEGAME_GRUPPO_OGGETTO = B.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND C.CODICE IN ('DSAL','DANT','DACC') \n"
          + "   AND B.ID_OGGETTO = C.ID_OGGETTO                             \n"
          + "   AND C.FLAG_ISTANZA = 'S'                                    \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              if (rs.next())
              {
                return Boolean.TRUE;
              }
              return Boolean.FALSE;
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

  public OggettiIstanzeDTO getParametriDefaultRicevuta(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getParametriDefaultRicevuta";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                     \n"
          + "    C.OGGETTO_RICEVUTA_DEFAULT,                             \n"
          + "    C.CORPO_RICEVUTA_DEFAULT                                \n"
          + "  FROM                                                      \n"
          + "    NEMBO_R_BANDO_OGGETTO A,                                  \n"
          + "    NEMBO_D_OGGETTO C,                                        \n"
          + "    NEMBO_R_LEGAME_GRUPPO_OGGETTO D                           \n"
          + "  WHERE                                                     \n"
          + "    A.ID_LEGAME_GRUPPO_OGGETTO = D.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "    AND D.ID_OGGETTO = C.ID_OGGETTO                         \n"
          + "    AND A.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO              \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<OggettiIstanzeDTO>()
          {
            @Override
            public OggettiIstanzeDTO extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              OggettiIstanzeDTO ogg = new OggettiIstanzeDTO();
              while (rs.next())
              {
                ogg.setCorpoRicevutaDefault(
                    rs.getString("CORPO_RICEVUTA_DEFAULT"));
                ogg.setOggettoRicevutaDefault(
                    rs.getString("OGGETTO_RICEVUTA_DEFAULT"));
              }
              return ogg;
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

  public List<ElencoQueryBandoDTO> getElencoReport(String extCodAttore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoReport";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 																\n"
          + "		A.ID_BANDO,														\n"
          + "		B.ID_ELENCO_QUERY,												\n"
          + "		B.DESCRIZIONE_BREVE DESCRIZIONE,								\n"
          + "		B.DESCRIZIONE_ESTESA INFO_AGGIUNTIVE,							\n"
          + "		C.DESCRIZIONE TIPOLOGIA_REPORT									\n"
          + " FROM																\n"
          + "		NEMBO_R_ELENCO_QUERY_BANDO A,										\n"
          + "		NEMBO_D_ELENCO_QUERY B,											\n"
          + "		NEMBO_D_TIPO_VISUALIZZAZIONE C,									\n"
          + "		NEMBO_D_BANDO D													\n"
          + " WHERE 																\n"
          + "		B.ID_ELENCO_QUERY = A.ID_ELENCO_QUERY							\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE = B.ID_TIPO_VISUALIZZAZIONE		\n"
          + " 	AND D.ID_BANDO = A.ID_BANDO										\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE=3									\n"
          + " 	AND D.FLAG_MASTER='S'											\n"
          + " 	AND A.EXT_COD_ATTORE=:EXT_COD_ATTORE							\n"
          + " ORDER BY A.ORDINAMENTO 												\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("EXT_COD_ATTORE", extCodAttore, Types.VARCHAR);

      return queryForList(SELECT, parameterSource, ElencoQueryBandoDTO.class);
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

  public List<ElencoQueryBandoDTO> getElencoGrafici(String extCodAttore)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getElencoGrafici";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT 																\n"
          + "		A.ID_BANDO,														\n"
          + "		B.ID_ELENCO_QUERY,												\n"
          + "		B.DESCRIZIONE_BREVE DESCRIZIONE,								\n"
          + "		B.DESCRIZIONE_ESTESA INFO_AGGIUNTIVE,							\n"
          + "		C.DESCRIZIONE TIPOLOGIA_REPORT									\n"
          + " FROM																\n"
          + "		NEMBO_R_ELENCO_QUERY_BANDO A,										\n"
          + "		NEMBO_D_ELENCO_QUERY B,											\n"
          + "		NEMBO_D_TIPO_VISUALIZZAZIONE C,									\n"
          + "		NEMBO_D_BANDO D													\n"
          + " WHERE 																\n"
          + "		B.ID_ELENCO_QUERY = A.ID_ELENCO_QUERY							\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE = B.ID_TIPO_VISUALIZZAZIONE		\n"
          + " 	AND D.ID_BANDO = A.ID_BANDO										\n"
          + " 	AND C.ID_TIPO_VISUALIZZAZIONE!=3								\n"
          + " 	AND D.FLAG_MASTER='S'											\n"
          + " 	AND A.EXT_COD_ATTORE=:EXT_COD_ATTORE							\n"
          + " ORDER BY A.ORDINAMENTO 												\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("EXT_COD_ATTORE", extCodAttore, Types.VARCHAR);

      return queryForList(SELECT, parameterSource, ElencoQueryBandoDTO.class);
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

  public EsitoCallPckDTO callDuplicaBandoCopiaOggettiStessoBando(
      Long idBandoOggettoOld, Long idBandoOggettoNew, int idQuadro)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "callDuplicaBandoCopiaOggettiStessoBando";

    StringBuffer CALL = new StringBuffer(
        "PCK_NEMBO_DUPLICA_BANDO.CopiaConfBando");
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }
    try
    {
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("pIdBandoOggettoOld", idBandoOggettoOld,
          Types.NUMERIC);
      parameterSource.addValue("pIdBandoOggettoNew", idBandoOggettoNew,
          Types.NUMERIC);
      parameterSource.addValue("pidQuadro", idQuadro, Types.NUMERIC);

      SimpleJdbcCall call = new SimpleJdbcCall(
          (DataSource) appContext.getBean("dataSource"))
              .withCatalogName("PCK_NEMBO_DUPLICA_BANDO")
              .withProcedureName("CopiaConfBando")
              .withoutProcedureColumnMetaDataAccess();

      call.addDeclaredParameter(
          new SqlParameter("pIdBandoOggettoOld", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("pIdBandoOggettoNew", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlParameter("pidQuadro", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("pRisultato", java.sql.Types.NUMERIC));
      call.addDeclaredParameter(
          new SqlOutParameter("pMessaggio", java.sql.Types.VARCHAR));
      // call.addDeclaredParameter(new
      // SqlOutParameter("RETURN",java.sql.Types.NUMERIC));

      // call.setFunction(true);

      Map<String, Object> results = call.execute(parameterSource);

      // Long r = call.executeFunction(Long.class, parameterSource);
      // Long q = r;
      EsitoCallPckDTO dto = new EsitoCallPckDTO();
      dto.setRisultato(((BigDecimal) results.get("pRisultato")).intValue());
      dto.setMessaggio((String) results.get("pMessaggio"));
      return dto;

    }
    catch (Throwable t)
    {
      InternalUnexpectedException e = new InternalUnexpectedException(t,
          CALL.toString());
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

  public boolean isOggettoDomandaPagamentoGAL(long idBandoOggetto)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isOggettoDomandaPagamentoGAL";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                        \n"
          + "   COUNT(*)                                                    \n"
          + " FROM                                                          \n"
          + "   NEMBO_D_BANDO A,                                              \n"
          + "   NEMBO_D_TIPO_LIVELLO B,                                       \n"
          + "   NEMBO_R_LEGAME_GRUPPO_OGGETTO C,                              \n"
          + "   NEMBO_R_BANDO_OGGETTO D,                                      \n"
          + "   NEMBO_D_OGGETTO E,  											\n"
          + "	NEMBO_D_GRUPPO_OGGETTO F           					        \n"
          + " WHERE                                                         \n"
          + "   A.ID_TIPO_LIVELLO = B.ID_TIPO_LIVELLO   	                \n"
          + "   AND C.ID_LEGAME_GRUPPO_OGGETTO = D.ID_LEGAME_GRUPPO_OGGETTO \n"
          + "   AND D.ID_BANDO = A.ID_BANDO                                 \n"
          + "   AND B.CODICE = 'G'                                          \n"
          + "   AND C.ID_OGGETTO = E.ID_OGGETTO                             \n"
          + "   AND D.ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO                  \n"
          + "   AND F.ID_GRUPPO_OGGETTO = C.ID_GRUPPO_OGGETTO				\n"
          + "	AND F.CODICE IN ('ACC','ANT','SAL','GINPR')                 \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);
      return namedParameterJdbcTemplate.queryForLong(SELECT,
          parameterSource) != 0;
      /*
       * return namedParameterJdbcTemplate.query(SELECT, parameterSource, new
       * ResultSetExtractor<Boolean>() {
       * 
       * @Override public Boolean extractData(ResultSet rs) throws SQLException,
       * DataAccessException { if (rs.next()) { return true; } return false; }
       * });
       */
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

  public BigDecimal getBudget(long idBando, long idLivello)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "getBudget";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                               	\n"
          + "   SUM(NVL(RISORSE_ATTIVATE,0)) AS RISORSE								\n"
          + " FROM                                                                 	\n"
          + "   NEMBO_T_RISORSE_LIVELLO_BANDO A                                     	\n"
          + " WHERE                                                                	\n"
          + "   A.ID_BANDO = :ID_BANDO                                             	\n"
          + "   AND A.ID_LIVELLO = :ID_LIVELLO                                      \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BigDecimal>()
          {
            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                BigDecimal risorse = rs.getBigDecimal("RISORSE");
                return risorse;
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

  public boolean controlloInfoCatalogo(long idBandoOggetto, Long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "controlloInfoCatalogo";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT DISTINCT *                                                                                              \n"
          + " FROM                                                                                                           \n"
          + " NEMBO_D_GRUPPO_INFO_CATALOGO  GIC,                                                                               \n"
          + " NEMBO_R_INFO_CATALOGO  RIC                                                                                       \n"
          + " WHERE                                                                                                          \n"
          + " RIC.ID_GRUPPO_INFO_CATALOGO = GIC.ID_GRUPPO_INFO_CATALOGO                                                      \n"
          + " AND RIC.ID_LIVELLO IN (SELECT ID_LIVELLO FROM NEMBO_R_LIVELLO_BANDO WHERE ID_BANDO = :ID_BANDO)                  \n"
          + " AND RIC.ID_QUADRO_OGGETTO IN (                                                                                 \n"
          + " 	SELECT QO.ID_QUADRO_OGGETTO                                                                                  \n"
          + " 	FROM NEMBO_R_QUADRO_OGGETTO QO, NEMBO_D_QUADRO Q, NEMBO_R_BANDO_OGGETTO_QUADRO BOQ                                 \n"
          + " 	WHERE QO.ID_QUADRO  = Q.ID_QUADRO                                                                            \n"
          + " 	AND BOQ.ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO	                   		                                         \n"
          + " 	AND BOQ.ID_QUADRO_OGGETTO = QO.ID_QUADRO_OGGETTO                                                             \n"
          + " 	AND Q.CODICE IN ('DICH','ALLEG','IMPEG')                                                                     \n"
          + " 	AND QO.ID_OGGETTO = (SELECT LGO.ID_OGGETTO                                                                   \n"
          + " 						FROM NEMBO_R_BANDO_OGGETTO BO, NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO                             \n"
          + " 						WHERE BO.ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO												 \n"
          + "						AND BO.ID_BANDO=:ID_BANDO                                 								 \n"
          + " 						AND LGO.ID_LEGAME_GRUPPO_OGGETTO=BO.ID_LEGAME_GRUPPO_OGGETTO                             \n"
          + " 						)                                                                                        \n"
          + " )                                                                                                              \n"
          + " AND RIC.ID_QUADRO_OGGETTO NOT IN                                                                               \n"
          + "		(SELECT ID_QUADRO_OGGETTO                                                                                \n"
          + "		 FROM NEMBO_D_GRUPPO_INFO GI                                                                               \n"
          + "		 WHERE GI.FLAG_INFO_CATALOGO='S'                                                                         \n"
          + "		 AND GI.ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO)                                                              \n"
          + " AND GIC.DESCRIZIONE NOT IN                                                                               		 \n"
          + "		(SELECT DESCRIZIONE                                                                             		 \n"
          + "		 FROM NEMBO_D_GRUPPO_INFO GI                                                                               \n"
          + "		 WHERE GI.FLAG_INFO_CATALOGO='S'                                                                         \n"
          + "		 AND GI.ID_BANDO_OGGETTO=:ID_BANDO_OGGETTO)	                                                             \n"
          + " AND GIC.FLAG_INFO_OBBLIGATORIA = 'S'                                                                           \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
      parameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,
          Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                return false;
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

  public boolean isPrimoOggettoAttivato(long idBando)
      throws InternalUnexpectedException
  {
    String THIS_METHOD = "isPrimoOggettoAttivato";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      SELECT = " SELECT                                                               \n"
          + "   *																   \n"
          + " FROM                                                                 \n"
          + "   NEMBO_R_BANDO_OGGETTO A                                              \n"
          + " WHERE                                                                \n"
          + "   A.ID_BANDO = :ID_BANDO                             				   \n"
          + "   AND A.FLAG_ATTIVO = 'S'				                               \n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<Boolean>()
          {
            @Override
            public Boolean extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                return false;
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

  public BigDecimal getRisorsePubblichePianoFinanziarioGal(long idBando,
      long idLivello) throws InternalUnexpectedException
  {
    String THIS_METHOD = "getRisorsePubblichePianoFinanziarioGal";
    String SELECT = "";
    if (logger.isDebugEnabled())
    {
      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
    }

    try
    {
      /*
       * SELECT =
       * " SELECT                                                               		\n"
       * + "   SUM(NVL(LFAL.RISORSE_PUBBLICHE ,0)) AS RISORSE							\n" +
       * " FROM                                                                 		\n"
       * + "   NEMBO_T_PIANO_FINANZIARIO_LEAD PFL,										\n" +
       * "	NEMBO_R_LIV_FOCUS_AREA_LEADER LFAL,                                       \n"
       * +
       * "   NEMBO_T_ITER_PIANO_FINANZ_LEAD PIFL,                                    \n"
       * +
       * "   NEMBO_D_BANDO_AMM_COMPETENZA BAC,                                     	\n"
       * +
       * "   NEMBO_R_LIV_BANDO_AMBITO LBA                                     			\n"
       * +
       * " WHERE                                                                		\n"
       * +
       * "   PFL.ID_PIANO_FINANZIARIO_LEADER = PIFL.ID_PIANO_FINANZIARIO_LEADER      \n"
       * +
       * "   AND LFAL.ID_PIANO_FINANZIARIO_LEADER = PFL.ID_PIANO_FINANZIARIO_LEADER  \n"
       * + "   AND LFAL.ID_LIVELLO = :ID_LIVELLO										\n" +
       * "   AND PIFL.ID_STATO_PIANO_FINANZIARIO = 2                                 \n"
       * +
       * "	AND PIFL.DATA_FINE IS NULL   		                                  	\n"
       * +
       * "	AND PFL.EXT_ID_AMM_COMPETENZA = BAC.EXT_ID_AMM_COMPETENZA               \n"
       * +
       * "   AND BAC.ID_BANDO = :ID_BANDO                                     		\n"
       * +
       * "	AND LBA.ID_LIVELLO = :ID_LIVELLO                                      	\n"
       * +
       * "	AND LBA.ID_BANDO = BAC.ID_BANDO                                      	\n"
       * ;
       */
      SELECT = " SELECT A.ID_PIANO_FINANZIARIO_LEADER,B.RISORSE_PUBBLICHE - 															\n"
          + " 				NVL((SELECT SUM(RIS.RISORSE_DISPONIBILI)                                                                    \n"
          + "                  FROM                                                                                                    	\n"
          + "                    (SELECT NVL(A1.RISORSE_ATTIVATE,0)-NVL((SELECT SUM(ECO.IMPORTO_ECONOMIA)                                 \n"
          + "                                                     FROM NEMBO_T_ECONOMIA ECO                                              	\n"
          + "                                                     WHERE ECO.ID_RISORSE_LIVELLO_BANDO = A1.ID_RISORSE_LIVELLO_BANDO     	\n"
          + "                                                     GROUP BY ECO.ID_RISORSE_LIVELLO_BANDO                                	\n"
          + "                                                     ),0) RISORSE_DISPONIBILI,                                            	\n"
          + "                            A5.ID_PIANO_FINANZIARIO_LEADER,A3.EXT_ID_AMM_COMPETENZA,											\n"
          + "							   A5.ID_AMBITO_TEMATICO,A5.ID_LIVELLO                                       						\n"
          + "                     FROM NEMBO_T_RISORSE_LIVELLO_BANDO A1,NEMBO_D_BANDO_AMM_COMPETENZA A3,                                   	\n"
          + "                          NEMBO_R_LIV_BANDO_AMBITO A4,NEMBO_R_LIV_FOCUS_AREA_LEADER A5                                        	\n"
          + "                     WHERE A5.ID_AMBITO_TEMATICO = A4.ID_AMBITO_TEMATICO                                                  	\n"
          + "                       AND A5.ID_LIVELLO = A4.ID_LIVELLO                                                                  	\n"
          + "                       AND A4.ID_LIVELLO = A1.ID_LIVELLO                                                                  	\n"
          + "                       AND A3.ID_BANDO = A1.ID_BANDO                                                                      	\n"
          + "                       AND A4.ID_BANDO = A1.ID_BANDO                                                                      	\n"
          + "                      ) RIS                                                                                               	\n"
          + "                      WHERE RIS.ID_PIANO_FINANZIARIO_LEADER = A.ID_PIANO_FINANZIARIO_LEADER                               	\n"
          + "                        AND RIS.EXT_ID_AMM_COMPETENZA = A.EXT_ID_AMM_COMPETENZA                                           	\n"
          + "                        AND RIS.ID_LIVELLO = C.ID_LIVELLO						                                           	\n"
          + "                        AND RIS.ID_AMBITO_TEMATICO = F.ID_AMBITO_TEMATICO		                                           	\n"
          + "                  ),0) RISORSE_DISPONIBILI,A.ID_PIANO_FINANZIARIO_LEADER,A.EXT_ID_AMM_COMPETENZA                             \n"
          + " FROM NEMBO_T_PIANO_FINANZIARIO_LEAD A,NEMBO_R_LIV_FOCUS_AREA_LEADER B,NEMBO_R_LIVELLO_BANDO C,                               	\n"
          + "      NEMBO_D_BANDO_AMM_COMPETENZA D,NEMBO_T_ITER_PIANO_FINANZ_LEAD E,NEMBO_R_LIV_BANDO_AMBITO F                              	\n"
          + " WHERE E.ID_PIANO_FINANZIARIO_LEADER = A.ID_PIANO_FINANZIARIO_LEADER                                                      	\n"
          + " AND E.DATA_FINE IS NULL                                                                                                  	\n"
          + " AND E.ID_STATO_PIANO_FINANZIARIO = 2			                                                                        	\n"
          + " AND E.ID_PIANO_FINANZIARIO_LEADER = B.ID_PIANO_FINANZIARIO_LEADER                                                        	\n"
          + " AND C.ID_LIVELLO = B.ID_LIVELLO                                                                                          	\n"
          + " AND D.ID_BANDO = C.ID_BANDO                                                                                              	\n"
          + " AND D.EXT_ID_AMM_COMPETENZA = A.EXT_ID_AMM_COMPETENZA                                                                    	\n"
          + " AND C.ID_BANDO = :ID_BANDO                                                                                               	\n"
          + " AND F.ID_AMBITO_TEMATICO = B.ID_AMBITO_TEMATICO                                                                          	\n"
          + " AND F.ID_LIVELLO = B.ID_LIVELLO                                                                                          	\n"
          + " AND F.ID_BANDO = C.ID_BANDO                                                                                             	\n";

      MapSqlParameterSource parameterSource = new MapSqlParameterSource();

      parameterSource.addValue("ID_LIVELLO", idLivello, Types.NUMERIC);
      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);

      return namedParameterJdbcTemplate.query(SELECT, parameterSource,
          new ResultSetExtractor<BigDecimal>()
          {
            @Override
            public BigDecimal extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
              while (rs.next())
              {
                BigDecimal risorse = rs.getBigDecimal("RISORSE_DISPONIBILI");
                return risorse;
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
  
	public long lockBando(long idBando) throws InternalUnexpectedException
	{
		final String QUERY = " SELECT 1 FROM NEMBO_D_BANDO WHERE ID_BANDO = :ID_BANDO FOR UPDATE ";
		MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
		return namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
	}

	public long inserisciComuniBando(
			long idBando, 
			String istatComune,
			String flagFoglio) throws InternalUnexpectedException{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    if(flagFoglio == null)
	    {
	    	flagFoglio="N";
	    }
	    final String INSERT = 
	    		"INSERT INTO NEMBO_R_BANDO_COMUNE									\r\n" + 
	    		"    (																\r\n" + 
	    		"        ID_BANDO_COMUNE,   										\r\n" + 
	    		"        EXT_ISTAT_COMUNE,											\r\n" + 
	    		"        FLAG_FOGLIO,												\r\n" + 
	    		"        ID_BANDO													\r\n" + 
	    		"    )																\r\n" + 
	    		"VALUES																\r\n" + 
	    		"    (																\r\n" + 
	    		"       :ID_BANDO_COMUNE,											\r\n" + 
	    		"       :EXT_ISTAT_COMUNE,											\r\n" + 
	    		"       :FLAG_FOGLIO,												\r\n" + 
	    		"       :ID_BANDO													\r\n" + 
	    		"    )"
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    long seqNemboRBandoComune=-1L;
	    try
	    {
	      seqNemboRBandoComune = getNextSequenceValue("SEQ_NEMBO_R_BANDO_COMUNE");
	      mapParameterSource.addValue("ID_BANDO_COMUNE", seqNemboRBandoComune, Types.NUMERIC);
	      mapParameterSource.addValue("EXT_ISTAT_COMUNE", istatComune, Types.VARCHAR);
	      mapParameterSource.addValue("FLAG_FOGLIO", flagFoglio, Types.VARCHAR);
	      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
	      namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO_COMUNE", seqNemboRBandoComune),
	              new LogParameter("EXT_ISTAT_COMUNE", istatComune),
	              new LogParameter("FLAG_FOGLIO", flagFoglio),
	              new LogParameter("ID_BANDO", idBando),
	          },
	          new LogVariable[]
	          {}, INSERT, mapParameterSource);
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
	    return seqNemboRBandoComune;
	}


	public List<ComuneBandoDTO> getListComuniBando(long idBando) 
		throws InternalUnexpectedException{
		   String THIS_METHOD = "getListComuniBando";
		    if (logger.isDebugEnabled())
		    {
		      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
		    }
		    String QUERY = "";
		    try
		    {
		    	QUERY = 
	    			"SELECT 																			\r\n" + 
	    			"    DA.ISTAT_COMUNE,																\r\n" + 
	    			"    DA.DESCRIZIONE_COMUNE,															\r\n" + 
	    			"    DA.DESCRIZIONE_PROVINCIA,														\r\n" + 
	    			"    DA.DESCRIZIONE_COMUNE || ' (' || DA.SIGLA_PROVINCIA || ') ' AS COMUNE,			\r\n" + 
	    			"    DA.SIGLA_PROVINCIA,															\r\n" + 
	    			"    BC.ID_BANDO_COMUNE,															\r\n" + 
	    			"    BC.FLAG_FOGLIO,																\r\n" + 
	    			"    BCF.EXT_FOGLIO AS FOGLIO														\r\n" + 
	    			"FROM 																				\r\n" + 
	    			"    SMRGAA_V_DATI_AMMINISTRATIVI DA,												\r\n" + 
	    			"    NEMBO_R_BANDO_COMUNE BC,														\r\n" + 
	    			"    NEMBO_R_BANDO_COMUNE_FOGLIO BCF												\r\n" + 
	    			"WHERE 																				\r\n" + 
	    			"        DA.ISTAT_COMUNE = BC.EXT_ISTAT_COMUNE										\r\n" + 
	    			"    AND BC.ID_BANDO_COMUNE = BCF.ID_BANDO_COMUNE(+)								\r\n" + 
	    			"    AND BC.ID_BANDO = :ID_BANDO													\r\n" + 
	    			"ORDER BY 																			\r\n" + 
	    			"    DA.DESCRIZIONE_COMUNE, DA.SIGLA_PROVINCIA, BCF.EXT_FOGLIO						\r\n"			
	    			;
		      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		      parameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
		      return namedParameterJdbcTemplate.query(QUERY, parameterSource,
		          new ResultSetExtractor<List<ComuneBandoDTO>>()
		          {
		            @Override
		            public List<ComuneBandoDTO> extractData(ResultSet rs)
		                throws SQLException, DataAccessException
		            {
		              ArrayList<ComuneBandoDTO> list = new ArrayList<ComuneBandoDTO>();
		              ComuneBandoDTO comune = null;
		              while (rs.next())
		              {
		            	String currentIstatComune = rs.getString("ISTAT_COMUNE");  
		                if(comune == null || !comune.getIstatComune().equals(currentIstatComune))
		                {
		                	comune = new ComuneBandoDTO();
		                	comune.setIstatComune(rs.getString("ISTAT_COMUNE"));
		                	comune.setDescrizioneComune(rs.getString("DESCRIZIONE_COMUNE"));
		                	comune.setSiglaProvincia(rs.getString("SIGLA_PROVINCIA"));
		                	comune.setIdBandoComune(rs.getLong("ID_BANDO_COMUNE"));
		                	comune.setFlagFoglio(rs.getString("FLAG_FOGLIO"));
		                	comune.setFogli(new ArrayList<Integer>());
		                	comune.setDecodificaFogli(new ArrayList<DecodificaDTO<Integer>>());
		                	list.add(comune);
		                }
		                if(rs.getObject("FOGLIO") != null)
		                {
		                	int foglio = rs.getInt("FOGLIO");
							comune.getFogli().add(foglio);
		                	DecodificaDTO<Integer> dec = new DecodificaDTO<Integer>();
		                	dec.setId(foglio);
		                	dec.setDescrizione(Integer.toString(foglio));
		                	dec.setCodice(Integer.toString(foglio));
		                	comune.getDecodificaFogli().add(dec);
		                }
		              }
		              return list.isEmpty() ? new ArrayList<ComuneBandoDTO>() : list;
		            }
		          });
		    }
		    catch (Throwable t)
		    {
		      InternalUnexpectedException e = new InternalUnexpectedException(t,QUERY);
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

	public List<Integer> getListFogliBandoComune(long idBandoComune) 
			throws InternalUnexpectedException
	{
	    String THIS_METHOD = "getListFogliBandoComune";
	    String SELECT = "";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
	    }

	    try
	    {
	      SELECT = 
	    		"SELECT 												\r\n" + 
	      		"    EXT_FOGLIO AS FOGLIO								\r\n" + 
	      		"FROM 													\r\n" + 
	      		"    NEMBO_R_BANDO_COMUNE_FOGLIO						\r\n" + 
	      		"WHERE 													\r\n" + 
	      		"    ID_BANDO_COMUNE = :ID_BANDO_COMUNE					\r\n" + 
	      		"ORDER BY 												\r\n" + 
	      		"    EXT_FOGLIO 										\r\n"
	      		;
	      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	      mapParameterSource.addValue("ID_BANDO_COMUNE", idBandoComune, Types.NUMERIC);

	      return namedParameterJdbcTemplate.query(SELECT,
	              mapParameterSource, new ResultSetExtractor<List<Integer>>()
	              {
	                @Override
	                public List<Integer> extractData(ResultSet rs)
	                    throws SQLException, DataAccessException
	                {
	                  List<Integer> list = new ArrayList<Integer>();
	                  while (rs.next())
	                  {
	                	  list.add(rs.getInt("FOGLIO"));
	                  }
	                  return list;
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

	public List<Integer> getFogliValidiByIdBandoComune(long idBandoComune) throws InternalUnexpectedException
	{
	    String THIS_METHOD = "getFogliValidiByIdBandoComune";
	    String SELECT = "";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
	    }

	    try
	    {
	      SELECT = 
	    		"SELECT DISTINCT 											\r\n" + 
	    		"    SP.FOGLIO												\r\n" + 
	    		"FROM 														\r\n" + 
	    		"    SMRGAA_V_STORICO_PARTICELLA SP,						\r\n" + 
	    		"    NEMBO_R_BANDO_COMUNE BC								\r\n" + 
	    		"WHERE 														\r\n" + 
	    		"    SP.ISTAT_COMUNE = BC.EXT_ISTAT_COMUNE					\r\n" + 
	    		"    AND SP.DATA_FINE_VALIDITA_PART IS NULL					\r\n" + 
	    		"    AND BC.ID_BANDO_COMUNE = :ID_BANDO_COMUNE				\r\n" + 
	    		"ORDER BY													\r\n" + 
	    		"    SP.FOGLIO"
	      		;
	      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	      mapParameterSource.addValue("ID_BANDO_COMUNE", idBandoComune, Types.NUMERIC);

	      return namedParameterJdbcTemplate.query(SELECT,
	              mapParameterSource, new ResultSetExtractor<List<Integer>>()
	              {
	                @Override
	                public List<Integer> extractData(ResultSet rs)
	                    throws SQLException, DataAccessException
	                {
	                  List<Integer> list = new ArrayList<Integer>();
	                  while (rs.next())
	                  {
	                	  list.add(rs.getInt("FOGLIO"));
	                  }
	                  return list;
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

	public void eliminaFogliBandoComune(long[] arrayIdBandoComune) throws InternalUnexpectedException
	{
	    String THIS_METHOD = "eliminaFogliBandoComune";
	    String DELETE = "";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
	    }
	    try
	    {

	      DELETE = 
	    		"  DELETE												\r\n" + 
	      		"  FROM NEMBO_R_BANDO_COMUNE_FOGLIO						\r\n" + 
	      		"  WHERE 1=1 \r\n" +
	      		getInCondition("ID_BANDO_COMUNE", arrayIdBandoComune)
	      		;
	  

	      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	      namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO_COMUNE", arrayIdBandoComune)
	          },
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

	public int inserisciFoglioComuneBando(long idBandoComune, Integer foglio) throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String INSERT = 
	    		"INSERT INTO NEMBO_R_BANDO_COMUNE_FOGLIO						\r\n" + 
	    		"    (															\r\n" + 
	    		"        ID_BANDO_COMUNE,										\r\n" + 
	    		"        EXT_FOGLIO												\r\n" + 
	    		"    )															\r\n" + 
	    		"VALUES 														\r\n" + 
	    		"    (															\r\n" + 
	    		"        :ID_BANDO_COMUNE,										\r\n" + 
	    		"        :EXT_FOGLIO											\r\n" + 
	    		"    )"
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      
	      mapParameterSource.addValue("ID_BANDO_COMUNE", idBandoComune, Types.NUMERIC);
	      mapParameterSource.addValue("EXT_FOGLIO", foglio, Types.NUMERIC);
	      return namedParameterJdbcTemplate.update(INSERT, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO_COMUNE", idBandoComune),
	              new LogParameter("EXT_FOGLIO", foglio)
	          },
	          new LogVariable[]
	          {}, INSERT, mapParameterSource);
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

	public int aggiornaBandoComune(long idBandoComune, String flagFoglio) throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String UPDATE = 
	    		"UPDATE NEMBO_R_BANDO_COMUNE					\r\n" + 
	    		"SET FLAG_FOGLIO = :FLAG_FOGLIO					\r\n" + 
	    		"WHERE ID_BANDO_COMUNE = :ID_BANDO_COMUNE		\r\n"
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	      
	      mapParameterSource.addValue("ID_BANDO_COMUNE", idBandoComune, Types.NUMERIC);
	      mapParameterSource.addValue("FLAG_FOGLIO", flagFoglio, Types.VARCHAR);
	      return namedParameterJdbcTemplate.update(UPDATE, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO_COMUNE", idBandoComune),
	              new LogParameter("FLAG_FOGLIO", flagFoglio)
	          },
	          new LogVariable[]
	          {}, UPDATE, mapParameterSource);
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

	public long getBandoComune(long idBandoComune) 
			throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String QUERY = 
	    		  "SELECT ID_BANDO "
	    		+ "FROM NEMBO_R_BANDO_COMUNE "
	    		+ "WHERE ID_BANDO_COMUNE = :ID_BANDO_COMUNE "
				;
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	    	mapParameterSource.addValue("ID_BANDO_COMUNE", idBandoComune, Types.NUMERIC);
	    	return namedParameterJdbcTemplate.queryForLong(QUERY, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO_COMUNE", idBandoComune)
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

	public int eliminaComuneBando(long idBando, long[] arrayIdBandoComune) 
		throws InternalUnexpectedException
	{
	    String THIS_METHOD = "eliminaFogliBandoComune";
	    String DELETE = "";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
	    }
	    try
	    {

	      DELETE = 
	    		"  DELETE												\r\n" + 
	      		"  FROM NEMBO_R_BANDO_COMUNE							\r\n" + 
	      		"  WHERE  												\r\n" +
	      		"  		 ID_BANDO = :ID_BANDO							\r\n" + 
	      		getInCondition("ID_BANDO_COMUNE", arrayIdBandoComune);
	      		;

	      MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	      mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
	      return namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO", idBando),
	              new LogParameter("ID_BANDO_COMUNE", arrayIdBandoComune)
	          },
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

	public int getIdProcedimentoAgricoloFromIdBando(Long idBandoSelezionato) throws InternalUnexpectedException{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String QUERY = " SELECT                       \n"
	    				   + "     ID_PROCEDIMENTO_AGRICOLO \n"
	    				   + " FROM                         \n"
	    				   + "     NEMBO_D_BANDO            \n"
	    				   + " WHERE                        \n"
	    				   + "     ID_BANDO = :ID_BANDO     \n";

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	    	mapParameterSource.addValue("ID_BANDO", idBandoSelezionato, Types.NUMERIC);
	    	return namedParameterJdbcTemplate.queryForInt(QUERY, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO_COMUNE", idBandoSelezionato)
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

	public List<DocumentiRichiestiDTO> getDocumentiRichiesti(long idBando, long idOggetto, long idGruppoOggetto) throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String QUERY = 
	    		     " WITH TIPO_DOC_RIC_BANDO_OGG AS (                                         \n"
				   + "     SELECT                                                               \n"
				   + "         TDRBO.*                                                          \n"
				   + "     FROM                                                                 \n"
				   + "         NEMBO_R_BANDO_OGGETTO BO,                                        \n"
				   + "         NEMBO_R_TIPO_DOC_RIC_BANDO_OGG TDRBO                             \n"
				   + "     WHERE                                                                \n"
				   + "         ID_BANDO = :ID_BANDO                                             \n"
				   + "         AND BO.ID_BANDO_OGGETTO = TDRBO.ID_BANDO_OGGETTO                 \n"
				   + " )                                                                        \n"
				   + " SELECT                                                                   \n"
				   + "     BO.ID_BANDO_OGGETTO, 			                                    \n"
				   + "     TDR.ID_TIPO_DOC_RICHIESTI,                                           \n"
				   + "     SDR.TESTO_SEZIONE,                                                   \n"
				   + "     TDR.ORDINE,                                                          \n"
				   + "     TDRO.ID_TIPO_DOC_RIC_OGGETTO,                                        \n"
				   + "     GO.DESCRIZIONE AS DESC_GRUPPO_OGGETTO,                               \n"
				   + "     O.DESCRIZIONE AS DESC_OGGETTO,                                       \n"
				   + "     SDR.CODICE || ' ' || SDR.TESTO_SEZIONE AS SEZIONE,                   \n"
				   + "     TDR.DESCRIZIONE AS DESC_TIPO_DOC_RICHIESTI,                          \n"
				   + "     BO.ID_BANDO_OGGETTO,                                                 \n"
				   + "     TDRBO.ID_TIPO_DOC_RIC_OGGETTO AS ID_TIPO_DOC_RIC_BANDO_OGG,                                     \n"
				   + "     LGO.ID_OGGETTO,                                                       \n"
				   + "     TDR.DATA_FINE_VALIDITA AS DATA_FINE_VAL_TIPO_DOC_RIC            		\n"
				   + " FROM                                                                     \n"
				   + "     NEMBO_R_TIPO_DOC_RIC_OGGETTO TDRO,                                   \n"
				   + "     NEMBO_D_TIPO_DOC_RICHIESTI TDR,                                      \n"
				   + "     NEMBO_D_SEZIONE_DOC_RICHIESTI SDR,                                   \n"
				   + "     NEMBO_D_GRUPPO_OGGETTO GO,                                           \n"
				   + "     NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,                                   \n"
				   + "     NEMBO_D_OGGETTO O,                                                   \n"
				   + "     NEMBO_R_BANDO_OGGETTO BO,                                            \n"
				   + "     TIPO_DOC_RIC_BANDO_OGG TDRBO                                         \n"
				   + " WHERE                                                                    \n"
				   + "     O.ID_OGGETTO = :ID_OGGETTO                                           \n"
				   + "     AND TDRO.ID_TIPO_DOC_RICHIESTI = TDR.ID_TIPO_DOC_RICHIESTI           \n"
				   + "     AND TDR.ID_SEZIONE_DOC_RICHIESTI = SDR.ID_SEZIONE_DOC_RICHIESTI      \n"
				   + "     AND TDRO.ID_OGGETTO = O.ID_OGGETTO                                   \n"
				   + "     AND O.ID_OGGETTO = LGO.ID_OGGETTO                                    \n"
				   + "     AND LGO.ID_GRUPPO_OGGETTO = GO.ID_GRUPPO_OGGETTO                     \n"
				   + "     AND GO.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO	                    \n"
				   + "     AND LGO.ID_LEGAME_GRUPPO_OGGETTO = BO.ID_LEGAME_GRUPPO_OGGETTO       \n"
				   + "     AND BO.ID_BANDO = :ID_BANDO                                          \n"
				   + "     AND TDRO.ID_TIPO_DOC_RIC_OGGETTO = TDRBO.ID_TIPO_DOC_RIC_OGGETTO (+) \n"
				   + "     AND (TDRBO.ID_TIPO_DOC_RIC_OGGETTO IS NOT NULL OR (TDR.DATA_FINE_VALIDITA IS NULL AND TDR.DATA_INIZIO_VALIDITA < SYSDATE)) \n"
				   + " ORDER BY                                                                 \n"
				   + "     SDR.CODICE,                                                          \n"
				   + "     SDR.TESTO_SEZIONE,                                                   \n"
				   + "     TDR.ORDINE                                                           \n"
	    		   ;

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	    	mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
	    	mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
	    	mapParameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto, Types.NUMERIC);
	    	return queryForList(QUERY, mapParameterSource, DocumentiRichiestiDTO.class);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO", idBando),
	              new LogParameter("ID_OGGETTO", idOggetto),
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
	
	
	final String queryGetIdBandoOggettoByIdBandoIdOggettoIdGruppoOggetto =
			     "     SELECT                                                             \n"
			   + "         BO.ID_BANDO_OGGETTO                                            \n"
			   + "     FROM                                                               \n"
			   + "         NEMBO_R_BANDO_OGGETTO BO,                                      \n"
			   + "         NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO                              \n"
			   + "     WHERE                                                              \n"
			   + "         BO.ID_BANDO = :ID_BANDO                                        \n"
			   + "         AND LGO.ID_LEGAME_GRUPPO_OGGETTO = BO.ID_LEGAME_GRUPPO_OGGETTO \n"
			   + "         AND LGO.ID_OGGETTO = :ID_OGGETTO                               \n"
			   + "         AND LGO.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO                 \n"
			   ;
	
	public long getIdBandoOggetto(long idBando, long idOggetto, long idGruppoOggetto) throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    String QUERY = queryGetIdBandoOggettoByIdBandoIdOggettoIdGruppoOggetto;
		
	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	    	mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
	    	mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
	    	mapParameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto, Types.NUMERIC);
	    	return queryForLong(QUERY, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO", idBando),
	              new LogParameter("ID_OGGETTO", idOggetto),
	              new LogParameter("ID_GRUPPO_OGGETTO", idGruppoOggetto),
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

	public long countNDocRichiestiInseritiBandoOggettoCheDevonoEssereEliminati(long idBando, long idOggetto,
			long idGruppoOggetto, long[] idsTipoDocRicOggetto) throws InternalUnexpectedException
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    String QUERY = 
	    		 " WITH BANDO_OGGETTO AS (                                                \n"
			   + "     SELECT                                                             \n"
			   + "         BO.ID_BANDO_OGGETTO                                            \n"
			   + "     FROM                                                               \n"
			   + "         NEMBO_R_BANDO_OGGETTO BO,                                      \n"
			   + "         NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO                              \n"
			   + "     WHERE                                                              \n"
			   + "         BO.ID_BANDO = :ID_BANDO                                        \n"
			   + "         AND LGO.ID_LEGAME_GRUPPO_OGGETTO = BO.ID_LEGAME_GRUPPO_OGGETTO \n"
			   + "         AND LGO.ID_OGGETTO = :ID_OGGETTO                               \n"
			   + "         AND LGO.ID_GRUPPO_OGGETTO = :ID_GRUPPO_OGGETTO                 \n"
			   + " )                                                                      \n"
			   + " SELECT                                                                 \n"
			   + "     COUNT(*) AS N                                                      \n"
			   + " FROM                                                                   \n"
			   + "     NEMBO_T_DOCUMENTI_RICHIESTI DR,                                    \n"
			   + "     NEMBO_T_PROCEDIMENTO_OGGETTO PO,                                   \n"
			   + "     NEMBO_T_PROCEDIMENTO P,                                            \n"
			   + "     NEMBO_R_TIPO_DOCUMENTI_RICHIES TDR,                                \n"
			   + "     NEMBO_R_TIPO_DOC_RIC_OGGETTO TDRO,                                 \n"
			   + "     NEMBO_R_TIPO_DOC_RIC_BANDO_OGG TDRBO,                              \n"
			   + "     BANDO_OGGETTO BO                                                   \n"
			   + " WHERE                                                                  \n"
			   + "     DR.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO            \n"
			   + "     AND PO.ID_PROCEDIMENTO = P.ID_PROCEDIMENTO                         \n"
			   + "     AND P.ID_BANDO = :ID_BANDO                                         \n"
			   + "     AND DR.ID_DOCUMENTI_RICHIESTI = TDR.ID_DOCUMENTI_RICHIESTI         \n"
			   + "     AND TDR.ID_TIPO_DOC_RICHIESTI = TDRO.ID_TIPO_DOC_RICHIESTI         \n"
			   + "     AND TDRO.ID_OGGETTO = :ID_OGGETTO                                  \n"
			   + "     AND TDRO.ID_TIPO_DOC_RIC_OGGETTO = TDRBO.ID_TIPO_DOC_RIC_OGGETTO   \n"
			   + "     AND TDRBO.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO                   \n"
			   ;
		if(idsTipoDocRicOggetto != null && idsTipoDocRicOggetto.length > 0)
		{
			QUERY = QUERY 	   
					+ getNotInCondition("TDRBO.ID_TIPO_DOC_RIC_OGGETTO", idsTipoDocRicOggetto);
		}

	    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    try
	    {
	    	mapParameterSource.addValue("ID_BANDO", idBando, Types.NUMERIC);
	    	mapParameterSource.addValue("ID_OGGETTO", idOggetto, Types.NUMERIC);
	    	mapParameterSource.addValue("ID_GRUPPO_OGGETTO", idGruppoOggetto, Types.NUMERIC);
	    	return queryForLong(QUERY, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO", idBando),
	              new LogParameter("ID_OGGETTO", idOggetto),
	              new LogParameter("ID_GRUPPO_OGGETTO", idGruppoOggetto),
	              new LogParameter("idsTipoDocRicOggetto", idsTipoDocRicOggetto),
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

	
	public void deleteTipoDocricBandoOgg(long idBandoOggetto) throws InternalUnexpectedException 
	{
		String THIS_METHOD = new Object(){}.getClass().getEnclosingMethod().getName();
	    if (logger.isDebugEnabled())
	    {
	      logger.debug("[" + THIS_CLASS + ":: " + THIS_METHOD + "] BEGIN.");
	    }
	    String DELETE = "";
	    try
	    {

	    	DELETE = DELETE +
	    			"DELETE NEMBO_R_TIPO_DOC_RIC_BANDO_OGG				\r\n" + 
	    			"WHERE 												\r\n" + 
	    			"    ID_BANDO_OGGETTO = :ID_BANDO_OGGETTO			\r\n"
	    			;

		    MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
		    mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto,Types.NUMERIC);
		    namedParameterJdbcTemplate.update(DELETE, mapParameterSource);
	    }
	    catch (Throwable t)
	    {
	      InternalUnexpectedException e = new InternalUnexpectedException(t,
	          new LogParameter[]
	          {
	              new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto),
	          },
	          new LogVariable[]{}, DELETE, new MapSqlParameterSource());
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

	public void insertTipoDocRicBandoOgg(long idBandoOggetto, long[] idsTipoDocRicOggetto) throws InternalUnexpectedException 
	{
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		String THIS_METHOD = "[" + THIS_CLASS + "::" + methodName + "]";
	    if (logger.isDebugEnabled())
	    {
	      logger.debug(THIS_METHOD + " BEGIN.");
	    }
	    final String INSERT = 
    		     " INSERT INTO NEMBO_R_TIPO_DOC_RIC_BANDO_OGG  \n"
			   + "  (							               \n"
			   + "   ID_TIPO_DOC_RIC_OGGETTO,                  \n"
			   + "   ID_BANDO_OGGETTO)                         \n"
			   + " VALUES                                      \n"
			   + "   (                                         \n"
			   + "     :ID_TIPO_DOC_RIC_OGGETTO,               \n"
			   + "     :ID_BANDO_OGGETTO                       \n"
			   + "   )                                         \n"
		;
	    int size = idsTipoDocRicOggetto.length;
	    MapSqlParameterSource[] batchParameters = new MapSqlParameterSource[size];
	    try
	    {
	      int i=0;
	      for(long idTipoDocRicOggetto : idsTipoDocRicOggetto)
	      {
	    	  MapSqlParameterSource mapParameterSource = new MapSqlParameterSource();
	    	  mapParameterSource.addValue("ID_TIPO_DOC_RIC_OGGETTO", idTipoDocRicOggetto, Types.NUMERIC);
	    	  mapParameterSource.addValue("ID_BANDO_OGGETTO", idBandoOggetto, Types.NUMERIC);
	    	  batchParameters[i++]=mapParameterSource;
	      }
	      namedParameterJdbcTemplate.batchUpdate(INSERT, batchParameters);
	    }
	    catch (Throwable t)
	    {
		      LogParameter[] parameters = new LogParameter[size * 2];
		      int i=0;
		      for(long idTipoDocRicOggetto : idsTipoDocRicOggetto)
		      {
		    	  parameters[i] = new LogParameter("ID_TIPO_DOC_RIC_OGGETTO", idTipoDocRicOggetto);
		    	  i++;
		    	  parameters[i] = new LogParameter("ID_BANDO_OGGETTO", idBandoOggetto);
		    	  i++;
	    	  }
	      
		      InternalUnexpectedException e = new InternalUnexpectedException(t,
		          parameters,
		          new LogVariable[]
		          {}, INSERT, batchParameters);
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
