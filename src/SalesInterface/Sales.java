package SalesInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import database.Database;
public class Sales extends JFrame{

    Date date = new Date(System.currentTimeMillis());
    public void sellToStock(String id, String qty){
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
                JOptionPane.showMessageDialog(null,"This product doesn't exist");
            }
            else
            {
                totalQty = Integer.parseInt(rs.getString("stock")) - Integer.parseInt(qty);
                if (totalQty < 0){
                    JOptionPane.showMessageDialog(null,"This Qty doesn't exist");
                }
                System.out.println(totalQty);
                st.executeUpdate("UPDATE construction.instock SET instock.stock = "+ String.valueOf(totalQty)  + " WHERE construction.instock.p_id = " + id);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public  void calenderMatch() throws SQLException {
        int p_id;
        int schedule_id;
        int qty;
        Statement st = con.createStatement();
        ResultSet rs = Database.executeSelectQuery("SELECT * FROM construction.schedule WHERE shipping_date = '" + date + "'",con);
        while(rs.next()){
            p_id = rs.getInt("p_id");
            schedule_id = rs.getInt("scheduled_id");
            qty = rs.getInt("qty");

            System.out.print(p_id);
            System.out.print("   ");
            System.out.print(schedule_id);
            System.out.print("   ");
            System.out.println(qty);
            sellToStock(String.valueOf(p_id),String.valueOf(qty));
            st.executeUpdate("DELETE FROM construction.schedule WHERE scheduled_id = " + schedule_id);
        }
    }

    Connection con = Database.getConnection();

    private JTable scheduledTable;
    public Sales() throws SQLException, ClassNotFoundException {

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(800,600));
        getContentPane().setLayout(null);

        calenderMatch();
        JLabel titleWarehouse = new JLabel("Sales Panel\r\n");
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
                    product_model.addRow(new Object[]{"ID","Color","Texture"});
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
                    ResultSet rs = Database.executeSelectQuery("SELECT reserved.p_id,products.color,products.texture, reserved.delivery_date, reserved.qty FROM construction.reserved INNER JOIN construction.products ON reserved.p_id = products.p_id",con);
                    product_model.addColumn("Product ID");
                    product_model.addColumn("Color");
                    product_model.addColumn("Texture");
                    product_model.addColumn("Date");
                    product_model.addColumn("Qty");

                    product_model.setColumnIdentifiers(new Object[]{"ID","Color","Texture","Date","Qty"});
                    product_model.addRow(new Object[]{"ID","Color","Texture","Date","Qty"});
                    while (rs.next())
                    {
                        product_id = rs.getInt("p_id");
                        color = rs.getString("color");
                        texture = rs.getString("texture");
                        date = rs.getString("delivery_date");
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
                    product_model.addRow(new Object[]{"ID","Color","Texture","Qty"});
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

        JButton addStock = new JButton("Sell Stock");
        addStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStock.doClick();
                String id = JOptionPane.showInputDialog(null,"Enter the product ID you want to sell stock of ");
                String qty = JOptionPane.showInputDialog(null,"Enter the product qty you want to sell stock of ");
                sellToStock(id,qty);
                showStock.doClick();



            }
        });
        buttonPanel.add(addStock);

        JButton addContract = new JButton("Add Contract");
        addContract.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStock.doClick();
                String id = JOptionPane.showInputDialog(null,"Enter the product ID you want to sell stock of ");
                String qty = JOptionPane.showInputDialog(null,"Enter the product qty you want to sell stock of ");
                String date = JOptionPane.showInputDialog(null,"Enter the date for the shipping");




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
                    sellToStock(id,qty);

                } catch (SQLException | ParseException throwables) {
                    throwables.printStackTrace();
                }


            }
        });
        buttonPanel.add(addContract);



    }
}
