package it.csi.nembo.nemboconf.dto;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.csi.nembo.nemboconf.util.NemboUtils;

public class Anno
{

  private BigDecimal importoTrimestre1      = BigDecimal.ZERO;
  private BigDecimal importoTrimestre2      = BigDecimal.ZERO;
  private BigDecimal importoTrimestre3      = BigDecimal.ZERO;
  private BigDecimal importoTrimestre4      = BigDecimal.ZERO;
  private BigDecimal importoTrimestre1Sigop = BigDecimal.ZERO;
  private BigDecimal importoTrimestre2Sigop = BigDecimal.ZERO;
  private BigDecimal importoTrimestre3Sigop = BigDecimal.ZERO;
  private BigDecimal importoTrimestre4Sigop = BigDecimal.ZERO;
  private int        trimestreCorrente;
  private int        trimestreConcluso;
  private int        anno;
  private String     flagStatoDichiarazioneTrimestre1;
  private String     flagStatoDichiarazioneTrimestre2;
  private String     flagStatoDichiarazioneTrimestre3;
  private String     flagStatoDichiarazioneTrimestre4;

  public Anno()
  {

  }

  public Anno(int anno)
  {
    this.anno = anno;
  }

  public BigDecimal getImportoTrimestre1()
  {

    return importoTrimestre1 == null ? BigDecimal.ZERO : importoTrimestre1;
  }

  public void setImportoTrimestre1(BigDecimal importoTrimestre1)
  {

    if (importoTrimestre1 == null)
      this.importoTrimestre1 = BigDecimal.ZERO;
    this.importoTrimestre1 = importoTrimestre1;
  }

  public BigDecimal getImportoTrimestre2()
  {

    return importoTrimestre2 == null ? BigDecimal.ZERO : importoTrimestre2;
  }

  public void setImportoTrimestre2(BigDecimal importoTrimestre2)
  {
    if (importoTrimestre2 == null)
      this.importoTrimestre2 = BigDecimal.ZERO;
    this.importoTrimestre2 = importoTrimestre2;
  }

  public BigDecimal getImportoTrimestre3()
  {
    return importoTrimestre3 == null ? BigDecimal.ZERO : importoTrimestre3;
  }

  public void setImportoTrimestre3(BigDecimal importoTrimestre3)
  {
    if (importoTrimestre3 == null)
      this.importoTrimestre3 = BigDecimal.ZERO;
    this.importoTrimestre3 = importoTrimestre3;
  }

  public BigDecimal getImportoTrimestre4()
  {

    return importoTrimestre4 == null ? BigDecimal.ZERO : importoTrimestre4;
  }

  public void setImportoTrimestre4(BigDecimal importoTrimestre4)
  {
    if (importoTrimestre4 == null)
      this.importoTrimestre4 = BigDecimal.ZERO;
    this.importoTrimestre4 = importoTrimestre4;
  }

  public String getImportoTrimestre1Str()
  {
    return NemboUtils.FORMAT.formatCurrency(getImportoTrimestre1());
  }

  public String getImportoTrimestre2Str()
  {
    return NemboUtils.FORMAT.formatCurrency(getImportoTrimestre2());
  }

  public String getImportoTrimestre3Str()
  {
    return NemboUtils.FORMAT.formatCurrency(getImportoTrimestre3());
  }

  public String getImportoTrimestre4Str()
  {
    return NemboUtils.FORMAT.formatCurrency(getImportoTrimestre4());
  }

  public int getAnno()
  {
    return anno;
  }

  public void setAnno(int anno)
  {
    this.anno = anno;
  }

  public String getTrimestre1()
  {

    if (this.getTrimestreConcluso() == 1
        && "N".equals(this.flagStatoDichiarazioneTrimestre1))
      return "Pagamenti (01/01 al 31/03)";

    if (this.getTrimestreCorrente() == 1
        && "N".equals(this.flagStatoDichiarazioneTrimestre1))
      return "Pagamenti (01/01 al 31/03)";

    return "Dichiarazione di spesa II (01/01 al 31/03)";

  }

