package com.tivic.manager.ptc.documentotramitacao;

import java.util.GregorianCalendar;

import com.tivic.manager.ptc.DocumentoTramitacao;

public class SalvarTramitacaoBuilder extends TramitacaoBuilder {

	public SalvarTramitacaoBuilder(int cdDocumento) {
		super(cdDocumento);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocumentoTramitacao build() {
		 this.documentoTramitacao.setDtTramitacao(new GregorianCalendar());
		 this.documentoTramitacao.setTxtTramitacao("Registro do Documento.");
		 
		return documentoTramitacao;
	}
	
	

}
