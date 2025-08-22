package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.str.ait.AitDuplicidadeDatabaseFactory;
import com.tivic.manager.str.ait.AitIdentifierFactory;
import com.tivic.manager.str.ait.IAitVerificadorDuplicidade;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.Conexao;

import sol.util.Result;

public class AitSyncServices extends AitServices {
	
	public static Result sync(ArrayList<Ait> aits) {
		return sync(aits, null);
	}
	
	public static Result sync(ArrayList<Ait> aits, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(aits.size()>1) {
				for (Ait ait : aits) {
					for (Ait aitCheck : aits) {
						if(ait!=aitCheck && ait.getNrAit()==aitCheck.getNrAit()) {
							aits.remove(ait);
							break;
						}
					}
				}
			}
			
			ArrayList<Ait> aitsRetorno = new ArrayList<Ait>();
			
			// divide as ait em lote 
//			int done = 0;
//			int left = aits.size();
//			int lote = ManagerConf.getInstance().getAsInteger("AIT_LOTE_SYNC", aits.size()); //aits.size();
			
			int retorno = 0;
			
			for (Ait ait: aits) {				
				Result r = sync(ait, connect);
				
				if(r.getCode() < 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);						
					return new Result(r.getCode(), "Erro ao sincronizar AIT No. "+ait.getNrAit());
				}	
				
				aitsRetorno.add((Ait)r.getObjects().get("AIT"));
			}
			
			if (isConnectionNull)
				connect.commit();
			
			//Result r = new Result(retorno, retorno>0 ? "Sincronizado " + (aits.size() == aitsRetorno.size() ? " com sucesso." : " parcialmente.") : "Erro ao sincronizar AITs.");
						
			return new Result(1, "Sincronizado " + (aits.size() == aitsRetorno.size() ? " com sucesso." : " parcialmente."));
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AITs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result sync(Ait ait, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			com.tivic.manager.mob.Infracao infracao = com.tivic.manager.mob.InfracaoDAO.get(ait.getCdInfracao(), connect);
			
			if(infracao == null && !isAitPendenteOuCancelado(ait)) {
				System.out.println("\n\t> AIT "+ait.getIdAit()+" ignorado por falta da indicação de infração.");
				ait.setCdAit(1);
				Result r = new Result(1, "", "AIT", ait);
				return r;
			}
			
			System.out.println("\n["+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy HH:mm")+"] Recebendo AIT...");
			System.out.println("\tNr. AIT: "+ait.getNrAit());
			System.out.println("\tPlaca: "+ait.getNrPlaca());
			System.out.println("\tAgente: "+com.tivic.manager.mob.AgenteDAO.get(ait.getCdAgente(), connect).getNmAgente());
			System.out.println("\tInfracao: "+(infracao != null ? infracao.getNrCodDetran() : "Sem infração, possívelmente cancelada."));
			System.out.println("\tLocalizacao: ["+ait.getVlLatitude()+", "+ait.getVlLongitude()+"]");
			
			int retorno = 0;
			
			IAitVerificadorDuplicidade verificador = new AitDuplicidadeDatabaseFactory().verificador();
			Ait aitDuplicada = verificador.findByIdAit(ait, connect).get();
				
			if(aitDuplicada != null) {
				retorno = 2;
				System.out.println("Diagnostico: AIT Duplicada...");
			}
			else {

				ait.setCdAit(0);
				
				if(infracao != null) {
					ait.setVlMulta(infracao.getVlInfracao());
				}
				
				retorno = insert(ait, connect);
				
				if(retorno > 0) {
					System.out.println("Diagnostico: AIT Recebida...");					
					ait.setCdAit(retorno);
				}
				else {
					System.out.println("Diagnostico: Erro ao inserir...");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			Result result = new Result(retorno, retorno>0 ? "Sincronizado com sucesso." : "Erro ao sincronizar AITs.");
			result.addObject("AIT", ait);
			
			return result;
		}
		catch(Exception e) {
			System.out.println("Diagnostico: Erro na sincronizacao...");
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao sincronizar AITs");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insert(Ait objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			int code = 0;
			objeto.setCdAit(code);
			
			compress(objeto.getImagens());
			
			Result result = save(objeto, null, null, null, null, connect);
			
			if(result.getCode() <= 0) {
				System.out.println(result.getMessage());
				throw new Exception(result.getMessage());	
			}
						
			code = ((Ait) result.getObjects().get("AIT")).getCdAit();
			
			if(code<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitServices.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static void compress(List<AitImagem> aitImagem) {
		long totalSize = aitImagem.stream()
			    .mapToLong(imagem -> imagem.getBlbImagem().length)
			    .sum();
		
		if(aitImagem !=null && aitImagem.size()> 0 && totalSize > 100 * 1024) {
			for (AitImagem imagem : aitImagem) {
				System.out.println("Comprimindo imagem...");
				imagem.setBlbImagem(ImagemServices.compress(imagem.getBlbImagem()));
			}				
		}
	}
	
	public static Result validarSync(ArrayList<String> aitIdentifiers) {
		return validarSync(aitIdentifiers, null);
	}
	
	public static Result validarSync(ArrayList<String> aitIdentifiers, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ArrayList<String> aitsNaoEncontradas = new ArrayList<String>();
			
			for (String aitIdentifier : aitIdentifiers) {
					
				if(new AitIdentifierFactory().verificador(aitIdentifier, connect).get() == null) {
					aitsNaoEncontradas.add(aitIdentifier);
				}
			}
			
			Result r = new Result(1);
			r.setCode(aitsNaoEncontradas.size()>0 ? -1 : 1);
			r.setMessage(aitsNaoEncontradas.size()>0 ? aitsNaoEncontradas.size()+" AIT(s) nao foram validada(s)." : "");
			r.addObject("AITS", aitsNaoEncontradas);
			
			return r;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Result(-2, "Erro ao validar AITs");
		}
	}
	
	public static boolean isAitPendenteOuCancelado(Ait ait) {
		return Arrays.asList(AitServices.ST_PENDENTE_CONFIRMACAO, AitServices.ST_CANCELADO).contains(ait.getStAit());
	}

}
