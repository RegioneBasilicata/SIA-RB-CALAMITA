package it.csi.nembo.nemboconf.dto.gestioneeventi;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class CategoriaEventoDTO implements ILoggable{
	private static final long serialVersionUID = -4307923607698000257L;
	private long idCategoriaEvento;
	private String descCategoriaEvento;
	public long getIdCategoriaEvento() {
		return idCategoriaEvento;
	}
	public void setIdCategoriaEvento(long idCategoriaEvento) {
		this.idCategoriaEvento = idCategoriaEvento;
	}
	public String getDescCategoriaEvento() {
		return descCategoriaEvento;
	}
	public void setDescCategoriaEvento(String descCategoriaEvento) {
		this.descCategoriaEvento = descCategoriaEvento;
	}
}
