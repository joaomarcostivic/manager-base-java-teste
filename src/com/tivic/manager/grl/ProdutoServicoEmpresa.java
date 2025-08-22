package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class ProdutoServicoEmpresa {

	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdUnidadeMedida;
	private String idReduzido;
	private float vlPrecoMedio;
	private float vlCustoMedio;
	private float vlUltimoCusto;
	private float qtIdeal;
	private float qtMinima;
	private float qtMaxima;
	private float qtDiasEstoque;
	private int qtPrecisaoCusto;
	private int qtPrecisaoUnidade;
	private int qtDiasGarantia;
	private int tpReabastecimento;
	private int tpControleEstoque;
	private int tpTransporte;
	private int stProdutoEmpresa;
	private GregorianCalendar dtDesativacao;
	private String nrOrdem;
	private int lgEstoqueNegativo;
	private int tpOrigem;
	private String idFabrica;
	private GregorianCalendar dtUltimaAlteracao;
	private GregorianCalendar dtCadastro;
	private String nrSerie;
	private byte[] imgProduto;
	private float prDescontoMaximo;
	private int cdFormulario;
	private GregorianCalendar dtVersao;
	private int cdLocalArmazenamento;
	
	public ProdutoServicoEmpresa(){}
	
	public ProdutoServicoEmpresa(int cdEmpresa,
			int cdProdutoServico,
			int cdUnidadeMedida,
			String idReduzido,
			float vlPrecoMedio,
			float vlCustoMedio,
			float vlUltimoCusto,
			float qtIdeal,
			float qtMinima,
			float qtMaxima,
			float qtDiasEstoque,
			int qtPrecisaoCusto,
			int qtPrecisaoUnidade,
			int qtDiasGarantia,
			int tpReabastecimento,
			int tpControleEstoque,
			int tpTransporte,
			int stProdutoEmpresa,
			GregorianCalendar dtDesativacao,
			String nrOrdem,
			int lgEstoqueNegativo){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdUnidadeMedida(cdUnidadeMedida);
		setIdReduzido(idReduzido);
		setVlPrecoMedio(vlPrecoMedio);
		setVlCustoMedio(vlCustoMedio);
		setVlUltimoCusto(vlUltimoCusto);
		setQtIdeal(qtIdeal);
		setQtMinima(qtMinima);
		setQtMaxima(qtMaxima);
		setQtDiasEstoque(qtDiasEstoque);
		setQtPrecisaoCusto(qtPrecisaoCusto);
		setQtPrecisaoUnidade(qtPrecisaoUnidade);
		setQtDiasGarantia(qtDiasGarantia);
		setTpReabastecimento(tpReabastecimento);
		setTpControleEstoque(tpControleEstoque);
		setTpTransporte(tpTransporte);
		setStProdutoEmpresa(stProdutoEmpresa);
		setDtDesativacao(dtDesativacao);
		setNrOrdem(nrOrdem);
		setLgEstoqueNegativo(lgEstoqueNegativo);
	}
	public ProdutoServicoEmpresa(int cdEmpresa,
			int cdProdutoServico,
			int cdUnidadeMedida,
			String idReduzido,
			float vlPrecoMedio,
			float vlCustoMedio,
			float vlUltimoCusto,
			float qtIdeal,
			float qtMinima,
			float qtMaxima,
			float qtDiasEstoque,
			int qtPrecisaoCusto,
			int qtPrecisaoUnidade,
			int qtDiasGarantia,
			int tpReabastecimento,
			int tpControleEstoque,
			int tpTransporte,
			int stProdutoEmpresa,
			GregorianCalendar dtDesativacao,
			String nrOrdem,
			int lgEstoqueNegativo,
			int tpOrigem,
			float prDescontoMaximo,
			int cdLocalArmazenamento){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdUnidadeMedida(cdUnidadeMedida);
		setIdReduzido(idReduzido);
		setVlPrecoMedio(vlPrecoMedio);
		setVlCustoMedio(vlCustoMedio);
		setVlUltimoCusto(vlUltimoCusto);
		setQtIdeal(qtIdeal);
		setQtMinima(qtMinima);
		setQtMaxima(qtMaxima);
		setQtDiasEstoque(qtDiasEstoque);
		setQtPrecisaoCusto(qtPrecisaoCusto);
		setQtPrecisaoUnidade(qtPrecisaoUnidade);
		setQtDiasGarantia(qtDiasGarantia);
		setTpReabastecimento(tpReabastecimento);
		setTpControleEstoque(tpControleEstoque);
		setTpTransporte(tpTransporte);
		setStProdutoEmpresa(stProdutoEmpresa);
		setDtDesativacao(dtDesativacao);
		setNrOrdem(nrOrdem);
		setLgEstoqueNegativo(lgEstoqueNegativo);
		setTpOrigem(tpOrigem);
		setPrDescontoMaximo(prDescontoMaximo);
		setCdLocalArmazenamento(cdLocalArmazenamento);
	}
	public ProdutoServicoEmpresa(int cdEmpresa,
			int cdProdutoServico,
			int cdUnidadeMedida,
			String idReduzido,
			float vlPrecoMedio,
			float vlCustoMedio,
			float vlUltimoCusto,
			float qtIdeal,
			float qtMinima,
			float qtMaxima,
			float qtDiasEstoque,
			int qtPrecisaoCusto,
			int qtPrecisaoUnidade,
			int qtDiasGarantia,
			int tpReabastecimento,
			int tpControleEstoque,
			int tpTransporte,
			int stProdutoEmpresa,
			GregorianCalendar dtDesativacao,
			String nrOrdem,
			int lgEstoqueNegativo,
			int tpOrigem,
			String idFabrica,
			GregorianCalendar dtUltimaAlteracao,
			GregorianCalendar dtCadastro,
			String nrSerie,
			byte[] imgProduto){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdUnidadeMedida(cdUnidadeMedida);
		setIdReduzido(idReduzido);
		setVlPrecoMedio(vlPrecoMedio);
		setVlCustoMedio(vlCustoMedio);
		setVlUltimoCusto(vlUltimoCusto);
		setQtIdeal(qtIdeal);
		setQtMinima(qtMinima);
		setQtMaxima(qtMaxima);
		setQtDiasEstoque(qtDiasEstoque);
		setQtPrecisaoCusto(qtPrecisaoCusto);
		setQtPrecisaoUnidade(qtPrecisaoUnidade);
		setQtDiasGarantia(qtDiasGarantia);
		setTpReabastecimento(tpReabastecimento);
		setTpControleEstoque(tpControleEstoque);
		setTpTransporte(tpTransporte);
		setStProdutoEmpresa(stProdutoEmpresa);
		setDtDesativacao(dtDesativacao);
		setNrOrdem(nrOrdem);
		setLgEstoqueNegativo(lgEstoqueNegativo);
		setTpOrigem(tpOrigem);
		setIdFabrica(idFabrica);
		setDtUltimaAlteracao(dtUltimaAlteracao);
		setNrSerie(nrSerie);
		setImgProduto(imgProduto);
	}
	public ProdutoServicoEmpresa(int cdEmpresa,
			int cdProdutoServico,
			int cdUnidadeMedida,
			String idReduzido,
			float vlPrecoMedio,
			float vlCustoMedio,
			float vlUltimoCusto,
			float qtIdeal,
			float qtMinima,
			float qtMaxima,
			float qtDiasEstoque,
			int qtPrecisaoCusto,
			int qtPrecisaoUnidade,
			int qtDiasGarantia,
			int tpReabastecimento,
			int tpControleEstoque,
			int tpTransporte,
			int stProdutoEmpresa,
			GregorianCalendar dtDesativacao,
			String nrOrdem,
			int lgEstoqueNegativo,
			int tpOrigem,
			String idFabrica,
			GregorianCalendar dtUltimaAlteracao,
			GregorianCalendar dtCadastro,
			String nrSerie,
			byte[] imgProduto,
			float prDescontoMaximo,
			int cdFormulario,
			GregorianCalendar dtVersao,
			int cdLocalArmazenamento){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdUnidadeMedida(cdUnidadeMedida);
		setIdReduzido(idReduzido);
		setVlPrecoMedio(vlPrecoMedio);
		setVlCustoMedio(vlCustoMedio);
		setVlUltimoCusto(vlUltimoCusto);
		setQtIdeal(qtIdeal);
		setQtMinima(qtMinima);
		setQtMaxima(qtMaxima);
		setQtDiasEstoque(qtDiasEstoque);
		setQtPrecisaoCusto(qtPrecisaoCusto);
		setQtPrecisaoUnidade(qtPrecisaoUnidade);
		setQtDiasGarantia(qtDiasGarantia);
		setTpReabastecimento(tpReabastecimento);
		setTpControleEstoque(tpControleEstoque);
		setTpTransporte(tpTransporte);
		setStProdutoEmpresa(stProdutoEmpresa);
		setDtDesativacao(dtDesativacao);
		setNrOrdem(nrOrdem);
		setLgEstoqueNegativo(lgEstoqueNegativo);
		setTpOrigem(tpOrigem);
		setIdFabrica(idFabrica);
		setDtUltimaAlteracao(dtUltimaAlteracao);
		setNrSerie(nrSerie);
		setImgProduto(imgProduto);
		setPrDescontoMaximo(prDescontoMaximo);
		setCdFormulario(cdFormulario);
		setDtVersao(dtVersao);
		setCdLocalArmazenamento(cdLocalArmazenamento);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public void setIdReduzido(String idReduzido){
		this.idReduzido=idReduzido;
	}
	public String getIdReduzido(){
		return this.idReduzido;
	}
	public void setVlPrecoMedio(float vlPrecoMedio){
		this.vlPrecoMedio=vlPrecoMedio;
	}
	public float getVlPrecoMedio(){
		return this.vlPrecoMedio;
	}
	public void setVlCustoMedio(float vlCustoMedio){
		this.vlCustoMedio=vlCustoMedio;
	}
	public float getVlCustoMedio(){
		return this.vlCustoMedio;
	}
	public void setVlUltimoCusto(float vlUltimoCusto){
		this.vlUltimoCusto=vlUltimoCusto;
	}
	public float getVlUltimoCusto(){
		return this.vlUltimoCusto;
	}
	public void setQtIdeal(float qtIdeal){
		this.qtIdeal=qtIdeal;
	}
	public float getQtIdeal(){
		return this.qtIdeal;
	}
	public void setQtMinima(float qtMinima){
		this.qtMinima=qtMinima;
	}
	public float getQtMinima(){
		return this.qtMinima;
	}
	public void setQtMaxima(float qtMaxima){
		this.qtMaxima=qtMaxima;
	}
	public float getQtMaxima(){
		return this.qtMaxima;
	}
	public void setQtDiasEstoque(float qtDiasEstoque){
		this.qtDiasEstoque=qtDiasEstoque;
	}
	public float getQtDiasEstoque(){
		return this.qtDiasEstoque;
	}
	public void setQtPrecisaoCusto(int qtPrecisaoCusto){
		this.qtPrecisaoCusto=qtPrecisaoCusto;
	}
	public int getQtPrecisaoCusto(){
		return this.qtPrecisaoCusto;
	}
	public void setQtPrecisaoUnidade(int qtPrecisaoUnidade){
		this.qtPrecisaoUnidade=qtPrecisaoUnidade;
	}
	public int getQtPrecisaoUnidade(){
		return this.qtPrecisaoUnidade;
	}
	public void setQtDiasGarantia(int qtDiasGarantia){
		this.qtDiasGarantia=qtDiasGarantia;
	}
	public int getQtDiasGarantia(){
		return this.qtDiasGarantia;
	}
	public void setTpReabastecimento(int tpReabastecimento){
		this.tpReabastecimento=tpReabastecimento;
	}
	public int getTpReabastecimento(){
		return this.tpReabastecimento;
	}
	public void setTpControleEstoque(int tpControleEstoque){
		this.tpControleEstoque=tpControleEstoque;
	}
	public int getTpControleEstoque(){
		return this.tpControleEstoque;
	}
	public void setTpTransporte(int tpTransporte){
		this.tpTransporte=tpTransporte;
	}
	public int getTpTransporte(){
		return this.tpTransporte;
	}
	public void setStProdutoEmpresa(int stProdutoEmpresa){
		this.stProdutoEmpresa=stProdutoEmpresa;
	}
	public int getStProdutoEmpresa(){
		return this.stProdutoEmpresa;
	}
	public void setDtDesativacao(GregorianCalendar dtDesativacao){
		this.dtDesativacao=dtDesativacao;
	}
	public GregorianCalendar getDtDesativacao(){
		return this.dtDesativacao;
	}
	public void setNrOrdem(String nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public String getNrOrdem(){
		return this.nrOrdem;
	}
	public void setLgEstoqueNegativo(int lgEstoqueNegativo){
		this.lgEstoqueNegativo=lgEstoqueNegativo;
	}
	public int getLgEstoqueNegativo(){
		return this.lgEstoqueNegativo;
	}
	public void setTpOrigem(int tpOrigem) {
		this.tpOrigem = tpOrigem;
	}
	public int getTpOrigem() {
		return tpOrigem;
	}
	public void setIdFabrica(String idFabrica) {
		this.idFabrica = idFabrica;
	}
	public String getIdFabrica() {
		return idFabrica;
	}
	public void setDtUltimaAlteracao(GregorianCalendar dtUltimaAlteracao) {
		this.dtUltimaAlteracao = dtUltimaAlteracao;
	}
	public GregorianCalendar getDtUltimaAlteracao() {
		return dtUltimaAlteracao;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro) {
		this.dtCadastro = dtCadastro;
	}
	public GregorianCalendar getDtCadastro() {
		return dtCadastro;
	}
	public void setNrSerie(String nrSerie) {
		this.nrSerie = nrSerie;
	}
	public String getNrSerie() {
		return nrSerie;
	}
	public void setImgProduto(byte[] imgProduto) {
		this.imgProduto = imgProduto;
	}
	public byte[] getImgProduto() {
		return imgProduto;
	}
	public void setPrDescontoMaximo(float prDescontoMaximo) {
		this.prDescontoMaximo = prDescontoMaximo;
	}
	public float getPrDescontoMaximo() {
		return prDescontoMaximo;
	}
	/**
	 * @return o cdFormulario
	 */
	public int getCdFormulario() {
		return cdFormulario;
	}
	/**
	 * @param cdFormulario o cdFormulario para set
	 */
	public void setCdFormulario(int cdFormulario) {
		this.cdFormulario = cdFormulario;
	}
	/**
	 * @return the dtVersao
	 */
	public GregorianCalendar getDtVersao() {
		return dtVersao;
	}
	/**
	 * @param dtVersao the dtVersao to set
	 */
	public void setDtVersao(GregorianCalendar dtVersao) {
		this.dtVersao = dtVersao;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento) {
		this.cdLocalArmazenamento = cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento() {
		return cdLocalArmazenamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		valueToString += ", idReduzido: " +  getIdReduzido();
		valueToString += ", vlPrecoMedio: " +  getVlPrecoMedio();
		valueToString += ", vlCustoMedio: " +  getVlCustoMedio();
		valueToString += ", vlUltimoCusto: " +  getVlUltimoCusto();
		valueToString += ", qtIdeal: " +  getQtIdeal();
		valueToString += ", qtMinima: " +  getQtMinima();
		valueToString += ", qtMaxima: " +  getQtMaxima();
		valueToString += ", qtDiasEstoque: " +  getQtDiasEstoque();
		valueToString += ", qtPrecisaoCusto: " +  getQtPrecisaoCusto();
		valueToString += ", qtPrecisaoUnidade: " +  getQtPrecisaoUnidade();
		valueToString += ", qtDiasGarantia: " +  getQtDiasGarantia();
		valueToString += ", tpReabastecimento: " +  getTpReabastecimento();
		valueToString += ", tpControleEstoque: " +  getTpControleEstoque();
		valueToString += ", tpTransporte: " +  getTpTransporte();
		valueToString += ", stProdutoEmpresa: " +  getStProdutoEmpresa();
		valueToString += ", dtDesativacao: " +  sol.util.Util.formatDateTime(getDtDesativacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", lgEstoqueNegativo: " +  getLgEstoqueNegativo();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", idFabrica: " +  getIdFabrica();
		valueToString += ", dtUltimaAlteracao: " +  sol.util.Util.formatDateTime(getDtUltimaAlteracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrSerie: " +  getNrSerie();
		valueToString += ", imgProduto: " +  getImgProduto();
		valueToString += ", prDescontoMaximo: " +  getPrDescontoMaximo();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", dtVersao: " +  getDtVersao();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoServicoEmpresa(getCdEmpresa(),
			getCdProdutoServico(),
			getCdUnidadeMedida(),
			getIdReduzido(),
			getVlPrecoMedio(),
			getVlCustoMedio(),
			getVlUltimoCusto(),
			getQtIdeal(),
			getQtMinima(),
			getQtMaxima(),
			getQtDiasEstoque(),
			getQtPrecisaoCusto(),
			getQtPrecisaoUnidade(),
			getQtDiasGarantia(),
			getTpReabastecimento(),
			getTpControleEstoque(),
			getTpTransporte(),
			getStProdutoEmpresa(),
			getDtDesativacao()==null ? null : (GregorianCalendar)getDtDesativacao().clone(),
			getNrOrdem(),
			getLgEstoqueNegativo(),
			getTpOrigem(),
			getIdFabrica(),
			getDtUltimaAlteracao()==null ? null : (GregorianCalendar)getDtUltimaAlteracao().clone(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getNrSerie(),
			getImgProduto(),
			getPrDescontoMaximo(),
			getCdFormulario(),
			getDtVersao(),
			getCdLocalArmazenamento());
	}

}
