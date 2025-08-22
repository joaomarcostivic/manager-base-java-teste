package com.tivic.manager.ptc.protocolosv3.formularios;

import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;

public class GeradorFormularioDefesaProtocolo extends GeradorFormularioProtocolo{
	
	public GeradorFormularioDefesaProtocolo() throws Exception {
		super();
	}

	@Override
	public byte[] gerar() throws Exception {
		Report report = new ReportBuilder()
				.reportCriterios(montarReportCriterios())
				.build();
		return report.getReportPdf("mob/formulario_defesa_previa");
	}
}
