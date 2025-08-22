package com.tivic.manager.mob.lote.impressao.viaunica.builders;

import java.sql.Types;
import java.util.List;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.lote.impressao.TipoLoteNotificacaoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.TipoImpressaoNotificacao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class TipoImpressaoNotificacaoBuilder {
	
	private TipoImpressaoNotificacao tipoImpressaoNotificacao;
	private IAitMovimentoService aitMovimentoServices;
	
	public TipoImpressaoNotificacaoBuilder(int cdAit, int tpNotificao, int cdUsuario) throws Exception {
		this.tipoImpressaoNotificacao = new TipoImpressaoNotificacao();
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		obterDadosNotificacao(cdAit, tpNotificao, cdUsuario);
	}
	
	private void obterDadosNotificacao(int cdAit, int tpNotificao, int cdUsuario) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(cdAit, tpNotificao);
		AitMovimento aitMovimentoCancelado = this.aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.CANCELAMENTO_NIP.getKey());
		List<LoteImpressao> loteImpressao = searchLote(cdAit).getList(LoteImpressao.class);
		this.tipoImpressaoNotificacao.setCdAit(cdAit);
		this.tipoImpressaoNotificacao.setCdUsuario(cdUsuario);
		this.tipoImpressaoNotificacao.setContemMovimento(aitMovimento.getCdMovimento() > 0);
		this.tipoImpressaoNotificacao.setMovimentoEnviado(aitMovimento.getLgEnviadoDetran() == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey());
		this.tipoImpressaoNotificacao.setMovimentoCancelado(aitMovimentoCancelado.getCdMovimento() > 0);
		this.tipoImpressaoNotificacao.setRegistradoEmLote(!(loteImpressao.isEmpty()));
	}
	
	private Search<LoteImpressao> searchLote(int cdAit) throws Exception {
		Search<LoteImpressao> search = new SearchBuilder<LoteImpressao>("mob_lote_impressao A")
				.fields("A.cd_lote_impressao, A.tp_impressao, B.cd_ait")
				.addJoinTable(" JOIN mob_lote_impressao_ait B ON (B.cd_lote_impressao = A.cd_lote_impressao) ")
				.searchCriterios(searchCriteriosLote(cdAit))
				.build();
		return search;
	}
	
	private SearchCriterios searchCriteriosLote(int cdAit) {
		SearchCriterios search = new SearchCriterios();
		search.addCriterios("A.tp_documento", String.valueOf(TipoLoteNotificacaoEnum.LOTE_NAI.getKey()) + ", " +
	            String.valueOf(TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey()), ItemComparator.IN, Types.INTEGER);
		search.addCriteriosEqualInteger("B.cd_ait", cdAit, true);
		return search;
	}
	
	public TipoImpressaoNotificacao build() {
		return this.tipoImpressaoNotificacao;
	}
	
}
