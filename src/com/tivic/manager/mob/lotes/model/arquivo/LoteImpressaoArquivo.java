package com.tivic.manager.mob.lotes.model.arquivo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoteImpressaoArquivo {

	private int cdLoteImpressao;
	private int cdArquivo;

	public LoteImpressaoArquivo() {
	}

	public LoteImpressaoArquivo(int cdLoteImpressao, int cdArquivo) {
		setCdLoteImpressao(cdLoteImpressao);
		setCdArquivo(cdArquivo);
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public int getCdLoteImpressao() {
		return this.cdLoteImpressao;
	}

	public void setCdArquivo(int cdArquivo) {
		this.cdArquivo = cdArquivo;
	}

	public int getCdArquivo() {
		return this.cdArquivo;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possivel serializar o objeto";
		}
	}
}
