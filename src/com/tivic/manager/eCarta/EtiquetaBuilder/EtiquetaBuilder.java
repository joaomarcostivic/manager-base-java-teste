package com.tivic.manager.eCarta.EtiquetaBuilder;

import com.tivic.manager.eCarta.Etiqueta;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;

public class EtiquetaBuilder {
	public EtiquetaBuilder() {}
	
	public Etiqueta build(String resposeLine) {
		Etiqueta etiqueta = new Etiqueta();
		String[] dismemberLine = resposeLine.split("\\p{Punct}");
		etiqueta.setCodAit(Integer.valueOf(dismemberLine[1]));
		etiqueta.setCodLote(Integer.valueOf(dismemberLine[2]));
		etiqueta.setSgInicial(dismemberLine[3].substring(0,2));
		etiqueta.setNumEtiqueta( Long.valueOf(dismemberLine[3].replaceAll("[\\D]", "")));
		etiqueta.setSgFinal(dismemberLine[3].substring(dismemberLine[3].length() -2));
		etiqueta.setTpDocumento(LoteImpressaoDAO.get(Integer.valueOf(dismemberLine[2])).getTpDocumento());
		return etiqueta;
	}
}
