package com.tivic.manager.wsdl.detran.mg.alterardatalimiterecurso;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AlterarDataLimiteRecursoDadosEntradaFactory implements IAlterarDataLimiteRecursoDadosEntradaFactory {
	
	public AlterarDataLimiteRecursoDadosEntrada fazerDadoEntrada(Ait ait) throws ValidacaoException{
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		if(infracao == null)
			throw new ValidacaoException("Movimento sem ocorrência");
		AlterarDataLimiteRecursoDadosEntrada alterarDataRecursoDadosEntrada = new AlterarDataLimiteRecursoDadosEntrada();
		alterarDataRecursoDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		alterarDataRecursoDadosEntrada.setOrgao(orgao.getCdOrgao());
		alterarDataRecursoDadosEntrada.setAit(ait.getIdAit());
		alterarDataRecursoDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		alterarDataRecursoDadosEntrada.setNovaDataLimiteRecurso(ait.getDtVencimento());
		return alterarDataRecursoDadosEntrada;
	}
	
}
