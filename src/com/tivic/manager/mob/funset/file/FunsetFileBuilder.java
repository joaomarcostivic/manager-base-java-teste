package com.tivic.manager.mob.funset.file;

import com.tivic.manager.mob.funset.FunsetInfos;
import com.tivic.sol.cdi.BeansFactory;

public class FunsetFileBuilder {

	private FunsetFile funsetFile;
	private String textoArquivo;
	FunsetInfos funsetInfos;
	
	public FunsetFileBuilder(String textoArquivo) throws Exception{
		funsetInfos = (FunsetInfos)BeansFactory.get(FunsetInfos.class);
		this.textoArquivo = textoArquivo;
		this.funsetFile = new FunsetFile();
	}
	
	
	public FunsetFile build() {
		this.funsetFile.setArquivo(this.textoArquivo.getBytes());
		this.funsetFile.setNmArquivo(funsetInfos.getNomeArquivoFunset());
		return this.funsetFile;
	}
}
