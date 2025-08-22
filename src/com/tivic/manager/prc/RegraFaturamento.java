package com.tivic.manager.prc;

public class RegraFaturamento {

	private int cdRegraFaturamento;
	private int cdTipoAndamento;
	private int cdTipoPrazo;
	private int cdProdutoServico;
	private int cdTipoProcesso;
	private int cdOrgao;
	private int cdCliente;
	private int cdGrupoProcesso;
	private int cdAreaDireito;
	private int cdEstado;
	private int tpFatoGerador;
	private int tpNaturezaEvento;
	private int tpParteCliente;
	private int tpInstancia;
	private int tpSegmento;
	private int tpPontoBase;
	private int qtMaxima;
	private int qtAposEncerramento;
	private Double vlLimiteInferior;
	private Double vlLimiteSuperior;
	private Double vlKmDeslocamento;
	private Double vlKmMaxima;
	private Double vlServico;
	private int cdCategoriaEconomica;
	private int cdCentroCusto;
	private int tpAplicacaoGrupo;
	private String nmRegraFaturamento;
	private int stRegraFaturamento;
	private int cdCidade;
	private int cdGrupoTrabalho;
	private int lgPreposto;
	private int cdProcesso;
	private String nrJuizo;
	private int cdJuizo;
	private Double vlPreposto;

	public RegraFaturamento() { }

