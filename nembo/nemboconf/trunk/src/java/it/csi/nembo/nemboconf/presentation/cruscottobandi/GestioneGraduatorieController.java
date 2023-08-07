package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.csi.nembo.nemboconf.business.ICruscottoBandiEJB;
import it.csi.nembo.nemboconf.dto.cruscottobandi.BandoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.FileAllegatoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.GraduatoriaDTO;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.presentation.BaseController;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;

@Controller
@RequestMapping("/cruscottobandi")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class GestioneGraduatorieController extends BaseController
{

  @Autowired
  private ICruscottoBandiEJB cruscottoEJB = null;

  @RequestMapping(value = "elencograduatorie_{idBando}")
  public String index(Model model, HttpSession session)
      throws InternalUnexpectedException
  {
    return "cruscottobandi/elencoGraduatorie";
  }

  @RequestMapping(value = "getElencoGraduatorie", produces = "application/json")
  @ResponseBody
  public List<GraduatoriaDTO> getGraduatorieJson(Model model,
      HttpSession session) throws InternalUnexpectedException
  {
    BandoDTO bando = (BandoDTO) session.getAttribute("bandoGestEconomica");
    List<GraduatoriaDTO> graduatorie = cruscottoEJB
        .getGraduatorieBando(bando.getIdBando());

    for (GraduatoriaDTO g : graduatorie)
    {
      List<FileAllegatoDTO> allegati = cruscottoEJB
          .getAllegatiGraduatoria(g.getIdGraduatoria());
      g.setElencoAllegati(allegati);
    }

    return graduatorie;
  }

  @RequestMapping(value = "downloadgraduatoria_{idAllegatiGraduatoria}", method = RequestMethod.GET)
  public ResponseEntity<byte[]> downloadAllegato(
      @PathVariable("idAllegatiGraduatoria") long idAllegatiGraduatoria,
      HttpSession session) throws IOException, InternalUnexpectedException
  {
    FileAllegatoDTO file = cruscottoEJB
        .getFileAllegatoGraduatoria(idAllegatiGraduatoria);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type",
        NemboUtils.FILE.getMimeType(file.getNomeFile()));
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + file.getNomeFile() + "\"");
    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
        file.getFileAllegato(), httpHeaders, HttpStatus.OK);
    return response;
  }
}
