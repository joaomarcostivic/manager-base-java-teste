package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class BemPenhora {

	private int cdBem;
	private int cdProcesso;
	private int cdCidade;
	private String nmBem;
	private int stBem;
	private double vlBem;
	private String nmLogradouro;
	private String nmBairro;
	private String nmDistrito;
	private String nrEndereco;
	private String nmComplemento;
	private String nmEndereco;
	private GregorianCalendar dtAtualizacaoEdi;
	private int stAtualizacaoEdi;
	private int tpBem;
	private int tpGarantia;
	private int tpGrau;
	private double vlJudicial;
	private double vlCredor;
	private GregorianCalendar dtEstimativa;
	private GregorianCalendar dtAvaliacaoJudicial;
	private GregorianCalendar dtAvaliacaoCredor;

	public BemPenhora(){ }
	
	public BemPenhora(int cdBem,
			int cdProcesso,
			int cdCidade,
			String nmBem,
			int stBem,
			double vlBem,
			String nmLogradouro,
			String nmBairro,
			String nmDistrito,
			String nrEndereco,
			String nmComplemento,
			String nmEndereco,
			GregorianCalendar dtAtualizacaoEdi,
			int stAtualizacaoEdi,
			int tpBem,
			int tpGarantia,
			int tpGrau,
			double vlJudicial,
			double vlCredor,
			GregorianCalendar dtEstimativa,
			GregorianCalendar dtAvaliacaoJudicial,
			GregorianCalendar dtAvaliacaoCredor){
		setCdBem(cdBem);
		setCdProcesso(cdProcesso);
		setCdCidade(cdCidade);
		setNmBem(nmBem);
		setStBem(stBem);
		setVlBem(vlBem);
		setNmLogradouro(nmLogradouro);
		setNmBairro(nmBairro);
		setNmDistrito(nmDistrito);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNmEndereco(nmEndereco);
		setDtAtualizacaoEdi(dtAtualizacaoEdi);
		setStAtualizacaoEdi(stAtualizacaoEdi);
		setTpBem(tpBem);
		setTpGarantia(tpGarantia);
		setTpGrau(tpGrau);
		setVlJudicial(vlJudicial);
		setVlCredor(vlCredor);
		setDtEstimativa(dtEstimativa);
		setDtAvaliacaoJudicial(dtAvaliacaoJudicial);
		setDtAvaliacaoCredor(dtAvaliacaoCredor);
	}
	public void setCdBem(int cdBem){
		this.cdBem=cdBem;
	}
	public int getCdBem(){
		return this.cdBem;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setNmBem(String nmBem){
		this.nmBem=nmBem;
	}
	public String getNmBem(){
		return this.nmBem;
	}
	public void setStBem(int stBem){
		this.stBem=stBem;
	}
	public int getStBem(){
		return this.stBem;
	}
	public void setVlBem(double vlBem){
		this.vlBem=vlBem;
	}
	public double getVlBem(){
		return this.vlBem;
	}
	public void setNmLogradouro(String nmLogradouro){
		this.nmLogradouro=nmLogradouro;
	}
	public String getNmLogradouro(){
		return this.nmLogradouro;
	}
	public void setNmBairro(String nmBairro){
		this.nmBairro=nmBairro;
	}
	public String getNmBairro(){
		return this.nmBairro;
	}
	public void setNmDistrito(String nmDistrito){
		this.nmDistrito=nmDistrito;
	}
	public String getNmDistrito(){
		return this.nmDistrito;
	}
	public void setNrEndereco(String nrEndereco){
		this.nrEndereco=nrEndereco;
	}
	public String getNrEndereco(){
		return this.nrEndereco;
	}
	public void setNmComplemento(String nmComplemento){
		this.nmComplemento=nmComplemento;
	}
	public String getNmComplemento(){
		return this.nmComplemento;
	}
	public void setNmEndereco(String nmEndereco){
		this.nmEndereco=nmEndereco;
	}
	public String getNmEndereco(){
		return this.nmEndereco;
	}
	public void setDtAtualizacaoEdi(GregorianCalendar dtAtualizacaoEdi){
		this.dtAtualizacaoEdi=dtAtualizacaoEdi;
	}
	public GregorianCalendar getDtAtualizacaoEdi(){
		return this.dtAtualizacaoEdi;
	}
	public void setStAtualizacaoEdi(int stAtualizacaoEdi){
		this.stAtualizacaoEdi=stAtualizacaoEdi;
	}
	public int getStAtualizacaoEdi(){
		return this.stAtualizacaoEdi;
	}
	public void setTpBem(int tpBem){
		this.tpBem=tpBem;
	}
	public int getTpBem(){
		return this.tpBem;
	}
	public void setTpGarantia(int tpGarantia){
		this.tpGarantia=tpGarantia;
	}
	public int getTpGarantia(){
		return this.tpGarantia;
	}
	public void setTpGrau(int tpGrau){
		this.tpGrau=tpGrau;
	}
	public int getTpGrau(){
		return this.tpGrau;
	}
	public void setVlJudicial(double vlJudicial){
		this.vlJudicial=vlJudicial;
	}
	public double getVlJudicial(){
		return this.vlJudicial;
	}
	public void setVlCredor(double vlCredor){
		this.vlCredor=vlCredor;
	}
	public double getVlCredor(){
		return this.vlCredor;
	}
	public void setDtEstimativa(GregorianCalendar dtEstimativa){
		this.dtEstimativa=dtEstimativa;
	}
	public GregorianCalendar getDtEstimativa(){
		return this.dtEstimativa;
	}
	public void setDtAvaliacaoJudicial(GregorianCalendar dtAvaliacaoJudicial){
		this.dtAvaliacaoJudicial=dtAvaliacaoJudicial;
	}
	public GregorianCalendar getDtAvaliacaoJudicial(){
		return this.dtAvaliacaoJudicial;
	}
	public void setDtAvaliacaoCredor(GregorianCalendar dtAvaliacaoCredor){
		this.dtAvaliacaoCredor=dtAvaliacaoCredor;
	}
	public GregorianCalendar getDtAvaliacaoCredor(){
		return this.dtAvaliacaoCredor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBem: " +  getCdBem();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", nmBem: " +  getNmBem();
		valueToString += ", stBem: " +  getStBem();
		valueToString += ", vlBem: " +  getVlBem();
		valueToString += ", nmLogradouro: " +  getNmLogradouro();
		valueToString += ", nmBairro: " +  getNmBairro();
		valueToString += ", nmDistrito: " +  getNmDistrito();
		valueToString += ", nrEndereco: " +  getNrEndereco();
		valueToString += ", nmComplemento: " +  getNmComplemento();
		valueToString += ", nmEndereco: " +  getNmEndereco();
		valueToString += ", dtAtualizacaoEdi: " +  sol.util.Util.formatDateTime(getDtAtualizacaoEdi(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAtualizacaoEdi: " +  getStAtualizacaoEdi();
		valueToString += ", tpBem: " +  getTpBem();
		valueToString += ", tpGarantia: " +  getTpGarantia();
		valueToString += ", tpGrau: " +  getTpGrau();
		valueToString += ", vlJudicial: " +  getVlJudicial();
		valueToString += ", vlCredor: " +  getVlCredor();
		valueToString += ", dtEstimativa: " +  sol.util.Util.formatDateTime(getDtEstimativa(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAvaliacaoJudicial: " +  sol.util.Util.formatDateTime(getDtAvaliacaoJudicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAvaliacaoCredor: " +  sol.util.Util.formatDateTime(getDtAvaliacaoCredor(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BemPenhora(getCdBem(),
			getCdProcesso(),
			getCdCidade(),
			getNmBem(),
			getStBem(),
			getVlBem(),
			getNmLogradouro(),
			getNmBairro(),
			getNmDistrito(),
			getNrEndereco(),
			getNmComplemento(),
			getNmEndereco(),
			getDtAtualizacaoEdi()==null ? null : (GregorianCalendar)getDtAtualizacaoEdi().clone(),
			getStAtualizacaoEdi(),
			getTpBem(),
			getTpGarantia(),
			getTpGrau(),
			getVlJudicial(),
			getVlCredor(),
			getDtEstimativa()==null ? null : (GregorianCalendar)getDtEstimativa().clone(),
			getDtAvaliacaoJudicial()==null ? null : (GregorianCalendar)getDtAvaliacaoJudicial().clone(),
			getDtAvaliacaoCredor()==null ? null : (GregorianCalendar)getDtAvaliacaoCredor().clone());
	}

}