package com.tivic.manager.fta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.CidadeServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.util.Util;

public class VeiculoDTO extends Veiculo implements Serializable {

	private static final long serialVersionUID = 5791462914565131854L;
	
	private MarcaModelo modelo;
	private Cidade cidade;
	private TipoVeiculo tipoVeiculo;
	private Cor cor;
	private CategoriaVeiculo categoriaVeiculo;
	private MarcaModelo marca;
	private EspecieVeiculo especieVeiculo;
	private Pessoa proprietario;

	public MarcaModelo getModelo() {
		return modelo;
	}

	public void setModelo(MarcaModelo modelo) {
		this.modelo = modelo;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public TipoVeiculo getTipoVeiculo() {
		return tipoVeiculo;
	}

	public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
		this.tipoVeiculo = tipoVeiculo;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor cor) {
		this.cor = cor;
	}

	public CategoriaVeiculo getCategoriaVeiculo() {
		return categoriaVeiculo;
	}

	public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
	}

	public MarcaModelo getMarca() {
		return marca;
	}

	public void setMarca(MarcaModelo marca) {
		this.marca = marca;
	}

	public EspecieVeiculo getEspecieVeiculo() {
		return especieVeiculo;
	}

	public void setEspecieVeiculo(EspecieVeiculo especieVeiculo) {
		this.especieVeiculo = especieVeiculo;
	}

	public Pessoa getProprietario() {
		return proprietario;
	}

	public void setProprietario(Pessoa proprietario) {
		this.proprietario = proprietario;
	}

	public static class Builder {

		private ObjectMapper mapper;
		private VeiculoDTO dto;

		public Builder() {}

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				map.remove("QT_CAPACIDADE");
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), VeiculoDTO.class);
				
				this.setMarca(Util.isStrBaseAntiga() ? this.dto.getCdModelo() : this.dto.getCdMarca());
				this.setCidade(this.dto.getCdCidade());
				this.setTipoVeiculo(this.dto.getCdTipoVeiculo());
				this.setCor(this.dto.getCdCor());
				this.setCategoriaVeiculo(this.dto.getCdCategoria());
				this.setEspecieVeiculo(this.dto.getCdEspecie());
				this.setProprietario(this.dto.getCdProprietario());
								
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(Veiculo veiculo) {
			try {
				this.dto = (VeiculoDTO) veiculo;
				
				this.setMarca(this.dto.getCdMarca());
				this.setCidade(this.dto.getCdCidade());
				this.setTipoVeiculo(this.dto.getCdTipoVeiculo());
				this.setCor(this.dto.getCdCor());
				this.setCategoriaVeiculo(this.dto.getCdCategoria());
				this.setEspecieVeiculo(this.dto.getCdEspecie());
				this.setProprietario(this.dto.getCdProprietario());
								
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		/* **************** */
		/* Busca por id */
		/* **************** */
		
		public void setMarca(int cdMarca) {
			this.dto.setModelo(MarcaModeloServices.get(cdMarca));
		}

		public void setCidade(int cdCidade) {
			this.dto.setCidade(CidadeDAO.get(cdCidade));
		}

		public void setTipoVeiculo(int cdTipoVeiculo) {
			this.dto.setTipoVeiculo(TipoVeiculoServices.get(cdTipoVeiculo));
		}

		public void setCor(int cdCor) {
			this.dto.setCor(CorServices.get(cdCor));
		}

		public void setCategoriaVeiculo(int cdCategoria) {
			this.dto.setCategoriaVeiculo(CategoriaVeiculoServices.get(cdCategoria));
		}

		public void setEspecieVeiculo(int cdEspecieVeiculo) {
			this.dto.setEspecieVeiculo(EspecieVeiculoServices.get(cdEspecieVeiculo));
		}

		public void setProprietario(int cdProprietario) {
			this.dto.setProprietario(PessoaDAO.get(cdProprietario));
		}
		
		/* **************** */
		/* Busca por string */
		/* **************** */
		
		public void setMarca(String nmMarca) {
			this.dto.setModelo(MarcaModeloServices.getByNome(nmMarca));
		}

		public void setCidade(String nmMunicipio) {
			this.dto.setCidade(CidadeServices.getByNmCidade(nmMunicipio));
		}

		public void setTipoVeiculo(String nmTipoVeiculo) {
			this.dto.setTipoVeiculo(TipoVeiculoServices.getByNome(nmTipoVeiculo));
		}

		public void setCor(String nmCor) {
			this.dto.setCor(CorServices.getByNome(nmCor));
		}

		public void setCategoriaVeiculo(String nmCategoria) {
			this.dto.setCategoriaVeiculo(CategoriaVeiculoServices.getByNome(nmCategoria));
		}

		public void setEspecieVeiculo(String nmEspecieVeiculo) {
			this.dto.setEspecieVeiculo(EspecieVeiculoServices.getByNome(nmEspecieVeiculo));
		}
		
		public VeiculoDTO build() {
			return dto;
		}
	}

}
