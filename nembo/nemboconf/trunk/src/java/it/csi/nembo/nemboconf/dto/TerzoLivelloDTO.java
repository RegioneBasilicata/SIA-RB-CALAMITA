package it.csi.nembo.nemboconf.dto;

import java.util.StringTokenizer;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class TerzoLivelloDTO extends SecondoLivelloDTO implements ILoggable
{
  /**
   * 
   */
  private static final long serialVersionUID = 3273434566171950998L;
  private String            tipoOp;
  private String            settore;
  private String            descrSettore;

  public TerzoLivelloDTO()
  {
  }

  public TerzoLivelloDTO(Long misura, String sottomisura, String descrizione)
  {
    super(misura, sottomisura, descrizione);
  }

  public TerzoLivelloDTO(Long misura, String sottomisura, String tipoOp,
      String settore, String descrizione)
  {
    super(misura, sottomisura, descrizione);
    this.tipoOp = tipoOp;
    this.settore = settore;
  }

  public String getTipoOp()
  {
    return tipoOp;
  }

  public String getCodiceTipoOp()
  {
    // es: 19.2.4.1.1 restituisce 4.1.1. Vengono eliminati misura e sottomisura
    // dal codice
    String tmp = "";
    StringTokenizer str = new StringTokenizer(tipoOp, ".");
    int i = 0;
    while (str.hasMoreElements())
    {
      if (i > 1)
      {
        tmp += str.nextElement() + ".";
      }
      else
      {
        i++;
        str.nextElement();
      }
    }
    return tmp.substring(0, tmp.length() - 1);
  }

  public void setTipoOp(String tipoOp)
  {
    this.tipoOp = tipoOp;
  }

  public String getSettore()
  {
    return settore;
  }

  public void setSettore(String settore)
  {
    this.settore = settore;
  }

  public String getDescrSettore()
  {
    return descrSettore;
  }

  public void setDescrSettore(String descrSettore)
  {
    this.descrSettore = descrSettore;
  }

}
