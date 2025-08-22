package com.tivic.manager.fta;

public class ModeloVeiculo extends com.tivic.manager.bpm.Bem {
	private int cdModelo;
	private int cdMarca;
	private int nrPortas;
	private int tpCombustivel;
	private String nrCapacidade;
	private int tpReboque;
	private int tpCarga;
	private int nrPotencia;
	private int nrCilindrada;
	private int qtCapacidadeTanque;
	private float qtConsumoUrbano;
	private float qtConsumoRodoviario;
	private int tpEixoDianteiro;
	private int tpEixoTraseiro;
	private int qtEixosDianteiros;
	private int qtEixosTraseiros;
	private String nmModelo;

	public ModeloVeiculo(int cdProdutoServico,
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
			int cdMarcaProdutoServico,
			String nmModeloProdutoServico,
			int cdNcm, 
			String nrReferencia,
			
			int cdClassificacao,
			float prDepreciacao,
			
			int cdModelo,
			int cdMarca,
			int nrPortas,
			int tpCombustivel,
			String nrCapacidade,
			int tpReboque,
			int tpCarga,
			int nrPotencia,
			int nrCilindrada,
			int qtCapacidadeTanque,
			float qtConsumoUrbano,
			float qtConsumoRodoviario,
			int tpEixoDianteiro,
			int tpEixoTraseiro,
			int qtEixosDianteiros,
			int qtEixosTraseiros,
			String nmModelo){
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
				cdMarcaProdutoServico,
				nmModeloProdutoServico,
				cdNcm,
				nrReferencia,
				cdClassificacao,
				prDepreciacao);
		setCdModelo(cdModelo);
		setCdMarca(cdMarca);
		setNrPortas(nrPortas);
		setTpCombustivel(tpCombustivel);
		setNrCapacidade(nrCapacidade);
		setTpReboque(tpReboque);
		setTpCarga(tpCarga);
		setNrPotencia(nrPotencia);
		setNrCilindrada(nrCilindrada);
		setQtCapacidadeTanque(qtCapacidadeTanque);
		setQtConsumoUrbano(qtConsumoUrbano);
		setQtConsumoRodoviario(qtConsumoRodoviario);
		setTpEixoDianteiro(tpEixoDianteiro);
		setTpEixoTraseiro(tpEixoTraseiro);
		setQtEixosDianteiros(qtEixosDianteiros);
		setQtEixosTraseiros(qtEixosTraseiros);
		setNmModelo(nmModelo);
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo = cdModelo;
		setCdProdutoServico(cdModelo);
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setNrPortas(int nrPortas){
		this.nrPortas=nrPortas;
	}
	public int getNrPortas(){
		return this.nrPortas;
	}
	public void setTpCombustivel(int tpCombustivel){
		this.tpCombustivel=tpCombustivel;
	}
	public int getTpCombustivel(){
		return this.tpCombustivel;
	}
	public void setNrCapacidade(String nrCapacidade){
		this.nrCapacidade=nrCapacidade;
	}
	public String getNrCapacidade(){
		return this.nrCapacidade;
	}
	public void setTpReboque(int tpReboque){
		this.tpReboque=tpReboque;
	}
	public int getTpReboque(){
		return this.tpReboque;
	}
	public void setTpCarga(int tpCarga){
		this.tpCarga=tpCarga;
	}
	public int getTpCarga(){
		return this.tpCarga;
	}
	public void setNrPotencia(int nrPotencia){
		this.nrPotencia=nrPotencia;
	}
	public int getNrPotencia(){
		return this.nrPotencia;
	}
	public void setNrCilindrada(int nrCilindrada){
		this.nrCilindrada=nrCilindrada;
	}
	public int getNrCilindrada(){
		return this.nrCilindrada;
	}
	public void setQtCapacidadeTanque(int qtCapacidadeTanque){
		this.qtCapacidadeTanque=qtCapacidadeTanque;
	}
	public int getQtCapacidadeTanque(){
		return this.qtCapacidadeTanque;
	}
	public void setQtConsumoUrbano(float qtConsumoUrbano){
		this.qtConsumoUrbano=qtConsumoUrbano;
	}
	public float getQtConsumoUrbano(){
		return this.qtConsumoUrbano;
	}
	public void setQtConsumoRodoviario(float qtConsumoRodoviario){
		this.qtConsumoRodoviario=qtConsumoRodoviario;
	}
	public float getQtConsumoRodoviario(){
		return this.qtConsumoRodoviario;
	}
	public void setTpEixoDianteiro(int tpEixoDianteiro){
		this.tpEixoDianteiro=tpEixoDianteiro;
	}
	public int getTpEixoDianteiro(){
		return this.tpEixoDianteiro;
	}
	public void setTpEixoTraseiro(int tpEixoTraseiro){
		this.tpEixoTraseiro=tpEixoTraseiro;
	}
	public int getTpEixoTraseiro(){
		return this.tpEixoTraseiro;
	}
	public void setQtEixosDianteiros(int qtEixosDianteiros){
		this.qtEixosDianteiros=qtEixosDianteiros;
	}
	public int getQtEixosDianteiros(){
		return this.qtEixosDianteiros;
	}
	public void setQtEixosTraseiros(int qtEixosTraseiros){
		this.qtEixosTraseiros=qtEixosTraseiros;
	}
	public int getQtEixosTraseiros(){
		return this.qtEixosTraseiros;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModelo: " +  getCdModelo();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", nrPortas: " +  getNrPortas();
		valueToString += ", tpCombustivel: " +  getTpCombustivel();
		valueToString += ", nrCapacidade: " +  getNrCapacidade();
		valueToString += ", tpReboque: " +  getTpReboque();
		valueToString += ", tpCarga: " +  getTpCarga();
		valueToString += ", nrPotencia: " +  getNrPotencia();
		valueToString += ", nrCilindrada: " +  getNrCilindrada();
		valueToString += ", qtCapacidadeTanque: " +  getQtCapacidadeTanque();
		valueToString += ", qtConsumoUrbano: " +  getQtConsumoUrbano();
		valueToString += ", qtConsumoRodoviario: " +  getQtConsumoRodoviario();
		valueToString += ", tpEixoDianteiro: " +  getTpEixoDianteiro();
		valueToString += ", tpEixoTraseiro: " +  getTpEixoTraseiro();
		valueToString += ", qtEixosDianteiros: " +  getQtEixosDianteiros();
		valueToString += ", qtEixosTraseiros: " +  getQtEixosTraseiros();
		valueToString += ", nmModelo: " +  getNmModelo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ModeloVeiculo(getCdProdutoServico(),
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
				super.getCdMarca(),
				super.getNmModelo(),
				super.getCdNcm(),
				super.getNrReferencia(),
			getCdClassificacao(),
			getPrDepreciacao(),
			getCdModelo(),
			getCdMarca(),
			getNrPortas(),
			getTpCombustivel(),
			getNrCapacidade(),
			getTpReboque(),
			getTpCarga(),
			getNrPotencia(),
			getNrCilindrada(),
			getQtCapacidadeTanque(),
			getQtConsumoUrbano(),
			getQtConsumoRodoviario(),
			getTpEixoDianteiro(),
			getTpEixoTraseiro(),
			getQtEixosDianteiros(),
			getQtEixosTraseiros(),
			getNmModelo());
	}

}
