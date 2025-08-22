package com.tivic.manager.wsdl.detran.mg;

public abstract class MovimentoDocumentoHandler {
	
	protected MovimentoDocumentoHandler nextGenerator;
	
	public void setNextGenerator(MovimentoDocumentoHandler nextGenerator) {
		this.nextGenerator = nextGenerator;
	}
	
	public abstract void gerar() throws Exception; 
}
