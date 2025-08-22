package com.tivic.manager.wsdl.interfaces;

import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.detran.mg.IArquivoConfiguracaoMg;

public abstract class ServicoDetranRecurso {
	public IArquivoConfiguracaoMg arquivoConfiguracao;
	public abstract ServicoDetranObjeto executarExterno(ProtocoloExternoDetranObject protocoloExternoDetranObject);
	public void setArquivoConfiguracao(ArquivoConfiguracao arquivoConfiguracao) {
		this.arquivoConfiguracao = (IArquivoConfiguracaoMg) arquivoConfiguracao;
	}
}
