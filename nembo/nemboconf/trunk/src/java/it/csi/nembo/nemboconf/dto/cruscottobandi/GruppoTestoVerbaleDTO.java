package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.ArrayList;
import java.util.List;

import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboConstants;

public class GruppoTestoVerbaleDTO implements ILoggable
{
  /** serialVersionUID */
  private static final long       serialVersionUID = -2776431928028387825L;
  protected long                  idGruppoTestoVerbale;
  protected String                descGruppoTestoVerbale;
  protected String                flagVisibile;
  protected int                   tipoCollocazioneTesto;
  protected List<TestoVerbaleDTO> testoVerbale     = new ArrayList<TestoVerbaleDTO>();
  protected Long                  idElencoCdu;

  public Long getIdElencoCdu()
  {
    return idElencoCdu;
  }

  public void setIdElencoCdu(Long idElencoCdu)
  {
    this.idElencoCdu = idElencoCdu;
  }

  public long getIdGruppoTestoVerbale()
  {
    return idGruppoTestoVerbale;
  }

  public void setIdGruppoTestoVerbale(long idGruppoTestoVerbale)
  {
    this.idGruppoTestoVerbale = idGruppoTestoVerbale;
  }

  public String getDescGruppoTestoVerbale()
  {
    return descGruppoTestoVerbale;
  }

  public void setDescGruppoTestoVerbale(String descGruppoTestoVerbale)
  {
    this.descGruppoTestoVerbale = descGruppoTestoVerbale;
  }

  public String getFlagVisibile()
  {
    return flagVisibile;
  }

  public void setFlagVisibile(String flagVisibile)
  {
    this.flagVisibile = flagVisibile;
  }

  public boolean isVisibile()
  {
    return NemboConstants.FLAGS.SI.equals(flagVisibile);
  }

  public int getTipoCollocazioneTesto()
  {
    return tipoCollocazioneTesto;
  }

  public void setTipoCollocazioneTesto(int tipoCollocazioneTesto)
  {
    this.tipoCollocazioneTesto = tipoCollocazioneTesto;
  }

  public List<TestoVerbaleDTO> getTestoVerbale()
  {
    return testoVerbale;
  }

  public void setTestoVerbale(List<TestoVerbaleDTO> testoVerbale)
  {
    this.testoVerbale = testoVerbale;
  }

  public void addTestoVerbale(TestoVerbaleDTO testoVerbaleDTO)
  {
    testoVerbale.add(testoVerbaleDTO);
  }

  public void updateTestoVerbaleNonCatalogo(String te)
  {

  }

  public void addTestiNonInCatalogo(String[] testi)
  {
    if (testi != null)
    {
      int lastOrder = 10;
      int size = testoVerbale.size();
      if (size > 0)
      {
        lastOrder = testoVerbale.get(size - 1).getOrdine();
      }
      for (String testo : testi)
      {
        TestoVerbaleDTO t = new TestoVerbaleDTO();
        t.setFlagCatalogo(NemboConstants.FLAGS.NO);
        t.setDescrizione(testo);
        lastOrder += 10;
        t.setOrdine(lastOrder);
        addTestoVerbale(t);
      }
    }
  }

  public void addTestiToUpdateList(List<DecodificaDTO<Long>> list)
  {
    for (TestoVerbaleDTO testoVerbaleDTO : testoVerbale)
    {
      if (!testoVerbaleDTO.isDisabled())
      {
        list.add(new DecodificaDTO<Long>(idGruppoTestoVerbale,
            testoVerbaleDTO.getDescrizione()));
      }
    }
  }
}
