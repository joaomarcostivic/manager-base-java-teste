package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.grl.parametro.repository.ParametroRepositoryDAO;

public class BuscaMensagemEmail {
	
	private String comprovante;
	private String deferido;
	private String indeferido;
	
	private ParametroRepositoryDAO parametroRepository;
	
	public BuscaMensagemEmail() {
		parametroRepository = new ParametroRepositoryDAO();
		setDefaultMessage();
	}

	public String buscar(String template) throws Exception {
		return setMensagem(template);
	}
	
	private String setMensagem(String template) throws Exception {
		String mensagem = "";
		switch(template) {
			case "PTC_TEMPLATE_COMPROVANTE_CREDENCIAL_DEFICIENTE":
				mensagem = parametroRepository.getValorAsStringWithCustomDb(template) != null 
						&& !parametroRepository.getValorAsStringWithCustomDb(template).equals("0")
						 ? parametroRepository.getValorAsStringWithCustomDb(template)
						 : comprovante;
				break;
			case "PTC_TEMPLATE_DEFERIMENTO_CREDENCIAL_DEFICIENTE":
				mensagem = parametroRepository.getValorAsStringWithCustomDb(template) != null 
						&& !parametroRepository.getValorAsStringWithCustomDb(template).equals("0")
				 		 ? parametroRepository.getValorAsStringWithCustomDb(template)
				 		 : deferido;
				break;
			case "PTC_TEMPLATE_INDEFERINEMTO_CREDENCIAL_DEFICIENTE":
				mensagem = parametroRepository.getValorAsStringWithCustomDb(template) != null 
						&& !parametroRepository.getValorAsStringWithCustomDb(template).equals("0")
		 		 		 ? parametroRepository.getValorAsStringWithCustomDb(template)
		 		 		 : indeferido;
				break;
		}
		return mensagem;
	}
	
	private void setDefaultMessage() {
		comprovante = "Sua solicitação de credencial de estacionamento está em processo de análise e "
					+ "você receberá um e-mail assim que for concluído.";
		
		deferido = "Informamos que sua solicitação de Credencial de Estacionamento foi DEFERIDA. "
				 + "Procure o Órgão responsável para a impressão da sua credencial.";
		
		indeferido = "Sua solicitação de Credencial de Estacionamento foi INDEFERIDA "
				   + "devido ao não cumprimento de todos os requisitos obrigatórios.";
	}
}
