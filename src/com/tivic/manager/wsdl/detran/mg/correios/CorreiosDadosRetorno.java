package com.tivic.manager.wsdl.detran.mg.correios;

import java.util.GregorianCalendar;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class CorreiosDadosRetorno extends DadosRetornoMG {
	
	private String numeroProcessamento;
	private String placa;
	private GregorianCalendar dataNovoPrazoDefesa;
	private GregorianCalendar dataNovoPrazoFici;
	private GregorianCalendar dataNovoPrazoJari;
	private int codigoOrgaoAutuador;
	private String ait;
	private int codigoInfracao;
	private String codigoRenainf;
	private int sneIndicadorAdesaoVeiculo;
	private GregorianCalendar sneDataAdesaoVeiculo;
	private GregorianCalendar sneHoraAdesaoVeiculo;
	private int sneIndicadorAdesaoOrgaoAutuador;
	private int origemPossuidorVeiculo;
	private int tipoDocumentoPossuidor;
	private String numeroIdentificacaoPossuidor;
	private String nomePossuidor;
	private String nomeLogradouroPossuidor;
	private String numeroImovelPossuidor;
	private String complementoImovelPossuidor;
	private String bairroImovelPossuidor;
	private int codigoMunicipioPossuidor;
	private String ufImovelPossuidor;
	private String cepImovelPossuidor;
	
	public CorreiosDadosRetorno() { }
	
	public CorreiosDadosRetorno(String numeroProcessamento, String placa, GregorianCalendar dataNovoPrazoDefesa,
			GregorianCalendar dataNovoPrazoFici, GregorianCalendar dataNovoPrazoJari, int codigoOrgaoAutuador,
			String ait, int codigoInfracao, String codigoRenainf, int sneIndicadorAdesaoVeiculo,
			GregorianCalendar sneDataAdesaoVeiculo, GregorianCalendar sneHoraAdesaoVeiculo,
			int sneIndicadorAdesaoOrgaoAutuador, int origemPossuidorVeiculo, int tipoDocumentoPossuidor,
			String numeroIdentificacaoPossuidor, String nomePossuidor, String nomeLogradouroPossuidor,
			String numeroImovelPossuidor, String complementoImovelPossuidor, String bairroImovelPossuidor,
			int codigoMunicipioPossuidor, String ufImovelPossuidor, String cepImovelPossuidor) {
		super();
		this.numeroProcessamento = numeroProcessamento;
		this.placa = placa;
		this.dataNovoPrazoDefesa = dataNovoPrazoDefesa;
		this.dataNovoPrazoFici = dataNovoPrazoFici;
		this.dataNovoPrazoJari = dataNovoPrazoJari;
		this.codigoOrgaoAutuador = codigoOrgaoAutuador;
		this.ait = ait;
		this.codigoInfracao = codigoInfracao;
		this.codigoRenainf = codigoRenainf;
		this.sneIndicadorAdesaoVeiculo = sneIndicadorAdesaoVeiculo;
		this.sneDataAdesaoVeiculo = sneDataAdesaoVeiculo;
		this.sneHoraAdesaoVeiculo = sneHoraAdesaoVeiculo;
		this.sneIndicadorAdesaoOrgaoAutuador = sneIndicadorAdesaoOrgaoAutuador;
		this.origemPossuidorVeiculo = origemPossuidorVeiculo;
		this.tipoDocumentoPossuidor = tipoDocumentoPossuidor;
		this.numeroIdentificacaoPossuidor = numeroIdentificacaoPossuidor;
		this.nomePossuidor = nomePossuidor;
		this.nomeLogradouroPossuidor = nomeLogradouroPossuidor;
		this.numeroImovelPossuidor = numeroImovelPossuidor;
		this.complementoImovelPossuidor = complementoImovelPossuidor;
		this.bairroImovelPossuidor = bairroImovelPossuidor;
		this.codigoMunicipioPossuidor = codigoMunicipioPossuidor;
		this.ufImovelPossuidor = ufImovelPossuidor;
		this.cepImovelPossuidor = cepImovelPossuidor;
	}
	
	@Override
	public String toString() {
		return "{\"codigoRetorno\":" + getCodigoRetorno() 
				+ ", \"mensagemRetorno\": \"" + getMensagemRetorno() + "\""
				+ ", \"numeroProcessamento\": \"" + getNumeroProcessamento() + "\"" 
				+ ", \"placa\": \"" + getPlaca() + "\""
				+ ", \"dataNovoPrazoDefesa\": \"" + Util.formatDate(getDataNovoPrazoDefesa(), "yyyyMMdd") + "\""
				+ ", \"dataNovoPrazoFici\": \"" + Util.formatDate(getDataNovoPrazoFici(), "yyyyMMdd") + "\""
				+ ", \"dataNovoPrazoJari\": \"" + Util.formatDate(getDataNovoPrazoJari(), "yyyyMMdd") + "\""
				+ ", \"codigoOrgaoAutuador\": " + getCodigoOrgaoAutuador() 
				+ ", \"ait\": \"" + getAit() + "\"" 
				+ ", \"codigoInfracao\": " + getCodigoInfracao()
				+ ", \"codigoRenainf\": \"" +  getCodigoRenainf() + "\"" 
				+ ", \"sneIndicadorAdesaoVeiculo\": " + getSneIndicadorAdesaoVeiculo()
				+ ", \"sneDataAdesaoVeiculo\": \"" + Util.formatDate(getSneDataAdesaoVeiculo(), "yyyyMMdd") + "\""
				+ ", \"sneHoraAdesaoVeiculo\": \"" + Util.formatDate(getSneDataAdesaoVeiculo(), "hhmm") + "\""
				+ ", \"sneIndicadorAdesaoOrgaoAutuador\": " + getSneIndicadorAdesaoOrgaoAutuador()
				+ ", \"origemPossuidorVeiculo\": " + getOrigemPossuidorVeiculo()
				+ ", \"tipoDocumentoPossuidor\": " + getTipoDocumentoPossuidor()
				+ ", \"numeroIdentificacaoPossuidor\": \"" + getNumeroIdentificacaoPossuidor() +"\""
				+ ", \"nomePossuidor\": \"" + getNomePossuidor() +"\""
				+ ", \"nomeLogradouroPossuidor\": \"" + getNomeLogradouroPossuidor() +"\""
				+ ", \"numeroImovelPossuidor\": \"" + getNumeroImovelPossuidor() +"\""
				+ ", \"complementoImovelPossuidor\": \"" + getComplementoImovelPossuidor() +"\""
				+ ", \"bairroImovelPossuidor\": \"" + getBairroImovelPossuidor() +"\""
				+ ", \"codigoMunicipioPossuidor\": " + getCodigoMunicipioPossuidor()
				+ ", \"ufImovelPossuidor\": \"" + getUfImovelPossuidor() +"\""
				+ ", \"cepImovelPossuidor\": \"" + getCepImovelPossuidor() +"\""
				+ ", \"mensagens\": " + getMensagensJson() + "}";
	}

	public String getNumeroProcessamento() {
		return numeroProcessamento;
	}

	public void setNumeroProcessamento(String numeroProcessamento) {
		this.numeroProcessamento = numeroProcessamento;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public GregorianCalendar getDataNovoPrazoDefesa() {
		return dataNovoPrazoDefesa;
	}

	public void setDataNovoPrazoDefesa(GregorianCalendar dataNovoPrazoDefesa) {
		this.dataNovoPrazoDefesa = dataNovoPrazoDefesa;
	}

	public GregorianCalendar getDataNovoPrazoFici() {
		return dataNovoPrazoFici;
	}

	public void setDataNovoPrazoFici(GregorianCalendar dataNovoPrazoFici) {
		this.dataNovoPrazoFici = dataNovoPrazoFici;
	}

	public GregorianCalendar getDataNovoPrazoJari() {
		return dataNovoPrazoJari;
	}

	public void setDataNovoPrazoJari(GregorianCalendar dataNovoPrazoJari) {
		this.dataNovoPrazoJari = dataNovoPrazoJari;
	}

	public int getCodigoOrgaoAutuador() {
		return codigoOrgaoAutuador;
	}

	public void setCodigoOrgaoAutuador(int codigoOrgaoAutuador) {
		this.codigoOrgaoAutuador = codigoOrgaoAutuador;
	}

	public String getAit() {
		return ait;
	}

	public void setAit(String ait) {
		this.ait = ait;
	}

	public int getCodigoInfracao() {
		return codigoInfracao;
	}

	public void setCodigoInfracao(int codigoInfracao) {
		this.codigoInfracao = codigoInfracao;
	}

	public String getCodigoRenainf() {
		return codigoRenainf;
	}

	public void setCodigoRenainf(String codigoRenainf) {
		this.codigoRenainf = codigoRenainf;
	}

	public int getSneIndicadorAdesaoVeiculo() {
		return sneIndicadorAdesaoVeiculo;
	}

	public void setSneIndicadorAdesaoVeiculo(int sneIndicadorAdesaoVeiculo) {
		this.sneIndicadorAdesaoVeiculo = sneIndicadorAdesaoVeiculo;
	}

	public GregorianCalendar getSneDataAdesaoVeiculo() {
		return sneDataAdesaoVeiculo;
	}

	public void setSneDataAdesaoVeiculo(GregorianCalendar sneDataAdesaoVeiculo) {
		this.sneDataAdesaoVeiculo = sneDataAdesaoVeiculo;
	}

	public GregorianCalendar getSneHoraAdesaoVeiculo() {
		return sneHoraAdesaoVeiculo;
	}

	public void setSneHoraAdesaoVeiculo(GregorianCalendar sneHoraAdesaoVeiculo) {
		this.sneHoraAdesaoVeiculo = sneHoraAdesaoVeiculo;
	}

	public int getSneIndicadorAdesaoOrgaoAutuador() {
		return sneIndicadorAdesaoOrgaoAutuador;
	}

	public void setSneIndicadorAdesaoOrgaoAutuador(int sneIndicadorAdesaoOrgaoAutuador) {
		this.sneIndicadorAdesaoOrgaoAutuador = sneIndicadorAdesaoOrgaoAutuador;
	}

	public int getOrigemPossuidorVeiculo() {
		return origemPossuidorVeiculo;
	}

	public void setOrigemPossuidorVeiculo(int origemPossuidorVeiculo) {
		this.origemPossuidorVeiculo = origemPossuidorVeiculo;
	}

	public int getTipoDocumentoPossuidor() {
		return tipoDocumentoPossuidor;
	}

	public void setTipoDocumentoPossuidor(int tipoDocumentoPossuidor) {
		this.tipoDocumentoPossuidor = tipoDocumentoPossuidor;
	}

	public String getNumeroIdentificacaoPossuidor() {
		return numeroIdentificacaoPossuidor;
	}

	public void setNumeroIdentificacaoPossuidor(String numeroIdentificacaoPossuidor) {
		this.numeroIdentificacaoPossuidor = numeroIdentificacaoPossuidor;
	}

	public String getNomePossuidor() {
		return nomePossuidor;
	}

	public void setNomePossuidor(String nomePossuidor) {
		this.nomePossuidor = nomePossuidor;
	}

	public String getNomeLogradouroPossuidor() {
		return nomeLogradouroPossuidor;
	}

	public void setNomeLogradouroPossuidor(String nomeLogradouroPossuidor) {
		this.nomeLogradouroPossuidor = nomeLogradouroPossuidor;
	}

	public String getNumeroImovelPossuidor() {
		return numeroImovelPossuidor;
	}

	public void setNumeroImovelPossuidor(String numeroImovelPossuidor) {
		this.numeroImovelPossuidor = numeroImovelPossuidor;
	}

	public String getComplementoImovelPossuidor() {
		return complementoImovelPossuidor;
	}

	public void setComplementoImovelPossuidor(String complementoImovelPossuidor) {
		this.complementoImovelPossuidor = complementoImovelPossuidor;
	}

	public String getBairroImovelPossuidor() {
		return bairroImovelPossuidor;
	}

	public void setBairroImovelPossuidor(String bairroImovelPossuidor) {
		this.bairroImovelPossuidor = bairroImovelPossuidor;
	}

	public int getCodigoMunicipioPossuidor() {
		return codigoMunicipioPossuidor;
	}

	public void setCodigoMunicipioPossuidor(int codigoMunicipioPossuidor) {
		this.codigoMunicipioPossuidor = codigoMunicipioPossuidor;
	}

	public String getUfImovelPossuidor() {
		return ufImovelPossuidor;
	}

	public void setUfImovelPossuidor(String ufImovelPossuidor) {
		this.ufImovelPossuidor = ufImovelPossuidor;
	}

	public String getCepImovelPossuidor() {
		return cepImovelPossuidor;
	}

	public void setCepImovelPossuidor(String cepImovelPossuidor) {
		this.cepImovelPossuidor = cepImovelPossuidor;
	}
	
	@Override
	public void importData(NodeList nodeList, String xml) {
		setXml(xml);
		for( int i = 0; i  < nodeList.getLength(); i++){
	       	 Node node = nodeList.item(i);
	       	 
	       	 if(node.getTextContent() == null || node.getTextContent().equals(""))
	       		 continue;
	       	 
	       	 switch(node.getNodeName()){
	       	 	case "codigo_retorno":
	       	 		setCodigoRetorno(Integer.parseInt(node.getTextContent()));
	       	 	break;
	       	 	
	       	 	case "mensagem_retorno":
	       	 		setMensagemRetorno(node.getTextContent());
	       	 	break;

	       	 	case "numero_processamento":
	       	 		setNumeroProcessamento(node.getTextContent());
	       	 	break;

	       	 	case "placa":
	       	 		setPlaca(node.getTextContent());
	       	 	break;

	       	 	case "data_novo_prazo_defesa":
					setDataNovoPrazoDefesa(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
				break;

	       	 	case "data_novo_prazo_fici":
					setDataNovoPrazoFici(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
	       	 	break;

	       	 	case "data_novo_prazo_jari":
					setDataNovoPrazoJari(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
				break;

	       	 	case "codigo_orgao_autuador":
	       	 		setCodigoOrgaoAutuador(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "ait":
	       	 		setAit(node.getTextContent());
	       	 	break;

	       	 	case "codigo_infracao":
	       	 		setCodigoInfracao(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "codigo_renainf":
	       	 		setCodigoRenainf(node.getTextContent());
	       	 	break;

	       	 	case "sne_indicador_adesao_veiculo":
	       	 		setSneIndicadorAdesaoVeiculo(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "sne_data_adesao_veiculo":
	       	 		setSneDataAdesaoVeiculo(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
	       	 	break;

	       	 	case "sne_hora_adesao_veiculo":
	       	 		setSneHoraAdesaoVeiculo(Util.convStringSemFormatacaoReverseSToGregorianCalendarHora(node.getTextContent()));
	       	 	break;

	       	 	case "sne_indicador_adesao_orgao_autuado":
	       	 		setSneIndicadorAdesaoOrgaoAutuador(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "origem_possuidor_veiculo":
	       	 		setOrigemPossuidorVeiculo(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "tipo_documento_possuidor":
	       	 		setTipoDocumentoPossuidor(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "numero_identificacao_possuidor":
	       	 		setNumeroIdentificacaoPossuidor(node.getTextContent());
	       	 	break;

	       	 	case "nome_possuidor":
	       	 		setNomePossuidor(node.getTextContent());
	       	 	break;

	       	 	case "nome_logradouro_possuidor":
	       	 		setNomeLogradouroPossuidor(node.getTextContent());
	       	 	break;

	       	 	case "numero_imovel_possuidor":
	       	 		setNumeroImovelPossuidor(node.getTextContent());
	       	 	break;

	       	 	case "complemento_imovel_possuidor":
	       	 		setComplementoImovelPossuidor(node.getTextContent());
	       	 	break;

	       	 	case "bairro_imovel_possuidor":
	       	 		setBairroImovelPossuidor(node.getTextContent());
	       	 	break;

	       	 	case "codigo_municipio_possuidor":
	       	 		setCodigoMunicipioPossuidor(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "uf_imovel_possuidor":
	       	 		setUfImovelPossuidor(node.getTextContent());
	       	 	break;

	       	 	case "cep_imovel_possuidor":
	       	 		setCepImovelPossuidor(node.getTextContent());
	       	 	break;

	       	 	case "mensagens":
	       	 		setMensagens(node.getChildNodes());
	       	 	break;

	       	 }
		}
	}	
}
