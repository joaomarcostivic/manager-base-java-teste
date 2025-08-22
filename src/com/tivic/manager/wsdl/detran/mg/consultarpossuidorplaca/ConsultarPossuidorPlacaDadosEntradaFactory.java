package com.tivic.manager.wsdl.detran.mg.consultarpossuidorplaca;

import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;

public class ConsultarPossuidorPlacaDadosEntradaFactory {

	public static ConsultarPossuidorPlacaDadosEntrada fazerDadoEntrada(String nrPlaca){
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		
		ConsultarPossuidorPlacaDadosEntrada consultarPossuidorPlacaDadosEntrada = new ConsultarPossuidorPlacaDadosEntrada();
		consultarPossuidorPlacaDadosEntrada.setOrigemSolicitacao(3);
		consultarPossuidorPlacaDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultarPossuidorPlacaDadosEntrada.setPlaca(nrPlaca);
		consultarPossuidorPlacaDadosEntrada.setNmServico("CONSULTAR POSSUIDOR PLACA");
		return consultarPossuidorPlacaDadosEntrada;
	}
}
