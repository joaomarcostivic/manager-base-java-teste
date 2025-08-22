package com.tivic.manager.grl.banco;

import com.tivic.sol.search.SearchCriterios;

public class BancoSearch {

	private SearchCriterios criterios;
	
	public BancoSearch() {
		this.criterios = new SearchCriterios();
	}
	
	public BancoSearch setIdBanco(String idBanco) {
		this.criterios.addCriteriosLikeAnyString("id_banco", idBanco, idBanco != null && !idBanco.trim().equals(""));
		return this;
	}
	
	public BancoSearch setNrBanco(String nrBanco) {
		this.criterios.addCriteriosLikeAnyString("nr_banco", nrBanco, nrBanco != null && !nrBanco.trim().equals(""));
		return this;
	}
	
	public BancoSearch setNmBanco(String nmBanco) {
		this.criterios.addCriteriosLikeAnyString("nm_banco", nmBanco, nmBanco != null && !nmBanco.trim().equals(""));
		return this;
	}
	
	public BancoSearch setLgBancoConveniado(boolean lgBancoConveniado) {
		this.criterios.addCriteriosEqualInteger("banco_conveniado", lgBancoConveniado ? TipoBancoConvenioEnum.COM_CONVENIO.getKey() : TipoBancoConvenioEnum.SEM_CONVENIO.getKey(), lgBancoConveniado );
		return this;
	}
	
	public BancoSearch setLimite(int nrLimite) {
		this.criterios.setQtLimite(nrLimite);		
		return this;
	}
	
	public BancoSearch setDeslocamento(int nrLimite, int nrPagina) {
		this.criterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));	
		return this;
	}

	
	public SearchCriterios build() {
		return this.criterios;
	}
}
