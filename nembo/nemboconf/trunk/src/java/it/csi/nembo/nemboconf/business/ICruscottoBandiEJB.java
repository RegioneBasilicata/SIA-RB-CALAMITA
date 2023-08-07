package it.csi.nembo.nemboconf.business;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import javax.ejb.Local;

import it.csi.nembo.nemboconf.dto.ComuneBandoDTO;
import it.csi.nembo.nemboconf.dto.ComuneDTO;
import it.csi.nembo.nemboconf.dto.CriterioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.ElencoQueryBandoDTO;
import it.csi.nembo.nemboconf.dto.FilieraDTO;
import it.csi.nembo.nemboconf.dto.SoluzioneDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.ControlloAmministrativoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.ControlloDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.CruscottoInterventiDTO;
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
import it.csi.nembo.nemboconf.dto.reportistica.GraficoVO;
import it.csi.nembo.nemboconf.dto.reportistica.ParametriQueryReportVO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;

@Local
public interface ICruscottoBandiEJB extends INemboAbstractEJB
{

  public List<BandoDTO> getElencoBandiDisponibili(boolean isMaster,
      boolean isBandoGal, List<Long> idsBandiGal)
      throws InternalUnexpectedException;

  public List<BandoDTO> getDettaglioBandiMaster()
      throws InternalUnexpectedException;

  public List<AmmCompetenzaDTO> getAmmCompetenzaAssociate(long idBando,
      boolean isMaster) throws InternalUnexpectedException;

  public List<AmmCompetenzaDTO> getAmmCompetenzaDisponibili(long idBando)
      throws InternalUnexpectedException;

  public List<AmmCompetenzaDTO> getAmmCompetenzaDisponibiliFondi(long idBando)
      throws InternalUnexpectedException;

  public List<TipoOperazioneDTO> getTipiOperazioniAssociati(long idBando)
      throws InternalUnexpectedException;

  public List<TipoOperazioneDTO> getTipiOperazioniDisponibili(long idBando)
      throws InternalUnexpectedException;

  public BandoDTO getInformazioniBando(long idBando)
      throws InternalUnexpectedException;

  public boolean isBandoModificabile(long idBando)
      throws InternalUnexpectedException;

  public List<AmmCompetenzaDTO> getDettagliAmmCompetenza(
      Vector<Long> vIdAmmCompetenza) throws InternalUnexpectedException;

  public long aggiornaDatiIdentificativi(BandoDTO bando)
      throws InternalUnexpectedException;

  public List<FileAllegatoDTO> getElencoAllegati(long idBando)
      throws InternalUnexpectedException;

  public void insertFileAllegato(FileAllegatoDTO fileDTO)
      throws InternalUnexpectedException;

  public void deleteFileAllegato(long idAllegatiBando)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoFiltriSelezioneAziende()
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoTipiVincoli()
      throws InternalUnexpectedException;

  public void updateFiltroBando(long idBando, String istruzioneSql,
      String descrizioneFiltro) throws InternalUnexpectedException;

  public List<CruscottoInterventiDTO> getInterventi(long idBando)
      throws InternalUnexpectedException;

  public List<CruscottoInterventiDTO> getElencoInterventiSelezionati(
      long idBando) throws InternalUnexpectedException;

  public List<CruscottoInterventiDTO> getElencoInterventiSelezionabili(
      long idBando) throws InternalUnexpectedException;

  public void insertInterventi(long idBando, Vector<DecodificaDTO<Long>> valori)
      throws InternalUnexpectedException;

  public void eliminaIntervento(long idBando, long idLivello,
      long idDescrizioneIntervento) throws InternalUnexpectedException;

  public void updateIntervento(long idBando, long idDescrizioneIntervento,
      long idLivello, BigDecimal costoUnitMin, BigDecimal costoUnitMax)
      throws InternalUnexpectedException;

  public List<GruppoOggettoDTO> getElencoGruppiOggetti(long idBando)
      throws InternalUnexpectedException;

  public List<GruppoOggettoDTO> getElencoGruppiOggettiBandoMaster(long idBando,
      long idGruppoOggetto) throws InternalUnexpectedException;

  public int aggiornaSelezioneOggettiBando(long idBando,
      List<OggettiIstanzeDTO> elenco) throws InternalUnexpectedException;

