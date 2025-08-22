package com.tivic.manager.msg;

public class RegraNotificacao {

	private int cdRegraNotificacao;
	private String nmRegraNotificacao;
	private String dsRegraNotificacao;
	private int tpEntidade;
	private int tpAcao;
	private int lgAtivo;
	private int cdProcesso;
	private int cdGrupoTrabalho;
	private int cdAndamento;
	private int cdArquivo;
	private int cdEventoFinanceiro;
	private int cdAdvogado;
	private int cdCliente;
	private int cdAgendaItem;
	private int cdResponsavelAgenda;
	private int tpAgendaItem;
	private int cdTipoAndamento;
	private int lgNotificarCliente;
	private int stAgendaItem;
	private int qtHorasFinalPrazo;
	private int cdGrupoResponsavelAgenda;
	private int cdTipoPrazo;
	private int lgNotificarResponsavel;
	private int lgNotificarAdvogado;
	private int lgNotificarGrupoTrabalho;
	private int lgMudancaPrazo;
	private int lgMudancaResponsavel;
	private int lgNotificarAutor;

	public RegraNotificacao() { }

	public RegraNotificacao(int cdRegraNotificacao,
			String nmRegraNotificacao,
			String dsRegraNotificacao,
			int tpEntidade,
			int tpAcao,
			int lgAtivo,
			int cdProcesso,
			int cdGrupoTrabalho,
			int cdAndamento,
			int cdArquivo,
			int cdEventoFinanceiro,
			int cdAdvogado,
			int cdCliente,
			int cdAgendaItem,
			int cdResponsavelAgenda,
			int tpAgendaItem,
			int cdTipoAndamento,
			int lgNotificarCliente,
			int stAgendaItem,
			int qtHorasFinalPrazo,
			int cdGrupoResponsavelAgenda,
			int cdTipoPrazo,
			int lgNotificarResponsavel,
			int lgNotificarAdvogado,
			int lgNotificarGrupoTrabalho,
			int lgMudancaPrazo,
			int lgMudancaResponsavel,
			int lgNotificarAutor) {
		setCdRegraNotificacao(cdRegraNotificacao);
		setNmRegraNotificacao(nmRegraNotificacao);
		setDsRegraNotificacao(dsRegraNotificacao);
		setTpEntidade(tpEntidade);
		setTpAcao(tpAcao);
		setLgAtivo(lgAtivo);
		setCdProcesso(cdProcesso);
		setCdGrupoTrabalho(cdGrupoTrabalho);
		setCdAndamento(cdAndamento);
		setCdArquivo(cdArquivo);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setCdAdvogado(cdAdvogado);
		setCdCliente(cdCliente);
		setCdAgendaItem(cdAgendaItem);
		setCdResponsavelAgenda(cdResponsavelAgenda);
		setTpAgendaItem(tpAgendaItem);
		setCdTipoAndamento(cdTipoAndamento);
		setLgNotificarCliente(lgNotificarCliente);
		setStAgendaItem(stAgendaItem);
		setQtHorasFinalPrazo(qtHorasFinalPrazo);
		setCdGrupoResponsavelAgenda(cdGrupoResponsavelAgenda);
		setCdTipoPrazo(cdTipoPrazo);
		setLgNotificarResponsavel(lgNotificarResponsavel);
		setLgNotificarAdvogado(lgNotificarAdvogado);
		setLgNotificarGrupoTrabalho(lgNotificarGrupoTrabalho);
		setLgMudancaPrazo(lgMudancaPrazo);
		setLgMudancaResponsavel(lgMudancaResponsavel);
		setLgNotificarAutor(lgNotificarAutor);
	}
	public void setCdRegraNotificacao(int cdRegraNotificacao){
		this.cdRegraNotificacao=cdRegraNotificacao;
	}
	public int getCdRegraNotificacao(){
		return this.cdRegraNotificacao;
	}
	public void setNmRegraNotificacao(String nmRegraNotificacao){
		this.nmRegraNotificacao=nmRegraNotificacao;
	}
	public String getNmRegraNotificacao(){
		return this.nmRegraNotificacao;
	}
	public void setDsRegraNotificacao(String dsRegraNotificacao){
		this.dsRegraNotificacao=dsRegraNotificacao;
	}
	public String getDsRegraNotificacao(){
		return this.dsRegraNotificacao;
	}
	public void setTpEntidade(int tpEntidade){
		this.tpEntidade=tpEntidade;
	}
	public int getTpEntidade(){
		return this.tpEntidade;
	}
	public void setTpAcao(int tpAcao){
		this.tpAcao=tpAcao;
	}
	public int getTpAcao(){
		return this.tpAcao;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setCdGrupoTrabalho(int cdGrupoTrabalho){
		this.cdGrupoTrabalho=cdGrupoTrabalho;
	}
	public int getCdGrupoTrabalho(){
		return this.cdGrupoTrabalho;
	}
	public void setCdAndamento(int cdAndamento){
		this.cdAndamento=cdAndamento;
	}
	public int getCdAndamento(){
		return this.cdAndamento;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setCdAdvogado(int cdAdvogado){
		this.cdAdvogado=cdAdvogado;
	}
	public int getCdAdvogado(){
		return this.cdAdvogado;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setCdAgendaItem(int cdAgendaItem){
		this.cdAgendaItem=cdAgendaItem;
	}
	public int getCdAgendaItem(){
		return this.cdAgendaItem;
	}
	public void setCdResponsavelAgenda(int cdResponsavelAgenda){
		this.cdResponsavelAgenda=cdResponsavelAgenda;
	}
	public int getCdResponsavelAgenda(){
		return this.cdResponsavelAgenda;
	}
	public void setTpAgendaItem(int tpAgendaItem){
		this.tpAgendaItem=tpAgendaItem;
	}
	public int getTpAgendaItem(){
		return this.tpAgendaItem;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setLgNotificarCliente(int lgNotificarCliente){
		this.lgNotificarCliente=lgNotificarCliente;
	}
	public int getLgNotificarCliente(){
		return this.lgNotificarCliente;
	}
	public void setStAgendaItem(int stAgendaItem){
		this.stAgendaItem=stAgendaItem;
	}
	public int getStAgendaItem(){
		return this.stAgendaItem;
	}
	public void setQtHorasFinalPrazo(int qtHorasFinalPrazo){
		this.qtHorasFinalPrazo=qtHorasFinalPrazo;
	}
	public int getQtHorasFinalPrazo(){
		return this.qtHorasFinalPrazo;
	}
	public void setCdGrupoResponsavelAgenda(int cdGrupoResponsavelAgenda){
		this.cdGrupoResponsavelAgenda=cdGrupoResponsavelAgenda;
	}
	public int getCdGrupoResponsavelAgenda(){
		return this.cdGrupoResponsavelAgenda;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setLgNotificarResponsavel(int lgNotificarResponsavel){
		this.lgNotificarResponsavel=lgNotificarResponsavel;
	}
	public int getLgNotificarResponsavel(){
		return this.lgNotificarResponsavel;
	}
	public void setLgNotificarAdvogado(int lgNotificarAdvogado){
		this.lgNotificarAdvogado=lgNotificarAdvogado;
	}
	public int getLgNotificarAdvogado(){
		return this.lgNotificarAdvogado;
	}
	public void setLgNotificarGrupoTrabalho(int lgNotificarGrupoTrabalho){
		this.lgNotificarGrupoTrabalho=lgNotificarGrupoTrabalho;
	}
	public int getLgNotificarGrupoTrabalho(){
		return this.lgNotificarGrupoTrabalho;
	}
	public void setLgMudancaPrazo(int lgMudancaPrazo){
		this.lgMudancaPrazo=lgMudancaPrazo;
	}
	public int getLgMudancaPrazo(){
		return this.lgMudancaPrazo;
	}
	public void setLgMudancaResponsavel(int lgMudancaResponsavel){
		this.lgMudancaResponsavel=lgMudancaResponsavel;
	}
	public int getLgMudancaResponsavel(){
		return this.lgMudancaResponsavel;
	}
	public void setLgNotificarAutor(int lgNotificarAutor){
		this.lgNotificarAutor=lgNotificarAutor;
	}
	public int getLgNotificarAutor(){
		return this.lgNotificarAutor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraNotificacao: " +  getCdRegraNotificacao();
		valueToString += ", nmRegraNotificacao: " +  getNmRegraNotificacao();
		valueToString += ", dsRegraNotificacao: " +  getDsRegraNotificacao();
		valueToString += ", tpEntidade: " +  getTpEntidade();
		valueToString += ", tpAcao: " +  getTpAcao();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", cdGrupoTrabalho: " +  getCdGrupoTrabalho();
		valueToString += ", cdAndamento: " +  getCdAndamento();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", cdAdvogado: " +  getCdAdvogado();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", cdAgendaItem: " +  getCdAgendaItem();
		valueToString += ", cdResponsavelAgenda: " +  getCdResponsavelAgenda();
		valueToString += ", tpAgendaItem: " +  getTpAgendaItem();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", lgNotificarCliente: " +  getLgNotificarCliente();
		valueToString += ", stAgendaItem: " +  getStAgendaItem();
		valueToString += ", qtHorasFinalPrazo: " +  getQtHorasFinalPrazo();
		valueToString += ", cdGrupoResponsavelAgenda: " +  getCdGrupoResponsavelAgenda();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", lgNotificarResponsavel: " +  getLgNotificarResponsavel();
		valueToString += ", lgNotificarAdvogado: " +  getLgNotificarAdvogado();
		valueToString += ", lgNotificarGrupoTrabalho: " +  getLgNotificarGrupoTrabalho();
		valueToString += ", lgMudancaPrazo: " +  getLgMudancaPrazo();
		valueToString += ", lgMudancaResponsavel: " +  getLgMudancaResponsavel();
		valueToString += ", lgNotificarAutor: " +  getLgNotificarAutor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraNotificacao(getCdRegraNotificacao(),
			getNmRegraNotificacao(),
			getDsRegraNotificacao(),
			getTpEntidade(),
			getTpAcao(),
			getLgAtivo(),
			getCdProcesso(),
			getCdGrupoTrabalho(),
			getCdAndamento(),
			getCdArquivo(),
			getCdEventoFinanceiro(),
			getCdAdvogado(),
			getCdCliente(),
			getCdAgendaItem(),
			getCdResponsavelAgenda(),
			getTpAgendaItem(),
			getCdTipoAndamento(),
			getLgNotificarCliente(),
			getStAgendaItem(),
			getQtHorasFinalPrazo(),
			getCdGrupoResponsavelAgenda(),
			getCdTipoPrazo(),
			getLgNotificarResponsavel(),
			getLgNotificarAdvogado(),
			getLgNotificarGrupoTrabalho(),
			getLgMudancaPrazo(),
			getLgMudancaResponsavel(),
			getLgNotificarAutor());
	}

}