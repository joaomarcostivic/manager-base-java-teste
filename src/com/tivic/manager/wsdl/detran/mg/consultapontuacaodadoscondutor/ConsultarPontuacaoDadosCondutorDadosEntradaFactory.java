package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;

public class ConsultarPontuacaoDadosCondutorDadosEntradaFactory {

	public static ConsultarPontuacaoDadosCondutorDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject) {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		DadosCondutorDocumentoEntrada dadosCondutorDocumentoEntrada = aitDetranObject.getDadosCondutorDocumentoEntrada();
		ConsultarPontuacaoDadosCondutorDadosEntrada consultarDadosCondutorDadosEntrada = new ConsultarPontuacaoDadosCondutorDadosEntrada();
		consultarDadosCondutorDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		consultarDadosCondutorDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultarDadosCondutorDadosEntrada.setDocumento(dadosCondutorDocumentoEntrada.getDocumumento());
		consultarDadosCondutorDadosEntrada.setTipoDocumento(dadosCondutorDocumentoEntrada.getTpDocumento());
		consultarDadosCondutorDadosEntrada.setNmServico("CONSULTAR PONTUACAO E DADOS DO CONDUTOR");
		return consultarDadosCondutorDadosEntrada;
	}
	
}
