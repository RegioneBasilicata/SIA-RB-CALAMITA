package it.csi.nembo.nemboconf.dto.pianofinanziario;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public abstract class GerarchiaLivelloDTO<T> implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = -6485271808206899165L;
  protected long            idLivello;
  protected String          codice;
  protected String          numeriCodice[]   = new String[3];
  protected String          descrizione;
  protected Long            idLivelloPadre;
  protected long            idTipologiaLivello;
  protected Long            idTipoLivello;
  protected List<T>         elenco;
  protected BigDecimal      totale           = null;
  protected BigDecimal      totaleTrascinato = null;
  protected BigDecimal      totaleRisorseAttivate;
  protected BigDecimal      totaleEconomie;
  protected BigDecimal      risorseDisponibili;
  protected BigDecimal      totalePagato;

  public GerarchiaLivelloDTO()
  {
    this.elenco = new ArrayList<T>();
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
    StringTokenizer st = new StringTokenizer(codice, ".");
    int count = 0;
    while (st.hasMoreTokens() && count < 3)
    {
      numeriCodice[count++] = st.nextToken();
    }
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public Long getIdLivelloPadre()
  {
    return idLivelloPadre;
  }

  public void setIdLivelloPadre(Long idLivelloPadre)
  {
    this.idLivelloPadre = idLivelloPadre;
  }

  public long getIdTipologiaLivello()
  {
    return idTipologiaLivello;
  }

  public Long getIdTipoLivello()
  {
    return idTipoLivello;
  }

  public void setIdTipoLivello(Long idTipoLivello)
  {
    this.idTipoLivello = idTipoLivello;
  }

  public void setIdTipologiaLivello(long idTipologiaLivello)
  {
    this.idTipologiaLivello = idTipologiaLivello;
  }

  public List<T> getElenco()
  {
    return elenco;
  }

  public void setElenco(List<T> elenco)
  {
    this.elenco = elenco;
  }

  public String getNumeroMisura()
  {
    return numeriCodice[0];
  }

  public String getNumeroSottoMisura()
  {
    return numeriCodice[1];
  }

  public String getNumeroTipoOperazione()
  {
    return numeriCodice[2];
  }

  public String getNumeroOperazione()
  {
    return numeriCodice[0] + "." + numeriCodice[1] + "." + numeriCodice[2];
  }

  public boolean isEspandibile()
  {
    return elenco.size() > 0;
  }

  public BigDecimal getTotale()
  {
    if (totale == null)
    {
      calcolaTotale();
    }
    return totale;
  }

  public BigDecimal getTotaleTrascinato()
  {
    if (totaleTrascinato == null)
    {
      calcolaTotaleTrascinato();
    }
    return totaleTrascinato;
  }

  public BigDecimal getTotaleRisorseAttivate()
  {
    if (totaleRisorseAttivate == null)
    {
      calcolaTotaleRisorseAttivate();
    }
    return totaleRisorseAttivate;
  }

  public BigDecimal getTotaleEconomie()
  {
    if (totaleEconomie == null)
    {
      calcolaTotaleEconomie();
    }
    return totaleEconomie;
  }

  public BigDecimal getTotalePagato()
  {
    if (totalePagato == null)
    {
      calcolaTotalePagato();
    }
    return totalePagato;
  }

  public String getTotalePagatoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(getTotalePagato());

  }

  public void setTotale(BigDecimal totale)
  {
    this.totale = totale;
  }

  public String getTotaleTrascinatoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(getTotaleTrascinato());
  }

  public String getTotaleEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(getTotale());
  }

  public String getTotaleRisorseAttivateEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(getTotaleRisorseAttivate());
  }

  public String getTotaleEconomieEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(getTotaleEconomie());
  }

  public BigDecimal getRisorseDisponibili()
  {
    if (risorseDisponibili == null)
    {
      risorseDisponibili = calcolaRisorseDisponibili();
    }
    return risorseDisponibili;
  }

  public BigDecimal calcolaRisorseDisponibili()
  {
    return getTotale().subtract(getTotaleTrascinato(), MathContext.DECIMAL128)
        .subtract(getTotaleRisorseAttivate(), MathContext.DECIMAL128)
        .add(getTotaleEconomie(), MathContext.DECIMAL128);
  }

  public String getRisorseDisponibiliEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(getRisorseDisponibili());
  }

  public abstract void calcolaTotale();

  public abstract void calcolaTotaleTrascinato();

  public abstract void calcolaTotaleRisorseAttivate();

  public abstract void calcolaTotaleEconomie();

  public abstract void calcolaTotalePagato();

}
