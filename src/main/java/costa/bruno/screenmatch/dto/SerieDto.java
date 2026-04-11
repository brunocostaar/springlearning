package costa.bruno.screenmatch.dto;

import costa.bruno.screenmatch.model.Categoria;

public record SerieDto(Long id, String titulo, Integer temporadas, Double avaliacao,
                       Categoria genero, String atores, String poster, String sinopse) {
}
