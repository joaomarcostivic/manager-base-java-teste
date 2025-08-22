package com.tivic.manager.mob.rrd;

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

public class RrdReport {

	public byte[] gerar(int cdRrd) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			RrdDTO impressaoRrd = buscarRrdComDocumentacao(cdRrd, customConnection);
			if (impressaoRrd == null) {
				throw new Exception("Não foi possível encontrar o RRD com o ID fornecido.");
			}
			byte[] rrd = imprimirRrd(impressaoRrd, customConnection);
			customConnection.finishConnection();
			return rrd;
		} finally {
			customConnection.closeConnection();
		}
	}

	public byte[] imprimirRrd(RrdDTO rrdDTO, CustomConnection customConnection) throws Exception {
		List<RrdDTO> listRrd = Collections.singletonList(rrdDTO);
		Report report = new ReportBuilder().list(listRrd).reportCriterios(setParametros(customConnection)).build();
		return report.getReportPdf("mob/rrd");
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
		String[] parametros = { "DS_TITULO_1", "DS_TITULO_3", "NM_ORGAO_AUTUADOR", "NR_TELEFONE", "NM_LOGRADOURO",
				"NM_BAIRRO", "NR_ENDERECO" };
		for (String parametro : parametros) {
			reportCriterios.addParametros(parametro,
					ParametroServices.getValorOfParametro(parametro, customConnection.getConnection()));
		}
		return reportCriterios;
	}

	private RrdDTO buscarRrdComDocumentacao(int cdRrd, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_rrd", cdRrd);
		Search<RrdDTO> search = new SearchBuilder<RrdDTO>("MOB_RRD A")
				.fields("A.*," + " B.cd_ait," + " C.*," + " D.*," + " E.nm_cidade," + " F.ds_especie," + " G.nm_tipo,"
						+ " H.nm_marca," + " H.nm_modelo," + " I.nm_categoria," + " J.ds_infracao2," + " J.nr_artigo,"
						+ " J.nr_paragrafo," + " J.nr_inciso," + " J.nr_alinea, "
						+ " K.nr_documento AS nr_documento_recolhido," + " L.nm_tipo_documentacao")
				.addJoinTable("LEFT OUTER JOIN mob_rrd_ait B ON (A.cd_rrd = B.cd_rrd)")
				.addJoinTable("LEFT OUTER JOIN ait C ON (B.cd_ait = C.codigo_ait)")
				.addJoinTable("LEFT OUTER JOIN agente D ON (A.cd_agente = D.cod_agente)")
				.addJoinTable("LEFT OUTER JOIN grl_cidade E ON (A.cd_cidade = E.cd_cidade)")
				.addJoinTable("LEFT OUTER JOIN especie_veiculo F ON (C.cod_especie = F.cod_especie)")
				.addJoinTable("LEFT OUTER JOIN tipo_veiculo G ON (C.cod_tipo = G.cod_tipo)")
				.addJoinTable("LEFT OUTER JOIN marca_modelo H ON (C.cod_marca = H.cod_marca)")
				.addJoinTable("LEFT OUTER JOIN categoria_veiculo I ON (C.cod_categoria = I.cod_categoria)")
				.addJoinTable("LEFT OUTER JOIN infracao J ON (C.cod_infracao = J.cod_infracao)")
				.addJoinTable("LEFT OUTER JOIN mob_recolhimento_documentacao K ON (A.cd_rrd = k.cd_rrd)")
				.addJoinTable(
						"LEFT OUTER JOIN grl_tipo_documentacao L ON (K.cd_tipo_documentacao = L.cd_tipo_documentacao)")
				.searchCriterios(searchCriterios).customConnection(customConnection).build();
		return (search != null) ? search.getList(RrdDTO.class).stream().findFirst().orElse(null) : null;
	}
}
