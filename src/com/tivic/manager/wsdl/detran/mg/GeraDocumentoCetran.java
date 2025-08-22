package com.tivic.manager.wsdl.detran.mg;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.aitmovimentodocumento.IAitMovimentoDocumentoService;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.manager.ptc.protocolosv3.resultado.builders.DocumentoOcorrenciaBuilder;
import com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento.GeradorMovimentoDocumentoFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraDocumentoCetran extends MovimentoDocumentoHandler{

	private List<AitMovimento> aitMovimentoList;
	private int cdAit;
	private CustomConnection customConnection;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;	
	private IAitMovimentoDocumentoService aitMovimentoDocumentoService;
	
	public GeraDocumentoCetran(List<AitMovimento> aitMovimentoList, int cdAit, CustomConnection customConnection) throws Exception {
		this.aitMovimentoDocumentoService = (IAitMovimentoDocumentoService) BeansFactory.get(IAitMovimentoDocumentoService.class);
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		this.aitMovimentoList = aitMovimentoList;
		this.cdAit = cdAit;
		this.customConnection = customConnection;
	}
	
	@Override
	public void gerar() throws Exception {
		List<AitMovimento> aitMovimentoCetranList = new ArrayList<AitMovimento>();
		
		for(AitMovimento movimento: this.aitMovimentoList) {
			if(lgStatusCetran(movimento)) 
				aitMovimentoCetranList.add(movimento);
		}
		
		if(aitMovimentoCetranList.isEmpty()) {			
			nextGenerator.gerar();
		} else {
			processarDocumento(aitMovimentoCetranList);
		}
	}
	
	private boolean lgStatusCetran(AitMovimento aitMovimento) {
		List<Integer> tpMovimento = new ArrayList<Integer>();
		tpMovimento.add(AitMovimentoServices.CETRAN_INDEFERIDO);
		tpMovimento.add(AitMovimentoServices.CETRAN_DEFERIDO);
		tpMovimento.add(AitMovimentoServices.RECURSO_CETRAN);	
		return tpMovimento.contains(aitMovimento.getTpStatus()) ? true : false;
	}
	
	private void processarDocumento(List<AitMovimento> aitMovimentoCetranList) throws Exception {
		AitMovimento aitMovimentoCetran = new AitMovimento();
	
		for (AitMovimento movimentoCetran: aitMovimentoCetranList) {
			switch(movimentoCetran.getTpStatus()) {
				case  AitMovimentoServices.CETRAN_INDEFERIDO:
					aitMovimentoCetran = movimentoCetran;
					break;
				case AitMovimentoServices.CETRAN_DEFERIDO:
					aitMovimentoCetran = movimentoCetran;
					break;
				case AitMovimentoServices.RECURSO_CETRAN:
					aitMovimentoCetran = movimentoCetran;
			}
		}
		aitMovimentoCetran.setCdAit(this.cdAit);
		new GeradorMovimentoDocumentoFactory().factory(aitMovimentoCetran, this.customConnection).build();
		if(aitMovimentoCetran.getTpStatus() == AitMovimentoServices.CETRAN_INDEFERIDO
		 ||aitMovimentoCetran.getTpStatus() == AitMovimentoServices.CETRAN_DEFERIDO) {
			this.documentoOcorrenciaRepository.insert(setOcorrencia(aitMovimentoCetran), this.customConnection);
		}
		nextGenerator.gerar();
	}
	
	private DocumentoOcorrencia setOcorrencia(AitMovimento aitMovimentoCetran) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitMovimentoCetran.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("cd_movimento", aitMovimentoCetran.getCdMovimento(), true);
		AitMovimentoDocumento AitMovimentoDocumento = this.aitMovimentoDocumentoService.getMovimentoDocumento(searchCriterios, this.customConnection);
		
		DocumentoOcorrencia ocorrencia = new DocumentoOcorrenciaBuilder()
				.setDocumento(AitMovimentoDocumento.getCdDocumento())
				.setCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1))
				.setDtOcorrencia(new GregorianCalendar())
				.setTxtOcorrencia("Import")
				.setCdTipoOcorrencia(tpStatusMap(aitMovimentoCetran))
				.build();
		return ocorrencia;
	}
	
	private int tpStatusMap(AitMovimento aitMovimentoCetran) {
		int tipoOcorrencia = 0;
		switch (aitMovimentoCetran.getTpStatus()) {
			case AitMovimentoServices.CETRAN_INDEFERIDO:
				return tipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INDEFERIDA", 0);
			case AitMovimentoServices.CETRAN_DEFERIDO:
				return tipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEFERIDA", 0);
		}
		return tipoOcorrencia;
	}
}
