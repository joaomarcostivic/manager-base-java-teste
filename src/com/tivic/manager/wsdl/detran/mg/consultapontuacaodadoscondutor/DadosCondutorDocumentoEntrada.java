package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DadosCondutorDocumentoEntrada {
	
	private String documento;
	private int tpDocumento;
	
	public DadosCondutorDocumentoEntrada(String documento, int tpDocumento) {
		this.documento = documento;
		this.tpDocumento = tpDocumento;
	}
	
	public String getDocumumento() {
		return documento;
	}
	public void setDocumumento(String documumento) {
		this.documento = documumento;
	}
	public int getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	
	public String toString() {
		try {
			JSONObject jsonDadosCondutorDTO = new JSONObject();
			jsonDadosCondutorDTO.put("documumento", getDocumumento());
			jsonDadosCondutorDTO.put("tpDocumento", getTpDocumento());
			return jsonDadosCondutorDTO.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "documumento: " + getDocumumento();
		}
	}
	
}
