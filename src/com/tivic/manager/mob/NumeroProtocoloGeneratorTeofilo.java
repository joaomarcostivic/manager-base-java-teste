package com.tivic.manager.mob;

import java.util.Calendar;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.CustomConnection;

public class NumeroProtocoloGeneratorTeofilo implements IGerarNumeroProtocoloGenerator {
	
	@Override
	public Documento generate(Documento documento, int cdAit, CustomConnection customConnection) throws Exception {
		String nrDocumento = documento.getNrDocumento();
		
		if(nrDocumento == null || nrDocumento.trim().equals(""))
			nrDocumento = createNrDocumento(documento, cdAit);
		
		documento.setNrDocumento(nrDocumento);
		nrDocumento = nrDocumento.replaceAll("[A-Za-z/-]", "");
		
		documento.setNrDocumentoExterno(Util.fillLong(Long.parseLong(nrDocumento), 16));
		return documento;
	}
	
	private String createNrDocumento(Documento documento, int cdAit) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(cdAit);
		stringBuilder.append(documento.getCdTipoDocumento());
		stringBuilder.append(documento.getDtProtocolo().get(Calendar.YEAR));
		return stringBuilder.toString();
	}

}
