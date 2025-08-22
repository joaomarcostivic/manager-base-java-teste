package com.tivic.manager.mob.lotes.service.dividaativa;

import com.tivic.sol.connection.CustomConnection;

public interface IGeraArquivoDividaAtiva {

	byte[] gerarArquivoEnvio(int cdLote, CustomConnection customConnection) throws Exception;
}
