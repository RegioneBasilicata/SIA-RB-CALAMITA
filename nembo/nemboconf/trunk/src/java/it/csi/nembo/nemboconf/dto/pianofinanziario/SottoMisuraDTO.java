package it.csi.nembo.nemboconf.dto.pianofinanziario;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import it.csi.nembo.nemboconf.util.NemboUtils;

public class SottoMisuraDTO extends GerarchiaLivelloDTO<TipoOperazioneDTO>
{
  /** serialVersionUID */
  private static final long     serialVersionUID   = 2015352631811124756L;
  private Map<Long, BigDecimal> mapTotaliFocusArea = null;

  public void calcolaTotale()
  {
    if (totale == null)
    {
      totale = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (TipoOperazioneDTO operazione : elenco)
        {
          totale = totale.add(operazione.getTotale(), MathContext.UNLIMITED);
        }
      }
    }
  }

  public void calcolaTotaleTrascinato()
  {
    if (totaleTrascinato == null)
    {
      totaleTrascinato = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (TipoOperazioneDTO operazione : elenco)
        {
          BigDecimal importoTrascinato = operazione.getTotaleTrascinato();
          if (importoTrascinato != null)
          {
            totaleTrascinato = totaleTrascinato.add(importoTrascinato,
                MathContext.UNLIMITED);
          }
        }
      }
    }
  }

  public void calcolaTotaleRisorseAttivate()
  {
    if (totaleRisorseAttivate == null)
    {
      totaleRisorseAttivate = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (TipoOperazioneDTO sottomisura : elenco)
        {
          BigDecimal risorseAttivate = sottomisura.getTotaleRisorseAttivate();
          if (risorseAttivate != null)
          {
            totaleRisorseAttivate = totaleRisorseAttivate.add(risorseAttivate,
                MathContext.UNLIMITED);
          }
        }
      }
    }
  }

  public void calcolaTotaleEconomie()
  {
    if (totaleEconomie == null)
    {
      totaleEconomie = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (TipoOperazioneDTO sottomisura : elenco)
        {
          BigDecimal economie = sottomisura.getTotaleEconomie();
          if (economie != null)
          {
            totaleEconomie = totaleEconomie.add(economie,
                MathContext.UNLIMITED);
          }
        }
      }
    }
  }

  public BigDecimal getTotaleFA(long idFocusArea)
  {
    if (mapTotaliFocusArea == null)
    {
      riempiMappaTotaliFocusArea();
    }
    return mapTotaliFocusArea.get(idFocusArea);
  }

  public String getTotaleFAEuro(long idFocusArea)
  {
    return NemboUtils.FORMAT.formatCurrency(getTotaleFA(idFocusArea));
  }

  public String getTotalePagatoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(getTotalePagato());
  }

  public Map<Long, BigDecimal> getMapTotaleFA()
  {
    if (mapTotaliFocusArea == null)
    {
      riempiMappaTotaliFocusArea();
    }
    return mapTotaliFocusArea;
  }

  private void riempiMappaTotaliFocusArea()
  {
    mapTotaliFocusArea = new HashMap<Long, BigDecimal>();
    if (!elenco.isEmpty())
    {
      for (TipoOperazioneDTO operazione : elenco)
      {
        for (ImportoFocusAreaDTO importo : operazione.elenco)
        {
          long idFocusArea = importo.getIdFocusArea();
          BigDecimal bdImporto = mapTotaliFocusArea.get(idFocusArea);
          if (bdImporto == null)
          {
            mapTotaliFocusArea.put(idFocusArea, importo.getImporto());
          }
          else
          {
            mapTotaliFocusArea.put(idFocusArea,
                bdImporto.add(importo.getImporto(), MathContext.UNLIMITED));
          }
        }
      }
    }
  }

  public void calcolaTotalePagato()
  {
    if (totalePagato == null)
    {
      totalePagato = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (TipoOperazioneDTO sottomisura : elenco)
        {
          BigDecimal pagato = sottomisura.getTotalePagato();
          if (pagato != null)
          {
            totalePagato = totalePagato.add(pagato, MathContext.UNLIMITED);
          }
        }
      }
    }
  }
}
