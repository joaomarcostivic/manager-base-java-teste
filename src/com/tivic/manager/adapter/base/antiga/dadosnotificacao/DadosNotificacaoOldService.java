package com.tivic.manager.adapter.base.antiga.dadosnotificacao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.dadosnotificacao.IDadosNotificacaoService;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class DadosNotificacaoOldService implements IDadosNotificacaoService {

	@Override
	public List<DadosNotificacao> buscarDadosNAI(int cdAit, CustomConnection customConnection) throws Exception {
		List<DadosNotificacao> dadosNotificacao = new DadosNotificacaoOldNAI()
				.search(montarCriterios(cdAit), customConnection)
				.getList(DadosNotificacao.class);
		tratarRenainf(cdAit, dadosNotificacao, customConnection);
		tratarRenavan(cdAit, dadosNotificacao, customConnection);
		return dadosNotificacao;
	}

	@Override
	public List<DadosNotificacao> buscarDadosNIP(int cdAit, CustomConnection customConnection) throws Exception {
		List<DadosNotificacao> dadosNotificacao = new DadosNotificacaoOldNIP()
				.search(montarCriterios(cdAit), customConnection)
				.getList(DadosNotificacao.class);;
		tratarRenainf(cdAit, dadosNotificacao, customConnection);
		tratarRenavan(cdAit, dadosNotificacao, customConnection);
		return dadosNotificacao;
	}
	
	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.codigo_ait", cdAit, true);
		return searchCriterios;
	}
	
	private void tratarRenainf(int cdAit, List<DadosNotificacao> dadosNotificacao, CustomConnection customConnection) throws Exception {
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement("SELECT nr_renainf from AIT WHERE codigo_ait = ?");
		pstmt.setInt(1, cdAit);
		ResultSet rsNrRenainf = pstmt.executeQuery();
		if (rsNrRenainf.next()) {
			String nrRenainf = rsNrRenainf.getBigDecimal("nr_renainf") != null ? rsNrRenainf.getBigDecimal("nr_renainf").toString() : "";
			dadosNotificacao.get(0).setNrRenainf(nrRenainf);
		}
	}
	
	private void tratarRenavan(int cdAit, List<DadosNotificacao> dadosNotificacao, CustomConnection customConnection) throws Exception {
		PreparedStatement pstmt = customConnection.getConnection().prepareStatement("SELECT cd_renavan from AIT WHERE codigo_ait = ?");
		pstmt.setInt(1, cdAit);
		ResultSet rsNrRenavan = pstmt.executeQuery();
		if (rsNrRenavan.next()) {
			String nrRenavan = rsNrRenavan.getBigDecimal("cd_renavan") != null ? rsNrRenavan.getBigDecimal("cd_renavan").toString() : "";
			dadosNotificacao.get(0).setNrRenavan(nrRenavan);
		}
	}

}