  public String getTrimestre2()
  {

    if (this.getTrimestreConcluso() == 2
        && "N".equals(this.flagStatoDichiarazioneTrimestre2))
      return "Pagamenti (01/04 al 30/06)";
    if (this.getTrimestreCorrente() == 2
        && "N".equals(this.flagStatoDichiarazioneTrimestre2))
      return "Pagamenti (01/04 al 30/06)";

    return "Dichiarazione di spesa III (01/04 al 30/06)";
  }

  public String getTrimestre3()
  {

    if (this.getTrimestreConcluso() == 3
        && "N".equals(this.flagStatoDichiarazioneTrimestre3))
      return "Pagamenti (01/07 al 15/10)";
    if (this.getTrimestreCorrente() == 3
        && "N".equals(this.flagStatoDichiarazioneTrimestre3))
      return "Pagamenti (01/07 al 15/10)";

    return "Dichiarazione di spesa IV (01/07 al 15/10)";

  }

  public String getTrimestre4()
  {

    if (this.getTrimestreConcluso() == 4
        && "N".equals(this.flagStatoDichiarazioneTrimestre4))
      return "Pagamenti(16/10 al 31/12)";
    if (this.getTrimestreCorrente() == 4
        && "N".equals(this.flagStatoDichiarazioneTrimestre4))
      return "Pagamenti(16/10 al 31/12)";

    return "Dichiarazione di spesa I (16/10 al 31/12)";

  }

  public BigDecimal getTotaleAnno()
  {
    return getImportoTrimestre1().add(getImportoTrimestre2())
        .add(getImportoTrimestre3())
        .add(getImportoTrimestre4());
  }

  public String getTotaleAnnoStr()
  {
    return NemboUtils.FORMAT
        .formatCurrency(getImportoTrimestre1().add(getImportoTrimestre2())
            .add(getImportoTrimestre3()).add(getImportoTrimestre4()));
  }

  public int getTrimestreCorrente()
  {
    return this.trimestreCorrente;
  }

  public BigDecimal getImportoTrimestre1Sigop()
  {
    return importoTrimestre1Sigop;
  }

  public void setImportoTrimestre1Sigop(BigDecimal importoTrimestre1Sigop)
  {
    this.importoTrimestre1Sigop = importoTrimestre1Sigop;
  }

  public BigDecimal getImportoTrimestre2Sigop()
  {
    return importoTrimestre2Sigop;
  }

  public void setImportoTrimestre2Sigop(BigDecimal importoTrimestre2Sigop)
  {
    this.importoTrimestre2Sigop = importoTrimestre2Sigop;
  }

  public BigDecimal getImportoTrimestre3Sigop()
  {
    return importoTrimestre3Sigop;
  }

  public void setImportoTrimestre3Sigop(BigDecimal importoTrimestre3Sigop)
  {
    this.importoTrimestre3Sigop = importoTrimestre3Sigop;
  }

  public BigDecimal getImportoTrimestre4Sigop()
  {
    return importoTrimestre4Sigop;
  }

  public void setImportoTrimestre4Sigop(BigDecimal importoTrimestre4Sigop)
  {
    this.importoTrimestre4Sigop = importoTrimestre4Sigop;
  }

