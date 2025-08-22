package com.tivic.manager.mob;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.str.endereco.Endereco;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class EDatDTO extends Boat {

	Agente agente;
	ArrayList<Inconsistencia> inconsistencias;
	ArrayList<BoatOcorrenciaDTO> boatOcorrencias;
	ArrayList<Boat> relacionados;
	String imgCenarioVia;
	Endereco endereco;

	public void setInconsistencia(ArrayList<Inconsistencia> inconsistencias) {
		this.inconsistencias = inconsistencias;
	}

	public ArrayList<Inconsistencia> getInconsistencias() {
		return inconsistencias;
	}

	public void setAgente(Agente agente) {
		this.agente = agente;
	}

	public Agente getAgente() {
		return agente;
	}

	public void setBoatOcorrencias(ArrayList<BoatOcorrenciaDTO> boatOcorrencias) {
		this.boatOcorrencias = boatOcorrencias;
	}

	public ArrayList<BoatOcorrenciaDTO> getBoatOcorrencias() {
		return boatOcorrencias;
	}

	public void setRelacionados(ArrayList<Boat> boats) {
		this.relacionados = boats;
	}

	public ArrayList<Boat> getRelacionados() {
		return relacionados;
	}

	public String getImgCenarioVia() {
		return imgCenarioVia;
	}

	public void setImgCenarioVia(String imgCenarioVia) {
		this.imgCenarioVia = imgCenarioVia;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public static class Builder {

		private ObjectMapper mapper;
		private EDatDTO dto;
		private ResultSetMapper<BoatDeclarante> boatDeclarante;

		public Builder(Map<String, Object> map) {
			new Builder(map, null);
		}
		
		public Builder(Map<String, Object> map, Connection connect) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), EDatDTO.class);

				this.setDeclarante(map);
				this.setBoatDeclarante(map);
				this.setDeclaranteEndereco((int) map.get("CD_DECLARANTE"), (int) map.get("CD_CIDADE"), connect);
				this.setVeiculos((int) map.get("CD_BOAT"), false, connect);
				this.setImagens((int) map.get("CD_BOAT"), false, connect);
				this.setRelacionados((int) map.get("CD_BOAT"), connect);
				this.setInconsistencias((int) map.get("CD_BOAT"), connect);
				this.setBoatOcorrencias((int) map.get("CD_BOAT"), connect);
				this.setEnderecoBoat(map);
				System.out.println(dto.getEndereco());
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(Map<String, Object> map, boolean withBlobs, Connection connect) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), EDatDTO.class);

				this.setDeclarante(map);
				this.setBoatDeclarante(map);
				this.setDeclaranteEndereco((int) map.get("CD_DECLARANTE"), (int) map.get("CD_CIDADE"), connect);
				this.setVeiculos((int) map.get("CD_BOAT"), false, connect);
				this.setRelacionados((int) map.get("CD_BOAT"), connect);
				this.setInconsistencias((int) map.get("CD_BOAT"), connect);
				this.setBoatOcorrencias((int) map.get("CD_BOAT"), connect);
				this.setEnderecoBoat(map);
				if(withBlobs) {
					this.setImagens((int) map.get("CD_BOAT"), withBlobs, connect);
				}
				

			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder setDeclarante(Map<String, Object> map)
				throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setDeclarante(
					mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Declarante.class));
			return this;
		}

		public Builder setDeclarante(int cdDeclarante, Connection connect) {
			try {
				dto.setDeclarante(DeclaranteDAO.get(cdDeclarante, connect));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setBoatDeclarante(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setBoatDeclarante(
					mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), BoatDeclarante.class));
			return this;
		}

		public Builder setEnderecoBoat(Map<String, Object> map) throws JsonParseException, JsonMappingException, IOException, Exception {
			dto.setEndereco(
					mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Endereco.class));
			return this;
		}
		
		public Builder setDeclaranteEndereco(int cdDeclarante, int cdCidade, Connection connect) {
			boolean isConnectionNull = connect == null;

			try {
				ResultSetMap rsmEndereco = DeclaranteEnderecoServices.find(new Criterios("CD_DECLARANTE", Integer.toString(cdDeclarante), ItemComparator.EQUAL, Types.INTEGER), connect);
				
				if(rsmEndereco.size() == 0) {
					return this;
				}
				
				dto.setDeclaranteEndereco(mapper.readValue(Util.map2Json((HashMap<String, Object>) rsmEndereco.getLines().get(0)).toString(), DeclaranteEndereco.class));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			} finally {
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
		}

		public Builder setVeiculos(int cdBoat, boolean withBlobs, Connection connect) {
			boolean isConnectionNull = connect == null;

			try {
				ResultSetMap rsmVeiculos = BoatVeiculoServices.getAllVeiculosByBoat(cdBoat, connect);
				ArrayList<BoatVeiculo> list = new ArrayList<BoatVeiculo>();

				while (rsmVeiculos.next()) {
					BoatVeiculoDTO boatVeiculo = mapper.readValue(Util.map2Json(rsmVeiculos.getRegister()).toString(),
							BoatVeiculoDTO.class);

					boatVeiculo.setCategoriaVeiculo(boatVeiculo.getCdCategoria(), connect);
					boatVeiculo.setTipoVeiculo(boatVeiculo.getCdTipo(), connect);
					boatVeiculo.setEspecieVeiculo(boatVeiculo.getCdEspecie(), connect);
					boatVeiculo.setCor(boatVeiculo.getCdCor(), connect);
					boatVeiculo.setCidade(boatVeiculo.getCdCidade(), connect);
					
					if(withBlobs) {
						ResultSetMapper<BoatImagem> imagens = new ResultSetMapper<BoatImagem>(BoatImagemServices.getAllWithBytes(cdBoat, connect), BoatImagem.class);					
						boatVeiculo.setImagens(new ArrayList<BoatImagem>(imagens.toList()));
					}

					if (boatVeiculo.getCdMarca() > 0) {
						boatVeiculo.setMarca(boatVeiculo.getCdMarca(), connect);
					}

					list.add(boatVeiculo);
				}

				dto.setVeiculos(list);
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			} finally {
				if (isConnectionNull)
					Conexao.desconectar(connect);
			}
		}

		@SuppressWarnings("unchecked")
		public Builder setVeiculos(Map<String, Object> map) {
			try {
				dto.setVeiculos(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), ArrayList.class));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

//		
		public Builder setImagens(int cdBoat, boolean withBlobs, Connection connect) {
			try {
				ResultSetMap rsmImagens;
				if(withBlobs) {
					rsmImagens = BoatImagemServices.getAllWithBytes(cdBoat, connect);	
				} else {
					rsmImagens = BoatImagemServices.getAllByBoat(cdBoat, connect);
				}
				ArrayList<BoatImagem> list = new ArrayList<BoatImagem>();
				while (rsmImagens.next()) {
					list.add(mapper.readValue(Util.map2Json(rsmImagens.getRegister()).toString(), BoatImagem.class));
				}

				dto.setImagens(list);
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		public Builder setImagens(Map<String, Object> map) {
			try {
				dto.setImagens(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), ArrayList.class));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}		

		public Builder setRelacionados(int cdBoat, Connection connect) {
			try {
				ArrayList<Boat> boats = new ArrayList<Boat>();				
				ResultSetMap rsmRelacoes = BoatRelacaoServices.getBoatsRelacionados(cdBoat, connect);
				
				while(rsmRelacoes.next()) {
					boats.add(BoatDAO.get(rsmRelacoes.getInt("CD_BOAT_RELACAO"), connect));
				}
				
				dto.setRelacionados(boats);
				
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setInconsistencias(int cdBoat, Connection connect) {
			try {
				ArrayList<Inconsistencia> inconsistencias = new ArrayList<Inconsistencia>();
				
				ResultSetMap rsmInconsistencias = BoatInconsistenciaServices.find(new Criterios("cd_boat", Integer.toString(cdBoat), ItemComparator.EQUAL, Types.INTEGER), connect);
				
				while(rsmInconsistencias.next()) {
					inconsistencias.add(mapper.readValue(Util.map2Json(rsmInconsistencias.getRegister()).toString(), Inconsistencia.class));
				}
				
				this.dto.inconsistencias = inconsistencias;
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setBoatOcorrencias(int cdBoat, Connection connect) {
			try {
				ArrayList<BoatOcorrenciaDTO> ocorrencias = new ArrayList<BoatOcorrenciaDTO>();
				
				ResultSetMap rsmOcorrencias = BoatOcorrenciaServices.find(new Criterios("cd_boat", Integer.toString(cdBoat), ItemComparator.EQUAL, Types.INTEGER), connect);
				
				while(rsmOcorrencias.next()) {
					BoatOcorrenciaDTO ocorrencia = mapper.readValue(Util.map2Json(rsmOcorrencias.getRegister()).toString(), BoatOcorrenciaDTO.class);
					ocorrencia.setAgente(AgenteDAO.get(ocorrencia.getCdAgente(), connect));
					ocorrencia.setOcorrencia(OcorrenciaDAO.get(ocorrencia.getCdOcorrencia(), connect));
					ocorrencias.add(ocorrencia);
				}
				
				this.dto.boatOcorrencias = ocorrencias;
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public EDatDTO build() {
			return dto;
		}
	}

	public static class ListBuilder {

		private int total;
		private ResultSetMapper<EDatDTO> boats;
		private ResultSetMapper<Declarante> declarantes;

		public ListBuilder(ResultSetMap rsm, int total) {
			try {
				this.boats = new ResultSetMapper<EDatDTO>(rsm, EDatDTO.class);
				this.setDeclarante(rsm);
				this.total = total;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setDeclarante(ResultSetMap rsm) {
			try {
				this.declarantes = new ResultSetMapper<Declarante>(rsm, Declarante.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public PagedResponse<EDatDTO> build() throws IllegalArgumentException, Exception {
			List<EDatDTO> boats = this.boats.toList();

			int size = boats.size();
			List<Declarante> declarantes = this.declarantes.toList();

			for (int i = 0; i < size; i++) {
				EDatDTO dto = boats.get(i);

				if (i < declarantes.size())
					dto.setDeclarante(declarantes.get(i));
			}

			return new PagedResponse<EDatDTO>(boats, this.total);
		}
	}

}
