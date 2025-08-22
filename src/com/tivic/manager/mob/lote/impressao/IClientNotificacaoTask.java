package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IClientNotificacaoTask {
	void taskGerarMovimentoNAI() throws ValidacaoException, Exception;
	void taskGerarMovimentoFimPrazoDefesa() throws ValidacaoException, Exception;
	void taskGerarMovimentoNIP() throws ValidacaoException, Exception;
	void taskGerarLoteImpressaoNAI() throws ValidacaoException, Exception;
	void taskGerarLoteImpressaoNIP() throws ValidacaoException, Exception;
	void taskArquivoRetornoCorreios() throws ValidacaoException, Exception;
}
