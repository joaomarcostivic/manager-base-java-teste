package com.tivic.manager.mob.servlets;


import javax.servlet.http.HttpServletRequest;

import com.tivic.manager.bdv.Veiculo;
import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;

import sol.util.RequestUtilities;

public class DetranConsultaBahia implements DetranConsulta {

	BdvService bdvService;
	
	public DetranConsultaBahia() {
		bdvService = new BdvService();
	}
	
	@Override
	public DadosRetornoBA send(HttpServletRequest request) throws Exception {
		ConsultaDadosEntrada consultaDadosEntrada = createDadosEntrada(request);
		XMLFactory xmlFactory = new XMLFactory(consultaDadosEntrada);
    	ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno = xmlFactory.enviarDados();
    	sendBdv(consultarPlacaDadosRetorno);	
    	return consultarPlacaDadosRetorno;
	}
	

	private ConsultaDadosEntrada createDadosEntrada(HttpServletRequest request) {
		int tpBusca = RequestUtilities.getParameterAsInteger(request, "tp", 0);
    	String nrPlaca = RequestUtilities.getParameterAsString(request, "nrPlaca", RequestUtilities.getParameterAsString(request, "p", ""));
    	String sgUf = RequestUtilities.getParameterAsString(request, "sgUf", "BA");
    	String idModulo = RequestUtilities.getParameterAsString(request, "idModulo", "detran");
    	String idOrgao = RequestUtilities.getParameterAsString(request, "o", "");
    	ConsultaDadosEntrada consultaDadosEntrada = ConsultaDadosEntradaFactory.create();
    	consultaDadosEntrada.setNrPlaca(nrPlaca);
    	if(tpBusca==1)
    		consultaDadosEntrada.setSgUf(sgUf);
    	consultaDadosEntrada.setIdModulo(idModulo);
    	consultaDadosEntrada.setIdOrgao(idOrgao);
    	return consultaDadosEntrada;
	}
	
	private void sendBdv(ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno) {
		try {
			Veiculo veiculo = createVeiculo(consultarPlacaDadosRetorno);
			bdvService.inserirVeiculo(veiculo);
		} catch(Exception e) {
			System.out.println("Erro ao inserir veículo no BDV: " + e.getMessage());
		}
		
	}
	
	private Veiculo createVeiculo(ConsultarPlacaDadosRetorno consultarPlacaDadosRetorno){
		Veiculo veiculo = new Veiculo();
		veiculo.setNrPlaca(consultarPlacaDadosRetorno.getPlacaVeiculo());
		veiculo.setNrRenavan(consultarPlacaDadosRetorno.getParamRenavam());
		veiculo.setNrAnoModelo(consultarPlacaDadosRetorno.getAnoModelo());
		veiculo.setNrAnoFabricacao(consultarPlacaDadosRetorno.getAnoFabricacao());
		veiculo.setNmMunicipio(consultarPlacaDadosRetorno.getNomeMunicipio());
		veiculo.setSgEstado(consultarPlacaDadosRetorno.getUnidadeFederacao());
		veiculo.setNmMarcaModelo(consultarPlacaDadosRetorno.getMarcaModelo());
		veiculo.setNmCor(consultarPlacaDadosRetorno.getCor());
		veiculo.setNmEspecie(consultarPlacaDadosRetorno.getEspecie());
		veiculo.setNmTipo(consultarPlacaDadosRetorno.getTipoVeiculo());
		veiculo.setNmCategoria(consultarPlacaDadosRetorno.getCategoriaVeiculo());
		return veiculo;
	}


}
