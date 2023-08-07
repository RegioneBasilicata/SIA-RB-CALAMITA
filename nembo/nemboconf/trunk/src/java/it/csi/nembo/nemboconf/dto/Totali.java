package it.csi.nembo.nemboconf.dto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class Totali
{
  public BigDecimal importo            = BigDecimal.ZERO;
  public BigDecimal trascinato         = BigDecimal.ZERO;
  public BigDecimal risorseAttivate    = BigDecimal.ZERO;
  public BigDecimal risorseDisponibili = BigDecimal.ZERO;
  public BigDecimal economie           = BigDecimal.ZERO;
  public BigDecimal pagato             = BigDecimal.ZERO;

  public String getPagatoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(pagato);
  }

  public BigDecimal getPagato()
  {
    return pagato;
  }

  public void setPagato(BigDecimal pagato)
  {
    this.pagato = pagato;
  }

  public String getImportoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(importo);
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public String getTrascinatoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(trascinato);
  }

  public void setTrascinato(BigDecimal trascinato)
  {
    this.trascinato = trascinato;
  }

  public String getRisorseAttivateEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(risorseAttivate);
  }

  public void setRisorseAttivate(BigDecimal risorseAttivate)
  {
    this.risorseAttivate = risorseAttivate;
  }

  public String getRisorseDisponibiliEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(risorseDisponibili);
  }

  public void setRisorseDisponibili(BigDecimal risorseDisponibili)
  {
    this.risorseDisponibili = risorseDisponibili;
  }

  public String getEconomieEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(economie);
  }

  public void setEconomie(BigDecimal economie)
  {
    this.economie = economie;
  }

  public static Totali calcolaTotali(List<MisuraDTO> elencoLivelli)
  {
    BigDecimal totaleImporto = BigDecimal.ZERO;
    BigDecimal totaleTrascinato = BigDecimal.ZERO;
    BigDecimal totaleRisorseAttivate = BigDecimal.ZERO;
    BigDecimal totaleEconomie = BigDecimal.ZERO;
    BigDecimal totalePagato = BigDecimal.ZERO;

    for (MisuraDTO misura : elencoLivelli)
    {
      totaleImporto = totaleImporto.add(misura.getTotale(),
          MathContext.DECIMAL128);
      totaleTrascinato = totaleTrascinato.add(misura.getTotaleTrascinato(),
          MathContext.DECIMAL128);

      totaleRisorseAttivate = totaleRisorseAttivate
          .add(misura.getTotaleRisorseAttivate(), MathContext.DECIMAL128);
      totaleEconomie = totaleEconomie.add(misura.getTotaleEconomie(),
          MathContext.DECIMAL128);
      totalePagato = totalePagato.add(misura.getTotalePagato(),
          MathContext.DECIMAL128);

    }
    Totali tot = new Totali();
    tot.setImporto(totaleImporto);
    tot.setTrascinato(totaleTrascinato);
    tot.setRisorseAttivate(totaleRisorseAttivate);
    tot.setEconomie(totaleEconomie);
    tot.setPagato(totalePagato);
    tot.setRisorseDisponibili(
        totaleImporto.subtract(totaleTrascinato, MathContext.DECIMAL128)
            .subtract(totaleRisorseAttivate, MathContext.DECIMAL128)
            .add(totaleEconomie, MathContext.DECIMAL128));
    return tot;
  }

}
