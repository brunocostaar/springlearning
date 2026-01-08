package costa.bruno.screenmatch.principal;

import costa.bruno.screenmatch.model.DadosEpisodio;
import costa.bruno.screenmatch.model.DadosSerie;
import costa.bruno.screenmatch.model.DadosTemporada;
import costa.bruno.screenmatch.service.ConsumoApi;
import costa.bruno.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=8e10c974";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para a busca.");
        var nomeserie = scanner.nextLine();
        var endereco = ENDERECO + nomeserie.replace(" ", "+") + APIKEY;
        String json = consumoApi.obterDados(endereco);
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.temporadas() ; i++) {
            endereco = ENDERECO + nomeserie.replace(" ", "+") + "&season=" + i + APIKEY;
            json = consumoApi.obterDados(endereco);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().
                forEach(e -> System.out.println(e.titulo())));
    }
}
