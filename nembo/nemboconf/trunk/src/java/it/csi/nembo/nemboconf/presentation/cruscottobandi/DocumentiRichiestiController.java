package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.DocumentiRichiestiDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoOggettoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.OggettiIstanzeDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.QuadroDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class DocumentiRichiestiController extends BaseController {

	@Autowired
	private ICruscottoBandiEJB cruscottoEJB = null;

	@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
	@RequestMapping(value = "documentirichiesti", method = RequestMethod.GET)
	public String ricevutaGet(ModelMap model, HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException 
	{
		BandoDTO bando = (BandoDTO) session.getAttribute("bando");
		long idBando = bando.getIdBando();
		List<GruppoOggettoDTO> elenco = cruscottoEJB.getElencoGruppiControlliDisponibili("N",idBando);
		
		model.addAttribute("elencoGruppiOggetto", elenco);
		model.addAttribute("idBando", idBando);
		return "cruscottobandi/documentirichiesti";
	}
	

	@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
	@RequestMapping(value = "visualizza_documenti_richiesti", method = RequestMethod.POST)
	public String visualizzaDocumentiRichiesti(ModelMap model, HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException 
	{
		BandoDTO bando = (BandoDTO) session.getAttribute("bando");
		long idBando = bando.getIdBando();
		String idUnivoco = request.getParameter("selGruppoOggetto");
		String[] arrayIdUnivoco = idUnivoco.split("&&");
		long idGruppoOggetto = Long.parseLong(arrayIdUnivoco[0]);
		long idOggetto = Long.parseLong(arrayIdUnivoco[1]);
		
		List<DocumentiRichiestiDTO> documenti = cruscottoEJB.getDocumentiRichiesti(idBando, idOggetto, idGruppoOggetto);
		List<GruppoOggettoDTO> elenco = cruscottoEJB.getElencoGruppiControlliDisponibili("N",idBando);
		model.addAttribute("elencoGruppiOggetto", elenco);
		model.addAttribute("idBando", idBando);
		model.addAttribute("documenti",documenti);
		model.addAttribute("idGruppoOggettoSelezionato",idUnivoco);
		gestisciModificaDati(model, session, idBando, idGruppoOggetto, idOggetto);
		return "cruscottobandi/documentirichiesti";
	}



	private void gestisciModificaDati(ModelMap model, HttpSession session, long idBando, long idGruppoOggetto,
			long idOggetto) throws InternalUnexpectedException {
		List<GruppoOggettoDTO> dettaglioOggetti = cruscottoEJB.getElencoGruppiOggetti(idBando, "N");
		long idBandoOggetto = cruscottoEJB.getIdBandoOggetto(idBando, idOggetto, idGruppoOggetto) ;
		OggettiIstanzeDTO oggettoSelezionato = null;
		GruppoOggettoDTO gruppoOggettoSelezionato = null;
        
		boolean withDocri = verificaBandoOggettoQuadro(model, idBando, idGruppoOggetto, idOggetto, NemboConstants.QUADRO.CODICE.DOCUMENTI_RICHIESTI,"withDocri");
		

			if (dettaglioOggetti != null)
			{
				for (GruppoOggettoDTO g : dettaglioOggetti)
				{
					List<OggettiIstanzeDTO> oggetti = g.getOggetti();
					
					if (oggetti != null)
					{
						for (OggettiIstanzeDTO o : oggetti)
						{
							if (o.getIdBandoOggetto() == idBandoOggetto)
							{
								oggettoSelezionato = o;
								gruppoOggettoSelezionato = g;
								boolean bandoOggettoModificabile = NemboConstants.FLAGS.NO
										.equals(o.getFlagAttivo());
								boolean isReadWrite = getUtenteAbilitazioni(session).isReadWrite();
//								if(withDocri)
//								{
//								}
								model.addAttribute("canUpdate",
										bandoOggettoModificabile && isReadWrite);
								model.addAttribute("modificabile",
										Boolean.valueOf(bandoOggettoModificabile));
								model.addAttribute("descrOggettoSelezionato",
										gruppoOggettoSelezionato.getDescrGruppo() + " - "
												+ o.getDescrOggetto());
							}
						}
					}
				}
			}
	}


	/**
	 * 
	 * @param model
	 * @param idBando
	 * @param idGruppoOggetto
	 * @param idOggetto
	 * @param codiceQuadro
	 * @param modelVariable
	 * @return
	 * @throws InternalUnexpectedException
	 * 
	 * Verifica se nel bando specificato da idBando sia stato attivato l'oggetto identificato dalla coppia idOggetto idGruppoOggetto 
	 * Se speficiata la variabile modelVariable, nel caso in cui i 
	 */
	public boolean verificaBandoOggettoQuadro(ModelMap model, long idBando, long idGruppoOggetto,
			long idOggetto, String codiceQuadro, String modelVariable) throws InternalUnexpectedException {
		List<QuadroDTO> quadri = cruscottoEJB.getElencoQuadriDisponibili(idBando, idGruppoOggetto, idOggetto);
		boolean withQuadro = false;
		for(int i=0; i<quadri.size() && !withQuadro; i++)
		{
			QuadroDTO quadro = quadri.get(i);
			if(codiceQuadro.equals(quadro.getCodice())
					&& quadro.isQuadroEsistente())
			{
				withQuadro = true;
			}
		}
		if(modelVariable != null)
		{
			model.addAttribute(modelVariable,withQuadro);
		}
		return withQuadro;
	}
	
	@RequestMapping(value = "inserisci", method = RequestMethod.POST)
	public String inserisci(ModelMap model, HttpServletRequest request, HttpSession session)
			throws InternalUnexpectedException 
	{
		BandoDTO bando = (BandoDTO) session.getAttribute("bando");
		long idBando = bando.getIdBando();
		String idUnivoco = request.getParameter("idGruppoOggettoSelezionato");
		long idOggetto = Long.parseLong(idUnivoco.split("&&")[1]);
	    long idGruppoOggetto = Long.parseLong(idUnivoco.split("&&")[0]);
	    long[] idsTipoDocRicOggetto = NemboUtils.ARRAY.toLong(request.getParameterValues("idTipoDocRicOggetto"));
	    	
		gestisciModificaDati(model, session, idBando, idGruppoOggetto, idOggetto);
		Boolean canUpdate = (Boolean)model.get("canUpdate");
		Boolean modificabile = (Boolean)model.get("modificabile");
		boolean withDocri = verificaBandoOggettoQuadro(model, idBando, idGruppoOggetto, idOggetto,
				NemboConstants.QUADRO.CODICE.DOCUMENTI_RICHIESTI,"withDocri");
		if(withDocri)
		{
			//controllo se il bando è attivo
			if(!Boolean.TRUE.equals(modificabile))
			{
				model.addAttribute("errBandoAttivo",Boolean.TRUE);
			}
			else
			{
				
				if(!Boolean.TRUE.equals(canUpdate))
				{
					model.addAttribute("errCanUpdate", Boolean.TRUE);
				}
				else
				{
					boolean ok = cruscottoEJB.deleteInsertTipoDocRicBandoOgg(idBando, idOggetto, idGruppoOggetto, idsTipoDocRicOggetto);
					if(!ok)
					{
						model.addAttribute("errProcOggPresenti",Boolean.TRUE);
					}
				}
			}
			List<DocumentiRichiestiDTO> documenti = cruscottoEJB.getDocumentiRichiesti(idBando, idOggetto, idGruppoOggetto);
			model.addAttribute("documenti",documenti);
		}
		List<GruppoOggettoDTO> elenco = cruscottoEJB.getElencoGruppiControlliDisponibili("N",idBando);
		model.addAttribute("elencoGruppiOggetto", elenco);
		model.addAttribute("idBando", idBando);
		model.addAttribute("idGruppoOggettoSelezionato", idUnivoco);
		return "cruscottobandi/documentirichiesti";
	}
	

}
