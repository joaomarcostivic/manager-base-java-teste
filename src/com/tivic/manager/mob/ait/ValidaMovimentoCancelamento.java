package com.tivic.manager.mob.ait;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ValidaMovimentoCancelamento {
	
	private int cdAit;
	private CustomConnection customConnection;
	private IAitMovimentoService aitMovimentoService;
	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	
	public ValidaMovimentoCancelamento(int cdAit, CustomConnection customConnection) throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.cdAit = cdAit;
		this.customConnection = customConnection;
	}

	public void cancelamento() throws Exception {
		if(!getMovimentoPendente(this.cdAit).isEmpty()) {
			throw new Exception("Este AIT possui movimentos pendentes e nao pode ser cancelado.");
		}
		
		if(!getMovimentoNaoEnviado(this.cdAit).isEmpty()) {
			throw new Exception("Este AIT já está cancelado e precisa ser enviado.");
		}
		
		
		Ait ait = aitRepository.get(this.cdAit, this.customConnection);
		AitMovimento movimentoAtual = aitMovimentoRepository.get(ait.getCdMovimentoAtual(), ait.getCdAit(), this.customConnection);
		
		if(movimentoAtual == null) {
			throw new Exception("Este AIT não possui um registro de movimento atual informado.");
		}
		
		if(verificarMovimento(movimentoAtual.getTpStatus())) {
			throw new Exception("Este AIT já está cancelado.");
		}
	}
	
	private List<AitMovimento> getMovimentoPendente(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoSituacaoAitEnum.ENVIADO_AGUARDANDO.getKey(), true);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoService.find(searchCriterios, this.customConnection);
		return aitMovimentoList;
	}
	
	private List<AitMovimento> getMovimentoNaoEnviado(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey(), true);
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoSituacaoAitEnum.NAO_ENVIADO.getKey(), true);
		List<AitMovimento> aitMovimentoList = this.aitMovimentoService.find(searchCriterios, this.customConnection);
		return aitMovimentoList;
	}
	
	private Boolean verificarMovimento(int tpStatus) {
		List<Integer> movimentosCancelamento = new ArrayList<Integer>();
		movimentosCancelamento.add(TipoStatusEnum.CADASTRO_CANCELADO.getKey());
		movimentosCancelamento.add(TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey());
		movimentosCancelamento.add(TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey());
		movimentosCancelamento.add(TipoStatusEnum.CANCELAMENTO_MULTA.getKey());
		movimentosCancelamento.add(TipoStatusEnum.DEVOLUCAO_PAGAMENTO.getKey());
		
		return movimentosCancelamento.contains(tpStatus);	
	}
}
