package com.tivic.manager.agd;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.prc.TipoPrazo;
import com.tivic.manager.prc.TipoPrazoDAO;
import com.tivic.manager.prc.TipoPrazoServices;
import com.tivic.sol.serializer.CalendarSerializer;
import com.tivic.manager.util.Util;

public class AgendaItem {

	private int cdAgendaItem;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInicial;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtFinal;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLancamento;
	private String dsDetalhe;
	private int stAgendaItem;
	private int cdTipoPrazo;
	private int cdPessoa;
	private int cdProcesso;
	private int cdTipoAgendamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtRealizacao;
	private String dsObservacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAlarme;
	private int cdUsuario;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAlteracao;
	private String dsAssunto;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAceite;
	private int cdDocumento;
	private int cdTipoPrazoDocumento;
	private int cdUsuarioAceite;
	private int cdLocal;
	private int cdEmpresa;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtRejeite;
	private int cdUsuarioRejeite;
	private Double vlServico;
	private String txtParecer;
	private int lgPreposto;
	private int cdUsuarioCumprimento;
	private int cdGrupoTrabalho;
	private int cdAgendaItemSuperior;
	private int qtPreposto;

	public AgendaItem() { }

	public AgendaItem(int cdAgendaItem,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			GregorianCalendar dtLancamento,
			String dsDetalhe,
			int stAgendaItem,
			int cdTipoPrazo,
			int cdPessoa,
			int cdProcesso,
			int cdTipoAgendamento,
			GregorianCalendar dtRealizacao,
			String dsObservacao,
			GregorianCalendar dtAlarme,
			int cdUsuario,
			GregorianCalendar dtAlteracao,
			String dsAssunto,
			GregorianCalendar dtAceite,
			int cdDocumento,
			int cdTipoPrazoDocumento,
			int cdUsuarioAceite,
			int cdLocal,
			int cdEmpresa,
			GregorianCalendar dtRejeite,
			int cdUsuarioRejeite,
			Double vlServico,
			String txtParecer,
			int lgPreposto,
			int cdUsuarioCumprimento,
			int cdGrupoTrabalho,
			int cdAgendaItemSuperior,
			int qtPreposto) {
		setCdAgendaItem(cdAgendaItem);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setDtLancamento(dtLancamento);
		setDsDetalhe(dsDetalhe);
		setStAgendaItem(stAgendaItem);
		setCdTipoPrazo(cdTipoPrazo);
		setCdPessoa(cdPessoa);
		setCdProcesso(cdProcesso);
		setCdTipoAgendamento(cdTipoAgendamento);
		setDtRealizacao(dtRealizacao);
		setDsObservacao(dsObservacao);
		setDtAlarme(dtAlarme);
		setCdUsuario(cdUsuario);
		setDtAlteracao(dtAlteracao);
		setDsAssunto(dsAssunto);
		setDtAceite(dtAceite);
		setCdDocumento(cdDocumento);
		setCdTipoPrazoDocumento(cdTipoPrazoDocumento);
		setCdUsuarioAceite(cdUsuarioAceite);
		setCdLocal(cdLocal);
		setCdEmpresa(cdEmpresa);
		setDtRejeite(dtRejeite);
		setCdUsuarioRejeite(cdUsuarioRejeite);
		setVlServico(vlServico);
		setTxtParecer(txtParecer);
		setLgPreposto(lgPreposto);
		setCdUsuarioCumprimento(cdUsuarioCumprimento);
		setCdGrupoTrabalho(cdGrupoTrabalho);
		setCdAgendaItemSuperior(cdAgendaItemSuperior);
		setQtPreposto(qtPreposto);
	}
	public void setCdAgendaItem(int cdAgendaItem){
		this.cdAgendaItem=cdAgendaItem;
	}
	public int getCdAgendaItem(){
		return this.cdAgendaItem;
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
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setDsDetalhe(String dsDetalhe){
		this.dsDetalhe=dsDetalhe;
	}
	public String getDsDetalhe(){
		return this.dsDetalhe;
	}
	public void setStAgendaItem(int stAgendaItem){
		this.stAgendaItem=stAgendaItem;
	}
	public int getStAgendaItem(){
		return this.stAgendaItem;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdTipoAgendamento(int cdTipoAgendamento){
		this.cdTipoAgendamento=cdTipoAgendamento;
	}
	public int getCdTipoAgendamento(){
		return this.cdTipoAgendamento;
	}
	public void setDtRealizacao(GregorianCalendar dtRealizacao){
		this.dtRealizacao=dtRealizacao;
	}
	public GregorianCalendar getDtRealizacao(){
		return this.dtRealizacao;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setDtAlarme(GregorianCalendar dtAlarme){
		this.dtAlarme=dtAlarme;
	}
	public GregorianCalendar getDtAlarme(){
		return this.dtAlarme;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setDtAlteracao(GregorianCalendar dtAlteracao){
		this.dtAlteracao=dtAlteracao;
	}
	public GregorianCalendar getDtAlteracao(){
		return this.dtAlteracao;
	}
	public void setDsAssunto(String dsAssunto){
		this.dsAssunto=dsAssunto;
	}
	public String getDsAssunto(){
		return this.dsAssunto;
	}
	public void setDtAceite(GregorianCalendar dtAceite){
		this.dtAceite=dtAceite;
	}
	public GregorianCalendar getDtAceite(){
		return this.dtAceite;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdTipoPrazoDocumento(int cdTipoPrazoDocumento){
		this.cdTipoPrazoDocumento=cdTipoPrazoDocumento;
	}
	public int getCdTipoPrazoDocumento(){
		return this.cdTipoPrazoDocumento;
	}
	public void setCdUsuarioAceite(int cdUsuarioAceite){
		this.cdUsuarioAceite=cdUsuarioAceite;
	}
	public int getCdUsuarioAceite(){
		return this.cdUsuarioAceite;
	}
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setDtRejeite(GregorianCalendar dtRejeite){
		this.dtRejeite=dtRejeite;
	}
	public GregorianCalendar getDtRejeite(){
		return this.dtRejeite;
	}
	public void setCdUsuarioRejeite(int cdUsuarioRejeite){
		this.cdUsuarioRejeite=cdUsuarioRejeite;
	}
	public int getCdUsuarioRejeite(){
		return this.cdUsuarioRejeite;
	}
	public void setVlServico(Double vlServico){
		this.vlServico=vlServico;
	}
	public Double getVlServico(){
		return this.vlServico;
	}
	public void setTxtParecer(String txtParecer){
		this.txtParecer=txtParecer;
	}
	public String getTxtParecer(){
		return this.txtParecer;
	}
	public void setLgPreposto(int lgPreposto){
		this.lgPreposto=lgPreposto;
	}
	public int getLgPreposto(){
		return this.lgPreposto;
	}
	public void setCdUsuarioCumprimento(int cdUsuarioCumprimento){
		this.cdUsuarioCumprimento=cdUsuarioCumprimento;
	}
	public int getCdUsuarioCumprimento(){
		return this.cdUsuarioCumprimento;
	}
	public void setCdGrupoTrabalho(int cdGrupoTrabalho){
		this.cdGrupoTrabalho=cdGrupoTrabalho;
	}
	public int getCdGrupoTrabalho(){
		return this.cdGrupoTrabalho;
	}
	public void setCdAgendaItemSuperior(int cdAgendaItemSuperior){
		this.cdAgendaItemSuperior=cdAgendaItemSuperior;
	}
	public int getCdAgendaItemSuperior(){
		return this.cdAgendaItemSuperior;
	}
	public void setQtPreposto(int qtPreposto){
		this.qtPreposto=qtPreposto;
	}
	public int getQtPreposto(){
		return this.qtPreposto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgendaItem: " +  getCdAgendaItem();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsDetalhe: " +  getDsDetalhe();
		valueToString += ", stAgendaItem: " +  getStAgendaItem();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdTipoAgendamento: " +  getCdTipoAgendamento();
		valueToString += ", dtRealizacao: " +  sol.util.Util.formatDateTime(getDtRealizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", dtAlarme: " +  sol.util.Util.formatDateTime(getDtAlarme(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtAlteracao: " +  sol.util.Util.formatDateTime(getDtAlteracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsAssunto: " +  getDsAssunto();
		valueToString += ", dtAceite: " +  sol.util.Util.formatDateTime(getDtAceite(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdTipoPrazoDocumento: " +  getCdTipoPrazoDocumento();
		valueToString += ", cdUsuarioAceite: " +  getCdUsuarioAceite();
		valueToString += ", cdLocal: " +  getCdLocal();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", dtRejeite: " +  sol.util.Util.formatDateTime(getDtRejeite(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuarioRejeite: " +  getCdUsuarioRejeite();
		valueToString += ", vlServico: " +  getVlServico();
		valueToString += ", txtParecer: " +  getTxtParecer();
		valueToString += ", lgPreposto: " +  getLgPreposto();
		valueToString += ", cdUsuarioCumprimento: " +  getCdUsuarioCumprimento();
		valueToString += ", cdGrupoTrabalho: " +  getCdGrupoTrabalho();
		valueToString += ", cdAgendaItemSuperior: " +  getCdAgendaItemSuperior();
		valueToString += ", qtPreposto: " +  getQtPreposto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendaItem(getCdAgendaItem(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getDsDetalhe(),
			getStAgendaItem(),
			getCdTipoPrazo(),
			getCdPessoa(),
			getCdProcesso(),
			getCdTipoAgendamento(),
			getDtRealizacao()==null ? null : (GregorianCalendar)getDtRealizacao().clone(),
			getDsObservacao(),
			getDtAlarme()==null ? null : (GregorianCalendar)getDtAlarme().clone(),
			getCdUsuario(),
			getDtAlteracao()==null ? null : (GregorianCalendar)getDtAlteracao().clone(),
			getDsAssunto(),
			getDtAceite()==null ? null : (GregorianCalendar)getDtAceite().clone(),
			getCdDocumento(),
			getCdTipoPrazoDocumento(),
			getCdUsuarioAceite(),
			getCdLocal(),
			getCdEmpresa(),
			getDtRejeite()==null ? null : (GregorianCalendar)getDtRejeite().clone(),
			getCdUsuarioRejeite(),
			getVlServico(),
			getTxtParecer(),
			getLgPreposto(),
			getCdUsuarioCumprimento(),
			getCdGrupoTrabalho(),
			getCdAgendaItemSuperior(),
			getQtPreposto());
	}
	
	public String getResumo(Connection connect) {
		connect = (connect==null ? Conexao.conectar() : connect);
		TipoPrazo tipo = TipoPrazoDAO.get(getCdTipoPrazo(), connect);
		String nmResponsavel = null;
		Pessoa resp = PessoaDAO.get(getCdPessoa(), connect);
		Grupo grupoResp = GrupoDAO.get(getCdGrupoTrabalho(), connect);
		if(resp!=null)
			nmResponsavel = resp.getNmPessoa();
		else if(grupoResp!=null)
			nmResponsavel = grupoResp.getNmGrupo();
		Processo processo = ProcessoDAO.get(getCdProcesso(), connect);
		String nmCliente = (processo!=null ? " - "+ProcessoServices.getClientes(getCdProcesso(), connect) : "");
		
		return (tipo!=null ? "["+TipoPrazoServices.tiposAgendaItem[tipo.getTpAgendaItem()]+"] "+tipo.getNmTipoPrazo() : "")+
			   (getStAgendaItem()==AgendaItemServices.ST_AGENDA_CUMPRIDO ? 
					   " - em "+(Util.formatDate(getDtRealizacao(), "dd/MM/yyyy HH:mm"))
					   :
					   " - até "+(Util.formatDate(getDtFinal(), "dd/MM/yyyy HH:mm"))
				)+"\n"+
			   (nmResponsavel!=null ? "Responsável: "+nmResponsavel+"\n" : "")+
			   (processo!=null ? "Processo Nº "+processo.getNrProcesso()+nmCliente : "");
	}

}