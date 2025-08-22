package com.tivic.manager.mob.ait.relatorios;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;
import com.tivic.manager.util.Util;

public class RelatorioRecursoDTO {
	String nmProprietario;
	String nmProprietarioAutuacao;
	String nrPlaca;
	int cdAit;
	int stAvisoRecebimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtEmissao;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtAvisoRecebimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtResultadoDefesa;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtArNai;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtPublicacaoDo;
	
	public String getNmProprietario() {
		return nmProprietario;
	}
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	public String getNmProprietarioAutuacao() {
		return nmProprietarioAutuacao;
	}
	public void setNmProprietarioAutuacao(String nmProprietarioAutuacao) {
		this.nmProprietarioAutuacao = nmProprietarioAutuacao;
	}
	public GregorianCalendar getDtEmissao() {
		return dtEmissao;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao) {
		this.dtEmissao = dtEmissao;
	}
	public GregorianCalendar getDtAvisoRecebimento() {
		return dtAvisoRecebimento;
	}
	public void setDtAvisoRecebimento(GregorianCalendar dtAvisoRecebimento) {
		this.dtAvisoRecebimento = dtAvisoRecebimento;
	}
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}
	public GregorianCalendar getDtResultadoDefesa() {
		return dtResultadoDefesa;
	}
	public void setDtResultadoDefesa(GregorianCalendar dtResultadoDefesa) {
		this.dtResultadoDefesa = dtResultadoDefesa;
	}
	public GregorianCalendar getDtArNai() {
		return dtArNai;
	}
	public void setDtArNai(GregorianCalendar dtArNai) {
		this.dtArNai = dtArNai;
	}
	public GregorianCalendar getDtPublicacaoDo() {
		return dtPublicacaoDo;
	}
	public void setDtPublicacaoDo(GregorianCalendar dtPublicacaoDo) {
		this.dtPublicacaoDo = dtPublicacaoDo;
	}
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public int getStAvisoRecebimento() {
		return stAvisoRecebimento;
	}
	public void setStAvisoRecebimento(int stAvisoRecebimento) {
		this.stAvisoRecebimento = stAvisoRecebimento;
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private RelatorioRecursoDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), RelatorioRecursoDTO.class);
							
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		

		public RelatorioRecursoDTO build() {
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
