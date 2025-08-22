package com.tivic.manager.mob.lote.impressao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ListAitsCandidatosNicNaiViaUnicaBuilder implements IListAitsCandidatosNai {

	@Override
	public List<Ait> build(ArrayList<ItemComparator> criterios, Connection connect) throws IllegalArgumentException, SQLException, Exception {
		SearchCriterios searchCriteriosNai = searchCriteriosNai(criterios);
		return searchNai(searchCriteriosNai).getList(Ait.class);
	}
	
	private Search<Ait> searchNai(SearchCriterios searchCriteriosNai) throws Exception {
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
				.fields(" A.*, B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, "
					  + " C.nm_cidade, C.nm_cidade AS nm_municipio, C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado, "
					  + " E.nm_categoria, F.nm_cor, G.ds_especie, H.nm_marca, H.nm_modelo, "
					  + " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, "
					  + " J.nm_agente, J.nr_matricula, "
					  + " K.dt_movimento, K.tp_status, K.nr_processo, M.dt_afericao, M.id_identificacao_inmetro, N.ds_logradouro, P.nm_bairro ")
				.addJoinTable(" LEFT OUTER JOIN mob_infracao 		   B  ON (A.cd_infracao = B.cd_infracao)")
				.addJoinTable(" LEFT OUTER JOIN grl_cidade 			   C  ON (A.cd_cidade = C.cd_cidade)")
				.addJoinTable(" LEFT OUTER JOIN grl_estado 			   C1 ON (C.cd_estado = C1.cd_estado)")
				.addJoinTable(" LEFT OUTER JOIN fta_categoria_veiculo  E  ON (A.cd_categoria_veiculo = E.cd_categoria)")
				.addJoinTable(" LEFT OUTER JOIN fta_cor 			   F  ON (A.cd_cor_veiculo = F.cd_cor)")
				.addJoinTable(" LEFT OUTER JOIN fta_especie_veiculo    G  ON (A.cd_especie_veiculo = G.cd_especie)")
				.addJoinTable(" LEFT OUTER JOIN fta_marca_modelo	   H  ON (A.cd_marca_veiculo = H.cd_marca)")
				.addJoinTable(" LEFT OUTER JOIN grl_equipamento		   I  ON (A.cd_equipamento = I.cd_equipamento)")
				.addJoinTable(" LEFT OUTER JOIN mob_agente			   J  ON (A.cd_agente = J.cd_agente)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_movimento	   K  ON (A.cd_ait = K.cd_ait AND A.cd_movimento_atual = K.cd_movimento)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_evento         L  ON (A.cd_ait = L.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_proprietario   N  ON (A.cd_ait = N.cd_ait)")
				.addJoinTable(" LEFT OUTER JOIN grl_bairro             P  ON (A.cd_bairro = P.cd_bairro)")
				.searchCriterios(searchCriteriosNai)
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ "		SELECT B.tp_competencia FROM mob_infracao B "
						+ " 	WHERE "
						+ "			( "
						+ "         	tp_competencia = " + TipoCompetenciaEnum.ESTADUAL.getKey()
						+ " 		)"
						+ " 	AND A.cd_infracao = B.cd_infracao "						
						+ " )"
						+ " ")
				.build();
		return search;
	}
	
	private SearchCriterios searchCriteriosNai(ArrayList<ItemComparator> criterios) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.setCriterios(criterios);
		searchCriterios.setQtLimite(1);
		return searchCriterios;
	}

}
