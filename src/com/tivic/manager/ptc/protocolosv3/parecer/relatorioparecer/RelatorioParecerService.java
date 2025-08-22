package com.tivic.manager.ptc.protocolosv3.parecer.relatorioparecer;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistoricoDTO;
import com.tivic.manager.grl.pessoavinculohistorico.enums.StPessoaVinculoEnum;
import com.tivic.manager.ptc.protocolosv3.documento.ata.AtaRelatorDTO;
import com.tivic.manager.ptc.protocolosv3.enums.TipoConsistenciaEnum;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
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

public class RelatorioParecerService implements IRelatorioParecerService {
	private HashMap<String, String> tipoRelator;

	@Override
	public byte[] gerarParecer(int cdAit, int cdTipoDocumento) throws Exception {
		return printRelatorioParecer(cdAit, cdTipoDocumento).getReportPdf("ptc/relatorio_parecer");
	}
	
	private Report printRelatorioParecer(int cdAit, int cdTipoDocumento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		CustomConnection customConnection = new CustomConnection();
		try {
			String cdOcorrenciaDeferida = ParametroServices.getValorOfParametro("CD_TIPO_OCORRENCIA_DEFERIDA");
			String cdOcorrenciaIndeferida = ParametroServices.getValorOfParametro("CD_TIPO_OCORRENCIA_INDEFERIDA");
			customConnection.initConnection(false);
			searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, cdAit > 0);
			searchCriterios.addCriteriosEqualInteger("D.cd_tipo_documento", cdTipoDocumento, cdTipoDocumento > 0);
			searchCriterios.addCriterios("I.cd_tipo_ocorrencia", cdOcorrenciaDeferida + ", " + cdOcorrenciaIndeferida , ItemComparator.IN, Types.INTEGER);
			searchCriterios.setOrderBy("A.cd_ait, I.dt_ocorrencia DESC");
			searchCriterios.setQtLimite(1);
			List<ParecerDTO> parecerDTO = searchProtocolo(searchCriterios);
			ReportCriterios reportCriterios = montarReportCriterios(parecerDTO, customConnection);
			Report report = new ReportBuilder()
					.list(parecerDTO)
					.reportCriterios(reportCriterios)
					.build();
			getRelatoresJari(parecerDTO, report, customConnection);
			customConnection.closeConnection();
			return report;
		} finally {
			customConnection.finishConnection();
		}
	}
	
	private void getRelatoresJari(List<ParecerDTO> parecerDTO, Report report, CustomConnection customConnection) throws Exception {
		if (parecerDTO.get(0).getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey()) {
			int habilitaListaMembrosJari = ParametroServices.getValorOfParametroAsInteger("LG_HABILITAR_LISTA_MEMBROS_JARI", -1);
			if (habilitaListaMembrosJari == -1) {
				throw new ValidacaoException("O parâmetro LG_HABILITAR_LISTA_MEMBROS_JARI não foi configurado.");
			}
			setParamsReport(report.getParams());
			getRelatoresPorVinculo(parecerDTO.get(0).getCdAta(), report, customConnection);
		}
	}
	
	private ReportCriterios montarReportCriterios(List<ParecerDTO> parecerDTO, CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();		
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", customConnection.getConnection()));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", customConnection.getConnection()));
		reportCriterios.addParametros("DT_DOCUMENTO", formatDate());		
		reportCriterios.addParametros("NM_AUTORIDADE_TRANSITO", nmAutoridadeTransito(parecerDTO, customConnection));
		reportCriterios.addParametros("PRAZO_LEGAL", parecerDTO.get(0).getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey() ? getPrazoLegalJari(parecerDTO) : getPrazoLegalDefesaPrevia(parecerDTO));
		reportCriterios.addParametros("TIPO_CONSISTENCIA", getTpConsistencia(parecerDTO));
		reportCriterios.addParametros("NM_TITULO", getNmTitulo(parecerDTO.get(0)));
		reportCriterios.addParametros("LG_LISTA", ParametroServices.getValorOfParametroAsInteger("LG_HABILITAR_LISTA_MEMBROS_JARI", -1));
		reportCriterios.addParametros("DETERMINA_ASSINATURA", determinarTipoAssinatura(parecerDTO));
		return reportCriterios;
	}
	
	
	private PessoaVinculoHistoricoDTO getAutoridadeValida(List<ParecerDTO> parecerDTO, CustomConnection customConnection) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriterios("D.nm_vinculo", "AUTORIDADE DE TRÂNSITO", ItemComparator.EQUAL, Types.VARCHAR);
	    Search<PessoaVinculoHistoricoDTO> search = new SearchBuilder<PessoaVinculoHistoricoDTO>("grl_pessoa_empresa A")
	            .fields("A.cd_vinculo, A.cd_pessoa, A.st_vinculo, C.nm_pessoa, A.dt_vinculo, B.dt_vinculo_historico")
	            .addJoinTable("LEFT JOIN grl_pessoa_vinculo_historico B ON (A.cd_pessoa = B.cd_pessoa AND A.cd_vinculo = B.cd_vinculo)")
	            .addJoinTable("JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa)")
	            .addJoinTable("JOIN grl_vinculo D ON (A.cd_vinculo = D.cd_vinculo)")
	            .customConnection(customConnection)
	            .searchCriterios(searchCriterios)
	            .orderBy("A.dt_vinculo DESC, B.dt_vinculo_historico DESC")
	          .build();
	    List<PessoaVinculoHistoricoDTO> pessoaVinculoHitoricoList = search.getList(PessoaVinculoHistoricoDTO.class);
	    PessoaVinculoHistoricoDTO autoridade = validarAutoridade(pessoaVinculoHitoricoList, parecerDTO);
	    return autoridade;
	}
	
	private PessoaVinculoHistoricoDTO validarAutoridade(List<PessoaVinculoHistoricoDTO> pessoaVinculoHitoricoList, List<ParecerDTO> parecerDTO) {
		GregorianCalendar dataJulgamento = parecerDTO.get(0).getDtOcorrencia();
		if (dataJulgamento != null && pessoaVinculoHitoricoList != null && !pessoaVinculoHitoricoList.isEmpty()) {
			for (PessoaVinculoHistoricoDTO autoridade : pessoaVinculoHitoricoList) {
				if (dataJulgamento.after(autoridade.getDtVinculo())) {
					if (autoridade.getStVinculo() == StPessoaVinculoEnum.ST_ATIVO.getKey()) {
						return autoridade;
					} else if (autoridade.getDtVinculoHistorico() != null
							&& dataJulgamento.before(autoridade.getDtVinculoHistorico())) {
						return autoridade;
					}
				}
			}
		}
		return null;
	}

	private String nmAutoridadeTransito(List<ParecerDTO> parecerDTO, CustomConnection customConnection) throws Exception {
	    PessoaVinculoHistoricoDTO autoridade = getAutoridadeValida(parecerDTO, customConnection);
	    return (autoridade != null) ? autoridade.getNmPessoa() : "";
	}
	
	private int determinarTipoAssinatura(List<ParecerDTO> parecerDTO) {
		return (parecerDTO.get(0).getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey()) ? 1 : 0;
	}
	
	private List<ParecerDTO> searchProtocolo(SearchCriterios searchCriterios) throws Exception {
		Search<ParecerDTO> search = new SearchBuilder<ParecerDTO>("mob_ait_movimento A")
				.fields("DISTINCT ON(A.cd_ait) B.id_ait, B.nr_placa, B.dt_vencimento, B.dt_prazo_defesa, D.dt_protocolo,  D.nm_requerente, D.ds_assunto, D.cd_tipo_documento, "
						+ "G.cd_situacao_documento, G.nm_situacao_documento, I.dt_ocorrencia, I.txt_ocorrencia, I.tp_consistencia, J.cd_ata, D.nr_documento")
				.addJoinTable("JOIN mob_ait B ON (A.cd_ait = B.cd_Ait)")
				.addJoinTable("JOIN mob_ait_movimento_documento C ON (C.cd_ait = B.cd_ait AND A.cd_movimento = C.cd_movimento)")
				.addJoinTable("JOIN ptc_documento D ON (C.cd_documento = D.cd_documento)")
				.addJoinTable("JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento)")
				.addJoinTable("JOIN ptc_fase F ON (F.cd_fase = D.cd_fase)")
				.addJoinTable("JOIN ptc_situacao_documento G ON (D.cd_situacao_documento = G.cd_situacao_documento)")
				.addJoinTable("LEFT JOIN ptc_apresentacao_condutor H ON (D.cd_documento = H.cd_documento)")
				.addJoinTable("JOIN ptc_documento_ocorrencia I ON (D.cd_documento = I.cd_documento)")
				.addJoinTable("LEFT JOIN ptc_recurso J ON (D.cd_documento = J.cd_documento)")
				.searchCriterios(searchCriterios)
				.orderBy(searchCriterios.getOrderBy())
			.build();
		return search.getList(ParecerDTO.class);
	}
	
	private String formatDate() {
		GregorianCalendar date = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
		String formattedDate = sdf.format(date.getTime());
		return formattedDate;
	}
	
	private String getPrazoLegalJari(List<ParecerDTO> listParecerDTO) {
	    if (listParecerDTO == null || listParecerDTO.isEmpty()) {
	        return null;
	    }
	    ParecerDTO parecer = listParecerDTO.get(0);
	    LocalDate dtProtocolo = formatDate(parecer.getDtProtocolo());
	    LocalDate dtVencimento = formatDate(parecer.getDtVencimento());
	    if (dtProtocolo == null || dtVencimento == null) {
	        return null;
	    }
	    if (dtProtocolo.isBefore(dtVencimento) || dtProtocolo.isEqual(dtVencimento)) {
	        return "Tempestivo";
	    }
	    return "Intempestivo";
	}
	
	private String getPrazoLegalDefesaPrevia(List<ParecerDTO> listParecerDTO) {
	    if (listParecerDTO == null || listParecerDTO.isEmpty()) {
	        return null;
	    }
	    ParecerDTO parecer = listParecerDTO.get(0);
	    LocalDate dtProtocolo = formatDate(parecer.getDtProtocolo());
	    LocalDate dtPrazoDefesa = formatDate(parecer.getDtPrazoDefesa());
	    if (dtProtocolo == null || dtPrazoDefesa == null) {
	        return null;
	    }
	    if (dtProtocolo.isBefore(dtPrazoDefesa) || dtProtocolo.isEqual(dtPrazoDefesa)) {
	        return "Tempestivo";
	    }
	    return "Intempestivo";
	}
	
	private LocalDate formatDate(GregorianCalendar date) {
		if(date != null) {
			LocalDate dateFormatter = date.toZonedDateTime().toLocalDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			dateFormatter.format(formatter);
			return dateFormatter;
		}
		return null;
	}
	
	private String getTpConsistencia(List<ParecerDTO> listParecerDTO) {
		if (listParecerDTO.isEmpty()) {
			return null;
		} else if (listParecerDTO.get(0).getTpConsistencia() == TipoConsistenciaEnum.CONSISTENTE.getKey()) {
			return TipoConsistenciaEnum.CONSISTENTE.getValue();
		} else if(listParecerDTO.get(0).getTpConsistencia() == TipoConsistenciaEnum.INCONSISTENTE.getKey()) {
			return TipoConsistenciaEnum.INCONSISTENTE.getValue();
		}
		return null;
	}
	
	private String getNmTitulo(ParecerDTO parecer) {
		if (parecer.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.DEFESA_PREVIA.getKey())
			return "DEFESA DE AUTUAÇÃO - RELATÓRIO";
		else if (parecer.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.DEFESA_PREVIA_ADVERTENCIA.getKey())
			return "DEFESA COM ADVERTÊNCIA - RELATÓRIO";
		else if (parecer.getCdTipoDocumento() == TipoDocumentoProtocoloEnum.RECURSO_JARI.getKey())
			return "RECURSO À JARI - RELATÓRIO";
		else return "RELATÓRIO DE RESULTADO DE JULGAMENTO";
	}
	
	void setParamsReport(HashMap<String, Object> params) throws ValidacaoException {
		ConfManager conf = ManagerConf.getInstance();
		String reportPath = conf.getProps().getProperty("REPORT_PATH");
		String path = ContextManager.getRealPath()+"/"+reportPath;
		ArrayList<String> reportNames = new ArrayList<>();
		reportNames.add("ptc//relatores_jari_resumo_subreport");
		
		params.put("SUBREPORT_DIR", path+"//ptc");
		params.put("REPORT_LOCALE", new Locale("pt", "BR"));		
		params.put("SUBREPORT_NAMES", reportNames);
	}

	private void getRelatoresPorVinculo(Integer cdAta, Report report, CustomConnection customConnection) throws Exception {
	    setTipoRelatorMapper();
	    List<AtaRelatorDTO> lista = getAtaRelator(cdAta, customConnection);
	    String presidenteJari = "";
	    List<HashMap<String, Object>> membrosRelatores = new ArrayList<>();
	    for (AtaRelatorDTO relator : lista) {
	        String key = tipoRelator.get(relator.getNmVinculo());	        
	        if ("PRESIDENTE_JARI".equals(key)) {
	            presidenteJari = relator.getNmPessoa();
	        } else {
	            HashMap<String, Object> relatorMap = new HashMap<>();
	            relatorMap.put("RELATOR", relator.getNmPessoa());
	            membrosRelatores.add(relatorMap);
	        }
	    }
	    report.getParams().put("PRESIDENTE_JARI", presidenteJari);
	    report.getParams().put("DATASET_LISTA_RELATORES", new JRBeanCollectionDataSource(membrosRelatores));

	}

	
	private List<AtaRelatorDTO> getAtaRelator(int cdAta, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ata", cdAta);
		Search<AtaRelatorDTO> search = new SearchBuilder<AtaRelatorDTO>("ptc_ata_relator A ")
				.fields("D.nm_vinculo, B.nm_pessoa")
				.addJoinTable("JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)")
				.addJoinTable("JOIN grl_pessoa_empresa C ON (A.cd_pessoa = C.cd_pessoa)")
				.addJoinTable("JOIN grl_vinculo D ON (C.cd_vinculo = D.cd_vinculo)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search.getList(AtaRelatorDTO.class);
	}
	
	private void setTipoRelatorMapper() {
		tipoRelator = new HashMap<String, String>();
		tipoRelator.put("SECRETÁRIO JARI", "SECRETARIO_JARI");
		tipoRelator.put("PRESIDENTE JARI", "PRESIDENTE_JARI");
		tipoRelator.put("RELATOR", "RELATOR");
	}	
}

