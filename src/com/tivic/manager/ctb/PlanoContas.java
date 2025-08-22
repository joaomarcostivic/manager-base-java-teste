package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class PlanoContas {

	private int cdPlanoContas;
	private int cdContaLucro;
	private int cdContaPrejuizo;
	private int cdMoeda;
	private String nmPlanoContas;
	private String dsMascaraConta;
	private GregorianCalendar dtInativacao;
	private String idPlanoContas;
	private int cdContaAtivo;
	private int cdContaPassivo;
	private int cdContaReceita;
	private int cdContaDespesa;
	private int cdContaCusto;
	private int cdContaDisponivel;
	private int cdContaPatrimonioLiquido;
	private int cdContaResultado;

	public PlanoContas(int cdPlanoContas,
			int cdContaLucro,
			int cdContaPrejuizo,
			int cdMoeda,
			String nmPlanoContas,
			String dsMascaraConta,
			GregorianCalendar dtInativacao,
			String idPlanoContas,
			int cdContaAtivo,
			int cdContaPassivo,
			int cdContaReceita,
			int cdContaDespesa,
			int cdContaCusto,
			int cdContaDisponivel,
			int cdContaPatrimonioLiquido,
			int cdContaResultado){
		setCdPlanoContas(cdPlanoContas);
		setCdContaLucro(cdContaLucro);
		setCdContaPrejuizo(cdContaPrejuizo);
		setCdMoeda(cdMoeda);
		setNmPlanoContas(nmPlanoContas);
		setDsMascaraConta(dsMascaraConta);
		setDtInativacao(dtInativacao);
		setIdPlanoContas(idPlanoContas);
		setCdContaAtivo(cdContaAtivo);
		setCdContaPassivo(cdContaPassivo);
		setCdContaReceita(cdContaReceita);
		setCdContaDespesa(cdContaDespesa);
		setCdContaCusto(cdContaCusto);
		setCdContaDisponivel(cdContaDisponivel);
		setCdContaPatrimonioLiquido(cdContaPatrimonioLiquido);
		setCdContaResultado(cdContaResultado);
	}
	public void setCdPlanoContas(int cdPlanoContas){
		this.cdPlanoContas=cdPlanoContas;
	}
	public int getCdPlanoContas(){
		return this.cdPlanoContas;
	}
	public void setCdContaLucro(int cdContaLucro){
		this.cdContaLucro=cdContaLucro;
	}
	public int getCdContaLucro(){
		return this.cdContaLucro;
	}
	public void setCdContaPrejuizo(int cdContaPrejuizo){
		this.cdContaPrejuizo=cdContaPrejuizo;
	}
	public int getCdContaPrejuizo(){
		return this.cdContaPrejuizo;
	}
	public void setCdMoeda(int cdMoeda){
		this.cdMoeda=cdMoeda;
	}
	public int getCdMoeda(){
		return this.cdMoeda;
	}
	public void setNmPlanoContas(String nmPlanoContas){
		this.nmPlanoContas=nmPlanoContas;
	}
	public String getNmPlanoContas(){
		return this.nmPlanoContas;
	}
	public void setDsMascaraConta(String dsMascaraConta){
		this.dsMascaraConta=dsMascaraConta;
	}
	public String getDsMascaraConta(){
		return this.dsMascaraConta;
	}
	public void setDtInativacao(GregorianCalendar dtInativacao){
		this.dtInativacao=dtInativacao;
	}
	public GregorianCalendar getDtInativacao(){
		return this.dtInativacao;
	}
	public void setIdPlanoContas(String idPlanoContas){
		this.idPlanoContas=idPlanoContas;
	}
	public String getIdPlanoContas(){
		return this.idPlanoContas;
	}
	public void setCdContaAtivo(int cdContaAtivo){
		this.cdContaAtivo=cdContaAtivo;
	}
	public int getCdContaAtivo(){
		return this.cdContaAtivo;
	}
	public void setCdContaPassivo(int cdContaPassivo){
		this.cdContaPassivo=cdContaPassivo;
	}
	public int getCdContaPassivo(){
		return this.cdContaPassivo;
	}
	public void setCdContaReceita(int cdContaReceita){
		this.cdContaReceita=cdContaReceita;
	}
	public int getCdContaReceita(){
		return this.cdContaReceita;
	}
	public void setCdContaDespesa(int cdContaDespesa){
		this.cdContaDespesa=cdContaDespesa;
	}
	public int getCdContaDespesa(){
		return this.cdContaDespesa;
	}
	public void setCdContaCusto(int cdContaCusto){
		this.cdContaCusto=cdContaCusto;
	}
	public int getCdContaCusto(){
		return this.cdContaCusto;
	}
	public void setCdContaDisponivel(int cdContaDisponivel){
		this.cdContaDisponivel=cdContaDisponivel;
	}
	public int getCdContaDisponivel(){
		return this.cdContaDisponivel;
	}
	public void setCdContaPatrimonioLiquido(int cdContaPatrimonioLiquido){
		this.cdContaPatrimonioLiquido=cdContaPatrimonioLiquido;
	}
	public int getCdContaPatrimonioLiquido(){
		return this.cdContaPatrimonioLiquido;
	}
	public void setCdContaResultado(int cdContaResultado){
		this.cdContaResultado=cdContaResultado;
	}
	public int getCdContaResultado(){
		return this.cdContaResultado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoContas: " +  getCdPlanoContas();
		valueToString += ", cdContaLucro: " +  getCdContaLucro();
		valueToString += ", cdContaPrejuizo: " +  getCdContaPrejuizo();
		valueToString += ", cdMoeda: " +  getCdMoeda();
		valueToString += ", nmPlanoContas: " +  getNmPlanoContas();
		valueToString += ", dsMascaraConta: " +  getDsMascaraConta();
		valueToString += ", dtInativacao: " +  sol.util.Util.formatDateTime(getDtInativacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idPlanoContas: " +  getIdPlanoContas();
		valueToString += ", cdContaAtivo: " +  getCdContaAtivo();
		valueToString += ", cdContaPassivo: " +  getCdContaPassivo();
		valueToString += ", cdContaReceita: " +  getCdContaReceita();
		valueToString += ", cdContaDespesa: " +  getCdContaDespesa();
		valueToString += ", cdContaCusto: " +  getCdContaCusto();
		valueToString += ", cdContaDisponivel: " +  getCdContaDisponivel();
		valueToString += ", cdContaPatrimonioLiquido: " +  getCdContaPatrimonioLiquido();
		valueToString += ", cdContaResultado: " +  getCdContaResultado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoContas(getCdPlanoContas(),
			getCdContaLucro(),
			getCdContaPrejuizo(),
			getCdMoeda(),
			getNmPlanoContas(),
			getDsMascaraConta(),
			getDtInativacao()==null ? null : (GregorianCalendar)getDtInativacao().clone(),
			getIdPlanoContas(),
			getCdContaAtivo(),
			getCdContaPassivo(),
			getCdContaReceita(),
			getCdContaDespesa(),
			getCdContaCusto(),
			getCdContaDisponivel(),
			getCdContaPatrimonioLiquido(),
			getCdContaResultado());
	}

}
