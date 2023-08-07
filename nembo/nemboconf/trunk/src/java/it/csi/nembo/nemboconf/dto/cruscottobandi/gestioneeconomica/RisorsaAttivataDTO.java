package it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.cruscottobandi.AmmCompetenzaDTO;
import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class RisorsaAttivataDTO implements ILoggable
{

  private static final long      serialVersionUID = 4601339650061084885L;

  private long                   idRisorseLivelloBando;
  private long                   idLivello;
  private long                   pageIndex;
  private long                   idBando;
  private long                   idTipoImporto;
  private String                 descrizione;
  private String                 flagBloccato;
  private String                 flagAmmCompetenza;
  private String                 raggruppamento;
  private String                 codiceLivello;
  private boolean                risorsaEliminabile;
  private BigDecimal             risorseAttivate;
  private Date                   dataInizio;
  private Date                   dataFine;
  private List<AmmCompetenzaDTO> elencoAmmCompetenza;
  private List<EconomiaDTO>      elencoEconomie;
  private BigDecimal             oldImportoForLivello;

  public long getIdRisorseLivelloBando()
  {
    return idRisorseLivelloBando;
  }

  public void setIdRisorseLivelloBando(long idRisorseLivelloBando)
  {
    this.idRisorseLivelloBando = idRisorseLivelloBando;
  }

  public String getRaggruppamento()
  {
    return raggruppamento;
  }

  public void setRaggruppamento(String raggruppamento)
  {
    this.raggruppamento = raggruppamento;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getFlagBloccato()
  {
    return flagBloccato;
  }

  public boolean isFlagBloccato()
  {
    return "S".equals(flagBloccato);
  }

  public void setFlagBloccato(String flagBloccato)
  {
    this.flagBloccato = flagBloccato;
  }

  public String getCodiceLivello()
  {
    return codiceLivello;
  }

  public void setCodiceLivello(String codiceLivello)
  {
    this.codiceLivello = codiceLivello;
  }

  public BigDecimal getRisorseAttivate()
  {
    if (risorseAttivate == null)
      risorseAttivate = BigDecimal.ZERO;
    return risorseAttivate;
  }

  public String getRisorseAttivateStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(risorseAttivate, 2, true);
  }

  public void setRisorseAttivate(BigDecimal risorseAttivate)
  {
    this.risorseAttivate = risorseAttivate;
  }

  public Date getDataInizio()
  {
    return dataInizio;
  }

  public String getDataInizioStr()
  {
    return NemboUtils.DATE.formatDate(dataInizio);
  }

  public String getDataFineStr()
  {
    return NemboUtils.DATE.formatDate(dataFine);
  }

  public void setDataInizio(Date dataInizio)
  {
    this.dataInizio = dataInizio;
  }

  public Date getDataFine()
  {
    return dataFine;
  }

  public void setDataFine(Date dataFine)
  {
    this.dataFine = dataFine;
  }

  public long getPageIndex()
  {
    return pageIndex;
  }

  public void setPageIndex(long pageIndex)
  {
    this.pageIndex = pageIndex;
  }

  public String getFlagAmmCompetenza()
  {
    return flagAmmCompetenza;
  }

  public void setFlagAmmCompetenza(String flagAmmCompetenza)
  {
    this.flagAmmCompetenza = flagAmmCompetenza;
  }

  public List<AmmCompetenzaDTO> getElencoAmmCompetenza()
  {
    return elencoAmmCompetenza;
  }

  public void setElencoAmmCompetenza(List<AmmCompetenzaDTO> elencoAmmCompetenza)
  {
    this.elencoAmmCompetenza = elencoAmmCompetenza;
  }

  public List<EconomiaDTO> getElencoEconomie()
  {
    return elencoEconomie;
  }

  public void setElencoEconomie(List<EconomiaDTO> elencoEconomie)
  {
    this.elencoEconomie = elencoEconomie;
  }

  public BigDecimal getTotaleEconomie()
  {
    BigDecimal tot = new BigDecimal(0);
    if (elencoEconomie != null && elencoEconomie.size() > 0)
    {
      for (EconomiaDTO item : elencoEconomie)
      {
        tot = tot.add(item.getImportoEconomia());
      }
    }
    return tot;
  }

  public String getTotaleEconomieStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(getTotaleEconomie(), 2,
        true);
  }

  public String getTotaleEconomieFormatted()
  {
    DecimalFormat formatter = new DecimalFormat("###,###.00");
    return formatter
        .format(getTotaleEconomie().setScale(2, RoundingMode.HALF_UP));
  }

  public BigDecimal getTotaleRisorseDisponibili()
  {
    return getRisorseAttivate().subtract(getTotaleEconomie());
  }

  public boolean isRisorsaEliminabile()
  {
    return risorsaEliminabile;
  }

  public void setRisorsaEliminabile(boolean risorsaEliminabile)
  {
    this.risorsaEliminabile = risorsaEliminabile;
  }

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdTipoImporto()
  {
    return idTipoImporto;
  }

  public void setIdTipoImporto(long idTipoImporto)
  {
    this.idTipoImporto = idTipoImporto;
  }

  public BigDecimal getOldImportoForLivello()
  {
    return oldImportoForLivello;
  }

  public void setOldImportoForLivello(BigDecimal oldImportoForLivello)
  {
    this.oldImportoForLivello = oldImportoForLivello;
  }

}
