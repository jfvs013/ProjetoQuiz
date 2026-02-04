package backend.model;

/**
 *
 * @author Jacson - Ray
 */
public class Resultado {

    private String nickname;
    private double pontuacao;

    public Resultado(String nickname, double pontuacao) {
        this.nickname = nickname;
        this.pontuacao = pontuacao;
    }

    public String getNickname() {
        return nickname;
    }

    public double getPontuacao() {
        return pontuacao;
    }

    @Override
    public String toString() {
        return nickname + ";" + pontuacao;
    }
}