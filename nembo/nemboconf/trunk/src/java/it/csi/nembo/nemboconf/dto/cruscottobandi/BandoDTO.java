package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboConstants;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class BandoDTO implements ILoggable
{

  private static final long            serialVersionUID = 4601339650061084885L;

  private long                         idBando;
  private long                         idBandoMaster;
  private long                         idTipoLivello;
  private String                       flagMaster;
  private String                       denominazione;
  private String                       annoCampagna;
  private String                       istruzioneSqlFiltro;
  private String                       descrizioneFiltro;
  private String                       descrTipoBando;
  private String                       codiceTipoBando;
  private String                       flagTitolaritaRegionale;
  private String                       flagDomandaMultipla;
  private String                       flagRendicontazioneDocSpesa;

  private String                       referenteBando;
  private String                       emailReferenteBando;
  private String                       flagRibassoInterventi;
  private Date                         dataInizio;
  private Date                         dataFine;

  private Vector<Long>                 vIdAmministrazioni;
  private Vector<Long>                 vIdTipiOperazioni;
  private Vector<Long>                 vIdAmbitiTematici;

  private boolean                      haveReport       = false;
  private boolean                      haveChart        = false;
  private boolean                      haveGraduatorie  = false;
  private boolean                      isBandoAttivo    = false;

  private List<LivelloDTO>             livelli;
  private List<SettoriDiProduzioneDTO> elencoSettori;
  private List<FocusAreaDTO>           elencoFocusArea;

  private List<FileAllegatoDTO>        allegati;
  private List<AmmCompetenzaDTO>       amministrazioniCompetenza;
  
  private Long idEventoCalamitoso;
  private String descEventoCalamitoso;
  private Long idCategoriaEvento;
  private String descCatEvento;
  private Date dataEvento;
  
  private int idProcedimentoAgricolo;
  
  

  public int getIdProcedimentoAgricolo() {
	return idProcedimentoAgricolo;
}

public void setIdProcedimentoAgricolo(int idProcedimentoAgricolo) {
	this.idProcedimentoAgricolo = idProcedimentoAgricolo;
}

public String getFlagRendicontazioneDocSpesa()
  {
    return flagRendicontazioneDocSpesa;
  }

  public void setFlagRendicontazioneDocSpesa(String flagRendicontazioneDocSpesa)
  {
    this.flagRendicontazioneDocSpesa = flagRendicontazioneDocSpesa;
  }

  private Long numeroAnniMantenimento;

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public String getDenominazione()
  {
    return denominazione;
  }
  public String getDenominazioneEscaped()
  {
	  return NemboUtils.STRING.safeHTMLText(denominazione);
  }

  public void setDenominazione(String denominazione)
  {
    this.denominazione = denominazione;
  }

  public String getAnnoCampagna()
  {
    return annoCampagna;
  }

  public void setAnnoCampagna(String annoCampagna)
  {
    this.annoCampagna = annoCampagna;
  }

  public String getDescrTipoBando()
  {
    return descrTipoBando;
  }

  public void setDescrTipoBando(String descrTipoBando)
  {
    this.descrTipoBando = descrTipoBando;
  }

  public String getFlagTitolaritaRegionale()
  {
    return flagTitolaritaRegionale;
  }

  public void setFlagTitolaritaRegionale(String flagTitolaritaRegionale)
  {
    this.flagTitolaritaRegionale = flagTitolaritaRegionale;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public String getDataInizioStr()
  {
    return NemboUtils.DATE.formatDateTime(dataInizio);
  }

  public String getDataInizioStrNoSec()
  {
    return NemboUtils.DATE.formatDate(dataInizio);
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public String getDataFineStr()
  {
    return NemboUtils.DATE.formatDateTime(dataFine);
  }

  public String getDataFineStrNoSec()
  {
    return NemboUtils.DATE.formatDate(dataFine);
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public Date getDataEvento() {
	return dataEvento;
  }
  
  public String getDataEventoStr() {
	return NemboUtils.DATE.formatDateTime(dataEvento);
  }
  
  public String getDataEventoStrNoSec() {
	return NemboUtils.DATE.formatDate(dataEvento);
  }

  public void setDataEvento(Date dataEvento) {	
	  this.dataEvento = dataEvento;
  }
  
  

public Long getIdEventoCalamitoso() {
	return idEventoCalamitoso;
}

public void setIdEventoCalamitoso(Long idEventoCalamitoso) {
	this.idEventoCalamitoso = idEventoCalamitoso;
}

public String getDescEventoCalamitoso() {
	return descEventoCalamitoso;
}

public void setDescEventoCalamitoso(String descEventoCalamitoso) {
	this.descEventoCalamitoso = descEventoCalamitoso;
}

public Long getIdCategoriaEvento() {
	return idCategoriaEvento;
}

public void setIdCategoriaEvento(Long idCategoriaEvento) {
	this.idCategoriaEvento = idCategoriaEvento;
}

public String getDescCatEvento() {
	return descCatEvento;
}

public void setDescCatEvento(String descCatEvento) {
	this.descCatEvento = descCatEvento;
}

public String getFlagMaster()
  {
    return flagMaster;
  }

  public void setFlagMaster(String flagMaster)
  {
    this.flagMaster = flagMaster;
  }

  public long getIdBandoMaster()
  {
    return idBandoMaster;
  }

  public void setIdBandoMaster(long idBandoMaster)
  {
    this.idBandoMaster = idBandoMaster;
  }

  public long getIdTipoLivello()
  {
    return idTipoLivello;
  }

  public void setIdTipoLivello(long idTipoLivello)
  {
    this.idTipoLivello = idTipoLivello;
  }

  public Vector<Long> getvIdAmministrazioni()
  {
    return vIdAmministrazioni;
  }

  public void setvIdAmministrazioni(Vector<Long> vIdAmministrazioni)
  {
    this.vIdAmministrazioni = vIdAmministrazioni;
  }

  public Vector<Long> getvIdTipiOperazioni()
  {
    return vIdTipiOperazioni;
  }

  public void setvIdTipiOperazioni(Vector<Long> vIdTipiOperazioni)
  {
    this.vIdTipiOperazioni = vIdTipiOperazioni;
  }

  public String getIstruzioneSqlFiltro()
  {
    return istruzioneSqlFiltro;
  }

  public void setIstruzioneSqlFiltro(String istruzioneSqlFiltro)
  {
    this.istruzioneSqlFiltro = istruzioneSqlFiltro;
  }

  public String getDescrizioneFiltro()
  {
    return descrizioneFiltro;
  }

  public void setDescrizioneFiltro(String descrizioneFiltro)
  {
    this.descrizioneFiltro = descrizioneFiltro;
  }

  public String getCodiceTipoBando()
  {
    return codiceTipoBando;
  }

  public void setCodiceTipoBando(String codiceTipoBando)
  {
    this.codiceTipoBando = codiceTipoBando;
  }

  // definiti per gestire il link al dettaglio bando dall'elenco bandi del
  // cruscotto
  public String getAzione()
  {
    return NemboConstants.PAGINATION.AZIONI.DETTAGLIO;
  }

  public String getAzioneHref()
  {
    return "datiidentificativi_" + idBando + ".do";
  }

  public String getTitleHref()
  {
    return "Vai al dettaglio";
  }

  public String getFlagDomandaMultipla()
  {
    return flagDomandaMultipla;
  }

  public void setFlagDomandaMultipla(String flagDomandaMultipla)
  {
    this.flagDomandaMultipla = flagDomandaMultipla;
  }

  public String getReferenteBando()
  {
    return referenteBando;
  }

  public void setReferenteBando(String referenteBando)
  {
    this.referenteBando = referenteBando;
  }

  public String getEmailReferenteBando()
  {
    return emailReferenteBando;
  }

  public void setEmailReferenteBando(String emailReferenteBando)
  {
    this.emailReferenteBando = emailReferenteBando;
  }

  public String getFlagRibassoInterventi()
  {
    return flagRibassoInterventi;
  }

  public void setFlagRibassoInterventi(String flagRibassoInterventi)
  {
    this.flagRibassoInterventi = flagRibassoInterventi;
  }

  public boolean isHaveReport()
  {
    return haveReport;
  }

  public void setHaveReport(boolean haveReport)
  {
    this.haveReport = haveReport;
  }

  public boolean isHaveChart()
  {
    return haveChart;
  }

  public void setHaveChart(boolean haveChart)
  {
    this.haveChart = haveChart;
  }

  public boolean isHaveGraduatorie()
  {
    return haveGraduatorie;
  }

  public void setHaveGraduatorie(boolean haveGraduatorie)
  {
    this.haveGraduatorie = haveGraduatorie;
  }

  public List<LivelloDTO> getLivelli()
  {
    return livelli;
  }

  public void setLivelli(List<LivelloDTO> livelli)
  {
    this.livelli = livelli;
  }

  public String getElencoIdLivelli()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += Long.toString(l.getIdLivello()) + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelliHtml()
  {
    // return elencoCodiciLivelliHtml;
    String htmlElenco = "";

    List<LivelloDTO> liv = this.getLivelli();
    int i = 0;
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        htmlElenco = htmlElenco + l.getCodice();

        if (i < livelli.size() - 1)
        {
          htmlElenco = htmlElenco + "<br>";
        }
        i++;
      }

    return htmlElenco;
  }

  public String getElencoCodiciLivelliMisureHtml()
  {
    // return elencoCodiciLivelliHtml;
    String htmlElenco = "";
    List<String> lCodici = new ArrayList<String>();
    List<LivelloDTO> liv = this.getLivelli();
    int i = 0;
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        if (!lCodici.contains(l.getCodiceLivello()))
        {
          htmlElenco = htmlElenco + l.getCodiceLivello();
          lCodici.add(l.getCodiceLivello());
          if (i < livelli.size() - 1)
          {
            htmlElenco = htmlElenco + "<br>";
          }
        }

        i++;
      }

    return htmlElenco;
  }

  public String getElencoIdLivelli2()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += Long.toString(l.getIdLivello()) + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelli()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodice() + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelliMisure()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceMisura() + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelliSottoMisure()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceSottoMisura() + "&&&";

      }
    return s;
  }

  public String getElencoCodiciLivelliOperazione()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getCodiceLivello() + "&&&";

      }
    return s;
  }

  public String getElencoDescrizioniLivelli()
  {
    String s = "&&&";

    List<LivelloDTO> liv = this.getLivelli();
    if (liv != null)
      for (LivelloDTO l : liv)
      {
        s += l.getDescrizione() + "&&&";

      }
    return s;
  }

  public boolean isBandoAttivo()
  {
    return isBandoAttivo;
  }

  public void setBandoAttivo(boolean isBandoAttivo)
  {
    this.isBandoAttivo = isBandoAttivo;
  }

  public List<SettoriDiProduzioneDTO> getElencoSettori()
  {
    return elencoSettori;
  }

  public String getElencoSettoriStr()
  {
    String s = "&&&";

    List<SettoriDiProduzioneDTO> liv = this.getElencoSettori();
    if (liv != null)
      for (SettoriDiProduzioneDTO l : liv)
      {
        s += l.getDescrizione() + "&&&";

      }
    return s;
  }

  public void setElencoSettori(List<SettoriDiProduzioneDTO> elencoSettori)
  {
    this.elencoSettori = elencoSettori;
  }

  public List<FocusAreaDTO> getElencoFocusArea()
  {
    return elencoFocusArea;
  }

  public String getElencoFocusAreaStr()
  {
    String s = "&&&";

    List<FocusAreaDTO> liv = this.getElencoFocusArea();
    if (liv != null)
      for (FocusAreaDTO l : liv)
      {
        s += l.getCodice() + "&&&";

      }
    return s;
  }

  public void setElencoFocusArea(List<FocusAreaDTO> elencoFocusArea)
  {
    this.elencoFocusArea = elencoFocusArea;
  }

  public List<FileAllegatoDTO> getAllegati()
  {
    return allegati;
  }

  public void setAllegati(List<FileAllegatoDTO> allegati)
  {
    this.allegati = allegati;
  }

  public List<AmmCompetenzaDTO> getAmministrazioniCompetenza()
  {
    return amministrazioniCompetenza;
  }

  public void setAmministrazioniCompetenza(
      List<AmmCompetenzaDTO> amministrazioniCompetenza)
  {
    this.amministrazioniCompetenza = amministrazioniCompetenza;
  }

  public String getAmministrazioniCompetenzaHtml()
  {
    String s = "<ul>";
    if (amministrazioniCompetenza != null)
      for (AmmCompetenzaDTO a : amministrazioniCompetenza)
      {
        if (a.getDenominazioneuno() != null
            && a.getDenominazioneuno().length() > 0)
        {
          s += "<li>" + a.getDescrizione() + " - " + a.getDenominazioneuno()
              + "</li>";
        }
        else
        {
          s += "<li>" + a.getDescrizione() + "</li>";
        }
      }

    return s + "</ul>";
  }

  public String getIdsAmministrazioniCompetenza()
  {
    String s = "&&&";
    if (amministrazioniCompetenza != null)
      for (AmmCompetenzaDTO a : amministrazioniCompetenza)
        s += a.getIdAmmCompetenza() + "&&&";
    return s;
  }

  public Vector<Long> getvIdAmbitiTematici()
  {
    return vIdAmbitiTematici;
  }

  public void setvIdAmbitiTematici(Vector<Long> vIdAmbitiTematici)
  {
    this.vIdAmbitiTematici = vIdAmbitiTematici;
  }

  public String getDettAmministrazioniHtml()
  {
    String s = "<ul>";
    if (amministrazioniCompetenza != null)
      for (AmmCompetenzaDTO a : amministrazioniCompetenza)
        s += "<li>" + a.getDenominazioneuno() + "</li>";
    return s + "</ul>";
  }

  public Long getNumeroAnniMantenimento()
  {
    return numeroAnniMantenimento;
  }

  public void setNumeroAnniMantenimento(Long numeroAnniMantenimento)
  {
    this.numeroAnniMantenimento = numeroAnniMantenimento;
  }

}
