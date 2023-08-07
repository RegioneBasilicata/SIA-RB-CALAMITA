--------------------------------------------------------
--  File creato - lunedì-luglio-31-2023   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Package PCK_NEMBO_CALCOLO_PUNTEGGIO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "NEMBO"."PCK_NEMBO_CALCOLO_PUNTEGGIO" IS

  TYPE recCriteri IS RECORD(ID_BANDO_LIVELLO_CRITERIO  NEMBO_R_BANDO_LIVELLO_CRITERIO.ID_BANDO_LIVELLO_CRITERIO%TYPE,
                            CODICE_CRITERIO            NEMBO_D_CRITERIO.CODICE%TYPE,
                            ID_LIVELLO                 NEMBO_D_LIVELLO.ID_LIVELLO%TYPE,
                            MISURA                     NEMBO_D_LIVELLO.CODICE%TYPE,
                            RAGGRUPPAMENTO             NEMBO_R_LIVELLO_CRITERIO.RAGGRUPPAMENTO%TYPE);

  TYPE typTbCriteri IS TABLE OF recCriteri INDEX BY PLS_INTEGER;

  PROCEDURE Main(pIdProcedimento             NEMBO_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                 pIdProcedimentoOggetto      NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                 pExtIdAzienda               NEMBO_T_PROCEDIMENTO_AZIENDA.EXT_ID_AZIENDA%TYPE,
                 pIdBando                    NEMBO_D_BANDO.ID_BANDO%TYPE,
                 pIdLivello                  NEMBO_R_LIVELLO_CRITERIO.ID_LIVELLO%TYPE,
                 pRaggruppamento             NEMBO_R_LIVELLO_CRITERIO.RAGGRUPPAMENTO%TYPE,
                 pRisultato              OUT NUMBER,
                 pMessaggio              OUT VARCHAR2);

END PCK_NEMBO_CALCOLO_PUNTEGGIO;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CALCOLO_PUNTEGGIO" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CALCOLO_PUNTEGGIO" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package PCK_NEMBO_CONTROLLI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "NEMBO"."PCK_NEMBO_CONTROLLI" IS
  --16/02/22: nuovo controllo DAN01 per nuovo quadro "Danni"
  
  gvCuaa                   SMRGAA_V_DATI_ANAGRAFICI.CUAA%TYPE;
  gnIdAzienda              SMRGAA_V_DATI_ANAGRAFICI.ID_AZIENDA%TYPE;
  gnIdProcedimentoOggetto  NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE;
  gnIdProcedimento         NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE;
  gnIdBandoOggetto         NEMBO_R_BANDO_OGGETTO.ID_BANDO_OGGETTO%TYPE;
  gnIdDatiProcedimento     NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE;
  gnExtIdUtente            NEMBO_T_ESECUZIONE_CONTROLLI.EXT_ID_UTENTE%TYPE;
  gnCodiceRaggruppamento   NEMBO_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;
  TYPE typTbLegameInfo          IS TABLE OF nembo_D_LEGAME_INFO%ROWTYPE INDEX BY BINARY_INTEGER; 
  TYPE typTbSelezioneInfo       IS TABLE OF nembo_T_SELEZIONE_INFO%ROWTYPE INDEX BY BINARY_INTEGER; 
  
  -- fonte controlli reg
  ID_FONTE_C_REG         CONSTANT NEMBO_D_CONTROLLO.ID_FONTE_CONTROLLO%TYPE:=1;


  FUNCTION MainControlliGravi(pIdBandoOggetto            NEMBO_R_BANDO_OGGETTO.ID_BANDO_OGGETTO%TYPE,
                              pIdProcedimento            NEMBO_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                              pExtIdAzienda              NUMBER,
                              pExtIdUtente               NUMBER,
                              pCodiceRaggruppamento      NEMBO_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE,
                              pRisultato             OUT NUMBER,
                              pMessaggio             OUT VARCHAR2) RETURN LIST_CONTROLLO;

  PROCEDURE MainControlli(pIdBandoOggetto             NEMBO_R_BANDO_OGGETTO.ID_BANDO_OGGETTO%TYPE,
                          pIdProcedimentoOggetto      NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                          pExtIdAzienda               NUMBER,
                          pExtIdUtente                NUMBER,
                          pRisultato              OUT NUMBER,
                          pMessaggio              OUT VARCHAR2);

  FUNCTION ScriviAnomalieParticelle(pIdParticellaUtilizzo         NEMBO_T_ANOMALIA_PARTICELLA.ID_PARTICELLA_UTILIZZO%TYPE,
                                    pCodiceControllo              NEMBO_D_CONTROLLO.CODICE%TYPE,
                                    pDescAnomalia                 NEMBO_T_ANOMALIA_PARTICELLA.DESCRIZIONE_ANOMALIA%TYPE,
                                    pUlterioriInfo                NEMBO_T_ANOMALIA_PARTICELLA.ULTERIORI_INFORMAZIONI%TYPE,
                                    pGravita                      NEMBO_T_ANOMALIA_PARTICELLA.GRAVITA%TYPE,
                                    pRisultato       OUT NUMBER,
                                    pMessaggio       OUT VARCHAR2) RETURN BOOLEAN;

  PROCEDURE ALL01(pRisultato            OUT NUMBER,
                  pMessaggio            OUT VARCHAR2,
                  pUlterioriInfo        OUT VARCHAR2);    
  PROCEDURE DIC01(pRisultato            OUT NUMBER,
                  pMessaggio            OUT VARCHAR2,
                  pUlterioriInfo        OUT VARCHAR2);                    
  PROCEDURE IMP01(pRisultato            OUT NUMBER,
                  pMessaggio            OUT VARCHAR2,
                  pUlterioriInfo        OUT VARCHAR2);
  PROCEDURE DAN01(pRisultato            OUT NUMBER,
                  pMessaggio            OUT VARCHAR2,
                  pUlterioriInfo        OUT VARCHAR2);
                  
END PCK_NEMBO_CONTROLLI;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CONTROLLI" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CONTROLLI" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package PCK_NEMBO_DUPLICA_BANDO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "NEMBO"."PCK_NEMBO_DUPLICA_BANDO" is
--03/05/2019: corretto punto in cui non utilizzavo SEQ_NEMBO_R_BANDO_LIVELLO_CRIT
    PROCEDURE Main(bando_selezionato        IN NUMBER,
                   bando_obiettivo          IN OUT NUMBER,
                   nome_bando               IN VARCHAR2,
                   ListaOggetti             IN VARCHAR2,
                   pRisultato               OUT NUMBER,
                   pMessaggio               OUT VARCHAR2) ;
    PROCEDURE CopiaConfBando(pIdBandoOggettoOld   IN NUMBER,
                             pIdBandoOggettoNew   IN NUMBER,
                             pidQuadro            IN NUMBER,
                             pRisultato           OUT NUMBER,
                             pMessaggio           OUT VARCHAR2
                             );
    FUNCTION inserisciDichImpAll  (sIdBandoOggettoOld   IN NUMBER,
                                   sIdBandoOggettoNew   IN NUMBER,
                                   sCodiceQuadro        IN NEMBO_D_QUADRO.CODICE%TYPE,
                                   sRisultato           OUT NUMBER,
                                   sMessaggio           OUT VARCHAR2
                                   ) RETURN BOOLEAN;


TYPE typBandoOggTable      IS TABLE OF NEMBO_R_BANDO_OGGETTO%ROWTYPE INDEX BY BINARY_INTEGER;
TYPE typGruppoInfoTable    IS TABLE OF NEMBO_D_GRUPPO_INFO%ROWTYPE INDEX BY BINARY_INTEGER;--correzione 10/5/17 perchï¿½ non copiava allegati, impegni e dichiarazioni
TYPE typGruppoTestoVerbale IS TABLE OF NEMBO_D_GRUPPO_TESTO_VERBALE%ROWTYPE INDEX BY BINARY_INTEGER;
TYPE typDettaglioInfo      IS TABLE OF NEMBO_D_DETTAGLIO_INFO%ROWTYPE INDEX BY BINARY_INTEGER;--per poter inserire i vincoli tra le dichiarazioni

END PCK_NEMBO_DUPLICA_BANDO;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_DUPLICA_BANDO" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_DUPLICA_BANDO" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package PCK_NEMBO_GESTIONE_PROC_OGGETT
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "NEMBO"."PCK_NEMBO_GESTIONE_PROC_OGGETT" IS

  -- fonte GIS
   ID_FONTE_GIS         CONSTANT nembo_D_CONTROLLO.ID_FONTE_CONTROLLO%TYPE:=2;

   TYPE typTbDettIntervPO   IS TABLE OF nembo_W_DETT_INTERV_PROC_OGG%ROWTYPE INDEX BY BINARY_INTEGER;
   TYPE typTbDettInterv   IS TABLE OF nembo_T_DETTAGLIO_INTERVENTO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE recProcCodOggetto   IS RECORD (ID_PROCEDIMENTO_OGGETTO  nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                       CODICE                   nembo_D_OGGETTO.CODICE%TYPE);
   TYPE typProcCodOggetto IS TABLE OF recProcCodOggetto INDEX BY BINARY_INTEGER;

   TYPE typQuadriDinamici IS TABLE OF nembo_D_QUADRO_DINAMICO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typDatiComuniElemQuadro IS TABLE OF NEMBO_T_DATI_COMUNI_ELEM_QUADR%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typDatoElemQuadro IS TABLE OF nembo_T_DATO_ELEMENTO_QUADRO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typControlliAmm IS TABLE OF nembo_T_ESITO_CONTROLLI_AMM%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typPremioComplessivo IS TABLE OF nembo_T_PREMIO_COMPLESSIVO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE recNewPartecipante   IS RECORD (ID_PARTECIPANTE                  nembo_T_PARTECIPANTE.ID_PARTECIPANTE%TYPE,
                                                           ID_PARTECIPANTE_NEW           nembo_T_PARTECIPANTE.ID_PARTECIPANTE%TYPE);
   TYPE typNewPartecipante IS TABLE OF recNewPartecipante INDEX BY BINARY_INTEGER;

   TYPE recNewAttivita         IS RECORD (ID_ATTIVITA           nembo_T_ATTIVITA.ID_ATTIVITA%TYPE,
                                                           ID_ATTIVITA_NEW           nembo_T_ATTIVITA.ID_ATTIVITA%TYPE);
   TYPE typNewAttivita IS TABLE OF recNewAttivita INDEX BY BINARY_INTEGER;

   TYPE typAttPart IS TABLE OF nembo_R_ATTIVITA_PARTECIPANTE%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typTbAnticipoLivello IS TABLE OF nembo_R_ANTICIPO_LIVELLO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typEsitoOperazione IS TABLE OF nembo_T_ESITO_OPERAZIONE%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typProcedimentoLivello      IS TABLE OF nembo_R_PROCEDIMENTO_LIVELLO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE recAmmComp                  IS TABLE OF nembo_D_BANDO_AMM_COMPETENZA.EXT_ID_AMM_COMPETENZA%TYPE INDEX BY BINARY_INTEGER;

   TYPE recDatiPart                       IS RECORD (ID_PARTICELLA                              SMRGAA_V_CONDUZIONE_UTILIZZO.ID_PARTICELLA%TYPE,
                                                     ID_CATALOGO_MATRICE                        SMRGAA_V_CONDUZIONE_UTILIZZO.ID_CATALOGO_MATRICE%TYPE,
                                                     ID_PARTICELLA_UTILIZZO                     nembo_T_PARTICELLA_UTILIZZO.ID_PARTICELLA_UTILIZZO%TYPE,
                                                     ID_PRATICA_MANTENIMENTO                    SMRGAA_V_CONDUZIONE_UTILIZZO.ID_PRATICA_MANTENIMENTO%TYPE,
                                                     COD_TIPO_UTILIZZO                          SMRGAA_V_CONDUZIONE_UTILIZZO.COD_TIPO_UTILIZZO%TYPE,
                                                     ID_ZONA_ALTIMETRICA                        SMRGAA_V_CONDUZIONE_UTILIZZO.ID_ZONA_ALTIMETRICA%TYPE);
   TYPE typDatiPart                       IS TABLE OF recDatiPart INDEX BY BINARY_INTEGER;

   TYPE typMisura                IS TABLE OF nembo_D_LIVELLO%ROWTYPE INDEX BY BINARY_INTEGER;
   TYPE typImpegno               IS TABLE OF nembo_D_IMPEGNO%ROWTYPE INDEX BY BINARY_INTEGER;
   TYPE typParticellaUtilizzo    IS TABLE OF nembo_T_PARTICELLA_UTILIZZO%ROWTYPE INDEX BY BINARY_INTEGER;
   TYPE recParticellaUtilizzoExt               IS RECORD (ID_PARTICELLA_UTILIZZO                                nembo_T_PARTICELLA_UTILIZZO.ID_PARTICELLA_UTILIZZO%TYPE,
                                                                            ID_PARTICELLA                                               SMRGAA_V_CONDUZIONE_UTILIZZO.ID_PARTICELLA%TYPE,
                                                                            ID_PRATICA_MANTENIMENTO                          SMRGAA_V_CONDUZIONE_UTILIZZO.ID_PRATICA_MANTENIMENTO%TYPE,
                                                                            COD_TIPO_UTILIZZO                                SMRGAA_V_CONDUZIONE_UTILIZZO.COD_TIPO_UTILIZZO%TYPE,
                                                                            ID_ZONA_ALTIMETRICA                        SMRGAA_V_CONDUZIONE_UTILIZZO.ID_ZONA_ALTIMETRICA%TYPE);
   TYPE typParticellaUtilizzoExt    IS TABLE OF recParticellaUtilizzoExt INDEX BY BINARY_INTEGER;

   TYPE typAllevamenti          IS TABLE OF nembo_T_ALLEVAMENTO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typInterventi               IS TABLE OF nembo_T_INTERVENTO%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typRendicontazioneSpese IS TABLE OF nembo_T_RENDICONTAZIONE_SPESE%ROWTYPE INDEX BY BINARY_INTEGER;

   TYPE typAccertamentoSpese IS TABLE OF nembo_T_ACCERTAMENTO_SPESE%ROWTYPE INDEX BY BINARY_INTEGER;

-- funzione che riporta l'iter del gruppo allo stato precedente a quello attuale (dati in input id procedimento e codice raggruppamento)
FUNCTION RiportaStatoPrecIterGruppo(pIdProcedimento                        NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_PROCEDIMENTO%TYPE,
                                    pCodiceRaggruppamento            NEMBO_T_ITER_PROCEDIMENTO_GRUP.CODICE_RAGGRUPPAMENTO%TYPE) RETURN BOOLEAN;

/* elenco operazioni possibili:
1 - Creazione
2 - Chiusura
3 - Riapertura
4 - Trasmissione
5 - Approvazione
6 - Avvio istruttoria
7 - Calcolo del premio
8 - Eliminazione
9 - Liquidazione (invio)
10 - Liquidazione (ritorno)
*/
PROCEDURE MainGestioneStati(pIdProcedimento                        nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                                             pIdProcedimentoOggetto             nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                             pIdLegameGruppoOggetto           nembo_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                                             pIdOperazione                           PLS_INTEGER,
                                             pIdEsito                                     nembo_D_ESITO.ID_ESITO%TYPE,
                                             pCodAttore                                nembo_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE,
                                             pIdBando                                   nembo_D_BANDO.ID_BANDO%TYPE,
                                             --pNumIstruttorie                          PLS_INTEGER,
                                             pCodiceRaggruppamento            nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE,
                                             --pExtIdUtente                              nembo_T_PROCEDIMENTO_OGGETTO.EXT_ID_UTENTE_AGGIORNAMENTO%TYPE,
                                             pIdStatoNuovoProcedimento        OUT nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE,
                                             pIdStatoNuovoOggetto                OUT NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE,
                                             pIdStatoNuovoGruppo                 OUT NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE,
                                             pIdEsitoNuovo                            OUT nembo_D_ESITO.ID_ESITO%TYPE,
                                             pRisultato                                  OUT NUMBER,
                                             pMessaggio                               OUT VARCHAR2);

 PROCEDURE DeleteCascade(table_owner   VARCHAR2,
                        parent_table  VARCHAR2,
                        where_clause  VARCHAR2);
  -- creazione dell'oggetto
    FUNCTION MainCreazione(pIdBando                    nembo_D_BANDO.ID_BANDO%TYPE,
                           pIdLegameGruppoOggetto           nembo_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                           --pIdProcedimentoAgricolo            NEMBO_T_PROCEDIMENTO.ID_PROCEDIMENTO_AGRICOLO%TYPE,
                           pIdProcedimento                        nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                           pExtIdAzienda                            SMRGAA_V_DATI_ANAGRAFICI.ID_AZIENDA%TYPE,
                           pCodiceRaggruppamento            nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE,
                           pForzaAlfaNumerico                   CHAR,
                           pExtIdUtente                              NUMBER,
                           pCodAttore                                nembo_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE,
                           pRisultato                                  OUT NUMBER,
                           pMessaggio                               OUT VARCHAR2) RETURN nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE;

  -- chiusura dell'oggetto
  PROCEDURE MainChiusura(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                         pIdEsito                    nembo_D_ESITO.ID_ESITO%TYPE,
                         pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                         pExtIdUtente                NUMBER,
                         pRisultato              OUT NUMBER,
                         pMessaggio              OUT VARCHAR2);

  -- eliminazione dell'oggetto
  PROCEDURE MainEliminazione(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                             pExtIdUtente                NUMBER,
                             pRisultato              OUT NUMBER,
                             pMessaggio              OUT VARCHAR2);

  -- riapertura dell'oggetto
  PROCEDURE MainRiapertura(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                           pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                           pExtIdUtente                NUMBER,
                           pRisultato              OUT NUMBER,
                           pMessaggio              OUT VARCHAR2);

  -- trasmissione - componente che gestisce il cambio stato
  PROCEDURE MainTrasmissione(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                               pIdEsito                    nembo_D_ESITO.ID_ESITO%TYPE,
                                               pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                                               pNoteGruppo             NEMBO_T_ITER_PROCEDIMENTO_GRUP.NOTE%TYPE,
                                               pExtIdUtente                NUMBER,
                                               pRisultato              OUT NUMBER,
                                               pMessaggio              OUT VARCHAR2);

  -- approvazione - componente che gestisce il cambio stato
  PROCEDURE MainApprovazione(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                               pIdEsito                    nembo_D_ESITO.ID_ESITO%TYPE,
                                               pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                                               pNoteGruppo             NEMBO_T_ITER_PROCEDIMENTO_GRUP.NOTE%TYPE,
                                               pExtIdUtente                NUMBER,
                                               pRisultato              OUT NUMBER,
                                               pMessaggio              OUT VARCHAR2);

  -- avvio istruttoria - componente che gestisce il cambio stato
  PROCEDURE MainAvvioIstruttoria(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                                  pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                                                  pNoteGruppo             NEMBO_T_ITER_PROCEDIMENTO_GRUP.NOTE%TYPE,
                                                  pExtIdUtente                NUMBER,
                                                  pRisultato              OUT NUMBER,
                                                  pMessaggio              OUT VARCHAR2);

END PCK_NEMBO_GESTIONE_PROC_OGGETT;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_GESTIONE_PROC_OGGETT" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_GESTIONE_PROC_OGGETT" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package PCK_NEMBO_SERVIZI_X_FRUITORI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "NEMBO"."PCK_NEMBO_SERVIZI_X_FRUITORI" IS

  TYPE recOggDichConsDiff IS RECORD (DESC_BANDO            NEMBO_D_BANDO.DENOMINAZIONE%TYPE);
  TYPE typTbOggDichConsDiff      IS TABLE OF recOggDichConsDiff INDEX BY BINARY_INTEGER;

  /* procedura che dati:
        - id azienda
        - anno campagna
        - dichiarazione di consistenza
     verifica che non esistano sul PSR procedimenti
                - non annullati
                - di tipo DAP/DP/ARTxx
                - della stessa azienda
                - dello stesso anno campagna
                - di dich cons diversa
                - considerando solo gli oggetti non annullati piu' recenti (max data inizio) per ciascun procedimento
    Restituisce 0 se non ha trovato altri procedimenti, 1 se ne ha trovati ed in questo casi oltre al messaggio d'errore
    restituisce anche la descrizione del bando (usata dal controllo IPR07 di PSR)
    */
    PROCEDURE VerificaProcDDCDifferente(pExtIdAzienda        NEMBO_T_PROCEDIMENTO_AZIENDA.EXT_ID_AZIENDA%TYPE,
                                                          pAnnoCampagna    NEMBO_D_BANDO.ANNO_CAMPAGNA%TYPE,
                                                          pExtIdDichCons       NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_ID_DICHIARAZIONE_CONSISTEN%TYPE,
                                                          pDescBando           OUT VARCHAR2,
                                                          pRisultato               OUT NUMBER,
                                                          pMessaggio            OUT VARCHAR2
                                                          );

END PCK_NEMBO_SERVIZI_X_FRUITORI;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_SERVIZI_X_FRUITORI" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_SERVIZI_X_FRUITORI" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package PCK_NEMBO_UTILITY
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "NEMBO"."PCK_NEMBO_UTILITY" IS
 TYPE tblParametri IS TABLE OF VARCHAR2(4000) INDEX BY BINARY_INTEGER;
 
  cnIdProcedimentoNEMBO        CONSTANT SMRGAA_V_DICH_CONSISTENZA.ID_PROCEDIMENTO%TYPE := 53; 
  -- tipo vincolo
  cnIdVincoloInfoUniv                   CONSTANT NEMBO_D_VINCOLO_INFO.ID_VINCOLO_DICHIARAZIONE%TYPE := 1; -- univoco
  cnIdVincoloInfoObbl                   CONSTANT NEMBO_D_VINCOLO_INFO.ID_VINCOLO_DICHIARAZIONE%TYPE := 2; -- obbligatorio
  cnIdVincoloInfoOpzUniv              CONSTANT NEMBO_D_VINCOLO_INFO.ID_VINCOLO_DICHIARAZIONE%TYPE := 3; -- opzionale univoco    
 
 -- stati
  cnIdStatoBozza                        CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=1;
  cnIdStatoChiusoDaFirm             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=2;
  cnIdStatoChiusoFirmato            CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=3;
  cnIdStatoInAttesaTrasm           CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=4;
  cnIdStatoInCorso                     CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=5;
  cnIdStatoTrasmesso                CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=10;
  cnIdStatoChiuso                       CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=11;
  cnIdStatoAmmessoFinanz         CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=12;
  cnIdStatoNonAmmessoFinanz   CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=13;
  cnIdStatoIstruttoriaInCorso      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=14;
  cnIdStatoIstruttoriaAvviata      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=15;
  cnIdStatoApprovato                 CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=17;
  cnIdStatoRifiutato                    CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=18;
  cnIdStatoAmmissibileFnz         CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=19;
  cnIdStatoNonAmmissibileFnz    CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=20;
  cnIdStatoInIstrXAnticISGC     CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=21;
  cnIdStatoIstruttoriaApprovata      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=22;
  cnIdStatoWaitingForCD              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=23;
  cnIdStatoCorreggibile              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=24;
  cnIdStatoCorrettivaChiusa              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=25;
  cnIdStatoPreavvisoRigettoTot              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=26;
  cnIdStatoPreavvisoRigettoPrz              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=27;
  cnIdStatoAvvioProcedimento       CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=28;
  cnIdStatoAmmessoFinanzWithAnt       CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=29;
  cnIdStatoIstruttoriaConclusa      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=30;
  cnIdStatoAmmessoFinanzWithAcc       CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=31;
  cnIdStatoIstrInCorsoFollowinCD      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=32;
  cnIdStatoIstrConclusaFollownCD      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=33;
  cnIdStatoCDTrasmesse                  CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=34;
  cnIdStatoAmmessoFinanzWithSld       CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=35;
  cnIdStatoInLiquidazione                CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=37;
  cnIdStatoLiquidato                     CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=38;
  cnIdStatoLiquidazioneRespinta       CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=40;
  cnIdStatoAnticInCorso               CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=41;
  cnIdStatoAcconInCorso               CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=42;
  cnIdStatoSaldoInCorso               CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=43;
  cnIdStatoSospensioneInCorso         CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=44;
  cnIdStatoRevocaInCorso                CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=45;
  cnIdStatoAcconRespinto              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=46;
  cnIdStatoSaldoApprovPos            CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=47;
  cnIdStatoSaldoRespinto              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=48;
  cnIdStatoInLiquidWithCD             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=49;
  cnIdStatoLiquidatoWithCD            CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=50;
  cnIdStatoInAttesaFirma              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=51;
  cnIdStatoActionConclusa             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=52;
  cnIdStatoAmmissibParzFinanz         CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=53;
  cnIdStatoAmmessoParzFinanz          CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=54;
  cnIdStatoAnticApprovPos             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=55;
  cnIdStatoAnticRespinto              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=56;
  cnIdStatoAcconApprovPos             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=57;
  cnIdStatoIstrConclusaPos            CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=58;
  cnIdStatoIstrConclusaNeg            CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=59;
  cnIdStatoIstrConclPosFollownCD      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=60;
  cnIdStatoIstrConclNegFollownCD      CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=61;
  cnIdStatoIstrNotApprov              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=62;
  cnIdStatoAnticInLiquid              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=63;
  cnIdStatoAnticLiquidato             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=64;
  cnIdStatoAcconInLiquid              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=65;
  cnIdStatoAcconLiquidato             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=66;
  cnIdStatoSaldoInLiquid              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=67;
  cnIdStatoSaldoLiquidato             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=68;
  cnIdStatoIntegInLiquid              CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=69;
  cnIdStatoIntegLiquidata             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=70;
  cnIdStatoIntegInCorso             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=71;
  cnIdStatoIntegApprovata             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=72;
  cnIdStatoRevocaRespinta             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=73;
  cnIdStatoLiquidMaNonErog             CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=74;
  cnIdStatoRevocato                     CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=96;
  cnIdStatoPosizioneNonFinanz           CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=97;
  cnIdStatoRinuncia                     CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=98;
  cnIdStatoAnnullato                   CONSTANT NEMBO_D_STATO_OGGETTO.ID_STATO_OGGETTO%TYPE:=99;
  
  -- attori
  cvCodAttoreReg             CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='FUNZIONARIO_REGIONALE';
  cvCodAttoreProv            CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='FUNZIONARIO_PROVINCIALE';
  cvCodAttoreCM             CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='FUNZIONARIO_CM';
  cvCodAttoreGAL            CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='FUNZIONARIO_GAL';
  cvCodAttoreOPR               CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='FUNZIONARIO_OPR';
  cvCodAttoreInterm             CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='INTERMEDIARIO_CAA';
  cvCodAttoreBenef             CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='BENEFICIARIO';
  cvCodAttoreAss             CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='ASSISTENZA';
  cvCodAttoreMonit            CONSTANT NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE:='ASSISTENZA MONIT';
  
  -- icone
  cnIdIconaApprovIstr                  CONSTANT NEMBO_D_ICONA.ID_ICONA%TYPE:=23;
  
  -- esiti
  cnIdEsitoTrasmessoI                   CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=4;
  cnIdEsitoApprovPosO                   CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=26;
  cnIdEsitoApprovNegO                   CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=27;
  cnIdEsitoApprovParzPosO               CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=28;
  cnIdEsitoAmmessoFinanzO               CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=45;
  cnIdEsitoNotAmmessoFinanzO            CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=46;
  cnIdEsitoAmmessoParzFinanzO           CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=47;
  cnIdEsitoPreavvRigettoTotO             CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=48;
  cnIdEsitoPreavvRigettoParzO            CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=49;
  cnIdEsitoAmmissibFinanzO              CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=50;
  cnIdEsitoNotAmmissibFinanzO           CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=51;
  cnIdEsitoAmmissibParzFinanzO          CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=52;
  cnIdEsitoAnnullatoO                   CONSTANT NEMBO_D_ESITO.ID_ESITO%TYPE:=54;
 
 FUNCTION InsLog(pDescrizioneErrore  NEMBO_T_LOG.DESCRIZIONE_ERRORE%TYPE) RETURN NEMBO_T_LOG.ID_LOG%TYPE;
 
 -- ritorna il CUAA in base all'id_azienda
 FUNCTION ReturnCuaa(pExtIdAzienda  SMRGAA_V_DATI_ANAGRAFICI.ID_AZIENDA%TYPE) RETURN SMRGAA_V_DATI_ANAGRAFICI.CUAA%TYPE;
 
 -- Ritorna l'elenco dei valori dei parametri
  FUNCTION ReturnValoriParametri(pValore      NEMBO_T_VALORI_PARAMETRI.VALORE%TYPE,
                                 pSeparatore  VARCHAR2) RETURN tblParametri;
                                 
  -- funzione che rende la capienza del dato bando per il dato id livello
FUNCTION RendiCapienzaBandoLiv(par_nIdProcedimento      NEMBO_R_PROCEDIMENTO_LIVELLO.ID_PROCEDIMENTO%TYPE,
                               par_nIdBando       NEMBO_D_BANDO.ID_BANDO%TYPE,
                               par_nIdLivello     NEMBO_R_PROCEDIMENTO_LIVELLO.ID_LIVELLO%TYPE) RETURN NEMBO_T_RISORSE_LIVELLO_BANDO.RISORSE_ATTIVATE%TYPE;                                 

-- recupero se l'istanza e' stata creata dal beneficiario
  FUNCTION ReturnIstanzaCreataDaBenef(pIdLegameGruppoOggetto NEMBO_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE) RETURN BOOLEAN;

  -- ritorna la dichiarazione di consistenza in base all'id_azienda
FUNCTION ReturnDichConsistenza(pExtIdAzienda        SMRGAA_V_DICH_CONSISTENZA.ID_AZIENDA%TYPE,
                                                  pDataRiferimento   SMRGAA_V_DICH_CONSISTENZA.DATA%TYPE) RETURN SMRGAA_V_DICH_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE;

-- funzione che recupera l'ultimo oggetto chiuso valido legato al procedimento precedente all'oggetto passato in input data la sequenza di codici oggetti intervallati da virgola (es. DAP,ART15,ART3)
-- valori possibili del selettore:
-- - 1: DAP,DP,ART3,ART15
FUNCTION ReturnLastObjMinorIpse(par_nIdProcedimento   NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE,
                                par_nIdProcedimentoOggetto     NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                pSelettore                                   PLS_INTEGER,
                                pRisultato                                  OUT NUMBER,
                                pMessaggio                               OUT VARCHAR2) RETURN NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE;
                                
  -- funzione che recupera l'ultimo oggetto chiuso valido legato al procedimento data la sequenza di codici oggetti intervallati da virgola (es. DAP,ART15,ART3)
  FUNCTION ReturnLastObj(par_nIdProcedimento   NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE,
                         par_nElencoOggetti    VARCHAR2) RETURN NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE;  

  -- In base ai parametri di input verifica la presenza di un quadro
  FUNCTION PresenzaQuadro(pIdBando                NEMBO_D_BANDO.ID_BANDO%TYPE,
                          pIdLegameGruppoOggetto  NEMBO_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                          pCodQuadro              NEMBO_D_QUADRO.CODICE%TYPE) RETURN BOOLEAN;
                          
  -- restituisce il check digit data la stringa
  FUNCTION ReturnCheckDigit(pStringa VARCHAR2) RETURN CHAR;                          

  FUNCTION ReturnMsgErrore(pIdMessaggioErrore  NEMBO_D_MESSAGGIO_ERRORE.ID_MESSAGGIO_ERRORE%TYPE) RETURN NEMBO_D_MESSAGGIO_ERRORE.DESCRIZIONE%TYPE;
                                
END PCK_NEMBO_UTILITY;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_UTILITY" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_UTILITY" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package Body PCK_NEMBO_CALCOLO_PUNTEGGIO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "NEMBO"."PCK_NEMBO_CALCOLO_PUNTEGGIO" IS

-- funzione che inserisce il criterio
FUNCTION InserisciCriterio(pIdDatiProcedimentoPunti  NEMBO_T_PUNTEGGIO_CALCOLATO.ID_DATI_PROCEDIMENTO_PUNTI%TYPE,
                           pIdBandoLivelloCriterio   NEMBO_T_PUNTEGGIO_CALCOLATO.ID_BANDO_LIVELLO_CRITERIO%TYPE,
                           pPunteggioCalcolato       NEMBO_T_PUNTEGGIO_CALCOLATO.PUNTEGGIO_CALCOLATO%TYPE) RETURN BOOLEAN IS
BEGIN
  INSERT INTO NEMBO_T_PUNTEGGIO_CALCOLATO
  (ID_PUNTEGGIO_CALCOLATO,ID_DATI_PROCEDIMENTO_PUNTI,ID_BANDO_LIVELLO_CRITERIO,PUNTEGGIO_CALCOLATO)
  VALUES
  (SEQ_NEMBO_T_PUNTEGGIO_CALCOLAT.NEXTVAL,pIdDatiProcedimentoPunti,pIdBandoLivelloCriterio,pPunteggioCalcolato);

  RETURN TRUE;

EXCEPTION
  WHEN OTHERS THEN
    RETURN FALSE;
END InserisciCriterio;

PROCEDURE Main(pIdProcedimento             NEMBO_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
               pIdProcedimentoOggetto      NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
               pExtIdAzienda               NEMBO_T_PROCEDIMENTO_AZIENDA.EXT_ID_AZIENDA%TYPE,
               pIdBando                    NEMBO_D_BANDO.ID_BANDO%TYPE,
               pIdLivello                  NEMBO_R_LIVELLO_CRITERIO.ID_LIVELLO%TYPE,
               pRaggruppamento             NEMBO_R_LIVELLO_CRITERIO.RAGGRUPPAMENTO%TYPE,
               pRisultato              OUT NUMBER,
               pMessaggio              OUT VARCHAR2) IS

  ERRORE                    EXCEPTION;
  nIdLivello                NEMBO_R_LIVELLO_CRITERIO.ID_LIVELLO%TYPE;
  nIdBandoOggetto           NEMBO_R_BANDO_OGGETTO.ID_BANDO_OGGETTO%TYPE;
  nAnnoCampagna             NEMBO_D_BANDO.ANNO_CAMPAGNA%TYPE;
  nIdDatiProcedimentoPunti  NEMBO_T_DATI_PROCEDIMENTO_PUNT.ID_DATI_PROCEDIMENTO_PUNTI%TYPE;
  nIdStatoProcedimento      NEMBO_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
  TbCriterio                typTbCriteri;
  vTipoLivello              NEMBO_D_TIPO_LIVELLO.CODICE%TYPE;
  vIdentificativo           NEMBO_T_PROCEDIMENTO.IDENTIFICATIVO%TYPE;
  nIdLegameGruppoOggetto    NEMBO_T_PROCEDIMENTO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;

BEGIN
  pRisultato := 0;
  pMessaggio := '';

  nIdLivello := pIdLivello;

  -- ricavo lo stato del procedimento
  SELECT ID_STATO_OGGETTO
  INTO   nIdStatoProcedimento
  FROM   NEMBO_T_PROCEDIMENTO
  WHERE  ID_PROCEDIMENTO = pIdProcedimento;

  TbCriterio.DELETE;

  -- innanzitutto ricavo l'id dati procedimento punti, legato a id procedimento, e se non esiste (prima volta) lo creo
  BEGIN
    SELECT ID_DATI_PROCEDIMENTO_PUNTI
    INTO   nIdDatiProcedimentoPunti
    FROM   NEMBO_T_DATI_PROCEDIMENTO_PUNT
    WHERE  ID_PROCEDIMENTO         = pIdProcedimento
    AND    ID_PROCEDIMENTO_OGGETTO IS NULL;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      INSERT INTO NEMBO_T_DATI_PROCEDIMENTO_PUNT
      (ID_DATI_PROCEDIMENTO_PUNTI, ID_PROCEDIMENTO, ID_PROCEDIMENTO_OGGETTO)
      VALUES (SEQ_NEMBO_T_DATI_PROCEDIMEN_PU.NEXTVAL, pIdProcedimento, NULL)
      RETURNING ID_DATI_PROCEDIMENTO_PUNTI INTO nIdDatiProcedimentoPunti;
  END;

  -- elimino i criteri AUTOMATICI vecchi
  DELETE NEMBO_T_PUNTEGGIO_CALCOLATO
  WHERE  ID_PUNTEGGIO_CALCOLATO IN (SELECT PC.ID_PUNTEGGIO_CALCOLATO
                                    FROM   NEMBO_T_PUNTEGGIO_CALCOLATO PC, NEMBO_R_BANDO_LIVELLO_CRITERIO BLC, NEMBO_R_LIVELLO_CRITERIO LC,
                                           NEMBO_D_CRITERIO CC
                                    WHERE  PC.ID_DATI_PROCEDIMENTO_PUNTI = nIdDatiProcedimentoPunti
                                    AND    PC.ID_BANDO_LIVELLO_CRITERIO  = BLC.ID_BANDO_LIVELLO_CRITERIO
                                    AND    BLC.ID_LIVELLO_CRITERIO       = LC.ID_LIVELLO_CRITERIO
                                    AND    LC.ID_CRITERIO                = CC.ID_CRITERIO
                                    AND    CC.FLAG_ELABORAZIONE          = 'A'
                                    AND    LC.ID_LIVELLO                 = NVL(nIdLivello,LC.ID_LIVELLO)
                                    AND    NVL(LC.RAGGRUPPAMENTO,'-')    = NVL(pRaggruppamento,'-'));

  COMMIT;

  -- recupero criteri AUTOMATICI dato il bando
  SELECT BLC.ID_BANDO_LIVELLO_CRITERIO, CC.CODICE CODICE_CRITERIO, LL.ID_LIVELLO, LL.CODICE_LIVELLO MISURA, LC.RAGGRUPPAMENTO
  BULK COLLECT INTO TbCriterio
  FROM   NEMBO_R_BANDO_LIVELLO_CRITERIO BLC, NEMBO_R_LIVELLO_CRITERIO LC, NEMBO_D_CRITERIO CC, NEMBO_D_LIVELLO LL, NEMBO_D_PRINCIPIO_SELEZIONE PS
  WHERE  BLC.ID_LIVELLO_CRITERIO    = LC.ID_LIVELLO_CRITERIO
  AND    LC.ID_CRITERIO             = CC.ID_CRITERIO
  AND    BLC.ID_BANDO               = pIdBando
  AND    LC.ID_LIVELLO              = LL.ID_LIVELLO
  AND    CC.FLAG_ELABORAZIONE       = 'A'
  AND    LC.ID_LIVELLO              = NVL(nIdLivello,LC.ID_LIVELLO)
  AND    NVL(LC.RAGGRUPPAMENTO,'-') = NVL(pRaggruppamento,'-')
  AND    CC.ID_PRINCIPIO_SELEZIONE  = PS.ID_PRINCIPIO_SELEZIONE
  ORDER BY PS.ORDINE, CC.ORDINE;

  -- recupero dati utili
  SELECT BO.ID_BANDO_OGGETTO, TL.CODICE, BB.ANNO_CAMPAGNA, PP.IDENTIFICATIVO, PO.ID_LEGAME_GRUPPO_OGGETTO
  INTO   nIdBandoOggetto, vTipoLivello, nAnnoCampagna, vIdentificativo, nIdLegameGruppoOggetto
  FROM   NEMBO_T_PROCEDIMENTO_OGGETTO PO, NEMBO_T_PROCEDIMENTO PP, NEMBO_R_BANDO_OGGETTO BO, NEMBO_D_BANDO BB, NEMBO_D_TIPO_LIVELLO TL
  WHERE  PO.ID_PROCEDIMENTO_OGGETTO  = pIdProcedimentoOggetto
  AND    PO.ID_PROCEDIMENTO          = PP.ID_PROCEDIMENTO
  AND    PP.ID_BANDO                 = BO.ID_BANDO
  AND    PO.ID_LEGAME_GRUPPO_OGGETTO = BO.ID_LEGAME_GRUPPO_OGGETTO
  AND    PP.ID_BANDO                 = BB.ID_BANDO
  AND    BB.ID_TIPO_LIVELLO          = TL.ID_TIPO_LIVELLO;

  -- infine, inserisco i criteri MANUALI che non esistono e tutti con punteggio a 0
  FOR recCriteriManuali IN (SELECT BLC.ID_BANDO_LIVELLO_CRITERIO, CC.CODICE CODICE_CRITERIO, LL.ID_LIVELLO, LL.CODICE_LIVELLO MISURA
                            FROM   NEMBO_R_BANDO_LIVELLO_CRITERIO BLC, NEMBO_R_LIVELLO_CRITERIO LC, NEMBO_D_CRITERIO CC, NEMBO_D_LIVELLO LL
                            WHERE  BLC.ID_LIVELLO_CRITERIO    = LC.ID_LIVELLO_CRITERIO
                            AND    LC.ID_CRITERIO             = CC.ID_CRITERIO
                            AND    BLC.ID_BANDO               = pIdBando
                            AND    LC.ID_LIVELLO              = LL.ID_LIVELLO
                            AND    LC.ID_LIVELLO              = NVL(nIdLivello,LC.ID_LIVELLO)
                            AND    NVL(LC.RAGGRUPPAMENTO,'-') = NVL(pRaggruppamento,'-')
                            AND    CC.FLAG_ELABORAZIONE       = 'M'
                            AND    NOT EXISTS                 (SELECT 'X'
                                                               FROM   NEMBO_T_PUNTEGGIO_CALCOLATO
                                                               WHERE  ID_DATI_PROCEDIMENTO_PUNTI =  nIdDatiProcedimentoPunti
                                                               AND    ID_BANDO_LIVELLO_CRITERIO  = BLC.ID_BANDO_LIVELLO_CRITERIO)) LOOP

    IF NOT InserisciCriterio(nIdDatiProcedimentoPunti,recCriteriManuali.ID_BANDO_LIVELLO_CRITERIO,0) THEN
      pMessaggio := 'Errore durante l''inserimento del criterio (codice:'||recCriteriManuali.CODICE_CRITERIO||')';
      RAISE ERRORE;
    END IF;
  END LOOP;

EXCEPTION
  WHEN ERRORE THEN
    pRisultato := 2;
    ROLLBACK;
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema = '||PCK_NEMBO_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
    ROLLBACK;
END Main;

END PCK_NEMBO_CALCOLO_PUNTEGGIO;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CALCOLO_PUNTEGGIO" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CALCOLO_PUNTEGGIO" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package Body PCK_NEMBO_CONTROLLI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "NEMBO"."PCK_NEMBO_CONTROLLI" IS

-- inizializzazione di variabili globali
PROCEDURE Init(pExtIdAzienda  NUMBER) IS
BEGIN
  gvCuaa      := PCK_NEMBO_UTILITY.ReturnCuaa(pExtIdAzienda);
  gnIdAzienda := pExtIdAzienda;
END Init;

-- funzione che verifica il rispetto dei vincoli per il quadro passato in input
FUNCTION ReturnValiditaVincolo(pIdBandoOggetto          nembo_D_GRUPPO_INFO.ID_BANDO_OGGETTO%TYPE,
                               pIdProcedimentoOggetto   nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                               pCodQuadro               nembo_D_QUADRO.CODICE%TYPE) RETURN BOOLEAN IS                        
  
  TbLegameInfo          typTbLegameInfo;  
  nCont                 PLS_INTEGER;

BEGIN
        -- raggruppo i vincoli distinti
        SELECT DISTINCT LI.*
        BULK COLLECT INTO TbLegameInfo
        FROM nembo_D_QUADRO QQ, nembo_D_OGGETTO OO, nembo_R_QUADRO_OGGETTO QO,nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_T_PROCEDIMENTO_OGGETTO PO,
             nembo_D_GRUPPO_INFO GI, nembo_D_DETTAGLIO_INFO DI, nembo_D_LEGAME_INFO LI, nembo_D_VINCOLO_INFO VI
        WHERE QQ.CODICE=pCodQuadro
        AND QQ.ID_QUADRO=QO.ID_QUADRO
        AND OO.ID_OGGETTO=QO.ID_OGGETTO
        AND OO.ID_OGGETTO=LGO.ID_OGGETTO
        AND LGO.ID_LEGAME_GRUPPO_OGGETTO=PO.ID_LEGAME_GRUPPO_OGGETTO
        AND PO.ID_PROCEDIMENTO_OGGETTO=pIdProcedimentoOggetto
        AND QO.ID_QUADRO_OGGETTO=GI.ID_QUADRO_OGGETTO
        AND GI.ID_BANDO_OGGETTO=pIdBandoOggetto
        AND GI.ID_GRUPPO_INFO=DI.ID_GRUPPO_INFO
        AND DI.ID_LEGAME_INFO=LI.ID_LEGAME_INFO
        AND LI.ID_VINCOLO_DICHIARAZIONE=VI.ID_VINCOLO_DICHIARAZIONE;  

        FOR i IN 1..TbLegameInfo.COUNT LOOP
                -- conto le dichiarazioni presenti sull'oggetto
                SELECT COUNT(*)
                INTO nCont
                FROM nembo_T_SELEZIONE_INFO SI, nembo_D_DETTAGLIO_INFO DI
                WHERE SI.ID_PROCEDIMENTO_OGGETTO=pIdProcedimentoOggetto
                AND SI.ID_DETTAGLIO_INFO=DI.ID_DETTAGLIO_INFO
                AND NVL(DI.ID_LEGAME_INFO,0)=TbLegameInfo(i).ID_LEGAME_INFO;
                -- discrimino
                IF TbLegameInfo(i).ID_VINCOLO_DICHIARAZIONE = PCK_nembo_UTILITY.cnIdVincoloInfoUniv THEN
                        IF nCont <> 1 THEN
                               RETURN FALSE;
                        END IF;
                ELSIF TbLegameInfo(i).ID_VINCOLO_DICHIARAZIONE = PCK_nembo_UTILITY.cnIdVincoloInfoObbl THEN
                        IF nCont = 0 THEN
                               RETURN FALSE;
                        END IF;
                ELSIF TbLegameInfo(i).ID_VINCOLO_DICHIARAZIONE = PCK_nembo_UTILITY.cnIdVincoloInfoOpzUniv THEN
                        IF nCont > 1 THEN
                               RETURN FALSE;
                        END IF;                                                                                                
                END IF;                
        END LOOP;

        RETURN TRUE;
END ReturnValiditaVincolo;    

FUNCTION ReturnCountSegnaposto(pCodQuadro  nembo_D_QUADRO.CODICE%TYPE) RETURN BOOLEAN IS
  
  nRecStringa  SIMPLE_INTEGER := 0;
  nCont        SIMPLE_INTEGER := 0;
BEGIN  
  SELECT NVL(SUM(STRINGA),0)
  INTO   nRecStringa
  FROM   (SELECT NVL(((LENGTH(DI.DESCRIZIONE) - LENGTH(REPLACE(DI.DESCRIZIONE, '$$', ''))) / 2),0) STRINGA
          FROM   nembo_D_QUADRO QQ, nembo_D_OGGETTO OO, nembo_R_QUADRO_OGGETTO QO,nembo_R_LEGAME_GRUPPO_OGGETTO LGO, 
                 nembo_T_PROCEDIMENTO_OGGETTO PO,nembo_D_GRUPPO_INFO GI, nembo_D_DETTAGLIO_INFO DI
          WHERE  QQ.CODICE                    = pCodQuadro
          AND    QQ.ID_QUADRO                 = QO.ID_QUADRO
          AND    OO.ID_OGGETTO                = QO.ID_OGGETTO
          AND    OO.ID_OGGETTO                = LGO.ID_OGGETTO
          AND    LGO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO
          AND    PO.ID_PROCEDIMENTO_OGGETTO   = gnIdProcedimentoOggetto
          AND    QO.ID_QUADRO_OGGETTO         = GI.ID_QUADRO_OGGETTO
          AND    GI.ID_BANDO_OGGETTO          = gnIdBandoOggetto
          AND    GI.ID_GRUPPO_INFO            = DI.ID_GRUPPO_INFO
          AND    (DI.FLAG_OBBLIGATORIO        = 'S' 
                  OR  DI.FLAG_OBBLIGATORIO    = 'N' AND EXISTS (SELECT 'X'
                                                                FROM   nembo_T_SELEZIONE_INFO SI
                                                                WHERE  SI.ID_PROCEDIMENTO_OGGETTO = gnIdProcedimentoOggetto
                                                                AND    SI.ID_DETTAGLIO_INFO       = DI.ID_DETTAGLIO_INFO)));
  
  SELECT COUNT(*)
  INTO   nCont
  FROM   nembo_D_QUADRO QQ, nembo_D_OGGETTO OO, nembo_R_QUADRO_OGGETTO QO,nembo_R_LEGAME_GRUPPO_OGGETTO LGO, 
         nembo_T_PROCEDIMENTO_OGGETTO PO,nembo_D_GRUPPO_INFO GI, nembo_D_DETTAGLIO_INFO DI,nembo_T_VALORI_INSERITI VI,
         nembo_T_SELEZIONE_INFO SI
  WHERE  QQ.CODICE                    = pCodQuadro
  AND    QQ.ID_QUADRO                 = QO.ID_QUADRO
  AND    OO.ID_OGGETTO                = QO.ID_OGGETTO
  AND    OO.ID_OGGETTO                = LGO.ID_OGGETTO
  AND    LGO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO
  AND    PO.ID_PROCEDIMENTO_OGGETTO   = gnIdProcedimentoOggetto
  AND    QO.ID_QUADRO_OGGETTO         = GI.ID_QUADRO_OGGETTO
  AND    GI.ID_BANDO_OGGETTO          = gnIdBandoOggetto
  AND    GI.ID_GRUPPO_INFO            = DI.ID_GRUPPO_INFO
  AND    VI.ID_SELEZIONE_INFO         = SI.ID_SELEZIONE_INFO
  AND    SI.ID_DETTAGLIO_INFO         = DI.ID_DETTAGLIO_INFO
  AND    SI.ID_PROCEDIMENTO_OGGETTO   = PO.ID_PROCEDIMENTO_OGGETTO
  AND    (DI.FLAG_OBBLIGATORIO        = 'S' 
          OR  DI.FLAG_OBBLIGATORIO    = 'N' AND EXISTS (SELECT 'X'
                                                        FROM   nembo_T_SELEZIONE_INFO SI1
                                                        WHERE  SI1.ID_PROCEDIMENTO_OGGETTO = gnIdProcedimentoOggetto
                                                        AND    SI1.ID_DETTAGLIO_INFO       = DI.ID_DETTAGLIO_INFO));
                                                        
  IF nRecStringa != nCont THEN
    RETURN FALSE;
  END IF;
  
  RETURN TRUE;
END ReturnCountSegnaposto;

FUNCTION ScriviAnomalieParticelle(pIdParticellaUtilizzo         NEMBO_T_ANOMALIA_PARTICELLA.ID_PARTICELLA_UTILIZZO%TYPE,
                                  pCodiceControllo              NEMBO_D_CONTROLLO.CODICE%TYPE,
                                  pDescAnomalia                 NEMBO_T_ANOMALIA_PARTICELLA.DESCRIZIONE_ANOMALIA%TYPE,
                                  pUlterioriInfo                NEMBO_T_ANOMALIA_PARTICELLA.ULTERIORI_INFORMAZIONI%TYPE,
                                  pGravita                      NEMBO_T_ANOMALIA_PARTICELLA.GRAVITA%TYPE,
                                  pRisultato       OUT NUMBER,
                                  pMessaggio       OUT VARCHAR2) RETURN BOOLEAN IS

BEGIN
      INSERT INTO NEMBO_T_ANOMALIA_PARTICELLA
      (ID_ANOMALIA_PARTICELLA, ID_PARTICELLA_UTILIZZO, DESCRIZIONE_ANOMALIA, ID_CONTROLLO, ULTERIORI_INFORMAZIONI,
       GRAVITA)
      VALUES
      (SEQ_NEMBO_T_ANOMALIA_PARTICELL.NEXTVAL,pIdParticellaUtilizzo,SUBSTR(pDescAnomalia,1,1000),(SELECT ID_CONTROLLO FROM NEMBO_D_CONTROLLO WHERE CODICE = pCodiceControllo),SUBSTR(pUlterioriInfo,1,2000),
       pGravita);

        RETURN(TRUE);
EXCEPTION
        WHEN OTHERS THEN
                pRisultato:=1;
                pMessaggio:='Errore grave nella scrittura delle anomalie particellari = '||SQLERRM;
                RETURN(FALSE);
END ScriviAnomalieParticelle;

FUNCTION MainControlliGravi(pIdBandoOggetto            NEMBO_R_BANDO_OGGETTO.ID_BANDO_OGGETTO%TYPE,
                            pIdProcedimento            NEMBO_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                            pExtIdAzienda              NUMBER,
                            pExtIdUtente               NUMBER,
                            pCodiceRaggruppamento      NEMBO_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE,
                            pRisultato             OUT NUMBER,
                            pMessaggio             OUT VARCHAR2) RETURN LIST_CONTROLLO IS

  controlli       LIST_CONTROLLO := LIST_CONTROLLO();
  vPlSql          VARCHAR2(4000);
  nContRec        SIMPLE_INTEGER := 0;
  ERRORE          EXCEPTION;
  vUlterioriInfo  NEMBO_T_ANOMALIE_CONTROLLI.ULTERIORI_INFORMAZIONI%TYPE;
  cExternalCode1  NEMBO_D_CONTROLLO.EXTERNAL_CODE%TYPE;
  cExternalCode2  NEMBO_D_CONTROLLO.EXTERNAL_CODE%TYPE;
  cExternalCode3  NEMBO_D_CONTROLLO.EXTERNAL_CODE%TYPE;
BEGIN
  controlli.DELETE;

  -- inizializzazione di variabili globali
  Init(pExtIdAzienda);
  gnIdProcedimento := pIdProcedimento;
  gnIdProcedimentoOggetto := NULL;
  gnIdBandoOggetto        := pIdBandoOggetto;
  gnExtIdUtente := pExtIdUtente;
  gnCodiceRaggruppamento := pCodiceRaggruppamento;

  IF gvCuaa IS NULL THEN
    pMessaggio := 'ERRORE DI SISTEMA = IMPOSSIBILE VALORIZZARE IL CUAA DELL''AZIENDA';
    RAISE ERRORE;
  END IF;

  FOR recControlli IN (SELECT C.*
                       FROM   NEMBO_R_BANDO_OGGETTO_CONTROLL BOC,NEMBO_D_CONTROLLO C
                       WHERE  BOC.ID_BANDO_OGGETTO = pIdBandoOggetto
                       AND    BOC.GRAVITA          = 'G'
                       AND    BOC.ID_CONTROLLO     = C.ID_CONTROLLO
                       AND    C.ID_FONTE_CONTROLLO = 1
                       AND    TRUNC(SYSDATE)       BETWEEN TRUNC(C.DATA_INIZIO) AND NVL(TRUNC(C.DATA_FINE),TRUNC(SYSDATE))
                       ORDER BY C.CODICE) LOOP

    IF recControlli.EXTERNAL_CODE IS NULL THEN
        BEGIN
              vPlSql := 'BEGIN PCK_NEMBO_CONTROLLI.'||recControlli.CODICE||'(:Risultato,:Messaggio,:UlterioriInfo); END;';
              EXECUTE IMMEDIATE vPlSql USING OUT pRisultato, OUT pMessaggio, OUT vUlterioriInfo;
        EXCEPTION
                WHEN OTHERS THEN
                        pRisultato := 1;
                        pMessaggio := 'Controllo non classificato';
                        vUlterioriInfo := NULL;
        END;
    ELSIF recControlli.EXTERNAL_CODE IS NOT NULL AND NVL(DBMS_LOB.GETLENGTH(recControlli.EXTERNAL_CODE),0) != 0 THEN
      -- gestione controlli d'emergenza superiori a 32k
      IF DBMS_LOB.getlength(recControlli.EXTERNAL_CODE) > 32000 THEN
        cExternalCode1 := DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32000,1);
        cExternalCode2 := DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32000,32001);
        cExternalCode3 := DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32000,64002);

        EXECUTE IMMEDIATE cExternalCode1||cExternalCode2||cExternalCode3
        USING IN gvCuaa,IN gnIdAzienda,IN gnIdProcedimentoOggetto,IN gnIdProcedimento,IN gnExtIdUtente,IN gnCodiceRaggruppamento,
              IN gnIdBandoOggetto,IN gnIdDatiProcedimento,OUT pRisultato, OUT pMessaggio, OUT vUlterioriInfo;
      ELSE
        EXECUTE IMMEDIATE DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32765,1)
        USING IN gvCuaa, IN gnIdAzienda,IN gnIdProcedimentoOggetto, IN gnIdProcedimento, IN gnExtIdUtente, IN gnCodiceRaggruppamento,
              IN gnIdBandoOggetto,IN gnIdDatiProcedimento,OUT pRisultato, OUT pMessaggio, OUT vUlterioriInfo;
      END IF;
    END IF;

    IF pRisultato != 0 THEN
      nContRec := nContRec + 1;
      controlli.EXTEND(1);
      controlli(nContRec) := OBJ_CONTROLLO(recControlli.CODICE,recControlli.DESCRIZIONE,pMessaggio);
    END IF;
  END LOOP;

  pRisultato := 0;
  pMessaggio := '';

  IF controlli.COUNT != 0 THEN
    pRisultato := 2;
    pMessaggio := 'E'' stata riscontrata almeno un''anomalia grave';
  END IF;

  RETURN controlli;
EXCEPTION
  WHEN ERRORE THEN
    pRisultato := 1;
    RETURN NULL;
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'ERRORE DI SISTEMA. Codice Errore = '||
                   PCK_NEMBO_UTILITY.InsLog('MainControlliGravi - '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'- CUAA='||gvCUAA||' / id_azienda='||TO_CHAR(gnIdAzienda)||' / id_bando_oggetto='||TO_CHAR(gnIdBandoOggetto)||' / id_procedimento='||TO_CHAR(gnIdProcedimento)||' / id_procedimento_oggetto='||TO_CHAR(gnIdProcedimentoOggetto)||' / utente='||TO_CHAR(pExtIdUtente)||' / Codice_Raggruppamento = '||TO_CHAR(gnCodiceRaggruppamento));
    RETURN NULL;
END MainControlliGravi;

/* funzione che bypassa la reale esecuzione dei controlli */
FUNCTION BypassOn(pIdProcedimentoOggetto      NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE)
                       RETURN BOOLEAN IS
nCont                              INTEGER;

BEGIN
         /* ricerco se la pratica è su elab_massiva con esito=R */
         SELECT COUNT(1)
         INTO nCont
         FROM NEMBO_T_ELAB_MASSIVA EM
         WHERE EM.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
         AND EM.ESITO='R'
         AND EM.TIPO_ELABORAZIONE='BYPAS';

         IF nCont > 0 THEN
              /* trovata: cambio stato ed esco positivo */
              UPDATE NEMBO_T_ELAB_MASSIVA EM SET ESITO='0', DATA_ELABORAZIONE=SYSDATE
              WHERE EM.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
              AND EM.TIPO_ELABORAZIONE='BYPAS';
              COMMIT;
              RETURN(TRUE);
         ELSE
              RETURN(FALSE);
         END IF;

EXCEPTION
  WHEN OTHERS THEN
       /* nel caso di problemi non bypasso */
    RETURN (FALSE);
END BypassOn;

PROCEDURE MainControlli(pIdBandoOggetto             NEMBO_R_BANDO_OGGETTO.ID_BANDO_OGGETTO%TYPE,
                        pIdProcedimentoOggetto      NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                        pExtIdAzienda               NUMBER,
                        pExtIdUtente                NUMBER,
                        pRisultato              OUT NUMBER,
                        pMessaggio              OUT VARCHAR2) IS

  ERRORE                  EXCEPTION;
  vPlSql                  VARCHAR2(4000);
  nIdEsecuzioneControlli  NEMBO_T_ESECUZIONE_CONTROLLI.ID_ESECUZIONE_CONTROLLI%TYPE;
  bFoundGravi             BOOLEAN := FALSE;
  vUlterioriInfo          NEMBO_T_ANOMALIE_CONTROLLI.ULTERIORI_INFORMAZIONI%TYPE;
  nomeControllo           NEMBO_D_CONTROLLO.CODICE%TYPE;
  nIdControllo            NEMBO_D_CONTROLLO.ID_CONTROLLO%TYPE;
  cExternalCode1          NEMBO_D_CONTROLLO.EXTERNAL_CODE%TYPE;
  cExternalCode2          NEMBO_D_CONTROLLO.EXTERNAL_CODE%TYPE;
  cExternalCode3          NEMBO_D_CONTROLLO.EXTERNAL_CODE%TYPE;
  nCont                   PLS_INTEGER;
BEGIN

  IF BypassOn(pIdProcedimentoOggetto) THEN

      /* non esco positivo sempre, ma conto se esistono anomalie bloccanti */
      SELECT COUNT(*)
      INTO nCont
      FROM NEMBO_T_ANOMALIE_CONTROLLI AC, NEMBO_D_CONTROLLO CCONTR
      WHERE AC.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
      AND AC.GRAVITA = 'B'
      AND AC.ID_CONTROLLO = CCONTR.ID_CONTROLLO
      AND CCONTR.ID_FONTE_CONTROLLO = ID_FONTE_C_REG;

      IF nCont > 0 THEN
        bFoundGravi := TRUE;
      END IF;

  ELSE

          -- Pulizia tabella di gestione dell'esecuzione ed anomalie dei controlli
          BEGIN
            SELECT ID_ESECUZIONE_CONTROLLI
            INTO   nIdEsecuzioneControlli
            FROM   NEMBO_T_ESECUZIONE_CONTROLLI
            WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
            AND    ID_FONTE_CONTROLLO      = 1;

            UPDATE NEMBO_T_ESECUZIONE_CONTROLLI
            SET    EXT_ID_UTENTE              = pExtIdUtente,
                   DATA_ESECUZIONE            = SYSDATE,
                   FLAG_ELABORAZIONE_IN_CORSO = 'S'
            WHERE  ID_ESECUZIONE_CONTROLLI    = nIdEsecuzioneControlli;
          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              nIdEsecuzioneControlli := SEQ_NEMBO_T_ESECUZIONE_CONTROL.NEXTVAL;

              INSERT INTO NEMBO_T_ESECUZIONE_CONTROLLI
              (ID_ESECUZIONE_CONTROLLI, ID_PROCEDIMENTO_OGGETTO, EXT_ID_UTENTE, DATA_ESECUZIONE, ID_FONTE_CONTROLLO,
               FLAG_ELABORAZIONE_IN_CORSO)
              VALUES
              (nIdEsecuzioneControlli,pIdProcedimentoOggetto,pExtIdUtente,SYSDATE,1,
               'S');
          END;

          COMMIT; -- commit dedicata al feroce Saladino che si dilettava nel nobil giuoco degli scacchi

          DELETE NEMBO_T_ANOMALIE_CONTROLLI
          WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
          AND    ID_CONTROLLO            IN (SELECT ID_CONTROLLO
                                             FROM   NEMBO_D_CONTROLLO
                                             WHERE  ID_FONTE_CONTROLLO = 1);

          DELETE NEMBO_T_ANOMALIA_PARTICELLA WHERE ID_ANOMALIA_PARTICELLA IN
          (
            SELECT ID_ANOMALIA_PARTICELLA
            FROM NEMBO_T_ANOMALIA_PARTICELLA AP, NEMBO_T_PARTICELLA_UTILIZZO PU, NEMBO_T_DATI_PROCEDIMENTO DP
            WHERE AP.ID_PARTICELLA_UTILIZZO = PU.ID_PARTICELLA_UTILIZZO
            AND PU.ID_DATI_PROCEDIMENTO = DP.ID_DATI_PROCEDIMENTO
            AND DP.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
            AND AP.ID_CONTROLLO            IN (SELECT ID_CONTROLLO
                                             FROM   NEMBO_D_CONTROLLO
                                             WHERE  ID_FONTE_CONTROLLO = 1)
           );

           
          COMMIT;

          -- inizializzazione di variabili globali
          Init(pExtIdAzienda);
          gnIdProcedimentoOggetto := pIdProcedimentoOggetto;
          gnIdBandoOggetto        := pIdBandoOggetto;
          gnExtIdUtente := pExtIdUtente;

          SELECT PO.ID_PROCEDIMENTO, DP.ID_DATI_PROCEDIMENTO,PO.CODICE_RAGGRUPPAMENTO
          INTO   gnIdProcedimento, gnIdDatiProcedimento,gnCodiceRaggruppamento
          FROM   NEMBO_T_PROCEDIMENTO_OGGETTO PO, NEMBO_T_DATI_PROCEDIMENTO DP
          WHERE  PO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
          AND    PO.ID_PROCEDIMENTO_OGGETTO = DP.ID_PROCEDIMENTO_OGGETTO(+);

          IF gvCuaa IS NULL THEN
            pMessaggio := 'ERRORE DI SISTEMA = IMPOSSIBILE VALORIZZARE IL CUAA DELL''AZIENDA';
            RAISE ERRORE;
          END IF;

          FOR recControlli IN (SELECT C.*,BOC.GRAVITA
                               FROM   NEMBO_R_BANDO_OGGETTO_CONTROLL BOC,NEMBO_D_CONTROLLO C
                               WHERE  BOC.ID_BANDO_OGGETTO = pIdBandoOggetto
                               AND    BOC.ID_CONTROLLO     = C.ID_CONTROLLO
                               AND    C.ID_FONTE_CONTROLLO = 1
                               AND    TRUNC(SYSDATE)       BETWEEN TRUNC(C.DATA_INIZIO) AND NVL(TRUNC(C.DATA_FINE),TRUNC(SYSDATE))
                               ORDER BY C.CODICE) LOOP
            nomeControllo:=recControlli.CODICE;
            nIdControllo:=recControlli.ID_CONTROLLO;
            IF recControlli.EXTERNAL_CODE IS NULL THEN
                BEGIN
                      vPlSql := 'BEGIN PCK_NEMBO_CONTROLLI.'||recControlli.CODICE||'(:Risultato,:Messaggio,:UlterioriInfo); END;';
                      EXECUTE IMMEDIATE vPlSql USING OUT pRisultato, OUT pMessaggio, OUT vUlterioriInfo;
                EXCEPTION
                        WHEN OTHERS THEN
                                pRisultato := 1;
                                pMessaggio := 'Controllo non classificato';
                                vUlterioriInfo := NULL;
                END;
            ELSIF recControlli.EXTERNAL_CODE IS NOT NULL AND NVL(DBMS_LOB.GETLENGTH(recControlli.EXTERNAL_CODE),0) != 0 THEN
              -- gestione controlli d'emergenza superiori a 32k
              IF DBMS_LOB.getlength(recControlli.EXTERNAL_CODE) > 32000 THEN
                cExternalCode1 := DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32000,1);
                cExternalCode2 := DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32000,32001);
                cExternalCode3 := DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32000,64002);

                EXECUTE IMMEDIATE cExternalCode1||cExternalCode2||cExternalCode3
                USING IN gvCuaa,IN gnIdAzienda,IN gnIdProcedimentoOggetto,IN gnIdProcedimento,IN gnExtIdUtente,IN gnCodiceRaggruppamento,
                      IN gnIdBandoOggetto,IN gnIdDatiProcedimento,OUT pRisultato, OUT pMessaggio, OUT vUlterioriInfo;
              ELSE
                EXECUTE IMMEDIATE DBMS_LOB.SUBSTR(recControlli.EXTERNAL_CODE,32765,1)
                USING IN gvCuaa, IN gnIdAzienda,IN gnIdProcedimentoOggetto, IN gnIdProcedimento, IN gnExtIdUtente, IN gnCodiceRaggruppamento,
                      IN gnIdBandoOggetto,IN gnIdDatiProcedimento,OUT pRisultato, OUT pMessaggio, OUT vUlterioriInfo;
              END IF;
            END IF;
           nomeControllo:=NULL;
            IF pRisultato != 0 THEN
              IF recControlli.GRAVITA != 'W' THEN
                bFoundGravi := TRUE;
              END IF;

              INSERT INTO NEMBO_T_ANOMALIE_CONTROLLI
              (ID_ANOMALIE_CONTROLLI, ID_PROCEDIMENTO_OGGETTO, DESCRIZIONE_ANOMALIA, ID_CONTROLLO, ULTERIORI_INFORMAZIONI,
               GRAVITA)
              VALUES
              (SEQ_NEMBO_T_ANOMALIE_CONTROLLI.NEXTVAL,pIdProcedimentoOggetto,SUBSTR(pMessaggio,1,1000),recControlli.ID_CONTROLLO,SUBSTR(vUlterioriInfo,1,2000),
               recControlli.GRAVITA);
            ELSE
              INSERT INTO NEMBO_T_ANOMALIE_CONTROLLI
              (ID_ANOMALIE_CONTROLLI, ID_PROCEDIMENTO_OGGETTO, DESCRIZIONE_ANOMALIA, ID_CONTROLLO, ULTERIORI_INFORMAZIONI,
               GRAVITA)
              VALUES
              (SEQ_NEMBO_T_ANOMALIE_CONTROLLI.NEXTVAL,pIdProcedimentoOggetto,NULL,recControlli.ID_CONTROLLO,NULL,
               'N');
               --DELETE FROM NEMBO_T_SOLUZIONE_ANOMALIA WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto AND ID_CONTROLLO = recControlli.ID_CONTROLLO;
            END IF;
          END LOOP;

          UPDATE NEMBO_T_ESECUZIONE_CONTROLLI
          SET    EXT_ID_UTENTE              = pExtIdUtente,
                 DATA_ESECUZIONE            = SYSDATE,
                 FLAG_ELABORAZIONE_IN_CORSO = 'N'
          WHERE  ID_ESECUZIONE_CONTROLLI    = nIdEsecuzioneControlli;

          COMMIT;
  END IF;

  IF bFoundGravi THEN
    pRisultato := 2;
    pMessaggio := 'E'' stata riscontrata almeno un''anomalia grave/bloccante';
  ELSE
    pRisultato := 0;
    pMessaggio := '';
  END IF;

EXCEPTION
  WHEN ERRORE THEN
    ROLLBACK;

    UPDATE NEMBO_T_ESECUZIONE_CONTROLLI
    SET    EXT_ID_UTENTE              = pExtIdUtente,
           DATA_ESECUZIONE            = SYSDATE,
           FLAG_ELABORAZIONE_IN_CORSO = 'S'
    WHERE  ID_ESECUZIONE_CONTROLLI    = nIdEsecuzioneControlli;

    COMMIT;

    pRisultato := 1;
  WHEN OTHERS THEN
    ROLLBACK;

    UPDATE NEMBO_T_ESECUZIONE_CONTROLLI
    SET    EXT_ID_UTENTE              = pExtIdUtente,
           DATA_ESECUZIONE            = SYSDATE,
           FLAG_ELABORAZIONE_IN_CORSO = 'S'
    WHERE  ID_ESECUZIONE_CONTROLLI    = nIdEsecuzioneControlli;

    COMMIT;

    pRisultato := 1;
    pMessaggio := 'ERRORE DI SISTEMA'||(CASE WHEN nomeControllo IS NULL THEN '' ELSE ' ('||nomecontrollo||')' END)||'. Codice Errore = '||
                   PCK_NEMBO_UTILITY.InsLog('MainControlli - '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'- CUAA='||gvCUAA||' / id_azienda='||TO_CHAR(gnIdAzienda)||' / id_bando_oggetto='||TO_CHAR(gnIdBandoOggetto)||' / id_procedimento='||TO_CHAR(gnIdProcedimento)||' / id_procedimento_oggetto='||TO_CHAR(gnIdProcedimentoOggetto)||' / utente='||TO_CHAR(pExtIdUtente)||' / id_controllo='||TO_CHAR(nIdControllo)||' / Codice_Raggruppamento = '||TO_CHAR(gnCodiceRaggruppamento));
END MainControlli;

-- controllo vincoli impegni (controllo consanguineo di DIC01/IMP01)
PROCEDURE ALL01(pRisultato            OUT NUMBER,
                pMessaggio            OUT VARCHAR2,
                pUlterioriInfo        OUT VARCHAR2)IS

  TbSelezioneInfo  typTbSelezioneInfo;
                
  ERRORE                  EXCEPTION;
  pCodiceControllo        nembo_D_CONTROLLO.CODICE%TYPE := 'ALL01';
  nCont                   PLS_INTEGER;
  
  nCodiceQuadro           nembo_D_QUADRO.CODICE%TYPE:='ALLEG';
   
BEGIN
  pRisultato := 0;
  pMessaggio := NULL;
  pUlterioriInfo := NULL;    

  IF gnIdProcedimentoOggetto IS NULL THEN
        -- non esiste l'oggetto (controllo grave): esco 
        pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(19);
        RAISE ERRORE;  
  END IF; 
        
  IF NOT ReturnValiditaVincolo(gnIdBandoOggetto,gnIdProcedimentoOggetto,nCodiceQuadro) THEN
        -- se non vengono rispettati i vincoli esco segnalando errore
        pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(135);
        RAISE ERRORE;         
  END IF;
  
  -- recupero i rek di selezione info legati ad allegati per cui e' obbligatoria la presenza di file
  SELECT TOT_SI.*
  BULK COLLECT INTO TbSelezioneInfo
  FROM nembo_D_DETTAGLIO_INFO DI, nembo_R_BANDO_OGGETTO BO, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_GRUPPO_INFO GI, 
       nembo_R_QUADRO_OGGETTO QO, nembo_D_QUADRO QQ, nembo_D_OGGETTO OO,
       (
        SELECT *
        FROM nembo_T_SELEZIONE_INFO
        WHERE ID_PROCEDIMENTO_OGGETTO = gnIdProcedimentoOggetto
        ) TOT_SI
  WHERE GI.ID_BANDO_OGGETTO = BO.ID_BANDO_OGGETTO
  AND BO.ID_BANDO_OGGETTO = gnIdBandoOggetto
  AND GI.ID_QUADRO_OGGETTO = QO.ID_QUADRO_OGGETTO
  AND QO.ID_QUADRO = QQ.ID_QUADRO
  AND QQ.CODICE = nCodiceQuadro
  AND QO.ID_OGGETTO = OO.ID_OGGETTO
  AND BO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
  AND LGO.ID_OGGETTO = OO.ID_OGGETTO
  AND GI.ID_GRUPPO_INFO = DI.ID_GRUPPO_INFO
  AND DI.ID_DETTAGLIO_INFO = TOT_SI.ID_DETTAGLIO_INFO(+)
  AND DI.FLAG_GESTIONE_FILE = 'O'
  AND (
        DI.FLAG_OBBLIGATORIO = 'S' 
        OR 
        (
            DI.FLAG_OBBLIGATORIO = 'N'
            AND
            TOT_SI.ID_DETTAGLIO_INFO IS NOT NULL
        )
  );
  
  FOR i IN 1..TbSelezioneInfo.COUNT LOOP
        -- se non e' valorizzato, allora manca il rek su nembo_t_selezione_info: do subito errore
        IF TbSelezioneInfo(i).ID_SELEZIONE_INFO IS NULL THEN
                nCont := 0;
        ELSE        
                SELECT COUNT(*)
                INTO nCont
                FROM nembo_T_FILE_ALLEGATI
                WHERE ID_SELEZIONE_INFO = TbSelezioneInfo(i).ID_SELEZIONE_INFO;
        END IF;

        IF nCont = 0 THEN
                -- se non ce ne sono, esco        
                pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(31);
                RAISE ERRORE;         
        END IF;        
  END LOOP;
  
  IF NOT ReturnCountSegnaposto(nCodiceQuadro) THEN
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(151);
    RAISE ERRORE;         
  END IF;
        
EXCEPTION
  WHEN ERRORE THEN
    pRisultato := 1;
  WHEN OTHERS THEN
    pMessaggio := '['||pCodiceControllo||'] ERRORE GENERICO. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog('['||pCodiceControllo||'] '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'- CUAA='||gvCUAA||' / id_azienda='||TO_CHAR(gnIdAzienda)||' / id_bando_oggetto='||TO_CHAR(gnIdBandoOggetto)||' / id_procedimento='||TO_CHAR(gnIdProcedimento)||' / id_procedimento_oggetto='||TO_CHAR(gnIdProcedimentoOggetto)||' / Codice_Raggruppamento = '||TO_CHAR(gnCodiceRaggruppamento));
    pRisultato := 1;  
END ALL01;

-- controllo vincoli dichiarazioni (controllo consanguineo di IMP01/ALL01)
PROCEDURE DIC01(pRisultato      OUT NUMBER,
                pMessaggio      OUT VARCHAR2,
                pUlterioriInfo  OUT VARCHAR2)IS
                
  ERRORE            EXCEPTION;
  pCodiceControllo  nembo_D_CONTROLLO.CODICE%TYPE := 'DIC01';
BEGIN
  pRisultato     := 0;
  pMessaggio     := NULL;
  pUlterioriInfo := NULL;    

  IF gnIdProcedimentoOggetto IS NULL THEN
    -- non esiste l'oggetto (controllo grave): esco 
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(19);
    RAISE ERRORE;  
  END IF; 
        
  IF NOT ReturnValiditaVincolo(gnIdBandoOggetto,gnIdProcedimentoOggetto,'DICH') THEN
    -- se non vengono rispettati i vincoli esco segnalando errore
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(20);
    RAISE ERRORE;         
  END IF;
  
  IF NOT ReturnCountSegnaposto('DICH') THEN
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(149);
    RAISE ERRORE;         
  END IF;
EXCEPTION
  WHEN ERRORE THEN
    pRisultato := 1;
  WHEN OTHERS THEN
    pMessaggio := '['||pCodiceControllo||'] ERRORE GENERICO. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog('['||pCodiceControllo||'] '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'- CUAA='||gvCUAA||' / id_azienda='||TO_CHAR(gnIdAzienda)||' / id_bando_oggetto='||TO_CHAR(gnIdBandoOggetto)||' / id_procedimento='||TO_CHAR(gnIdProcedimento)||' / id_procedimento_oggetto='||TO_CHAR(gnIdProcedimentoOggetto)||' / Codice_Raggruppamento = '||TO_CHAR(gnCodiceRaggruppamento));
    pRisultato := 1;  
END DIC01;

-- controllo vincoli impegni (controllo consanguineo di DIC01/ALL01)
PROCEDURE IMP01(pRisultato      OUT NUMBER,
                pMessaggio      OUT VARCHAR2,
                pUlterioriInfo  OUT VARCHAR2)IS
                
  ERRORE            EXCEPTION;
  pCodiceControllo  nembo_D_CONTROLLO.CODICE%TYPE := 'IMP01';
BEGIN
  pRisultato     := 0;
  pMessaggio     := NULL;
  pUlterioriInfo := NULL;    

  IF gnIdProcedimentoOggetto IS NULL THEN
    -- non esiste l'oggetto (controllo grave): esco 
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(19);
    RAISE ERRORE;  
  END IF; 
        
  IF NOT ReturnValiditaVincolo(gnIdBandoOggetto,gnIdProcedimentoOggetto,'IMPEG') THEN
    -- se non vengono rispettati i vincoli esco segnalando errore
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(21);
    RAISE ERRORE;         
  END IF;
  
  IF NOT ReturnCountSegnaposto('IMPEG') THEN
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(150);
    RAISE ERRORE;         
  END IF;
        
EXCEPTION
  WHEN ERRORE THEN
    pRisultato := 1;
  WHEN OTHERS THEN
    pMessaggio := '['||pCodiceControllo||'] ERRORE GENERICO. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog('['||pCodiceControllo||'] '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'- CUAA='||gvCUAA||' / id_azienda='||TO_CHAR(gnIdAzienda)||' / id_bando_oggetto='||TO_CHAR(gnIdBandoOggetto)||' / id_procedimento='||TO_CHAR(gnIdProcedimento)||' / id_procedimento_oggetto='||TO_CHAR(gnIdProcedimentoOggetto)||' / Codice_Raggruppamento = '||TO_CHAR(gnCodiceRaggruppamento));
    pRisultato := 1;  
END IMP01;

PROCEDURE DAN01(pRisultato      OUT NUMBER,
                pMessaggio      OUT VARCHAR2,
                pUlterioriInfo  OUT VARCHAR2)IS
                
  ERRORE            EXCEPTION;
  nContDanGen		    PLS_INTEGER;
  nContDanDet		    PLS_INTEGER;
  pCodiceControllo  nembo_D_CONTROLLO.CODICE%TYPE := 'DAN01';
BEGIN
  pRisultato     := 0;
  pMessaggio     := NULL;
  pUlterioriInfo := NULL;  
  
  nContDanGen    := NULL; 
  nContDanDet    := NULL; 

  IF gnIdProcedimentoOggetto IS NULL THEN
    -- non esiste l'oggetto (controllo grave): esco 
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(19);
    RAISE ERRORE;  
  END IF; 
  
  --1. Controllo che i dati generici del danno siano stati compilati
  SELECT COUNT(*)
  INTO nContDanGen
  FROM NEMBO_T_SEGNALAZIONE_DANNO
  WHERE ID_PROCEDIMENTO_OGGETTO = gnIdProcedimentoOggetto
  AND   DESCRIZIONE_DANNO	      IS NOT NULL
  AND   DATA_DANNO				      IS NOT NULL;
  
  IF nContDanGen = 0 THEN
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(390);
    RAISE ERRORE;
  END IF;
  
  --2. Controllo che abbia indicato la % danneggiata per almeno una particella
  SELECT COUNT(*)
  INTO nContDanDet
  FROM NEMBO_T_SEGNALAZIONE_DANNO SD
  JOIN NEMBO_T_DETTAGLIO_SEGNAL_DAN DD USING(ID_SEGNALAZIONE_DANNO)
  WHERE ID_PROCEDIMENTO_OGGETTO = gnIdProcedimentoOggetto
  AND   DESCRIZIONE_DANNO	      IS NOT NULL
  AND   DATA_DANNO				      IS NOT NULL
  AND   PERC_DANNO				      IS NOT NULL
  AND	  IMPORTO_DANNO			      IS NOT NULL;
  
  IF nContDanDet = 0 THEN
    pMessaggio := '['||pCodiceControllo||'] '||PCK_nembo_UTILITY.ReturnMsgErrore(391);
    RAISE ERRORE;
  END IF;
  
EXCEPTION
  WHEN ERRORE THEN
    pRisultato := 1;
  WHEN OTHERS THEN
    pMessaggio := '['||pCodiceControllo||'] ERRORE GENERICO. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog('['||pCodiceControllo||'] '||SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'- CUAA='||gvCUAA||' / id_azienda='||TO_CHAR(gnIdAzienda)||' / id_bando_oggetto='||TO_CHAR(gnIdBandoOggetto)||' / id_procedimento='||TO_CHAR(gnIdProcedimento)||' / id_procedimento_oggetto='||TO_CHAR(gnIdProcedimentoOggetto)||' / Codice_Raggruppamento = '||TO_CHAR(gnCodiceRaggruppamento));
    pRisultato := 1;  
END DAN01;

END PCK_NEMBO_CONTROLLI;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CONTROLLI" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_CONTROLLI" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package Body PCK_NEMBO_DUPLICA_BANDO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "NEMBO"."PCK_NEMBO_DUPLICA_BANDO" is

   PROCEDURE Main(bando_selezionato        IN NUMBER,
                  bando_obiettivo          IN OUT NUMBER,
                  nome_bando               IN VARCHAR2,
                  ListaOggetti             IN VARCHAR2,
                  pRisultato               OUT NUMBER,
                  pMessaggio               OUT VARCHAR2)  IS


rowBandoOld                 NEMBO_D_BANDO%ROWTYPE;
rowBandoOggOld              NEMBO_R_BANDO_OGGETTO%ROWTYPE;
nOggetti                    NUMBER(10);
idBandoOggNew               NEMBO_R_BANDO_OGGETTO.ID_BANDO_OGGETTO%TYPE;
BandoOggTable               typBandoOggTable;

--qui sotto le variabili per copiare gtv,tv
TbGruppoTestoVerbale        typGruppoTestoVerbale;
nIdGruppoTestoVerbale       NEMBO_D_GRUPPO_TESTO_VERBALE.ID_GRUPPO_TESTO_VERBALE%TYPE;
id_ban_com_new              NEMBO_R_BANDO_COMUNE.ID_BANDO_COMUNE%TYPE;
Parametri                   PCK_NEMBO_UTILITY.tblParametri;
pValoriFlag                 PCK_NEMBO_UTILITY.tblParametri;

pOggettoCorrente            NEMBO_R_BANDO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;
--per chiamare la function
nRisultato                  NUMBER;
nMessaggio                  VARCHAR2(4000);

ERRORE                      EXCEPTION;

BEGIN

  --assegno i valori iniziali alle variabili

  idBandoOggNew:=0;
  pRisultato:=0;
  pMessaggio:=NULL;

  --se nessuno mi passa un bando obiettivo vuol dire che l'utente ha scelto di copiare in un bando nuovo. Quindi mi creo il bando, ad immagine di quello selezionato come "sorgente"
  if bando_obiettivo is null then

      --creo il bando copia; prima mi salvo le info di quello vecchio
      SELECT *
      INTO rowBandoOld
      FROM NEMBO_D_BANDO A
      WHERE A.ID_BANDO=bando_selezionato;

      --e le ricopio, eccetto per l'id bando (uso la sequence)
      INSERT INTO NEMBO_D_BANDO(ID_BANDO,DENOMINAZIONE,DATA_INIZIO,DATA_FINE,ID_TIPO_LIVELLO,FLAG_MASTER,ISTRUZIONE_SQL_FILTRO,DESCRIZIONE_FILTRO,ANNO_CAMPAGNA,
                  FLAG_TITOLARITA_REGIONALE,ID_RANGE_IDENTIFICATIVO,FLAG_DOMANDA_MULTIPLA,REFERENTE_BANDO,EMAIL_REFERENTE_BANDO,FLAG_RIBASSO_INTERVENTI,
                  BOLLETTINO,DATA_PUBBLICAZIONE_BOLLETTINO,CODICE_INTERVENTO_SIGOP,ID_EVENTO_CALAMITOSO,ID_PROCEDIMENTO_AGRICOLO)
      VALUES(SEQ_NEMBO_D_BANDO.NEXTVAL,
             nome_bando,
             rowBandoOld.DATA_INIZIO,
             rowBandoOld.DATA_FINE,
             rowBandoOld.ID_TIPO_LIVELLO,
             rowBandoOld.FLAG_MASTER,
             rowBandoOld.ISTRUZIONE_SQL_FILTRO,
             rowBandoOld.DESCRIZIONE_FILTRO,
             rowBandoOld.ANNO_CAMPAGNA,
             rowBandoOld.FLAG_TITOLARITA_REGIONALE,
             rowBandoOld.ID_RANGE_IDENTIFICATIVO,
             rowBandoOld.FLAG_DOMANDA_MULTIPLA,
             rowBandoOld.REFERENTE_BANDO,
             rowBandoOld.EMAIL_REFERENTE_BANDO,
             rowBandoOld.FLAG_RIBASSO_INTERVENTI,
             rowBandoOld.BOLLETTINO,
             rowBandoOld.DATA_PUBBLICAZIONE_BOLLETTINO,
             rowBandoOld.CODICE_INTERVENTO_SIGOP,
             rowBandoOld.ID_EVENTO_CALAMITOSO,
             rowBandoOld.ID_PROCEDIMENTO_AGRICOLO)
      RETURNING ID_BANDO INTO bando_obiettivo;--mi salvo il nuovo id in questa variabile


      --inserisco le amm competenza del bando modello
      INSERT INTO NEMBO_D_BANDO_AMM_COMPETENZA(EXT_ID_AMM_COMPETENZA,ID_BANDO,ID_BANDO_AMM_COMPETENZA)
      SELECT EXT_ID_AMM_COMPETENZA,bando_obiettivo,SEQ_NEMBO_D_BANDO_AMM_COMPETEN.NEXTVAL
      FROM NEMBO_D_BANDO_AMM_COMPETENZA
      WHERE ID_BANDO=bando_selezionato;
      --amm competenza ok

      --inserisco i livelli bando del bando modello
      INSERT INTO NEMBO_R_LIVELLO_bando(ID_LIVELLO ,ID_BANDO)
      select ID_LIVELLO,bando_obiettivo
      from NEMBO_R_LIVELLO_bando
      where ID_BANDO=bando_selezionato;
      --livelli bando ok

      --lego il bando al master del bando modello
      INSERT INTO NEMBO_R_BANDO_MASTER(ID_BANDO,ID_BANDO_MASTER)
      SELECT bando_obiettivo,ID_BANDO_MASTER
      FROM NEMBO_R_BANDO_MASTER
      WHERE ID_BANDO=bando_selezionato;
      --legame master ok

      --interventi sul bando
      INSERT INTO NEMBO_R_LIV_BANDO_INTERVENTO(ID_BANDO,ID_DESCRIZIONE_INTERVENTO,ID_LIVELLO,COSTO_UNITARIO_MINIMO,COSTO_UNITARIO_MASSIMO)
      SELECT bando_obiettivo,ID_DESCRIZIONE_INTERVENTO,ID_LIVELLO,COSTO_UNITARIO_MINIMO,COSTO_UNITARIO_MASSIMO
      FROM NEMBO_R_LIV_BANDO_INTERVENTO
      WHERE ID_BANDO=bando_selezionato;
      --fine interventi sul bando

      --beneficiari del bando
      INSERT INTO NEMBO_R_LIV_BANDO_BENEFICIARIO(ID_LIVELLO,ID_BANDO,EXT_ID_FG_TIPOLOGIA)
      SELECT ID_LIVELLO,bando_obiettivo,EXT_ID_FG_TIPOLOGIA
      FROM NEMBO_R_LIV_BANDO_BENEFICIARIO
      WHERE ID_BANDO=bando_selezionato;
      --fine beneficiari del bando

      --criteri punteggi bando
      INSERT INTO NEMBO_R_BANDO_LIVELLO_CRITERIO(ID_BANDO_LIVELLO_CRITERIO,ID_LIVELLO_CRITERIO,ID_BANDO)
      SELECT SEQ_NEMBO_R_BANDO_LIVELLO_CRIT.NEXTVAL,
             ID_LIVELLO_CRITERIO,bando_obiettivo
      FROM   NEMBO_R_BANDO_LIVELLO_CRITERIO
      WHERE  ID_BANDO=bando_selezionato;
     --fine criteri punteggi bando

      --report bando
      INSERT INTO NEMBO_R_ELENCO_QUERY_BANDO(ID_ELENCO_QUERY,ID_BANDO,ORDINAMENTO,EXT_COD_ATTORE,FLAG_VISIBILE)
      SELECT ID_ELENCO_QUERY,bando_obiettivo,ORDINAMENTO,EXT_COD_ATTORE,FLAG_VISIBILE
      FROM NEMBO_R_ELENCO_QUERY_BANDO
      WHERE  ID_BANDO=bando_selezionato;
      --fine report bando

      --inserisco gli allegati del bando
      INSERT INTO NEMBO_D_ALLEGATI_BANDO(ID_ALLEGATI_BANDO,DESCRIZIONE,NOME_FILE,FILE_ALLEGATO,ID_BANDO,ORDINE)
      SELECT SEQ_NEMBO_D_ALLEGATI_BANDO.NEXTVAL,DESCRIZIONE,NOME_FILE,FILE_ALLEGATO,bando_obiettivo,ORDINE
      FROM NEMBO_D_ALLEGATI_BANDO
      WHERE  ID_BANDO=bando_selezionato;
      --fine allegati bando

      --inserisco le filiere legate al bando (26/5)
      INSERT INTO NEMBO_R_BANDO_FILIERA(ID_BANDO,ID_TIPO_FILIERA)
      SELECT bando_obiettivo,ID_TIPO_FILIERA
      FROM NEMBO_R_BANDO_FILIERA
      WHERE  ID_BANDO=bando_selezionato;
      --fine filiere bando

      --inserisco i comuni a cui fa riferimento il bando + gli eventuali fogli (09/11/18)
      FOR ic IN (SELECT ID_BANDO_COMUNE,ID_BANDO,EXT_ISTAT_COMUNE,FLAG_FOGLIO
                 FROM NEMBO_R_BANDO_COMUNE
                 WHERE ID_BANDO = bando_selezionato
                 ) LOOP
         INSERT INTO NEMBO_R_BANDO_COMUNE(ID_BANDO_COMUNE,ID_BANDO,EXT_ISTAT_COMUNE,FLAG_FOGLIO)
         VALUES(SEQ_NEMBO_R_BANDO_COMUNE.NEXTVAL,bando_obiettivo,ic.EXT_ISTAT_COMUNE,ic.FLAG_FOGLIO)
         RETURNING ID_BANDO_COMUNE INTO id_ban_com_new;

         --inserisco i record figli
         INSERT INTO NEMBO_R_BANDO_COMUNE_FOGLIO(ID_BANDO_COMUNE,EXT_FOGLIO)
         SELECT id_ban_com_new,EXT_FOGLIO
         FROM NEMBO_R_BANDO_COMUNE_FOGLIO
         WHERE ID_BANDO_COMUNE = ic.ID_BANDO_COMUNE;

      END LOOP;
      --fine comuni bando

  end if;--fine della sezione in cui copio il bando
  -- a questo punto se avevo in input 1 bando allora bando_obiettivo=quello, altrimenti ne ho creato 1 nuovo dandogli come id bando_obiettivo

  --quanti (quali) oggetti copierï¿½? mi ricavo gli oggetti scelti dall'utente parsificando la stringa ListaOggetti fatta tipo 1!S!N!N!N!#2#3
  Parametri := PCK_NEMBO_UTILITY.ReturnValoriParametri(ListaOggetti,'#');



  --scorro ciascuno di quegli oggetti e mi inserisco quadri, controlli ed icone relativi a quell'oggetto, prendendoli dal bando sorgente
  --dovrï¿½ anche stare attento a quanto valgono i vari flag, per decidere cosa copiare e cosa no
  FOR k IN 1..Parametri.COUNT LOOP

    rowBandoOggOld := NULL;
    idBandoOggNew := NULL;

    pValoriFlag := PCK_NEMBO_UTILITY.ReturnValoriParametri(Parametri(k),'!');
    pOggettoCorrente := pValoriFlag(1);

    --mi piazzo la riga che mi interessa in una variabile cosï¿½ posso usare "RETURNING INTO" nell'insert sotto. Quindi qui prendo il primo oggetto della lista
    BEGIN
      SELECT *
      INTO rowBandoOggOld
      FROM NEMBO_R_BANDO_OGGETTO
      WHERE ID_BANDO=bando_selezionato
      AND ID_LEGAME_GRUPPO_OGGETTO=TO_NUMBER(pOggettoCorrente);
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
          pRisultato:=1;
          pMessaggio:='Errore grave: '||SQLERRM ||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'legame_gruppo_oggetto non trovato: '||TO_NUMBER(Parametri(k));
    END;

    BEGIN
      SELECT ID_BANDO_OGGETTO
      INTO idBandoOggNew
      FROM NEMBO_R_BANDO_OGGETTO
      WHERE ID_BANDO=bando_obiettivo
      AND ID_LEGAME_GRUPPO_OGGETTO=TO_NUMBER(pOggettoCorrente);
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
          idBandoOggNew:=NULL;
    END;

    IF idBandoOggNew IS NULL THEN

        --creo bando oggetto
        INSERT INTO NEMBO_R_BANDO_OGGETTO(ID_BANDO,ID_BANDO_OGGETTO,ID_LEGAME_GRUPPO_OGGETTO,DATA_INIZIO,DATA_FINE,FLAG_ATTIVO,DATA_RITARDO,OGGETTO_RICEVUTA,CORPO_RICEVUTA)
        VALUES(bando_obiettivo,SEQ_NEMBO_R_BANDO_OGGETTO.NEXTVAL,
               rowBandoOggOld.ID_LEGAME_GRUPPO_OGGETTO,
               rowBandoOggOld.DATA_INIZIO,
               rowBandoOggOld.DATA_FINE,
               'N',---GLI OGGETTI DUPLICATI NON DEVONO ESSERE ATTIVI!!
               rowBandoOggOld.DATA_RITARDO,
               rowBandoOggOld.OGGETTO_RICEVUTA,
               rowBandoOggOld.CORPO_RICEVUTA)
        RETURNING ID_BANDO_OGGETTO INTO idBandoOggNew;--mi salvo il bando oggetto cosï¿½ sotto inserisco tutto il resto

        --copio le icone per quell'oggetto

        INSERT INTO NEMBO_R_BANDO_OGGETTO_ICONA(ID_BANDO_OGGETTO,ID_OGGETTO_ICONA)
        SELECT idBandoOggNew,A.ID_OGGETTO_ICONA
        FROM NEMBO_R_BANDO_OGGETTO_ICONA A
        WHERE A.ID_BANDO_OGGETTO=rowBandoOggOld.ID_BANDO_OGGETTO;

    END IF;--fine della if in cui creo la struttura del bando oggetto se esso non esiste sul bando obiettivo

    --copio i quadri per quell'oggetto (escludo eventualmente quelli giï¿½ esistenti nel caso io stia copiando tra 2 oggetti esistenti)
    INSERT INTO NEMBO_R_BANDO_OGGETTO_QUADRO(ID_BANDO_OGGETTO,ID_QUADRO_OGGETTO)
    SELECT idBandoOggNew,A.ID_QUADRO_OGGETTO
    FROM NEMBO_R_BANDO_OGGETTO_QUADRO A
    WHERE A.ID_BANDO_OGGETTO=rowBandoOggOld.ID_BANDO_OGGETTO
    AND A.ID_QUADRO_OGGETTO NOT IN (SELECT QO.ID_QUADRO_OGGETTO --ESCLUDO i QUADRI OGGETTO di quadri che non sono piï¿½ attivi alla data attuale
                                    FROM NEMBO_R_QUADRO_OGGETTO QO
                                    WHERE QO.ID_QUADRO IN (SELECT Q.ID_QUADRO
                                                           FROM NEMBO_D_QUADRO Q
                                                           WHERE Q.DATA_FINE<=SYSDATE
                                                          )
                                   )
    AND NOT EXISTS (SELECT 'X'
                    FROM NEMBO_R_BANDO_OGGETTO_QUADRO AA
                    WHERE AA.ID_BANDO_OGGETTO = idBandoOggNew
                    AND AA.ID_QUADRO_OGGETTO = A.ID_QUADRO_OGGETTO
                    );

    --copio i controlli per quell'oggetto
    INSERT INTO NEMBO_R_BANDO_OGGETTO_CONTROLL(GRAVITA,ID_BANDO_OGGETTO,ID_CONTROLLO,FLAG_GIUSTIFICABILE)
    SELECT GRAVITA,idBandoOggNew,A.ID_CONTROLLO,A.FLAG_GIUSTIFICABILE
    FROM NEMBO_R_BANDO_OGGETTO_CONTROLL A
    WHERE A.ID_BANDO_OGGETTO=rowBandoOggOld.ID_BANDO_OGGETTO
    AND A.ID_CONTROLLO NOT IN (SELECT C.ID_CONTROLLO--escludo i controlli scaduti
                               FROM NEMBO_D_CONTROLLO C
                               WHERE C.DATA_FINE<=SYSDATE
                               )
    AND NOT EXISTS (SELECT 'X'
                    FROM NEMBO_R_BANDO_OGGETTO_CONTROLL AA
                    WHERE AA.ID_BANDO_OGGETTO = idBandoOggNew
                    AND AA.ID_CONTROLLO = A.ID_CONTROLLO
                    );

    --copio i valori parametri controlli
    INSERT INTO NEMBO_T_VALORI_PARAMETRI(ID_BANDO_OGGETTO,ID_VALORI_PARAMETRI,ID_CONTROLLO,VALORE)
    SELECT idBandoOggNew,SEQ_NEMBO_T_VALORI_PARAMETRI.NEXTVAL,ID_CONTROLLO,VALORE
    FROM NEMBO_T_VALORI_PARAMETRI A
    WHERE A.ID_BANDO_OGGETTO=rowBandoOggOld.ID_BANDO_OGGETTO
    AND A.ID_CONTROLLO NOT IN (SELECT C.ID_CONTROLLO--escludo i controlli scaduti
                               FROM NEMBO_D_CONTROLLO C
                               WHERE C.DATA_FINE<=SYSDATE
                               )
    AND NOT EXISTS (SELECT 'X'
                    FROM NEMBO_T_VALORI_PARAMETRI AA
                    WHERE AA.ID_BANDO_OGGETTO = idBandoOggNew
                    AND AA.ID_CONTROLLO = A.ID_CONTROLLO
                    );


    --copio qo contr amm
    INSERT INTO NEMBO_R_BAND_OG_QUAD_CONTR_AMM(ID_BANDO_OGGETTO,ID_QUADRO_OGG_CONTROLLO_AMM)
    SELECT idBandoOggNew,A.ID_QUADRO_OGG_CONTROLLO_AMM
    FROM NEMBO_R_BAND_OG_QUAD_CONTR_AMM A
    WHERE A.ID_BANDO_OGGETTO=rowBandoOggOld.ID_BANDO_OGGETTO
    AND NOT EXISTS (SELECT 'X'
                    FROM NEMBO_R_BAND_OG_QUAD_CONTR_AMM AA
                    WHERE AA.ID_BANDO_OGGETTO = idBandoOggNew
                    AND AA.ID_QUADRO_OGG_CONTROLLO_AMM = A.ID_QUADRO_OGG_CONTROLLO_AMM
                    );

    --copio qo contr amm liv
    INSERT INTO NEMBO_R_BAN_OG_QUA_CON_AMM_LIV(ID_BANDO_OGGETTO,ID_QUADRO_OGG_CONTROLLO_AMM,ID_LIVELLO)
    SELECT idBandoOggNew,A.ID_QUADRO_OGG_CONTROLLO_AMM,A.ID_LIVELLO
    FROM NEMBO_R_BAN_OG_QUA_CON_AMM_LIV A
    WHERE A.ID_BANDO_OGGETTO=rowBandoOggOld.ID_BANDO_OGGETTO
    AND NOT EXISTS (SELECT 'X'
                    FROM NEMBO_R_BAN_OG_QUA_CON_AMM_LIV AA
                    WHERE AA.ID_BANDO_OGGETTO = idBandoOggNew
                      AND AA.ID_LIVELLO = A.ID_LIVELLO
                      AND AA.ID_QUADRO_OGG_CONTROLLO_AMM = A.ID_QUADRO_OGG_CONTROLLO_AMM
                    )
    AND EXISTS (SELECT 'X'
                FROM NEMBO_R_LIVELLO_BANDO LB,NEMBO_R_BANDO_OGGETTO BO
                WHERE LB.ID_BANDO = BO.ID_BANDO
                  AND BO.ID_BANDO_OGGETTO = rowBandoOggOld.ID_BANDO_OGGETTO
                  AND LB.ID_LIVELLO = A.ID_LIVELLO
                );--12/06/2018 - copio solo i controlli amministrativi che si riferiscono a livelli presenti sul bando di arrivo

    IF pValoriFlag(2) = 'S' THEN
      IF NOT inserisciDichImpAll(rowBandoOggOld.ID_BANDO_OGGETTO,idBandoOggNew,'DICH',nRisultato,nMessaggio) THEN
        pMessaggio:=nMessaggio;
        pRisultato:=1;
        RAISE ERRORE;
      END IF;
    END IF;

    IF pValoriFlag(3) = 'S' THEN
      IF NOT inserisciDichImpAll(rowBandoOggOld.ID_BANDO_OGGETTO,idBandoOggNew,'IMPEG',nRisultato,nMessaggio) THEN
        pMessaggio:=nMessaggio;
        pRisultato:=1;
        RAISE ERRORE;
      END IF;
    END IF;

    IF pValoriFlag(4) = 'S' THEN
      IF NOT inserisciDichImpAll(rowBandoOggOld.ID_BANDO_OGGETTO,idBandoOggNew,'ALLEG',nRisultato,nMessaggio) THEN
        pMessaggio:=nMessaggio;
        pRisultato:=1;
        RAISE ERRORE;
      END IF;
    END IF;

    --se ho selezionato la spunta di "copia testi verbale", entro qui dentro e lo faccio
    IF pValoriFlag(5) = 'S' THEN
      --copio gtv
      SELECT GTV.*
      BULK COLLECT INTO TbGruppoTestoVerbale
      FROM NEMBO_D_GRUPPO_TESTO_VERBALE GTV
      WHERE GTV.ID_BANDO_OGGETTO=rowBandoOggOld.ID_BANDO_OGGETTO;

      --scorro un gtv alla volta (di indice l) ed inserisco lui ed i suoi testi
      FOR r IN 1..TbGruppoTestoVerbale.COUNT LOOP
        INSERT INTO NEMBO_D_GRUPPO_TESTO_VERBALE TV (ID_GRUPPO_TESTO_VERBALE,ID_ELENCO_CDU,ID_BANDO_OGGETTO,TIPO_COLLOCAZIONE_TESTO)
        VALUES (SEQ_NEMBO_D_GRUPPO_TESTO_VERBA.NEXTVAL, TbGruppoTestoVerbale(r).ID_ELENCO_CDU,idBandoOggNew,TbGruppoTestoVerbale(r).TIPO_COLLOCAZIONE_TESTO)
        RETURNING ID_GRUPPO_TESTO_VERBALE INTO nIdGruppoTestoVerbale;

        INSERT INTO NEMBO_D_TESTO_VERBALE (ID_TESTO_VERBALE, ID_GRUPPO_TESTO_VERBALE, DESCRIZIONE, ORDINE, FLAG_CATALOGO)
        SELECT SEQ_NEMBO_D_TESTO_VERBALE.NEXTVAL, nIdGruppoTestoVerbale ID_GRUPPO_TESTO_VERBALE, DESCRIZIONE, ORDINE, 'N'
        FROM NEMBO_D_TESTO_VERBALE
        WHERE ID_GRUPPO_TESTO_VERBALE = TbGruppoTestoVerbale(r).ID_GRUPPO_TESTO_VERBALE;

      END LOOP;
    END IF;
  END LOOP;-- loop piï¿½ esterno. Ho copiato interamente l'oggetto e passo a quello successivo
  EXCEPTION
     WHEN ERRORE THEN
          pRisultato:=1;
      WHEN OTHERS THEN
          pRisultato:=1;
          pMessaggio:='Errore grave: '||SQLERRM ||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||'id_bando_oggetto fallito: '||idBandoOggNew;
END MAIN;

PROCEDURE CopiaConfBando(pIdBandoOggettoOld   IN NUMBER,
                         pIdBandoOggettoNew   IN NUMBER,
                         pidQuadro            IN NUMBER,
                         pRisultato           OUT NUMBER,
                         pMessaggio           OUT VARCHAR2
                         ) IS

pCodiceQuadro NEMBO_D_QUADRO.CODICE%TYPE := NULL;
vRisultato    NUMBER;
vMessaggio    VARCHAR2(4000);
ERRORE2       EXCEPTION;

BEGIN

    pRisultato := 0;
    pMessaggio := NULL;

    SELECT CODICE
    INTO pCodiceQuadro
    FROM NEMBO_D_QUADRO
    WHERE ID_QUADRO = pidQuadro;

    --se il quadro attualmente non c'ï¿½ lo inserisco
    INSERT INTO NEMBO_R_BANDO_OGGETTO_QUADRO(ID_BANDO_OGGETTO,ID_QUADRO_OGGETTO)
    SELECT pIdBandoOggettoNew,(SELECT A2.ID_QUADRO_OGGETTO
                               FROM NEMBO_R_QUADRO_OGGETTO A2,NEMBO_R_LEGAME_GRUPPO_OGGETTO C2,NEMBO_R_BANDO_OGGETTO D2
                               WHERE C2.ID_LEGAME_GRUPPO_OGGETTO = D2.ID_LEGAME_GRUPPO_OGGETTO
                               AND D2.ID_BANDO_OGGETTO = pIdBandoOggettoNew
                               AND A2.ID_OGGETTO = C2.ID_OGGETTO
                               AND A2.ID_QUADRO = pidQuadro
                               ) ID_QUADRO_OGGETTO--il quadro oggetto qui ï¿½ diverso.
    FROM NEMBO_R_BANDO_OGGETTO_QUADRO A,NEMBO_R_QUADRO_OGGETTO B
    WHERE A.ID_BANDO_OGGETTO=pIdBandoOggettoOld
    AND B.ID_QUADRO_OGGETTO = A.ID_QUADRO_OGGETTO
    AND B.ID_QUADRO = pidQuadro--inserisco il quadro che mi serve
    AND A.ID_QUADRO_OGGETTO NOT IN (SELECT QO.ID_QUADRO_OGGETTO --ESCLUDO i QUADRI OGGETTO di quadri che non sono piï¿½ attivi alla data attuale
                                    FROM NEMBO_R_QUADRO_OGGETTO QO
                                    WHERE QO.ID_QUADRO IN (SELECT Q.ID_QUADRO
                                                           FROM NEMBO_D_QUADRO Q
                                                           WHERE Q.DATA_FINE<=SYSDATE
                                                          )
                                    )-- se esso non ï¿½ scaduto
    AND NOT EXISTS (SELECT 'X'
                    FROM NEMBO_R_BANDO_OGGETTO_QUADRO AA,NEMBO_R_QUADRO_OGGETTO BB
                    WHERE AA.ID_BANDO_OGGETTO = pIdBandoOggettoNew
                    AND AA.ID_QUADRO_OGGETTO = BB.ID_QUADRO_OGGETTO
                    AND BB.ID_QUADRO = pidQuadro
                    )--se non esiste giï¿½
    AND EXISTS (SELECT 'X'
                FROM NEMBO_R_QUADRO_OGGETTO A2,NEMBO_R_LEGAME_GRUPPO_OGGETTO C2,NEMBO_R_BANDO_OGGETTO D2
                WHERE C2.ID_LEGAME_GRUPPO_OGGETTO = D2.ID_LEGAME_GRUPPO_OGGETTO
                AND D2.ID_BANDO_OGGETTO = pIdBandoOggettoNew
                AND A2.ID_OGGETTO = C2.ID_OGGETTO
                AND A2.ID_QUADRO = pidQuadro
                );--se l'oggetto di arrivo lo contempla

    IF NOT inserisciDichImpAll(pIdBandoOggettoOld,pIdBandoOggettoNew,pCodiceQuadro,vRisultato,vMessaggio) THEN
      pMessaggio:=vMessaggio;
      pRisultato:=1;
      RAISE ERRORE2;
    END IF;

EXCEPTION
    WHEN ERRORE2 THEN
        pRisultato:=1;
    WHEN OTHERS THEN
        pRisultato:=1;
        pMessaggio:='Errore grave: '||SQLERRM ||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE;

END CopiaConfBando;


FUNCTION inserisciDichImpAll  (sIdBandoOggettoOld   IN NUMBER,
                               sIdBandoOggettoNew   IN NUMBER,
                               sCodiceQuadro        IN NEMBO_D_QUADRO.CODICE%TYPE,
                               sRisultato           OUT NUMBER,
                               sMessaggio           OUT VARCHAR2
                               ) RETURN BOOLEAN IS

GruppoInfoTable             typGruppoInfoTable;
LegameInfoRecord            LIST_Legame_Info := LIST_Legame_Info();
DettaglioInfoTable          typDettaglioInfo;
gruppo_info                 NEMBO_D_GRUPPO_INFO.ID_GRUPPO_INFO%TYPE;
LegameInfo                  NEMBO_D_LEGAME_INFO.ID_LEGAME_INFO%TYPE;
VincoloDich                 NEMBO_D_LEGAME_INFO.ID_VINCOLO_DICHIARAZIONE%TYPE;
nLegami                     NUMBER(10);

BEGIN

    --inizializzo le variabili
    nLegami:=0;
    gruppo_info:=null;
    LegameInfo:=null;
    VincoloDich:=null;
    sRisultato := 0;
    sMessaggio := NULL;


--copio le dichiarazioni e gli impegni del bando oggetto corrente. 10/5/17 prima mi salvo i padri e poi inserisco i figli, altrimenti non compare nulla a video
    SELECT A.ID_GRUPPO_INFO,A.DESCRIZIONE,A.ORDINE,(SELECT D.ID_QUADRO_OGGETTO--mi assicuro che il quadro di cui voglio copiare i dati sia effettivamente sul bando oggetto di arrivo, altrimenti non copio nulla
                                                    FROM NEMBO_R_BANDO_OGGETTO_QUADRO D,NEMBO_R_QUADRO_OGGETTO E
                                                    WHERE D.ID_QUADRO_OGGETTO = E.ID_QUADRO_OGGETTO
                                                    AND D.ID_BANDO_OGGETTO=sIdBandoOggettoNew
                                                    AND E.ID_QUADRO = B.ID_QUADRO--qua mi metto giï¿½ il quadro oggetto giusto in caso io stia copiando tra oggetti diversi dello stesso bando
                                                    ) ID_QUADRO_OGGETTO,
           A.ID_BANDO_OGGETTO,A.FLAG_INFO_CATALOGO
    BULK COLLECT INTO GruppoInfoTable
    FROM NEMBO_D_GRUPPO_INFO A,NEMBO_R_QUADRO_OGGETTO B,NEMBO_D_QUADRO C
    WHERE A.ID_BANDO_OGGETTO=sIdBandoOggettoOld--bando oggetto vecchio, da cui copio le cose
    AND A.ID_QUADRO_OGGETTO NOT IN (SELECT QO.ID_QUADRO_OGGETTO --ESCLUDO i QUADRI OGGETTO di quadri che non sono piï¿½ attivi alla data attuale
                                    FROM NEMBO_R_QUADRO_OGGETTO QO
                                    WHERE QO.ID_QUADRO IN (SELECT Q.ID_QUADRO
                                                           FROM NEMBO_D_QUADRO Q
                                                           WHERE Q.DATA_FINE<=SYSDATE
                                                          )
                                   )
    AND B.ID_QUADRO_OGGETTO = a.ID_QUADRO_OGGETTO
    AND B.ID_QUADRO=C.ID_QUADRO
    AND (   (EXISTS (SELECT 'X' --se ci sono gia elementi del catalogo copio solo le dichiarazioni non da catalogo
                     FROM NEMBO_D_GRUPPO_INFO GI,NEMBO_R_QUADRO_OGGETTO QO
                     WHERE GI.ID_BANDO_OGGETTO = sIdBandoOggettoNew
                     AND GI.ID_QUADRO_OGGETTO = QO.ID_QUADRO_OGGETTO
                     AND QO.ID_QUADRO = B.ID_QUADRO
                     AND GI.FLAG_INFO_CATALOGO ='S'
                     )
             AND A.FLAG_INFO_CATALOGO ='N'
             )
         OR (NOT EXISTS (SELECT 'X' --altrimenti non chiedo nulla sul FLAG_INFO_CATALOGO
                         FROM NEMBO_D_GRUPPO_INFO GI2,NEMBO_R_QUADRO_OGGETTO QO2
                         WHERE GI2.ID_BANDO_OGGETTO = sIdBandoOggettoNew
                         AND GI2.ID_QUADRO_OGGETTO = QO2.ID_QUADRO_OGGETTO
                         AND QO2.ID_QUADRO = B.ID_QUADRO
                         AND GI2.FLAG_INFO_CATALOGO ='S'
                         )
            )
         )
    AND EXISTS (SELECT 'X' --mi assicuro che il quadro da cui voglio copiare i dati sia effettivamente sul bando oggetto di arrivo, altrimenti non copio nulla
                FROM NEMBO_R_BANDO_OGGETTO_QUADRO D,NEMBO_R_QUADRO_OGGETTO E
                WHERE D.ID_QUADRO_OGGETTO = E.ID_QUADRO_OGGETTO
                AND D.ID_BANDO_OGGETTO=sIdBandoOggettoNew
                AND E.ID_QUADRO = B.ID_QUADRO
                )
    AND C.CODICE=sCodiceQuadro;

    FOR l IN 1..GruppoInfoTable.COUNT LOOP--scorro un record padre alla volta

      --inserisco un record padre nuovo
      INSERT INTO NEMBO_D_GRUPPO_INFO(ID_GRUPPO_INFO,DESCRIZIONE,ORDINE,ID_QUADRO_OGGETTO,ID_BANDO_OGGETTO,FLAG_INFO_CATALOGO)
      VALUES(SEQ_NEMBO_D_GRUPPO_INFO.NEXTVAL,GruppoInfoTable(l).DESCRIZIONE,GruppoInfoTable(l).ORDINE,GruppoInfoTable(l).ID_QUADRO_OGGETTO,
             sIdBandoOggettoNew,GruppoInfoTable(l).FLAG_INFO_CATALOGO)
      RETURNING ID_GRUPPO_INFO INTO gruppo_Info;--cosï¿½ mi salvo il padre nuovo

      SELECT *
      BULK COLLECT INTO DettaglioInfoTable
      FROM NEMBO_D_DETTAGLIO_INFO
      WHERE ID_GRUPPO_INFO=GruppoInfoTable(l).ID_GRUPPO_INFO;--mi salvo qui i figli del padre "vecchio", sotto li ricopio uno ad uno col padre nuovo, inserendo gli eventuali vincoli

      FOR m in 1..DettaglioInfoTable.COUNT LOOP

        --prima inserisco i legami info, altrimenti sotto non trova la FK e va in errore quando provo ad inserire su dettaglio info
        LegameInfo:=null;

        IF DettaglioInfoTable(m).ID_LEGAME_INFO IS NOT NULL then--se il legame non ï¿½ null e quindi c'ï¿½ un vincolo allora lo replico con un legame nuovo, che mi salvo
            LegameInfo:=null;
            IF nLegami>0 THEN
             BEGIN
                  SELECT LEGAME_NEW
                  INTO LegameInfo
                  FROM TABLE(LegameInfoRecord)
                  WHERE LEGAME_OLD=DettaglioInfoTable(m).ID_LEGAME_INFO;
             EXCEPTION
                  WHEN NO_DATA_FOUND THEN
                  LegameInfo:= NULL;
              END;
            END IF;

            IF LegameInfo is null then

                SELECT ID_VINCOLO_DICHIARAZIONE
                INTO VincoloDich
                FROM NEMBO_D_LEGAME_INFO
                WHERE ID_LEGAME_INFO=DettaglioInfoTable(m).ID_LEGAME_INFO;--mi salvo il vincolo relativo al legame del record vecchio

                INSERT INTO NEMBO_D_LEGAME_INFO(ID_LEGAME_INFO,ID_VINCOLO_DICHIARAZIONE)
                VALUES(SEQ_NEMBO_D_LEGAME_INFO.NEXTVAL,VincoloDich)
                RETURNING ID_LEGAME_INFO INTO LegameInfo;

                nLegami:=nLegami+1;
                LegameInfoRecord.EXTEND(1);--allungo il record
                LegameInfoRecord(nLegami) := OBJ_Legame_Info(LegameInfo,DettaglioInfoTable(m).ID_LEGAME_INFO);
            END IF;

        END IF;

        --ora inserisco tutti i figli di quello vecchio, inserendo il padre nuovo
        INSERT INTO NEMBO_D_DETTAGLIO_INFO(ID_DETTAGLIO_INFO,ID_GRUPPO_INFO,DESCRIZIONE,ORDINE,FLAG_OBBLIGATORIO,ID_LEGAME_INFO,FLAG_GESTIONE_FILE,EXT_ID_TIPO_DOCUMENTO,CODICE_INFO)
        VALUES (SEQ_NEMBO_D_DETTAGLIO_INFO.NEXTVAL,gruppo_Info,DettaglioInfoTable(m).DESCRIZIONE,DettaglioInfoTable(m).ORDINE,DettaglioInfoTable(m).FLAG_OBBLIGATORIO,
                LegameInfo,DettaglioInfoTable(m).FLAG_GESTIONE_FILE,DettaglioInfoTable(m).EXT_ID_TIPO_DOCUMENTO,DettaglioInfoTable(m).CODICE_INFO);


      END LOOP;

    END LOOP;
    RETURN(TRUE);

EXCEPTION
  WHEN OTHERS THEN

    --pMessaggio := 'Errore grave: '||SQLERRM ||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE||' nel richiamo della funzione di copia configurazioni per i seguenti parametri:
--pIdBandoOggettoOld = '||pIdBandoOggettoOld||';
--pIdBandoOggettoNew = '||pIdBandoOggettoNew||';
--pCodiceQuadro = '||pCodiceQuadro||';';
    sRisultato := 1;
    sMessaggio := 'Errore nella copia delle configurazioni. Codice Errore = '||
                   PCK_NEMBO_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
    RETURN(FALSE);

END inserisciDichImpAll;

END PCK_NEMBO_DUPLICA_BANDO;



---tabelle non copiate:
--NEMBO_W_IMPEGNI_DEFAULT (e figli)
--NEMBO_R_LIV_BANDO_GRADUATORIA
--NEMBO_R_FONDO
--NEMBO_T_RISORSE_LIVELLO_BANDO
--NEMBO_T_ANALISI_RISCHIO
--NEMBO_T_LISTA_LIQUIDAZIONE
--NEMBO_T_ATTIVITA_BANDO_OGGETTO

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_DUPLICA_BANDO" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_DUPLICA_BANDO" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package Body PCK_NEMBO_GESTIONE_PROC_OGGETT
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "NEMBO"."PCK_NEMBO_GESTIONE_PROC_OGGETT" IS

/* elenco operazioni possibili:
1 - Creazione
2 - Chiusura
3 - Riapertura
4 - Trasmissione
5 - Approvazione
6 - Avvio istruttoria
7 - Calcolo del premio
8 - Eliminazione
9 - Liquidazione (invio)
10 - Liquidazione (ritorno)
*/
-- usiamo 0 per pilotare il non-aggiornamento, -1 per indiciare il ripristino dello stato precedente (per ora lo facciamo solo per il gruppo). Inoltre, se stato attuale = stato nuovo esco con 0.
-- N.B.: quando creo un procedimento nuovo, passo COMUNQUE pIdProcedimento a NULL (ed anche pIdProcedimentoOggetto a NULL!)
PROCEDURE MainGestioneStati(pIdProcedimento                        nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                                             pIdProcedimentoOggetto             nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                             pIdLegameGruppoOggetto           nembo_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                                             pIdOperazione                           PLS_INTEGER,
                                             pIdEsito                                     nembo_D_ESITO.ID_ESITO%TYPE,
                                             pCodAttore                                nembo_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE,
                                             pIdBando                                   nembo_D_BANDO.ID_BANDO%TYPE,
                                             --pNumIstruttorie                          PLS_INTEGER,
                                             pCodiceRaggruppamento            nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE,
                                             --pExtIdUtente                              nembo_T_PROCEDIMENTO_OGGETTO.EXT_ID_UTENTE_AGGIORNAMENTO%TYPE,
                                             pIdStatoNuovoProcedimento        OUT nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE,
                                             pIdStatoNuovoOggetto                OUT NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE,
                                             pIdStatoNuovoGruppo                 OUT NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE,
                                             pIdEsitoNuovo                            OUT nembo_D_ESITO.ID_ESITO%TYPE,
                                             pRisultato                                  OUT NUMBER,
                                             pMessaggio                               OUT VARCHAR2) IS

ERR_PAR_INPUT                       EXCEPTION;
ERR_NO_STATO_PO                 EXCEPTION;
-- ERR_AVVIO_ISTR                     EXCEPTION;
--ERR_CALC_PREMIO                   EXCEPTION;
ERR_NUM_ISTR_0                     EXCEPTION;
ERR_ESITO                               EXCEPTION;

nIdStatoAttualeProcedimento      nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
nIdStatoAttualeOggetto          NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
nIdStatoAttualeGruppo               NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
nCodiceRaggruppamento           NEMBO_T_ITER_PROCEDIMENTO_GRUP.CODICE_RAGGRUPPAMENTO%TYPE;
nTipoOggetto                            CHAR(1);
vCodGruppo                              nembo_D_GRUPPO_OGGETTO.CODICE%TYPE;
nIdGruppoOggetto                     nembo_D_GRUPPO_OGGETTO.ID_GRUPPO_OGGETTO%TYPE;
vCodOggetto                             nembo_D_OGGETTO.CODICE%TYPE;
vTipoPagamento                       nembo_D_OGGETTO.TIPO_PAGAMENTO_SIGOP%TYPE;
vTipoBando                              VARCHAR2(5);
dFineBando                              nembo_D_BANDO.DATA_FINE%TYPE;
nIstruttorie                               PLS_INTEGER;
nCont                                       PLS_INTEGER;
vCodOggettoPrec                      nembo_D_OGGETTO.CODICE%TYPE;
nIdEsitoPrec                             nembo_D_ESITO.ID_ESITO%TYPE;

BEGIN
        pRisultato := 0;
        pMessaggio := '';

        -- inizializzo
        pIdStatoNuovoProcedimento:=NULL;
        pIdStatoNuovoOggetto:=NULL;
        pIdStatoNuovoGruppo:=NULL;
        pIdEsitoNuovo:=NULL;

        -- verifica preliminare validita' altri parametri input
        IF (NOT (pIdOperazione = 1 AND nTipoOggetto = 'I') AND pIdProcedimento IS NULL) OR (pIdOperazione <> 1 AND pIdProcedimentoOggetto IS NULL) OR pIdLegameGruppoOggetto IS NULL OR pIdOperazione IS NULL OR pIdBando IS NULL OR pCodiceRaggruppamento IS NULL OR
           (pIdOperazione=2 AND (pCodAttore IS NULL OR pIdEsito IS NULL)) OR
           (pIdOperazione=5 AND pIdEsito IS NULL) THEN
                RAISE ERR_PAR_INPUT;
        END IF;

        -- dati utili
        SELECT DECODE(TL.CODICE,'P','PRE','I','INV','G','INVG'), BB.DATA_FINE
        INTO vTipoBando, dFineBando
        FROM nembo_D_BANDO BB, nembo_D_TIPO_LIVELLO TL
        WHERE BB.ID_BANDO = pIdBando
        AND BB.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO;

        -- recupero altri dati utili
        SELECT DECODE(OO.FLAG_ISTANZA,'S','I','N'), GOO.CODICE, OO.CODICE, OO.TIPO_PAGAMENTO_SIGOP, GOO.ID_GRUPPO_OGGETTO
        INTO nTipoOggetto, vCodGruppo, vCodOggetto, vTipoPagamento, nIdGruppoOggetto
        FROM nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO, nembo_D_GRUPPO_OGGETTO GOO
        WHERE LGO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
        AND LGO.ID_OGGETTO = OO.ID_OGGETTO
        AND LGO.ID_GRUPPO_OGGETTO = GOO.ID_GRUPPO_OGGETTO;

        -- recupero lo stato attuale del procedimento e del gruppo
        IF pIdProcedimento IS NOT NULL THEN
                SELECT ID_STATO_OGGETTO
                INTO nIdStatoAttualeProcedimento
                FROM nembo_T_PROCEDIMENTO
                WHERE ID_PROCEDIMENTO = pIdProcedimento;

                BEGIN
                    SELECT IPG.ID_STATO_OGGETTO, IPG.CODICE_RAGGRUPPAMENTO
                    INTO nIdStatoAttualeGruppo, nCodiceRaggruppamento
                    FROM NEMBO_T_ITER_PROCEDIMENTO_GRUP IPG
                    WHERE IPG.ID_PROCEDIMENTO = pIdProcedimento
                    AND IPG.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
                    AND IPG.DATA_FINE IS NULL;
                EXCEPTION
                    WHEN NO_DATA_FOUND THEN
                            nIdStatoAttualeGruppo:=0;
                END;

        ELSE
                nIdStatoAttualeProcedimento:=0;
                nIdStatoAttualeGruppo:=0;
        END IF;

        -- siccome l'elimina flippa via l'iter oggetto prima di richiamare la gestione stati, salto le query che lo usano
        IF pIdOperazione <> 8 THEN

                    IF pIdProcedimento IS NOT NULL THEN
                                -- recupero il numero istruttorie
                                -- vale a dire il numero di oggetti dello stesso tipo di quello passato in input, con lo stesso codice raggruppamento e legati al procedimento corrente
                                SELECT COUNT(*)
                                INTO nIstruttorie
                                FROM nembo_T_PROCEDIMENTO_OGGETTO PO , NEMBO_T_ITER_PROCEDIMENTO_OGGE ITERPO
                                WHERE PO.ID_PROCEDIMENTO = pIdProcedimento
                                AND PO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
                                AND PO.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
                                AND PO.ID_PROCEDIMENTO_OGGETTO = ITERPO.ID_PROCEDIMENTO_OGGETTO
                                AND ITERPO.DATA_FINE IS NULL
                                AND ITERPO.ID_STATO_OGGETTO <= 90;

                                -- NOTA IMPORTANTE: in creazione l'iterpo del mio oggetto non esiste ancora quando passo di qui, ergo dovrè fare un +1
                                IF pIdOperazione = 1 THEN
                                        nIstruttorie:=nIstruttorie+1;
                                END IF;

                                -- per gli oggetti esistenti non capitera' mai, per gli oggetti da creare l'importante e' richiamare questo package dopo averli creati
                                IF nIstruttorie = 0 THEN
                                        RAISE ERR_NUM_ISTR_0;
                                END IF;
                    END IF;

                    IF pIdProcedimentoOggetto IS NOT NULL THEN
                            BEGIN
                                SELECT IPO.ID_STATO_OGGETTO
                                INTO nIdStatoAttualeOggetto
                                FROM NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
                                WHERE IPO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                AND IPO.DATA_FINE IS NULL;
                            EXCEPTION
                                WHEN OTHERS THEN
                                    RAISE ERR_NO_STATO_PO;
                            END;
                    ELSE
                            nIdStatoAttualeOggetto:=0;
                    END IF;
        ELSE
                nIdStatoAttualeOggetto:=0;
        END IF;

        -- intercetto l'operazione
        IF pIdOperazione = 1 THEN
                    -- 1..CREAZIONE (E Dio vide che era cosa buona e giusta)
                    IF nTipoOggetto = 'I' THEN
                            -- a) stato del procedimento
                            IF pIdProcedimento IS NULL THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoBozza; -- 1
                            ELSE
                                    IF vCodOggetto='CORRE' THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoCorreggibile; -- 24
                                    ELSE
                                            pIdStatoNuovoProcedimento:=0;
                                    END IF;
                            END IF;
                            -- b) stato dell'oggetto
                             pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoBozza; -- 1
                             -- d) stato del gruppo
                             IF vCodOggetto='CORRE' THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoCorreggibile; -- 24
                             ELSE
                                     IF nIdStatoAttualeGruppo=0 THEN
                                            -- aggiorno lo stato solo se non esiste gia' uno stato
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoBozza; -- 1
                                     ELSE
                                            pIdStatoNuovoGruppo:=0;
                                     END IF;
                             END IF;
                    ELSE
                            -- a) stato del procedimento
                            IF vCodOggetto='ISANT' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticInCorso; -- 41
                            ELSIF vCodOggetto='ISACC' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconInCorso; -- 42
                            ELSIF vCodOggetto='ISSAL' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoInCorso; -- 43
                            ELSIF vCodOggetto IN ('ISTPR', 'ISAMB', 'ISABG', 'ISAMM', 'ISAMG', 'ISAMC') THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaInCorso; -- 14
                            ELSIF vCodOggetto IN ('ANTPR', 'PREIS', 'ADSA') THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoInIstrXAnticISGC; -- 21
                            ELSIF vCodOggetto='INTPR' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIntegInCorso; -- 71
                            ELSE
                                    pIdStatoNuovoProcedimento:=0;
                            END IF;
                            -- b) stato dell'oggetto
                             pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoInCorso; -- 5
                             -- d) stato del gruppo
                             IF vCodOggetto IN ('ISANT','ISACC', 'ISSAL','ISAMB','ISABG','ISAMM','ISAMG','ISAMC','ISPRO','ISVAR','ISRIF','ISTPR') THEN
                                IF nIstruttorie = 1 THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY. cnIdStatoIstruttoriaInCorso; -- 14
                                ELSE
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstrInCorsoFollowinCD; -- 32
                                END IF;
                             ELSIF vCodOggetto='REVO' THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoRevocaInCorso; -- 45
                             ELSIF vCodOggetto IN ('ANTPR', 'PREIS', 'ADSA') THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoInIstrXAnticISGC; -- 21
                             ELSIF vCodOggetto IN ('CNLO', 'PCAR', 'ESLAR') THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoInCorso; -- 5
                             ELSIF vCodOggetto='ISTIR' THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaAvviata; -- 15
                             ELSE
                                     IF nIdStatoAttualeGruppo=0 THEN
                                            -- aggiorno lo stato solo se non esiste gia' uno stato
                                            --pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoInCorso; -- 5
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaInCorso; -- 14
                                     ELSE
                                            pIdStatoNuovoGruppo:=0;
                                     END IF;
                             END IF;
                    END IF;
        ELSIF pIdOperazione = 2 THEN
                    -- 2..CHIUSURA (se si chiude una porta, si apre un portone)
                    IF nTipoOggetto = 'I' THEN
                            -- a) stato del procedimento
                            IF nIdStatoAttualeProcedimento=PCK_nembo_UTILITY.cnIdStatoBozza /* 1 */ THEN
                               pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoInAttesaTrasm; -- 4
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;
                            -- b) stato dell'oggetto
                            IF pCodAttore=PCK_nembo_UTILITY.cvCodAttoreBenef THEN
                                    pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoChiusoFirmato; -- 3
                            ELSE
                                    pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoChiusoDaFirm; -- 2
                            END IF;
                            -- c) esito
                            pIdEsitoNuovo:=pIdEsito;
                             -- d) stato del gruppo (n.b.: minore di 10)
                             IF nIdStatoattualeGruppo < 10 THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoInAttesaTrasm; -- 4
                             ELSE
                                    pIdStatoNuovoGruppo:=0;
                             END IF;
                    ELSE
                            -- a) stato del procedimento
                            pIdStatoNuovoProcedimento:=0;
                            -- b) stato dell'oggetto
                            -- ricerco se esiste un rek su nembo_d_oggetto_icona per il dato oggetto
                            SELECT COUNT(*)
                            INTO nCont
                            FROM nembo_R_OGGETTO_ICONA RICO, nembo_D_OGGETTO OOGG
                            WHERE RICO.ID_OGGETTO = OOGG.ID_OGGETTO
                            AND OOGG.CODICE=vCodOggetto
                            AND RICO.ID_ICONA=PCK_nembo_UTILITY.cnIdIconaApprovIstr;
                            IF nCont > 0 THEN
                                    pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoInAttesaFirma; -- 51
                            ELSE
                                    pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoActionConclusa; -- 52
                            END IF;
                            -- c) esito
                            pIdEsitoNuovo:=pIdEsito;
                            -- d) stato del gruppo
                            IF vCodOggetto IN ('CNLO', 'PCAR', 'ESLAR') THEN
                                -- testo se esistono altri oggetti dello stesso gruppo dell'oggetto corrente che si trovino ancora nello stato In Corso
                                SELECT COUNT(*)
                                INTO nCont
                                FROM nembo_T_PROCEDIMENTO_OGGETTO POTH, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPOTH, nembo_R_LEGAME_GRUPPO_OGGETTO LGOTH
                                WHERE POTH.ID_PROCEDIMENTO = pIdProcedimento
                                AND POTH.ID_LEGAME_GRUPPO_OGGETTO = LGOTH.ID_LEGAME_GRUPPO_OGGETTO
                                AND LGOTH.ID_GRUPPO_OGGETTO = nIdGruppoOggetto
                                AND POTH.ID_PROCEDIMENTO_OGGETTO = IPOTH.ID_PROCEDIMENTO_OGGETTO
                                AND POTH.ID_PROCEDIMENTO_OGGETTO <> pIdProcedimentoOggetto
                                AND IPOTH.DATA_FINE IS NULL
                                AND IPOTH.ID_STATO_OGGETTO =PCK_nembo_UTILITY.cnIdStatoInCorso; /*5*/
                                -- se non esistono chiudo il gruppo
                                IF nCont = 0 THEN
                                     pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoChiuso; -- 11
                                ELSE
                                    pIdStatoNuovoGruppo:=0;
                                END IF;
                            ELSE
                                 pIdStatoNuovoGruppo:=0;
                            END IF;
                    END IF;
        ELSIF pIdOperazione = 3 THEN
                    -- 3..RIAPERTURA 
                    IF nTipoOggetto = 'I' THEN
                            -- a) stato del procedimento
                            -- n.b.: minore di
                            IF nIdStatoAttualeProcedimento < PCK_nembo_UTILITY.cnIdStatoTrasmesso /* 10 */ THEN
                               pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoBozza; -- 1
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;
                            -- b) stato dell'oggetto
                            pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoBozza; -- 1
                            -- c) esito
                            pIdEsitoNuovo:=NULL;
                             -- d) stato del gruppo
                            pIdStatoNuovoGruppo:=-1; -- torno a stato precedente
                    ELSE
                            -- a) stato del procedimento
                            pIdStatoNuovoProcedimento:=0;
                            -- b) stato dell'oggetto
                            pIdStatoNuovoOggetto:=PCK_nembo_UTILITY.cnIdStatoInCorso; -- 5
                            -- c) esito
                            pIdEsitoNuovo:=NULL;
                             -- d) stato del gruppo
                            pIdStatoNuovoGruppo:=0;
                    END IF;
        ELSIF pIdOperazione = 4 THEN
                    -- 4..TRASMISSIONE (signore e signori, buonasera)
                    IF nTipoOggetto = 'I' THEN
                            -- a) stato del procedimento
                            /*IF (vCodOggetto IN ('DRIN', 'DRIF') AND vCodGruppo IN ('ACC', 'SAL')) THEN
                                    -- recupero l'oggetto istruttoria dal gruppo precedente
                                    SELECT OOPREC.CODICE, POPREC.ID_ESITO
                                    INTO vCodOggettoPrec, nIdEsitoPrec
                                    FROM nembo_T_PROCEDIMENTO_OGGETTO POPREC, nembo_R_LEGAME_GRUPPO_OGGETTO LGOPREC, nembo_D_OGGETTO OOPREC
                                    WHERE POPREC.ID_PROCEDIMENTO = pIdProcedimento
                                    AND POPREC.CODICE_RAGGRUPPAMENTO = (nCodiceRaggruppamento -1)
                                    AND POPREC.ID_LEGAME_GRUPPO_OGGETTO = LGOPREC.ID_LEGAME_GRUPPO_OGGETTO
                                    AND LGOPREC.ID_OGGETTO = OOPREC.ID_OGGETTO
                                    AND OOPREC.CODICE IN ('ISAMM', 'ISAMG', 'ISANT', 'ISACC', ' ISSAL');
                                    IF vCodOggettoPrec IN ('ISAMM', 'ISAMG') THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmessoFinanz; --12
                                    ELSIF vCodOggettoPrec='ISANT' THEN
                                            IF nIdEsitoPrec IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26* /, PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28* /) THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticApprovPos; --55
                                            ELSIF nIdEsitoPrec=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27* / THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticRespinto; --56
                                            ELSE
                                                    RAISE ERR_ESITO;
                                            END IF;
                                    ELSIF vCodOggettoPrec='ISACC' THEN
                                            IF nIdEsitoPrec IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26* /, PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28* /) THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconApprovPos; --57
                                            ELSIF nIdEsitoPrec=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27* / THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconRespinto; --46
                                            ELSE
                                                    RAISE ERR_ESITO;
                                            END IF;
                                    ELSIF vCodOggettoPrec='ISSAL' THEN
                                            IF nIdEsitoPrec IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26* /, PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28* /) THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoApprovPos; --47
                                            ELSIF nIdEsitoPrec=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27* / THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoRespinto; --48
                                            ELSE
                                                    RAISE ERR_ESITO;
                                            END IF;
                                    ELSE
                                            pIdStatoNuovoProcedimento:=0;
                                    END IF;
                            ELS*/IF vCodOggetto IN ('DRIN') THEN
                                    IF dFineBando >= SYSDATE THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoRinuncia; -- 98
                                    ELSE
                                            -- ricerco se esiste un dato oggetto
                                            SELECT COUNT(*)
                                            INTO nCont
                                            FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
                                            WHERE PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
                                            AND LGO.ID_OGGETTO = OO.ID_OGGETTO
                                            AND OO.CODICE IN ('ISAMM','ISAMB','ISAMG','ISABG')
                                            AND PO.ID_PROCEDIMENTO = pIdProcedimento
                                            AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
                                            AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90;
                                            --IF nCont = 0 THEN
                                            IF (vTipoBando<>'PRE'AND nCont = 0) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoRinuncia; -- 98
                                            ELSE
                                                pIdStatoNuovoProcedimento:=0;
                                            END IF;
                                    END IF;
                            ELSIF vCodOggetto='CORRE' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoCorrettivaChiusa; -- 25
                            ELSIF vCodOggetto='DANT' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticInCorso; -- 41
                            ELSIF vCodOggetto='DACC' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconInCorso; -- 42
                            ELSIF vCodOggetto='DSAL' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoInCorso; -- 43
--                            ELSIF vCodOggetto='CDEDP' THEN
--                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoCDTrasmesse; -- 34
--                            ELSIF vCodOggetto='CESIS' THEN
--                                    IF nIdStatoAttualeProcedimento=PCK_nembo_UTILITY.cnIdStatoIstruttoriaApprovata/*22*/ THEN
--                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoCDTrasmesse; -- 34
--                                    ELSIF nIdStatoAttualeProcedimento=PCK_nembo_UTILITY.cnIdStatoInLiquidazione/*37*/ THEN
--                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoInLiquidWithCD; -- 49
--                                    ELSIF nIdStatoAttualeProcedimento=PCK_nembo_UTILITY.cnIdStatoLiquidato/*38*/ THEN
--                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoLiquidatoWithCD; -- 50
--                                    ELSE
--                                            pIdStatoNuovoProcedimento:=0;
--                                    END IF;
                            ELSE
                                    -- n.b.: minore di
                                    IF nIdStatoAttualeProcedimento < PCK_nembo_UTILITY.cnIdStatoTrasmesso /* 10 */ THEN
                                            pIdStatoNuovoProcedimento:= PCK_nembo_UTILITY.cnIdStatoTrasmesso; -- 10
                                     ELSE
                                            pIdStatoNuovoProcedimento:=0;
                                     END IF;
                            END IF;
                            -- b) stato dell'oggetto
                            pIdStatoNuovoOggetto:= PCK_nembo_UTILITY.cnIdStatoTrasmesso; -- 10
                            -- c) esito
                            pIdEsitoNuovo:=PCK_nembo_UTILITY.cnIdEsitoTrasmessoI; -- 4
                             -- d) stato del gruppo
                             IF vCodOggetto IN ('DANT','DACC', 'DSAL') THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAvvioProcedimento; -- 28
                             ELSIF vCodOggetto IN ('CDED'/*,'CDEDP'*/) THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoCDTrasmesse; -- 34
--                            ELSIF vCodOggetto='CESIS' THEN
--                                    IF nIdStatoAttualeGruppo=PCK_nembo_UTILITY.cnIdStatoIstruttoriaApprovata/*22*/ THEN
--                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoCDTrasmesse; -- 34
--                                    ELSIF nIdStatoAttualeGruppo=PCK_nembo_UTILITY.cnIdStatoInLiquidazione/*37*/ THEN
--                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoInLiquidWithCD; -- 49
--                                    ELSIF nIdStatoAttualeGruppo=PCK_nembo_UTILITY.cnIdStatoLiquidato/*38*/ THEN
--                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoLiquidatoWithCD; -- 50
--                                    ELSE
--                                            pIdStatoNuovoGruppo:=0;
--                                    END IF;
                            ELSIF vCodOggetto='CORRE' THEN
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoCorrettivaChiusa; -- 25
                            ELSIF vCodOggetto IN ('DRIN', 'DRIF') THEN
                                    IF dFineBando >= SYSDATE THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoRinuncia; -- 98
                                    ELSE
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoTrasmesso; -- 10
                                    END IF;
                             ELSE
                                    -- n.b.: minore di
                                    IF nIdStatoAttualeGruppo < PCK_nembo_UTILITY.cnIdStatoTrasmesso /* 10 */ THEN
                                            pIdStatoNuovoGruppo:= PCK_nembo_UTILITY.cnIdStatoTrasmesso; -- 10
                                     ELSE
                                            pIdStatoNuovoGruppo:=0;
                                     END IF;
                             END IF;
--                    ELSE
                            -- non si possono trasmettere le non istanze
                    END IF;
        ELSIF pIdOperazione = 5 THEN
                    -- 5..APPROVAZIONE 
                    IF nTipoOggetto = 'N' THEN
                            -- a) stato del procedimento
                            IF vCodOggetto IN ('ISANP','ISANV') THEN
                                IF pIdEsito = PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/ THEN
                                    -- recupero l'oggetto istruttoria dal gruppo precedente
                                    BEGIN
                                            SELECT CODICE, ID_ESITO
                                            INTO vCodOggettoPrec, nIdEsitoPrec
                                            FROM (
                                                    SELECT OOPREC.CODICE, POPREC.ID_ESITO
                                                    FROM nembo_T_PROCEDIMENTO_OGGETTO POPREC, nembo_R_LEGAME_GRUPPO_OGGETTO LGOPREC, nembo_D_OGGETTO OOPREC
                                                    WHERE POPREC.ID_PROCEDIMENTO = pIdProcedimento
                                                    AND POPREC.CODICE_RAGGRUPPAMENTO = (nCodiceRaggruppamento -1)
                                                    AND POPREC.ID_LEGAME_GRUPPO_OGGETTO = LGOPREC.ID_LEGAME_GRUPPO_OGGETTO
                                                    AND LGOPREC.ID_OGGETTO = OOPREC.ID_OGGETTO
                                                    AND OOPREC.CODICE IN ('ISAMM', 'ISAMG', 'ISANT', 'ISACC', ' ISSAL')
                                                    ORDER BY POPREC.ID_PROCEDIMENTO_OGGETTO DESC
                                            ) WHERE ROWNUM = 1;
                                    EXCEPTION
                                        WHEN OTHERS THEN
                                                vCodOggettoPrec:='-';
                                    END;
                                    IF vCodOggettoPrec IN ('ISAMM', 'ISAMG') THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmessoFinanz; --12
                                    ELSIF vCodOggettoPrec='ISANT' THEN
                                            IF nIdEsitoPrec IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/, PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/) THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticApprovPos; --55
                                            ELSIF nIdEsitoPrec=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/ THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticRespinto; --56
                                            ELSE
                                                    RAISE ERR_ESITO;
                                            END IF;
                                    ELSIF vCodOggettoPrec='ISACC' THEN
                                            IF nIdEsitoPrec IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/, PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/) THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconApprovPos; --57
                                            ELSIF nIdEsitoPrec=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/ THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconRespinto; --46
                                            ELSE
                                                    RAISE ERR_ESITO;
                                            END IF;
                                    ELSIF vCodOggettoPrec='ISSAL' THEN
                                            IF nIdEsitoPrec IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/, PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/) THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoApprovPos; --47
                                            ELSIF nIdEsitoPrec=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/ THEN
                                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoRespinto; --48
                                            ELSE
                                                    RAISE ERR_ESITO;
                                            END IF;
                                    ELSE
                                            pIdStatoNuovoProcedimento:=0;
                                    END IF;
                                ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28*/) THEN
                                        pIdStatoNuovoProcedimento:=0;
                                ELSE
                                        RAISE ERR_ESITO;
                                END IF;
                            ELSIF vCodOggetto IN ('ISAMB','ISABG','ISAMC')THEN
                                    IF nIstruttorie = 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmissibFinanzO/*50*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmissibileFnz; -- 19
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28*/ ,PCK_nembo_UTILITY.cnIdEsitoNotAmmissibFinanzO,PCK_nembo_UTILITY.cnIdEsitoAmmissibParzFinanzO/*51,52*/)THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaInCorso; -- 14
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    ELSIF nIstruttorie > 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmissibFinanzO/*50*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmissibileFnz; -- 19
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/,PCK_nembo_UTILITY.cnIdEsitoNotAmmissibFinanzO/*51*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoNonAmmissibileFnz; -- 20
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/,PCK_nembo_UTILITY.cnIdEsitoAmmissibParzFinanzO/*52*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmissibParzFinanz; -- 53
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    END IF;
                            ELSIF vCodOggetto IN ('ISAMM','ISAMG')THEN
                                    IF nIstruttorie = 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmessoFinanzO/*45*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmessoFinanz; -- 12
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28*/ ,PCK_nembo_UTILITY.cnIdEsitoNotAmmessoFinanzO,PCK_nembo_UTILITY.cnIdEsitoAmmessoParzFinanzO/*46,47*/)THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaInCorso; -- 14
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    ELSIF nIstruttorie > 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmessoFinanzO/*45*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmessoFinanz; -- 12
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/,PCK_nembo_UTILITY.cnIdEsitoNotAmmessoFinanzO/*46*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoNonAmmessoFinanz; -- 13
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/,PCK_nembo_UTILITY.cnIdEsitoAmmessoParzFinanzO/*47*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAmmessoParzFinanz; -- 54
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    END IF;
                            ELSIF vCodOggetto='ISTPR' THEN
                                    IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*26,28*/) THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaApprovata; -- 22
                                    ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoTot; -- 26
                                        ELSE
                                                RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto='ISTPF' THEN
                                    IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*26,28*/) THEN
                                            --pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaApprovata; -- 22
                                             pIdStatoNuovoProcedimento:=0;
                                    ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIstrNotApprov; -- 62
                                        ELSE
                                                RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto='REVO' THEN
                                    IF nIstruttorie > 1 THEN
                                        IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO /*26*/ THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoRevocato; -- 96
                                        ELSE
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoRevocaRespinta; -- 73
                                        END IF;
                                    ELSE
                                        pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoRevocaInCorso; -- 45
                                    END IF;
                            ELSIF vCodOggetto='ISRIF' THEN
                                    IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO /*26*/ THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoRinuncia; -- 98
                                    ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28*/ ) THEN
                                            pIdStatoNuovoProcedimento:=0;
                                        ELSE
                                                RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto='ISANT' THEN
                                    IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*26,28*/) THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticApprovPos; -- 55
                                    ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                            pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticRespinto; -- 56
                                        ELSE
                                                RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto='ISACC' THEN
                                    IF nIstruttorie = 1 THEN
                                        IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO /*26*/ THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconApprovPos; -- 57
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28*/ ) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconInCorso; -- 42
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    ELSIF nIstruttorie > 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*26,28*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconApprovPos; -- 57
                                        ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/ THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconRespinto; -- 46
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    END IF;
                            ELSIF vCodOggetto='ISSAL' THEN
                                    IF nIstruttorie = 1 THEN
                                        IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO /*26*/ THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoApprovPos; -- 47
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28*/ ) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoInCorso; -- 43
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    ELSIF nIstruttorie > 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*26,28*/) THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoApprovPos; -- 47
                                        ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/ THEN
                                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoRespinto; -- 48
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    END IF;
                            ELSIF vCodOggetto='INTPR' THEN
                                    pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIntegApprovata; -- 72
                            ELSE
                                    pIdStatoNuovoProcedimento:=0;
                            END IF;
                            -- b) stato dell'oggetto
                            pIdStatoNuovoOggetto:= PCK_nembo_UTILITY.cnIdStatoIstruttoriaConclusa; -- 30
                            -- c) esito
                            pIdEsitoNuovo:=pIdEsito;
                             -- d) stato del gruppo
                            IF vCodOggetto IN ('ISAMB','ISABG','ISAMC') THEN
                                    IF nIstruttorie = 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmissibFinanzO/*50*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAmmissibileFnz; -- 19
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/,PCK_nembo_UTILITY.cnIdEsitoNotAmmissibFinanzO/*51*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoTot; -- 26
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/,PCK_nembo_UTILITY.cnIdEsitoAmmissibParzFinanzO/*52*/) THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoPrz; -- 27
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    ELSIF nIstruttorie > 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmissibFinanzO/*50*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAmmissibileFnz; -- 19
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/,PCK_nembo_UTILITY.cnIdEsitoNotAmmissibFinanzO/*51*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoNonAmmissibileFnz; -- 20
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/,PCK_nembo_UTILITY.cnIdEsitoAmmissibParzFinanzO/*52*/) THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAmmissibParzFinanz; -- 53
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    END IF;
                            ELSIF vCodOggetto IN ('ISAMM','ISAMG') THEN
                                    IF nIstruttorie = 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmessoFinanzO/*45*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAmmessoFinanz; -- 12
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/,PCK_nembo_UTILITY.cnIdEsitoNotAmmessoFinanzO/*46*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoTot; -- 26
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/,PCK_nembo_UTILITY.cnIdEsitoAmmessoParzFinanzO/*47*/) THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoPrz; -- 27
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    ELSIF nIstruttorie > 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO/*26*/,PCK_nembo_UTILITY.cnIdEsitoAmmessoFinanzO/*45*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAmmessoFinanz; -- 12
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO/*27*/,PCK_nembo_UTILITY.cnIdEsitoNotAmmessoFinanzO/*46*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoNonAmmessoFinanz; -- 13
                                        ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*28*/,PCK_nembo_UTILITY.cnIdEsitoAmmessoParzFinanzO/*47*/) THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAmmessoParzFinanz; -- 54
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    END IF;
                            ELSIF vCodOggetto='ISANT' THEN
                                    IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*26,28*/) THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstrConclusaPos; -- 58
                                    ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstrConclusaNeg; -- 59
                                    ELSE
                                            RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto IN ('ISACC','ISSAL','ISVAR','ISVOL') THEN
                                    IF nIstruttorie = 1 THEN
                                        IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO /*26*/ THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstrConclusaPos; -- 58
                                        ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoTot; -- 26
                                        ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*28*/ THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoPrz; -- 27
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    ELSIF nIstruttorie > 1 THEN
                                        IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*26,28*/) THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstrConclPosFollownCD; -- 60
                                        ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstrConclNegFollownCD; -- 61
                                        ELSE
                                                RAISE ERR_ESITO;
                                        END IF;
                                    END IF;
                            ELSIF vCodOggetto IN ('ISTPR', 'ISTIR') THEN
                                    IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*26,28*/) THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaApprovata; -- 22
                                    ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoPreavvisoRigettoTot; -- 26
                                    ELSE
                                            RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto='ISTPF' THEN
                                    IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovPosO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO/*26,28*/) THEN
                                            --pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaApprovata; -- 22
                                            pIdStatoNuovoGruppo:=0;
                                    ELSIF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovNegO /*27*/  THEN
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstrNotApprov; -- 62
                                    ELSE
                                            RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto='REVO' THEN
                                    IF nIstruttorie > 1 THEN
                                        IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO /*26*/ THEN
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoRevocato; -- 96
                                        ELSE
                                                pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaConclusa; -- 30
                                        END IF;
                                    ELSE
                                        pIdStatoNuovoGruppo:=0;
                                    END IF;
                            ELSIF vCodOggetto IN ('ISANP','ISANV') THEN
                                    IF nIstruttorie > 1 THEN
                                            IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO THEN -- 26
                                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAnnullato; -- 99
                                            ELSE/*IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28* /)  THEN*/
                                                    pIdStatoNuovoGruppo:=0;
                                            /*ELSE
                                                    RAISE ERR_ESITO;*/
                                            END IF;
                                    ELSE
                                            IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO THEN -- 26
                                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoAnnullato; -- 99
                                            ELSE/*IF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28* /)  THEN*/
                                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaConclusa; -- 30
                                            /*ELSE
                                                    RAISE ERR_ESITO;*/
                                            END IF;
                                    END IF;
                            ELSIF vCodOggetto='ISRIF' THEN
                                    IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO THEN -- 26
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoRinuncia; -- 98
                                    ELSIF pIdEsito IN (PCK_nembo_UTILITY.cnIdEsitoApprovNegO,PCK_nembo_UTILITY.cnIdEsitoApprovParzPosO /*27,28*/)  THEN
                                            pIdStatoNuovoGruppo:=0;
                                    ELSE
                                            RAISE ERR_ESITO;
                                    END IF;
                            ELSIF vCodOggetto='COEXP' THEN
                                    IF nIstruttorie > 1 THEN
                                        pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaConclusa; -- 30
                                    ELSE
                                        IF pIdEsito=PCK_nembo_UTILITY.cnIdEsitoApprovPosO THEN -- 26
                                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaConclusa; -- 30
                                        ELSE
                                            pIdStatoNuovoGruppo:=0;
                                        END IF;
                                    END IF;
                            ELSE
                                    pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoIstruttoriaConclusa; -- 30
                            END IF;
--                    ELSE
                            -- non si possono approvare le istanze
                    END IF;
        ELSIF pIdOperazione = 6 THEN
                    -- 6..AVVIO ISTRUTTORIA (ma non correre, neh)
                    -- caso ultra specifico
                    -- a) stato del procedimento
                    IF vCodOggetto='ISTPR' THEN
                            pIdStatoNuovoProcedimento:= PCK_nembo_UTILITY.cnIdStatoIstruttoriaAvviata; -- 15
                    ELSE
                            pIdStatoNuovoProcedimento:=0;
--                            RAISE ERR_AVVIO_ISTR;
                    END IF;
                    -- b) stato dell'oggetto
                    pIdStatoNuovoOggetto:= 0;
                    -- c) esito
                    pIdEsitoNuovo:=0;
                    -- d) stato del gruppo
                    IF vCodOggetto='ISTPR' THEN
                            pIdStatoNuovoGruppo:= PCK_nembo_UTILITY.cnIdStatoIstruttoriaAvviata; -- 15
                    ELSE
                            pIdStatoNuovoGruppo:=0;
                    END IF;
        ELSIF pIdOperazione = 7 THEN
                    -- 7..CALCOLO PREMIO
                    -- caso ultra specifico
                    -- a) stato del procedimento
                    IF vCodOggetto='ISTPR' AND nIdStatoAttualeProcedimento = PCK_nembo_UTILITY.cnIdStatoIstruttoriaInCorso /* 14 */  THEN
                            pIdStatoNuovoProcedimento:= PCK_nembo_UTILITY.cnIdStatoIstruttoriaAvviata; -- 15
                    ELSE
                            pIdStatoNuovoProcedimento:=0;
--                            RAISE ERR_CALC_PREMIO;
                    END IF;
                    -- b) stato dell'oggetto
                    pIdStatoNuovoOggetto:= 0;
                    -- c) esito
                    pIdEsitoNuovo:=0;
                     -- d) stato del gruppo
                    IF vCodOggetto='ISTPR' AND nIdStatoAttualeProcedimento = PCK_nembo_UTILITY.cnIdStatoIstruttoriaInCorso /* 14 */  THEN
                            pIdStatoNuovoGruppo:= PCK_nembo_UTILITY.cnIdStatoIstruttoriaAvviata; -- 15
                    ELSE
                            pIdStatoNuovoGruppo:=0;
                    END IF;
        ELSIF pIdOperazione = 8 THEN
                    -- 8..ELIMINAZIONE (svuota il cestino)
                    -- a) stato del procedimento
                    pIdStatoNuovoProcedimento:=0;
                    IF nTipoOggetto = 'I' THEN
                             -- d) stato del gruppo
                             pIdStatoNuovoGruppo:=0;
                    ELSE
                             -- d) stato del gruppo
                             pIdStatoNuovoGruppo:=-1; -- torno a stato precedente
                    END IF;
        ELSIF pIdOperazione = 9 THEN
                    -- 9..LIQUIDAZIONE (e non solidazione)
                    -- a) stato del procedimento
                    IF vTipoBando = 'PRE' THEN
                            -- premio
                            IF vTipoPagamento='SALDO' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoInLiquidazione; -- 37
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;
                    ELSE
                            -- investimento
                            /*IF vTipoPagamento='ANTIC' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticInLiquid; -- 63
                            ELSIF vTipoPagamento='ACCON' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconInLiquid; -- 65
                            ELSIF vTipoPagamento='SALDO' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoInLiquid; -- 67
                            ELSIF vTipoPagamento='INTEG' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIntegInLiquid; -- 69
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;*/
                            --IF vTipoPagamento='SALDO' THEN
                            IF vTipoPagamento IN ('SALDO', 'INTEG') THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoInLiquidazione; -- 37
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;
                    END IF;
                     -- d) stato del gruppo
                     pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoInLiquidazione; -- 37
        ELSIF pIdOperazione = 10 THEN
                    -- 10..LIQUIDATO 
                    -- a) stato del procedimento
                    IF vTipoBando = 'PRE' THEN
                            -- premio
                            IF vTipoPagamento='SALDO' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoLiquidato; -- 38
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;
                    ELSE
                            -- investimento
                           /* IF vTipoPagamento='ANTIC' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAnticLiquidato; -- 64
                            ELSIF vTipoPagamento='ACCON' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoAcconLiquidato; -- 66
                            ELSIF vTipoPagamento='SALDO' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoSaldoLiquidato; -- 68
                            ELSIF vTipoPagamento='INTEG' THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoIntegLiquidata; -- 70
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;*/
                            --IF vTipoPagamento='SALDO' THEN
                            IF vTipoPagamento IN ('SALDO', 'INTEG') THEN
                                pIdStatoNuovoProcedimento:=PCK_nembo_UTILITY.cnIdStatoLiquidato; -- 38
                            ELSE
                                pIdStatoNuovoProcedimento:=0;
                            END IF;
                    END IF;
                     -- d) stato del gruppo
--                     -- caso particolare: se pullulano le controdeduzioni, l'Arcivescovo di Forlimpopoli intende non sterminarle
--                     IF nIdStatoAttualeGruppo=PCK_nembo_UTILITY.cnIdStatoInLiquidWithCD/*49*/ THEN
--                            pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoLiquidatoWithCD; -- 50
--                     ELSE
                     pIdStatoNuovoGruppo:=PCK_nembo_UTILITY.cnIdStatoLiquidato; -- 38
--                     END IF;
        END IF;
--        -- per le misure a premio blocco lo stato del gruppo
--        IF vTipoBando = 'PRE' THEN
--                pIdStatoNuovoGruppo:=0;
--        END IF;
        -- se gli stati attuali sono uguali agli stati nuovi non faccio nulla. E che ci stiamo qui a fare, a fare ombra ai baobab?
        IF pIdStatoNuovoProcedimento=nIdStatoAttualeProcedimento THEN
                pIdStatoNuovoProcedimento:=0;
        END IF;
        IF pIdStatoNuovoOggetto=nIdStatoAttualeOggetto THEN
                pIdStatoNuovoOggetto:=0;
        END IF;
        IF pIdStatoNuovoGruppo=nIdStatoAttualeGruppo THEN
                pIdStatoNuovoGruppo:=0;
        END IF;

EXCEPTION
  WHEN ERR_ESITO THEN
    pRisultato := 1;
    pMessaggio := 'Esito non previsto, impossibile procedere';-- (ID_ESITO='||TO_CHAR(pIdEsito)||')';
  WHEN ERR_NUM_ISTR_0 THEN
    pRisultato := 1;
    pMessaggio := 'Numero istruttorie pari a zero, impossibile procedere';
--  WHEN ERR_AVVIO_ISTR THEN
--    pRisultato := 1;
--    pMessaggio := 'Operazione consentita solo per l''oggetto ISTPR';
----  WHEN ERR_CALC_PREMIO THEN
--    pRisultato := 1;
--    pMessaggio := 'Operazione consentita solo per l''oggetto ISTPR quando lo stato di partenza del procedimento è 14-Istruttoria in corso';
  WHEN ERR_PAR_INPUT THEN
    pRisultato := 1;
    pMessaggio := 'Parametri di input non validi';
  WHEN ERR_NO_STATO_PO THEN
    pRisultato := 1;
    pMessaggio := 'Impossibile recuperare lo stato attuale dell''oggetto';
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nella chiusura dell''oggetto. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
END  MainGestioneStati;

-- funzione che riporta l'iter del gruppo allo stato precedente a quello attuale (dati in input id procedimento e codice raggruppamento)
FUNCTION RiportaStatoPrecIterGruppo(pIdProcedimento                        NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_PROCEDIMENTO%TYPE,
                                                                  pCodiceRaggruppamento            NEMBO_T_ITER_PROCEDIMENTO_GRUP.CODICE_RAGGRUPPAMENTO%TYPE) RETURN BOOLEAN IS

nIdIterProcedimentoGruppoPrec       NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_ITER_PROCEDIMENTO_GRUPPO%TYPE;

BEGIN

        -- individuo lo stato precedente
        BEGIN
                SELECT DISTINCT FIRST_VALUE(IPG.ID_ITER_PROCEDIMENTO_GRUPPO) OVER (ORDER BY IPG.DATA_INIZIO DESC)
                INTO nIdIterProcedimentoGruppoPrec
                FROM NEMBO_T_ITER_PROCEDIMENTO_GRUP IPG
                WHERE ID_PROCEDIMENTO = pIdProcedimento
                AND CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
                AND DATA_FINE IS NOT NULL;
        EXCEPTION
                WHEN NO_DATA_FOUND THEN
                        nIdIterProcedimentoGruppoPrec:=NULL;
        END;

        IF nIdIterProcedimentoGruppoPrec IS NOT NULL THEN
                -- 1. elimino lo stato corrente del gruppo
                DELETE FROM NEMBO_T_ITER_PROCEDIMENTO_GRUP
                WHERE ID_PROCEDIMENTO = pIdProcedimento
                AND CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
                AND DATA_FINE IS NULL;

                -- 2. riapro lo stato precedente
                UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP
                SET DATA_FINE = NULL
                WHERE ID_ITER_PROCEDIMENTO_GRUPPO = nIdIterProcedimentoGruppoPrec;

        END IF;

        RETURN(TRUE);

EXCEPTION
        WHEN OTHERS THEN
                RETURN(FALSE);
END  RiportaStatoPrecIterGruppo;

/* recupero l'eventuale stringa da mettere nelle note dell'iter.
Input:
- id_procedimento
- id legame gruppo oggetto
Output:
- stringa note
*/
FUNCTION ReturnNote(pIdLegameGruppoOggetto           nembo_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                                  pIdProcedimento                        nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                                  pCodiceRaggruppamento            nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE ) RETURN NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE IS

vCodiceOggettoCurr              nembo_D_OGGETTO.CODICE%TYPE;
vIdentificativoAltroOgg           nembo_T_PROCEDIMENTO_OGGETTO.IDENTIFICATIVO%TYPE;
BEGIN

  SELECT O.CODICE
  INTO vCodiceOggettoCurr
  FROM nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO O
  WHERE LGO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
  AND LGO.ID_OGGETTO=O.ID_OGGETTO;

  IF vCodiceOggettoCurr='RPROP' THEN
        -- ricerco ad hoc un oggetto valido a parita' di procedimento/codice/raggruppamento
        BEGIN
            SELECT PO.IDENTIFICATIVO
            INTO vIdentificativoAltroOgg
            FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO O, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
            WHERE PO.ID_PROCEDIMENTO = pIdProcedimento
            AND PO.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
            AND PO.ID_LEGAME_GRUPPO_OGGETTO  = LGO.ID_LEGAME_GRUPPO_OGGETTO
            AND LGO.ID_OGGETTO = O.ID_OGGETTO
            AND O.CODICE = 'CPROP'
            AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
            AND IPO.DATA_FINE IS NULL
            AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                    RETURN NULL;
            WHEN OTHERS THEN
                    RETURN NULL;
        END;
        RETURN 'Rettifica della domanda numero:'||vIdentificativoAltroOgg;
  ELSE
        RETURN NULL;
  END IF;

EXCEPTION
    WHEN OTHERS THEN
        RETURN NULL;
END ReturnNote;


-- funzione che si occupa gi aggiornare gli importi sulla nembo_R_PROCEDIMENTO_LIVELLO. Verra' richiamata da MainApprovazione e MainTrasmissione
FUNCTION AggiornaImportiLivelli(pIdProcedimento                        nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                                pIdProcedimentoOggetto             nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                pIdLegameGruppoOggetto           nembo_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                                pCodiceRaggruppamento            nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE,
                                pFlagIstanza                              nembo_D_OGGETTO.FLAG_ISTANZA%TYPE,
                                pCodiceEsito                              nembo_D_ESITO.CODICE%TYPE,
                                pCodiceOggetto                          nembo_D_OGGETTO.CODICE%TYPE,
                                pRisultato                                  OUT NUMBER,
                                pMessaggio                               OUT VARCHAR2) RETURN BOOLEAN IS

  ERR_CAPIENZA                          EXCEPTION;

  bFlagProcedi                          BOOLEAN;

  TbProcedimentoLivello           typProcedimentoLivello;

  nTotImpInvestimento               nembo_T_DETTAGLIO_INTERVENTO.IMPORTO_INVESTIMENTO%TYPE;
  nTotImpAmmesso                    nembo_T_DETTAGLIO_INTERVENTO.IMPORTO_AMMESSO%TYPE;
  nTotImpContributo                   nembo_T_DETTAGLIO_INTERVENTO.IMPORTO_CONTRIBUTO%TYPE;
  nTotContributoErog                    NEMBO_R_PROCEDIMENTO_OGG_LIVEL.CONTRIBUTO_EROGABILE%TYPE;
  nTotContributoNonErog               NEMBO_R_PROCEDIMENTO_OGG_LIVEL.CONTRIBUTO_NON_EROGABILE%TYPE;

  nImpAnticipoLivello                   nembo_R_ANTICIPO_LIVELLO.IMPORTO_ANTICIPO%TYPE;

  nIstruttorie                               PLS_INTEGER;

  nIdBando                              nembo_T_PROCEDIMENTO.ID_BANDO%TYPE;
  nCapienzaBando                        nembo_T_RISORSE_LIVELLO_BANDO.RISORSE_ATTIVATE%TYPE;
  vTipoLivello                          nembo_D_TIPO_LIVELLO.CODICE%TYPE;


BEGIN

            pRisultato := 0;
            pMessaggio := '';

            IF (pCodiceEsito = 'APP-N' AND pCodiceOggetto NOT IN ('ISAMM','ISAMB','ISAMC','ISAMG','ISABG')) THEN
                    -- se esito negativo e non sono in quelle tre istruttorie ad hoc non devo fare nulla
                    bFlagProcedi:=FALSE;
            ELSE
                    bFlagProcedi:=TRUE;
            END IF;
            IF bFlagProcedi THEN
                    -- bando
                    SELECT BB.ID_BANDO, TL.CODICE
                    INTO nIdBando, vTipoLivello
                    FROM nembo_T_PROCEDIMENTO PP, nembo_D_BANDO BB, nembo_D_TIPO_LIVELLO TL
                    WHERE PP.ID_PROCEDIMENTO = pIdProcedimento
                    AND PP.ID_BANDO = BB.ID_BANDO
                    AND BB.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO;
                    -- recupero i rek
                    TbProcedimentoLivello.DELETE;
                    SELECT *
                    BULK COLLECT INTO TbProcedimentoLivello
                    FROM nembo_R_PROCEDIMENTO_LIVELLO
                    WHERE ID_PROCEDIMENTO = pIdProcedimento;
                    FOR uu IN 1..TbProcedimentoLivello.COUNT LOOP
                        -- per ogni livello recupero i valori di importo investimento, importo ammesso e importo contributo
                        SELECT NVL(SUM(DI.IMPORTO_INVESTIMENTO),0), NVL(SUM(NVL(DI.IMPORTO_AMMESSO,0)),0), NVL(SUM(NVL(DI.IMPORTO_CONTRIBUTO,0)),0)
                        INTO nTotImpInvestimento, nTotImpAmmesso, nTotImpContributo
                        FROM nembo_T_DETTAGLIO_INTERVENTO DI, nembo_T_INTERVENTO II, nembo_D_DESCRIZIONE_INTERVENTO DESCINT, nembo_R_LIVELLO_INTERVENTO LI
                        WHERE II.ID_PROCEDIMENTO =  pIdProcedimento
                        AND DI.ID_INTERVENTO = II.ID_INTERVENTO
                        AND II.ID_DESCRIZIONE_INTERVENTO = DESCINT.ID_DESCRIZIONE_INTERVENTO
                        AND DESCINT.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO
                        AND LI.ID_LIVELLO = TbProcedimentoLivello(uu).ID_LIVELLO
                        AND DI.FLAG_TIPO_OPERAZIONE <> 'D'
                        AND (SELECT NVL(DATA_FINE,SYSDATE) FROM nembo_T_PROCEDIMENTO_OGGETTO WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE,SYSDATE);
                        IF (pCodiceEsito = 'APP-N' AND pCodiceOggetto IN ('ISAMM','ISAMB','ISAMC','ISAMG','ISABG')) THEN
                                nTotImpAmmesso:=0;
                                nTotImpContributo:=0;
                        END IF;
                        -- aggiorno diversamente a seconda che sia un'istanza oppure no
                        IF pFlagIstanza = 'I' THEN
                                UPDATE nembo_R_PROCEDIMENTO_LIVELLO SET IMPORTO_INVESTIMENTO = nTotImpInvestimento
                                WHERE ID_PROCEDIMENTO = pIdProcedimento AND ID_LIVELLO = TbProcedimentoLivello(uu).ID_LIVELLO;
                        ELSE
                                IF (vTipoLivello IN ('I','G') AND nTotImpContributo > 0) THEN
                                        nCapienzaBando:=PCK_nembo_UTILITY.RendiCapienzaBandoLiv(pIdProcedimento, nIdBando, TbProcedimentoLivello(uu).ID_LIVELLO);
                                        IF nTotImpContributo > nCapienzaBando THEN
                                                RAISE ERR_CAPIENZA;
                                        END IF;
                                END IF;
                                SELECT NVL(SUM(POL.CONTRIBUTO_EROGABILE),0), NVL(SUM(POL.CONTRIBUTO_NON_EROGABILE),0)
                                INTO nTotContributoErog, nTotContributoNonErog
                                FROM NEMBO_R_PROCEDIMENTO_OGG_LIVEL POL, nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_D_ESITO EE
                                WHERE POL.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO
                                AND POL.ID_LIVELLO = TbProcedimentoLivello(uu).ID_LIVELLO
                                AND PO.ID_PROCEDIMENTO = pIdProcedimento
                                AND PO.ID_ESITO = EE.ID_ESITO
                                AND EE.CODICE IN ('APP-P');
                                BEGIN
                                    SELECT ANTCLIV.IMPORTO_ANTICIPO
                                    INTO nImpAnticipoLivello
                                    FROM nembo_T_ANTICIPO ANTC, nembo_R_ANTICIPO_LIVELLO ANTCLIV
                                    WHERE ANTC.ID_PROCEDIMENTO = pIdProcedimento
                                    AND ANTC.ID_STATO_OGGETTO = PCK_nembo_UTILITY.cnIdStatoApprovato
                                    AND ANTC.ID_ANTICIPO = ANTCLIV.ID_ANTICIPO
                                    AND ANTCLIV.ID_LIVELLO = TbProcedimentoLivello(uu).ID_LIVELLO;
                                EXCEPTION
                                    WHEN NO_DATA_FOUND THEN
                                            nImpAnticipoLivello:=0;
                                END;
                                -- aggiorno
                                UPDATE nembo_R_PROCEDIMENTO_LIVELLO SET IMPORTO_INVESTIMENTO = nTotImpInvestimento, SPESA_AMMESSA = nTotImpAmmesso, CONTRIBUTO_CONCESSO = nTotImpContributo,
                                                                                                    CONTRIBUTO_EROGABILE =  (nTotContributoErog+nImpAnticipoLivello), CONTRIBUTO_NON_EROGABILE =  nTotContributoNonErog
                                WHERE ID_PROCEDIMENTO = pIdProcedimento AND ID_LIVELLO = TbProcedimentoLivello(uu).ID_LIVELLO;
                        END IF;
                    END LOOP;
            END IF;

            IF (pCodiceEsito = 'APP-N' AND pCodiceOggetto='ISSAL') THEN
                        -- recupero il numero istruttorie
                        -- vale a dire il numero di oggetti dello stesso tipo di quello passato in input, con lo stesso codice raggruppamento e legati al procedimento corrente
                        SELECT COUNT(*)
                        INTO nIstruttorie
                        FROM nembo_T_PROCEDIMENTO_OGGETTO PO , NEMBO_T_ITER_PROCEDIMENTO_OGGE ITERPO
                        WHERE PO.ID_PROCEDIMENTO = pIdProcedimento
                        AND PO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
                        AND PO.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
                        AND PO.ID_PROCEDIMENTO_OGGETTO = ITERPO.ID_PROCEDIMENTO_OGGETTO
                        AND ITERPO.DATA_FINE IS NULL
                        AND ITERPO.ID_STATO_OGGETTO <= 90;

                        IF nIstruttorie > 1 THEN
                                FOR recLiv IN (SELECT ID_LIVELLO
                                               FROM nembo_R_PROCEDIMENTO_LIVELLO
                                               WHERE ID_PROCEDIMENTO = pIdProcedimento) LOOP

                                               UPDATE nembo_R_PROCEDIMENTO_LIVELLO SET CONTRIBUTO_NON_EROGABILE = NVL(CONTRIBUTO_CONCESSO,0) - NVL(CONTRIBUTO_EROGABILE,0)
                                               WHERE ID_PROCEDIMENTO = pIdProcedimento
                                               AND ID_LIVELLO = recLiv.ID_LIVELLO;
                                END LOOP;
                        END IF;
            END IF;

            RETURN(TRUE);
EXCEPTION
        WHEN ERR_CAPIENZA THEN
                pRisultato := 2;
                pMessaggio := 'Il contributo concesso supera la capienza del bando';
                RETURN(FALSE);
        WHEN OTHERS THEN
                pRisultato := 1;
                pMessaggio := 'Errore di sistema. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
                RETURN(FALSE);
END AggiornaImportiLivelli;

/*** Ricavo comune ***/
FUNCTION RicavoComune(pIstatComune                         SMRGAA_V_DATI_AMMINISTRATIVI.ISTAT_COMUNE%TYPE)
                          RETURN VARCHAR2 IS
/* funzione che serve a ricavare la descrizione del comune passato come id */

vDescom              SMRGAA_V_DATI_AMMINISTRATIVI.DESCRIZIONE_COMUNE%TYPE;

BEGIN
      SELECT DESCRIZIONE_COMUNE
      INTO vDescom
      FROM SMRGAA_V_DATI_AMMINISTRATIVI
      WHERE ISTAT_COMUNE=pIstatComune;

      RETURN (vDescom || ' (' || pIstatComune || ')');

exception
 when others then
    RETURN ('<comune non trovato');
END RicavoComune;

/*** Ricavo utilizzo ***/
FUNCTION RicavoUtilizzo(pIdUtilizzo                NEMBO_D_UTILIZZO.EXT_ID_UTILIZZO%TYPE)
                          RETURN VARCHAR2 IS
/* funzione che serve a ricavare la descrizione dell'utilizzo passato come id */

vDescUtil              SMRGAA_V_MATRICE.DESCRIZIONE_UTILIZZO%TYPE;

BEGIN
      SELECT DISTINCT DESCRIZIONE_UTILIZZO
      INTO vDescUtil
      FROM SMRGAA_V_MATRICE
      WHERE ID_UTILIZZO=pIdUtilizzo;

      RETURN (vDescUtil || '(ID=' || pIdUtilizzo || ')');

exception
 when others then
    RETURN '<utilizzo non trovato>';
END RicavoUtilizzo;

/*** Valori Medi Superfici ***/
FUNCTION RendiValoriMediSuperf(pIdUtilizzo                NEMBO_D_UTILIZZO.EXT_ID_UTILIZZO%TYPE,
                                   pIstatComune                         SMRGAA_V_DATI_AMMINISTRATIVI.ISTAT_COMUNE%TYPE,
                                   pProdHAMedia                        OUT NEMBO_D_UTILIZZO.PRODUZIONE_HA_MEDIA%TYPE,
                                   pPrezzoMedio                        OUT NEMBO_D_UTILIZZO.PREZZO_MEDIO%TYPE,
                                   pGGLavMedie                              OUT NEMBO_D_GG_LAVORATE.GIORNATE_LAVORATE_MEDIE%TYPE,
                                   pRisultato                                  OUT NUMBER,
                                   pMessaggio                               OUT VARCHAR2)
                           RETURN BOOLEAN IS

BEGIN

    pRisultato := 0;

     BEGIN
         /* cerco se esiste un record con il dato id utilizzo nella tabella */
         SELECT A.PREZZO_MEDIO
         INTO pPrezzoMedio
         FROM NEMBO_D_UTILIZZO A
         WHERE A.EXT_ID_UTILIZZO=pIdUtilizzo;
     EXCEPTION
         when NO_DATA_FOUND then
            pRisultato := 1;
            pMessaggio := 'Utilizzo non trovato ('|| RicavoUtilizzo(pIdUtilizzo)||')';
              RETURN(FALSE);
     END;

     BEGIN
         /* cerco se esiste un record con il dato id utilizzo/zona altimetrica nella tabella */
         SELECT A1.PRODUZIONE_HA_MEDIA
         INTO pProdHAMedia
         FROM NEMBO_D_PRODUZIONE_HA A1, SMRGAA_V_DATI_AMMINISTRATIVI B
         WHERE A1.EXT_ID_UTILIZZO=pIdUtilizzo
         AND A1.EXT_ID_ZONA_ALTIMETRICA = B.ZONAALT
         AND B.ISTAT_COMUNE             = pIstatComune;
     EXCEPTION
         when NO_DATA_FOUND then
            pRisultato := 1;
            pMessaggio := 'Produzione Ha/media non trovata (Utilizzo:' || RicavoUtilizzo(pIdUtilizzo) ||
                 ' / Comune:' || RicavoComune(pIstatComune)||')';
              RETURN(FALSE);
     END;

     BEGIN
         /* cerco se esiste un record con il dato id utilizzo e la zona altimetrica del comune nella tabella */
         SELECT A.GIORNATE_LAVORATE_MEDIE
         INTO pGGLavMedie
         FROM NEMBO_D_GG_LAVORATE A, SMRGAA_V_DATI_AMMINISTRATIVI B
         WHERE A.EXT_ID_UTILIZZO          =    pIdUtilizzo
         AND A.EXT_ID_ZONA_ALTIMETRICA = B.ZONAALT
         AND B.ISTAT_COMUNE             = pIstatComune;
     EXCEPTION
         when NO_DATA_FOUND then
            pRisultato := 1;
            pMessaggio := 'Giornate lavorate non trovate (Utilizzo:' || RicavoUtilizzo(pIdUtilizzo) ||
                 ' / Comune:' || RicavoComune(pIstatComune)||')';
              RETURN(FALSE);
     END;

    RETURN(TRUE);

exception
 when others then
    pRisultato := 1;
    pMessaggio := 'Errore grave nella funzione RendiValoriMediSuperf: '|| SQLERRM;
    RETURN(FALSE);
END RendiValoriMediSuperf;


-- creazione dell'oggetto
FUNCTION MainCreazione(pIdBando                    nembo_D_BANDO.ID_BANDO%TYPE,
                       pIdLegameGruppoOggetto           nembo_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                       --pIdProcedimentoAgricolo            NEMBO_T_PROCEDIMENTO.ID_PROCEDIMENTO_AGRICOLO%TYPE,
                       pIdProcedimento                        nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE,
                       pExtIdAzienda                            SMRGAA_V_DATI_ANAGRAFICI.ID_AZIENDA%TYPE,
                       pCodiceRaggruppamento            nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE,
                       pForzaAlfaNumerico                   CHAR,
                       pExtIdUtente                              NUMBER,
                       pCodAttore                                nembo_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE,
                       pRisultato                                  OUT NUMBER,
                       pMessaggio                               OUT VARCHAR2) RETURN nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE IS

  nIdOperazione                     CONSTANT PLS_INTEGER := 1; -- creazione

  nIdStatoNuovoProcedimento         nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoOggetto              NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoGruppo               NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
  nIdEsitoNuovo                     nembo_D_ESITO.ID_ESITO%TYPE;
  nRisultato                        NUMBER;
  vMessaggio                        VARCHAR2(250);

  ERRORE                            EXCEPTION;
  ERRORE_TIPOLOGIA                  EXCEPTION;
  ERRORE_STATI                      EXCEPTION;
  ERR_STATO_GRUPPO_PREC             EXCEPTION;
  ERR_VAL_MEDI_S                    EXCEPTION;
  ERR_DANNO_DEFAULT                 EXCEPTION;

  nIdProcedimento                   nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE;
  nIdProcedimentoOggetto            nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE := SEQ_NEMBO_T_PROCEDIMENTO_OGGET.NEXTVAL;
  nCont                             SIMPLE_INTEGER := 0;
  nIdContoCorrente                  SMRGAA_V_CONTO_CORRENTE.ID_CONTO_CORRENTE%TYPE;
  nIdDichConsistenza                nembo_T_PROCEDIMENTO_OGGETTO.EXT_ID_DICHIARAZIONE_CONSISTEN%TYPE;
  nCodiceRaggruppamento             nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;
  vNote                             nembo_T_PROCEDIMENTO_OGGETTO.NOTE%TYPE;
  bFlagIstanzaBenef                 BOOLEAN;
  nIdDatiProcedimento               nembo_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE;

  TbAmmComp                         recAmmComp;

  vTipoOggetto                      nembo_D_OGGETTO.CODICE%TYPE;
  vOggettoCurr                      nembo_D_OGGETTO.CODICE%TYPE;
  vDescOggetto                      nembo_D_OGGETTO.DESCRIZIONE%TYPE;
  --nIdGruppoOggetto                nembo_R_LEGAME_GRUPPO_OGGETTO.ID_GRUPPO_OGGETTO%TYPE;
  vCodiceGruppoOggetto              nembo_D_GRUPPO_OGGETTO.CODICE%TYPE;

  dRitardoBO                        nembo_R_BANDO_OGGETTO.DATA_RITARDO%TYPE;
  dFineBO                           nembo_R_BANDO_OGGETTO.DATA_FINE%TYPE;
  nAnnoCampagna                     nembo_D_BANDO.ANNO_CAMPAGNA%TYPE;

  bFlagProcedi                      BOOLEAN;
  vTipoLivello                      nembo_D_TIPO_LIVELLO.CODICE%TYPE;

  vDescOggettoCurr                  nembo_D_OGGETTO.DESCRIZIONE%TYPE;
  vDescGruppoCurr                   nembo_D_GRUPPO_OGGETTO.DESCRIZIONE%TYPE;
  bFlagBloccaCreazione              BOOLEAN;

  strApp                            VARCHAR2(1000);

  flagAggiornaCurr                  BOOLEAN;

  nIdAziendaProvenienza             SMRGAA_V_DATI_ANAGRAFICI.ID_AZIENDA_PROVENIENZA%TYPE;
  nIdAziendaCurr                    SMRGAA_V_DATI_ANAGRAFICI.ID_AZIENDA%TYPE;
  vCUAA                             SMRGAA_V_DATI_ANAGRAFICI.CUAA%TYPE;

  nSupUtilCurr                      nembo_T_PARTICELLA_UTILIZZO.SUPERFICIE_UTILIZZATA%TYPE;

  nIdAmmCompetenza                  NEMBO_T_PROCEDIM_AMMINISTRAZIO.EXT_ID_AMM_COMPETENZA%TYPE;
  nIdUfficioZona                    NEMBO_T_PROCEDIM_AMMINISTRAZIO.EXT_ID_UFFICIO_ZONA%TYPE;

  vCodiceEsito                      nembo_D_ESITO.CODICE%TYPE;
  vFlagMultipla                     nembo_d_bando.flag_domanda_multipla%TYPE;
  --nIdSuperficieColtura              NEMBO_T_SUPERFICIE_COLTURA.ID_SUPERFICIE_COLTURA%TYPE;

  nProdHAMedia                      NEMBO_T_SUPERFICIE_COLTURA.PRODUZIONE_HA%TYPE;
  nGGLavMedie                       NEMBO_T_SUPERFICIE_COLTURA.GIORNATE_LAVORATE%TYPE;
  nPrezzoMedio                      NEMBO_T_SUPERFICIE_COLTURA.PREZZO%TYPE;

  dEvento                           NEMBO_D_EVENTO_CALAMITOSO.DATA_EVENTO%TYPE;

  -- oggetto precedente
  nIdDatiProcedimentoLastObj        NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE;
  nIdDannoATM                       NEMBO_T_DANNO_ATM.ID_DANNO_ATM%TYPE;
  nIdProduzioneZootecnica           NEMBO_T_PRODUZIONE_ZOOTECNICA.ID_PRODUZIONE_ZOOTECNICA%TYPE;

  nIdProcedimentoAgricolo           NEMBO_T_PROCEDIMENTO.ID_PROCEDIMENTO_AGRICOLO%TYPE;
  nIdSegnalazDanno                  NEMBO_T_SEGNALAZIONE_DANNO.ID_SEGNALAZIONE_DANNO%TYPE;
  --valori di default della descrizione e data del danno
  vDescDannoDefault                 NEMBO_D_EVENTO_CALAMITOSO.DESCRIZIONE_EVENTO_DEFAULT%TYPE;
  vDataDannoDefault                 NEMBO_D_EVENTO_CALAMITOSO.DATA_EVENTO_DEFAULT%TYPE;
  

BEGIN
  pRisultato := 0;
  pMessaggio := '';

   --bFlagAggiornaStatoProc:=FALSE;

  -- recupero il tipo oggetto e non solo
  SELECT OO.CODICE, OO.DESCRIZIONE, /*LGO.ID_GRUPPO_OGGETTO,*/ GGO.CODICE
  INTO vTipoOggetto, vDescOggetto,/* nIdGruppoOggetto,*/ vCodiceGruppoOggetto
  FROM nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO, nembo_D_GRUPPO_OGGETTO GGO
  WHERE LGO.ID_OGGETTO = OO.ID_OGGETTO
  AND LGO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
  AND LGO.ID_GRUPPO_OGGETTO = GGO.ID_GRUPPO_OGGETTO;

  BEGIN
          -- recupero dati del bando/ bandooggetto
          SELECT BO.DATA_RITARDO, BO.DATA_FINE, BB.ANNO_CAMPAGNA,  TL.CODICE TIPO_LIVELLO, NVL(EVCAL.DATA_EVENTO,SYSDATE),FLAG_DOMANDA_MULTIPLA
          INTO dRitardoBO, dFineBO, nAnnoCampagna, vTipoLivello, dEvento,vFlagMultipla
          FROM nembo_D_BANDO BB, nembo_R_BANDO_OGGETTO BO, nembo_D_TIPO_LIVELLO TL, NEMBO_D_EVENTO_CALAMITOSO EVCAL
          WHERE BB.ID_BANDO = pIdBando
          AND BB.ID_BANDO = BO.ID_BANDO
          AND BO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
          AND BB.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO
          AND BB.ID_EVENTO_CALAMITOSO = EVCAL.ID_EVENTO_CALAMITOSO(+);
  EXCEPTION
        WHEN NO_DATA_FOUND THEN
            pMessaggio := 'Errore di configurazione (assenza rek su nembo_R_BANDO_OGGETTO)';
            RAISE ERRORE;
  END;

  BEGIN
        -- recupero  l'id procedimento agricolo dal bando facendo un giro sul tagada'
        SELECT DISTINCT PAGRICLIV.ID_PROCEDIMENTO_AGRICOLO
        INTO nIdProcedimentoAgricolo
        FROM NEMBO_R_LIVELLO_BANDO LIVB, NEMBO_R_PROCEDIMENTO_AGRIC_LIV PAGRICLIV
        WHERE LIVB.ID_BANDO = pIdBando
        AND LIVB.ID_LIVELLO = PAGRICLIV.ID_LIVELLO;
  EXCEPTION
        WHEN OTHERS THEN
            pMessaggio := 'Id procedimento agricolo non trovato o non univoco (ID_BANDO = '||TO_CHAR(pIdBando)||')';
            RAISE ERRORE;
  END;

  -- 1. non e' possibile creare se si e' oltre la data di scadenza configurata per l'oggetto
  IF SYSDATE > NVL(dRitardoBO, NVL(dFineBO, SYSDATE)) THEN
            pMessaggio := 'Termini scaduti per la presentazione dell''oggetto '||vDescOggetto||', impossibile procedere con l''operazione';
            RAISE ERRORE;
  END IF;

  IF PCK_nembo_UTILITY.ReturnIstanzaCreataDaBenef(pIdLegameGruppoOggetto) THEN
        bFlagIstanzaBenef:=TRUE;
        -- se l'oggetto è stato creato da Beneficiario/CAA, recupero la dichiarazione di consistenza. DIPENDE DALLA DATA EVENTO
        nIdDichConsistenza:=PCK_nembo_UTILITY.RETURNDICHCONSISTENZA(pExtIdAzienda,dEvento);
  ELSE
        bFlagIstanzaBenef:=FALSE;
        -- altri tipi di oggetto
        --nIdDichConsistenza:=NULL;
        SELECT EXT_ID_DICHIARAZIONE_CONSISTEN
        INTO nIdDichConsistenza
        FROM
        (
            SELECT PO.EXT_ID_DICHIARAZIONE_CONSISTEN
            FROM NEMBO_T_PROCEDIMENTO_OGGETTO PO, NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO, NEMBO_D_OGGETTO OO
            WHERE PO.ID_PROCEDIMENTO = pIdProcedimento
            AND PO.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
            --AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN IS NOT NULL
            AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
            AND LGO.ID_OGGETTO = OO.ID_OGGETTO
            AND OO.FLAG_ISTANZA = 'S'
            ORDER BY DATA_INIZIO
        ) WHERE ROWNUM = 1;
  END IF;


  IF pIdProcedimento IS NULL THEN

   -- 2. non e' possibile creare se esiste giè un altro procedimento non annullato per lo stesso bando e per la stessa azienda (tranne se il bando è multimisura)
     IF vFlagMultipla ='N' THEN
                  -- Controllo preliminare che non esista giè un procedimento per la stessa azienda e lo stesso bando (escludendo le annullate)
                  SELECT COUNT(*)
                  INTO nCont
                  FROM nembo_T_PROCEDIMENTO PP, nembo_T_PROCEDIMENTO_AZIENDA PROCAZ
                  WHERE PP.ID_PROCEDIMENTO = PROCAZ.ID_PROCEDIMENTO
                  AND PROCAZ.EXT_ID_AZIENDA = pExtIdAzienda
                  AND PROCAZ.DATA_FINE IS NULL
                  AND PP.ID_BANDO = pIdBando
                  AND PP.ID_STATO_OGGETTO <= 90;
                  IF nCont > 0 THEN
                            pMessaggio := 'Esiste giè un procedimento non annullato relativo alla stessa azienda per lo stesso bando';
                            RAISE ERRORE;
                  END IF;
    END IF;
    nIdProcedimento := SEQ_nembo_T_PROCEDIMENTO.NEXTVAL;

    INSERT INTO nembo_T_PROCEDIMENTO
    (ID_PROCEDIMENTO, ID_BANDO, EXT_ID_UTENTE_AGGIORNAMENTO, DATA_ULTIMO_AGGIORNAMENTO, ID_STATO_OGGETTO, ID_PROCEDIMENTO_AGRICOLO)
    VALUES
    (nIdProcedimento,pIdBando,pExtIdUtente,SYSDATE,PCK_nembo_UTILITY.cnIdStatoBozza/*nIdStatoProcedimento*/,nIdProcedimentoAgricolo);

    -- inserimento azienda
    INSERT INTO nembo_T_PROCEDIMENTO_AZIENDA
    (ID_PROCEDIMENTO_AZIENDA, ID_PROCEDIMENTO, EXT_ID_AZIENDA, DATA_INIZIO, EXT_ID_UTENTE_AGGIORNAMENTO)
    VALUES
    (SEQ_NEMBO_T_PROCEDIMENTO_AZIEN.NEXTVAL,nIdProcedimento,pExtIdAzienda,SYSDATE,pExtIdUtente);

    -- inserimento amministrazione e' condizionato dal fatto che ci sia un'unica amministrazione. Se si', allora la inserisco
    TbAmmComp.DELETE;
    SELECT EXT_ID_AMM_COMPETENZA
    BULK COLLECT INTO TbAmmComp
    FROM nembo_D_BANDO_AMM_COMPETENZA
    WHERE ID_BANDO = pIdBando;
    IF TbAmmComp.COUNT = 1 THEN
        nCont:=TbAmmComp.FIRST;
        INSERT INTO NEMBO_T_PROCEDIM_AMMINISTRAZIO
        (ID_PROCEDIM_AMMINISTRAZIONE, ID_PROCEDIMENTO, EXT_ID_AMM_COMPETENZA, DATA_INIZIO, EXT_ID_UTENTE_AGGIORNAMENTO)
        VALUES
        (SEQ_NEMBO_T_PROCEDIM_AMMINISTR.NEXTVAL,nIdProcedimento,TbAmmComp(nCont),SYSDATE,pExtIdUtente);
    END IF;
    nCodiceRaggruppamento:=1;

    -- pre-carico i livelli del procedimento con tutti quelli del bando
    INSERT INTO nembo_R_PROCEDIMENTO_LIVELLO (ID_PROCEDIMENTO, ID_LIVELLO)
        SELECT nIdProcedimento,ID_LIVELLO FROM nembo_R_LIVELLO_BANDO WHERE ID_BANDO = pIdBando;

  ELSE

     nIdProcedimento := pIdProcedimento;

    -- eseguo  controlli preliminari
    -- 3a. non e' possibile creare un oggetto di alcuni tipi se legati al procedimento esistono altri oggetti del medesimo tipo non annullati
    SELECT COUNT(*)
    INTO nCont
    FROM nembo_T_PROCEDIMENTO_OGGETTO PO,  nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO O,
             nembo_R_LEGAME_GRUPPO_OGGETTO LGOCURR, nembo_D_OGGETTO OCURR, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
    WHERE PO.ID_PROCEDIMENTO             = nIdProcedimento
    AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
    AND LGO.ID_OGGETTO = O.ID_OGGETTO
    AND O.CODICE = OCURR.CODICE
    AND OCURR.ID_OGGETTO = LGOCURR.ID_OGGETTO
    AND LGOCURR.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
    AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
    AND IPO.ID_STATO_OGGETTO <= 90
    AND IPO.DATA_FINE IS NULL
    AND OCURR.FLAG_OGGETTO_MULTIPLO = 'N';

     IF nCont > 0 THEN
                pMessaggio := 'Esiste giè per il procedimento l''oggetto selezionato';
                RAISE ERRORE;
     END IF;

    -- 3b. non e' possibile creare un oggetto di alcuni tipi del codice raggruppamento passato in input se legati al procedimento esistono altri oggetti del medesimo tipo non annullati
    SELECT COUNT(*)
    INTO nCont
    FROM nembo_T_PROCEDIMENTO_OGGETTO PO,  nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO O,
             nembo_R_LEGAME_GRUPPO_OGGETTO LGOCURR, nembo_D_OGGETTO OCURR, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
    WHERE PO.ID_PROCEDIMENTO             = nIdProcedimento
    AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
    AND PO.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
    AND LGO.ID_OGGETTO = O.ID_OGGETTO
    AND O.CODICE = OCURR.CODICE
    AND OCURR.ID_OGGETTO = LGOCURR.ID_OGGETTO
    AND LGOCURR.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
    AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
    AND IPO.ID_STATO_OGGETTO <= 90
    AND IPO.DATA_FINE IS NULL
    AND OCURR.FLAG_OGGETTO_MULTIPLO = 'G';

     IF nCont > 0 THEN
                pMessaggio := 'Esiste giè per il procedimento l''oggetto selezionato all''interno dello stesso raggruppamento';
                RAISE ERRORE;
     END IF;

    -- 7. non e' possibile creare un oggetto se il gruppo corrispondente ha il flag_chiuso = S
    SELECT COUNT(*)
    INTO nCont
    FROM nembo_T_PROCEDIMENTO_GRUPPO PG
    WHERE PG.ID_PROCEDIMENTO             = nIdProcedimento
    AND PG.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
    AND PG.DATA_FINE IS NULL
    AND PG.FLAG_GRUPPO_CHIUSO = 'S';

     IF nCont > 0 THEN
                pMessaggio := 'Non è possibile creare un oggetto se il gruppo è chiuso';
                RAISE ERRORE;
     END IF;

         IF bFlagIstanzaBenef THEN

                -- 4. non e' possibile creare un oggetto se il procedimento e' annullato
                SELECT COUNT(*)
                INTO nCont
                FROM nembo_T_PROCEDIMENTO
                WHERE ID_PROCEDIMENTO             = nIdProcedimento
                AND ID_STATO_OGGETTO > 90;

                IF nCont > 0 THEN
                        pMessaggio := 'Il procedimento si trova in uno stato per il quale non  è possibile procedere con l''operazione';
                        RAISE ERRORE;
                 END IF;

                -- 5. non e' possibile creare un oggetto di tipo istanza creata da beneficiario se legati al procedimento esistono altri oggetti di tipo istanza creata da beneficiario ancora da trasmettere (cioe' con id stato < 10)
                SELECT COUNT(*)
                INTO nCont
                FROM nembo_T_PROCEDIMENTO_OGGETTO PO,  nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO O, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
                WHERE PO.ID_PROCEDIMENTO             = nIdProcedimento
                AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
                AND LGO.ID_OGGETTO=O.ID_OGGETTO
                AND O.FLAG_ISTANZA = 'S'
                AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
                AND IPO.DATA_FINE IS NULL
                AND IPO.ID_STATO_OGGETTO < 10;

                IF nCont > 0 THEN
                        pMessaggio := 'Esistono istanze ancora da trasmettere, impossibile procedere con l''operazione';
                        RAISE ERRORE;
                 END IF;

--    ELSE
--
--               IF vTipoLivello IN ('I', 'G') THEN
--                        -- 6. non e' possibile creare un oggetto di tipo non istanza se esiste un altro oggetto di tipo non istanza non approvato a parita' di codice raggruppamento
--                        SELECT COUNT(*)
--                        INTO nCont
--                        FROM nembo_T_PROCEDIMENTO_OGGETTO PO,  nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO O, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO, nembo_D_ESITO EE
--                        WHERE PO.ID_PROCEDIMENTO             = nIdProcedimento
--                        AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
--                        AND LGO.ID_OGGETTO=O.ID_OGGETTO
--                        AND O.FLAG_ISTANZA = 'N'
--                        AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
--                        AND IPO.DATA_FINE IS NULL
--                        AND IPO.ID_STATO_OGGETTO <= 90
--                        AND PO.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento
--                        AND PO.ID_ESITO = EE.ID_ESITO(+)
--                        AND (EE.CODICE IS NULL OR EE.CODICE NOT LIKE 'APP%');
--
--                        IF nCont > 0 THEN
--                                pMessaggio := 'Esistono istruttorie non approvate all''interno dello stesso gruppo, impossibile procedere con l''operazione';
--                                RAISE ERRORE;
--                         END IF;
--               END IF;
--
--               IF vCodiceGruppoOggetto IN ('ACC','ANT','DIRPG','PRO','RIN','SAL','VAR','VOLTU','VAPSL') THEN
--               --'GINPR', rimosso da questa gestione il 26/07/17
--                        -- 13. non e' possibile creare un oggetto di tipo non istanza se non esiste almeno un oggetto istanza trasmesso (non annullato) all'interno dello stesso gruppo (verifica fatta solo per alcuni gruppi)
--                        SELECT COUNT(*)
--                        INTO nCont
--                        FROM nembo_T_PROCEDIMENTO_OGGETTO PO,  nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO O, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
--                        WHERE PO.ID_PROCEDIMENTO             = nIdProcedimento
--                        AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
--                        AND LGO.ID_OGGETTO=O.ID_OGGETTO
--                        AND O.FLAG_ISTANZA = 'S'
--                        AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
--                        AND IPO.DATA_FINE IS NULL
--                        AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90
--                        AND PO.CODICE_RAGGRUPPAMENTO = pCodiceRaggruppamento;
--
--                        IF nCont = 0 THEN
--                                pMessaggio := 'Non esistono istanze trasmesse all''interno dello stesso gruppo, impossibile procedere con l''operazione';
--                                RAISE ERRORE;
--                         END IF;
--               END IF;
    END IF;

     -- controllo vincoli specifici sul tipo oggetto
     bFlagBloccaCreazione:=FALSE;


     IF pCodiceRaggruppamento IS NOT NULL THEN
            -- se mi e' stato passato uso quello
            nCodiceRaggruppamento:=pCodiceRaggruppamento;
            -- recupero eventuali note da scrivere ad hoc in base al tipo oggetto
            vNote:=ReturnNote(pIdLegameGruppoOggetto,nIdProcedimento,nCodiceRaggruppamento);
     ELSE
            SELECT NVL(MAX(CODICE_RAGGRUPPAMENTO),0)+1
            INTO nCodiceRaggruppamento
            FROM nembo_T_PROCEDIMENTO_OGGETTO
            WHERE ID_PROCEDIMENTO             = nIdProcedimento;
     END IF;

  END IF;

  INSERT INTO nembo_T_PROCEDIMENTO_OGGETTO
  (ID_PROCEDIMENTO_OGGETTO, ID_PROCEDIMENTO, EXT_ID_UTENTE_AGGIORNAMENTO, DATA_ULTIMO_AGGIORNAMENTO,
   DATA_INIZIO, ID_LEGAME_GRUPPO_OGGETTO, CODICE_RAGGRUPPAMENTO,EXT_COD_ATTORE,EXT_ID_DICHIARAZIONE_CONSISTEN,FLAG_PREMIO_ACCERTATO,FLAG_ACQUISIZIONE_GIS, FLAG_VALIDAZIONE_GRAFICA, ID_ATTO_AMMI)
  VALUES
  (nIdProcedimentoOggetto,nIdProcedimento,pExtIdUtente,SYSDATE,
   SYSDATE,pIdLegameGruppoOggetto,nCodiceRaggruppamento,pCodAttore,nIdDichConsistenza,'N','N', 'N', NULL);

   PCK_nembo_GESTIONE_PROC_OGGETT.MAINGESTIONESTATI (pIdProcedimento, NULL/*nIdProcedimentoOggetto*/, pIdLegameGruppoOggetto, nIdOperazione, NULL/*pIdEsito*/,/*pCodAttore*/NULL, pIdBando, nCodiceRaggruppamento, nIdStatoNuovoProcedimento, nIdStatoNuovoOggetto, nIdStatoNuovoGruppo, nIdEsitoNuovo, nRisultato, vMessaggio );
   IF nRisultato <> 0 THEN
                    -- lancio eccezione
                    pMessaggio:='Errore durante il richiamo della procedura MainGestioneStati: '||vMessaggio;
                    RAISE ERRORE_STATI;
   ELSE
                    -- aggiorno
                    IF NVL(nIdStatoNuovoProcedimento,-10) <> 0 THEN
                                -- a) stato del procedimento
                                UPDATE nembo_T_PROCEDIMENTO SET ID_STATO_OGGETTO = nIdStatoNuovoProcedimento,
                                                                                EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                                                                DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE
                                WHERE ID_PROCEDIMENTO = nIdProcedimento;
                    END IF;
                    IF NVL(nIdStatoNuovoOggetto,-10) <> 0 THEN
                                -- b) stato dell'oggetto
                                -- chiusura vecchio iter
                                INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_OGGE
                                (ID_ITER_PROCEDIMENTO_OGGETT, ID_PROCEDIMENTO_OGGETTO, ID_STATO_OGGETTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                VALUES
                                (SEQ_NEMBO_T_ITER_PROCEDIMEN_OG.NEXTVAL,nIdProcedimentoOggetto,nIdStatoNuovoOggetto,SYSDATE,NULL,pExtIdUtente,vNote);
                    END IF;
                    IF NVL(nIdStatoNuovoGruppo,-10) <> 0 THEN
                                -- d) stato del gruppo
                                IF NVL(nIdStatoNuovoGruppo,-10) = -1 THEN
                                        IF NOT RiportaStatoPrecIterGruppo(nIdProcedimento,nCodiceRaggruppamento) THEN
                                                RAISE ERR_STATO_GRUPPO_PREC;
                                        END IF;
                                ELSE
                                        -- chiusura vecchio iter
                                        UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP SET DATA_FINE = SYSDATE
                                        WHERE ID_PROCEDIMENTO = nIdProcedimento
                                        AND DATA_FINE IS NULL
                                        AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                        -- apertura nuovo iter
                                        INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                        (ID_ITER_PROCEDIMENTO_GRUPPO, ID_PROCEDIMENTO, ID_STATO_OGGETTO, CODICE_RAGGRUPPAMENTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                        VALUES
                                        (SEQ_NEMBO_T_ITER_PROCEDIMEN_GR.NEXTVAL,nIdProcedimento,nIdStatoNuovoGruppo,nCodiceRaggruppamento,SYSDATE,NULL,pExtIdUtente,NULL/*pNoteGruppo*/); -- ##
                                END IF;
                    END IF;
   END IF;


    INSERT INTO nembo_T_DATI_PROCEDIMENTO
    (ID_DATI_PROCEDIMENTO, ID_PROCEDIMENTO, ID_PROCEDIMENTO_OGGETTO)
    VALUES
    (SEQ_nembo_T_DATI_PROCEDIMENTO.NEXTVAL,NULL,nIdProcedimentoOggetto)
    RETURNING ID_DATI_PROCEDIMENTO INTO nIdDatiProcedimento;

--  -- quadri dinamici
--  -- processo i rek dei quadri dinamici
--  SELECT QD.*
--  BULK COLLECT INTO TbQuadriDinamici
--  FROM   nembo_R_BANDO_OGGETTO BO,nembo_R_QUADRO_OGGETTO QO,nembo_D_QUADRO Q,nembo_R_LEGAME_GRUPPO_OGGETTO LGO,
--         nembo_R_BANDO_OGGETTO_QUADRO BOQ, nembo_D_QUADRO_DINAMICO QD
--  WHERE  BO.ID_BANDO                  = pIdBando
--  AND    BO.ID_LEGAME_GRUPPO_OGGETTO  = LGO.ID_LEGAME_GRUPPO_OGGETTO
--  AND    Q.ID_QUADRO                  = QO.ID_QUADRO
--  AND    LGO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
--  AND    QO.ID_OGGETTO                = LGO.ID_OGGETTO
--  AND    BOQ.ID_BANDO_OGGETTO         = BO.ID_BANDO_OGGETTO
--  AND    BOQ.ID_QUADRO_OGGETTO        = QO.ID_QUADRO_OGGETTO
--  AND    Q.ID_QUADRO                  = QD.ID_QUADRO
--  AND    QD.ID_QUADRO_PADRE IS NOT NULL
--  ORDER BY Q.ID_QUADRO;
--
--  -- ciclo
--  FOR jj IN 1..TbQuadriDinamici.COUNT LOOP
--        -- ricerco il max id proc ogg che possedeva il quadro padre
--        SELECT MAX(PO.ID_PROCEDIMENTO_OGGETTO)
--        INTO nIdProcedimentoOggettoLastObj
--        FROM NEMBO_T_DATI_COMUNI_ELEM_QUADR DCEQ, nembo_T_PROCEDIMENTO_OGGETTO PO
--        WHERE DCEQ.ID_QUADRO = TbQuadriDinamici(jj).ID_QUADRO_PADRE
--        AND DCEQ.ID_PROCEDIMENTO_OGGETTO = PO.ID_PROCEDIMENTO_OGGETTO
--        AND PO.ID_PROCEDIMENTO = nIdProcedimento;
--
--        IF nIdProcedimentoOggettoLastObj IS NOT NULL THEN
--                -- recupero i rek relativi su NEMBO_T_DATI_COMUNI_ELEM_QUADR
--                TbDatiComuniElemQuadro.DELETE;
--                SELECT *
--                BULK COLLECT INTO TbDatiComuniElemQuadro
--                FROM NEMBO_T_DATI_COMUNI_ELEM_QUADR
--                WHERE ID_PROCEDIMENTO_OGGETTO = nIdProcedimentoOggettoLastObj
--                AND ID_QUADRO = TbQuadriDinamici(jj).ID_QUADRO_PADRE
--                ORDER BY NUM_PROGRESSIVO_RECORD;
--                -- e li duplico
--                FOR zz IN 1..TbDatiComuniElemQuadro.COUNT LOOP
--                        INSERT INTO NEMBO_T_DATI_COMUNI_ELEM_QUADR(ID_DATI_COMUNI_ELEM_QUADRO, NUM_PROGRESSIVO_RECORD, ID_PROCEDIMENTO_OGGETTO, ID_QUADRO)
--                        VALUES(SEQ_NEMBO_T_DATI_COMUNI_ELEM_Q.NEXTVAL, TbDatiComuniElemQuadro(zz).NUM_PROGRESSIVO_RECORD, nIdProcedimentoOggetto, TbQuadriDinamici(jj).ID_QUADRO)
--                        RETURNING ID_DATI_COMUNI_ELEM_QUADRO INTO nIdDatiComuniElemQuadro;
--                        -- recupero altresi' i figli
--                        TbDatoElemQuadro.DELETE;
--                        SELECT *
--                        BULK COLLECT INTO TbDatoElemQuadro
--                        FROM nembo_T_DATO_ELEMENTO_QUADRO
--                        WHERE ID_DATI_COMUNI_ELEM_QUADRO = TbDatiComuniElemQuadro(zz).ID_DATI_COMUNI_ELEM_QUADRO
--                        ORDER BY ID_DATO_ELEMENTO_QUADRO;
--                        -- e li duplico facendo un simpatico gioco dell'oca per cambiare l'elemento quadro
--                        FOR kk IN 1..TbDatoElemQuadro.COUNT LOOP
--                                -- recupero i dati dell'elemento quadro vecchio
--                                SELECT *
--                                INTO tbElemQuadroOld
--                                FROM nembo_D_ELEMENTO_QUADRO
--                                WHERE ID_ELEMENTO_QUADRO = TbDatoElemQuadro(kk).ID_ELEMENTO_QUADRO;
--                                -- recupero l'elemento quadro nuovo
--                                BEGIN
--                                        SELECT *
--                                        INTO tbElemQuadroNew
--                                        FROM nembo_D_ELEMENTO_QUADRO
--                                        WHERE ID_QUADRO = TbQuadriDinamici(jj).ID_QUADRO
--                                        AND CODICE_ELEM_PADRE = tbElemQuadroOld.CODICE;
--                                EXCEPTION
--                                        WHEN NO_DATA_FOUND THEN
--                                                tbElemQuadroNew.ID_ELEMENTO_QUADRO:=NULL;
--                                        WHEN TOO_MANY_ROWS THEN
--                                                pMessaggio := 'Quadri dinamici non configurati correttamente (ID_QUADRO:'||TO_CHAR(TbQuadriDinamici(jj).ID_QUADRO)||'/CODICE_ELEM_PADRE:'||tbElemQuadroOld.CODICE||')';
--                                                RAISE ERRORE;
--                                END;
--                                IF tbElemQuadroNew.ID_ELEMENTO_QUADRO IS NOT NULL THEN
--                                        INSERT INTO nembo_T_DATO_ELEMENTO_QUADRO(ID_DATO_ELEMENTO_QUADRO, VALORE_ELEMENTO, ID_ELEMENTO_QUADRO, ID_DATI_COMUNI_ELEM_QUADRO)
--                                        VALUES(SEQ_NEMBO_T_DATO_ELEMENTO_QUAD.NEXTVAL, TbDatoElemQuadro(kk).VALORE_ELEMENTO, tbElemQuadroNew.ID_ELEMENTO_QUADRO, nIdDatiComuniElemQuadro);
--                                END IF;
--                        END LOOP;
--                END LOOP;
--        END IF;
--  END LOOP;

  IF vTipoOggetto = 'ISAMB' THEN
        nIdDatiProcedimentoLastObj:=PCK_NEMBO_UTILITY.ReturnLastObj(nIdProcedimento,'DA');
  ELSIF vTipoOggetto = 'ISAMM' THEN
        nIdDatiProcedimentoLastObj:=PCK_NEMBO_UTILITY.ReturnLastObj(nIdProcedimento,'ISAMB');
        IF nIdDatiProcedimentoLastObj IS NULL THEN
                nIdDatiProcedimentoLastObj:=PCK_NEMBO_UTILITY.ReturnLastObj(nIdProcedimento,'DA');
        END IF;
  ELSE
        nIdDatiProcedimentoLastObj:=NULL;
  END IF;

  -- verifico la presenza del quadro superfici
  --IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'SUPCO') OR PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'COLAZ')) THEN
    -- verifico la presenza del quadro danni
  IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'DANNI')) THEN
          IF bFlagIstanzaBenef THEN
                  IF nIdDichConsistenza IS NOT NULL THEN
                        FOR rec IN (SELECT COMUNE, ID_UTILIZZO/*, ID_TITOLO_POSSESSO, ID_STORICO_PARTICELLA*/, SUM(SUPERFICIE_UTILIZZATA) SUM_SUP_UTIL
                                          FROM SMRGAA_V_CONDUZIONE_UTILIZZO VCU
                                          WHERE ID_DICHIARAZIONE_CONSISTENZA = nIdDichConsistenza
                                          AND ID_TITOLO_POSSESSO <> 5
                                          GROUP BY COMUNE, ID_UTILIZZO--, ID_TITOLO_POSSESSO, ID_STORICO_PARTICELLA
                        ) LOOP
                                -- ricerco i valori mancanti
                                IF NOT RendiValoriMediSuperf(rec.ID_UTILIZZO, rec.COMUNE, nProdHAMedia, nPrezzoMedio, nGGLavMedie, pRisultato, pMessaggio) THEN
                                            RAISE ERR_VAL_MEDI_S;
                                END IF;
                                -- inserisco
                                INSERT INTO NEMBO_T_SUPERFICIE_COLTURA
                                (ID_SUPERFICIE_COLTURA, ID_PROCEDIMENTO_OGGETTO, EXT_ID_DICHIARAZIONE_CONSISTEN, EXT_ISTAT_COMUNE, EXT_ID_UTILIZZO, PRODUZIONE_HA, GIORNATE_LAVORATE, QLI_REIMPIEGATI, PREZZO, RECORD_MODIFICATO, NOTE, COLTURA_SECONDARIA, PRODUZIONE_TOTALE_DANNO)
                                VALUES(SEQ_NEMBO_T_SUPERFICIE_COLTURA.NEXTVAL, nIdProcedimentoOggetto, nIdDichConsistenza, rec.COMUNE, rec.ID_UTILIZZO, nProdHAMedia, nGGLavMedie, NULL, nPrezzoMedio, 'N', NULL,  'N', NULL);
                                --RETURNING ID_SUPERFICIE_COLTURA INTO nIdSuperficieColtura;
                        END LOOP;
                  END IF;
          ELSE
                IF nIdDatiProcedimentoLastObj IS NOT NULL THEN
                        -- ribalto
                        INSERT INTO NEMBO_T_SUPERFICIE_COLTURA (ID_SUPERFICIE_COLTURA, ID_PROCEDIMENTO_OGGETTO, EXT_ID_DICHIARAZIONE_CONSISTEN, EXT_ISTAT_COMUNE, EXT_ID_UTILIZZO, PRODUZIONE_HA, GIORNATE_LAVORATE, QLI_REIMPIEGATI, UF_REIMPIEGATE, PREZZO, RECORD_MODIFICATO, NOTE, COLTURA_SECONDARIA, PRODUZIONE_TOTALE_DANNO, PERCENTUALE_DANNO, PREZZO_DANNEGGIATO)
                        SELECT SEQ_NEMBO_T_SUPERFICIE_COLTURA.NEXTVAL, nIdProcedimentoOggetto, EXT_ID_DICHIARAZIONE_CONSISTEN, EXT_ISTAT_COMUNE, EXT_ID_UTILIZZO, PRODUZIONE_HA, GIORNATE_LAVORATE, QLI_REIMPIEGATI, UF_REIMPIEGATE, PREZZO, RECORD_MODIFICATO, NOTE, COLTURA_SECONDARIA, PRODUZIONE_TOTALE_DANNO, PERCENTUALE_DANNO, PREZZO_DANNEGGIATO
                        FROM NEMBO_T_SUPERFICIE_COLTURA
                        WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj);
                END IF;
          END IF;
  END IF;

  -- verifico la presenza del quadro allevamenti
  --IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'ALLEV')) THEN
    -- verifico la presenza del quadro danni
  IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'DANNI')) THEN
          IF bFlagIstanzaBenef THEN
                  IF nIdDichConsistenza IS NOT NULL THEN
                        FOR rec IN (SELECT VALLEV.ISTAT_COMUNE, VALLEV.ID_CATEGORIA_ANIMALE, NCATANIM.PESO_VIVO_MEDIO, NCATANIM.GIORNATE_LAVORATIVE_MEDIE
                                          FROM SMRGAA_V_ALLEVAMENTI VALLEV, NEMBO_D_CATEGORIA_ANIMALE NCATANIM
                                          WHERE VALLEV.ID_DICHIARAZIONE_CONSISTENZA = nIdDichConsistenza
                                          AND VALLEV.ID_CATEGORIA_ANIMALE =  NCATANIM.EXT_ID_CATEGORIA_ANIMALE
                                          GROUP BY VALLEV.ISTAT_COMUNE, VALLEV.ID_CATEGORIA_ANIMALE, NCATANIM.PESO_VIVO_MEDIO, NCATANIM.GIORNATE_LAVORATIVE_MEDIE
                        ) LOOP
                                -- inserisco
                                INSERT INTO NEMBO_T_PRODUZIONE_ZOOTECNICA
                                (ID_PRODUZIONE_ZOOTECNICA, ID_PROCEDIMENTO_OGGETTO, EXT_ID_CATEGORIA_ANIMALE, PESO_VIVO_MEDIO, GIORNATE_LAVORATIVE_MEDIE, EXT_ISTAT_COMUNE, DATA_ULTIMO_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO)
                                VALUES(SEQ_NEMBO_T_PRODUZIONE_ZOOTECN.NEXTVAL, nIdProcedimentoOggetto, rec.ID_CATEGORIA_ANIMALE, rec.PESO_VIVO_MEDIO, rec.GIORNATE_LAVORATIVE_MEDIE, rec.ISTAT_COMUNE, NULL, NULL);
                        END LOOP;
                  END IF;
          ELSE
                IF nIdDatiProcedimentoLastObj IS NOT NULL THEN
                        -- ribalto
                        /*INSERT INTO NEMBO_T_PRODUZIONE_ZOOTECNICA (ID_PRODUZIONE_ZOOTECNICA, ID_PROCEDIMENTO_OGGETTO, EXT_ID_CATEGORIA_ANIMALE, PESO_VIVO_MEDIO, GIORNATE_LAVORATIVE_MEDIE, NOTE, EXT_ISTAT_COMUNE, DATA_ULTIMO_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO)
                        SELECT SEQ_NEMBO_T_PRODUZIONE_ZOOTECN.NEXTVAL, nIdProcedimentoOggetto, EXT_ID_CATEGORIA_ANIMALE, PESO_VIVO_MEDIO, GIORNATE_LAVORATIVE_MEDIE, NOTE, EXT_ISTAT_COMUNE, DATA_ULTIMO_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO
                        FROM NEMBO_T_PRODUZIONE_ZOOTECNICA
                        WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj);*/
                        FOR recPZ IN (SELECT *
                                             FROM NEMBO_T_PRODUZIONE_ZOOTECNICA
                                             WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj)) LOOP
                                    INSERT INTO NEMBO_T_PRODUZIONE_ZOOTECNICA (ID_PRODUZIONE_ZOOTECNICA, ID_PROCEDIMENTO_OGGETTO, EXT_ID_CATEGORIA_ANIMALE, PESO_VIVO_MEDIO, GIORNATE_LAVORATIVE_MEDIE, NOTE, EXT_ISTAT_COMUNE, DATA_ULTIMO_AGGIORNAMENTO, EXT_ID_UTENTE_AGGIORNAMENTO)
                                    VALUES (SEQ_NEMBO_T_PRODUZIONE_ZOOTECN.NEXTVAL, nIdProcedimentoOggetto, recPZ.EXT_ID_CATEGORIA_ANIMALE, recPZ.PESO_VIVO_MEDIO, recPZ.GIORNATE_LAVORATIVE_MEDIE, recPZ.NOTE, recPZ.EXT_ISTAT_COMUNE, recPZ.DATA_ULTIMO_AGGIORNAMENTO, recPZ.EXT_ID_UTENTE_AGGIORNAMENTO)
                                    RETURNING ID_PRODUZIONE_ZOOTECNICA INTO nIdProduzioneZootecnica;
                                    INSERT INTO NEMBO_T_PRODUZIONE_VENDIBILE (ID_PRODUZIONE_VENDIBILE, ID_PRODUZIONE, NUMERO_CAPI, QUANTITA_PRODOTTA, QUANTITA_REIMPIEGATA, PREZZO, ID_PRODUZIONE_ZOOTECNICA)
                                    SELECT SEQ_NEMBO_T_PRODUZIONE_VENDIBI.nextval, ID_PRODUZIONE, NUMERO_CAPI, QUANTITA_PRODOTTA, QUANTITA_REIMPIEGATA, PREZZO, nIdProduzioneZootecnica
                                    FROM NEMBO_T_PRODUZIONE_VENDIBILE WHERE ID_PRODUZIONE_ZOOTECNICA = recPZ.ID_PRODUZIONE_ZOOTECNICA;
                        END LOOP;
                END IF;
          END IF;
  END IF;

    -- verifico la presenza del quadro scorte
  --IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'SCORT')) THEN
    -- verifico la presenza del quadro danni
  IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'DANNI')) THEN
          IF NOT bFlagIstanzaBenef THEN
                IF nIdDatiProcedimentoLastObj IS NOT NULL THEN
                        -- ribalto
                        INSERT INTO NEMBO_T_SCORTA_MAGAZZINO (ID_SCORTA_MAGAZZINO, ID_PROCEDIMENTO_OGGETTO, ID_SCORTA, DESCRIZIONE, QUANTITA, ID_UNITA_MISURA)
                        SELECT SEQ_NEMBO_T_SCORTA_MAGAZZINO.NEXTVAL, nIdProcedimentoOggetto, ID_SCORTA, DESCRIZIONE, QUANTITA, ID_UNITA_MISURA
                        FROM NEMBO_T_SCORTA_MAGAZZINO
                        WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj);
                END IF;
          END IF;
  END IF;

    -- verifico la presenza del quadro danni
  IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'DANNI')) THEN
          IF NOT bFlagIstanzaBenef THEN
                IF nIdDatiProcedimentoLastObj IS NOT NULL THEN
                        -- ribalto
                        FOR recDannoATM IN (SELECT *
                                                         FROM NEMBO_T_DANNO_ATM
                                                         WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj)) LOOP
                                    INSERT INTO NEMBO_T_DANNO_ATM (ID_DANNO_ATM, ID_DANNO, EXT_ID_ENTITA_DANNEGGIATA, ID_PROCEDIMENTO_OGGETTO, PROGRESSIVO, DESCRIZIONE, QUANTITA, IMPORTO, DATA_CANCELLAZIONE, EXT_ID_UTENTE_CANCELLAZIONE, ID_UNITA_MISURA)
                                    VALUES (SEQ_NEMBO_T_DANNO_ATM.NEXTVAL, recDannoATM.ID_DANNO, recDannoATM.EXT_ID_ENTITA_DANNEGGIATA, nIdProcedimentoOggetto, recDannoATM.PROGRESSIVO, recDannoATM.DESCRIZIONE, recDannoATM.QUANTITA, recDannoATM.IMPORTO, recDannoATM.DATA_CANCELLAZIONE, recDannoATM.EXT_ID_UTENTE_CANCELLAZIONE, recDannoATM.ID_UNITA_MISURA)
                                    RETURNING  ID_DANNO_ATM INTO nIdDannoATM;
                                    INSERT INTO NEMBO_R_PARTICELLA_DANNEGGIATA (ID_DANNO_ATM,EXT_ID_UTILIZZO_DICHIARATO)
                                    SELECT nIdDannoATM, EXT_ID_UTILIZZO_DICHIARATO
                                    FROM  NEMBO_R_PARTICELLA_DANNEGGIATA WHERE ID_DANNO_ATM = recDannoATM.ID_DANNO_ATM;
                        END LOOP;
                END IF;
          END IF;
          -- se c'e' anche il quadro interventi ne ribalto un'altra
          --IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'INTER')) THEN
                  IF NOT bFlagIstanzaBenef THEN
                        IF nIdDatiProcedimentoLastObj IS NOT NULL THEN
                                -- ribalto
                                INSERT INTO NEMBO_R_DANNO_ATM_INTERVENTO (ID_INTERVENTO, ID_PROCEDIMENTO_OGGETTO, PROGRESSIVO)
                                SELECT ID_INTERVENTO, nIdProcedimentoOggetto, PROGRESSIVO
                                FROM NEMBO_R_DANNO_ATM_INTERVENTO
                                WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj);
                        END IF;
                  END IF;
          --END IF;
  END IF;

    -- verifico la presenza del quadro prestiti agrari
  --IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'PRAGR')) THEN
    -- verifico la presenza del quadro danni
  IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'DANNI')) THEN
          IF NOT bFlagIstanzaBenef THEN
                IF nIdDatiProcedimentoLastObj IS NOT NULL THEN
                        -- ribalto
                        INSERT INTO NEMBO_T_PRESTITI_AGRARI (ID_PRESTITI_AGRARI, DATA_SCADENZA, FINALITA_PRESTITO, IMPORTO, ISTITUTO_EROGANTE, ID_PROCEDIMENTO_OGGETTO)
                        SELECT SEQ_NEMBO_T_PRESTITI_AGRARI.NEXTVAL, DATA_SCADENZA, FINALITA_PRESTITO, IMPORTO, ISTITUTO_EROGANTE, nIdProcedimentoOggetto
                        FROM NEMBO_T_PRESTITI_AGRARI
                        WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj);
                END IF;
          END IF;
  END IF;

    -- verifico la presenza del quadro assicurazioni colture
  --IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'ASSIC')) THEN
    -- verifico la presenza del quadro danni
  IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'DANNI')) THEN
          IF NOT bFlagIstanzaBenef THEN
                IF nIdDatiProcedimentoLastObj IS NOT NULL THEN
                        -- ribalto
                        INSERT INTO NEMBO_T_ASSICURAZIONI_COLTURE (ID_ASSICURAZIONI_COLTURE, ID_PROCEDIMENTO_OGGETTO, ID_CONSORZIO_DIFESA, NOME_ENTE_PRIVATO, NUMERO_SOCIO_POLIZZA, IMPORTO_PREMIO, IMPORTO_ASSICURATO, IMPORTO_RIMBORSO)
                        SELECT SEQ_NEMBO_T_ASSICURAZIONI_COLT.NEXTVAL, nIdProcedimentoOggetto, ID_CONSORZIO_DIFESA, NOME_ENTE_PRIVATO, NUMERO_SOCIO_POLIZZA, IMPORTO_PREMIO, IMPORTO_ASSICURATO, IMPORTO_RIMBORSO
                        FROM NEMBO_T_ASSICURAZIONI_COLTURE
                        WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO FROM NEMBO_T_DATI_PROCEDIMENTO WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj);
                END IF;
          END IF;
  END IF;

  -- verifico la presenza del quadro conto corrente
  IF PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'CC') THEN
    SELECT COUNT(*)
    INTO   nCont
    FROM   NEMBO_T_PROCEDIMENTO_CONTO_COR
    WHERE  ID_PROCEDIMENTO = nIdProcedimento
    AND    DATA_FINE       IS NULL;

    IF nCont = 0 THEN
      BEGIN
        SELECT DISTINCT FIRST_VALUE(ID_CONTO_CORRENTE) OVER (ORDER BY DATA_INIZIO_VALIDITA_CC DESC ROWS UNBOUNDED PRECEDING)
        INTO   nIdContoCorrente
        FROM   SMRGAA_V_CONTO_CORRENTE
        WHERE  ID_AZIENDA                   = pExtIdAzienda
        AND    DATA_FINE_VALIDITA_CC        IS NULL
        AND    DATA_ESTINZIONE_CC           IS NULL
        AND    DATA_FINE_VALIDITA_BANCA     IS NULL
        AND    DATA_FINE_VALIDITA_SPORTELLO IS NULL
        AND    FLAG_VALIDATO                = 'S';

        INSERT INTO NEMBO_W_PROCEDIMENTO_OGGETT_CC
        (ID_PROCEDIMENTO_OGGETTO, EXT_ID_CONTO_CORRENTE)
        VALUES
        (nIdProcedimentoOggetto,nIdContoCorrente);
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
      END;
    END IF;
  END IF;

  IF (PCK_nembo_UTILITY.PRESENZAQUADRO(pIdBando,pIdLegameGruppoOggetto,'DANNB')) THEN
          --se è il primo oggetto creato recupero i dati dal fascicolo, aggregati x comune
          IF nIdDatiProcedimentoLastObj IS NULL THEN
              IF nIdDichConsistenza IS NOT NULL THEN
                    BEGIN
                        SELECT DESCRIZIONE_EVENTO_DEFAULT,DATA_EVENTO_DEFAULT
                        INTO vDescDannoDefault,vDataDannoDefault 
                        FROM NEMBO_D_EVENTO_CALAMITOSO EC
                        JOIN NEMBO_D_BANDO BA USING(ID_EVENTO_CALAMITOSO)
                        WHERE ID_BANDO = pIdBando;
                    EXCEPTION WHEN NO_DATA_FOUND THEN
                        vDescDannoDefault := NULL;
                        vDataDannoDefault := NULL;
                    END;
                    
                    --caso A) sul bando non è stato specificato un default
                    IF vDescDannoDefault IS NULL AND vDataDannoDefault IS NULL THEN
                        INSERT INTO NEMBO_T_SEGNALAZIONE_DANNO(ID_SEGNALAZIONE_DANNO,ID_PROCEDIMENTO_OGGETTO)
                        VALUES(SEQ_NEMBO_T_SEGNALAZIONE_DANNO.NEXTVAL, nIdProcedimentoOggetto)
                        RETURNING ID_SEGNALAZIONE_DANNO INTO nIdSegnalazDanno;
                    --caso B) sul bando è stato specificato un default
                    ELSIF vDescDannoDefault IS NOT NULL AND vDataDannoDefault IS NOT NULL THEN
                        INSERT INTO NEMBO_T_SEGNALAZIONE_DANNO(ID_SEGNALAZIONE_DANNO,ID_PROCEDIMENTO_OGGETTO,
                                                               DESCRIZIONE_DANNO,DATA_DANNO,FLAG_VALORE_DEFAULT)
                        VALUES(SEQ_NEMBO_T_SEGNALAZIONE_DANNO.NEXTVAL, nIdProcedimentoOggetto,
                               vDescDannoDefault,vDataDannoDefault,'S'/*in questo modo il valore è protetto a video*/)
                        RETURNING ID_SEGNALAZIONE_DANNO INTO nIdSegnalazDanno;
                    --caso C) sul bando è stato specificato un default, ma non ci sono entrambi i valori (errore)
                    ELSE
                        RAISE ERR_DANNO_DEFAULT;
                    END IF;
                    
                    FOR rec IN (SELECT COMUNE, ID_UTILIZZO,FOGLIO,SUM(SUPERFICIE_UTILIZZATA) SUM_SUP_UTIL
                                FROM SMRGAA_V_CONDUZIONE_UTILIZZO VCU
                                WHERE ID_DICHIARAZIONE_CONSISTENZA = nIdDichConsistenza
                                AND   ID_TITOLO_POSSESSO           <> 5
                                GROUP BY COMUNE, ID_UTILIZZO,FOGLIO) LOOP
                        INSERT INTO NEMBO_T_DETTAGLIO_SEGNAL_DAN(ID_DETTAGLIO_SEGNAL_DAN,ID_SEGNALAZIONE_DANNO,
                                                                 COMUNE,FOGLIO,EXT_ID_UTILIZZO,SUPERFICIE_GRAFICA)
                        VALUES(SEQ_NEMBO_T_DETTAGLIO_SEGN_DAN.NEXTVAL, nIdSegnalazDanno,
                               rec.COMUNE,rec.FOGLIO,rec.ID_UTILIZZO,rec.SUM_SUP_UTIL);
                    END LOOP;
              END IF;
          ELSE
              -- se c'è già un oggetto precedente, ribalto (qui sotto scorro i padri presi da last obj)
              FOR rec IN (SELECT ID_SEGNALAZIONE_DANNO,ID_PROCEDIMENTO_OGGETTO,DESCRIZIONE_DANNO,DATA_DANNO,FLAG_VALORE_DEFAULT
                          FROM NEMBO_T_SEGNALAZIONE_DANNO 
                          WHERE ID_PROCEDIMENTO_OGGETTO = (SELECT ID_PROCEDIMENTO_OGGETTO 
                                                           FROM NEMBO_T_DATI_PROCEDIMENTO 
                                                           WHERE ID_DATI_PROCEDIMENTO = nIdDatiProcedimentoLastObj)) LOOP
                  --nuovo padre
                  INSERT INTO NEMBO_T_SEGNALAZIONE_DANNO(ID_SEGNALAZIONE_DANNO,ID_PROCEDIMENTO_OGGETTO,DESCRIZIONE_DANNO,DATA_DANNO,FLAG_VALORE_DEFAULT)
                  VALUES(SEQ_NEMBO_T_SEGNALAZIONE_DANNO.NEXTVAL, nIdProcedimentoOggetto,rec.DESCRIZIONE_DANNO,rec.DATA_DANNO,rec.FLAG_VALORE_DEFAULT)
                  RETURNING ID_SEGNALAZIONE_DANNO INTO nIdSegnalazDanno;
                  
                  --figli del padre del last obj, ora copiati sul nuovo padre
                  INSERT INTO NEMBO_T_DETTAGLIO_SEGNAL_DAN(ID_DETTAGLIO_SEGNAL_DAN,ID_SEGNALAZIONE_DANNO,
                                                           COMUNE,FOGLIO,EXT_ID_UTILIZZO,SUPERFICIE_GRAFICA,
                                                           PERC_DANNO,IMPORTO_DANNO,NOTE_DANNO)
                  SELECT SEQ_NEMBO_T_DETTAGLIO_SEGN_DAN.NEXTVAL, nIdSegnalazDanno,
                         COMUNE,FOGLIO,EXT_ID_UTILIZZO,SUPERFICIE_GRAFICA,
                         PERC_DANNO,IMPORTO_DANNO,NOTE_DANNO
                  FROM NEMBO_T_DETTAGLIO_SEGNAL_DAN
                  WHERE ID_SEGNALAZIONE_DANNO =  rec.ID_SEGNALAZIONE_DANNO;
              END LOOP;
          END IF;
  END IF;
  
  RETURN nIdProcedimento;
EXCEPTION
  WHEN ERRORE_STATI THEN
    pRisultato := 1;
    RETURN NULL;
  WHEN ERR_STATO_GRUPPO_PREC THEN
    pRisultato := 1;
    pMessaggio :='Errore nel recupero dello stato precedente del gruppo';
    RETURN NULL;
  WHEN ERR_DANNO_DEFAULT THEN 
    pRisultato := 1;
    pMessaggio :='I valori della descrizione o della data default dell''eventi calamitoso risultano non configurati correttamente per il bando in oggetto. Si prega di contattare i servizi di assistenza.';
    RETURN NULL;
  WHEN ERRORE THEN
    pRisultato := 2;
    RETURN NULL;
  WHEN ERRORE_TIPOLOGIA THEN
    pRisultato := 2;
    pMessaggio := 'Quadro non compatibile con la tipologia del bando';
    RETURN NULL;
  WHEN  ERR_VAL_MEDI_S THEN
    RETURN NULL;
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nella creazione dell''oggetto. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
    RETURN NULL;
END MainCreazione;

-- funzione che recupera il rek giusto da una bulk collect di nembo_R_PROCEDIMENTO_LIVELLO
FUNCTION RecuperaDatiProcLivello(par_tbProcedimentoLivello        typProcedimentoLivello,
                                                    par_nIdLivello                          nembo_R_PROCEDIMENTO_LIVELLO.ID_LIVELLO%TYPE) RETURN  nembo_R_PROCEDIMENTO_LIVELLO%ROWTYPE IS

BEGIN

    FOR gugu IN 1..par_tbProcedimentoLivello.COUNT LOOP
        IF par_tbProcedimentoLivello(gugu).ID_LIVELLO = par_nIdLivello THEN
            RETURN(par_tbProcedimentoLivello(gugu));
        END IF;
    END LOOP;

    -- se non trovo niente
    RETURN(NULL);

END RecuperaDatiProcLivello;

-- chiusura dell'oggetto
PROCEDURE MainChiusura(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                       pIdEsito                    nembo_D_ESITO.ID_ESITO%TYPE,
                       pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                       pExtIdUtente                NUMBER,
                       pRisultato              OUT NUMBER,
                       pMessaggio              OUT VARCHAR2) IS

  nIdOperazione                          CONSTANT PLS_INTEGER:=2; -- chiusura

  nIdStatoNuovoProcedimento        nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoOggetto                NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoGruppo                 NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
  nIdEsitoNuovo                            nembo_D_ESITO.ID_ESITO%TYPE;
  nRisultato                                  NUMBER;
  vMessaggio                               VARCHAR2(250);

  ERRORE                                 EXCEPTION;
  ERR_CONTROLLI                     EXCEPTION;
  ERR_CRITERIO                        EXCEPTION;
  ERR_NO_RANGE                      EXCEPTION;
  ERR_RANGE_ESAURITO            EXCEPTION;
  ERR_NO_CHECKDIGIT                 EXCEPTION;
  ERR_INTERV_INS                    EXCEPTION;
  ERR_INTERV_UPD_DEL            EXCEPTION;
  ERR_ESITO                             EXCEPTION;
  ERR_ANTICIPO                       EXCEPTION;
  ERR_IMPEGNO_PROC_OGG      EXCEPTION;
  ERRORE_STATI                      EXCEPTION;
  ERR_STATO_GRUPPO_PREC             EXCEPTION;

  TbDettIntervPO  typTbDettIntervPO;

  dDataFine  nembo_T_PROCEDIMENTO_OGGETTO.DATA_FINE%TYPE;
  nCont            SIMPLE_INTEGER := 0;
  nIdProcedimento  nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE;
  dDataInizio      nembo_T_PROCEDIMENTO_OGGETTO.DATA_INIZIO%TYPE;
  nIdLegameGruppoOggetto    nembo_T_PROCEDIMENTO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;
  nIdBando                            nembo_T_PROCEDIMENTO.ID_BANDO%TYPE;
  vIdentificativoProc                nembo_T_PROCEDIMENTO.IDENTIFICATIVO%TYPE; -- identificativo corrente del procedimento
  vIdentificativo                       nembo_T_PROCEDIMENTO_OGGETTO.IDENTIFICATIVO%TYPE;
  vCodAttore                           nembo_T_PROCEDIMENTO_OGGETTO.EXT_COD_ATTORE%TYPE;
  nCodiceRaggruppamento         nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;
  vCodiceOggetto                    nembo_D_OGGETTO.CODICE%TYPE;
  vCriterioNumIdent                 nembo_D_OGGETTO.CRITERIO_NUM_IDENTIFICATIVO%TYPE;
  vTipoPagamento                    nembo_D_OGGETTO.TIPO_PAGAMENTO_SIGOP%TYPE;

  nIdAzienda               nembo_T_PROCEDIMENTO_AZIENDA.EXT_ID_AZIENDA%TYPE;

  nIdIntervento              nembo_T_DETTAGLIO_INTERVENTO.ID_INTERVENTO%TYPE;

  nIdDettaglioInterv        nembo_T_DETTAGLIO_INTERVENTO.ID_DETTAGLIO_INTERVENTO%TYPE;
  nProgressivo               nembo_T_DETTAGLIO_INTERVENTO.PROGRESSIVO%TYPE;

  nIdDettaglioIntervento    nembo_T_DETTAGLIO_INTERVENTO.ID_DETTAGLIO_INTERVENTO%TYPE;

  nIdRangeIdentificativo        nembo_D_BANDO.ID_RANGE_IDENTIFICATIVO%TYPE; -- puo' essere null

  vUltimo                               nembo_D_RANGE_IDENTIFICATIVO.ULTIMO_NUMERO%TYPE;
  vRangeA                             nembo_D_RANGE_IDENTIFICATIVO.RANGE_A%TYPE;
  vNewNumber                       nembo_D_RANGE_IDENTIFICATIVO.ULTIMO_NUMERO%TYPE;
  vCheckDigit                           VARCHAR2(1);

  bFlagIstanzaBenef                     BOOLEAN;

  nIdAnticipoCurr                       nembo_R_ANTICIPO_PROC_OGG.ID_ANTICIPO%TYPE;

  -- stati
  nIdStatoProcedimentoNow/*Attuale*/    nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;

  nIdStatoAnticipo                      nembo_T_ANTICIPO.ID_STATO_OGGETTO%TYPE;

  nIdDatiProcedimentoPunti          NEMBO_T_DATI_PROCEDIMENTO_PUNT.ID_DATI_PROCEDIMENTO_PUNTI%TYPE;

  bAggiornaLivelli                      BOOLEAN;
  vTipoLivello                          nembo_D_TIPO_LIVELLO.CODICE%TYPE;
  vCodiceEsito                         nembo_D_ESITO.CODICE%TYPE;

  -- dati pre-eliminazione
  TbProcedimentoLivelloOld           typProcedimentoLivello;
  recProcedimentoLivelloOld          nembo_R_PROCEDIMENTO_LIVELLO%ROWTYPE;

  TbRendicontazioneSpese        typRendicontazioneSpese;
  TbAccertamentoSpese           typAccertamentoSpese;
  TbProcedimentoLivello           typProcedimentoLivello;

  nTotImpContributo                   nembo_T_DETTAGLIO_INTERVENTO.IMPORTO_CONTRIBUTO%TYPE;

  -- importi liquidati
  nImpLiquidato                         nembo_T_IMPORTI_LIQUIDATI.IMPORTO_LIQUIDATO%TYPE;

  TbEsitoOperazione                     typEsitoOperazione;
  nImportoPremioCurr                   nembo_T_PREMIO_COMPLESSIVO.IMPORTO_PREMIO%TYPE;
  nImpLiqInPrecedenza                  nembo_T_IMPORTI_LIQUIDATI.IMPORTO_LIQUIDATO%TYPE;


  nIdLivelloCurr                            nembo_T_ESITO_OPERAZIONE.ID_LIVELLO%TYPE;
  vFlagIstanza     nembo_D_OGGETTO.FLAG_ISTANZA%TYPE;

  vCodiceGruppoOggetto         nembo_D_GRUPPO_OGGETTO.CODICE%TYPE;
  bFlagProcedi                 BOOLEAN;

BEGIN
  pRisultato := 0;
  pMessaggio := '';

  -- recupero dati utili
  SELECT PO.DATA_FINE, PO.ID_PROCEDIMENTO, PO.ID_LEGAME_GRUPPO_OGGETTO, PP.ID_BANDO, PP.IDENTIFICATIVO, BB.ID_RANGE_IDENTIFICATIVO,
              PO.EXT_COD_ATTORE, PP.ID_STATO_OGGETTO, OO.CODICE, OO.CRITERIO_NUM_IDENTIFICATIVO, TL.CODICE, OO.TIPO_PAGAMENTO_SIGOP,
              PROCAZ.EXT_ID_AZIENDA, PO.CODICE_RAGGRUPPAMENTO, OO.FLAG_ISTANZA, GGO.CODICE
  INTO dDataFine, nIdProcedimento, nIdLegameGruppoOggetto, nIdBando, vIdentificativoProc, nIdRangeIdentificativo, vCodAttore, nIdStatoProcedimentoNow,
          vCodiceOggetto, vCriterioNumIdent, vTipoLivello, vTipoPagamento, nIdAzienda, nCodiceRaggruppamento, vFlagIstanza, vCodiceGruppoOggetto
  FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_T_PROCEDIMENTO PP, nembo_D_BANDO BB, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO, nembo_D_TIPO_LIVELLO TL, nembo_T_PROCEDIMENTO_AZIENDA PROCAZ, nembo_D_GRUPPO_OGGETTO GGO
  WHERE PO.ID_PROCEDIMENTO = PP.ID_PROCEDIMENTO
  AND PO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
  AND PP.ID_BANDO = BB.ID_BANDO
  AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
  AND LGO.ID_OGGETTO = OO.ID_OGGETTO
  AND BB.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO
  AND PP.ID_PROCEDIMENTO = PROCAZ.ID_PROCEDIMENTO
  AND PROCAZ.DATA_FINE IS NULL
  AND LGO.ID_GRUPPO_OGGETTO = GGO.ID_GRUPPO_OGGETTO;

  -- se e' gia' chiuso do errore
  IF dDataFine IS NOT NULL THEN
    RAISE ERRORE;
  END IF;

  SELECT COUNT(*)
  INTO nCont
  FROM nembo_T_ESECUZIONE_CONTROLLI
  WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
  AND FLAG_ELABORAZIONE_IN_CORSO = 'S';

  -- se i controlli sono andati nella Libera Repubblica di Bananas, do errore
  IF nCont > 0 THEN
    RAISE ERR_CONTROLLI;
  END IF;

  bAggiornaLivelli:=FALSE;
  IF PCK_nembo_UTILITY.ReturnIstanzaCreataDaBenef(nIdLegameGruppoOggetto) THEN
        bFlagIstanzaBenef:=TRUE;
        -- se l'istanza e' stata creata da beneficiario, procedo alla verifica sul CriterioNumIdent:
        IF vCriterioNumIdent IS NULL THEN
                RAISE ERR_CRITERIO;
        ELSE
            IF vCriterioNumIdent='**' THEN
                --calcolo dell'identificativo
                -- dapprima verifico che esista il record di range legato al bando
                IF nIdRangeIdentificativo IS NULL THEN
                        RAISE ERR_NO_RANGE;
                END IF;
                -- blocco per aggiornare
                SELECT RI.ULTIMO_NUMERO, RI.RANGE_A
                INTO vUltimo, vRangeA
                FROM nembo_D_RANGE_IDENTIFICATIVO RI
                WHERE RI.ID_RANGE_IDENTIFICATIVO = nIdRangeIdentificativo
                FOR UPDATE;
                vNewNumber:= TRIM(TO_CHAR(TO_NUMBER(vUltimo) + 1,'0000000000'));
                IF vNewNumber > vRangeA THEN
                        RAISE ERR_RANGE_ESAURITO;
                ELSE
                        -- aggiorno il range
                        UPDATE nembo_D_RANGE_IDENTIFICATIVO SET ULTIMO_NUMERO=vNewNumber
                        WHERE ID_RANGE_IDENTIFICATIVO = nIdRangeIdentificativo;
                        -- recupero il check digit
                        vCheckDigit:=PCK_nembo_UTILITY.ReturnCheckDigit(vNewNumber);
                        IF vCheckDigit IS NOT NULL THEN
                            vIdentificativo:=vNewNumber||vCheckDigit;
                            -- aggiorno
                            UPDATE nembo_T_PROCEDIMENTO_OGGETTO SET IDENTIFICATIVO = vIdentificativo WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
                        ELSE
                            RAISE ERR_NO_CHECKDIGIT;
                        END IF;
                END IF;
            ELSIF  vCriterioNumIdent='##' THEN
                -- aggiorno il procedimento oggetto con l'identificativo del procedimento
                UPDATE nembo_T_PROCEDIMENTO_OGGETTO SET IDENTIFICATIVO = vIdentificativoProc WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
            ELSE
                -- conto il numero di oggetti dello stesso tipo di quello chiudaturo
                SELECT COUNT(*)
                INTO nCont
                FROM nembo_T_PROCEDIMENTO_OGGETTO POX, nembo_R_LEGAME_GRUPPO_OGGETTO LGOX, nembo_D_OGGETTO OOX
                WHERE POX.ID_PROCEDIMENTO = nIdProcedimento
                AND POX.ID_LEGAME_GRUPPO_OGGETTO = LGOX.ID_LEGAME_GRUPPO_OGGETTO
                AND LGOX.ID_OGGETTO = OOX.ID_OGGETTO
                AND OOX.CODICE = vCodiceOggetto;
                -- aggiorno il procedimento oggetto con l'identificativo del procedimento a cui concateno alcune info
                UPDATE nembo_T_PROCEDIMENTO_OGGETTO SET IDENTIFICATIVO = vIdentificativoProc||'-'||vCriterioNumIdent||'-'||LPAD(TO_CHAR(nCont),2,'0')
                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
            END IF;
            -- verifico se devo aggiornare i livelli corrispondenti al procedimento
            IF nIdStatoProcedimentoNow <=10 AND vCriterioNumIdent IN ('**' ,'##') THEN
                bAggiornaLivelli:=TRUE;
            END IF;
        END IF;

  ELSE
        bFlagIstanzaBenef:=FALSE;
        IF vTipoLivello IN ('I','G') THEN
            bAggiornaLivelli:=TRUE;
        END IF;
  END IF;

   PCK_nembo_GESTIONE_PROC_OGGETT.MAINGESTIONESTATI ( nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nIdOperazione, pIdEsito, vCodAttore, nIdBando, nCodiceRaggruppamento, nIdStatoNuovoProcedimento, nIdStatoNuovoOggetto, nIdStatoNuovoGruppo, nIdEsitoNuovo, nRisultato, vMessaggio );
   IF nRisultato <> 0 THEN
                    -- lancio eccezione
                    pMessaggio:='Errore durante il richiamo della procedura MainGestioneStati: '||vMessaggio;
                    RAISE ERRORE_STATI;
   ELSE
                    -- aggiorno
                    IF NVL(nIdStatoNuovoProcedimento,-10) <> 0 THEN
                                -- a) stato del procedimento
                                UPDATE nembo_T_PROCEDIMENTO SET ID_STATO_OGGETTO = nIdStatoNuovoProcedimento,
                                                                                EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                                                                DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE
                                WHERE ID_PROCEDIMENTO = nIdProcedimento;
                    END IF;
                    IF NVL(nIdStatoNuovoOggetto,-10) <> 0 THEN
                                -- b) stato dell'oggetto
                                -- chiusura vecchio iter
                                UPDATE NEMBO_T_ITER_PROCEDIMENTO_OGGE SET DATA_FINE = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                AND DATA_FINE IS NULL;
                                -- apertura nuovo iter
                                INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_OGGE
                                (ID_ITER_PROCEDIMENTO_OGGETT, ID_PROCEDIMENTO_OGGETTO, ID_STATO_OGGETTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                VALUES
                                (SEQ_NEMBO_T_ITER_PROCEDIMEN_OG.NEXTVAL,pIdProcedimentoOggetto,nIdStatoNuovoOggetto,SYSDATE,NULL,pExtIdUtente,pNote);
                    END IF;
                    IF NVL(nIdEsitoNuovo,-10) <> 0 THEN
                                -- c) esito
                                UPDATE nembo_T_PROCEDIMENTO_OGGETTO
                                SET ID_ESITO = nIdEsitoNuovo, DATA_FINE                   = SYSDATE,
                                       EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                       DATA_ULTIMO_AGGIORNAMENTO = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                RETURNING DATA_INIZIO, DATA_FINE INTO dDataInizio, dDataFine;
                    ELSE
                                UPDATE nembo_T_PROCEDIMENTO_OGGETTO
                                SET DATA_FINE                   = SYSDATE,
                                       EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                       DATA_ULTIMO_AGGIORNAMENTO = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                RETURNING DATA_INIZIO INTO dDataInizio;
                    END IF;
                    IF NVL(nIdStatoNuovoGruppo,-10) <> 0 THEN
                                -- d) stato del gruppo
                                IF NVL(nIdStatoNuovoGruppo,-10) = -1 THEN
                                        IF NOT RiportaStatoPrecIterGruppo(nIdProcedimento,nCodiceRaggruppamento) THEN
                                                RAISE ERR_STATO_GRUPPO_PREC;
                                        END IF;
                                ELSE
                                        -- chiusura vecchio iter
                                        UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP SET DATA_FINE = SYSDATE
                                        WHERE ID_PROCEDIMENTO = nIdProcedimento
                                        AND DATA_FINE IS NULL
                                        AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                        -- apertura nuovo iter
                                        INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                        (ID_ITER_PROCEDIMENTO_GRUPPO, ID_PROCEDIMENTO, ID_STATO_OGGETTO, CODICE_RAGGRUPPAMENTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                        VALUES
                                        (SEQ_NEMBO_T_ITER_PROCEDIMEN_GR.NEXTVAL,nIdProcedimento,nIdStatoNuovoGruppo,nCodiceRaggruppamento,SYSDATE,NULL,pExtIdUtente,NULL/*pNoteGruppo*/); -- ##
                                END IF;
                    END IF;
   END IF;

   IF nIdEsitoNuovo IS NOT NULL THEN
        BEGIN
            SELECT CODICE
            INTO vCodiceEsito
            FROM nembo_D_ESITO
            WHERE ID_ESITO = nIdEsitoNuovo;
        EXCEPTION
            WHEN OTHERS THEN
                vCodiceEsito:='-';
        END;
   ELSE
        vCodiceEsito:='-';
   END IF;

-- processo l'inserimento di taluni oggetti
-- cc
  SELECT COUNT(*)
  INTO   nCont
  FROM   NEMBO_W_PROCEDIMENTO_OGGETT_CC
  WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;

  IF nCont != 0 THEN
    UPDATE NEMBO_T_PROCEDIMENTO_CONTO_COR
    SET    DATA_FINE       = dDataInizio
    WHERE  ID_PROCEDIMENTO = nIdProcedimento
    AND    DATA_FINE       IS NULL;

    INSERT INTO NEMBO_T_PROCEDIMENTO_CONTO_COR
    (ID_PROCEDIMENTO_CONTO_CORR, EXT_ID_CONTO_CORRENTE, ID_PROCEDIMENTO, DATA_INIZIO)
    (SELECT SEQ_NEMBO_T_PROCEDIME_CONTO_CO.NEXTVAL,EXT_ID_CONTO_CORRENTE,nIdProcedimento,dDataInizio
     FROM   NEMBO_W_PROCEDIMENTO_OGGETT_CC
     WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto);

    DELETE NEMBO_W_PROCEDIMENTO_OGGETT_CC
    WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
  END IF;

-- interventi
  bFlagProcedi:=TRUE;
  IF bFlagProcedi   THEN
          SELECT *
          BULK COLLECT INTO TbDettIntervPO
          FROM nembo_W_DETT_INTERV_PROC_OGG
          WHERE ID_PROCEDIMENTO_OGGETTO =  pIdProcedimentoOggetto
          ORDER BY ID_DETT_INTERV_PROC_OGG;

          FOR i IN 1..TbDettIntervPO.COUNT LOOP

            nIdIntervento:=TbDettIntervPO(i).ID_INTERVENTO;
            -- a seconda del tipo operazione devo fare cose diverse
            IF TbDettIntervPO(i).FLAG_TIPO_OPERAZIONE IN ('D', 'U', 'E') THEN
                    -- recupero ll dettaglio intervento aperto con il relativo progressivo
                    BEGIN
                        SELECT DI.ID_DETTAGLIO_INTERVENTO, DI.PROGRESSIVO
                        INTO nIdDettaglioInterv, nProgressivo
                        FROM nembo_T_DETTAGLIO_INTERVENTO DI
                        WHERE DI.ID_INTERVENTO = nIdIntervento
                        AND DATA_FINE IS NULL;
                    EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                            RAISE ERR_INTERV_UPD_DEL;
                    END;
           ELSIF TbDettIntervPO(i).FLAG_TIPO_OPERAZIONE = 'I' THEN
                SELECT COUNT(*)
                INTO nCont
                FROM nembo_T_DETTAGLIO_INTERVENTO DI
                WHERE DI.ID_PROCEDIMENTO_OGGETTO =  pIdProcedimentoOggetto
                AND DI.ID_INTERVENTO = nIdIntervento;
                IF nCont > 0 THEN
                    RAISE ERR_INTERV_INS;
                END IF;
                -- calcolo il nuovo progressivo nProgressivo
                SELECT NVL(MAX(PROGRESSIVO),0)+1
                INTO nProgressivo
                FROM nembo_T_DETTAGLIO_INTERVENTO DI, nembo_T_INTERVENTO II
                WHERE DI.ID_INTERVENTO=II.ID_INTERVENTO
                AND II.ID_PROCEDIMENTO = nIdProcedimento;
           END IF;
        --
           IF TbDettIntervPO(i).FLAG_TIPO_OPERAZIONE IN ('D', 'U', 'E') THEN
                -- chiudo il record imponendo come data fine la data inizio del procedimento oggetto
                UPDATE nembo_T_DETTAGLIO_INTERVENTO
                SET DATA_FINE = dDataInizio
                WHERE ID_DETTAGLIO_INTERVENTO = nIdDettaglioInterv;
           END IF;

            -- inserisco il nuovo record; il progressivo potrebbe essere nuovo oppure del rek che ho chiuso, a seconda dell'operaione, la data fine e' valorizzata solo per la delete
            INSERT INTO nembo_T_DETTAGLIO_INTERVENTO
            (ID_DETTAGLIO_INTERVENTO, ID_INTERVENTO, ID_PROCEDIMENTO_OGGETTO, IMPORTO_INVESTIMENTO, IMPORTO_AMMESSO, PERCENTUALE_CONTRIBUTO, IMPORTO_CONTRIBUTO,
             DATA_INIZIO, DATA_FINE, PROGRESSIVO, ULTERIORI_INFORMAZIONI, IMPORTO_UNITARIO, FLAG_ASSOCIATO_ALTRA_MISURA, FLAG_TIPO_OPERAZIONE, CUAA_BENEFICIARIO, EXT_ID_AZIENDA_BENEFICIARIO,  FLAG_CONDOTTA, FLAG_CANALE, FLAG_OPERA_PRESA)
            VALUES
            (SEQ_NEMBO_T_DETTAGLIO_INTERVEN.NEXTVAL, nIdIntervento, TbDettIntervPO(i).ID_PROCEDIMENTO_OGGETTO,
             TbDettIntervPO(i).IMPORTO_INVESTIMENTO, TbDettIntervPO(i).IMPORTO_AMMESSO, TbDettIntervPO(i).PERCENTUALE_CONTRIBUTO,
             TbDettIntervPO(i).IMPORTO_CONTRIBUTO, dDataInizio, DECODE(TbDettIntervPO(i).FLAG_TIPO_OPERAZIONE,'D',dDataInizio,NULL), nProgressivo,
             TbDettIntervPO(i).ULTERIORI_INFORMAZIONI, TbDettIntervPO(i).IMPORTO_UNITARIO, TbDettIntervPO(i).FLAG_ASSOCIATO_ALTRA_MISURA, TbDettIntervPO(i).FLAG_TIPO_OPERAZIONE, TbDettIntervPO(i).CUAA_BENEFICIARIO, TbDettIntervPO(i).EXT_ID_AZIENDA_BENEFICIARIO,  TbDettIntervPO(i).FLAG_CONDOTTA, TbDettIntervPO(i).FLAG_CANALE, TbDettIntervPO(i).FLAG_OPERA_PRESA)
             RETURNING ID_DETTAGLIO_INTERVENTO INTO nIdDettaglioIntervento;

             -- tabella figlia numero 1
             INSERT INTO NEMBO_R_DETT_INTERV_MISURAZION
            (ID_DETTAGLIO_INTERVENTO, ID_MISURAZIONE_INTERVENTO, QUANTITA)
            (SELECT nIdDettaglioIntervento,  ID_MISURAZIONE_INTERVENTO, QUANTITA
             FROM NEMBO_R_DETT_INTE_PROC_OGG_MIS
             WHERE ID_DETT_INTERV_PROC_OGG = TbDettIntervPO(i).ID_DETT_INTERV_PROC_OGG);

             -- tabella figlia numero 2
             INSERT INTO nembo_T_LOCALIZZAZIONE_INTERV
            (ID_LOCALIZZAZIONE_INTERV, ID_DETTAGLIO_INTERVENTO, ISTAT_COMUNE, EXT_ID_PARTICELLA_CERTIFICATA, EXT_ID_CONDUZIONE_DICHIARATA, EXT_ID_UTILIZZO_DICHIARATO, SUPERFICIE_IMPEGNO, SUPERFICIE_EFFETTIVA, SUPERFICIE_ISTRUTTORIA, SUPERFICIE_ACCERTATA_GIS)
            (SELECT SEQ_NEMBO_T_LOCALIZZAZIONE_INT.NEXTVAL, nIdDettaglioIntervento,  ISTAT_COMUNE, EXT_ID_PARTICELLA_CERTIFICATA, EXT_ID_CONDUZIONE_DICHIARATA, EXT_ID_UTILIZZO_DICHIARATO, SUPERFICIE_IMPEGNO, SUPERFICIE_EFFETTIVA, SUPERFICIE_ISTRUTTORIA, SUPERFICIE_ACCERTATA_GIS
             FROM nembo_W_LOCAL_INTERV_PROC_OGG
             WHERE ID_DETT_INTERV_PROC_OGG = TbDettIntervPO(i).ID_DETT_INTERV_PROC_OGG);

             -- tabella figlia numero 3
             INSERT INTO NEMBO_R_FILE_ALLEGATI_DETT_INT
            (ID_DETTAGLIO_INTERVENTO, ID_FILE_ALLEGATI_INTERVENTO)
            (SELECT nIdDettaglioIntervento,  ID_FILE_ALLEGATI_INTERVENTO
             FROM NEMBO_W_FILE_ALL_INTE_PROC_OGG
             WHERE ID_DETT_INTERV_PROC_OGG = TbDettIntervPO(i).ID_DETT_INTERV_PROC_OGG);

             IF vCodiceGruppoOggetto<>'VAR' THEN
                     -- infine elimino i rek dalle tabelle wrk
                     DELETE
                     FROM NEMBO_R_DETT_INTE_PROC_OGG_MIS
                     WHERE ID_DETT_INTERV_PROC_OGG = TbDettIntervPO(i).ID_DETT_INTERV_PROC_OGG;

                     DELETE
                     FROM nembo_W_LOCAL_INTERV_PROC_OGG
                     WHERE ID_DETT_INTERV_PROC_OGG = TbDettIntervPO(i).ID_DETT_INTERV_PROC_OGG;

                     DELETE
                     FROM NEMBO_W_FILE_ALL_INTE_PROC_OGG
                     WHERE ID_DETT_INTERV_PROC_OGG = TbDettIntervPO(i).ID_DETT_INTERV_PROC_OGG;

                     DELETE
                     FROM nembo_W_DETT_INTERV_PROC_OGG
                     WHERE ID_DETT_INTERV_PROC_OGG = TbDettIntervPO(i).ID_DETT_INTERV_PROC_OGG;
                END IF;
          END LOOP;
  END IF;

--  -- rendicontazione e accertamento spese
--  SELECT *
--  BULK COLLECT INTO TbRendicontazioneSpese
--  FROM nembo_T_RENDICONTAZIONE_SPESE
--  WHERE ID_PROCEDIMENTO_OGGETTO =  pIdProcedimentoOggetto;
--
--  FOR j1 IN 1..TbRendicontazioneSpese.COUNT LOOP
--        UPDATE nembo_T_INTERVENTO SET FLAG_INTERVENTO_COMPLETATO = TbRendicontazioneSpese(j1).FLAG_INTERVENTO_COMPLETATO
--        WHERE ID_INTERVENTO = TbRendicontazioneSpese(j1).ID_INTERVENTO;
--  END LOOP;
--
--  SELECT *
--  BULK COLLECT INTO TbAccertamentoSpese
--  FROM nembo_T_ACCERTAMENTO_SPESE
--  WHERE ID_PROCEDIMENTO_OGGETTO =  pIdProcedimentoOggetto;
--
--  FOR j2 IN 1..TbAccertamentoSpese.COUNT LOOP
--        UPDATE nembo_T_INTERVENTO SET FLAG_INTERVENTO_COMPLETATO = TbAccertamentoSpese(j2).FLAG_INTERVENTO_COMPLETATO
--        WHERE ID_INTERVENTO = TbAccertamentoSpese(j2).ID_INTERVENTO;
--  END LOOP;

 -- operazioni propedeutiche stampa
  DELETE NEMBO_T_PROCEDIM_OGGETTO_STAMP
  WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
--
  FOR rec IN (SELECT BO.ID_BANDO_OGGETTO,OI.ID_OGGETTO_ICONA
              FROM   nembo_R_BANDO_OGGETTO BO,nembo_R_BANDO_OGGETTO_ICONA BOI,nembo_R_OGGETTO_ICONA OI,
                     nembo_T_PROCEDIMENTO_OGGETTO PO,nembo_T_PROCEDIMENTO P,nembo_D_ELENCO_CDU EC
              WHERE  PO.ID_PROCEDIMENTO_OGGETTO  = pIdProcedimentoOggetto
              AND    BO.ID_BANDO_OGGETTO         = BOI.ID_BANDO_OGGETTO
              AND    OI.ID_OGGETTO_ICONA         = BOI.ID_OGGETTO_ICONA
              AND    PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO
              AND    P.ID_BANDO                  = BO.ID_BANDO
              AND    BO.ID_LEGAME_GRUPPO_OGGETTO = PO.ID_LEGAME_GRUPPO_OGGETTO
              AND    OI.EXT_ID_TIPO_DOCUMENTO    IS NOT NULL
              AND    OI.ID_ELENCO_CDU            = EC.ID_ELENCO_CDU
              AND    EC.CODICE_CDU              != 'CU-NEMBO-126-I') LOOP

    INSERT INTO NEMBO_T_PROCEDIM_OGGETTO_STAMP
    (ID_PROCEDIM_OGGETTO_STAMPA, ID_PROCEDIMENTO_OGGETTO, ID_BANDO_OGGETTO,
     ID_OGGETTO_ICONA, EXT_ID_UTENTE_AGGIORNAMENTO, DATA_ULTIMO_AGGIORNAMENTO, DATA_INIZIO,ID_STATO_STAMPA)
    VALUES
    (SEQ_NEMBO_T_PROCEDIM_OGGET_STA.NEXTVAL,pIdProcedimentoOggetto,rec.ID_BANDO_OGGETTO,
     rec.ID_OGGETTO_ICONA,pExtIdUtente,SYSDATE,SYSDATE,1);
  END LOOP;


--     IF vCodiceEsito IN ('P') THEN
--              -- recupero i rek
--              SELECT *
--              BULK COLLECT INTO TbProcedimentoLivello
--              FROM nembo_R_PROCEDIMENTO_LIVELLO
--              WHERE ID_PROCEDIMENTO = nIdProcedimento
--              ORDER BY ID_LIVELLO;
--              FOR vv IN 1..TbProcedimentoLivello.COUNT LOOP
--                        IF NVL(vTipoPagamento,'-') IN ('ACCON','SALDO','ANTIC','INTEG') THEN
--                            IF vTipoPagamento IN ('ACCON','SALDO','INTEG') THEN
--                                -- recupero l'importo dall'accertamento spese
--                                   BEGIN
--                                           SELECT NVL(CONTRIBUTO_EROGABILE,0)
--                                           INTO nImpLiquidato
--                                           FROM NEMBO_R_PROCEDIMENTO_OGG_LIVEL
--                                           WHERE ID_PROCEDIMENTO_OGGETTO =  pIdProcedimentoOggetto
--                                           AND ID_LIVELLO = TbProcedimentoLivello(vv).ID_LIVELLO;
--                                    EXCEPTION
--                                           WHEN NO_DATA_FOUND THEN
--                                                    nImpLiquidato:=0;
--                                    END;
--                            ELSIF vTipoPagamento = 'ANTIC' THEN
--                                -- recupero l'importo dalla tabella ad hoc
--                                BEGIN
--                                    SELECT ANTCLIV.IMPORTO_ANTICIPO
--                                    INTO nImpLiquidato
--                                    FROM nembo_T_ANTICIPO ANTC, nembo_R_ANTICIPO_LIVELLO ANTCLIV
--                                    WHERE ANTC.ID_PROCEDIMENTO = nIdProcedimento
--                                    AND ANTC.ID_STATO_OGGETTO = PCK_nembo_UTILITY.cnIdStatoApprovato
--                                    AND ANTC.ID_ANTICIPO = ANTCLIV.ID_ANTICIPO
--                                    AND ANTCLIV.ID_LIVELLO = TbProcedimentoLivello(vv).ID_LIVELLO;
--                                EXCEPTION
--                                    WHEN NO_DATA_FOUND THEN
--                                            RAISE ERR_ANTICIPO;
--                                END;
--                            END IF;
--                            -- inserimento su nembo_T_IMPORTI_LIQUIDATI
--                            SELECT NVL(SUM(NVL(DI.IMPORTO_CONTRIBUTO,0)),0)
--                            INTO nTotImpContributo
--                            FROM nembo_T_DETTAGLIO_INTERVENTO DI, nembo_T_INTERVENTO II, nembo_D_DESCRIZIONE_INTERVENTO DESCINT, nembo_R_LIVELLO_INTERVENTO LI
--                            WHERE II.ID_PROCEDIMENTO =  nIdProcedimento
--                            AND DI.ID_INTERVENTO = II.ID_INTERVENTO
--                            AND II.ID_DESCRIZIONE_INTERVENTO = DESCINT.ID_DESCRIZIONE_INTERVENTO
--                            AND DESCINT.ID_DESCRIZIONE_INTERVENTO = LI.ID_DESCRIZIONE_INTERVENTO
--                            AND LI.ID_LIVELLO = TbProcedimentoLivello(vv).ID_LIVELLO
--                            AND DI.FLAG_TIPO_OPERAZIONE <> 'D'
--                            AND (SELECT NVL(DATA_FINE,SYSDATE) FROM nembo_T_PROCEDIMENTO_OGGETTO WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto) BETWEEN DI.DATA_INIZIO AND NVL(DI.DATA_FINE,SYSDATE);
--                            IF nImpLiquidato >= 12 THEN
--                                    INSERT INTO nembo_T_IMPORTI_LIQUIDATI(ID_IMPORTI_LIQUIDATI, ID_PROCEDIMENTO_OGGETTO, ID_LIVELLO, RAGGRUPPAMENTO, IMPORTO_COMPLESSIVO, IMPORTO_LIQUIDATO)
--                                    VALUES (SEQ_nembo_T_IMPORTI_LIQUIDATI.NEXTVAL, pIdProcedimentoOggetto, TbProcedimentoLivello(vv).ID_LIVELLO, NULL, nTotImpContributo, nImpLiquidato);
--                            END IF;
--                        END IF;
--              END LOOP;
--     END IF;

EXCEPTION
  WHEN ERRORE_STATI THEN
    pRisultato := 1;
   WHEN ERR_STATO_GRUPPO_PREC THEN
    pRisultato := 1;
    pMessaggio:='Errore nel recupero dello stato precedente del gruppo';
  WHEN ERRORE THEN
    pRisultato := 2;
    pMessaggio := 'L''oggetto selezionato risulta giè chiuso; impossibile procedere con l''operazione';
  WHEN ERR_CONTROLLI THEN
    pRisultato := 2;
    pMessaggio := 'L''esecuzione dei controlli non è andata a buon fine: si prega di rieseguirili prima di procedere con l''operazione';
  WHEN ERR_CRITERIO THEN
    pRisultato := 1;
    pMessaggio := 'Non è stato possibile assegnare l''identificativo perchè l''oggetto selezionato non è stato configurato correttamente; impossibile procedere con l''operazione';
  WHEN ERR_NO_RANGE THEN
    pRisultato := 1;
    pMessaggio := 'Non è stato possibile assegnare un numero identificativo all''oggetto perchè al bando non è stato associato alcun range';
  WHEN ERR_RANGE_ESAURITO THEN
    pRisultato := 1;
    pMessaggio := 'Non è stato possibile assegnare un numero identificativo all''oggetto perchè è stato superato il numero massimo assegnato al range';
  WHEN ERR_NO_CHECKDIGIT THEN
    pRisultato := 1;
    pMessaggio := 'Checkdigit non trovato (ID_RANGE_IDENTIFICATIVO:'||TO_CHAR(nIdRangeIdentificativo)||')';
  WHEN ERR_INTERV_INS THEN
    pRisultato := 1;
    pMessaggio := 'Errore durante la fase di inserimento intervento: intervento gia'' presente (ID_INTERVENTO:'||TO_CHAR(nIdIntervento)||')';
  WHEN ERR_INTERV_UPD_DEL THEN
    pRisultato := 1;
    pMessaggio := 'Errore durante la fase di aggiornamento/eliminazione intervento: intervento inesistente (ID_INTERVENTO:'||TO_CHAR(nIdIntervento)||')';
  WHEN ERR_ESITO THEN
    pRisultato := 1;
    pMessaggio := 'L''esito della chiusura dell''oggetto non è tra quelli previsti (Positivo/Negativo)';
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nella chiusura dell''oggetto. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
END MainChiusura;

PROCEDURE DeleteCascade(table_owner   VARCHAR2,
                        parent_table  VARCHAR2,
                        where_clause  VARCHAR2) IS
    /*   Example call:  execute delete_cascade('MY_SCHEMA', 'MY_MASTER', 'where ID=1'); */

  child_cons        VARCHAR2(30);
  parent_cons       VARCHAR2(30);
  child_table       VARCHAR2(30);
  child_cols        VARCHAR(500);
  parent_cols       VARCHAR(500);
  delete_command    VARCHAR(10000);
  new_where_clause  VARCHAR2(10000);

    /* gets the foreign key constraints on other tables which depend on columns in parent_table */
  CURSOR cons_cursor IS SELECT owner, constraint_name, r_constraint_name, table_name, delete_rule
                        FROM   user_constraints
                        WHERE  constraint_type   = 'R'
                        AND    delete_rule       = 'NO ACTION'
                        AND    r_constraint_name IN (SELECT constraint_name
                                                     FROM   user_constraints
                                                     WHERE  constraint_type IN ('P', 'U')
                                                     AND    table_name      = parent_table
                                                     AND    owner           = table_owner)
                        AND    NOT table_name    = parent_table; -- ignore self-referencing constraints


    /* for the current constraint, gets the child columns and corresponding parent columns */
  CURSOR columns_cursor IS SELECT cc1.column_name AS child_col, cc2.column_name AS parent_col
                           FROM   user_cons_columns cc1, user_cons_columns cc2
                           WHERE  cc1.constraint_name = child_cons
                           AND    cc1.table_name      = child_table
                           AND    cc2.constraint_name = parent_cons
                           AND    cc1.position        = cc2.position
                           ORDER BY cc1.position;
BEGIN
    /* loops through all the constraints which refer back to parent_table */
  FOR cons IN cons_cursor LOOP
    child_cons  := cons.constraint_name;
    parent_cons := cons.r_constraint_name;
    child_table := cons.table_name;
    child_cols  := '';
    parent_cols := '';

    /* loops through the child/parent column pairs, building the column lists of the DELETE statement */
    FOR cols IN columns_cursor LOOP
      IF child_cols IS NULL THEN
        child_cols := cols.child_col;
      ELSE
        child_cols := child_cols || ', ' || cols.child_col;
      END IF;

      IF parent_cols IS NULL THEN
        parent_cols := cols.parent_col;
      ELSE
        parent_cols := parent_cols || ', ' || cols.parent_col;
      END IF;
    END LOOP;

  /* construct the WHERE clause of the delete statement, including a subquery to get the related parent rows */
    new_where_clause := 'where (' || child_cols || ') in (select ' || parent_cols || ' from ' || table_owner || '.' || parent_table ||
                        ' ' || where_clause || ')';

    DeleteCascade(cons.owner, child_table, new_where_clause);
    --delete_command := 'delete from ' || cons.owner || '.' || child_table || ' ' || new_where_clause;
    --EXECUTE IMMEDIATE delete_command;
    --DBMS_OUTPUT.put_line(delete_command);
  END LOOP;

  /* construct the delete statement for the current table */
  delete_command := 'delete from ' || table_owner || '.' || parent_table || ' ' || where_clause;

  -- this just prints the delete command
  --DBMS_OUTPUT.put_line('2 = '||delete_command);
  --DBMS_OUTPUT.put_line(delete_command);
  -- uncomment if you want to actually execute it:
  EXECUTE IMMEDIATE delete_command;
  COMMIT;
  -- remember to issue a COMMIT (not included here, for safety)
END DeleteCascade;

-- eliminazione dell'oggetto
PROCEDURE MainEliminazione(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                           pExtIdUtente                NUMBER,
                           pRisultato              OUT NUMBER,
                           pMessaggio              OUT VARCHAR2) IS

  TYPE recCons IS RECORD (tableName   VARCHAR2(30),
                          columnName  VARCHAR2(30));

  TYPE tblCons       IS TABLE OF recCons INDEX BY BINARY_INTEGER;
  TYPE tblIntervento IS TABLE OF nembo_T_DETTAGLIO_INTERVENTO.ID_INTERVENTO%TYPE INDEX BY BINARY_INTEGER;

  nIdOperazione                          CONSTANT PLS_INTEGER:=8; -- eliminazione

  nIdStatoNuovoProcedimento        nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoOggetto                NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoGruppo                 NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
  nIdEsitoNuovo                            nembo_D_ESITO.ID_ESITO%TYPE;
  nRisultato                                  NUMBER;
  vMessaggio                               VARCHAR2(250);

--  tbCons           tblCons;
  tbConsProc       tblCons; -- tabelle figlie del procedimento
--  tbConsProcOgg    tblCons; -- tabelle pronipoti del procedimento oggetto
  dDataFine        nembo_T_PROCEDIMENTO_OGGETTO.DATA_FINE%TYPE;
  nIdProcedimento  nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE;
  nCodiceRaggruppamento         nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;
  vCodiceOggetto                    nembo_D_OGGETTO.CODICE%TYPE;

  ERRORE                                    EXCEPTION;
  ERRORE_TIPO_OGGETTO           EXCEPTION;
  ERRORE_STATI                          EXCEPTION;
  ERR_STATO_GRUPPO_PREC             EXCEPTION;

  nCont                         PLS_INTEGER;
  nIdAnticipo                   nembo_T_ANTICIPO.ID_ANTICIPO%TYPE;
  Intervento                    tblIntervento;
  vFlagIstanza                  nembo_D_OGGETTO.FLAG_ISTANZA%TYPE;
  nIdBando                      nembo_T_PROCEDIMENTO.ID_BANDO%TYPE;
  nIdLegameGruppoOggetto        nembo_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;

  vFlagValidazioneGrafica       nembo_T_PROCEDIMENTO_OGGETTO.FLAG_VALIDAZIONE_GRAFICA%TYPE;
  vTipoLivello                  nembo_D_TIPO_LIVELLO.CODICE%TYPE;

BEGIN
  pRisultato := 0;
  pMessaggio := '';

  SELECT PO.DATA_FINE, PO.ID_PROCEDIMENTO, OO.CODICE,OO.FLAG_ISTANZA,P.ID_BANDO,LGO.ID_LEGAME_GRUPPO_OGGETTO, PO.CODICE_RAGGRUPPAMENTO, PO.FLAG_VALIDAZIONE_GRAFICA, TL.CODICE
  INTO   dDataFine, nIdProcedimento, vCodiceOggetto,vFlagIstanza,nIdBando,nIdLegameGruppoOggetto, nCodiceRaggruppamento, vFlagValidazioneGrafica, vTipoLivello
  FROM   nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO,nembo_T_PROCEDIMENTO P, nembo_D_BANDO BB, nembo_D_TIPO_LIVELLO TL
  WHERE  PO.ID_PROCEDIMENTO_OGGETTO  = pIdProcedimentoOggetto
  AND    PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
  AND    LGO.ID_OGGETTO              = OO.ID_OGGETTO
  AND    PO.ID_PROCEDIMENTO          = P.ID_PROCEDIMENTO
  AND    P.ID_BANDO                  = BB.ID_BANDO
  AND    BB.ID_TIPO_LIVELLO          = TL.ID_TIPO_LIVELLO;

  -- 1. non posso eliminare se l'oggetto e' chiuso
  IF dDataFine IS NOT NULL THEN
    pMessaggio := 'L''oggetto selezionato risulta chiuso, non e'' possibile eliminarlo';
    RAISE ERRORE;
  END IF;

  -- 2. - inibisco l'eliminazione di taluni simpaticissimi oggetti
  IF vCodiceOggetto IN ('ISTPR','CORRE') THEN
    RAISE ERRORE_TIPO_OGGETTO;
  END IF;

  -- verifico la presenza del quadro punti
  IF PCK_nembo_UTILITY.PRESENZAQUADRO(nIdBando,nIdLegameGruppoOggetto,'PUNTI') AND vFlagIstanza = 'N' THEN
    DELETE nembo_T_PUNTEGGIO_ISTRUTTORIA
    WHERE  ID_DATI_PROCEDIMENTO_PUNTI IN (SELECT ID_DATI_PROCEDIMENTO_PUNTI
                                          FROM   NEMBO_T_DATI_PROCEDIMENTO_PUNT
                                          WHERE  ID_PROCEDIMENTO = nIdProcedimento);
  END IF;

  BEGIN
    SELECT a.ID_ANTICIPO
    INTO   nIdAnticipo
    FROM   nembo_T_ANTICIPO A, nembo_R_ANTICIPO_PROC_OGG APO
    WHERE  APO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
    AND    A.ID_ANTICIPO               = APO.ID_ANTICIPO;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      nIdAnticipo := NULL;
  END;

  SELECT ID_INTERVENTO
  BULK COLLECT INTO Intervento
  FROM   (SELECT ID_INTERVENTO
          FROM   nembo_W_DETT_INTERV_PROC_OGG
          WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
          UNION
          SELECT ID_INTERVENTO
          FROM   nembo_T_DETTAGLIO_INTERVENTO
          WHERE  ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto);

  DeleteCascade('NEMBO','NEMBO_T_PROCEDIMENTO_OGGETTO','WHERE ID_PROCEDIMENTO_OGGETTO = '||pIdProcedimentoOggetto);

  --elimino il procedimento se e solo se non ho altri oggetti figli
  SELECT COUNT(*)
  INTO   nCont
  FROM   nembo_T_PROCEDIMENTO_OGGETTO
  WHERE  ID_PROCEDIMENTO = nIdProcedimento;

  IF nCont = 0 THEN
    FOR recDeleteProc IN (SELECT RCONS.TABLE_NAME,CONSCOL.COLUMN_NAME
                          FROM   USER_CONSTRAINTS CONS, USER_CONSTRAINTS RCONS,USER_CONS_COLUMNS CONSCOL
                          WHERE  CONS.CONSTRAINT_TYPE     = 'P'
                          AND    CONS.TABLE_NAME          = 'NEMBO_T_PROCEDIMENTO'
                          AND    RCONS.R_CONSTRAINT_NAME  = CONS.CONSTRAINT_NAME
                          AND    RCONS.TABLE_NAME         = CONSCOL.TABLE_NAME
                          AND    RCONS.CONSTRAINT_NAME    = CONSCOL.CONSTRAINT_NAME
                          AND    RCONS.TABLE_NAME        != 'NEMBO_T_PROCEDIMENTO_OGGETTO') LOOP

      tbConsProc.DELETE;

      SELECT RCONS.TABLE_NAME,CONSCOL.COLUMN_NAME
      BULK COLLECT INTO tbConsProc
      FROM   USER_CONSTRAINTS CONS, USER_CONSTRAINTS RCONS,USER_CONS_COLUMNS CONSCOL
      WHERE  CONS.CONSTRAINT_TYPE    = 'P'
      AND    CONS.TABLE_NAME         = recDeleteProc.TABLE_NAME
      AND    RCONS.R_CONSTRAINT_NAME = CONS.CONSTRAINT_NAME
      AND    RCONS.TABLE_NAME        = CONSCOL.TABLE_NAME
      AND    RCONS.CONSTRAINT_NAME   = CONSCOL.CONSTRAINT_NAME;

      IF tbConsProc.FIRST IS NULL THEN
        EXECUTE IMMEDIATE 'DELETE '||recDeleteProc.TABLE_NAME||' WHERE '||recDeleteProc.COLUMN_NAME||' = '||nIdProcedimento;
        COMMIT;
      ELSE
        FOR i IN tbConsProc.FIRST..tbConsProc.LAST LOOP
            EXECUTE IMMEDIATE 'DELETE '||tbConsProc(i).tableName||' WHERE '||tbConsProc(i).columnName||' IN (SELECT '||
                               tbConsProc(i).columnName||' FROM '||recDeleteProc.TABLE_NAME||' WHERE '||recDeleteProc.COLUMN_NAME||' = '||nIdProcedimento||')';
            COMMIT;
        END LOOP;

        EXECUTE IMMEDIATE 'DELETE '||recDeleteProc.TABLE_NAME||' WHERE '||recDeleteProc.COLUMN_NAME||' = '||nIdProcedimento;
        COMMIT;
      END IF;
    END LOOP;

    DELETE nembo_T_PROCEDIMENTO
    WHERE  ID_PROCEDIMENTO = nIdProcedimento;
    COMMIT;

    ELSE
            -- nuovo ramo per gestione stati
           PCK_nembo_GESTIONE_PROC_OGGETT.MAINGESTIONESTATI ( nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nIdOperazione, NULL/*pIdEsito*/, NULL/*pCodAttore*/, nIdBando, nCodiceRaggruppamento, nIdStatoNuovoProcedimento, nIdStatoNuovoOggetto, nIdStatoNuovoGruppo, nIdEsitoNuovo, nRisultato, vMessaggio );
           IF nRisultato <> 0 THEN
                            -- lancio eccezione
                            pMessaggio:='Errore durante il richiamo della procedura MainGestioneStati: '||vMessaggio;
                            RAISE ERRORE_STATI;
           ELSE
                            -- aggiorno
                            IF NVL(nIdStatoNuovoProcedimento,-10) <> 0 THEN
                                        -- a) stato del procedimento
                                        UPDATE nembo_T_PROCEDIMENTO SET ID_STATO_OGGETTO = nIdStatoNuovoProcedimento,
                                                                                        EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                                                                        DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE
                                        WHERE ID_PROCEDIMENTO = nIdProcedimento;
                            END IF;

                              -- caso particolare: elimino i rek da NEMBO_T_ITER_PROCEDIMENTO_GRUP/nembo_t_procedimento_gruppo se l'oggetto che ho eliminato era l'ultimo del suo gruppo
                              SELECT COUNT(*)
                              INTO   nCont
                              FROM   nembo_T_PROCEDIMENTO_OGGETTO
                              WHERE  ID_PROCEDIMENTO = nIdProcedimento
                              AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;

                                IF nCont = 0 THEN
                                    -- cancello da qui
                                    DELETE FROM NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                    WHERE  ID_PROCEDIMENTO = nIdProcedimento
                                    AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                    -- e pure da qui
                                    DELETE FROM nembo_T_PROCEDIMENTO_GRUPPO
                                    WHERE  ID_PROCEDIMENTO = nIdProcedimento
                                    AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                ELSE

                                    IF NVL(nIdStatoNuovoGruppo,-10) <> 0 THEN
                                                -- d) stato del gruppo
                                                IF NVL(nIdStatoNuovoGruppo,-10) = -1 THEN
                                                        IF NOT RiportaStatoPrecIterGruppo(nIdProcedimento,nCodiceRaggruppamento) THEN
                                                                RAISE ERR_STATO_GRUPPO_PREC;
                                                        END IF;
                                                ELSE
                                                        -- chiusura vecchio iter
                                                        UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP SET DATA_FINE = SYSDATE
                                                        WHERE ID_PROCEDIMENTO = nIdProcedimento
                                                        AND DATA_FINE IS NULL
                                                        AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                                        -- apertura nuovo iter
                                                        INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                                        (ID_ITER_PROCEDIMENTO_GRUPPO, ID_PROCEDIMENTO, ID_STATO_OGGETTO, CODICE_RAGGRUPPAMENTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                                        VALUES
                                                        (SEQ_NEMBO_T_ITER_PROCEDIMEN_GR.NEXTVAL,nIdProcedimento,nIdStatoNuovoGruppo,nCodiceRaggruppamento,SYSDATE,NULL,pExtIdUtente,NULL/*pNoteGruppo*/); -- ##
                                                END IF;
                                    END IF;
                                END IF;
           END IF;

  END IF;

EXCEPTION
  WHEN ERRORE_STATI THEN
    pRisultato := 1;
   WHEN ERR_STATO_GRUPPO_PREC THEN
    pRisultato := 1;
    pMessaggio:='Errore nel recupero dello stato precedente del gruppo';
  WHEN ERRORE THEN
    pRisultato := 2;
  WHEN ERRORE_TIPO_OGGETTO THEN
    pRisultato := 2;
    pMessaggio := 'L''oggetto selezionato non puè essere eliminato';
  WHEN OTHERS THEN
    ROLLBACK;
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nell''eliminazione dell''oggetto. Codice Errore = '||
                   PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);

END MainEliminazione;

-- riapertura dell'oggetto
PROCEDURE MainRiapertura(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                         pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                         pExtIdUtente                NUMBER,
                         pRisultato              OUT NUMBER,
                         pMessaggio              OUT VARCHAR2) IS

  nIdOperazione                          CONSTANT PLS_INTEGER:=3; -- riapertura

  nIdStatoNuovoProcedimento        nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoOggetto                NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
  nIdStatoNuovoGruppo                 NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
  nIdEsitoNuovo                            nembo_D_ESITO.ID_ESITO%TYPE;
  nRisultato                                  NUMBER;
  vMessaggio                               VARCHAR2(250);

  ERRORE                        EXCEPTION;
  ERR_IMP_LIQ                EXCEPTION;
  ERR_NO_STATO_PO      EXCEPTION;
  ERR_STATO_PO            EXCEPTION;
  ERR_WREK                    EXCEPTION;
  ERR_ESITO_APP             EXCEPTION;
  ERR_TRMAS                 EXCEPTION;
  ERRORE_STATI            EXCEPTION;
  ERR_STATO_GRUPPO_PREC             EXCEPTION;

  TbDettInterv  typTbDettInterv;

  nCont            SIMPLE_INTEGER := 0;
  dDataFine  nembo_T_PROCEDIMENTO_OGGETTO.DATA_FINE%TYPE;
  nIdProcedimento  nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE;
  nIdLegameGruppoOggetto    nembo_T_PROCEDIMENTO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;
  nCodiceRaggruppamento         nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;
  nIdBando                            nembo_T_PROCEDIMENTO.ID_BANDO%TYPE;
  nIdDettIntervProcOgg      nembo_W_DETT_INTERV_PROC_OGG.ID_DETT_INTERV_PROC_OGG%TYPE;

  nIdIntervento              nembo_T_DETTAGLIO_INTERVENTO.ID_INTERVENTO%TYPE;
  dDataInizio      nembo_T_PROCEDIMENTO_OGGETTO.DATA_INIZIO%TYPE;
  vEsito            nembo_D_ESITO.CODICE%TYPE;

  bFlagIstanzaBenef                 BOOLEAN;

  -- stati
  nIdStatoProcedimentoNow    nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
  nIdStatoProcOggNow           NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;

  bAggiornaLivelli                      BOOLEAN;
  vCriterioNumIdent                 nembo_D_OGGETTO.CRITERIO_NUM_IDENTIFICATIVO%TYPE;
  vTipoLivello                          nembo_D_TIPO_LIVELLO.CODICE%TYPE;

  -- dati pre-eliminazione
  TbProcedimentoLivelloOld           typProcedimentoLivello;
  recProcedimentoLivelloOld          nembo_R_PROCEDIMENTO_LIVELLO%ROWTYPE;

  TbProcedimentoLivello           typProcedimentoLivello;
  vCodiceOggetto                  nembo_D_OGGETTO.CODICE%TYPE;

  nIdAzienda               nembo_T_PROCEDIMENTO_AZIENDA.EXT_ID_AZIENDA%TYPE;

  vFlagIstanza     nembo_D_OGGETTO.FLAG_ISTANZA%TYPE;
  vCodiceGruppoOggetto         nembo_D_GRUPPO_OGGETTO.CODICE%TYPE;

BEGIN
  pRisultato := 0;
  pMessaggio := '';

  -- recupero dati utili
  SELECT PO.DATA_FINE, PO.ID_PROCEDIMENTO, PO.ID_LEGAME_GRUPPO_OGGETTO, PP.ID_BANDO, PP.ID_STATO_OGGETTO, EE.CODICE, OO.CRITERIO_NUM_IDENTIFICATIVO,
              TL.CODICE TIPO_LIVELLO, PROCAZ.EXT_ID_AZIENDA, PO.CODICE_RAGGRUPPAMENTO, OO.FLAG_ISTANZA, OO.CODICE, GGO.CODICE
  INTO dDataFine, nIdProcedimento, nIdLegameGruppoOggetto, nIdBando,nIdStatoProcedimentoNow, vEsito, vCriterioNumIdent, vTipoLivello, nIdAzienda, nCodiceRaggruppamento, vFlagIstanza, vCodiceOggetto, vCodiceGruppoOggetto
  FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_T_PROCEDIMENTO PP, nembo_D_BANDO BB, nembo_D_ESITO EE, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO, nembo_D_TIPO_LIVELLO TL, nembo_T_PROCEDIMENTO_AZIENDA PROCAZ, nembo_D_GRUPPO_OGGETTO GGO
  WHERE PO.ID_PROCEDIMENTO = PP.ID_PROCEDIMENTO
  AND PO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
  AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
  AND LGO.ID_OGGETTO = OO.ID_OGGETTO
  AND PP.ID_BANDO = BB.ID_BANDO
  AND BB.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO
  AND PO.ID_ESITO = EE.ID_ESITO(+)
  AND PP.ID_PROCEDIMENTO = PROCAZ.ID_PROCEDIMENTO
  AND PROCAZ.DATA_FINE IS NULL
  AND LGO.ID_GRUPPO_OGGETTO = GGO.ID_GRUPPO_OGGETTO;

  -- 1. se e' gia' aperto do errore
  IF dDataFine IS NULL THEN
    pMessaggio := 'L''oggetto selezionato risulta giè aperto, impossibile procedere con l''operazione';
    RAISE ERRORE;
  END IF;

  -- 2. - non e' possibile continuare se esistono importi liquidati associati ad elenchi di liquidazione
  SELECT COUNT(*)
  INTO nCont
  FROM nembo_T_IMPORTI_LIQUIDATI ILIQ, nembo_R_LISTA_LIQUIDAZ_IMP_LIQ LLIL
  WHERE ILIQ.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
  AND ILIQ.ID_IMPORTI_LIQUIDATI = LLIL.ID_IMPORTI_LIQUIDATI;

  IF nCont > 0 THEN
            RAISE ERR_IMP_LIQ;
  ELSE
            -- elimino i rek di imp liq
            DELETE FROM nembo_T_IMPORTI_LIQUIDATI
            WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
  END IF;

  bAggiornaLivelli:=FALSE;
  IF PCK_nembo_UTILITY.ReturnIstanzaCreataDaBenef(nIdLegameGruppoOggetto) THEN
        bFlagIstanzaBenef:=TRUE;
        BEGIN
            SELECT IPO.ID_STATO_OGGETTO
            INTO nIdStatoProcOggNow
            FROM NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
            WHERE IPO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
            AND DATA_FINE IS NULL;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE ERR_NO_STATO_PO;
        END;
        -- 4. se e' oltre un certo range, do errore
        IF nIdStatoProcOggNow >= 10 THEN
            RAISE ERR_STATO_PO;
        END IF;
          -- verifico se devo aggiornare i livelli corrispondenti al procedimento
          IF vTipoLivello IN ('I','G') THEN
                IF nIdStatoProcedimentoNow <=10 AND vCriterioNumIdent IN ('**' ,'##') THEN
                    bAggiornaLivelli:=TRUE;
                END IF;
           END IF;
  ELSE
        bFlagIstanzaBenef:=FALSE;
        -- 5. - devo bloccare la riapertura se l'esito dell'oggetto e' Approvato
        IF (vEsito LIKE 'APP-%' OR vEsito LIKE 'NOLIQ-%') THEN
                RAISE ERR_ESITO_APP;
        END IF;
        IF vTipoLivello IN ('I','G') THEN
            bAggiornaLivelli:=TRUE;
        END IF;
  END IF;

   PCK_nembo_GESTIONE_PROC_OGGETT.MAINGESTIONESTATI ( nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nIdOperazione, NULL/*pIdEsito*/, NULL/*pCodAttore*/, nIdBando, nCodiceRaggruppamento, nIdStatoNuovoProcedimento, nIdStatoNuovoOggetto, nIdStatoNuovoGruppo, nIdEsitoNuovo, nRisultato, vMessaggio );
   IF nRisultato <> 0 THEN
                    -- lancio eccezione
                    pMessaggio:='Errore durante il richiamo della procedura MainGestioneStati: '||vMessaggio;
                    RAISE ERRORE_STATI;
   ELSE
                    -- aggiorno
                    IF NVL(nIdStatoNuovoProcedimento,-10) <> 0 THEN
                                -- a) stato del procedimento
                                UPDATE nembo_T_PROCEDIMENTO SET ID_STATO_OGGETTO = nIdStatoNuovoProcedimento,
                                                                                EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                                                                DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE
                                WHERE ID_PROCEDIMENTO = nIdProcedimento;
                    END IF;
                    IF NVL(nIdStatoNuovoOggetto,-10) <> 0 THEN
                                -- b) stato dell'oggetto
                                -- chiusura vecchio iter
                                UPDATE NEMBO_T_ITER_PROCEDIMENTO_OGGE SET DATA_FINE = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                AND DATA_FINE IS NULL;
                                -- apertura nuovo iter
                                INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_OGGE
                                (ID_ITER_PROCEDIMENTO_OGGETT, ID_PROCEDIMENTO_OGGETTO, ID_STATO_OGGETTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                VALUES
                                (SEQ_NEMBO_T_ITER_PROCEDIMEN_OG.NEXTVAL,pIdProcedimentoOggetto,nIdStatoNuovoOggetto,SYSDATE,NULL,pExtIdUtente,pNote);
                    END IF;
                    -- c) esito
                    UPDATE nembo_T_PROCEDIMENTO_OGGETTO
                    SET ID_ESITO = NULL, DATA_FINE = NULL,
                           EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                           DATA_ULTIMO_AGGIORNAMENTO = SYSDATE
                    WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
                    IF NVL(nIdStatoNuovoGruppo,-10) <> 0 THEN
                                -- d) stato del gruppo
                                IF NVL(nIdStatoNuovoGruppo,-10) = -1 THEN
                                        IF NOT RiportaStatoPrecIterGruppo(nIdProcedimento,nCodiceRaggruppamento) THEN
                                                RAISE ERR_STATO_GRUPPO_PREC;
                                        END IF;
                                ELSE
                                        -- chiusura vecchio iter
                                        UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP SET DATA_FINE = SYSDATE
                                        WHERE ID_PROCEDIMENTO = nIdProcedimento
                                        AND DATA_FINE IS NULL
                                        AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                        -- apertura nuovo iter
                                        INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                        (ID_ITER_PROCEDIMENTO_GRUPPO, ID_PROCEDIMENTO, ID_STATO_OGGETTO, CODICE_RAGGRUPPAMENTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                        VALUES
                                        (SEQ_NEMBO_T_ITER_PROCEDIMEN_GR.NEXTVAL,nIdProcedimento,nIdStatoNuovoGruppo,nCodiceRaggruppamento,SYSDATE,NULL,pExtIdUtente,NULL/*pNoteGruppo*/); -- ##
                                END IF;
                    END IF;
   END IF;


  IF bFlagIstanzaBenef THEN
          -- inoltre, azzero l'identificativo sull'oggetto
          UPDATE nembo_T_PROCEDIMENTO_OGGETTO
          SET IDENTIFICATIVO=NULL
          WHERE ID_PROCEDIMENTO_OGGETTO =  pIdProcedimentoOggetto;
  END IF;

  SELECT *
  BULK COLLECT INTO TbDettInterv
  FROM nembo_T_DETTAGLIO_INTERVENTO
  WHERE ID_PROCEDIMENTO_OGGETTO =  pIdProcedimentoOggetto
  ORDER BY ID_DETTAGLIO_INTERVENTO;

  FOR i IN 1..TbDettInterv.COUNT LOOP

    nIdIntervento:=TbDettInterv(i).ID_INTERVENTO;
    dDataInizio:=TbDettInterv(i).DATA_INIZIO;
   IF TbDettInterv(i).FLAG_TIPO_OPERAZIONE IN ('D', 'U', 'E') THEN
        -- riapro il record precedente a parita' di intervento, ovviamente escludendo il record che dovro' andare ad eliminare
        UPDATE nembo_T_DETTAGLIO_INTERVENTO
        SET DATA_FINE = NULL
        WHERE ID_INTERVENTO = nIdIntervento
        AND DATA_FINE = dDataInizio
        AND ID_DETTAGLIO_INTERVENTO <> TbDettInterv(i).ID_DETTAGLIO_INTERVENTO;
   END IF;

   IF vCodiceGruppoOggetto<>'VAR' THEN
            -- inserisco il nuovo record sulla wrk
            INSERT INTO nembo_W_DETT_INTERV_PROC_OGG
            (ID_DETT_INTERV_PROC_OGG,ID_PROCEDIMENTO_OGGETTO,ID_INTERVENTO,IMPORTO_INVESTIMENTO,IMPORTO_AMMESSO,PERCENTUALE_CONTRIBUTO,IMPORTO_CONTRIBUTO,
             FLAG_TIPO_OPERAZIONE,ULTERIORI_INFORMAZIONI,IMPORTO_UNITARIO,FLAG_ASSOCIATO_ALTRA_MISURA, CUAA_BENEFICIARIO, EXT_ID_AZIENDA_BENEFICIARIO,  FLAG_CONDOTTA, FLAG_CANALE, FLAG_OPERA_PRESA)
            VALUES
            (SEQ_NEMBO_W_DETT_INTE_PROC_OGG.NEXTVAL, TbDettInterv(i).ID_PROCEDIMENTO_OGGETTO, nIdIntervento,
             TbDettInterv(i).IMPORTO_INVESTIMENTO, TbDettInterv(i).IMPORTO_AMMESSO, TbDettInterv(i).PERCENTUALE_CONTRIBUTO,
             TbDettInterv(i).IMPORTO_CONTRIBUTO, /*vFlagTipoOperazione*/TbDettInterv(i).FLAG_TIPO_OPERAZIONE, TbDettInterv(i).ULTERIORI_INFORMAZIONI, TbDettInterv(i).IMPORTO_UNITARIO, TbDettInterv(i).FLAG_ASSOCIATO_ALTRA_MISURA, TbDettInterv(i).CUAA_BENEFICIARIO, TbDettInterv(i).EXT_ID_AZIENDA_BENEFICIARIO,  TbDettInterv(i).FLAG_CONDOTTA, TbDettInterv(i).FLAG_CANALE, TbDettInterv(i).FLAG_OPERA_PRESA)
             RETURNING ID_DETT_INTERV_PROC_OGG INTO nIdDettIntervProcOgg;

             -- tabella figlia numero 1
             INSERT INTO NEMBO_R_DETT_INTE_PROC_OGG_MIS
            (ID_DETT_INTERV_PROC_OGG, ID_MISURAZIONE_INTERVENTO, QUANTITA)
            (SELECT nIdDettIntervProcOgg,  ID_MISURAZIONE_INTERVENTO, QUANTITA
             FROM NEMBO_R_DETT_INTERV_MISURAZION
             WHERE ID_DETTAGLIO_INTERVENTO = TbDettInterv(i).ID_DETTAGLIO_INTERVENTO);

             -- tabella figlia numero 2
             INSERT INTO nembo_W_LOCAL_INTERV_PROC_OGG
            (ID_LOCAL_INTERV_PROC_OGG, ID_DETT_INTERV_PROC_OGG, ISTAT_COMUNE, EXT_ID_PARTICELLA_CERTIFICATA, EXT_ID_CONDUZIONE_DICHIARATA, EXT_ID_UTILIZZO_DICHIARATO, SUPERFICIE_IMPEGNO, SUPERFICIE_EFFETTIVA, SUPERFICIE_ISTRUTTORIA, SUPERFICIE_ACCERTATA_GIS)
            (SELECT SEQ_NEMBO_W_LOCAL_INTE_PROC_OG.NEXTVAL, nIdDettIntervProcOgg,  ISTAT_COMUNE, EXT_ID_PARTICELLA_CERTIFICATA, EXT_ID_CONDUZIONE_DICHIARATA, EXT_ID_UTILIZZO_DICHIARATO, SUPERFICIE_IMPEGNO, SUPERFICIE_EFFETTIVA, SUPERFICIE_ISTRUTTORIA, SUPERFICIE_ACCERTATA_GIS
             FROM nembo_T_LOCALIZZAZIONE_INTERV
             WHERE ID_DETTAGLIO_INTERVENTO = TbDettInterv(i).ID_DETTAGLIO_INTERVENTO);

             -- tabella figlia numero 3
             INSERT INTO NEMBO_W_FILE_ALL_INTE_PROC_OGG
            (ID_DETT_INTERV_PROC_OGG, ID_FILE_ALLEGATI_INTERVENTO)
            (SELECT nIdDettIntervProcOgg,  ID_FILE_ALLEGATI_INTERVENTO
             FROM NEMBO_R_FILE_ALLEGATI_DETT_INT
             WHERE ID_DETTAGLIO_INTERVENTO = TbDettInterv(i).ID_DETTAGLIO_INTERVENTO);
   END IF;
   -- fine 3. V@# a)
     -- infine elimino i rek dalle tabelle t
     DELETE
     FROM NEMBO_R_DETT_INTERV_MISURAZION
     WHERE ID_DETTAGLIO_INTERVENTO = TbDettInterv(i).ID_DETTAGLIO_INTERVENTO;

     DELETE
     FROM nembo_T_LOCALIZZAZIONE_INTERV
     WHERE ID_DETTAGLIO_INTERVENTO = TbDettInterv(i).ID_DETTAGLIO_INTERVENTO;

     DELETE
     FROM NEMBO_R_FILE_ALLEGATI_DETT_INT
     WHERE ID_DETTAGLIO_INTERVENTO = TbDettInterv(i).ID_DETTAGLIO_INTERVENTO;

     DELETE
     FROM nembo_T_DETTAGLIO_INTERVENTO
     WHERE ID_DETTAGLIO_INTERVENTO = TbDettInterv(i).ID_DETTAGLIO_INTERVENTO;

  END LOOP;
--     aggiornamento livelli
  IF bAggiornaLivelli THEN
        -- recupero i rek prima di fulminarli
        SELECT *
        BULK COLLECT INTO TbProcedimentoLivelloOld
        FROM nembo_R_PROCEDIMENTO_LIVELLO
        WHERE ID_PROCEDIMENTO = nIdProcedimento;
        -- pulisco
        DELETE FROM nembo_R_PROCEDIMENTO_LIVELLO WHERE ID_PROCEDIMENTO = nIdProcedimento;
        IF vTipoLivello IN ('I','G') THEN
            -- investimento
            -- conto gli interventi
            SELECT COUNT(*)
            INTO nCont
            FROM nembo_T_INTERVENTO IINNTT
            WHERE IINNTT.ID_PROCEDIMENTO = nIdProcedimento;
            IF nCont = 0 THEN
                -- se non ce ne sono, reinserisco tuuuuuuuuuuuuuuutti i livelli del bando
                INSERT INTO nembo_R_PROCEDIMENTO_LIVELLO (ID_PROCEDIMENTO, ID_LIVELLO)
                    SELECT nIdProcedimento,ID_LIVELLO FROM nembo_R_LIVELLO_BANDO WHERE ID_BANDO = nIdBando;

            ELSE
                -- altrimenti inserisco solo quelli per cui esiste un intervento
                INSERT INTO nembo_R_PROCEDIMENTO_LIVELLO (ID_PROCEDIMENTO, ID_LIVELLO)
                    SELECT nIdProcedimento,ID_LIVELLO FROM nembo_R_LIVELLO_BANDO LB
                    WHERE ID_BANDO = nIdBando
                    AND EXISTS (
                        SELECT *
                        FROM nembo_T_INTERVENTO III, nembo_R_LIVELLO_INTERVENTO LINT
                        WHERE III.ID_PROCEDIMENTO = nIdProcedimento
                        AND III.ID_DESCRIZIONE_INTERVENTO = LINT.ID_DESCRIZIONE_INTERVENTO
                        AND LINT.ID_LIVELLO = LB.ID_LIVELLO
                    );
            END IF;
            -- recupero i rek
            SELECT *
            BULK COLLECT INTO TbProcedimentoLivello
            FROM nembo_R_PROCEDIMENTO_LIVELLO
            WHERE ID_PROCEDIMENTO = nIdProcedimento;
            FOR uu IN 1..TbProcedimentoLivello.COUNT LOOP
                 recProcedimentoLivelloOld:=RecuperaDatiProcLivello(TbProcedimentoLivelloOld,TbProcedimentoLivello(uu).ID_LIVELLO);
                UPDATE nembo_R_PROCEDIMENTO_LIVELLO SET IMPORTO_INVESTIMENTO = recProcedimentoLivelloOld.IMPORTO_INVESTIMENTO, SPESA_AMMESSA = recProcedimentoLivelloOld.SPESA_AMMESSA, CONTRIBUTO_CONCESSO = recProcedimentoLivelloOld.CONTRIBUTO_CONCESSO,
                                                                                    CONTRIBUTO_EROGABILE =  recProcedimentoLivelloOld.CONTRIBUTO_EROGABILE, CONTRIBUTO_NON_EROGABILE =  recProcedimentoLivelloOld.CONTRIBUTO_NON_EROGABILE,
                                                                                    DATA_AMMISSIONE = recProcedimentoLivelloOld.DATA_AMMISSIONE
                WHERE ID_PROCEDIMENTO = nIdProcedimento AND ID_LIVELLO = TbProcedimentoLivello(uu).ID_LIVELLO;
            END LOOP;
        END IF;
  END IF;

EXCEPTION
  WHEN ERRORE_STATI THEN
    pRisultato := 1;
   WHEN ERR_STATO_GRUPPO_PREC THEN
    pRisultato := 1;
    pMessaggio:='Errore nel recupero dello stato precedente del gruppo';
  WHEN ERR_IMP_LIQ THEN
    pRisultato := 2;
    pMessaggio := 'L''oggetto è giè stato associato ad un elenco di liquidazione; impossibile procedere con l''operazione selezionata';
--  WHEN ERR_WREK THEN
--    pRisultato := 2;
--    pMessaggio := 'Esistono record sulla tabella d''appoggio; impossibile procedere con l''operazione selezionata';
  WHEN ERR_NO_STATO_PO THEN
    pRisultato := 1;
    pMessaggio := 'Impossibile recuperare lo stato attuale dell''oggetto';
  WHEN ERR_STATO_PO THEN
    pRisultato := 2;
    pMessaggio := 'L''oggetto selezionato si trova in uno stato per cui non è possibile la riapertura';
  WHEN ERR_ESITO_APP THEN
    pRisultato := 2;
    pMessaggio := 'L''oggetto selezionato è stato chiuso con esito Approvato; impossibile procedere con l''operazione selezionata';
  WHEN ERRORE THEN
    pRisultato := 2;
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nella riapertura dell''oggetto. Codice Errore = '||
    PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
END MainRiapertura;

-- trasmissione - componente che gestisce il cambio stato
PROCEDURE MainTrasmissione(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                               pIdEsito                    nembo_D_ESITO.ID_ESITO%TYPE,
                                               pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                                               pNoteGruppo             NEMBO_T_ITER_PROCEDIMENTO_GRUP.NOTE%TYPE,
                                               pExtIdUtente              NUMBER,
                                               pRisultato                  OUT NUMBER,
                                               pMessaggio               OUT VARCHAR2) IS

   nIdProcedimento                      nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE;
   nIdLegameGruppoOggetto         nembo_T_PROCEDIMENTO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;
   nIdBando                                 nembo_T_PROCEDIMENTO.ID_BANDO%TYPE;
   nCodiceRaggruppamento           nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;

  ERRORE_STATI                               EXCEPTION;
  ERR_STATO_GRUPPO_PREC             EXCEPTION;

   nIdOperazione                          CONSTANT PLS_INTEGER:=4; -- trasmissione

   nIdStatoNuovoProcedimento        nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
   nIdStatoNuovoOggetto                NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
   nIdStatoNuovoGruppo                 NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
   nIdEsitoNuovo                            nembo_D_ESITO.ID_ESITO%TYPE;
   nRisultato                                  NUMBER;
   vMessaggio                               VARCHAR2(250);

   vCodiceOggetto                    nembo_D_OGGETTO.CODICE%TYPE;
   vCriterioNumIdent                 nembo_D_OGGETTO.CRITERIO_NUM_IDENTIFICATIVO%TYPE;
   nIdStatoProcedimentoNow/*Attuale*/    nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;

BEGIN

            pRisultato := 0;
            pMessaggio := '';

            -- ricavo i parametri che mi servono per invocare il MainGestioneStati e altre cose utili
            SELECT PP.ID_PROCEDIMENTO, PO.ID_LEGAME_GRUPPO_OGGETTO, PP.ID_BANDO, PO.CODICE_RAGGRUPPAMENTO, OO.CODICE, OO.CRITERIO_NUM_IDENTIFICATIVO, PP.ID_STATO_OGGETTO
            INTO nIdProcedimento, nIdLegameGruppoOggetto, nIdBando, nCodiceRaggruppamento, vCodiceOggetto, vCriterioNumIdent,nIdStatoProcedimentoNow
            FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_T_PROCEDIMENTO PP, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO
            WHERE PO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
            AND PO.ID_PROCEDIMENTO = PP.ID_PROCEDIMENTO
            AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
            AND LGO.ID_OGGETTO = OO.ID_OGGETTO;

            UPDATE NEMBO_R_PROCEDIMENTO_SCORTA SET DATA_FINE = SYSDATE WHERE ID_PROCEDIMENTO = nIdProcedimento AND DATA_FINE IS NULL;
            INSERT INTO NEMBO_R_PROCEDIMENTO_SCORTA(ID_PROCEDIMENTO_SCORTA, ID_PROCEDIMENTO, ID_SCORTA_MAGAZZINO, DATA_INIZIO, DATA_FINE)
            SELECT SEQ_NEMBO_R_PROCEDIMENTO_SCORT.NEXTVAL, nIdProcedimento, ID_SCORTA_MAGAZZINO, SYSDATE, NULL
            FROM NEMBO_T_SCORTA_MAGAZZINO WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;

            UPDATE NEMBO_R_PROCEDIMENTO_DANNO SET DATA_FINE = SYSDATE WHERE ID_PROCEDIMENTO = nIdProcedimento AND DATA_FINE IS NULL;
            INSERT INTO NEMBO_R_PROCEDIMENTO_DANNO(ID_PROCEDIMENTO_DANNO, ID_PROCEDIMENTO, ID_DANNO_ATM, DATA_INIZIO, DATA_FINE)
            SELECT SEQ_NEMBO_R_PROCEDIMENTO_DANNO.NEXTVAL, nIdProcedimento, ID_DANNO_ATM, SYSDATE, NULL
            FROM NEMBO_T_DANNO_ATM WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;

            UPDATE NEMBO_R_PROC_SUPERFICIE_COLTUR SET DATA_FINE = SYSDATE WHERE ID_PROCEDIMENTO = nIdProcedimento AND DATA_FINE IS NULL;
            INSERT INTO NEMBO_R_PROC_SUPERFICIE_COLTUR(ID_PROC_SUPERFICIE_COLTUR, ID_PROCEDIMENTO, ID_SUPERFICIE_COLTURA, DATA_INIZIO, DATA_FINE)
            SELECT SEQ_NEMBO_R_PROC_SUPERFICIE_CO.NEXTVAL, nIdProcedimento, ID_SUPERFICIE_COLTURA, SYSDATE, NULL
            FROM NEMBO_T_SUPERFICIE_COLTURA WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;

            UPDATE NEMBO_R_PROC_PRESTITI_AGRARI SET DATA_FINE = SYSDATE WHERE ID_PROCEDIMENTO = nIdProcedimento AND DATA_FINE IS NULL;
            INSERT INTO NEMBO_R_PROC_PRESTITI_AGRARI(ID_PROC_PRESTITI_AGRARI, ID_PROCEDIMENTO, ID_PRESTITI_AGRARI, DATA_INIZIO, DATA_FINE)
            SELECT SEQ_NEMBO_R_PROC_PRESTITI_AGRA.NEXTVAL, nIdProcedimento, ID_PRESTITI_AGRARI, SYSDATE, NULL
            FROM NEMBO_T_PRESTITI_AGRARI WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;

            UPDATE NEMBO_R_PROC_PRODUZIONE_ZOOTEC SET DATA_FINE = SYSDATE WHERE ID_PROCEDIMENTO = nIdProcedimento AND DATA_FINE IS NULL;
            INSERT INTO NEMBO_R_PROC_PRODUZIONE_ZOOTEC(ID_PROC_PRODUZIONE_ZOOTEC, ID_PROCEDIMENTO, ID_PRODUZIONE_ZOOTECNICA, DATA_INIZIO, DATA_FINE)
            SELECT SEQ_NEMBO_R_PROC_PRODUZIONE_ZO.NEXTVAL, nIdProcedimento, ID_PRODUZIONE_ZOOTECNICA, SYSDATE, NULL
            FROM NEMBO_T_PRODUZIONE_ZOOTECNICA WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;

            UPDATE NEMBO_R_PROC_ASSICURAZ_COLTURE SET DATA_FINE = SYSDATE WHERE ID_PROCEDIMENTO = nIdProcedimento AND DATA_FINE IS NULL;
            INSERT INTO NEMBO_R_PROC_ASSICURAZ_COLTURE(ID_PROCEDIMENTO_ASSICURAZ_COLT, ID_PROCEDIMENTO, ID_ASSICURAZIONI_COLTURE, DATA_INIZIO, DATA_FINE)
            SELECT SEQ_NEMBO_R_PROC_ASSICURAZ_COL.NEXTVAL, nIdProcedimento, ID_ASSICURAZIONI_COLTURE, SYSDATE, NULL
            FROM NEMBO_T_ASSICURAZIONI_COLTURE WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;

            -- lancio il moloch
            PCK_nembo_GESTIONE_PROC_OGGETT.MAINGESTIONESTATI ( nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nIdOperazione, pIdEsito, NULL/*pCodAttore*/, nIdBando, nCodiceRaggruppamento, nIdStatoNuovoProcedimento, nIdStatoNuovoOggetto, nIdStatoNuovoGruppo, nIdEsitoNuovo, nRisultato, vMessaggio );
            IF nRisultato <> 0 THEN
                    -- lancio eccezione
                    pMessaggio:='Errore durante il richiamo della procedura MainGestioneStati: '||vMessaggio;
                    RAISE ERRORE_STATI;
            ELSE
                    -- aggiorno
                    IF NVL(nIdStatoNuovoProcedimento,-10) <> 0 THEN
                                -- a) stato del procedimento
                                UPDATE nembo_T_PROCEDIMENTO SET ID_STATO_OGGETTO = nIdStatoNuovoProcedimento,
                                                                                EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                                                                DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE
                                WHERE ID_PROCEDIMENTO = nIdProcedimento;
                    END IF;
                    IF NVL(nIdStatoNuovoOggetto,-10) <> 0 THEN
                                -- b) stato dell'oggetto
                                -- chiusura vecchio iter
                                UPDATE NEMBO_T_ITER_PROCEDIMENTO_OGGE SET DATA_FINE = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                AND DATA_FINE IS NULL;
                                -- apertura nuovo iter
                                INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_OGGE
                                (ID_ITER_PROCEDIMENTO_OGGETT, ID_PROCEDIMENTO_OGGETTO, ID_STATO_OGGETTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                VALUES
                                (SEQ_NEMBO_T_ITER_PROCEDIMEN_OG.NEXTVAL,pIdProcedimentoOggetto,nIdStatoNuovoOggetto,SYSDATE,NULL,pExtIdUtente,pNote);
                    END IF;
                    IF NVL(nIdEsitoNuovo,-10) <> 0 THEN
                                -- c) esito
                                UPDATE nembo_T_PROCEDIMENTO_OGGETTO
                                SET ID_ESITO = nIdEsitoNuovo,
                                       EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                       DATA_ULTIMO_AGGIORNAMENTO = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto;
                    END IF;
                    IF NVL(nIdStatoNuovoGruppo,-10) <> 0 THEN
                                -- d) stato del gruppo
                                IF NVL(nIdStatoNuovoGruppo,-10) = -1 THEN
                                        IF NOT RiportaStatoPrecIterGruppo(nIdProcedimento,nCodiceRaggruppamento) THEN
                                                RAISE ERR_STATO_GRUPPO_PREC;
                                        END IF;
                                ELSE
                                        -- chiusura vecchio iter
                                        UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP SET DATA_FINE = SYSDATE
                                        WHERE ID_PROCEDIMENTO = nIdProcedimento
                                        AND DATA_FINE IS NULL
                                        AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                        -- apertura nuovo iter
                                        INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                        (ID_ITER_PROCEDIMENTO_GRUPPO, ID_PROCEDIMENTO, ID_STATO_OGGETTO, CODICE_RAGGRUPPAMENTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                        VALUES
                                        (SEQ_NEMBO_T_ITER_PROCEDIMEN_GR.NEXTVAL,nIdProcedimento,nIdStatoNuovoGruppo,nCodiceRaggruppamento,SYSDATE,NULL,pExtIdUtente,NULL/*pNoteGruppo*/); -- ##
                                END IF;
                    END IF;
            END IF;

--              -- aggiorno gli importi. Lo faccio solo per determinate istanze
--            IF ((nIdStatoProcedimentoNow <=10 AND vCriterioNumIdent IN ('**' ,'##')) OR vCriterioNumIdent='VI') THEN
--                    -- l'esito sara' sempre P
--                    IF NOT AggiornaImportiLivelli(nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nCodiceRaggruppamento, 'I', 'P'/*vCodiceEsito*/, vCodiceOggetto, nRisultato, vMessaggio) THEN
--                            -- lancio eccezione
--                            pMessaggio:='Errore durante il richiamo della procedura AggiornaImportiLivelli: '||vMessaggio;
--                            RAISE ERRORE_STATI;
--                    END IF;
--            END IF;
EXCEPTION
  WHEN ERRORE_STATI THEN
    pRisultato := 1;
   WHEN ERR_STATO_GRUPPO_PREC THEN
    pRisultato := 1;
    pMessaggio:='Errore nel recupero dello stato precedente del gruppo';
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nella procedura MainTrasmissione. Codice Errore = '||
    PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
END MainTrasmissione;

-- approvazione - componente che gestisce il cambio stato
PROCEDURE MainApprovazione(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                               pIdEsito                    nembo_D_ESITO.ID_ESITO%TYPE,
                                               pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                                               pNoteGruppo             NEMBO_T_ITER_PROCEDIMENTO_GRUP.NOTE%TYPE,
                                               pExtIdUtente              NUMBER,
                                               pRisultato                  OUT NUMBER,
                                               pMessaggio               OUT VARCHAR2) IS

   nIdProcedimento                      nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE;
   nIdLegameGruppoOggetto         nembo_T_PROCEDIMENTO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;
   nIdBando                                 nembo_T_PROCEDIMENTO.ID_BANDO%TYPE;
   nCodiceRaggruppamento           nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;
   -- per la decodifica dell'esito
  nIdEsitoDecod                   NEMBO_R_ESITO_DECODIFICATO.ID_ESITO_DECODIFICATO%TYPE;

  ERRORE                                EXCEPTION;
  ERRORE_STATI                               EXCEPTION;
  ERR_STATO_GRUPPO_PREC             EXCEPTION;

   nIdOperazione                          CONSTANT PLS_INTEGER:=5; -- approvazione

   nIdStatoNuovoProcedimento        nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
   nIdStatoNuovoOggetto                NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
   nIdStatoNuovoGruppo                 NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
   nIdEsitoNuovo                            nembo_D_ESITO.ID_ESITO%TYPE;
   nRisultato                                  NUMBER;
   vMessaggio                               VARCHAR2(250);

   vCodiceEsito                             nembo_D_ESITO.CODICE%TYPE;
   vCodiceOggetto                         nembo_D_OGGETTO.CODICE%TYPE;
   vCodiceGruppo                          nembo_D_GRUPPO_OGGETTO.CODICE%TYPE;

   vTipoLivello                           nembo_D_TIPO_LIVELLO.CODICE%TYPE;

   dApprovazione                    NEMBO_T_ITER_PROCEDIMENTO_OGGE.DATA_INIZIO%TYPE;

   nIstruttorie                               PLS_INTEGER;

   TbProdCodOggetto                     typProcCodOggetto;

   nCont                                PLS_INTEGER;

   nIdEsitoIstruttoria                          nembo_T_PROCEDIMENTO_OGGETTO.ID_ESITO_ISTRUTTORIA%TYPE;

BEGIN

            pRisultato := 0;
            pMessaggio := '';

            -- decodifico l'esito
            BEGIN
                SELECT EED.ID_ESITO_DECODIFICATO
                INTO nIdEsitoDecod
                FROM NEMBO_D_ESITO EE, NEMBO_R_ESITO_DECODIFICATO EED
                WHERE EE.ID_ESITO = pIdEsito
                AND EE.ID_ESITO = EED.ID_ESITO
                AND EED.ID_TIPO_OPERAZIONE = 5; -- approvazione
            EXCEPTION
                WHEN OTHERS THEN
                        nIdEsitoDecod:=NULL;
            END;

            -- ricavo i parametri che mi servono per invocare il MainGestioneStati e altre cose utili
            SELECT PP.ID_PROCEDIMENTO, PO.ID_LEGAME_GRUPPO_OGGETTO, PP.ID_BANDO, PO.CODICE_RAGGRUPPAMENTO, OO.CODICE, TL.CODICE, GOO.CODICE
            INTO nIdProcedimento, nIdLegameGruppoOggetto, nIdBando, nCodiceRaggruppamento, vCodiceOggetto, vTipoLivello, vCodiceGruppo
            FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_T_PROCEDIMENTO PP, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO, nembo_D_BANDO BB, nembo_D_TIPO_LIVELLO TL, nembo_D_GRUPPO_OGGETTO GOO
            WHERE PO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
            AND PO.ID_PROCEDIMENTO = PP.ID_PROCEDIMENTO
            AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
            AND LGO.ID_OGGETTO = OO.ID_OGGETTO
            AND PP.ID_BANDO = BB.ID_BANDO
            AND BB.ID_TIPO_LIVELLO = TL.ID_TIPO_LIVELLO
            AND LGO.ID_GRUPPO_OGGETTO = GOO.ID_GRUPPO_OGGETTO;

            -- recupero il numero istruttorie
            -- vale a dire il numero di oggetti dello stesso tipo di quello passato in input, con lo stesso codice raggruppamento e legati al procedimento corrente
            SELECT COUNT(*)
            INTO nIstruttorie
            FROM nembo_T_PROCEDIMENTO_OGGETTO PO , NEMBO_T_ITER_PROCEDIMENTO_OGGE ITERPO
            WHERE PO.ID_PROCEDIMENTO = nIdProcedimento
            AND PO.ID_LEGAME_GRUPPO_OGGETTO = nIdLegameGruppoOggetto
            AND PO.CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento
            AND PO.ID_PROCEDIMENTO_OGGETTO = ITERPO.ID_PROCEDIMENTO_OGGETTO
            AND ITERPO.DATA_FINE IS NULL
            AND ITERPO.ID_STATO_OGGETTO <= 90;

            -- lancio il moloch
            PCK_nembo_GESTIONE_PROC_OGGETT.MAINGESTIONESTATI ( nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nIdOperazione, nIdEsitoDecod/*pIdEsito*/, NULL/*pCodAttore*/, nIdBando, nCodiceRaggruppamento, nIdStatoNuovoProcedimento, nIdStatoNuovoOggetto, nIdStatoNuovoGruppo, nIdEsitoNuovo, nRisultato, vMessaggio );
            IF nRisultato <> 0 THEN
                    -- lancio eccezione
                    pMessaggio:='Errore durante il richiamo della procedura MainGestioneStati: '||vMessaggio;
                    RAISE ERRORE_STATI;
            ELSE
                    -- aggiorno
                    IF NVL(nIdStatoNuovoProcedimento,-10) <> 0 THEN
                                -- a) stato del procedimento
                                UPDATE nembo_T_PROCEDIMENTO SET ID_STATO_OGGETTO = nIdStatoNuovoProcedimento,
                                                                                EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                                                                DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE
                                WHERE ID_PROCEDIMENTO = nIdProcedimento;
                    END IF;
                    IF NVL(nIdStatoNuovoOggetto,-10) <> 0 THEN
                                -- b) stato dell'oggetto
                                -- chiusura vecchio iter
                                UPDATE NEMBO_T_ITER_PROCEDIMENTO_OGGE SET DATA_FINE = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                AND DATA_FINE IS NULL;
                                -- apertura nuovo iter
                                INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_OGGE
                                (ID_ITER_PROCEDIMENTO_OGGETT, ID_PROCEDIMENTO_OGGETTO, ID_STATO_OGGETTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                VALUES
                                (SEQ_NEMBO_T_ITER_PROCEDIMEN_OG.NEXTVAL,pIdProcedimentoOggetto,nIdStatoNuovoOggetto,SYSDATE,NULL,pExtIdUtente,pNote);
                    END IF;
                    IF NVL(nIdEsitoNuovo,-10) <> 0 THEN
                                -- c) esito
                                UPDATE nembo_T_PROCEDIMENTO_OGGETTO
                                SET ID_ESITO = nIdEsitoNuovo,
                                       EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                       DATA_ULTIMO_AGGIORNAMENTO = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                RETURNING DATA_ULTIMO_AGGIORNAMENTO INTO dApprovazione;
                                IF vTipoLivello <> 'P' THEN
                                         -- aggiorno
                                         IF nIdEsitoIstruttoria IS NOT NULL THEN
                                                UPDATE  nembo_T_PROCEDIMENTO_OGGETTO
                                                SET ID_ESITO_ISTRUTTORIA = nIdEsitoIstruttoria
                                                WHERE ID_PROCEDIMENTO_OGGETTO IN
                                                (
                                                    SELECT PO.ID_PROCEDIMENTO_OGGETTO
                                                    FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_R_LEGAME_GRUPPO_OGGETTO LGO, nembo_D_OGGETTO OO
                                                    WHERE ID_PROCEDIMENTO = nIdProcedimento
                                                    AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento
                                                    AND FLAG_ISTANZA = 'S'
                                                    AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
                                                    AND LGO.ID_OGGETTO = OO.ID_OGGETTO
                                                    AND OO.FLAG_ESTRAZIONE_CAMPIONE = 'S'
                                                );
                                        END IF;
                                END IF;
                    END IF;
                    IF NVL(nIdStatoNuovoGruppo,-10) <> 0 THEN
                                -- d) stato del gruppo
                                IF NVL(nIdStatoNuovoGruppo,-10) = -1 THEN
                                        IF NOT RiportaStatoPrecIterGruppo(nIdProcedimento,nCodiceRaggruppamento) THEN
                                                RAISE ERR_STATO_GRUPPO_PREC;
                                        END IF;
                                ELSE
                                        -- chiusura vecchio iter
                                        UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP SET DATA_FINE = SYSDATE
                                        WHERE ID_PROCEDIMENTO = nIdProcedimento
                                        AND DATA_FINE IS NULL
                                        AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                        -- apertura nuovo iter
                                        INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                        (ID_ITER_PROCEDIMENTO_GRUPPO, ID_PROCEDIMENTO, ID_STATO_OGGETTO, CODICE_RAGGRUPPAMENTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                        VALUES
                                        (SEQ_NEMBO_T_ITER_PROCEDIMEN_GR.NEXTVAL,nIdProcedimento,nIdStatoNuovoGruppo,nCodiceRaggruppamento,SYSDATE,NULL,pExtIdUtente,NULL/*pNoteGruppo*/); -- ##
                                END IF;
                    END IF;
            END IF;

--            -- aggiorno gli importi. Lo faccio solo se esistono determinati quadri
--            IF (PCK_nembo_UTILITY.PRESENZAQUADRO(nIdBando,nIdLegameGruppoOggetto,'QECON') OR PCK_nembo_UTILITY.PRESENZAQUADRO(nIdBando,nIdLegameGruppoOggetto,'ACTFN') OR PCK_nembo_UTILITY.PRESENZAQUADRO(nIdBando,nIdLegameGruppoOggetto,'ACTSP')  OR PCK_nembo_UTILITY.PRESENZAQUADRO(nIdBando,nIdLegameGruppoOggetto,'DTANT')  OR PCK_nembo_UTILITY.PRESENZAQUADRO(nIdBando,nIdLegameGruppoOggetto,'DINTP')) THEN
--                    -- recupero l'esito
--                    vCodiceEsito:=PCK_nembo_UTILITY.RendiCodiceEsito(nIdEsitoNuovo);
--                    IF NOT AggiornaImportiLivelli(nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nCodiceRaggruppamento, 'N', vCodiceEsito, vCodiceOggetto, nRisultato, vMessaggio) THEN
--                            -- lancio eccezione
--                            pMessaggio:='Errore durante il richiamo della procedura AggiornaImportiLivelli: '||vMessaggio;
--                            RAISE ERRORE_STATI;
--                    END IF;
--            END IF;


EXCEPTION
  WHEN ERRORE THEN
    pRisultato := 1;
  WHEN ERRORE_STATI THEN
    pRisultato := 1;
   WHEN ERR_STATO_GRUPPO_PREC THEN
    pRisultato := 1;
    pMessaggio:='Errore nel recupero dello stato precedente del gruppo';
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nella procedura MainApprovazione. Codice Errore = '||
    PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
END MainApprovazione;

  -- avvio istruttoria - componente che gestisce il cambio stato
  PROCEDURE MainAvvioIstruttoria(pIdProcedimentoOggetto      nembo_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                                  pNote                       NEMBO_T_ITER_PROCEDIMENTO_OGGE.NOTE%TYPE,
                                                  pNoteGruppo             NEMBO_T_ITER_PROCEDIMENTO_GRUP.NOTE%TYPE,
                                                  pExtIdUtente                NUMBER,
                                                  pRisultato              OUT NUMBER,
                                                  pMessaggio              OUT VARCHAR2) IS

   nIdProcedimento                      nembo_T_PROCEDIMENTO.ID_PROCEDIMENTO%TYPE;
   nIdLegameGruppoOggetto         nembo_T_PROCEDIMENTO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE;
   nIdBando                                 nembo_T_PROCEDIMENTO.ID_BANDO%TYPE;
   nCodiceRaggruppamento           nembo_T_PROCEDIMENTO_OGGETTO.CODICE_RAGGRUPPAMENTO%TYPE;

   ERRORE_STATI                               EXCEPTION;
   ERR_STATO_GRUPPO_PREC             EXCEPTION;

   nIdOperazione                          CONSTANT PLS_INTEGER:=6; -- avvio istruttoria

   nIdStatoNuovoProcedimento        nembo_T_PROCEDIMENTO.ID_STATO_OGGETTO%TYPE;
   nIdStatoNuovoOggetto                NEMBO_T_ITER_PROCEDIMENTO_OGGE.ID_STATO_OGGETTO%TYPE;
   nIdStatoNuovoGruppo                 NEMBO_T_ITER_PROCEDIMENTO_GRUP.ID_STATO_OGGETTO%TYPE;
   nIdEsitoNuovo                            nembo_D_ESITO.ID_ESITO%TYPE;
   nRisultato                                  NUMBER;
   vMessaggio                               VARCHAR2(250);

BEGIN

            pRisultato := 0;
            pMessaggio := '';

            -- ricavo i parametri che mi servono per invocare il MainGestioneStati e altre cose utili
            SELECT PP.ID_PROCEDIMENTO, PO.ID_LEGAME_GRUPPO_OGGETTO, PP.ID_BANDO, PO.CODICE_RAGGRUPPAMENTO
            INTO nIdProcedimento, nIdLegameGruppoOggetto, nIdBando, nCodiceRaggruppamento
            FROM nembo_T_PROCEDIMENTO_OGGETTO PO, nembo_T_PROCEDIMENTO PP
            WHERE PO.ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
            AND PO.ID_PROCEDIMENTO = PP.ID_PROCEDIMENTO;

            -- lancio il moloch
            PCK_nembo_GESTIONE_PROC_OGGETT.MAINGESTIONESTATI ( nIdProcedimento, pIdProcedimentoOggetto, nIdLegameGruppoOggetto, nIdOperazione, NULL/*pIdEsito*/, NULL/*pCodAttore*/, nIdBando, nCodiceRaggruppamento, nIdStatoNuovoProcedimento, nIdStatoNuovoOggetto, nIdStatoNuovoGruppo, nIdEsitoNuovo, nRisultato, vMessaggio );

            IF nRisultato <> 0 THEN
                    -- lancio eccezione
                    pMessaggio:='Errore durante il richiamo della procedura MainGestioneStati: '||vMessaggio;
                    RAISE ERRORE_STATI;
            ELSE
                    -- aggiorno
                    IF NVL(nIdStatoNuovoProcedimento,-10) <> 0 THEN
                                -- a) stato del procedimento
                                UPDATE nembo_T_PROCEDIMENTO SET ID_STATO_OGGETTO = nIdStatoNuovoProcedimento,
                                                                                EXT_ID_UTENTE_AGGIORNAMENTO = pExtIdUtente,
                                                                                DATA_ULTIMO_AGGIORNAMENTO   = SYSDATE
                                WHERE ID_PROCEDIMENTO = nIdProcedimento;
                    END IF;
                    IF NVL(nIdStatoNuovoOggetto,-10) <> 0 THEN
                                -- b) stato dell'oggetto
                                -- chiusura vecchio iter
                                UPDATE NEMBO_T_ITER_PROCEDIMENTO_OGGE SET DATA_FINE = SYSDATE
                                WHERE ID_PROCEDIMENTO_OGGETTO = pIdProcedimentoOggetto
                                AND DATA_FINE IS NULL;
                                -- apertura nuovo iter
                                INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_OGGE
                                (ID_ITER_PROCEDIMENTO_OGGETT, ID_PROCEDIMENTO_OGGETTO, ID_STATO_OGGETTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                VALUES
                                (SEQ_NEMBO_T_ITER_PROCEDIMEN_OG.NEXTVAL,pIdProcedimentoOggetto,nIdStatoNuovoOggetto,SYSDATE,NULL,pExtIdUtente,pNote);
                    END IF;
                    IF NVL(nIdStatoNuovoGruppo,-10) <> 0 THEN
                                -- d) stato del gruppo
                                IF NVL(nIdStatoNuovoGruppo,-10) = -1 THEN
                                        IF NOT RiportaStatoPrecIterGruppo(nIdProcedimento,nCodiceRaggruppamento) THEN
                                                RAISE ERR_STATO_GRUPPO_PREC;
                                        END IF;
                                ELSE
                                        -- chiusura vecchio iter
                                        UPDATE NEMBO_T_ITER_PROCEDIMENTO_GRUP SET DATA_FINE = SYSDATE
                                        WHERE ID_PROCEDIMENTO = nIdProcedimento
                                        AND DATA_FINE IS NULL
                                        AND CODICE_RAGGRUPPAMENTO = nCodiceRaggruppamento;
                                        -- apertura nuovo iter
                                        INSERT INTO NEMBO_T_ITER_PROCEDIMENTO_GRUP
                                        (ID_ITER_PROCEDIMENTO_GRUPPO, ID_PROCEDIMENTO, ID_STATO_OGGETTO, CODICE_RAGGRUPPAMENTO, DATA_INIZIO,DATA_FINE,EXT_ID_UTENTE_AGGIORNAMENTO,NOTE)
                                        VALUES
                                        (SEQ_NEMBO_T_ITER_PROCEDIMEN_GR.NEXTVAL,nIdProcedimento,nIdStatoNuovoGruppo,nCodiceRaggruppamento,SYSDATE,NULL,pExtIdUtente,NULL/*pNoteGruppo*/); -- ##
                                END IF;
                    END IF;
            END IF;

EXCEPTION
  WHEN ERRORE_STATI THEN
    pRisultato := 1;
   WHEN ERR_STATO_GRUPPO_PREC THEN
    pRisultato := 1;
    pMessaggio:='Errore nel recupero dello stato precedente del gruppo';
  WHEN OTHERS THEN
    pRisultato := 1;
    pMessaggio := 'Errore di sistema nella procedura MainAvvioIstruttoria. Codice Errore = '||
    PCK_nembo_UTILITY.InsLog(SQLERRM||' - RIGA = '||dbms_utility.FORMAT_ERROR_BACKTRACE);
END MainAvvioIstruttoria;


END PCK_NEMBO_GESTIONE_PROC_OGGETT;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_GESTIONE_PROC_OGGETT" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_GESTIONE_PROC_OGGETT" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package Body PCK_NEMBO_SERVIZI_X_FRUITORI
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "NEMBO"."PCK_NEMBO_SERVIZI_X_FRUITORI" IS

  /* procedura che dati:
        - id azienda
        - anno campagna
        - dichiarazione di consistenza
     verifica che non esistano sul PSR procedimenti
                - non annullati
                - di tipo DAP/ARTxx
                - della stessa azienda
                - dello stesso anno campagna
                - di dich cons diversa
                - considerando solo gli oggetti non annullati piu' recenti (max data inizio) per ciascun procedimento
    Restituisce 0 se non ha trovato altri procedimenti, 1 se ne ha trovati ed in questo casi oltre al messaggio d'errore
    restituisce anche la descrizione del bando (usata dal controllo IPR07 di PSR)
    */
    PROCEDURE VerificaProcDDCDifferente(pExtIdAzienda        NEMBO_T_PROCEDIMENTO_AZIENDA.EXT_ID_AZIENDA%TYPE,
                                                          pAnnoCampagna    NEMBO_D_BANDO.ANNO_CAMPAGNA%TYPE,
                                                          pExtIdDichCons       NEMBO_T_PROCEDIMENTO_OGGETTO.EXT_ID_DICHIARAZIONE_CONSISTEN%TYPE,
                                                          pDescBando           OUT VARCHAR2,
                                                          pRisultato               OUT NUMBER,
                                                          pMessaggio            OUT VARCHAR2
                                                          ) IS

        TbOggDichConsDiff       typTbOggDichConsDiff;

    BEGIN
                pRisultato := 0;
                pMessaggio := NULL;
                pDescBando:=NULL;

                SELECT BB.DENOMINAZIONE DESC_BANDO
                BULK COLLECT INTO TbOggDichConsDiff
                FROM NEMBO_T_PROCEDIMENTO_OGGETTO PO,  NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO, NEMBO_D_OGGETTO O,
                          NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO, NEMBO_T_PROCEDIMENTO PP, NEMBO_T_PROCEDIMENTO_AZIENDA PROCAZ, NEMBO_D_BANDO BB
                WHERE PO.ID_PROCEDIMENTO = PP.ID_PROCEDIMENTO
                AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
                AND LGO.ID_OGGETTO = O.ID_OGGETTO
                AND O.CODICE IN ('DAP', 'DP', 'ART15', 'ART3')
                AND PP.ID_PROCEDIMENTO=PROCAZ.ID_PROCEDIMENTO
                AND PROCAZ.DATA_FINE IS NULL
                AND PP.ID_BANDO = BB.ID_BANDO
                AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
                AND IPO.DATA_FINE IS NULL
                AND PROCAZ.EXT_ID_AZIENDA= pExtIdAzienda
                AND BB.ANNO_CAMPAGNA = pAnnoCampagna
                AND PO.EXT_ID_DICHIARAZIONE_CONSISTEN <> pExtIdDichCons
                AND IPO.ID_STATO_OGGETTO <= 90
                AND PP.ID_STATO_OGGETTO <= 90
                AND PO.DATA_INIZIO = (
                                    SELECT MAX(POSQ.DATA_INIZIO)
                                    FROM NEMBO_T_PROCEDIMENTO_OGGETTO POSQ,  NEMBO_R_LEGAME_GRUPPO_OGGETTO LGOSQ, NEMBO_D_OGGETTO OSQ, NEMBO_T_ITER_PROCEDIMENTO_OGGE IPOSQ
                                    WHERE POSQ.ID_PROCEDIMENTO =  PO.ID_PROCEDIMENTO
                                    AND POSQ.ID_LEGAME_GRUPPO_OGGETTO = LGOSQ.ID_LEGAME_GRUPPO_OGGETTO
                                    AND LGOSQ.ID_OGGETTO = OSQ.ID_OGGETTO
                                    AND OSQ.CODICE IN ('DAP', 'DP', 'ART15', 'ART3')
                                    AND POSQ.ID_PROCEDIMENTO_OGGETTO = IPOSQ.ID_PROCEDIMENTO_OGGETTO
                                    AND IPOSQ.DATA_FINE IS NULL
                                    AND IPOSQ.ID_STATO_OGGETTO <= 90
                                    )
                  ORDER BY PO.DATA_INIZIO DESC;

                    IF TbOggDichConsDiff.COUNT > 0 THEN
                            -- ce ne sono
                            pRisultato := 1;
                            pMessaggio:='Esiste un''altra domanda attiva della stessa azienda collegata ad una dichiarazione di consistenza differente';
                            pDescBando:=TbOggDichConsDiff(TbOggDichConsDiff.FIRST).DESC_BANDO;
                    END IF;


    END VerificaProcDDCDifferente;

END PCK_NEMBO_SERVIZI_X_FRUITORI;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_SERVIZI_X_FRUITORI" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_SERVIZI_X_FRUITORI" TO "NEMBO_RO";
--------------------------------------------------------
--  DDL for Package Body PCK_NEMBO_UTILITY
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "NEMBO"."PCK_NEMBO_UTILITY" IS

FUNCTION InsLog(pDescrizioneErrore  NEMBO_T_LOG.DESCRIZIONE_ERRORE%TYPE) RETURN NEMBO_T_LOG.ID_LOG%TYPE IS

  PRAGMA  AUTONOMOUS_TRANSACTION;
  nIdLog  NEMBO_T_LOG.ID_LOG%TYPE := SEQ_NEMBO_T_LOG.NEXTVAL;
BEGIN
  INSERT INTO NEMBO_T_LOG
  (ID_LOG, DESCRIZIONE_ERRORE)
  VALUES
  (nIdLog,pDescrizioneErrore);

  COMMIT;

  RETURN nIdLog;
END InsLog;

-- ritorna il CUAA in base all'id_azienda
FUNCTION ReturnCuaa(pExtIdAzienda  SMRGAA_V_DATI_ANAGRAFICI.ID_AZIENDA%TYPE) RETURN SMRGAA_V_DATI_ANAGRAFICI.CUAA%TYPE IS
 vCuaa  SMRGAA_V_DATI_ANAGRAFICI.CUAA%TYPE;
BEGIN
  SELECT AA.CUAA
  INTO   vCuaa
  FROM   SMRGAA_V_DATI_ANAGRAFICI AA
  WHERE  AA.ID_AZIENDA         = pExtIdAzienda
  AND    AA.DATA_FINE_VALIDITA IS NULL;

  RETURN vCuaa;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END ReturnCuaa;

-- Ritorna l'elenco dei valori dei parametri
FUNCTION ReturnValoriParametri(pValore      NEMBO_T_VALORI_PARAMETRI.VALORE%TYPE,
                               pSeparatore  VARCHAR2) RETURN tblParametri IS
                               
  Parametri  tblParametri;
  nPosI      PLS_INTEGER := 0;
  nPosj      PLS_INTEGER := 0;
  vElemCorr  VARCHAR2(4000);
  nPosK      PLS_INTEGER := 0;
BEGIN
  LOOP
    nPosJ := nPosJ + 1;
    nPosI := INSTR(pValore,pSeparatore,1,nPosJ);
    EXIT WHEN nPosI = 0;

    Parametri(nPosJ) := SUBSTR(pValore,nPosK+1,(nPosI-1)-nPosK);
    nPosK            := nPosI;
  END LOOP;
  
  RETURN Parametri;
END ReturnValoriParametri;

-- funzione che rende la capienza del dato bando per il dato id livello
FUNCTION RendiCapienzaBandoLiv(par_nIdProcedimento      NEMBO_R_PROCEDIMENTO_LIVELLO.ID_PROCEDIMENTO%TYPE,
                               par_nIdBando             NEMBO_D_BANDO.ID_BANDO%TYPE,
                               par_nIdLivello           NEMBO_R_PROCEDIMENTO_LIVELLO.ID_LIVELLO%TYPE) RETURN NEMBO_T_RISORSE_LIVELLO_BANDO.RISORSE_ATTIVATE%TYPE IS

nRisorseAttivate        NEMBO_T_RISORSE_LIVELLO_BANDO.RISORSE_ATTIVATE%TYPE;                               
nEconomia               NEMBO_T_ECONOMIA.IMPORTO_ECONOMIA%TYPE;
nTotContributoConcesso  NEMBO_R_PROCEDIMENTO_LIVELLO.CONTRIBUTO_CONCESSO%TYPE;
                               
BEGIN                                

        -- sommo le risorse attivate per il dato livello/bando
        SELECT NVL(SUM(RISORSE_ATTIVATE),0)
        INTO nRisorseAttivate
        FROM NEMBO_T_RISORSE_LIVELLO_BANDO
        WHERE ID_LIVELLO = par_nIdLivello
        AND ID_BANDO = par_nIdBando;
        
        -- recupero le economie
        SELECT NVL(SUM(EKOM.IMPORTO_ECONOMIA),0)
        INTO nEconomia
        FROM NEMBO_T_RISORSE_LIVELLO_BANDO RLB, NEMBO_T_ECONOMIA EKOM
        WHERE RLB.ID_LIVELLO = par_nIdLivello
        AND RLB.ID_BANDO = par_nIdBando
        AND RLB.ID_RISORSE_LIVELLO_BANDO = EKOM.ID_RISORSE_LIVELLO_BANDO(+);
        
        -- recupero i contributi concessi sui procedimenti del bando esluso il corrente
        SELECT NVL(SUM(NVL(PROLIV.CONTRIBUTO_CONCESSO,0)),0)
        INTO nTotContributoConcesso
        FROM NEMBO_R_PROCEDIMENTO_LIVELLO PROLIV, NEMBO_T_PROCEDIMENTO PP
        WHERE PROLIV.ID_PROCEDIMENTO = PP.ID_PROCEDIMENTO
        AND PP.ID_PROCEDIMENTO <> par_nIdProcedimento
        AND PROLIV.ID_LIVELLO = par_nIdLivello
        AND PP.ID_BANDO = par_nIdBando
        AND PP.ID_STATO_OGGETTO BETWEEN 10 AND 90
        AND PROLIV.DATA_AMMISSIONE IS NOT NULL;
        
        RETURN(nRisorseAttivate-nEconomia-nTotContributoConcesso);        

END RendiCapienzaBandoLiv;

-- recupero se l'istanza e' stata creata dal beneficiario
FUNCTION ReturnIstanzaCreataDaBenef(pIdLegameGruppoOggetto NEMBO_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE) RETURN BOOLEAN IS

nCont   PLS_INTEGER;
BEGIN

  SELECT COUNT(*)
  INTO nCont
  FROM NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO, NEMBO_D_OGGETTO O 
  WHERE LGO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
  AND LGO.ID_OGGETTO=O.ID_OGGETTO
  AND O.FLAG_ISTANZA = 'S';
  
  IF nCont > 0 THEN
        RETURN(TRUE);
  ELSE
        RETURN(FALSE);  
  END IF;
  
END ReturnIstanzaCreataDaBenef;  

-- ritorna la dichiarazione di consistenza in base all'id_azienda; esclude le dichiarazioni oltre una certa data
FUNCTION ReturnDichConsistenza(pExtIdAzienda        SMRGAA_V_DICH_CONSISTENZA.ID_AZIENDA%TYPE,
                                                  pDataRiferimento   SMRGAA_V_DICH_CONSISTENZA.DATA%TYPE) RETURN SMRGAA_V_DICH_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE IS
 
 nIdDichCons  SMRGAA_V_DICH_CONSISTENZA.ID_DICHIARAZIONE_CONSISTENZA%TYPE;
BEGIN
            BEGIN 
                    SELECT DISTINCT FIRST_VALUE(ID_DICHIARAZIONE_CONSISTENZA) OVER (ORDER BY DATA DESC)
                    INTO nIdDichCons
                    FROM SMRGAA_V_DICH_CONSISTENZA DC
                    WHERE DC.ID_AZIENDA         = pExtIdAzienda
                    AND DC.NUMERO_PROTOCOLLO IS NOT NULL AND DC.DATA_PROTOCOLLO IS NOT NULL
                    AND DC.ID_PROCEDIMENTO      = cnIdProcedimentoNEMBO
                    AND NVL(DC.ESCLUSO,'-')='N'
                   AND DC.DATA <= NVL(pDataRiferimento,SYSDATE);
              EXCEPTION
                      WHEN NO_DATA_FOUND THEN
                                 BEGIN
                                         SELECT DISTINCT FIRST_VALUE(ID_DICHIARAZIONE_CONSISTENZA) OVER (ORDER BY DATA)
                                        INTO nIdDichCons
                                        FROM SMRGAA_V_DICH_CONSISTENZA DC
                                        WHERE DC.ID_AZIENDA         = pExtIdAzienda
                                        AND DC.NUMERO_PROTOCOLLO IS NOT NULL AND DC.DATA_PROTOCOLLO IS NOT NULL
                                        AND DC.ID_PROCEDIMENTO      = cnIdProcedimentoNEMBO
                                        AND NVL(DC.ESCLUSO,'-')='N'
                                       AND DC.DATA > NVL(pDataRiferimento,SYSDATE);
                                  EXCEPTION
                                        WHEN OTHERS THEN
                                                RETURN NULL;
                                  END;              
              END;
  
  RETURN nIdDichCons;
EXCEPTION  
  WHEN OTHERS THEN 
    RETURN NULL;
  END ReturnDichConsistenza;
  
  -- funzione che recupera l'ultimo oggetto chiuso valido legato al procedimento precedente all'oggetto passato in input data la sequenza di codici oggetti intervallati da virgola (es. DAP,ART15,ART3)
-- valori possibili del selettore:
-- - 1: DAP,DP,ART3,ART15
FUNCTION ReturnLastObjMinorIpse(par_nIdProcedimento   NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE,
                                par_nIdProcedimentoOggetto     NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO_OGGETTO%TYPE,
                                pSelettore                                   PLS_INTEGER,
                                pRisultato                                  OUT NUMBER,
                                pMessaggio                               OUT VARCHAR2) RETURN NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE IS

  ERR_SELETTORE           EXCEPTION; 

nIdDatiProcedimento         NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE;
vRaggruppamento             VARCHAR2(100); 

  -- elenco oggetti precedenti
  vElencoOggettiPrec                  VARCHAR2(250);  

dInizio                         NEMBO_T_PROCEDIMENTO_OGGETTO.DATA_INIZIO%TYPE;

BEGIN

        pRisultato := 0;
        pMessaggio := '';    

        CASE pSelettore
          WHEN 1 THEN 
                 vElencoOggettiPrec:='DAP,DP,ART3,ART15';      
          ELSE  
                RAISE ERR_SELETTORE;
        END CASE;

        -- recupero la data inizio dell'oggetto passato in input
        SELECT DATA_INIZIO
        INTO dInizio
        FROM NEMBO_T_PROCEDIMENTO_OGGETTO
        WHERE ID_PROCEDIMENTO_OGGETTO = par_nIdProcedimentoOggetto;

        vRaggruppamento:=TRIM(vElencoOggettiPrec);

        BEGIN
                WITH OGGETTI_CONCATENATI AS
                   ( SELECT vRaggruppamento AS OggettiConc,
                            LEVEL AS pos,
                            SUBSTR(vRaggruppamento,ROWNUM,1) AS CH,
                            COUNT(CASE WHEN SUBSTR(vRaggruppamento,ROWNUM,1) = ',' THEN '#' END)
                            OVER (ORDER BY LEVEL) AS section
                       FROM dual
                 CONNECT BY LEVEL <= LENGTH(vRaggruppamento)),
                     OGGETTI AS
                   ( SELECT SUBSTR(OggettiConc,MIN(pos), 1 + MAX(pos) - MIN(pos)) AS CODICE_OGGETTO
                        FROM OGGETTI_CONCATENATI
                      WHERE CH <> ','
                   GROUP BY OggettiConc,section )     
                        SELECT DISTINCT FIRST_VALUE(DP.ID_DATI_PROCEDIMENTO) OVER (ORDER BY PO.DATA_FINE DESC)
                        INTO nIdDatiProcedimento
                        FROM NEMBO_T_PROCEDIMENTO_OGGETTO PO,  NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO, NEMBO_D_OGGETTO O, NEMBO_T_DATI_PROCEDIMENTO DP, 
                                  NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
                        WHERE PO.ID_PROCEDIMENTO             = par_nIdProcedimento
                        AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
                        AND LGO.ID_OGGETTO = O.ID_OGGETTO
                        AND O.CODICE IN (SELECT CODICE_OGGETTO FROM OGGETTI)
                        AND PO.ID_PROCEDIMENTO_OGGETTO = DP.ID_PROCEDIMENTO_OGGETTO
                        AND DP.ID_PROCEDIMENTO IS NULL
                        AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
                        AND IPO.DATA_FINE IS NULL
                        AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90
                        AND PO.DATA_FINE IS NOT NULL
                        AND PO.DATA_INIZIO < dInizio;
                EXCEPTION
                        WHEN NO_DATA_FOUND THEN
                                pRisultato:=1;
                                pMessaggio:='Oggetto precedente non trovato';
                                RETURN(NULL);
                        WHEN OTHERS THEN
                                pRisultato:=1;
                                pMessaggio:='Errore nella ricerca dell''Oggetto precedente';
                                RETURN(NULL);
                END;
                
                RETURN(nIdDatiProcedimento);

EXCEPTION
  WHEN ERR_SELETTORE THEN
        pRisultato:=1;
        pMessaggio:='Selettore non previsto';
END ReturnLastObjMinorIpse; 

-- funzione che recupera l'ultimo oggetto chiuso valido legato al procedimento data la sequenza di codici oggetti intervallati da virgola (es. DAP,ART15,ART3)
FUNCTION ReturnLastObj(par_nIdProcedimento   NEMBO_T_PROCEDIMENTO_OGGETTO.ID_PROCEDIMENTO%TYPE,
                                     par_nElencoOggetti       VARCHAR2) RETURN NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE IS

nIdDatiProcedimento         NEMBO_T_DATI_PROCEDIMENTO.ID_DATI_PROCEDIMENTO%TYPE;
vRaggruppamento             VARCHAR2(100); 

BEGIN

        vRaggruppamento:=TRIM(par_nElencoOggetti);

        WITH OGGETTI_CONCATENATI AS
           ( SELECT vRaggruppamento AS OggettiConc,
                    LEVEL AS pos,
                    SUBSTR(vRaggruppamento,ROWNUM,1) AS CH,
                    COUNT(CASE WHEN SUBSTR(vRaggruppamento,ROWNUM,1) = ',' THEN '#' END)
                    OVER (ORDER BY LEVEL) AS section
               FROM dual
         CONNECT BY LEVEL <= LENGTH(vRaggruppamento)),
             OGGETTI AS
           ( SELECT SUBSTR(OggettiConc,MIN(pos), 1 + MAX(pos) - MIN(pos)) AS CODICE_OGGETTO
                FROM OGGETTI_CONCATENATI
              WHERE CH <> ','
           GROUP BY OggettiConc,section )     
                SELECT DISTINCT FIRST_VALUE(DP.ID_DATI_PROCEDIMENTO) OVER (ORDER BY PO.DATA_FINE DESC)
                INTO nIdDatiProcedimento
                FROM NEMBO_T_PROCEDIMENTO_OGGETTO PO,  NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO, NEMBO_D_OGGETTO O, NEMBO_T_DATI_PROCEDIMENTO DP, 
                          NEMBO_T_ITER_PROCEDIMENTO_OGGE IPO
                WHERE PO.ID_PROCEDIMENTO             = par_nIdProcedimento
                AND PO.ID_LEGAME_GRUPPO_OGGETTO = LGO.ID_LEGAME_GRUPPO_OGGETTO
                AND LGO.ID_OGGETTO = O.ID_OGGETTO
                AND O.CODICE IN (SELECT CODICE_OGGETTO FROM OGGETTI)
                AND PO.ID_PROCEDIMENTO_OGGETTO = DP.ID_PROCEDIMENTO_OGGETTO
                AND DP.ID_PROCEDIMENTO IS NULL
                AND PO.ID_PROCEDIMENTO_OGGETTO = IPO.ID_PROCEDIMENTO_OGGETTO
                AND IPO.DATA_FINE IS NULL
                AND IPO.ID_STATO_OGGETTO BETWEEN 10 AND 90
                AND PO.DATA_FINE IS NOT NULL;                
                
                RETURN(nIdDatiProcedimento);

EXCEPTION
               WHEN NO_DATA_FOUND THEN
                    RETURN(NULL);
                WHEN OTHERS THEN
                    RETURN(NULL);                    
END ReturnLastObj;    

-- In base ai parametri di input verifica la presenza di un quadro
FUNCTION PresenzaQuadro(pIdBando                NEMBO_D_BANDO.ID_BANDO%TYPE,
                        pIdLegameGruppoOggetto  NEMBO_R_LEGAME_GRUPPO_OGGETTO.ID_LEGAME_GRUPPO_OGGETTO%TYPE,
                        pCodQuadro              NEMBO_D_QUADRO.CODICE%TYPE) RETURN BOOLEAN IS

  nCont  SIMPLE_INTEGER := 0;
BEGIN
  SELECT COUNT(*)
  INTO   nCont
  FROM   NEMBO_R_BANDO_OGGETTO BO,NEMBO_R_QUADRO_OGGETTO QO,NEMBO_D_QUADRO Q,NEMBO_R_LEGAME_GRUPPO_OGGETTO LGO,
         NEMBO_R_BANDO_OGGETTO_QUADRO BOQ
  WHERE  BO.ID_BANDO                  = pIdBando
  AND    BO.ID_LEGAME_GRUPPO_OGGETTO  = LGO.ID_LEGAME_GRUPPO_OGGETTO
  AND    Q.CODICE                     = pCodQuadro
  AND    Q.ID_QUADRO                  = QO.ID_QUADRO
  AND    LGO.ID_LEGAME_GRUPPO_OGGETTO = pIdLegameGruppoOggetto
  AND    QO.ID_OGGETTO                = LGO.ID_OGGETTO
  AND    BOQ.ID_BANDO_OGGETTO         = BO.ID_BANDO_OGGETTO
  AND    BOQ.ID_QUADRO_OGGETTO        = QO.ID_QUADRO_OGGETTO;  
  
  IF nCont != 0 THEN
    RETURN TRUE;
  ELSE
    RETURN FALSE;
  END IF;
END PresenzaQuadro;

-- verifica check digit
FUNCTION FncCheckDigit(pCUAA SMRGAA_V_DATI_ANAGRAFICI.CUAA%TYPE DEFAULT NULL ) RETURN BOOLEAN IS
  set1    varchar2(36) := '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  set2    varchar2(36) := 'ABCDEFGHIJABCDEFGHIJKLMNOPQRSTUVWXYZ';
  setpari varchar2(26) := 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  setdisp varchar2(26) := 'BAKPLCQDREVOSFTGUHMINJWZYX';
  j       integer      := 0;
  tmp     integer      := 0;
  rc      boolean      := false;
  
result              integer      := 0;
result2             integer      := 0;
clenPIVA   constant integer      := 11;
clenCF     constant integer      := 16;  
err_num             varchar2(15);
err_msg             varchar2(85);
begin

  result  := 0;
  result2 := 0;

  if length( pCUAA ) = clenPIVA then
    -- Case A) It's Partita IVA
    j := 1;
    while j <= (clenPIVA - 1) loop
      result := result + ( ascii( substr(pCUAA,j,1) ) - ascii('0') );
      j := j + 2;
    end loop;

    j := 2;
    while j <= (clenPIVA - 1) loop
      result2 := 2 * ( ascii( substr(pCUAA,j,1) ) - ascii('0') );
      if result2 > 9 then
        result2 := result2 - 9;
      end if;
      result := result + result2;
      j := j + 2;
    end loop;
    if mod(10 - mod(result,10),10) != ( ascii( substr(pCUAA,clenPIVA,1) ) - ascii('0') ) then
      rc := false; -- It's incorrect
    else
      rc := true; -- It's correct
    end if;

  elsif length( pCUAA ) = clenCF then
    -- Case B) It's Codice Fiscale
    j := 2;
    while j <= (clenCF -1) loop
      tmp   := instr(setpari , substr(set2, instr(set1, substr(pCUAA,j,1) ),1) ) -1;
      result := result + tmp;
      j := j + 2;
    end loop;

    j := 1;
    while j <= (clenCF - 1)  loop
      tmp   := instr(setdisp , substr(set2, instr(set1, substr(pCUAA,j,1) ),1) ) -1;
      result := result + tmp;
      j := j + 2;
    end loop;

    if MOD(result,26) = ( ascii( substr(pCUAA,16,1) ) - ascii('A') ) then
      rc := true; -- It's correct
    else
      -- It's incorrect
      rc := false;
    end if;
  else
    -- Case C) What's this?!?
    rc := false;
  end if;

  return rc;
exception
  when others then
    -- Error on Function
    err_num := to_char(sqlcode);
    err_msg := substr(sqlerrm, 1, 100);
end fncCheckDigit;

  -- restituisce il check digit data la stringa
FUNCTION ReturnCheckDigit(pStringa VARCHAR2) RETURN CHAR IS

bTrovato        BOOLEAN:=FALSE;
nStringaCurr            VARCHAR2(11);
BEGIN

        FOR INDICE IN 0 ..  9 LOOP
                                                                                                            
            nStringaCurr := pStringa || TO_CHAR(INDICE);
            IF ( fncCheckDigit( nStringaCurr ) ) THEN
               bTrovato := TRUE;
               RETURN(TO_CHAR(INDICE));
            END IF;
                                                                                                            
        END LOOP;
        
        RETURN(NULL);

END ReturnCheckDigit;

FUNCTION ReturnMsgErrore(pIdMessaggioErrore  NEMBO_D_MESSAGGIO_ERRORE.ID_MESSAGGIO_ERRORE%TYPE) RETURN NEMBO_D_MESSAGGIO_ERRORE.DESCRIZIONE%TYPE IS

  vDescrizione  NEMBO_D_MESSAGGIO_ERRORE.DESCRIZIONE%TYPE;
BEGIN
  SELECT DESCRIZIONE
  INTO   vDescrizione
  FROM   NEMBO_D_MESSAGGIO_ERRORE
  WHERE  ID_MESSAGGIO_ERRORE = pIdMessaggioErrore;
  
  RETURN vDescrizione;
EXCEPTION  
    WHEN NO_DATA_FOUND THEN
        RETURN '<Messaggio d''errore non configurato>';
END ReturnMsgErrore;

END PCK_NEMBO_UTILITY;

/

  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_UTILITY" TO "NEMBO_RW";
  GRANT EXECUTE ON "NEMBO"."PCK_NEMBO_UTILITY" TO "NEMBO_RO";
