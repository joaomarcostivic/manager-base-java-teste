package com.tivic.manager.mob.correios;

import com.tivic.manager.util.Util;

public class DigitoVerificador {
	private String nrEtiqueta;
	private int digitoVerificador;
	private Integer[] multiplicadores = {8, 6, 4, 2, 3, 5, 9, 7};
	private Integer soma = 0;
	private int qtdEtiqueta = 8;
	
	public int GerarDigitoVerificador(int numeroEtiqueta) {
		nrEtiqueta = Util.fillNum(numeroEtiqueta, qtdEtiqueta);

		for(int i = 0; i < 8; i++) {
			soma += new Integer(nrEtiqueta.substring(i, (i+1))) * multiplicadores[i];
		}
		
		Integer resto = soma % 11;		
		if(resto == 0) {
			digitoVerificador = 5;
		} else if(resto == 1) {
			digitoVerificador = 0;
		} else {
			digitoVerificador = new Integer(11 - resto);
		}
		
		return digitoVerificador;
	}
}
