package com.tivic.manager.bdv;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.auth.jwt.JWT;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.log.BuscaPlaca;
import com.tivic.manager.log.BuscaPlacaServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;

public class VeiculoServices {

	public static Result save(Veiculo veiculo){
		return save(veiculo, null, null);
	}

	public static Result save(Veiculo veiculo, AuthData authData){
		return save(veiculo, authData, null);
	}

	public static Result save(Veiculo veiculo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(veiculo==null)
				return new Result(-1, "Erro ao salvar. Veiculo é nulo");

			if(veiculo.getDtInformacao() == null) {
				veiculo.setDtInformacao(new GregorianCalendar());
			}
			
			PreparedStatement ps = connect.prepareStatement("SELECT cd_veiculo FROM bdv_veiculo WHERE nr_placa = ?");
			ps.setString(1, veiculo.getNrPlaca());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				veiculo.setCdVeiculo(rs.getInt("cd_veiculo"));
			}
			
			int retorno;
			if(veiculo.getCdVeiculo()==0){
				retorno = VeiculoDAO.insert(veiculo, connect);
				veiculo.setCdVeiculo(retorno);
			}
			else {
				retorno = VeiculoDAO.update(veiculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VEICULO", veiculo);
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
	public static Result remove(Veiculo veiculo) {
		return remove(veiculo.getCdVeiculo());
	}
	public static Result remove(int cdVeiculo){
		return remove(cdVeiculo, false, null, null);
	}
	public static Result remove(int cdVeiculo, boolean cascade){
		return remove(cdVeiculo, cascade, null, null);
	}
	public static Result remove(int cdVeiculo, boolean cascade, AuthData authData){
		return remove(cdVeiculo, cascade, authData, null);
	}
	public static Result remove(int cdVeiculo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = VeiculoDAO.delete(cdVeiculo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM bdv_veiculo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM bdv_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static Veiculo get(String nrPlaca) {
		return get(nrPlaca, null);
	}
	
	public static Veiculo get(String nrPlaca, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			Veiculo veiculo = null;
			
			PreparedStatement ps = connect.prepareStatement("SELECT cd_veiculo FROM bdv_veiculo WHERE nr_placa = ?");
			ps.setString(1, nrPlaca);
			ResultSet rs = ps.executeQuery();
			int cdVeiculo = 0;
			if(rs.next()) {
				cdVeiculo = rs.getInt("cd_veiculo");
			}
			rs.close();
			
			veiculo = VeiculoDAO.get(cdVeiculo, connect);
			
			if(veiculo == null) {
				return new Veiculo();
			}

			List<Proprietario> proprietarios = new ArrayList<Proprietario>();
			ps = connect.prepareStatement("SELECT cd_proprietario FROM bdv_proprietario WHERE cd_veiculo = ? ORDER BY cd_proprietario DESC LIMIT 1");
			ps.setInt(1, cdVeiculo);
			rs = ps.executeQuery();
			while(rs.next()) {
				proprietarios.add(ProprietarioDAO.get(rs.getInt("cd_proprietario"), cdVeiculo, connect));
			}
			if(proprietarios.size() > 0)
				veiculo.setProprietarios(proprietarios);
			rs.close();
			
			List<Restricao> restricoes = new ArrayList<Restricao>();
			ps = connect.prepareStatement("SELECT cd_restricao FROM bdv_restricao WHERE cd_veiculo = ?");
			ps.setInt(1, cdVeiculo);
			rs = ps.executeQuery();
			while(rs.next()) {
				restricoes.add(RestricaoDAO.get(rs.getInt("cd_restricao"), cdVeiculo, connect));
			}
			if(restricoes.size() > 0)
				veiculo.setRestricoes(restricoes);
			rs.close();
			
			List<Debito> debitos = new ArrayList<Debito>();
			ps = connect.prepareStatement("SELECT cd_debito FROM bdv_debito WHERE cd_veiculo = ?");
			ps.setInt(1, cdVeiculo);
			rs = ps.executeQuery();
			while(rs.next()) {
				debitos.add(DebitoDAO.get(rs.getInt("cd_debito"), cdVeiculo, connect));
			}
			if(debitos.size() > 0)
				veiculo.setDebitos(debitos);
			rs.close();
			
			return veiculo;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	protected static Result auth(Usuario usuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			Result result = new Result(1, "Autenticado com sucesso.");
			
			PreparedStatement ps = connect.prepareStatement("SELECT * FROM seg_usuario WHERE nm_login = ? AND nm_senha = ? AND st_login = 1");
			ps.setString(1, usuario.getNmLogin());
			ps.setString(2, usuario.getNmSenha());
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				/*
				 * Authorization Token
				 */
				HashMap<String, Object> headers = new HashMap<String, Object>();
				HashMap<String, Object> payload = new HashMap<String, Object>();
				GregorianCalendar issue = new GregorianCalendar();
				issue.add(Calendar.HOUR, 1);
				
				headers.put("exp", issue);
				headers.put("sub", usuario.getNmLogin());

				payload.put("id", usuario.getCdUsuario());
				payload.put("login", usuario.getNmLogin());
				
				JWT jwt = new JWT();
				
				result.addObject("CD_USUARIO", rs.getInt("cd_usuario"));
				result.addObject("AUTHORIZATION", jwt.generate(headers, payload));
				
				return result;
			} else {
				return new Result(-2, "Usuário ou senha incorreto.");
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoServices.auth: " + e);
			return new Result(-1, "Erro ao autenticar.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
