package com.tivic.manager.mob;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class ArquivoMovimentoDTO extends ArquivoMovimento {

	private Ait ait;
	private AitMovimento movimento;
	private ErroRetorno erro;

	public Ait getAit() {
		return ait;
	}

	public void setAit(Ait ait) {
		this.ait = ait;
	}

	public AitMovimento getMovimento() {
		return movimento;
	}

	public void setMovimento(AitMovimento movimento) {
		this.movimento = movimento;
	}

	public ErroRetorno getErro() {
		return erro;
	}

	public void setErro(ErroRetorno erro) {
		this.erro = erro;
	}

	public static class ListBuilder {

		private ResultSetMapper<ArquivoMovimentoDTO> arquivosMovimento;
		private ResultSetMapper<Ait> aits;
		private ResultSetMapper<AitMovimento> movimentos;
		private ResultSetMapper<ErroRetorno> erros;

		public ListBuilder(ResultSetMap rsm) {
			try {
				this.arquivosMovimento = new ResultSetMapper<ArquivoMovimentoDTO>(rsm, ArquivoMovimentoDTO.class);
				setAit(rsm);
				setMovimento(rsm);
				setErro(rsm);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public ListBuilder setAit(ResultSetMap rsm) {
			try {
				this.aits = new ResultSetMapper<Ait>(rsm, Ait.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public ListBuilder setMovimento(ResultSetMap rsm) {
			try {
				this.movimentos = new ResultSetMapper<AitMovimento>(rsm, AitMovimento.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public ListBuilder setErro(ResultSetMap rsm) {
			try {
				this.erros = new ResultSetMapper<ErroRetorno>(rsm, ErroRetorno.class);
				return this;
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}

		public List<ArquivoMovimentoDTO> build() throws IllegalArgumentException, Exception {

			List<ArquivoMovimentoDTO> arquivosMovimento = this.arquivosMovimento.toList();

			List<Ait> aits = this.aits.toList();
			List<AitMovimento> movimentos = this.movimentos.toList();
			List<ErroRetorno> erros = this.erros.toList();

			for (int i = 0; i < aits.size(); i++) {
				ArquivoMovimentoDTO dto = arquivosMovimento.get(i);

				if (i < aits.size() && dto.getCdAit() > 0)
					dto.setAit(aits.get(i));

				if (i < movimentos.size() && dto.getCdMovimento() > 0)
					dto.setMovimento(movimentos.get(i));
				
				if(i < erros.size() && dto.getNrErro() != null)
					dto.setErro(erros.get(i));

			}

			return arquivosMovimento;
		}
	}

	public static class Builder {

		private ObjectMapper mapper;
		private ArquivoMovimentoDTO dto;

		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(),
						ArquivoMovimentoDTO.class);

				// TODO: imagens

			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(int cdArquivoMovimento, int cdMovimento, int cdAit, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				ArquivoMovimento arquivoMovimento = ArquivoMovimentoDAO.get(cdArquivoMovimento, cdMovimento, cdAit);
				dto = objectMapper.readValue(arquivoMovimento.toString(), ArquivoMovimentoDTO.class);

				if (cascade) {

					if (arquivoMovimento.getCdAit() > 0)
						setAit(arquivoMovimento.getCdAit(), cascade);

					if (arquivoMovimento.getCdMovimento() > 0)
						setMovimento(arquivoMovimento.getCdMovimento(), arquivoMovimento.getCdAit(), cascade);
					
					if (arquivoMovimento.getNrErro() != null)
						setErro(arquivoMovimento.getNrErro(), cascade);

				}

			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder setAit(Map<String, Object> map) {
			try {
				dto.setAit(mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), Ait.class));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setAit(Ait ait) {
			try {
				dto.setAit(ait);
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setAit(int cdAit, boolean cascade) {
			try {
				Ait ait = AitDAO.get(cdAit);
				dto.setAit(ait);
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setMovimento(Map<String, Object> map) {
			try {
				dto.setMovimento(
						mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), AitMovimento.class));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setMovimento(AitMovimento movimento) {
			try {
				dto.setMovimento(movimento);
				;
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setMovimento(int cdMovimento, int cdAit, boolean cascade) {
			try {
				AitMovimento movimento = AitMovimentoDAO.get(cdMovimento, cdAit);
				dto.setMovimento(movimento);
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setErro(Map<String, Object> map) {
			try {
				dto.setErro(
						mapper.readValue(Util.map2Json((HashMap<String, Object>) map).toString(), ErroRetorno.class));
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public Builder setErro(ErroRetorno erro) {
			try {
				dto.setErro(erro);
				;
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setErro(String nrErro, boolean cascade) {
			try {
				while (nrErro.length() < 4) 
					nrErro = "0" + nrErro;
				
				ErroRetorno erroRetorno = ErroRetornoDAO.get(nrErro);
				dto.setErro(erroRetorno);
				return this;
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}

		public ArquivoMovimentoDTO build() {
			return dto;
		}

	}

}
