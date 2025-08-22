package com.tivic.manager.fix.mob.ait.infracao;

public interface IFixInfracaoAitService {

	public void corrigirInfracaoAit(int cdInfracaoAntigo, int cdInfracaoNovo, String dtInfracao) throws Exception;
	public void corrigirEventoAit(int cdLoteImpressao) throws Exception;

}
