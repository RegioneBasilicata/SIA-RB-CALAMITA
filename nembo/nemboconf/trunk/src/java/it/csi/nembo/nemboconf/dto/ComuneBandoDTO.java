package it.csi.nembo.nemboconf.dto;


import java.util.List;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class ComuneBandoDTO extends ComuneDTO implements ILoggable
{
  
  private static final long serialVersionUID = -4938245835707727317L;
  private List<DecodificaDTO<Integer>> decodificaFogli;
  private List<Integer>		fogli;
  private Long 				idBandoComune;
  private String			flagFoglio;
  




	public List<Integer> getFogli() {
		return fogli;
	}

	public void setFogli(List<Integer> fogli) {
		this.fogli = fogli;
	}

	public Long getIdBandoComune() {
		return idBandoComune;
	}

	public void setIdBandoComune(Long idBandoComune) {
		this.idBandoComune = idBandoComune;
	}

	public List<DecodificaDTO<Integer>> getDecodificaFogli() {
		return decodificaFogli;
	}

	public void setDecodificaFogli(List<DecodificaDTO<Integer>> decodificaFogli) 
	{
		this.decodificaFogli = decodificaFogli;
	}

	public String getFlagFoglio() 
	{
		return flagFoglio;
	}

	public void setFlagFoglio(String flagFoglio) 
	{
		this.flagFoglio = flagFoglio;
	}
	
	public String getComuneProvincia()
	{
		return getDescrizioneComune() + " (" + getSiglaProvincia() + ")";
	}
	
	public boolean getTuttiFogli()
	{
		if(flagFoglio.trim().equals("S"))
		{
			return false;
		}
		return true;
	}
	
	
	
	
}
