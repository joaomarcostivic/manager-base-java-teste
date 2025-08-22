package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

public class BoatVeiculoServices {

	public static final String[] tpCategoriaCnh = {"A", "B", "AB", "C", "AC", "D", "AD", "E", "AE"};
	
	public static Result save(int cdBoat, ArrayList<BoatVeiculo> veiculos) {
		return save(cdBoat, veiculos, null);
	}

	public static Result save(int cdBoat, ArrayList<BoatVeiculo> veiculos,  Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			int retorno = 0;
			
			verificarRemocaoVeiculos(cdBoat, veiculos, connect);
			
			for (BoatVeiculo veiculo: veiculos) {				
				veiculo.setCdBoat(cdBoat);

				veiculo.setNrPlaca(_fixPlaca(veiculo.getNrPlaca()));
				veiculo.setNrDocumento(_fixDocumento(veiculo.getNrDocumento()));
				veiculo.setNrDocumentoCondutor(_fixDocumento(veiculo.getNrDocumentoCondutor()));
				veiculo.setNrCnhCondutor(_fixDocumento(veiculo.getNrCnhCondutor()));
				retorno = BoatVeiculoServices.save(veiculo, null, connect).getCode();
				
				if(retorno<=0)
					break;
				
				if(veiculo.getImagens() != null && veiculo.getImagens().size() > 0) {
					retorno = BoatImagemServices.save(cdBoat, veiculo.getCdBoatVeiculo(), veiculo.getImagens(), connect).getCode();
				}
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Veiculos(s) incluídos com sucesso.");
			else
				return new Result(-1, "Erro ao incluir veiculo(s).");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir veiculo(s).");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static Result saveSync(int cdBoat, ArrayList<BoatVeiculo> veiculos,  Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			int retorno = 0;
			for (BoatVeiculo veiculo: veiculos) {				
				veiculo.setCdBoat(cdBoat);
				veiculo.setNrPlaca(_fixPlaca(veiculo.getNrPlaca()));
				retorno = BoatVeiculoDAO.insert(veiculo, connect);
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Veiculos(s) incluídos com sucesso.");
			else
				return new Result(-1, "Erro ao incluir veiculo(s).");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir veiculo(s).");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result save(BoatVeiculo boatVeiculo){
		return save(boatVeiculo, null, null);
	}

	public static Result save(BoatVeiculo boatVeiculo, AuthData authData){
		return save(boatVeiculo, authData, null);
	}

	public static Result save(BoatVeiculo boatVeiculo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatVeiculo==null)
				return new Result(-1, "Erro ao salvar. BoatVeiculo é nulo");

			int retorno;

			if(BoatVeiculoDAO.get(boatVeiculo.getCdBoatVeiculo(), boatVeiculo.getCdBoat(), connect)==null){
				boatVeiculo.setNrPlaca(_fixPlaca(boatVeiculo.getNrPlaca()));
				retorno = BoatVeiculoDAO.insert(boatVeiculo, connect);
				boatVeiculo.setCdVeiculo(retorno);
			}
			else {
				retorno = BoatVeiculoDAO.update(boatVeiculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATVEICULO", boatVeiculo);
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
	public static Result remove(int cdVeiculo, int cdBoat){
		return remove(cdVeiculo, cdBoat, false, null, null);
	}
	public static Result remove(int cdVeiculo, int cdBoat, boolean cascade){
		return remove(cdVeiculo, cdBoat, cascade, null, null);
	}
	public static Result remove(int cdVeiculo, int cdBoat, boolean cascade, AuthData authData){
		return remove(cdVeiculo, cdBoat, cascade, authData, null);
	}
	public static Result remove(int cdVeiculo, int cdBoat, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = BoatVeiculoDAO.delete(cdVeiculo, cdBoat, connect);
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
	
	public static Result removeAll(int cdBoat, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = connect.prepareStatement("DELETE FROM mob_boat_veiculo WHERE cd_boat="+cdBoat).executeUpdate();
						
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_veiculo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllVeiculosByBoat(int cdBoat) {
		return getAllVeiculosByBoat(cdBoat, null);
	}

	public static ResultSetMap getAllVeiculosByBoat(int cdBoat, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
						"SELECT A.*, "
						+ "A.nr_documento AS nr_cpf, "
						+ "E.nm_marca, "
						+ "E.nm_modelo, "
						+ "C.nm_cor, "
						+ "D.ds_especie, "
						+ "H.nm_categoria, "
						+ "G.nm_tipo, "
						+ "F.nm_municipio, "
						+ "I.ds_declaracao" +
					    " FROM mob_boat_veiculo A " +
						" LEFT OUTER JOIN fta_cor C ON (A.cd_cor = C.cd_cor) " + 
						" LEFT OUTER JOIN fta_especie_veiculo D ON (A.cd_especie = D.cd_especie) " + 
						" LEFT OUTER JOIN fta_marca_modelo E ON (A.cd_marca_modelo = E.cd_marca_modelo) " + 
						" LEFT OUTER JOIN grl_cidade F ON (A.cd_cidade = F.cd_cidade) " + 
						" LEFT OUTER JOIN fta_tipo_veiculo G ON (A.cd_tipo_veiculo = G.cd_tipo_veiculo) " +
						" LEFT OUTER JOIN fta_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " +
						" LEFT OUTER JOIN fta_categoria_veiculo H ON (A.cd_categoria = H.cd_categoria) " + 
						" LEFT OUTER JOIN mob_boat_declarante I ON (A.cd_boat = I.cd_boat)" +
						" WHERE A.cd_boat = ? ORDER BY cd_boat_veiculo "
					);
			
			if(Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement(
					"SELECT A.*, "
					+ "A.nr_documento AS nr_cpf, "
					+ "E.nm_marca, "
					+ "E.nm_modelo, "
					+ "C.nm_cor, "
					+ "D.ds_especie, "
					+ "H.nm_categoria, "
					+ "G.nm_tipo, "
					+ "F.nm_municipio, "
					+ "I.ds_declaracao" +
				    " FROM mob_boat_veiculo A " +
					" LEFT OUTER JOIN cor C ON (A.cd_cor = C.cod_cor) " + 
					" LEFT OUTER JOIN especie_veiculo D ON (A.cd_especie = D.cod_especie) " + 
					" LEFT OUTER JOIN marca_modelo E ON (A.cd_marca = E.cod_marca) " + 
					" LEFT OUTER JOIN municipio F ON (A.cd_cidade = F.cod_municipio) " + 
					" LEFT OUTER JOIN tipo_veiculo G ON (A.cd_tipo = G.cod_tipo) " +
					" LEFT OUTER JOIN fta_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " +
					" LEFT OUTER JOIN fta_categoria_veiculo H ON (A.cd_categoria = H.cd_categoria) " + 
					" LEFT OUTER JOIN mob_boat_declarante I ON (A.cd_boat = I.cd_boat)" +
					" WHERE A.cd_boat = ? ORDER BY cd_boat_veiculo "
				);
			}
			
			pstmt.setInt(1, cdBoat);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoServices.getAllByBoat: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoServices.getAllByBoat: " + e);
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
		return Search.find("SELECT * FROM mob_boat_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static void verificarRemocaoVeiculos(int cdBoat, ArrayList<BoatVeiculo> veiculos, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {		
			if (isConnectionNull)
				connect = Conexao.conectar();
			int retorno = 1;
			
			List<BoatVeiculo> veiculosAtuais = new ResultSetMapper<BoatVeiculo>(getAllVeiculosByBoat(cdBoat, connect), BoatVeiculo.class).toList();
			List<BoatVeiculo> veiculosRemovidos = compararObjetoVeiculos(veiculos, veiculosAtuais);
			
			if(veiculosRemovidos == null) {
				return;
			}
			
			for(BoatVeiculo veiculo : veiculosRemovidos) {
				List<BoatImagem> imagens = new ResultSetMapper<BoatImagem>(BoatImagemServices.getAllByBoat(cdBoat, connect), BoatImagem.class).toList();
				
				for(BoatImagem imagem : imagens) {
					if(imagem.getCdBoatVeiculo() == veiculo.getCdBoatVeiculo()) {
						retorno = BoatImagemServices.remove(imagem.getCdImagem(), cdBoat, true, null, connect).getCode();
					}
				}
				
				if(retorno > 0) {
					retorno = BoatVeiculoServices.remove(veiculo.getCdBoatVeiculo(), cdBoat, true, null, connect).getCode();
				}
				
				if(retorno < 0) {
					throw new Exception("Não foi possível remover o veículo do BOAT.");
				}							
			}
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatVeiculoServices.verificarRemocaoVeiculos: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static List<BoatVeiculo> compararObjetoVeiculos(List<BoatVeiculo> veiculosAtualizacao, List<BoatVeiculo> veiculosAtuais) {		
		if(veiculosAtualizacao == null || veiculosAtuais == null) {
			return null;
		}
		
		Set<Integer> naoRemovidos = retornarCodigosListaVeiculos(veiculosAtualizacao);

		return filtrarVeiculoRemovidos(naoRemovidos, veiculosAtuais);
	}
	
	private static Set<Integer> retornarCodigosListaVeiculos(List<BoatVeiculo> veiculos) {
		return veiculos.stream().map(BoatVeiculo::getCdBoatVeiculo).collect(Collectors.toSet());
	}
	
	private static List<BoatVeiculo> filtrarVeiculoRemovidos(Set<Integer> ids, List<BoatVeiculo> veiculos) {
		return veiculos
				  .stream()
				  .filter(veiculo -> !ids.contains(veiculo.getCdBoatVeiculo()))
				  .collect(Collectors.toList());
	}
	
	private static String _fixPlaca(String nrPlaca) {
		return nrPlaca != null && !nrPlaca.trim().equals("") ? nrPlaca.replaceAll("[^a-zA-Z0-9]+", "").toUpperCase() : null;
	}
	
	private static String _fixDocumento(String nrDocumento) {
		return nrDocumento != null && !nrDocumento.trim().equals("") ? nrDocumento.replaceAll("[^0-9]+", "").toUpperCase() : null;
	}
}