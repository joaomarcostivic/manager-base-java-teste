package com.tivic.manager.ptc.documento;

import java.util.GregorianCalendar;

public class DocumentoBuilder {
	
	private com.tivic.manager.ptc.Documento documento;
	
	public DocumentoBuilder() {
		documento = new com.tivic.manager.ptc.Documento();
	}
	
	public DocumentoBuilder setCdDocumento(int cdDocumento) {
        documento.setCdDocumento(cdDocumento);
        return this;
    }

    public DocumentoBuilder setCdArquivo(int cdArquivo) {
        documento.setCdArquivo(cdArquivo);
        return this;
    }

    public DocumentoBuilder setCdSetor(int cdSetor) {
        documento.setCdSetor(cdSetor);
        return this;
    }

    public DocumentoBuilder setCdUsuario(int cdUsuario) {
        documento.setCdUsuario(cdUsuario);
        return this;
    }

    public DocumentoBuilder setNmLocalOrigem(String nmLocalOrigem) {
        documento.setNmLocalOrigem(nmLocalOrigem);
        return this;
    }

    public DocumentoBuilder setDtProtocolo(GregorianCalendar dtProtocolo) {
        documento.setDtProtocolo(dtProtocolo);
        return this;
    }

    public DocumentoBuilder setTpDocumento(int tpDocumento) {
        documento.setTpDocumento(tpDocumento);
        return this;
    }

    public DocumentoBuilder setTxtObservacao(String txtObservacao) {
        documento.setTxtObservacao(txtObservacao);
        return this;
    }

    public DocumentoBuilder setIdDocumento(String idDocumento) {
        documento.setIdDocumento(idDocumento);
        return this;
    }

    public DocumentoBuilder setNrDocumento(String nrDocumento) {
        documento.setNrDocumento(nrDocumento);
        return this;
    }

    public DocumentoBuilder setCdTipoDocumento(int cdTipoDocumento) {
        documento.setCdTipoDocumento(cdTipoDocumento);
        return this;
    }

    public DocumentoBuilder setCdFase(int cdFase) {
        documento.setCdFase(cdFase);
        return this;
    }

    public DocumentoBuilder setCdSetorAtual(int cdSetorAtual) {
        documento.setCdSetorAtual(cdSetorAtual);
        return this;
    }

    public DocumentoBuilder setCdSituacaoDocumento(int cdSituacaoDocumento) {
        documento.setCdSituacaoDocumento(cdSituacaoDocumento);
        return this;
    }

    public DocumentoBuilder setCdServico(int cdServico) {
        documento.setCdServico(cdServico);
        return this;
    }

    public DocumentoBuilder setCdAtendimento(int cdAtendimento) {
        documento.setCdAtendimento(cdAtendimento);
        return this;
    }

    public DocumentoBuilder setTxtDocumento(String txtDocumento) {
        documento.setTxtDocumento(txtDocumento);
        return this;
    }

    public DocumentoBuilder setCdEmpresa(int cdEmpresa) {
        documento.setCdEmpresa(cdEmpresa);
        return this;
    }

    public DocumentoBuilder setCdProcesso(int cdProcesso) {
        documento.setCdProcesso(cdProcesso);
        return this;
    }

    public DocumentoBuilder setTpPrioridade(int tpPrioridade) {
        documento.setTpPrioridade(tpPrioridade);
        return this;
    }

    public DocumentoBuilder setCdDocumentoSuperior(int cdDocumentoSuperior) {
        documento.setCdDocumentoSuperior(cdDocumentoSuperior);
        return this;
    }

    public DocumentoBuilder setDsAssunto(String dsAssunto) {
        documento.setDsAssunto(dsAssunto);
        return this;
    }

    public DocumentoBuilder setNrAtendimento(String nrAtendimento) {
        documento.setNrAtendimento(nrAtendimento);
        return this;
    }

    public DocumentoBuilder setLgNotificacao(int lgNotificacao) {
        documento.setLgNotificacao(lgNotificacao);
        return this;
    }

    public DocumentoBuilder setCdTipoDocumentoAnterior(int cdTipoDocumentoAnterior) {
        documento.setCdTipoDocumentoAnterior(cdTipoDocumentoAnterior);
        return this;
    }

    public DocumentoBuilder setCdDocumentoExterno(String cdDocumentoExterno) {
        documento.setCdDocumentoExterno(cdDocumentoExterno);
        return this;
    }

    public DocumentoBuilder setNrAssunto(String nrAssunto) {
        documento.setNrAssunto(nrAssunto);
        return this;
    }

    public DocumentoBuilder setNrDocumentoExterno(String nrDocumentoExterno) {
        documento.setNrDocumentoExterno(nrDocumentoExterno);
        return this;
    }

    public DocumentoBuilder setNrProtocoloExterno(String nrProtocoloExterno) {
        documento.setNrProtocoloExterno(nrProtocoloExterno);
        return this;
    }

    public DocumentoBuilder setNrAnoExterno(String nrAnoExterno) {
        documento.setNrAnoExterno(nrAnoExterno);
        return this;
    }

    public DocumentoBuilder setTpDocumentoExterno(int tpDocumentoExterno) {
        documento.setTpDocumentoExterno(tpDocumentoExterno);
        return this;
    }

    public DocumentoBuilder setTpInternoExterno(int tpInternoExterno) {
        documento.setTpInternoExterno(tpInternoExterno);
        return this;
    }

    public DocumentoBuilder setNmRequerente(String nmRequerente) {
        documento.setNmRequerente(nmRequerente);
        return this;
    }

    public com.tivic.manager.ptc.Documento build() {
        return documento;
    }
}
