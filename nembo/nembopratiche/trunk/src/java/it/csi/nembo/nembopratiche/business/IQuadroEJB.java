package it.csi.nembo.nembopratiche.business;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.Local;

import org.springframework.web.multipart.MultipartFile;

import it.csi.nembo.nembopratiche.dto.AmmCompetenzaDTO;
import it.csi.nembo.nembopratiche.dto.AziendaDTO;
import it.csi.nembo.nembopratiche.dto.DecodificaAmmCompentenza;
import it.csi.nembo.nembopratiche.dto.DecodificaDTO;
import it.csi.nembo.nembopratiche.dto.DecodificaSoggFirmatario;
import it.csi.nembo.nembopratiche.dto.ElencoCduDTO;
import it.csi.nembo.nembopratiche.dto.ExcelRicevutePagInterventoDTO;
import it.csi.nembo.nembopratiche.dto.ExcelRigaDocumentoSpesaDTO;
import it.csi.nembo.nembopratiche.dto.FornitoreDTO;
import it.csi.nembo.nembopratiche.dto.GraduatoriaDTO;
import it.csi.nembo.nembopratiche.dto.ImportoInterventoVO;
import it.csi.nembo.nembopratiche.dto.ImportoLiquidatoDTO;
import it.csi.nembo.nembopratiche.dto.IntegrazioneAlPremioDTO;
import it.csi.nembo.nembopratiche.dto.LogOperationOggettoQuadroDTO;
import it.csi.nembo.nembopratiche.dto.ProcedimentoDTO;
import it.csi.nembo.nembopratiche.dto.ProcedimentoEstrattoVO;
import it.csi.nembo.nembopratiche.dto.ProspettoEconomicoDTO;
import it.csi.nembo.nembopratiche.dto.RegistroAntimafiaDTO;
import it.csi.nembo.nembopratiche.dto.RiduzioniSanzioniDTO;
import it.csi.nembo.nembopratiche.dto.RigaFiltroDTO;
import it.csi.nembo.nembopratiche.dto.RipartizioneImportoDTO;
import it.csi.nembo.nembopratiche.dto.VolturaDTO;
import it.csi.nembo.nembopratiche.dto.danni.RigaSegnalazioneDannoDTO;
import it.csi.nembo.nembopratiche.dto.danni.SegnalazioneDannoDTO;
import it.csi.nembo.nembopratiche.dto.estrazionecampione.EstrazioneACampioneDTO;
import it.csi.nembo.nembopratiche.dto.estrazionecampione.NumeroLottoDTO;
import it.csi.nembo.nembopratiche.dto.login.ProcedimentoAgricoloDTO;
import it.csi.nembo.nembopratiche.dto.nuovoprocedimento.BandoDTO;
import it.csi.nembo.nembopratiche.dto.nuovoprocedimento.FileAllegatoDTO;
import it.csi.nembo.nembopratiche.dto.nuovoprocedimento.LivelloDTO;
import it.csi.nembo.nembopratiche.dto.permission.UpdatePermissionProcedimentoOggetto;
import it.csi.nembo.nembopratiche.dto.plsql.EstrazioneCampioneDTO;
import it.csi.nembo.nembopratiche.dto.plsql.MainControlloDTO;
import it.csi.nembo.nembopratiche.dto.plsql.MainPunteggioDTO;
import it.csi.nembo.nembopratiche.dto.procedimento.DocumentoSpesaVO;
import it.csi.nembo.nembopratiche.dto.procedimento.OrganismoDelegatoDTO;
import it.csi.nembo.nembopratiche.dto.procedimento.RicevutaPagamentoVO;
import it.csi.nembo.nembopratiche.dto.procedimento.TestataProcedimento;
import it.csi.nembo.nembopratiche.dto.procedimento.TipoDocumentoSpesaVO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.OggettoIconaDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.ProcedimentoOggetto;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.QuadroOggettoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.ResponsabileDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.allegati.ContenutoFileAllegatiDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.allegati.FileAllegatiDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.anticipo.DatiAnticipo;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.anticipo.DatiAnticipoModificaDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.anticipo.SospensioneAnticipoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.anticipo.SportelloBancaDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.conticorrenti.ContoCorrenteDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.conticorrenti.ContoCorrenteEstesoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlli.ControlloDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlli.FonteControlloDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlli.GiustificazioneAnomaliaDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliamministrativi.AnnoExPostsDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliamministrativi.ControlloAmministrativoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliamministrativi.EsitoControlliAmmDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliamministrativi.InfoExPostsDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliamministrativi.VisitaLuogoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliamministrativi.VisitaLuogoExtDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliinlocomisureinvestimento.ControlliInLocoInvestDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.controlliinlocomisureinvestimento.DatiSpecificiDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.datafinelavori.DataFineLavoriDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.datiidentificativi.DatiIdentificativi;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.datiidentificativi.DatiIdentificativiModificaDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.datiidentificativi.SettoriDiProduzioneDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.dichiarazioni.GruppoInfoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.esitofinale.EsitoFinaleDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.estrazionecampione.RigaSimulazioneEstrazioneDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.interventi.DecodificaInterventoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.interventi.DettaglioInterventoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.interventi.RigaRendicontazioneDocumentiSpesaDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.interventi.RigaRendicontazioneSpese;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.partecipanti.PartecipanteDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.punteggi.RaggruppamentoLivelloCriterio;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.stampa.ProcedimOggettoStampaDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.stampa.SegnapostoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.stampa.StampaOggettoDTO;
import it.csi.nembo.nembopratiche.dto.procedimentooggetto.stampa.StampaOggettoIconaDTO;
import it.csi.nembo.nembopratiche.exception.ApplicationException;
import it.csi.nembo.nembopratiche.exception.InternalUnexpectedException;
import it.csi.nembo.nembopratiche.exception.NemboPermissionException;
import it.csi.nembo.nembopratiche.integration.RigaAnticipoLivello;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;
import it.csi.solmr.dto.anag.services.DelegaAnagrafeVO;

