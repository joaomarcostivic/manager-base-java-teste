package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.CalculaPrazoRecurso;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTO;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.DadosDocumento;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoListaPostagem;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RemessaSimplesNICNAIBuilder implements IBuilderRemessa{
	private DadosDocumento dadosDocumento;
	private CustomConnection customConnection;
	private IAitMovimentoService aitMovimentoServices;
	
	public RemessaSimplesNICNAIBuilder() throws Exception {
		dadosDocumento = new DadosDocumento();
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public void montarDadosRemessa(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		List<LoteImpressaoAitDTO> loteImpressaoAitDTOList = searchLoteAits(loteImpressao.getCdLoteImpressao()).getList(LoteImpressaoAitDTO.class);
		dadosDocumento.setCdLoteNotificacao(loteImpressao.getCdLoteImpressao());
		dadosDocumento.setDadosNotificacaoList(getDadosNotificacao(loteImpressaoAitDTOList));
		dadosDocumento.setNmDocumento("mob/nic_nai_v2");
		dadosDocumento.setArquivoListaPostagem(new ArquivoListaPostagem(dadosDocumento, TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey(), customConnection).gerar());
	}
	
	private Search<LoteImpressaoAitDTO> searchLoteAits(int cdLote) throws Exception {
		SearchCriterios searchCriterios = montarCriteriosLoteAits(cdLote);
		Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("MOB_LOTE_IMPRESSAO_AIT A")
				.addJoinTable("JOIN mob_ait B ON (A.cd_ait = B.cd_ait)")
				.orderBy("B.nr_cep")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private SearchCriterios montarCriteriosLoteAits(int cdLote) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLote, true);
		return searchCriterios;
	}
	
	private List<DadosNotificacao> getDadosNotificacao(List<LoteImpressaoAitDTO> loteImpressaoAitDTOList) throws Exception {
		List<DadosNotificacao> dadosNotificacaosList = new ArrayList<DadosNotificacao>();
		for (LoteImpressaoAitDTO loteImpressaoAitDTO: loteImpressaoAitDTOList) {
			SearchCriterios searchCriterios = montarCriterios(loteImpressaoAitDTO.getCdAit());
			DadosNotificacao dadosNotificacao = searchNais(searchCriterios).getList(DadosNotificacao.class).get(0);
			dadosNotificacao.setCdLoteImpressao(loteImpressaoAitDTO.getCdLoteImpressao());
			dadosNotificacaosList.add(dadosNotificacao);
		}
		montarDadosDocumento(dadosNotificacaosList);
		return dadosNotificacaosList;
	}
	
	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
	
	private Search<DadosNotificacao> searchNais(SearchCriterios searchCriterios) throws Exception {
		Search<DadosNotificacao> search = new SearchBuilder<DadosNotificacao>("MOB_AIT A")
				.fields(  " A.cd_Ait, A.id_ait, A.nr_renainf, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, A.ds_observacao, A.dt_afericao,"
						+ " A.nr_lacre, A.nr_placa, A.sg_uf_veiculo, A.nr_renavan, A.ds_observacao, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.ds_local_infracao, A.ds_ponto_referencia,  A.vl_multa, A.vl_velocidade_permitida, "
						+ " A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, "
						+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, B.nr_inciso, B.nr_paragrafo, B.nr_alinea, B.nm_natureza, B.nr_pontuacao,"
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
						+ " E.nm_categoria,"
						+ " F.nm_cor,"
						+ " G.ds_especie,"
						+ " H.nm_marca, H.nm_modelo,"
						+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento,"
						+ " J.nm_agente, J.nr_matricula,"
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.lg_enviado_detran, "
						+ " M.dt_afericao, M.id_identificacao_inmetro,"
						+ " N.ds_logradouro,"
						+ " P.nm_bairro, "
						+ " Q.cd_cidade, Q.nm_cidade AS cidade_proprietario, "
						+ " R.sg_estado AS uf_proprietario, R.nm_estado AS estado_proprietario, "
						+ " S.id_ait as id_ait_geradora, S.ds_local_infracao AS ds_local_infracao_geradora, S.dt_infracao AS dt_infracao_geradora, "
						+ "	S.vl_multa as vl_multa_geradora, S.vl_velocidade_permitida as vl_velocidade_permitida_geradora, S.vl_velocidade_aferida as vl_velocidade_aferida_geradora,"
						+ " S.vl_velocidade_penalidade as vl_velocidade_penalidade_geradora, S.vl_velocidade_penalidade as vl_velocidade_penalidade_geradora,  T.nr_cod_detran as nr_cod_detran_geradora, "
						+ " T.nr_paragrafo as nr_paragrafo_geradora, T.nr_alinea as nr_alinea_geradora, T.nm_natureza as nm_natureza_geradora, T.nr_pontuacao as nr_pontuacao_geradora,"
						+ " T.nr_artigo as nr_artigo_geradora, T.nr_inciso as nr_inciso_geradora, T.ds_infracao as ds_infracao_geradora") 
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
				.addJoinTable(" JOIN mob_ait S ON (A.cd_ait_origem = S.cd_ait)")
				.addJoinTable(" JOIN mob_infracao T ON (S.cd_infracao = T.cd_infracao)")
				.searchCriterios(searchCriterios)
				.orderBy(" B.dt_fim_vigencia desc")
				.build();
		return search;
	}
	
	private void montarDadosDocumento(List<DadosNotificacao> listDadosNotificacao) throws Exception {
		for (DadosNotificacao dadosNotificacao : listDadosNotificacao) {
			AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(dadosNotificacao.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
			dadosNotificacao.setDtMovimento(aitMovimento.getDtMovimento());
			dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
			if (dadosNotificacao.getDtPrazoDefesa() == null) {
				dadosNotificacao.setDtPrazoDefesa(new CalculaPrazoRecurso()
						.getStrategy(TipoStatusEnum.DEFESA_PREVIA.getKey())
						.gerarPrazo(dadosNotificacao.getCdAit(), customConnection));
			}
		}
	}
	
	@Override
	public DadosDocumento build() {
		return this.dadosDocumento;
	}
	
}
