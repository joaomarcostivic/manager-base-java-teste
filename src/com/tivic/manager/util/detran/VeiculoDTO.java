package com.tivic.manager.util.detran;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.grl.Cidade;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VeiculoDTO {
	private String cdRetorno;
	private String nrPlaca;
	private String nrChassi;
	private String nrRenavam;
	private String nmMunicipio;
	private String sgEstado;
	private String nmMarcaModelo;
	private String tpVeiculo;
	private String tpCarroceria;
	private String nmCor;
	private String nmCategoria;
	private String nmEspecie;
	private String nrAnoModelo;
	private String nrAnoFabricacao;
	private String nrAnoLicenciamento;
	private String tpCombustivel;
	private String stVeiculo;
	private List<RestricaoVeiculoDTO> restricoes;
	private String nmProprietario;
	private String nrCpfCnpj;
	private String codEspecie;
	private String codMarca;
	private String codMarcaAutuacao;
	private String codCor;
	private String codTipo;
	private String codMunicipio;
	private String vlDebIpva;
	private String vlDebLicenc;
	private String vlDebMulta;
	private String vlDebDpvat;
	private String vlDebInfTrami;
	private String cpfProprietario;
	private String numeroCep;
	private String nomeBairro;
	private String endereco;
	private String complemEndereco;
	private String numeroEndereco;
	private MarcaModelo marcaModelo;
	private Cor cor;
	private TipoVeiculo tipoVeiculo;
	private EspecieVeiculo especieVeiculo;
	private CategoriaVeiculo categoriaVeiculo;
	private Cidade cidade;
	private Veiculo veiculo;
	
	public String getCdRetorno() {
		return cdRetorno;
	}
	
	public void setCdRetorno(String cdRetorno) {
		this.cdRetorno = cdRetorno;
	}
	
	public String getNrPlaca() {
		return nrPlaca;
	}
	
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	
	public String getNrChassi() {
		return nrChassi;
	}
	
	public void setNrChassi(String nrChassi) {
		this.nrChassi = nrChassi;
	}
	
	public String getNrRenavam() {
		return nrRenavam;
	}
	
	public void setNrRenavam(String nrRenavam) {
		this.nrRenavam = nrRenavam;
	}
	
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	
	public String getSgEstado() {
		return sgEstado;
	}
	
	public void setSgEstado(String sgEstado) {
		this.sgEstado = sgEstado;
	}
	
	public String getNmMarcaModelo() {
		return nmMarcaModelo;
	}
	
	public void setNmMarcaModelo(String nmMarcaModelo) {
		this.nmMarcaModelo = nmMarcaModelo;
	}
	
	public String getTpVeiculo() {
		return tpVeiculo;
	}
	
	public void setTpVeiculo(String tpVeiculo) {
		this.tpVeiculo = tpVeiculo;
	}
	
	public String getTpCarroceria() {
		return tpCarroceria;
	}
	
	public void setTpCarroceria(String tpCarroceria) {
		this.tpCarroceria = tpCarroceria;
	}
	
	public String getNmCor() {
		return nmCor;
	}
	
	public void setNmCor(String nmCor) {
		this.nmCor = nmCor;
	}
	
	public String getNmCategoria() {
		return nmCategoria;
	}
	
	public void setNmCategoria(String nmCategoria) {
		this.nmCategoria = nmCategoria;
	}
	
	public String getNmEspecie() {
		return nmEspecie;
	}
	
	public void setNmEspecie(String nmEspecie) {
		this.nmEspecie = nmEspecie;
	}
	
	public String getNrAnoModelo() {
		return nrAnoModelo;
	}
	
	public void setNrAnoModelo(String nrAnoModelo) {
		this.nrAnoModelo = nrAnoModelo;
	}
	
	public String getNrAnoFabricacao() {
		return nrAnoFabricacao;
	}
	
	public void setNrAnoFabricacao(String nrAnoFabricacao) {
		this.nrAnoFabricacao = nrAnoFabricacao;
	}
	
	public String getNrAnoLicenciamento() {
		return nrAnoLicenciamento;
	}
	
	public void setNrAnoLicenciamento(String nrAnoLicenciamento) {
		this.nrAnoLicenciamento = nrAnoLicenciamento;
	}
	
	public String getTpCombustivel() {
		return tpCombustivel;
	}
	
	public void setTpCombustivel(String tpCombustivel) {
		this.tpCombustivel = tpCombustivel;
	}
	
	public String getStVeiculo() {
		return stVeiculo;
	}
	
	public void setStVeiculo(String stVeiculo) {
		this.stVeiculo = stVeiculo;
	}
	
	public List<RestricaoVeiculoDTO> getRestricoes() {
		return restricoes;
	}
	
	public void setRestricoes(List<RestricaoVeiculoDTO> restricoes) {
		this.restricoes = restricoes;
	}
	
	public String getNmProprietario() {
		return nmProprietario;
	}
	
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	
	public String getNrCpfCnpj() {
		return nrCpfCnpj;
	}
	
	public void setNrCpfCnpj(String nrCpfCnpj) {
		this.nrCpfCnpj = nrCpfCnpj;
	}
	
	public String getCodEspecie() {
		return codEspecie;
	}
	
	public void setCodEspecie(String codEspecie) {
		this.codEspecie = codEspecie;
	}
	
	public String getCodMarca() {
		return codMarca;
	}
	
	public void setCodMarca(String codMarca) {
		this.codMarca = codMarca;
	}
	
	public String getCodMarcaAutuacao() {
		return codMarcaAutuacao;
	}
	
	public void setCodMarcaAutuacao(String codMarcaAutuacao) {
		this.codMarcaAutuacao = codMarcaAutuacao;
	}
	
	public String getCodCor() {
		return codCor;
	}
	
	public void setCodCor(String codCor) {
		this.codCor = codCor;
	}
	
	public String getCodTipo() {
		return codTipo;
	}
	
	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}
	
	public String getCodMunicipio() {
		return codMunicipio;
	}
	
	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}
	
	public String getVlDebIpva() {
		return vlDebIpva;
	}
	
	public void setVlDebIpva(String vlDebIpva) {
		this.vlDebIpva = vlDebIpva;
	}
	
	public String getVlDebLicenc() {
		return vlDebLicenc;
	}
	
	public void setVlDebLicenc(String vlDebLicenc) {
		this.vlDebLicenc = vlDebLicenc;
	}
	
	public String getVlDebMulta() {
		return vlDebMulta;
	}
	
	public void setVlDebMulta(String vlDebMulta) {
		this.vlDebMulta = vlDebMulta;
	}
	
	public String getVlDebDpvat() {
		return vlDebDpvat;
	}
	
	public void setVlDebDpvat(String vlDebDpvat) {
		this.vlDebDpvat = vlDebDpvat;
	}
	
	public String getVlDebInfTrami() {
		return vlDebInfTrami;
	}
	
	public void setVlDebInfTrami(String vlDebInfTrami) {
		this.vlDebInfTrami = vlDebInfTrami;
	}
	
	public String getCpfProprietario() {
		return cpfProprietario;
	}
	
	public void setCpfProprietario(String cpfProprietario) {
		this.cpfProprietario = cpfProprietario;
	}
	
	public String getNumeroCep() {
		return numeroCep;
	}
	
	public void setNumeroCep(String numeroCep) {
		this.numeroCep = numeroCep;
	}
	
	public String getNomeBairro() {
		return nomeBairro;
	}
	
	public void setNomeBairro(String nomeBairro) {
		this.nomeBairro = nomeBairro;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public String getComplemEndereco() {
		return complemEndereco;
	}
	
	public void setComplemEndereco(String complemEndereco) {
		this.complemEndereco = complemEndereco;
	}
	
	public String getNumeroEndereco() {
		return numeroEndereco;
	}
	
	public void setNumeroEndereco(String numeroEndereco) {
		this.numeroEndereco = numeroEndereco;
	}
	
	public MarcaModelo getMarcaModelo() {
		return marcaModelo;
	}
	
	public void setMarcaModelo(MarcaModelo marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	
	public Cor getCor() {
		return cor;
	}
	
	public void setCor(Cor cor) {
		this.cor = cor;
	}
	
	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}
	
	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}
	
	public EspecieVeiculo getEspecieVeiculo() {
		return especieVeiculo;
	}
	
	public void setEspecieVeiculo(EspecieVeiculo especieVeiculo) {
		this.especieVeiculo = especieVeiculo;
	}
	
	public CategoriaVeiculo getCategoriaVeiculo() {
		return categoriaVeiculo;
	}
	
	public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
	}
	
	public Cidade getCidade() {
		return cidade;
	}
	
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	
	public Veiculo getVeiculo() {
		return veiculo;
	}
	
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}