package com.tivic.manager.fix.mob.ait.cidade;

import java.util.List;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IFixCidadeService {
	public void corrigirCidadeUf(String dtInfracao, List<Integer> cdsLoteImpressao, List<Integer> cdAit) throws Exception, ValidacaoException ;
}
