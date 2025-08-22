package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.codehaus.groovy.syntax.Types;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.mob.AitMovimentoDocumentoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sol.dao.ItemComparator;
import sol.util.ConfManager;

public class GenerateReportsAta {
	private IAtaRelatorService ataRelatorService;
	private HashMap<String, String> tipoRelator;
	
	public GenerateReportsAta() throws Exception {
		ataRelatorService = (IAtaRelatorService) BeansFactory.get(IAtaRelatorService.class);
		setTipoRelatorMapper();
	}
	
	public Report printAta(Integer cdAta, CustomConnection customConnection ) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ata", cdAta, cdAta != null);
		Search<AtaDTO> search = gerarSearchRelatorio(searchCriterios);
		if(search.getList(AtaDTO.class).isEmpty())
			throw new Exception("Nenhuma Ata encontrada");
		Report report = new ReportBuilder()
				.search(search)
				.reportCriterios(montarReportCriterios())
				.build();

		setParamsReport(report.getParams());
		getRelatoresPorVinculo(cdAta, report, customConnection);
		String sessao = search.getList(AtaDTO.class).get(0).getIdAta().split("-")[0];
		if(search.getRsm().next()) {
			int registros = 0;	
			registros += getRegistrosAta(searchCriterios, "DEFERIDO", report);
			registros += getRegistrosAta(searchCriterios, "INDEFERIDO", report);
			
			report.getParams().put("SESSAO", sessao);
			report.getParams().put("REGISTROS", registros);
			report.getParams().put("DT_PROTOCOLO", new Timestamp(search.getList(AtaDTO.class).get(0).getDtAta().getTimeInMillis()));
		}
		return report;
	}
	
	public Search<AtaDTO> gerarSearchRelatorio(SearchCriterios searchCriterios) throws Exception {
		List<String> resultadoJari = new ArrayList<String>();
		resultadoJari.add(String.valueOf(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()));
		resultadoJari.add(String.valueOf(TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey()));
		searchCriterios.addCriterios("H.tp_status", resultadoJari.toString().replace("[", "").replace("]", ""), ItemComparator.IN, Types.STRING);
		Search<AtaDTO> search = new SearchBuilder<AtaDTO>("PTC_ATA A")
				.fields("D.DT_MOVIMENTO, H.dt_movimento AS dt_julgamento, A.ID_ATA, A.DT_ATA, E.ID_AIT, E.NR_PLACA, F.CD_DOCUMENTO, G.NM_SITUACAO_DOCUMENTO")
				.addJoinTable("JOIN PTC_RECURSO B ON (A.cd_ata = B.cd_ata)")
				.addJoinTable("JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (B.CD_DOCUMENTO = C.CD_DOCUMENTO) ")
				.addJoinTable("JOIN MOB_AIT_MOVIMENTO D ON (C.CD_MOVIMENTO = D.CD_MOVIMENTO AND C.CD_AIT = D.CD_AIT) ")
				.addJoinTable("JOIN MOB_AIT_MOVIMENTO H ON (C.CD_AIT = H.CD_AIT) ")
				.addJoinTable("JOIN MOB_AIT E ON (D.CD_AIT = E.CD_AIT) ")
				.addJoinTable("JOIN PTC_DOCUMENTO F ON(F.CD_DOCUMENTO = B.CD_DOCUMENTO) ")
				.addJoinTable("LEFT OUTER JOIN PTC_SITUACAO_DOCUMENTO G ON(G.CD_SITUACAO_DOCUMENTO = F.CD_SITUACAO_DOCUMENTO)")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	ReportCriterios montarReportCriterios() throws ValidacaoException, Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE"));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP"));
		reportCriterios.addParametros("SG_UF", new AitMovimentoDocumentoServices().getEstado().toUpperCase());
		return reportCriterios;
	}
	
	private void getRelatoresPorVinculo(Integer cdAta, Report report, CustomConnection customConnection) throws Exception {
		String nmRelatores = "";
		String presidenteJari = "";
		String secretarioJari = "";
		List<AtaRelatorDTO> lista = ataRelatorService.getAtaRelator(cdAta, customConnection);
		List<HashMap<String, Object>> membrosRelatores = new ArrayList<>();
		for (AtaRelatorDTO relator : lista) {
			String key = tipoRelator.get(relator.getNmVinculo());
			if ("PRESIDENTE_JARI".equals(key)) {
				presidenteJari = relator.getNmPessoa();
			} else {
				HashMap<String, Object> relatorMap = new HashMap<>();
				relatorMap.put("RELATOR", relator.getNmPessoa());
				membrosRelatores.add(relatorMap);
				nmRelatores += relator.getNmPessoa() + "; ";
				if ("SECRETARIO_JARI".equals(key)) {
					secretarioJari = relator.getNmPessoa();
				}
			}
		}
		report.getParams().put("PRESIDENTE_JARI", presidenteJari);
		report.getParams().put("SECRETARIO_JARI", secretarioJari);
		report.getParams().put("LISTA_RELATORES", nmRelatores);
		report.getParams().put("DATASET_LISTA_RELATORES", new JRBeanCollectionDataSource(membrosRelatores));

	}
	
	private void setTipoRelatorMapper() {
		tipoRelator = new HashMap<String, String>();
		tipoRelator.put("SECRETÁRIO JARI", "SECRETARIO_JARI");
		tipoRelator.put("PRESIDENTE JARI", "PRESIDENTE_JARI");
		tipoRelator.put("RELATOR", "RELATOR");
	}
	
	int getRegistrosAta(SearchCriterios searchCriterios, String nmSituacao, Report report) {
		String registros = null;
		int count = 0;
		try {
			int cdDeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0, 0, null);
			int cdIndeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_INDEFERIDO", 0, 0, null);
			
			searchCriterios.getAndRemoveCriterio("F.CD_SITUACAO_DOCUMENTO");
			searchCriterios.addCriteriosEqualInteger("F.CD_SITUACAO_DOCUMENTO", (nmSituacao == "DEFERIDO")? cdDeferido: cdIndeferido);
			Search<AtaDTO> search = gerarSearchRelatorio(searchCriterios);
			if(!search.getList(AtaDTO.class).isEmpty()) {
				registros = "";
				while(search.getRsm().next()) {
					registros += search.getList(AtaDTO.class).get(count).getIdAit() + "; ";
					count++;
				}
			}
			report.getParams().put((nmSituacao), registros);
			return count;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	void setParamsReport(HashMap<String, Object> params) throws ValidacaoException {
		ConfManager conf = ManagerConf.getInstance();
		String reportPath = conf.getProps().getProperty("REPORT_PATH");
		String path = ContextManager.getRealPath()+"/"+reportPath;
		ArrayList<String> reportNames = new ArrayList<>();
		reportNames.add("mob//relatores_resumo_ata_subreport");
		
		params.put("SUBREPORT_DIR", path+"//mob");
		params.put("REPORT_LOCALE", new Locale("pt", "BR"));		
		params.put("SUBREPORT_NAMES", reportNames);
	}
	
	String getNmRelator(Integer cdRelator) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_pessoa", cdRelator);
		Search<Pessoa> search = new SearchBuilder<Pessoa>("GRL_PESSOA")
				.fields("NM_PESSOA")
				.searchCriterios(searchCriterios)
				.build();
		String nmRelator = search.getList(Pessoa.class).get(0).getNmPessoa();
		return nmRelator;
	}
}
