package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoatVeiculo {

	private int cdBoatVeiculo;
	private int cdBoat;
	private String nmProprietario;
	private int tpDocumento;
	private String nrDocumento;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String ufCnhCondutor;
	private String nrPlaca;
	private String txtDeclaracaoCondutor;
	private int cdEspecie;
	private int cdMarca;
	private int cdCidade;
	private int cdCor;
	private int cdTipo;
	private String dsEndereco;
	private String nrTelefone;
	private int tpCategoriaCnh;
	private int tpSexo;
	private int nrIdade;
	private int cdCategoria;
	private String nrDocumentoCondutor;
	private GregorianCalendar dtVencimentoCnh;
	private GregorianCalendar dtPrimeiraHabilitacao;
	private int cdVeiculo;
	private int lgCondutorEvadiu;
	private String nrTelefoneProprietario;
	private String nrChassi;
	private int lgBuscaDetran;
	private String nrRenavam;
	private String nrAnoFabricacao;
	private int lgDanoDianteiraDireita;
	private int lgDanoDianteiraEsquerda;
	private int lgDanoLateralDireita;
	private int lgDanoLateralEsquerda;
	private int lgDanoTraseiraDireita;
	private int lgDanoTraseiraEsquerda;
	private String nrRgCondutor;
	private String nmOrgaoRgCondutor;
	private ArrayList<BoatImagem> imagens;

	public BoatVeiculo() { }

	public BoatVeiculo(int cdBoatVeiculo,
			int cdBoat,
			String nmProprietario,
			int tpDocumento,
			String nrDocumento,
			String nmCondutor,
			String nrCnhCondutor,
			String ufCnhCondutor,
			String nrPlaca,
			String txtDeclaracaoCondutor,
			int cdEspecie,
			int cdMarca,
			int cdCidade,
			int cdCor,
			int cdTipo,
			String dsEndereco,
			String nrTelefone,
			int tpCategoriaCnh,
			int tpSexo,
			int nrIdade,
			int cdCategoria,
			String nrDocumentoCondutor,
			GregorianCalendar dtVencimentoCnh,
			GregorianCalendar dtPrimeiraHabilitacao,
			int cdVeiculo,
			int lgCondutorEvadiu,
			String nrTelefoneProprietario,
			String nrChassi,
			int lgBuscaDetran,
			String nrRenavam,
			String nrAnoFabricacao,
			int lgDanoDianteiraDireita,
			int lgDanoDianteiraEsquerda,
			int lgDanoLateralDireita,
			int lgDanoLateralEsquerda,
			int lgDanoTraseiraDireita,
			int lgDanoTraseiraEsquerda,
			String nrRgCondutor,
			String nmOrgaoRgCondutor) {
		setCdBoatVeiculo(cdBoatVeiculo);
		setCdBoat(cdBoat);
		setNmProprietario(nmProprietario);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setUfCnhCondutor(ufCnhCondutor);
		setNrPlaca(nrPlaca);
		setTxtDeclaracaoCondutor(txtDeclaracaoCondutor);
		setCdEspecie(cdEspecie);
		setCdMarca(cdMarca);
		setCdCidade(cdCidade);
		setCdCor(cdCor);
		setCdTipo(cdTipo);
		setDsEndereco(dsEndereco);
		setNrTelefone(nrTelefone);
		setTpCategoriaCnh(tpCategoriaCnh);
		setTpSexo(tpSexo);
		setNrIdade(nrIdade);
		setCdCategoria(cdCategoria);
		setNrDocumentoCondutor(nrDocumentoCondutor);
		setDtVencimentoCnh(dtVencimentoCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setCdVeiculo(cdVeiculo);
		setLgCondutorEvadiu(lgCondutorEvadiu);
		setNrTelefoneProprietario(nrTelefoneProprietario);
		setNrChassi(nrChassi);
		setLgBuscaDetran(lgBuscaDetran);
		setNrRenavam(nrRenavam);
		setNrAnoFabricacao(nrAnoFabricacao);
		setLgDanoDianteiraDireita(lgDanoDianteiraDireita);
		setLgDanoDianteiraEsquerda(lgDanoDianteiraEsquerda);
		setLgDanoLateralDireita(lgDanoLateralDireita);
		setLgDanoLateralEsquerda(lgDanoLateralEsquerda);
		setLgDanoTraseiraDireita(lgDanoTraseiraDireita);
		setLgDanoTraseiraEsquerda(lgDanoTraseiraEsquerda);
		setNrRgCondutor(nrRgCondutor);
		setNmOrgaoRgCondutor(nmOrgaoRgCondutor);
	}
	public void setCdBoatVeiculo(int cdBoatVeiculo){
		this.cdBoatVeiculo=cdBoatVeiculo;
	}
	public int getCdBoatVeiculo(){
		return this.cdBoatVeiculo;
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setNmProprietario(String nmProprietario){
		this.nmProprietario=nmProprietario;
	}
	public String getNmProprietario(){
		return this.nmProprietario;
	}
	public void setTpDocumento(int tpDocumento){
		this.tpDocumento=tpDocumento;
	}
	public int getTpDocumento(){
		return this.tpDocumento;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setNmCondutor(String nmCondutor){
		this.nmCondutor=nmCondutor;
	}
	public String getNmCondutor(){
		return this.nmCondutor;
	}
	public void setNrCnhCondutor(String nrCnhCondutor){
		this.nrCnhCondutor=nrCnhCondutor;
	}
	public String getNrCnhCondutor(){
		return this.nrCnhCondutor;
	}
	public void setUfCnhCondutor(String ufCnhCondutor){
		this.ufCnhCondutor=ufCnhCondutor;
	}
	public String getUfCnhCondutor(){
		return this.ufCnhCondutor;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setTxtDeclaracaoCondutor(String txtDeclaracaoCondutor){
		this.txtDeclaracaoCondutor=txtDeclaracaoCondutor;
	}
	public String getTxtDeclaracaoCondutor(){
		return this.txtDeclaracaoCondutor;
	}
	public void setCdEspecie(int cdEspecie){
		this.cdEspecie=cdEspecie;
	}
	public int getCdEspecie(){
		return this.cdEspecie;
	}
	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
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
	public void setDsEndereco(String dsEndereco){
		this.dsEndereco=dsEndereco;
	}
	public String getDsEndereco(){
		return this.dsEndereco;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setTpCategoriaCnh(int tpCategoriaCnh){
		this.tpCategoriaCnh=tpCategoriaCnh;
	}
	public int getTpCategoriaCnh(){
		return this.tpCategoriaCnh;
	}
	public void setTpSexo(int tpSexo){
		this.tpSexo=tpSexo;
	}
	public int getTpSexo(){
		return this.tpSexo;
	}
	public void setNrIdade(int nrIdade){
		this.nrIdade=nrIdade;
	}
	public int getNrIdade(){
		return this.nrIdade;
	}
	public void setCdCategoria(int cdCategoria){
		this.cdCategoria=cdCategoria;
	}
	public int getCdCategoria(){
		return this.cdCategoria;
	}
	public void setNrDocumentoCondutor(String nrDocumentoCondutor){
		this.nrDocumentoCondutor=nrDocumentoCondutor;
	}
	public String getNrDocumentoCondutor(){
		return this.nrDocumentoCondutor;
	}
	public void setDtVencimentoCnh(GregorianCalendar dtVencimentoCnh){
		this.dtVencimentoCnh=dtVencimentoCnh;
	}
	public GregorianCalendar getDtVencimentoCnh(){
		return this.dtVencimentoCnh;
	}
	public void setDtPrimeiraHabilitacao(GregorianCalendar dtPrimeiraHabilitacao){
		this.dtPrimeiraHabilitacao=dtPrimeiraHabilitacao;
	}
	public GregorianCalendar getDtPrimeiraHabilitacao(){
		return this.dtPrimeiraHabilitacao;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setLgCondutorEvadiu(int lgCondutorEvadiu){
		this.lgCondutorEvadiu=lgCondutorEvadiu;
	}
	public int getLgCondutorEvadiu(){
		return this.lgCondutorEvadiu;
	}
	public void setNrTelefoneProprietario(String nrTelefoneProprietario){
		this.nrTelefoneProprietario=nrTelefoneProprietario;
	}
	public String getNrTelefoneProprietario(){
		return this.nrTelefoneProprietario;
	}
	public void setNrChassi(String nrChassi){
		this.nrChassi=nrChassi;
	}
	public String getNrChassi(){
		return this.nrChassi;
	}
	public void setLgBuscaDetran(int lgBuscaDetran){
		this.lgBuscaDetran=lgBuscaDetran;
	}
	public int getLgBuscaDetran(){
		return this.lgBuscaDetran;
	}
	public void setNrRenavam(String nrRenavam){
		this.nrRenavam=nrRenavam;
	}
	public String getNrRenavam(){
		return this.nrRenavam;
	}
	public void setNrAnoFabricacao(String nrAnoFabricacao){
		this.nrAnoFabricacao=nrAnoFabricacao;
	}
	public String getNrAnoFabricacao(){
		return this.nrAnoFabricacao;
	}
	public void setLgDanoDianteiraDireita(int lgDanoDianteiraDireita){
		this.lgDanoDianteiraDireita=lgDanoDianteiraDireita;
	}
	public int getLgDanoDianteiraDireita(){
		return this.lgDanoDianteiraDireita;
	}
	public void setLgDanoDianteiraEsquerda(int lgDanoDianteiraEsquerda){
		this.lgDanoDianteiraEsquerda=lgDanoDianteiraEsquerda;
	}
	public int getLgDanoDianteiraEsquerda(){
		return this.lgDanoDianteiraEsquerda;
	}
	public void setLgDanoLateralDireita(int lgDanoLateralDireita){
		this.lgDanoLateralDireita=lgDanoLateralDireita;
	}
	public int getLgDanoLateralDireita(){
		return this.lgDanoLateralDireita;
	}
	public void setLgDanoLateralEsquerda(int lgDanoLateralEsquerda){
		this.lgDanoLateralEsquerda=lgDanoLateralEsquerda;
	}
	public int getLgDanoLateralEsquerda(){
		return this.lgDanoLateralEsquerda;
	}
	public void setLgDanoTraseiraDireita(int lgDanoTraseiraDireita){
		this.lgDanoTraseiraDireita=lgDanoTraseiraDireita;
	}
	public int getLgDanoTraseiraDireita(){
		return this.lgDanoTraseiraDireita;
	}
	public void setLgDanoTraseiraEsquerda(int lgDanoTraseiraEsquerda){
		this.lgDanoTraseiraEsquerda=lgDanoTraseiraEsquerda;
	}
	public int getLgDanoTraseiraEsquerda(){
		return this.lgDanoTraseiraEsquerda;
	}
	public void setNrRgCondutor(String nrRgCondutor){
		this.nrRgCondutor=nrRgCondutor;
	}
	public String getNrRgCondutor(){
		return this.nrRgCondutor;
	}
	public void setNmOrgaoRgCondutor(String nmOrgaoRgCondutor){
		this.nmOrgaoRgCondutor=nmOrgaoRgCondutor;
	}
	public String getNmOrgaoRgCondutor(){
		return this.nmOrgaoRgCondutor;
	}
	
	
	public ArrayList<BoatImagem> getImagens() {
		return imagens;
	}

	public void setImagens(ArrayList<BoatImagem> imagens) {
		this.imagens = imagens;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdBoatVeiculo: " +  getCdBoatVeiculo();
		valueToString += ", cdBoat: " +  getCdBoat();
		valueToString += ", nmProprietario: " +  getNmProprietario();
		valueToString += ", tpDocumento: " +  getTpDocumento();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", nmCondutor: " +  getNmCondutor();
		valueToString += ", nrCnhCondutor: " +  getNrCnhCondutor();
		valueToString += ", ufCnhCondutor: " +  getUfCnhCondutor();
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", txtDeclaracaoCondutor: " +  getTxtDeclaracaoCondutor();
		valueToString += ", cdEspecie: " +  getCdEspecie();
		valueToString += ", cdMarca: " +  getCdMarca();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdCor: " +  getCdCor();
		valueToString += ", cdTipo: " +  getCdTipo();
		valueToString += ", dsEndereco: " +  getDsEndereco();
		valueToString += ", nrTelefone: " +  getNrTelefone();
		valueToString += ", tpCategoriaCnh: " +  getTpCategoriaCnh();
		valueToString += ", tpSexo: " +  getTpSexo();
		valueToString += ", nrIdade: " +  getNrIdade();
		valueToString += ", cdCategoria: " +  getCdCategoria();
		valueToString += ", nrDocumentoCondutor: " +  getNrDocumentoCondutor();
		valueToString += ", dtVencimentoCnh: " +  sol.util.Util.formatDateTime(getDtVencimentoCnh(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtPrimeiraHabilitacao: " +  sol.util.Util.formatDateTime(getDtPrimeiraHabilitacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", lgCondutorEvadiu: " +  getLgCondutorEvadiu();
		valueToString += ", nrTelefoneProprietario: " +  getNrTelefoneProprietario();
		valueToString += ", nrChassi: " +  getNrChassi();
		valueToString += ", lgBuscaDetran: " +  getLgBuscaDetran();
		valueToString += ", nrRenavam: " +  getNrRenavam();
		valueToString += ", nrAnoFabricacao: " +  getNrAnoFabricacao();
		valueToString += ", lgDanoDianteiraDireita: " +  getLgDanoDianteiraDireita();
		valueToString += ", lgDanoDianteiraEsquerda: " +  getLgDanoDianteiraEsquerda();
		valueToString += ", lgDanoLateralDireita: " +  getLgDanoLateralDireita();
		valueToString += ", lgDanoLateralEsquerda: " +  getLgDanoLateralEsquerda();
		valueToString += ", lgDanoTraseiraDireita: " +  getLgDanoTraseiraDireita();
		valueToString += ", lgDanoTraseiraEsquerda: " +  getLgDanoTraseiraEsquerda();
		valueToString += ", nrRgCondutor: " +  getNrRgCondutor();
		valueToString += ", nmOrgaoRgCondutor: " +  getNmOrgaoRgCondutor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatVeiculo(getCdBoatVeiculo(),
			getCdBoat(),
			getNmProprietario(),
			getTpDocumento(),
			getNrDocumento(),
			getNmCondutor(),
			getNrCnhCondutor(),
			getUfCnhCondutor(),
			getNrPlaca(),
			getTxtDeclaracaoCondutor(),
			getCdEspecie(),
			getCdMarca(),
			getCdCidade(),
			getCdCor(),
			getCdTipo(),
			getDsEndereco(),
			getNrTelefone(),
			getTpCategoriaCnh(),
			getTpSexo(),
			getNrIdade(),
			getCdCategoria(),
			getNrDocumentoCondutor(),
			getDtVencimentoCnh()==null ? null : (GregorianCalendar)getDtVencimentoCnh().clone(),
			getDtPrimeiraHabilitacao()==null ? null : (GregorianCalendar)getDtPrimeiraHabilitacao().clone(),
			getCdVeiculo(),
			getLgCondutorEvadiu(),
			getNrTelefoneProprietario(),
			getNrChassi(),
			getLgBuscaDetran(),
			getNrRenavam(),
			getNrAnoFabricacao(),
			getLgDanoDianteiraDireita(),
			getLgDanoDianteiraEsquerda(),
			getLgDanoLateralDireita(),
			getLgDanoLateralEsquerda(),
			getLgDanoTraseiraDireita(),
			getLgDanoTraseiraEsquerda(),
			getNrRgCondutor(),
			getNmOrgaoRgCondutor());
	}

}