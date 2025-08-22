package com.tivic.manager.mob.servlets;

import javax.servlet.http.HttpServletRequest;

import com.tivic.manager.bdv.Veiculo;
import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;

import sol.util.RequestUtilities;

public class DetranConsultaBdv implements DetranConsulta {

	BdvService bdvService;
	
	public DetranConsultaBdv() {
		bdvService = new BdvService();
	}

	@Override
	public DadosRetornoBA send(HttpServletRequest request) throws Exception {
		String nrPlaca = RequestUtilities.getParameterAsString(request, "nrPlaca", RequestUtilities.getParameterAsString(request, "p", ""));
		Veiculo veiculo = bdvService.consultaVeiculo(nrPlaca);
		return (DadosRetornoBA) createRetorno(nrPlaca, veiculo);
	}
	

	private ConsultarPlacaDadosRetorno createRetorno(String nrPlaca, Veiculo veiculo){
		ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = new ConsultarPlacaDadosRetorno();
		consultarPlacaDadosRetorno.setPlacaVeiculo(veiculo.getNrPlaca());
		consultarPlacaDadosRetorno.setParamPlaca(nrPlaca);
		consultarPlacaDadosRetorno.setParamRenavam(veiculo.getNrRenavan());
		consultarPlacaDadosRetorno.setAnoModelo(veiculo.getNrAnoModelo());
		consultarPlacaDadosRetorno.setAnoFabricacao(veiculo.getNrAnoFabricacao());
		consultarPlacaDadosRetorno.setNomeMunicipio(veiculo.getNmMunicipio());
		consultarPlacaDadosRetorno.setUnidadeFederacao(veiculo.getSgEstado());
		consultarPlacaDadosRetorno.setMarcaModelo(veiculo.getNmMarcaModelo());
		consultarPlacaDadosRetorno.setCor(veiculo.getNmCor());
		consultarPlacaDadosRetorno.setEspecie(veiculo.getNmEspecie());
		consultarPlacaDadosRetorno.setTipoVeiculo(veiculo.getNmTipo());
		consultarPlacaDadosRetorno.setCategoriaVeiculo(veiculo.getNmCategoria());
		return consultarPlacaDadosRetorno;
	}

}
