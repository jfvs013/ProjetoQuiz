package frontend;

import backend.dao.DBQuiz;
import backend.model.Resultado;
import backend.service.GerenciadorDados;
import backend.service.QuizService;
import backend.service.RankingService;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author Jacson
 */
public class TelaJogo extends JFrame {

    private JLabel labelPergunta;
    private JLabel labelCronometro;
    private JButton btnVerdadeiro;
    private JButton btnFalso;

    private Timer timer;
    private int tempoRestante = 60;

    private List<DBQuiz> perguntas;
    private int indiceAtual = 0;

    private String nickname;
    private int qp;

    private QuizService quizService;
    private RankingService rankingService;

    public TelaJogo(String nickname, int qpSolicitado) {
        this.nickname = nickname;

        quizService = new QuizService();
        rankingService = new RankingService();

        GerenciadorDados gd = new GerenciadorDados();
        List<DBQuiz> todasPerguntas = gd.carregarPerguntas();

        if (todasPerguntas == null || todasPerguntas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar perguntas.");
            new TelaInicial().setVisible(true);
            dispose();
            return;
        }

        Collections.shuffle(todasPerguntas);
        this.qp = Math.min(qpSolicitado, todasPerguntas.size());
        this.perguntas = new ArrayList<>(todasPerguntas.subList(0, this.qp));

        configurarJanela();
        configurarTimer();
        configurarAtalhos();
        mostrarPergunta();
    }

    private void configurarJanela() {
        setTitle("Quiz - " + nickname);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        labelCronometro = new JLabel("Tempo: 60s", SwingConstants.CENTER);
        labelCronometro.setFont(new Font("Arial", Font.BOLD, 22));
        labelCronometro.setForeground(Color.RED);
        add(labelCronometro, BorderLayout.NORTH);

        labelPergunta = new JLabel("", SwingConstants.CENTER);
        labelPergunta.setFont(new Font("Arial", Font.PLAIN, 18));
        add(labelPergunta, BorderLayout.CENTER);

        JPanel painel = new JPanel();

        btnVerdadeiro = new JButton("Verdadeiro (V)");
        btnFalso = new JButton("Falso (F)");

        btnVerdadeiro.addActionListener(e -> processarResposta(true));
        btnFalso.addActionListener(e -> processarResposta(false));

        painel.add(btnVerdadeiro);
        painel.add(btnFalso);

        add(painel, BorderLayout.SOUTH);
    }

    private void configurarAtalhos() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_V)
                    processarResposta(true);
                else if (e.getKeyCode() == KeyEvent.VK_F)
                    processarResposta(false);
            }
        });
    }

    private void configurarTimer() {
        timer = new Timer(1000, e -> {
            tempoRestante--;
            labelCronometro.setText("Tempo: " + tempoRestante + "s");

            if (tempoRestante <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Tempo esgotado!");
                indiceAtual++;
                mostrarPergunta();
            }
        });
    }

    private void mostrarPergunta() {
        if (indiceAtual >= perguntas.size()) {
            finalizarJogo();
            return;
        }

        DBQuiz p = perguntas.get(indiceAtual);

        labelPergunta.setText("<html><center>Questão "
                + (indiceAtual + 1) + "/" + qp + "<br><br>"
                + p.getQuestion() + "</center></html>");

        tempoRestante = 60;
        labelCronometro.setText("Tempo: 60s");
        timer.restart();
        requestFocusInWindow();
    }

    private void processarResposta(boolean respostaUsuario) {
        timer.stop();

        DBQuiz pergunta = perguntas.get(indiceAtual);

        boolean correta = quizService.processarResposta(
                pergunta,
                respostaUsuario,
                tempoRestante,
                qp
        );

        JOptionPane.showMessageDialog(this,
                correta ? "Resposta correta!" :
                        "Errado! Resposta correta: "
                                + (pergunta.isAnswer() ? "Verdadeiro" : "Falso"));

        indiceAtual++;
        mostrarPergunta();
    }

    private void finalizarJogo() {
        timer.stop();

        Resultado resultado = new Resultado(
                nickname,
                quizService.getPontuacao()
        );

        rankingService.salvarResultado(resultado);

        JOptionPane.showMessageDialog(this,
                "Fim de jogo!\nPontuação final: "
                        + String.format("%.2f", resultado.getPontuacao()));

        new TelaInicial().setVisible(true);
        dispose();
    }
}