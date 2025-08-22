package com.tivic.manager.mob.infracao.observacao;

import java.util.List;

import com.tivic.manager.mob.InfracaoObservacao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InfracaoObservacaoService implements IInfracaoObservacaoService{
	
	private InfracaoObservacaoRepository infracaoObservacaoRepository;
	
	public InfracaoObservacaoService() throws Exception {
		infracaoObservacaoRepository = (InfracaoObservacaoRepository) BeansFactory.get(InfracaoObservacaoRepository.class);
	}

	@Override
	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao) throws Exception {
		return getObservacaoByCdInfracao(cdInfracao, new CustomConnection());
	}

	@Override
	public List<InfracaoObservacao> getObservacaoByCdInfracao(int cdInfracao, CustomConnection customConnection) throws Exception {
		return infracaoObservacaoRepository.getObservacaoByCdInfracao(cdInfracao, customConnection);
	}

}
