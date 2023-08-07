package it.csi.nembo.nemboconf.presentation.cruscottobandi;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.nembo.nemboconf.util.stampa.Stampa;

@Controller
@RequestMapping("/cruscottobandi/stampa_{idBandoOggetto}_{cuName}")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_WRITE, macroCDU = Security.GESTISCI_CRUSCOTTO_BANDI)
public class CUNEMBO016StampeOggettiDaCruscotto
{

  @RequestMapping("/index")
  protected ResponseEntity<byte[]> stampaByCUName(HttpSession session,
      @PathVariable("idBandoOggetto") long idBandoOggetto,
      @PathVariable("cuName") String cuName) throws Exception
  {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "application/pdf");
    Stampa stampa = NemboUtils.STAMPA.getStampaFromCdU(cuName);
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + stampa.getDefaultFileName() + "\"");
    byte[] contenuto = stampa.genera(idBandoOggetto);

    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contenuto,
        httpHeaders, HttpStatus.OK);
    return response;
  }

  @RequestMapping("/indexTesti")
  protected ResponseEntity<byte[]> stampaByCUNameTesti(HttpSession session,
      @PathVariable("idBandoOggetto") long idBandoOggetto,
      @PathVariable("cuName") String cuName) throws Exception
  {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Content-type", "application/pdf");
    Stampa stampa = NemboUtils.STAMPA.getStampaFromCdU(cuName);
    httpHeaders.add("Content-Disposition",
        "attachment; filename=\"" + stampa.getDefaultFileName() + "\"");
    byte[] contenuto = stampa.genera(idBandoOggetto, cuName);

    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contenuto,
        httpHeaders, HttpStatus.OK);
    return response;
  }
}
