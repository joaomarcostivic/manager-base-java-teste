package com.tivic.manager.grl;

import java.util.Map;

public class ProdutoServico {

	private int cdProdutoServico;
	private int cdCategoriaEconomica;
	private String nmProdutoServico;
	private String txtProdutoServico;
	private String txtEspecificacao;
	private String txtDadoTecnico;
	private String txtPrazoEntrega;
	private int tpProdutoServico;
	private String idProdutoServico;
	private String sgProdutoServico;
	private int cdClassificacaoFiscal;
	private int cdFabricante;
	private int cdMarca;
	private String nmModelo;
	private int cdNcm;
	private String nrReferencia;
	private int cdCategoriaReceita;
	private int cdCategoriaDespesa;
	private double vlServico;
	private int lgReembolsavel;
	
	public ProdutoServico(){ }
	
	//merge da base do juris, campos a mais
	public ProdutoServico(int cdProdutoServico,
			int cdCategoriaEconomica,
			String nmProdutoServico,
			String txtProdutoServico,
			String txtEspecificacao,
			String txtDadoTecnico,
			String txtPrazoEntrega,
			int tpProdutoServico,
			String idProdutoServico,
			String sgProdutoServico,
			int cdClassificacaoFiscal,
			int cdFabricante,
			int cdMarca,
			String nmModelo,
			int cdNcm,
			String nrReferencia){
		setCdProdutoServico(cdProdutoServico);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setNmProdutoServico(nmProdutoServico);
		setTxtProdutoServico(txtProdutoServico);
		setTxtEspecificacao(txtEspecificacao);
		setTxtDadoTecnico(txtDadoTecnico);
		setTxtPrazoEntrega(txtPrazoEntrega);
		setTpProdutoServico(tpProdutoServico);
		setIdProdutoServico(idProdutoServico);
		setSgProdutoServico(sgProdutoServico);
		setCdClassificacaoFiscal(cdClassificacaoFiscal);
		setCdFabricante(cdFabricante);
		setCdMarca(cdMarca);
		setNmModelo(nmModelo);
		setCdNcm(cdNcm);
		setNrReferencia(nrReferencia);
		
		setCdCategoriaReceita(0);
		setCdCategoriaDespesa(0);
		setVlServico(0);
	}
	
	//teste retirando campos cdNcm nrReferencia
	public ProdutoServico(int cdProdutoServico,
			int cdCategoriaEconomica,
			String nmProdutoServico,
			String txtProdutoServico,
			String txtEspecificacao,
			String txtDadoTecnico,
			String txtPrazoEntrega,
			int tpProdutoServico,
			String idProdutoServico,
			String sgProdutoServico,
			int cdClassificacaoFiscal,
			int cdFabricante,
			int cdMarca,
			String nmModelo){
		setCdProdutoServico(cdProdutoServico);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setNmProdutoServico(nmProdutoServico);
		setTxtProdutoServico(txtProdutoServico);
		setTxtEspecificacao(txtEspecificacao);
		setTxtDadoTecnico(txtDadoTecnico);
		setTxtPrazoEntrega(txtPrazoEntrega);
		setTpProdutoServico(tpProdutoServico);
		setIdProdutoServico(idProdutoServico);
		setSgProdutoServico(sgProdutoServico);
		setCdClassificacaoFiscal(cdClassificacaoFiscal);
		setCdFabricante(cdFabricante);
		setCdMarca(cdMarca);
		setNmModelo(nmModelo);
	}
	
