package com.tivic.manager.mob.lotes.impressao.ait;

import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public abstract class DadosNotificacaoAitBase {
	
	protected abstract Search<Notificacao> getNotificacaoSearchByCdAit(int cdAit) throws Exception;
	
	public Notificacao getNotificacaoByCdAit(int cdAit) throws Exception {
		Search<Notificacao> notificacaoSearch = getNotificacaoSearchByCdAit(cdAit);
		return notificacaoSearch.getList(Notificacao.class).get(0);	
	}
	
	public SearchBuilder<Notificacao> getDadosByCdAit(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		return new SearchBuilder<Notificacao>("mob_ait A")
				.fields(  " A.cd_ait, A.id_ait, A.nr_renainf, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, A.ds_observacao, A.nr_cpf_condutor, A.dt_afericao,"
						+ " A.nr_lacre, A.nr_placa, A.sg_uf_veiculo, A.nr_renavan, A.ds_observacao, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.ds_local_infracao, A.ds_ponto_referencia, A.vl_velocidade_permitida, K.nr_erro, A.nr_inventario_inmetro, "
						+ " A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, A.lg_auto_assinado, B.tp_responsabilidade, A.dt_vencimento, "
						+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, B.nr_inciso, B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao, B.lg_suspensao_cnh, B.vl_infracao, "
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
						+ " E.nm_categoria,"
						+ " F.nm_cor,"
						+ " G.ds_especie,"
						+ " H.nm_marca, H.nm_modelo,"
						+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, I.cd_equipamento,"
						+ " J.nm_agente, J.nr_matricula,"
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran, "
						+ " M.dt_afericao, M.id_identificacao_inmetro,"
						+ " N.ds_logradouro,"
						+ " P.nm_bairro, "
						+ " Q.cd_cidade, Q.nm_cidade AS CIDADE_PROPRIETARIO, "
						+ " R.sg_estado AS UF_PROPRIETARIO, R.nm_estado AS ESTADO_PROPRIETARIO, "
						+ " CURRENT_DATE AS dt_emissao ")
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
				.addJoinTable(" LEFT OUTER JOIN grl_cidade             Q  ON (Q.cd_cidade = A.cd_cidade_proprietario)")
				.addJoinTable(" LEFT OUTER JOIN grl_estado             R  ON (R.cd_estado = Q.cd_estado)")
				.searchCriterios(searchCriterios)
				.orderBy(" B.dt_fim_vigencia desc");
	}

}
