package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class MovimentoConta {

	private int cdMovimentoConta;
	private int cdConta;
	private int cdContaOrigem;
	private int cdMovimentoOrigem;
	private int cdUsuario;
	private int cdCheque;
	private int cdViagem;
	private GregorianCalendar dtMovimento;
	private Double vlMovimento;
	private String nrDocumento;
	private int tpMovimento;
	private int tpOrigem;
	private int stMovimento;
	private String dsHistorico;
	private GregorianCalendar dtDeposito;
	private String idExtrato;
	private int cdFormaPagamento;
	private int cdFechamento;
	private int cdTurno;
	private int cdPlanoPagamento;
	private String nrDocConta;

	public MovimentoConta() { }

	public MovimentoConta(int cdMovimentoConta,
			int cdConta,
			int cdContaOrigem,
			int cdMovimentoOrigem,
			int cdUsuario,
			int cdCheque,
			int cdViagem,
			GregorianCalendar dtMovimento,
			Double vlMovimento,
			String nrDocumento,
			int tpMovimento,
			int tpOrigem,
			int stMovimento,
			String dsHistorico,
			GregorianCalendar dtDeposito,
			String idExtrato,
			int cdFormaPagamento,
			int cdFechamento,
			int cdTurno,
			int cdPlanoPagamento,
			String nrDocConta) {
		setCdMovimentoConta(cdMovimentoConta);
		setCdConta(cdConta);
		setCdContaOrigem(cdContaOrigem);
		setCdMovimentoOrigem(cdMovimentoOrigem);
		setCdUsuario(cdUsuario);
		setCdCheque(cdCheque);
		setCdViagem(cdViagem);
		setDtMovimento(dtMovimento);
		setVlMovimento(vlMovimento);
		setNrDocumento(nrDocumento);
		setTpMovimento(tpMovimento);
		setTpOrigem(tpOrigem);
		setStMovimento(stMovimento);
		setDsHistorico(dsHistorico);
		setDtDeposito(dtDeposito);
		setIdExtrato(idExtrato);
		setCdFormaPagamento(cdFormaPagamento);
		setCdFechamento(cdFechamento);
		setCdTurno(cdTurno);
		setCdPlanoPagamento(cdPlanoPagamento);
		setNrDocConta(nrDocConta);
	}
	
	/**
	 * @deprecated
	 * @param cdMovimentoConta
	 * @param cdConta
	 * @param cdContaOrigem
	 * @param cdMovimentoOrigem
	 * @param cdUsuario
	 * @param cdCheque
	 * @param cdViagem
	 * @param dtMovimento
	 * @param vlMovimento
	 * @param nrDocumento
	 * @param tpMovimento
	 * @param tpOrigem
	 * @param stMovimento
	 * @param dsHistorico
	 * @param dtDeposito
	 * @param idExtrato
	 * @param cdFormaPagamento
	 * @param cdFechamento
	 * @param cdTurno
	 */
	public MovimentoConta(int cdMovimentoConta,
			int cdConta,
			int cdContaOrigem,
			int cdMovimentoOrigem,
			int cdUsuario,
			int cdCheque,
			int cdViagem,
			GregorianCalendar dtMovimento,
			double vlMovimento,
			String nrDocumento,
			int tpMovimento,
			int tpOrigem,
			int stMovimento,
			String dsHistorico,
			GregorianCalendar dtDeposito,
			String idExtrato,
			int cdFormaPagamento,
			int cdFechamento,
			int cdTurno){
		setCdMovimentoConta(cdMovimentoConta);
		setCdConta(cdConta);
		setCdContaOrigem(cdContaOrigem);
		setCdMovimentoOrigem(cdMovimentoOrigem);
		setCdUsuario(cdUsuario);
		setCdCheque(cdCheque);
		setCdViagem(cdViagem);
		setDtMovimento(dtMovimento);
		setVlMovimento(vlMovimento);
		setNrDocumento(nrDocumento);
		setTpMovimento(tpMovimento);
		setTpOrigem(tpOrigem);
		setStMovimento(stMovimento);
		setDsHistorico(dsHistorico);
		setDtDeposito(dtDeposito);
		setIdExtrato(idExtrato);
		setCdFormaPagamento(cdFormaPagamento);
		setCdFechamento(cdFechamento);
		setCdTurno(cdTurno);
	}
	public void setCdMovimentoConta(int cdMovimentoConta){
		this.cdMovimentoConta=cdMovimentoConta;
	}
	public int getCdMovimentoConta(){
		return this.cdMovimentoConta;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdContaOrigem(int cdContaOrigem){
		this.cdContaOrigem=cdContaOrigem;
	}
	public int getCdContaOrigem(){
		return this.cdContaOrigem;
	}
	public void setCdMovimentoOrigem(int cdMovimentoOrigem){
		this.cdMovimentoOrigem=cdMovimentoOrigem;
	}
	public int getCdMovimentoOrigem(){
		return this.cdMovimentoOrigem;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdCheque(int cdCheque){
		this.cdCheque=cdCheque;
	}
	public int getCdCheque(){
		return this.cdCheque;
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento){
		this.dtMovimento=dtMovimento;
	}
	public GregorianCalendar getDtMovimento(){
		return this.dtMovimento;
	}
	public void setVlMovimento(Double vlMovimento){
		this.vlMovimento=vlMovimento;
	}
	public Double getVlMovimento(){
		return this.vlMovimento;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setTpMovimento(int tpMovimento){
		this.tpMovimento=tpMovimento;
	}
	public int getTpMovimento(){
		return this.tpMovimento;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setStMovimento(int stMovimento){
		this.stMovimento=stMovimento;
	}
	public int getStMovimento(){
		return this.stMovimento;
	}
	public void setDsHistorico(String dsHistorico){
		this.dsHistorico=dsHistorico;
	}
	public String getDsHistorico(){
		return this.dsHistorico;
	}
	public void setDtDeposito(GregorianCalendar dtDeposito){
		this.dtDeposito=dtDeposito;
	}
	public GregorianCalendar getDtDeposito(){
		return this.dtDeposito;
	}
	public void setIdExtrato(String idExtrato){
		this.idExtrato=idExtrato;
	}
	public String getIdExtrato(){
		return this.idExtrato;
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setCdFechamento(int cdFechamento){
		this.cdFechamento=cdFechamento;
	}
	public int getCdFechamento(){
		return this.cdFechamento;
	}
	public void setCdTurno(int cdTurno){
		this.cdTurno=cdTurno;
	}
	public int getCdTurno(){
		return this.cdTurno;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setNrDocConta(String nrDocConta){
		this.nrDocConta=nrDocConta;
	}
	public String getNrDocConta(){
		return this.nrDocConta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdContaOrigem: " +  getCdContaOrigem();
		valueToString += ", cdMovimentoOrigem: " +  getCdMovimentoOrigem();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdCheque: " +  getCdCheque();
		valueToString += ", cdViagem: " +  getCdViagem();
		valueToString += ", dtMovimento: " +  sol.util.Util.formatDateTime(getDtMovimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlMovimento: " +  getVlMovimento();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", tpMovimento: " +  getTpMovimento();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", stMovimento: " +  getStMovimento();
		valueToString += ", dsHistorico: " +  getDsHistorico();
		valueToString += ", dtDeposito: " +  sol.util.Util.formatDateTime(getDtDeposito(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idExtrato: " +  getIdExtrato();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", cdTurno: " +  getCdTurno();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", nrDocConta: " +  getNrDocConta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoConta(getCdMovimentoConta(),
			getCdConta(),
			getCdContaOrigem(),
			getCdMovimentoOrigem(),
			getCdUsuario(),
			getCdCheque(),
			getCdViagem(),
			getDtMovimento()==null ? null : (GregorianCalendar)getDtMovimento().clone(),
			getVlMovimento(),
			getNrDocumento(),
			getTpMovimento(),
			getTpOrigem(),
			getStMovimento(),
			getDsHistorico(),
			getDtDeposito()==null ? null : (GregorianCalendar)getDtDeposito().clone(),
			getIdExtrato(),
			getCdFormaPagamento(),
			getCdFechamento(),
			getCdTurno(),
			getCdPlanoPagamento(),
			getNrDocConta());
	}

}