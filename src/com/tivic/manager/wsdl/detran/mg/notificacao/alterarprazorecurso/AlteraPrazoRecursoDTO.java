package com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlteraPrazoRecursoDTO {
	
	private int cdAit;
	private int cdLoteImpressao;
	private int tipoRecurso;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar novoPrazoRecurso;
	private int cdUsuario;
	private String dsObservacao;
	private List<Integer> cdsAit;
	private int qtdDiasPrazo;
	
	public int getCdAit() {
		return cdAit;
	}
	
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public int getTipoRecurso() {
		return tipoRecurso;
	}
	
	public void setTipoRecurso(int tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}
	
	public GregorianCalendar getNovoPrazoRecurso() {
		return novoPrazoRecurso;
	}
	
	public void setNovoPrazoRecurso(GregorianCalendar novoPrazoRecurso) {
		this.novoPrazoRecurso = novoPrazoRecurso;
	}
	
	public int getCdUsuario() {
		return cdUsuario;
	}
	
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	
	public String getDsObservacao() {
		return dsObservacao;
	}
	
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
	public List<Integer> getCdsAit() {
		return cdsAit;
	}

	public void setCdsAit(List<Integer> cdsAit) {
		this.cdsAit = cdsAit;
	}

	public int getQtdDiasPrazo() {
		return qtdDiasPrazo;
	}

	public void setQtdDiasPrazo(int qtdDiasPrazo) {
		this.qtdDiasPrazo = qtdDiasPrazo;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
