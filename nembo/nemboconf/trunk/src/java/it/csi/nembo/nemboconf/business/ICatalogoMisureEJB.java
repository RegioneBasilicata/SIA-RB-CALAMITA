package it.csi.nembo.nemboconf.business;

import java.util.List;
import java.util.Vector;

import javax.ejb.Local;

import it.csi.nembo.nemboconf.dto.DecodificaDTO;
import it.csi.nembo.nemboconf.dto.PrincipioDiSelezioneDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.BeneficiarioDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.FocusAreaDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.InterventiDTO;
import it.csi.nembo.nemboconf.dto.catalogomisura.SettoriDiProduzioneDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.MisuraDTO;
import it.csi.nembo.nemboconf.dto.pianofinanziario.TipoOperazioneDTO;
import it.csi.nembo.nemboconf.exception.ApplicationException;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;

@Local
public interface ICatalogoMisureEJB extends INemboAbstractEJB
{
  public TipoOperazioneDTO getAlberaturaOperazione(long idLivello)
      throws InternalUnexpectedException;

  public List<BeneficiarioDTO> getBeneficiari(long idLivello)
      throws InternalUnexpectedException;

  public List<BeneficiarioDTO> getElencoBeneficiariSelezionabili(long idLivello)
      throws InternalUnexpectedException;

  public List<BeneficiarioDTO> getElencoBeneficiariSelezionati(long idLivello)
      throws InternalUnexpectedException;

  public void insertBeneficiari(long idLivello, Vector<String> vctIdBeneficiari)
      throws InternalUnexpectedException, ApplicationException;

  public void eliminaBeneficiario(long idFgTipologia, long idLivello)
      throws InternalUnexpectedException, ApplicationException;

  public List<InterventiDTO> getInterventi(long idLivello)
      throws InternalUnexpectedException;

  public void eliminaIntervento(long idLivello, long idDescrizioneIntervento)
      throws InternalUnexpectedException, ApplicationException;

  public List<InterventiDTO> getElencoInterventiSelezionabili(long idLivello)
      throws InternalUnexpectedException;

  public List<InterventiDTO> getElencoInterventiSelezionati(long idLivello)
      throws InternalUnexpectedException;

  public void insertInterventi(long idLivello,
      Vector<String> vctIdIDescrIntervento)
      throws InternalUnexpectedException, ApplicationException;

  public List<FocusAreaDTO> getFocusArea(long idLivello)
      throws InternalUnexpectedException;

  public List<FocusAreaDTO> getElencoFocusArea(long idLivello)
      throws InternalUnexpectedException;

  public String abbinaFocusArea(long idLivello, String idFaPrincipale,
      String[] idFaSecondari) throws InternalUnexpectedException;

  public DecodificaDTO<String> getInfoTipoLivello(long idLivello)
      throws InternalUnexpectedException;

  public List<SettoriDiProduzioneDTO> getSettoriDiProduzione(long idLivello)
      throws InternalUnexpectedException;

  public List<SettoriDiProduzioneDTO> getElencoSettoriSelezionabili(
      long idLivello) throws InternalUnexpectedException;

  public List<SettoriDiProduzioneDTO> getElencoSettoriSelezionati(
      long idLivello) throws InternalUnexpectedException;

  public void insertSettori(long idLivello, Vector<String> vctSettori)
      throws InternalUnexpectedException, ApplicationException;

  public void eliminaSettore(long idSettore, long idLivello)
      throws InternalUnexpectedException;

  public List<MisuraDTO> getCatalogoMisure() throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoMisureNemboconf(
      long idTipologiaLivello) throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoSettoriNemboconf()
      throws InternalUnexpectedException;

  public List<DecodificaDTO<String>> getElencoFocusAreaNemboconf()
      throws InternalUnexpectedException;

  public List<PrincipioDiSelezioneDTO> getCriteriDiSelezione(long idLivello)
      throws InternalUnexpectedException;

  public PrincipioDiSelezioneDTO getPrincipioDiSelezioneById(long idLivello,
      long idPrincipioSelezione) throws InternalUnexpectedException;

  public String eliminaPrincipioDiSelezione(long idPrincipioSelezione)
      throws InternalUnexpectedException;

  public String updatePrincipioDiSelezione(PrincipioDiSelezioneDTO principio,
      long idLivello) throws InternalUnexpectedException;

  public String getMaxCodiceCriterio(long idLivello)
      throws InternalUnexpectedException;
}
