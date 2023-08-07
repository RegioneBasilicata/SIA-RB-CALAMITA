package it.csi.nembo.nemboconf.dto.pianofinanziario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.Anno;
import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

//FIXME: MAINTAIN
public class ImportoFocusAreaDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long serialVersionUID = 7790926048419167514L;
  private long              idFocusArea;
  private BigDecimal        importo;
  private BigDecimal        importoTrascinato;
  private String            codice;
  private BigDecimal        importoPagato;
  private String            motivazioni;
  private Long              numeroRecordStorico;
  private Date              dataInserimento;
  private Long              extIdUtenteAggiornamento;
  private String            infoModifica;
  private List<Anno>        anni;
  private Long              idPriorita;
  private int               contatorePerAggregazioneFA;
  private BigDecimal        importoAggregato;

  public int getContatorePerAggregazioneFA()
  {
    return contatorePerAggregazioneFA;
  }

  public void setContatorePerAggregazioneFA(int cnt)
  {
    this.contatorePerAggregazioneFA = cnt;
  }

  public String getDataInserimentoStr()
  {
    return NemboUtils.DATE.formatDateTime(dataInserimento);
  }

  public String getImportoStr()
  {
    return NemboUtils.FORMAT.formatCurrency(importo);
  }

  public String getImportoTrascinatoStr()
  {
    return NemboUtils.FORMAT.formatCurrency(importoTrascinato);
  }

  public long getIdFocusArea()
  {
    return idFocusArea;
  }

  public void setIdFocusArea(long idFocusArea)
  {
    this.idFocusArea = idFocusArea;
  }

  public BigDecimal getImporto()
  {
    return importo;
  }

  public String getImportoEuro()
  {
    return NemboUtils.FORMAT.formatCurrency(importo);
  }

  public String getImportoTrascinatoEuro()
  {
    return NemboUtils.FORMAT
        .formatCurrency(NemboUtils.NUMBERS.nvl(importoTrascinato));
  }

  public void setImporto(BigDecimal importo)
  {
    this.importo = importo;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public BigDecimal getImportoTrascinato()
  {
    return importoTrascinato;
  }

  public void setImportoTrascinato(BigDecimal importoTrascinato)
  {
    this.importoTrascinato = importoTrascinato;
  }

  public BigDecimal getImportoPagato()
  {
    return importoPagato;
  }

  public void setImportoPagato(BigDecimal importoPagato)
  {
    this.importoPagato = importoPagato;
  }

  public String getImportoPagatoEuro()
  {
    return NemboUtils.FORMAT
        .formatCurrency(NemboUtils.NUMBERS.nvl(importoPagato));
  }

  public String getMotivazioni()
  {
    return motivazioni;
  }

  public void setMotivazioni(String motivazioni)
  {
    this.motivazioni = motivazioni;
  }

  public Long getNumeroRecordStorico()
  {
    return numeroRecordStorico;
  }

  public void setNumeroRecordStorico(Long numeroRecordStorico)
  {
    this.numeroRecordStorico = numeroRecordStorico;
  }

  public Date getDataInserimento()
  {
    return dataInserimento;
  }

  public void setDataInserimento(Date dataInserimento)
  {
    this.dataInserimento = dataInserimento;
  }

  public Long getExtIdUtenteAggiornamento()
  {
    return extIdUtenteAggiornamento;
  }

  public void setExtIdUtenteAggiornamento(Long extIdUtenteAggiornamento)
  {
    this.extIdUtenteAggiornamento = extIdUtenteAggiornamento;
  }

  public String getInfoModifica()
  {
    return infoModifica;
  }

  public void setInfoModifica(String infoModifica)
  {
    this.infoModifica = infoModifica;
  }

  public List<Anno> getAnni()
  {
    return anni;
  }

  public void setAnni(List<Anno> anni)
  {
    this.anni = anni;
  }

  public BigDecimal getTotaleComplessivo()
  {
    BigDecimal tot = BigDecimal.ZERO;
    if (this.anni != null && !this.anni.isEmpty())
      for (Anno a : this.anni)
        tot = tot.add(a.getTotaleAnno());
    return tot;
  }

  public String getTotaleComplessivoStr()
  {
    BigDecimal tot = BigDecimal.ZERO;
    if (this.anni != null && !this.anni.isEmpty())
      for (Anno a : this.anni)
        tot = tot.add(a.getTotaleAnno());
    return NemboUtils.FORMAT.formatCurrency(tot);
  }

  public String getDeltaStr()
  {
    if (importo == null || getTotaleComplessivo() == null)
      return "";
    BigDecimal i = null;
    if (this.importoAggregato == null
        || this.importoAggregato.equals(BigDecimal.ZERO))
      i = importo;
    else
      i = importoAggregato;
    BigDecimal tot = getTotaleComplessivo();
    BigDecimal ret = i.subtract(tot);
    return NemboUtils.FORMAT.formatCurrency(ret);
  }

  public BigDecimal getDelta()
  {
    if (importo == null || getTotaleComplessivo() == null)
      return BigDecimal.ZERO;
    BigDecimal i = null;
    if (this.importoAggregato == null
        || this.importoAggregato.equals(BigDecimal.ZERO))
      i = importo;
    else
      i = importoAggregato;

    BigDecimal tot = getTotaleComplessivo();
    BigDecimal ret = i.subtract(tot);

    if (this.contatorePerAggregazioneFA != 0)
      ret = ret.divide(new BigDecimal(this.contatorePerAggregazioneFA), 3,
          RoundingMode.HALF_DOWN);
    return ret;
  }

  public Long getIdPriorita()
  {
    return idPriorita;
  }

  public void setIdPriorita(Long idPriorita)
  {
    this.idPriorita = idPriorita;
  }

  public BigDecimal getImportoAggregato()
  {
    return importoAggregato;
  }

  public void setImportoAggregato(BigDecimal importoAggregato)
  {
    this.importoAggregato = importoAggregato;
  }

  public String getImportoAggregatoStr()
  {
    return NemboUtils.FORMAT.formatCurrency(importoAggregato);
  }

}
