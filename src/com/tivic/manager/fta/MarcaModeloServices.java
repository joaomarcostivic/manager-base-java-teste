package com.tivic.manager.fta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class MarcaModeloServices {

	public static ResultSetMap getSyncData (List<Integer> existentes) {
		return getSyncData (existentes, null);
	}
		
	public static ResultSetMap getSyncData (GregorianCalendar dtInicial) {
		return getSyncData (dtInicial, null);
	}
	
	public static ResultSetMap getSyncData (GregorianCalendar dtInicial, Connection connect) {
		
		boolean isConnectionNull = connect == null;
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			//ResultSetMap rsm = getAll(1, connect);
			
			Criterios criterios = new Criterios();
			criterios.add("dt_atualizacao", Util.formatDate(dtInicial, "YYYY-DD-MM"), ItemComparator.GREATER_EQUAL, Types.VARCHAR);
			
			ResultSetMap rsm = find(criterios);
			
			return toStrMarcas(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
		

	}
	
	public static ResultSetMap getSyncData (List<Integer> existentes, Connection connect) {
		
		boolean isConnectionNull = connect == null;
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Criterios criterios = new Criterios();
			List<String> join = existentes.stream().map(Object::toString).collect(Collectors.toList());
			criterios.add("cd_marca", ""+String.join(",", join)+"", ItemComparator.NOTIN, Types.VARCHAR);
			
			ResultSetMap rsm = find(criterios);
			
			return toStrMarcas(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
		

	}

	public static Result save(MarcaModelo marcaModelo) {
		return save(marcaModelo, null);
	}

	public static Result save(MarcaModelo marcaModelo, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if (marcaModelo == null)
				return new Result(-1, "Erro ao salvar. MarcaModelo Ã© nulo");

			int retorno;
			if (marcaModelo.getCdMarca() == 0) {
				retorno = MarcaModeloDAO.insert(marcaModelo, connect);
				marcaModelo.setCdMarca(retorno);
			} else {
				retorno = MarcaModeloDAO.update(marcaModelo, connect);
			}

			if (retorno <= 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno <= 0) ? "Erro ao salvar..." : "Salvo com sucesso...", "MARCAMODELO",
					marcaModelo);
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result remove(int cdMarca) {
		return remove(cdMarca, false, null);
	}

	public static Result remove(int cdMarca, boolean cascade) {
		return remove(cdMarca, cascade, null);
	}

	public static Result remove(int cdMarca, boolean cascade, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if (cascade) {
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if (!cascade || retorno > 0)
				retorno = MarcaModeloDAO.delete(cdMarca, connect);
			if (retorno <= 0) {
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estÃ¡ vinculado a outros e nÃ£o pode ser excluÃ­do!");
			} else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluÃ­do com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static MarcaModelo get(int cdMarca) {
		return get(cdMarca, null);
	}

	public static MarcaModelo get(int cdMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_marca_modelo WHERE cd_marca=?");
				pstmt.setInt(1, cdMarca);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new MarcaModelo(rs.getInt("cd_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							rs.getInt("tp_marca"),
							Util.convTimestampToCalendar(rs.getTimestamp("dt_atualizacao")),
							rs.getString("nr_marca"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM marca_modelo WHERE cod_marca=?");
				pstmt.setInt(1, cdMarca);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new MarcaModelo(rs.getInt("cod_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							0,
							Util.convTimestampToCalendar(rs.getTimestamp("dt_atualizacao")),
							null);
				}
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static MarcaModelo getByNrMarca(String nrMarca){
		return getByNrMarca(nrMarca, null);
	}
	
	public static MarcaModelo getByNrMarca(String nrMarca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_marca_modelo WHERE nr_marca=?");
			pstmt.setString(1, nrMarca);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				return MarcaModeloDAO.get(rs.getInt("cd_marca"));
			}
			
			return null;			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getByNrMarca: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getByNrMarca: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static ResultSetMap getAll() {
		return getAll(-1, null);
	}

	public static ResultSetMap getAll(int tpMarca) {
		return getAll(tpMarca, null);
	}

	public static ResultSetMap getAll(int tpMarca, Connection connect) {				
		boolean isConnectionNull = connect == null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
			PreparedStatement pstmt;
		try {

			String sql = "";
			
			if(!Util.isStrBaseAntiga()) {
				sql = (tpMarca >= 0 ? "SELECT DISTINCT ON(nm_marca) nm_marca, cd_marca FROM fta_marca_modelo" : "SELECT * FROM fta_marca_modelo");
			} else {
				sql = (tpMarca >= 0 ? "SELECT DISTINCT ON(nm_marca) nm_marca, cod_marca FROM marca_modelo" : "SELECT * FROM marca_modelo");
			}
			
			pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());	
			
		} catch (SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getAll: " + sqlExpt);
			return null;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getAll: " + e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MarcaModelo getByNome(String nmMarcaModelo) {
		return getByNome(nmMarcaModelo, null);
	}

	public static MarcaModelo getByNome(String nmMarcaModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_marca_modelo WHERE nm_modelo LIKE ?");
				pstmt.setString(1, "%"+nmMarcaModelo+"%");
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					return new MarcaModelo(rs.getInt("cd_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							rs.getInt("tp_marca"),
							Util.convTimestampToCalendar(rs.getTimestamp("dt_atualizacao")),
							rs.getString("nr_marca"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM marca_modelo WHERE nm_modelo LIKE ?");
				pstmt.setString(1, "%"+nmMarcaModelo+"%");
				rs = pstmt.executeQuery();

				if(rs.next()){
					return new MarcaModelo(rs.getInt("cod_marca"),
							rs.getString("nm_marca"),
							rs.getString("nm_modelo"),
							0,
							Util.convTimestampToCalendar(rs.getTimestamp("dt_atualizacao")),
							null);
				}
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getByNome: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaModeloServices.getByNome: " + e);
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
		try {
			ResultSetMap rsm = new ResultSetMap();
			
			String limit = "";
			String keyword  = "";
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
					limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
				} 
				else if (criterios.get(i).getColumn().equalsIgnoreCase("offset")) {
					limit += " OFFSET " + criterios.get(i).getValue().toString().trim();
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("keyword")) {
					keyword = criterios.get(i).getValue();
					criterios.remove(i);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cd_marca") && Util.isStrBaseAntiga()) {
					//crt.add(new ItemComparator("COD_MARCA", criterios.get(i).getValue(), criterios.get(i).getTypeComparation(), criterios.get(i).getValue().getT));
					criterios.get(i).setColumn("COD_MARCA");
					crt.add(criterios.get(i));
				}
				else {
					crt.add(criterios.get(i));
				}
			}
			
			if(!Util.isStrBaseAntiga()) {
				
				String sql = "SELECT A.*, A.nm_marca AS NM_MARCA_CARROCERIA, A.nm_modelo AS NM_MODELO_CARROCERIA, "
						+ "A.cd_marca AS CD_MARCA_CARROCERIA FROM fta_marca_modelo A WHERE 1=1";
				
				if(!keyword.equals("")) {
					sql += " AND ((UPPER(A.nm_marca) ILIKE '%"+keyword+"%' OR UPPER(A.nm_modelo) ILIKE '%"+keyword+"%')) ";
				}
				
				rsm = Search.find(sql, " ORDER BY A.cd_marca " + limit,
								crt, connect != null ? connect : Conexao.conectar(), connect == null);
			} else {
				String sql = "SELECT * FROM marca_modelo A WHERE 1=1 ";
				
				if(!keyword.equals("")) {
					sql += " AND ((UPPER(A.nm_marca) ILIKE '%"+keyword+"%' OR UPPER(A.nm_modelo) ILIKE '%"+keyword+"%')) ";
				}
				
				rsm = Search.find(sql, "", crt, connect != null ? connect : Conexao.conectar(), connect == null);
			}
			return rsm;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private static ResultSetMap toStrMarcas(ResultSetMap rsm) {
		
		if(Util.isStrBaseAntiga()) {
		
			ResultSetMap rsmOut = new ResultSetMap();
			
			while(rsm.next()) {
				MarcaModelo marcaFta = new MarcaModelo();
				
				marcaFta.setCdMarca(rsm.getInt("cd_marca"));
				marcaFta.setNmModelo(rsm.getString("nm_modelo"));
				marcaFta.setNmModelo(rsm.getString("nm_marca"));

				SimpleDateFormat spm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
				
				try {
					gc.setTime(spm.parse(rsm.getString("dt_atualizacao")));
					marcaFta.setDtAtualizacao(gc);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				com.tivic.manager.str.MarcaModelo conv = toStrMarcaModelo(marcaFta);
				
				HashMap<String, Object> hash = new HashMap<String, Object>();
				
				hash.put("cd_marca", conv.getCdMarca());
				hash.put("nm_modelo", conv.getNmModelo());
				hash.put("nm_marca", conv.getNmMarca());
				hash.put("dt_atualizacao", conv.getDtAtualizacao());
				
				rsmOut.addRegister(hash);
			}			
			
			return rsmOut;
			
		} else {
			
			return rsm;
			
		}
		
	}
	
	public static com.tivic.manager.str.MarcaModelo toStrMarcaModelo(MarcaModelo marcaModelo) {		
		return new com.tivic.manager.str.MarcaModelo(
				marcaModelo.getCdMarca(),
				marcaModelo.getNmMarca(),
				marcaModelo.getNmModelo(),
				marcaModelo.getDtAtualizacao()
				);		
	}

}
