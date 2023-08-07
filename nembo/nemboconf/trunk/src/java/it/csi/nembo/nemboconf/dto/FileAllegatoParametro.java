package it.csi.nembo.nemboconf.dto;

public class FileAllegatoParametro extends Parametro
{
  /** serialVersionUID */
  private static final long serialVersionUID = -2415323387705339838L;
  protected byte[]          fileAllegato;

  public byte[] getFileAllegato()
  {
    return fileAllegato;
  }

  public void setFileAllegato(byte[] fileAllegato)
  {
    this.fileAllegato = fileAllegato;
  }
}
