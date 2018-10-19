package br.com.livro.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.livro.domain.Carro;
import br.com.livro.domain.CarroService;
import br.com.livro.domain.ListaCarros;
import br.com.livro.domain.Response;
import br.com.livro.util.JAXBUtil;
import br.com.livro.util.RegexUtil;
import br.com.livro.util.ServletUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet("/carros/*")
public class CarroServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private CarroService carroService = new CarroService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException{
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		if(id != null) {
			//Informou o id
			Carro carro = carroService.getCarro(id);
			if(carro != null) { //Retorna o carro do respectivo id
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(carro);
				ServletUtil.writeJSON(resp,json);
			}else { 
				resp.sendError(404,"Carro não encontrado!");
			}
		}else {//Retorna a lista de carros
			List<Carro> carros = carroService.getCarros();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(carros);
			ServletUtil.writeJSON(resp,json);
		}
		//-----------Gera o JSON---------------

		//Converter JSON para objeto
		//List<Carro> listaobj = (List<Carro>) gson.fromJson(json, ListaCarros.class);
		//-----------Gera o XML---------------
		//String xml = JAXBUtil.toXML(lista);
		//Escreve  o XML na response do servlet com application/xml
		//ServletUtil.writeXML(resp,xml);
		//-----------Gera em texto puro----------
		//String carrosString = carros.toString();
		//resp.getWriter().write(carrosString);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException{
		//Cria o carro
		Carro carro = getCarroFromRequest(req);
		//Salva o carro
		carroService.save(carro);
		//Escreve o JSON do novo carro salvo
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(carro);
		ServletUtil.writeJSON(resp, json);
	}

	//Lê os parâmetros da request e cria o objeto Carro
	private Carro getCarroFromRequest(HttpServletRequest req) {
		Carro c = new Carro();
		String id = req.getParameter("id");
		if(id != null) {
			//Se informou o id, busca o objeto do baco de dados
			c = carroService.getCarro(Long.parseLong(id));
		}
		c.setNome(req.getParameter("nome"));
		c.setDesc(req.getParameter("descricao"));
		c.setUrlFoto(req.getParameter("url_foto"));
		c.setUrlVideo(req.getParameter("url_video"));
		c.setLatitude(req.getParameter("latitude"));
		c.setLongitude(req.getParameter("longitude"));
		c.setTipo(req.getParameter("tipo"));
		return c;
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String requestUri = req.getRequestURI();
		Long id = RegexUtil.matchId(requestUri);
		if (id != null) {
			carroService.delete(id);
			Response r = Response.OK("Carro excluído com sucesso");
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(r);
			ServletUtil.writeJSON(resp, json);
		} else {
			// URL inválida
			resp.sendError(404, "URL inválida");
		}
	}
}
