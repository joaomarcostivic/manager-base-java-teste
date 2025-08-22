package com.tivic.manager.mob.lotes.impressao.codigobarras;

import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.sol.report.ReportCriterios;

public interface IGerarCodigoBarras {
	CodigoBarras gerarCodigoBarras(Notificacao dadosNotificacao, ReportCriterios reportCriterios);
}
