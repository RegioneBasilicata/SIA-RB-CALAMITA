package it.csi.nembo.nemboconf.dto.cruscottobandi;

import java.util.Date;

import it.csi.nembo.nemboconf.dto.internal.ILoggable;

public class DocumentiRichiestiDTO implements ILoggable {

	private static final long serialVersionUID = -460625869410765862L;
	private String descGruppoOggetto;
	private long idBandoOggetto;
	private String descOggetto;
	private String sezione;
	private String descSezioneDocRichiesti;
	private String descTipoDocRichiesti;
	private String testoSezione;
	private Long idTipoDocRicBandoOgg;
	private long idTipoDocRicOggetto;
	private Date dataFineValTipoDocRic;
	
	public String getDescGruppoOggetto() {
		return descGruppoOggetto;
	}

	public void setDescGruppoOggetto(String descGruppoOggetto) {
		this.descGruppoOggetto = descGruppoOggetto;
	}

	public long getIdBandoOggetto() {
		return idBandoOggetto;
	}

	public void setIdBandoOggetto(long idBandoOggetto) {
		this.idBandoOggetto = idBandoOggetto;
	}

	public String getDescOggetto() {
		return descOggetto;
	}

	public void setDescOggetto(String descOggetto) {
		this.descOggetto = descOggetto;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getDescSezioneDocRichiesti() {
		return descSezioneDocRichiesti;
	}

	public void setDescSezioneDocRichiesti(String descSezioneDocRichiesti) {
		this.descSezioneDocRichiesti = descSezioneDocRichiesti;
	}

	public String getDescTipoDocRichiesti() {
		return descTipoDocRichiesti;
	}

	public void setDescTipoDocRichiesti(String descTipoDocRichiesti) {
		this.descTipoDocRichiesti = descTipoDocRichiesti;
	}

	public String getTestoSezione() {
		return testoSezione;
	}

	public void setTestoSezione(String testoSezione) {
		this.testoSezione = testoSezione;
	}

	public Long getIdTipoDocRicBandoOgg() {
		return idTipoDocRicBandoOgg;
	}

	public void setIdTipoDocRicBandoOgg(Long idTipoDocRicBandoOgg) {
		this.idTipoDocRicBandoOgg = idTipoDocRicBandoOgg;
	}

	public long getIdTipoDocRicOggetto() {
		return idTipoDocRicOggetto;
	}

	public void setIdTipoDocRicOggetto(long idTipoDocRicOggetto) {
		this.idTipoDocRicOggetto = idTipoDocRicOggetto;
	}

	public Date getDataFineValTipoDocRic() {
		return dataFineValTipoDocRic;
	}

	public void setDataFineValTipoDocRic(Date dataFineValTipoDocRic) {
		this.dataFineValTipoDocRic = dataFineValTipoDocRic;
	}
}
