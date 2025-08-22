package com.tivic.manager.adapter.base.antiga.dadosnotificacao;

import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class DadosNotificacaoOldNIP {
	public Search<DadosNotificacao> search(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<DadosNotificacao> search = new SearchBuilder<DadosNotificacao>("AIT A")
				.fields( " A.CODIGO_AIT AS CD_AIT, A.nr_ait as id_ait, A.nr_inventario_inmetro, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, "
					   + " A.ds_observacao, A.nr_cpf_condutor, A.dt_afericao, A.nr_lacre, A.nr_placa, A.COD_AGENTE as CD_AGENTE, "
					   + " A.UF_VEICULO AS SG_UF_VEICULO, A.CD_RENAVAN AS NR_RENAVAN, A.nr_renainf, A.ds_observacao, A.nm_proprietario, "
					   + " A.nr_cpf_cnpj_proprietario, A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.ds_local_infracao, A.ds_ponto_referencia, "
					   + " A.vl_velocidade_permitida, A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel,"
					   + " A.nr_cep, A.lg_auto_assinado, A.dt_vencimento, "
					   + " B.nr_cod_detran, B.DS_INFRACAO2 AS DS_INFRACAO, B.nr_artigo, B.nr_inciso, B.vl_infracao AS vl_multa, "
					   + " B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao, "
					   + " C.nm_municipio, C.nm_uf, "
					   + " D.nm_bairro, "
					   + " E.nm_categoria, "
					   + " F.nm_cor, "
					   + " G.ds_especie, "
					   + " H.nm_marca, H.nm_modelo,"
					   + " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, "
					   + " I.nm_modelo AS modelo_equipamento, I.tp_equipamento, " 
					   + " J.nm_agente, J.nr_matricula, "
					   + " K.dt_movimento, K.tp_status, K.nr_processo, "
					   + " M.dt_afericao, M.id_identificacao_inmetro, "
					   + " P.nm_bairro, "
					   + " Q.cod_municipio, Q.nm_municipio AS CIDADE_PROPRIETARIO, Q.nm_uf as UF_PROPRIETARIO " 
					   + " ")
				.addJoinTable(" LEFT OUTER JOIN infracao 		 	B ON (A.cod_infracao = B.cod_infracao) ")
				.addJoinTable(" LEFT OUTER JOIN municipio 		 	C ON (A.cod_municipio = C.cod_municipio) ")
				.addJoinTable(" LEFT OUTER JOIN bairro 			 	D ON (A.cod_bairro = D.cod_bairro) ")
				.addJoinTable(" LEFT OUTER JOIN categoria_veiculo	E ON (A.cod_categoria = E.cod_categoria) ")
				.addJoinTable(" LEFT OUTER JOIN cor 				F ON (A.cod_cor = F.cod_cor) ")
				.addJoinTable(" LEFT OUTER JOIN especie_veiculo 	G ON (A.cod_especie = G.cod_especie) ")
				.addJoinTable(" LEFT OUTER JOIN marca_modelo 	 	H ON (A.cod_marca = H.cod_marca) ")
				.addJoinTable(" LEFT OUTER JOIN grl_equipamento 	I ON (A.cd_equipamento = I.cd_equipamento) ")
				.addJoinTable(" LEFT OUTER JOIN agente 			 	J ON (A.cod_agente = J.cod_agente) ")
				.addJoinTable(" LEFT OUTER JOIN ait_movimento		K  ON (A.codigo_ait = K.codigo_ait AND A.tp_status = K.tp_status)")
				.addJoinTable(" LEFT OUTER JOIN mob_ait_evento      L  ON (A.codigo_ait = L.cd_ait) ")
				.addJoinTable(" LEFT OUTER JOIN mob_evento_equipamento M  ON (L.cd_evento = M.cd_evento) ")
				.addJoinTable(" LEFT OUTER JOIN bairro             P  ON (A.cod_bairro = P.cod_bairro)")
				.addJoinTable(" LEFT OUTER JOIN municipio             Q  ON (Q.cod_municipio = P.cod_municipio)")
				.searchCriterios(searchCriterios)
				.orderBy(" A.dt_infracao ASC")
				.customConnection(customConnection)
				.build();
		return search;
	}
}
