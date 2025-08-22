package com.tivic.manager.wsdl.detran.mg.consultarplaca;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultarPlacaDadosRetorno extends DadosRetornoMG {

	private int orgao;
	private String placa;
	private String nome;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String codigoMunicipio;
	private String ufMunicipio;
	private String cnpjProprietario;
	private String cpfProprietario;
	private String nomeArrendatario;
	private String cnpjArrendatario;
	private String cpfArrendatario;
	private String nomeAdquirente;
	private String logradouroAdquirente;
	private String numeroImovelAdquirente;
	private String complementoImovelAdquirente;
	private String bairroImovelAdquirente;
	private String cepImovelAdquirente;
	private String codigoMunicipioAdquirente;
	private String ufMunicipioAdquirente;
	private String cnpjAdquirente;
	private String cpfAdquirente;
	private String dataRecibo;
	private String dataImpedimentoNaoTransferencia;
	private int codigoTipo;
	private int codigoCor;
	private int codigoEspecie;
	private int codigoCategoria;
	private int codigoMarcaModelo;
	private String codigoRenavam;
	private String placaAntiga;
	
	
	public ConsultarPlacaDadosRetorno() {
		// TODO Auto-generated constructor stub
	}


	public ConsultarPlacaDadosRetorno(int codigoRetorno, String mensagemRetorno, int orgao, String placa, String nome,
			String logradouro, String numero, String complemento, String bairro, String cep, String codigoMunicipio,
			String ufMunicipio, String cnpjProprietario, String cpfProprietario, String nomeArrendatario,
			String cnpjArrendatario, String cpfArrendatario, String nomeAdquirente, String logradouroAdquirente,
			String numeroImovelAdquirente, String complementoImovelAdquirente, String bairroImovelAdquirente,
			String cepImovelAdquirente, String codigoMunicipioAdquirente, String ufMunicipioAdquirente,
			String cnpjAdquirente, String cpfAdquirente, String dataRecibo, String dataImpedimentoNaoTransferencia,
			int codigoTipo, int codigoCor, int codigoEspecie, int codigoCategoria, int codigoMarcaModelo,
			String codigoRenavam, String placaAntiga) {
		super(codigoRetorno, mensagemRetorno);
		this.orgao = orgao;
		this.placa = placa;
		this.nome = nome;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.codigoMunicipio = codigoMunicipio;
		this.ufMunicipio = ufMunicipio;
		this.cnpjProprietario = cnpjProprietario;
		this.cpfProprietario = cpfProprietario;
		this.nomeArrendatario = nomeArrendatario;
		this.cnpjArrendatario = cnpjArrendatario;
		this.cpfArrendatario = cpfArrendatario;
		this.nomeAdquirente = nomeAdquirente;
		this.logradouroAdquirente = logradouroAdquirente;
		this.numeroImovelAdquirente = numeroImovelAdquirente;
		this.complementoImovelAdquirente = complementoImovelAdquirente;
		this.bairroImovelAdquirente = bairroImovelAdquirente;
		this.cepImovelAdquirente = cepImovelAdquirente;
		this.codigoMunicipioAdquirente = codigoMunicipioAdquirente;
		this.ufMunicipioAdquirente = ufMunicipioAdquirente;
		this.cnpjAdquirente = cnpjAdquirente;
		this.cpfAdquirente = cpfAdquirente;
		this.dataRecibo = dataRecibo;
		this.dataImpedimentoNaoTransferencia = dataImpedimentoNaoTransferencia;
		this.codigoTipo = codigoTipo;
		this.codigoCor = codigoCor;
		this.codigoEspecie = codigoEspecie;
		this.codigoCategoria = codigoCategoria;
		this.codigoMarcaModelo = codigoMarcaModelo;
		this.codigoRenavam = codigoRenavam;
		this.placaAntiga = placaAntiga;
	}

	public int getOrgao() {
		return orgao;
	}


	public void setOrgao(int orgao) {
		this.orgao = orgao;
	}


	public String getPlaca() {
		return placa;
	}


	public void setPlaca(String placa) {
		this.placa = placa;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getLogradouro() {
		return logradouro;
	}


	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}


	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}


	public String getUfMunicipio() {
		return ufMunicipio;
	}


	public void setUfMunicipio(String ufMunicipio) {
		this.ufMunicipio = ufMunicipio;
	}


	public String getCnpjProprietario() {
		return cnpjProprietario;
	}


	public void setCnpjProprietario(String cnpjProprietario) {
		this.cnpjProprietario = cnpjProprietario;
	}


	public String getCpfProprietario() {
		return cpfProprietario;
	}


	public void setCpfProprietario(String cpfProprietario) {
		this.cpfProprietario = cpfProprietario;
	}


	public String getNomeArrendatario() {
		return nomeArrendatario;
	}


	public void setNomeArrendatario(String nomeArrendatario) {
		this.nomeArrendatario = nomeArrendatario;
	}


	public String getCnpjArrendatario() {
		return cnpjArrendatario;
	}


	public void setCnpjArrendatario(String cnpjArrendatario) {
		this.cnpjArrendatario = cnpjArrendatario;
	}


	public String getCpfArrendatario() {
		return cpfArrendatario;
	}


	public void setCpfArrendatario(String cpfArrendatario) {
		this.cpfArrendatario = cpfArrendatario;
	}


	public String getNomeAdquirente() {
		return nomeAdquirente;
	}


	public void setNomeAdquirente(String nomeAdquirente) {
		this.nomeAdquirente = nomeAdquirente;
	}


	public String getLogradouroAdquirente() {
		return logradouroAdquirente;
	}


	public void setLogradouroAdquirente(String logradouroAdquirente) {
		this.logradouroAdquirente = logradouroAdquirente;
	}


	public String getNumeroImovelAdquirente() {
		return numeroImovelAdquirente;
	}


	public void setNumeroImovelAdquirente(String numeroImovelAdquirente) {
		this.numeroImovelAdquirente = numeroImovelAdquirente;
	}


	public String getComplementoImovelAdquirente() {
		return complementoImovelAdquirente;
	}


	public void setComplementoImovelAdquirente(String complementoImovelAdquirente) {
		this.complementoImovelAdquirente = complementoImovelAdquirente;
	}


	public String getBairroImovelAdquirente() {
		return bairroImovelAdquirente;
	}


	public void setBairroImovelAdquirente(String bairroImovelAdquirente) {
		this.bairroImovelAdquirente = bairroImovelAdquirente;
	}


	public String getCepImovelAdquirente() {
		return cepImovelAdquirente;
	}


	public void setCepImovelAdquirente(String cepImovelAdquirente) {
		this.cepImovelAdquirente = cepImovelAdquirente;
	}


	public String getCodigoMunicipioAdquirente() {
		return codigoMunicipioAdquirente;
	}


	public void setCodigoMunicipioAdquirente(String codigoMunicipioAdquirente) {
		this.codigoMunicipioAdquirente = codigoMunicipioAdquirente;
	}


	public String getUfMunicipioAdquirente() {
		return ufMunicipioAdquirente;
	}


	public void setUfMunicipioAdquirente(String ufMunicipioAdquirente) {
		this.ufMunicipioAdquirente = ufMunicipioAdquirente;
	}


	public String getCnpjAdquirente() {
		return cnpjAdquirente;
	}


	public void setCnpjAdquirente(String cnpjAdquirente) {
		this.cnpjAdquirente = cnpjAdquirente;
	}


	public String getCpfAdquirente() {
		return cpfAdquirente;
	}


	public void setCpfAdquirente(String cpfAdquirente) {
		this.cpfAdquirente = cpfAdquirente;
	}


	public String getDataRecibo() {
		return dataRecibo;
	}


	public void setDataRecibo(String dataRecibo) {
		this.dataRecibo = dataRecibo;
	}


	public String getDataImpedimentoNaoTransferencia() {
		return dataImpedimentoNaoTransferencia;
	}


	public void setDataImpedimentoNaoTransferencia(String dataImpedimentoNaoTransferencia) {
		this.dataImpedimentoNaoTransferencia = dataImpedimentoNaoTransferencia;
	}


	public int getCodigoTipo() {
		return codigoTipo;
	}


	public void setCodigoTipo(int codigoTipo) {
		this.codigoTipo = codigoTipo;
	}


	public int getCodigoCor() {
		return codigoCor;
	}


	public void setCodigoCor(int codigoCor) {
		this.codigoCor = codigoCor;
	}


	public int getCodigoEspecie() {
		return codigoEspecie;
	}


	public void setCodigoEspecie(int codigoEspecie) {
		this.codigoEspecie = codigoEspecie;
	}


	public int getCodigoCategoria() {
		return codigoCategoria;
	}


	public void setCodigoCategoria(int codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}


	public int getCodigoMarcaModelo() {
		return codigoMarcaModelo;
	}


	public void setCodigoMarcaModelo(int codigoMarcaModelo) {
		this.codigoMarcaModelo = codigoMarcaModelo;
	}


	public String getCodigoRenavam() {
		return codigoRenavam;
	}


	public void setCodigoRenavam(String codigoRenavam) {
		this.codigoRenavam = codigoRenavam;
	}


	public String getPlacaAntiga() {
		return placaAntiga;
	}


	public void setPlacaAntiga(String placaAntiga) {
		this.placaAntiga = placaAntiga;
	}


	@Override
	public String toString() {
		return "ConsultarPlacaDadosRetorno [codigoRetorno=" + getCodigoRetorno() + ", mensagemRetorno=" + getMensagemRetorno()
				+ ", orgao=" + orgao + ", placa=" + placa + ", nome=" + nome + ", logradouro=" + logradouro
				+ ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro + ", cep=" + cep
				+ ", codigoMunicipio=" + codigoMunicipio + ", ufMunicipio=" + ufMunicipio + ", cnpjProprietario="
				+ cnpjProprietario + ", cpfProprietario=" + cpfProprietario + ", nomeArrendatario=" + nomeArrendatario
				+ ", cnpjArrendatario=" + cnpjArrendatario + ", cpfArrendatario=" + cpfArrendatario + ", nomeAdquirente="
				+ nomeAdquirente + ", logradouroAdquirente=" + logradouroAdquirente + ", numeroImovelAdquirente="
				+ numeroImovelAdquirente + ", complementoImovelAdquirente=" + complementoImovelAdquirente
				+ ", bairroImovelAdquirente=" + bairroImovelAdquirente + ", cepImovelAdquirente=" + cepImovelAdquirente
				+ ", codigoMunicipioAdquirente=" + codigoMunicipioAdquirente + ", ufMunicipioAdquirente="
				+ ufMunicipioAdquirente + ", cnpjAdquirente=" + cnpjAdquirente + ", cpfAdquirente=" + cpfAdquirente
				+ ", dataRecibo=" + dataRecibo + ", dataImpedimentoNaoTransferencia=" + dataImpedimentoNaoTransferencia
				+ ", codigoTipo=" + codigoTipo + ", codigoCor=" + codigoCor + ", codigoEspecie=" + codigoEspecie
				+ ", codigoCategoria=" + codigoCategoria + ", codigoMarcaModelo=" + codigoMarcaModelo
				+ ", codigoRenavam=" + codigoRenavam + ", placaAntiga=" + placaAntiga + "]";
	}


	@Override
	public void importData(NodeList nodeList, String xml) {
		
		setXml(xml);
		
		for( int i = 0; i  < nodeList.getLength(); i++){
	       	 Node node = nodeList.item(i);
	       	 
	       	 switch(node.getNodeName()){
	       	 	case "codigo_retorno":
	       	 		setCodigoRetorno(Integer.parseInt(node.getTextContent()));
	       	 	break;
	       	 	
	       	 	case "mensagem_retorno":
	       	 		setMensagemRetorno(node.getTextContent());
	       	 	break;

	       	 	case "orgao":
	       	 		setOrgao(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "placa":
	       	 		setPlaca(node.getTextContent());
	       	 	break;

	       	 	case "nome":
	       	 		setNome(node.getTextContent());
	       	 	break;

	       	 	case "logradouro":
	       	 		setLogradouro(node.getTextContent());
	       	 	break;

	       	 	case "numero":
	       	 		setNumero(node.getTextContent());
	       	 	break;

	       	 	case "complemento":
	       	 		setComplemento(node.getTextContent());
	       	 	break;

	       	 	case "bairro":
	       	 		setBairro(node.getTextContent());
	       	 	break;

	       	 	case "cep":
	       	 		setCep(node.getTextContent());
	       	 	break;

	       	 	case "codigo_municipio":
	       	 		setCodigoMunicipio(node.getTextContent());
	       	 	break;

	       	 	case "cnpj_proprietario":
	       	 		setCnpjProprietario(node.getTextContent());
	       	 	break;

	       	 	case "cpf_proprietario":
	       	 		setCpfProprietario(node.getTextContent());
	       	 	break;

	       	 	case "nome_arrendatario":
	       	 		setNomeArrendatario(node.getTextContent());
	       	 	break;

	       	 	case "cnpj_arrendatario":
	       	 		setCnpjArrendatario(node.getTextContent());
	       	 	break;

	       	 	case "cpf_arrendatario":
	       	 		setCpfArrendatario(node.getTextContent());
	       	 	break;

	       	 	case "nome_adquirente":
	       	 		setNomeAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "logradouro_adquirente":
	       	 		setLogradouroAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "complemento_imovel_adquirente":
	       	 		setComplementoImovelAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "bairro_imovel_adquirente":
	       	 		setBairroImovelAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "cep_imovel_adquirente":
	       	 		setCepImovelAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "codigo_municipio_adquirente":
	       	 		setCodigoMunicipioAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "uf_municipio_adquirente":
	       	 		setUfMunicipioAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "uf_municipio":
	       	 		setUfMunicipio(node.getTextContent());
	       	 		break;

	       	 	case "cnpj_adquirente":
	       	 		setCnpjAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "cpf_adquirente":
	       	 		setCpfAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "data_recibo":
	       	 		setDataRecibo(node.getTextContent());
	       	 	break;

	       	 	case "data_impedimento_nao_transferencia":
	       	 		setDataImpedimentoNaoTransferencia(node.getTextContent());
	       	 	break;

	       	 	case "codigo_tipo":
	       	 		if(node.getTextContent().matches("-?(0|[1-9]\\d*)")) setCodigoTipo(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "codigo_cor":
	       	 		if(node.getTextContent().matches("-?(0|[1-9]\\d*)")) setCodigoCor(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "codigo_especie":
	       	 		if(node.getTextContent().matches("-?(0|[1-9]\\d*)")) setCodigoEspecie(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "codigo_categoria":
	       	 		if(node.getTextContent().matches("-?(0|[1-9]\\d*)")) setCodigoCategoria(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "codigo_marca_modelo":
	       	 		if(node.getTextContent().matches("-?([0-9]\\d*)")) setCodigoMarcaModelo(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "codigo_renavam":
	       	 		setCodigoRenavam(node.getTextContent());
	       	 	break;

	       	 	case "placa_antiga":
	       	 		setPlacaAntiga(node.getTextContent());
	       	 	break;
	       	 	
	       	 }
	   }
	}

}