@Local
public interface IQuadroEJB extends INemboAbstractEJB
{
  
	
  public DatiIdentificativi getDatiIdentificativiProcedimentoOggetto(
      long idProcedimentoOggetto, long idQuadroOggetto, Date dataValidita)
      throws InternalUnexpectedException;

  public DatiIdentificativiModificaDTO getDatiIdentificativiModificaProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<QuadroOggettoDTO> getQuadriProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaAmmCompentenza> getListAmmCompetenzaAbilitateBando(
      long idBando) throws InternalUnexpectedException;

  void updateDatiProcedimentoOggetto(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto, Integer idAmmCompetenza,
      String note,
      long extIdUtenteAggiornamento, String idContitolare, Long idSettore,
      String cup, String avvioIstruttoria, boolean hasVercod, Long vercod,
      Date dataVisuraCamerale) throws InternalUnexpectedException,
      NemboPermissionException, ApplicationException;

  public UpdatePermissionProcedimentoOggetto canUpdateProcedimentoOggetto(
      long idProcedimentoOggetto, boolean throwException)
      throws NemboPermissionException, InternalUnexpectedException;

  boolean hasDelega(long idIntermediario, long extIdAzienda,
      Date dataRiferimento) throws InternalUnexpectedException;

  boolean hasDelegaForProcedimento(long idIntermediario, long idProcedimento,
      Date dataRiferimento) throws InternalUnexpectedException;

  public Long getAziendaAgricolaProcedimento(long idProcedimento, Date date)
      throws InternalUnexpectedException;

