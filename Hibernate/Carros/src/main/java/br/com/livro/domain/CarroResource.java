package br.com.livro.domain;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/carros")
@Consumes(MediaType.APPLICATION_JSON+";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON+";charset=utf-8")
@Component
public class CarroResource {
	
	//Sem o Spring
	//private CarroService carroService = new CarroService();
	
	//Com o Spring
	@Autowired
	private CarroService carroService;

	@GET
	public List<Carro> get(){
		List<Carro> carros = carroService.getCarros();
		return carros;
	}
	
	@GET
	@Path("{id}")
	public Carro get(@PathParam("id") long id) throws SQLException {
		Carro c = carroService.getCarro(id);
		return c;
	}
	
	@GET
	@Path("/tipo/{tipo}")
	public List<Carro> getByTipo(@PathParam("tipo") String tipo) {
		List<Carro> carros = carroService.findByTipo(tipo);
		return carros;
	}
	
	@GET
	@Path("/nome/{nome}")
	public List<Carro> getByNome(@PathParam("nome") String nome) {
		List<Carro> carros = carroService.findByName(nome);
		return carros;
	}
	
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") long id) {
		carroService.delete(id);
		return Response.OK("Carro deletado com sucesso!");
	}
	
	@POST
	public Response post(Carro c) {
		carroService.save(c);
		return Response.OK("Carro salvo com sucesso!");
	}
	
	@PUT
	public Response put(Carro c) {
		carroService.save(c);
		return Response.OK("Carro atualizado com sucesso!");
	}
}
