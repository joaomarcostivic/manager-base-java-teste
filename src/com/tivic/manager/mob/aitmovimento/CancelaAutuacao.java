package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CancelaAutuacao {
	
	private AitMovimentoRepository aitMovimentoRepository;
	private CustomConnection customConnection;
	private AitMovimento aitMovimento;
	
	public CancelaAutuacao(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.customConnection = customConnection;
		this.aitMovimento = aitMovimento;
		this.cancelar();
	}

	private void cancelar() throws Exception {
		
		AitMovimento cancelamentoAutuacao = new AitMovimentoBuilder()
				.setCdAit(this.aitMovimento.getCdAit())
				.setCdOcorrencia(this.aitMovimento.getCdOcorrencia())
				.setDtMovimento(new GregorianCalendar())
				.setTpStatus(TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey())
				.setLgEnviadoDetran(TipoSituacaoAitEnum.NAO_ENVIADO.getKey())
				.setDtDigitacao(new GregorianCalendar())
				.setCdUsuario(this.aitMovimento.getCdUsuario())
				.setDsObservacao(this.aitMovimento.getDsObservacao())
			.build();
		
		this.aitMovimentoRepository.insert(cancelamentoAutuacao, this.customConnection);
	}
}
