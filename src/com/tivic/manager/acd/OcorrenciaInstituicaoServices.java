package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.util.Util;

public class OcorrenciaInstituicaoServices {

	public static Result save(OcorrenciaInstituicao ocorrenciaInstituicao){
		return save(ocorrenciaInstituicao, null);
	}

	public static Result save(OcorrenciaInstituicao ocorrenciaInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ocorrenciaInstituicao==null)
				return new Result(-1, "Erro ao salvar. OcorrenciaInstituicao é nulo");

			int retorno;
			if(ocorrenciaInstituicao.getCdOcorrencia()==0){
				retorno = OcorrenciaInstituicaoDAO.insert(ocorrenciaInstituicao, connect);
				ocorrenciaInstituicao.setCdOcorrencia(retorno);
			}
			else {
				retorno = OcorrenciaInstituicaoDAO.update(ocorrenciaInstituicao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OCORRENCIAINSTITUICAO", ocorrenciaInstituicao);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdOcorrencia){
		return remove(cdOcorrencia, false, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade){
		return remove(cdOcorrencia, cascade, null);
	}
	public static Result remove(int cdOcorrencia, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = OcorrenciaInstituicaoDAO.delete(cdOcorrencia, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + cdPeriodoLetivo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = OcorrenciaInstituicaoDAO.find(criterios, connect);
			while (rsm.next()) {
				int ret = OcorrenciaInstituicaoDAO.delete(rsm.getInt("cd_ocorrencia"), connect);
				if(ret<0){
					Conexao.rollback(connect);
					return new Result(ret, "Erro ao excluir ocorrência instituição");
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_instituicao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM acd_ocorrencia_instituicao A, grl_ocorrencia B WHERE A.cd_ocorrencia = B.cd_ocorrencia ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllByInstituicaoMatriculaInicial( int cdInstituicao ) {
		return getAllByInstituicaoMatriculaInicial(cdInstituicao, null);
	}

	public static ResultSetMap getAllByInstituicaoMatriculaInicial(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario FROM acd_ocorrencia_instituicao A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "						  WHERE A.cd_instituicao = "+cdInstituicao
					+ "							AND B.cd_tipo_ocorrencia IN ("+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_DECLARAR_PRONTA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+", "
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+","
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+","
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.getAllByInstituicaoSituacaoCenso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.getAllByInstituicaoSituacaoCenso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByInstituicaoSituacaoCenso( int cdInstituicao ) {
		return getAllByInstituicaoSituacaoCenso(cdInstituicao, null);
	}

	public static ResultSetMap getAllByInstituicaoSituacaoCenso(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario FROM acd_ocorrencia_instituicao A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "						  WHERE A.cd_instituicao = "+cdInstituicao
					+ "							AND B.cd_tipo_ocorrencia IN ("+InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_SITUACAO_ALUNO_CENSO+", "+InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO+", "+InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.getAllByInstituicaoSituacaoCenso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.getAllByInstituicaoSituacaoCenso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarOcorrenciaArquivoSituacaoCenso( int cdInstituicao, int cdPeriodoLetivo, int cdUsuario ) {
		return gerarOcorrenciaArquivoSituacaoCenso(cdInstituicao, cdPeriodoLetivo, cdUsuario, null);
	}

	public static Result gerarOcorrenciaArquivoSituacaoCenso(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(String.valueOf(InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_SITUACAO_ALUNO_CENSO), connect).getCdTipoOcorrencia();
			if(cdTipoOcorrencia > 0){
				
				Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
				
				String txtOcorrencia = "Gerado arquivo de Situação do Aluno do Educacenso para " + instituicao.getNmPessoa() + " no periodo de " + instituicaoPeriodo.getNmPeriodoLetivo();
				
				OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, cdPeriodoLetivo);
				if(OcorrenciaInstituicaoServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de geração de arquivo de Situação do Aluno");
				}
				
				return new Result(1, "Sucesso ao gerar ocorrencia");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(-1, "Tipo de Ocorrência não cadastrada");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.gerarOcorrenciaArquivoSituacaoCenso: " + e);
			return new Result(-1, "Erro ao gerar ocorrência");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result finalizarSituacaoAlunoCenso( int cdInstituicao, int cdPeriodoLetivo, int cdUsuario ) {
		return finalizarSituacaoAlunoCenso(cdInstituicao, cdPeriodoLetivo, cdUsuario, null);
	}

	public static Result finalizarSituacaoAlunoCenso(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(String.valueOf(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO), connect).getCdTipoOcorrencia();
			if(cdTipoOcorrencia > 0){
				
				Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
				
				String txtOcorrencia = instituicao.getNmPessoa() + " finalizada na etapa de Situação do Aluno do Educacenso no periodo de " + instituicaoPeriodo.getNmPeriodoLetivo();
				
				OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, cdPeriodoLetivo);
				if(OcorrenciaInstituicaoServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia finalização da situação de aluno");
				}
				
				return new Result(1, "Sucesso ao gerar ocorrencia");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(-1, "Tipo de Ocorrência não cadastrada");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.gerarOcorrenciaArquivoSituacaoCenso: " + e);
			return new Result(-1, "Erro ao gerar ocorrência");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result liberarSituacaoAlunoCenso( int cdInstituicao, int cdPeriodoLetivo, int cdUsuario ) {
		return liberarSituacaoAlunoCenso(cdInstituicao, cdPeriodoLetivo, cdUsuario, null);
	}

	public static Result liberarSituacaoAlunoCenso(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int cdTipoOcorrencia = com.tivic.manager.grl.TipoOcorrenciaServices.getById(String.valueOf(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO), connect).getCdTipoOcorrencia();
			if(cdTipoOcorrencia > 0){
				
				Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
				
				String txtOcorrencia = instituicao.getNmPessoa() + " liberada na etapa de Situação do Aluno do Educacenso no periodo de " + instituicaoPeriodo.getNmPeriodoLetivo();
				
				OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, instituicao.getCdInstituicao(), txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), Util.getDataAtual(), cdUsuario, cdPeriodoLetivo);
				if(OcorrenciaInstituicaoServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia liberação da situação de aluno");
				}
				
				return new Result(1, "Sucesso ao gerar ocorrencia");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(-1, "Tipo de Ocorrência não cadastrada");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaInstituicaoServices.liberarSituacaoAlunoCenso: " + e);
			return new Result(-1, "Erro ao gerar ocorrência");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllEducacenso( int cdInstituicao, int cdPeriodoLetivo ) {
		return getAllEducacenso(cdInstituicao, cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllEducacenso(int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT *, P.nm_pessoa AS nm_usuario FROM acd_ocorrencia_instituicao A"
					+ "							JOIN grl_ocorrencia B ON (A.cd_ocorrencia = B.cd_ocorrencia) "
					+ "							JOIN seg_usuario U ON (B.cd_usuario = U.cd_usuario) "
					+ "							JOIN grl_pessoa P ON (U.cd_pessoa = P.cd_pessoa) "
					+ "							LEFT OUTER JOIN grl_tipo_ocorrencia C ON (B.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia) "
					+ "						  WHERE A.cd_instituicao = "+cdInstituicao
					+ "						    AND A.cd_periodo_letivo = " + cdPeriodoLetivo
					+ "							AND B.cd_tipo_ocorrencia IN ("+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_DECLARAR_PRONTA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+", "
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+","
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+","
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia()+","
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_SITUACAO_ALUNO_CENSO, connect).getCdTipoOcorrencia()+","
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO, connect).getCdTipoOcorrencia()+","
					+ "														 "+TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO, connect).getCdTipoOcorrencia()+")"
					+ "						  ORDER BY dt_ocorrencia DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("DT_OCORRENCIA DESC");
			rsm.orderBy(fields);
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaServices.getAllByTurmaSituacaoCenso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}
