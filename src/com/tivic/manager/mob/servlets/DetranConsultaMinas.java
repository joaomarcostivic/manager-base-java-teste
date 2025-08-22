package com.tivic.manager.mob.servlets;


import javax.servlet.http.HttpServletRequest;

import com.tivic.manager.mob.ServicoDetranConsultaServices;
import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

import sol.util.RequestUtilities;

public class DetranConsultaMinas implements DetranConsulta {

	@Override
	public DadosRetornoBA send(HttpServletRequest request) throws Exception {
		String nrPlaca = RequestUtilities.getParameterAsString(request, "nrPlaca", RequestUtilities.getParameterAsString(request, "p", ""));
    	ServicoDetranConsultaServices servicoDetranServices = new ServicoDetranConsultaServices();
    	ServicoDetranObjeto servicoDetranObjeto = servicoDetranServices.consultarPlaca(nrPlaca, "BA");
    	int codigoRetorno = ((DadosRetornoBA)servicoDetranObjeto.getDadosRetorno()).getCodigoRetorno();
    	if(codigoRetorno != 0)
    		throw new Exception("Erro na consulta de Minas");
    	return (DadosRetornoBA)servicoDetranObjeto.getDadosRetorno();
	}
	
}
