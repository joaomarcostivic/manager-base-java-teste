package com.tivic.manager.mob;

import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.protocolosv3.gerardornumerodocumento.GeneratorNrDocumento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class NumeroProtocoloGenerator implements IGerarNumeroProtocoloGenerator {
	private final GeneratorNrDocumento generatorNrDocumento;

	public NumeroProtocoloGenerator() throws Exception {
		generatorNrDocumento = new GeneratorNrDocumento();
    }
	
 	@Override
	public Documento generate(Documento documento, int cdAit, CustomConnection customConnection) throws ValidacaoException, Exception {
		String nrDocumento = documento.getNrDocumento();
		if(nrDocumento == null || nrDocumento.trim().isEmpty()) {
			nrDocumento = generatorNrDocumento.gerarNumero(documento.getCdTipoDocumento(), customConnection);
	        documento.setNrDocumento(nrDocumento);
	 		documento.setNrDocumentoExterno(nrDocumento);
	 		return documento;
		}
		return null;
	}
}
