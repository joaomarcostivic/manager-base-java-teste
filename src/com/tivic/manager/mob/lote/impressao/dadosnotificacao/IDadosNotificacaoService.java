package com.tivic.manager.mob.lote.impressao.dadosnotificacao;

import java.util.List;

import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.sol.connection.CustomConnection;

public interface IDadosNotificacaoService {
	List<DadosNotificacao> buscarDadosNAI(int cdAit, CustomConnection customConnection) throws Exception;
	List<DadosNotificacao> buscarDadosNIP(int cdAit, CustomConnection customConnection) throws Exception;
}
