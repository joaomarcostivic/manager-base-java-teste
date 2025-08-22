package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.eventoequipamento.enums.LgEnviadoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CancelaRegistro {

	private AitMovimentoRepository aitMovimentoRepository;
	private IAitMovimentoService aitMovimentoService;
	private CustomConnection customConnection;
	private AitMovimento aitMovimento;
	
	public CancelaRegistro(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.customConnection = customConnection;
		this.aitMovimento = aitMovimento;
		gerenciarMovimentoCancelamento(this.aitMovimento, this.customConnection);
	}
	
	private void gerenciarMovimentoCancelamento(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		AitMovimento movimento = aitMovimentoService.getStatusMovimento(this.aitMovimento.getCdAit(), aitMovimento.getTpStatus());
		if(movimento.getCdAit() > 0 && movimento.getLgEnviadoDetran() == TipoSituacaoAitEnum.REGISTRADO.getKey()) {
			cancelar();
			setLgNaoEnviarMovimentos();
		} else {
			setLgNaoEnviarMovimentos();
		}
	}
	
	
	private void cancelar() throws Exception {
		AitMovimento cancelamentoRegistro = new AitMovimentoBuilder()
				.setCdAit(this.aitMovimento.getCdAit())
				.setCdOcorrencia(this.aitMovimento.getCdOcorrencia())
				.setDtMovimento(new GregorianCalendar())
				.setTpStatus(TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey())
				.setLgEnviadoDetran(TipoSituacaoAitEnum.NAO_ENVIADO.getKey())
				.setDtDigitacao(new GregorianCalendar())
				.setCdUsuario(this.aitMovimento.getCdUsuario())
				.setDsObservacao(this.aitMovimento.getDsObservacao())
			.build();
		aitMovimentoRepository.insert(cancelamentoRegistro, customConnection);	
	}
	
	private void setLgNaoEnviarMovimentos() throws Exception {
		List<AitMovimento> movimentos = this.aitMovimentoService.getByAit(this.aitMovimento.getCdAit());
		for(AitMovimento aitMovimento: movimentos) {
			if(aitMovimento.getLgEnviadoDetran() == LgEnviadoEnum.NAO_ENVIADO.getKey()) {
				aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
				aitMovimentoRepository.update(aitMovimento, customConnection);
			}
		}
	}
	
}
