package com.tivic.manager.triagem.builders;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.eventoequipamento.builders.EventoEquipamentoBuilder;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.util.date.DateUtil;

public class EventoEquipamentoTriagemBuilder {
	
	private EventoEquipamento eventoEquipamento;
	private IParametroRepository parametroRepository;
	
	public EventoEquipamentoTriagemBuilder() throws Exception {
		eventoEquipamento = new EventoEquipamento();
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	public EventoEquipamentoTriagemBuilder notificacaoEstacionamentoDigital(NotificacaoEstacionamentoDigitalDTO notificacaoEstacionamentoDigitalDTO) throws Exception {
		eventoEquipamento = new EventoEquipamentoBuilder()
				.cdTipoEvento(parametroRepository.getValorOfParametroAsInt("MOB_EVENTO_ESTACIONAMENTO_DIGITAL"))
				.cdEquipamento(parametroRepository.getValorOfParametroAsInt("CD_EQUIPAMENTO_ESTACIONAMENTO_DIGITAL"))
				.dtEvento(DateUtil.getDataAtual())
				.dtConclusao(notificacaoEstacionamentoDigitalDTO.getDtNotificacao())
				.dsLocal(notificacaoEstacionamentoDigitalDTO.getNmRuaNotificacao())
				.nmOrgaoAutuador(parametroRepository.getValorOfParametroAsString("NM_ORGAO"))
				.nrPlaca(notificacaoEstacionamentoDigitalDTO.getNrPlaca())
				.build();
		return this;
	}
	
	public EventoEquipamento build() {
		return eventoEquipamento;
	}

}
