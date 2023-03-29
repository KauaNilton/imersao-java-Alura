package estudos.alurastickers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        // fazer uma conexão HTTP e buscar os top 250 filmes
        String imdbKey = System.getenv("IMDB_API_KEY2");
        String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/" + imdbKey;
        URI endereco = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();

        // extrair só os dados que interessam (titulos, poster, classificação)
        var parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);

        //criação do repositorio para as figurinhas
        File diretorio = new File("figurinhas/");
        diretorio.mkdir();

        // gerando figurinhas
        var geradora = new GeradorDeFigurinhas();
        for (Map<String, String> filme : listaDeFilmes) {

            String urlImagem = filme.get("image");
            String titulo = filme.get("title");

            InputStream inputStream = new URL(urlImagem).openStream();
            String nomeArquivo = "figurinhas/" + titulo + ".png";

            geradora.criar(inputStream, nomeArquivo);

            System.out.println(titulo);
            System.out.println();


        }
    }
}
