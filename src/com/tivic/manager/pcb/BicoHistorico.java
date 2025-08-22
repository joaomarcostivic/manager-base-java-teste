package com.tivic.manager.pcb;

import java.util.GregorianCalendar;

public class BicoHistorico {

	private int cdBicoHistorico;
	private int cdBico;
	private int tpBicoHistorico;
	private GregorianCalendar dtBicoHistorico;
	private int cdTurno;
	private String idBicoAnterior;
	private String idBicoNovo;
	private float qtEncerranteFinal;
	private float qtEncerranteInicial;
	private float vlEncerranteFinal;
	private float vlEncerranteInicial;
	private String txtObservacao;
	private int nrIntervencao;
	private String dsMotivoIntervencao;
	private int cdTanqueAnterior;
	private int cdTanqueNovo;
	private int cdUsuario;
	private int cdTecnico;
	private int cdEmpresaInterventora;

	public BicoHistorico(int cdBicoHistorico,
			int cdBico,
			int tpBicoHistorico,
			GregorianCalendar dtBicoHistorico,
			int cdTurno,
			String idBicoAnterior,
			String idBicoNovo,
			float qtEncerranteFinal,
			float qtEncerranteInicial,
			float vlEncerranteFinal,
			float vlEncerranteInicial,
			String txtObservacao,
			int nrIntervencao,
			String dsMotivoIntervencao,
			int cdTanqueAnterior,
			int cdTanqueNovo,
			int cdUsuario,
			int cdTecnico,
			int cdEmpresaInterventora){
		setCdBicoHistorico(cdBicoHistorico);
		setCdBico(cdBico);
		setTpBicoHistorico(tpBicoHistorico);
		setDtBicoHistorico(dtBicoHistorico);
		setCdTurno(cdTurno);
		setIdBicoAnterior(idBicoAnterior);
		setIdBicoNovo(idBicoNovo);
		setQtEncerranteFinal(qtEncerranteFinal);
		setQtEncerranteInicial(qtEncerranteInicial);
		setVlEncerranteFinal(vlEncerranteFinal);
		setVlEncerranteInicial(vlEncerranteInicial);
		setTxtObservacao(txtObservacao);
		setNrIntervencao(nrIntervencao);
		setDsMotivoIntervencao(dsMotivoIntervencao);
		setCdTanqueAnterior(cdTanqueAnterior);
		setCdTanqueNovo(cdTanqueNovo);
		setCdUsuario(cdUsuario);
		setCdTecnico(cdTecnico);
		setCdEmpresaInterventora(cdEmpresaInterventora);
	}
	public void setCdBicoHistorico(int cdBicoHistorico){
		this.cdBicoHistorico=cdBicoHistorico;
	}
	public int getCdBicoHistorico(){
		return this.cdBicoHistorico;
	}
	public void setCdBico(int cdBico){
		this.cdBico=cdBico;
	}
	public int getCdBico(){
		return this.cdBico;
	}
	public void setTpBicoHistorico(int tpBicoHistorico){
		this.tpBicoHistorico=tpBicoHistorico;
	}
	public int getTpBicoHistorico(){
		return this.tpBicoHistorico;
	}
	public void setDtBicoHistorico(GregorianCalendar dtBicoHistorico){
		this.dtBicoHistorico=dtBicoHistorico;
	}
	public GregorianCalendar getDtBicoHistorico(){
		return this.dtBicoHistorico;
	}
	public void setCdTurno(int cdTurno){
		this.cdTurno=cdTurno;
	}
	public int getCdTurno(){
		return this.cdTurno;
	}
	public void setIdBicoAnterior(String idBicoAnterior){
		this.idBicoAnterior=idBicoAnterior;
	}
	public String getIdBicoAnterior(){
		return this.idBicoAnterior;
	}
	public void setIdBicoNovo(String idBicoNovo){
		this.idBicoNovo=idBicoNovo;
	}
	public String getIdBicoNovo(){
		return this.idBicoNovo;
	}
	public void setQtEncerranteFinal(float qtEncerranteFinal){
		this.qtEncerranteFinal=qtEncerranteFinal;
	}
	public float getQtEncerranteFinal(){
		return this.qtEncerranteFinal;
	}
	public void setQtEncerranteInicial(float qtEncerranteInicial){
		this.qtEncerranteInicial=qtEncerranteInicial;
	}
	public float getQtEncerranteInicial(){
		return this.qtEncerranteInicial;
	}
	public void setVlEncerranteFinal(float vlEncerranteFinal){
		this.vlEncerranteFinal=vlEncerranteFinal;
	}
	public float getVlEncerranteFinal(){
		return this.vlEncerranteFinal;
	}
	public void setVlEncerranteInicial(float vlEncerranteInicial){
		this.vlEncerranteInicial=vlEncerranteInicial;
	}
	public float getVlEncerranteInicial(){
		return this.vlEncerranteInicial;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrIntervencao(int nrIntervencao){
		this.nrIntervencao=nrIntervencao;
	}
	public int getNrIntervencao(){
		return this.nrIntervencao;
	}
	public void setDsMotivoIntervencao(String dsMotivoIntervencao){
		this.dsMotivoIntervencao=dsMotivoIntervencao;
	}
	public String getDsMotivoIntervencao(){
		return this.dsMotivoIntervencao;
	}
	public void setCdTanqueAnterior(int cdTanqueAnterior){
		this.cdTanqueAnterior=cdTanqueAnterior;
	}
	public int getCdTanqueAnterior(){
		return this.cdTanqueAnterior;
	}
	public void setCdTanqueNovo(int cdTanqueNovo){
		this.cdTanqueNovo=cdTanqueNovo;
	}
	public int getCdTanqueNovo(){
		return this.cdTanqueNovo;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdTecnico(int cdTecnico){
		this.cdTecnico=cdTecnico;
	}
	public int getCdTecnico(){
		return this.cdTecnico;
	}
	public void setCdEmpresaInterventora(int cdEmpresaInterventora){
		this.cdEmpresaInterventora=cdEmpresaInterventora;
	}
	public int getCdEmpresaInterventora(){
		return this.cdEmpresaInterventora;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBicoHistorico: " +  getCdBicoHistorico();
		valueToString += ", cdBico: " +  getCdBico();
		valueToString += ", tpBicoHistorico: " +  getTpBicoHistorico();
		valueToString += ", dtBicoHistorico: " +  sol.util.Util.formatDateTime(getDtBicoHistorico(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTurno: " +  getCdTurno();
		valueToString += ", idBicoAnterior: " +  getIdBicoAnterior();
		valueToString += ", idBicoNovo: " +  getIdBicoNovo();
		valueToString += ", qtEncerranteFinal: " +  getQtEncerranteFinal();
		valueToString += ", qtEncerranteInicial: " +  getQtEncerranteInicial();
		valueToString += ", vlEncerranteFinal: " +  getVlEncerranteFinal();
		valueToString += ", vlEncerranteInicial: " +  getVlEncerranteInicial();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrIntervencao: " +  getNrIntervencao();
		valueToString += ", dsMotivoIntervencao: " +  getDsMotivoIntervencao();
		valueToString += ", cdTanqueAnterior: " +  getCdTanqueAnterior();
		valueToString += ", cdTanqueNovo: " +  getCdTanqueNovo();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdTecnico: " +  getCdTecnico();
		valueToString += ", cdEmpresaInterventora: " +  getCdEmpresaInterventora();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BicoHistorico(getCdBicoHistorico(),
			getCdBico(),
			getTpBicoHistorico(),
			getDtBicoHistorico()==null ? null : (GregorianCalendar)getDtBicoHistorico().clone(),
			getCdTurno(),
			getIdBicoAnterior(),
			getIdBicoNovo(),
			getQtEncerranteFinal(),
			getQtEncerranteInicial(),
			getVlEncerranteFinal(),
			getVlEncerranteInicial(),
			getTxtObservacao(),
			getNrIntervencao(),
			getDsMotivoIntervencao(),
			getCdTanqueAnterior(),
			getCdTanqueNovo(),
			getCdUsuario(),
			getCdTecnico(),
			getCdEmpresaInterventora());
	}

}