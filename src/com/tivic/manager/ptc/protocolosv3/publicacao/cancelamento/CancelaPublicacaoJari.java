package com.tivic.manager.ptc.protocolosv3.publicacao.cancelamento;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.lotes.enums.publicacao.TipoLotePublicacaoEnum;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.manager.mob.lotes.repository.publicacao.ILotePublicacaoAitRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class CancelaPublicacaoJari {
	private AitMovimentoRepository aitMovimentoRepository;
	private ILotePublicacaoAitRepository lotePublicacaoAitRepository;
	private IAitMovimentoService aitMovimentoService;
	private CustomConnection customConnection;
	
	public CancelaPublicacaoJari() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.lotePublicacaoAitRepository = (ILotePublicacaoAitRepository) BeansFactory.get(ILotePublicacaoAitRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	public void cancelar(AitMovimento aitMovimento, int cdUsuario, CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		AitMovimento movimentoCancelamento = aitMovimentoService.getMovimentoTpStatus(aitMovimento.getCdAit(), TipoStatusEnum.CANCELAMENTO_PUBLICACAO_RESULTADO_JARI.getKey());
		if (aitMovimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey()) {
			aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
			this.aitMovimentoRepository.update(aitMovimento, customConnection);
			return;
		}
		if (movimentoCancelamento.getCdMovimento() > 0) {
	        if (movimentoCancelamento.getLgEnviadoDetran() != TipoLgEnviadoDetranEnum.REGISTRADO.getKey()) {
	            throw new Exception("Essa publicação já está cancelada mas não registrada junto ao Detran.");
	        }
	        throw new Exception("Essa publicação já está cancelada.");
	    }
		AitMovimento novoMovimentoCancelamento = criarMovimento(aitMovimento, cdUsuario);
		aitMovimento.setCdMovimentoCancelamento(novoMovimentoCancelamento.getCdMovimento());
		aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey());
		this.aitMovimentoRepository.update(aitMovimento, customConnection);
	    deletarAitLoteImpressao(aitMovimento.getCdAit());
	}

	private AitMovimento criarMovimento(AitMovimento aitMovimento, int cdUsuario) throws Exception {
	    AitMovimento cancelamentoPublicacaoJari = new AitMovimentoBuilder()
	            .setCdAit(aitMovimento.getCdAit())
	            .setCdOcorrencia(aitMovimento.getCdOcorrencia())
	            .setDtMovimento(new GregorianCalendar())
	            .setTpStatus(TipoStatusEnum.CANCELAMENTO_PUBLICACAO_RESULTADO_JARI.getKey())
	            .setDtDigitacao(new GregorianCalendar())
	            .setCdUsuario(cdUsuario)
	            .setDsObservacao(aitMovimento.getDsObservacao())
	            .setNrProcesso(aitMovimento.getNrProcesso())
	            .build();	    
	    this.aitMovimentoRepository.insert(cancelamentoPublicacaoJari, this.customConnection);
	    return cancelamentoPublicacaoJari;
	}

	private void deletarAitLoteImpressao(int cdAit) throws Exception {
	    LotePublicacaoAit LotePublicacaoAit = searchLoteAit(cdAit);
	    if (LotePublicacaoAit.getCdAit() > 0) {
	    	lotePublicacaoAitRepository.delete(LotePublicacaoAit, this.customConnection);
	    } 
	}
	
	private LotePublicacaoAit searchLoteAit(int cdAit) throws Exception {
        LotePublicacaoAit LotePublicacaoAit = new LotePublicacaoAit();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("B.tp_publicacao", TipoLotePublicacaoEnum.LOTE_PUBLICACAO_RESULTADO_JARI.getKey());
		Search<LotePublicacaoAit> search = new SearchBuilder<LotePublicacaoAit>("mob_lote_publicacao_ait A")
				.addJoinTable("JOIN mob_lote_publicacao B ON (A.cd_lote_impressao = B.cd_lote_impressao)")
				.searchCriterios(searchCriterios)
				.build();
		List<LotePublicacaoAit> listaLoteImpressao = search.getList(LotePublicacaoAit.class);
	    if (!listaLoteImpressao.isEmpty()) {
	    	LotePublicacaoAit = listaLoteImpressao.get(0);
	    } 
	    return LotePublicacaoAit;
	}
	
}
