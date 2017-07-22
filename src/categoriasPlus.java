import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Ricardo on 1/19/2016.
 */
public class categoriasPlus extends JFrame{

    JPanel categoriasPlusPlanel = new JPanel();
    JPanel cantidadTipoCerveza = new JPanel();
    JSpinner cantidad = new JSpinner();
    JComboBox cerveza;
    Connection c = null;
    java.util.List<String> jcomboFill = new ArrayList<>();
    Boolean ispressed = false;
    String esBotana = "c249337b-7bb1-4582-917a-588f310e1540", nombreProducto, cantidadPrecio, extraProducto, extraPrecio,extraidProducto;


    public categoriasPlus(JButton modificarCuentaButton, String idCategoria, JTextField addProducto, JButton agregar,Stack cantidadDe, String idMesa, Stack lista){
        super("Productos");

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "juanote1");

            PreparedStatement stmt = c.prepareStatement("SELECT * FROM productos WHERE id_categoria = ?;");
            PreparedStatement stmt2 = c.prepareStatement("SELECT * FROM productos WHERE id_categoria = ? EXCEPT SELECT * FROM productos where id_categoria = ?;");
            stmt.setObject(1,idCategoria, Types.OTHER);
            if(idCategoria.equals(esBotana)){
                stmt2.setObject(1,"e8d70321-2850-4983-a275-8bfa565c5ff0", Types.OTHER);
                stmt2.setObject(2,null,Types.OTHER);
            }
            else{
                stmt2.setObject(1,"7937b766-acbe-4544-a504-094fb3eaa47b", Types.OTHER);
                stmt2.setObject(2,esBotana, Types.OTHER);
            }

            ResultSet rs = stmt.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();
            while ( rs.next() ) {
                String  name = rs.getString("nombre");
                String precio = rs.getString("precio");
                String idProducto = rs.getString("id_producto");
                nombreProducto = name;
                cantidadPrecio = precio;
                JButton nuevaMesa = new JButton(name+"  "+precio);
                nuevaMesa.setActionCommand(name);
                nuevaMesa.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        modificarCuentaButton.setEnabled(true);
                        extraProducto = name;
                        extraPrecio = precio;
                        extraidProducto =idProducto;
                        addProducto.setText(name +"    "+ precio);
                        if(ispressed){
                            nuevaMesa.setBackground(null);
                            ispressed = false;
                        }
                        else{
                            nuevaMesa.setBackground(Color.pink);
                            ispressed =true;
                        }
                    }
                });
                categoriasPlusPlanel.add(nuevaMesa);
            }
            while(rs2.next()){
                String nombre = rs2.getString("nombre");
                jcomboFill.add(nombre);
            }
            cerveza = new JComboBox(jcomboFill.toArray());
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+ ":  "+e.getMessage());
            System.exit(0);
        }

        Dimension dimension = new Dimension(60,20);
        JButton ok = new JButton("Aceptar");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cantidadDe.push(Integer.parseInt(cantidad.getValue().toString()));
                if(cerveza.getSelectedIndex() != 0){
                    addProducto.setText(extraProducto + "     " + extraPrecio + "  + $5.00 ");
                }
                for(int i = 0; i < Integer.parseInt(cantidad.getValue().toString()) ; i++){
                    productosObject item = new productosObject();
                    item.setMesa(idMesa);
                    item.setIdProdcuto(extraidProducto);
                    item.setPrecio(extraPrecio);
                    lista.push(item);
                }
                agregar.doClick();
                dispose();
            }
        });

        categoriasPlusPlanel.setLayout(new GridLayout(6,2));
        setContentPane(categoriasPlusPlanel);
        cantidad.setValue(new Integer(1));
        cantidad.setPreferredSize(dimension);
        cantidadTipoCerveza.add(cantidad);
        cantidadTipoCerveza.add(cerveza);
        cantidadTipoCerveza.add(ok);
        categoriasPlusPlanel.add(cantidadTipoCerveza);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

    }
}
