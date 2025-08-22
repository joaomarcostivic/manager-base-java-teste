package com.tivic.manager.mob;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.grl.Empresa;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.util.Util;
import com.tivic.manager.mob.Linha;
import com.tivic.manager.mob.ConcessaoVeiculo;
import com.tivic.manager.mob.Concessao;

public class HorarioAfericaoDTO {
	
	private HorarioAfericao horarioAfericao;
	private Pessoa concessionario;
	private Agente agente;
	private Linha linha;
	private ConcessaoVeiculo concessaoVeiculo;
	private Empresa empresa;
  
	public HorarioAfericaoDTO() {
		super();	
	}

	public HorarioAfericaoDTO(HorarioAfericao horarioAfericao, Pessoa concessionario, Agente agente, Linha linha, ConcessaoVeiculo concessaoVeiculo, Empresa empresa ) {
		super();
		this.horarioAfericao = horarioAfericao;
		this.concessionario =concessionario;
		this.agente= agente;
		this.linha = linha;
		this.concessaoVeiculo = concessaoVeiculo;
		this.empresa = empresa;
	}
	
	public HorarioAfericao getHorarioAfericao() {
		return horarioAfericao;
	}

	public void setHorarioAfericao(HorarioAfericao horarioAfericao) {
		this.horarioAfericao = horarioAfericao;
	}


	public Pessoa getConcessionario() {
		return concessionario;
	}

	public void setConcessionario(Pessoa concessionario) {
		this.concessionario = concessionario;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public ConcessaoVeiculo getConcessaoVeiculo() {
		return concessaoVeiculo;
	}

	public void setConcessaoVeiculo(ConcessaoVeiculo concessaoVeiculo) {
		this.concessaoVeiculo = concessaoVeiculo;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	} 
	
	public static class Builder {

		private ObjectMapper mapper;
		private HorarioAfericaoDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), HorarioAfericaoDTO.class);
				
				this.dto.setLinha(dto.getLinha());
				this.dto.setConcessaoVeiculo(dto.getConcessaoVeiculo());				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public HorarioAfericaoDTO build() {
			return dto;
		}
		
		@Override
		public String toString() {
			try {
				return mapper.writeValueAsString(this.dto);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	
}
