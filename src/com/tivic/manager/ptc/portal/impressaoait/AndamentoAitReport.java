package com.tivic.manager.ptc.portal.impressaoait;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.TipoConveniosEnum;
import com.tivic.manager.ptc.portal.dtos.AndamentoAitDTO;
import com.tivic.manager.ptc.portal.response.AndamentoAitResponse;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AndamentoAitReport implements IAndamentoAitReport{
	
	@Override
	public AndamentoAitResponse imprimir(int cdAit, CustomConnection customConnection) throws Exception {
	    List<AndamentoAitDTO> impressoesAit = buscarAit(cdAit, customConnection).getList(AndamentoAitDTO.class);
	    if (impressoesAit.isEmpty()) {
	        throw new Exception("Não foi possível encontrar o Ait com o ID fornecido.");
	    }
	    AndamentoAitDTO impressaoAitDTO = impressoesAit.get(0);
	    Report report = new ReportBuilder()
	        .reportCriterios(setParametros(impressaoAitDTO, customConnection))
	        .list(impressoesAit)
	        .build();
	    byte[] pdfData = report.getReportPdf("mob/auto_segunda_via_portal");
	    AndamentoAitResponse response = new AndamentoAitResponse(pdfData);
	    return response;
	}

	private ReportCriterios setParametros(AndamentoAitDTO impressaoAitDTO, CustomConnection customConnection) throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("NM_TITULO_IMPRESSAO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("NM_TITULO_IMPRESSAO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("NM_TITULO_IMPRESSAO_3"));
		reportCriterios.addParametros("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", customConnection.getConnection()));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", customConnection.getConnection()));			
		reportCriterios.addParametros("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR"));
		reportCriterios.addParametros("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO"));
		reportCriterios.addParametros("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO"));
		reportCriterios.addParametros("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO"));
		reportCriterios.addParametros("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP"));
		reportCriterios.addParametros("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO"));
		reportCriterios.addParametros("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO"));		
		reportCriterios.addParametros("DS_ENDERECO", ParametroServices.getValorOfParametro("DS_ENDERECO"));
		reportCriterios.addParametros("NM_SUBORGAO", ParametroServices.getValorOfParametro("NM_SUBORGAO", "LOREM IPSUM DOLOR SIT AMET", customConnection.getConnection()));
		reportCriterios.addParametros("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO"));
		reportCriterios.addParametros("NM_MUNICIPIO", ParametroServices.getValorOfParametro("NM_MUNICIPIO"));
		reportCriterios.addParametros("MOB_TP_CONVENIO", getConvenio(impressaoAitDTO.getTpConvenio()));
		reportCriterios.addParametros("BLB_IMAGEM", imagem(impressaoAitDTO.getCdAit(), customConnection));
		reportCriterios.addParametros("DATA_IMPRESSAO", getDataAtual());
		reportCriterios.addParametros("NM_ORGAO", ParametroServices.getValorOfParametro("NM_ORGAO"));
		return reportCriterios;
	}
	
	private Search<AndamentoAitDTO> buscarAit(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		Search<AndamentoAitDTO> search = new SearchBuilder<AndamentoAitDTO>("MOB_AIT A")
				.fields("A.*, B.nr_cod_detran, B.ds_infracao, C.nr_matricula, D.nm_modelo, E.nm_cidade AS nm_municipio, L.nr_matricula, G.ds_especie, M.id_equipamento")
				.addJoinTable("LEFT OUTER JOIN mob_infracao B ON A.cd_infracao = B.cd_infracao")
				.addJoinTable("LEFT OUTER JOIN mob_agente C ON A.cd_agente = C.cd_agente")
				.addJoinTable("LEFT OUTER JOIN fta_marca_modelo D ON A.cd_marca_veiculo = D.cd_marca")
				.addJoinTable("LEFT OUTER JOIN grl_cidade E ON A.cd_cidade = E.cd_cidade")
				.addJoinTable("LEFT OUTER JOIN fta_tipo_veiculo F ON A.cd_tipo_veiculo = F.cd_tipo_veiculo")
				.addJoinTable("LEFT OUTER JOIN fta_especie_veiculo G ON A.cd_especie_veiculo = G.cd_especie")
				.addJoinTable("LEFT OUTER JOIN fta_cor H ON A.cd_cor_veiculo = H.cd_cor")
				.addJoinTable("LEFT OUTER JOIN MOB_AIT_MOVIMENTO I ON A.cd_ait = I.cd_ait")
				.addJoinTable(
						"INNER JOIN (SELECT cd_ait, MAX(dt_movimento) AS ultima_situacao FROM mob_ait_movimento GROUP BY cd_ait) AS ultima_situacao ON A.cd_ait = ultima_situacao.cd_ait AND I.dt_movimento = ultima_situacao.ultima_situacao")
				.addJoinTable("LEFT OUTER JOIN fta_categoria_veiculo J ON A.cd_categoria_veiculo = J.cd_categoria")
				.addJoinTable("LEFT OUTER JOIN mob_agente L ON (A.cd_agente = L.cd_agente)")
				.addJoinTable("LEFT OUTER JOIN grl_equipamento M ON (A.cd_equipamento = M.cd_equipamento)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private String getConvenio(int tpConvenio) throws Exception {
		String convenio = "";
		convenio = TipoConveniosEnum.valueOf(tpConvenio);
		return convenio;
	}
	
	private String imagem(int cdAit, CustomConnection customConnection)throws Exception{
		List<AitImagem> imagemAit = getImagem(cdAit, customConnection).getList(AitImagem.class); 
		 if (imagemAit.isEmpty()) {
			 return null;
		 }
		 byte[] imagem = imagemAit.get(0).getBlbImagem();
		 return Base64.getEncoder().encodeToString(imagem);
	}
	
	private Search<AitImagem> getImagem(int cdAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("lg_impressao", 1);
		Search<AitImagem> search = new SearchBuilder<AitImagem>("mob_ait_imagem")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		return search;
	}
	
	private String getDataAtual() {
		GregorianCalendar dataHoraAtual = Util.getDataAtual();
		SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		formatoData.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
	    return formatoData.format(dataHoraAtual.getTime());
	}
}