package com.tivic.manager.mob.tabelashorarios.relatorio;

public class RelatorioHorarioDTO {
	private String idEmpresa;
	private String nrLinha;
	private Integer nrPrefixo;
	private Integer stHorarioAfericao;
	private String dtLancamento;
	private String dtLancamentoInicial;
	private String dtLancamentoFinal;
	private String hrPrevisto;
	private String hrChegada;
	private String hrPartida;
	private String nmAgente;
	private String nmConcessionario;
	private int cdConcessionario;

	public RelatorioHorarioDTO() {
		super();
	}

	public RelatorioHorarioDTO(String idEmpresa, String nrLinha, Integer nrPrefixo, Integer stHorarioAfericao,
			String dtLancamentoInicial, String dtLancamentoFinal, String dtLancamento,
			String hrPrevisto, String hrChegada, String hrPartida, String nmAgente,
			String nmConcessionario, int cdConcessionario) {
		this.idEmpresa = idEmpresa;
		this.nrLinha = nrLinha;
		this.nrPrefixo = nrPrefixo;
		this.stHorarioAfericao = stHorarioAfericao;
		this.dtLancamento = dtLancamento;
		this.hrPrevisto = hrPrevisto;
		this.hrChegada = hrChegada;
		this.hrPartida = hrPartida;
		this.nmAgente = nmAgente;
		this.nmConcessionario = nmConcessionario;
		this.cdConcessionario = cdConcessionario;
		this.dtLancamentoFinal = dtLancamentoFinal;
		this.dtLancamentoInicial = dtLancamentoInicial;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getNrLinha() {
		return nrLinha;
	}

	public void setNrLinha(String nrLinha) {
		this.nrLinha = nrLinha;
	}

	public Integer getNrPrefixo() {
		return nrPrefixo;
	}

	public void setNrPrefixo(Integer nrPrefixo) {
		this.nrPrefixo = nrPrefixo;
	}

	public Integer getStHorarioAfericao() {
		return stHorarioAfericao;
	}

	public void setStHorarioAfericao(Object stHorarioAfericao) {
	    if(stHorarioAfericao != null)
	    	this.stHorarioAfericao = (Integer) stHorarioAfericao;
	}

	public String getDtLancamentoInicial() {
		return dtLancamentoInicial;
	}

	public void setDtLancamentoInicial(String dtLancamentoInicial) {
		this.dtLancamentoInicial = dtLancamentoInicial;
	}

	public String getDtLancamentoFinal() {
		return dtLancamentoFinal;
	}

	public void setDtLancamentoFinal(String dtLancamentoFinal) {
		this.dtLancamentoFinal = dtLancamentoFinal;
	}

	public String getDtLancamento() {
		return dtLancamento;
	}

	public void setDtLancamento(String dtLancamento) {
		this.dtLancamento = dtLancamento;
	}

	public String getHrPrevisto() {
		return hrPrevisto;
	}

	public void setHrPrevisto(String hrPrevista) {
		this.hrPrevisto = hrPrevista;
	}

	public String getHrChegada() {
		return hrChegada;
	}

	public void setHrChegada(String hrChegada) {
		this.hrChegada = hrChegada;
	}

	public String getHrSaida() {
		return hrPartida;
	}

	public void setHrPartida(String hrPartida) {
		this.hrPartida = hrPartida;
	}

	public String getNmAgente() {
		return nmAgente;
	}

	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}

	public String getNmConcessionario() {
		return nmConcessionario;
	}

	public void setNmConcessionario(String nmConcessionario) {
		this.nmConcessionario = nmConcessionario;
	}

	public int getCdConcessionario() {
		return cdConcessionario;
	}

	public void setCdConcessionario(int cdConcessionario) {
		this.cdConcessionario = cdConcessionario;
	}
}
