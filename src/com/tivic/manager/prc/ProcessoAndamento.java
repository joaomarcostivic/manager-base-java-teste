package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class ProcessoAndamento {

	private int cdAndamento;
	private int cdProcesso;
	private int cdTipoAndamento;
	private GregorianCalendar dtAndamento;
	private GregorianCalendar dtLancamento;
	private String txtAndamento;
	private int stAndamento;
	private int tpInstancia;
	private String txtAta;
	private int cdUsuario;
	private GregorianCalendar dtAlteracao;
	private int tpOrigem;
	private byte[] blbAta;
	private int tpVisibilidade;
	private int tpEventoFinanceiro;
	private Double vlEventoFinanceiro;
	private int cdContaPagar;
	private int cdContaReceber;
	private GregorianCalendar dtAtualizacaoEdi;
	private int stAtualizacaoEdi;
	private String txtPublicacao;
	private int cdDocumento;
	private int cdOrigemAndamento;
	private int cdRecorte;

	public ProcessoAndamento() { }

	public ProcessoAndamento(int cdAndamento,
			int cdProcesso,
			int cdTipoAndamento,
			GregorianCalendar dtAndamento,
			GregorianCalendar dtLancamento,
			String txtAndamento,
			int stAndamento,
			int tpInstancia,
			String txtAta,
			int cdUsuario,
			GregorianCalendar dtAlteracao,
			int tpOrigem,
			byte[] blbAta,
			int tpVisibilidade,
			int tpEventoFinanceiro,
			Double vlEventoFinanceiro,
			int cdContaPagar,
			int cdContaReceber,
			GregorianCalendar dtAtualizacaoEdi,
			int stAtualizacaoEdi,
			String txtPublicacao,
			int cdDocumento,
			int cdOrigemAndamento,
			int cdRecorte) {
		setCdAndamento(cdAndamento);
		setCdProcesso(cdProcesso);
		setCdTipoAndamento(cdTipoAndamento);
		setDtAndamento(dtAndamento);
		setDtLancamento(dtLancamento);
		setTxtAndamento(txtAndamento);
		setStAndamento(stAndamento);
		setTpInstancia(tpInstancia);
		setTxtAta(txtAta);
		setCdUsuario(cdUsuario);
		setDtAlteracao(dtAlteracao);
		setTpOrigem(tpOrigem);
		setBlbAta(blbAta);
		setTpVisibilidade(tpVisibilidade);
		setTpEventoFinanceiro(tpEventoFinanceiro);
		setVlEventoFinanceiro(vlEventoFinanceiro);
		setCdContaPagar(cdContaPagar);
		setCdContaReceber(cdContaReceber);
		setDtAtualizacaoEdi(dtAtualizacaoEdi);
		setStAtualizacaoEdi(stAtualizacaoEdi);
		setTxtPublicacao(txtPublicacao);
		setCdDocumento(cdDocumento);
		setCdOrigemAndamento(cdOrigemAndamento);
		setCdRecorte(cdRecorte);
	}
	public void setCdAndamento(int cdAndamento){
		this.cdAndamento=cdAndamento;
	}
	public int getCdAndamento(){
		return this.cdAndamento;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setDtAndamento(GregorianCalendar dtAndamento){
		this.dtAndamento=dtAndamento;
	}
	public GregorianCalendar getDtAndamento(){
		return this.dtAndamento;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setTxtAndamento(String txtAndamento){
		this.txtAndamento=txtAndamento;
	}
	public String getTxtAndamento(){
		return this.txtAndamento;
	}
	public void setStAndamento(int stAndamento){
		this.stAndamento=stAndamento;
	}
	public int getStAndamento(){
		return this.stAndamento;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public void setTxtAta(String txtAta){
		this.txtAta=txtAta;
	}
	public String getTxtAta(){
		return this.txtAta;
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
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setBlbAta(byte[] blbAta){
		this.blbAta=blbAta;
	}
	public byte[] getBlbAta(){
		return this.blbAta;
	}
	public void setTpVisibilidade(int tpVisibilidade){
		this.tpVisibilidade=tpVisibilidade;
	}
	public int getTpVisibilidade(){
		return this.tpVisibilidade;
	}
	public void setTpEventoFinanceiro(int tpEventoFinanceiro){
		this.tpEventoFinanceiro=tpEventoFinanceiro;
	}
	public int getTpEventoFinanceiro(){
		return this.tpEventoFinanceiro;
	}
	public void setVlEventoFinanceiro(Double vlEventoFinanceiro){
		this.vlEventoFinanceiro=vlEventoFinanceiro;
	}
	public Double getVlEventoFinanceiro(){
		return this.vlEventoFinanceiro;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
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
	public void setTxtPublicacao(String txtPublicacao){
		this.txtPublicacao=txtPublicacao;
	}
	public String getTxtPublicacao(){
		return this.txtPublicacao;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdOrigemAndamento(int cdOrigemAndamento){
		this.cdOrigemAndamento=cdOrigemAndamento;
	}
	public int getCdOrigemAndamento(){
		return this.cdOrigemAndamento;
	}
	public void setCdRecorte(int cdRecorte){
		this.cdRecorte=cdRecorte;
	}
	public int getCdRecorte(){
		return this.cdRecorte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAndamento: " +  getCdAndamento();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", dtAndamento: " +  sol.util.Util.formatDateTime(getDtAndamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtAndamento: " +  getTxtAndamento();
		valueToString += ", stAndamento: " +  getStAndamento();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		valueToString += ", txtAta: " +  getTxtAta();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", dtAlteracao: " +  sol.util.Util.formatDateTime(getDtAlteracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", blbAta: " +  getBlbAta();
		valueToString += ", tpVisibilidade: " +  getTpVisibilidade();
		valueToString += ", tpEventoFinanceiro: " +  getTpEventoFinanceiro();
		valueToString += ", vlEventoFinanceiro: " +  getVlEventoFinanceiro();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", dtAtualizacaoEdi: " +  sol.util.Util.formatDateTime(getDtAtualizacaoEdi(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stAtualizacaoEdi: " +  getStAtualizacaoEdi();
		valueToString += ", txtPublicacao: " +  getTxtPublicacao();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdOrigemAndamento: " +  getCdOrigemAndamento();
		valueToString += ", cdRecorte: " +  getCdRecorte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoAndamento(getCdAndamento(),
			getCdProcesso(),
			getCdTipoAndamento(),
			getDtAndamento()==null ? null : (GregorianCalendar)getDtAndamento().clone(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getTxtAndamento(),
			getStAndamento(),
			getTpInstancia(),
			getTxtAta(),
			getCdUsuario(),
			getDtAlteracao()==null ? null : (GregorianCalendar)getDtAlteracao().clone(),
			getTpOrigem(),
			getBlbAta(),
			getTpVisibilidade(),
			getTpEventoFinanceiro(),
			getVlEventoFinanceiro(),
			getCdContaPagar(),
			getCdContaReceber(),
			getDtAtualizacaoEdi()==null ? null : (GregorianCalendar)getDtAtualizacaoEdi().clone(),
			getStAtualizacaoEdi(),
			getTxtPublicacao(),
			getCdDocumento(),
			getCdOrigemAndamento(),
			getCdRecorte());
	}

}