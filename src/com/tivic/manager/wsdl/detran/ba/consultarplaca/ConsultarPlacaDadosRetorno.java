package com.tivic.manager.wsdl.detran.ba.consultarplaca;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.CategoriaVeiculoDAO;
import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.CorDAO;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoDAO;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoDAO;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;

public class ConsultarPlacaDadosRetorno extends DadosRetornoBA {

	private int codigoUsuario;
	private String senhaUsuario;
	private String codigoOperacao;
	private String paramPlaca;
	private String paramUF;
	private String paramChassi;
	private String paramRenavam;
	private int codigoRetExec;
	private String placaVeiculo;
	private String codSituacaoVeic;
	private String situacaoVeiculo;
	private String novaPlaca;
	private int codigoMunicipio;
	private String nomeMunicipio;
	private String unidadeFederacao;
	private String nomeUf;
	private String indRouboFurto;
	private int codigoMarcaMod;
	private String marcaModelo;
	private String motorDifAcesso;
	private String comunicacaoVenda;
	private int codigoCarroceria;
	private String carroceria;
	private int codigoCor;
	private String cor;
	private int codigoCategoria;
	private String categoriaVeiculo;
	private int codigoEspecie;
	private String especie;
	private int codigoTipVeic;
	private String tipoVeiculo;
	private int anoFabricacao;
	private int anoModelo;
	private int potencia;
	private int cilindrada;
	private int codigoCombustive;
	private String combustivel;
	private String numeroMotor;
	private double tracaoMax;
	private double pesoBrutoTotal;
	private int anoLicenciamento;
	private double capacidadeCarga;
	private String procedencia;
	private int capacidadePassag;
	private int numeroEixos;
	private String restricao1;
	private String restricao2;
	private String restricao3;
	private String restricao4;
	private String restricao5;
	private String restricao6;
	private int tipoDocumento;
	private String numeroCpfCgc;
	private String nomeProprietario;
	private String endereco;
	private String numeroEndereco;
	private String complemEndereco;
	private String nomeBairro;
	private String numeroCep;
	private String numeroTelefone;
	private String regravChassi;
	private GregorianCalendar dataAtualizacao;
	private double valorDebIpva;
	private double valorDebLicenc;
	private double valorDebMulta;
	private double valorDebDpvat;
	private double valorDebInfTrami;
	
	public ConsultarPlacaDadosRetorno() {
		// TODO Auto-generated constructor stub
	}
	
	public int getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(int codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getSenhaUsuario() {
		return senhaUsuario;
	}

	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}

	public String getCodigoOperacao() {
		return codigoOperacao;
	}

	public void setCodigoOperacao(String codigoOperacao) {
		this.codigoOperacao = codigoOperacao;
	}

	public String getParamPlaca() {
		return paramPlaca;
	}

	public void setParamPlaca(String paramPlaca) {
		this.paramPlaca = paramPlaca;
	}

	public String getParamUF() {
		return paramUF;
	}

	public void setParamUF(String paramUF) {
		this.paramUF = paramUF;
	}

	public String getParamChassi() {
		return paramChassi;
	}

	public void setParamChassi(String paramChassi) {
		this.paramChassi = paramChassi;
	}

	public String getParamRenavam() {
		return paramRenavam;
	}

	public void setParamRenavam(String paramRenavam) {
		this.paramRenavam = paramRenavam;
	}

	public int getCodigoRetExec() {
		return codigoRetExec;
	}

	public void setCodigoRetExec(int codigoRetExec) {
		this.codigoRetExec = codigoRetExec;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}

	public String getCodSituacaoVeic() {
		return codSituacaoVeic;
	}

	public void setCodSituacaoVeic(String codSituacaoVeic) {
		this.codSituacaoVeic = codSituacaoVeic;
	}

	public String getSituacaoVeiculo() {
		return situacaoVeiculo;
	}

	public void setSituacaoVeiculo(String situacaoVeiculo) {
		this.situacaoVeiculo = situacaoVeiculo;
	}

	public String getNovaPlaca() {
		return novaPlaca;
	}

