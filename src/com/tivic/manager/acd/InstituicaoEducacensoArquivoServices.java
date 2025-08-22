package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class InstituicaoEducacensoArquivoServices {

	public static Result save(InstituicaoEducacensoArquivo instituicaoEducacensoArquivo){
		return save(instituicaoEducacensoArquivo, null, null);
	}

	public static Result save(InstituicaoEducacensoArquivo instituicaoEducacensoArquivo, AuthData authData){
		return save(instituicaoEducacensoArquivo, authData, null);
	}

	public static Result save(InstituicaoEducacensoArquivo instituicaoEducacensoArquivo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoEducacensoArquivo==null)
				return new Result(-1, "Erro ao salvar. InstituicaoEducacensoArquivo é nulo");

			int retorno;
			if(instituicaoEducacensoArquivo.getCdInstituicao()==0){
				retorno = InstituicaoEducacensoArquivoDAO.insert(instituicaoEducacensoArquivo, connect);
				instituicaoEducacensoArquivo.setCdInstituicao(retorno);
			}
			else {
				retorno = InstituicaoEducacensoArquivoDAO.update(instituicaoEducacensoArquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOEDUCACENSOARQUIVO", instituicaoEducacensoArquivo);
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
	public static Result remove(InstituicaoEducacensoArquivo instituicaoEducacensoArquivo) {
		return remove(instituicaoEducacensoArquivo.getCdInstituicao(), instituicaoEducacensoArquivo.getCdPeriodoLetivo());
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo){
		return remove(cdInstituicao, cdPeriodoLetivo, false, null, null);
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo, boolean cascade){
		return remove(cdInstituicao, cdPeriodoLetivo, cascade, null, null);
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo, boolean cascade, AuthData authData){
		return remove(cdInstituicao, cdPeriodoLetivo, cascade, authData, null);
	}
	public static Result remove(int cdInstituicao, int cdPeriodoLetivo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = InstituicaoEducacensoArquivoDAO.delete(cdInstituicao, cdPeriodoLetivo, connect);
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_educacenso_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoArquivoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_educacenso_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static sol.util.Result getProgressoArquivo(int cdInstituicao, int cdPeriodoLetivo) throws SQLException	{
		return getProgressoArquivo(cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static sol.util.Result getProgressoArquivo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) throws SQLException	{
		boolean isConnectionNull = connect == null;
    	
		try {
		
			connect   = (isConnectionNull ? Conexao.conectar() : connect);
			connect.setAutoCommit((isConnectionNull ? false : connect.getAutoCommit()));
			
			InstituicaoEducacensoArquivo instituicaoEducacensoArquivo = InstituicaoEducacensoArquivoDAO.get(cdInstituicao, cdPeriodoLetivo, connect);
			
			if(instituicaoEducacensoArquivo == null){
				instituicaoEducacensoArquivo = new InstituicaoEducacensoArquivo(cdInstituicao, cdPeriodoLetivo, 0, 0, Util.getDataAtual(), 0);
				if(InstituicaoEducacensoArquivoDAO.insert(instituicaoEducacensoArquivo) < 0){
					return new Result(-1, "Erro ao gerar arquivo de educacenso");
				}
			}
					
			String txtProgressoArquivo = "Geraçao total do arquivo esta em: " + instituicaoEducacensoArquivo.getPrRegistro() + "%\n";
			
			String ultimoRegistro = "";
			String registroAtual = "";
			int lgFinalizado = 0;
			
			if(instituicaoEducacensoArquivo.getPrRegistro() == 14){
				ultimoRegistro = "00 - Instituicao";
				registroAtual = "10 - Instituicao - Estrutura";
				lgFinalizado = 0;
			}
			else if(instituicaoEducacensoArquivo.getPrRegistro() == 28){
				ultimoRegistro = "10 - Instituicao - Estrutura";
				registroAtual = "20 - Turmas";
				lgFinalizado = 0;
			}
			else if(instituicaoEducacensoArquivo.getPrRegistro() == 42){
				ultimoRegistro = "20 - Turmas";
				registroAtual = "30 40 50 - Professores/Gestor";
				lgFinalizado = 0;
			}
			else if(instituicaoEducacensoArquivo.getPrRegistro() == 86){
				ultimoRegistro = "30 40 50 - Professores/Gestor";
				registroAtual = "30 60 - Alunos";
				lgFinalizado = 0;
			}
			else if(instituicaoEducacensoArquivo.getPrRegistro() == 100){
				ultimoRegistro = "30 60 - Alunos";
				registroAtual = "Completo";
				lgFinalizado = 1;
			}
			
			txtProgressoArquivo += "Ultimo registro analisado: " + ultimoRegistro + "\n";
			txtProgressoArquivo += "Registro atual: " + registroAtual + (lgFinalizado == 0 ? " - Progresso: " + Util.formatNumber(instituicaoEducacensoArquivo.getPrCampos(), 2) + "%" : "") + "\n";
			txtProgressoArquivo += "Data da ultima atualizacao: " + Util.convCalendarStringCompleto(instituicaoEducacensoArquivo.getDtAtualizacao());
			
			
			
			Result result = new Result(1, "Progresso buscado com sucesso!");
			result.addObject("TXT_PROGRESSO_ARQUIVO", txtProgressoArquivo);
			result.addObject("LG_FINALIZADO", lgFinalizado);
			
			if(lgFinalizado == 1){
				Result resultado = InstituicaoServices.exportEducacenso(cdInstituicao, cdPeriodoLetivo, 1);
				result.addObject("REGISTRO", resultado.getObjects().get("REGISTRO"));
			}
			
			return result;
		
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar buscar progresso do arquivo do educacenso!", e);
		}
		finally{
			if(isConnectionNull)
	  			Conexao.desconectar(connect);
		}
		
    }
	
	public static sol.util.Result zerarArquivo(int cdInstituicao, int cdPeriodoLetivo) throws SQLException	{
		return zerarArquivo(cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static sol.util.Result zerarArquivo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) throws SQLException	{
		boolean isConnectionNull = connect == null;
    	
		try {
		
			connect   = (isConnectionNull ? Conexao.conectar() : connect);
			connect.setAutoCommit((isConnectionNull ? false : connect.getAutoCommit()));
			
			InstituicaoEducacensoArquivo instituicaoEducacensoArquivo = InstituicaoEducacensoArquivoDAO.get(cdInstituicao, cdPeriodoLetivo, connect);
			
			if(instituicaoEducacensoArquivo == null){
				instituicaoEducacensoArquivo = new InstituicaoEducacensoArquivo(cdInstituicao, cdPeriodoLetivo, 0, 0, Util.getDataAtual(), 0);
				if(InstituicaoEducacensoArquivoDAO.insert(instituicaoEducacensoArquivo, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao zerar arquivo de educacenso");
				}
			}
			else{
				if(instituicaoEducacensoArquivo.getLgExecucao() == 1){
					return new Result(-1, "Arquivo ja esta sendo gerado. Não é possível zerar o arquivo enquanto este processo não se encerrar.");
				}
				
				instituicaoEducacensoArquivo.setPrRegistro(0);
				instituicaoEducacensoArquivo.setPrCampos(0);
				instituicaoEducacensoArquivo.setLgExecucao(0);
				
				if(InstituicaoEducacensoArquivoDAO.update(instituicaoEducacensoArquivo, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao zerar arquivo de educacenso");
				}
				
				int ret = connect.prepareStatement("DELETE FROM acd_instituicao_educacenso_arquivo_registro WHERE cd_instituicao = " + cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo).executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					
					return new Result(-1, "Erro ao zerar arquivo de educacenso");
				}
				
			}
			
			if(isConnectionNull)
				connect.commit();
				
			
			Result result = new Result(1, "Sucesso ao zerar arquivo do educacenso!");
			return result;
		
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new sol.util.Result(-1, "Erro ao tentar buscar progresso do arquivo do educacenso!", e);
		}
		finally{
			if(isConnectionNull)
	  			Conexao.desconectar(connect);
		}
		
    }
}