package backend.service;

import backend.model.Resultado;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Jacson - Ray
 */
public class RankingService {

    private static final String ARQUIVO = "data/ranking.csv";

    public void salvarResultado(Resultado resultado) {
        try (PrintWriter out = new PrintWriter(new FileWriter(ARQUIVO, true))) {
            out.println(resultado.getNickname() + ";" + resultado.getPontuacao());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar ranking.", e);
        }
    }

    public List<Resultado> listarRanking() {
        List<Resultado> ranking = new ArrayList<>();
        File file = new File(ARQUIVO);

        if (!file.exists()) return ranking;

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(";");

                ranking.add(new Resultado(
                        data[0],
                        Double.parseDouble(data[1])
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler ranking.", e);
        }

        ranking.sort((a, b) ->
                Double.compare(b.getPontuacao(), a.getPontuacao())
        );

        return ranking;
    }
}