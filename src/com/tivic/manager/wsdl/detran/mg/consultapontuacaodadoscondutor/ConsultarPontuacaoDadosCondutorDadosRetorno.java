package com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultarPontuacaoDadosCondutorDadosRetorno extends DadosRetornoMG {

	private int codigoRetorno;
	private String mensagemRetorno;
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
	
	public int getCodigoRetorno() {
		return codigoRetorno;
	}
	public void setCodigoRetorno(int codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}

	public String getMensagemRetorno() {
		return mensagemRetorno;
	}
	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}

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

	@Override
	public void importData(NodeList nodeList, String xml) {
		setXml(xml);
		for( int i = 0; i  < nodeList.getLength(); i++){
	       	 Node node = nodeList.item(i);
	       	 String value = node.getTextContent();
	       	 try {
	       		switch(node.getNodeName()){
		       	 	case "codigo_retorno":
		       	 		setCodigoRetorno(Integer.parseInt(value));
		       	 		break;
		       	 	case "mensagem_retorno":
		       	 		setMensagemRetorno(value);
		       	 		break;
		       	 	case "cpf_condutor":
		       	 		setCpfCondutor(value);
		       	 		break;
		       	 	case "renach_condutor":
		       	 		setRenachCondutor(value);
		       	 		break;
		       	 	case "pgu_condutor":
		       	 		setPguCondutor(value);
		       	 		break;
		       	 	case "nome_condutor":
		       	 		setNomeCondutor(value);
		       	 		break;
		       	 	case "pontos_condutor":
		       	 		setPontosCondutor(Integer.parseInt(value));
		       	 		break;
		       	 	case "uf_cnh":
		       	 		setUfCnh(value);
		       	 		break;
		       	 	case "categoria_cnh":
		       	 		setCategoriaCnh(value);
		       	 		break;
		       	 	case "data_emissao_cnh":
		       	 		setDataEmissaoCnh(value);
		       	 		break;
		       	 	case "nome_logradouro":
		       	 		setEnderecoCondutor(value);
		       	 		break;
		       	 	case "numero_logradouro":
		       	 		setNumeroLogradouro(value);
		       	 		break;
		       	 	case "complemento_logradouro":
		       	 		setComplementoEndereco(value);
		       	 		break;
		       	 	case "bairro":
		       	 		setBairro(value);
		       	 		break;
		       	 	case "codigo_municipio":
		       	 		setCodigoMunicipio(value);
		       	 		break;
		       	 	case "descricao_municipio":
		       	 		setNomeMunicipio(value);
		       	 		break;
		       	 	case "uf_municipio":
		       	 		setUfMunicipio(value);
		       	 		break;
		       	 	case "cep":
		       	 		setCepEnderecoCondutor(Integer.valueOf(value));
		       	 		break;
	       		}
	       	 } catch (NumberFormatException nfe) {
	       		 System.out.println(nfe.getMessage());
	       	 }
		}
	}
	
}
