package com.tivic.manager.wsdl.detran.mg.consultarmovimentacoes;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultarMovimentacoesDadosRetorno extends DadosRetornoMG  {

	private int codigoRetorno;
	private String mensagemRetorno;
	private String codigoRenavam;
	private String chassi;
	private String placa;
	private String ufVeiculo;
	private String orgao;
	private String nomeOrgao;
	private String ait;
	private String codigoInfracao;
	private String codigoDesdobramentoInfracao;
	private String numeroProcessamento;
	private Integer quantidadeMovimentacoes;
	private List<Movimentacao> movimentacoes;
	
	public ConsultarMovimentacoesDadosRetorno() {
		this.movimentacoes = new ArrayList<Movimentacao>();
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
		       	 	case "codigo_renavam":
		       	 		setCodigoRenavam(value);
		       	 		break;
		       	 	case "chassi":
		       	 		setChassi(value);
		       	 		break;
		       	 	case "placa":
		       	 		setPlaca(value);
		       	 		break;
		       	 	case "uf_veiculo":
		       	 		setUfVeiculo(value);
		       	 		break;
		       	 	case "orgao":
		       	 		setOrgao(value);
		       	 		break;
		       	 	case "nome_orgao":
		       	 		setNomeOrgao(value);
		       	 		break;
		       	 	case "ait":
		       	 		setAit(value);
		       	 		break;
		       	 	case "codigo_infracao":
		       	 		setCodigoInfracao(value);
		       	 		break;
		       	 	case "codigo_desdobramento_infracao":
		       	 		setCodigoDesdobramentoInfracao(value);
		       	 		break;
		       	 	case "numero_processamento":
		       	 		setNumeroProcessamento(value);
		       	 		break;
		       	 	case "quantidade_movimentacoes":
		       	 		setQuantidadeMovimentacoes(Integer.parseInt(value));
		       	 		break;
		       	 	case "movimentacoes":
		       	 		setMovimentacoes(node);
		       	 		break;
	       		}
	       	 } catch (NumberFormatException nfe) {
	       		 System.out.println(nfe.getMessage());
	       	 }
		}
	}

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

	public String getCodigoRenavam() {
		return codigoRenavam;
	}

	public void setCodigoRenavam(String codigoRenavam) {
		this.codigoRenavam = codigoRenavam;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getUfVeiculo() {
		return ufVeiculo;
	}

	public void setUfVeiculo(String ufVeiculo) {
		this.ufVeiculo = ufVeiculo;
	}

	public String getOrgao() {
		return orgao;
	}

	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}

	public String getNomeOrgao() {
		return nomeOrgao;
	}

	public void setNomeOrgao(String nomeOrgao) {
		this.nomeOrgao = nomeOrgao;
	}

	public String getAit() {
		return ait;
	}

	public void setAit(String ait) {
		this.ait = ait;
	}

	public String getCodigoInfracao() {
		return codigoInfracao;
	}

	public void setCodigoInfracao(String codigoInfracao) {
		this.codigoInfracao = codigoInfracao;
	}

	public String getCodigoDesdobramentoInfracao() {
		return codigoDesdobramentoInfracao;
	}

	public void setCodigoDesdobramentoInfracao(String codigoDesdobramentoInfracao) {
		this.codigoDesdobramentoInfracao = codigoDesdobramentoInfracao;
	}

	public String getNumeroProcessamento() {
		return numeroProcessamento;
	}

	public void setNumeroProcessamento(String numeroProcessamento) {
		this.numeroProcessamento = numeroProcessamento;
	}

	public Integer getQuantidadeMovimentacoes() {
		return quantidadeMovimentacoes;
	}

	public void setQuantidadeMovimentacoes(Integer quantidadeMovimentacoes) {
		this.quantidadeMovimentacoes = quantidadeMovimentacoes;
	}

	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public void setMovimentacoes(Node node) {
		NodeList nodeList = node.getChildNodes();
		for( int i = 0; i  < nodeList.getLength(); i++){
			Movimentacao movimentacao = new Movimentacao();
			Node nodeChild = nodeList.item(i);
			NodeList nodeChildList = nodeChild.getChildNodes();
			for( int j = 0; j  < nodeChildList.getLength(); j++){
				Node nodeChildChild = nodeChildList.item(j);
		       	String value = nodeChildChild.getTextContent();
		       	try {
		       		switch(nodeChildChild.getNodeName()){
			       	 	case "codigo_movimentacao":
			       	 		movimentacao.setCodigoMovimentacao(Integer.parseInt(value));
			       	 		break;
			       	 	case "descricao_movimentacao_1":
			       	 		movimentacao.setDescricaoMovimentacao1(value);
			       	 		break;
			       	 	case "descricao_movimentacao_2":
			       	 		movimentacao.setDescricaoMovimentacao2(value);
			       	 		break;
		       		}
		      	} catch (NumberFormatException nfe) {
		      		System.out.println(nfe.getMessage());
		      	}
			}
			this.movimentacoes.add(movimentacao);
		}
	}
	
}
