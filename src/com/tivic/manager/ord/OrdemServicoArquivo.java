package com.tivic.manager.ord;

public class OrdemServicoArquivo {

	private int cdOrdemServico;
	private int cdArquivo;

	public OrdemServicoArquivo() { }

	public OrdemServicoArquivo(int cdOrdemServico,
			int cdArquivo) {
		setCdOrdemServico(cdOrdemServico);
		setCdArquivo(cdArquivo);
	}
	public void setCdOrdemServico(int cdOrdemServico){
		this.cdOrdemServico=cdOrdemServico;
	}
	public int getCdOrdemServico(){
		return this.cdOrdemServico;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrdemServico: " +  getCdOrdemServico();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrdemServicoArquivo(getCdOrdemServico(),
			getCdArquivo());
	}

}