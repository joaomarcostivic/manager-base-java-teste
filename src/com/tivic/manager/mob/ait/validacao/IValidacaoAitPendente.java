package com.tivic.manager.mob.ait.validacao;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IValidacaoAitPendente {
	public PagedResponse<AitPendenteDTO> buscarAitsPendentes(ValidacaoAitPendenteSearch validacaoAitPendenteSearch) throws ValidacaoException, Exception;
	public void validarAit(AitPendenteDTO aitPendenteDTO) throws Exception;
	public void invalidarAit(AitPendenteDTO aitPendenteDTO) throws Exception;
}
