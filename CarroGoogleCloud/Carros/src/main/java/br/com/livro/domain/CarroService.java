package br.com.livro.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarroService {
	
	//Sem o Spring
	//private CarroDAO db = new CarroDAO();
	
	//Com o Spring
	@Autowired
	private CarroDAO db;
	
	//Lista todos os carros do banco de dados
	public List<Carro> getCarros(){
		List<Carro> carros = db.getCarros();
		return carros;
	}
	
	//Buscar um carro pelo id
	public Carro getCarro(Long id) throws SQLException {
		return db.getCarroById(id);
	}
	
	//Deleta o carro pelo id
	@Transactional(rollbackOn=Exception.class)
	public boolean delete(Long id) {
		return db.delete(id);
	}
	
	//Salva o atualiza o carro
	@Transactional(rollbackOn=Exception.class)
	public boolean save(Carro carro) {
		db.saveOrUpdate(carro);
		return true;
	}
	
	//Busca o carro pelo nome
	public List<Carro> findByName(String name){
		return db.findByName(name);
	}
	
	//Busca o carro pelo tipo
	public List<Carro> findByTipo(String tipo){
		return db.findByTipo(tipo);
	}
}
