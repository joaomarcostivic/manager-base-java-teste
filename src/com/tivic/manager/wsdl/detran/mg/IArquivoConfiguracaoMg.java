package com.tivic.manager.wsdl.detran.mg;

import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;

public interface IArquivoConfiguracaoMg extends ArquivoConfiguracao {
	public String getPrefixNameSpace();
	public String getNameSpace();
	public String getPrefixNameSpaceUri();
	public String getNameSpaceUri();
	public String getUrl();
	public String getCnpj();
	public String getChave();
	public boolean getLgHomologacao();
	
}
