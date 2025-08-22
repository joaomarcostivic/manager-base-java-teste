package com.tivic.manager.wsdl.detran.mg.consultaautobasenacional;

import java.util.GregorianCalendar;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultaAutoBaseNacionalDadosRetorno extends DadosRetornoMG {

	private int orgao;
	private String ait;
	private String codigoInfracaoEditado;
	private String tipoAit;
	private String placa;
	private String exigivel;
	private GregorianCalendar dataCadastroAit;
	private String indicadorAssinatura;
	private String cnhCondutor;
	private String ufCnhCondutor;
	private GregorianCalendar dataAit;
	private GregorianCalendar horaAit;
	private String localAit;
	private String municipioAit;
	private double medicaoReal;
	private double medicaoConsiderada;
	private double limite;
	private int unidadeMedida;
	private double valorInfracao;
	private String municipioEmplacamento;
	private String ufEmplacamento;
	private String codigoRenavam;
	private String tipoVeiculo;
	private String marcaModelo;
	private String cor;
	private String especie;
	private String carroceria;
	private String categoria;
	private String nomePossuidor;
	private String cpfCnpjPossuidor;
	private GregorianCalendar dataEmissaoAutuacao;
	private GregorianCalendar dataLimiteDefesa;
	private GregorianCalendar dataAceiteAutuacaoUf;
	private String numeroNotificacaoPenalidade;
	private GregorianCalendar dataEmissaoPenalidade;
	private GregorianCalendar dataLimiteJari;
	private GregorianCalendar dataAceitePenalidadeUf;
	private double valorPago;
	private String ufPagamento;
	private GregorianCalendar dataRegistroPagamento;
	private GregorianCalendar dataPagamento;
	private int quantidadePagamento;
	private String tipoOcorrencia;
	private String origemOcorrencia;
	private String numeroProcessamentoOcorrencia;
	private GregorianCalendar dataOcorrencia;
	private GregorianCalendar dataRegistroOcorrencia;
	private String nomeInfrator;
	private String cnhInfrator;
	private String ufCnhInfrator;
	private String tipoDocumentoInfrator;
	private String numeroDocumentoInfrator;
	private GregorianCalendar dataApresentacao;
	private String statusPontuacao;
	private GregorianCalendar dataAceiteUf;
	
	public ConsultaAutoBaseNacionalDadosRetorno() {
		// TODO Auto-generated constructor stub
	}
	
	public ConsultaAutoBaseNacionalDadosRetorno(int orgao, String ait, String codigoInfracaoEditado, String tipoAit,
			String placa, String exigivel, GregorianCalendar dataCadastroAit, String indicadorAssinatura,
			String cnhCondutor, String ufCnhCondutor, GregorianCalendar dataAit, GregorianCalendar horaAit,
			String localAit, String municipioAit, double medicaoReal, double medicaoConsiderada, double limite,
			int unidadeMedida, double valorInfracao, String municipioEmplacamento, String ufEmplacamento,
			String codigoRenavam, String tipoVeiculo, String marcaModelo, String cor, String especie, String carroceria,
			String categoria, String nomePossuidor, String cpfCnpjPossuidor, GregorianCalendar dataEmissaoAutuacao,
			GregorianCalendar dataLimiteDefesa, GregorianCalendar dataAceiteAutuacaoUf,
			String numeroNotificacaoPenalidade, GregorianCalendar dataEmissaoPenalidade,
			GregorianCalendar dataLimiteJari, GregorianCalendar dataAceitePenalidadeUf, double valorPago,
			String ufPagamento, GregorianCalendar dataRegistroPagamento, GregorianCalendar dataPagamento,
			int quantidadePagamento, String tipoOcorrencia, String origemOcorrencia,
			String numeroProcessamentoOcorrencia, GregorianCalendar dataOcorrencia,
			GregorianCalendar dataRegistroOcorrencia, String nomeInfrator, String cnhInfrator, String ufCnhInfrator,
			String tipoDocumentoInfrator, String numeroDocumentoInfrator, GregorianCalendar dataApresentacao,
			String statusPontuacao, GregorianCalendar dataAceiteUf) {
		super();
		this.orgao = orgao;
		this.ait = ait;
		this.codigoInfracaoEditado = codigoInfracaoEditado;
		this.tipoAit = tipoAit;
		this.placa = placa;
		this.exigivel = exigivel;
		this.dataCadastroAit = dataCadastroAit;
		this.indicadorAssinatura = indicadorAssinatura;
		this.cnhCondutor = cnhCondutor;
		this.ufCnhCondutor = ufCnhCondutor;
		this.dataAit = dataAit;
		this.horaAit = horaAit;
		this.localAit = localAit;
		this.municipioAit = municipioAit;
		this.medicaoReal = medicaoReal;
		this.medicaoConsiderada = medicaoConsiderada;
		this.limite = limite;
		this.unidadeMedida = unidadeMedida;
		this.valorInfracao = valorInfracao;
		this.municipioEmplacamento = municipioEmplacamento;
		this.ufEmplacamento = ufEmplacamento;
		this.codigoRenavam = codigoRenavam;
		this.tipoVeiculo = tipoVeiculo;
		this.marcaModelo = marcaModelo;
		this.cor = cor;
		this.especie = especie;
		this.carroceria = carroceria;
		this.categoria = categoria;
		this.nomePossuidor = nomePossuidor;
		this.cpfCnpjPossuidor = cpfCnpjPossuidor;
		this.dataEmissaoAutuacao = dataEmissaoAutuacao;
		this.dataLimiteDefesa = dataLimiteDefesa;
		this.dataAceiteAutuacaoUf = dataAceiteAutuacaoUf;
		this.numeroNotificacaoPenalidade = numeroNotificacaoPenalidade;
		this.dataEmissaoPenalidade = dataEmissaoPenalidade;
		this.dataLimiteJari = dataLimiteJari;
		this.dataAceitePenalidadeUf = dataAceitePenalidadeUf;
		this.valorPago = valorPago;
		this.ufPagamento = ufPagamento;
		this.dataRegistroPagamento = dataRegistroPagamento;
		this.dataPagamento = dataPagamento;
		this.quantidadePagamento = quantidadePagamento;
		this.tipoOcorrencia = tipoOcorrencia;
		this.origemOcorrencia = origemOcorrencia;
		this.numeroProcessamentoOcorrencia = numeroProcessamentoOcorrencia;
		this.dataOcorrencia = dataOcorrencia;
		this.dataRegistroOcorrencia = dataRegistroOcorrencia;
		this.nomeInfrator = nomeInfrator;
		this.cnhInfrator = cnhInfrator;
		this.ufCnhInfrator = ufCnhInfrator;
		this.tipoDocumentoInfrator = tipoDocumentoInfrator;
		this.numeroDocumentoInfrator = numeroDocumentoInfrator;
		this.dataApresentacao = dataApresentacao;
		this.statusPontuacao = statusPontuacao;
		this.dataAceiteUf = dataAceiteUf;
	}
	
	public int getOrgao() {
		return orgao;
	}

	public void setOrgao(int orgao) {
		this.orgao = orgao;
	}

	public String getAit() {
		return ait;
	}

	public void setAit(String ait) {
		this.ait = ait;
	}

	public String getCodigoInfracaoEditado() {
		return codigoInfracaoEditado;
	}

	public void setCodigoInfracaoEditado(String codigoInfracaoEditado) {
		this.codigoInfracaoEditado = codigoInfracaoEditado;
	}

	public String getTipoAit() {
		return tipoAit;
	}

	public void setTipoAit(String tipoAit) {
		this.tipoAit = tipoAit;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getExigivel() {
		return exigivel;
	}

	public void setExigivel(String exigivel) {
		this.exigivel = exigivel;
	}

	public GregorianCalendar getDataCadastroAit() {
		return dataCadastroAit;
	}

	public void setDataCadastroAit(GregorianCalendar dataCadastroAit) {
		this.dataCadastroAit = dataCadastroAit;
	}

	public String getIndicadorAssinatura() {
		return indicadorAssinatura;
	}

	public void setIndicadorAssinatura(String indicadorAssinatura) {
		this.indicadorAssinatura = indicadorAssinatura;
	}

	public String getCnhCondutor() {
		return cnhCondutor;
	}

	public void setCnhCondutor(String cnhCondutor) {
		this.cnhCondutor = cnhCondutor;
	}

	public String getUfCnhCondutor() {
		return ufCnhCondutor;
	}

	public void setUfCnhCondutor(String ufCnhCondutor) {
		this.ufCnhCondutor = ufCnhCondutor;
	}

	public GregorianCalendar getDataAit() {
		return dataAit;
	}

	public void setDataAit(GregorianCalendar dataAit) {
		this.dataAit = dataAit;
	}

	public GregorianCalendar getHoraAit() {
		return horaAit;
	}

	public void setHoraAit(GregorianCalendar horaAit) {
		this.horaAit = horaAit;
	}

	public String getLocalAit() {
		return localAit;
	}

	public void setLocalAit(String localAit) {
		this.localAit = localAit;
	}

	public String getMunicipioAit() {
		return municipioAit;
	}

	public void setMunicipioAit(String municipioAit) {
		this.municipioAit = municipioAit;
	}

	public double getMedicaoReal() {
		return medicaoReal;
	}

	public void setMedicaoReal(double medicaoReal) {
		this.medicaoReal = medicaoReal;
	}

	public double getMedicaoConsiderada() {
		return medicaoConsiderada;
	}

	public void setMedicaoConsiderada(double medicaoConsiderada) {
		this.medicaoConsiderada = medicaoConsiderada;
	}

	public double getLimite() {
		return limite;
	}

	public void setLimite(double limite) {
		this.limite = limite;
	}

	public int getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(int unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public double getValorInfracao() {
		return valorInfracao;
	}

	public void setValorInfracao(double valorInfracao) {
		this.valorInfracao = valorInfracao;
	}

	public String getMunicipioEmplacamento() {
		return municipioEmplacamento;
	}

	public void setMunicipioEmplacamento(String municipioEmplacamento) {
		this.municipioEmplacamento = municipioEmplacamento;
	}

	public String getUfEmplacamento() {
		return ufEmplacamento;
	}

	public void setUfEmplacamento(String ufEmplacamento) {
		this.ufEmplacamento = ufEmplacamento;
	}

	public String getCodigoRenavam() {
		return codigoRenavam;
	}

	public void setCodigoRenavam(String codigoRenavam) {
		this.codigoRenavam = codigoRenavam;
	}

	public String getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(String tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public String getMarcaModelo() {
		return marcaModelo;
	}

	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getCarroceria() {
		return carroceria;
	}

	public void setCarroceria(String carroceria) {
		this.carroceria = carroceria;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNomePossuidor() {
		return nomePossuidor;
	}

	public void setNomePossuidor(String nomePossuidor) {
		this.nomePossuidor = nomePossuidor;
	}

	public String getCpfCnpjPossuidor() {
		return cpfCnpjPossuidor;
	}

	public void setCpfCnpjPossuidor(String cpfCnpjPossuidor) {
		this.cpfCnpjPossuidor = cpfCnpjPossuidor;
	}

	public GregorianCalendar getDataEmissaoAutuacao() {
		return dataEmissaoAutuacao;
	}

	public void setDataEmissaoAutuacao(GregorianCalendar dataEmissaoAutuacao) {
		this.dataEmissaoAutuacao = dataEmissaoAutuacao;
	}

	public GregorianCalendar getDataLimiteDefesa() {
		return dataLimiteDefesa;
	}

	public void setDataLimiteDefesa(GregorianCalendar dataLimiteDefesa) {
		this.dataLimiteDefesa = dataLimiteDefesa;
	}

	public GregorianCalendar getDataAceiteAutuacaoUf() {
		return dataAceiteAutuacaoUf;
	}

	public void setDataAceiteAutuacaoUf(GregorianCalendar dataAceiteAutuacaoUf) {
		this.dataAceiteAutuacaoUf = dataAceiteAutuacaoUf;
	}

	public String getNumeroNotificacaoPenalidade() {
		return numeroNotificacaoPenalidade;
	}

	public void setNumeroNotificacaoPenalidade(String numeroNotificacaoPenalidade) {
		this.numeroNotificacaoPenalidade = numeroNotificacaoPenalidade;
	}

	public GregorianCalendar getDataEmissaoPenalidade() {
		return dataEmissaoPenalidade;
	}

	public void setDataEmissaoPenalidade(GregorianCalendar dataEmissaoPenalidade) {
		this.dataEmissaoPenalidade = dataEmissaoPenalidade;
	}

	public GregorianCalendar getDataLimiteJari() {
		return dataLimiteJari;
	}

	public void setDataLimiteJari(GregorianCalendar dataLimiteJari) {
		this.dataLimiteJari = dataLimiteJari;
	}

	public GregorianCalendar getDataAceitePenalidadeUf() {
		return dataAceitePenalidadeUf;
	}

	public void setDataAceitePenalidadeUf(GregorianCalendar dataAceitePenalidadeUf) {
		this.dataAceitePenalidadeUf = dataAceitePenalidadeUf;
	}

	public double getValorPago() {
		return valorPago;
	}

	public void setValorPago(double valorPago) {
		this.valorPago = valorPago;
	}

	public String getUfPagamento() {
		return ufPagamento;
	}

	public void setUfPagamento(String ufPagamento) {
		this.ufPagamento = ufPagamento;
	}

	public GregorianCalendar getDataRegistroPagamento() {
		return dataRegistroPagamento;
	}

	public void setDataRegistroPagamento(GregorianCalendar dataRegistroPagamento) {
		this.dataRegistroPagamento = dataRegistroPagamento;
	}

	public GregorianCalendar getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(GregorianCalendar dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public int getQuantidadePagamento() {
		return quantidadePagamento;
	}

	public void setQuantidadePagamento(int quantidadePagamento) {
		this.quantidadePagamento = quantidadePagamento;
	}

	public String getTipoOcorrencia() {
		return tipoOcorrencia;
	}

	public void setTipoOcorrencia(String tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}

	public String getOrigemOcorrencia() {
		return origemOcorrencia;
	}

	public void setOrigemOcorrencia(String origemOcorrencia) {
		this.origemOcorrencia = origemOcorrencia;
	}

	public String getNumeroProcessamentoOcorrencia() {
		return numeroProcessamentoOcorrencia;
	}

	public void setNumeroProcessamentoOcorrencia(String numeroProcessamentoOcorrencia) {
		this.numeroProcessamentoOcorrencia = numeroProcessamentoOcorrencia;
	}

	public GregorianCalendar getDataOcorrencia() {
		return dataOcorrencia;
	}

	public void setDataOcorrencia(GregorianCalendar dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	public GregorianCalendar getDataRegistroOcorrencia() {
		return dataRegistroOcorrencia;
	}

	public void setDataRegistroOcorrencia(GregorianCalendar dataRegistroOcorrencia) {
		this.dataRegistroOcorrencia = dataRegistroOcorrencia;
	}

	public String getNomeInfrator() {
		return nomeInfrator;
	}

	public void setNomeInfrator(String nomeInfrator) {
		this.nomeInfrator = nomeInfrator;
	}

	public String getCnhInfrator() {
		return cnhInfrator;
	}

	public void setCnhInfrator(String cnhInfrator) {
		this.cnhInfrator = cnhInfrator;
	}

	public String getUfCnhInfrator() {
		return ufCnhInfrator;
	}

	public void setUfCnhInfrator(String ufCnhInfrator) {
		this.ufCnhInfrator = ufCnhInfrator;
	}

	public String getTipoDocumentoInfrator() {
		return tipoDocumentoInfrator;
	}

	public void setTipoDocumentoInfrator(String tipoDocumentoInfrator) {
		this.tipoDocumentoInfrator = tipoDocumentoInfrator;
	}

	public String getNumeroDocumentoInfrator() {
		return numeroDocumentoInfrator;
	}

	public void setNumeroDocumentoInfrator(String numeroDocumentoInfrator) {
		this.numeroDocumentoInfrator = numeroDocumentoInfrator;
	}

	public GregorianCalendar getDataApresentacao() {
		return dataApresentacao;
	}

	public void setDataApresentacao(GregorianCalendar dataApresentacao) {
		this.dataApresentacao = dataApresentacao;
	}

	public String getStatusPontuacao() {
		return statusPontuacao;
	}

	public void setStatusPontuacao(String statusPontuacao) {
		this.statusPontuacao = statusPontuacao;
	}

	public GregorianCalendar getDataAceiteUf() {
		return dataAceiteUf;
	}

	public void setDataAceiteUf(GregorianCalendar dataAceiteUf) {
		this.dataAceiteUf = dataAceiteUf;
	}
	
	@Override
	public String toString() {
		return "{\"orgao\": " + getOrgao() 
				+ ", \"ait\": \"" + getAit() + "\"" 
				+ ", \"codigoInfracaoEditado\": \""+ getCodigoInfracaoEditado()+"\"" 
				+ ", \"tipoAit\": " + getTipoAit()
				+ ", \"placa\": \"" + getPlaca() + "\"" 
				+ ", \"exigivel\": \"" + getExigivel() + "\""
				+ ", \"dataCadastroAit\": \"" + Util.formatDate(getDataCadastroAit(), "dd/MM/yyyy") + "\"" 
				+ ", \"indicadorAssinatura\": \"" + getIndicadorAssinatura() + "\""
				+ ", \"cnhCondutor\": \"" + getCnhCondutor() + "\"" 
				+ ", \"ufCnhCondutor\": \"" + getUfCnhCondutor() + "\"" 
				+ ", \"dataAit\": \"" + Util.formatDate(getDataAit(), "dd/MM/yyyy") + "\""
				+ ", \"horaAit\": \"" + Util.formatDate(getHoraAit(), "HH:mm") + "\"" 
				+ ", \"localAit\": \"" + getLocalAit() + "\""
				+ ", \"municipioAit\": \"" + getMunicipioAit() + "\""
				+ ", \"medicaoReal\": " + getMedicaoReal() 
				+ ", \"medicaoConsiderada\": " + getMedicaoConsiderada() 
				+ ", \"limite\": " + getLimite()
				+ ", \"unidadeMedida\": " + getUnidadeMedida() 
				+ ", \"valorInfracao\": " + getValorInfracao() 
				+ ", \"municipioEmplacamento\": \"" + getMunicipioEmplacamento() + "\""
				+ ", \"ufEmplacamento\": \"" + getUfEmplacamento() + "\""
				+ ", \"codigoRenavam\": \"" + getCodigoRenavam() + "\""
				+ ", \"tipoVeiculo\": \"" + getTipoVeiculo() + "\""
				+ ", \"marcaModelo\": \"" + getMarcaModelo() + "\"" 
				+ ", \"cor\": \"" + getCor() + "\""
				+ ", \"especie\": \"" + getEspecie() + "\"" 
				+ ", \"carroceria\": \"" + getCarroceria() + "\"" 
				+ ", \"categoria\": \"" + getCategoria() + "\"" 
				+ ", \"nomePossuidor\": \"" + getNomePossuidor() + "\"" 
				+ ", \"cpfCnpjPossuidor\": \"" + getCpfCnpjPossuidor() + "\"" 
				+ ", \"dataEmissaoAutuacao\": \"" + Util.formatDate(getDataEmissaoAutuacao(), "dd/MM/yyyy") + "\"" 
				+ ", \"dataLimiteDefesa\": \"" + Util.formatDate(getDataLimiteDefesa(), "dd/MM/yyyy") + "\""
				+ ", \"dataAceiteAutuacaoUf\": \"" + Util.formatDate(getDataAceiteAutuacaoUf(), "dd/MM/yyyy") + "\""
				+ ", \"numeroNotificacaoPenalidade\": \"" + getNumeroNotificacaoPenalidade() + "\""
				+ ", \"dataEmissaoPenalidade\": \"" + Util.formatDate(getDataEmissaoPenalidade(), "dd/MM/yyyy") + "\""
				+ ", \"dataLimiteJari\": \"" + Util.formatDate(getDataLimiteJari(), "dd/MM/yyyy") + "\""
				+ ", \"dataAceitePenalidadeUf\": \"" + Util.formatDate(getDataAceitePenalidadeUf(), "dd/MM/yyyy") + "\"" 
				+ ", \"valorPago\": " + getValorPago() 
				+ ", \"ufPagamento\": \"" + getUfPagamento() + "\"" 
				+ ", \"dataRegistroPagamento\": \"" + Util.formatDate(getDataRegistroPagamento(), "dd/MM/yyyy") + "\""
				+ ", \"dataPagamento\": \"" + Util.formatDate(getDataPagamento(), "dd/MM/yyyy") + "\""
				+ ", \"quantidadePagamento\": " + getQuantidadePagamento()
				+ ", \"tipoOcorrencia\": \"" + getTipoOcorrencia() + "\""
				+ ", \"origemOcorrencia\": \"" + getOrigemOcorrencia() + "\""
				+ ", \"numeroProcessamentoOcorrencia\": \"" + getNumeroProcessamentoOcorrencia() + "\"" 
				+ ", \"dataOcorrencia\": \"" + Util.formatDate(getDataOcorrencia(), "dd/MM/yyyy") + "\""
				+ ", \"dataRegistroOcorrencia\": \"" + Util.formatDate(getDataRegistroOcorrencia(), "dd/MM/yyyy") + "\""
				+ ", \"nomeInfrator\": \"" + getNomeInfrator() + "\"" 
				+ ", \"cnhInfrator\": \"" + getCnhInfrator() + "\""
				+ ", \"ufCnhInfrator\": \"" + getUfCnhInfrator() + "\"" 
				+ ", \"tipoDocumentoInfrator\": \"" + getTipoDocumentoInfrator() + "\""
				+ ", \"numeroDocumentoInfrator\": \"" + getNumeroDocumentoInfrator() + "\"" 
				+ ", \"dataApresentacao\": \"" + Util.formatDate(getDataApresentacao(), "dd/MM/yyyy") + "\""
				+ ", \"statusPontuacao\": \"" + getStatusPontuacao() + "\"" 
				+ ", \"dataAceiteUf\": \"" + Util.formatDate(getDataAceiteUf(), "dd/MM/yyyy") + "\"}";
	}

	@Override
	public void importData(NodeList nodeList, String xml) {

		setXml(xml);
		
		for( int i = 0; i  < nodeList.getLength(); i++){
	       	 Node node = nodeList.item(i);
	       	 
	       	 try {
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

		       	 	case "ait":
		       	 		setAit(node.getTextContent());
		       	 	break;

		       	 	case "codigo_infracao_editado":
		       	 		setCodigoInfracaoEditado(node.getTextContent());
		       	 	break;

		       	 	case "tipo_ait":
		       	 		setTipoAit(node.getTextContent());
		       	 	break;

		       	 	case "placa":
		       	 		setPlaca(node.getTextContent());
		       	 	break;

		       	 	case "exigivel":
		       	 		setExigivel(node.getTextContent());
		       	 	break;

		       	 	case "data_cadastro_ait":
		       	 		setDataCadastroAit(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;
		       	 	
		       	 	case "indicador_assinatura":
		       	 		setIndicadorAssinatura(node.getTextContent());
		       	 	break;

		       	 	case "cnh_condutor":
		       	 		setCnhCondutor(node.getTextContent());
		       	 	break;

		       	 	case "uf_cnh_condutor":
		       	 		setUfCnhCondutor(node.getTextContent());
		       	 	break;

		       	 	case "data_ait":
		       	 		setDataAit(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "hora_ait":
		       	 		setHoraAit(Util.convStringSemFormatacaoReverseSToGregorianCalendarHora(node.getTextContent()));
		       	 	break;

		       	 	case "local_ait":
		       	 		setLocalAit(node.getTextContent());
		       	 	break;

		       	 	case "municipio_ait":
		       	 		setMunicipioAit(node.getTextContent());
		       	 	break;

		       	 	case "medicao_real":
		       	 		setMedicaoReal(toDouble(node.getTextContent()));
		       	 	break;

		       	 	case "medicao_considerada":
		       	 		setMedicaoConsiderada(toDouble(node.getTextContent()));
		       	 	break;

		       	 	case "limite":
		       	 		setLimite(toDouble(node.getTextContent()));
		       	 	break;

		       	 	case "unidade_medida":
		       	 		setUnidadeMedida(Integer.parseInt(node.getTextContent()));
		       	 	break;

		       	 	case "valor_infracao":
		       	 		setValorInfracao(toDouble(node.getTextContent()));
		       	 	break;

		       	 	case "municipio_emplacamento":
		       	 		setMunicipioEmplacamento(node.getTextContent());
		       	 	break;

		       	 	case "uf_emplacamento":
		       	 		setUfEmplacamento(node.getTextContent());
		       	 	break;

		       	 	case "codigo_renavam":
		       	 		setCodigoRenavam(node.getTextContent());
		       	 	break;

		       	 	case "tipo_veiculo":
		       	 		setTipoVeiculo(node.getTextContent());
		       	 	break;

		       	 	case "marca_modelo":
		       	 		setMarcaModelo(node.getTextContent());
		       	 	break;

		       	 	case "cor":
		       	 		setCor(node.getTextContent());
		       	 	break;

		       	 	case "especie":
		       	 		setEspecie(node.getTextContent());
		       	 	break;

		       	 	case "carroceria":
		       	 		setCarroceria(node.getTextContent());
		       	 	break;

		       	 	case "categoria":
		       	 		setCategoria(node.getTextContent());
		       	 	break;

		       	 	case "nome_possuidor":
		       	 		setNomePossuidor(node.getTextContent());
		       	 	break;

		       	 	case "cpf_cnpj_possuidor":
		       	 		setCpfCnpjPossuidor(node.getTextContent());
		       	 	break;

		       	 	case "data_emissao_autuacao":
		       	 		try {
		       	 			setDataEmissaoAutuacao(Util.convStringToCalendar(node.getTextContent()));
		       	 		} catch(Exception e) {}
		       	 	break;

		       	 	case "data_limite_defesa":
		       	 		setDataLimiteDefesa(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "data_aceite_autuacao_uf":
		       	 		setDataAceiteAutuacaoUf(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "numero_notificao_penalidade":
		       	 		setNumeroNotificacaoPenalidade(node.getTextContent());
		       	 	break;

		       	 	case "data_emissao_penalidade":
		       	 		try {
		       	 			setDataEmissaoPenalidade(Util.convStringToCalendar(node.getTextContent()));
		       	 		} catch(Exception e) {}
		       	 	break;

		       	 	case "data_limite_jari":
		       	 		setDataLimiteJari(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "data_aceite_penalidade_uf":
		       	 		setDataAceitePenalidadeUf(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "valor_pago":
		       	 		setValorPago(toDouble(node.getTextContent()));
		       	 	break;

		       	 	case "uf_pagamento":
		       	 		setUfPagamento(node.getTextContent());
		       	 	break;

		       	 	case "data_registro_pagamento":
		       	 		setDataRegistroPagamento(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "data_pagamento":
		       	 		setDataPagamento(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "quantidade_pagamento":
		       	 		setQuantidadePagamento(Integer.parseInt(node.getTextContent()));
		       	 	break;

		       	 	case "tipo_ocorrencia":
		       	 		setTipoOcorrencia(node.getTextContent());
		       	 	break;

		       	 	case "origem_ocorrencia":
		       	 		setOrigemOcorrencia(node.getTextContent());
		       	 	break;

		       	 	case "numero_processamento_ocorrencia":
		       	 		setNumeroProcessamentoOcorrencia(node.getTextContent());
		       	 	break;

		       	 	case "data_ocorrencia":
		       	 		setDataOcorrencia(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "data_registro_ocorrencia":
		       	 		setDataRegistroOcorrencia(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "nome_infrator":
		       	 		setNomeInfrator(node.getTextContent());
		       	 	break;

		       	 	case "cnh_infrator":
		       	 		setCnhInfrator(node.getTextContent());
		       	 	break;

		       	 	case "uf_cnh_infrator":
		       	 		setUfCnhInfrator(node.getTextContent());
		       	 	break;

		       	 	case "tipo_documento_infrator":
		       	 		setTipoDocumentoInfrator(node.getTextContent());
		       	 	break;

		       	 	case "numero_documento_infrator":
		       	 		setNumeroDocumentoInfrator(node.getTextContent());
		       	 	break;

		       	 	case "data_apresentacao":
		       	 		setDataApresentacao(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;

		       	 	case "status_pontuacao":
		       	 		setStatusPontuacao(node.getTextContent());
		       	 	break;

		       	 	case "data_aceite_uf":
		       	 		setDataAceiteUf(Util.convStringToCalendar(node.getTextContent()));
		       	 	break;
		       	 }

	       	 } catch(NumberFormatException nfe) {}
	       	 
		}

	}
	
	private Double toDouble(String valor) {
		try {
			return Double.parseDouble( valor.substring(0, valor.length()-2) + "." + valor.substring(valor.length()-2) );			
		} catch(Exception e) {
			return 0d;
		}
	}

}
