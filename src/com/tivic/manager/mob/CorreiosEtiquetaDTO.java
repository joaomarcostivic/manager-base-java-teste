package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.grl.Logradouro;
import com.tivic.manager.grl.LogradouroDTO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class CorreiosEtiquetaDTO extends CorreiosEtiqueta implements Serializable {
	
	private String idAit;
	
	public CorreiosEtiquetaDTO() {
		
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdEtiqueta: " +  getCdEtiqueta();
		valueToString += ", cdLote: " +  getCdLote();
		valueToString += ", nrEtiqueta: " +  getNrEtiqueta();
		valueToString += ", dtEnvio: " +  sol.util.Util.formatDateTime(getDtEnvio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", sgServico: " +  getSgServico();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", tpStatus: " +  getTpStatus();
		valueToString += ", nrMovimento: " +  getNrMovimento();
		valueToString += ", nrDigitoVerificador: " +  getNrDigitoVerificador();
		valueToString += ", idAit: " +  getIdAit();
		return "{" + valueToString + "}";
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private CorreiosEtiqueta correiosEtiqueta;
		private CorreiosEtiquetaDTO dto;
		private ResultSetMap rsm;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), CorreiosEtiquetaDTO.class);
				
			
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(CorreiosEtiqueta correiosEtiqueta) {
			this.correiosEtiqueta = correiosEtiqueta;
			this.dto = new CorreiosEtiquetaDTO();
			this.montarDTO(correiosEtiqueta);
		}

		public void montarDTO(CorreiosEtiqueta correiosEtiqueta) {
			this.dto.setCdEtiqueta(correiosEtiqueta.getCdEtiqueta());
			this.dto.setCdLote(correiosEtiqueta.getCdLote());
			this.dto.setNrEtiqueta(correiosEtiqueta.getNrEtiqueta());
			this.dto.setDtEnvio(correiosEtiqueta.getDtEnvio());
			this.dto.setSgServico(correiosEtiqueta.getSgServico());
			this.dto.setCdAit(correiosEtiqueta.getCdAit());
			this.dto.setTpStatus(correiosEtiqueta.getTpStatus());
			this.dto.setNrMovimento(correiosEtiqueta.getNrMovimento());
			this.dto.setNrDigitoVerificador(correiosEtiqueta.getNrDigitoVerificador());
		}
	
		public CorreiosEtiquetaDTO build() {
			return this.dto;
		}
	}
}
