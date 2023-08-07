package it.csi.nembo.nemboconf.util;

import org.apache.log4j.Logger;

public class NemboConstants
{

  public static class LIVELLI_ABILITAZIONI
  {
    public static final long MODIFICA_PIANO_FINANZIARIO = 60001;

  }

  public static class NEMBOCONF
  {
    public static int    ID          = 52; 
    public static String WEB_CONTEXT = "nemboconf";
  }

  public static final class MAIL
  {
    public static final String SERVIZI_AGRICOLTURA = "hd_servagri@csi.it";
  }

  public static final class PORTAL
  {
    public static final String NEMBOCONF_LOGIN_PORTAL   = "NEMBOCONF_LOGIN_PORTAL";
    public static final String PUBBLICA_AMMINISTRAZIONE = "RUPAR";
    public static final String PRIVATI                  = "SISPIE";
    public static final String SPID                     = "SPID";
  }

  public static final class JNDI
  {
    public static final String MAIL_SESSION = "java:jboss/mail/Default";
  }

  public static class NOMI_TABELLE
  {
    public static String NEMBO_D_QUADRO     = "NEMBO_D_QUADRO";
    public static String NEMBO_D_OGGETTO    = "NEMBO_D_OGGETTO";
    public static String NEMBO_D_CONTROLLO  = "NEMBO_D_CONTROLLO";
    public static String NEMBO_D_AZIONE     = "NEMBO_D_AZIONE";
    public static String NEMBO_D_ELENCO_CDU = "NEMBO_D_ELENCO_CDU";
    public static String NEMBO_D_ICONA      = "NEMBO_D_ICONA";
  }

  public static class FLAGS
  {
    public static String SI = "S";
    public static String NO = "N";
  }

  public static class PARAMETRO
  {
    public static final String MESSAGGISTICA_MINUTI_VISUALIZZAZIONE_TESTATA = "MESE";
    public static final String MESSAGGISTICA_MINUTI_ATTESA_VERIFICA_LOGOUT  = "MESL";
    public static final String MESSAGGISTICA_MINUTI_ATTESA_VERIFICA_REFRESH = "MESR";
    public static final String MITTENTE_RICEVUTA                            = "MITTENTE_RICEVUTA";
    public static final String TESTO_RICEVUTA                               = "TESTO_RICEVUTA";
    public static final String OGGETTO_RICEVUTA                             = "OGGETTO_RICEVUTA";
    public static final String OGGETTO_NO_ISTANZA                           = "OGGETTO_NO_ISTANZA";
    public static final String TESTO_NO_ISTANZA                             = "TESTO_NO_ISTANZA";
    public static final String ID_LIVELLO_PF                                = "ID_LIVELLO_PF";
    public static final String TESTO_RICEVUTA_PAG                           = "TESTO_RICEVUTA_PAG";
    public static final String OGGETTO_RICEVUTA_PAG                         = "OGGETTO_RICEVUTA_PAG";

    public static class FILE
    {
      public static String TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO     = "ESPORTA_EXCEL_PF";
      public static String TEMPLATE_ESPORTAZIONE_EXCEL_PIANO_FINANZIARIO_ODS = "ESPORTA_EXCEL_PF_ODS";

    }
  }

  public static class TIPO_BANDO
  {
    public static String PREMIO       = "P";
    public static String INVESTIMENTO = "I";
    public static String GAL          = "G";
  }
  
  public static class TIPO_PROCEDIMENTO_AGRICOLO
  {
	  public static int AVVERSITA_ATMOSFERICHE = 53;
  }

  public static class TIPO_LIVELLO
  {
    public static int INVESTIMENTO = 1;
    public static int PREMIO       = 2;
    public static int GAL          = 3;
  }

