package com.tivic.manager.mob.lote.impressao.viaunica.nic;

public class NicSimplificadoDTOBuilder {
	private NicSimplificadaDTO nicSimplificadaDTO;
	
	public NicSimplificadoDTOBuilder() {
		this.nicSimplificadaDTO = new NicSimplificadaDTO();
	}
	
	public NicSimplificadoDTOBuilder addCdAit(int cdAit) {
		this.nicSimplificadaDTO.setCdAit(cdAit);
		return this;
	}
	
	public NicSimplificadoDTOBuilder addCdAitOrigem(int cdAitOrigem) {
		this.nicSimplificadaDTO.setCdAitOrigem(cdAitOrigem);
		return this;
	}	
	
	public NicSimplificadoDTOBuilder addLgEnviado(int lgEnviado) {
		this.nicSimplificadaDTO.setLgEnviado(lgEnviado);
		return this;
	}
	
	public NicSimplificadoDTOBuilder addArquivo(byte[] arquivo) {
		this.nicSimplificadaDTO.setArquivo(arquivo);
		return this;
	}
	
	public NicSimplificadoDTOBuilder addCdLoteImpressao(int cdLoteImpressao) {
		this.nicSimplificadaDTO.setCdLoteImpressao(cdLoteImpressao);
		return this;
	}
	
	public NicSimplificadaDTO build() {
		return this.nicSimplificadaDTO;
	}
}
