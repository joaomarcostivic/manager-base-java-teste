package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoEducacensoArquivo {

	private int cdInstituicao;
	private int cdPeriodoLetivo;
	private int prRegistro;
	private float prCampos;
	private GregorianCalendar dtAtualizacao;
	private int lgExecucao;

	public InstituicaoEducacensoArquivo() { }

	public InstituicaoEducacensoArquivo(int cdInstituicao,
			int cdPeriodoLetivo,
			int prRegistro,
			float prCampos,
			GregorianCalendar dtAtualizacao,
			int lgExecucao) {
		setCdInstituicao(cdInstituicao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setPrRegistro(prRegistro);
		setPrCampos(prCampos);
		setDtAtualizacao(dtAtualizacao);
		setLgExecucao(lgExecucao);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setPrRegistro(int prRegistro){
		this.prRegistro=prRegistro;
	}
	public int getPrRegistro(){
		return this.prRegistro;
	}
	public void setPrCampos(float prCampos){
		this.prCampos=prCampos;
	}
	public float getPrCampos(){
		return this.prCampos;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao){
		this.dtAtualizacao=dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao(){
		return this.dtAtualizacao;
	}
	public void setLgExecucao(int lgExecucao){
		this.lgExecucao=lgExecucao;
	}
	public int getLgExecucao(){
		return this.lgExecucao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", prRegistro: " +  getPrRegistro();
		valueToString += ", prCampos: " +  getPrCampos();
		valueToString += ", dtAtualizacao: " +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgExecucao: " +  getLgExecucao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoEducacensoArquivo(getCdInstituicao(),
			getCdPeriodoLetivo(),
			getPrRegistro(),
			getPrCampos(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getLgExecucao());
	}

}