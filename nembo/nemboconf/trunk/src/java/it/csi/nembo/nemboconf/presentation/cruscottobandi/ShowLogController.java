package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.LogRecordDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.UtenteLogin;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class ShowLogController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @RequestMapping(value = "showLog_{idBando}", method = RequestMethod.GET)
  public String showLog(ModelMap model, HttpServletRequest request,
      HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
    model.addAttribute("idBando", idBando);

    model.addAttribute("denominazioneBando",
        cruscottoEJB.getInformazioniBando(idBando).getDenominazione());

    return "cruscottobandi/showLog";
  }

  @RequestMapping(value = "getLog_{idBando}", produces = "application/json")
  @Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
  @ResponseBody
  public List<LogRecordDTO> getGraduatorieJson(Model model, HttpSession session,
      @PathVariable("idBando") long idBando) throws InternalUnexpectedException
  {
    List<LogRecordDTO> records = cruscottoEJB.readLog(idBando);
    Map<Long, String> mapUtenti = new HashMap<>();

    for (LogRecordDTO r : records)
    {

      if (!mapUtenti.containsKey(r.getExtIdUtenteAggiornamento()))
      {
        List<Long> idUtenti = new ArrayList<Long>();
        idUtenti.add(r.getExtIdUtenteAggiornamento());
        List<UtenteLogin> utentiList = loadRuoloDescr(idUtenti);
        String descr = super.getUtenteDescrizione(
            r.getExtIdUtenteAggiornamento(), utentiList);
        r.setUtente(descr);
        mapUtenti.put(r.getExtIdUtenteAggiornamento(), descr);
      }
      else
      {
        r.setUtente(mapUtenti.get(r.getExtIdUtenteAggiornamento()));
      }

    }

    return records;
  }

}