	public void setNovaPlaca(String novaPlaca) {
		this.novaPlaca = novaPlaca;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		if(!codigoMunicipio.matches("\\d+"))
			return;
		
		setCodigoMunicipio(Integer.parseInt(codigoMunicipio));
		
		Cidade cidade = CidadeServices.getByIdCidade(codigoMunicipio);
		if(cidade != null)
			setNomeMunicipio(cidade.getNmCidade());
	}
	
	public int getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(int codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public String getNomeMunicipio() {
		return nomeMunicipio;
	}

	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}

	public String getUnidadeFederacao() {
		return unidadeFederacao;
	}

	public void setUnidadeFederacao(String unidadeFederacao) {
		this.unidadeFederacao = unidadeFederacao;
	}

	public String getNomeUf() {
		return nomeUf;
	}

	public void setNomeUf(String nomeUf) {
		this.nomeUf = nomeUf;
	}

	public String getIndRouboFurto() {
		return indRouboFurto;
	}

	public void setIndRouboFurto(String indRouboFurto) {
		this.indRouboFurto = indRouboFurto;
	}

	public void setCodigoMarcaModelo(String codigoMarcaModelo){
		if(!codigoMarcaModelo.matches("\\d+"))
			return;
		
		setCodigoMarcaMod(Integer.parseInt(codigoMarcaModelo));
		
		MarcaModelo marcaModelo = MarcaModeloDAO.get(Integer.parseInt(codigoMarcaModelo));
		if(marcaModelo != null)
			setMarcaModelo(String.valueOf(marcaModelo.getNmMarca() + " " + marcaModelo.getNmModelo()).trim());
	}
	
	public int getCodigoMarcaMod() {
		return codigoMarcaMod;
	}

	public void setCodigoMarcaMod(int codigoMarcaMod) {
		this.codigoMarcaMod = codigoMarcaMod;
	}

