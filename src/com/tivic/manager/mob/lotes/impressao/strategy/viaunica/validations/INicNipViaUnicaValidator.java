package com.tivic.manager.mob.lotes.impressao.strategy.viaunica.validations;

import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface INicNipViaUnicaValidator {
	public boolean validate(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception, ValidacaoException;
}
