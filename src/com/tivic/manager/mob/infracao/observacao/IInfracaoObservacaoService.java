package com.tivic.manager.mob.infracao.observacao;

import java.util.List;

import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.sol.connection.CustomConnection;

public interface IInfracaoObservacaoService {

	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao) throws Exception;
	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao, CustomConnection customConnection) throws Exception;
}
