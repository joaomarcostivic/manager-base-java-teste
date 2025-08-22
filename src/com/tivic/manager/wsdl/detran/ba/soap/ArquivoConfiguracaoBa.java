package com.tivic.manager.wsdl.detran.ba.soap;

import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;

public class ArquivoConfiguracaoBa implements ArquivoConfiguracao {

	private final String url = "http://gestaodeinfracoes.prodemge.gov.br/sram/webservice/soap/service";
	private final String prefixUriNameSpace = "ns1";
	private final String uriNameSpace = "http://gestaodeinfracoes.prodemge.gov.br";
	private final String prefixNameSpace = "ns2";
	private final String nameSpace = "SRAM";
	private final String cnpj = "18404780000109";
	private final String chave = "Q2TZq1Cn81ecRZlfWb1cud2WJ76qQ84C";
	
	public String getPrefixNameSpace() {
		return this.prefixNameSpace;
	}

	public String getNameSpace() {
		return this.nameSpace;
	}

	public String getPrefixNameSpaceUri() {
		return this.prefixUriNameSpace;
	}

	public String getNameSpaceUri() {
		return this.uriNameSpace;
	}

	public String getUrl() {
		return this.url;
	}

	public String getCnpj() {
		return this.cnpj;
	}

	public String getChave() {
		return this.chave;
	}
	
	public boolean getLgHomologacao(){
		return false;
	}

}
