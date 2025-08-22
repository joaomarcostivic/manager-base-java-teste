package com.tivic.manager.wsdl.detran.mg.consultarrecursos;

import java.util.GregorianCalendar;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultarRecursosDadosRetorno extends DadosRetornoMG  {
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
	private GregorianCalendar dataPublicacaoRecurso;
	private String numeroProcessoDefesa;
	private GregorianCalendar dataDefesaTransito;
	private GregorianCalendar dataEntradaDefesa;
	private GregorianCalendar dataEncerramentoDefesa;
	private String decisaoDefesa;
	private String numeroProcessoAdvertencia;
	private GregorianCalendar dataAdvertenciaTransito;
	private GregorianCalendar dataEntradaAdvertencia;
	private GregorianCalendar dataEncerramentoAdvertencia;
	private String parecerFinalAdvertencia;
	private String numeroProcessoRecurso;
	private GregorianCalendar dataRecursoTransito;
	private GregorianCalendar dataEntradaRecurso;
	private GregorianCalendar dataEncerramentoRecurso;
	private String decisaoRecurso;
	private String numeroProcessoCetran;
	private GregorianCalendar dataCetranTransito;
	private GregorianCalendar dataEntradaCetran;
	private GregorianCalendar dataEncerramentoCetran;
	private String decisaoCetran;
	
	public ConsultarRecursosDadosRetorno() {
		// TODO Auto-generated constructor stub
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
		       	 	case "data_publicacao_recurso":
		       	 		setDataPublicacaoRecurso(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "numero_processo_defesa":
		       	 		setNumeroProcessoDefesa(value);
		       	 		break;
		       	 	case "data_defesa_transito":
		       	 		setDataDefesaTransito(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_entrada_defesa":
		       	 		setDataEntradaDefesa(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_encerramento_defesa":
		       	 		setDataEncerramentoDefesa(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "decisao_defesa":
		       	 		setDecisaoDefesa(value);
		       	 		break;
		       	 	case "numero_processo_advertencia":
		       	 		setNumeroProcessoAdvertencia(value);
		       	 		break;
		       	 	case "data_advertencia_transito":
		       	 		setDataAdvertenciaTransito(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_entrada_advertencia":
		       	 		setDataEntradaAdvertencia(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_encerramento_advertencia":
		       	 		setDataEncerramentoAdvertencia(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "parecer_final_advertencia":
		       	 		setParecerFinalAdvertencia(value);
		       	 		break;
		       	 	case "numero_processo_recurso":
		       	 		setNumeroProcessoRecurso(value);
		       	 		break;
		       	 	case "data_recurso_transito":
		       	 		setDataRecursoTransito(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_entrada_recurso":
		       	 		setDataEntradaRecurso(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_encerramento_recurso":
		       	 		setDataEncerramentoRecurso(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "decisao_recurso":
		       	 		setDecisaoRecurso(value);
		       	 		break;
		       	 	case "numero_processo_cetran":
		       	 		setNumeroProcessoCetran(value);
		       	 		break;
		       	 	case "data_cetran_transito":
		       	 		setDataCetranTransito(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_entrada_cetran":
		       	 		setDataEntradaCetran(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "data_encerramento_cetran":
		       	 		setDataEncerramentoCetran(Util.convStringToCalendar(value));
		       	 		break;
		       	 	case "decisao_cetran":
		       	 		setDecisaoCetran(value);
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

	public GregorianCalendar getDataPublicacaoRecurso() {
		return dataPublicacaoRecurso;
	}

	public void setDataPublicacaoRecurso(GregorianCalendar dataPublicacaoRecurso) {
		this.dataPublicacaoRecurso = dataPublicacaoRecurso;
	}

	public String getNumeroProcessoDefesa() {
		return numeroProcessoDefesa;
	}

	public void setNumeroProcessoDefesa(String numeroProcessoDefesa) {
		this.numeroProcessoDefesa = numeroProcessoDefesa;
	}

	public GregorianCalendar getDataDefesaTransito() {
		return dataDefesaTransito;
	}

	public void setDataDefesaTransito(GregorianCalendar dataDefesaTransito) {
		this.dataDefesaTransito = dataDefesaTransito;
	}

	public GregorianCalendar getDataEntradaDefesa() {
		return dataEntradaDefesa;
	}

	public void setDataEntradaDefesa(GregorianCalendar dataEntradaDefesa) {
		this.dataEntradaDefesa = dataEntradaDefesa;
	}

	public GregorianCalendar getDataEncerramentoDefesa() {
		return dataEncerramentoDefesa;
	}

	public void setDataEncerramentoDefesa(GregorianCalendar dataEncerramentoDefesa) {
		this.dataEncerramentoDefesa = dataEncerramentoDefesa;
	}

	public String getDecisaoDefesa() {
		return decisaoDefesa;
	}

	public void setDecisaoDefesa(String decisaoDefesa) {
		this.decisaoDefesa = decisaoDefesa;
	}

	public String getNumeroProcessoAdvertencia() {
		return numeroProcessoAdvertencia;
	}

	public void setNumeroProcessoAdvertencia(String numeroProcessoAdvertencia) {
		this.numeroProcessoAdvertencia = numeroProcessoAdvertencia;
	}

	public GregorianCalendar getDataAdvertenciaTransito() {
		return dataAdvertenciaTransito;
	}

	public void setDataAdvertenciaTransito(GregorianCalendar dataAdvertenciaTransito) {
		this.dataAdvertenciaTransito = dataAdvertenciaTransito;
	}

	public GregorianCalendar getDataEntradaAdvertencia() {
		return dataEntradaAdvertencia;
	}

	public void setDataEntradaAdvertencia(GregorianCalendar dataEntradaAdvertencia) {
		this.dataEntradaAdvertencia = dataEntradaAdvertencia;
	}

	public GregorianCalendar getDataEncerramentoAdvertencia() {
		return dataEncerramentoAdvertencia;
	}

	public void setDataEncerramentoAdvertencia(GregorianCalendar dataEncerramentoAdvertencia) {
		this.dataEncerramentoAdvertencia = dataEncerramentoAdvertencia;
	}

	public String getParecerFinalAdvertencia() {
		return parecerFinalAdvertencia;
	}

	public void setParecerFinalAdvertencia(String parecerFinalAdvertencia) {
		this.parecerFinalAdvertencia = parecerFinalAdvertencia;
	}

	public String getNumeroProcessoRecurso() {
		return numeroProcessoRecurso;
	}

	public void setNumeroProcessoRecurso(String numeroProcessoRecurso) {
		this.numeroProcessoRecurso = numeroProcessoRecurso;
	}

	public GregorianCalendar getDataRecursoTransito() {
		return dataRecursoTransito;
	}

	public void setDataRecursoTransito(GregorianCalendar dataRecursoTransito) {
		this.dataRecursoTransito = dataRecursoTransito;
	}

	public GregorianCalendar getDataEntradaRecurso() {
		return dataEntradaRecurso;
	}

	public void setDataEntradaRecurso(GregorianCalendar dataEntradaRecurso) {
		this.dataEntradaRecurso = dataEntradaRecurso;
	}

	public GregorianCalendar getDataEncerramentoRecurso() {
		return dataEncerramentoRecurso;
	}

	public void setDataEncerramentoRecurso(GregorianCalendar dataEncerramentoRecurso) {
		this.dataEncerramentoRecurso = dataEncerramentoRecurso;
	}

	public String getDecisaoRecurso() {
		return decisaoRecurso;
	}

	public void setDecisaoRecurso(String decisaoRecurso) {
		this.decisaoRecurso = decisaoRecurso;
	}

	public String getNumeroProcessoCetran() {
		return numeroProcessoCetran;
	}

	public void setNumeroProcessoCetran(String numeroProcessoCetran) {
		this.numeroProcessoCetran = numeroProcessoCetran;
	}

	public GregorianCalendar getDataCetranTransito() {
		return dataCetranTransito;
	}

	public void setDataCetranTransito(GregorianCalendar dataCetranTransito) {
		this.dataCetranTransito = dataCetranTransito;
	}

	public GregorianCalendar getDataEntradaCetran() {
		return dataEntradaCetran;
	}

	public void setDataEntradaCetran(GregorianCalendar dataEntradaCetran) {
		this.dataEntradaCetran = dataEntradaCetran;
	}

	public GregorianCalendar getDataEncerramentoCetran() {
		return dataEncerramentoCetran;
	}

	public void setDataEncerramentoCetran(GregorianCalendar dataEncerramentoCetran) {
		this.dataEncerramentoCetran = dataEncerramentoCetran;
	}

	public String getDecisaoCetran() {
		return decisaoCetran;
	}

	public void setDecisaoCetran(String decisaoCetran) {
		this.decisaoCetran = decisaoCetran;
	}
	
	
	
}
