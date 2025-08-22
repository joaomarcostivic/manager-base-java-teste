package com.tivic.manager.mob;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class CorreiosLoteDTO extends CorreiosLote implements Serializable{
	private int qtdEtiquetasLivres;
	private ObjectMapper mapper;
	
	public CorreiosLoteDTO() {
		
	}
	
	
	public int getQtdEtiquetasLivres() {
		return this.qtdEtiquetasLivres;
	}
	
	public void setQtdEtiquetasLivres(int qtdEtiquetasLivres) {
		this.qtdEtiquetasLivres = qtdEtiquetasLivres;
	}
	
	
	
	@Override
	public String toString() {
		String valueToString = "";
		valueToString += "cdLote: " +  getCdLote();
		valueToString += ", dtLote: " +  sol.util.Util.formatDateTime(getDtLote(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrInicial: " +  getNrInicial();
		valueToString += ", nrFinal: " +  getNrFinal();
		valueToString += ", stLote: " +  getStLote();
		valueToString += ", tpLote: " +  getTpLote();
		valueToString += ", sgLote: " +  getSgLote();
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtdEtiquetasLivres: " +  getQtdEtiquetasLivres();
		return "{" + valueToString + "}";
	}
	
	
	public static class Builder {

		private ObjectMapper mapper;
		private CorreiosLoteDTO dto;
		private CorreiosLote correiosLote;
		private ResultSetMap rsm;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), CorreiosLoteDTO.class);
				
				this.dto.setQtdEtiquetasLivres(dto.getQtdEtiquetasLivres());
			
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(CorreiosLote correiosLote) {
			this.correiosLote = correiosLote;
			this.dto = new CorreiosLoteDTO();
			this.montarDTO(correiosLote);
		}
		
		
		public Builder getEtiquetasLivres() {
			int qtdEtiquetasLivres = CorreiosEtiquetaServices.etiquetasLivres(this.dto.getCdLote());
			this.dto.setQtdEtiquetasLivres(qtdEtiquetasLivres);
			return this;
		}
		
		
	
		public void montarDTO(CorreiosLote correiosLote) {
			this.dto.setCdLote(correiosLote.getCdLote());
			this.dto.setDtLote(correiosLote.getDtLote());
			this.dto.setDtVencimento(correiosLote.getDtVencimento());
			this.dto.setNrInicial(correiosLote.getNrInicial());
			this.dto.setNrFinal(correiosLote.getNrFinal());
			this.dto.setSgLote(correiosLote.getSgLote());
			this.dto.setStLote(correiosLote.getStLote());
			this.dto.setTpLote(correiosLote.getTpLote());
		}
	
		public CorreiosLoteDTO build() {
			return this.dto;
		}
	
	}
	

	
}
