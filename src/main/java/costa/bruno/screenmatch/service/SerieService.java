package costa.bruno.screenmatch.service;

import costa.bruno.screenmatch.dto.EpisodioDTO;
import costa.bruno.screenmatch.dto.SerieDto;
import costa.bruno.screenmatch.model.Categoria;
import costa.bruno.screenmatch.model.Serie;
import costa.bruno.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDto> obterTodasAsSeries() {
        return converteDados(repository.findAll());
    }

    public List<SerieDto> top5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDto> converteDados(List<Serie> series) {
        return series
                .stream()
                .map(s -> new SerieDto(s.getId(), s.getTitulo(), s.getTemporadas(),
                        s.getAvaliacao(), s.getGenero(), s.getAtores(),
                        s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterLancamentos() {
        return converteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    public SerieDto obterPorID(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDto(s.getId(), s.getTitulo(), s.getTemporadas(),
                    s.getAvaliacao(), s.getGenero(), s.getAtores(),
                    s.getPoster(), s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repository.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return repository.obterEpisodiosPorTemporada(id, numero).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDto> obterSeriesPorCategoria(String categoria) {
        Categoria generoCategoria = Categoria.fromPortugues(categoria);
        return converteDados(repository.findByGenero(generoCategoria));
    }

    public List<EpisodioDTO> top5EpisodiosSerie(Long id) {
        return repository.topEpisodiosPorSerie(id).stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }
}
