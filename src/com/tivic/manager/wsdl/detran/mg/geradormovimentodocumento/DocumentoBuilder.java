package com.tivic.manager.wsdl.detran.mg.geradormovimentodocumento;

import com.tivic.manager.gpn.TipoDocumentoServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.ptc.tipodocumento.TipoDocumentoRepository;
import com.tivic.manager.wsdl.detran.mg.TipoDocumentoMap;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class DocumentoBuilder {

	private Documento documento;
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	public DocumentoBuilder(AitMovimento aitMovimento) throws Exception {
		this.tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
		this.documento = new Documento();
		this.documento.setCdSetor(1);
		this.documento.setDtProtocolo(aitMovimento.getDtMovimento());
		this.documento.setCdSetorAtual(1);
		this.documento.setCdTipoDocumento(buscarCdTipoDocumento(aitMovimento));
		this.documento.setNrDocumento(setNrProcesso(aitMovimento));
	}
	
	public DocumentoBuilder cdTipoDocumento(int cdTipoDocumento) {
		this.documento.setCdTipoDocumento(cdTipoDocumento);
		return this;
	}

	public DocumentoBuilder cdFase(int cdFase) {
		this.documento.setCdFase(cdFase);
		return this;
	}
	
	public DocumentoBuilder cdSituacaoDocumeto(int cdSituacaoDocumento) {
		this.documento.setCdSituacaoDocumento(cdSituacaoDocumento);
		return this;
	}

	public Documento build() {
		return this.documento;
	}
	
	private int buscarCdTipoDocumento(AitMovimento aitMovimento) throws Exception {
		String idTipoDocumento = new TipoDocumentoMap().getIdByTpStatus(aitMovimento.getTpStatus());
		TipoDocumento tipoDocumento = getTipoDocumento(idTipoDocumento);
		return tipoDocumento.getCdTipoDocumento();
	}
	
	private TipoDocumento getTipoDocumento(String idTipoDocumento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_tipo_documento", idTipoDocumento, true);
		TipoDocumento tipoDocumento = this.tipoDocumentoRepository.find(searchCriterios);
		return tipoDocumento;
	}
	
	private String setNrProcesso(AitMovimento aitMovimento) {
		if(aitMovimento.getNrProcesso() == null) {
			return TipoDocumentoServices.getNextNumeracao(documento.getCdTipoDocumento(), documento.getCdEmpresa(), true, null);
		} else {
			return aitMovimento.getNrProcesso();
		}
	}
}
