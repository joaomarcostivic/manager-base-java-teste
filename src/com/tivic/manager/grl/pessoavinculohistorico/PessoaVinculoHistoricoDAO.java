package com.tivic.manager.grl.pessoavinculohistorico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import com.tivic.sol.connection.Conexao;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Util;

public class PessoaVinculoHistoricoDAO {
	public static int insert(PessoaVinculoHistorico objeto, Connection connect) {
		try {
			int code = Conexao.getSequenceCode("grl_pessoa_vinculo_historico", connect);
			if(code <= 0) {
				return -1;
			}
			objeto.setCdVinculoHistorico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_pessoa_vinculo_historico (cd_vinculo_historico, "+
																										  "cd_pessoa, "+
																										  "cd_vinculo, "+
																										  "cd_usuario, "+
																										  "st_vinculo, "+
																										  "dt_vinculo_historico) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdVinculoHistorico());
			pstmt.setInt(2, objeto.getCdPessoa());
			pstmt.setInt(3, objeto.getCdVinculo());
			pstmt.setInt(4, objeto.getCdUsuario());
			pstmt.setInt(5, objeto.getStVinculo());
			if(objeto.getDtVinculoHistorico()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,  new Timestamp(objeto.getDtVinculoHistorico().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro ao salvar PessoaVinculoHistoricoDAO: "+ e);
			return -1;
		}
	}
	
	public static int update(PessoaVinculoHistorico objeto, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE  grl_pessoa_vinculo_historico SET cd_vinculo_historico=?, "+
					  																					 "cd_pessoa=?, "+
					  																					 "cd_vinculo=?, "+
					  																					 "cd_usuario=?, "+
					  																					 "st_vinculo=?, "+
					  																					 "dt_vinculo_historico=? WHERE cd_vinculo_historico=?");
			pstmt.setInt(1, objeto.getCdVinculoHistorico());
			pstmt.setInt(2, objeto.getCdPessoa());
			pstmt.setInt(3, objeto.getCdVinculo());
			pstmt.setInt(4, objeto.getCdUsuario());
			pstmt.setInt(5, objeto.getStVinculo());
			if(objeto.getDtVinculoHistorico()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,  new Timestamp(objeto.getDtVinculoHistorico().getTimeInMillis()));
			pstmt.setInt(7, objeto.getCdVinculoHistorico());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro ao salvar PessoaVinculoHistoricoDAO: "+ e);
			return -1;
		}
	}
	
	public static PessoaVinculoHistorico get(int cdVinculoHistorico) {
		return get(cdVinculoHistorico, null);
	}

	public static PessoaVinculoHistorico get(int cdVinculoHistorico, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_pessoa_vinculo_historico WHERE cd_vinculo_historico=?");
			pstmt.setInt(1, cdVinculoHistorico);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				PessoaVinculoHistorico pessoaVinculoHistorico = new PessoaVinculoHistorico();
				pessoaVinculoHistorico.setCdVinculoHistorico(rs.getInt("cd_vinculo_historico"));
				pessoaVinculoHistorico.setCdPessoa(rs.getInt("cd_pessoa"));
				pessoaVinculoHistorico.setCdVinculo(rs.getInt("cd_vinculo"));
				pessoaVinculoHistorico.setStVinculo(rs.getInt("st_vinculo"));
				pessoaVinculoHistorico.setDtVinculoHistorico((rs.getTimestamp("dt_vinculo_historico")==null) ? null : Util.longToCalendar(rs.getTimestamp("dt_vinculo_historico").getTime()));
				return pessoaVinculoHistorico;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PessoaVinculoHistoricoDAO: "+ e);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_pessoa_vinculo_historico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
