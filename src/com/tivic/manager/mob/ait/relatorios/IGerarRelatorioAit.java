package com.tivic.manager.mob.ait.relatorios;
import com.tivic.sol.search.SearchCriterios;

public interface IGerarRelatorioAit {
	public byte[] gerar(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento) throws Exception;
}
