package backend.service;

import backend.dao.DBQuiz;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Jacson - Ray
 */
public class QuizService {

    private double pontuacaoTotal = 0.0;

    public void embaralharPerguntas(List<DBQuiz> perguntas) {
        Collections.shuffle(perguntas);
    }

    public int converterDificuldade(char nivel) {
        switch (Character.toUpperCase(nivel)) {
            case 'F': return 1;
            case 'M': return 2;
            case 'D': return 3;
            default:
                throw new IllegalArgumentException("Nível inválido: " + nivel);
        }
    }

    /**
     * Processa a resposta do usuário e acumula a pontuação.
     */
    public boolean processarResposta(DBQuiz pergunta,
                                     boolean respostaUsuario,
                                     int tempoGasto,
                                     int quantidadePerguntas) {

        if (tempoGasto <= 0) tempoGasto = 1;

        if (pergunta.isAnswer() != respostaUsuario) {
            return false;
        }

        int nd = converterDificuldade(pergunta.getLevel());

        double pontos = (1200.0 * nd) / (tempoGasto * quantidadePerguntas);
        pontuacaoTotal += pontos;

        return true;
    }

    public double getPontuacao() {
        return pontuacaoTotal;
    }
}