package com.tivic.manager.mob.processamento.conversao.factories.infracao;

import com.tivic.manager.mob.Infracao;
import com.tivic.sol.connection.CustomConnection;

public interface InfracaoStrategy {
	Infracao getInfracao(CustomConnection customConnection) throws Exception;
}
