package com.tivic.manager.mob.lotes.builders.dividaativa;

import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtiva;

public class LoteDividaAtivaBuilder {
	
	private LoteDividaAtiva loteDitivaAtiva;
	
	public LoteDividaAtivaBuilder() {
		loteDitivaAtiva = new LoteDividaAtiva();
	}
	
	public LoteDividaAtivaBuilder setCdLoteDividaATiva(int cdLoteDividaATiva) {
		loteDitivaAtiva.setCdLoteDividaAtiva(cdLoteDividaATiva);
		return this;
	}

	public LoteDividaAtivaBuilder setCdLote(int cdLote) {
		loteDitivaAtiva.setCdLote(cdLote);
		return this;
	}
	
	public LoteDividaAtivaBuilder setStLote(int stLote) {
		loteDitivaAtiva.setStLote(stLote);
		return this;
	}
	
	public LoteDividaAtivaBuilder setCdArquivoRetorno(int cdArquivoRetorno) {
		loteDitivaAtiva.setCdArquivoRetorno(cdArquivoRetorno);
		return this;
	}
	
	public LoteDividaAtiva build() {
		return loteDitivaAtiva;
	}
}
