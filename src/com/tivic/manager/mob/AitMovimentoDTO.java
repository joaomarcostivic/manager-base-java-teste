package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;

public class AitMovimentoDTO extends AitMovimento implements Serializable {
	private static final long serialVersionUID = -9172768618354379815L;
	
	private Ait ait;
	public String ultimoRetorno;
	private int qtArquivoMovimento;

	public Ait getAit() {
		return ait;
	}

	public void setAit(Ait ait) {
		this.ait = ait;
	}
	
	public String getUltimoRetorno() {
		return ultimoRetorno;
	}
	
	public void setUltimoRetorno(String ultimoRetorno) {
		this.ultimoRetorno = ultimoRetorno;
	}
	
	public int getQtArquivoMovimento() {
		return qtArquivoMovimento;
	}
	
	public void setQtArquivoMovimento(int qtArquivoMovimento) {
		this.qtArquivoMovimento = qtArquivoMovimento;
	}

	public static class ListBuilder {
		
		private ResultSetMapper<AitMovimentoDTO> aitsMovimento;
		private ResultSetMapper<Ait> aits;
		 
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.aitsMovimento = new ResultSetMapper<AitMovimentoDTO>(rsm, AitMovimentoDTO.class);
				setAit(rsm);				
			} catch (SQLException e) {
				e.printStackTrace(System.out);
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
				
		public List<AitMovimentoDTO> build() throws IllegalArgumentException, Exception {
			
			List<AitMovimentoDTO> aitsMovimento = this.aitsMovimento.toList();
			
			List<Ait> aits = this.aits.toList();
			
			for(int i = 0; i < aits.size(); i++) {
				AitMovimentoDTO dto = aitsMovimento.get(i);
				
				if(i < aits.size() && dto.getCdAit()>0)
					dto.setAit(aits.get(i));
				
			}
			
			return aitsMovimento;
		}
	}
	
	public static class Builder {

		private ObjectMapper mapper;
		private AitMovimentoDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), AitMovimentoDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder(int cdAitMovimento, int cdAit, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				AitMovimento aitMovimento = AitMovimentoDAO.get(cdAitMovimento, cdAit);
				dto = objectMapper.readValue(aitMovimento.toString(), AitMovimentoDTO.class);
				
				if(cascade) {
					if(aitMovimento.getCdAit()>0)
						setAit(aitMovimento.getCdAit(), cascade);
				}
				
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		public Builder setAit(Map<String, Object> map) {
			try {
				dto.setAit(mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), Ait.class));
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setAit(Ait ait) {
			try {
				dto.setAit(ait);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public Builder setAit(int cdAit, boolean cascade) {
			try {
				Ait ait = AitDAO.get(cdAit);
				dto.setAit(ait);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
				
		public AitMovimentoDTO build() {
			return dto;
		}
		
	}

}
