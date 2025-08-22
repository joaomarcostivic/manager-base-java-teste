package com.tivic.manager.mob.ait.relatorios;
import com.tivic.sol.search.SearchCriterios;

public interface IGeraRelatorioAitSne {
	public byte[] gerarSne(SearchCriterios searchCriterios) throws Exception;
}
