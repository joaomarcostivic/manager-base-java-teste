package com.tivic.manager.mob;

public class ImportacaoAit {

	private int cdImportacao;
	private int tabelaFirebird;
	private int cdAntigo;
	private int cdNovo;
	private String tabelaPostgre;

	public ImportacaoAit(){ }

	public ImportacaoAit(int cdImportacao,
			int tabelaFirebird,
			int cdAntigo,
			int cdNovo,
			String tabelaPostgre){
		setCdImportacao(cdImportacao);
		setTabelaFirebird(tabelaFirebird);
		setCdAntigo(cdAntigo);
		setCdNovo(cdNovo);
		setTabelaPostgre(tabelaPostgre);
	}
	public void setCdImportacao(int cdImportacao){
		this.cdImportacao=cdImportacao;
	}
	public int getCdImportacao(){
		return this.cdImportacao;
	}
	public void setTabelaFirebird(int tabelaFirebird){
		this.tabelaFirebird=tabelaFirebird;
	}
	public int getTabelaFirebird(){
		return this.tabelaFirebird;
	}
	public void setCdAntigo(int cdAntigo){
		this.cdAntigo=cdAntigo;
	}
	public int getCdAntigo(){
		return this.cdAntigo;
	}
	public void setCdNovo(int cdNovo){
		this.cdNovo=cdNovo;
	}
	public int getCdNovo(){
		return this.cdNovo;
	}
	public void setTabelaPostgre(String tabelaPostgre){
		this.tabelaPostgre=tabelaPostgre;
	}
	public String getTabelaPostgre(){
		return this.tabelaPostgre;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdImportacao: " +  getCdImportacao();
		valueToString += ", tabelaFirebird: " +  getTabelaFirebird();
		valueToString += ", cdAntigo: " +  getCdAntigo();
		valueToString += ", cdNovo: " +  getCdNovo();
		valueToString += ", tabelaPostgre: " +  getTabelaPostgre();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ImportacaoAit(getCdImportacao(),
			getTabelaFirebird(),
			getCdAntigo(),
			getCdNovo(),
			getTabelaPostgre());
	}

}