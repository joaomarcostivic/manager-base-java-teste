package com.tivic.manager.mob.servlets;

import javax.servlet.http.HttpServletRequest;

import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;

public interface DetranConsulta {
	DadosRetornoBA send(HttpServletRequest request) throws Exception;
}
