package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.rest.request.filter.Criterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ConcessionarioPessoaServices {
	
	public static final String[] tiposVinculo = {"Representante","Auxiliar"};

	public static final int TP_REPRESENTANTE  = 0;
	public static final int TP_AUXILIAR       = 1;
	public static final int TP_DESIGNADO      = 2;
	
	public static final String[] stConcessionarioPessoa = {"Inativo","Ativo","Pendente"};

	public static final int ST_INATIVO  = 0;
	public static final int ST_ATIVO    = 1;
	public static final int ST_PENDENTE = 2;
	
	public static Result save(ConcessionarioPessoa concessionarioPessoa){
		return save(concessionarioPessoa, null);
	}

	public static Result save(ConcessionarioPessoa concessionarioPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(concessionarioPessoa==null)
				return new Result(-1, "Erro ao salvar. ConcessionarioPessoa é nulo");

			int retorno;
			String sql = "SELECT * FROM mob_concessionario_pessoa "+
						 " WHERE cd_concessionario <> " + concessionarioPessoa.getCdConcessionario() + 
						 "   AND cd_pessoa = " + concessionarioPessoa.getCdPessoa() +
						 "   AND st_concessionario_pessoa <> " + ST_INATIVO;
			ResultSetMap rsm = Search.find(sql + " ", "", null, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			if(rsm.next()){
				ArrayList<ItemComparator> criteriosPessoa = new ArrayList<ItemComparator>();
				ArrayList<ItemComparator> criteriosConcessionario = new ArrayList<ItemComparator>();
				
				criteriosPessoa.add(new ItemComparator("A.cd_pessoa", "" + rsm.getInt("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
				criteriosConcessionario.add(new ItemComparator("A.cd_pessoa", "" + rsm.getInt("cd_concessionario"), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmPessoa = PessoaServices.find(criteriosPessoa);
				ResultSetMap rsmConcessionario = PessoaServices.find(criteriosConcessionario);
				
				rsmPessoa.next();
				rsmConcessionario.next();
				
				String msgReturn = "";
				
				if(rsm.getInt("st_concessionario_pessoa") == ST_ATIVO){
					msgReturn = rsmPessoa.getString("nm_pessoa").toUpperCase() + " está vinculado a " + 
								rsmConcessionario.getString("nm_pessoa").toUpperCase();
				}else{
					msgReturn = rsmPessoa.getString("nm_pessoa").toUpperCase() + " está com pendência com " + 
								rsmConcessionario.getString("nm_pessoa").toUpperCase();
				}
									
				return new Result(-2, msgReturn);
			}
			
			if(concessionarioPessoa.getCdConcessionarioPessoa()==0){
				retorno = ConcessionarioPessoaDAO.insert(concessionarioPessoa, connect);
				concessionarioPessoa.setCdConcessionarioPessoa(retorno);
			}
			else {
				retorno = ConcessionarioPessoaDAO.update(concessionarioPessoa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSIONARIOPESSOA", concessionarioPessoa);
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
	
	public static Result saveDTO(ConcessionarioPessoa concessionarioPessoa, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, int cdEmpresa, int cdVinculo){
		return saveDTO(concessionarioPessoa, pessoa, pessoaFisica, pessoaEndereco, cdEmpresa, cdVinculo, null);
	}

	public static Result saveDTO(ConcessionarioPessoa concessionarioPessoa, Pessoa pessoa, PessoaFisica pessoaFisica, PessoaEndereco pessoaEndereco, int cdEmpresa, int cdVinculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(concessionarioPessoa==null)
				return new Result(-1, "Erro ao salvar. ConcessionarioPessoa é nulo");

			pessoaFisica.setCdPessoa(pessoa.getCdPessoa());
			pessoaFisica.setNmPessoa(pessoa.getNmPessoa());
			pessoaFisica.setNmEmail(pessoa.getNmEmail());
			pessoaFisica.setNrCelular(pessoa.getNrCelular());
			pessoaFisica.setNrCelular2(pessoa.getNrCelular2());
			pessoaFisica.setNrTelefone1(pessoa.getNrTelefone1());
			
			pessoaEndereco.setCdPessoa(pessoa.getCdPessoa());
			
			concessionarioPessoa.setCdPessoa(pessoa.getCdPessoa());
						
			int retorno;
			
			if (pessoa.getCdPessoa()==0) {
				retorno = PessoaServices.insert(pessoaFisica, pessoaEndereco, cdEmpresa, cdVinculo).getCode();
				if (retorno>0)
					concessionarioPessoa.setCdPessoa(retorno);
					retorno = ConcessionarioPessoaDAO.insert(concessionarioPessoa, connect);
				
			} else {
				
				retorno = PessoaServices.update(pessoaFisica, pessoaEndereco).getCode();
				
				if(retorno>0) {
					
					String nmConcessionario = getVinculoAtivo(concessionarioPessoa.getCdPessoa(), concessionarioPessoa.getTpVinculo(), connect);
					if(nmConcessionario==null){
						retorno = ConcessionarioPessoaDAO.insert(concessionarioPessoa, connect);
						concessionarioPessoa.setCdConcessionarioPessoa(retorno);
						
					}if(concessionarioPessoa.getCdConcessionarioPessoa()>0) {
						retorno = ConcessionarioPessoaDAO.update(concessionarioPessoa, connect);
					}
					else {
						return new Result(-3, "Pessoa já possui vinculo com a concessão/permissão: " + nmConcessionario);
					}
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSIONARIOPESSOA", concessionarioPessoa);
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
	
	public static String getVinculoAtivo(int cdPessoa, int tpVinculo, Connection connect){

		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT nm_pessoa FROM mob_concessionario_pessoa A " + 
											"JOIN grl_pessoa B on (A.cd_concessionario = B.cd_pessoa) " + 
											"WHERE A.cd_pessoa=? AND A.tp_vinculo = ?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, tpVinculo);
			rs = pstmt.executeQuery();
			
			if(rs.next())
				return rs.getString("NM_PESSOA");
			else
				return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.verifyPessoaAtiva: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaDAO.verifyPessoaAtiva: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdConcessionarioPessoa){
		return remove(cdConcessionarioPessoa, false, null);
	}
	public static Result remove(int cdConcessionarioPessoa, boolean cascade){
		return remove(cdConcessionarioPessoa, cascade, null);
	}
	public static Result remove(int cdConcessionarioPessoa, boolean cascade, Connection connect){
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
			retorno = ConcessionarioPessoaDAO.delete(cdConcessionarioPessoa, connect);
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
		return getAll(0, null);
	}
	public static ResultSetMap getAll(Connection connect) {
		return getAll(0, connect);
	}
	public static ResultSetMap getAllByRepresentante(int cdRepresentante) {
		return getAll(cdRepresentante, null);
	}
	public static ResultSetMap getAll(int cdRepresentante, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessionario_pessoa" +
											 " WHERE 1 = 1 "+
											 (cdRepresentante> 0 ?" AND cd_pessoa = " + cdRepresentante : ""));
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaServices.getAll: " + e);
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
		boolean findEnderecoPrincipal = false;
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findEnderecoPrincipal")){
				findEnderecoPrincipal = true;
				criterios.remove(i);
			}else
				crt.add(criterios.get(i));
		}
		return Search.find("SELECT A.*, B.NM_PESSOA AS NM_CONCESSIONARIO, C.*, D.*, E.* " + 
						   (findEnderecoPrincipal ? ", F.*, G.nm_cidade AS NM_NATURALIDADE, G.NM_CIDADE, H.SG_ESTADO, "+
						   " H.sg_estado AS SG_ESTADO_NATURALIDADE, I.nm_pais " : "")+  
						   " , J.tp_sangue, J.tp_fator_rh "+
						   " FROM mob_concessionario_pessoa A " +
						   " JOIN GRL_PESSOA B ON ( A.CD_CONCESSIONARIO = B.CD_PESSOA ) " +
						   " JOIN GRL_PESSOA C ON ( A.CD_PESSOA = C.CD_PESSOA ) " +						   
						   " LEFT OUTER JOIN grl_pessoa_fisica       D ON (C.cd_pessoa = D.cd_pessoa) " + 
						   " LEFT OUTER JOIN grl_pessoa_juridica     E ON (C.cd_pessoa = E.cd_pessoa) " +
						   //
  (findEnderecoPrincipal ? " LEFT OUTER JOIN grl_pessoa_endereco     F ON (C.cd_pessoa = F.cd_pessoa AND F.lg_principal = 1) " +						   
						   " LEFT OUTER JOIN grl_cidade          	 G ON (F.cd_cidade = G.cd_cidade) " +
						   " LEFT OUTER JOIN grl_estado          	 H ON (G.cd_estado = H.cd_estado) " +						   
						   " LEFT OUTER JOIN grl_pais                I ON (H.cd_pais = I.cd_pais) " : "") +
  						   //
						   " LEFT OUTER JOIN grl_pessoa_ficha_medica J ON (C.cd_pessoa = J.cd_pessoa) "+						   
						   " WHERE 1=1 ",
							criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getArquivos(int cdConcessao) {
		return getAllArquivosByPessoasConcessao(cdConcessao, null);
	}
	
	public static ResultSetMap getAllArquivosByPessoasConcessao(int cdConcessao) {
		return getAllArquivosByPessoasConcessao(cdConcessao, null);
	}
	
	public static ResultSetMap getAllArquivosByPessoasConcessao(int cdConcessionario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_pessoa FROM mob_concessionario_pessoa" +											 
											 " WHERE cd_concessionario = " + cdConcessionario);
			
			ResultSetMap rsm, rsmConcessionarioPessoa = new ResultSetMap(pstmt.executeQuery());
			//pega os arquivos do concessionários/permissionários
			ResultSetMap rsmArquivosByConcessao = PessoaServices.getAllArquivosOfPessoa(cdConcessionario);
			//pega os arquivos dos Representantes/Auxiliares
			while (rsmConcessionarioPessoa.next()){
				rsm = PessoaServices.getAllArquivosOfPessoa(rsmConcessionarioPessoa.getInt("cd_pessoa"));
				//acrescenta registros ao resultsetmap
				while(rsm.next())
					rsmArquivosByConcessao.addRegister(rsm.getRegister());
			}
			
				return rsmArquivosByConcessao;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaServices.getAllArquivosByPessoasConcessao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaServices.getAllArquivosByPessoasConcessao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int isValidCnh(int cdConcessionario) {
		return isValidCnh(cdConcessionario, -1, null);
	}
	
	public static int isValidCnh(int cdConcessionario, int tpVinculo) {
		return isValidCnh(cdConcessionario, tpVinculo, null);
	}
	
	public static int isValidCnh(int cdConcessionario, int tpVinculo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT B.* FROM mob_concessionario_pessoa A" +
											" LEFT OUTER JOIN grl_pessoa_fisica B ON (A.cd_concessionario = B.cd_pessoa)" +
											" WHERE cd_concessionario = ?" + 
											" AND (A.dt_inativacao IS NULL)" +
											" AND (B.dt_validade_cnh IS NOT NULL)" +
											//" AND dt_validade_cnh < CURRENT_DATE" +
											(tpVinculo>=0?" AND tp_vinculo = ?":""));
			
			pstmt.setInt(1, cdConcessionario);
			if (tpVinculo>0)
				pstmt.setInt(2, tpVinculo);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int aux = -1; // PENDENCIA DE CADASTRO
			
			while (rsm.next()) {
				GregorianCalendar dtValidade = rsm.getGregorianCalendar("DT_VALIDADE_CNH");
				
				if (dtValidade.after(new GregorianCalendar())) {
					aux = 1; // CARTEIRA VALIDA
				} else {
					aux = 0; // CARTEIRA VENCIDA
					break;
				}
			} 

			return aux;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaServices.isValidCnh: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaServices.isValidCnh: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static List<ConcessionarioPessoaDTO> findDTO(ArrayList<ItemComparator> criterios) {
		return findDTO(criterios, null);
	}

	public static List<ConcessionarioPessoaDTO> findDTO(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			List<ConcessionarioPessoaDTO> list = new ArrayList<ConcessionarioPessoaDTO>();
			
			String sql = "SELECT * FROM mob_concessionario_pessoa A" +
						" JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa)" +
						" JOIN grl_pessoa_fisica B1 ON (B.cd_pessoa = B1.cd_pessoa)" +
						" JOIN mob_concessao C ON (A.cd_concessionario = C.cd_concessionario)" +
						" WHERE 1=1";
			
			ResultSetMap rsm = Search.find(sql, "", criterios, connect, isConnectionNull);

			while (rsm.next()) {

				ConcessionarioPessoaDTO dto = new ConcessionarioPessoaDTO();
				
				ConcessionarioPessoa concessionarioPessoa = ConcessionarioPessoaDAO.get(rsm.getInt("CD_CONCESSIONARIO_PESSOA"));
				dto.setConcessionarioPessoa(concessionarioPessoa);
				
				Pessoa pessoa = PessoaDAO.get(rsm.getInt("CD_PESSOA"));
				dto.setPessoa(pessoa);
				
				PessoaFisica pessoaFisica = PessoaFisicaDAO.get(rsm.getInt("CD_PESSOA"));
				dto.setPessoaFisica(pessoaFisica);
				
				PessoaEndereco pessoaEndereco = PessoaEnderecoDAO.get(rsm.getInt("CD_PESSOA"));
				dto.setPessoaEndereco(pessoaEndereco);
				
				list.add(dto);
			}
			
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessionarioPessoaServices.findDTO: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<ConcessionarioPessoaDTO> findAuxiliares(int cdConcessao) {
		Criterios crt = new Criterios();
		crt.add("C.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
		crt.add("A.tp_vinculo", Integer.toString(TP_AUXILIAR), ItemComparator.EQUAL, Types.INTEGER);
		
		return findDTO(crt);
	}
	
	public static List<ConcessionarioPessoaDTO> findRepresentantes(int cdConcessao) {
		Criterios crt = new Criterios();
		crt.add("C.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
		crt.add("A.tp_vinculo", Integer.toString(TP_REPRESENTANTE), ItemComparator.EQUAL, Types.INTEGER);
		
		return findDTO(crt);
	}
	
}
