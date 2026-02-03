package frontend;

import backend.service.GerenciadorDados;
import backend.model.Resultado;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 *
 * @author Jacson
 */
public class TelaInicial extends JFrame {
    private JTextField campoNickname;
    private JTextArea areaTop3;

    public TelaInicial() {
        setTitle("Quiz Master - Início");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));

        JPanel painelSuperior = new JPanel(new BorderLayout());
        JLabel labelTitulo = new JLabel("BEM-VINDO AO QUIZ", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        areaTop3 = new JTextArea(5, 20);
        areaTop3.setEditable(false);
        areaTop3.setBackground(new Color(240, 240, 240));

        // Chamada para carregar o ranking assim que abre
        carregarRanking();

        painelSuperior.add(labelTitulo, BorderLayout.NORTH);
        painelSuperior.add(new JScrollPane(areaTop3), BorderLayout.CENTER);
        add(painelSuperior, BorderLayout.NORTH);

        JPanel painelCentral = new JPanel(new GridLayout(3, 1, 10, 10));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        JLabel labelNick = new JLabel("Digite seu Nickname / Apelido:", SwingConstants.CENTER);
        campoNickname = new JTextField();
        JButton btnIniciar = new JButton("INICIAR QUIZ");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIniciar.setBackground(new Color(100, 200, 100));

        painelCentral.add(labelNick);
        painelCentral.add(campoNickname);
        painelCentral.add(btnIniciar);
        add(painelCentral, BorderLayout.CENTER);

        JButton btnConfig = new JButton("Configurações");
        add(btnConfig, BorderLayout.SOUTH);
        btnConfig.addActionListener(e -> new TelaConfig().setVisible(true));

        btnIniciar.addActionListener(e -> {
            String nick = campoNickname.getText().trim();
            if (nick.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite um Nickname!");
            } else {
                GerenciadorDados gd = new GerenciadorDados();
                int qpSalvo = gd.lerConfiguracao();

                String instrucoes = "Olá " + nick + "!\n\nREGRAS DO QUIZ:\n" +
                        "1. Você responderá " + qpSalvo + " perguntas.\n" +
                        "2. Responda Verdadeiro ou Falso.\n" +
                        "3. Você tem no máximo 60 segundos por pergunta.\n" +
                        "4. Pontuação baseada na rapidez.\n\n" +
                        "Deseja iniciar agora?";

                int escolha = JOptionPane.showConfirmDialog(this, instrucoes, "Como Funciona", JOptionPane.YES_NO_OPTION);
                if (escolha == JOptionPane.YES_OPTION) {
                    this.dispose();
                    new TelaJogo(nick, qpSalvo).setVisible(true);
                }
            }
        });
    }

    private void carregarRanking() {
        GerenciadorDados gd = new GerenciadorDados();
        List<Resultado> top3 = gd.obterTop3();
        StringBuilder sb = new StringBuilder("--- TOP 3 JOGADORES ---\n");

        if (top3.isEmpty()) {
            sb.append("Ainda não há recordes registrados.");
        } else {
            for (int i = 0; i < top3.size(); i++) {
                Resultado r = top3.get(i);
                sb.append((i + 1)).append(". ").append(r.getNickname())
                        .append(": ").append(String.format("%.2f", r.getPontuacao())).append(" pts\n");
            }
        }
        areaTop3.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInicial().setVisible(true));
    }
}