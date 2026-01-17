package costa.bruno.screenmatch.model;

public enum Categoria {
    ACAO("Action"),
    AVENTURA("Adventure"),
    ANIMACAO("Animation"),
    BIOGRAFIA("Biography"),
    COMEDIA("Comedy"),
    CRIME("Crime"),
    DOCUMENTARIO("Documentary"),
    DRAMA("Drama"),
    FAMILIA("Family"),
    FANTASIA("Fantasy"),
    FILM_NOIR("Film Noir"),
    HISTORIA("History"),
    TERROR("Horror"),
    MUSICA("Music"),
    MUSICAL("Musical"),
    MISTERIO("Mystery"),
    ROMANCE("Romance"),
    FICCAOCIENTIFICA("Sci-Fi"),
    CURTA("Short"),
    ESPORTE("Sport"),
    SUPERHEROI("Superhero"),
    SUSPENSE("Thriller"),
    GUERRA("War"),
    FAROESTE("Western"),
    DESCONHECIDO("Unknown");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        return DESCONHECIDO;
    }
}
