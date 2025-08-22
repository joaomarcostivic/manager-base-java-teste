package com.tivic.manager.mob.ait.relatorios.quantitativo;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IRelatorioQuantitativoService {

	public PagedResponse<RelatorioQuantitativoDTO> buscarRelatorioQuantitativo(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws ValidacaoException, Exception;
	public byte[] gerar(RelatorioQuantitativoSearch relatorioQuantitativoSearch) throws Exception;

}
