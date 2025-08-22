package com.tivic.manager.triagem.webclients.estacionamento_digital.apis;

import java.util.HashMap;
import java.util.List;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;

public interface IEstacionamentoDigitalAPI {
	List<NotificacaoEstacionamentoDigitalDTO> buscarNotificacoes() throws Exception;
	List<NotificacaoEstacionamentoDigitalDTO> buscarNotificacoes(HashMap<String, String> hashMap) throws Exception;
}
