package com.tivic.manager.mob.ecarta.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.CorreiosEtiqueta;

public class CorreiosEtiquetaBuilder {
	CorreiosEtiqueta correiosEtiqueta;

	public CorreiosEtiquetaBuilder() {
		this.correiosEtiqueta = new CorreiosEtiqueta();
	}

	public CorreiosEtiqueta build(String linha, int tpDocumento, int cdLoteImpressao) {
		String[] linhaDesmenbrada = linha.split("\\p{Punct}");
		correiosEtiqueta.setCdAit(Integer.valueOf(linhaDesmenbrada[1]));
		correiosEtiqueta.setCdLote(Integer.valueOf(linhaDesmenbrada[2]));
		correiosEtiqueta.setDtEnvio(new GregorianCalendar());
		correiosEtiqueta.setNrEtiqueta(Integer.valueOf(linhaDesmenbrada[3].replaceAll("[\\D]", "")));
		correiosEtiqueta.setTpStatus(tpDocumento);
		correiosEtiqueta.setSgServico(linhaDesmenbrada[3].substring(0, 2));
		correiosEtiqueta.setCdLoteImpressao(cdLoteImpressao);
		return correiosEtiqueta;
	}
}
