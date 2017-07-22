import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Stack;

/**
 * Created by Ricardo on 1/19/2016.
 */
public class categorias extends JFrame{

    JPanel categoriasPanel = new JPanel();
    Connection c = null;

    public categorias(JButton modificarCuentaButton, JTextField addProducto, JButton agregar, Stack cantidad, String idMesa, Stack lista){

        super("Categorias");

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "juanote1");

            PreparedStatement stmt = c.prepareStatement("SELECT * FROM categoria EXCEPT SELECT * from categoria WHERE id_categoria = ?;" );
            stmt.setObject(1,"e8d70321-2850-4983-a275-8bfa565c5ff0", Types.OTHER);
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                String  name = rs.getString("nombre_categoria");
                String idCategoria = rs.getString("id_categoria");
                JButton nuevaMesa = new JButton(name);
                nuevaMesa.setActionCommand(name);
                nuevaMesa.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(false){}
                        categoriasPlus mesaNumero = new categoriasPlus(modificarCuentaButton, idCategoria, addProducto, agregar, cantidad, idMesa, lista);
                        dispose();
                    }
                });
                categoriasPanel.add(nuevaMesa);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+ ":  "+e.getMessage());
            System.exit(0);
        }

        categoriasPanel.setLayout(new GridLayout(6,3));
        setContentPane(categoriasPanel);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
