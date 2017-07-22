import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.Date;
import java.util.Stack;
import java.util.UUID;

/**
 * Created by Ricardo on 12/10/2015.
 */
public class productos extends JFrame implements ListSelectionListener {
    private JTable table1;
    private JPanel paneMesa;
    private JButton modificarCuentaButton;
    private JList list1;
    private JList list;
    private JMenuBar menuBar;
    private JMenu menu;
    private DefaultListModel listModel;
    private static final String hireString = "Agregar";
    private static final String fireString = "Quitar";
    private JButton fireButton;
    private JTextField employeeName;
    private Stack cantidad = new Stack();
    String mesaJuntar;
    Stack listaProductos = new Stack();
    Stack productosCuenta = new Stack();
    Connection c = null;
    Statement stmt = null;

    public productos(String btnName, String idMesa, JFrame components){

        super(btnName);
        //setContentPane(paneMesa);
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem juntar = new JMenuItem("Juntar Mesas");
        juntar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                juntarMesas juntar = new juntarMesas(idMesa, components);
                mesaJuntar = juntar.mesaAJuntar;
            }
        });
        JMenuItem exit = new JMenuItem("Salir");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JMenuItem historial = new JMenuItem("Historial");
        historial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/test",
                                    "postgres", "juanote1");

                    stmt = c.createStatement();
                    PreparedStatement stmt1 = c.prepareStatement("SELECT p.nombre, p.precio,h.fecha FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" WHERE \"idMesa\" = ?;");
                    stmt1.setObject( 1,idMesa, Types.OTHER);

                    ResultSet rs = stmt1.executeQuery();
                    String historial = "";
                    while ( rs.next() ) {
                        //int id = rs.getInt("idCategoria");
                        productosObject item = new productosObject();
                        String  nombre = rs.getString("nombre");
                        String precio  = rs.getString("precio");
                        String fecha = rs.getString("fecha");
                        historial = historial + "\n" + nombre + "    Precio:  " + precio + "    Fecha:   " + fecha;
                    }
                    JOptionPane.showMessageDialog(null,historial);
                    rs.close();
                    stmt.close();
                    c.close();

                } catch (Exception e9) {
                    e9.printStackTrace();
                    System.err.println(e.getClass().getName()+": "+e9.getMessage());
                    System.exit(0);
                }
            }
        });
        menu.add(juntar);
        menu.add(exit);
        menu.add(historial);
        this.setJMenuBar(menuBar);
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "juanote1");

            stmt = c.createStatement();
            PreparedStatement stmt1 = c.prepareStatement("SELECT  p.nombre, p.precio FROM cuenta c INNER JOIN productos p ON p.id_producto=c.id_producto WHERE id_mesa = ?;");
            stmt1.setObject( 1,idMesa, Types.OTHER);

            ResultSet rs = stmt1.executeQuery();
            listModel = new DefaultListModel();

            while ( rs.next() ) {
                //int id = rs.getInt("idCategoria");
                String  name = rs.getString("nombre");
                String age  = rs.getString("precio");
                //System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "AGE = " + age );
                System.out.println();
                listModel.addElement(""+ name +"   Precio: "+ age);
            }
            rs.close();
            stmt.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        System.out.println("Operation done successfully");




        //Create the list and put it in a scroll pane.
        if(listModel.isEmpty()){
            listModel.addElement("\n");
        }
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton hireButton = new JButton(hireString);
        HireListener hireListener = new HireListener(hireButton);
        hireButton.setActionCommand(hireString);
        hireButton.addActionListener(hireListener);
        hireButton.setEnabled(true);
        hireButton.setVisible(false);

        JButton cerrarCuenta = new JButton("Cerrar Cuenta");
        JButton imprimir = new JButton("Imprimir");
        cerrarCuenta.setEnabled(true);
        cerrarCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/test",
                                    "postgres", "juanote1");

                    stmt = c.createStatement();

                    while(!listaProductos.isEmpty()){

                            stmt = c.createStatement();
                            PreparedStatement stmt0 = c.prepareStatement("INSERT INTO cuenta(id_mesa, precio, id_cuenta, id_producto)VALUES (?,?,?,?);");
                            UUID idCuenta = UUID.randomUUID();
                            productosObject item = (productosObject) listaProductos.pop();
                            stmt0.setObject( 1,item.getMesa(), Types.OTHER);
                            stmt0.setObject(2,item.getPrecio(), Types.OTHER);
                            stmt0.setObject(3,idCuenta, Types.OTHER);
                            stmt0.setObject(4,item.getIdProdcuto(), Types.INTEGER);

                            stmt0.executeUpdate();

                        }


                    PreparedStatement stmt1 = c.prepareStatement("SELECT  * FROM cuenta WHERE id_mesa = ?");
                    stmt1.setObject( 1,idMesa, Types.OTHER);

                    ResultSet rs = stmt1.executeQuery();
                    while ( rs.next() ) {
                        String idProducto = rs.getString("id_producto");
                        String idMesa = rs.getString("id_mesa");
                        String precio  = rs.getString("precio");

                        productosObject item = new productosObject();
                        item.setPrecio(precio);
                        item.setIdProdcuto(idProducto);
                        item.setMesa(idMesa);
                        productosCuenta.push(item);
                    }
                    rs.close();
                    stmt.close();
                    while (!productosCuenta.isEmpty()){
                        PreparedStatement stmt2 = c.prepareStatement("INSERT INTO historial VALUES (?,?,?,?,?,?);");
                        productosObject item = (productosObject) productosCuenta.pop();
                        stmt2.setObject( 1,item.getMesa(), Types.OTHER);
                        stmt2.setObject(2,item.getIdProdcuto(), Types.INTEGER);
                        java.util.Date date = new Date();
                        Object timestamp = new java.sql.Timestamp(date.getTime());
                        stmt2.setObject(3, timestamp, Types.OTHER);
                        stmt2.setObject(4,"NULL", Types.VARCHAR);
                        stmt2.setObject(5, item.getPrecio(), Types.OTHER);
                        UUID idHistorial = UUID.randomUUID();
                        stmt2.setObject(6, idHistorial, Types.OTHER);

                        stmt2.executeUpdate();
                        stmt2.close();
                    }

                    PreparedStatement stmt3 = c.prepareStatement("DELETE FROM cuenta WHERE id_mesa = ?");
                    stmt3.setObject(1,idMesa, Types.OTHER);

                    stmt3.executeUpdate();
                    stmt3.close();
                    c.close();
                    dispose();

                } catch (Exception e1) {
                    e1.printStackTrace();
                    System.err.println(e.getClass().getName()+": "+e1.getMessage());
                    System.exit(0);
                }
            }
        });

        fireButton = new JButton(fireString);
        fireButton.setActionCommand(fireString);
        fireButton.addActionListener(new FireListener());


        modificarCuentaButton.setActionCommand("categorias");
        modificarCuentaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                categorias categorias = new categorias(modificarCuentaButton, employeeName, hireButton, cantidad, idMesa, listaProductos);
            }
        });

        employeeName = new JTextField(10);
        employeeName.setVisible(false);
        employeeName.addActionListener(hireListener);
        String name = listModel.getElementAt(
                list.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(fireButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(cerrarCuenta);
        buttonPane.add(imprimir);
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(employeeName);
        buttonPane.add(modificarCuentaButton);
        buttonPane.add(hireButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
        employeeName.getDocument().addDocumentListener(hireListener);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                while(!listaProductos.isEmpty()){
                    try {
                        Class.forName("org.postgresql.Driver");
                        c = DriverManager
                                .getConnection("jdbc:postgresql://localhost:5432/test",
                                        "postgres", "juanote1");

                        stmt = c.createStatement();
                        PreparedStatement stmt1 = c.prepareStatement("INSERT INTO cuenta(id_mesa, precio, id_cuenta, id_producto)VALUES (?,?,?,?);");
                        UUID idCuenta = UUID.randomUUID();
                        productosObject item = (productosObject) listaProductos.pop();
                        stmt1.setObject( 1,item.getMesa(), Types.OTHER);
                        stmt1.setObject(2,item.getPrecio(), Types.OTHER);
                        stmt1.setObject(3,idCuenta, Types.OTHER);
                        stmt1.setObject(4,item.getIdProdcuto(), Types.INTEGER);

                      stmt1.executeUpdate();

                    } catch (Exception e1) {
                        e1.printStackTrace();
                        System.err.println(e.getClass().getName()+": "+e1.getMessage());
                        System.exit(0);
                    }
                }
            }
        });
        setVisible(true);

    }

    public String juntarMesas(){
        return mesaJuntar;
    }

    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                fireButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
                }
            }
        }

            //This listener is shared by the text field and the hire button.
    class HireListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public HireListener(JButton button) {
        this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
        String name = employeeName.getText();

           //User didn't type in a unique name...
           if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                employeeName.requestFocusInWindow();
                employeeName.selectAll();
                return;
                }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
                } else {//add after the selected item
                index++;
                }
            int cantidadDeProducto = Integer.parseInt(cantidad.pop().toString());
            while(cantidadDeProducto >=1){
                String producto = employeeName.getText();
                listModel.insertElementAt(producto, index);
                cantidadDeProducto = cantidadDeProducto -1;
            }
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            employeeName.requestFocusInWindow();
            employeeName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
            }

                //This method tests for string equality. You could certainly
                //get more sophisticated about the algorithm.  For example,
                //you might want to ignore white space and capitalization.
                protected boolean alreadyInList(String name) {
                return listModel.contains(name);
            }

            //Required by DocumentListener.
                public void insertUpdate(DocumentEvent e) {
                enableButton();
                }

        //Required by DocumentListener.
                public void removeUpdate(DocumentEvent e) {
                handleEmptyTextField(e);
            }

                    //Required by DocumentListener.
                public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
               enableButton();
                }
            }

                private void enableButton() {
            if (!alreadyEnabled) {
            button.setEnabled(true);
            }
            }

                private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
                }
            return false;
            }



                }

            //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable fire button.
                fireButton.setEnabled(false);

                } else {
                //Selection, enable the fire button.
                fireButton.setEnabled(true);
                }
            }
        }

    public void windowClosing(WindowEvent e)
    {
        // can do cleanup here if necessary
    }

}

