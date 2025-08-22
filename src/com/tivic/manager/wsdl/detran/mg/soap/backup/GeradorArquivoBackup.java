package com.tivic.manager.wsdl.detran.mg.soap.backup;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;

public class GeradorArquivoBackup implements Backup {
	
	@Override
	public void gerarEntrada(DadosEntradaMG dadosEntrada) {
		montarArquivo(dadosEntrada, dadosEntrada.getXml(), "ENTRADA");
	}

	@Override
	public void gerarRetorno(DadosEntradaMG dadosEntrada, String retorno) {
		montarArquivo(dadosEntrada, retorno, "RETORNO");
	}
	
	private void montarArquivo(DadosEntradaMG dadosEntrada, String texto, String sufixo) {
		PrintWriter printWriter = null;
		try {
			File directory = new File("BACKUP DETRAN/" + dadosEntrada.getNmServico());
			if(!directory.exists()) {
				directory.mkdirs();
			}
			File file = new File(new NomeArquivoBackupFactory()
					.getStrategy(dadosEntrada)
					.build(directory.getAbsolutePath(), sufixo));
			printWriter = new PrintWriter(file);
			printWriter.println(texto);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			if(printWriter != null)
				printWriter.close();
		}
	}
}
