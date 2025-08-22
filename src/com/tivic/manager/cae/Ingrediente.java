package com.tivic.manager.cae;

public class Ingrediente extends com.tivic.manager.grl.Produto {

	private int cdIngrediente;
	private String nmIngrediente;
	private Double vlPerCapta;
	private Double vlKcal;
	private Double vlCho;
	private Double vlPtn;
	private Double vlLip;
	private Double vlFibras;
	private Double vlVitA;
	private Double vlVitC;
	private Double vlCa;
	private Double vlFe;
	private Double vlMg;
	private Double vlZn;
	private int cdGrupo;
	private int cdUnidadeMedida;

	public Ingrediente(){ }

	public Ingrediente(int cdProdutoServico,
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
			float vlPesoUnitario,
			float vlPesoUnitarioEmbalagem,
			float vlComprimento,
			float vlLargura,
			float vlAltura,
			float vlComprimentoEmbalagem,
			float vlLarguraEmbalagem,
			float vlAlturaEmbalagem,
			int qtEmbalagem,
			String nmIngrediente,
			Double vlPerCapta,
			Double vlKcal,
			Double vlCho,
			Double vlPtn,
			Double vlLip,
			Double vlFibras,
			Double vlVitA,
			Double vlVitC,
			Double vlCa,
			Double vlFe,
			Double vlMg,
			Double vlZn,
			int cdGrupo,
			int cdUnidadeMedida){
		super(cdProdutoServico,
			cdCategoriaEconomica,
			nmProdutoServico,
			txtProdutoServico,
			txtEspecificacao,
			txtDadoTecnico,
			txtPrazoEntrega,
			tpProdutoServico,
			idProdutoServico,
			sgProdutoServico,
			cdClassificacaoFiscal,
			cdFabricante,
			cdMarca,
			nmModelo,
			cdNcm,
			nrReferencia,
			vlPesoUnitario,
			vlPesoUnitarioEmbalagem,
			vlComprimento,
			vlLargura,
			vlAltura,
			vlComprimentoEmbalagem,
			vlLarguraEmbalagem,
			vlAlturaEmbalagem,
			qtEmbalagem);
		setCdIngrediente(cdProdutoServico);
		setNmIngrediente(nmIngrediente);
		setVlPerCapta(vlPerCapta);
		setVlKcal(vlKcal);
		setVlCho(vlCho);
		setVlPtn(vlPtn);
		setVlLip(vlLip);
		setVlFibras(vlFibras);
		setVlVitA(vlVitA);
		setVlVitC(vlVitC);
		setVlCa(vlCa);
		setVlFe(vlFe);
		setVlMg(vlMg);
		setVlZn(vlZn);
		setCdGrupo(cdGrupo);
		setCdUnidadeMedida(cdUnidadeMedida);
	}
	public void setCdIngrediente(int cdIngrediente){
		this.cdIngrediente=cdIngrediente;
	}
	public int getCdIngrediente(){
		return this.cdIngrediente;
	}
	public void setNmIngrediente(String nmIngrediente){
		this.nmIngrediente=nmIngrediente;
	}
	public String getNmIngrediente(){
		return this.nmIngrediente;
	}
	public void setVlPerCapta(Double vlPerCapta){
		this.vlPerCapta=vlPerCapta;
	}
	public Double getVlPerCapta(){
		return this.vlPerCapta;
	}
	public void setVlKcal(Double vlKcal){
		this.vlKcal=vlKcal;
	}
	public Double getVlKcal(){
		return this.vlKcal;
	}
	public void setVlCho(Double vlCho){
		this.vlCho=vlCho;
	}
	public Double getVlCho(){
		return this.vlCho;
	}
	public void setVlPtn(Double vlPtn){
		this.vlPtn=vlPtn;
	}
	public Double getVlPtn(){
		return this.vlPtn;
	}
	public void setVlLip(Double vlLip){
		this.vlLip=vlLip;
	}
	public Double getVlLip(){
		return this.vlLip;
	}
	public void setVlFibras(Double vlFibras){
		this.vlFibras=vlFibras;
	}
	public Double getVlFibras(){
		return this.vlFibras;
	}
	public void setVlVitA(Double vlVitA){
		this.vlVitA=vlVitA;
	}
	public Double getVlVitA(){
		return this.vlVitA;
	}
	public void setVlVitC(Double vlVitC){
		this.vlVitC=vlVitC;
	}
	public Double getVlVitC(){
		return this.vlVitC;
	}
	public void setVlCa(Double vlCa){
		this.vlCa=vlCa;
	}
	public Double getVlCa(){
		return this.vlCa;
	}
	public void setVlFe(Double vlFe){
		this.vlFe=vlFe;
	}
	public Double getVlFe(){
		return this.vlFe;
	}
	public void setVlMg(Double vlMg){
		this.vlMg=vlMg;
	}
	public Double getVlMg(){
		return this.vlMg;
	}
	public void setVlZn(Double vlZn){
		this.vlZn=vlZn;
	}
	public Double getVlZn(){
		return this.vlZn;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdUnidadeMedida(int cdUnidadeMedida){
		this.cdUnidadeMedida=cdUnidadeMedida;
	}
	public int getCdUnidadeMedida(){
		return this.cdUnidadeMedida;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdIngrediente: " +  getCdIngrediente();
		valueToString += ", nmIngrediente: " +  getNmIngrediente();
		valueToString += ", vlPerCapta: " +  getVlPerCapta();
		valueToString += ", vlKcal: " +  getVlKcal();
		valueToString += ", vlCho: " +  getVlCho();
		valueToString += ", vlPtn: " +  getVlPtn();
		valueToString += ", vlLip: " +  getVlLip();
		valueToString += ", vlFibras: " +  getVlFibras();
		valueToString += ", vlVitA: " +  getVlVitA();
		valueToString += ", vlVitC: " +  getVlVitC();
		valueToString += ", vlCa: " +  getVlCa();
		valueToString += ", vlFe: " +  getVlFe();
		valueToString += ", vlMg: " +  getVlMg();
		valueToString += ", vlZn: " +  getVlZn();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdUnidadeMedida: " +  getCdUnidadeMedida();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ingrediente(getCdProdutoServico(),
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
			getVlPesoUnitario(),
			getVlPesoUnitarioEmbalagem(),
			getVlComprimento(),
			getVlLargura(),
			getVlAltura(),
			getVlComprimentoEmbalagem(),
			getVlLarguraEmbalagem(),
			getVlAlturaEmbalagem(),
			getQtEmbalagem(),
			getNmIngrediente(),
			getVlPerCapta(),
			getVlKcal(),
			getVlCho(),
			getVlPtn(),
			getVlLip(),
			getVlFibras(),
			getVlVitA(),
			getVlVitC(),
			getVlCa(),
			getVlFe(),
			getVlMg(),
			getVlZn(),
			getCdGrupo(),
			getCdUnidadeMedida());
	}

}