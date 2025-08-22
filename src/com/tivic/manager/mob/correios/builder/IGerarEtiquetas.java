package com.tivic.manager.mob.correios.builder;

import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.sol.connection.CustomConnection;

public interface IGerarEtiquetas {
	void gerarEtiquetas(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception;
	CorreiosEtiqueta build();
}
