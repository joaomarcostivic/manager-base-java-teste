package com.tivic.manager.fta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Veiculo {

	private int cdVeiculo;
	private int cdProprietario;
	private int cdModelo;
	private int cdTipoVeiculo;
	private int cdReboque;
	private String nrPlaca;
	private String nrAnoFabricacao;
	private String nrAnoModelo;
	private String nmCor;
	private int nrPortas;
	private String nrChassi;
	private String nrRenavam;
	private int nrHodometroInicial;
	private int tpCombustivel;
	private String nrCapacidade;
	private int nrPotencia;
	private int nrCilindrada;
	private String nrTabelaReferencia;
	private String txtObservacao;
	private int qtCapacidadeTanque;
	private float qtConsumoUrbano;
	private float qtConsumoRodoviario;
	private int tpEixoDianteiro;
	private int tpEixoTraseiro;
	private int qtEixosDianteiros;
	private int qtEixosTraseiros;
	private int stVeiculo;
	private float qtHodometroAtual;
	private int cdCor;
	private int cdTipo;
	private int cdCategoria;
	private int cdMarca;
	private int cdEspecie;
	private int cdEndereco;
	private int cdCidade;
	private int cdFrota;
	private int tpAdaptacao;
	private int tpUtilizacao;
	private int cdMarcaCarroceria;
	private String nrAnoChassi;
	private String nrAnoCarroceria;
	private String nmPrefixo;
	private String nrAnoLicenciamento;
	private float qtEntreEixos;
	private float qtComprimentoInterno;
	private float qtLarguraInterna;
	private float qtLarguraCorredor;
	private float qtAreaCorredor;
	private int qtMaxEmPe;
	private String nrLicenciamento;
	private int cdPlanoVistoria;
	
	public Veiculo(){ }
	
	public Veiculo(int cdVeiculo,
			int cdProprietario,
			int cdModelo,
			int cdTipoVeiculo,
			int cdReboque,
			String nrPlaca,
			String nrAnoFabricacao,
			String nrAnoModelo,
			String nmCor,
			int nrPortas,
			String nrChassi,
			String nrRenavam,
			int nrHodometroInicial,
			int tpCombustivel,
			String nrCapacidade,
			int nrPotencia,
			int nrCilindrada,
			String nrTabelaReferencia,
			String txtObservacao,
			int qtCapacidadeTanque,
			float qtConsumoUrbano,
			float qtConsumoRodoviario,
			int tpEixoDianteiro,
			int tpEixoTraseiro,
			int qtEixosDianteiros,
			int qtEixosTraseiros,
			int stVeiculo,
			float qtHodometroAtual,
			int cdCor,
			int cdTipo,
			int cdCategoria,
			int cdMarca,
			int cdEspecie,
			int cdEndereco,
			int cdCidade,
			int cdFrota,
			int tpAdaptacao,
			int tpUtilizacao,
			int cdMarcaCarroceria,
			String nrAnoChassi,
			String nrAnoCarroceria,
			String nmPrefixo,
			String nrAnoLicenciamento,
			float qtEntreEixos,
			float qtComprimentoInterno,
			float qtLarguraInterna,
			float qtLarguraCorredor,
			float qtAreaCorredor,
			int qtMaxEmPe,
			String nrLicenciamento,
			int cdPlanoVistoria){
		setCdVeiculo(cdVeiculo);
		setCdProprietario(cdProprietario);
		setCdModelo(cdModelo);
		setCdTipoVeiculo(cdTipoVeiculo);
		setCdReboque(cdReboque);
		setNrPlaca(nrPlaca);
		setNrAnoFabricacao(nrAnoFabricacao);
		setNrAnoModelo(nrAnoModelo);
		setNmCor(nmCor);
		setNrPortas(nrPortas);
		setNrChassi(nrChassi);
		setNrRenavam(nrRenavam);
		setNrHodometroInicial(nrHodometroInicial);
		setTpCombustivel(tpCombustivel);
		setNrCapacidade(nrCapacidade);
		setNrPotencia(nrPotencia);
		setNrCilindrada(nrCilindrada);
		setNrTabelaReferencia(nrTabelaReferencia);
		setTxtObservacao(txtObservacao);
		setQtCapacidadeTanque(qtCapacidadeTanque);
		setQtConsumoUrbano(qtConsumoUrbano);
		setQtConsumoRodoviario(qtConsumoRodoviario);
		setTpEixoDianteiro(tpEixoDianteiro);
		setTpEixoTraseiro(tpEixoTraseiro);
		setQtEixosDianteiros(qtEixosDianteiros);
		setQtEixosTraseiros(qtEixosTraseiros);
		setStVeiculo(stVeiculo);
		setQtHodometroAtual(qtHodometroAtual);
		setCdCor(cdCor);
		setCdTipo(cdTipo);
		setCdCategoria(cdCategoria);
		setCdMarca(cdMarca);
		setCdEspecie(cdEspecie);
		setCdEndereco(cdEndereco);
		setCdCidade(cdCidade);
		setCdFrota(cdFrota);
		setTpAdaptacao(tpAdaptacao);
		setTpUtilizacao(tpUtilizacao);
		setCdMarcaCarroceria(cdMarcaCarroceria);
		setNrAnoChassi(nrAnoChassi);
		setNrAnoCarroceria(nrAnoCarroceria);
		setNmPrefixo(nmPrefixo);
		setNrAnoLicenciamento(nrAnoLicenciamento);
		setQtEntreEixos(qtEntreEixos);
		setQtComprimentoInterno(qtComprimentoInterno);
		setQtLarguraInterna(qtLarguraInterna);
		setQtLarguraCorredor(qtLarguraCorredor);
		setQtAreaCorredor(qtAreaCorredor);
		setQtMaxEmPe(qtMaxEmPe);
		setNrLicenciamento(nrLicenciamento);
		setCdPlanoVistoria(cdPlanoVistoria);
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdProprietario(int cdProprietario){
		this.cdProprietario=cdProprietario;
	}
	public int getCdProprietario(){
		return this.cdProprietario;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setCdTipoVeiculo(int cdTipoVeiculo){
		this.cdTipoVeiculo=cdTipoVeiculo;
	}
	public int getCdTipoVeiculo(){
		return this.cdTipoVeiculo;
	}
	public void setCdReboque(int cdReboque){
		this.cdReboque=cdReboque;
	}
	public int getCdReboque(){
		return this.cdReboque;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setNrAnoFabricacao(String nrAnoFabricacao){
		this.nrAnoFabricacao=nrAnoFabricacao;
	}
	public String getNrAnoFabricacao(){
		return this.nrAnoFabricacao;
	}
	public void setNrAnoModelo(String nrAnoModelo){
		this.nrAnoModelo=nrAnoModelo;
	}
	public String getNrAnoModelo(){
		return this.nrAnoModelo;
	}
	public void setNmCor(String nmCor){
		this.nmCor=nmCor;
	}
	public String getNmCor(){
		return this.nmCor;
	}
	public void setNrPortas(int nrPortas){
		this.nrPortas=nrPortas;
	}
	public int getNrPortas(){
		return this.nrPortas;
	}
	public void setNrChassi(String nrChassi){
		this.nrChassi=nrChassi;
	}
	public String getNrChassi(){
		return this.nrChassi;
	}
	public void setNrRenavam(String nrRenavam){
		this.nrRenavam=nrRenavam;
	}
	public String getNrRenavam(){
		return this.nrRenavam;
	}
	public void setNrHodometroInicial(int nrHodometroInicial){
		this.nrHodometroInicial=nrHodometroInicial;
	}
	public int getNrHodometroInicial(){
		return this.nrHodometroInicial;
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
	public void setNrTabelaReferencia(String nrTabelaReferencia){
		this.nrTabelaReferencia=nrTabelaReferencia;
	}
	public String getNrTabelaReferencia(){
		return this.nrTabelaReferencia;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
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
	public void setStVeiculo(int stVeiculo){
		this.stVeiculo=stVeiculo;
	}
	public int getStVeiculo(){
		return this.stVeiculo;
	}
	public void setQtHodometroAtual(float qtHodometroAtual){
		this.qtHodometroAtual=qtHodometroAtual;
	}
	public float getQtHodometroAtual(){
		return this.qtHodometroAtual;
	}
	public void setCdCor(int cdCor){
		this.cdCor=cdCor;
	}
	public int getCdCor(){
		return this.cdCor;
	}
	public void setCdTipo(int cdTipo){
		this.cdTipo=cdTipo;
	}
	public int getCdTipo(){
		return this.cdTipo;
	}
	public void setCdCategoria(int cdCategoria){
		this.cdCategoria=cdCategoria;
	}
	public int getCdCategoria(){
		return this.cdCategoria;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setCdEspecie(int cdEspecie){
		this.cdEspecie=cdEspecie;
	}
	public int getCdEspecie(){
		return this.cdEspecie;
	}
	public void setCdEndereco(int cdEndereco){
		this.cdEndereco=cdEndereco;
	}
	public int getCdEndereco(){
		return this.cdEndereco;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdFrota(int cdFrota){
		this.cdFrota=cdFrota;
	}
	public int getCdFrota(){
		return this.cdFrota;
	}
	public void setTpAdaptacao(int tpAdaptacao){
		this.tpAdaptacao=tpAdaptacao;
	}
	public int getTpAdaptacao(){
		return this.tpAdaptacao;
	}
	public void setTpUtilizacao(int tpUtilizacao){
		this.tpUtilizacao=tpUtilizacao;
	}
	public int getTpUtilizacao(){
		return this.tpUtilizacao;
	}
	public void setCdMarcaCarroceria(int cdMarcaCarroceria){
		this.cdMarcaCarroceria=cdMarcaCarroceria;
	}
	public int getCdMarcaCarroceria(){
		return this.cdMarcaCarroceria;
	}
	public void setNrAnoChassi(String nrAnoChassi){
		this.nrAnoChassi=nrAnoChassi;
	}
	public String getNrAnoChassi(){
		return this.nrAnoChassi;
	}
	public void setNrAnoCarroceria(String nrAnoCarroceria){
		this.nrAnoCarroceria=nrAnoCarroceria;
	}
	public String getNrAnoCarroceria(){
		return this.nrAnoCarroceria;
	}
	public void setNmPrefixo(String nmPrefixo){
		this.nmPrefixo=nmPrefixo;
	}
	public String getNmPrefixo(){
		return this.nmPrefixo;
	}
	public void setNrAnoLicenciamento(String nrAnoLicenciamento){
		this.nrAnoLicenciamento=nrAnoLicenciamento;
	}
	public String getNrAnoLicenciamento(){
		return this.nrAnoLicenciamento;
	}
	public void setQtEntreEixos(float qtEntreEixos){
		this.qtEntreEixos=qtEntreEixos;
	}
	public float getQtEntreEixos(){
		return this.qtEntreEixos;
	}
	public void setQtComprimentoInterno(float qtComprimentoInterno ){
		this.qtComprimentoInterno=qtComprimentoInterno;
	}
	public float getQtComprimentoInterno(){
		return this.qtComprimentoInterno;
	}
	public void setQtLarguraInterna(float qtLarguraInterna){
		this.qtLarguraInterna=qtLarguraInterna;
	}
	public float getQtLarguraInterna(){
		return this.qtLarguraInterna;
	}
	public void setQtLarguraCorredor(float qtLarguraCorredor){
		this.qtLarguraCorredor=qtLarguraCorredor;
	}
	public float getQtLarguraCorredor(){
		return this.qtLarguraCorredor;
	}
	public void setQtAreaCorredor(float qtAreaCorredor){
		this.qtAreaCorredor=qtAreaCorredor;
	}
	public float getQtAreaCorredor(){
		return this.qtAreaCorredor;
	}
	public void setQtMaxEmPe(int qtMaxEmPe){
		this.qtMaxEmPe=qtMaxEmPe;
	}
	public int getQtMaxEmPe(){
		return this.qtMaxEmPe;
	}
	public void setNrLicenciamento(String nrLicenciamento){
		this.nrLicenciamento=nrLicenciamento;
	}
	public String getNrLicenciamento(){
		return this.nrLicenciamento;
	}
	public void setCdPlanoVistoria(int cdPlanoVistoria){
		this.cdPlanoVistoria=cdPlanoVistoria;
	}
	public int getCdPlanoVistoria(){
		return this.cdPlanoVistoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdProprietario: " +  getCdProprietario();
		valueToString += ", cdModelo: " +  getCdModelo();
		valueToString += ", cdTipoVeiculo: " +  getCdTipoVeiculo();
		valueToString += ", cdReboque: " +  getCdReboque();
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", nrAnoFabricacao: " +  getNrAnoFabricacao();
		valueToString += ", nrAnoModelo: " +  getNrAnoModelo();
		valueToString += ", nmCor: " +  getNmCor();
		valueToString += ", nrPortas: " +  getNrPortas();
		valueToString += ", nrChassi: " +  getNrChassi();
		valueToString += ", nrRenavam: " +  getNrRenavam();
		valueToString += ", nrHodometroInicial: " +  getNrHodometroInicial();
		valueToString += ", tpCombustivel: " +  getTpCombustivel();
		valueToString += ", nrCapacidade: " +  getNrCapacidade();
		valueToString += ", nrPotencia: " +  getNrPotencia();
		valueToString += ", nrCilindrada: " +  getNrCilindrada();
		valueToString += ", nrTabelaReferencia: " +  getNrTabelaReferencia();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", qtCapacidadeTanque: " +  getQtCapacidadeTanque();
		valueToString += ", qtConsumoUrbano: " +  getQtConsumoUrbano();
		valueToString += ", qtConsumoRodoviario: " +  getQtConsumoRodoviario();
		valueToString += ", tpEixoDianteiro: " +  getTpEixoDianteiro();
		valueToString += ", tpEixoTraseiro: " +  getTpEixoTraseiro();
		valueToString += ", qtEixosDianteiros: " +  getQtEixosDianteiros();
		valueToString += ", qtEixosTraseiros: " +  getQtEixosTraseiros();
		valueToString += ", stVeiculo: " +  getStVeiculo();
		valueToString += ", qtHodometroAtual: " +  getQtHodometroAtual();
		valueToString += ", cdCor: " +  getCdCor();
		valueToString += ", cdTipo: " +  getCdTipo();
		valueToString += ", cdCategoria: " +  getCdCategoria();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", cdEspecie: " +  getCdEspecie();
		valueToString += ", cdEndereco: " +  getCdEndereco();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdFrota: " +  getCdFrota();
		valueToString += ", tpAdaptacao: " +  getTpAdaptacao();
		valueToString += ", tpUtilizacao: " +  getTpUtilizacao();
		valueToString += ", cdMarcaCarroceria: " +  getCdMarcaCarroceria();
		valueToString += ", nrAnoChassi: " +  getNrAnoChassi();
		valueToString += ", nrAnoCarroceria: " +  getNrAnoCarroceria();
		valueToString += ", nmPrefixo: " +  getNmPrefixo();
		valueToString += ", nrAnoLicenciamento: " +  getNrAnoLicenciamento();
		valueToString += ", qtEntreEixos: " +  getQtEntreEixos();
		valueToString += ", qtComprimentoInterno: " +  getQtComprimentoInterno();
		valueToString += ", qtLarguraInterna: " +  getQtLarguraInterna();
		valueToString += ", qtLarguraCorredor: " +  getQtLarguraCorredor();
		valueToString += ", qtAreaCorredor: " +  getQtAreaCorredor();
		valueToString += ", qtMaxEmPe: " +  getQtMaxEmPe();
		valueToString += ", nrLicenciamento: " +  getNrLicenciamento();
		valueToString += ", cdPlanoVistoria: " +  getCdPlanoVistoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Veiculo(getCdVeiculo(),
			getCdProprietario(),
			getCdModelo(),
			getCdTipoVeiculo(),
			getCdReboque(),
			getNrPlaca(),
			getNrAnoFabricacao(),
			getNrAnoModelo(),
			getNmCor(),
			getNrPortas(),
			getNrChassi(),
			getNrRenavam(),
			getNrHodometroInicial(),
			getTpCombustivel(),
			getNrCapacidade(),
			getNrPotencia(),
			getNrCilindrada(),
			getNrTabelaReferencia(),
			getTxtObservacao(),
			getQtCapacidadeTanque(),
			getQtConsumoUrbano(),
			getQtConsumoRodoviario(),
			getTpEixoDianteiro(),
			getTpEixoTraseiro(),
			getQtEixosDianteiros(),
			getQtEixosTraseiros(),
			getStVeiculo(),
			getQtHodometroAtual(),
			getCdCor(),
			getCdTipo(),
			getCdCategoria(),
			getCdMarca(),
			getCdEspecie(),
			getCdEndereco(),
			getCdCidade(),
			getCdFrota(),
			getTpAdaptacao(),
			getTpUtilizacao(),
			getCdMarcaCarroceria(),
			getNrAnoChassi(),
			getNrAnoCarroceria(),
			getNmPrefixo(),
			getNrAnoLicenciamento(),
			getQtEntreEixos(),
			getQtComprimentoInterno(),
			getQtLarguraInterna(),
			getQtLarguraCorredor(),
			getQtAreaCorredor(),
			getQtMaxEmPe(),
			getNrLicenciamento(),
			getCdPlanoVistoria());
	}

}