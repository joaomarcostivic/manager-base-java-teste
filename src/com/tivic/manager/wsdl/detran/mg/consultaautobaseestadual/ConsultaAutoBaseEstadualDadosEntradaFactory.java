package com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;

public class ConsultaAutoBaseEstadualDadosEntradaFactory {
	
	public static ConsultaAutoBaseEstadualDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject){
		
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Ait ait = aitDetranObject.getAit();
		
		ConsultaAutoBaseEstadualDadosEntrada consultaAutoBaseEstadualDadosEntrada = new ConsultaAutoBaseEstadualDadosEntrada();
		consultaAutoBaseEstadualDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		consultaAutoBaseEstadualDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultaAutoBaseEstadualDadosEntrada.setPlaca(ait.getNrPlaca());
		consultaAutoBaseEstadualDadosEntrada.setAit(ait.getIdAit());
		consultaAutoBaseEstadualDadosEntrada.setNmServico("CONSULTA AUTO BASE ESTADUAL");
		return consultaAutoBaseEstadualDadosEntrada;
		
	}

}
