package frontend;

import backend.service.GerenciadorDados;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Jacson
 */
public class TelaConfig extends JFrame {
    private JSpinner spinnerQuantidade;

    public TelaConfig() {
        GerenciadorDados gd = new GerenciadorDados();
        int valorAtual = gd.lerConfiguracao();

        setTitle("Configurações");
        setSize(300, 200);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setLocationRelativeTo(null);

        add(new JLabel("Quantidade de Perguntas (5-20):"));

        SpinnerModel model = new SpinnerNumberModel(valorAtual, 5, 20, 1);
        spinnerQuantidade = new JSpinner(model);
        add(spinnerQuantidade);

        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            int valor = (int) spinnerQuantidade.getValue();
            gd.salvarConfiguracao(valor);
            JOptionPane.showMessageDialog(this, "Configuração salva: " + valor + " perguntas.");
            this.dispose();
        });
    }
}