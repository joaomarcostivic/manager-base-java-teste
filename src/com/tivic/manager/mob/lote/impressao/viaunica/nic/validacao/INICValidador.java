package com.tivic.manager.mob.lote.impressao.viaunica.nic.validacao;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface INICValidador {
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException;
}
