package com.tivic.manager.ptc.protocolosv3.formularios;

import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;

public class GeradorFormularioCondutorProtocolo extends GeradorFormularioProtocolo {

	public GeradorFormularioCondutorProtocolo() throws Exception {
		super();
	}

	@Override
	public byte[] gerar() throws Exception {
		Report report = new ReportBuilder()
				.reportCriterios(montarReportCriterios())
				.build();
		return report.getReportPdf("mob/formulario_apresentacao_condutor");
	}

}