  public ProcedimentoOggetto getProcedimentoOggetto(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public ProcedimentoOggetto getProcedimentoOggettoByIdProcedimento(
      long idProcedimento) throws InternalUnexpectedException;

  public List<GruppoInfoDTO> getDichiarazioniOggetto(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto)
      throws InternalUnexpectedException;

  public TestataProcedimento getTestataProcedimento(long idProcedimento)
      throws InternalUnexpectedException;

  public void updateDichiarazioniOAllegatiOggetto(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto,
      List<GruppoInfoDTO> dichiarazioni,
      long extIdUtenteAggiornamento) throws InternalUnexpectedException,
      NemboPermissionException, ApplicationException;

  public ProcedimentoDTO getIterProcedimento(long idProcedimento)
      throws InternalUnexpectedException;

  public List<AmmCompetenzaDTO> getAmmCompetenzaList(long idProcedimento)
      throws InternalUnexpectedException;

  public List<AziendaDTO> getAziendeProcedimentoList(long idProcedimento)
      throws InternalUnexpectedException;

  public ContoCorrenteEstesoDTO getContoCorrente(long idProcedimentoOggeto,
      Date dataRiferimento) throws InternalUnexpectedException;

  public List<ContoCorrenteDTO> getContiCorrentiValidi(long idProcedimento)
      throws InternalUnexpectedException;

  public void updateContoCorrenteOggetto(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto, long extIdUtenteAggiornamento,
      long idContoCorrente)
      throws InternalUnexpectedException, NemboPermissionException;

  public Map<Long, List<FileAllegatiDTO>> getMapFileAllegati(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<FonteControlloDTO> getControlliList(long idProcedimentoOggeto,
      Date dataRiferimento, boolean onlyFailed)
      throws InternalUnexpectedException;

  public List<FonteControlloDTO> getControlliList(long idProcedimentoOggeto,
      Date dataRiferimento, boolean onlyFailed, boolean acceptWarning)
      throws InternalUnexpectedException;

  public MainControlloDTO callMainControlli(long idBandoOggetto,
      long idProcedimentoOggetto, long idAzienda, long idUtenteLogin)
      throws InternalUnexpectedException;

  public void insertFileAllegati(long idProcedimentoOggetto,
      FileAllegatiDTO fileAllegatiDTO, byte[] fileContent)
      throws InternalUnexpectedException;

  void deleteFileAllegati(long idProcedimentoOggetto, long idFileAllegati)
      throws InternalUnexpectedException, ApplicationException;

  public ContenutoFileAllegatiDTO getFileFisicoAllegato(
      long idProcedimentoOggetto, long idFileAllegati)
      throws InternalUnexpectedException;

  public List<ElencoCduDTO> getElencoCdu() throws InternalUnexpectedException;

  public Long getIdAmmCompetenzaProcedimento(long idProcedimento)
      throws InternalUnexpectedException;

  public List<StampaOggettoDTO> getElencoStampeOggetto(
      long idProcedimentoOggetto, Long idOggettoIcona)
      throws InternalUnexpectedException;

  public List<StampaOggettoIconaDTO> getElencoDocumentiStampeDaAllegare(
      long idProcedimentoOggetto, Long extIdTipoDocumento)
      throws InternalUnexpectedException;

  public String getIdentificativo(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public String aggiungiOggettoStampa(ProcedimOggettoStampaDTO stampa, UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException;

  public ProcedimOggettoStampaDTO getProcedimOggettoStampaByIdOggetoIcona(
      long idProcedimentoOggetto, long idOggettoIcona)
      throws InternalUnexpectedException;

  public int deleteStampeOggettoInseriteDaUtente(long idProcedimentoOggetto,
      long idOggettoIcona)
      throws InternalUnexpectedException, ApplicationException;

  public Boolean ripristinaStampaOggetto(long idProcedimentoOggetto,
      long idOggettoIcona, long idUtenteLogin)
      throws InternalUnexpectedException;

  public Map<String, OggettoIconaDTO> getIconeTestata(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public AziendaDTO getDatiAziendaAgricolaProcedimento(long idProcedimento,
      Date date) throws InternalUnexpectedException;

  public DecodificaDTO<Integer> chiudiOggetto(long idProcedimentoOggetto,
      Long idEsito, String note, UtenteAbilitazioni utenteAbilitazioni)
      throws InternalUnexpectedException;

  public List<DecodificaInterventoDTO> getListInterventiByIdProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaSoggFirmatario> getListSoggettiFirmatari(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public boolean canDeleteProcedimentoOggetto(long idProcedimentoOggetto,
      long idBandoOggetto, boolean checkIstanza)
      throws InternalUnexpectedException;

  public MainControlloDTO callMainEliminazione(long idProcedimentoOggetto,
      long idUtenteLogin) throws InternalUnexpectedException;

  public MainControlloDTO callMainRiapertura(long idProcedimentoOggetto,
      String note, Long idUtenteLogin) throws InternalUnexpectedException;

  public String riaperturaProcedimentoOggetto(long idProcedimentoOggetto,
      long idUtente, String note) throws InternalUnexpectedException;

  public DelegaAnagrafeVO getDettaglioDelega(long idAzienda,
      Date dataRiferimento) throws InternalUnexpectedException;

  public String trasmettiIstanza(long idProcedimento, long idProcedimentoOggetto,long idBandoOggetto,long idQuadroOggettoDatiIdentificativi, String identificativoProcOggetto, UtenteAbilitazioni utenteAbilitazioni) throws InternalUnexpectedException;

  public byte[] getStampaProcedimOggettoStampaByIdOggetoIcona(long idProcedimentoOggetto, long idOggettoIcona) throws InternalUnexpectedException;

  
  public FonteControlloDTO getControlliListByIdFonteControllo(
      long idProcedimentoOggeto, long idFonteControllo, Date dataRiferimento,
      boolean onlyFailed)
      throws InternalUnexpectedException;

  public Long getIdDatiProcedimento(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public List<RaggruppamentoLivelloCriterio> getCriteriPunteggio(
      final long idOggetto, final long idDatiProcedimentoPunti, long idBando)
      throws InternalUnexpectedException;

  public List<RaggruppamentoLivelloCriterio> getCriteriPunteggioManuali(
      final long idProcedimento, long idBando)
      throws InternalUnexpectedException;

  public Long getIdDatiProcedimentoPunti(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public MainPunteggioDTO callMainCalcoloPunteggi(
      LogOperationOggettoQuadroDTO logOperation, long idProcedimento,
      long idProcedimentoOggetto, long idAzienda,
      long idBando, Long idLivello) throws InternalUnexpectedException;

  public void updateCriteriManuali(
		  LogOperationOggettoQuadroDTO logOperation,
		  Map<Long, BigDecimal> listaIdBandoLivelloCriterio, 
		  long idProcedimento,
		  long idBando, 
		  long idProcedimentoOggetto) throws InternalUnexpectedException, ApplicationException;

  public void updateCriteriIstruttoria(long idOggetto,
      LogOperationOggettoQuadroDTO logOperation,
      Map<Long, BigDecimal> listaIdBandoLivelloCriterio,
      long idProcedimento, 
      //parametri per aggiornare l'intervento
      long idBando, Long idDatiProcedimentoPunti, long idProcedimentoOggetto) throws InternalUnexpectedException, ApplicationException;

  public List<DecodificaDTO<Long>> getElencoTecniciDisponibili(
      long idProcedimentoOggetto) throws InternalUnexpectedException;
  
  public List<DecodificaDTO<Long>> getElencoTecniciDisponibili(long idProcedimentoOggetto, Boolean singoloUfficioZona, Long istruttoreGradoSup) throws InternalUnexpectedException;
  
  public List<DecodificaDTO<Long>> getElencoTecniciDisponibili(long idProcedimentoOggetto, Boolean singoloUfficioZona) throws InternalUnexpectedException;
  
  public List<DecodificaDTO<Long>> getElencoTecniciDisponibiliPerAmmCompetenza(long idProcedimentoOggetto, long idProcedimentoAgricolo)  throws InternalUnexpectedException;

  public EsitoFinaleDTO getEsitoFinale(long idProcedimentoOggetto,
      long idQuadroOggetto) throws InternalUnexpectedException;

  public void updateEsitoFinale(long idProcedimentoOggetto,
      long idQuadroOggetto, long idBandoOggetto, EsitoFinaleDTO esito,
      long idUtenteLogin)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoEsiti(String tipoEsito)
      throws InternalUnexpectedException;

  public List<ControlloAmministrativoDTO> getControlliAmministrativi(
      long idProcedimentoOggetto, String codiceQuadro, List<Long> ids)
      throws InternalUnexpectedException;

  DatiSpecificiDTO getDatiSpecifici(long idProcedimentoOggetto,
      long idProcedimento) throws InternalUnexpectedException;

  public void updateEsitiControlli(
      List<EsitoControlliAmmDTO> esitiControlliDaAggiornare,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  List<VisitaLuogoExtDTO> getVisiteLuogo(long idProcedimentoOggetto,
      long idQuadroOggetto, List<Long> ids) throws InternalUnexpectedException;

  public void insertVisitaLuogo(VisitaLuogoDTO visitaLuogoDTO,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public void eliminaVisiteLuogo(List<Long> ids,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  void updateEsitoTecnico(EsitoFinaleDTO esito,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public void updateVisiteLuogo(List<VisitaLuogoDTO> visiteDaAggiornare,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  DatiAnticipo getDatiAnticipo(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  List<DecodificaDTO<Integer>> getDecodificheBanche()
      throws InternalUnexpectedException;

  List<DecodificaDTO<Integer>> getDecodificheSportelli(int idBanca)
      throws InternalUnexpectedException;

  SportelloBancaDTO getSportelloBancaById(long idSportello)
      throws InternalUnexpectedException;

  void updateAnticipo(DatiAnticipoModificaDTO updateDTO,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public boolean isBeneficiarioAbilitatoATrasmettere(String codiceFiscale,
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public PartecipanteDTO findPartecipanteInAnagrafe(String cuaa)
      throws InternalUnexpectedException;

  public List<LivelloDTO> getLivelliProcOggetto(long idProcedimento)
      throws InternalUnexpectedException;

  public String findIstatComune(String comune)
      throws InternalUnexpectedException;

  public List<SettoriDiProduzioneDTO> getSettoriDiProduzioneProcedimentoOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<SettoriDiProduzioneDTO> getSettoriDiProduzioneInLivelliBandi(
      long idProcedimentoOggetto, long idBando)
      throws InternalUnexpectedException;

  public List<GraduatoriaDTO> getGraduatorieByIdProcedimento(
      long idProcedimento) throws InternalUnexpectedException;

  public boolean isOggettoIstanza(long idOggetto)
      throws InternalUnexpectedException;

  public boolean isCalcoloPuntiEseguito(long idProcedimento)
      throws InternalUnexpectedException;

  public Long getIdEsitoEquivalente(Long idEsito, String tipoEsito)
      throws InternalUnexpectedException;

  public List<SospensioneAnticipoDTO> getElencoSospensioniAnticipo(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<SospensioneAnticipoDTO> getElencoSospensioniAnticipoDisponibili(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void updateSospensioneAnticipo(long idProcedimentoOggetto,
      List<SospensioneAnticipoDTO> elenco,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public ProcedimentoEstrattoVO getProcedimentoEstratto(long idProcedimento,
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public LinkedList<LivelloDTO> getLivelliControlloInLoco(long idProcedimento,
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void eliminaControlloInLoco(long idProcedimentoOggetto,
      LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;

  public void inserisciRecordControlloInLoco(LivelloDTO livello,
      long idProcedimentoOggetto, LogOperationOggettoQuadroDTO logOperationDTO)
      throws InternalUnexpectedException;

  public void updateFlagEstrazione(long idProcedimentoEstratto)
      throws InternalUnexpectedException;

  public void eliminaSospensioneAnticipo(long idProcedimentoOggetto,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public boolean checkExtension(String extension)
      throws InternalUnexpectedException;

  public boolean checkNotifiche(long idProcedimento, Long idGravita,
      String attore) throws InternalUnexpectedException;

  public List<RigaRendicontazioneSpese> getElencoRendicontazioneSpese(
      long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException;

  Map<String, List<String>> getTestiStampeIstruttoria(String codiceCdu,
      long idBandoOggetto) throws InternalUnexpectedException;

  public List<ImportoLiquidatoDTO> getElencoImportiLiquidazione(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<RipartizioneImportoDTO> getRipartizioneImporto(
      long idListaLiquidazImpLiq) throws InternalUnexpectedException;

  public ImportoLiquidatoDTO getElencoImportiLiquidazioneByIdImportoLiquidato(
      long idImportoLiquidato) throws InternalUnexpectedException;

  public List<RiduzioniSanzioniDTO> getElencoRiduzioniSanzioni(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getSanzioniInvestimento(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getTipologieSanzioni(
      long idProcedimentoOggetto, boolean escludiSanzioniAutomatiche)
      throws InternalUnexpectedException;

  public void inserisciSanzioneRiduzione(Long idTipologia, Long idOperazione,
      Long idDescrizione, String note, BigDecimal importo,
      Long idProcedimentoOggetto,
      String tipoPagamentoSigop,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public void eliminaRiduzioneSanzione(List<Long> ids,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      Long idProcedimentoOggetto,
      String tipoPagamentoSigop) throws InternalUnexpectedException;

  public void modificaRiduzioneSanzione(List<RiduzioniSanzioniDTO> riduzioni,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      Long idProcedimentoOggetto, String tipoPagamentoSigop)
      throws InternalUnexpectedException;

  void updateOrInsertControlliInLocoInvest(
      ControlliInLocoInvestDTO controlliInLocoInvestDTO,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public EstrazioneCampioneDTO callEseguiSimulazioneEstrazCampione(
      long idTipoEstrazione) throws InternalUnexpectedException;

  public List<EstrazioneACampioneDTO> getElencoEstrazioniACampione(
      boolean isUtenteGal) throws InternalUnexpectedException;

  public List<NumeroLottoDTO> getElencoNumeroLotti()
      throws InternalUnexpectedException;

  public List<RigaFiltroDTO> getFiltroElencoStatoEstrazioniCampione()
      throws InternalUnexpectedException;

  public List<RigaFiltroDTO> getFiltroElencoTipoEstrazioniCampione()
      throws InternalUnexpectedException;

  public List<SegnapostoDTO> getSegnapostiStampe()
      throws InternalUnexpectedException;

  public String getValoreSegnaposto(SegnapostoDTO segnapostoDTO,
      long idProcedimento, long idProcedimentoOggetto)
      throws InternalUnexpectedException, ApplicationException;

  public List<RigaSimulazioneEstrazioneDTO> getElencoRisultatiSimulazione(
      long idTipoEstrazione) throws InternalUnexpectedException;

  public BigDecimal calcolaImportoMaxRiduzioneSanzione(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public BigDecimal calcolaImportoParzialeRiduzioneSanzione(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public String approvaIstanza(long idProcedimento, long idProcedimentoOggetto, long idBandoOggetto, String identificativoProcOggetto, UtenteAbilitazioni utenteAbilitazioni,long idQuadroOggettoDatiIdentificativi)
      throws InternalUnexpectedException;

  public List<StampaOggettoIconaDTO> getElencoDocumentiStampeDaVerificare(
      long idProcedimentoOggetto, Long extIdTipoDocumento, Long idCategoriaDoc)
      throws InternalUnexpectedException;

  public List<DataFineLavoriDTO> getElencoDateFineLavori(long idProcedimento,
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void aggiornaDataFineLavori(long idDataFineLavori,
      DataFineLavoriDTO dataFineLavoriDTO,
      LogOperationOggettoQuadroDTO logOperation)
      throws InternalUnexpectedException;

  public void inserisciDataFineLavori(DataFineLavoriDTO dataFineLavoriDTO,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public void deleteDataFineLavori(long idFineLavori,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public DataFineLavoriDTO getLastDataFineLavori(long idProcedimento)
      throws InternalUnexpectedException;

  public List<LivelloDTO> getLivelliImpegnoProcOggetto(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public Date getMinDataProroga(long idProcedimento)
      throws InternalUnexpectedException;

  public GiustificazioneAnomaliaDTO getGiustificazioneAnomalia(long idControllo,
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public ControlloDTO getControllo(long idControllo, long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getTipiRisoluzioneControlli(long idControllo)
      throws InternalUnexpectedException;

  public String getFlagObbligatorieta(Long idTipoRisoluzione, String string)
      throws InternalUnexpectedException;

  public void inserisciGiustificazioneAnomaliaControllo(
      Long idSoluzioneAnomalia, long idControllo, long idProcedimentoOggetto,
      Long idTipoRisoluzione,
      String note, MultipartFile fileAllegato, String nomeFile,
      String nomeAllegato, Long idUtenteLogin, String prov, Date dataProtocollo,
      Date dataDocumento,
      String numProtocollo) throws InternalUnexpectedException, IOException;

  public void eliminaGiustificazione(long idSoluzioneAnomalia)
      throws InternalUnexpectedException;

  public FileAllegatoDTO getAllegatoGiustificazione(long idSoluzioneAnomalia)
      throws InternalUnexpectedException;

  public EsitoFinaleDTO getEsitoFinale(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public void aggiornaEsitoFinale(long idProcedimentoOggetto, long idEsito,
      long extIdFunzionarioIstruttore,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public boolean findOggettoByIdProcedimento(long idProcedimento,
      String codiceOggetto) throws InternalUnexpectedException;

  public String getValoreParametroFunzSupEsitoVerbale()
      throws InternalUnexpectedException;

  public String getTipoPagamentoSigopOggetto(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public String getValoreParametroDichiarazioneByCodice(
      long idProcedimentoOggetto, String codiceInfo)
      throws InternalUnexpectedException;

  public Long getLastIstanzaWithPremio(long idProcedimento)
      throws InternalUnexpectedException;

  public String getResponsabileProcedimento(long idProcedimento)
      throws InternalUnexpectedException;

  public boolean isTipoGiustificazioneAntimafia(long idTipoSoluzione)
      throws InternalUnexpectedException;

  public boolean isIstruttoriaInCorsoMisuraPremio(long idProcedimento)
      throws InternalUnexpectedException;

  public String getInfoAziendaByCuaa(String cuaa)
      throws InternalUnexpectedException;

  public void updateVoltura(VolturaDTO voltura, long idProcedimentoOggetto,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public VolturaDTO getVoltura(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public List<AziendaDTO> getInfoAziendaByDenominazione(
      String denominazioneAzienda) throws InternalUnexpectedException;

  public List<AziendaDTO> getAziendeByCuaa(String cuaa)
      throws InternalUnexpectedException;

  public AziendaDTO getAziendaById(long idAzienda)
      throws InternalUnexpectedException;

  public boolean isQuadroAttestazioneVisible(long idOggetto)
      throws InternalUnexpectedException;

  public boolean isUtenteConflitto(String codiceFiscale, String cuaaAzienda)
      throws InternalUnexpectedException;

  public ResponsabileDTO getResponsabileCAA(long idIntermediarioCAA, int idProcedimentoAgricolo)
      throws InternalUnexpectedException;

  public OrganismoDelegatoDTO getOrganismoDelegato(long idProcedimento)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoUfficiZona(long idAmmCompetenza)
      throws InternalUnexpectedException;

  public void updateOrganismoDelegato(Vector<Long> idProcedimentiSelezionati,
      long idAmministrazione, Long idUfficiozona, long idUtenteLogin,
      String note,
      Long idTecnico) throws InternalUnexpectedException;

  public List<RigaAnticipoLivello> getListAnticipoLivello(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void unsplitRiduzioneSanzione(long idProcOggSanzione,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      RiduzioniSanzioniDTO riduzioniSanzioniDTO)
      throws InternalUnexpectedException;

  public void splitRiduzioneSanzione(RiduzioniSanzioniDTO riduzione,
      long idProcOggSanzione,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      BigDecimal importoA,
      BigDecimal importoB, String noteA, String noteB, String codice)
      throws InternalUnexpectedException;

  public String getDescrizioneSanzioneSplit(String codice)
      throws InternalUnexpectedException;

  public RiduzioniSanzioniDTO getRiduzioneSanzione(long idProcOggSanzione,
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  boolean isCodiceLivelloInvestimentoEsistente(long idProcedimento,
      String codiceLivello) throws InternalUnexpectedException;

  public List<ProspettoEconomicoDTO> getProspettoEconomico(long idProcedimento)
      throws InternalUnexpectedException;

  public Long getAziendaRichiestaVoltura(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public String getCodiceOggetto(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public List<IntegrazioneAlPremioDTO> getIntegrazioneAlPremio(
      long idProcedimento, long idProcedimentoOggetto, List<Long> ids)
      throws InternalUnexpectedException;

  public void updateIntegrazionePremio(ProcedimentoOggetto procedimentoOggetto,
      List<IntegrazioneAlPremioDTO> integrazione,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public boolean isAmmissioneFuoriLinea(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public String getProcedimentoOggEstrattoCampioneDescr(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public String getProcedimentoEstrattoCampioneExPostDescr(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public String getProcedimentoEstrattoCampioneDescr(long idProcedimento)
      throws InternalUnexpectedException;

  public ProcedimentoDTO getIterProcedimento(long idProcedimento,
      long codiceRaggruppamento) throws InternalUnexpectedException;

  public String checkFlagAltreInfoAtto(long idTipoAtto)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getElencoAtti()
      throws InternalUnexpectedException;

  public boolean isEsisteImpBaseByCodiceLivello(long idProcedimentoOggetto,
      String codiceLivello) throws InternalUnexpectedException;

  public boolean isDataAmmissioneDaGestire(long idProcedimento)
      throws InternalUnexpectedException;

  public void updateDataAmmissione(long idProcedimento, Date dataAmmissione)
      throws InternalUnexpectedException;

  public Date findDataProtocolloLettera(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getTecniciByUfficioDiZona(Long idUfficioZona, int idProcedimentoAgricolo)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> geElencoTipiDocumenti()
      throws InternalUnexpectedException;

  public TipoDocumentoSpesaVO getDettagioTipoDocumento(
      long idTipoDocumentoSpesa) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> geElencoFornitori()
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> geElencoModalitaPagamento()
      throws InternalUnexpectedException;

  public void inserisciInterventoSpesa(long idDocumentoSpesa,
      List<ImportoInterventoVO> importi, long idUtente)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> geElencoFornitori(String piva)
      throws InternalUnexpectedException;

  public long inserisciFornitore(FornitoreDTO fornitore,
      boolean updateDocumento, Long idDettDocuemntoSpesa)
      throws InternalUnexpectedException;

  public FornitoreDTO getDettaglioFornitore(long idFornitore)
      throws InternalUnexpectedException;

  public void eliminaDocumentoSpesaById(long idDocumentoSpesa)
      throws InternalUnexpectedException;

  public DocumentoSpesaVO getDettagioDocumentoSpesa(long idDocumentoSpesa)
      throws InternalUnexpectedException;

  public void aggiornaDocumentoSpesa(long idDettDocumentoSpesa,
      DocumentoSpesaVO documentoVO) throws InternalUnexpectedException;

  public void aggiornaInterventoSpesa(long idProcedimentoOggetto,
      long idDocumentoSpesa, List<ImportoInterventoVO> importi)
      throws InternalUnexpectedException;

  public BigDecimal getImportoIntervento(long idDocumentoSpesa,
      long idIntervento) throws InternalUnexpectedException;

  public FileAllegatoDTO getFileAllegatoDocSpesa(long idDocumentoSpesa)
      throws InternalUnexpectedException;

  public Boolean checkSeIProcSelezHannoLaStessaAmm(Vector<Long> vect)
      throws InternalUnexpectedException;

  public boolean checkStatiOggetti(Vector<Long> vect)
      throws InternalUnexpectedException;

  public boolean checkSeIProcSelezAppartengonoAlloStessoBando(Vector<Long> vect)
      throws InternalUnexpectedException;

  public EsitoFinaleDTO getEsitoDefinitivo(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public boolean checkIfMotivazioniObbligatorieEsito(Long idEsito)
      throws InternalUnexpectedException;

  public void updateEsitoDefinitivo(long idProcedimentoOggetto,
      EsitoFinaleDTO esito,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO,
      long idUtenteLogin) throws InternalUnexpectedException;

  public String getCuaaByIdProcedimentoOggetto(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public List<ImportoLiquidatoDTO> getListeDegliImportiLiquidazione(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void updateGiustificazioneAnomaliaControllo(Long idSoluzioneAnomalia,
      long idControllo, long idProcedimentoOggetto, Long idTipoRisoluzione,
      String note, MultipartFile fileAllegato, String nomeFile,
      String nomeAllegato, Long idUtenteLogin, String prov, Date dataProtocollo,
      Date dataDocumento, String numProtocollo, boolean maintainOldFile)
      throws InternalUnexpectedException, IOException;

  public boolean checkIfProcHaveSameUfficioZona(
      Vector<Long> idProcedimentiSelezionati)
      throws InternalUnexpectedException;

  public boolean checkIfProcHaveSameTecnico(
      Vector<Long> idProcedimentiSelezionati)
      throws InternalUnexpectedException;

  public void aggiornaFlagRendizontazione(String flagRendicontazione,
      long idProcedimento) throws InternalUnexpectedException;

  public List<Long> searchDocumentoSpesaByKey(DocumentoSpesaVO documentoVO)
      throws InternalUnexpectedException;

  public long inserisciDocumentoSpesa(long idProcedimento,
      String firstCodiceAttore, DocumentoSpesaVO documentoVO)
      throws InternalUnexpectedException;

  public FornitoreDTO getDettaglioFornitoreByPiva(String piva)
      throws InternalUnexpectedException;

  public long storicizzaFornitore(long idFornitore,
      FornitoreDTO fornitoreTributaria) throws InternalUnexpectedException;

  public BandoDTO getInformazioniBandoByProcedimento(long idProcedimento)
      throws InternalUnexpectedException;

  public String getCodiceAmmCompetenza(long extIdAmmComp)
      throws InternalUnexpectedException;

  public List<DocumentoSpesaVO> getElencoDocumentiSpesa(long idProcedimento,
      long[] idsDocSpesa, long idIntervento) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> geElencoFornitoriProcedimento(
      long idProcedimento) throws InternalUnexpectedException;

  public List<RigaRendicontazioneDocumentiSpesaDTO> getListInterventiByIdDocumentoSpesa(
      long idDocumentoSpesa) throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> geElencoFornitoriRicercaFatture(String CUAA,
      String denominazione) throws InternalUnexpectedException;

  public Boolean isComunicazionePagamento(long idOggetto, String cuName)
      throws InternalUnexpectedException;

  public boolean findFornitore(FornitoreDTO fornitore)
      throws InternalUnexpectedException;

  public BigDecimal findImportoRendicontaTO(long idDocumentoSpesa,
      long idIntervento) throws InternalUnexpectedException;

  public String getStatoOggetto(long idProcedimento)
      throws InternalUnexpectedException;

  public long updateOrInsertDatiDocumentoSpesa(long idProcedimento,
      String codAttore, DocumentoSpesaVO documentoVO)
      throws InternalUnexpectedException;

  public List<String> getMacroCDUList(long idLegameGruppoOggetto)
      throws InternalUnexpectedException;

  public int insertLog(String descrizione) throws InternalUnexpectedException;

  String getFlagEstratta(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public boolean isPrimoOggettoDelGruppo(long idProcedimento,
      long codiceRaggruppamento) throws InternalUnexpectedException;

  public boolean hasTecnico(long idProcedimento)
      throws InternalUnexpectedException;

  public void storicizzaProcedimentoOggetto(
      ProcedimentoOggetto procedimentoOggetto, Long extIdUtenteAggiornamento)
      throws InternalUnexpectedException;

  public boolean isSoggettoAdApprovazione(Long idOggetto)
      throws InternalUnexpectedException;

  public void deletedocSpesaIntervento(long[] idDocumentoSpesa,
      long idIntervento) throws InternalUnexpectedException;

  public boolean bandoHasLivWithVERCOD(long idBando)
      throws InternalUnexpectedException;

  public boolean isEsitoApprovato(long idEsito)
      throws InternalUnexpectedException;

  public String getFlagEstrattaControlliDichiarazione(long idProcedimento,
      long codiceRaggruppamento, long idProcedimentoOggetto,
      String codiceTipoBando)
      throws InternalUnexpectedException;

  public List<RicevutaPagamentoVO> getElencoRicevutePagamento(
      long[] idsDocumentoSpesa) throws InternalUnexpectedException;

  public void updateOrInsertRicevutaPagamento(long[] idsDocuemntiSpesa,
      RicevutaPagamentoVO ricevuta, String codiceAttore)
      throws InternalUnexpectedException;

  public RicevutaPagamentoVO getRicevutaPagamento(long idDettRicevutaPagamento)
      throws InternalUnexpectedException;

  public void eliminaRicevutaSpesaById(long idDettRicevutaPagamento)
      throws InternalUnexpectedException;

  public boolean controlloSommaRicevute(DocumentoSpesaVO documento,
      RicevutaPagamentoVO ricevuta, String flagRendicontazioneConIva)
      throws InternalUnexpectedException;

  public boolean canDeleteRicevuta(long idDettRicevutaPagamento)
      throws InternalUnexpectedException;

  public void inserisciImportiInterventoDocRicevuta(
      List<DettaglioInterventoDTO> listaInterventi,
      long extIdUtenteAggiornamento)
      throws InternalUnexpectedException;

  public BigDecimal getImportoDocSpesaIntRic(long idIntervento,
      long idDocumentoSpesa, long idRicevutaPagamento)
      throws InternalUnexpectedException;

  public BigDecimal getImportoGiaAssociato(long idIntervento,
      long idDocumentoSpesa, long idRicevutaPagamento)
      throws InternalUnexpectedException;

  public Boolean canModifyRendicontazioneIva(long idProcedimento)
      throws InternalUnexpectedException;

  public List<ExcelRicevutePagInterventoDTO> getElencoExcelRicevutePagamentoIntervento(
      long idProcedimento) throws InternalUnexpectedException;

  public List<ExcelRigaDocumentoSpesaDTO> getElencoExcelRicevutePagamento(
      long idProcedimento) throws InternalUnexpectedException;

  public List<RegistroAntimafiaDTO> getDatiGeneraliRegistroAntimafia(
      String cuaa, String denominazione) throws InternalUnexpectedException;

  public List<RegistroAntimafiaDTO> getDatiTitolareRegistroAntimafia(
      String cuaa, String denominazione) throws InternalUnexpectedException;

  public List<RegistroAntimafiaDTO> getDatiCertificatoRegistroAntimafia(
      String cuaa, String denominazione) throws InternalUnexpectedException;

  public BigDecimal getImportoDocSpesaIntRicPag(long idRicevutaPagamento)
      throws InternalUnexpectedException;

  public boolean canDeleteDocSpesa(long idDocumentoSpesa)
      throws InternalUnexpectedException;

  public void eliminaAllegatiDocumentoSpesaById(long idDocumentoSpesa)
      throws InternalUnexpectedException;

  public List<DocumentoSpesaVO> getElencoDocumentiSpesa(long idProcedimento,
      ArrayList<String> idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato,
      String flagRendicontazioneIva) throws InternalUnexpectedException;

  public void deleteRDocSpesaIntRicPag(long[] idsDocSpesa, long idInt)
      throws InternalUnexpectedException;

  public String showIconDocGiaRendicontato(DocumentoSpesaVO doc,
      String flagRendicontazioneConIva) throws InternalUnexpectedException;

  public DocumentoSpesaVO getDettagioDocumentoSpesaGiaRendicontato(
      long idDocumentoSpesa) throws InternalUnexpectedException;

  public void insertOrUpdateFileAllegatoDocSpesaGiaRendicontato(
      DocumentoSpesaVO documentoVO, String codAttore, Long idDocumentoSpesaFile)
      throws InternalUnexpectedException;

  public List<DocumentoSpesaVO> getElencoAllegatiDocSpesa(long idDocumentoSpesa,
      ArrayList<String> idsDegliOggettiPerCuiVisualizzareAllegatiESommareImportoRendicontato)
      throws InternalUnexpectedException;

  public boolean canDeleteRicevutaDocGiaRendicontato(
      long idDettRicevutaPagamento) throws InternalUnexpectedException;

  public BigDecimal getImportoAssociatoDoc(long idDocumentoSpesa)
      throws InternalUnexpectedException;

  public boolean ricEsisteInDocSpesIntRicPag(long idRicevutaPagamento)
      throws InternalUnexpectedException;

  public BigDecimal getImportoPagamentoLordo(long idDocumentoSpesa)
      throws InternalUnexpectedException;

  public boolean canDeleteAndDeleteFile(long idDocumentoSpesaFile)
      throws InternalUnexpectedException;

  public boolean docHasInterventi(long idDocSpesa)
      throws InternalUnexpectedException;

  public boolean ricEsisteInDocSpesRicPag(long idRicevutaPagamento)
      throws InternalUnexpectedException;

  public Long getIdRicevutaPagamento(long idDettRicevutaPagamento)
      throws InternalUnexpectedException;

  public List<DocumentoSpesaVO> getElencoAllegatiIdIntervento(
      long idDocumentoSpesa, long idIntervento)
      throws InternalUnexpectedException;

  public boolean canDeleteAllegato(long idDocumentoSpesaFile)
      throws InternalUnexpectedException;

  public List<RegistroAntimafiaDTO> getDatiAziendaCertificatoRegistroAntimafia(
      String cuaa, String denominazione) throws InternalUnexpectedException;

  public List<RicevutaPagamentoVO> getElencoRicevutePagamentoDomande(
      long idDocumentoSpesa,
      ArrayList<Long> idsDomande) throws InternalUnexpectedException;

  public List<ExcelRicevutePagInterventoDTO> getElencoExcelRicevutePagamentoInterventoPerDomanda(
      long idProcedimento, long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public boolean canUseDocumentiSpesa(long idProcedimento, long idBando)
      throws InternalUnexpectedException;

  public long contaInterventiRendicontazConFlagS(long idProcedimento,
      long idBando) throws InternalUnexpectedException;

  public DatiSpecificiDTO getDatiSpecificiExPost(long idProcedimentoOggetto,
      long idProcedimento) throws InternalUnexpectedException;

  public List<ControlloAmministrativoDTO> getControlliAmministrativiExPost(
      long idProcedimentoOggetto, String codiceQuadro, List<Long> ids)
      throws InternalUnexpectedException;

  public void updateOrInsertControlliInLocoExPost(
      ControlliInLocoInvestDTO controlliInLocoInvestDTO,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public InfoExPostsDTO getInformazioniExposts(long idProcedimentoOggetto)
      throws InternalUnexpectedException;

  public List<DecodificaDTO<Long>> getDecodificheRischioElevato()
      throws InternalUnexpectedException;

  public List<AnnoExPostsDTO> getDecodificheAnnoExpost(
      long idProcedimentoOggetto) throws InternalUnexpectedException;

  public void updateInfoExPost(InfoExPostsDTO info,
      LogOperationOggettoQuadroDTO logOperationOggettoQuadroDTO)
      throws InternalUnexpectedException;

  public boolean procedimentoHasOggettoInStato(long idProcedimento,
      String codiceOggetto, long idStatoOggetto)
      throws InternalUnexpectedException;
  public List<ProcedimentoAgricoloDTO> getProcedimentiAgricoli()
      throws InternalUnexpectedException;

  boolean verificaEsistenzaProcedimentoAgricolo(int idProcedimentoAgricolo)
      throws InternalUnexpectedException;

public ContenutoFileAllegatiDTO getImmagineDaVisualizzare(long idProcedimentoAgricolo) throws InternalUnexpectedException;

public byte[] caricaTemplate(String codice)throws InternalUnexpectedException;

public DecodificaDTO<String> findDatiCaa(long idProcedimentoOggetto) throws InternalUnexpectedException;  

public Map<String, String> getParametriBandoOggetto(long idBandoOggetto, String[] paramNames) throws InternalUnexpectedException;

public SegnalazioneDannoDTO getSegnalazioneDanno(long idProcedimentoOggetto, List<Long> ids)throws InternalUnexpectedException;

public void deleteSegnalazioniDanno(long idSegnalazioneDanno)throws InternalUnexpectedException;

public void updateSegnalazioniDanno(List<RigaSegnalazioneDannoDTO> elenco)throws InternalUnexpectedException;

public void updateDatiGeneraliDanno(long idProcedimentoOggetto, String descrizione, String data)throws InternalUnexpectedException; 
}
