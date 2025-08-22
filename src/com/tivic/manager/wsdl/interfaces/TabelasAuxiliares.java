package com.tivic.manager.wsdl.interfaces;

public interface TabelasAuxiliares {
	public int getTipoDocumento(int tpDocumentoBanco);
	public int getTipoCnh(int tpCnhBanco);
	public int getAssinatura(int lgAutoAssinado);
	public int getMovimento(int tpStatus);
	public int getPrazoMovimento(int tpStatus);
}
