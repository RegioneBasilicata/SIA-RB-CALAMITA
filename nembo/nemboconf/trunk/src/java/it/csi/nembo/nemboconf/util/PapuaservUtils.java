package it.csi.nembo.nemboconf.util;

import org.apache.log4j.Logger;

import it.csi.papua.papuaserv.dto.gestioneutenti.Abilitazione;
import it.csi.papua.papuaserv.dto.gestioneutenti.AmmCompetenza;
import it.csi.papua.papuaserv.dto.gestioneutenti.Attore;
import it.csi.papua.papuaserv.dto.gestioneutenti.AziendaAgricola;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteBase;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteGenerico;
import it.csi.papua.papuaserv.dto.gestioneutenti.EnteLogin;
import it.csi.papua.papuaserv.dto.gestioneutenti.Intermediario;
import it.csi.papua.papuaserv.dto.gestioneutenti.Livello;
import it.csi.papua.papuaserv.dto.gestioneutenti.MacroCU;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class PapuaservUtils
{
  public static final String DIRITTO_ACCESSO_READ_ONLY  = "R";
  public static final String DIRITTO_ACCESSO_READ_WRITE = "W";
  public static final int    ENTE_NON_PREREGISTRATO     = 0;

  public boolean isUtenteReadOnly(UtenteAbilitazioni utenteAbilitazioni)
  {
    return DIRITTO_ACCESSO_READ_ONLY
        .equals(utenteAbilitazioni.getDirittoAccessoPrincipale());
  }

  public boolean isUtenteReadWrite(UtenteAbilitazioni utenteAbilitazioni)
  {
    return DIRITTO_ACCESSO_READ_WRITE
        .equals(utenteAbilitazioni.getDirittoAccessoPrincipale());
  }

  public boolean isMacroCUAbilitato(UtenteAbilitazioni utenteAbilitazioni,
      String codiceCdU)
  {
    for (MacroCU cu : utenteAbilitazioni.getMacroCU())
    {
      if (cu.getCodice().equals(codiceCdU))
      {
        return true;
      }
    }
    return false;
  }

  public boolean isLivelloAbilitato(UtenteAbilitazioni utenteAbilitazioni,
      long idLivello)
  {
    for (Abilitazione abilitazione : utenteAbilitazioni.getAbilitazioni())
    {
      Livello livello = abilitazione.getLivello();
      if (livello != null && livello.getIdLivello() == idLivello)
      {
        return true;
      }
    }
    return false;
  }

  public boolean hasAmministrazioneCompetenza(
      UtenteAbilitazioni utenteAbilitazioni, long idAmmCompetenza)
  {
    /* Prima controllo se l'amministrazione corrisponde all'ente dell'utente */
    AmmCompetenza ammCompetenza = utenteAbilitazioni.getEnteAppartenenza()
        .getAmmCompetenza();
    if (ammCompetenza != null)
    {
      if (ammCompetenza.getIdAmmCompetenza() == idAmmCompetenza)
      {
        return true;
      }
    }
    /* Poi cerco nell'elenco di enti abilitati */
    for (EnteLogin ente : utenteAbilitazioni.getEntiAbilitati())
    {
      ammCompetenza = ente.getAmmCompetenza();
      if (ammCompetenza != null)
      {
        if (ammCompetenza.getIdAmmCompetenza() == idAmmCompetenza)
        {
          return true;
        }
      }
    }
    /* Se non la trovo ovviamente la risposta � "false" */
    return false;
  }

  public boolean isAttoreAbilitato(UtenteAbilitazioni utenteAbilitazioni,
      String codAttore)
  {
    for (Attore attore : utenteAbilitazioni.getAttori())
    {
      if (attore.getCodice().equals(codAttore))
      {
        return true;
      }
    }
    return false;
  }

  public String getDenominazioneUtente(UtenteAbilitazioni utenteAbilitazioni)
  {
    return utenteAbilitazioni.getNome() + " " + utenteAbilitazioni.getCognome();
  }

  public EnteBase extractInfoEnteBaseFromEnteLogin(EnteLogin enteAppartenenza)
  {
    if (enteAppartenenza != null)
    {
      AmmCompetenza ammCompetenza = enteAppartenenza.getAmmCompetenza();
      if (ammCompetenza != null)
      {
        return ammCompetenza;
      }
      else
      {
        Intermediario intermediario = enteAppartenenza.getIntermediario();
        if (intermediario != null)
        {
          return intermediario;
        }
        else
        {
          AziendaAgricola aziendaAgricola = enteAppartenenza
              .getAziendaAgricola();
          if (aziendaAgricola != null)
          {
            return aziendaAgricola;
          }
          else
          {
            final EnteGenerico enteGenerico = enteAppartenenza
                .getEnteGenerico();
            if (enteGenerico == null)
            {
              Logger.getLogger("nemboconf.util").warn(
                  "[PapuaservUtils.extractInfoEnteBaseFromEnteLogin] non � stato trovata nessuna propriet� valorizzata nell'oggetto EnteLogin passato al metodo! Possibile NullPointer in arrivo!");
            }
            return enteGenerico;
          }
        }
      }
    }
    return null;
  }

  public int getIdEnte(EnteLogin ente)
  {
    return extractInfoEnteBaseFromEnteLogin(ente).getIdEnte();
  }

  public String getDenominazioneEnte(EnteLogin ente)
  {
    return extractInfoEnteBaseFromEnteLogin(ente).getDenominazioneEnte();
  }

  public String getFirstCodiceAttore(UtenteAbilitazioni utenteAbilitazioni)
  {
    return utenteAbilitazioni.getAttori()[0].getCodice();
  }
}
