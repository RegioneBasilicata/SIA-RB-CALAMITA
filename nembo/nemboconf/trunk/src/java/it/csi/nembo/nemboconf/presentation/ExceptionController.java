package it.csi.nembo.nemboconf.presentation;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import it.csi.nembo.nemboconf.dto.internal.MailAttachment;
import it.csi.nembo.nemboconf.exception.InternalServiceException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;
import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.nembo.nemboconf.util.annotation.NoLoginRequired;
import it.csi.nembo.nemboconf.util.annotation.Security;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

@Controller
@RequestMapping("/error")
@Security(dirittoAccessoMinimo = Security.DIRITTO_ACCESSO.READ_ONLY, macroCDU = "")
@NoLoginRequired
public class ExceptionController
{
  private static final String SESSION_PARAM_EXCEPTION = "SESSION_PARAM_EXCEPTION";

  @RequestMapping(value = "notfound")
  public String notFound(ModelMap model) throws InternalServiceException
  {
    // Gestisce gli errori 404
    model.put("titolo", "Si e' verificato un errore");
    model.put("messaggio", "Pagina non trovata");
    return "errore/erroreInterno";
  }

  @RequestMapping(value = "internalerror")
  public String internalError(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalServiceException
  {
    // Gestisce gli errori 500
    Throwable exception = (Throwable) request
        .getAttribute(RequestDispatcher.ERROR_EXCEPTION);// "javax.servlet.error.exception");
    session.removeAttribute(SESSION_PARAM_EXCEPTION);
    session.setAttribute(SESSION_PARAM_EXCEPTION, exception.getCause());
    model.put("titolo", "Si e' verificato un errore");
    model.put("messaggio", "Si è verificato un errore interno del server");
    model.put("manage_mail", "manage_mail");
    return "errore/erroreInterno";
  }

  @RequestMapping(value = "inviamail")
  public String inviaMil(ModelMap model, HttpServletRequest request,
      HttpSession session) throws InternalUnexpectedException
  {
    // Invia la mail di segnalazione
    InternalUnexpectedException exception = (InternalUnexpectedException) session
        .getAttribute(SESSION_PARAM_EXCEPTION);
    UtenteAbilitazioni utente = (UtenteAbilitazioni) session
        .getAttribute("utenteAbilitazioni");

    StringBuffer message = new StringBuffer();
    message.append("Invio segnalazione di errore \n");
    message.append("Prodotto: Nemboconf Configurazione \n\n");
    message.append("Dati utente: \n");
    message.append(utente.getCognome() + " " + utente.getNome() + " ("
        + NemboUtils.PAPUASERV.getDenominazioneUtente(utente) + ") \n");
    message.append(utente.getCodiceFiscale() + "\n\n");
    if (utente.getEnteAppartenenza() != null)
    {
      message.append("Ente: " + NemboUtils.PAPUASERV
          .getDenominazioneEnte(utente.getEnteAppartenenza()) + "\n\n");
    }

    message.append("Errore: " + exception.getMessage());
    message.append("Query: " + exception.getQuery());

    try
    {
      ByteArrayOutputStream fos = new ByteArrayOutputStream();
      ZipOutputStream zos = new ZipOutputStream(fos);

      // Preparo file di testo con l'eccezione
      ByteArrayOutputStream fosTemp = new ByteArrayOutputStream();
      PrintStream printFile = new PrintStream(fosTemp);
      printFile.println(exception.getExceptionStackTrace());

      addToZipFile(fosTemp, "Eccezione.txt", zos);
      zos.close();
      fos.close();
      fosTemp.close();

      MailAttachment[] attachments = new MailAttachment[1];
      attachments[0] = new MailAttachment();
      attachments[0].setAttach(fos.toByteArray());
      attachments[0].setFileName("Eccezione.zip");
      attachments[0].setFileType("application/zip");

      NemboUtils.MAIL.postMail("TOBECONFIG@TOBECONFIG.it",
          new String[]
          { "TOBECONFIG@TOBECONFIG" },
          null,
          "Invio segnalazione Nemboconf - Configurazione",
          message.toString(), attachments);

    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    session.removeAttribute(SESSION_PARAM_EXCEPTION);
    model.put("esito_mail", "Segnalazione inviata correttamente");
    model.put("manage_mail", "manage_mail");
    return "errore/erroreInterno";
  }

  private void addToZipFile(ByteArrayOutputStream os, String fileName,
      ZipOutputStream zos) throws FileNotFoundException, IOException
  {
    ZipEntry zipEntry = new ZipEntry(fileName);
    zos.putNextEntry(zipEntry);
    byte[] bytes = os.toByteArray();
    zos.write(bytes, 0, bytes.length);
    zos.closeEntry();
  }
}
