package warehouse;

import javax.swing.*;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import database.Database;

public class WarehouseInterface extends JFrame{

    public void addToStock(String id, String qty){
        Statement st = null;
        try {
            st = con.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ResultSet rs = null;
        try {
            rs = Database.executeSelectQuery("SELECT  * FROM construction.instock WHERE p_id = " + id,con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        int totalQty = 0;
        try {
            if (!rs.next())
            {
                st.executeUpdate("INSERT INTO construction.instock VALUES(" + id + "," + qty + ")" );
            }
            else
            {
                totalQty = Integer.parseInt(rs.getString("stock")) + Integer.parseInt(qty);
                System.out.println(totalQty);
                st.executeUpdate("UPDATE construction.instock SET instock.stock = "+ String.valueOf(totalQty)  + " WHERE construction.instock.p_id = " + id);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    Connection con = Database.getConnection();

    private JTable scheduledTable;
    public WarehouseInterface() throws SQLException, ClassNotFoundException {

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(800,600));
        getContentPane().setLayout(null);

        JLabel titleWarehouse = new JLabel("Warehouse Panel\r\n");
        titleWarehouse.setHorizontalAlignment(SwingConstants.CENTER);
        titleWarehouse.setBounds(296, 11, 250, 23);
        getContentPane().add(titleWarehouse);

        JScrollPane tablePanel = new JScrollPane();
        tablePanel.setBounds(33, 45, 790, 409);
        tablePanel.setLayout(null);
        getContentPane().add(tablePanel);



        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Column 1");
        model.addColumn("Column 2");
        model.addRow(new Object[]{"Row 1, Column 1", "Row 1, Column 2"});
        model.addRow(new Object[]{"Row 2, Column 1", "Row 2, Column 2"});

        scheduledTable = new JTable();
        scheduledTable.setFillsViewportHeight(false);
        scheduledTable.setEnabled(false);
        scheduledTable.setBounds(34, 11, 700, 387);

        tablePanel.setViewportView(scheduledTable);
        tablePanel.add(scheduledTable);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(33, 465, 790, 62);
        getContentPane().add(buttonPanel);

        JButton showProducts = new JButton("Show Products");
        showProducts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int product_id;
                String color;
                String texture;
                System.out.println(con);
                DefaultTableModel product_model = new DefaultTableModel();
                try {
                    ResultSet rs = Database.executeSelectQuery("SELECT * FROM construction.products",con);
                    product_model.addColumn("Product ID");
                    product_model.addColumn("Color");
                    product_model.addColumn("Texture");

                    product_model.setColumnIdentifiers(new Object[]{"ID","Color","Texture"});

                    while (rs.next())
                    {
                        product_id = rs.getInt("p_id");
                        color = rs.getString("color");
                        texture = rs.getString("texture");
                        product_model.addRow(new Object[]{product_id,color,texture});
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                scheduledTable.setModel(product_model);
            }
        });
        buttonPanel.add(showProducts);

        JButton showContracts = new JButton("Show Contracts");
        showContracts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int product_id;
                String color;
                String texture;
                String date;
                int qty;
                System.out.println(con);
                DefaultTableModel product_model = new DefaultTableModel();
                try {
                    ResultSet rs = Database.executeSelectQuery("SELECT schedule.p_id,products.color,products.texture, schedule.shipping_date, schedule.qty FROM construction.schedule INNER JOIN construction.products ON schedule.p_id = products.p_id",con);
                    product_model.addColumn("Product ID");
                    product_model.addColumn("Color");
                    product_model.addColumn("Texture");
                    product_model.addColumn("Date");
                    product_model.addColumn("Qty");

                    product_model.setColumnIdentifiers(new Object[]{"ID","Color","Texture","Date","Qty"});

                    while (rs.next())
                    {
                        product_id = rs.getInt("p_id");
                        color = rs.getString("color");
                        texture = rs.getString("texture");
                        date = rs.getString("shipping_date");
                        qty = rs.getInt("qty");
                        product_model.addRow(new Object[]{product_id,color,texture,date,qty});
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                scheduledTable.setModel(product_model);
            }

        });
        buttonPanel.add(showContracts);


        JButton showStock = new JButton("Show Stock");
        showStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int product_id;
                int qty;
                String color;
                String texture;
                System.out.println("con");
                DefaultTableModel product_model = new DefaultTableModel();
                try {
                    ResultSet rs = Database.executeSelectQuery("SELECT * FROM construction.instock INNER JOIN construction.products ON instock.p_id = products.p_id ",con);
                    product_model.addColumn("Product ID");
                    product_model.addColumn("Color");
                    product_model.addColumn("Texture");

                    product_model.addColumn("Qty");

                    product_model.setColumnIdentifiers(new Object[]{"ID","Color","Texture","Qty"});

                    while (rs.next())
                    {
                        product_id = rs.getInt("p_id");
                        color = rs.getString("color");
                        texture = rs.getString("texture");
                        qty = rs.getInt("stock");
                        product_model.addRow(new Object[]{product_id,color,texture,qty});
                    }
                    scheduledTable.setModel(product_model);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        buttonPanel.add(showStock);

        JButton addStock = new JButton("Add Stock");
        addStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStock.doClick();
                String id = JOptionPane.showInputDialog(null,"Enter the product ID you want to add stock of ");
                String qty = JOptionPane.showInputDialog(null,"Enter the product qty you want to add stock of ");
                addToStock(id,qty);
                showStock.doClick();



            }
        });
        buttonPanel.add(addStock);

        JButton addContract = new JButton("Add Order");
        addContract.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStock.doClick();
                String id = JOptionPane.showInputDialog(null,"Enter the product ID you want to add stock of ");
                String qty = JOptionPane.showInputDialog(null,"Enter the product qty you want to add stock of ");
                String date = JOptionPane.showInputDialog(null,"Enter the date for the order");




                Statement st = null;
                try {
                    st = con.createStatement();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                ResultSet rs = null;
                try {
                    rs = Database.executeSelectQuery("SELECT  * FROM construction.schedule WHERE p_id = " + id,con);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateStr = formatter.parse(date);
                    java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
                    System.out.println(dateDB);
                        st.executeUpdate("INSERT INTO construction.schedule (p_id,shipping_date,qty) VALUES(" + id + ",'" + dateDB + "'," + qty + ")" );
                        addToStock(id,qty);

                } catch (SQLException | ParseException throwables) {
                    throwables.printStackTrace();
                }


            }
        });
        buttonPanel.add(addContract);

    }
}
