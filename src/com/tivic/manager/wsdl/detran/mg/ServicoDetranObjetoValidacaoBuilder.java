package com.tivic.manager.wsdl.detran.mg;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ServicoDetranObjetoValidacaoBuilder {

	private ServicoDetranObjeto servicoDetranObjeto;
	
	public ServicoDetranObjetoValidacaoBuilder(AitDetranObject aitDetranObject, String message) {
		this.servicoDetranObjeto = new ServicoDetranObjetoMG();
		servicoDetranObjeto.setAit(aitDetranObject.getAit());
		servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
		servicoDetranObjeto.setDadosRetorno(new DadosRetornoErroMG(-1, message));
	}
	
	public ServicoDetranObjeto build() {
		return this.servicoDetranObjeto;
	}
	
}
