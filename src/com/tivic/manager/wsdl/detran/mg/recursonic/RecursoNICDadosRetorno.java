package com.tivic.manager.wsdl.detran.mg.recursonic;

import java.util.GregorianCalendar;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class RecursoNICDadosRetorno extends DadosRetornoMG {
	private String numeroProcessamento;
	private String placa;
	private GregorianCalendar prazoDefesa;
	private int codigoOrgaoAutuador;
	private String ait;
	private int codigoInfracaoGeradora;
	private int codigoDesdobramentoInfracaoGeradora;
	private String placaVeiculoAutuado;
	private String codigoRenainf;
	private String ufJurisdicaoVeiculo;
	private String codigoMunicipioEmplacamento;
	private int codigoMarcaModelo;
	private String descricaoMarcaModelo;
	private String codigoRenavam;
	private int codigoTipo;
	private int codigoEspecie;
	private int codigoCarroceria;
	private int codigoCategoria;
	private int codigoCor;
	private int codigoRestricao1;
	private int codigoRestricao2;
	private int codigoRestricao3;
	private int codigoRestricao4;
	private int indicadorRestricaoRenajud;
	private int indicadorRestricaoRenavam;
	private int indicadorRouboFurtoRenavam;
	private int origemPossuidorVeiculo;
	private int tipoDocumentoPossuidor;
	private String numeroIdentificacaoPossuidor;
	private String nomePossuidor;
	private String nomeLogradouroPossuidor;
	private String numeroImovelPossuidor;
	private String complementoImovelPossuidor;
	private String bairroImovelPossuidor;
	private String codigoMunicipioPossuidor;
	private String ufImovelPossuidor;
	private String cepImovelPossuidor;
	private int modeloCnhPossuidor;
	private String numeroRegistroCnhPossuidor;
	private String ufExpedicaoCnhPossuidor;
	private int indicadorExigibilidade;
	private int sneIndicadorAdesaoVeiculo;
	private GregorianCalendar sneDataAdesaoVeiculo;
	private GregorianCalendar sneHoraAdesaoVeiculo;
	private int sneIndicadorAdesaoOrgaoAutuador;

	public RecursoNICDadosRetorno() {}
	
	public RecursoNICDadosRetorno(String numeroProcessamento, String placa, GregorianCalendar prazoDefesa, int codigoOrgaoAutuador, String ait, int codigoInfracaoGeradora, int codigoDesdobramentoInfracaoGeradora,
	 String placaVeiculoAutuado, String codigoRenainf, String ufJurisdicaoVeiculo, String codigoMunicipioEmplacamento, int codigoMarcaModelo, String descricaoMarcaModelo, String codigoRenavam, int codigoTipo,
	  int codigoEspecie, int codigoCarroceria, int codigoCategoria, int codigoCor, int codigoRestricao1, int codigoRestricao2, int codigoRestricao3, int codigoRestricao4, int indicadorRestricaoRenajud,
	  int indicadorRestricaoRenavam, int indicadorRouboFurtoRenavam, int origemPossuidorVeiculo, int tipoDocumentoPossuidor, String numeroIdentificacaoPossuidor, String nomePossuidor, String nomeLogradouroPossuidor,
	  String numeroImovelPossuidor, String complementoImovelPossuidor, String bairroImovelPossuidor, String codigoMunicipioPossuidor, String ufImovelPossuidor, String cepImovelPossuidor, int modeloCnhPossuidor,
	  String numeroRegistroCnhPossuidor, String ufExpedicaoCnhPossuidor, int indicadorExigibilidade, int sneIndicadorAdesaoVeiculo, GregorianCalendar sneDataAdesaoVeiculo,
		GregorianCalendar sneHoraAdesaoVeiculo, int sneIndicadorAdesaoOrgaoAutuador) {
		this.numeroProcessamento = numeroProcessamento;
		this.placa = placa;
		this.prazoDefesa = prazoDefesa;
		this.codigoOrgaoAutuador =codigoOrgaoAutuador ;
		this.ait = ait;
		this.codigoInfracaoGeradora = codigoInfracaoGeradora;
		this.codigoDesdobramentoInfracaoGeradora = codigoDesdobramentoInfracaoGeradora;
		this.placaVeiculoAutuado = placaVeiculoAutuado;
		this.codigoRenainf = codigoRenainf;
		this.ufJurisdicaoVeiculo = ufJurisdicaoVeiculo;
		this.codigoMunicipioEmplacamento = codigoMunicipioEmplacamento;
		this.codigoMarcaModelo = codigoMarcaModelo;
		this.descricaoMarcaModelo = descricaoMarcaModelo;
		this.codigoRenavam = codigoRenavam;
		this.codigoTipo = codigoTipo;
		this.codigoEspecie = codigoEspecie;
		this.codigoCarroceria = codigoCarroceria;
		this.codigoCategoria = codigoCategoria;
		this.codigoCor = codigoCor;
		this.codigoRestricao1 = codigoRestricao1;
		this.codigoRestricao2 = codigoRestricao2;
		this.codigoRestricao3 = codigoRestricao3;
		this.codigoRestricao4 = codigoRestricao4;
		this.indicadorRestricaoRenajud = indicadorRestricaoRenajud;
		this.indicadorRestricaoRenavam = indicadorRestricaoRenavam;
		this.indicadorRouboFurtoRenavam = indicadorRouboFurtoRenavam;
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
		this.modeloCnhPossuidor = modeloCnhPossuidor;
		this.numeroRegistroCnhPossuidor = numeroRegistroCnhPossuidor;
		this.ufExpedicaoCnhPossuidor = ufExpedicaoCnhPossuidor;
		this.indicadorExigibilidade = indicadorExigibilidade;
		this.sneIndicadorAdesaoVeiculo = sneIndicadorAdesaoVeiculo;
		this.sneDataAdesaoVeiculo = sneDataAdesaoVeiculo;
		this.sneHoraAdesaoVeiculo = sneHoraAdesaoVeiculo;
		this.sneIndicadorAdesaoOrgaoAutuador = sneIndicadorAdesaoOrgaoAutuador;
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

	public GregorianCalendar getPrazoDefesa() {
		return prazoDefesa;
	}

	public void setPrazoDefesa(GregorianCalendar prazoDefesa) {
		this.prazoDefesa = prazoDefesa;
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

	public int getCodigoInfracaoGeradora() {
		return codigoInfracaoGeradora;
	}

	public void setCodigoInfracaoGeradora(int codigoInfracaoGeradora) {
		this.codigoInfracaoGeradora = codigoInfracaoGeradora;
	}

	public int getCodigoDesdobramentoInfracaoGeradora() {
		return codigoDesdobramentoInfracaoGeradora;
	}

	public void setCodigoDesdobramentoInfracaoGeradora(int codigoDesdobramentoInfracaoGeradora) {
		this.codigoDesdobramentoInfracaoGeradora = codigoDesdobramentoInfracaoGeradora;
	}

	public String getPlacaVeiculoAutuado() {
		return placaVeiculoAutuado;
	}

	public void setPlacaVeiculoAutuado(String placaVeiculoAutuado) {
		this.placaVeiculoAutuado = placaVeiculoAutuado;
	}

	public String getCodigoRenainf() {
		return codigoRenainf;
	}

	public void setCodigoRenainf(String codigoRenainf) {
		this.codigoRenainf = codigoRenainf;
	}

	public String getUfJurisdicaoVeiculo() {
		return ufJurisdicaoVeiculo;
	}

	public void setUfJurisdicaoVeiculo(String ufJurisdicaoVeiculo) {
		this.ufJurisdicaoVeiculo = ufJurisdicaoVeiculo;
	}

	public String getCodigoMunicipioEmplacamento() {
		return codigoMunicipioEmplacamento;
	}

	public void setCodigoMunicipioEmplacamento(String codigoMunicipioEmplacamento) {
		this.codigoMunicipioEmplacamento = codigoMunicipioEmplacamento;
	}

	public int getCodigoMarcaModelo() {
		return codigoMarcaModelo;
	}

	public void setCodigoMarcaModelo(int codigoMarcaModelo) {
		this.codigoMarcaModelo = codigoMarcaModelo;
	}

	public String getDescricaoMarcaModelo() {
		return descricaoMarcaModelo;
	}

	public void setDescricaoMarcaModelo(String descricaoMarcaModelo) {
		this.descricaoMarcaModelo = descricaoMarcaModelo;
	}

	public String getCodigoRenavam() {
		return codigoRenavam;
	}

	public void setCodigoRenavam(String codigoRenavam) {
		this.codigoRenavam = codigoRenavam;
	}

	public int getCodigoTipo() {
		return codigoTipo;
	}

	public void setCodigoTipo(int codigoTipo) {
		this.codigoTipo = codigoTipo;
	}

	public int getCodigoEspecie() {
		return codigoEspecie;
	}

	public void setCodigoEspecie(int codigoEspecie) {
		this.codigoEspecie = codigoEspecie;
	}

	public int getCodigoCarroceria() {
		return codigoCarroceria;
	}

	public void setCodigoCarroceria(int codigoCarroceria) {
		this.codigoCarroceria = codigoCarroceria;
	}

	public int getCodigoCategoria() {
		return codigoCategoria;
	}

	public void setCodigoCategoria(int codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}

	public int getCodigoCor() {
		return codigoCor;
	}

	public void setCodigoCor(int codigoCor) {
		this.codigoCor = codigoCor;
	}

	public int getCodigoRestricao1() {
		return codigoRestricao1;
	}

	public void setCodigoRestricao1(int codigoRestricao1) {
		this.codigoRestricao1 = codigoRestricao1;
	}

	public int getCodigoRestricao2() {
		return codigoRestricao2;
	}

	public void setCodigoRestricao2(int codigoRestricao2) {
		this.codigoRestricao2 = codigoRestricao2;
	}

	public int getCodigoRestricao3() {
		return codigoRestricao3;
	}

	public void setCodigoRestricao3(int codigoRestricao3) {
		this.codigoRestricao3 = codigoRestricao3;
	}

	public int getCodigoRestricao4() {
		return codigoRestricao4;
	}

	public void setCodigoRestricao4(int codigoRestricao4) {
		this.codigoRestricao4 = codigoRestricao4;
	}

	public int getIndicadorRestricaoRenajud() {
		return indicadorRestricaoRenajud;
	}

	public void setIndicadorRestricaoRenajud(int indicadorRestricaoRenajud) {
		this.indicadorRestricaoRenajud = indicadorRestricaoRenajud;
	}

	public int getIndicadorRestricaoRenavam() {
		return indicadorRestricaoRenavam;
	}

	public void setIndicadorRestricaoRenavam(int indicadorRestricaoRenavam) {
		this.indicadorRestricaoRenavam = indicadorRestricaoRenavam;
	}

	public int getIndicadorRouboFurtoRenavam() {
		return indicadorRouboFurtoRenavam;
	}

	public void setIndicadorRouboFurtoRenavam(int indicadorRouboFurtoRenavam) {
		this.indicadorRouboFurtoRenavam = indicadorRouboFurtoRenavam;
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

	public String getCodigoMunicipioPossuidor() {
		return codigoMunicipioPossuidor;
	}

	public void setCodigoMunicipioPossuidor(String codigoMunicipioPossuidor) {
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

	public int getModeloCnhPossuidor() {
		return modeloCnhPossuidor;
	}

	public void setModeloCnhPossuidor(int modeloCnhPossuidor) {
		this.modeloCnhPossuidor = modeloCnhPossuidor;
	}

	public String getNumeroRegistroCnhPossuidor() {
		return numeroRegistroCnhPossuidor;
	}

	public void setNumeroRegistroCnhPossuidor(String numeroRegistroCnhPossuidor) {
		this.numeroRegistroCnhPossuidor = numeroRegistroCnhPossuidor;
	}

	public String getUfExpedicaoCnhPossuidor() {
		return ufExpedicaoCnhPossuidor;
	}

	public void setUfExpedicaoCnhPossuidor(String ufExpedicaoCnhPossuidor) {
		this.ufExpedicaoCnhPossuidor = ufExpedicaoCnhPossuidor;
	}
	
	public int getIndicadorExigibilidade() {
		return indicadorExigibilidade;
	}

	public void setIndicadorExigibilidade(int indicadorExigibilidade) {
		this.indicadorExigibilidade = indicadorExigibilidade;
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

	@Override
	public void importData(NodeList nodeList, String xml) {
		setXml(xml);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getTextContent() == null || node.getTextContent().equals(""))
				continue;

			switch (node.getNodeName()) {
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

			case "prazo_defesa":
				setPrazoDefesa(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
				break;

			case "codigo_orgao_autuador":
				setCodigoOrgaoAutuador(Integer.parseInt(node.getTextContent()));
				break;

			case "ait":
				setAit(node.getTextContent());
				break;

			case "codigo_infracao_geradora":
				setCodigoInfracaoGeradora(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_desdobramento_infracao_geradora":
				setCodigoDesdobramentoInfracaoGeradora(Integer.parseInt(node.getTextContent()));
				break;

			case "placa_veiculo_autuado":
				setPlacaVeiculoAutuado(node.getTextContent());
				break;

			case "codigo_renainf":
				setCodigoRenainf(node.getTextContent());
				break;

			case "uf_jurisdicao_veiculo":
				setUfJurisdicaoVeiculo(node.getTextContent());
				break;

			case "codigo_municipio_emplacamento":
				setCodigoMunicipioEmplacamento(node.getTextContent());
				break;

			case "codigo_marca_modelo":
				setCodigoMarcaModelo(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_renavam":
				setCodigoRenavam(node.getTextContent());
				break;

			case "codigo_tipo":
				setCodigoTipo(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_especie":
				setCodigoEspecie(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_carroceria":
				setCodigoCarroceria(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_categoria":
				setCodigoCategoria(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_cor":
				setCodigoCor(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_restricao_1":
				setCodigoRestricao1(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_restricao_2":
				setCodigoRestricao2(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_restricao_3":
				setCodigoRestricao3(Integer.parseInt(node.getTextContent()));
				break;

			case "codigo_restricao_4":
				setCodigoRestricao4(Integer.parseInt(node.getTextContent()));
				break;

			case "indicador_restricao_renajud":
				setIndicadorRestricaoRenajud(Integer.parseInt(node.getTextContent()));
				break;

			case "indicador_restricao_renavam":
				setIndicadorRestricaoRenavam(Integer.parseInt(node.getTextContent()));
				break;

			case "indicador_roubo_furto_renavam":
				setIndicadorRouboFurtoRenavam(Integer.parseInt(node.getTextContent()));
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
				setCodigoMunicipioPossuidor(node.getTextContent());
				break;

			case "uf_imovel_possuidor":
				setUfImovelPossuidor(node.getTextContent());
				break;

			case "cep_imovel_possuidor":
				setCepImovelPossuidor(node.getTextContent());
				break;

			case "modelo_cnh_possuidor":
				setModeloCnhPossuidor(Integer.parseInt(node.getTextContent()));
				break;

			case "numero_registro_cnh_possuidor":
				setNumeroRegistroCnhPossuidor(node.getTextContent());
				break;

			case "uf_expedicao_cnh_possuidor":
				setUfExpedicaoCnhPossuidor(node.getTextContent());
				break;
				
			case "indicador_exigibilidade":
				setIndicadorExigibilidade(Integer.parseInt(node.getTextContent()));
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

			case "sne_indicador_adesao_orgao_autuador":
				setSneIndicadorAdesaoOrgaoAutuador(Integer.parseInt(node.getTextContent()));
				break;

			case "mensagens":
				setMensagens(node.getChildNodes());
				break;
			}
		}
	}

	@Override
	public String toString() {
		return "{\"codigoRetorno\":" + getCodigoRetorno() + ", \"mensagemRetorno\": \"" + getMensagemRetorno() + "\""
				+ ", \"numeroProcessamento\": \"" + getNumeroProcessamento() + "\"" + ", \"placa\": \"" + getPlaca()
				+ "\"" + ", \"prazoDefesa\": \"" + Util.formatDate(getPrazoDefesa(), "yyyyMMdd") + "\""
				+ ", \"codigoOrgaoAutuador\": " + getCodigoOrgaoAutuador() + ", \"ait\": \"" + getAit() + "\""
				+ ", \"codigoInfracaoGeradora\": \"" + getCodigoInfracaoGeradora() + "\"" + ", \"codigoDesdobramentoInfracaoGeradora\": "
				+ getCodigoDesdobramentoInfracaoGeradora() + ", \"placaVeiculoAutuado\": \"" + getPlacaVeiculoAutuado() + "\""
				+ ", \"codigoRenainf\": \"" + getCodigoRenainf() + "\"" + ", \"ufJurisdicaoVeiculo\": \""
				+ getUfJurisdicaoVeiculo() + "\"" + ", \"codigoMunicipioEmplacamento\": \""
				+ getCodigoMunicipioEmplacamento() + "\"" + ", \"codigoMarcaModelo\": " + getCodigoMarcaModelo()
				+ ", \"descricaoMarcaModelo\": \"" + getDescricaoMarcaModelo() + "\"" + ", \"codigoRenavam\": \""
				+ getCodigoRenavam() + "\"" + ", \"codigoTipo\": " + getCodigoTipo() + ", \"codigoEspecie\": "
				+ getCodigoEspecie() + ", \"codigoCarroceria\": " + getCodigoCarroceria() + ", \"codigoCategoria\": "
				+ getCodigoCategoria() + ", \"codigoCor\": " + getCodigoCor() + ", \"codigoRestricao1\": "
				+ getCodigoRestricao1() + ", \"codigoRestricao2\": " + getCodigoRestricao2()
				+ ", \"codigoRestricao3\": " + getCodigoRestricao3() + ", \"codigoRestricao4\": "
				+ getCodigoRestricao4() + ", \"indicadorRestricaoRenajud\": " + getIndicadorRestricaoRenajud()
				+ ", \"indicadorRestricaoRenavam\": " + getIndicadorRestricaoRenavam()
				+ ", \"indicadorRouboFurtoRenavam\": " + getIndicadorRouboFurtoRenavam()
				+ ", \"origemPossuidorVeiculo\": " + getOrigemPossuidorVeiculo() + ", \"tipoDocumentoPossuidor\": "
				+ getTipoDocumentoPossuidor() + ", \"numeroIdentificacaoPossuidor\": \""
				+ getNumeroIdentificacaoPossuidor() + "\"" + ", \"nomePossuidor\": \"" + getNomePossuidor() + "\""
				+ ", \"nomeLogradouroPossuidor\": \"" + getNomeLogradouroPossuidor() + "\""
				+ ", \"numeroImovelPossuidor\": \"" + getNumeroImovelPossuidor() + "\""
				+ ", \"complementoImovelPossuidor\": \"" + getComplementoImovelPossuidor() + "\""
				+ ", \"bairroImovelPossuidor\": \"" + getBairroImovelPossuidor() + "\""
				+ ", \"codigoMunicipioPossuidor\": \"" + getCodigoMunicipioPossuidor() + "\""
				+ ", \"ufImovelPossuidor\": \"" + getUfImovelPossuidor() + "\"" + ", \"cepImovelPossuidor\": \""
				+ getCepImovelPossuidor() + "\"" + ", \"modeloCnhPossuidor\": " + getModeloCnhPossuidor()
				+ ", \"numeroRegistroCnhPossuidor\": \"" + getNumeroRegistroCnhPossuidor() + "\""
				+ ", \"ufExpedicaoCnhPossuidor\": \"" + getUfExpedicaoCnhPossuidor() + "\""
				+ ", \"indicadorExigibilidade\": " + getIndicadorExigibilidade() + ", \"sneIndicadorAdesaoVeiculo\": "
				+ getSneIndicadorAdesaoVeiculo() + ", \"sneDataAdesaoVeiculo\": \""
				+ Util.formatDate(getSneDataAdesaoVeiculo(), "yyyyMMdd") + "\"" + ", \"sneHoraAdesaoVeiculo\": \""
				+ Util.formatDate(getSneHoraAdesaoVeiculo(), "hhmm") + "\"" + ", \"sneIndicadorAdesaoOrgaoAutuador\": "
				+ getSneIndicadorAdesaoOrgaoAutuador() + ", \"mensagens\": " + getMensagensJson() + "}";
	}
}
