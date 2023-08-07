package it.csi.nembo.nemboconf.dto.cruscottobandi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class QuadroOggettoVO implements ILoggable
{

  private static final long serialVersionUID = 4601339650061084885L;

  private long              idOggetto;
  private long              idQuadro;
  private long              idQuadroOggetto;
  private String            codOggetto;
  private String            descrOggetto;
  private String            codQuadro;
  private String            descrQuadro;

  public long getIdOggetto()
  {
    return idOggetto;
  }

  public void setIdOggetto(long idOggetto)
  {
    this.idOggetto = idOggetto;
  }

  public long getIdQuadro()
  {
    return idQuadro;
  }

  public void setIdQuadro(long idQuadro)
  {
    this.idQuadro = idQuadro;
  }

  public long getIdQuadroOggetto()
  {
    return idQuadroOggetto;
  }

  public void setIdQuadroOggetto(long idQuadroOggetto)
  {
    this.idQuadroOggetto = idQuadroOggetto;
  }

  public String getCodOggetto()
  {
    return codOggetto;
  }

  public void setCodOggetto(String codOggetto)
  {
    this.codOggetto = codOggetto;
  }

  public String getDescrOggetto()
  {
    return descrOggetto;
  }

  public void setDescrOggetto(String descrOggetto)
  {
    this.descrOggetto = descrOggetto;
  }

  public String getCodQuadro()
  {
    return codQuadro;
  }

  public void setCodQuadro(String codQuadro)
  {
    this.codQuadro = codQuadro;
  }

  public String getDescrQuadro()
  {
    return descrQuadro;
  }

  public void setDescrQuadro(String descrQuadro)
  {
    this.descrQuadro = descrQuadro;
  }
}
