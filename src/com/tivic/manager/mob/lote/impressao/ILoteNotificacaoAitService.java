package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.sol.connection.CustomConnection;

public interface ILoteNotificacaoAitService {
	LoteImpressaoAit save(LoteImpressaoAit loteImpressaoAit) throws Exception;
	LoteImpressaoAit save(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception;
}