	public RegraFaturamento(int cdRegraFaturamento,
			int cdTipoAndamento,
			int cdTipoPrazo,
			int cdProdutoServico,
			int cdTipoProcesso,
			int cdOrgao,
			int cdCliente,
			int cdGrupoProcesso,
			int cdAreaDireito,
			int cdEstado,
			int tpFatoGerador,
			int tpNaturezaEvento,
			int tpParteCliente,
			int tpInstancia,
			int tpSegmento,
			int tpPontoBase,
			int qtMaxima,
			int qtAposEncerramento,
			Double vlLimiteInferior,
			Double vlLimiteSuperior,
			Double vlKmDeslocamento,
			Double vlKmMaxima,
			Double vlServico,
			int cdCategoriaEconomica,
			int cdCentroCusto,
			int tpAplicacaoGrupo,
			String nmRegraFaturamento,
			int stRegraFaturamento,
			int cdCidade,
			int cdGrupoTrabalho,
			int lgPreposto,
			int cdProcesso,
			String nrJuizo,
			int cdJuizo,
			Double vlPreposto) {
		setCdRegraFaturamento(cdRegraFaturamento);
		setCdTipoAndamento(cdTipoAndamento);
		setCdTipoPrazo(cdTipoPrazo);
		setCdProdutoServico(cdProdutoServico);
		setCdTipoProcesso(cdTipoProcesso);
		setCdOrgao(cdOrgao);
		setCdCliente(cdCliente);
		setCdGrupoProcesso(cdGrupoProcesso);
		setCdAreaDireito(cdAreaDireito);
		setCdEstado(cdEstado);
		setTpFatoGerador(tpFatoGerador);
		setTpNaturezaEvento(tpNaturezaEvento);
		setTpParteCliente(tpParteCliente);
		setTpInstancia(tpInstancia);
		setTpSegmento(tpSegmento);
		setTpPontoBase(tpPontoBase);
		setQtMaxima(qtMaxima);
		setQtAposEncerramento(qtAposEncerramento);
		setVlLimiteInferior(vlLimiteInferior);
		setVlLimiteSuperior(vlLimiteSuperior);
		setVlKmDeslocamento(vlKmDeslocamento);
		setVlKmMaxima(vlKmMaxima);
		setVlServico(vlServico);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setCdCentroCusto(cdCentroCusto);
		setTpAplicacaoGrupo(tpAplicacaoGrupo);
		setNmRegraFaturamento(nmRegraFaturamento);
		setStRegraFaturamento(stRegraFaturamento);
		setCdCidade(cdCidade);
		setCdGrupoTrabalho(cdGrupoTrabalho);
		setLgPreposto(lgPreposto);
		setCdProcesso(cdProcesso);
		setNrJuizo(nrJuizo);
		setCdJuizo(cdJuizo);
		setVlPreposto(vlPreposto);
	}
	public void setCdRegraFaturamento(int cdRegraFaturamento){
		this.cdRegraFaturamento=cdRegraFaturamento;
	}
	public int getCdRegraFaturamento(){
		return this.cdRegraFaturamento;
	}
	public void setCdTipoAndamento(int cdTipoAndamento){
		this.cdTipoAndamento=cdTipoAndamento;
	}
	public int getCdTipoAndamento(){
		return this.cdTipoAndamento;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setCdGrupoProcesso(int cdGrupoProcesso){
		this.cdGrupoProcesso=cdGrupoProcesso;
	}
	public int getCdGrupoProcesso(){
		return this.cdGrupoProcesso;
	}
	public void setCdAreaDireito(int cdAreaDireito){
		this.cdAreaDireito=cdAreaDireito;
	}
	public int getCdAreaDireito(){
		return this.cdAreaDireito;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setTpFatoGerador(int tpFatoGerador){
		this.tpFatoGerador=tpFatoGerador;
	}
	public int getTpFatoGerador(){
		return this.tpFatoGerador;
	}
	public void setTpNaturezaEvento(int tpNaturezaEvento){
		this.tpNaturezaEvento=tpNaturezaEvento;
	}
	public int getTpNaturezaEvento(){
		return this.tpNaturezaEvento;
	}
	public void setTpParteCliente(int tpParteCliente){
		this.tpParteCliente=tpParteCliente;
	}
	public int getTpParteCliente(){
		return this.tpParteCliente;
	}
	public void setTpInstancia(int tpInstancia){
		this.tpInstancia=tpInstancia;
	}
	public int getTpInstancia(){
		return this.tpInstancia;
	}
	public void setTpSegmento(int tpSegmento){
		this.tpSegmento=tpSegmento;
	}
	public int getTpSegmento(){
		return this.tpSegmento;
	}
	public void setTpPontoBase(int tpPontoBase){
		this.tpPontoBase=tpPontoBase;
	}
	public int getTpPontoBase(){
		return this.tpPontoBase;
	}
	public void setQtMaxima(int qtMaxima){
		this.qtMaxima=qtMaxima;
	}
	public int getQtMaxima(){
		return this.qtMaxima;
	}
	public void setQtAposEncerramento(int qtAposEncerramento){
		this.qtAposEncerramento=qtAposEncerramento;
	}
	public int getQtAposEncerramento(){
		return this.qtAposEncerramento;
	}
	public void setVlLimiteInferior(Double vlLimiteInferior){
		this.vlLimiteInferior=vlLimiteInferior;
	}
	public Double getVlLimiteInferior(){
		return this.vlLimiteInferior;
	}
	public void setVlLimiteSuperior(Double vlLimiteSuperior){
		this.vlLimiteSuperior=vlLimiteSuperior;
	}
	public Double getVlLimiteSuperior(){
		return this.vlLimiteSuperior;
	}
	public void setVlKmDeslocamento(Double vlKmDeslocamento){
		this.vlKmDeslocamento=vlKmDeslocamento;
	}
	public Double getVlKmDeslocamento(){
		return this.vlKmDeslocamento;
	}
	public void setVlKmMaxima(Double vlKmMaxima){
		this.vlKmMaxima=vlKmMaxima;
	}
	public Double getVlKmMaxima(){
		return this.vlKmMaxima;
	}
	public void setVlServico(Double vlServico){
		this.vlServico=vlServico;
	}
	public Double getVlServico(){
		return this.vlServico;
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public void setTpAplicacaoGrupo(int tpAplicacaoGrupo){
		this.tpAplicacaoGrupo=tpAplicacaoGrupo;
	}
	public int getTpAplicacaoGrupo(){
		return this.tpAplicacaoGrupo;
	}
	public void setNmRegraFaturamento(String nmRegraFaturamento){
		this.nmRegraFaturamento=nmRegraFaturamento;
	}
	public String getNmRegraFaturamento(){
		return this.nmRegraFaturamento;
	}
	public void setStRegraFaturamento(int stRegraFaturamento){
		this.stRegraFaturamento=stRegraFaturamento;
	}
	public int getStRegraFaturamento(){
		return this.stRegraFaturamento;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdGrupoTrabalho(int cdGrupoTrabalho){
		this.cdGrupoTrabalho=cdGrupoTrabalho;
	}
	public int getCdGrupoTrabalho(){
		return this.cdGrupoTrabalho;
	}
	public void setLgPreposto(int lgPreposto){
		this.lgPreposto=lgPreposto;
	}
	public int getLgPreposto(){
		return this.lgPreposto;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setNrJuizo(String nrJuizo){
		this.nrJuizo=nrJuizo;
	}
	public String getNrJuizo(){
		return this.nrJuizo;
	}
	public void setCdJuizo(int cdJuizo){
		this.cdJuizo=cdJuizo;
	}
	public int getCdJuizo(){
		return this.cdJuizo;
	}
	public void setVlPreposto(Double vlPreposto){
		this.vlPreposto=vlPreposto;
	}
	public Double getVlPreposto(){
		return this.vlPreposto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegraFaturamento: " +  getCdRegraFaturamento();
		valueToString += ", cdTipoAndamento: " +  getCdTipoAndamento();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", cdOrgao: " +  getCdOrgao();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", cdGrupoProcesso: " +  getCdGrupoProcesso();
		valueToString += ", cdAreaDireito: " +  getCdAreaDireito();
		valueToString += ", cdEstado: " +  getCdEstado();
		valueToString += ", tpFatoGerador: " +  getTpFatoGerador();
		valueToString += ", tpNaturezaEvento: " +  getTpNaturezaEvento();
		valueToString += ", tpParteCliente: " +  getTpParteCliente();
		valueToString += ", tpInstancia: " +  getTpInstancia();
		valueToString += ", tpSegmento: " +  getTpSegmento();
		valueToString += ", tpPontoBase: " +  getTpPontoBase();
		valueToString += ", qtMaxima: " +  getQtMaxima();
		valueToString += ", qtAposEncerramento: " +  getQtAposEncerramento();
		valueToString += ", vlLimiteInferior: " +  getVlLimiteInferior();
		valueToString += ", vlLimiteSuperior: " +  getVlLimiteSuperior();
		valueToString += ", vlKmDeslocamento: " +  getVlKmDeslocamento();
		valueToString += ", vlKmMaxima: " +  getVlKmMaxima();
		valueToString += ", vlServico: " +  getVlServico();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", tpAplicacaoGrupo: " +  getTpAplicacaoGrupo();
		valueToString += ", nmRegraFaturamento: " +  getNmRegraFaturamento();
		valueToString += ", stRegraFaturamento: " +  getStRegraFaturamento();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdGrupoTrabalho: " +  getCdGrupoTrabalho();
		valueToString += ", lgPreposto: " +  getLgPreposto();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", nrJuizo: " +  getNrJuizo();
		valueToString += ", cdJuizo: " +  getCdJuizo();
		valueToString += ", vlPreposto: " +  getVlPreposto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegraFaturamento(getCdRegraFaturamento(),
			getCdTipoAndamento(),
			getCdTipoPrazo(),
			getCdProdutoServico(),
			getCdTipoProcesso(),
			getCdOrgao(),
			getCdCliente(),
			getCdGrupoProcesso(),
			getCdAreaDireito(),
			getCdEstado(),
			getTpFatoGerador(),
			getTpNaturezaEvento(),
			getTpParteCliente(),
			getTpInstancia(),
			getTpSegmento(),
			getTpPontoBase(),
			getQtMaxima(),
			getQtAposEncerramento(),
			getVlLimiteInferior(),
			getVlLimiteSuperior(),
			getVlKmDeslocamento(),
			getVlKmMaxima(),
			getVlServico(),
			getCdCategoriaEconomica(),
			getCdCentroCusto(),
			getTpAplicacaoGrupo(),
			getNmRegraFaturamento(),
			getStRegraFaturamento(),
			getCdCidade(),
			getCdGrupoTrabalho(),
			getLgPreposto(),
			getCdProcesso(),
			getNrJuizo(),
			getCdJuizo(),
			getVlPreposto());
	}

}