package com.tivic.manager.wsdl;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.detran.ba.ServicoDetranServicesBA;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranServicesMG;
import com.tivic.manager.wsdl.detran.sp.ServicoDetranServicesSP;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;

public class ServicoDetranServicesFactory {

	public static final String MG = "MG";
	public static final String BA = "BA";
	public static final String SP = "SP";
	public static final String PA = "PA";
	

	public static ServicoDetranServices gerarServico() throws Exception{
		
		String sgEstado = getSgEstadoOrgao();
		
		ServicoDetranServices servicoDetranServices = null;
		switch(sgEstado){
			case MG:
				servicoDetranServices = new ServicoDetranServicesMG();
			break;
			case BA:
				servicoDetranServices = new ServicoDetranServicesBA();
			break;
			case SP:
			case PA:
				servicoDetranServices = new ServicoDetranServicesSP();
			break;
		}
		return servicoDetranServices;
	}
	
	

	public static String getSgEstadoOrgao(){
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidadeOrgao = CidadeDAO.get(orgao.getCdCidade());
		Estado estadoOrgao = EstadoDAO.get(cidadeOrgao.getCdEstado());
		return estadoOrgao.getSgEstado();
	}
	
}
