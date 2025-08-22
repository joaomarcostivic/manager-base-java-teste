package com.tivic.manager.wsdl.interfaces;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.IArquivoConfiguracaoMg;

public abstract class ServicoDetran {
	public IArquivoConfiguracaoMg arquivoConfiguracao;
	public abstract ServicoDetranObjeto executar(AitDetranObject aitDetranObject);
	public void setArquivoConfiguracao(ArquivoConfiguracao arquivoConfiguracao) {
		this.arquivoConfiguracao = (IArquivoConfiguracaoMg) arquivoConfiguracao;
	}
}
