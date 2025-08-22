package com.tivic.manager.triagem.app;

import java.util.List;

public interface IEstacionamentoAppSyncService {
	
	List<EstacionamentoDigitalAppResponse> findNotificacoesPendentes() throws Exception;

}
