package com.tivic.manager.ptc.protocolosv3.externo.impressao;


import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.protocolosv3.enums.TipoDocumentoProtocoloEnum;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class GeraImpressaoProtocoloExterno implements IGeraImpressaoProtocoloExterno{

	@Override
	public byte[] gerar(int cdDocumentoExterno, int cdDocumento) throws Exception {
		return printProtocoloExterno(cdDocumentoExterno, cdDocumento).getReportPdf("mob/protocolo_externo");
	}
	
	private Report printProtocoloExterno(int cdDocumentoExterno, int cdDocumento) throws Exception {
		ProtocoloExternoDTO protocoloExternoDTO = buscarProtocoloExterno(cdDocumentoExterno, cdDocumento).getList(ProtocoloExternoDTO.class).get(0);
		ReportCriterios reportCriterios = montarReportCriterios(protocoloExternoDTO);
		Report report = new ReportBuilder()
				.search(searchProtocoloExterno(cdDocumentoExterno, cdDocumento, new CustomConnection()))
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}

	private ReportCriterios montarReportCriterios(ProtocoloExternoDTO protocoloExternoDTO) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE"));
		reportCriterios.addParametros("NR_TELEFONE_2", ParametroServices.getValorOfParametro("NR_TELEFONE2"));
		reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("NM_REQUERENTE", protocoloExternoDTO.getNmRequerente());	
		reportCriterios.addParametros("NM_TITULO", TipoDocumentoProtocoloEnum.valueOf(protocoloExternoDTO.getCdTipoDocumento()));
		reportCriterios.addParametros("DS_DT_PROTOCOLO", Util.formatDate(protocoloExternoDTO.getDtProtocolo(), "dd/MM/yyyy"));
		reportCriterios.addParametros("NR_AIT", protocoloExternoDTO.getIdAit());
		reportCriterios.addParametros("NR_DOCUMENTO", protocoloExternoDTO.getNrDocumento());
		reportCriterios.addParametros("NR_PLACA", protocoloExternoDTO.getNrPlaca());
		return reportCriterios;
	}
	
	private Search<ProtocoloExternoDTO> buscarProtocoloExterno(int cdDocumentoExterno, int cdDocumento) throws Exception {
		Search<ProtocoloExternoDTO> protocoloExternoDTO = searchProtocoloExterno(cdDocumentoExterno, cdDocumento, new CustomConnection());
		return protocoloExternoDTO;
	}
	
	private Search<ProtocoloExternoDTO> searchProtocoloExterno(int cdDocumentoExterno, int cdDocumento, CustomConnection customConnection) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("B.cd_documento_externo", cdDocumentoExterno);
			searchCriterios.addCriteriosEqualInteger("B.cd_documento", cdDocumento);
			
			customConnection.initConnection(true);
			Search<ProtocoloExternoDTO> search = new SearchBuilder<ProtocoloExternoDTO>("ptc_documento A")
					.fields("A.nr_documento, A.dt_protocolo, A.nm_requerente, A.cd_tipo_documento, B.id_ait, B.nr_placa, B.nm_condutor, D.sg_orgao_externo, B.*")
					.addJoinTable("LEFT OUTER JOIN mob_documento_externo B ON (A.cd_documento = B.cd_documento)")
					.addJoinTable("LEFT OUTER JOIN ptc_apresentacao_condutor C ON (A.cd_documento = C.cd_documento)")
					.addJoinTable("LEFT OUTER JOIN mob_orgao_externo D ON (D.cd_orgao_externo = B.cd_orgao_externo)")
					.searchCriterios(searchCriterios)
					.build();
			customConnection.finishConnection();
			
			List<ProtocoloExternoDTO> list = search.getList(ProtocoloExternoDTO.class);
			
			if(list.isEmpty()) {
				throw new Exception("Não há protocolo para geração do comprovante.");
			}
			return search;
		} finally {
			customConnection.closeConnection();
		}
	}	
}