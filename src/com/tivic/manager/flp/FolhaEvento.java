package com.tivic.manager.flp;

public class FolhaEvento {

	private int cdEventoFinanceiro;
	private int cdMatricula;
	private int cdFolhaPagamento;
	private int cdRescisao;
	private int cdFerias;
	private int tpLancamento;
	private float qtEvento;
	private float vlEvento;

	public FolhaEvento(int cdEventoFinanceiro,
			int cdMatricula,
			int cdFolhaPagamento,
			int cdRescisao,
			int cdFerias,
			int tpLancamento,
			float qtEvento,
			float vlEvento){
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setCdMatricula(cdMatricula);
		setCdFolhaPagamento(cdFolhaPagamento);
		setCdRescisao(cdRescisao);
		setCdFerias(cdFerias);
		setTpLancamento(tpLancamento);
		setQtEvento(qtEvento);
		setVlEvento(vlEvento);
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdFolhaPagamento(int cdFolhaPagamento){
		this.cdFolhaPagamento=cdFolhaPagamento;
	}
	public int getCdFolhaPagamento(){
		return this.cdFolhaPagamento;
	}
	public void setCdRescisao(int cdRescisao){
		this.cdRescisao=cdRescisao;
	}
	public int getCdRescisao(){
		return this.cdRescisao;
	}
	public void setCdFerias(int cdFerias){
		this.cdFerias=cdFerias;
	}
	public int getCdFerias(){
		return this.cdFerias;
	}
	public void setTpLancamento(int tpLancamento){
		this.tpLancamento=tpLancamento;
	}
	public int getTpLancamento(){
		return this.tpLancamento;
	}
	public void setQtEvento(float qtEvento){
		this.qtEvento=qtEvento;
	}
	public float getQtEvento(){
		return this.qtEvento;
	}
	public void setVlEvento(float vlEvento){
		this.vlEvento=vlEvento;
	}
	public float getVlEvento(){
		return this.vlEvento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", cdFolhaPagamento: " +  getCdFolhaPagamento();
		valueToString += ", cdRescisao: " +  getCdRescisao();
		valueToString += ", cdFerias: " +  getCdFerias();
		valueToString += ", tpLancamento: " +  getTpLancamento();
		valueToString += ", qtEvento: " +  getQtEvento();
		valueToString += ", vlEvento: " +  getVlEvento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FolhaEvento(getCdEventoFinanceiro(),
			getCdMatricula(),
			getCdFolhaPagamento(),
			getCdRescisao(),
			getCdFerias(),
			getTpLancamento(),
			getQtEvento(),
			getVlEvento());
	}

}