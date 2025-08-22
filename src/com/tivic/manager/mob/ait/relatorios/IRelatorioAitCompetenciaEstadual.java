package com.tivic.manager.mob.ait.relatorios;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IRelatorioAitCompetenciaEstadual {
	public PagedResponse<RelatorioAitDTO> filtrarListagemAits(RelatorioAitCompetenciaEstadualSearch relatorioAitCompetenciaEstadualSearch) throws ValidacaoException, Exception;
	public byte[] gerarMovimentacoesAit(int[] cdAits) throws Exception;
	public byte[] gerarSegudaVia(int[] cdAits) throws Exception;
}
