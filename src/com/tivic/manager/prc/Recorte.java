package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class Recorte {

	private int cdRecorte;
	private String idRecorte;
	private String txtRecorte;
	private int cdProcesso;
	private GregorianCalendar dtProcessamento;
	private GregorianCalendar dtBusca;
	private int stRecorte;
	private int cdServico;
	private GregorianCalendar dtPublicacao;
	private String nmDiario;
	private String nrPagina;
	private String nmOrgao;
	private String nmJuizo;
	private String txtAndamento;
	private int cdEstado;
	private int stAnterior;
	private String idProcessoRecorte;

	public Recorte() { }

	public Recorte(int cdRecorte,
			String idRecorte,
			String txtRecorte,
			int cdProcesso,
			GregorianCalendar dtProcessamento,
			GregorianCalendar dtBusca,
			int stRecorte,
			int cdServico,
			GregorianCalendar dtPublicacao,
			String nmDiario,
			String nrPagina,
			String nmOrgao,
			String nmJuizo,
			String txtAndamento,
			int cdEstado,
			int stAnterior,
			String idProcessoRecorte) {
		setCdRecorte(cdRecorte);
		setIdRecorte(idRecorte);
		setTxtRecorte(txtRecorte);
		setCdProcesso(cdProcesso);
		setDtProcessamento(dtProcessamento);
		setDtBusca(dtBusca);
		setStRecorte(stRecorte);
		setCdServico(cdServico);
		setDtPublicacao(dtPublicacao);
		setNmDiario(nmDiario);
		setNrPagina(nrPagina);
		setNmOrgao(nmOrgao);
		setNmJuizo(nmJuizo);
		setTxtAndamento(txtAndamento);
		setCdEstado(cdEstado);
		setStAnterior(stAnterior);
		setIdProcessoRecorte(idProcessoRecorte);
	}
	public void setCdRecorte(int cdRecorte){
		this.cdRecorte=cdRecorte;
	}
	public int getCdRecorte(){
		return this.cdRecorte;
	}
	public void setIdRecorte(String idRecorte){
		this.idRecorte=idRecorte;
	}
	public String getIdRecorte(){
		return this.idRecorte;
	}
	public void setTxtRecorte(String txtRecorte){
		this.txtRecorte=txtRecorte;
	}
	public String getTxtRecorte(){
		return this.txtRecorte;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setDtProcessamento(GregorianCalendar dtProcessamento){
		this.dtProcessamento=dtProcessamento;
	}
	public GregorianCalendar getDtProcessamento(){
		return this.dtProcessamento;
	}
	public void setDtBusca(GregorianCalendar dtBusca){
		this.dtBusca=dtBusca;
	}
	public GregorianCalendar getDtBusca(){
		return this.dtBusca;
	}
	public void setStRecorte(int stRecorte){
		this.stRecorte=stRecorte;
	}
	public int getStRecorte(){
		return this.stRecorte;
	}
	public void setCdServico(int cdServico){
		this.cdServico=cdServico;
	}
	public int getCdServico(){
		return this.cdServico;
	}
	public void setDtPublicacao(GregorianCalendar dtPublicacao){
		this.dtPublicacao=dtPublicacao;
	}
	public GregorianCalendar getDtPublicacao(){
		return this.dtPublicacao;
	}
	public void setNmDiario(String nmDiario){
		this.nmDiario=nmDiario;
	}
	public String getNmDiario(){
		return this.nmDiario;
	}
	public void setNrPagina(String nrPagina){
		this.nrPagina=nrPagina;
	}
	public String getNrPagina(){
		return this.nrPagina;
	}
	public void setNmOrgao(String nmOrgao){
		this.nmOrgao=nmOrgao;
	}
	public String getNmOrgao(){
		return this.nmOrgao;
	}
	public void setNmJuizo(String nmJuizo){
		this.nmJuizo=nmJuizo;
	}
	public String getNmJuizo(){
		return this.nmJuizo;
	}
	public void setTxtAndamento(String txtAndamento){
		this.txtAndamento=txtAndamento;
	}
	public String getTxtAndamento(){
		return this.txtAndamento;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setStAnterior(int stAnterior){
		this.stAnterior=stAnterior;
	}
	public int getStAnterior(){
		return this.stAnterior;
	}
	public void setIdProcessoRecorte(String idProcessoRecorte){
		this.idProcessoRecorte=idProcessoRecorte;
	}
	public String getIdProcessoRecorte(){
		return this.idProcessoRecorte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRecorte: " +  getCdRecorte();
		valueToString += ", idRecorte: " +  getIdRecorte();
		valueToString += ", txtRecorte: " +  getTxtRecorte();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", dtProcessamento: " +  sol.util.Util.formatDateTime(getDtProcessamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtBusca: " +  sol.util.Util.formatDateTime(getDtBusca(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stRecorte: " +  getStRecorte();
		valueToString += ", cdServico: " +  getCdServico();
		valueToString += ", dtPublicacao: " +  sol.util.Util.formatDateTime(getDtPublicacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmDiario: " +  getNmDiario();
		valueToString += ", nrPagina: " +  getNrPagina();
		valueToString += ", nmOrgao: " +  getNmOrgao();
		valueToString += ", nmJuizo: " +  getNmJuizo();
		valueToString += ", txtAndamento: " +  getTxtAndamento();
		valueToString += ", cdEstado: " +  getCdEstado();
		valueToString += ", stAnterior: " +  getStAnterior();
		valueToString += ", idProcessoRecorte: " +  getIdProcessoRecorte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Recorte(getCdRecorte(),
			getIdRecorte(),
			getTxtRecorte(),
			getCdProcesso(),
			getDtProcessamento()==null ? null : (GregorianCalendar)getDtProcessamento().clone(),
			getDtBusca()==null ? null : (GregorianCalendar)getDtBusca().clone(),
			getStRecorte(),
			getCdServico(),
			getDtPublicacao()==null ? null : (GregorianCalendar)getDtPublicacao().clone(),
			getNmDiario(),
			getNrPagina(),
			getNmOrgao(),
			getNmJuizo(),
			getTxtAndamento(),
			getCdEstado(),
			getStAnterior(),
			getIdProcessoRecorte());
	}

}