  public static class QUADRO
  {
    public static class CODICE
    {
      public static final String DATI_IDENTIFICATIVI      = "DID";
      public static final String DICHIARAZIONI            = "DICH";
      public static final String IMPEGNI                  = "IMPEG";
      public static final String ALLEGATI                 = "ALLEG";
      public static final String CONTI_CORRENTI           = "CC";
      public static final String CARATTERISTICHE_GAL      = "CARAT";
      public static final String ENTI_CDA                 = "ENTI";
      public static final String AMBITI_TEMATICI          = "AMBIT";
      public static final String INTERVENTI               = "INTER";
      public static final String QUADRO_ECONOMICO         = "QECON";
      public static final String COMUNI_INDICATORI        = "COMUN";
      public static final String PIANO_FINANZIARIO_LEADER = "PFL";
      public static final String OPERAZIONI_IN_CAMPO      = "OPCAM";
      public static final String CONTROLLI_AMMINISTRATIVI = "CTRAM";
      public static final String INVESTIMENTI             = "INVES";
      public static final String OPERAZIONI_IMPEGNI       = "IMPPR";
      public static final String DATI_PASCOLAMENTO        = "PASCO";
      public static final String CANI_GUARDIANIA          = "CANI";
      public static final String RAZZE_MINACCIATE         = "RAZZE";
      public static final String PARTICELLE_IMPEGNO       = "PARPR";
      public static final String DATI_ANTICIPO            = "DTANT";
      public static final String RENDICONTAZIONE_SPESE    = "RENSP";
      public static final String RENDICONTAZIONE_SALDO    = "RENFN";
      public static final String CONTROLLI_LOCO           = "CTRLC";
      
      public static final String SUPERFICIE_COLTURA       = "SUPCO";
      public static final String ALLEVAMENTI              = "ALLEV";
      public static final String SCORTE                   = "SCORT";
      public static final String DANNI                    = "DANNI";
      public static final String INTERVENTI_INFRASTRUTTURE             = "INTCI";
      public static final String COLTURE_AZIENDALI                     = "COLAZ";
      public static final String DOCUMENTI_RICHIESTI                   = "DOCRI";
    }
  }

  public static class LOGGIN
  {
    public static String LOGGER_NAME    = "nemboconf";
    public static Logger DEFAULT_LOGGER = Logger.getLogger(LOGGER_NAME);

    public static class LOG_ATTIVITA_OGGETTI
    {
      public static final String DATI_IDENTIFICATIVI = "Modifica dati identificativi";
      public static final String FILTRO_BENEFICIARI  = "Modifica filtro beneficiari";
      public static final String ATTI_PUBBLICATI     = "Modifica atti pubblicati";
      public static final String INTERVENTI          = "Modifica interventi";
      public static final String OGGETTI_ISTANZE     = "Modifica oggetti/istanze";
      public static final String QUADRI              = "Modifica quadri";
      public static final String CONTROLLI_TEC_AMM   = "Modifica controlli tecnico amministrativi";
      public static final String CONTROLLI           = "Modifica controlli";
      public static final String DICHIARAZIONI       = "Modifica dichiarazioni";
      public static final String IMPEGNI             = "Modifica impegni";
      public static final String ALLEGATI            = "Modifica allegati";
      public static final String RICEVUTA            = "Modifica ricevuta";
      public static final String GRAFICI             = "Modifica grafici/report";
      public static final String TESTI_VERBALI       = "Modifica testo verbali";

    }
  }

  public static class SQL
  {
    public static class SEQUENCE
    {
      public static final String NEMBO_T_PIANO_FINANZIARIO       = "SEQ_NEMBO_T_PIANO_FINANZIARIO";
      public static final String NEMBO_T_ITER_PIANO_FINANZIARIO  = "SEQ_NEMBO_T_ITER_PIANO_FINANZI";
      public static final String NEMBO_T_STORICO_AMBITI_TEMATICI = "SEQ_NEMBO_T_STORICO_AMBITI_TEM";
      public static final String NEMBO_T_ITER_AMBITI_TEMATICI    = "SEQ_NEMBO_T_ITER_STO_AMBITI_TE";
      public static final String NEMBO_R_LIVELLO_FG_TIPOLOGIA    = "SEQ_NEMBO_R_LIVELLO_FG_TIPOLOGIA";
      public static final String NEMBO_R_LIVELLO_INTERVENTO      = "SEQ_NEMBO_R_LIVELLO_INTERVENTO";
    }
  }

  public static class GENERIC_ERRORS
  {
    public static final String DATI_NON_TROVATI           = "Dati non trovati";
    public static final String CUAA_ERRATO                = "Il CUAA inserito non è valido";
    public static final String AZIENDE_NON_TROVATE        = "Non è stata trovata nessuna azienda valida";
    public static final String CAMPO_OBBLIGATORIO         = "Campo obbligatorio!";
    public static String       SESSION_VAR_HELP_IS_ACTIVE = "HELP_IS_ACTIVE";
  }