  public List<QuadroDTO> getElencoQuadriDisponibili(long idBando,
      long idGruppoOggetto, long idOggetto) throws InternalUnexpectedException;

  public void aggiornaQuadri(List<QuadroDTO> elenco)
      throws InternalUnexpectedException;

  public void attivaBandoOggetto(long idBandoOggetto, Long idUtente,
      String descrizione, String note) throws InternalUnexpectedException;

  public void disattivaBandoOggetto(long idBandoOggetto, Long idUtente,
      String descrizione, String note) throws InternalUnexpectedException;

  public List<GruppoOggettoDTO> getElencoGruppiControlliDisponibili(
      long idBando) throws InternalUnexpectedException;
  
  public List<GruppoOggettoDTO> getElencoGruppiControlliDisponibili(
	      String flagIstanza, long idBando) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoGruppiControlliDisponibili(
      long idBando, String codiceQuadro) throws InternalUnexpectedException;

  public List<ControlloDTO> getElencoControlliDisponibili(long idBando,
      long idGruppoOggetto, long idOggetto) throws InternalUnexpectedException;

  public boolean esisteValoreParametro(long idBandoOggetto, long idControllo)
      throws InternalUnexpectedException;

  public void updateControlli(List<ControlloDTO> controlli, Long idUtente,
      long idBandoOggetto, String msgLogFinal)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getParametriControllo(long idBandoOggetto,
      long idControllo) throws InternalUnexpectedException;

  public void aggiornaValoriParametri(long idBandoOggetto, long idControllo,
      List<String> valoriParametri) throws InternalUnexpectedException;

