package com.tivic.manager.mob.lote.impressao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ListAitsCandidatosNipViaUnicaBuilder implements IListAitsCandidatasNotificacao {
	private CustomConnection customConnection;
	private List<Ait> aitList;
	private SearchCriterios searchCriterios;
	
	
	public ListAitsCandidatosNipViaUnicaBuilder(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		aitList = new ArrayList<Ait>();
		this.customConnection = customConnection;
		this.searchCriterios = searchCriterios;
		aitList = searchNip(searchCriteriosNip()).getList(Ait.class);
	}

	private Search<Ait> searchNip(SearchCriterios searchCriteriosNip) throws Exception {
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
				.customConnection(customConnection)
				.fields(" A.cd_ait, A.cd_infracao, A.cd_agente, A.cd_usuario, A.dt_infracao, A.ds_observacao, A.ds_local_infracao, A.vl_velocidade_permitida, A.vl_velocidade_aferida,"
					  + " A.vl_velocidade_penalidade, A.nr_placa, A.ds_ponto_referencia, A.lg_auto_assinado, A.vl_latitude, A.vl_longitude, A.cd_cidade, A.dt_digitacao, A.cd_equipamento,"
					  + " A.lg_enviado_detran, A.st_ait, A.cd_evento_equipamento, A.dt_sincronizacao, A.nr_tentativa_sincronizacao, A.cd_bairro, A.cd_usuario_exclusao, A.dt_vencimento, "
					  + " A.tp_natureza_autuacao, A.dt_movimento, A.tp_status, A.tp_arquivo, A.cd_ocorrencia, A.ds_parecer, A.nr_erro, A.st_entrega, A.nr_processo, A.dt_registro_detran, "
					  + " A.st_recurso, A.lg_advertencia, A.nr_controle, A.nr_renainf, A.nr_sequencial, A.dt_ar_nai, A.nr_notificacao_nai, A.nr_notificacao_nip, A.tp_cancelamento, A.lg_cancela_movimento, "
					  + " A.dt_cancelamento_movimento, A.nr_remessa, A.nr_codigo_barras, A.nr_remessa_registro, A.dt_emissao_nip, A.dt_resultado_defesa, A.dt_atualizacao, A.dt_resultado_jari, "
					  + " A.dt_resultado_cetran, A.lg_advertencia_jari, A.lg_advertencia_cetran, A.lg_notrigger, A.st_detran, A.lg_erro, A.txt_observacao, A.cd_movimento_atual, A.cd_ait_origem, "
					  + " A.nr_fator_nic, A.lg_detran_febraban, A.nr_placa_antiga, A.cd_especie_veiculo, A.cd_marca_veiculo, A.cd_cor_veiculo, A.cd_tipo_veiculo, A.cd_categoria_veiculo, "
					  + " A.nr_renavan, A.nr_ano_fabricacao, A.nr_ano_modelo, A.nm_proprietario, A.tp_documento, A.nr_documento, A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.tp_cnh_condutor, "
					  + " A.cd_marca_autuacao, A.nm_condutor_autuacao, A.nm_proprietario_autuacao, A.nr_cnh_autuacao, A.uf_cnh_autuacao, A.nr_documento_autuacao, A.tp_pessoa_proprietario, A.cd_banco, "
					  + " A.sg_uf_veiculo, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, A.nr_ddd, A.nr_telefone, A.ds_endereco_condutor, A.nr_cpf_proprietario, A.nr_cpf_cnpj_proprietario, A.nm_complemento,"
					  + " A.ds_bairro_condutor, A.nr_imovel_condutor, A.ds_complemento_condutor, A.cd_cidade_condutor, A.nr_cep_condutor, A.dt_primeiro_registro, A.nr_cpf_condutor, A.cd_veiculo, A.cd_endereco,"
					  + " A.cd_proprietario, A.cd_condutor, A.cd_endereco_condutor, A.dt_notificacao_devolucao, A.tp_origem, A.lg_publicar_nai_diario_oficial, A.tp_convenio,id_ait, A.nr_ait, A.cd_talao, "
					  + " A.dt_afericao, A.nr_lacre, A.nr_inventario_inmetro, A.nr_processamento, A.dt_prazo_defesa, A.tp_status_temp, A.dt_registro_detran_temp, A.cd_movimento_atual_temp, "
					  + " A.cd_logradouro_infracao,  B.vl_infracao AS vl_multa, B.nr_cod_detran, B.ds_infracao, B.nr_artigo, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao, "
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
				.searchCriterios(searchCriteriosNip)
				.additionalCriterias(" NOT EXISTS "
						+ " ("
						+ "		SELECT B.tp_competencia FROM mob_infracao B "
						+ " 	WHERE "
						+ "			( "
						+ "         	B.tp_competencia = " + TipoCompetenciaEnum.ESTADUAL.getKey()
						+ " 		)"
						+ " 	AND A.cd_infracao = B.cd_infracao "						
						+ " )"
						+ " ")
				.orderBy(" B.dt_fim_vigencia desc")
				.build();
		return search;
	}
	
	private SearchCriterios searchCriteriosNip() {
		searchCriterios.setQtLimite(1);
		return searchCriterios;
	}

	@Override
	public List<Ait> build() throws IllegalArgumentException, SQLException, Exception {
		return aitList;
	}

}