	public ProdutoServico(int cdProdutoServico,
			int cdCategoriaEconomica,
			String nmProdutoServico,
			String txtProdutoServico,
			String txtEspecificacao,
			String txtDadoTecnico,
			String txtPrazoEntrega,
			int tpProdutoServico,
			String idProdutoServico,
			String sgProdutoServico,
			int cdClassificacaoFiscal,
			int cdFabricante,
			int cdMarca,
			String nmModelo,
			int cdNcm,
			String nrReferencia,
			int cdCategoriaReceita,
			int cdCategoriaDespesa,
			double vlServico){
		setCdProdutoServico(cdProdutoServico);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setNmProdutoServico(nmProdutoServico);
		setTxtProdutoServico(txtProdutoServico);
		setTxtEspecificacao(txtEspecificacao);
		setTxtDadoTecnico(txtDadoTecnico);
		setTxtPrazoEntrega(txtPrazoEntrega);
		setTpProdutoServico(tpProdutoServico);
		setIdProdutoServico(idProdutoServico);
		setSgProdutoServico(sgProdutoServico);
		setCdClassificacaoFiscal(cdClassificacaoFiscal);
		setCdFabricante(cdFabricante);
		setCdMarca(cdMarca);
		setNmModelo(nmModelo);
		setCdNcm(cdNcm);
		setNrReferencia(nrReferencia);
		setCdCategoriaReceita(cdCategoriaReceita);
		setCdCategoriaDespesa(cdCategoriaDespesa);
		setVlServico(vlServico);
		setLgReembolsavel(0);
	}
	
