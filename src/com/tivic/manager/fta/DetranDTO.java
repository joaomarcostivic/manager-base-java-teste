package com.tivic.manager.fta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeServices;
import com.tivic.manager.util.Util;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetranDTO implements Serializable {
	private static final long serialVersionUID = -5555944334008977379L;
	
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
	private ArrayList<String> restricoes;
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
	private String cnpjProprietario;
	private String numerocep;
	private String nomebairro;
	private String endereco;
	private String complemendereco;
	private String numeroendereco;

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

	public ArrayList<String> getRestricoes() {
		return restricoes;
	}

	public void setRestricoes(ArrayList<String> restricoes) {
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
	
	public String getCpfProprietario() {
		return cpfProprietario;
	}

	public void setCpfProprietario(String cpfProprietario) {
		this.cpfProprietario = cpfProprietario;
	}
	
	public String getCnpjProprietario() {
		return cnpjProprietario;
	}

	public void setCnpjProprietario(String cnpjProprietario) {
		this.cnpjProprietario = cnpjProprietario;
	}

	public String getNumerocep() {
		return numerocep;
	}

	public void setNumerocep(String numerocep) {
		this.numerocep = numerocep;
	}

	public String getNomebairro() {
		return nomebairro;
	}

	public void setNomebairro(String nomebairro) {
		this.nomebairro = nomebairro;
	}

	public String getComplemendereco() {
		return complemendereco;
	}

	public void setComplemendereco(String complemendereco) {
		this.complemendereco = complemendereco;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public String getNumeroendereco() {
		return numeroendereco;
	}

	public void setNumeroendereco(String numeroendereco) {
		this.numeroendereco = numeroendereco;
	}

	public static class Builder {
		
		private ObjectMapper mapper;
		private DetranDTO dto;
		
		public Builder(Map<String, Object> map) {
			new Builder(map, false);
		} 
		
		public Builder(Map<String, Object> map, boolean getObjs) {
			try {								
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), DetranDTO.class);
				if(getObjs) {
					MarcaModelo marcaModelo = MarcaModeloServices.getByNome(dto.nmMarcaModelo);
					if(marcaModelo!=null)
						dto.setMarcaModelo(marcaModelo);
					
					Cor cor = CorServices.getByNome(dto.nmCor);
					if(cor!=null)
						dto.setCor(cor);
					
					TipoVeiculo tipoVeiculo = TipoVeiculoServices.getByNome(dto.tpVeiculo);
					if(tipoVeiculo!=null)
						dto.setTipoVeiculo(tipoVeiculo);
					
					EspecieVeiculo especieVeiculo = EspecieVeiculoServices.getByNome(dto.nmEspecie);
					if(especieVeiculo!=null)
						dto.setEspecieVeiculo(especieVeiculo);
					
					Cidade cidade = CidadeServices.getByNmCidade(dto.nmMunicipio);
					if(cidade!=null)
						dto.setCidade(cidade);
					
					CategoriaVeiculo categoria = CategoriaVeiculoServices.getByNome(dto.nmCategoria);
					if(categoria!=null)
						dto.setCategoriaVeiculo(categoria);
						
					Veiculo veiculo = VeiculoServices.getVeiculoByPlaca(dto.nrPlaca);
					if(veiculo!=null)
						dto.setVeiculo(veiculo);
				}
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		
		public DetranDTO build() {
			return dto;
		}
		
	}

}
