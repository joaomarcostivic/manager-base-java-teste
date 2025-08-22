package com.tivic.manager.mob.correios;

import com.tivic.manager.mob.CorreiosLote;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IUpdateLoteEtiqueta {
	CorreiosLote updateLoteEtiqueta(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception;
	SearchCriterios gerarCriteriosDeleteEtiquetasLivres(CorreiosLote correiosLote);
}
