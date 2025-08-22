package com.tivic.manager.ptc.protocolosv3.externo.builders;

import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class DocumentoBuilder {

    private Documento documento;

    public DocumentoBuilder() throws Exception {
    	this.documento = new Documento();
    }
    
    public DocumentoBuilder(ProtocoloExternoDTO protocoloExternoDTO) throws Exception {
        this.documento = new Documento();
        addCdDocumento(protocoloExternoDTO.getCdDocumento());
        addCdUsuario(protocoloExternoDTO.getCdUsuario());
        addCdTipoDocumento(protocoloExternoDTO.getCdTipoDocumento());
        addDtProtocolo(protocoloExternoDTO.getDtProtocolo());
        addNmRequerente(protocoloExternoDTO.getNmRequerente());
        addTxtObservacao(protocoloExternoDTO.getTxtObservacao());
        addNrDocumento(protocoloExternoDTO.getNrDocumento() != null? protocoloExternoDTO.getNrDocumento() : gerarNumeroDocumento());
    }
    
    public DocumentoBuilder addCdDocumento(int cdDocumento) {
        this.documento.setCdDocumento(cdDocumento);
        return this;
    }
    
    public DocumentoBuilder addCdUsuario(int cdUsuario) {
        this.documento.setCdUsuario(cdUsuario);
        return this;
    }

    public DocumentoBuilder addCdTipoDocumento(int cdTipoDocumento) {
        this.documento.setCdTipoDocumento(cdTipoDocumento);
        return this;
    }

    public DocumentoBuilder addDtProtocolo(GregorianCalendar dtProtocolo) {
        this.documento.setDtProtocolo(dtProtocolo);
        return this;
    }
    
    public DocumentoBuilder addNmRequerente(String nmRequerente) {
        this.documento.setNmRequerente(nmRequerente);
        return this;
    }
    
    public DocumentoBuilder addTxtObservacao(String txtObservacao) {
        this.documento.setTxtObservacao(txtObservacao);
        return this;
    }
    
    public DocumentoBuilder addNrDocumento(String nrDocumento) {
        this.documento.setNrDocumento(nrDocumento);
        return this;
    }
    
    private String gerarNumeroDocumento() throws Exception {
    	String nrDocumento = getUltimoNrDocumento();
    	int number;
    	if(nrDocumento == null) {
    		number = 1;
    	}else {
    		number = nrDocumento.contains("/") 
    	            ? Integer.parseInt(nrDocumento.split("/")[0]) 
    	            : Integer.parseInt(nrDocumento);
    	    number += 1;
    	}
    	String sequencial = String.format("%03d", number);
    	nrDocumento = mountarNrDocumento(sequencial);
    	return nrDocumento;
    }
    
	private String mountarNrDocumento(String sequencial) {
		int ano = LocalDate.now().getYear();
		return sequencial + "/" + ano;
	}
    
    private String getUltimoNrDocumento() throws Exception{
    	int anoAtual = LocalDate.now().getYear();
    	SearchCriterios searchCriterios = new SearchCriterios();
    	Search<Documento> search = new SearchBuilder<Documento>("ptc_documento A")
    			.fields("nr_documento")
    			.addJoinTable("JOIN mob_documento_externo B ON (A.cd_documento = B.cd_documento)")
    			.searchCriterios(searchCriterios)
    			.additionalCriterias("A.nr_documento LIKE '%/" + anoAtual + "'")
    			.orderBy("A.cd_documento DESC")
    			.build();
        List<Documento> documentos = search.getList(Documento.class);
        return (documentos.isEmpty()) ? null : documentos.get(0).getNrDocumento();
    }
    
    public Documento build() {
        return this.documento;
    }
}
