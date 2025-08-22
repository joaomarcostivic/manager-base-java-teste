package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.validation.Validators;

import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class OrgaoServicoServices {
	
	OrgaoServicoDAO _orgaoServicoDAO = new OrgaoServicoDAO();
	
	public OrgaoServicoServices() {}
	
	public Result save(OrgaoServico orgaoServico) throws ValidationException {
		return save(orgaoServico, null);
	}
	
	public Result save(OrgaoServico orgaoServico, Connection conn) throws ValidationException {
		
		Validators<OrgaoServico> validators = new Validators<OrgaoServico>(orgaoServico).put(new OrgaoServicoValidator());
		Optional<String> error = validators.validateAll();				
		
		if(error.isPresent())
			throw new ValidationException(error.get());
		
		int result;
		if(orgaoServico.getCdOrgaoServico() <= 0)
			result = this._orgaoServicoDAO.insert(orgaoServico);
		else
			result = this._orgaoServicoDAO.update(orgaoServico);
		
		return new Result(result, (result <= 0) ? "Erro ao salvar..." : "Salvo com sucesso !", "ORGAO_SERVICO", orgaoServico);
	}
	
	public List<OrgaoServico> getList() throws SQLException, Exception {
		return getList(null);
	}
	
	public List<OrgaoServico> getList(Connection conn) throws SQLException, Exception {
		ResultSetMap _rsm = this._orgaoServicoDAO.getAll();		
		List<OrgaoServico> _list = new ResultSetMapper<OrgaoServico>(_rsm, OrgaoServico.class).toList();
				
		return _list;
	}
	
	public List<OrgaoServico> getByOrgao(int cdOrgao) {
		return getByOrgao(cdOrgao, null);
	}
	
	public List<OrgaoServico> getByOrgao(int cdOrgao, Connection conn) {
		boolean isConnectionNull = conn == null;
		
		try {

			if(isConnectionNull)
				conn = Conexao.conectar();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM mob_orgao_servico WHERE cd_orgao = ?");
			pstmt.setInt(1, cdOrgao);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			if(_rsm.next()) {
				List<OrgaoServico> _list = new ResultSetMapper<OrgaoServico>(_rsm, OrgaoServico.class).toList();			
				return _list;
			}
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			System.err.println("Erro: OrgaoServicoServices.getByOrgao: " + ex);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(conn);
		}
	}
	
	public List<OrgaoServico> getByCdOrgaoServico(int cdOrgaoServico) {
		return getByCdOrgaoServico(cdOrgaoServico, null);
	}
	
	public List<OrgaoServico> getByCdOrgaoServico(int cdOrgaoServico, Connection conn) {
		boolean isConnectionNull = conn == null;
		
		try {
			if(isConnectionNull)
				conn = Conexao.conectar();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM mob_orgao_servico WHERE cd_orgao_servico = ?");
			pstmt.setInt(1, cdOrgaoServico);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			if(_rsm.next()) {
				List<OrgaoServico> _list = new ResultSetMapper<OrgaoServico>(_rsm, OrgaoServico.class).toList();			
				return _list;
			}
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			System.err.println("Erro: OrgaoServicoServices.getByCdOrgaoServico: " + ex);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(conn);
		}
	}
	
	public OrgaoServico getByNmServico(String nmServico) {
		return getByNmServico(nmServico, null);
	}
	
	public OrgaoServico getByNmServico(String nmServico, Connection conn) {
		boolean isConnectionNull = conn==null;
		
		try {
			if(isConnectionNull)
				conn = Conexao.conectar();
			
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM mob_orgao_servico WHERE nm_orgao_servico = ?");
			pstmt.setString(1, nmServico);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			if(_rsm.next()) {
				List<OrgaoServico> _list = new ResultSetMapper<OrgaoServico>(_rsm, OrgaoServico.class).toList();	
				return _list.get(0);
			}
			
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			System.err.println("Erro: OrgaoServicoServices.getByNmServico: " + ex);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(conn);
		}
	}
	
}
