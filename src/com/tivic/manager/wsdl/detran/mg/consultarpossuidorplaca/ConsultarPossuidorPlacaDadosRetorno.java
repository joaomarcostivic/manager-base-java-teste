package com.tivic.manager.wsdl.detran.mg.consultarpossuidorplaca;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultarPossuidorPlacaDadosRetorno extends DadosRetornoMG {

	private String placa;
	private GregorianCalendar dataAquisicao;
	private String cpf;
	private String cnpj;
	private String nome;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String rg;
	private String orgaoExpedidorRg;
	private String ufRg;
	private int quantidadePossuidoresAnteriores;
	private List<PossuidorAnterior> possuidoresAnteriores = new ArrayList<>();

	public ConsultarPossuidorPlacaDadosRetorno() {
	}

	public ConsultarPossuidorPlacaDadosRetorno(int codigoRetorno, String mensagemRetorno, String placa,
			GregorianCalendar dataAquisicao, String cpf, String cnpj, String nome, String logradouro, String numero,
			String complemento, String bairro, String rg, String orgaoExpedidorRg, String ufRg,
			int quantidadePossuidoresAnteriores, List<PossuidorAnterior> possuidoresAnteriores) {
		super(codigoRetorno, mensagemRetorno);
		this.placa = placa;
		this.dataAquisicao = dataAquisicao;
		this.cpf = cpf;
		this.cnpj = cnpj;
		this.nome = nome;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.rg = rg;
		this.orgaoExpedidorRg = orgaoExpedidorRg;
		this.ufRg = ufRg;
		this.quantidadePossuidoresAnteriores = quantidadePossuidoresAnteriores;
		this.possuidoresAnteriores = possuidoresAnteriores;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public GregorianCalendar getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(GregorianCalendar dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
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

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getOrgaoExpedidorRg() {
		return orgaoExpedidorRg;
	}

	public void setOrgaoExpedidorRg(String orgaoExpedidorRg) {
		this.orgaoExpedidorRg = orgaoExpedidorRg;
	}

	public String getUfRg() {
		return ufRg;
	}

	public void setUfRg(String ufRg) {
		this.ufRg = ufRg;
	}

	public int getQuantidadePossuidoresAnteriores() {
		return quantidadePossuidoresAnteriores;
	}

	public void setQuantidadePossuidoresAnteriores(int quantidadePossuidoresAnteriores) {
		this.quantidadePossuidoresAnteriores = quantidadePossuidoresAnteriores;
	}

	public List<PossuidorAnterior> getPossuidoresAnteriores() {
		return possuidoresAnteriores;
	}

	public void setPossuidoresAnteriores(List<PossuidorAnterior> possuidoresAnteriores) {
		this.possuidoresAnteriores = possuidoresAnteriores;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}

	@Override
	public void importData(NodeList nodeList, String xml) {
		setXml(xml);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			switch (node.getNodeName()) {
			case "codigo_retorno":
				setCodigoRetorno(Integer.parseInt(node.getTextContent()));
				break;
			case "mensagem_retorno":
				setMensagemRetorno(node.getTextContent());
				break;
			case "placa":
				setPlaca(node.getTextContent());
				break;
			case "data_aquisicao":
				setDataAquisicao(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
				break;
			case "cpf":
				setCpf(node.getTextContent());
				break;
			case "cnpj":
				setCnpj(node.getTextContent());
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
			case "rg":
				setRg(node.getTextContent());
				break;
			case "orgao_expedidor_rg":
				setOrgaoExpedidorRg(node.getTextContent());
				break;
			case "uf_rg":
				setUfRg(node.getTextContent());
				break;
			case "quantidade_possuidores_anteriores":
				setQuantidadePossuidoresAnteriores(Integer.parseInt(node.getTextContent()));
				break;
			case "possuidores_anteriores":
				NodeList possuidores = node.getChildNodes();
				for (int j = 0; j < possuidores.getLength(); j++) {
					Node possuidorNode = possuidores.item(j);
					if (possuidorNode.getNodeName().equals("possuidor_anterior")) {
						PossuidorAnterior possuidorAnterior = new PossuidorAnterior();
						NodeList dadosPossuidor = possuidorNode.getChildNodes();
						for (int k = 0; k < dadosPossuidor.getLength(); k++) {
							Node dadoPossuidor = dadosPossuidor.item(k);
							switch (dadoPossuidor.getNodeName()) {
							case "data_aquisicao":
								possuidorAnterior
										.setDataAquisicao(Util.convStringSemFormatacaoReverseSToGregorianCalendar(
												dadoPossuidor.getTextContent()));
								break;
							case "cpf":
								possuidorAnterior.setCpf(dadoPossuidor.getTextContent());
								break;
							case "cnpj":
								possuidorAnterior.setCnpj(dadoPossuidor.getTextContent());
								break;
							case "codigo_municipio":
								possuidorAnterior.setCodigoMunicipio(dadoPossuidor.getTextContent());
								break;
							case "uf":
								possuidorAnterior.setUf(dadoPossuidor.getTextContent());
								break;
							case "nome":
								possuidorAnterior.setNome(dadoPossuidor.getTextContent());
								break;
							case "logradouro":
								possuidorAnterior.setLogradouro(dadoPossuidor.getTextContent());
								break;
							case "numero":
								possuidorAnterior.setNumero(dadoPossuidor.getTextContent());
								break;
							case "complemento":
								possuidorAnterior.setComplemento(dadoPossuidor.getTextContent());
								break;
							case "bairro":
								possuidorAnterior.setBairro(dadoPossuidor.getTextContent());
								break;
							}
						}
						possuidoresAnteriores.add(possuidorAnterior);
					}
				}
				break;
			}
		}
	}

}
