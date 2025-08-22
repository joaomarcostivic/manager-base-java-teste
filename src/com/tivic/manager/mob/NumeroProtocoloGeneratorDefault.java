package com.tivic.manager.mob;

import java.util.Calendar;

import com.tivic.manager.mob.aitmovimentodocumento.IAitMovimentoDocumentoService;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class NumeroProtocoloGeneratorDefault implements IGerarNumeroProtocoloGenerator{
	private IAitMovimentoDocumentoService aitMovimentoDocumentoService;
	private int cdAit;
	
	public NumeroProtocoloGeneratorDefault() throws Exception {
		aitMovimentoDocumentoService = (IAitMovimentoDocumentoService) BeansFactory.get(IAitMovimentoDocumentoService.class);
	}
	
	@Override
	public Documento generate(Documento documento, int cdAit, CustomConnection customConnection) throws Exception {
		String nrDocumento = documento.getNrDocumento();
		this.cdAit = cdAit;
		
		if(nrDocumento == null || nrDocumento.trim().equals(""))
			nrDocumento = com.tivic.manager.gpn.TipoDocumentoServices.getNumeracaoProtocolo(documento.getCdTipoDocumento(), customConnection.getConnection());
		
		documento.setNrDocumento(nrDocumento);
		nrDocumento = nrDocumento.replaceAll("[A-Za-z/-]", "");
		
		documento.setNrDocumentoExterno(Util.fillLong(Long.parseLong(nrDocumento), 16));
		return documento;
	}
	
	private String createNrDocumento(Documento documento) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.cdAit);
		stringBuilder.append(documento.getCdTipoDocumento());
		stringBuilder.append(documento.getDtProtocolo().get(Calendar.YEAR));
		return stringBuilder.toString();
	}

	private int getCdAitByDocumento(int cdDocumento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_documento", cdDocumento);
		return aitMovimentoDocumentoService.getAitByDocumento(searchCriterios);
	}
}