	public String getMarcaModelo() {
		return marcaModelo;
	}

	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}

	public String getMotorDifAcesso() {
		return motorDifAcesso;
	}

	public void setMotorDifAcesso(String motorDifAcesso) {
		this.motorDifAcesso = motorDifAcesso;
	}

	public String getComunicacaoVenda() {
		return comunicacaoVenda;
	}

	public void setComunicacaoVenda(String comunicacaoVenda) {
		this.comunicacaoVenda = comunicacaoVenda;
	}

	public int getCodigoCarroceria() {
		return codigoCarroceria;
	}

	public void setCodigoCarroceria(int codigoCarroceria) {
		this.codigoCarroceria = codigoCarroceria;
	}

	public String getCarroceria() {
		return carroceria;
	}

	public void setCarroceria(String carroceria) {
		this.carroceria = carroceria;
	}

	public void setCodigoCor(String codigoCor){
		if(!codigoCor.matches("\\d+"))
			return;
		
		setCodigoCor(Integer.parseInt(codigoCor));
		
		Cor cor = CorDAO.get(Integer.parseInt(codigoCor));
		if(cor != null)
			setCor(cor.getNmCor());
	}
	
	public int getCodigoCor() {
		return codigoCor;
	}

	public void setCodigoCor(int codigoCor) {
		this.codigoCor = codigoCor;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public void setCodigoCategoria(String codigoCategoria){
		if(!codigoCategoria.matches("\\d+"))
			return;
		
		setCodigoCategoria(Integer.parseInt(codigoCategoria));
		
		
		CategoriaVeiculo categoriaVeiculo = CategoriaVeiculoDAO.get(Integer.parseInt(codigoCategoria));
		if(categoriaVeiculo != null)
			setCategoriaVeiculo(categoriaVeiculo.getNmCategoria());
	}
	
	public int getCodigoCategoria() {
		return codigoCategoria;
	}

	public void setCodigoCategoria(int codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}

	public String getCategoriaVeiculo() {
		return categoriaVeiculo;
	}

	public void setCategoriaVeiculo(String categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
	}

	public void setCodigoEspecie(String codigoEspecie){
		if(!codigoEspecie.matches("\\d+"))
			return;
		
		setCodigoEspecie(Integer.parseInt(codigoEspecie));
		
		EspecieVeiculo especieVeiculo = EspecieVeiculoDAO.get(Integer.parseInt(codigoEspecie));
		if(especieVeiculo != null)
			setEspecie(especieVeiculo.getDsEspecie());
	}
	
	public int getCodigoEspecie() {
		return codigoEspecie;
	}

	public void setCodigoEspecie(int codigoEspecie) {
		this.codigoEspecie = codigoEspecie;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public int getCodigoTipVeic() {
		return codigoTipVeic;
	}

	public void setCodigoTipo(String codigoTipo){
		if(!codigoTipo.matches("\\d+"))
			return;
		
		setCodigoTipVeic(Integer.parseInt(codigoTipo));
		
		TipoVeiculo tipoVeiculo = TipoVeiculoDAO.get(Integer.parseInt(codigoTipo));
		if(tipoVeiculo != null)
			setTipoVeiculo(tipoVeiculo.getNmTipoVeiculo());
	}
	
	public void setCodigoTipVeic(int codigoTipVeic) {
		this.codigoTipVeic = codigoTipVeic;
	}

	public String getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(String tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public int getAnoFabricacao() {
		return anoFabricacao;
	}

	public void setAnoFabricacao(int anoFabricacao) {
		this.anoFabricacao = anoFabricacao;
	}

	public int getAnoModelo() {
		return anoModelo;
	}

	public void setAnoModelo(int anoModelo) {
		this.anoModelo = anoModelo;
	}

	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public int getCilindrada() {
		return cilindrada;
	}

	public void setCilindrada(int cilindrada) {
		this.cilindrada = cilindrada;
	}

	public int getCodigoCombustive() {
		return codigoCombustive;
	}

	public void setCodigoCombustive(int codigoCombustive) {
		this.codigoCombustive = codigoCombustive;
	}

	public String getCombustivel() {
		return combustivel;
	}

	public void setCombustivel(String combustivel) {
		this.combustivel = combustivel;
	}

	public String getNumeroMotor() {
		return numeroMotor;
	}

	public void setNumeroMotor(String numeroMotor) {
		this.numeroMotor = numeroMotor;
	}

	public double getTracaoMax() {
		return tracaoMax;
	}

	public void setTracaoMax(double tracaoMax) {
		this.tracaoMax = tracaoMax;
	}

	public double getPesoBrutoTotal() {
		return pesoBrutoTotal;
	}

	public void setPesoBrutoTotal(double pesoBrutoTotal) {
		this.pesoBrutoTotal = pesoBrutoTotal;
	}

	public int getAnoLicenciamento() {
		return anoLicenciamento;
	}

	public void setAnoLicenciamento(int anoLicenciamento) {
		this.anoLicenciamento = anoLicenciamento;
	}

	public double getCapacidadeCarga() {
		return capacidadeCarga;
	}

	public void setCapacidadeCarga(double capacidadeCarga) {
		this.capacidadeCarga = capacidadeCarga;
	}

	public String getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia = procedencia;
	}

	public int getCapacidadePassag() {
		return capacidadePassag;
	}

	public void setCapacidadePassag(int capacidadePassag) {
		this.capacidadePassag = capacidadePassag;
	}

	public int getNumeroEixos() {
		return numeroEixos;
	}

	public void setNumeroEixos(int numeroEixos) {
		this.numeroEixos = numeroEixos;
	}

	public String getRestricao1() {
		return restricao1;
	}

	public void setRestricao1(String restricao1) {
		this.restricao1 = restricao1;
	}

	public String getRestricao2() {
		return restricao2;
	}

	public void setRestricao2(String restricao2) {
		this.restricao2 = restricao2;
	}

	public String getRestricao3() {
		return restricao3;
	}

	public void setRestricao3(String restricao3) {
		this.restricao3 = restricao3;
	}

	public String getRestricao4() {
		return restricao4;
	}

	public void setRestricao4(String restricao4) {
		this.restricao4 = restricao4;
	}

	public String getRestricao5() {
		return restricao5;
	}

	public void setRestricao5(String restricao5) {
		this.restricao5 = restricao5;
	}

	public String getRestricao6() {
		return restricao6;
	}

	public void setRestricao6(String restricao6) {
		this.restricao6 = restricao6;
	}

	public int getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(int tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumeroCpfCgc() {
		return numeroCpfCgc;
	}

	public void setNumeroCpfCgc(String numeroCpfCgc) {
		this.numeroCpfCgc = numeroCpfCgc;
	}

	public String getNomeProprietario() {
		return nomeProprietario;
	}

	public void setNomeProprietario(String nomeProprietario) {
		this.nomeProprietario = nomeProprietario;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumeroEndereco() {
		return numeroEndereco;
	}

	public void setNumeroEndereco(String numeroEndereco) {
		this.numeroEndereco = numeroEndereco;
	}

	public String getComplemEndereco() {
		return complemEndereco;
	}

	public void setComplemEndereco(String complemEndereco) {
		this.complemEndereco = complemEndereco;
	}

	public String getNomeBairro() {
		return nomeBairro;
	}

	public void setNomeBairro(String nomeBairro) {
		this.nomeBairro = nomeBairro;
	}

	public String getNumeroCep() {
		return numeroCep;
	}

	public void setNumeroCep(String numeroCep) {
		this.numeroCep = numeroCep;
	}

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public String getRegravChassi() {
		return regravChassi;
	}

	public void setRegravChassi(String regravChassi) {
		this.regravChassi = regravChassi;
	}

	public GregorianCalendar getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(GregorianCalendar dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public double getValorDebIpva() {
		return valorDebIpva;
	}

	public void setValorDebIpva(double valorDebIpva) {
		this.valorDebIpva = valorDebIpva;
	}

	public double getValorDebLicenc() {
		return valorDebLicenc;
	}

	public void setValorDebLicenc(double valorDebLicenc) {
		this.valorDebLicenc = valorDebLicenc;
	}

	public double getValorDebMulta() {
		return valorDebMulta;
	}

	public void setValorDebMulta(double valorDebMulta) {
		this.valorDebMulta = valorDebMulta;
	}

	public double getValorDebDpvat() {
		return valorDebDpvat;
	}

	public void setValorDebDpvat(double valorDebDpvat) {
		this.valorDebDpvat = valorDebDpvat;
	}

	public double getValorDebInfTrami() {
		return valorDebInfTrami;
	}

	public void setValorDebInfTrami(double valorDebInfTrami) {
		this.valorDebInfTrami = valorDebInfTrami;
	}
	
	@Override
	public String toString() {
		return "{\"codigoUsuario\":" + codigoUsuario + ", \"senhaUsuario\":\"" + senhaUsuario
				+ "\", \"codigoOperacao\":\"" + codigoOperacao + "\", \"paramPlaca\":\"" + paramPlaca + "\", \"paramUF\":\"" + paramUF
				+ "\", \"paramChassi\":\"" + paramChassi + "\", \"paramRenavam\":\"" + paramRenavam + "\", \"codigoRetExec\":" + codigoRetExec
				+ ", \"placaVeiculo\":\"" + placaVeiculo + "\", \"codSituacaoVeic\":\"" + codSituacaoVeic + "\", \"situacaoVeiculo\":\""
				+ situacaoVeiculo + "\", \"novaPlaca\":\"" + novaPlaca + "\", \"codigoMunicipio\":" + codigoMunicipio
				+ ", \"nomeMunicipio\":\"" + nomeMunicipio + "\", \"unidadeFederacao\":\"" + unidadeFederacao + "\", \"nomeUf\":\"" + nomeUf
				+ "\", \"indRouboFurto\":\"" + indRouboFurto + "\", \"codigoMarcaMod\":" + codigoMarcaMod + ", \"marcaModelo\":\""
				+ marcaModelo + "\", \"motorDifAcesso\":\"" + motorDifAcesso + "\", \"comunicacaoVenda\":\"" + comunicacaoVenda
				+ "\", \"codigoCarroceria\":" + codigoCarroceria + ", \"carroceria\":\"" + carroceria + "\", \"codigoCor\":" + codigoCor
				+ ", \"cor\":\"" + cor + "\", \"codigoCategoria\":" + codigoCategoria + ", \"categoriaVeiculo\":\"" + categoriaVeiculo
				+ "\", \"codigoEspecie\":" + codigoEspecie + ", \"especie\":\"" + especie + "\", \"codigoTipVeic\":" + codigoTipVeic
				+ ", \"tipoVeiculo\":\"" + tipoVeiculo + "\", \"anoFabricacao\":" + anoFabricacao + ", \"anoModelo\":" + anoModelo
				+ ", \"potencia\":" + potencia + ", \"cilindrada\":" + cilindrada + ", \"codigoCombustive\":" + codigoCombustive
				+ ", \"combustivel\":\"" + combustivel + "\", \"numeroMotor\":\"" + numeroMotor + "\", \"tracaoMax\":" + tracaoMax
				+ ", \"pesoBrutoTotal\":" + pesoBrutoTotal + ", \"anoLicenciamento\":" + anoLicenciamento + ", \"capacidadeCarga\":"
				+ capacidadeCarga + ", \"procedencia\":\"" + procedencia + "\", \"capacidadePassag\":" + capacidadePassag
				+ ", \"numeroEixos\":" + numeroEixos + ", \"restricao1\":\"" + restricao1 + "\", \"restricao2\":\"" + restricao2
				+ "\", \"restricao3\":\"" + restricao3 + "\", \"restricao4\":\"" + restricao4 + "\", \"restricao5\":\"" + restricao5
				+ "\", \"restricao6\":\"" + restricao6 + "\", \"tipoDocumento\":" + tipoDocumento + ", \"numeroCpfCgc\":\"" + numeroCpfCgc
				+ "\", \"nomeProprietario\":\"" + nomeProprietario + "\", \"endereco\":\"" + endereco + "\", \"numeroEndereco\":\""
				+ numeroEndereco + "\", \"complemEndereco\":\"" + complemEndereco + "\", \"nomeBairro\":\"" + nomeBairro
				+ "\", \"numeroCep\":\"" + numeroCep + "\", \"numeroTelefone\":\"" + numeroTelefone + "\", \"regravChassi\":\"" + regravChassi
				+ "\", \"dataAtualizacao\":\"" + dataAtualizacao + "\", \"valorDebIpva\":\"" + valorDebIpva + "\", \"valorDebLicenc\":"
				+ valorDebLicenc + ", \"valorDebMulta\":" + valorDebMulta + ", \"valorDebDpvat\":" + valorDebDpvat
				+ ", \"valorDebInfTrami\":" + valorDebInfTrami + "}";
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
	       	 		//setOrgao(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "placa":
	       	 		setParamPlaca(node.getTextContent());
	       	 	break;

	       	 	case "nome":
	       	 		setNomeProprietario(node.getTextContent());
	       	 	break;

	       	 	case "logradouro":
	       	 		setEndereco(node.getTextContent());
	       	 	break;

	       	 	case "numero":
	       	 		setNumeroEndereco(node.getTextContent());
	       	 	break;

	       	 	case "complemento":
	       	 		setComplemEndereco(node.getTextContent());
	       	 	break;

	       	 	case "bairro":
	       	 		setNomeBairro(node.getTextContent());
	       	 	break;

	       	 	case "cep":
	       	 		setNumeroCep(node.getTextContent());
	       	 	break;

	       	 	case "codigo_municipio":
	       	 		setCodigoMunicipio(node.getTextContent());
	       	 	break;

	       	 	case "cnpj_proprietario":
	       	 		//setCnpjProprietario(node.getTextContent());
	       	 	break;

	       	 	case "cpf_proprietario":
	       	 		setNumeroCpfCgc(node.getTextContent());
	       	 	break;

	       	 	case "nome_arrendatario":
	       	 		//setNomeArrendatario(node.getTextContent());
	       	 	break;

	       	 	case "cnpj_arrendatario":
	       	 		//setCnpjArrendatario(node.getTextContent());
	       	 	break;

	       	 	case "cpf_arrendatario":
	       	 		//setCpfArrendatario(node.getTextContent());
	       	 	break;

	       	 	case "nome_adquirente":
	       	 		//setNomeAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "logradouro_adquirente":
	       	 		//setLogradouroAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "complemento_imovel_adquirente":
	       	 		//setComplementoImovelAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "bairro_imovel_adquirente":
	       	 		//setBairroImovelAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "cep_imovel_adquirente":
	       	 		//setCepImovelAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "codigo_municipio_adquirente":
	       	 		//setCodigoMunicipioAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "uf_municipio_adquirente":
	       	 		//setUfMunicipioAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "cnpj_adquirente":
	       	 		//setCnpjAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "cpf_adquirente":
	       	 		//setCpfAdquirente(node.getTextContent());
	       	 	break;

	       	 	case "data_recibo":
	       	 		//setDataRecibo(node.getTextContent());
	       	 	break;

	       	 	case "data_impedimento_nao_transferencia":
	       	 		//setDataImpedimentoNaoTransferencia(node.getTextContent());
	       	 	break;

	       	 	case "codigo_tipo":
	       	 		setCodigoTipo(node.getTextContent());
	       	 	break;

	       	 	case "codigo_cor":
	       	 		setCodigoCor(node.getTextContent());
	       	 	break;

	       	 	case "codigo_especie":
	       	 		setCodigoEspecie(node.getTextContent());
	       	 	break;

	       	 	case "codigo_categoria":
	       	 		setCodigoCategoria(node.getTextContent());
	       	 	break;

	       	 	case "codigo_marca_modelo":
	       	 		setCodigoMarcaModelo(node.getTextContent());
	       	 	break;

	       	 	case "codigo_renavam":
	       	 		setParamRenavam(node.getTextContent());
	       	 	break;

	       	 	case "placa_antiga":
	       	 		//setPlacaAntiga(node.getTextContent());
	       	 	break;
	       	 	
	       	 }
	   }
	}


	@Override
	public List<String> getMensagens() {
		List<String> mensagens = new ArrayList<String>();
		return mensagens;
	}

	@Override
	public String exportXml() { 
		try{
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
			SOAPBody soapBody = soapEnvelope.getBody();
			SOAPElement consultarVeiculos = soapBody.addChildElement("consultaVeiculoDadosProprietResponse");
	        SOAPElement consultarVeiculosRequest = consultarVeiculos.addChildElement("consultaVeiculoDadosProprietResult");
			addElement(consultarVeiculosRequest, "codigoUsuario", getCodigoUsuario());
			addElement(consultarVeiculosRequest, "senhaUsuario", getSenhaUsuario());
			addElement(consultarVeiculosRequest, "codigoOperacao", getCodigoOperacao());
			addElement(consultarVeiculosRequest, "paramPlaca", getParamPlaca());
			addElement(consultarVeiculosRequest, "paramUF", getParamUF());
			addElement(consultarVeiculosRequest, "paramChassi", getParamChassi());
			addElement(consultarVeiculosRequest, "paramRenavam", getParamRenavam());
			addElement(consultarVeiculosRequest, "codigoRetExec", String.valueOf(getCodigoRetExec()));
			addElement(consultarVeiculosRequest, "placaVeiculo", getPlacaVeiculo());
			addElement(consultarVeiculosRequest, "codSituacaoVeic", String.valueOf(getCodSituacaoVeic()));
			addElement(consultarVeiculosRequest, "situacaoVeiculo", getSituacaoVeiculo());
			addElement(consultarVeiculosRequest, "novaPlaca", getNovaPlaca());
			addElement(consultarVeiculosRequest, "codigoMunicipio", String.valueOf(getCodigoMunicipio()));
			addElement(consultarVeiculosRequest, "nomeMunicipio", getNomeMunicipio());
			addElement(consultarVeiculosRequest, "unidadeFederacao", getUnidadeFederacao());
			addElement(consultarVeiculosRequest, "nomeUf", getNomeUf());
			addElement(consultarVeiculosRequest, "indRouboFurto", getIndRouboFurto());
			addElement(consultarVeiculosRequest, "codigoMarcaMod", String.valueOf(getCodigoMarcaMod()));
			addElement(consultarVeiculosRequest, "marcaModelo", getMarcaModelo());
			addElement(consultarVeiculosRequest, "motorDifAcesso", getMotorDifAcesso());
			addElement(consultarVeiculosRequest, "comunicacaoVenda", getComunicacaoVenda());
			addElement(consultarVeiculosRequest, "codigoCarroceria", String.valueOf(getCodigoCarroceria()));
			addElement(consultarVeiculosRequest, "carroceria", getCarroceria());
			addElement(consultarVeiculosRequest, "codigoCor", String.valueOf(getCodigoCor()));
			addElement(consultarVeiculosRequest, "cor", getCor());
			addElement(consultarVeiculosRequest, "codigoCategoria", String.valueOf(getCodigoCategoria()));
			addElement(consultarVeiculosRequest, "categoriaVeiculo", getCategoriaVeiculo());
			addElement(consultarVeiculosRequest, "codigoEspecie", String.valueOf(getCodigoEspecie()));
			addElement(consultarVeiculosRequest, "especie", getEspecie());
			addElement(consultarVeiculosRequest, "codigoTipVeic", String.valueOf(getCodigoTipVeic()));
			addElement(consultarVeiculosRequest, "tipoVeiculo", getTipoVeiculo());
			addElement(consultarVeiculosRequest, "anoFabricacao", String.valueOf(getAnoFabricacao()));
			addElement(consultarVeiculosRequest, "anoModelo", String.valueOf(getAnoModelo()));
			addElement(consultarVeiculosRequest, "potencia", String.valueOf(getPotencia()));
			addElement(consultarVeiculosRequest, "cilindrada", getCilindrada());
			addElement(consultarVeiculosRequest, "codigoCombustive", String.valueOf(getCodigoCombustive()));
			addElement(consultarVeiculosRequest, "combustivel", getCombustivel());
			addElement(consultarVeiculosRequest, "numeroMotor", getNumeroMotor());
			addElement(consultarVeiculosRequest, "tracaoMax", String.valueOf(getTracaoMax()));
			addElement(consultarVeiculosRequest, "pesoBrutoTotal", String.valueOf(getPesoBrutoTotal()));
			addElement(consultarVeiculosRequest, "anoLicenciamento", String.valueOf(getAnoLicenciamento()));
			addElement(consultarVeiculosRequest, "capacidadeCarga", String.valueOf(getCapacidadeCarga()));
			addElement(consultarVeiculosRequest, "procedencia", getProcedencia());
			addElement(consultarVeiculosRequest, "capacidadePassag", String.valueOf(getCapacidadePassag()));
			addElement(consultarVeiculosRequest, "numeroEixos", String.valueOf(getNumeroEixos()));
			addElement(consultarVeiculosRequest, "restricao1", getRestricao1());
			addElement(consultarVeiculosRequest, "restricao2", getRestricao2());
			addElement(consultarVeiculosRequest, "restricao3", getRestricao3());
			addElement(consultarVeiculosRequest, "restricao4", getRestricao4());
			addElement(consultarVeiculosRequest, "restricao5", getRestricao5());
			addElement(consultarVeiculosRequest, "restricao6", getRestricao6());
			addElement(consultarVeiculosRequest, "tipoDocumento", String.valueOf(getTipoDocumento()));
			addElement(consultarVeiculosRequest, "numeroCpfCgc", getNumeroCpfCgc());
			addElement(consultarVeiculosRequest, "nomeProprietario", getNomeProprietario());
			addElement(consultarVeiculosRequest, "endereco", getEndereco());
			addElement(consultarVeiculosRequest, "numeroEndereco", getNumeroEndereco());
			addElement(consultarVeiculosRequest, "complemEndereco", getComplemEndereco());
			addElement(consultarVeiculosRequest, "nomeBairro", getNomeBairro());
			addElement(consultarVeiculosRequest, "numeroCep", getNumeroCep());
			addElement(consultarVeiculosRequest, "numeroTelefone", getNumeroTelefone());
			addElement(consultarVeiculosRequest, "regravChassi", getRegravChassi());
			addElement(consultarVeiculosRequest, "dataAtualizacao", Util.convCalendarString3(getDataAtualizacao()));
			addElement(consultarVeiculosRequest, "valorDebIpva", String.valueOf(getValorDebIpva()));
			addElement(consultarVeiculosRequest, "valorDebLicenc", String.valueOf(getValorDebLicenc()));
			addElement(consultarVeiculosRequest, "valorDebMulta", String.valueOf(getValorDebMulta()));
			addElement(consultarVeiculosRequest, "valorDebDpvat", String.valueOf(getValorDebDpvat()));
			addElement(consultarVeiculosRequest, "valorDebInfTrami", String.valueOf(getValorDebInfTrami()));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapMessage.writeTo(out);
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + new String(out.toByteArray());
		}
		catch(Exception e){e.printStackTrace();return null;}
	}
	
	private void addElement(SOAPElement soapElement, String nameElement, Object value) throws Exception{
		SOAPElement element = soapElement.addChildElement(nameElement);
		element.addTextNode((value != null ? String.valueOf(value) : ""));
	}
	
	@Override
	public JSONObject exportJson() {
		try {
			return new JSONObject(this.toString());
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
