package it.csi.nembo.nemboconf.dto.pianofinanziario;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.nembo.nemboconf.dto.Anno;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class MisuraDTO extends GerarchiaLivelloDTO<SottoMisuraDTO>
{
  /** serialVersionUID */
  private static final long         serialVersionUID   = -6583195053252310160L;
  private Map<Long, BigDecimal>     mapTotaliFocusArea = null;
  private List<ImportoFocusAreaDTO> focusAreaMisura;

  public void calcolaTotale()
  {
    if (totale == null)
    {
      totale = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (SottoMisuraDTO sottomisura : elenco)
        {
          totale = totale.add(sottomisura.getTotale(), MathContext.UNLIMITED);
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
        for (SottoMisuraDTO sottomisura : elenco)
        {
          BigDecimal importoTrascinato = sottomisura.getTotaleTrascinato();
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
        for (SottoMisuraDTO sottomisura : elenco)
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
        for (SottoMisuraDTO sottomisura : elenco)
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

  private void riempiMappaTotaliFocusArea()
  {
    mapTotaliFocusArea = new HashMap<Long, BigDecimal>();
    if (!elenco.isEmpty())
    {
      for (SottoMisuraDTO sottomisura : elenco)
      {
        Map<Long, BigDecimal> mapTotaliSottomisura = sottomisura
            .getMapTotaleFA();
        for (Long id : mapTotaliSottomisura.keySet())
        {
          BigDecimal bdImporto = mapTotaliSottomisura.get(id);
          BigDecimal bdTotaleFA = mapTotaliFocusArea.get(id);
          if (bdTotaleFA == null)
          {
            bdTotaleFA = bdImporto;
          }
          else
          {
            bdTotaleFA = bdTotaleFA.add(bdImporto, MathContext.UNLIMITED);
          }
          mapTotaliFocusArea.put(id, bdTotaleFA);
        }
      }
    }
  }

  @Override
  public void calcolaTotalePagato()
  {
    if (totalePagato == null)
    {
      totalePagato = BigDecimal.ZERO;
      if (!elenco.isEmpty())
      {
        for (SottoMisuraDTO sottomisura : elenco)
        {
          BigDecimal pagati = sottomisura.getTotalePagato();
          if (pagati != null)
          {
            totalePagato = totalePagato.add(pagati, MathContext.UNLIMITED);
          }
        }
      }
    }
  }

  /*
   * Metodi per la gestione delle dichiarazioni di spesa Ogni anno ha 4
   * trimesetri con i relativi importi.
   */

  private List<Anno> anni = new ArrayList<>();

  public List<Anno> getAnni()
  {
    ArrayList<Anno> anni = new ArrayList<>();
    this.anni = anni;
    Anno anno = new Anno();

    if (this.focusAreaMisura != null)
    {
      for (ImportoFocusAreaDTO f : this.focusAreaMisura)
      {

        if (f.getAnni() != null)
          for (Anno a : f.getAnni())
          {
            if (!hasAnno(a.getAnno()))
            {
              anno = new Anno(a.getAnno());
              anno.setFlagStatoDichiarazioneTrimestre1(
                  a.getFlagStatoDichiarazioneTrimestre1());
              anno.setFlagStatoDichiarazioneTrimestre2(
                  a.getFlagStatoDichiarazioneTrimestre2());
              anno.setFlagStatoDichiarazioneTrimestre3(
                  a.getFlagStatoDichiarazioneTrimestre3());
              anno.setFlagStatoDichiarazioneTrimestre4(
                  a.getFlagStatoDichiarazioneTrimestre4());
              anno.impostaTrimestreCorrente();
              anni.add(anno);
            }
            else
            {
              for (Anno aaa : anni)
                if (aaa.getAnno() == a.getAnno())
                  anno = aaa;
            }

            if (f.getContatorePerAggregazioneFA() == 0)
            {
              anno.setImportoTrimestre1(
                  anno.getImportoTrimestre1().add(a.getImportoTrimestre1()));
              anno.setImportoTrimestre2(
                  anno.getImportoTrimestre2().add(a.getImportoTrimestre2()));
              anno.setImportoTrimestre3(
                  anno.getImportoTrimestre3().add(a.getImportoTrimestre3()));
              anno.setImportoTrimestre4(
                  anno.getImportoTrimestre4().add(a.getImportoTrimestre4()));
            }
            else
            {
              anno.setImportoTrimestre1(anno.getImportoTrimestre1()
                  .add(a.getImportoTrimestre1().divide(
                      new BigDecimal(f.getContatorePerAggregazioneFA()), 3,
                      RoundingMode.HALF_UP)));
              anno.setImportoTrimestre2(anno.getImportoTrimestre2()
                  .add(a.getImportoTrimestre2().divide(
                      new BigDecimal(f.getContatorePerAggregazioneFA()), 3,
                      RoundingMode.HALF_UP)));
              anno.setImportoTrimestre3(anno.getImportoTrimestre3()
                  .add(a.getImportoTrimestre3().divide(
                      new BigDecimal(f.getContatorePerAggregazioneFA()), 3,
                      RoundingMode.HALF_UP)));
              anno.setImportoTrimestre4(anno.getImportoTrimestre4()
                  .add(a.getImportoTrimestre4().divide(
                      new BigDecimal(f.getContatorePerAggregazioneFA()), 3,
                      RoundingMode.HALF_UP)));
            }
          }
      }
    }

    this.anni = anni;
    return anni;
  }

  private boolean hasAnno(int anno)
  {

    boolean trovato = false;
    if (this.anni == null || this.anni.isEmpty())
      return false;
    else
    {
      for (Anno a : this.anni)
        if (a.getAnno() == anno)
          trovato = true;
    }
    return trovato;
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

  /*
   * Per le dichiarazioni di spesa. Mi serve per popolare una lista di FocusArea
   * considerando solo la suddivisione per misura, non sottomisure, operazioni
   * ecc. E' un riepilogo della focus area per misura.
   */

  public List<ImportoFocusAreaDTO> getFocusAreaDellaMisura()
  {

    ImportoFocusAreaDTO fa = new ImportoFocusAreaDTO();
    List<ImportoFocusAreaDTO> list = new ArrayList<>();
    for (SottoMisuraDTO s : elenco)
      for (TipoOperazioneDTO o : s.getElenco())
        for (ImportoFocusAreaDTO f : o.getElenco())
        {
          if (!hasMisura(list, f))
          {
            fa = new ImportoFocusAreaDTO();
            fa.setIdFocusArea(f.getIdFocusArea());
            fa.setCodice(f.getCodice());
            fa.setImporto(f.getImporto());
            fa.setIdPriorita(f.getIdPriorita());
            fa.setAnni(new ArrayList<>());
            fa.setContatorePerAggregazioneFA(f.getContatorePerAggregazioneFA());
            list.add(fa);
          }
          else
            addInMisura(list, f);
        }

    // ordino la lista per codice focus area
    Collections.sort(list, new Comparator<ImportoFocusAreaDTO>()
    {
      @Override
      public int compare(ImportoFocusAreaDTO a, ImportoFocusAreaDTO b)
      {
        return a.getCodice().compareTo(b.getCodice());
      }
    });

    return list;
  }

  private void addInMisura(List<ImportoFocusAreaDTO> list,
      ImportoFocusAreaDTO f)
  {

    if (list == null || list.isEmpty())
      return;
    for (ImportoFocusAreaDTO fff : list)
      if (fff.getIdFocusArea() == f.getIdFocusArea())
      {
        BigDecimal imp = fff.getImporto();
        imp = imp.add(f.getImporto());
        fff.setImporto(imp);
      }
  }

  private boolean hasMisura(List<ImportoFocusAreaDTO> list,
      ImportoFocusAreaDTO f)
  {

    boolean trovato = false;
    if (list == null || list.isEmpty())
      return false;
    else
    {
      for (ImportoFocusAreaDTO fff : list)
        if (fff.getIdFocusArea() == f.getIdFocusArea())
          trovato = true;
    }
    return trovato;
  }

  public void setFocusAreaMisura(List<ImportoFocusAreaDTO> focusAreaMisura)
  {
    this.focusAreaMisura = focusAreaMisura;
  }

  public List<ImportoFocusAreaDTO> getFocusAreaMisura()
  {
    return this.focusAreaMisura;
  }

  public String getDeltaComplessivoMisuraStr()
  {
    BigDecimal tot = BigDecimal.ZERO;
    for (ImportoFocusAreaDTO f : focusAreaMisura)
    {
      tot = tot.add(f.getDelta());
    }
    return NemboUtils.FORMAT.formatCurrency(tot);
  }

}
