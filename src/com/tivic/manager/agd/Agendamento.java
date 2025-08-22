package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class Agendamento {

	private int cdAgendamento;
	private String nmAgendamento;
	private String nmLocal;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int stAgendamento;
	private String txtAgendamento;
	private int lgLembrete;
	private int qtTempoLembrete;
	private int tpUnidadeTempoLembrete;
	private int lgAnexos;
	private GregorianCalendar dtCadastro;
	private int cdRecorrencia;
	private String idAgendamento;
	private int nrRecorrencia;
	private int cdTipoAgendamento;
	private int lgOriginal;
	private int cdAgenda;
	private String txtAbertura;
	private int cdMailing;
	private int cdDocumento;
	private GregorianCalendar dtLembrete;
	private int cdTipoSituacao;

	public Agendamento() { }
	
	@Deprecated
	public Agendamento(int cdAgendamento,
			String nmAgendamento,
			String nmLocal,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int stAgendamento,
			String txtAgendamento,
			int lgLembrete,
			int qtTempoLembrete,
			int tpUnidadeTempoLembrete,
			int lgAnexos,
			GregorianCalendar dtCadastro,
			int cdRecorrencia,
			String idAgendamento,
			int nrRecorrencia,
			int cdTipoAgendamento,
			int lgOriginal,
			int cdAgenda,
			int cdMailing,
			int cdDocumento,
			GregorianCalendar dtLembrete){
		setCdAgendamento(cdAgendamento);
		setNmAgendamento(nmAgendamento);
		setNmLocal(nmLocal);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setStAgendamento(stAgendamento);
		setTxtAgendamento(txtAgendamento);
		setLgLembrete(lgLembrete);
		setQtTempoLembrete(qtTempoLembrete);
		setTpUnidadeTempoLembrete(tpUnidadeTempoLembrete);
		setLgAnexos(lgAnexos);
		setDtCadastro(dtCadastro);
		setCdRecorrencia(cdRecorrencia);
		setIdAgendamento(idAgendamento);
		setNrRecorrencia(nrRecorrencia);
		setCdTipoAgendamento(cdTipoAgendamento);
		setLgOriginal(lgOriginal);
		setCdAgenda(cdAgenda);
		setCdMailing(cdMailing);
		setCdDocumento(cdDocumento);
		setDtLembrete(dtLembrete);
}

	public Agendamento(int cdAgendamento,
			String nmAgendamento,
			String nmLocal,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int stAgendamento,
			String txtAgendamento,
			int lgLembrete,
			int qtTempoLembrete,
			int tpUnidadeTempoLembrete,
			int lgAnexos,
			GregorianCalendar dtCadastro,
			int cdRecorrencia,
			String idAgendamento,
			int nrRecorrencia,
			int cdTipoAgendamento,
			int lgOriginal,
			int cdAgenda,
			String txtAbertura,
			int cdMailing,
			int cdDocumento,
			GregorianCalendar dtLembrete,
			int cdTipoSituacao) {
		setCdAgendamento(cdAgendamento);
		setNmAgendamento(nmAgendamento);
		setNmLocal(nmLocal);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setStAgendamento(stAgendamento);
		setTxtAgendamento(txtAgendamento);
		setLgLembrete(lgLembrete);
		setQtTempoLembrete(qtTempoLembrete);
		setTpUnidadeTempoLembrete(tpUnidadeTempoLembrete);
		setLgAnexos(lgAnexos);
		setDtCadastro(dtCadastro);
		setCdRecorrencia(cdRecorrencia);
		setIdAgendamento(idAgendamento);
		setNrRecorrencia(nrRecorrencia);
		setCdTipoAgendamento(cdTipoAgendamento);
		setLgOriginal(lgOriginal);
		setCdAgenda(cdAgenda);
		setTxtAbertura(txtAbertura);
		setCdMailing(cdMailing);
		setCdDocumento(cdDocumento);
		setDtLembrete(dtLembrete);
		setCdTipoSituacao(cdTipoSituacao);
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setNmAgendamento(String nmAgendamento){
		this.nmAgendamento=nmAgendamento;
	}
	public String getNmAgendamento(){
		return this.nmAgendamento;
	}
	public void setNmLocal(String nmLocal){
		this.nmLocal=nmLocal;
	}
	public String getNmLocal(){
		return this.nmLocal;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setStAgendamento(int stAgendamento){
		this.stAgendamento=stAgendamento;
	}
	public int getStAgendamento(){
		return this.stAgendamento;
	}
	public void setTxtAgendamento(String txtAgendamento){
		this.txtAgendamento=txtAgendamento;
	}
	public String getTxtAgendamento(){
		return this.txtAgendamento;
	}
	public void setLgLembrete(int lgLembrete){
		this.lgLembrete=lgLembrete;
	}
	public int getLgLembrete(){
		return this.lgLembrete;
	}
	public void setQtTempoLembrete(int qtTempoLembrete){
		this.qtTempoLembrete=qtTempoLembrete;
	}
	public int getQtTempoLembrete(){
		return this.qtTempoLembrete;
	}
	public void setTpUnidadeTempoLembrete(int tpUnidadeTempoLembrete){
		this.tpUnidadeTempoLembrete=tpUnidadeTempoLembrete;
	}
	public int getTpUnidadeTempoLembrete(){
		return this.tpUnidadeTempoLembrete;
	}
	public void setLgAnexos(int lgAnexos){
		this.lgAnexos=lgAnexos;
	}
	public int getLgAnexos(){
		return this.lgAnexos;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setCdRecorrencia(int cdRecorrencia){
		this.cdRecorrencia=cdRecorrencia;
	}
	public int getCdRecorrencia(){
		return this.cdRecorrencia;
	}
	public void setIdAgendamento(String idAgendamento){
		this.idAgendamento=idAgendamento;
	}
	public String getIdAgendamento(){
		return this.idAgendamento;
	}
	public void setNrRecorrencia(int nrRecorrencia){
		this.nrRecorrencia=nrRecorrencia;
	}
	public int getNrRecorrencia(){
		return this.nrRecorrencia;
	}
	public void setCdTipoAgendamento(int cdTipoAgendamento){
		this.cdTipoAgendamento=cdTipoAgendamento;
	}
	public int getCdTipoAgendamento(){
		return this.cdTipoAgendamento;
	}
	public void setLgOriginal(int lgOriginal){
		this.lgOriginal=lgOriginal;
	}
	public int getLgOriginal(){
		return this.lgOriginal;
	}
	public void setCdAgenda(int cdAgenda){
		this.cdAgenda=cdAgenda;
	}
	public int getCdAgenda(){
		return this.cdAgenda;
	}
	public void setTxtAbertura(String txtAbertura){
		this.txtAbertura=txtAbertura;
	}
	public String getTxtAbertura(){
		return this.txtAbertura;
	}
	public void setCdMailing(int cdMailing){
		this.cdMailing=cdMailing;
	}
	public int getCdMailing(){
		return this.cdMailing;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setDtLembrete(GregorianCalendar dtLembrete){
		this.dtLembrete=dtLembrete;
	}
	public GregorianCalendar getDtLembrete(){
		return this.dtLembrete;
	}
	public void setCdTipoSituacao(int cdTipoSituacao){
		this.cdTipoSituacao=cdTipoSituacao;
	}
	public int getCdTipoSituacao(){
		return this.cdTipoSituacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgendamento: " +  getCdAgendamento();
		valueToString += ", nmAgendamento: " +  getNmAgendamento();
		valueToString += ", nmLocal: " +  getNmLocal();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAgendamento: " +  getStAgendamento();
		valueToString += ", txtAgendamento: " +  getTxtAgendamento();
		valueToString += ", lgLembrete: " +  getLgLembrete();
		valueToString += ", qtTempoLembrete: " +  getQtTempoLembrete();
		valueToString += ", tpUnidadeTempoLembrete: " +  getTpUnidadeTempoLembrete();
		valueToString += ", lgAnexos: " +  getLgAnexos();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdRecorrencia: " +  getCdRecorrencia();
		valueToString += ", idAgendamento: " +  getIdAgendamento();
		valueToString += ", nrRecorrencia: " +  getNrRecorrencia();
		valueToString += ", cdTipoAgendamento: " +  getCdTipoAgendamento();
		valueToString += ", lgOriginal: " +  getLgOriginal();
		valueToString += ", cdAgenda: " +  getCdAgenda();
		valueToString += ", txtAbertura: " +  getTxtAbertura();
		valueToString += ", cdMailing: " +  getCdMailing();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", dtLembrete: " +  sol.util.Util.formatDateTime(getDtLembrete(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTipoSituacao: " +  getCdTipoSituacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Agendamento(getCdAgendamento(),
			getNmAgendamento(),
			getNmLocal(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getStAgendamento(),
			getTxtAgendamento(),
			getLgLembrete(),
			getQtTempoLembrete(),
			getTpUnidadeTempoLembrete(),
			getLgAnexos(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getCdRecorrencia(),
			getIdAgendamento(),
			getNrRecorrencia(),
			getCdTipoAgendamento(),
			getLgOriginal(),
			getCdAgenda(),
			getTxtAbertura(),
			getCdMailing(),
			getCdDocumento(),
			getDtLembrete()==null ? null : (GregorianCalendar)getDtLembrete().clone(),
			getCdTipoSituacao());
	}

}