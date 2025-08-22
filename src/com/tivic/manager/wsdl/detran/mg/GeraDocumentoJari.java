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
import com.tivic.manager.ptc.protocolosv3.documento.ata.Ata;
import com.tivic.manager.ptc.protocolosv3.documento.ata.AtaBuilder;
import com.tivic.manager.ptc.protocolosv3.documento.ata.IAtaRepository;
import com.tivic.manager.ptc.protocolosv3.recursos.IRecursoRepository;
import com.tivic.manager.ptc.protocolosv3.recursos.Recurso;
import com.tivic.manager.ptc.protocolosv3.resultado.builders.DocumentoOcorrenciaBuilder;
import com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento.GeradorMovimentoDocumentoFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraDocumentoJari extends MovimentoDocumentoHandler {
	
	private List<AitMovimento> aitMovimentoList;
	private int cdAit;
	private CustomConnection customConnection;
	private IAtaRepository ataRepository;
	private IAitMovimentoDocumentoService aitMovimentoDocumentoService;
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;
	private IRecursoRepository recursoRepository;
	private DocumentoOcorrencia documentoOcorrencia;
	
	public GeraDocumentoJari(List<AitMovimento> aitMovimentoList, int cdAit, CustomConnection customConnection) throws Exception {
		this.ataRepository = (IAtaRepository) BeansFactory.get(IAtaRepository.class);
		this.aitMovimentoDocumentoService = (IAitMovimentoDocumentoService) BeansFactory.get(IAitMovimentoDocumentoService.class);
		this.documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
		this.recursoRepository = (IRecursoRepository) BeansFactory.get(IRecursoRepository.class);
		this.aitMovimentoList = aitMovimentoList;
		this.cdAit = cdAit;
		this.customConnection = customConnection;
		this.documentoOcorrencia = new DocumentoOcorrencia();
	}

	@Override
	public void gerar() throws Exception {
		List<AitMovimento> aitMovimentoJariList = new ArrayList<AitMovimento>();
		
		for(AitMovimento movimento: this.aitMovimentoList) {
			if(lgStatusJari(movimento)) 
				aitMovimentoJariList.add(movimento);
		}
		if(aitMovimentoJariList.isEmpty()) {
			nextGenerator.gerar();
		} else {
			processarDocumento(aitMovimentoJariList);			
		}
	}
	
	private boolean lgStatusJari(AitMovimento aitMovimento) {
		List<Integer> tpMovimento = new ArrayList<Integer>();
		tpMovimento.add(AitMovimentoServices.JARI_SEM_PROVIMENTO);
		tpMovimento.add(AitMovimentoServices.JARI_COM_PROVIMENTO);
		tpMovimento.add(AitMovimentoServices.RECURSO_JARI);	
		return tpMovimento.contains(aitMovimento.getTpStatus()) ? true : false;
	}
	
	private void processarDocumento(List<AitMovimento> aitMovimentoJariList) throws Exception {
		AitMovimento aitMovimentoJari = getSituacaoJari(aitMovimentoJariList);
		new GeradorMovimentoDocumentoFactory().factory(aitMovimentoJari, this.customConnection).build();		
		salvarResultadoJulgamento(aitMovimentoJari);
		nextGenerator.gerar();
	}
	
	private AitMovimento getSituacaoJari(List<AitMovimento> aitMovimentoJariList) {
		AitMovimento aitMovimentoJari = new AitMovimento();
		for (AitMovimento movimentoJari: aitMovimentoJariList) {
			switch(movimentoJari.getTpStatus()) {
				case  AitMovimentoServices.JARI_SEM_PROVIMENTO:
					aitMovimentoJari = movimentoJari;
					break;
				case AitMovimentoServices.JARI_COM_PROVIMENTO:
					aitMovimentoJari = movimentoJari;
					break;
				case AitMovimentoServices.RECURSO_JARI:
					aitMovimentoJari = movimentoJari;
			}
		}
		aitMovimentoJari.setCdAit(this.cdAit);
		return aitMovimentoJari;
	}
	
	private void salvarResultadoJulgamento(AitMovimento aitMovimentoJari) throws Exception {
		Ata ataImport = getAta();
		if(aitMovimentoJari.getTpStatus() == AitMovimentoServices.JARI_SEM_PROVIMENTO
		 ||aitMovimentoJari.getTpStatus() == AitMovimentoServices.JARI_COM_PROVIMENTO) {
			if(ataImport == null) {	
				ataImport = criarAtaImport(aitMovimentoJari);
				this.ataRepository.insert(ataImport, this.customConnection);		
			}
			inserirDocumentoOcorrencia(aitMovimentoJari);
			inserirRecurso(ataImport);
		}
	}
	
	private Ata getAta() throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ata", "Import", true);
		List<Ata> ataList = this.ataRepository.find(searchCriterios, this.customConnection);
		return ataList.isEmpty() ? null : ataList.get(0);
	}
	
	private Ata criarAtaImport(AitMovimento aitMovimentoJari) {
		Ata ata = new AtaBuilder()
				.addCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1))
				.addDtAta(new GregorianCalendar())
				.addIdAta("Import")
			.build();
		return ata;
	}
	
	private void inserirDocumentoOcorrencia(AitMovimento aitMovimentoJari) throws Exception {
		this.documentoOcorrencia = setOcorrencia(aitMovimentoJari);
		this.documentoOcorrenciaRepository.insert(this.documentoOcorrencia, this.customConnection);
	}
	
	private DocumentoOcorrencia setOcorrencia(AitMovimento aitMovimentoJari) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitMovimentoJari.getCdAit(), true);
		searchCriterios.addCriteriosEqualInteger("cd_movimento", aitMovimentoJari.getCdMovimento(), true);
		AitMovimentoDocumento AitMovimentoDocumento = this.aitMovimentoDocumentoService.getMovimentoDocumento(searchCriterios, this.customConnection);
		
		DocumentoOcorrencia ocorrencia = new DocumentoOcorrenciaBuilder()
				.setDocumento(AitMovimentoDocumento.getCdDocumento())
				.setCdUsuario(ParametroServices.getValorOfParametroAsInteger("MOB_USER_TIVIC", 1))
				.setDtOcorrencia(new GregorianCalendar())
				.setTxtOcorrencia("Import")
				.setCdTipoOcorrencia(tpStatusMap(aitMovimentoJari))
			.build();
		return ocorrencia;
	}
	
	private int tpStatusMap(AitMovimento aitMovimentoJari) {
		int tipoOcorrencia = 0;
		switch (aitMovimentoJari.getTpStatus()) {
			case AitMovimentoServices.JARI_SEM_PROVIMENTO:
				return tipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_INDEFERIDA", 0);
			case AitMovimentoServices.JARI_COM_PROVIMENTO:
				return tipoOcorrencia = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OCORRENCIA_DEFERIDA", 0);
		}
		return tipoOcorrencia;
	}
	
	private void inserirRecurso(Ata ataImport) throws Exception {
		Recurso recurso = setRecurso(ataImport, this.documentoOcorrencia);
		this.recursoRepository.insert(recurso, this.customConnection);
	}
	
	private Recurso setRecurso(Ata ata, DocumentoOcorrencia documentoOcorrencia ) {
		Recurso recurso = new Recurso();
		recurso.setCdAta(ata.getCdAta());
		recurso.setCdDocumento(documentoOcorrencia.getCdDocumento());
		return recurso;
	}
}
