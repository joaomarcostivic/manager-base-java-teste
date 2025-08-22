package com.tivic.manager.str;

public class ProcessoArquivo {

	private int cdProcesso;
	private int cdArquivo;

	public ProcessoArquivo() { }

	public ProcessoArquivo(int cdProcesso,
			int cdArquivo) {
		setCdProcesso(cdProcesso);
		setCdArquivo(cdArquivo);
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProcesso: " +  getCdProcesso();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoArquivo(getCdProcesso(),
			getCdArquivo());
	}

}