package com.tivic.manager.wsdl.detran.mg.enviorenainf;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class EnvioRenainfDadosEntradaFactory {

	public static EnvioRenainfDadosEntrada fazerDadoEntrada(Ait ait, AitMovimento aitMovimento) throws ValidacaoException{
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		if(infracao == null)
			throw new ValidacaoException("Movimento sem ocorrência");
		
		EnvioRenainfDadosEntrada envioRenainfDadosEntrada = new EnvioRenainfDadosEntrada();
		envioRenainfDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		envioRenainfDadosEntrada.setOrgao(orgao.getCdOrgao());
		envioRenainfDadosEntrada.setAit(ait.getIdAit());
		envioRenainfDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		envioRenainfDadosEntrada.setCodigoMovimentacao(CodigoMovimentacaoRenainfEnum.valueOf(aitMovimento.getTpStatus()));
		envioRenainfDadosEntrada.setTipoProcessamento("E");//Envio
		envioRenainfDadosEntrada.setNmServico("ENVIO RENAINF");
		return envioRenainfDadosEntrada;
	}
	
}
