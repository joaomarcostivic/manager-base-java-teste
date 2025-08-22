package com.tivic.manager.adapter.base.antiga.marcamodelo;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarcaModeloOld {
	private int codMarca;
	private String nmMarca;
	private String nmModelo;
	private GregorianCalendar dtAtualizacao;
	
	public MarcaModeloOld() {}
	
	public MarcaModeloOld(int codMarca,
			String nmMarca,
			String nmModelo,
			GregorianCalendar dtAtualizacao){
		setCodMarca(codMarca);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setDtAtualizacao(dtAtualizacao);
	}
	
	public int getCodMarca() {
		return codMarca;
	}
	
	public void setCodMarca(int codMarca) {
		this.codMarca = codMarca;
	}
	
	public String getNmMarca() {
		return nmMarca;
	}
	
	public void setNmMarca(String nmMarca) {
		this.nmMarca = nmMarca;
	}
	
	public String getNmModelo() {
		return nmModelo;
	}
	
	public void setNmModelo(String nmModelo) {
		this.nmModelo = nmModelo;
	}
	
	
	public GregorianCalendar getDtAtualizacao() {
		return dtAtualizacao;
	}
	
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
	
}
