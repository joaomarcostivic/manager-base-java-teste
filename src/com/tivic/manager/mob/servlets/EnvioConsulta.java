package com.tivic.manager.mob.servlets;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;

public class EnvioConsulta {

	public static DadosRetornoBA consultar(HttpServletRequest request, List<DetranConsulta> lista) {
		DadosRetornoBA dadosRetornoBa = null;
		for(DetranConsulta detranConsulta : lista) {
			try {
				dadosRetornoBa = detranConsulta.send(request);
				break;
			} catch(Exception e) {}
		}
		return dadosRetornoBa;
	}
	
}