  public static class JSP
  {
    public static class VIEW
    {
      public static class ERROR
      {
        public static final String SESSION_EXPIRED = "/WEB-INF/jsp/errore/sessioneScaduta.jsp";
      }
    }
  }

  public static class LIVELLO
  {
    public static class TIPOLOGIA
    {
      public static class ID
      {
        public static final int MISURA      = 1;
        public static final int SOTTOMISURA = 2;
        public static final int OPERAZIONE  = 3;
      }
    }
  }

  public static class GENERIC
  {
    public static String ID_REGIONE_PIEMONTE                  = "17";
    public static String SESSION_VAR_FILTER_AZIENDA           = "filtroAziende";
    public static String SESSION_NO_ELENCO_AZIENDA            = "sessionNoElencoAzienda";
    public static String SESSION_VAR_AZIENDE_COLONNE_NASCOSTE = "colonneNascoste";
    public static String SESSION_VAR_NUMERO_PAGINA            = "sessionMapNumeroPagina";
    public static long   FONTE_CONTROLLO_PREMIO               = 4;
    public static String SESSION_VAR_HELP_IS_ACTIVE           = "HELP_IS_ACTIVE";
  }

  public static class PIANO_FINANZIARIO
  {
    public static class STATO
    {
      public static class ID
      {
        public static final long CONSOLIDATO  = 1;
        public static final long STORICIZZATO = 2;
        public static final long BOZZA        = -2;
      }
    }

    public static class ERROR
    {
      public static String INSERIMENTO_CON_VERSIONE_CORRENTE_IN_BOZZA        = "Impossibile create una nuova versione del piano finanziario finchè è presente una versione in bozza";
      public static String PIANO_FINANZIARIO_NON_TROVATO                     = "Il piano finanziario indicato non è stato trovato. E' possibile che sia stato cancellato da qualche altro utente";
      public static String CONSOLIDAMENTO_CON_VERSIONE_CORRENTE_NON_IN_BOZZA = "Impossibile consolidare questo piano finanziario perchè non è in bozza";
      public static String ELIMINAZIONE_CON_VERSIONE_CORRENTE_NON_IN_BOZZA   = "Impossibile eliminare questo piano finanziario perchè non è in bozza";
    }

    public static class TIPO
    {
      public static final String CODICE_REGIONALE = "R";
      public static final String CODICE_GAL       = "G";
    }
  }

  public static class AMBITI_TEMATICI
  {
    public static class STATO
    {
      public static class ID
      {
        public static final long BOZZA        = 1;
        public static final long CONSOLIDATO  = 2;
        public static final long STORICIZZATO = 3;
      }
    }

    public static class ERROR
    {
      public static String INSERIMENTO_CON_VERSIONE_CORRENTE_NON_CONSOLIDATA = "Non è possibile proseguire con la creazione di una nuova versione. E' necessario consolidare la versione più recente.";
      public static String CONSOLIDA_CON_VERSIONE_CORRENTE_NON_IN_BOZZA      = "Non è possibile consolidare la versione. Lo stato deve essere In Bozza.";
      public static String ELIMINA_CON_VERSIONE_CORRENTE_NON_IN_BOZZA        = "Non è possibile eliminare la versione. Lo stato deve essere In Bozza.";
    }

    public static class ALERT
    {
      public static String CONFERMA_NUOVA_VERSIONE = "Attenzione, proseguendo con l'operazione verrà creata una nuova versione di ambito tematico, si vuole continuare?";
      public static String CONFERMA_CONSOLIDA      = "Attenzione, proseguendo con l'operazione verrà consolidata la versione selezionata, si vuole continuare?";
      public static String CONFERMA_ELIMINA        = "Attenzione, proseguendo con l'operazione verrà eliminata la versione selezionata, si vuole continuare?";
    }
  }

