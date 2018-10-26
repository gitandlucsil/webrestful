package br.com.livro.domain;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.Statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
@SuppressWarnings("unchecked")
public class CarroDAO extends HibernateDAO<Carro>{

	public CarroDAO() {
		//Informa o tipo da entidade para o Hibernate
		super(Carro.class);
	}

	public Carro getCarroById(Long id) throws SQLException {
		return super.get(id);
	}
	
	public List<Carro> findByName(String nome){
		Query q = getSession().createQuery("from Carro where lower(nome) like lower(?)");
		q.setString(0, "%"+nome+"%");
		return q.list();
	}

	public List<Carro> findByTipo(String tipo){
		Query q = getSession().createQuery("from Carro where tipo=?");
		q.setString(0,tipo);
		List<Carro> carros = q.list();
		return carros;
	}
	
	public List<Carro> getCarros(){
		Query q = getSession().createQuery("from Carro");
		List<Carro> carros = q.list();
		return carros;
	}
	
	public void salvar(Carro c){
		super.save(c);
	}

	public boolean delete(Long id){
		Carro c = get(id);
		delete(c);
		return true;
	}
}
