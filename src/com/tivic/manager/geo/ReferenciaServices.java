package com.tivic.manager.geo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.geo.CamadaReferencia;
import com.tivic.manager.geo.CamadaReferenciaDAO;
import com.tivic.manager.geo.Ponto;
import com.tivic.manager.geo.PontoDAO;
import com.tivic.manager.geo.PontoServices;
import com.tivic.manager.geo.Referencia;
import com.tivic.manager.geo.ReferenciaDAO;


public class ReferenciaServices {
	
	public static int TP_REF_CAMADA = 0;
	public static int TP_REF_LOGRADOURO = 1;
	public static int TP_REF_BAIRRO = 2;
	public static int TP_REF_DISTRITO = 3;
	public static int TP_REF_MUNICIPIO = 4;
	public static int TP_REF_ESTADO = 5;
	public static int TP_REF_PAIS = 6;
	public static int TP_REF_LOCALIZACAO = 7;
	
	public static int PONTO = 0;
	public static int LINHA = 1;
	public static int POLIGONO = 2;
	
	public static Result save(Referencia referencia){
		return save(referencia, 0, null);
	}
	
	public static Result save(Referencia referencia, int cdCamada){
		return save(referencia, cdCamada, null);
	}
	
	public static Result save(Referencia referencia, int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(referencia==null)
				return new Result(-1, "Erro ao salvar. Referencia é nula");
			
			int retorno;
			if(referencia.getCdReferencia()==0){
				retorno = ReferenciaDAO.insert(referencia, connect);
				referencia.setCdReferencia(retorno);
				
				if(retorno>0 && cdCamada>0)
					retorno = CamadaReferenciaDAO.insert(new CamadaReferencia(referencia.getCdReferencia(), cdCamada), connect);
			}
			else {				
				retorno = ReferenciaDAO.update(referencia, connect);
			}
			
			if(retorno>0 && referencia.getPontos()!=null && referencia.getPontos().size()>0){
				retorno = PontoServices.removeAllByReferencia(referencia.getCdReferencia(), connect).getCode();
				for (Ponto ponto : referencia.getPontos()){
					if(retorno>0){
						ponto.setCdReferencia(referencia.getCdReferencia());
						retorno = PontoDAO.insert(ponto, connect);
					}
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			if(retorno>0)
				return new Result(referencia.getCdReferencia(), "Salvo com sucesso...");
			else
				return new Result(retorno, "Erro ao salvar...");
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
	
	public static Result saveAll(ArrayList<Referencia> referencias, int cdCamada){
		return saveAll(referencias, cdCamada, null);
	}
	
	public static Result saveAll(ArrayList<Referencia> referencias, int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(referencias==null)
				return new Result(-1, "Erro ao salvar. Array de referencias é nulo");
			
			Result result = null;
			for (Referencia r : referencias) {
				result = save(r, cdCamada, connect);
				
				if(result.getCode()<=0)
					break;
			}
			
			if(result.getCode()<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			if(result.getCode()>0)
				return new Result(1, "Salvo com sucesso...");
			else
				return new Result(result.getCode(), "Erro ao salvar...");
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
	
	public static ResultSetMap findAllByCamada(int cdCamada) {
		return findAllByCamada(cdCamada, null);
	}

	public static ResultSetMap findAllByCamada(int cdCamada, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, C.* FROM geo_referencia A " +
					" JOIN geo_camada_referencia B ON (A.cd_referencia = B.cd_referencia) " +
					" JOIN geo_camada C ON (C.cd_camada = B.cd_camada) " +
					" WHERE B.cd_camada = " + cdCamada +
					" ORDER BY nm_referencia");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.findAllByCamada: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.findAllByCamada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<Referencia> getAllByCamada(int cdCamada) {
		return getAllByCamada(cdCamada, true, null);
	}
	
	public static ArrayList<Referencia> getAllByCamada(int cdCamada, boolean lgImage) {
		return getAllByCamada(cdCamada, lgImage, null);
	}

	public static ArrayList<Referencia> getAllByCamada(int cdCamada, boolean lgImage, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_referencia FROM geo_camada_referencia WHERE cd_camada = ?");
			pstmt.setInt(1, cdCamada);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<Referencia> referencias = new ArrayList<Referencia>();
			Referencia r;
			while(rs.next()){
				r = get(rs.getInt("cd_referencia"), lgImage, connect);
				r.setPontos(PontoServices.getAllByReferencia(rs.getInt("cd_referencia"), connect));
				//r.setFormularios(FormularioServices.getAllByReferencia(rs.getInt("cd_referencia"), connect));
				//r.setAplicacoes(FormularioServices.getAllAplicacoesByReferencia(rs.getInt("cd_referencia"), connect));
				//r.setGalerias(GaleriaServices.getAllByReferencia(rs.getInt("cd_referencia"), connect));
				referencias.add(r);
			}
			rs.close();
			
			return referencias;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.getAllByCamada: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.getAllByCamada: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdReferencia){
		return remove(cdReferencia, 0, true, null);
	}
	
	public static Result remove(int cdReferencia, int cdCamada){
		return remove(cdReferencia, cdCamada, true, null);
	}
	
	public static Result removeDaCamada(int cdReferencia, int cdCamada){
		return remove(cdReferencia, cdCamada, false, null);
	}
	
	public static Result remove(int cdReferencia, int cdCamada, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cdCamada>0)
				retorno = CamadaReferenciaDAO.delete(cdReferencia, cdCamada, connect);
			else { 
				//se camada nao informada, assume que quer deletar referencia diretamente
				retorno = 1;
				cascade = true;
			}
			
			if(retorno>0 && cascade){
				retorno = PontoServices.removeAllByReferencia(cdReferencia, connect).getCode();
				if(retorno>0)
					retorno = ReferenciaDAO.delete(cdReferencia, connect);
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta Referência está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Referência excluída com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! ReferênciaServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Referência!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeAllByCamada(int cdCamada, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
						
			//pontos das referencias
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM geo_ponto WHERE cd_referencia IN " +
					"(SELECT cd_referencia FROM geo_camada_referencia WHERE cd_camada = ?)");
			pstmt.setInt(1, cdCamada);
			pstmt.executeUpdate();
			
			//camada x referencia
			pstmt = connect.prepareStatement("DELETE FROM geo_camada_referencia WHERE cd_camada=?");
			pstmt.setInt(1, cdCamada);
			pstmt.executeUpdate();
			
			//referencias
			pstmt = connect.prepareStatement("DELETE FROM geo_referencia WHERE cd_referencia IN " +
					"(SELECT cd_referencia FROM geo_camada_referencia WHERE cd_camada = ?)");
			pstmt.setInt(1, cdCamada);
			pstmt.executeUpdate();
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Referências excluídas com sucesso!");
			
		}
		catch(Exception e){
			System.out.println("Erro! ReferenciaServices.removeAllByCamada: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Referências!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Referencia get(int cdReferencia, boolean lgImage) {
		return get(cdReferencia, lgImage, null);
	}

	public static Referencia get(int cdReferencia, boolean lgImage, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_referencia WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Referencia(rs.getInt("cd_referencia"),
						rs.getString("nm_referencia"),
						rs.getInt("tp_referencia"),
						rs.getInt("tp_representacao"),
						rs.getString("txt_observacao"),
						lgImage ? rs.getBytes("img_referencia") : null,
						rs.getInt("vl_cor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static byte[] getImageBytes(int cdReferencia) {
		return getImageBytes(cdReferencia, null);
	}

	public static  byte[] getImageBytes(int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT img_referencia FROM geo_referencia WHERE cd_referencia=?");
			pstmt.setInt(1, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getBytes("img_referencia");
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.getImageBytes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReferenciaServices.getImageBytes: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
