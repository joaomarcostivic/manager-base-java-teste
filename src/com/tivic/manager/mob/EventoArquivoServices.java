package com.tivic.manager.mob;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.radar.crypto.LocalDecrypt;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.OpencvUtils;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class EventoArquivoServices {

	public static final int TP_ARQUIVO_FOTO = 0;
	public static final int TP_ARQUIVO_VIDEO = 1;
	public static final int TP_ARQUIVO_PERFIL = 2;
	
	public static Result save(EventoArquivo eventoArquivo){
		return save(eventoArquivo, null, null);
	}

	public static Result save(EventoArquivo eventoArquivo, AuthData authData){
		return save(eventoArquivo, authData, null);
	}

	public static Result save(EventoArquivo eventoArquivo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(eventoArquivo==null)
				return new Result(-1, "Erro ao salvar. EventoArquivo é nulo");

			int retorno;
			
			EventoArquivo e = EventoArquivoDAO.get(eventoArquivo.getCdEvento(), eventoArquivo.getCdArquivo(), connect);
			
			if(e == null){
				retorno = EventoArquivoDAO.insert(eventoArquivo, connect);
				eventoArquivo.setCdArquivo(retorno);
			}
			else {
				retorno = EventoArquivoDAO.update(eventoArquivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EVENTOARQUIVO", eventoArquivo);
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
	public static Result remove(EventoArquivo eventoArquivo) {
		return remove(eventoArquivo.getCdEvento(), eventoArquivo.getCdArquivo());
	}
	public static Result remove(int cdEvento, int cdArquivo){
		return remove(cdEvento, cdArquivo, false, null, null);
	}
	public static Result remove(int cdEvento, int cdArquivo, boolean cascade){
		return remove(cdEvento, cdArquivo, cascade, null, null);
	}
	public static Result remove(int cdEvento, int cdArquivo, boolean cascade, AuthData authData){
		return remove(cdEvento, cdArquivo, cascade, authData, null);
	}
	public static Result remove(int cdEvento, int cdArquivo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = EventoArquivoDAO.delete(cdEvento, cdArquivo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_arquivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
//	public static HashMap<String, Object> importArquivos(FormDataBodyPart body){
//		return importArquivos(body, null);
//	}
//	
	public static HashMap<String, Object> importArquivos(EventoEquipamento evento, ArrayList<Arquivo> arquivos, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Result result = new Result(0);
			HashMap<String, Object> map = new HashMap<String, Object>();
			int alreadyExists = 0;

//			for (BodyPart part : body.getParent().getBodyParts()) {
//				InputStream is = part.getEntityAs(InputStream.class);
//				ContentDisposition meta = part.getContentDisposition();
//
//				PreparedStatement ps = connect.prepareStatement("SELECT COUNT(*) as total FROM grl_arquivo WHERE nm_arquivo = ?");
//				ps.setString(1, meta.getFileName());
//				ResultSet rs = ps.executeQuery();
//
//				if (rs.next()) {
//					if (rs.getInt("total") > 0) {
//						alreadyExists++;
//						continue;
//					}
//				}
//
//				result = EventoEquipamentoServices.saveCameraEventos(evento, arquivos, connect);
//			}

			map.put("QT_EXISTENTES", alreadyExists);

			if(result.getCode()<=0){
				Conexao.rollback(connect);
				return null;
			}
			else if (isConnectionNull)
				connect.commit();
			
			return map;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.importArquivos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static BufferedImage removerPlacas(byte[] file, String blur) {
		try {	
			Mat 	  mat   = Imgcodecs.imdecode(new MatOfByte(file), Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
			
			if(!blur.equals("")){
				JSONArray areas = new JSONArray(blur.split("/")[2]);

				for (int i = 0; i < areas.length(); i++) {
										
					JSONArray area = (JSONArray) areas.get(i);
				    Point[] points = new Point[4];

					for (int j = 0; j < area.length(); j++){
						points[j] = new Point(area.getJSONObject(j).getDouble("x"), area.getJSONObject(j).getDouble("y"));
					}
												    
				    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				    MatOfPoint regionMat = new MatOfPoint(points);
				    contours.add(regionMat);

				    // create mask    
				    Imgproc.drawContours(mat, contours, 0, new Scalar(169,169,169), -1);				    
				}				
			}
			
			return OpencvUtils.Mat2BufferedImage(mat);
		} catch(Exception e) {
			return null;
		}
	}
	
	public static BufferedImage removerRostos(byte[] file) {
		try {			
			Mat 	  mat    	     = Imgcodecs.imdecode(new MatOfByte(file), Imgcodecs.CV_LOAD_IMAGE_COLOR);
			ArrayList<String> patterns = new ArrayList<String>();
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_profileface.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_alt.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_alt_tree.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_alt2.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_default.xml");
						
			for (String pattern : patterns){
				CascadeClassifier faceDetector = new CascadeClassifier();
				faceDetector.load(EventoArquivoServices.class.getResource("/"+Util.getPackageRootPath()+pattern).getPath().substring(1));
				
				MatOfRect faceDetections = new MatOfRect();
		        faceDetector.detectMultiScale(mat, faceDetections, 1.3, 1, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(15, 15), new Size(50, 50));
		       
		        //System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		        
		        // Draw a bounding box around each face.
		        for (Rect rect : faceDetections.toArray()) {
		        	Mat mask = mat.submat(rect);
		        	Imgproc.GaussianBlur(mask, mask, new Size(55, 55), 55);
		        }
			}
				        
			return OpencvUtils.Mat2BufferedImage(mat);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.removerRostos: " + e);
			return null;
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean lgOpenCV = Util.getConfManager().getProps().getProperty("INIT_OPENCV_LIB").equals("1");
		ResultSetMap rsm = Search.find(
				" SELECT A.* "
						  + " FROM mob_evento_arquivo A ", 
						      criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		if(lgOpenCV) {
			while(rsm.next()) {
				Arquivo arquivo = ArquivoDAO.get(rsm.getInt("CD_ARQUIVO"), connect);
				rsm.setValueToField("facesPositions", OpencvUtils.faceDetector(arquivo.getBlbArquivo()).toString());
			}
		}
			
		return rsm;
	}
	
	
	public static Result decryptByCdEvento(int cdEvento) {
		return decryptByCdEvento(cdEvento, null);
	}

	public static Result decryptByCdEvento(int cdEvento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
				
			pstmt = connect.prepareStatement(" SELECT A.*, B.* "
					  + " FROM mob_evento_arquivo A, grl_arquivo B "
					  + " WHERE A.cd_arquivo = B.cd_arquivo "
					  + " AND A.tp_arquivo = " + TP_ARQUIVO_FOTO
					  + " AND A.cd_evento = " + cdEvento);
			
			ResultSet rsArquivos = pstmt.executeQuery();
			
			while(rsArquivos.next()) {
				byte[] blbArquivo = rsArquivos.getBytes("blb_arquivo");
				
				byte[] blbArquivoDec = LocalDecrypt.decrypt(blbArquivo);
				
				File f = new File(rsArquivos.getString("cd_arquivo")+""+System.currentTimeMillis()+".jpg");
				System.out.print("Gravando "+f.getAbsolutePath()+"... ");
				FileUtils.writeByteArrayToFile(f, blbArquivoDec);
				System.out.println("done");
			}
			
			rsArquivos.close();
			
			return new Result(1);
				
		}
		catch(BadPaddingException badPaddingExpt) {
			System.err.println("Evento: "+cdEvento+"Arquivo não está criptografado ou não foi possível descriptografar.");
			//badPaddingExpt.printStackTrace();
			return new Result(2, "Arquivo não está criptografado ou não foi possível descriptografar.");
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.decryptByCdEventoEquipamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.decryptByCdEventoEquipamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result decryptAll() {
		return decryptAll(null);
	}

	
	public static Result decryptAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}			
			
			//1. verifica se campo temporário existe
			pstmt = connect.prepareStatement("SELECT column_name FROM information_schema.columns WHERE table_name='grl_arquivo' and column_name='blb_arquivo_temp'");
			if(!pstmt.executeQuery().next()) {
				pstmt = connect.prepareStatement("ALTER TABLE grl_arquivo ADD COLUMN blb_arquivo_temp BYTEA;");
				pstmt.execute();
			}
			
			//2.selecionar todas as imagens de radares que criptografam
			pstmt = connect.prepareStatement(" SELECT A.*, B.* "
					  + " FROM mob_evento_arquivo A, grl_arquivo B, mob_evento_equipamento C "
					  + " WHERE A.cd_arquivo = B.cd_arquivo "
					  + " AND A.cd_evento = C.cd_evento "
					  + " AND C.cd_equipamento <> 403 "
					  + " AND A.tp_arquivo = " + TP_ARQUIVO_FOTO);
			
			ResultSet rsArquivos = pstmt.executeQuery();
			
			//3.executar decrypt gravando em campo temporario os bytes antigos (por enquanto) e os dados novos no campo adequado
			while(rsArquivos.next()) {
				
				System.out.print("Gravando "+rsArquivos.getInt("cd_arquivo")+"... ");
				
				byte[] blbArquivo = rsArquivos.getBytes("blb_arquivo");
				
				byte[] blbArquivoDec = LocalDecrypt.decrypt(blbArquivo);
				
				pstmt = connect.prepareStatement("UPDATE grl_arquivo SET blb_arquivo=?, blb_arquivo_temp=? WHERE cd_arquivo=?");
				pstmt.setBytes(1, blbArquivoDec);
				pstmt.setBytes(2, blbArquivo);
				pstmt.setInt(3, rsArquivos.getInt("cd_arquivo"));
				pstmt.execute();
				
//				File f = new File(rsArquivos.getString("cd_arquivo")+""+System.currentTimeMillis()+".jpg");
//				System.out.print("Gravando "+f.getAbsolutePath()+"... ");
//				FileUtils.writeByteArrayToFile(f, blbArquivoDec);
				System.out.println("done");
			}
			
			if (isConnectionNull)
				connect.commit();
			
			rsArquivos.close();
			
			return new Result(1);
				
		}
		catch(BadPaddingException badPaddingExpt) {
			Conexao.rollback(connect);
			System.err.println("Arquivo não está criptografado ou não foi possível descriptografar.");
			//badPaddingExpt.printStackTrace();
			return new Result(2, "Arquivo não está criptografado ou não foi possível descriptografar.");
		}
		catch(SQLException sqlExpt) {
			Conexao.rollback(connect);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.decryptAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoServices.decryptAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result definirImagemImpressao(int cdEvento, int cdArquivo){
		return definirImagemImpressao(cdEvento, cdArquivo, null);
	}
	
	public static Result definirImagemImpressao(int cdEvento, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			Criterios criterios = new Criterios();
			criterios.add("CD_EVENTO", String.valueOf(cdEvento), ItemComparator.EQUAL, Types.INTEGER);
			ResultSetMap rsm = EventoArquivoServices.find(criterios, connect);
			ResultSetMapper<EventoArquivo> arquivos = new ResultSetMapper<EventoArquivo>(rsm, EventoArquivo.class); 
			
			for(EventoArquivo arquivo : arquivos.toList()) {
				arquivo.setLgImpressao(arquivo.getCdArquivo() == cdArquivo ? 1 : 0);				
				save(arquivo, null, connect);
			}
						
			if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro atualizado com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao atualizar registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
}