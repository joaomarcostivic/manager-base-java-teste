package com.tivic.manager.mob.servlets;

public class ConsultaDadosEntradaFactory {

	public static ConsultaDadosEntrada create() {
		
		ArquivoConfiguracaoDetranServlet arquivoConfiguracaoDetranServlet = new ArquivoConfiguracaoDetranServlet();
		
		ConsultaDadosEntrada consultaDadosEntrada = new ConsultaDadosEntrada();
		consultaDadosEntrada.setCodigoUsuario(arquivoConfiguracaoDetranServlet.getCodigoUsuario());
		consultaDadosEntrada.setSenhaUsuario(arquivoConfiguracaoDetranServlet.getSenhaUsuario());
		consultaDadosEntrada.setCodigoOperacao(arquivoConfiguracaoDetranServlet.getCodigoOperacao());
		
		return consultaDadosEntrada;
		
	}
	
}
