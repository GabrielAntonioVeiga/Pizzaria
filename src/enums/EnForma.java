package enums;

import java.util.Arrays;

public enum EnForma {
    CIRCULO("Circulo"),
    TRIANGULO("Triangulo"),
    QUADRADO("Quadrado");

    private final String nome;

    EnForma(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static EnForma findByNome(String nome) {
        return Arrays.stream(EnForma.values())
                .filter(forma -> forma.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }
}
