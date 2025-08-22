package com.tivic.manager.mob.ecarta.services;

import java.util.List;

import com.tivic.manager.eCarta.CorreiosLoteBuilder.CorreiosLoteBuilder;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.correios.CorreiosLoteRepository;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class EtiquetaCorreiosECT {
	private int cdLoteCorreios;
	private CorreiosLoteRepository correiosLoteRepository;
	private ICorreiosEtiquetaRepository correiosEtiquetaRepository;

	public EtiquetaCorreiosECT() throws Exception {
		correiosLoteRepository = (CorreiosLoteRepository) BeansFactory.get(CorreiosLoteRepository.class);
		correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
	}

	public void salvar(List<CorreiosEtiqueta> correiosEtiqueta, int cdLoteImpressao, CustomConnection customConnection)
			throws Exception {
		criarCorreiosLote(customConnection);
		etiquetas(correiosEtiqueta, cdLoteImpressao, customConnection);
	}

	private void criarCorreiosLote(CustomConnection customConnection) throws Exception {
		CorreiosLote correiosLote = new CorreiosLoteBuilder().build();
		correiosLote = this.correiosLoteRepository.insert(correiosLote, customConnection);
		this.cdLoteCorreios = correiosLote.getCdLote();
	}

	private void etiquetas(List<CorreiosEtiqueta> correiosEtiqueta, int cdLoteImpressao,
			CustomConnection customConnection) throws Exception {
		for (CorreiosEtiqueta etiqueta : correiosEtiqueta) {
			etiqueta.setCdLote(cdLoteCorreios);
			this.correiosEtiquetaRepository.insert(etiqueta, customConnection);
		}
	}
}
