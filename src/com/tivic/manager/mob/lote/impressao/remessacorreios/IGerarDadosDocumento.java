package com.tivic.manager.mob.lote.impressao.remessacorreios;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.sol.connection.CustomConnection;

public interface IGerarDadosDocumento {
	DadosDocumento gerarDadosDocumento(int tpNotificacao, LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
}