  public static class PAGINATION
  {
    public static final String CONTENUTO                    = "content";
    public static final String NUOVA_PAGINA                 = "newPage";
    public static final String NUMERO_PAGINE                = "numeroPagine";
    public static final String TOTALE_ELEMENTI              = "totaleElementi";
    public static final String LISTA_COUNT_RISULTATI_PAGINA = "listRisultatiPagina";
    public static final String COUNT_ELEMENTI_PAGINA        = "countElementiPagina";
    public static final String ORDER_PROPERTY               = "orderProperty";
    public static final String ORDER_TYPE                   = "orderType";
    public static final String VISIBLE_COLUMNS              = "visibleColumns";

    public static final String FISRT_PAGE_ROW               = "firstPageRow";
    public static final String LAST_PAGE_ROW                = "lastPageRow";

    public static final String IS_VALID                     = "isValidJSON";

    public static class AZIONI
    {
      public static final String AGGIUNGI  = "icon-circle-arrow-right icon-large";
      public static final String DETTAGLIO = "icon-list icon-large";
    }
  }

  public static class ESITO
  {
    public static class TIPO
    {
      public static final String ANALISI                  = "A";
      public static final String NON_ISTANZA              = "O";
      public static final String FINALE                   = "F";
      public static final String ISTANZA                  = "I";
      public static final String VISITA_LUOGO             = "V";
      public static final String CONTROLLI_AMMINISTRATIVI = "C";
      public static final String TECNICO_AMMINISTRATIVO   = "T";
      public static final String CONTROLLI_LOCO           = "L";
      public static final String POSITIVO                 = "P";
      public static final String NEGATIVO                 = "N";
      public static final String PARZIALMENTE_POSITIVO    = "PN";
    }
  }
  
  public static class ERRORI
  {
	  public static class EVENTI_CALAMITOSI
	  {
		  public static int BANDI_ESISTENTI = -1;
	  }
  }

  public static class USECASE
  {
    public static class STAMPA
    {
      public static class CRUSCOTTO_BANDI
      {
        public static String       PRATICA_Nemboconf                                                 = "CU-NEMBO-128";
        public static String       GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO                       = "CU-NEMBO-209";
        public static String       GENERA_AMMISSIONE_FINANZIAMENTO                                   = "CU-NEMBO-200";

        public static final String GENERA_AMMISSIONE_FINANZIAMENTO_POSITIVO_1                        = GENERA_AMMISSIONE_FINANZIAMENTO
            + "-P1";
        public static final String GENERA_AMMISSIONE_FINANZIAMENTO_POSITIVO_2                        = GENERA_AMMISSIONE_FINANZIAMENTO
            + "-P2";
        public static final String GENERA_AMMISSIONE_FINANZIAMENTO_NEGATIVO_1                        = GENERA_AMMISSIONE_FINANZIAMENTO
            + "-N1";
        public static final String GENERA_AMMISSIONE_FINANZIAMENTO_NEGATIVO_2                        = GENERA_AMMISSIONE_FINANZIAMENTO
            + "-N2";
        public static final String GENERA_AMMISSIONE_FINANZIAMENTO_PARZIALE_1                        = GENERA_AMMISSIONE_FINANZIAMENTO
            + "-PN1";
        public static final String GENERA_AMMISSIONE_FINANZIAMENTO_PARZIALE_2                        = GENERA_AMMISSIONE_FINANZIAMENTO
            + "-PN2";

        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_1            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO
            + "-P1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_POSITIVO_2            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO
            + "-P2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_1            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO
            + "-N1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_NEGATIVO_2            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO
            + "-N2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_PARZIALE_1            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO
            + "-PN1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO_PARZIALE_2            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_SOSTEGNO
            + "-PN2";

        public static String       COMUNICAZIONE_ESITO_ISTRUTTORIA_MISURE_A_PREMIO                   = "CU-NEMBO-238";
        public static String       VERBALE_ISTRUTTORIA_MISURE_A_PREMIO                               = "CU-NEMBO-239";

