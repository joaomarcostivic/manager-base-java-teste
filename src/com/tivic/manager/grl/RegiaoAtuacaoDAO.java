package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class RegiaoAtuacaoDAO{

	public static int insert(RegiaoAtuacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegiaoAtuacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_regiao_atuacao (cd_regiao,"+
			                                  "cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "cd_vinculo,"+
			                                  "cd_conta_carteira,"+
			                                  "cd_conta,"+
			                                  "nm_regiao_atuacao,"+
			                                  "dt_inicio_atuacao,"+
			                                  "dt_final_atuacao,"+
			                                  "lg_ativo,"+
			                                  "id_regiao_atuacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegiao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdVinculo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdVinculo());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdConta());
			pstmt.setString(7,objeto.getNmRegiaoAtuacao());
			if(objeto.getDtInicioAtuacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicioAtuacao().getTimeInMillis()));
			if(objeto.getDtFinalAtuacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinalAtuacao().getTimeInMillis()));
			pstmt.setInt(10,objeto.getLgAtivo());
			pstmt.setString(11,objeto.getIdRegiaoAtuacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegiaoAtuacao objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(RegiaoAtuacao objeto, int cdRegiaoOld, int cdEmpresaOld, int cdPessoaOld, int cdVinculoOld) {
		return update(objeto, cdRegiaoOld, cdEmpresaOld, cdPessoaOld, cdVinculoOld, null);
	}

	public static int update(RegiaoAtuacao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(RegiaoAtuacao objeto, int cdRegiaoOld, int cdEmpresaOld, int cdPessoaOld, int cdVinculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_regiao_atuacao SET cd_regiao=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_vinculo=?,"+
												      		   "cd_conta_carteira=?,"+
												      		   "cd_conta=?,"+
												      		   "nm_regiao_atuacao=?,"+
												      		   "dt_inicio_atuacao=?,"+
												      		   "dt_final_atuacao=?,"+
												      		   "lg_ativo=?,"+
												      		   "id_regiao_atuacao=? WHERE cd_regiao=? AND cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1,objeto.getCdRegiao());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdPessoa());
			pstmt.setInt(4,objeto.getCdVinculo());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdConta());
			pstmt.setString(7,objeto.getNmRegiaoAtuacao());
			if(objeto.getDtInicioAtuacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicioAtuacao().getTimeInMillis()));
			if(objeto.getDtFinalAtuacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFinalAtuacao().getTimeInMillis()));
			pstmt.setInt(10,objeto.getLgAtivo());
			pstmt.setString(11,objeto.getIdRegiaoAtuacao());
			pstmt.setInt(12, cdRegiaoOld!=0 ? cdRegiaoOld : objeto.getCdRegiao());
			pstmt.setInt(13, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(14, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(15, cdVinculoOld!=0 ? cdVinculoOld : objeto.getCdVinculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegiao, int cdEmpresa, int cdPessoa, int cdVinculo) {
		return delete(cdRegiao, cdEmpresa, cdPessoa, cdVinculo, null);
	}

	public static int delete(int cdRegiao, int cdEmpresa, int cdPessoa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_regiao_atuacao WHERE cd_regiao=? AND cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1, cdRegiao);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdPessoa);
			pstmt.setInt(4, cdVinculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegiaoAtuacao get(int cdRegiao, int cdEmpresa, int cdPessoa, int cdVinculo) {
		return get(cdRegiao, cdEmpresa, cdPessoa, cdVinculo, null);
	}

	public static RegiaoAtuacao get(int cdRegiao, int cdEmpresa, int cdPessoa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_regiao_atuacao WHERE cd_regiao=? AND cd_empresa=? AND cd_pessoa=? AND cd_vinculo=?");
			pstmt.setInt(1, cdRegiao);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdPessoa);
			pstmt.setInt(4, cdVinculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegiaoAtuacao(rs.getInt("cd_regiao"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_vinculo"),
						rs.getInt("cd_conta_carteira"),
						rs.getInt("cd_conta"),
						rs.getString("nm_regiao_atuacao"),
						(rs.getTimestamp("dt_inicio_atuacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_atuacao").getTime()),
						(rs.getTimestamp("dt_final_atuacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_atuacao").getTime()),
						rs.getInt("lg_ativo"),
						rs.getString("id_regiao_atuacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_regiao_atuacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoAtuacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_regiao_atuacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
