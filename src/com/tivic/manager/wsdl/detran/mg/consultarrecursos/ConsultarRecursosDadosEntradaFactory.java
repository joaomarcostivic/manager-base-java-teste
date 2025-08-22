package com.tivic.manager.wsdl.detran.mg.consultarrecursos;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.consultarrecursos.ConsultarRecursosDadosEntrada;

public class ConsultarRecursosDadosEntradaFactory {

	public static ConsultarRecursosDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject){
		
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Ait ait = aitDetranObject.getAit();
		
		ConsultarRecursosDadosEntrada consultarRecursosDadosEntrada = new ConsultarRecursosDadosEntrada();
		consultarRecursosDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		consultarRecursosDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultarRecursosDadosEntrada.setPlaca(ait.getNrPlaca());
		consultarRecursosDadosEntrada.setAit(ait.getIdAit());
		consultarRecursosDadosEntrada.setNmServico("CONSULTAR RECURSOS");
		return consultarRecursosDadosEntrada;
		
	}

}
