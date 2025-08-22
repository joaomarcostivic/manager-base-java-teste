package com.tivic.manager.mob.lote.impressao.codigobarras;

import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.sol.report.ReportCriterios;

public interface IGerarCodigoBarras {
	CodigoBarras gerarCodigoBarras(DadosNotificacao dadosNotificacao, ReportCriterios reportCriterios);
}
