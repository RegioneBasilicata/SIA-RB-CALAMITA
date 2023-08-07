package it.csi.nembo.nembopratiche.presentation.stampeoggetto;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.nembo.nembopratiche.util.annotation.NemboSecurity;

@Controller
@NemboSecurity(value = "CU-NEMBO-290", controllo = NemboSecurity.Controllo.PROCEDIMENTO_OGGETTO)
public class CUNEMBO290GeneraStampaVerbaleIstruttoriaVoltura
    extends StampaController
{
  @RequestMapping(value = "/cunembo290/stampa")
  public ResponseEntity<byte[]> stampa(HttpSession session) throws Exception
  {
    return stampaByCUName(getIdProcedimentoOggetto(session),
        CUNEMBO290GeneraStampaVerbaleIstruttoriaVoltura.class
            .getAnnotation(NemboSecurity.class).value());
  }
}