	public ProdutoServico(int cdProdutoServico,
			int cdCategoriaEconomica,
			String nmProdutoServico,
			String txtProdutoServico,
			String txtEspecificacao,
			String txtDadoTecnico,
			String txtPrazoEntrega,
			int tpProdutoServico,
			String idProdutoServico,
			String sgProdutoServico,
			int cdClassificacaoFiscal,
			int cdFabricante,
			int cdMarca,
			String nmModelo,
			int cdNcm,
			String nrReferencia,
			int cdCategoriaReceita,
			int cdCategoriaDespesa,
			double vlServico,
			int lgReembolsavel){
		setCdProdutoServico(cdProdutoServico);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setNmProdutoServico(nmProdutoServico);
		setTxtProdutoServico(txtProdutoServico);
		setTxtEspecificacao(txtEspecificacao);
		setTxtDadoTecnico(txtDadoTecnico);
		setTxtPrazoEntrega(txtPrazoEntrega);
		setTpProdutoServico(tpProdutoServico);
		setIdProdutoServico(idProdutoServico);
		setSgProdutoServico(sgProdutoServico);
		setCdClassificacaoFiscal(cdClassificacaoFiscal);
		setCdFabricante(cdFabricante);
		setCdMarca(cdMarca);
		setNmModelo(nmModelo);
		setCdNcm(cdNcm);
		setNrReferencia(nrReferencia);
		setCdCategoriaReceita(cdCategoriaReceita);
		setCdCategoriaDespesa(cdCategoriaDespesa);
		setVlServico(vlServico);
		setLgReembolsavel(lgReembolsavel);
	}
	
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public void setNmProdutoServico(String nmProdutoServico){
		this.nmProdutoServico=nmProdutoServico;
	}
	public String getNmProdutoServico(){
		return this.nmProdutoServico;
	}
	public void setTxtProdutoServico(String txtProdutoServico){
		this.txtProdutoServico=txtProdutoServico;
	}
	public String getTxtProdutoServico(){
		return this.txtProdutoServico;
	}
	public void setTxtEspecificacao(String txtEspecificacao){
		this.txtEspecificacao=txtEspecificacao;
	}
	public String getTxtEspecificacao(){
		return this.txtEspecificacao;
	}
	public void setTxtDadoTecnico(String txtDadoTecnico){
		this.txtDadoTecnico=txtDadoTecnico;
	}
	public String getTxtDadoTecnico(){
		return this.txtDadoTecnico;
	}
	public void setTxtPrazoEntrega(String txtPrazoEntrega){
		this.txtPrazoEntrega=txtPrazoEntrega;
	}
	public String getTxtPrazoEntrega(){
		return this.txtPrazoEntrega;
	}
	public void setTpProdutoServico(int tpProdutoServico){
		this.tpProdutoServico=tpProdutoServico;
	}
	public int getTpProdutoServico(){
		return this.tpProdutoServico;
	}
	public void setIdProdutoServico(String idProdutoServico){
		this.idProdutoServico=idProdutoServico;
	}
	public String getIdProdutoServico(){
		return this.idProdutoServico;
	}
	public void setSgProdutoServico(String sgProdutoServico){
		this.sgProdutoServico=sgProdutoServico;
	}
	public String getSgProdutoServico(){
		return this.sgProdutoServico;
	}
	public void setCdClassificacaoFiscal(int cdClassificacaoFiscal){
		this.cdClassificacaoFiscal=cdClassificacaoFiscal;
	}
	public int getCdClassificacaoFiscal(){
		return this.cdClassificacaoFiscal;
	}
	public void setCdFabricante(int cdFabricante){
		this.cdFabricante=cdFabricante;
	}
	public int getCdFabricante(){
		return this.cdFabricante;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public void setCdNcm(int cdNcm) {
		this.cdNcm = cdNcm;
	}
	public int getCdNcm() {
		return cdNcm;
	}
	public void setNrReferencia(String nrReferencia) {
		this.nrReferencia = nrReferencia;
	}
	public String getNrReferencia() {
		return nrReferencia;
	}
	public void setCdCategoriaReceita(int cdCategoriaReceita){
		this.cdCategoriaReceita=cdCategoriaReceita;
	}
	public int getCdCategoriaReceita(){
		return this.cdCategoriaReceita;
	}
	public void setCdCategoriaDespesa(int cdCategoriaDespesa){
		this.cdCategoriaDespesa=cdCategoriaDespesa;
	}
	public int getCdCategoriaDespesa(){
		return this.cdCategoriaDespesa;
	}
	public void setVlServico(double vlServico){
		this.vlServico=vlServico;
	}
	public double getVlServico(){
		return this.vlServico;
	}
	public void setLgReembolsavel(int lgReembolsavel){
		this.lgReembolsavel=lgReembolsavel;
	}
	public int getLgReembolsavel(){
		return this.lgReembolsavel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdProdutoServico\": " +  getCdProdutoServico();
		valueToString += ", \"cdCategoriaEconomica\": " +  getCdCategoriaEconomica();
		valueToString += ", \"nmProdutoServico\": \"" +  getNmProdutoServico()+"\"";
		valueToString += ", \"txtProdutoServico\": \"" +  getTxtProdutoServico()+"\"";
		valueToString += ", \"txtEspecificacao\": \"" +  getTxtEspecificacao()+"\"";
		valueToString += ", \"txtDadoTecnico\": \"" +  getTxtDadoTecnico()+"\"";
		valueToString += ", \"txtPrazoEntrega\": \"" +  getTxtPrazoEntrega()+"\"";
		valueToString += ", \"tpProdutoServico\": " +  getTpProdutoServico();
		valueToString += ", \"idProdutoServico\": \"" +  getIdProdutoServico()+"\"";
		valueToString += ", \"sgProdutoServico\": \"" +  getSgProdutoServico()+"\"";
		valueToString += ", \"cdClassificacaoFiscal\": " +  getCdClassificacaoFiscal();
		valueToString += ", \"cdFabricante\": " +  getCdFabricante();
		valueToString += ", \"cdMarca\": " +  getCdMarca();
		valueToString += ", \"nmModelo\": \"" +  getNmModelo()+"\"";
		valueToString += ", \"cdNcm\": " +  getCdNcm();
		valueToString += ", \"nrReferencia\": \"" +  getNrReferencia()+"\"";
		valueToString += ", \"cdCategoriaReceita\": " +  getCdCategoriaReceita();
		valueToString += ", \"cdCategoriaDespesa\": " +  getCdCategoriaDespesa();
		valueToString += ", \"vlServico\": " +  getVlServico();
		valueToString += ", \"lgReembolsavel\": "+getLgReembolsavel();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdProdutoServico\": " +  getCdProdutoServico();
		valueToString += ", \"cdCategoriaEconomica\": " +  getCdCategoriaEconomica();
		valueToString += ", \"nmProdutoServico\": \"" +  getNmProdutoServico()+"\"";
		valueToString += ", \"txtProdutoServico\": \"" +  getTxtProdutoServico()+"\"";
		valueToString += ", \"txtEspecificacao\": \"" +  getTxtEspecificacao()+"\"";
		valueToString += ", \"txtDadoTecnico\": \"" +  getTxtDadoTecnico()+"\"";
		valueToString += ", \"txtPrazoEntrega\": \"" +  getTxtPrazoEntrega()+"\"";
		valueToString += ", \"tpProdutoServico\": " +  getTpProdutoServico();
		valueToString += ", \"idProdutoServico\": \"" +  getIdProdutoServico()+"\"";
		valueToString += ", \"sgProdutoServico\": \"" +  getSgProdutoServico()+"\"";
		valueToString += ", \"cdClassificacaoFiscal\": " +  getCdClassificacaoFiscal();
		valueToString += ", \"cdFabricante\": " +  getCdFabricante();
		valueToString += ", \"cdMarca\": " +  getCdMarca();
		valueToString += ", \"nmModelo\": \"" +  getNmModelo()+"\"";
		valueToString += ", \"cdNcm\": " +  getCdNcm();
		valueToString += ", \"nrReferencia\": \"" +  getNrReferencia()+"\"";
		valueToString += ", \"cdCategoriaReceita\": " +  getCdCategoriaReceita();
		valueToString += ", \"cdCategoriaDespesa\": " +  getCdCategoriaDespesa();
		valueToString += ", \"vlServico\": " +  getVlServico();
		valueToString += ", \"lgReembolsavel\": "+getLgReembolsavel();
		return "{" + valueToString + "}";
	}
	
	public static String fromRegister(Map<String, Object> register) {
		String valueToString = "";
		valueToString += "\"cdProdutoServico\": " +  register.get("CD_PRODUTO_SERVICO");
		valueToString += ", \"cdCategoriaEconomica\": " +  register.get("CD_CATEGORIA_ECONOMICA");
		valueToString += ", \"nmProdutoServico\": \"" +  register.get("NM_PRODUTO_SERVICO")+"\"";
		valueToString += ", \"txtProdutoServico\": \"" + register.get("TXT_PRODUTO_SERVICO")+"\"";
		valueToString += ", \"txtEspecificacao\": \"" + register.get("TXT_ESPECIFICACAO")+"\"";
		valueToString += ", \"txtDadoTecnico\": \"" + register.get("TXT_DADO_TECNICO")+"\"";
		valueToString += ", \"txtPrazoEntrega\": \"" + register.get("TXT_PRAZO_ENTREGA")+"\"";
		valueToString += ", \"tpProdutoServico\": " + register.get("TP_PRODUTO_SERVICO");
		valueToString += ", \"idProdutoServico\": \"" + register.get("ID_PRODUTO_SERVICO")+"\"";
		valueToString += ", \"sgProdutoServico\": \"" + register.get("SG_PRODUTO_SERVICO")+"\"";
		valueToString += ", \"cdClassificacaoFiscal\": " + register.get("CD_CLASSIFICACAO_FISCAL");
		valueToString += ", \"cdFabricante\": " + register.get("CD_FABRICANTE");
		valueToString += ", \"cdMarca\": " + register.get("CD_MARCA");
		valueToString += ", \"nmModelo\": \"" + register.get("NM_MODELO")+"\"";
		valueToString += ", \"cdNcm\": " + register.get("CD_NCM");
		valueToString += ", \"nrReferencia\": \"" + register.get("NR_REFERENCIA")+"\"";
		valueToString += ", \"cdCategoriaReceita\": " + register.get("CD_CATEGORIA_RECEITA");
		valueToString += ", \"cdCategoriaDespesa\": " + register.get("CD_CATEGORIA_DESPESA");
		valueToString += ", \"vlServico\": " + register.get("VL_SERVICO");
		valueToString += ", \"lgReembolsavel\": "+ register.get("LG_REEMBOLSAVEL");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoServico(getCdProdutoServico(),
			getCdCategoriaEconomica(),
			getNmProdutoServico(),
			getTxtProdutoServico(),
			getTxtEspecificacao(),
			getTxtDadoTecnico(),
			getTxtPrazoEntrega(),
			getTpProdutoServico(),
			getIdProdutoServico(),
			getSgProdutoServico(),
			getCdClassificacaoFiscal(),
			getCdFabricante(),
			getCdMarca(),
			getNmModelo(),
			getCdNcm(),
			getNrReferencia(),
			getCdCategoriaReceita(),
			getCdCategoriaDespesa(),
			getVlServico(),
			getLgReembolsavel());
	}

}
