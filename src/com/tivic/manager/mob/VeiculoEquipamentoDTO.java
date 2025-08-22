package com.tivic.manager.mob;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.Util;

public class VeiculoEquipamentoDTO {
	
	private VeiculoEquipamento veiculoEquipamento;
	private Equipamento equipamento;
	
	public VeiculoEquipamentoDTO() {}

	public VeiculoEquipamentoDTO(VeiculoEquipamento veiculoEquipamento, Equipamento equipamento) {
		super();
		this.veiculoEquipamento = veiculoEquipamento;
		this.equipamento = equipamento;
	}

	public VeiculoEquipamento getVeiculoEquipamento() {
		return veiculoEquipamento;
	}

	public void setVeiculoEquipamento(VeiculoEquipamento veiculoEquipamento) {
		this.veiculoEquipamento = veiculoEquipamento;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}
	
	
	public void populate() {
		try {
			BeanUtils.copyProperties(this, this.veiculoEquipamento);
			BeanUtils.copyProperties(this, this.equipamento);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static class Builder {
		
		private VeiculoEquipamentoDTO dto;
		public ObjectMapper mapper;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), VeiculoEquipamentoDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public VeiculoEquipamentoDTO build() {
			return dto;
		}
		
	}
	
}
