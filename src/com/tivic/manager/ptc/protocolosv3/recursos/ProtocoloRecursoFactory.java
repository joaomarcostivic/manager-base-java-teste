package com.tivic.manager.ptc.protocolosv3.recursos;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.sol.cdi.BeansFactory;

public class ProtocoloRecursoFactory implements IProtocoloRecursoFactory {

	public static final String MG = "MG";
	public static final String BA = "BA";
	public static final String SP = "SP";
	
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	
	public ProtocoloRecursoFactory () throws Exception {
		cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
	}
	
	
	@Override
	public IProtocoloRecursoServices gerarServico(int tpStatus) throws Exception {
		switch(getSgEstadoAutuador()) {
		case MG: 
			return new ProtocoloRecursoFactoryMG().gerarServico(tpStatus);
		
		default:
			throw new Exception("Serviço não implementado");
		}	
	}
	

	private String getSgEstadoAutuador() throws Exception{
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = cidadeRepository.get(orgao.getCdCidade());
		Estado estado = estadoRepository.get(cidade.getCdEstado());
		return estado.getSgEstado();
	}


}
