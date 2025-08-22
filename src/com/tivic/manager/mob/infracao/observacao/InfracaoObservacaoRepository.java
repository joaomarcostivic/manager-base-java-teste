package com.tivic.manager.mob.infracao.observacao;

import java.util.List;

import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.sol.connection.CustomConnection;

public interface InfracaoObservacaoRepository {
	public List<InfracaoObservacao> getAll() throws Exception;
	public List<InfracaoObservacao> getAll(CustomConnection customConnection) throws Exception;
	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao) throws Exception;
	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao, CustomConnection customConnection) throws Exception;
}
