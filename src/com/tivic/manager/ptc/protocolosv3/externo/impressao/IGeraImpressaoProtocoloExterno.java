package com.tivic.manager.ptc.protocolosv3.externo.impressao;

public interface IGeraImpressaoProtocoloExterno {

	public byte[] gerar(int cdDocumentoExterno, int cdDocumento) throws Exception;
	
}
