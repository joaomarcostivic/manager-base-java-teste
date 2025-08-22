package com.tivic.manager.mob.lotes.builders.impressao.viaunica;

import com.tivic.manager.mob.lotes.dto.impressao.NicSimplificadaDTO;
import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;

public class NipImpressaoDTOBuilder {
	private NipImpressaoDTO nipImpressaoDTO;
	
	public NipImpressaoDTOBuilder() {
		this.nipImpressaoDTO = new NipImpressaoDTO();
	}
	
	public NipImpressaoDTOBuilder addCdAit(int cdAit) {
		this.nipImpressaoDTO.setCdAit(cdAit);
		return this;
	}
	
	public NipImpressaoDTOBuilder addArquivo(byte[] arquivo) {
		this.nipImpressaoDTO.setArquivo(arquivo);
		return this;
	}
	
	public NipImpressaoDTOBuilder addNicSimplificadoDTO(NicSimplificadaDTO nicSimplificadaDTO) {
		this.nipImpressaoDTO.setNicSimplificadaDTO(nicSimplificadaDTO);;
		return this;
	}
	
	public NipImpressaoDTO build() {
		return this.nipImpressaoDTO;
	}

}
