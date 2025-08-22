package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.sol.connection.CustomConnection;

public class DirectorRemessa {
	
	public void constructorRemessa(IBuilderRemessa builderRemessa, LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		builderRemessa.montarDadosRemessa(loteImpressao, customConnection);
	}
	
}
