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

public class GeraDocumentoDefesa extends MovimentoDocumentoHandler {
	
	private List<AitMovimento> aitMovimentoList;
	private int cdAit;
	private CustomConnection customConnection;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;	
	private IAitMovimentoDocumentoService aitMovimentoDocumentoService;
	
	public GeraDocumentoDefesa(List<AitMovimento> aitMovimentoList, int cdAit, CustomConnection customConnection) throws Exception {
		this.aitMovimentoDocumentoService = (IAitMovimentoDocumentoService) BeansFactory.get(IAitMovimentoDocumentoService.class);
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		this.aitMovimentoList = aitMovimentoList;
		this.cdAit = cdAit;
		this.customConnection = customConnection;
	}

	@Override
	public void gerar() throws Exception {
		List<AitMovimento> aitMovimentoDefesaList = new ArrayList<AitMovimento>();
		
		for(AitMovimento movimento: this.aitMovimentoList) {
			if(lgStatusDefesa(movimento)) 
				aitMovimentoDefesaList.add(movimento);
		}
		
		if(aitMovimentoDefesaList.isEmpty()) {			
			nextGenerator.gerar();
		} else {
			processarDocumento(aitMovimentoDefesaList);
		}
	}
	
	private boolean lgStatusDefesa(AitMovimento aitMovimento) {
		List<Integer> tpMovimento = new ArrayList<Integer>();
		tpMovimento.add(AitMovimentoServices.DEFESA_PREVIA);
		tpMovimento.add(AitMovimentoServices.DEFESA_DEFERIDA);
		tpMovimento.add(AitMovimentoServices.DEFESA_INDEFERIDA);	
		return tpMovimento.contains(aitMovimento.getTpStatus()) ? true : false;
	}
	
	private void processarDocumento(List<AitMovimento> aitMovimentoDefesaList) throws Exception {
		AitMovimento aitMovimentoDefesa = new AitMovimento();
		
		for(AitMovimento movimentoDefesa: aitMovimentoDefesaList) {
			switch(movimentoDefesa.getTpStatus()) {
				case  AitMovimentoServices.DEFESA_INDEFERIDA:
					aitMovimentoDefesa = movimentoDefesa;
					break;
				case AitMovimentoServices.DEFESA_DEFERIDA:
					aitMovimentoDefesa = movimentoDefesa;
					break;
				case AitMovimentoServices.DEFESA_PREVIA:
					aitMovimentoDefesa = movimentoDefesa;
			}
		}
		aitMovimentoDefesa.setCdAit(this.cdAit);
		new GeradorMovimentoDocumentoFactory().factory(aitMovimentoDefesa, this.customConnection).build();
		if(aitMovimentoDefesa.getTpStatus() == AitMovimentoServices.DEFESA_DEFERIDA
		 ||aitMovimentoDefesa.getTpStatus() == AitMovimentoServices.DEFESA_INDEFERIDA) {
			this.documentoOcorrenciaRepository.insert(setOcorrencia(aitMovimentoDefesa), this.customConnection);
		}
		nextGenerator.gerar();
	}
	
	private DocumentoOcorrencia setOcorrencia(AitMovimento aitMovimentoDefesa) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitMovimentoDefesa.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("cd_movimento", aitMovimentoDefesa.getCdMovimento(), true);
		AitMovimentoDocumento AitMovimentoDocumento = this.aitMovimentoDocumentoService.getMovimentoDocumento(searchCriterios, this.customConnection);
		
		DocumentoOcorrencia ocorrencia = new DocumentoOcorrenciaBuilder()
				.setDocumento(AitMovimentoDocumento.getCdDocumento())
				.setCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1))
				.setDtOcorrencia(new GregorianCalendar())
				.setTxtOcorrencia("Import")
				.setCdTipoOcorrencia(tpStatusMap(aitMovimentoDefesa))
				.build();
		return ocorrencia;
	}
	
	private int tpStatusMap(AitMovimento aitMovimentoDefesa) {
		int tipoOcorrencia = 0;
		switch (aitMovimentoDefesa.getTpStatus()) {
			case AitMovimentoServices.DEFESA_INDEFERIDA:
				return tipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INDEFERIDA", 0);
			case AitMovimentoServices.DEFESA_DEFERIDA:
				return tipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEFERIDA", 0);
		}
		return tipoOcorrencia;
	}
}