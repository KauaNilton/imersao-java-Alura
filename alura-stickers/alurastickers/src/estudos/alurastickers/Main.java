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

        // exibir e manipular os dados
       /* for (Map<String, String> filme : listaDeFilmes) {
            System.out.println("\u001b[1mTítulo: \u001b[93m" + filme.get("title") + "\u001b[0m");
            System.out.println("\u001b[1mPoster: " + filme.get("image") + "\u001b[0m");
            System.out.println("\u001b[38;2;255;255;255m\u001b[48;2;9;46;132mClassificação: " + filme.get("imDbRating") + "\u001b[0m");
            double classificacao = Double.parseDouble(filme.get("imDbRating"));
            int numeroEstrelas = (int) classificacao;
            for(int i = 0; i < numeroEstrelas; i++) {
                System.out.print("\u2B50");
            }

            System.out.println("\n");
        }*/

        File diretorio = new File("figurinhas/");
        diretorio.mkdir();
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
