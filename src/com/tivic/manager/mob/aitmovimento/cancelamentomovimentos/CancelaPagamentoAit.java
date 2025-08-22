package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CancelaPagamentoAit {	
	private AitMovimentoRepository aitMovimentoRepository;
	private CustomConnection customConnection;
	private AitMovimento aitMovimento;
	
	public CancelaPagamentoAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.customConnection = customConnection;
		this.aitMovimento = aitMovimento;
		this.cancelarPagamento();
	}
	
	private void cancelarPagamento() throws Exception {
		AitMovimento cancelamentoPagamento = new AitMovimentoBuilder()
				.setCdAit(this.aitMovimento.getCdAit())
				.setCdOcorrencia(ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_CANCELAMENTO", 0))
				.setDtMovimento(new GregorianCalendar())
				.setTpStatus(TipoStatusEnum.CANCELAMENTO_PAGAMENTO.getKey())
				.setLgEnviadoDetran(TipoSituacaoAitEnum.NAO_ENVIADO.getKey())
				.setDtDigitacao(new GregorianCalendar())
				.setCdUsuario(this.aitMovimento.getCdUsuario())
			.build();
		this.aitMovimentoRepository.insert(cancelamentoPagamento, this.customConnection);
	}

}
