package com.tivic.manager.eCarta;

public class ArquivoRespostaECarta {
	private String nomeArquivoRecibo;
	private String arquivoRecibo;
	private String nomeArquivoInconsistencia;
	private String arquivoInconsistencia;
	
	public ArquivoRespostaECarta()
	{
		super();
	}
	
	public ArquivoRespostaECarta(String arquivoRecibo,  String arquivoInconsistencia)
	{
		super();
		this.arquivoRecibo = arquivoRecibo;
		this.arquivoInconsistencia = arquivoInconsistencia;
	}
	
	public String getNomeArquivoRecibo() {
		return nomeArquivoRecibo;
	}
	public void setNomeArquivoRecibo(String nomeArquivoRecibo) {
		this.nomeArquivoRecibo = nomeArquivoRecibo;
	}
	
	public String getArquivoRecibo() {
		return arquivoRecibo;
	}
	public void setArquivoRecibo(String arquivoRecibo) {
		this.arquivoRecibo = arquivoRecibo;
	}
	
	public String getNomeArquivoInconsistencia() {
		return nomeArquivoInconsistencia;
	}
	public void setNomeArquivoInconsistencia(String nomeArquivoInconsistencia) {
		this.nomeArquivoInconsistencia = nomeArquivoInconsistencia;
	}
	
	public String getArquivoInconsistencia() {
		return arquivoInconsistencia;
	}
	public void setArquivoInconsistencia(String arquivoInconsistencia) {
		this.arquivoInconsistencia = arquivoInconsistencia;
	}
	
	
	
}
