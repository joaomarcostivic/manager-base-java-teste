package com.tivic.manager.mob;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.RestData;
import sol.util.Result;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import com.tivic.sol.connection.Conexao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ImagemServices;

public class BoatImagemServices {
	
	public static final int TP_POSICAO_ANY 		= 0;
	public static final int TP_LATERAL_ESQUERDA = 1;
	public static final int TP_LATERAL_DIREITA  = 2;
	public static final int TP_FRENTE 			= 3;
	public static final int TP_TRASEIRA 		= 4;
	public static final int TP_DECLARACAO 		= 5;
	public static final int TP_CENARIO 			= 6;

	public static Result save(int cdBoat, ArrayList<BoatImagem> imagens) {
		return save(cdBoat, 0, imagens, null);
	}
	
	public static Result save(int cdBoat, int cdBoatVeiculo, ArrayList<BoatImagem> imagens) {
		return save(cdBoat, cdBoatVeiculo, imagens, null);
	}

	public static Result save(int cdBoat, int cdBoatVeiculo, ArrayList<BoatImagem> imagens, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int retorno = 0;
			for (BoatImagem imagem: imagens) {
				imagem.setCdBoat(cdBoat);
				imagem.setCdBoatVeiculo(cdBoatVeiculo);
				retorno = BoatImagemServices.save(imagem, connect).getCode();
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Imagem(s) incluídas com sucesso.");
			else
				return new Result(-1, "Erro ao incluir imagem(s).");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir imagem(s).");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static Result save(int cdBoat, ArrayList<BoatImagem> imagens, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int retorno = 0;
			for (BoatImagem imagem: imagens) {
				imagem.setCdBoat(cdBoat);
				retorno = BoatImagemServices.save(imagem, connect).getCode();
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "Imagem(s) incluídas com sucesso.");
			else
				return new Result(-1, "Erro ao incluir imagem(s).");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir imagem(s).");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(BoatImagem boatImagem){
		return save(boatImagem, null);
	}


	public static Result save(BoatImagem boatImagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(boatImagem==null)
				return new Result(-1, "Erro ao salvar. BoatImagem é nulo");

			int retorno;
			if(BoatImagemDAO.get(boatImagem.getCdImagem(), boatImagem.getCdBoat(), connect)==null){
				retorno = BoatImagemDAO.insert(boatImagem, connect);
				boatImagem.setCdImagem(retorno);
			}
			else {
				retorno = BoatImagemDAO.update(boatImagem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BOATIMAGEM", boatImagem);
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
	

	public static Result save(FormDataBodyPart cdboat, FormDataBodyPart files) {
		return save(cdboat, files, null);
	}
	
	public static Result save(FormDataBodyPart cdBoatBodyPart, FormDataBodyPart filesBodyPart, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdBoat = Integer.parseInt(cdBoatBodyPart.getEntityAs(String.class));
			
			InputStream is = filesBodyPart.getEntityAs(InputStream.class);
			BoatImagem boatImagem = new BoatImagem(0, cdBoat, ImagemServices.writeToByteArray(is).toByteArray(), TP_POSICAO_ANY, 0);

			Result result = save(boatImagem, connect);
			if(result.getCode()<0) {
				if(isConnectionNull)
					connect.rollback();
				return result;
			}
			
			if(isConnectionNull)
				connect.commit();

			return new Result(1, "Salvo com sucesso...");
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
	
	public static Result remove(BoatImagem boatImagem) {
		return remove(boatImagem.getCdImagem(), boatImagem.getCdBoat());
	}
	public static Result remove(int cdImagem, int cdBoat){
		return remove(cdImagem, cdBoat, false, null, null);
	}
	public static Result remove(int cdImagem, int cdBoat, boolean cascade){
		return remove(cdImagem, cdBoat, cascade, null, null);
	}
	public static Result remove(int cdImagem, int cdBoat, boolean cascade, AuthData authData){
		return remove(cdImagem, cdBoat, cascade, authData, null);
	}
	public static Result remove(int cdImagem, int cdBoat, boolean cascade, AuthData authData, Connection connect){
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
			retorno = BoatImagemDAO.delete(cdImagem, cdBoat, connect);
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
			
			int retorno = connect.prepareStatement("DELETE FROM mob_boat_imagem WHERE cd_boat="+cdBoat).executeUpdate();
			
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_boat_imagem");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByBoat(int cdBoat) {
		return getAllByBoat(cdBoat, null);
	}

	public static ResultSetMap getAllByBoat(int cdBoat, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {			
			pstmt = connect.prepareStatement(
				  " SELECT A.cd_boat, A.cd_imagem, A.tp_posicao, A.cd_boat_veiculo" 
				+ " FROM mob_boat_imagem A"
				+ " WHERE A.cd_boat="+cdBoat);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemServices.getAllByBoat: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemServices.getAllByBoat: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllWithBytes(int cdBoat) {
		return getAllWithBytes(cdBoat, null);
	}

	public static ResultSetMap getAllWithBytes(int cdBoat, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {			
			pstmt = connect.prepareStatement(
				  " SELECT A.* " 
				+ " FROM mob_boat_imagem A"
				+ " WHERE A.cd_boat="+cdBoat
				+ " ORDER BY A.cd_boat_veiculo");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemServices.getAllWithBytes: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BoatImagemServices.getAllWithBytes: " + e);
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
		return Search.find("SELECT * FROM mob_boat_imagem", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result upload(RestData boatImagens) {
		return upload(boatImagens, null);
	}
	
	public static Result upload(RestData boatImagens, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
	            connect = Conexao.conectar();
	            connect.setAutoCommit(false);
	        }
	        List<BoatImagemDto> imagens = extrairListaDto(boatImagens);
	        Result result = salvarListaDeImagens(imagens, connect, isConnectionNull);
	        if (isConnectionNull && result.getCode() > 0) {
	            connect.commit();
	        }
	        return result;
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
	
	private static List<BoatImagemDto> extrairListaDto(RestData boatImagens) throws Exception {
	    ObjectMapper objectMapper = new ObjectMapper();
	    return objectMapper.convertValue(
	        boatImagens.getArg("imagens"),
	        objectMapper.getTypeFactory().constructCollectionType(List.class, BoatImagemDto.class)
	    );
	}
	
	private static Result salvarListaDeImagens(List<BoatImagemDto> imagens, Connection connect, boolean rollbackOnError) throws Exception {
	    for (BoatImagemDto dto : imagens) {
	        byte[] imagemBytes = Base64.getDecoder().decode(dto.getImageBase64());
	        BoatImagem boatImagem = boatImagemBuild(dto.getCdBoat(), imagemBytes);
	        Result result = save(boatImagem, connect);
	        if (result.getCode() < 0) {
	            if (rollbackOnError) {
	                connect.rollback();
	            }
	            return result;
	        }
	    }
	    return new Result(1, "Imagem salva com sucesso.");
	}

	public static BoatImagem boatImagemBuild(int cdBoat, byte[] imagem) throws Exception{
		BoatImagem boatImagem = new BoatImagem();
		boatImagem.setCdBoat(cdBoat);
		boatImagem.setBlbImagem(imagem);
		return boatImagem;
	}
}