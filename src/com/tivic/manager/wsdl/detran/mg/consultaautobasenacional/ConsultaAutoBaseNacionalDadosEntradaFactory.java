package com.tivic.manager.wsdl.detran.mg.consultaautobasenacional;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;

public class ConsultaAutoBaseNacionalDadosEntradaFactory {

	public static ConsultaAutoBaseNacionalDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject){
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Ait ait = aitDetranObject.getAit();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		
		ConsultaAutoBaseNacionalDadosEntrada consultaAutoBaseNacionalDadosEntrada = new ConsultaAutoBaseNacionalDadosEntrada();
		consultaAutoBaseNacionalDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultaAutoBaseNacionalDadosEntrada.setAit(ait.getIdAit());		
		consultaAutoBaseNacionalDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		consultaAutoBaseNacionalDadosEntrada.setNmServico("CONSULTA AUTO BASE NACIONAL");
		return consultaAutoBaseNacionalDadosEntrada;
	}
}
