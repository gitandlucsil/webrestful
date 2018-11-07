package br.com.livro.domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
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
	@Autowired
	private UploadService uploadService;

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
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postFoto(final FormDataMultiPart multiPart) {
		if(multiPart != null && multiPart.getFields() != null) {
			Set<String> keys = multiPart.getFields().keySet();
			for(String key : keys) {
				//Obtem a InputStream para ler o arquivo
				FormDataBodyPart field = multiPart.getField(key);
				InputStream in = field.getValueAs(InputStream.class);
				try {
					//Salva o arquivo
					String fileName = field.getFormDataContentDisposition().getFileName();
					String path = uploadService.upload(fileName, in);
					System.out.println("Arquivo: "+path);
					return Response.OK("Arquivo recebido com sucesso!");
				}catch(IOException e) {
					e.printStackTrace();
					return Response.Error("Erro ao enviar o arquivo.");
				}
			}
		}
		return Response.OK("Requisição inválida");
	}
	
	@POST
	@Path("/toBase64")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String toBase64(final FormDataMultiPart multiPart) {
		if(multiPart != null) {
			Set<String> keys = multiPart.getFields().keySet();
			for(String key : keys) {
				try {
					//Obtem a InputStream para ler o arquivo
					FormDataBodyPart field = multiPart.getField(key);
					InputStream in = field.getValueAs(InputStream.class);
					byte[] bytes = IOUtils.toByteArray(in);
					String base64 = Base64.getEncoder().encodeToString(bytes);
					return base64;
				}catch(Exception e) {
					e.printStackTrace();
					return "Erro: " + e.getMessage();
				}
			}
		}
		return "Requisição inválida";
	}
	
	@POST
	@Path("/postFotoBase64")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postFotoBase64(@FormParam("fileName") String fileName,@FormParam("base64") String base64) {
		if(fileName != null && base64 != null) {
				try {
					//Decode: converter o Base64 para array de bytes
					byte[] bytes = Base64.getDecoder().decode(base64);
					InputStream in = new ByteArrayInputStream(bytes);
					//Faz o upload (salvar o arquivo em uma pasta)
					String path = uploadService.upload(fileName, in);
					System.out.println("Arquivo: "+path);
					//OK
					return Response.OK("Arquivo recebido com sucesso!");
				}catch(Exception e) {
					e.printStackTrace();
					return Response.Error("Erro ao enviar o arquivo");
				}	
		}
		return Response.Error("Requisição inválida");
	}
}