        public static final String GENERA_COMUNICAZIONE_ESITO_ISTRUTTORIA_MISURE_A_PREMIO_POSITIVO_1 = COMUNICAZIONE_ESITO_ISTRUTTORIA_MISURE_A_PREMIO
            + "-P1";
        public static final String GENERA_COMUNICAZIONE_ESITO_ISTRUTTORIA_MISURE_A_PREMIO_NEGATIVO_1 = COMUNICAZIONE_ESITO_ISTRUTTORIA_MISURE_A_PREMIO
            + "-N1";
        public static final String GENERA_COMUNICAZIONE_ESITO_ISTRUTTORIA_MISURE_A_PREMIO_PARZIALE_1 = COMUNICAZIONE_ESITO_ISTRUTTORIA_MISURE_A_PREMIO
            + "-PN1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_MISURE_A_PREMIO_POSITIVO_1             = VERBALE_ISTRUTTORIA_MISURE_A_PREMIO
            + "-P1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_MISURE_A_PREMIO_NEGATIVO_1             = VERBALE_ISTRUTTORIA_MISURE_A_PREMIO
            + "-N1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_MISURE_A_PREMIO_PARZIALE_1             = VERBALE_ISTRUTTORIA_MISURE_A_PREMIO
            + "-PN1";

        public static final String GENERA_VERBALE_ISTRUTTORIA_ACCONTO                                = "CU-NEMBO-242";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_1                     = GENERA_VERBALE_ISTRUTTORIA_ACCONTO
            + "-P1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ACCONTO_POSITIVO_2                     = GENERA_VERBALE_ISTRUTTORIA_ACCONTO
            + "-P2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_1                     = GENERA_VERBALE_ISTRUTTORIA_ACCONTO
            + "-N1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ACCONTO_NEGATIVO_2                     = GENERA_VERBALE_ISTRUTTORIA_ACCONTO
            + "-N2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ACCONTO_PARZIALE_1                     = GENERA_VERBALE_ISTRUTTORIA_ACCONTO
            + "-PN1";

        public static final String GENERA_VERBALE_ISTRUTTORIA_ANTICIPO                               = "CU-NEMBO-243";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_1                    = GENERA_VERBALE_ISTRUTTORIA_ANTICIPO
            + "-P1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_POSITIVO_2                    = GENERA_VERBALE_ISTRUTTORIA_ANTICIPO
            + "-P2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_1                    = GENERA_VERBALE_ISTRUTTORIA_ANTICIPO
            + "-N1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_NEGATIVO_2                    = GENERA_VERBALE_ISTRUTTORIA_ANTICIPO
            + "-N2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_ANTICIPO_PARZIALE_1                    = GENERA_VERBALE_ISTRUTTORIA_ANTICIPO
            + "-PN1";

        public static final String GENERA_VERBALE_ISTRUTTORIA_SALDO                                  = "CU-NEMBO-244";
        public static final String GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_1                       = GENERA_VERBALE_ISTRUTTORIA_SALDO
            + "-P1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_SALDO_POSITIVO_2                       = GENERA_VERBALE_ISTRUTTORIA_SALDO
            + "-P2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_1                       = GENERA_VERBALE_ISTRUTTORIA_SALDO
            + "-N1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_SALDO_NEGATIVO_2                       = GENERA_VERBALE_ISTRUTTORIA_SALDO
            + "-N2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_SALDO_PARZIALE_1                       = GENERA_VERBALE_ISTRUTTORIA_SALDO
            + "-PN1";

        public static final String GENERA_LETTERA_ISTRUTTORIA_ACCONTO                                = "CU-NEMBO-245";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_1                     = GENERA_LETTERA_ISTRUTTORIA_ACCONTO
            + "-P1";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ACCONTO_POSITIVO_2                     = GENERA_LETTERA_ISTRUTTORIA_ACCONTO
            + "-P2";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_1                     = GENERA_LETTERA_ISTRUTTORIA_ACCONTO
            + "-N1";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ACCONTO_NEGATIVO_2                     = GENERA_LETTERA_ISTRUTTORIA_ACCONTO
            + "-N2";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ACCONTO_PARZIALE_1                     = GENERA_LETTERA_ISTRUTTORIA_ACCONTO
            + "-PN1";

        public static final String GENERA_LETTERA_ISTRUTTORIA_ANTICIPO                               = "CU-NEMBO-246";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_1                    = GENERA_LETTERA_ISTRUTTORIA_ANTICIPO
            + "-P1";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_POSITIVO_2                    = GENERA_LETTERA_ISTRUTTORIA_ANTICIPO
            + "-P2";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_1                    = GENERA_LETTERA_ISTRUTTORIA_ANTICIPO
            + "-N1";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_NEGATIVO_2                    = GENERA_LETTERA_ISTRUTTORIA_ANTICIPO
            + "-N2";
        public static final String GENERA_LETTERA_ISTRUTTORIA_ANTICIPO_PARZIALE_1                    = GENERA_LETTERA_ISTRUTTORIA_ANTICIPO
            + "-PN1";

