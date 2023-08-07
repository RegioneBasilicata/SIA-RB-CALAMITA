package it.csi.nembo.nemboconf.dto.stampa;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class InfoHeader implements ILoggable
{
  /**
   * 
   */
  private static final long serialVersionUID = 4300138184609952655L;
  protected String          descBando;
  protected String          descOggetto;
  protected String          listaMisure;
  protected String          listaAmmCompetenza;

  public String getDescBando()
  {
    return descBando;
  }

  public void setDescBando(String descBando)
  {
    this.descBando = descBando;
  }

  public String getDescOggetto()
  {
    return descOggetto;
  }

  public void setDescOggetto(String descOggetto)
  {
    this.descOggetto = descOggetto;
  }

  public String getListaMisure()
  {
    return listaMisure;
  }

  public void setListaMisure(String listaMisure)
  {
    this.listaMisure = listaMisure;
  }

  public String getListaAmmCompetenza()
  {
    return listaAmmCompetenza;
  }

  public void setListaAmmCompetenza(String listaAmmCompetenza)
  {
    this.listaAmmCompetenza = listaAmmCompetenza;
  }

}
