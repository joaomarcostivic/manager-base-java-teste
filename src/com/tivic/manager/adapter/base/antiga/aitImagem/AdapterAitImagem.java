package com.tivic.manager.adapter.base.antiga.aitImagem;

import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.aitimagem.AitImagemBuilder;

public class AdapterAitImagem {
	public AitImagemOld adapterToOld(AitImagem aitImagem) {
		return new AitImagemOldBuilder()
				.setCdImagem(aitImagem.getCdImagem())
				.setCdAit(aitImagem.getCdAit())
				.setBlbImagem(aitImagem.getBlbImagem())
				.setTpImagem(aitImagem.getTpImagem())
				.setLgImpressao(aitImagem.getLgImpressao())
				.build();
	}
	
	public AitImagem adapterToBaseNova(AitImagemOld aitImagemOld) {
		return new AitImagemBuilder()
			.setCdImagem(aitImagemOld.getCdImagem())
			.setCdAit(aitImagemOld.getCdAit())
			.setBlbImagem(aitImagemOld.getBlbImagem())
			.setTpImagem(aitImagemOld.getTpImagem())
			.setLgImpressao(aitImagemOld.getLgImpressao())
			.build();
	}
}
