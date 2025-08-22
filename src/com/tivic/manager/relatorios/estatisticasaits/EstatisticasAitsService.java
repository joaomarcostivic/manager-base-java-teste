package com.tivic.manager.relatorios.estatisticasaits;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.fta.tipoveiculo.ITipoVeiculoService;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.manager.relatorios.estatisticasaits.builders.PagedResponseRelatorio;
import com.tivic.manager.relatorios.estatisticasaits.enums.SituacaoAgenteEnum;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class EstatisticasAitsService implements IEstatisticasAitsService {
	
	private ITipoVeiculoService tipoVeiculoService;
	
	public EstatisticasAitsService() throws Exception {
		this.tipoVeiculoService = (ITipoVeiculoService) BeansFactory.get(ITipoVeiculoService.class);
	}
	
	@Override
	public PagedResponse<RelatorioEstatisticasAitsDTO> findInfracoes(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<RelatorioEstatisticasAitsDTO> search = selectRelatorio(searchCriterios, customConnection);
			int totalInfracoes = 0;
			for(RelatorioEstatisticasAitsDTO item : search.getList(RelatorioEstatisticasAitsDTO.class)) {
				totalInfracoes += item.getQtdInfracoes();
			}
			customConnection.finishConnection();
			return new PagedResponseRelatorio<RelatorioEstatisticasAitsDTO>(search.getList(RelatorioEstatisticasAitsDTO.class), search.getRsm().getTotal(), totalInfracoes);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Search<RelatorioEstatisticasAitsDTO> selectRelatorio(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		int tpRelatorio = Integer.parseInt((searchCriterios.getAndRemoveCriterio("tp_relatorio")).getValue());
		switch (tpRelatorio) {
			case 1: return searchInfracoes(searchCriterios, customConnection);
			case 2: return searchAgenteAtivo(searchCriterios, customConnection);
			case 3: return searchEvolucaoMensal(searchCriterios, customConnection);
			case 4: return searchGravidade(searchCriterios, customConnection);
			case 5: return searchInfracaoMoto(searchCriterios, customConnection);
			case 6: return searchEmissaoNaiNip(searchCriterios, customConnection);
			case 7: return searchCompetenciaEstadual(searchCriterios, customConnection);
			case 8: return searchLogradouro(searchCriterios, customConnection);
			case 9: return searchMunicipioPlaca(searchCriterios, customConnection);
		}
		return null;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchInfracoes(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("COUNT(A.cd_ait) qtd_infracoes, B.ds_infracao, B.cd_infracao, B.nr_cod_detran, "
						+ "SUM(CASE WHEN A.cd_cidade <> D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_fora, "
						+ "SUM(CASE WHEN A.cd_cidade = D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_local")
				.addJoinTable(" JOIN mob_infracao B on (A.cd_infracao = B.cd_infracao)")
				.addJoinTable(" JOIN mob_agente C ON A.cd_agente = C.cd_agente ")
				.addJoinTable(" JOIN mob_orgao D ON C.cd_orgao = D.cd_orgao")
				.searchCriterios(searchCriterios)
				.groupBy(" B.cd_infracao ")
				.orderBy(" qtd_infracoes DESC ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchAgenteAtivo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.addCriteriosEqualInteger("B.st_agente", SituacaoAgenteEnum.ST_ATIVO.getKey());
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("COUNT(A.cd_ait) qtd_infracoes, B.nm_agente, "
						+ "SUM(CASE WHEN A.cd_cidade <> D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_fora, "
						+ "SUM(CASE WHEN A.cd_cidade = D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_local")
				.addJoinTable(" LEFT JOIN mob_agente B ON A.cd_agente = B.cd_agente")
				.addJoinTable(" LEFT JOIN mob_orgao D ON B.cd_orgao = D.cd_orgao")
				.searchCriterios(searchCriterios)
				.groupBy(" B.cd_agente ")
				.orderBy(" qtd_infracoes DESC ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchEvolucaoMensal(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("COUNT(A.cd_ait) qtd_infracoes, EXTRACT(MONTH FROM A.dt_infracao) AS mes_infracao, EXTRACT(YEAR FROM A.dt_infracao) AS ano_infracao, "
						+ "SUM(CASE WHEN A.cd_cidade <> D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_fora, "
						+ "SUM(CASE WHEN A.cd_cidade = D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_local")
				.addJoinTable(" JOIN mob_agente C ON A.cd_agente = C.cd_agente")
				.addJoinTable(" JOIN mob_orgao D ON C.cd_orgao = D.cd_orgao")
				.searchCriterios(searchCriterios)
				.groupBy(" EXTRACT(YEAR FROM A.dt_infracao), EXTRACT(MONTH FROM A.dt_infracao) ")
				.orderBy(" ano_infracao, mes_infracao ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}

	public Search<RelatorioEstatisticasAitsDTO> searchGravidade(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("COUNT(A.cd_ait) qtd_infracoes, B.nm_natureza, "
						+ "SUM(CASE WHEN A.cd_cidade <> D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_fora, "
						+ "SUM(CASE WHEN A.cd_cidade = D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_local")
				.addJoinTable(" JOIN mob_agente C ON A.cd_agente = C.cd_agente")
				.addJoinTable(" JOIN mob_orgao D ON C.cd_orgao = D.cd_orgao")
				.searchCriterios(searchCriterios)
				.addJoinTable(" INNER JOIN mob_infracao B ON (A.cd_infracao = B.cd_infracao) ")
				.groupBy(" nm_natureza ")
				.orderBy(" CASE "
						+ "WHEN UPPER(B.nm_natureza) = 'LEVE' THEN 1 "
						+ "WHEN UPPER(B.nm_natureza) = 'MÉDIA' THEN 2 "
						+ "WHEN UPPER(B.nm_natureza) = 'GRAVE' THEN 3 "
						+ "WHEN UPPER(B.nm_natureza) = 'GRAVÍSSIMA' THEN 4 END ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchInfracaoMoto(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Integer> tiposVeiculoMoto = new ArrayList<Integer>();
		tiposVeiculoMoto.add(tipoVeiculoService.getByNmTipoVeiculo("CICLOMOTOR").getCdTipoVeiculo());
		tiposVeiculoMoto.add(tipoVeiculoService.getByNmTipoVeiculo("MOTONETA").getCdTipoVeiculo());
		tiposVeiculoMoto.add(tipoVeiculoService.getByNmTipoVeiculo("MOTOCICLETA").getCdTipoVeiculo());
		searchCriterios.addCriterios("A.cd_tipo_veiculo", tiposVeiculoMoto.toString().replace("[", "").replace("]", ""), ItemComparator.IN, Types.INTEGER);
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("D.nr_cod_detran, D.ds_infracao, COUNT(A.cd_ait) AS qtd_infracoes, "
						+ "SUM(CASE WHEN A.cd_cidade <> C.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_fora, "
						+ "SUM(CASE WHEN A.cd_cidade = C.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_local")
				.searchCriterios(searchCriterios)
				.addJoinTable(" JOIN mob_agente B ON (A.cd_agente = B.cd_agente) ")
				.addJoinTable(" JOIN mob_orgao C ON (B.cd_orgao = C.cd_orgao) ")
				.addJoinTable(" JOIN mob_infracao D ON (A.cd_infracao = D.cd_infracao) ")
				.groupBy(" D.cd_infracao ")
				.orderBy(" qtd_infracoes DESC ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchEmissaoNaiNip(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		List<Integer> movimentosNaiNip = new ArrayList<Integer>();
		movimentosNaiNip.add(TipoStatusEnum.NAI_ENVIADO.getKey());
		movimentosNaiNip.add(TipoStatusEnum.NIP_ENVIADA.getKey());
		searchCriterios.addCriterios("A.tp_status", movimentosNaiNip.toString().replace("[", "").replace("]", ""), ItemComparator.IN, Types.INTEGER);
		searchCriterios.addCriteriosGreaterDate("date(A.dt_movimento)", searchCriterios.getAndRemoveCriterio("A.dt_infracao").getValue().split(" ")[0]);
		searchCriterios.addCriteriosMinorDate("date(A.dt_movimento)", searchCriterios.getAndRemoveCriterio("A.dt_infracao").getValue().split(" ")[0]);
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait_movimento A")
				.fields("date(A.dt_movimento) dt_movimento, COUNT(A.*) AS qtd_infracoes, "
						+ "SUM(CASE WHEN B.cd_cidade <> D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_fora, "
						+ "SUM(CASE WHEN B.cd_cidade = D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_local, "
						+ "SUM(CASE WHEN A.tp_status = 3 THEN 1 ELSE 0 END) AS qtd_nais, "
						+ "SUM(CASE WHEN A.tp_status = 5 THEN 1 ELSE 0 END) AS qtd_nips")
				.addJoinTable(" JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
				.addJoinTable(" JOIN mob_agente C ON (B.cd_agente = C.cd_agente) ")
				.addJoinTable(" JOIN mob_orgao D ON (C.cd_orgao = D.cd_orgao) ")
				.searchCriterios(searchCriterios)
				.groupBy(" date(A.dt_movimento) ")
				.orderBy(" date(A.dt_movimento) DESC ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchCompetenciaEstadual(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.addCriteriosEqualInteger("B.tp_competencia", TipoCompetenciaEnum.ESTADUAL.getKey());
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("COUNT(A.cd_ait) AS qtd_infracoes, B.nr_cod_detran, B.ds_infracao, "
						+ "SUM(CASE WHEN A.cd_cidade <> D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_fora, "
						+ "SUM(CASE WHEN A.cd_cidade = D.cd_cidade THEN 1 ELSE 0 END) AS qtd_infracoes_local")
				.addJoinTable(" JOIN mob_infracao B ON (A.cd_infracao = B.cd_infracao) ")
				.addJoinTable(" JOIN mob_agente C ON (A.cd_agente = C.cd_agente) ")
				.addJoinTable(" JOIN mob_orgao D ON (C.cd_orgao = D.cd_orgao) ")
				.searchCriterios(searchCriterios)
				.groupBy(" B.nr_cod_detran, B.cd_infracao ")
				.orderBy(" qtd_infracoes DESC ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchLogradouro(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("COUNT(A.cd_ait) AS qtd_infracoes, A.ds_local_infracao, "
						+ "SUM(A.vl_multa) AS soma_parcial")
				.searchCriterios(searchCriterios)
				.groupBy(" A.ds_local_infracao ")
				.orderBy(" qtd_infracoes DESC ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RelatorioEstatisticasAitsDTO> searchMunicipioPlaca(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioEstatisticasAitsDTO> search = new SearchBuilder<RelatorioEstatisticasAitsDTO>("mob_ait A")
				.fields("COUNT(A.cd_ait) AS qtd_infracoes, B.nm_cidade AS nm_municipio_placa, "
						+ "SUM(A.vl_multa) AS soma_parcial")
				.addJoinTable(" LEFT JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) ")
				.searchCriterios(searchCriterios)
				.groupBy(" B.nm_cidade ")
				.orderBy(" qtd_infracoes DESC ")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
}
