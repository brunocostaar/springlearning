package costa.bruno.screenmatch.controller;

import costa.bruno.screenmatch.dto.EpisodioDTO;
import costa.bruno.screenmatch.dto.SerieDto;
import costa.bruno.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @GetMapping
    public List<SerieDto> obterSeries(){
        return serieService.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDto> top5Series(){
        return serieService.top5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDto> obterLancamentos(){
        return serieService.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDto obterPorID(@PathVariable Long id){
        return serieService.obterPorID(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id,
                                                      @PathVariable Long numero){
        return serieService.obterTemporadasPorNumero(id, numero);
    }

    @GetMapping("/categoria/{categoria}")
    public List<SerieDto> obterSeriesPorCategoria(@PathVariable String categoria){
        return serieService.obterSeriesPorCategoria(categoria);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> top5EpisodiosSerie(@PathVariable Long id){
        return serieService.top5EpisodiosSerie(id);
    }

}
