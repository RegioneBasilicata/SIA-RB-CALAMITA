package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.Date;
import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class ControlloAmministrativoDTO implements ILoggable
{

  private static final long                serialVersionUID = 4601339650061084885L;

  private long                             idQuadroOggControlloAmm;
  private long                             idControlloAmministrativo;
  private long                             idBandoOggetto;
  private Long                             idControlloAmministratPadre;
  private String                           codice;
  private String                           descrizione;
  private String                           flagObbligatorio;
  private String                           flagSelezionato;
  private String                           flagAttivo;
  private Date                             dataFineValidita;

  private List<ControlloAmministrativoDTO> controlliFigli;
  private List<LivelloDTO>                 elencoLivelli;

  public long getIdQuadroOggControlloAmm()
  {
    return idQuadroOggControlloAmm;
  }

  public void setIdQuadroOggControlloAmm(long idQuadroOggControlloAmm)
  {
    this.idQuadroOggControlloAmm = idQuadroOggControlloAmm;
  }

  public long getIdControlloAmministrativo()
  {
    return idControlloAmministrativo;
  }

  public void setIdControlloAmministrativo(long idControlloAmministrativo)
  {
    this.idControlloAmministrativo = idControlloAmministrativo;
  }

  public Long getIdControlloAmministratPadre()
  {
    return idControlloAmministratPadre;
  }

  public void setIdControlloAmministratPadre(Long idControlloAmministratPadre)
  {
    this.idControlloAmministratPadre = idControlloAmministratPadre;
  }

  public String getCodice()
  {
    return codice;
  }

  public void setCodice(String codice)
  {
    this.codice = codice;
  }

  public String getDescrizione()
  {
    return descrizione;
  }

  public void setDescrizione(String descrizione)
  {
    this.descrizione = descrizione;
  }

  public String getFlagObbligatorio()
  {
    return flagObbligatorio;
  }

  public void setFlagObbligatorio(String flagObbligatorio)
  {
    this.flagObbligatorio = flagObbligatorio;
  }

  public Date getDataFineValidita()
  {
    return dataFineValidita;
  }

  public void setDataFineValidita(Date dataFineValidita)
  {
    this.dataFineValidita = dataFineValidita;
  }

  public String getFlagSelezionato()
  {
    return flagSelezionato;
  }

  public void setFlagSelezionato(String flagSelezionato)
  {
    this.flagSelezionato = flagSelezionato;
  }

  public List<ControlloAmministrativoDTO> getControlliFigli()
  {
    return controlliFigli;
  }

  public void setControlliFigli(List<ControlloAmministrativoDTO> controlliFigli)
  {
    this.controlliFigli = controlliFigli;
  }

  public String getFlagAttivo()
  {
    return flagAttivo;
  }

  public void setFlagAttivo(String flagAttivo)
  {
    this.flagAttivo = flagAttivo;
  }

  public List<LivelloDTO> getElencoLivelli()
  {
    return elencoLivelli;
  }

  public void setElencoLivelli(List<LivelloDTO> elencoLivelli)
  {
    this.elencoLivelli = elencoLivelli;
  }

  public long getIdBandoOggetto()
  {
    return idBandoOggetto;
  }

  public void setIdBandoOggetto(long idBandoOggetto)
  {
    this.idBandoOggetto = idBandoOggetto;
  }

}