        public static final String GENERA_LETTERA_ISTRUTTORIA_SALDO                                  = "CU-NEMBO-247";
        public static final String GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_1                       = GENERA_LETTERA_ISTRUTTORIA_SALDO
            + "-P1";
        public static final String GENERA_LETTERA_ISTRUTTORIA_SALDO_POSITIVO_2                       = GENERA_LETTERA_ISTRUTTORIA_SALDO
            + "-P2";
        public static final String GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_1                       = GENERA_LETTERA_ISTRUTTORIA_SALDO
            + "-N1";
        public static final String GENERA_LETTERA_ISTRUTTORIA_SALDO_NEGATIVO_2                       = GENERA_LETTERA_ISTRUTTORIA_SALDO
            + "-N2";
        public static final String GENERA_LETTERA_ISTRUTTORIA_SALDO_PARZIALE_1                       = GENERA_LETTERA_ISTRUTTORIA_SALDO
            + "-PN1";

        public static final String GENERA_VARIANTE                                                   = "CU-NEMBO-267";
        public static final String GENERA_VARIANTE_POSITIVO_1                                        = GENERA_VARIANTE
            + "-P1";
        public static final String GENERA_VARIANTE_POSITIVO_2                                        = GENERA_VARIANTE
            + "-P2";
        public static final String GENERA_VARIANTE_NEGATIVO_1                                        = GENERA_VARIANTE
            + "-N1";
        public static final String GENERA_VARIANTE_NEGATIVO_2                                        = GENERA_VARIANTE
            + "-N2";
        public static final String GENERA_VARIANTE_PARZIALE_1                                        = GENERA_VARIANTE
            + "-PN1";
        public static final String GENERA_VARIANTE_PARZIALE_2                                        = GENERA_VARIANTE
            + "-PN2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE                       = "CU-NEMBO-268";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_1            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE
            + "-P1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_POSITIVO_2            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE
            + "-P2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_1            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE
            + "-N1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_NEGATIVO_2            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE
            + "-N2";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_PARZIALE_1            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE
            + "-PN1";
        public static final String GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE_PARZIALE_2            = GENERA_VERBALE_ISTRUTTORIA_DOMANDA_VARIANTE
            + "-PN2";
        public static final String GENERA_COMUNICAZIONE_PROVVEDIMENTO_FINALE                         = "CU-NEMBO-269";

        public static final String GENERA_VERBALE_INTEG_PAGAMENTO                                    = "CU-NEMBO-272";
        public static final String GENERA_LETTERA_INTEG_PAGAMENTO                                    = "CU-NEMBO-271";

        public static final String GENERA_VERBALE_POST_ISTRUTTORIA_MISURE_PREMIO                     = "CU-NEMBO-277";
        public static final String GENERA_VERBALE_POST_ISTRUTTORIA_MISURE_PREMIO_POSITIVO            = GENERA_VERBALE_POST_ISTRUTTORIA_MISURE_PREMIO
            + "-P";
        public static final String GENERA_VERBALE_POST_ISTRUTTORIA_MISURE_PREMIO_PARZIALE            = GENERA_VERBALE_POST_ISTRUTTORIA_MISURE_PREMIO
            + "-PN";
        public static final String GENERA_VERBALE_POST_ISTRUTTORIA_MISURE_PREMIO_NEGATIVO            = GENERA_VERBALE_POST_ISTRUTTORIA_MISURE_PREMIO
            + "-N";

        public static final String GENERA_LETTERA_POST_ISTRUTTORIA_MISURE_PREMIO                     = "CU-NEMBO-276";
        public static final String GENERA_LETTERA_POST_ISTRUTTORIA_MISURE_PREMIO_POSITIVO_1          = GENERA_LETTERA_POST_ISTRUTTORIA_MISURE_PREMIO
            + "-P";
        public static final String GENERA_LETTERA_POST_ISTRUTTORIA_MISURE_PREMIO_NEGATIVO_1          = GENERA_LETTERA_POST_ISTRUTTORIA_MISURE_PREMIO
            + "-N";
        public static final String GENERA_LETTERA_POST_ISTRUTTORIA_MISURE_PREMIO_PARZIALE_1          = GENERA_LETTERA_POST_ISTRUTTORIA_MISURE_PREMIO
            + "-PN";

