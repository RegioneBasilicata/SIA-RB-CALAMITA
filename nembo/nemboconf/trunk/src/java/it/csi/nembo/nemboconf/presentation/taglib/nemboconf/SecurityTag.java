
package it.csi.nembo.nemboconf.presentation.taglib.nemboconf;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.commons.validator.GenericValidator;

import it.csi.nembo.nemboconf.util.NemboUtils;
import it.csi.papua.papuaserv.dto.gestioneutenti.ws.UtenteAbilitazioni;

public class SecurityTag extends BaseTag
{
  /** serialVersionUID */
  private static final long serialVersionUID = 6582461845796172740L;
  private String            dirittoAccessoMinimo;
  private String            macroCDU;
  private long              idLivello;

  @Override
  public int doEndTag() throws JspException
  {
    UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni) this.pageContext
        .getSession().getAttribute("utenteAbilitazioni");
    boolean abilitazione = true;
    if ("W".equals(dirittoAccessoMinimo)
        && !NemboUtils.PAPUASERV.isUtenteReadWrite(utenteAbilitazioni))
    {
      abilitazione = false;
    }
    if (!GenericValidator.isBlankOrNull(macroCDU) && !NemboUtils.PAPUASERV
        .isMacroCUAbilitato(utenteAbilitazioni, macroCDU))
    {
      abilitazione = false;
    }

    if (idLivello > 0)
    {
      if (!NemboUtils.PAPUASERV.isLivelloAbilitato(utenteAbilitazioni,
          idLivello))
      {
        abilitazione = false;
      }
    }

    if (abilitazione && this.bodyContent != null)
    {
      try
      {
        this.pageContext.getOut().write(this.bodyContent.getString());
      }
      catch (IOException e)
      {
        throw new JspException(e);
      }
    }

    return super.doEndTag();
  }

  public String getDirittoAccessoMinimo()
  {
    return dirittoAccessoMinimo;
  }

  public void setDirittoAccessoMinimo(String dirittoAccessoMinimo)
  {
    this.dirittoAccessoMinimo = dirittoAccessoMinimo;
  }

  public String getMacroCDU()
  {
    return macroCDU;
  }

  public void setMacroCDU(String macroCDU)
  {
    this.macroCDU = macroCDU;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

}
