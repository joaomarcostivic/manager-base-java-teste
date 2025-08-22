package com.tivic.manager.pcb;

import java.util.GregorianCalendar;

public class Tanque extends com.tivic.manager.alm.LocalArmazenamento {

	private int cdTanque;
	private float qtLastro;
	private int stTanque;
	private GregorianCalendar dtCadastro;
	private GregorianCalendar dtInstalacao;
	private String txtObservacao;
	private int cdTipoTanque;
	private int cdProdutoServico;

	public Tanque(int cdLocalArmazenamento,
			int cdSetor,
			int cdNivelLocal,
			int cdResponsavel,
			String nmLocalArmazenamento,
			String idLocalArmazenamento,
			int cdLocalArmazenamentoSuperior,
			int cdEmpresa,
			float qtLastro,
			int stTanque,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtInstalacao,
			String txtObservacao,
			int cdTipoTanque,
			int cdProdutoServico){
		super(cdLocalArmazenamento,
			cdSetor,
			cdNivelLocal,
			cdResponsavel,
			nmLocalArmazenamento,
			idLocalArmazenamento,
			cdLocalArmazenamentoSuperior,
			cdEmpresa);
		setCdTanque(cdLocalArmazenamento);
		setQtLastro(qtLastro);
		setStTanque(stTanque);
		setDtCadastro(dtCadastro);
		setDtInstalacao(dtInstalacao);
		setTxtObservacao(txtObservacao);
		setCdTipoTanque(cdTipoTanque);
		setCdProdutoServico(cdProdutoServico);
	}
	public void setCdTanque(int cdTanque){
		this.cdTanque=cdTanque;
	}
	public int getCdTanque(){
		return this.cdTanque;
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
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTanque: " +  getCdTanque();
		valueToString += ", qtLastro: " +  getQtLastro();
		valueToString += ", stTanque: " +  getStTanque();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInstalacao: " +  sol.util.Util.formatDateTime(getDtInstalacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdTipoTanque: " +  getCdTipoTanque();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Tanque(getCdLocalArmazenamento(),
			getCdSetor(),
			getCdNivelLocal(),
			getCdResponsavel(),
			getNmLocalArmazenamento(),
			getIdLocalArmazenamento(),
			getCdLocalArmazenamentoSuperior(),
			getCdEmpresa(),
			getQtLastro(),
			getStTanque(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtInstalacao()==null ? null : (GregorianCalendar)getDtInstalacao().clone(),
			getTxtObservacao(),
			getCdTipoTanque(),
			getCdProdutoServico());
	}

}