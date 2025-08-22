package com.tivic.manager.ptc.protocolos;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.ptc.protocolos.mg.ProtocoloRecursoFactoryMG;

public class ProtocoloRecursoFactory implements IProtocoloRecursoFactory{
	public static final String MG = "MG";
	public static final String BA = "BA";
	public static final String SP = "SP";
	
	public IProtocoloRecursoServices gerarServico(int tpStatus) throws Exception{
		switch(getSgEstadoAutuador()) {
			case MG: 
				return new ProtocoloRecursoFactoryMG().gerarServico(tpStatus);
			
			default:
				throw new Exception("Serviço não implementado");
		}
	}
	
	private String getSgEstadoAutuador() {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
		Estado estado = EstadoDAO.get(cidade.getCdEstado());
		return estado.getSgEstado();
	}
}
