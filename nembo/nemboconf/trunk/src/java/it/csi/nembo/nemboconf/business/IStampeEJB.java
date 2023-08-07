package it.csi.nembo.nemboconf.business;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.csi.nembo.nemboconf.dto.cruscottobandi.GruppoInfoDTO;
import it.csi.nembo.nemboconf.dto.stampa.IconaStampa;
import it.csi.nembo.nemboconf.dto.stampa.InfoHeader;
import it.csi.nembo.nemboconf.exception.InternalUnexpectedException;

@Local
public interface IStampeEJB extends INemboAbstractEJB
{
  List<String> getElencoCodiciQuadroInOggetto(long idBandoOggetto)
      throws InternalUnexpectedException;

  InfoHeader getInfoHeader(long idBandoOggetto)
      throws InternalUnexpectedException;

  List<GruppoInfoDTO> getDichiarazioni(long idBandoOggetto, String codice)
      throws InternalUnexpectedException;

  Map<Long, List<IconaStampa>> getMapCuStampePerBandoOggettoByIdBando(
      long idBando) throws InternalUnexpectedException;

  public Map<String, List<String>> getTestiStampeIstruttoria(String codiceCdu,
      long idBandoOggetto) throws InternalUnexpectedException;

  String getCodiceTipoBando(long idBandoOggetto)
      throws InternalUnexpectedException;

  String getCodiceOggetto(long idBandoOggetto)
      throws InternalUnexpectedException;
}
