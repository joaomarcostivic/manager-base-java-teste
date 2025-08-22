package com.tivic.manager.ptc.protocolosv3.cancelamento;

import java.util.ArrayList;
import java.util.List;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.documento.ata.Ata;
import com.tivic.manager.ptc.protocolosv3.documento.ata.AtaRelator;
import com.tivic.manager.ptc.protocolosv3.documento.ata.IAtaRelatorRepository;
import com.tivic.manager.ptc.protocolosv3.documento.ata.IAtaRepository;
import com.tivic.manager.ptc.protocolosv3.recursos.IRecursoRepository;
import com.tivic.manager.ptc.protocolosv3.recursos.Recurso;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class AtaDocumentoCancelado {
	
	private IRecursoRepository recursoRepository;
	private IAtaRelatorRepository ataRelatorRepository;
	private IAtaRepository ataRepository;
	private ProtocoloDTO protocolo;
	private CustomConnection customConnection;
	private ArrayList<Integer> documentosAta = new ArrayList<Integer>();
	private boolean isDocumentoAta = false;

	public AtaDocumentoCancelado(ProtocoloDTO protocolo, CustomConnection customConnection) throws Exception {
		this.recursoRepository = (IRecursoRepository) BeansFactory.get(IRecursoRepository.class);
		this.ataRelatorRepository = (IAtaRelatorRepository) BeansFactory.get(IAtaRelatorRepository.class);
		this.ataRepository = (IAtaRepository) BeansFactory.get(IAtaRepository.class);
		this.protocolo = protocolo;
		this.customConnection = customConnection;
		adicionarDocumentosAta();
	}
	
	private void adicionarDocumentosAta() {
		this.documentosAta.add(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey());
		this.documentosAta.add(TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey());
	}
	
	public AtaDocumentoCancelado verificar() {
		if (this.documentosAta.contains(Integer.valueOf(this.protocolo.getTipoDocumento().getIdTipoDocumento()))) {
			this.isDocumentoAta = true;
		}
		return this;
	}
	
	public void removerDadosAta() throws Exception {
		if (this.isDocumentoAta == false) {
			return;
		}
		int cdAtaRecurso = removerRecursoDocumentoAta();
		SearchCriterios criteriosAta = criteriosAtaRecurso(cdAtaRecurso);
		List<Recurso> recursoAtaList = this.recursoRepository.find(criteriosAta, this.customConnection);
		if (recursoAtaList.isEmpty()) {
			removerRelatores(criteriosAta);
			Ata ata = this.ataRepository.get(cdAtaRecurso, this.customConnection);
			this.ataRepository.delete(ata.getCdAta(), customConnection);
		}
	}
	
	private int removerRecursoDocumentoAta() throws Exception {
		int cdDocumento = this.protocolo.getDocumento().getCdDocumento();
		SearchCriterios criteriosDocumento = criteriosDocumentoRecurso(cdDocumento);
		Recurso recursoDocumento = this.recursoRepository.find(criteriosDocumento, this.customConnection).get(0);
		this.recursoRepository.delete(cdDocumento, recursoDocumento.getCdAta(), customConnection);
		return recursoDocumento.getCdAta();
	}
	
	private void removerRelatores(SearchCriterios criteriosAta) throws Exception {
		List<AtaRelator> ataRelatorList = this.ataRelatorRepository.find(criteriosAta, this.customConnection);
		for(AtaRelator ataRelator : ataRelatorList) {
			this.ataRelatorRepository.delete(ataRelator.getCdAta(), ataRelator.getCdPessoa(), customConnection);
		}
	}
	
	private SearchCriterios criteriosDocumentoRecurso(int cdDocumento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_documento", cdDocumento, true);
		return searchCriterios;
	}
	
	private SearchCriterios criteriosAtaRecurso(int cdAta) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ata", cdAta, true);
		return searchCriterios;
	}
	
}
