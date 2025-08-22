package com.tivic.manager.wsdl.detran.mg.consultarplaca;

import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;

public class ConsultarPlacaDadosEntradaFactory {

	public static ConsultarPlacaDadosEntrada fazerDadoEntrada(String nrPlaca){
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		
		ConsultarPlacaDadosEntrada consultarPlacaDadosEntrada = new ConsultarPlacaDadosEntrada();
		consultarPlacaDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		consultarPlacaDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultarPlacaDadosEntrada.setPlaca(nrPlaca);
		consultarPlacaDadosEntrada.setNmServico("CONSULTAR PLACA");
		return consultarPlacaDadosEntrada;
	}
}
