package com.tivic.manager.mob;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.tivic.manager.util.ResultSetMapper;

import sol.dao.ResultSetMap;

public class AitPagamentoReportDataDTO implements Serializable  {
	private static final long serialVersionUID = 400036498049360486L;
	
	private int quantidade;
	private Double vlPago;
	private LocalDate dtPagamento;
	
	public AitPagamentoReportDataDTO(int quantidade, Double vlPago, LocalDate dtPagamento) {
		super();
		this.quantidade = quantidade;
		this.vlPago = vlPago;
		this.dtPagamento = dtPagamento;
	}
	
	public static class ListBuilder {
		private ResultSetMapper<AitPagamentoReportDataDTO> listReport;
		public ListBuilder(ResultSetMap rsm) {
			try {
				this.listReport = new ResultSetMapper<AitPagamentoReportDataDTO>(rsm, AitPagamentoReportDataDTO.class);
			} catch (SQLException e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
		
		public List<AitPagamentoReportDataDTO> build() throws IllegalArgumentException, Exception {
			List<AitPagamentoReportDataDTO> listReport = this.listReport.toList();
			
			return listReport;
		}
	}
	
}
