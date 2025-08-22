package com.tivic.manager.wsdl.detran.mg.consultarmovimentacoes;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;

public class ConsultarMovimentacoesDadosEntradaFactory {

	public static ConsultarMovimentacoesDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject){
		
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Ait ait = aitDetranObject.getAit();
		
		ConsultarMovimentacoesDadosEntrada consultarMovimentacoesDadosEntrada = new ConsultarMovimentacoesDadosEntrada();
		consultarMovimentacoesDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		consultarMovimentacoesDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultarMovimentacoesDadosEntrada.setPlaca(ait.getNrPlaca());
		consultarMovimentacoesDadosEntrada.setAit(ait.getIdAit());
		consultarMovimentacoesDadosEntrada.setNmServico("CONSULTAR MOVIMENTACOES");
		return consultarMovimentacoesDadosEntrada;
		
	}

}
