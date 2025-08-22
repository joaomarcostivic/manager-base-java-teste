package com.tivic.manager.ptc.protocolosv3.documento.ata;

public class AtaDTOBuilder {
	private AtaDTO ataDto;
	
	public AtaDTOBuilder(AtaDTO ataDto) {
		this.ataDto = ataDto;
	}
	
	public AtaDTOBuilder montaByAta(Ata ata) {
		this.ataDto.setCdAta(ata.getCdAta());
		this.ataDto.setDtEntradaRecurso(ata.getDtCadastro());
		this.ataDto.setDtAta(ata.getDtAta());
		this.ataDto.setIdAta(ata.getIdAta());
		return this;
	}
	
	public AtaDTO build() {
		return this.ataDto;
	}
}
