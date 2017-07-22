import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by Ricardo on 2/4/2016.
 */
public class juntarMesas extends JFrame {
    JPanel root = new JPanel();
    Connection c = null;
    String mesaAJuntar;
    JFrame buttons;

    public juntarMesas(String idMesa, JFrame components){
        super("Juntar con ");
        buttons = components;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "juanote1");
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM mesas EXCEPT SELECT * FROM mesas WHERE id_mesa = ? ORDER BY orden ASC");
            stmt.setObject(1,idMesa, Types.OTHER);
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                String  name = rs.getString("nombre_mesa");
                String idmesa = rs.getString("id_mesa");
                String orden = rs.getString("orden");
                JButton nuevaMesa = new JButton(name);
                nuevaMesa.setActionCommand(name);
                nuevaMesa.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mesaAJuntar = name;

                        JButton button = (JButton) mainFrameMesas.getComponentByName(orden);
                        button.setEnabled(false);
                    }
                });
                root.add(nuevaMesa);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+ ":  "+e.getMessage());
            System.exit(0);
        }
        root.setLayout(new GridLayout(6,3));
        root.setPreferredSize(new Dimension(600, 300));
        setContentPane(root);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    public String mesaJuntar(){
        return mesaAJuntar;
    }

}
