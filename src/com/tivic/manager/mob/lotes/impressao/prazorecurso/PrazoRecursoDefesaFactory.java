package com.tivic.manager.mob.lotes.impressao.prazorecurso;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class PrazoRecursoDefesaFactory implements IPrazoRecurso {
	
	public static final String MG = "MG";
	public static final String BA = "BA";
	public static final String SP = "SP";
	
	@Override
	public GregorianCalendar gerarPrazo(int cdAit, CustomConnection customConnection) throws Exception {
		String sgEstado = EstadoDAO.get(CidadeDAO.get(OrgaoServices.getOrgaoUnico().getCdCidade()).getCdEstado()).getSgEstado();
		switch (sgEstado) {
			case BA:
				return new PrazoRecursoDefesaBA().gerarPrazo(cdAit, customConnection);
			case MG:
				return new PrazoRecursoDefesaMG().gerarPrazo(cdAit, customConnection);
			default: 
				throw new ValidacaoException("Estado não identificado para gerar prazo de recurso.");
		}
	}
}
