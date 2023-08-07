package it.csi.nembo.nemboconf.util.stampa.misurepremio.postistruttoria;

import it.csi.nembo.nemboconf.util.stampa.Stampa;

public class VerbalePostIstruttoriaPremio extends Stampa
{
  public static final String ROOT_TAG = "Domanda";

  private String             cuaaBeneficiario;
  private String             identificativoProcedimento;

  protected String getCodiceModulo()
  {
    return "";
  }

  protected String getCodiceModello()
  {
    return "";
  }

  protected String getRifAdobe()
  {
    return "";
  }

  @Override
  public String getDefaultFileName()
  {
    String ret = "";
    if (cuaaBeneficiario != null)
      ret += cuaaBeneficiario;

    if (identificativoProcedimento != null)
      ret += "_" + identificativoProcedimento;

    return ret + "_Verbale post istruttoria domanda premio.pdf";
  }

  @Override
  public byte[] genera(long idBandoOggetto) throws Exception
  {
    return null;
  }

}