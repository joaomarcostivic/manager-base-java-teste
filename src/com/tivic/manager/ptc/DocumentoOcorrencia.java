package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DocumentoOcorrencia {

	private int cdDocumento;
	private int cdTipoOcorrencia;
	private int cdOcorrencia;
	private int cdUsuario;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;
	private String txtOcorrencia;
	private int tpVisibilidade;
	private int tpConsistencia;

	public DocumentoOcorrencia() { } 
	
	public DocumentoOcorrencia(int cdDocumento,
			int cdTipoOcorrencia,
			int cdOcorrencia,
			int cdUsuario,
			GregorianCalendar dtOcorrencia,
			String txtOcorrencia,
			int tpVisibilidade,
			int tpConsistencia){
		setCdDocumento(cdDocumento);
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setCdOcorrencia(cdOcorrencia);
		setCdUsuario(cdUsuario);
		setDtOcorrencia(dtOcorrencia);
		setTxtOcorrencia(txtOcorrencia);
		setTpVisibilidade(tpVisibilidade);
		setTpConsistencia(tpConsistencia);
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setTxtOcorrencia(String txtOcorrencia){
		this.txtOcorrencia=txtOcorrencia;
	}
	public String getTxtOcorrencia(){
		return this.txtOcorrencia;
	}
	public void setTpVisibilidade(int tpVisibilidade){
		this.tpVisibilidade=tpVisibilidade;
	}
	public int getTpVisibilidade(){
		return this.tpVisibilidade;
	}
	public int getTpConsistencia() {
		return tpConsistencia;
	}
	public void setTpConsistencia(int tpConsistencia) {
		this.tpConsistencia = tpConsistencia;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumento: " +  getCdDocumento();
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", tpVisibilidade: " +  getTpVisibilidade();
		valueToString += ", tpConsistencia: " +  getTpConsistencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoOcorrencia(getCdDocumento(),
			getCdTipoOcorrencia(),
			getCdOcorrencia(),
			getCdUsuario(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getTxtOcorrencia(),
			getTpVisibilidade(),
			getTpConsistencia());
	}

}