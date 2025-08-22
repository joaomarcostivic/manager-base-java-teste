package com.tivic.manager.geo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.geo.Ponto;
import com.tivic.manager.geo.PontoDAO;
import com.tivic.manager.sinc.Local;
import com.tivic.manager.sinc.LocalDAO;

public class PontoServices {

	public static Result save(Ponto ponto){
		return save(ponto, null);
	}

	public static Result save(Ponto ponto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ponto==null)
				return new Result(-1, "Erro ao salvar. Ponto é nulo");

			int retorno;
			if(ponto.getCdPonto()==0){
				retorno = PontoDAO.insert(ponto, connect);
				ponto.setCdPonto(retorno);
			}
			else {
				retorno = PontoDAO.update(ponto, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PONTO", ponto);
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
	
	public static ArrayList<Ponto> getAllByReferencia(int cdReferencia) {
		return getAllByReferencia(cdReferencia, null);
	}

	public static ArrayList<Ponto> getAllByReferencia(int cdReferencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_ponto FROM geo_ponto WHERE cd_referencia = ? ORDER BY cd_ponto");
			pstmt.setInt(1, cdReferencia);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<Ponto> pontos = new ArrayList<Ponto>();
			Ponto p;
			while(rs.next()){
				p = PontoDAO.get(rs.getInt("cd_ponto"), cdReferencia, connect);
				pontos.add(p);
			}
			rs.close();
			
			return pontos;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.getAllByReferencia: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.getAllByReferencia: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeAllByReferencia(int cdReferencia) {
		return removeAllByReferencia(cdReferencia, null);
	}

	public static Result removeAllByReferencia(int cdReferencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM geo_ponto WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
			
			pstmt.executeUpdate();
			
			return new Result(1, "Pontos deletados com sucesso");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.removeAllByReferencia: " + e);
			return new Result(-1, "Erro ao deletar pontos");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
