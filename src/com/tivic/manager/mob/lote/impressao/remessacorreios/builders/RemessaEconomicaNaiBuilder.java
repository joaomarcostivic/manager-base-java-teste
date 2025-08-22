package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.infracao.TipoResponsabilidadeInfracaoEnum;
import com.tivic.manager.mob.lote.impressao.CalculaPrazoRecurso;
import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitDTO;
import com.tivic.manager.mob.lote.impressao.TipoArDigitalEnum;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.DadosDocumento;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoListaPostagem;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoPrevisaoPostagem;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RemessaEconomicaNaiBuilder implements IBuilderRemessa {
	private DadosDocumento dadosDocumento;
	private CustomConnection customConnection;
	private ICorreiosEtiquetaRepository correiosEtiquetaRepository;
	private IAitMovimentoService aitMovimentoServices;
	private IParametroService parametroService;
	
	public RemessaEconomicaNaiBuilder() throws Exception {
		dadosDocumento = new DadosDocumento();
		correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
	}
	
	@Override
	public void montarDadosRemessa(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		int tipoAr = Integer.parseInt(ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nai"));
		List<LoteImpressaoAitDTO> loteImpressaoAitDTOList = searchLoteAits(loteImpressao.getCdLoteImpressao()).getList(LoteImpressaoAitDTO.class);
		dadosDocumento.setCdLoteNotificacao(loteImpressao.getCdLoteImpressao());
		dadosDocumento.setDadosNotificacaoList(getDadosNotificacao(loteImpressaoAitDTOList));
		dadosDocumento.setDadosEtiqueta(getEtiquetas(loteImpressao.getCdLoteImpressao(), dadosDocumento.getDadosNotificacaoList()));
		dadosDocumento.setNmDocumento((tipoAr == TipoArDigitalEnum.AR_DIGITAL.getKey() ? "mob/nai_remessa_economica_ar_digital": "mob/nai_remessa_economica_ar_digital_2D"));
		dadosDocumento.setArquivoPostagem(new ArquivoPrevisaoPostagem(dadosDocumento, customConnection).gerar()); 
		dadosDocumento.setArquivoListaPostagem(new ArquivoListaPostagem(dadosDocumento, TipoLoteNotificacaoEnum.LOTE_NAI.getKey(), customConnection).gerar());
	}

	private Search<LoteImpressaoAitDTO> searchLoteAits(int cdLote) throws Exception {
		SearchCriterios searchCriterios = montarCriteriosLoteAits(cdLote);
		Search<LoteImpressaoAitDTO> search = new SearchBuilder<LoteImpressaoAitDTO>("MOB_LOTE_IMPRESSAO_AIT A")
				.addJoinTable("JOIN mob_ait B ON(A.cd_ait = B.cd_ait)")
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

	private List<CorreiosEtiqueta> getEtiquetas(int cdLoteNotificacao, List<DadosNotificacao> dadosNotificacaoList) throws Exception {
		List<CorreiosEtiqueta> correiosEtiquetasList = correiosEtiquetaRepository.find(criteriosEtiquetasReservadas(cdLoteNotificacao), customConnection);
		for (int index = 0; dadosNotificacaoList.size() > index; index++) {
			new DadosNotificacaoBuilder(dadosNotificacaoList.get(index), correiosEtiquetasList.get(index));
		}
		return correiosEtiquetasList;
	}
	
	private SearchCriterios criteriosEtiquetasReservadas(int cdLoteNotificacao) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", cdLoteNotificacao, true);
		return searchCriterios;
	}
	
	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
	
	private Search<DadosNotificacao> searchNais(SearchCriterios searchCriterios) throws Exception {
		Search<DadosNotificacao> search = new SearchBuilder<DadosNotificacao>("MOB_AIT A")
				.fields(  " A.cd_Ait, A.nr_inventario_inmetro, A.id_ait, A.nr_renainf, A.nr_controle, A.dt_infracao, A.dt_prazo_defesa, A.ds_observacao, A.nr_cpf_condutor, A.dt_afericao,"
						+ " A.nr_lacre, A.nr_placa, A.sg_uf_veiculo, A.nr_renavan, A.ds_observacao, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.nm_condutor, A.nr_cnh_condutor, A.uf_cnh_condutor, A.ds_local_infracao, A.ds_ponto_referencia,  B.vl_infracao AS vl_multa, A.vl_velocidade_permitida, "
						+ " A.vl_velocidade_aferida, A.vl_velocidade_penalidade, A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, A.lg_auto_assinado, "
						+ " B.nr_cod_detran, B.ds_infracao, B.nr_artigo, B.tp_responsabilidade, nr_inciso, nr_paragrafo, nr_alinea, nm_natureza, nr_pontuacao,"
						+ " C.nm_cidade, C.nm_cidade AS nm_municipio, "
						+ " C1.sg_estado, C1.sg_estado AS nm_uf, C1.nm_estado,"
						+ " E.nm_categoria,"
						+ " F.nm_cor,"
						+ " G.ds_especie,"
						+ " H.nm_marca, H.nm_modelo,"
						+ " I.nm_equipamento, I.id_equipamento, I.nm_marca AS marca_equipamento, I.nm_modelo AS modelo_equipamento, I.tp_equipamento, I.cd_equipamento,"
						+ " J.nm_agente, J.nr_matricula,"
						+ " K.dt_movimento, K.tp_status, K.nr_processo, K.nr_erro, K.lg_enviado_detran, "
						+ " M.id_identificacao_inmetro,"
						+ " N.ds_logradouro,"
						+ " P.nm_bairro, "
						+ " Q.cd_cidade, Q.nm_cidade AS CIDADE_PROPRIETARIO, "
						+ " R.sg_estado AS UF_PROPRIETARIO, R.nm_estado AS ESTADO_PROPRIETARIO ")
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
				.orderBy(" B.dt_fim_vigencia desc")
				.build();
		return search;
	}
	
	private void montarDadosDocumento(List<DadosNotificacao> listDadosNotificacao) throws Exception {
		for (DadosNotificacao dadosNotificacao : listDadosNotificacao) {
			this.dadosDocumento.getCriteriosDocumentos().addParametros("MOB_INFORMACOES_ADICIONAIS_NAI", verificarTipoResponsabilidadeInfracao(dadosNotificacao.getTpResponsabilidade()));
			AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(dadosNotificacao.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey());
			dadosNotificacao.setDtMovimento(aitMovimento.getDtMovimento());
			if(dadosNotificacao.getCdEquipamento() > 0) {
				dadosNotificacao.setNomeEquipamento(EquipamentoEnum.valueOf(dadosNotificacao.getTpEquipamento()));
			} else {
				dadosNotificacao.setNomeEquipamento(EquipamentoEnum.NENHUM.getValue());
			}
			if (dadosNotificacao.getDtPrazoDefesa() == null) {
				dadosNotificacao.setDtPrazoDefesa(new CalculaPrazoRecurso()
						.getStrategy(TipoStatusEnum.DEFESA_PREVIA.getKey())
						.gerarPrazo(dadosNotificacao.getCdAit(), customConnection));
			}
		}
		this.dadosDocumento.getCriteriosDocumentos().addParametros("MOB_LOGO_RECEBIMENTO_AR_DIGITAL", ParametroServices.RecImg("MOB_LOGO_RECEBIMENTO_AR_DIGITAL", customConnection.getConnection()));
		this.dadosDocumento.getCriteriosDocumentos().addParametros("MOB_LOGO_CORREIOS_CHANCELA", ParametroServices.RecImg("MOB_LOGO_CORREIOS_CHANCELA", customConnection.getConnection()));
	}	
	
	@Override
	public DadosDocumento build() {
		return this.dadosDocumento;
	}
	
	private String verificarTipoResponsabilidadeInfracao(int tpResponsabilidade) throws Exception {
		String parametro;
		if(tpResponsabilidade == TipoResponsabilidadeInfracaoEnum.MULTA_RESPONSABILIDADE_PROPRIETARIO.getKey()) {
			parametro = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_RESPONSABILIDADE_PROPRIETARIO").getTxtValorParametro();
			return parametro;
		}
		parametro = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_NAI").getTxtValorParametro();
		return parametro;
	}
}
