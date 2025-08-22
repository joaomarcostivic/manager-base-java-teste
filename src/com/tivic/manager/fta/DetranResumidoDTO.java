package com.tivic.manager.fta;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.Util;

public class DetranResumidoDTO implements Serializable {
	private static final long serialVersionUID = -5555944334008977379L;
	
	private String nmMarcaModelo;
	private String tpVeiculo;
	private String nmCategoria;
	private String nmEspecie;
	private String codMarca;
	private String codTipo;
	private String codEspecie;
	private String codCategoria;
	
	private MarcaModelo marcaModelo;
	private TipoVeiculo tipoVeiculo;
	private EspecieVeiculo especieVeiculo;
	private CategoriaVeiculo categoriaVeiculo;
	
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

	public String getCodTipo() {
		return codTipo;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public String getCodCategoria() {
		return codCategoria;
	}

	public void setCodCategoria(String codCategoria) {
		this.codCategoria = codCategoria;
	}

	public MarcaModelo getMarcaModelo() {
		return marcaModelo;
	}

	public void setMarcaModelo(MarcaModelo marcaModelo) {
		this.marcaModelo = marcaModelo;
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

	public static class Builder {
		
		private ObjectMapper mapper;
		private DetranResumidoDTO dto;
		
		public Builder(Map<String, Object> map) {
			new Builder(map, false);
		} 
		
		public Builder(Map<String, Object> map, boolean getObjs) {
			try {								
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), DetranResumidoDTO.class);
				
				if(getObjs) {
					MarcaModelo marcaModelo = MarcaModeloServices.getByNome(dto.nmMarcaModelo);
					if(marcaModelo!=null)
						dto.setMarcaModelo(marcaModelo);
					
					TipoVeiculo tipoVeiculo = TipoVeiculoServices.getByNome(dto.tpVeiculo);
					if(tipoVeiculo!=null)
						dto.setTipoVeiculo(tipoVeiculo);
					
					EspecieVeiculo especieVeiculo = EspecieVeiculoServices.getByNome(dto.nmEspecie);
					if(especieVeiculo!=null)
						dto.setEspecieVeiculo(especieVeiculo);
					
					
					CategoriaVeiculo categoria = CategoriaVeiculoServices.getByNome(dto.nmCategoria);
					if(categoria!=null)
						dto.setCategoriaVeiculo(categoria);
				}
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(JSONObject map, boolean getObjs) {
			try {								
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = new DetranResumidoDTO();

				if(!map.isNull("marcaModelo")) {	
					MarcaModelo marcaModelo = MarcaModeloServices.getByNome(map.getString("marcaModelo"));
					if(marcaModelo!=null) {
						dto.setMarcaModelo(getObjs ? marcaModelo : null);
						dto.setCodMarca(String.valueOf(marcaModelo.getCdMarca()));
					}	
				}
				
				if(!map.isNull("tipoVeiculo")) {
					TipoVeiculo tipoVeiculo = TipoVeiculoServices.getByNome(map.getString("tipoVeiculo"));
					if(tipoVeiculo!=null) {
						dto.setTipoVeiculo(getObjs ? tipoVeiculo : null);
						dto.setCodTipo(String.valueOf(tipoVeiculo.getCdTipoVeiculo()));
					}	
				}
				
				if(!map.isNull("especieVeiculo")) {
					EspecieVeiculo especieVeiculo = EspecieVeiculoServices.getByNome(map.getString("especieVeiculo"));
					if(especieVeiculo!=null) {
						dto.setEspecieVeiculo(getObjs ? especieVeiculo : null);
						dto.setCodEspecie(String.valueOf(especieVeiculo.getCdEspecie()));
					}
				}
				
				if(!map.isNull("categoriaVeiculo")) {					
					CategoriaVeiculo categoria = CategoriaVeiculoServices.getByNome(map.getString("categoriaVeiculo"));
					if(categoria!=null) {
						dto.setCategoriaVeiculo(getObjs ? categoria : null);
						dto.setCodCategoria(String.valueOf(categoria.getCdCategoria()));
					}	
				}
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public DetranResumidoDTO build() {
			return dto;
		}
		
	}

}
