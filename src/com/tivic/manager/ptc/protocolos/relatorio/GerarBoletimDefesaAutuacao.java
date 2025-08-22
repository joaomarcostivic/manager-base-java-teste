package com.tivic.manager.ptc.protocolos.relatorio;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.mob.AitMovimentoDocumentoServices;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;

public class GerarBoletimDefesaAutuacao implements IGerarBoletimDefesaAutuacao {
	
	public GerarBoletimDefesaAutuacao() throws Exception {
	}
	
	@Override
	public byte[] gerarBoletimDefesa() throws Exception {
		return findBoletimDefesa().getReportPdf("mob/boletim_informativo_defesa");
	}
	
	private Report findBoletimDefesa() throws Exception {
		Report report = new ReportBuilder()
				.reportCriterios(montarReportCriterios())
				.build();
		return report;
	}
	
	private ReportCriterios montarReportCriterios() throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE"));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP"));
		reportCriterios.addParametros("SG_UF", new AitMovimentoDocumentoServices().getEstado().toUpperCase());
		reportCriterios.addParametros("NM_DIRETOR_JARI", ParametroServices.getValorOfParametro("NM_DIRETOR_JARI"));
		reportCriterios.addParametros("NM_SECRETARIO_GERAL", getSecretarioGeral().toUpperCase());
		return reportCriterios;
	}
	
	String getSecretarioGeral() {
		String nmSecretario = "";
		int cdSecretarioGeral = ParametroServices.getValorOfParametroAsInteger("CD_SECRETARIO_GERAL", 0);
		if(cdSecretarioGeral > 0) {
			Pessoa secretario = PessoaDAO.get(cdSecretarioGeral);
			return secretario.getNmPessoa();
		}
		return nmSecretario;
	}

}
