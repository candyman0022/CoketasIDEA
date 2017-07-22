import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


/**
 * Created by Ricardo on 12/9/2015.
 */
public class mainFrameMesas extends JFrame{

    private JTabbedPane tabbedPane1;
    private JPanel rootPanel;
    private JButton addButton;
    private JTextField textMesa;
    private JPanel mesaPanel;
    private JButton mesa1Button;
    static private HashMap componentMap;
    private JMenuBar menuBar;
    private JMenu menu;
    String seJuntaMesa;

    Connection c = null;
    Statement stmt = null;
    Vector<Object> columnNames = new Vector<Object>();
    Vector<Object> data = new Vector<Object>();


    public mainFrameMesas(){
        super("Coketas");
        JFrame that = this;
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem historialSemanal = new JMenuItem("Historial Semanal");
        historialSemanal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar dateCalendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.WEEK_OF_YEAR, dateCalendar.get(Calendar.WEEK_OF_YEAR));
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                java.util.Date today = new Date();
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/test",
                                    "postgres", "juanote1");

                    stmt = c.createStatement();
                    PreparedStatement stmt1 = c.prepareStatement("SELECT p.nombre, p.precio,h.fecha FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" WHERE fecha >= ? AND fecha <?;");
                    PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(precio) FROM (SELECT p.nombre, p.precio,h.fecha,c.nombre_categoria FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" INNER JOIN categoria c ON c.id_categoria = p.id_categoria  WHERE fecha >= ? AND fecha <  ?) AS hola");
                    stmt1.setObject( 1,sdf.format(cal.getTime()), Types.OTHER);
                    stmt1.setObject(2,today, Types.OTHER);
                    stmt2.setObject( 1,sdf.format(cal.getTime()), Types.OTHER);
                    stmt2.setObject(2,today, Types.OTHER);

                    ResultSet rs = stmt1.executeQuery();
                    ResultSet rs1 = stmt2.executeQuery();
                    rs1.next();
                    String historial = "TOTAL: " + rs1.getString("sum");
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
        JMenuItem historialMensual = new JMenuItem("Historial Mensual");
        historialMensual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar dateCalendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_MONTH, 01);

                java.util.Date today = new Date();
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/test",
                                    "postgres", "juanote1");

                    stmt = c.createStatement();
                    PreparedStatement stmt1 = c.prepareStatement("SELECT p.nombre, p.precio,h.fecha FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" WHERE fecha >= ? AND fecha <?;");
                    PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(precio) FROM (SELECT p.nombre, p.precio,h.fecha,c.nombre_categoria FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" INNER JOIN categoria c ON c.id_categoria = p.id_categoria  WHERE fecha >= ? AND fecha <  ?) AS hola");
                    stmt1.setObject( 1,sdf.format(cal.getTime()), Types.OTHER);
                    stmt1.setObject(2,today, Types.OTHER);
                    stmt2.setObject( 1,sdf.format(cal.getTime()), Types.OTHER);
                    stmt2.setObject(2,today, Types.OTHER);

                    ResultSet rs = stmt1.executeQuery();ResultSet rs1 = stmt2.executeQuery();
                    rs1.next();
                    String historial = "TOTAL: " + rs1.getString("sum");
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
        JMenuItem histiralDia = new JMenuItem("Historial Dia");
        histiralDia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar dateCalendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));

                Vector<Object> columnNamesReporteDia = new Vector<Object>();
                Vector<Object> data = new Vector<Object>();
                java.util.Date today = new Date();
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/test",
                                    "postgres", "juanote1");

                    stmt = c.createStatement();
                    PreparedStatement stmt1 = c.prepareStatement("SELECT p.nombre, p.precio,h.fecha FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" WHERE fecha >= ? AND fecha <?;");
                    PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(precio) FROM (SELECT p.nombre, p.precio,h.fecha,c.nombre_categoria FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" INNER JOIN categoria c ON c.id_categoria = p.id_categoria  WHERE fecha >= ? AND fecha <?) AS hola");
                    stmt1.setObject( 1,sdf.format(cal.getTime()), Types.OTHER);
                    stmt1.setObject(2,today, Types.OTHER);
                    stmt2.setObject( 1,sdf.format(cal.getTime()), Types.OTHER);
                    stmt2.setObject(2,today, Types.OTHER);

                    ResultSet rs = stmt1.executeQuery();
                    ResultSetMetaData md = rs.getMetaData();
                    ResultSet rs1 = stmt2.executeQuery();
                    rs1.next();
                    String historial = "TOTAL: " + rs1.getString("sum");

                    int columns = md.getColumnCount();
                    //Get column names
                    for(int i = 1; i <= columns; i++){
                        columnNamesReporteDia.addElement( md.getColumnName(i) );
                    }
                    //  Get row data
                    while ( rs.next() ) {
                        String  nombre = rs.getString("nombre");
                        String precio  = rs.getString("precio");
                        String fecha = rs.getString("fecha");
                        historial = historial + "\n" + nombre + "    Precio:  " + precio + "    Fecha:   " + fecha;

                        Vector<Object> row = new Vector<Object>(columns);

                        for (int i = 1; i <= columns; i++)
                        {
                            row.addElement( rs.getObject(i) );
                        }

                        data.addElement( row );

                    }
                    rs.close();
                    stmt.close();
                    c.close();

                    DefaultTableModel model = new DefaultTableModel(data, columnNamesReporteDia)
                    {
                        @Override
                        public Class getColumnClass(int column)
                        {
                            for (int row = 0; row < getRowCount(); row++)
                            {
                                Object o = getValueAt(row, column);

                                if (o != null)
                                {
                                    return o.getClass();
                                }
                            }

                            return Object.class;
                        }
                    };

                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);

                    JFrame frame = new JFrame("FrameDemo");

                    frame.add(scrollPane);

                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    frame.pack();

                    frame.setVisible(true);

                } catch (Exception e9) {
                    e9.printStackTrace();
                    System.err.println(e.getClass().getName()+": "+e9.getMessage());
                    System.exit(0);
                }
            }
        });
        JMenuItem histiralTODO = new JMenuItem("Historial todo");
        histiralTODO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar dateCalendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
                cal.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));

                Vector<Object> columnNamesReporteDia = new Vector<Object>();
                Vector<Object> data = new Vector<Object>();
                java.util.Date today = new Date();
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/test",
                                    "postgres", "juanote1");

                    stmt = c.createStatement();
                    PreparedStatement stmt1 = c.prepareStatement("SELECT p.nombre, p.precio, m.nombre_mesa,COUNT(h.fecha), h.fecha FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" INNER JOIN mesas m ON m.id_mesa = h.\"idMesa\" GROUP BY h.fecha, p.nombre, p.precio, m.nombre_mesa;");
                    PreparedStatement stmt2 = c.prepareStatement("SELECT SUM(precio) FROM (SELECT p.nombre, p.precio,h.fecha,c.nombre_categoria FROM historial h INNER JOIN productos p ON p.id_producto=h.\"idProducto\" INNER JOIN categoria c ON c.id_categoria = p.id_categoria  WHERE fecha >= ? AND fecha <?) AS hola");
                    stmt2.setObject( 1,sdf.format(cal.getTime()), Types.OTHER);
                    stmt2.setObject(2,today, Types.OTHER);

                    ResultSet rs = stmt1.executeQuery();
                    ResultSetMetaData md = rs.getMetaData();
                    ResultSet rs1 = stmt2.executeQuery();
                    rs1.next();
                    String historial = "TOTAL: " + rs1.getString("sum");

                    int columns = md.getColumnCount();
                    //Get column names
                    for(int i = 1; i <= columns; i++){
                        columnNamesReporteDia.addElement( md.getColumnName(i) );
                    }
                    columnNamesReporteDia.addElement("Cantidad");
                    //  Get row data
                    while ( rs.next() ) {
                        String  nombre = rs.getString("nombre");
                        String precio  = rs.getString("precio");
                        String fecha = rs.getString("fecha");
                        historial = historial + "\n" + nombre + "    Precio:  " + precio + "    Fecha:   " + fecha;

                        Vector<Object> row = new Vector<Object>(columns);

                        for (int i = 1; i <= columns; i++)
                        {
                            row.addElement( rs.getObject(i) );
                        }

                        data.addElement( row );

                    }
                    rs.close();
                    stmt.close();
                    c.close();

                    DefaultTableModel model = new DefaultTableModel(data, columnNamesReporteDia)
                    {
                        @Override
                        public Class getColumnClass(int column)
                        {
                            for (int row = 0; row < getRowCount(); row++)
                            {
                                Object o = getValueAt(row, column);

                                if (o != null)
                                {
                                    return o.getClass();
                                }
                            }

                            return Object.class;
                        }
                    };

                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);

                    JFrame frame = new JFrame("FrameDemo");

                    frame.add(scrollPane);

                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    frame.pack();

                    frame.setVisible(true);

                } catch (Exception e9) {
                    e9.printStackTrace();
                    System.err.println(e.getClass().getName()+": "+e9.getMessage());
                    System.exit(0);
                }
            }
        });
        menu.add(historialSemanal);
        menu.add(historialMensual);
        menu.add(histiralDia);
        menu.add(histiralTODO);
        this.setJMenuBar(menuBar);
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "juanote1");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM mesas;" );
            while ( rs.next() ) {
                String  name = rs.getString("nombre_mesa");
                String idMesa = rs.getString("id_mesa");
                JButton nuevaMesa = new JButton(name);
                nuevaMesa.setActionCommand(name);
                nuevaMesa.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        productos mesaNumero = new productos(name, idMesa, that);
                        seJuntaMesa = mesaNumero.juntarMesas();

                    }
                });
                mesaPanel.add(nuevaMesa);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+ ":  "+e.getMessage());
            System.exit(0);
        }

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/test",
                            "postgres", "juanote1");

            //Read data from table
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT p.nombre, p.promocion, p.precio , c.nombre_categoria from productos p INNER JOIN categoria c on p.id_categoria=c.id_categoria;" );
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            //Get column names
            for(int i = 1; i <= columns; i++){
                this.columnNames.addElement( md.getColumnName(i) );
            }
            //  Get row data
            while ( rs.next() ) {
                String  name = rs.getString("nombre");
                String promocion  = rs.getString("promocion");
                Double precio = rs.getDouble("precio");
                String categoria = rs.getString("nombre_categoria");
                //System.out.println( "ID = " + id );
                System.out.println( "Producto = " + name );
                //System.out.println( "AGE = " + age );
                System.out.println("promoicon = " + promocion);
                System.out.println("precio = " + precio);
                System.out.println("categoria = " + categoria);
                System.out.println();

                Vector<Object> row = new Vector<Object>(columns);

                for (int i = 1; i <= columns; i++)
                {
                    row.addElement( rs.getObject(i) );
                }

                this.data.addElement( row );

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+ ":  "+e.getMessage());
            System.exit(0);
        }

        //  Create table with database data

        DefaultTableModel model = new DefaultTableModel(this.data, this.columnNames)
        {
            @Override
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JLabel label = new JLabel(" ");
        mesaPanel.setLayout(new GridLayout(6,3));
        rootPanel.setPreferredSize(new Dimension(600, 300));
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        createComponentMap();

        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String imagePath = "table1.png";
                ImageIcon table = new ImageIcon(imagePath);

                mesaPanel.add(new JButton("Mesa "+ textMesa.getText()) );
                rootPanel.revalidate();
                validate();

                Connection c = null;

                //Insert mesa onto table
                try {
                    Class.forName("org.postgresql.Driver");
                    c = DriverManager
                            .getConnection("jdbc:postgresql://localhost:5432/test",
                                    "postgres", "juanote1");


                    PreparedStatement stmt = c.prepareStatement("INSERT INTO mesas VALUES(?,?)");
                    UUID idmesa = UUID.randomUUID();
                    System.out.println(idmesa);

                    stmt.setObject( 1,idmesa, Types.OTHER);
                    stmt.setString(2,"Mesa "+textMesa.getText());
                    stmt.executeUpdate();



                } catch (Exception e1) {
                    e1.printStackTrace();
                    System.err.println(e.getClass().getName()+": "+e1.getMessage());
                    System.exit(0);
                }
                System.out.println("Opened database successfully");
                System.out.println("Operation done successfully");

            }
        });
    }/*
    @SuppressWarnings("unchecked")
    static public <T extends Component> T getComponentByName(Window window, String name) {

        // loop through all of the class fields on that form
        for (Field field : window.getClass().getDeclaredFields()) {

            try {
                // let us look at private fields, please
                field.setAccessible(true);

                // compare the variable name to the name passed in
                if (name.equals(field.getName())) {

                    // get a potential match (assuming correct &lt;T&gt;ype)
                    final Object potentialMatch = field.get(window);

                    // cast and return the component
                    return (T) potentialMatch;
                }

            } catch (SecurityException | IllegalArgumentException
                    | IllegalAccessException ex) {

                // ignore exceptions
            }

        }

        // no match found
        return null;
    }*/

    private void createComponentMap() {
        componentMap = new HashMap<Integer,Component>();
        Component[] components = mesaPanel.getComponents();
        for (int i=0; i < components.length; i++) {
            componentMap.put(i, components[i]);
        }
    }

    static public Component getComponentByName(String name) {
        if (componentMap.containsKey(Integer.parseInt(name)-1)) {
            return (Component) componentMap.get(Integer.parseInt(name)-1);
        }
        else return null;
    }
}
