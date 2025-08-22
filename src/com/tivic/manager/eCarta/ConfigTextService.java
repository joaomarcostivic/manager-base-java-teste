package com.tivic.manager.eCarta;

public class ConfigTextService {
	private static String servicoAdicional; 
	private static String arquivoSpool;
	private static String nomeSpool;
	
	public String getServicoAdicional() {
		return servicoAdicional;
	}
	public void setServicoAdicional(String servicoAdicional) {
		ConfigTextService.servicoAdicional = servicoAdicional;
	}
	public String getArquivoSpool() {
		return arquivoSpool;
	}
	public void setArquivoSpool(String arquivoSpool) {
		ConfigTextService.arquivoSpool = arquivoSpool;
	}
	public String getNomeSpool() {
		return nomeSpool;
	}
	public void setNomeSpool(String nomeSpool) {
		ConfigTextService.nomeSpool = nomeSpool;
	}
}
