package com.tivic.manager.mob;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.util.Util;

public class AitTransporteBuilder {

	private ObjectMapper mapper;
	private AitTransporteDTO dto;

	public AitTransporteBuilder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), AitTransporteDTO.class);

				// TODO: imagens

			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

	public AitTransporteBuilder(int cdAit, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				AitTransporte aitTransporte = AitTransporteDAO.get(cdAit);
				dto = objectMapper.readValue(aitTransporte.toString(), AitTransporteDTO.class);

				if (cascade) {

					if (aitTransporte.getCdInfracao() > 0)
						setInfracaoTransporte(aitTransporte.getCdInfracao(), cascade);

					if (aitTransporte.getCdEquipamento() > 0)
						setEquipamento(aitTransporte.getCdEquipamento(), cascade);

					if (aitTransporte.getCdAgente() > 0)
						setAgente(aitTransporte.getCdAgente(), cascade);

					if (aitTransporte.getCdTalao() > 0)
						setTalonario(aitTransporte.getCdTalao(), cascade);
				}

			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

	public AitTransporteBuilder setInfracaoTransporte(Map<String, Object> map) {
		try {
			dto.setInfracaoTransporte(
					mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), InfracaoTransporte.class));
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setInfracaoTransporte(InfracaoTransporte infracaoTransporte) {
		try {
			dto.setInfracaoTransporte(infracaoTransporte);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setInfracaoTransporte(int cdInfracao, boolean cascade) {
		try {
			InfracaoTransporte infracaoTransporte = InfracaoTransporteDAO.get(cdInfracao);
			dto.setInfracaoTransporte(infracaoTransporte);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setEquipamento(Map<String, Object> map) {
		try {
			dto.setEquipamento(
					mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Equipamento.class));
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setEquipamento(Equipamento equipamento) {
		try {
			dto.setEquipamento(equipamento);
			;
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setEquipamento(int cdEquipamento, boolean cascade) {
		try {
			com.tivic.manager.grl.Equipamento equipamento = com.tivic.manager.grl.EquipamentoDAO.get(cdEquipamento);
			dto.setEquipamento(equipamento);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setUsuario(Map<String, Object> map) {
		try {
			dto.setUsuario(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Usuario.class));
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setAgente(Map<String, Object> map) {
		try {
			dto.setAgente(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Agente.class));
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setAgente(Agente agente) {
		try {
			dto.setAgente(agente);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setAgente(int cdAgente, boolean cascade) {
		try {
			Agente agente = AgenteDAO.get(cdAgente);
			dto.setAgente(agente);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setConcessionario(Pessoa concessionario) {
		try {
			dto.setConcessionario(concessionario);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setConcessionario(int cdPessoa, boolean cascade) {
		try {
			Pessoa concessionario = PessoaDAO.get(cdPessoa);
			dto.setConcessionario(concessionario);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setConcessaoVeiculo(ConcessaoVeiculo concessaoVeiculo) {
		try {
			dto.setConcessaoVeiculo(concessaoVeiculo);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setConcessaoVeiculo(int cdConcessaoVeiculo, boolean cascade) {
		try {
			ConcessaoVeiculo concessaoVeiculo = ConcessaoVeiculoDAO.get(cdConcessaoVeiculo);
			dto.setConcessaoVeiculo(concessaoVeiculo);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setTalonario(Map<String, Object> map) {
		try {
			dto.setTalonario(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Talonario.class));
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setTalonario(Talonario talonario) {
		try {
			dto.setTalonario(talonario);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteBuilder setTalonario(int cdTalonario, boolean cascade) {
		try {
			Talonario talonario = TalonarioDAO.get(cdTalonario);
			dto.setTalonario(talonario);
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public AitTransporteDTO build() {
		return dto;
	}

}