  public List<GruppoInfoDTO> getElencoDettagliInfo(long idBando,
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException;

  public List<GruppoInfoDTO> getElencoDettagliInfoCatalogo(long idBando,
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException;

  public void inserisciDichiarazioni(long idBando, long idBandoOggetto,
      long idQuadroOggetto, List<GruppoInfoDTO> dichiarazioni)
      throws InternalUnexpectedException;

  public boolean verificaDichiarazioni(long idBando, long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException;

  public DecodificaDTO<Long> getIdBandoOggetto(long idBando,
      long idQuadroOggetto) throws InternalUnexpectedException;
  
  public long getIdBandoOggetto(long idBando,
	      long idOggetto, long idGruppoOggetto) throws InternalUnexpectedException;

  public boolean isDichiarazionePresente(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoTipiFileDisponibili()
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> elencoQueryBando(long idBando,
      boolean flagElenco, String attore) throws InternalUnexpectedException;

  public GraficoVO getGrafico(long idElencoQuery, ParametriQueryReportVO params)
      throws InternalUnexpectedException;

  public void updateAllegato(long idAllegatiBando,
      String descrizione) throws InternalUnexpectedException;

  public void updateOrdineAllegato(long idAllegatiBandoPartenza,
      long idAllegatiBandoArrivo) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getIdLegameInfo(long idBandoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException;

  public boolean graficiTabellariPresenti(long idBando, String string)
      throws InternalUnexpectedException;

  public boolean isProcedimentoOggettoQuadroInserito(long idBando,
      long idBandoOggetto, long idQuadroOggetto)
      throws InternalUnexpectedException;

  public byte[] getImmagineByIdQuadro(long idQuadro)
      throws InternalUnexpectedException;

  public List<AmmCompetenzaDTO> getAmmCompetenzaRisorsa(
      long idRisorseLivelloBando) throws InternalUnexpectedException;

  public List<GraduatoriaDTO> getGraduatorieBando(long idBando)
      throws InternalUnexpectedException;

  public List<FileAllegatoDTO> getAllegatiGraduatoria(long idGraduatoria)
      throws InternalUnexpectedException;

  public FileAllegatoDTO getFileAllegatoGraduatoria(long idAllegatiGraduatoria)
      throws InternalUnexpectedException;

  public List<LivelloDTO> getElencoLivelli() throws InternalUnexpectedException;

  public List<LivelloDTO> getLivelliBando(long idBando)
      throws InternalUnexpectedException;

  public List<ControlloAmministrativoDTO> getElencoControlliAmministrativiByIdQuadroOggetto(
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException;

  public void updateControlliTecnici(long idBandoOggetto, long idQuadroOggetto,
      List<ControlloAmministrativoDTO> elencoTmp)
      throws InternalUnexpectedException;

  public void logAttivitaBandoOggetto(Long idBandoOggetto, Long idUtente,
      String descrizione, String note) throws InternalUnexpectedException;

  public Long getFirstIdBandoOggetto(long idBando)
      throws InternalUnexpectedException;

  public Boolean checkControlliObbligatoriInseriti(long idBandoOggetto)
      throws InternalUnexpectedException;

  public FileAllegatoDTO getAllegato(long idAllegatoBando)
      throws InternalUnexpectedException;

  public String getDescrizioneFiltro(long idBando)
      throws InternalUnexpectedException;

  public List<LogRecordDTO> readLog(long idBando)
      throws InternalUnexpectedException;

  public String getDescrizioneOggetto(long idBandoOggetto)
      throws InternalUnexpectedException;

  public String getHelpCdu(String codCdu) throws InternalUnexpectedException;

  public List<FocusAreaDTO> getElencoFocusAreaBandi()
      throws InternalUnexpectedException;

  public List<SettoriDiProduzioneDTO> getElencoSettoriBandi()
      throws InternalUnexpectedException;

  public boolean checkParametriControlliInseriti(long idBandoOggetto)
      throws InternalUnexpectedException;

  public Ricevuta getDatiRicevutaMail(long idBandoOggetto)
      throws InternalUnexpectedException;

  public boolean canLogAttivitaBandoOggetto(long idBandoOggetto)
      throws InternalUnexpectedException;

  public void updateRicevutaMail(long idUtenteAggiornamento,
      long idBandoOggetto, String oggettoMail, String corpoMail)
      throws InternalUnexpectedException;

  public Boolean checkQuadroInserito(long idBandoOggetto)
      throws InternalUnexpectedException;

  public GraficoVO getDescTabella(String nomeTabella)
      throws InternalUnexpectedException;

  public void insertRow(String nomeTabella,
      LinkedHashMap<String, String> mapValues)
      throws InternalUnexpectedException;

  public void deleteRowByID(String nomeTabella, String nomeColonnaId, long id)
      throws InternalUnexpectedException;

  public List<OggettiIstanzeDTO> getElencoOggettiNemboconf()
      throws InternalUnexpectedException;

  public List<QuadroDTO> getElencoQuadriDisponibili(long idOggetto)
      throws InternalUnexpectedException;

  public List<QuadroDTO> getElencoQuadriSelezionati(long idOggetto)
      throws InternalUnexpectedException;

  public void updateQuadroOggetto(long idOggetto, String[] aIdQuadri)
      throws InternalUnexpectedException;

  public String getDescrizioneOggettoById(long idOggetto)
      throws InternalUnexpectedException;

  public void updateOrdineQuadroOggetto(long idOggetto, long idQuadroPartenza,
      long idQuadroArrivo) throws InternalUnexpectedException;

  public List<BeneficiarioDTO> getBeneficiari(long idBando)
      throws InternalUnexpectedException;

  public List<BeneficiarioDTO> getElencoBeneficiariDisponibili(long idBando)
      throws InternalUnexpectedException;

  public List<BeneficiarioDTO> getElencoBeneficiariSelezionati(long idBando)
      throws InternalUnexpectedException;

  public void updateFormeGiuridiche(long idBando, String[] aIdFgTipologia)
      throws InternalUnexpectedException;

  public List<QuadroOggettoVO> getElencoQuadroOggettoNemboconf()
      throws InternalUnexpectedException;

  public List<ControlloDTO> getElencoQuadriControlliDisponibili(
      long idQuadroOggetto) throws InternalUnexpectedException;

  public List<ControlloDTO> getElencoQuadriControlliSelezionati(
      long idQuadroOggetto) throws InternalUnexpectedException;

  public void updateQuadroOggettoControllo(long idQuadroOggetto,
      String[] aIdControlli) throws InternalUnexpectedException;

  public void updateQuadroOggettoControlli(
      List<ControlloDTO> vQuadroOggControlli)
      throws InternalUnexpectedException;

  public List<LivelloDTO> getElencoLivelliDisponibili(long idBandoOggetto,
      long idQuadroOggControlloAmm) throws InternalUnexpectedException;

  public List<LivelloDTO> getElencoLivelliSelezionati(long idBandoOggetto,
      long idQuadroOggControlloAmm) throws InternalUnexpectedException;

  public void updateControlliLivelli(long idBandoOggetto,
      long idQuadroOggControlloAmm, String[] aIdControlli)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoPlaceholder()
      throws InternalUnexpectedException;

  public Boolean isOggettoIstanza(long idBandoOggetto)
      throws InternalUnexpectedException;

  List<GruppoOggettoDTO> getElencoGruppiOggetti(long idBando,
      String flagIstanza) throws InternalUnexpectedException;

  public List<GraficoVO> elencoQueryBando(long idBando, boolean flagElenco)
      throws InternalUnexpectedException;

  public void aggiornaGraficiBando(long idBando, Long[] idsElencoQuery,
      Long idUtente, String msgLog) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoDocumentiBandoOggetto(long idBando,
      long idBandoMaster, long idBandoOggetto, boolean getFromMaster)
      throws InternalUnexpectedException;

  List<GruppoTestoVerbaleDTO> getGruppiTestoVerbale(long idBandoOggetto,
      long idElencoCdu, String flagVisibile, String flagCatalogo)
      throws InternalUnexpectedException;

  public List<TestoVerbaleDTO> updateTestiVerbali(long idBandoOggetto,
      long lIdElencoCdu, List<DecodificaDTO<Long>> testi, Long idUtente,
      String msgLog) throws InternalUnexpectedException, ApplicationException;

  public List<DecodificaDTO<String>> getElencoPlaceholderNemboconf()
      throws InternalUnexpectedException;

  public List<BandoDTO> getIdElencoBandiDisponibili()
      throws InternalUnexpectedException;

  public List<TipoOperazioneDTO> getTipiOperazioniDisponibiliGAL(long idBando,
      long idAmmComp) throws InternalUnexpectedException;

  // public EsitoCallPckDTO callDuplicaBando(long idBandoSelezionato, Long
  // idBandoObiettivo, String flagTestiVerbali, String idsLegameGruppoOggetto,
  // String nomeNuovoBando) throws InternalUnexpectedException;

  public List<FilieraDTO> getTipiFiliereAssociate(long idBando)
      throws InternalUnexpectedException;

  public List<FilieraDTO> getTipiFiliereDisponibili(long idBando)
      throws InternalUnexpectedException;

  public void eliminaTipoFiliera(long idBando, long idTipoFiliera)
      throws InternalUnexpectedException;

  public void updateFiliere(long idBando, Vector<Long> strToVector)
      throws InternalUnexpectedException;

  public void updateOrdineTestiGruppo(GruppoTestoVerbaleDTO gr)
      throws InternalUnexpectedException;

  public List<TestoVerbaleDTO> getTestoVerbali(long idBandoOggetto,
      long idElencoCdu, long idGruppoTestoVerbali)
      throws InternalUnexpectedException;

  public void duplicaTestoVerbali(long idBandoOggetto, long idElencoCdu,
      List<TestoVerbaleDTO> testiNonInCatalogo, long idGruppoTestoVerbali,
      String msgLog, Long idUtenteLogin) throws InternalUnexpectedException;

  public void importaTestiDaCatalogo(long idBando, long idBandoMaster,
      long idBandoOggetto, long idElencoCdu, String msgLog, Long idUtenteLogin)
      throws InternalUnexpectedException;

  public boolean isDatiIdentificativiModificabili(long idBando)
      throws InternalUnexpectedException;

  public boolean hasGruppiDaImportare(long idBandoMaster, long idBando,
      long idBandoOggetto, long idElencoCdu) throws InternalUnexpectedException;

  public void creaGruppiTestoVerbali(long idBando, long idBandoOggetto,
      long lIdElencoCdu, long idBandoMaster) throws InternalUnexpectedException;

  public DecodificaDTO<Long> getInfoBandoOggetto(long idBando,
      Long idQuadroOggetto, Long idBandoOggetto)
      throws InternalUnexpectedException;

  public ControlloDTO getControlloById(String idControllo)
      throws InternalUnexpectedException;

  public List<SoluzioneDTO> getSoluzioniControllo(String idControllo)
      throws InternalUnexpectedException;

  public List<BandoDTO> getElencoBandiByDenominazione(String denominazione,
      Long idBandoSelezionato) throws InternalUnexpectedException;

  public boolean verificaUnivocitaNomeBando(String nomeNuovoBando)
      throws InternalUnexpectedException;

  public List<GruppoOggettoDTO> getElencoGruppiOggettiAttivi(Long idBando)
      throws InternalUnexpectedException;

  public boolean haQualcosaDiVuoto(long idBando, long idGruppoOggetto,
      long idOggetto, String flagIstanza) throws InternalUnexpectedException;

  public boolean hasQualcosaInQuadro(long idBando, long idGruppoOggetto,
      long idOggetto, String codiceQuadro) throws InternalUnexpectedException;

  public boolean hasTestiVerbali(long idBando, long idGruppoOggetto,
      long idOggetto) throws InternalUnexpectedException;

  public EsitoCallPckDTO callDuplicaBando(Long idBandoSelezionato,
      Long idBandoObiettivo,
      List<GruppoOggettoDTO> gruppi, String nomeNuovoBando)
      throws InternalUnexpectedException;

  public boolean hasQuadro(Long idBandoObiettivo, long idOggetto,
      long idGruppoOggetto, String codiceQuadro)
      throws InternalUnexpectedException;

  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezione(long idBando)
      throws InternalUnexpectedException;

  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezioneDisponibili(
      long idBando) throws InternalUnexpectedException;

  public List<CriterioDiSelezioneDTO> getElencoCriteriSelezioneSelezionati(
      long idBando) throws InternalUnexpectedException;

  public void updateCriteri(long idBando, List<String> idsCriteri)
      throws InternalUnexpectedException;

  public boolean checkCriteriConPunteggio(long idBando, List<String> ids)
      throws InternalUnexpectedException;

  public Boolean isOggettoIstanzaPagamento(long idBandoOggetto)
      throws InternalUnexpectedException;

  public OggettiIstanzeDTO getParametriDefaultRicevuta(long idBandoOggetto)
      throws InternalUnexpectedException;

  public List<ElencoQueryBandoDTO> getElencoReport(String extCodAttore)
      throws InternalUnexpectedException;

  public List<ElencoQueryBandoDTO> getElencoGrafici(String extCodAttore)
      throws InternalUnexpectedException;

  byte[] getExcelParametroDiElencoQuery(long idElencoQuery)
      throws InternalUnexpectedException;

  public EsitoCallPckDTO callDuplicaBandoCopiaOggettiStessoBando(
      Long idBandoOggettoOld, Long idBandoOggettoNuovo,
      int idQuadro) throws InternalUnexpectedException;

  public boolean isOggettoDomandaPagamentoGAL(long idBandoOggetto)
      throws InternalUnexpectedException;

  public BigDecimal getBudget(long idBandoOggetto, long idLivello)
      throws InternalUnexpectedException;

  public long getIdBando(long idBandoOggetto)
      throws InternalUnexpectedException;

  public boolean controlloInfoCatalogo(long idBandoOggetto)
      throws InternalUnexpectedException;

  public boolean isPrimoOggettoAttivato(long idBandoOggetto)
      throws InternalUnexpectedException;

  public BigDecimal getRisorsePubblichePianoFinanziarioGal(long idBando,
      long idLivello) throws InternalUnexpectedException;

  public List<ComuneDTO> getComuniNonSelezionatiPerBando(String idRegione, String istatProvincia,
	      String flagEstinto, String flagEstero, long idBando) throws InternalUnexpectedException;
  
  public int inserisciComuniBando(long idBando, String[] arrayComune) throws InternalUnexpectedException;

  public List<ComuneBandoDTO> getListComuniBando(long idBando) throws InternalUnexpectedException;

  public List<Integer> getListFogliBandoComune(long idBandoComune) throws InternalUnexpectedException;

  public List<Integer> getFogliValidiByIdBandoComune(long idBandoComune) throws InternalUnexpectedException;

  public void inserisciFogliComuneBando(long idBandoComune, Integer[] arrayFogliInseritiInteger) throws InternalUnexpectedException;

  public int inserisciTuttiFogliComuneComune(long idBandoComune) throws InternalUnexpectedException;

  public int eliminaComuniBando(long idBando, long[] arrayIdBandoComune)  throws InternalUnexpectedException;
  
  public List<DocumentiRichiestiDTO> getDocumentiRichiesti(long idBando, long idOggetto, long idGruppoOggetto) throws InternalUnexpectedException;

  public boolean deleteInsertTipoDocRicBandoOgg(long idBando, long idOggetto, long idGruppoOggetto, long[] idsTipoDocRicOggetto) throws InternalUnexpectedException;

}
