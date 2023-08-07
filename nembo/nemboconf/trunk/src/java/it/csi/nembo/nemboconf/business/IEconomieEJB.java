package it.csi.nembo.nemboconf.business;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.ContributoDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.EconomiaDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.RisorsaAttivataDTO;
import it.csi.nembo.nemboconf.dto.cruscottobandi.gestioneeconomica.TipoImportoDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;

@Local
public interface IEconomieEJB extends INemboAbstractEJB
{
  public List<ContributoDTO> getElencoContributi(long idBando,
      List<Long> idLivelli) throws InternalUnexpectedException;

  public void updateContributoLivelli(List<ContributoDTO> lContributi)
      throws InternalUnexpectedException;

  public List<TipoImportoDTO> getElencoTipiImportoDisponibili(long idBando)
      throws InternalUnexpectedException;

  public void insertTipoFondo(long idBando, long idTipoImporto,
      long idUtenteLogin) throws InternalUnexpectedException;

  public List<TipoImportoDTO> getElencoFondiByIdBando(long idBando)
      throws InternalUnexpectedException;

  public void updateRisorseTipoImporto(long idBando, long idTipoImporto,
      long idUtenteAggiornamennto, List<RisorsaAttivataDTO> risorse)
      throws InternalUnexpectedException, ApplicationException;

  public void deleteRisorsa(long idRisorseLivelloBando)
      throws InternalUnexpectedException;

  public void inserisciEconomie(long idBando, long idTipoImporto,
      long idUtenteAggiornamennto, long idRisorseLivelloBando,
      List<EconomiaDTO> economie)
      throws InternalUnexpectedException, ApplicationException;

  public void cleanFondiVuoti(long idBando) throws InternalUnexpectedException;

  public BigDecimal getTotaleImportoLiquidatoEconomia(
      long idRisorseLivelloBando) throws InternalUnexpectedException;

  public RisorsaAttivataDTO getRisorsa(long idRisorseLivelloBando)
      throws InternalUnexpectedException;
}
