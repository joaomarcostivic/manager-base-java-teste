package com.tivic.manager.ptc.protocolosv3;

public class InsertProtocoloDTOBuilder {
	private ProtocoloInsertDTO insertDto;
	private ProtocoloDTO protocolo;
	
	public InsertProtocoloDTOBuilder(ProtocoloInsertDTO insertDto) {
		this.insertDto = insertDto;
		protocolo = new ProtocoloDTO();
	}
	
	public ProtocoloDTO build() {
		return protocolo;
	}
	
	public InsertProtocoloDTOBuilder setAit() throws Exception {
		if(isNullOrZero(insertDto.getCdAit()))
			throw new Exception("Código do Ait é nulo.");
		
		protocolo.getAit().setCdAit(insertDto.getCdAit());
		
		return this;
	}
	
	public InsertProtocoloDTOBuilder setTipoDocumento() throws Exception{
		if(isNullOrZero(insertDto.getCdTipoDocumento()))
			throw new Exception("Código do Tipo de documento é nulo.");
		protocolo.getTipoDocumento().setCdTipoDocumento(insertDto.getCdTipoDocumento());
		protocolo.getDocumento().setCdTipoDocumento(insertDto.getCdTipoDocumento());
		
		return this;
	}
	
	public InsertProtocoloDTOBuilder setMovimento() {
		protocolo.getAitMovimento().setCdOcorrencia(insertDto.getCdOcorrencia());
		return this;
	}
	
	public InsertProtocoloDTOBuilder setDocumento() {
		protocolo.getDocumento().setNrDocumento(insertDto.getNrDocumento());
		protocolo.getDocumento().setNmRequerente(insertDto.getNmRequerente());
		protocolo.getDocumento().setNrDocumento(insertDto.getNrDocumento());
		protocolo.getDocumento().setTxtObservacao(insertDto.getTxtObservacao());
		protocolo.getDocumento().setDtProtocolo(insertDto.getDtProtocolo());
		protocolo.getDocumento().setCdUsuario(insertDto.getCdUsuario());
		protocolo.getDocumento().setTpDocumento(insertDto.getTpDocumento());
		protocolo.getDocumento().setCdSituacaoDocumento(insertDto.getCdSituacaoDocumento());
		return this;
	}
	
	public InsertProtocoloDTOBuilder setUsuario() {
		protocolo.getUsuario().setCdUsuario(insertDto.getCdUsuario());
		return this;
	}
	
	public InsertProtocoloDTOBuilder setArquivos() {
		protocolo.setArquivos(insertDto.getArquivos());
		return this;
	}
	
	private boolean isNullOrZero(Integer number){
		return number == null || number.intValue() == 0;
	}
}
