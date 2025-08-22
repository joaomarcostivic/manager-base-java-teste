package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.util.Util;

public class LoteHorariosDTO {
	private GregorianCalendar hrInicial;
	private GregorianCalendar hrFinal;
	private int periodo;
	private int cdTabelaHorario;
	private int cdLinha;
	private int cdRota;
	private ArrayList<com.tivic.manager.mob.tabelashorarios.ParadaDTO> paradas;
	
	public LoteHorariosDTO() {
			
	}
	
	public LoteHorariosDTO(GregorianCalendar hrInicial, GregorianCalendar hrFinal, int periodo,int cdTabelaHorario, int cdLinha, int cdRota, ArrayList<com.tivic.manager.mob.tabelashorarios.ParadaDTO> paradas) {
		this.hrInicial = hrInicial;
		this.hrFinal =hrFinal;
		this.periodo= periodo;
		this.cdTabelaHorario = cdTabelaHorario;
		this.cdLinha = cdLinha;
		this.cdRota = cdRota;
		this.paradas = paradas;
	}
	
	public GregorianCalendar getHrInicial() {
		return hrInicial;
	}

	public void setHrInicial(GregorianCalendar hrInicial) {
		this.hrInicial = hrInicial;
	}

	public GregorianCalendar getHrFinal() {
		return hrFinal;
	}

	public void setHrFinal(GregorianCalendar hrFinal) {
		this.hrFinal = hrFinal;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public int getCdTabelaHorario() {
		return cdTabelaHorario;
	}

	public void setCdTabelaHorario(int cdTabelaHorario) {
		this.cdTabelaHorario = cdTabelaHorario;
	}

	public ArrayList<com.tivic.manager.mob.tabelashorarios.ParadaDTO> getParadas() {
		return paradas;
	}

	public void setParadas(ArrayList<com.tivic.manager.mob.tabelashorarios.ParadaDTO> paradas) {
		this.paradas = paradas;
	}

	public int getCdLinha() {
		return cdLinha;
	}

	public void setCdLinha(int cdLinha) {
		this.cdLinha = cdLinha;
	}

	public int getCdRota() {
		return cdRota;
	}

	public void setCdRota(int cdRota) {
		this.cdRota = cdRota;
	}



	public static class Builder {

		private ObjectMapper mapper;
		private LoteHorariosDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), LoteHorariosDTO.class);
					
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public LoteHorariosDTO build() {
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
