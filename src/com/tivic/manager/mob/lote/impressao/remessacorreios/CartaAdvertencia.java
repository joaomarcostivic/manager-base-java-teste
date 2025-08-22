package com.tivic.manager.mob.lote.impressao.remessacorreios;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.DirectorRemessa;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.IBuilderRemessa;
import com.tivic.manager.mob.lote.impressao.remessacorreios.builders.RemessaSimplesAdvertenciaBuilder;
import com.tivic.sol.connection.CustomConnection;

public class CartaAdvertencia implements IGerarDadosDocumento {

	@Override
	public DadosDocumento gerarDadosDocumento(int tpNotificacao, LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		DirectorRemessa directorRemessa = new DirectorRemessa();
		IBuilderRemessa remessaSimplesBuilder = new RemessaSimplesAdvertenciaBuilder();
		directorRemessa.constructorRemessa(remessaSimplesBuilder, loteImpressao, customConnection);
		return remessaSimplesBuilder.build();
	}

}
