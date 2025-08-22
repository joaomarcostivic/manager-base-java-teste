package com.tivic.manager.mob.lotes.model.dividaativa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoteDividaAtiva {

	private int cdLoteDividaAtiva;
	private int cdLote;
	private int stLote;
	private int cdArquivoRetorno;

	public LoteDividaAtiva() {
	}

	public LoteDividaAtiva(int cdLoteDividaAtiva, int cdLote, int stLote, int cdArquivoRetorno) {
		setCdLoteDividaAtiva(cdLoteDividaAtiva);
		setCdLote(cdLote);
		setStLote(stLote);
		setCdArquivoRetorno(cdArquivoRetorno);
	}

	public void setCdLoteDividaAtiva(int cdLoteDividaAtiva) {
		this.cdLoteDividaAtiva = cdLoteDividaAtiva;
	}

	public int getCdLoteDividaAtiva() {
		return this.cdLoteDividaAtiva;
	}

	public void setCdLote(int cdLote) {
		this.cdLote = cdLote;
	}

	public int getCdLote() {
		return this.cdLote;
	}

	public void setStLote(int stLote) {
		this.stLote = stLote;
	}

	public int getStLote() {
		return this.stLote;
	}

	public void setCdArquivoRetorno(int cdArquivoRetorno) {
		this.cdArquivoRetorno = cdArquivoRetorno;
	}

	public int getCdArquivoRetorno() {
		return this.cdArquivoRetorno;
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