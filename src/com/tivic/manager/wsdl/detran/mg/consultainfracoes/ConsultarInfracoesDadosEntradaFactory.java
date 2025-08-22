package com.tivic.manager.wsdl.detran.mg.consultainfracoes;

import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.wsdl.AitDetranObject;

public class ConsultarInfracoesDadosEntradaFactory {

	public static ConsultarInfracoesDadosEntrada fazerDadoEntrada(AitDetranObject aitDetranObject) {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		DadosInfratorConsultaEntrada dadosInfratorConsultaEntrada = aitDetranObject.getDadosInfratorConsultaEntrada();
		ConsultarInfracoesDadosEntrada consultarInfracaoDadosEntrada = new ConsultarInfracoesDadosEntrada();
		consultarInfracaoDadosEntrada.setOrigemSolicitacao(3);
		consultarInfracaoDadosEntrada.setOrgao(orgao.getCdOrgao());
		consultarInfracaoDadosEntrada.setTipoConsulta(dadosInfratorConsultaEntrada.getTpDocumento());
		if(dadosInfratorConsultaEntrada.getTpDocumento() == TipoConsultaEnum.CNH.getKey()) {
			consultarInfracaoDadosEntrada.setCnh(dadosInfratorConsultaEntrada.getDocumumento());
		}
		else if(dadosInfratorConsultaEntrada.getTpDocumento() == TipoConsultaEnum.CPF.getKey()) {
			consultarInfracaoDadosEntrada.setCpf(dadosInfratorConsultaEntrada.getDocumumento());
		}
		consultarInfracaoDadosEntrada.setDataInicioConsulta(dadosInfratorConsultaEntrada.getDataInicioConsulta());
		consultarInfracaoDadosEntrada.setDataFinalConsulta(dadosInfratorConsultaEntrada.getDataFinalConsulta());
		consultarInfracaoDadosEntrada.setNmServico("CONSULTAR INFRACOES");
		return consultarInfracaoDadosEntrada;
	}
	
}
