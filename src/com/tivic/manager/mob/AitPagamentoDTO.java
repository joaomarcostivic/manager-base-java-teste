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

public class AitPagamentoDTO extends AitPagamento implements Serializable {
	private static final long serialVersionUID = 3896141178117474106L;

	private Ait ait;
	
	public Ait getAit() {
		return ait;
	}

	public void setAit(Ait ait) {
		this.ait = ait;
	}
	
	
	public static class ListBuilder {
		private ResultSetMapper<AitPagamentoDTO> aitPagamentos;
		private ResultSetMapper<Ait> aits;
		
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.aitPagamentos = new ResultSetMapper<AitPagamentoDTO>(rsm, AitPagamentoDTO.class);
				setAit(rsm);
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
		
		public List<AitPagamentoDTO> build() throws IllegalArgumentException, Exception{
			List<AitPagamentoDTO> aitPagamentos = this.aitPagamentos.toList();
			
			List<Ait> aits = this.aits.toList();
			
			for(int i = 0; i < aits.size(); i++) {
				AitPagamentoDTO dto = aitPagamentos.get(i);
				
				if(i < aits.size() && dto.getCdAit()>0)
					dto.setAit(aits.get(i));
				
			}
			
			return aitPagamentos;
		}
	}
	
	public static class Builder{
		private ObjectMapper mapper;
		private AitPagamentoDTO dto;
		
		public Builder(Map<String, Object> map) {
			try {
				mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				dto = mapper.readValue(Util.map2Json((HashMap<String, Object>)map).toString(), AitPagamentoDTO.class);
			} catch(Exception e) {
				e.printStackTrace(System.out);
			}
		}

		public Builder(int cdAit, boolean cascade) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				AitPagamento aitPagamento = AitPagamentoDAO.get(cdAit);
				dto = objectMapper.readValue(aitPagamento.toString(), AitPagamentoDTO.class);
				
				if(cascade) {
					if(aitPagamento.getCdAit()>0)
						setAit(aitPagamento.getCdAit());
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
		
		public Builder setAit(int cdAit) {
			try {
				Ait ait = AitDAO.get(cdAit);
				dto.setAit(ait);
				return this;
			} catch(Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		public AitPagamentoDTO build() {
			return dto;
		}
	}
}
