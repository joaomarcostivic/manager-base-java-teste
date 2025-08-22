package com.tivic.manager.mob.lotes.builders.dividaativa;

import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.sol.search.SearchCriterios;

public class DividaAtivaSearchBuilder {
	
	SearchCriterios searchCriterios;
	
	public DividaAtivaSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	public DividaAtivaSearchBuilder setIdAit(String idAit) {
		searchCriterios.addCriteriosEqualString("A.id_ait", idAit, idAit != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setDtEmissaoInicial(String dtEmissaoInicialNp) {
		searchCriterios.addCriteriosGreaterDate("B.dt_movimento", dtEmissaoInicialNp, dtEmissaoInicialNp != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setDtEmissaoFinal(String dtEmissaoFinalNp) {
		searchCriterios.addCriteriosMinorDate("B.dt_movimento", dtEmissaoFinalNp, dtEmissaoFinalNp != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setDtVencimentoInicial(String dtVencimentoInicialNp) {
		searchCriterios.addCriteriosGreaterDate("A.dt_vencimento", dtVencimentoInicialNp, dtVencimentoInicialNp != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setDtVencimentoFinal(String dtVencimentoFinalNp) {
		searchCriterios.addCriteriosMinorDate("A.dt_vencimento", dtVencimentoFinalNp, dtVencimentoFinalNp != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setNrPlaca(String nrPlaca) {
		searchCriterios.addCriteriosEqualString("A.nr_Placa", nrPlaca, nrPlaca != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setvlMultaInicial(String vlMultaInicial) {
		searchCriterios.addCriteriosEqualString("vl_multa_inicial", vlMultaInicial, vlMultaInicial != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setvlMultaFinal(String vlMultaFinal) {
		searchCriterios.addCriteriosEqualString("vl_multa_final", vlMultaFinal, vlMultaFinal != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		searchCriterios.addCriteriosEqualString("A.nr_cpf_cnpj_proprietario", nrCpfCnpjProprietario, nrCpfCnpjProprietario != null);
		return this;
	}
	
	public DividaAtivaSearchBuilder setTpStatus() {
		searchCriterios.addCriteriosEqualInteger("B.tp_status", TipoStatusEnum.NIP_ENVIADA.getKey());
		return this;
	}
	
	public SearchCriterios build() {
		return searchCriterios;
	}
}