        public static final String GENERA_LETTERA_PROROGA                                            = "CU-NEMBO-278";
        public static final String GENERA_LETTERA_PROROGA_POSITIVO                                   = GENERA_LETTERA_PROROGA
            + "-P";
        public static final String GENERA_LETTERA_PROROGA_NEGATIVO                                   = GENERA_LETTERA_PROROGA
            + "-N";
        public static final String GENERA_VERBALE_PROROGA                                            = "CU-NEMBO-279";
        public static final String GENERA_VERBALE_PROROGA_POSITIVO                                   = GENERA_VERBALE_PROROGA
            + "-P";
        public static final String GENERA_VERBALE_PROROGA_NEGATIVO                                   = GENERA_VERBALE_PROROGA
            + "-N";

        public static final String GENERA_LETTERA_REVOCA                                             = "CU-NEMBO-281";
        public static final String GENERA_LETTERA_PREAVVISO_REVOCA                                   = "CU-NEMBO-281-P1";
        public static final String GENERA_LETTERA_REVOCA_DEFINITIVA                                  = "CU-NEMBO-281-P2";
        public static final String GENERA_LETTERA_REVOCA_NEGATIVA                                    = "CU-NEMBO-281-N";
        public static final String GENERA_LETTERA_REVOCA_NEGATIVA_2                                  = "CU-NEMBO-281-N2";

        public static final String GENERA_LETTERA_VOLTURA                                            = "CU-NEMBO-289";
        public static final String GENERA_LETTERA_VOLTURA_POSITIVO                                   = "CU-NEMBO-289-P1";
        public static final String GENERA_LETTERA_VOLTURA_POSITIVO_2                                 = "CU-NEMBO-289-P2";
        public static final String GENERA_LETTERA_VOLTURA_NEGATIVO                                   = "CU-NEMBO-289-N2";
        public static final String GENERA_LETTERA_VOLTURA_PREAVVISO_RIGETTO                          = "CU-NEMBO-289-N1";

        public static final String GENERA_VERBALE_VOLTURA                                            = "CU-NEMBO-290";
        public static final String GENERA_VERBALE_VOLTURA_POSITIVO                                   = "CU-NEMBO-290-P1";
        public static final String GENERA_VERBALE_VOLTURA_POSITIVO_2                                 = "CU-NEMBO-290-P2";
        public static final String GENERA_VERBALE_VOLTURA_NEGATIVO                                   = "CU-NEMBO-290-N1";
        public static final String GENERA_VERBALE_VOLTURA_NEGATIVO_2                                 = "CU-NEMBO-290-N2";

        public static final String GENERA_LETTERA_ANNULLAMENTO                                       = "CU-NEMBO-292";
        public static final String GENERA_LETTERA_ANNULLAMENTO_POSITIVO                              = "CU-NEMBO-292-P";
        public static final String GENERA_LETTERA_ANNULLAMENTO_NEGATIVO                              = "CU-NEMBO-292-N";

        public static final String GENERA_VERBALE_REVOCA                                             = "CU-NEMBO-293";
        public static final String GENERA_VERBALE_REVOCA_POSITIVO                                    = "CU-NEMBO-293-P1";
        public static final String GENERA_VERBALE_REVOCA_POSITIVO_2                                  = "CU-NEMBO-293-P2";
        public static final String GENERA_VERBALE_REVOCA_NEGATIVO                                    = "CU-NEMBO-293-N";

      }
    }

  }

  public static class PAGE
  {
    public static class DO
    {
      public static final String LOGIN = "/" + NEMBOCONF.WEB_CONTEXT
          + "/login/seleziona_ruolo.do";
    }

    public static class JSP
    {
      public static class ERROR
      {
        public static final String SESSION_EXPIRED = "/WEB-INF/jsp/errore/sessioneScaduta.jsp";
        public static final String INTERNAL_ERROR  = "/WEB-INF/jsp/errore/erroreInterno.jsp";
      }
    }
  }
}
