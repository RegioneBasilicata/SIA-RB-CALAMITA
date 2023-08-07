package it.csi.nembo.nemboconf.dto.pianofinanziario;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import it.csi.nembo.nemboconf.util.NemboUtils;

public class TipoOperazioneDTO extends GerarchiaLivelloDTO<ImportoFocusAreaDTO>
{
  /** serialVersionUID */
  private static final long serialVersionUID = 2015352631811124756L;
  protected Long            idSettore;
  protected String          codiceSettore;
  protected String          codMisura;
  protected String          codSottomisura;
  protected String          codTipoOperazione;
  protected String          descrMisura;
  protected String          descrSottomisura;
  protected String          descrTipoOperazione;
  protected BigDecimal      risorseAttivate;
  protected BigDecimal      importoEconomia;
  protected BigDecimal      importoPagato;

  public List<ImportoFocusAreaDTO> getListFocusArea()
  {
    if (elenco == null)
    {
      elenco = new ArrayList<ImportoFocusAreaDTO>();
    }
    if (elenco.size() == 0)
    {
      ImportoFocusAreaDTO ifa = new ImportoFocusAreaDTO();
      ifa.setImporto(BigDecimal.ZERO);
      elenco.add(ifa);
    }
    return elenco;
  }

  public Long getIdSettore()
  {
    return idSettore;
  }

  public void setIdSettore(Long idSettore)
  {
    this.idSettore = idSettore;
  }

  public String getCodiceSettore()
  {
    return codiceSettore;
  }

  public void setCodiceSettore(String codiceSettore)
  {
    this.codiceSettore = codiceSettore;
  }

  public void calcolaTotaleRisorseAttivate()
  {
    if (totaleRisorseAttivate == null)
    {
      totaleRisorseAttivate = NemboUtils.NUMBERS.nvl(risorseAttivate);
    }
  }

  public void calcolaTotaleEconomie()
  {
    if (totaleEconomie == null)
    {
      totaleEconomie = NemboUtils.NUMBERS.nvl(importoEconomia);
    }
  }

  public String getImportoFAEuro(String codiceFocusArea)
  {
    // L'implementazione di default (GerarchiaLivelloDTO) crea l'oggetto
    // elenco nel costruttore QUINDI qui deve essere != null
    // e quindi posso testare solo l'isEmpty()
    if (!elenco.isEmpty() && codiceFocusArea != null)
    {
      for (ImportoFocusAreaDTO imp : elenco)
      {
        if (codiceFocusArea.equals(imp.getCodice()))
        {
          return NemboUtils.FORMAT.formatCurrency(imp.getImporto());
        }
      }
    }
    return null;
  }

  public void calcolaTotale()
  {
    if (totale == null)
    {
      totale = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (ImportoFocusAreaDTO imp : elenco)
        {
          totale = totale.add(imp.getImporto(), MathContext.UNLIMITED);
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
        for (ImportoFocusAreaDTO imp : elenco)
        {
          BigDecimal importoTrascinato = imp.getImportoTrascinato();
          if (importoTrascinato != null)
          {
            totaleTrascinato = totaleTrascinato.add(importoTrascinato,
                MathContext.UNLIMITED);
          }
        }
      }
    }
  }

  public String getCodMisura()
  {
    return codMisura;
  }

  public void setCodMisura(String codMisura)
  {
    this.codMisura = codMisura;
  }

  public String getCodSottomisura()
  {
    return codSottomisura;
  }

  public void setCodSottomisura(String codSottomisura)
  {
    this.codSottomisura = codSottomisura;
  }

  public String getCodTipoOperazione()
  {
    return codTipoOperazione;
  }

  public void setCodTipoOperazione(String codTipoOperazione)
  {
    this.codTipoOperazione = codTipoOperazione;
  }

  public String getDescrMisura()
  {
    return descrMisura;
  }

  public void setDescrMisura(String descrMisura)
  {
    this.descrMisura = descrMisura;
  }

  public String getDescrSottomisura()
  {
    return descrSottomisura;
  }

  public void setDescrSottomisura(String descrSottomisura)
  {
    this.descrSottomisura = descrSottomisura;
  }

  public String getDescrTipoOperazione()
  {
    return descrTipoOperazione;
  }

  public void setDescrTipoOperazione(String descrTipoOperazione)
  {
    this.descrTipoOperazione = descrTipoOperazione;
  }

  public BigDecimal getRisorseAttivate()
  {
    return risorseAttivate;
  }

  public void setRisorseAttivate(BigDecimal risorseAttivate)
  {
    this.risorseAttivate = risorseAttivate;
  }

  public BigDecimal getImportoEconomia()
  {
    return importoEconomia;
  }

  public void setImportoEconomia(BigDecimal importoEconomia)
  {
    this.importoEconomia = importoEconomia;
  }

  public BigDecimal getImportoPagato()
  {
    return importoPagato;
  }

  public String getImportoPagatoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(importoPagato);
  }

  public void setImportoPagato(BigDecimal importoPagato)
  {
    this.importoPagato = importoPagato;
  }

  @Override
  public void calcolaTotalePagato()
  {
    if (totalePagato == null)
    {
      totalePagato = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (ImportoFocusAreaDTO imp : elenco)
        {
          BigDecimal importoPagato = imp.getImportoPagato();
          if (importoPagato != null)
          {
            totalePagato = totalePagato.add(importoPagato,
                MathContext.UNLIMITED);
          }
        }
      }
    }
  }

}
