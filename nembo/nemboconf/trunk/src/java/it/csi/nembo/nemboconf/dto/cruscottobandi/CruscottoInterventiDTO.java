package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.math.BigDecimal;
import java.util.List;

import it.csi.nembo.nemboconf.dto.catalogomisura.InfoMisurazioneIntervento;
import it.csi.nembo.nemboconf.dto.internal.ILoggable;
import it.csi.nembo.nemboconf.util.NemboUtils;

public class CruscottoInterventiDTO implements ILoggable
{

  private static final long               serialVersionUID = 4601339650061084885L;

  private long                            idBando;
  private long                            idTipoAggregazione;
  private long                            idLivello;
  private long                            idDescrizioneIntervento;
  private long                            idTipoLocalizzazione;
  private String                          tipoIntervento;
  private String                          descrIntervento;
  private String                          localizzazione;
  private String                          operazione;
  private String                          flagGestioneCostoUnitario;
  private BigDecimal                      costoUnitMinimo;
  private BigDecimal                      costoUnitMassimo;
  private List<InfoMisurazioneIntervento> infoMisurazioni;

  public long getIdBando()
  {
    return idBando;
  }

  public void setIdBando(long idBando)
  {
    this.idBando = idBando;
  }

  public long getIdTipoAggregazione()
  {
    return idTipoAggregazione;
  }

  public void setIdTipoAggregazione(long idTipoAggregazione)
  {
    this.idTipoAggregazione = idTipoAggregazione;
  }

  public long getIdDescrizioneIntervento()
  {
    return idDescrizioneIntervento;
  }

  public void setIdDescrizioneIntervento(long idDescrizioneIntervento)
  {
    this.idDescrizioneIntervento = idDescrizioneIntervento;
  }

  public long getIdTipoLocalizzazione()
  {
    return idTipoLocalizzazione;
  }

  public void setIdTipoLocalizzazione(long idTipoLocalizzazione)
  {
    this.idTipoLocalizzazione = idTipoLocalizzazione;
  }

  public String getTipoIntervento()
  {
    return tipoIntervento;
  }

  public void setTipoIntervento(String tipoIntervento)
  {
    this.tipoIntervento = tipoIntervento;
  }

  public String getDescrIntervento()
  {
    return descrIntervento;
  }

  public void setDescrIntervento(String descrIntervento)
  {
    this.descrIntervento = descrIntervento;
  }

  public String getLocalizzazione()
  {
    return localizzazione;
  }

  public void setLocalizzazione(String localizzazione)
  {
    this.localizzazione = localizzazione;
  }

  public String getOperazione()
  {
    return operazione;
  }

  public void setOperazione(String operazione)
  {
    this.operazione = operazione;
  }

  public BigDecimal getCostoUnitMinimo()
  {
    return costoUnitMinimo;
  }

  public String getCostoUnitMinimoStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(costoUnitMinimo, 2, false);
  }

  public String getCostoUnitMassimoStr()
  {
    return NemboUtils.FORMAT.formatGenericNumber(costoUnitMassimo, 2,
        false);
  }

  public void setCostoUnitMinimo(BigDecimal costoUnitMinimo)
  {
    this.costoUnitMinimo = costoUnitMinimo;
  }

  public BigDecimal getCostoUnitMassimo()
  {
    return costoUnitMassimo;
  }

  public void setCostoUnitMassimo(BigDecimal costoUnitMassimo)
  {
    this.costoUnitMassimo = costoUnitMassimo;
  }

  public long getIdLivello()
  {
    return idLivello;
  }

  public void setIdLivello(long idLivello)
  {
    this.idLivello = idLivello;
  }

  public String getFlagGestioneCostoUnitario()
  {
    return flagGestioneCostoUnitario;
  }

  public void setFlagGestioneCostoUnitario(String flagGestioneCostoUnitario)
  {
    this.flagGestioneCostoUnitario = flagGestioneCostoUnitario;
  }

  public List<InfoMisurazioneIntervento> getInfoMisurazioni()
  {
    return infoMisurazioni;
  }

  public void setInfoMisurazioni(
      List<InfoMisurazioneIntervento> infoMisurazioni)
  {
    this.infoMisurazioni = infoMisurazioni;
  }

}
