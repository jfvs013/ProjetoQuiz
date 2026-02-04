package backend.service;

import backend.dao.DBQuiz;
import backend.model.Resultado;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Jacson - Ray
 */
public class GerenciadorDados {

    private static final String FILE_NAME = "data/efeito-estufa.csv";
    private static final String CONFIG_FILE = "data/config.txt";
    private static final String RANKING_FILE = "data/ranking.csv";

    // ---------------- PERGUNTAS ----------------

    public List<DBQuiz> carregarPerguntas() {
        List<DBQuiz> perguntas = new ArrayList<>();
        Set<Integer> ids = new HashSet<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) {
            throw new IllegalStateException(
                    "Arquivo CSV de perguntas não encontrado."
            );
        }

        try (Scanner sc = new Scanner(file)) {
            int linha = 0;

            while (sc.hasNextLine()) {
                linha++;
                String line = sc.nextLine().trim();

                if (line.isEmpty()) continue;

                String[] data = line.split(";");

                if (data.length < 5) {
                    throw new IllegalArgumentException(
                            "Linha " + linha + ": número de colunas inválido."
                    );
                }

                int id;
                int categoria;
                boolean resposta;
                char nivel;

                try {
                    id = Integer.parseInt(data[0].trim());
                    categoria = Integer.parseInt(data[2].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            "Linha " + linha + ": ID ou categoria inválidos."
                    );
                }

                if (!ids.add(id)) {
                    throw new IllegalArgumentException(
                            "Linha " + linha + ": ID duplicado (" + id + ")."
                    );
                }

                if (!data[3].equalsIgnoreCase("V")
                        && !data[3].equalsIgnoreCase("F")) {
                    throw new IllegalArgumentException(
                            "Linha " + linha + ": resposta deve ser V ou F."
                    );
                }
                resposta = data[3].equalsIgnoreCase("V");

                nivel = Character.toUpperCase(data[4].charAt(0));
                if (nivel != 'F' && nivel != 'M' && nivel != 'D') {
                    throw new IllegalArgumentException(
                            "Linha " + linha + ": nível deve ser F, M ou D."
                    );
                }

                perguntas.add(new DBQuiz(
                        id,
                        data[1].trim(),
                        categoria,
                        resposta,
                        nivel
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao ler o arquivo CSV.", e
            );
        }

        return perguntas;
    }

    // ---------------- CONFIGURAÇÃO ----------------

    public void salvarConfiguracao(int qp) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CONFIG_FILE))) {
            out.println(qp);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao salvar configuração.", e
            );
        }
    }

    public int lerConfiguracao() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) return 10;

        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextInt()) {
                return sc.nextInt();
            }
        } catch (Exception ignored) {
        }
        return 10;
    }

    // ---------------- RANKING ----------------

    public void salvarPontuacao(String nick, double pontos) {
        try (PrintWriter out = new PrintWriter(
                new FileWriter(RANKING_FILE, true))) {
            out.println(nick + ";" + pontos);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao salvar pontuação.", e
            );
        }
    }

    public List<Resultado> obterTop3() {
        File file = new File(RANKING_FILE);
        if (!file.exists()) return new ArrayList<>();

        List<Resultado> resultados = new ArrayList<>();

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(";");

                if (data.length >= 2) {
                    try {
                        resultados.add(new Resultado(
                                data[0].trim(),
                                Double.parseDouble(data[1].trim())
                        ));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "Erro ao ler ranking.", e
            );
        }

        return resultados.stream()
                .sorted((a, b) ->
                        Double.compare(b.getPontuacao(), a.getPontuacao()))
                .limit(3)
                .collect(Collectors.toList());
    }
}