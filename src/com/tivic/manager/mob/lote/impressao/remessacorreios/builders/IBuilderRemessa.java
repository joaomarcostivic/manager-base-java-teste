package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.remessacorreios.DadosDocumento;
import com.tivic.sol.connection.CustomConnection;

public interface IBuilderRemessa {
	void montarDadosRemessa(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	DadosDocumento build();
}
