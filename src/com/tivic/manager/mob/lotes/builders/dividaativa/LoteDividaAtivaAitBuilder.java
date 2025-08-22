package com.tivic.manager.mob.lotes.builders.dividaativa;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtivaAit;

public class LoteDividaAtivaAitBuilder {
	
	private LoteDividaAtivaAit loteDividaAtivaAit;
	
	public LoteDividaAtivaAitBuilder() {
		loteDividaAtivaAit = new LoteDividaAtivaAit();
	}
	
	public LoteDividaAtivaAitBuilder setCdDividaAtiva(int cdDividaAtiva) {
		loteDividaAtivaAit.setCdLoteDividaAtiva(cdDividaAtiva);
		return this;
	}
	
	public LoteDividaAtivaAitBuilder setCdAit(int cdAit) {
		loteDividaAtivaAit.setCdAit(cdAit);
		return this;
	}
	
	public LoteDividaAtivaAitBuilder setTxtErro(int lgErro) {
		loteDividaAtivaAit.setLgErro(lgErro);
		return this;
	}
	
	public LoteDividaAtivaAitBuilder setDtEnvio(GregorianCalendar dtEnvio) {
		loteDividaAtivaAit.setDtEnvio(dtEnvio);
		return this;
	}
	
	public LoteDividaAtivaAit build() {
		return loteDividaAtivaAit;
	}
}
