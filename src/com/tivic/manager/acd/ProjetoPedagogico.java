package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class ProjetoPedagogico {

	private int cdProjetoPedagogico;
	private int cdInstituicao;
	private GregorianCalendar dtCadastro;
	private GregorianCalendar dtVigenciaInicial;
	private GregorianCalendar dtVigenciaFinal;
	private String txtProposta;
	private String nmProjetoPedagogico;
	private int cdPeriodoLetivo;

	public ProjetoPedagogico() { }

	public ProjetoPedagogico(int cdProjetoPedagogico,
			int cdInstituicao,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtVigenciaInicial,
			GregorianCalendar dtVigenciaFinal,
			String txtProposta,
			String nmProjetoPedagogico,
			int cdPeriodoLetivo) {
		setCdProjetoPedagogico(cdProjetoPedagogico);
		setCdInstituicao(cdInstituicao);
		setDtCadastro(dtCadastro);
		setDtVigenciaInicial(dtVigenciaInicial);
		setDtVigenciaFinal(dtVigenciaFinal);
		setTxtProposta(txtProposta);
		setNmProjetoPedagogico(nmProjetoPedagogico);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdProjetoPedagogico(int cdProjetoPedagogico){
		this.cdProjetoPedagogico=cdProjetoPedagogico;
	}
	public int getCdProjetoPedagogico(){
		return this.cdProjetoPedagogico;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setDtVigenciaInicial(GregorianCalendar dtVigenciaInicial){
		this.dtVigenciaInicial=dtVigenciaInicial;
	}
	public GregorianCalendar getDtVigenciaInicial(){
		return this.dtVigenciaInicial;
	}
	public void setDtVigenciaFinal(GregorianCalendar dtVigenciaFinal){
		this.dtVigenciaFinal=dtVigenciaFinal;
	}
	public GregorianCalendar getDtVigenciaFinal(){
		return this.dtVigenciaFinal;
	}
	public void setTxtProposta(String txtProposta){
		this.txtProposta=txtProposta;
	}
	public String getTxtProposta(){
		return this.txtProposta;
	}
	public void setNmProjetoPedagogico(String nmProjetoPedagogico){
		this.nmProjetoPedagogico=nmProjetoPedagogico;
	}
	public String getNmProjetoPedagogico(){
		return this.nmProjetoPedagogico;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProjetoPedagogico: " +  getCdProjetoPedagogico();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVigenciaInicial: " +  sol.util.Util.formatDateTime(getDtVigenciaInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVigenciaFinal: " +  sol.util.Util.formatDateTime(getDtVigenciaFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtProposta: " +  getTxtProposta();
		valueToString += ", nmProjetoPedagogico: " +  getNmProjetoPedagogico();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProjetoPedagogico(getCdProjetoPedagogico(),
			getCdInstituicao(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtVigenciaInicial()==null ? null : (GregorianCalendar)getDtVigenciaInicial().clone(),
			getDtVigenciaFinal()==null ? null : (GregorianCalendar)getDtVigenciaFinal().clone(),
			getTxtProposta(),
			getNmProjetoPedagogico(),
			getCdPeriodoLetivo());
	}

}