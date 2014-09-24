/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import sharedresources.Message;
/**
 *
 * @author angor
 */
public class Client extends javax.swing.JFrame {
    private Socket socketClient;
    /**
     * Creates new form ChatClient
     */
    public Client() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ConnectToServerButton = new javax.swing.JButton();
        HostnameTextField = new javax.swing.JTextField();
        PortTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        MainPanelTextArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        EnterTextArea = new javax.swing.JTextArea();
        SendMessageButton = new javax.swing.JButton();
        ServerStatusLAbel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Hostname:");

        jLabel2.setText("Port:");

        ConnectToServerButton.setText("Connect to server");
        ConnectToServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectToServerButtonActionPerformed(evt);
            }
        });

        HostnameTextField.setText("localhost");

        PortTextField.setText("4444");

        MainPanelTextArea.setColumns(20);
        MainPanelTextArea.setRows(5);
        jScrollPane1.setViewportView(MainPanelTextArea);

        jLabel3.setText("Enter your text here:");

        EnterTextArea.setColumns(20);
        EnterTextArea.setRows(5);
        jScrollPane2.setViewportView(EnterTextArea);

        SendMessageButton.setText("Send message");
        SendMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendMessageButtonActionPerformed(evt);
            }
        });

        ServerStatusLAbel.setText("Server status...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(HostnameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6)
                        .addComponent(PortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ConnectToServerButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(ServerStatusLAbel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SendMessageButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(ConnectToServerButton)
                    .addComponent(HostnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SendMessageButton)
                    .addComponent(ServerStatusLAbel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ConnectToServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectToServerButtonActionPerformed
        // TODO add your handling code here:
        new Thread() {
             public void run() {
                try {
				    socketClient= new Socket(getHostName(),getPort());
				    System.out.println("Client: "+"Connection Established");
		                    setServerStatus("Connection Established!",true); 
				    BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
				    //BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
				    ObjectOutputStream objectWriter = new ObjectOutputStream(socketClient.getOutputStream());
				    String serverMsg;
				    String hostname = InetAddress.getLocalHost().getHostName();
				    Message message = new Message(false, false, false, false, "hostname;"+hostname, "addfds");
				    objectWriter.writeObject(message);
				    
				 	System.out.println("hostname: " + hostname); //DEBUG
				    
		            while ((serverMsg = reader.readLine()) != null) {
		                AddTextToMainPanel(serverMsg);
		            }
                    
                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Client: "+"Connection failed..");
                    setServerStatus("Connection failed..", false);
                }
            }
        }.start();
        
        MClientListener mClientListener = new MClientListener(this);
        mClientListener.start();
        
    }//GEN-LAST:event_ConnectToServerButtonActionPerformed

    private void SendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendMessageButtonActionPerformed
        // TODO add your handling code here:
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socketClient.getOutputStream());
            Message message = new Message(true, false, false, false, "user1", this.EnterTextArea.getText());
            outputStream.writeObject(message);
            this.EnterTextArea.setText("");
        } catch(IOException ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_SendMessageButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        try {
        	if(socketClient != null) {
        		socketClient.close();
        	}
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
    
    public String getHostName(){
        return this.HostnameTextField.getText();
    }
    
    public int getPort(){
        return Integer.parseInt(PortTextField.getText());
    }
    
    public void setServerStatus(String status, boolean flag){
        this.ServerStatusLAbel.setText(status);
        if(flag) 
            this.ServerStatusLAbel.setForeground(Color.green);
        else
            this.ServerStatusLAbel.setForeground(Color.red);
    }
    
    public void AddTextToMainPanel(String text){
        this.MainPanelTextArea.setText(this.MainPanelTextArea.getText() + text + "\n");
    }
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConnectToServerButton;
    private javax.swing.JTextArea EnterTextArea;
    private javax.swing.JTextField HostnameTextField;
    private javax.swing.JTextArea MainPanelTextArea;
    private javax.swing.JTextField PortTextField;
    private javax.swing.JButton SendMessageButton;
    private javax.swing.JLabel ServerStatusLAbel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
