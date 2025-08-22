package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PontuacaoDadosCondutorDTO {
	
	private String cpfCondutor;
	private String renachCondutor;
	private String pguCondutor;
	private String nomeCondutor;
	private int pontosCondutor;
	private String ufCnh;
	private String categoriaCnh;
	private String dataEmissaoCnh;
	private String enderecoCondutor;
	private String numeroLogradouro;
	private String complementoEndereco;
	private String bairro;
	private String codigoMunicipio;
	private String nomeMunicipio;
	private String ufMunicipio;
	private int cepEnderecoCondutor;
	
	public String getCpfCondutor() {
		return cpfCondutor;
	}
	public void setCpfCondutor(String cpfCondutor) {
		this.cpfCondutor = cpfCondutor;
	}
	public String getRenachCondutor() {
		return renachCondutor;
	}
	public void setRenachCondutor(String renachCondutor) {
		this.renachCondutor = renachCondutor;
	}
	public String getPguCondutor() {
		return pguCondutor;
	}
	public void setPguCondutor(String pguCondutor) {
		this.pguCondutor = pguCondutor;
	}
	public String getNomeCondutor() {
		return nomeCondutor;
	}
	public void setNomeCondutor(String nomeCondutor) {
		this.nomeCondutor = nomeCondutor;
	}
	public int getPontosCondutor() {
		return pontosCondutor;
	}
	public void setPontosCondutor(int pontosCondutor) {
		this.pontosCondutor = pontosCondutor;
	}
	public String getUfCnh() {
		return ufCnh;
	}
	public void setUfCnh(String ufCnh) {
		this.ufCnh = ufCnh;
	}
	public String getCategoriaCnh() {
		return categoriaCnh;
	}
	public void setCategoriaCnh(String categoriaCnh) {
		this.categoriaCnh = categoriaCnh;
	}
	public String getDataEmissaoCnh() {
		return dataEmissaoCnh;
	}
	public void setDataEmissaoCnh(String dataEmissaoCnh) {
		this.dataEmissaoCnh = dataEmissaoCnh;
	}
	public String getEnderecoCondutor() {
		return enderecoCondutor;
	}
	public void setEnderecoCondutor(String enderecoCondutor) {
		this.enderecoCondutor = enderecoCondutor;
	}
	public String getNumeroLogradouro() {
		return numeroLogradouro;
	}
	public void setNumeroLogradouro(String numeroLogradouro) {
		this.numeroLogradouro = numeroLogradouro;
	}
	public String getComplementoEndereco() {
		return complementoEndereco;
	}
	public void setComplementoEndereco(String complementoEndereco) {
		this.complementoEndereco = complementoEndereco;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}
	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}
	public String getNomeMunicipio() {
		return nomeMunicipio;
	}
	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}
	public String getUfMunicipio() {
		return ufMunicipio;
	}
	public void setUfMunicipio(String ufMunicipio) {
		this.ufMunicipio = ufMunicipio;
	}
	public int getCepEnderecoCondutor() {
		return cepEnderecoCondutor;
	}
	public void setCepEnderecoCondutor(int cepEnderecoCondutor) {
		this.cepEnderecoCondutor = cepEnderecoCondutor;
	}
	
	public String toString() {
		try {
			JSONObject jsonDadosCondutorDTO = new JSONObject();
			jsonDadosCondutorDTO.put("cpfCondutor", getCpfCondutor());
			jsonDadosCondutorDTO.put("renachCondutor", getRenachCondutor());
			jsonDadosCondutorDTO.put("pguCondutor", getPguCondutor());
			jsonDadosCondutorDTO.put("nomeCondutor", getNomeCondutor());
			jsonDadosCondutorDTO.put("pontosCondutor", getPontosCondutor());
			jsonDadosCondutorDTO.put("ufCnh", getUfCnh());
			jsonDadosCondutorDTO.put("categoriaCnh", getCategoriaCnh());
			jsonDadosCondutorDTO.put("dataEmissaoCnh", getDataEmissaoCnh());
			jsonDadosCondutorDTO.put("enderecoCondutor", getEnderecoCondutor());
			jsonDadosCondutorDTO.put("numeroLogradouro", getNumeroLogradouro());
			jsonDadosCondutorDTO.put("complementoEndereco", getComplementoEndereco());
			jsonDadosCondutorDTO.put("bairro", getBairro());
			jsonDadosCondutorDTO.put("codigoMunicipio", getCodigoMunicipio());
			jsonDadosCondutorDTO.put("nomeMunicipio", getNomeMunicipio());
			jsonDadosCondutorDTO.put("ufMunicipio", getUfMunicipio());
			jsonDadosCondutorDTO.put("cepEnderecoCondutor", getCepEnderecoCondutor());
			return jsonDadosCondutorDTO.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "cpfCondutor: " + getCpfCondutor();
		}
	}
	
}
