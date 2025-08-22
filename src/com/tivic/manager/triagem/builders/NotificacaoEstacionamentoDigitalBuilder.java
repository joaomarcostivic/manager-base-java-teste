package com.tivic.manager.triagem.builders;

import java.util.GregorianCalendar;
import java.util.List;
import com.tivic.manager.triagem.dtos.ImagemNotificacaoDTO;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;

public class NotificacaoEstacionamentoDigitalBuilder {

	private NotificacaoEstacionamentoDigitalDTO notificacaoEstacionamentoDigitalDTO;
	
	public NotificacaoEstacionamentoDigitalBuilder() {
		notificacaoEstacionamentoDigitalDTO = new NotificacaoEstacionamentoDigitalDTO();
	}
	
	public NotificacaoEstacionamentoDigitalBuilder nmOrgaoAutuador(String nmOrgaoAutuador) {
		notificacaoEstacionamentoDigitalDTO.setNmOrgaoAutuador(nmOrgaoAutuador);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder nrNotificacao(String nrNotificacao) {
		notificacaoEstacionamentoDigitalDTO.setNrNotificacao(nrNotificacao);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder dtNotificacao(GregorianCalendar dtNotificacao) {
		notificacaoEstacionamentoDigitalDTO.setDtNotificacao(dtNotificacao);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder dsNotificacao(String dsNotificacao) {
		notificacaoEstacionamentoDigitalDTO.setDsNotificacao(dsNotificacao);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder nrPlaca(String nrPlaca) {
		notificacaoEstacionamentoDigitalDTO.setNrPlaca(nrPlaca);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder idDispositivo(String idDispositivo) {
		notificacaoEstacionamentoDigitalDTO.setIdDispositivo(idDispositivo);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder nrVaga(String nrVaga) {
		notificacaoEstacionamentoDigitalDTO.setNrVaga(nrVaga);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder nmRuaNotificacao(String nmRuaNotificacao) {
		notificacaoEstacionamentoDigitalDTO.setNmRuaNotificacao(nmRuaNotificacao);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder nrImovelReferencia(String nrImovelReferencia) {
		notificacaoEstacionamentoDigitalDTO.setNrImovelReferencia(nrImovelReferencia);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder idColaborador(String idColaborador) {
		notificacaoEstacionamentoDigitalDTO.setIdColaborador(idColaborador);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalBuilder imagemNotificacaoDTOList(List<ImagemNotificacaoDTO> imagemNotificacaoList) {
		notificacaoEstacionamentoDigitalDTO.setImagemNotificacaoDTOList(imagemNotificacaoList);
		return this;
	}
	
	public NotificacaoEstacionamentoDigitalDTO build() {
		return notificacaoEstacionamentoDigitalDTO;
	}
	
}
