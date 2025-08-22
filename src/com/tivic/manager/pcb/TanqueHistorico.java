package com.tivic.manager.pcb;

import java.util.GregorianCalendar;

public class TanqueHistorico {

	private int cdTanqueHistorico;
	private int cdTanque;
	private int tpTanqueHistorico;
	private GregorianCalendar dtTanqueHistorico;
	private float qtLastro;
	private int stTanque;
	private GregorianCalendar dtCadastro;
	private GregorianCalendar dtInstalacao;
	private String txtObservacao;
	private int cdTipoTanque;
	private String nmLocalArmazenamento;
	private String idLocalArmazenamento;
	private int cdCombustivelAnterior;
	private int cdCombustivelNovo;
	private int cdTurno;
	private int cdUsuario;
	private int nrIntervencao;
	private String dsMotivoIntervencao;
	private int cdTecnico;
	private int cdEmpresaInterventora;

	public TanqueHistorico(int cdTanqueHistorico,
			int cdTanque,
			int tpTanqueHistorico,
			GregorianCalendar dtTanqueHistorico,
			float qtLastro,
			int stTanque,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtInstalacao,
			String txtObservacao,
			int cdTipoTanque,
			String nmLocalArmazenamento,
			String idLocalArmazenamento,
			int cdCombustivelAnterior,
			int cdCombustivelNovo,
			int cdTurno,
			int cdUsuario,
			int nrIntervencao,
			String dsMotivoIntervencao,
			int cdTecnico,
			int cdEmpresaInterventora){
		setCdTanqueHistorico(cdTanqueHistorico);
		setCdTanque(cdTanque);
		setTpTanqueHistorico(tpTanqueHistorico);
		setDtTanqueHistorico(dtTanqueHistorico);
		setQtLastro(qtLastro);
		setStTanque(stTanque);
		setDtCadastro(dtCadastro);
		setDtInstalacao(dtInstalacao);
		setTxtObservacao(txtObservacao);
		setCdTipoTanque(cdTipoTanque);
		setNmLocalArmazenamento(nmLocalArmazenamento);
		setIdLocalArmazenamento(idLocalArmazenamento);
		setCdCombustivelAnterior(cdCombustivelAnterior);
		setCdCombustivelNovo(cdCombustivelNovo);
		setCdTurno(cdTurno);
		setCdUsuario(cdUsuario);
		setNrIntervencao(nrIntervencao);
		setDsMotivoIntervencao(dsMotivoIntervencao);
		setCdTecnico(cdTecnico);
		setCdEmpresaInterventora(cdEmpresaInterventora);
	}
	public String getNmLocalArmazenamento() {
		return nmLocalArmazenamento;
	}
	public void setNmLocalArmazenamento(String nmLocalArmazenamento) {
		this.nmLocalArmazenamento = nmLocalArmazenamento;
	}
	public String getIdLocalArmazenamento() {
		return idLocalArmazenamento;
	}
	public void setIdLocalArmazenamento(String idLocalArmazenamento) {
		this.idLocalArmazenamento = idLocalArmazenamento;
	}
	public void setCdTanqueHistorico(int cdTanqueHistorico){
		this.cdTanqueHistorico=cdTanqueHistorico;
	}
	public int getCdTanqueHistorico(){
		return this.cdTanqueHistorico;
	}
	public void setCdTanque(int cdTanque){
		this.cdTanque=cdTanque;
	}
	public int getCdTanque(){
		return this.cdTanque;
	}
	public void setTpTanqueHistorico(int tpTanqueHistorico){
		this.tpTanqueHistorico=tpTanqueHistorico;
	}
	public int getTpTanqueHistorico(){
		return this.tpTanqueHistorico;
	}
	public void setDtTanqueHistorico(GregorianCalendar dtTanqueHistorico){
		this.dtTanqueHistorico=dtTanqueHistorico;
	}
	public GregorianCalendar getDtTanqueHistorico(){
		return this.dtTanqueHistorico;
	}
	public void setQtLastro(float qtLastro){
		this.qtLastro=qtLastro;
	}
	public float getQtLastro(){
		return this.qtLastro;
	}
	public void setStTanque(int stTanque){
		this.stTanque=stTanque;
	}
	public int getStTanque(){
		return this.stTanque;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setDtInstalacao(GregorianCalendar dtInstalacao){
		this.dtInstalacao=dtInstalacao;
	}
	public GregorianCalendar getDtInstalacao(){
		return this.dtInstalacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdTipoTanque(int cdTipoTanque){
		this.cdTipoTanque=cdTipoTanque;
	}
	public int getCdTipoTanque(){
		return this.cdTipoTanque;
	}
	public void setCdCombustivelAnterior(int cdCombustivelAnterior){
		this.cdCombustivelAnterior=cdCombustivelAnterior;
	}
	public int getCdCombustivelAnterior(){
		return this.cdCombustivelAnterior;
	}
	public void setCdCombustivelNovo(int cdCombustivelNovo){
		this.cdCombustivelNovo=cdCombustivelNovo;
	}
	public int getCdCombustivelNovo(){
		return this.cdCombustivelNovo;
	}
	public void setCdTurno(int cdTurno){
		this.cdTurno=cdTurno;
	}
	public int getCdTurno(){
		return this.cdTurno;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
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
		valueToString += "cdTanqueHistorico: " +  getCdTanqueHistorico();
		valueToString += ", cdTanque: " +  getCdTanque();
		valueToString += ", tpTanqueHistorico: " +  getTpTanqueHistorico();
		valueToString += ", dtTanqueHistorico: " +  sol.util.Util.formatDateTime(getDtTanqueHistorico(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtLastro: " +  getQtLastro();
		valueToString += ", stTanque: " +  getStTanque();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInstalacao: " +  sol.util.Util.formatDateTime(getDtInstalacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdTipoTanque: " +  getCdTipoTanque();
		valueToString += ", nmLocalArmazenamento: " +  getNmLocalArmazenamento();
		valueToString += ", idLocalArmazenamento: " +  getIdLocalArmazenamento();
		valueToString += ", cdCombustivelAnterior: " +  getCdCombustivelAnterior();
		valueToString += ", cdCombustivelNovo: " +  getCdCombustivelNovo();
		valueToString += ", cdTurno: " +  getCdTurno();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", nrIntervencao: " +  getNrIntervencao();
		valueToString += ", dsMotivoIntervencao: " +  getDsMotivoIntervencao();
		valueToString += ", cdTecnico: " +  getCdTecnico();
		valueToString += ", cdEmpresaInterventora: " +  getCdEmpresaInterventora();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TanqueHistorico(getCdTanqueHistorico(),
			getCdTanque(),
			getTpTanqueHistorico(),
			getDtTanqueHistorico()==null ? null : (GregorianCalendar)getDtTanqueHistorico().clone(),
			getQtLastro(),
			getStTanque(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtInstalacao()==null ? null : (GregorianCalendar)getDtInstalacao().clone(),
			getTxtObservacao(),
			getCdTipoTanque(),
			getNmLocalArmazenamento(),
			getIdLocalArmazenamento(),
			getCdCombustivelAnterior(),
			getCdCombustivelNovo(),
			getCdTurno(),
			getCdUsuario(),
			getNrIntervencao(),
			getDsMotivoIntervencao(),
			getCdTecnico(),
			getCdEmpresaInterventora());
	}

}
