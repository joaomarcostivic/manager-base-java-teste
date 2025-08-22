package com.tivic.manager.ptc.documentotramitacao;

import java.util.GregorianCalendar;

import com.tivic.manager.ptc.DocumentoTramitacao;

public class DeferimentoTramitacaoBuilder extends TramitacaoBuilder {

	public DeferimentoTramitacaoBuilder(int cdDocumento) {
		super(cdDocumento);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocumentoTramitacao build() {
		this.documentoTramitacao.setDtTramitacao(new GregorianCalendar());
		this.documentoTramitacao.setTxtTramitacao("Deferido sem necessidade de perícia, solicitação enviada para ATUV.");
		
		return documentoTramitacao;
	}

}
