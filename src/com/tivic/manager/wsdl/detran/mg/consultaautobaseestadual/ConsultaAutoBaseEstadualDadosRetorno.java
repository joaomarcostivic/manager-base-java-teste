package com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.JsonToStringBuilder;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultaAutoBaseEstadualDadosRetorno extends DadosRetornoMG {

	private String codigoRenavam;
	private String chassi;
	private String placa;
	private String ufVeiculo;
	private String situacao;
	private int orgaoNorificacao;
	private int sequencialAit;
	private int orgao;
	private String nomeOrgao;
	private String ait;
	private String codigoAgente;
	private String descricaoMarcaModelo;
	private String cor;
	private int codigoInfracao;
	private int codigoDesdobramento;
	private String codigoRenainf;
	private String valorInfracao;
	private String descricaoInfracao;
	private String numeroProcessamento;
	private String dataAit;
	private String horaAit;
	private String localAit;
	private String nomeCondutor;
	private String cnhCondutor;
	private String ufCondutor;
	private String observacaoAit;
	private String dataLimiteDefesa;
	private String dataLimiteFici;
	private String dataLimiteRecurso;
	private String numeroArAutuacao;
	private String descricaoArAutuacao;
	private String dataEmissaoAutuacao;
	private String dataPostagemAutuacao;
	private String dataPublicacaoAutuacao;
	private String editalAutuacao;
	private String numeroArPenalidade;
	private String descricaoArPenalidade;
	private String dataEmissaoPenalidade;
	private String dataPostagemPenalidade;
	private String dataPublicacaoPenalidade;
	private String editalPenalidade;
	private String aitGeradora;

	// possuidor:
	private int origemPossuidor;
	private int tipoDocumentoCpfCnpj;
	private String numeroCpfCnpj;
	private String nome;
	private int numeroCnh;
	private String ufImovel;
	private String nomeLogradouro;
	private String numeroImovel;
	private String complementoImovel;
	private String bairroImovel;
	private int codigoMunicipio;
	private String cepImovel;
	private int modeloCnh;
	private String ufExpedicaoCnh;
	private int codigoMunicipioImovelPossuidor;

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
		       	 	case "situacao":
		       	 		setSituacao(value);
		       	 		break;
		       	 	case "orgao_notificacao":
		       	 		if(value!=null && !value.equals(""))
		       	 			setOrgaoNorificacao(Integer.parseInt(value));
		       	 		break;
		       	 	case "sequencial_ait":
		       	 		setSequencialAit(Integer.parseInt(value));
		       	 		break;
		       	 	case "orgao":
		       	 		setOrgao(Integer.parseInt(value));
		       	 		break;
		       	 	case "nome_orgao":
		       	 		setNomeOrgao(value);
		       	 		break;
		       	 	case "ait":
		       	 		setAit(value);
		       	 		break;
		       	 	case "codigo_agente":
		       	 		setCodigoAgente(value);
		       	 		break;
		       	 	case "descricao_marca_modelo":
		       	 		setDescricaoMarcaModelo(value);
		       	 		break;
		       	 	case "cor":
		       	 		setCor(value);
		       	 		break;
		       	 	case "codigo_infracao":
		       	 		setCodigoInfracao(Integer.parseInt(value));
		       	 		break;
		       	 	case "codigo_desdobramento":
		       	 	case "codigo_desdobramento_infracao":
		       	 		setCodigoDesdobramento(Integer.parseInt(value));
		       	 		break;
	       	 		case "codigo_renainf":
	       	 			setCodigoRenainf(value);
	       	 			break;
	       	 		case "valor_infracao":
	       	 			setValorInfracao(value);
	       	 			break;
	       	 		case "descricao_infracao":
	       	 			setDescricaoInfracao(value);
	       	 			break;
	       	 		case "numero_processamento":
	       	 			setNumeroProcessamento(value);
	       	 			break;
	       	 		case "data_ait":
	       	 			setDataAit(value);
	       	 			break;
	       	 		case "hora_ait":
	       	 			setHoraAit(value);
	       	 			break;
	       	 		case "local_ait":
	       	 			setLocalAit(value);
	       	 			break;
	       	 		case "nome_condutor":
	       	 			setNomeCondutor(value);
	       	 			break;
	       	 		case "cnh_condutor":
	       	 			setCnhCondutor(value);
	       	 			break;
	       	 		case "uf_condutor":
	       	 			setUfCondutor(value);
	       	 			break;
	       	 		case "observacao_ait":
	       	 			setObservacaoAit(value);
	       	 			break;
	       	 		case "data_limite_defesa":
	       	 			setDataLimiteDefesa(value);
	       	 			break;
	       	 		case "data_limite_fici":
	       	 			setDataLimiteFici(value);
	       	 			break;
	       	 		case "data_limite_jari":
	       	 			setDataLimiteRecurso(value);
	       	 			break;
	       	 		case "numero_ar_autuacao":
	       	 			setNumeroArAutuacao(value);
	       	 			break;
	       	 		case "descricao_ar_autuacao":
	       	 			setDescricaoArAutuacao(value);
	       	 			break;
	       	 		case "data_emissao_autuacao":
	       	 			setDataEmissaoAutuacao(value);
	       	 			break;
	       	 		case "data_postagem_autuacao":
	       	 			setDataPostagemAutuacao(value);
	       	 			break;
	       	 		case "data_publicacao_autuacao":
	       	 			setDataPublicacaoAutuacao(value);
	       	 			break;
	       	 		case "edital_autuacao":
	       	 			setEditalAutuacao(value);
	       	 			break;
		       	 	case "numero_ar_penalidade":
		   	 			setNumeroArPenalidade(value);
		   	 			break;
		   	 		case "descricao_ar_penalidade":
		   	 			setDescricaoArPenalidade(value);
		   	 			break;
		   	 		case "data_emissao_penalidade":
		   	 			setDataEmissaoPenalidade(value);
		   	 			break;
		   	 		case "data_postagem_penalidade":
		   	 			setDataPostagemPenalidade(value);
		   	 			break;
		   	 		case "data_publicacao_penalidade":
		   	 			setDataPublicacaoPenalidade(value);
		   	 			break;
		   	 		case "edital_penalidade":
		   	 			setEditalPenalidade(value);
		   	 			break;
		   	 		case "ait_geradora":
		   	 			setAitGeradora(value);
		   	 			break;
		   	 		case "origem-possuidor":
		   	 			setOrigemPossuidor(Integer.parseInt(value));
		   	 			break;
		   	 		case "tipo-documento-cpf-cnpj":
		   	 			setTipoDocumentoCpfCnpj(Integer.parseInt(value));
		   	 			break;
		   	 		case "numero-cpf-cnpj":
		   	 		case "numero_documento_possuidor":
		   	 			setNumeroCpfCnpj(value);
		   	 			break;
		   	 		case "nome_possuidor":
		   	 		case "nome":
		   	 			setNome(value);
		   	 			break;
		   	 		case "numero_registro_cnh_possuidor":
		   	 		case "numero-cnh":
		   	 			setNumeroCnh(Integer.parseInt(value));
		   	 			break;
		   	 		case "uf_imovel_possuidor":
		   	 		case "uf-imovel":
		   	 			setUfImovel(value);
		   	 			break;
		   	 		case "nome_imovel_possuidor":
		   	 		case "nome-logradouro":
		   	 			setNomeLogradouro(value);
		   	 			break;
		   	 		case "numero-imovel":
		   	 		case "numero_imovel_possuidor":
		   	 			setNumeroImovel(value);
		   	 			break;
		   	 		case "complemento-imovel":
		   	 		case "complemento_imovel_possuidor":
		   	 			setComplementoImovel(value);
		   	 			break;
		   	 		case "bairro_imovel_possuidor":
		   	 		case "bairro-imovel":
		   	 			setBairroImovel(value);
		   	 			break;
		   	 		case "codigo-municipio":
		   	 			setCodigoMunicipio(Integer.parseInt(value));
		   	 			break;
		   	 		case "cep_imovel_possuidor":
		   	 		case "cep-imovel":
		   	 			setCepImovel(value);
		   	 			break;
		   	 		case "modelo-cnh":
		   	 		case "modelo_cnh_possuidor":
		   	 			setModeloCnh(Integer.parseInt(value));
		   	 			break;
		   	 		case "uf-expedicao-cnh":
		   	 		case "uf_expedicao_cnh_possuidor":
		   	 			setUfExpedicaoCnh(value);
		   	 			break;	   
		   	 		case "codigo_municipio_imovel_possuidor":
		   	 			setCodigoMunicipioImovelPossuidor(Integer.parseInt(value));
		   	 			break;
		       	 }
	       	 } catch (NumberFormatException nfe) {
	       		 System.out.println(nfe.getMessage());
	       	 }
		}

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

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public int getOrgaoNorificacao() {
		return orgaoNorificacao;
	}

	public void setOrgaoNorificacao(int orgaoNorificacao) {
		this.orgaoNorificacao = orgaoNorificacao;
	}

	public int getSequencialAit() {
		return sequencialAit;
	}

	public void setSequencialAit(int sequencialAit) {
		this.sequencialAit = sequencialAit;
	}

	public int getOrgao() {
		return orgao;
	}

	public void setOrgao(int orgao) {
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

	public String getCodigoAgente() {
		return codigoAgente;
	}

	public void setCodigoAgente(String codigoAgente) {
		this.codigoAgente = codigoAgente;
	}

	public String getDescricaoMarcaModelo() {
		return descricaoMarcaModelo;
	}

	public void setDescricaoMarcaModelo(String descricaoMarcaModelo) {
		this.descricaoMarcaModelo = descricaoMarcaModelo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public int getCodigoInfracao() {
		return codigoInfracao;
	}

	public void setCodigoInfracao(int codigoInfracao) {
		this.codigoInfracao = codigoInfracao;
	}

	public int getCodigoDesdobramento() {
		return codigoDesdobramento;
	}

	public void setCodigoDesdobramento(int codigoDesdobramento) {
		this.codigoDesdobramento = codigoDesdobramento;
	}

	public String getCodigoRenainf() {
		return codigoRenainf;
	}

	public void setCodigoRenainf(String codigoRenainf) {
		this.codigoRenainf = codigoRenainf;
	}

	public String getValorInfracao() {
		return valorInfracao;
	}

	public void setValorInfracao(String valorInfracao) {
		this.valorInfracao = valorInfracao;
	}

	public String getDescricaoInfracao() {
		return descricaoInfracao;
	}

	public void setDescricaoInfracao(String descricaoInfracao) {
		this.descricaoInfracao = descricaoInfracao;
	}

	public String getNumeroProcessamento() {
		return numeroProcessamento;
	}

	public void setNumeroProcessamento(String numeroProcessamento) {
		this.numeroProcessamento = numeroProcessamento;
	}

	public String getDataAit() {
		return dataAit;
	}

	public void setDataAit(String dataAit) {
		this.dataAit = dataAit;
	}

	public String getHoraAit() {
		return horaAit;
	}

	public void setHoraAit(String horaAit) {
		this.horaAit = horaAit;
	}

	public String getLocalAit() {
		return localAit;
	}

	public void setLocalAit(String localAit) {
		this.localAit = localAit;
	}

	public String getNomeCondutor() {
		return nomeCondutor;
	}

	public void setNomeCondutor(String nomeCondutor) {
		this.nomeCondutor = nomeCondutor;
	}

	public String getCnhCondutor() {
		return cnhCondutor;
	}

	public void setCnhCondutor(String cnhCondutor) {
		this.cnhCondutor = cnhCondutor;
	}

	public String getUfCondutor() {
		return ufCondutor;
	}

	public void setUfCondutor(String ufCondutor) {
		this.ufCondutor = ufCondutor;
	}

	public String getObservacaoAit() {
		return observacaoAit;
	}

	public void setObservacaoAit(String observacaoAit) {
		this.observacaoAit = observacaoAit;
	}

	public String getDataLimiteDefesa() {
		return dataLimiteDefesa;
	}

	public void setDataLimiteDefesa(String dataLimiteDefesa) {
		this.dataLimiteDefesa = dataLimiteDefesa;
	}

	public String getDataLimiteFici() {
		return dataLimiteFici;
	}

	public void setDataLimiteFici(String dataLimiteFici) {
		this.dataLimiteFici = dataLimiteFici;
	}

	public String getDataLimiteRecurso() {
		return dataLimiteRecurso;
	}

	public void setDataLimiteRecurso(String dataLimiteRecurso) {
		this.dataLimiteRecurso = dataLimiteRecurso;
	}

	public String getNumeroArAutuacao() {
		return numeroArAutuacao;
	}

	public void setNumeroArAutuacao(String numeroArAutuacao) {
		this.numeroArAutuacao = numeroArAutuacao;
	}

	public String getDescricaoArAutuacao() {
		return descricaoArAutuacao;
	}

	public void setDescricaoArAutuacao(String descricaoArAutuacao) {
		this.descricaoArAutuacao = descricaoArAutuacao;
	}

	public String getDataEmissaoAutuacao() {
		return dataEmissaoAutuacao;
	}

	public void setDataEmissaoAutuacao(String dataEmissaoAutuacao) {
		this.dataEmissaoAutuacao = dataEmissaoAutuacao;
	}

	public String getDataPostagemAutuacao() {
		return dataPostagemAutuacao;
	}

	public void setDataPostagemAutuacao(String dataPostagemAutuacao) {
		this.dataPostagemAutuacao = dataPostagemAutuacao;
	}

	public String getDataPublicacaoAutuacao() {
		return dataPublicacaoAutuacao;
	}

	public void setDataPublicacaoAutuacao(String dataPublicacaoAutuacao) {
		this.dataPublicacaoAutuacao = dataPublicacaoAutuacao;
	}

	public String getEditalAutuacao() {
		return editalAutuacao;
	}

	public void setEditalAutuacao(String editalAutuacao) {
		this.editalAutuacao = editalAutuacao;
	}

	public String getNumeroArPenalidade() {
		return numeroArPenalidade;
	}

	public void setNumeroArPenalidade(String numeroArPenalidade) {
		this.numeroArPenalidade = numeroArPenalidade;
	}

	public String getDescricaoArPenalidade() {
		return descricaoArPenalidade;
	}

	public void setDescricaoArPenalidade(String descricaoArPenalidade) {
		this.descricaoArPenalidade = descricaoArPenalidade;
	}

	public String getDataEmissaoPenalidade() {
		return dataEmissaoPenalidade;
	}

	public void setDataEmissaoPenalidade(String dataEmissaoPenalidade) {
		this.dataEmissaoPenalidade = dataEmissaoPenalidade;
	}

	public String getDataPostagemPenalidade() {
		return dataPostagemPenalidade;
	}

	public void setDataPostagemPenalidade(String dataPostagemPenalidade) {
		this.dataPostagemPenalidade = dataPostagemPenalidade;
	}

	public String getDataPublicacaoPenalidade() {
		return dataPublicacaoPenalidade;
	}

	public void setDataPublicacaoPenalidade(String dataPublicacaoPenalidade) {
		this.dataPublicacaoPenalidade = dataPublicacaoPenalidade;
	}

	public String getEditalPenalidade() {
		return editalPenalidade;
	}

	public void setEditalPenalidade(String editalPenalidade) {
		this.editalPenalidade = editalPenalidade;
	}
	
	public String getAitGeradora() {
		return aitGeradora;
	}
	
	public void setAitGeradora(String aitGeradora) {
		this.aitGeradora = aitGeradora;
	}

	public int getOrigemPossuidor() {
		return origemPossuidor;
	}

	public void setOrigemPossuidor(int origemPossuidor) {
		this.origemPossuidor = origemPossuidor;
	}

	public int getTipoDocumentoCpfCnpj() {
		return tipoDocumentoCpfCnpj;
	}

	public void setTipoDocumentoCpfCnpj(int tipoDocumentoCpfCnpj) {
		this.tipoDocumentoCpfCnpj = tipoDocumentoCpfCnpj;
	}

	public String getNumeroCpfCnpj() {
		return numeroCpfCnpj;
	}

	public void setNumeroCpfCnpj(String numeroCpfCnpj) {
		this.numeroCpfCnpj = numeroCpfCnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getNumeroCnh() {
		return numeroCnh;
	}

	public void setNumeroCnh(int numeroCnh) {
		this.numeroCnh = numeroCnh;
	}

	public String getUfImovel() {
		return ufImovel;
	}

	public void setUfImovel(String ufImovel) {
		this.ufImovel = ufImovel;
	}

	public String getNomeLogradouro() {
		return nomeLogradouro;
	}

	public void setNomeLogradouro(String nomeLogradouro) {
		this.nomeLogradouro = nomeLogradouro;
	}

	public String getNumeroImovel() {
		return numeroImovel;
	}

	public void setNumeroImovel(String numeroImovel) {
		this.numeroImovel = numeroImovel;
	}

	public String getComplementoImovel() {
		return complementoImovel;
	}

	public void setComplementoImovel(String complementoImovel) {
		this.complementoImovel = complementoImovel;
	}

	public String getBairroImovel() {
		return bairroImovel;
	}

	public void setBairroImovel(String bairroImovel) {
		this.bairroImovel = bairroImovel;
	}

	public int getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(int codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public String getCepImovel() {
		return cepImovel;
	}

	public void setCepImovel(String cepImovel) {
		this.cepImovel = cepImovel;
	}

	public int getModeloCnh() {
		return modeloCnh;
	}

	public void setModeloCnh(int modeloCnh) {
		this.modeloCnh = modeloCnh;
	}

	public String getUfExpedicaoCnh() {
		return ufExpedicaoCnh;
	}

	public void setUfExpedicaoCnh(String ufExpedicaoCnh) {
		this.ufExpedicaoCnh = ufExpedicaoCnh;
	}

	public int getCodigoMunicipioImovelPossuidor() {
		return codigoMunicipioImovelPossuidor;
	}

	public void setCodigoMunicipioImovelPossuidor(int codigoMunicipioImovelPossuidor) {
		this.codigoMunicipioImovelPossuidor = codigoMunicipioImovelPossuidor;
	}

	@Override
	public String toString() {
		JsonToStringBuilder builder = new JsonToStringBuilder(this);
		builder.append("codigoRenavam", codigoRenavam);
		builder.append("chassi", chassi);
		builder.append("placa", placa);
		builder.append("ufVeiculo", ufVeiculo);
		builder.append("situacao", situacao);
		builder.append("orgaoNorificacao", orgaoNorificacao);
		builder.append("sequencialAit", sequencialAit);
		builder.append("orgao", orgao);
		builder.append("nomeOrgao", nomeOrgao);
		builder.append("ait", ait);
		builder.append("codigoAgente", codigoAgente);
		builder.append("descricaoMarcaModelo", descricaoMarcaModelo);
		builder.append("cor", cor);
		builder.append("codigoInfracao", codigoInfracao);
		builder.append("codigoDesdobramento", codigoDesdobramento);
		builder.append("codigoRenainf", codigoRenainf);
		builder.append("valorInfracao", valorInfracao);
		builder.append("descricaoInfracao", descricaoInfracao);
		builder.append("numeroProcessamento", numeroProcessamento);
		builder.append("dataAit", dataAit);
		builder.append("horaAit", horaAit);
		builder.append("localAit", localAit);
		builder.append("nomeCondutor", nomeCondutor);
		builder.append("cnhCondutor", cnhCondutor);
		builder.append("ufCondutor", ufCondutor);
		builder.append("observacaoAit", observacaoAit);
		builder.append("dataLimiteDefesa", dataLimiteDefesa);
		builder.append("dataLimiteFici", dataLimiteFici);
		builder.append("dataLimiteRecurso", dataLimiteRecurso);
		builder.append("numeroArAutuacao", numeroArAutuacao);
		builder.append("descricaoArAutuacao", descricaoArAutuacao);
		builder.append("dataEmissaoAutuacao", dataEmissaoAutuacao);
		builder.append("dataPostagemAutuacao", dataPostagemAutuacao);
		builder.append("dataPublicacaoAutuacao", dataPublicacaoAutuacao);
		builder.append("editalAutuacao", editalAutuacao);
		builder.append("numeroArPenalidade", numeroArPenalidade);
		builder.append("descricaoArPenalidade", descricaoArPenalidade);
		builder.append("dataEmissaoPenalidade", dataEmissaoPenalidade);
		builder.append("dataPostagemPenalidade", dataPostagemPenalidade);
		builder.append("dataPublicacaoPenalidade", dataPublicacaoPenalidade);
		builder.append("editalPenalidade", editalPenalidade);
		builder.append("aitGeradora", aitGeradora);
		builder.append("origemPossuidor", origemPossuidor);
		builder.append("tipoDocumentoCpfCnpj", tipoDocumentoCpfCnpj);
		builder.append("numeroCpfCnpj", numeroCpfCnpj);
		builder.append("nome", nome);
		builder.append("numeroCnh", numeroCnh);
		builder.append("ufImovel", ufImovel);
		builder.append("nomeLogradouro", nomeLogradouro);
		builder.append("numeroImovel", numeroImovel);
		builder.append("complementoImovel", complementoImovel);
		builder.append("bairroImovel", bairroImovel);
		builder.append("codigoMunicipio", codigoMunicipio);
		builder.append("cepImovel", cepImovel);
		builder.append("modeloCnh", modeloCnh);
		builder.append("ufExpedicaoCnh", ufExpedicaoCnh);
		builder.append("codigoMunicipioImovelPossuidor", codigoMunicipioImovelPossuidor);
		return builder.toString();
	}
}
