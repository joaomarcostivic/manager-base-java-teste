package com.tivic.manager.mob.trrav;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class TrravReport {

	public byte[] gerar(int cdTrrav) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			TrravDTO impressaoTrrav = buscarTrravComDocumentacao(cdTrrav, customConnection);
			if (impressaoTrrav == null) {
				throw new Exception("Não foi possível encontrar o Trrav com o ID fornecido.");
			}
			byte[] trrav = imprimirTrrav(impressaoTrrav, customConnection);
			customConnection.finishConnection();
			return trrav;
		} finally {
			customConnection.closeConnection();
		}
	}

	public byte[] imprimirTrrav(TrravDTO trravDTO, CustomConnection customConnection) throws Exception {
		List<TrravDTO> listTrrav = Collections.singletonList(trravDTO);
		Report report = new ReportBuilder().list(listTrrav).reportCriterios(setParametros(customConnection)).build();
		return report.getReportPdf("mob/trrav");
	}

	public ReportCriterios setParametros(CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios = setParametrosImagem(customConnection.getConnection(), reportCriterios);
		reportCriterios = setParametrosTexto(customConnection, reportCriterios);
		return reportCriterios;
	}

	private ReportCriterios setParametrosImagem(Connection connection, ReportCriterios reportCriterios)
			throws Exception {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		pstmt = connection.prepareStatement("select * from PARAMETRO", ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			reportCriterios.addParametros("LOGO_1", rs.getBytes("IMG_LOGO_ORGAO"));
			reportCriterios.addParametros("LOGO_2", rs.getBytes("IMG_LOGO_DEPARTAMENTO"));
		}
		return reportCriterios;
	}


	private ReportCriterios setParametrosTexto(CustomConnection customConnection, ReportCriterios reportCriterios)
			throws Exception {
		String[] parametros = { "DS_TITULO_1", "DS_TITULO_3" };
		for (String parametro : parametros) {
			reportCriterios.addParametros(parametro,
					ParametroServices.getValorOfParametro(parametro, customConnection.getConnection()));
		}

		return reportCriterios;
	}

	private TrravDTO buscarTrravComDocumentacao(int cdTrrav, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_trrav", cdTrrav);
		Search<TrravDTO> search = new SearchBuilder<TrravDTO>("MOB_TRRAV A")
				.fields("A.*," + " B.nm_tipo_remocao," + " C.nm_agente," + " D.nm_meio_remocao,"
						+ " E.nm_motivo_remocao," + " F.nm_cidade," + " G.nm_categoria," + " H.nm_local_remocao,"
						+ " F.nm_cidade AS nm_cidade_remocao," + " F.nm_cidade AS nm_cidade_proprietario,"
						+ " I.sg_estado AS sg_estado_remocao," + " I.sg_estado AS sg_estado_proprietario,"
						+ " J.nr_documento AS nr_documento_recolhido," + "	K.nm_tipo_documentacao")
				.addJoinTable("LEFT OUTER JOIN mob_tipo_remocao B ON (A.cd_tipo_remocao = B.cd_tipo_remocao)")
				.addJoinTable("LEFT OUTER JOIN agente C ON (A.cd_agente = C.cod_agente)")
				.addJoinTable("LEFT OUTER JOIN mob_meio_remocao D ON (A.cd_meio_remocao = D.cd_meio_remocao)")
				.addJoinTable("LEFT OUTER JOIN mob_motivo_remocao E ON (A.cd_motivo_remocao = E.cd_motivo_remocao)")
				.addJoinTable("LEFT OUTER JOIN grl_cidade F ON (A.cd_cidade_proprietario = F.cd_cidade)")
				.addJoinTable(
						"LEFT OUTER JOIN mob_categoria_cnh G ON (A.cd_categoria_cnh_proprietario = G.cd_categoria_cnh)")
				.addJoinTable("LEFT OUTER JOIN mob_local_remocao H ON (A.cd_local_remocao = H.cd_local_remocao)")
				.addJoinTable("LEFT OUTER JOIN grl_estado I ON (F.cd_estado = I.cd_estado)")
				.addJoinTable("LEFT OUTER JOIN mob_recolhimento_documentacao J ON (A.cd_trrav = J.cd_trrav)")
				.addJoinTable(
						"LEFT OUTER JOIN grl_tipo_documentacao K ON (J.cd_tipo_documentacao = K.cd_tipo_documentacao)")
				.searchCriterios(searchCriterios).customConnection(customConnection).build();
		return (search != null) ? search.getList(TrravDTO.class).stream().findFirst().orElse(null) : null;
	}
}
