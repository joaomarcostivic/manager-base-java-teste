package com.tivic.manager.mob.correios.builder;

import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.correios.DigitoVerificador;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GerarEtiquetasBuilder implements IGerarEtiquetas {
	private ICorreiosEtiquetaRepository correiosEtiquetaRepository;
	private CorreiosEtiqueta correiosEtiqueta;
	private int nrInicial = 0;
	private int nrFinal = 0;
	
	public GerarEtiquetasBuilder() throws Exception {
		correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
		correiosEtiqueta = new CorreiosEtiqueta();
	}
	
	@Override
	public void gerarEtiquetas(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		nrInicial = correiosLote.getNrInicial();
		nrFinal = correiosLote.getNrFinal();
		for (int i = nrInicial; i <= nrFinal ; i++) {
			CorreiosEtiqueta correiosEtiqueta = new CorreiosEtiqueta();
			DigitoVerificador digitoVerificador = new DigitoVerificador();
			correiosEtiqueta.setCdLote(correiosLote.getCdLote());
			correiosEtiqueta.setNrEtiqueta(i);
			correiosEtiqueta.setSgServico(correiosLote.getSgLote());
			correiosEtiqueta.setNrDigitoVerificador(digitoVerificador.GerarDigitoVerificador(i));
			
			correiosEtiquetaRepository.insert(correiosEtiqueta, customConnection);
		}
	}

	@Override
	public CorreiosEtiqueta build() {
		return this.correiosEtiqueta;
	}
	
}
