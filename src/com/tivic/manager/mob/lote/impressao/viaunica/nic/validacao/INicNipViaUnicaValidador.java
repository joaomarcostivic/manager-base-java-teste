package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface INicNipViaUnicaValidador {
	public boolean validate(TipoImpressaoNotificacao tipoImpressaoNotificacao, CustomConnection customConnection) throws Exception, ValidacaoException;
}
