package it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class TipoImportoDTO implements ILoggable
{

  private static final long        serialVersionUID = 4601339650061084885L;

  private long                     idTipoImporto;
  private long                     idBando;
  private long                     idUtenteAggiornamento;
  private String                   descrizioneUtenteAggiornamento;
  private String                   descrizione;
  private String                   fondoPagamento;
  private Date                     dataUltimoAggiornamento;
  private List<RisorsaAttivataDTO> risorseAttivateList;

  public BigDecimal getTotRisorseAttivate()
  {
    BigDecimal tot = BigDecimal.ZERO;
    if (risorseAttivateList != null)
      for (RisorsaAttivataDTO r : risorseAttivateList)
        tot = tot.add(r.getRisorseAttivate());
    return tot;
  };

  public long getIdTipoImporto()
  {
    return idTipoImporto;
  }

  public void setIdTipoImporto(long idTipoImporto)
  {
    this.idTipoImporto = idTipoImporto;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getFondoPagamento()
  {
    return fondoPagamento;
  }

  public void setFondoPagamento(String fondoPagamento)
  {
    this.fondoPagamento = fondoPagamento;
  }

  public List<RisorsaAttivataDTO> getRisorseAttivateList()
  {
    return risorseAttivateList;
  }

  public void setRisorseAttivateList(
      List<RisorsaAttivataDTO> risorseAttivateList)
  {
    this.risorseAttivateList = risorseAttivateList;
  }

  public long getIdUtenteAggiornamento()
  {
    return idUtenteAggiornamento;
  }

  public void setIdUtenteAggiornamento(long idUtenteAggiornamento)
  {
    this.idUtenteAggiornamento = idUtenteAggiornamento;
  }

  public Date getDataUltimoAggiornamento()
  {
    return dataUltimoAggiornamento;
  }

  public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento)
  {
    this.dataUltimoAggiornamento = dataUltimoAggiornamento;
  }

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public BigDecimal getTotaleRisorseAttivate()
  {
    BigDecimal totale = BigDecimal.ZERO;
    if (risorseAttivateList != null)
    {
      for (RisorsaAttivataDTO risorsaAttivataDTO : risorseAttivateList)
      {
        totale = totale.add(risorsaAttivataDTO.getRisorseAttivate());
      }
    }
    return totale;
  }

  public BigDecimal getTotaleEconomie()
  {
    BigDecimal tot = new BigDecimal(0);
    if (risorseAttivateList != null)
    {
      for (RisorsaAttivataDTO risorsa : risorseAttivateList)
      {
        if (risorsa.getElencoEconomie() != null
            && risorsa.getElencoEconomie().size() > 0)
        {
          for (EconomiaDTO item : risorsa.getElencoEconomie())
          {
            tot = tot.add(item.getImportoEconomia());
          }
        }
      }
    }
    return tot;
  }

  public BigDecimal getTotaleRisorseDisponibili()
  {
    if (getTotaleRisorseAttivate() != null)
    {
      return getTotaleRisorseAttivate().subtract(getTotaleEconomie());
    }
    else
    {
      return null;
    }
  }

  public String getDescrizioneUtenteAggiornamento()
  {
    return descrizioneUtenteAggiornamento;
  }

  public void setDescrizioneUtenteAggiornamento(
      String descrizioneUtenteAggiornamento)
  {
    this.descrizioneUtenteAggiornamento = descrizioneUtenteAggiornamento;
  }

  public BigDecimal getTotaleRisorseAttivateLiv(Long idLivello)
  {
    BigDecimal totale = BigDecimal.ZERO;
    if (risorseAttivateList != null)
    {
      for (RisorsaAttivataDTO risorsaAttivataDTO : risorseAttivateList)
      {
        if (risorsaAttivataDTO.getIdLivello() == idLivello)
          totale = totale.add(risorsaAttivataDTO.getRisorseAttivate());
      }
    }
    return totale;
  }

}