  public void impostaTrimestreCorrente()
  {

    Date today = new Date();

    Date inizioTrimestre1 = new GregorianCalendar(this.anno, Calendar.JANUARY,
        01).getTime();
    Date fineTrimestre1 = new GregorianCalendar(this.anno, Calendar.MARCH, 31)
        .getTime();
    Date inizioTrimestre2 = new GregorianCalendar(this.anno, Calendar.APRIL, 01)
        .getTime();
    Date fineTrimestre2 = new GregorianCalendar(this.anno, Calendar.JUNE, 30)
        .getTime();
    Date inizioTrimestre3 = new GregorianCalendar(this.anno, Calendar.JULY, 01)
        .getTime();
    Date fineTrimestre3 = new GregorianCalendar(this.anno, Calendar.OCTOBER, 15)
        .getTime();
    Date inizioTrimestre4 = new GregorianCalendar(this.anno, Calendar.OCTOBER,
        06).getTime();
    Date fineTrimestre4 = new GregorianCalendar(this.anno, Calendar.DECEMBER,
        31).getTime();
    ;

    if (today.compareTo(inizioTrimestre1) >= 0
        && today.compareTo(fineTrimestre1) < 0)
      this.trimestreCorrente = 1;
    if (today.compareTo(inizioTrimestre2) >= 0
        && today.compareTo(fineTrimestre2) < 0)
      this.trimestreCorrente = 2;
    if (today.compareTo(inizioTrimestre3) >= 0
        && today.compareTo(fineTrimestre3) < 0)
      this.trimestreCorrente = 3;
    if (today.compareTo(inizioTrimestre4) >= 0
        && today.compareTo(fineTrimestre4) < 0)
      this.trimestreCorrente = 4;
  }

  public void setTrimestreCorrente(int trimestreCorrente)
  {
    this.trimestreCorrente = trimestreCorrente;
  }

  public String getFlagStatoDichiarazioneTrimestre1()
  {
    return flagStatoDichiarazioneTrimestre1;
  }

  public void setFlagStatoDichiarazioneTrimestre1(
      String flagStatoDichiarazioneTrimestre1)
  {
    this.flagStatoDichiarazioneTrimestre1 = flagStatoDichiarazioneTrimestre1;
  }

  public String getFlagStatoDichiarazioneTrimestre2()
  {
    return flagStatoDichiarazioneTrimestre2;
  }

  public void setFlagStatoDichiarazioneTrimestre2(
      String flagStatoDichiarazioneTrimestre2)
  {
    this.flagStatoDichiarazioneTrimestre2 = flagStatoDichiarazioneTrimestre2;
  }

  public String getFlagStatoDichiarazioneTrimestre3()
  {
    return flagStatoDichiarazioneTrimestre3;
  }

  public void setFlagStatoDichiarazioneTrimestre3(
      String flagStatoDichiarazioneTrimestre3)
  {
    this.flagStatoDichiarazioneTrimestre3 = flagStatoDichiarazioneTrimestre3;
  }

  public String getFlagStatoDichiarazioneTrimestre4()
  {
    return flagStatoDichiarazioneTrimestre4;
  }

  public void setFlagStatoDichiarazioneTrimestre4(
      String flagStatoDichiarazioneTrimestre4)
  {
    this.flagStatoDichiarazioneTrimestre4 = flagStatoDichiarazioneTrimestre4;
  }

  public boolean isCurrentYear()
  {
    int currYear = Calendar.getInstance().get(Calendar.YEAR);
    return this.anno == currYear;
  }

  public int getTrimestreConcluso()
  {
    if (trimestreCorrente == 1 || trimestreCorrente == 0)
      this.trimestreConcluso = 4;
    else
      this.trimestreConcluso = this.trimestreCorrente - 1;

    return this.trimestreConcluso;
  }

  public void setTrimestreConcluso(int trimestreConcluso)
  {
    this.trimestreConcluso = trimestreConcluso;
  }

  /*
   * DATI UTILIZZATI PER IL MONITORAGGIO N+3
   */
  private BigDecimal              totaleNemboconf   = BigDecimal.ZERO;
  private BigDecimal              riservaEfficacia  = BigDecimal.ZERO;
  private BigDecimal              feasrSenzaRiserva = BigDecimal.ZERO;
  private BigDecimal              totalePagato      = BigDecimal.ZERO;
  private BigDecimal              disimpegno        = BigDecimal.ZERO;
  private BigDecimal              prefinanziamento  = BigDecimal.ZERO;
  private static final BigDecimal QUOTA_FEASR       = new BigDecimal(
      100 / 43.12);

  public BigDecimal getTotaleNemboconf()
  {
    if (totaleNemboconf == null)
      totaleNemboconf = BigDecimal.ZERO;
    return totaleNemboconf;
  }

