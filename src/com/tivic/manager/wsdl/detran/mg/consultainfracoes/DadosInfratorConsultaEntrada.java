package com.tivic.manager.wsdl.detran.mg.consultainfracoes;

import java.util.GregorianCalendar;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.IsoFormat;

public class DadosInfratorConsultaEntrada {
	
	private String documento;
	private int tpDocumento;
	private GregorianCalendar dataInicioConsulta;
	private GregorianCalendar dataFinalConsulta;
	
	public DadosInfratorConsultaEntrada(String documento, int tpDocumento, String dtInicial, String dtFinal) {
		this.documento = documento;
		this.tpDocumento = tpDocumento;
		this.dataInicioConsulta = DateUtil.convStringToCalendar(dtInicial, new IsoFormat());
		this.dataFinalConsulta = DateUtil.convStringToCalendar(dtFinal, new IsoFormat());
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
	public GregorianCalendar getDataFinalConsulta() {
		return dataFinalConsulta;
	}
	public void setDataFinalConsulta(GregorianCalendar dataFinalConsulta) {
		this.dataFinalConsulta = dataFinalConsulta;
	}
	public GregorianCalendar getDataInicioConsulta() {
		return dataInicioConsulta;
	}
	public void setDataInicioConsulta(GregorianCalendar dataInicioConsulta) {
		this.dataInicioConsulta = dataInicioConsulta;
	}
	
	public String toString() {
		try {
			JSONObject jsonDadosCondutorDTO = new JSONObject();
			jsonDadosCondutorDTO.put("documumento", getDocumumento());
			jsonDadosCondutorDTO.put("tpDocumento", getTpDocumento());
			jsonDadosCondutorDTO.put("dtInicial", DateUtil.formatDate(dataInicioConsulta, "dd/MM/yyyy"));
			jsonDadosCondutorDTO.put("dtFinal", DateUtil.formatDate(dataFinalConsulta, "dd/MM/yyyy"));
			return jsonDadosCondutorDTO.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "documumento: " + getDocumumento();
		}
	}
	
}
