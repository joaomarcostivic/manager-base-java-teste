package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class AtaRelatorDAO{

	public static int insert(AtaRelator objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtaRelator objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_ata_relator (cd_ata,"+
			                                  "cd_pessoa) VALUES (?, ?)");
			if(objeto.getCdAta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAta());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtaRelator objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AtaRelator objeto, int cdAtaOld, int cdPessoaOld) {
		return update(objeto, cdAtaOld, cdPessoaOld, null);
	}

	public static int update(AtaRelator objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AtaRelator objeto, int cdAtaOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_ata_relator SET cd_ata=?,"+
												      		   "cd_pessoa=? WHERE cd_ata=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdAta());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3, cdAtaOld!=0 ? cdAtaOld : objeto.getCdAta());
			pstmt.setInt(4, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAta, int cdPessoa) {
		return delete(cdAta, cdPessoa, null);
	}

	public static int delete(int cdAta, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_ata_relator WHERE cd_ata=? AND cd_pessoa=?");
			pstmt.setInt(1, cdAta);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtaRelator get(int cdAta, int cdPessoa) {
		return get(cdAta, cdPessoa, null);
	}

	public static AtaRelator get(int cdAta, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_ata_relator WHERE cd_ata=? AND cd_pessoa=?");
			pstmt.setInt(1, cdAta);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtaRelator(rs.getInt("cd_ata"),
						rs.getInt("cd_pessoa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_ata_relator");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AtaRelator> getList() {
		return getList(null);
	}

	public static ArrayList<AtaRelator> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AtaRelator> list = new ArrayList<AtaRelator>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AtaRelator obj = AtaRelatorDAO.get(rsm.getInt("cd_ata"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtaRelatorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_ata_relator", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