  public BigDecimal getTotaleNemboconf43()
  {
    if (totaleNemboconf == null)
      totaleNemboconf = BigDecimal.ZERO;
    return totaleNemboconf.multiply(QUOTA_FEASR);
  }

  public void setTotaleNemboconf(BigDecimal totaleNemboconf)
  {
    this.totaleNemboconf = totaleNemboconf;
  }

  public BigDecimal getRiservaEfficacia()
  {
    if (riservaEfficacia == null)
      riservaEfficacia = BigDecimal.ZERO;
    return riservaEfficacia;
  }

  public BigDecimal getRiservaEfficacia43()
  {
    if (riservaEfficacia == null)
      riservaEfficacia = BigDecimal.ZERO;
    return riservaEfficacia.multiply(QUOTA_FEASR);
  }

  public void setRiservaEfficacia(BigDecimal riservaEfficacia)
  {
    this.riservaEfficacia = riservaEfficacia;
  }

  public BigDecimal getFeasrSenzaRiserva()
  {
    if (totaleNemboconf == null)
      totaleNemboconf = BigDecimal.ZERO;
    if (riservaEfficacia == null)
      riservaEfficacia = BigDecimal.ZERO;
    return totaleNemboconf.subtract(riservaEfficacia);
  }

  public BigDecimal getFeasrSenzaRiserva43()
  {
    return getFeasrSenzaRiserva().multiply(QUOTA_FEASR);
  }

  public void setFeasrSenzaRiserva(BigDecimal feasrSenzaRiserva)
  {
    this.feasrSenzaRiserva = feasrSenzaRiserva;
  }

  public BigDecimal getTotalePagato()
  {
    if (totalePagato == null)
      totalePagato = BigDecimal.ZERO;
    return totalePagato;
  }

  public BigDecimal getTotalePagato43()
  {
    if (totalePagato == null)
      totalePagato = BigDecimal.ZERO;
    return totalePagato.multiply(QUOTA_FEASR);
  }

  public void setTotalePagato(BigDecimal totalePagato)
  {
    this.totalePagato = totalePagato;
  }

  public BigDecimal getDelta()
  {
    if (totalePagato == null)
      totalePagato = BigDecimal.ZERO;
    if (feasrSenzaRiserva == null)
      feasrSenzaRiserva = BigDecimal.ZERO;
    return totalePagato.subtract(getFeasrSenzaRiserva());
  }

  public BigDecimal getDelta43()
  {
    return getDelta().multiply(QUOTA_FEASR);
  }

  public BigDecimal getDisimpegno()
  {
    if (disimpegno == null)
      disimpegno = BigDecimal.ZERO;
    return disimpegno;
  }

  public BigDecimal getDisimpegno43()
  {
    if (disimpegno == null)
      disimpegno = BigDecimal.ZERO;
    return disimpegno.multiply(QUOTA_FEASR);
  }

  public void setDisimpegno(BigDecimal disimpegno)
  {
    this.disimpegno = disimpegno;
  }

  public BigDecimal getPrefinanziamento()
  {
    return prefinanziamento;
  }

  public BigDecimal getPrefinanziamento43()
  {
    if (prefinanziamento == null)
      prefinanziamento = BigDecimal.ZERO;
    return prefinanziamento.multiply(QUOTA_FEASR);
  }

  public String getPrefinanziamentoStr()
  {
    return NemboUtils.FORMAT.formatCurrency(prefinanziamento);
  }

  public String getPrefinanziamento43Str()
  {
    return NemboUtils.FORMAT.formatCurrency(getPrefinanziamento43());
  }

  public void setPrefinanziamento(BigDecimal prefinanziamento)
  {
    this.prefinanziamento = prefinanziamento;
  }

  public String getTotaleNemboconfStr()
  {
    return NemboUtils.FORMAT.formatCurrency(totaleNemboconf);
  }

  public String getRiservaEfficaciaStr()
  {
    return NemboUtils.FORMAT.formatCurrency(riservaEfficacia);
  }

}
