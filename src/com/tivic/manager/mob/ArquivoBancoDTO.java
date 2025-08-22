package com.tivic.manager.mob;

public class ArquivoBancoDTO {
	private String arquivoBanco;
	private String idBanco;
	private String nmArquivo;
	
	public String getArquivo() {
		return this.arquivoBanco;
	}
	
	public void setArquivo(String arquivoBanco) {
		this.arquivoBanco = arquivoBanco;
	}
	
	public String getIdBanco() {
		return this.idBanco;
	}
	
	public void setIdBanco(String idBanco) {
		this.idBanco = idBanco;
	}
	
	public String getArquivoBanco() {
		return arquivoBanco;
	}

	public void setArquivoBanco(String arquivoBanco) {
		this.arquivoBanco = arquivoBanco;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public ArquivoBancoDTO() {
		super();
	}
	
	public ArquivoBancoDTO (String arquivoBanco, String idBanco, String nmArquivo) {
		super();
		this.arquivoBanco = arquivoBanco;
		this.idBanco = idBanco;
		this.nmArquivo = nmArquivo;
	}
}
