package costa.bruno.screenmatch.model;

import java.time.LocalDate;

public class Episodio {
    private Integer temporada;
    private Integer numeroEpisodio;
    private String titulo;
    private LocalDate dataLancamento;
    private Double avaliacao;

    public Episodio(Integer temporada, DadosEpisodio dadosEpisodio) {
        this.temporada = temporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();
        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (Exception e) {
            this.dataLancamento = null;
        }
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = null;
        }
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return "temporada=" + temporada +
                ", numeroEpisodio=" + numeroEpisodio +
                ", titulo='" + titulo + '\'' +
                ", dataLancamento=" + dataLancamento +
                ", avaliacao=" + avaliacao;
    }

}
