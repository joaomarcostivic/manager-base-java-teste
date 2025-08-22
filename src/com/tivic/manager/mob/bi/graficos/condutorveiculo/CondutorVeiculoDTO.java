package com.tivic.manager.mob.bi.graficos.condutorveiculo;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.BoatVeiculo;

public class CondutorVeiculoDTO {

	private Boat boat;
	private BoatVeiculo boatVeiculo;
	
	
	public CondutorVeiculoDTO() {}

	public CondutorVeiculoDTO(Boat boat, BoatVeiculo boatVeiculo) {
		super();
		this.boat = boat;
		this.boatVeiculo = boatVeiculo;
	}

	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}
	
	public BoatVeiculo getBoatVeiculo() {
		return boatVeiculo;
	}

	public void setBoatVeiculo(BoatVeiculo boatVeiculo) {
		this.boatVeiculo = boatVeiculo;
	}

	
	
	public void populate() {
		try {
			BeanUtils.copyProperties(this, this.boat);
			BeanUtils.copyProperties(this, this.boatVeiculo);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static class Builder {
		
		private CondutorVeiculoDTO dto;
		public ObjectMapper mapper;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), CondutorVeiculoDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public CondutorVeiculoDTO build() {
			return dto;
		}
		
	}
	
}
