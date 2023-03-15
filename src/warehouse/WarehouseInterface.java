package warehouse;

import javax.swing.*;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import database.Database;

public class WarehouseInterface extends JFrame{

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
                    ResultSet rs = Database.executeSelectQuery("SELECT reserved.p_id,products.color,products.texture, reserved.delivery_date, reserved.qty FROM construction.reserved INNER JOIN construction.products ON reserved.p_id = products.p_id",con);
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
        buttonPanel.add(showStock);

        JButton addStock = new JButton("Add Stock");
        buttonPanel.add(addStock);

        JButton addContract = new JButton("Add Contract");
        buttonPanel.add(addContract);

    }
}
