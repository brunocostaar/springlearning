package costa.bruno.screenmatch.principal;

import costa.bruno.screenmatch.model.*;
import costa.bruno.screenmatch.repository.SerieRepository;
import costa.bruno.screenmatch.service.ConsumoApi;
import costa.bruno.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = System.getProperty("OMDBAPIKEY");
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar série por título
                5 - Buscar série por ator
                6 - Top 5 Séries
                7 - Buscar por gênero
                8 - Buscar por número de temporadas
                9 - Buscar episódios por trecho
                10 - Top 5 episódios por série
                11 - Buscar episódios a partir de uma data
                
                0 - Sair
                """;
        var opcao = -1;
        while(opcao != 0) {
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarPorTemporadas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var ano = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repository.episodioPorSerieEAno(serie, ano);
            episodiosAno.forEach(System.out::println);
        }
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s | Temporada: %s - Episódio: %s - Título: %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s | Temporada: %s - Episódio: %s - Título: %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void buscarPorTemporadas() {
        System.out.println("Qual o número máximo de temporadas que você deseja buscar?");
        var temporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Você deseja inserir uma avaliação mínima? 1 = sim, 2 = não");
        var opcao = leitura.nextInt();
        leitura.nextLine();
        Double avaliacao = 0.0;
        if (opcao == 1) {
            System.out.println("Qual a avaliação mínima?");
            avaliacao = leitura.nextDouble();
        }
        else if (opcao == 2) {
            System.out.println("Sem avaliação mínima então!");
        }
        else {
            while (opcao != 1 && opcao != 2) {
                if (opcao == 1) {
                    System.out.println("Qual a avaliação mínima?");
                    avaliacao = leitura.nextDouble();
                    break;
                } else if (opcao == 2) {
                    System.out.println("Sem avaliação mínima então!");
                    break;
                } else {
                    System.out.println("Opção inválida, digite novamente: 1 = sim, 2 = não");
                    opcao = leitura.nextInt();
                    leitura.nextLine();
                }
            }
        }

        List<Serie> seriesEncontradas = repository.seriesPorTemporadaEAvaliacao(temporadas, avaliacao);
        if(seriesEncontradas.isEmpty()){
            System.out.println("Nenhuma série encontrada");
        }else {
            System.out.println("Séries em que " + temporadas + " é o número máximo de temporadas: ");
            seriesEncontradas.forEach(s ->
                    System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        }
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero?");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Séries por categoria: " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorAtor() {
        System.out.println("Qual o nome do ator?");
        var nomeAtor = leitura.nextLine();
        System.out.println("Você deseja inserir uma avaliação mínima? 1 = sim, 2 = não");
        var opcao = leitura.nextInt();
        leitura.nextLine();
        Double avaliacao = 0.0;
        if (opcao == 1) {
            System.out.println("Qual a avaliação mínima?");
            avaliacao = leitura.nextDouble();
        }
        else if (opcao == 2) {
            System.out.println("Sem avaliação mínima então!");
        }
        else {
            while (opcao != 1 && opcao != 2) {
                if (opcao == 1) {
                    System.out.println("Qual a avaliação mínima?");
                    avaliacao = leitura.nextDouble();
                    break;
                } else if (opcao == 2) {
                    System.out.println("Sem avaliação mínima então!");
                    break;
                } else {
                    System.out.println("Opção inválida, digite novamente: 1 = sim, 2 = não");
                    opcao = leitura.nextInt();
                    leitura.nextLine();
                }
            }
        }

        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        if(seriesEncontradas.isEmpty()){
            System.out.println("Nenhuma série encontrada");
        }else {
            System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
            seriesEncontradas.forEach(s ->
                    System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        }
    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(serie);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        String nomeSerie = leitura.nextLine();
        List<DadosTemporada> temporadas = new ArrayList<>();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()) {
            Serie serieEncontrada = serie.get();
            for (int i = 1; i <= serieEncontrada.getTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                        .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        String nomeSerie = leitura.nextLine();
        serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()){
            System.out.println("Dados da série: "+ serieBusca.get());
        } else {
            System.out.println("Série não encontrada.");
        }
    }
}
