package com.tivic.manager.mob.lote.impressao;

import com.tivic.sol.connection.CustomConnection;

public interface ILoteNotificacaoNaiViaUnica {
	byte[] gerar(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception;
}
