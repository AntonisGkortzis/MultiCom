/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import monitor.HoldbackQueueMonitor;
import monitor.ReceivedAcknowledgmentsByClientMonitor;
import sharedresources.Commands;
import sharedresources.Config;
import sharedresources.Message;
import sharedresources.Message.MessageType;
import sharedresources.MessageController;
import sharedresources.Misc;
import sharedresources.OneToManyListener;
import sharedresources.OneToOneListener;
/**
 * This class is used to create the Gui of the client and to start communication with the hosts. 
 * The connections started are:
 *  - Client to host communication
 *  - Client to multiple hosts communication
 */
public class Client extends javax.swing.JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 5095009435533641880L;
    private Socket socketClient;
    public ClientToHost clientToHost;
    public MessageController messageController = new MessageController();
    public boolean isConnected = false;
    public boolean rerouteAttempt = false;
    public BlockingQueue<Message> holdbackQueue = new PriorityBlockingQueue<Message>();
    
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ConnectToServerButton = new javax.swing.JButton();
        UsernameTextField = new javax.swing.JTextField();
//        PortTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        MainPanelTextArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        EnterTextArea = new javax.swing.JTextArea();
        SendMessageButton = new javax.swing.JButton();
        ServerStatusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Username:");

        jLabel2.setText("Port:");

        ConnectToServerButton.setText("Connect to server");
        ConnectToServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectToServerButtonActionPerformed(evt);
            }
        });

        UsernameTextField.setText("username");

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

        ServerStatusLabel.setText("Server status: Not connected");

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
                        .addComponent(UsernameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6)
//                        .addComponent(PortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ConnectToServerButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(ServerStatusLabel)
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
                    .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//                    .addComponent(PortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SendMessageButton)
                    .addComponent(ServerStatusLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ConnectToServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectToServerButtonActionPerformed
    	if(socketClient !=null) {
    		this.showErrorMessage("You are already connected.");
    		return;
    	}
    	
    	startConnection();
        
    }//GEN-LAST:event_ConnectToServerButtonActionPerformed
 
    MessagePresenter messagePresenter = null;
    MClientListener mClientListener = null;
    ClientHeartBeatToHost clientHeartBeatToHost = null;
    ClientToHostAckSender clientToHostAckSender  = null;
    OneToOneListener oneToOneListener = null;
    ReceivedAcknowledgmentsByClientMonitor ackMonitor = null;
    HoldbackQueueMonitor holdbackQueueMonitor = null;
    
    public void startConnection() {
        this.stopConnections();
        messagePresenter = new MessagePresenter(this);
        messagePresenter.start();
        
        holdbackQueueMonitor = new HoldbackQueueMonitor(this);
        holdbackQueueMonitor.start();
        
        //Listen for response of previous request (or should this be placed before clientomhost?
        OneToManyListener oneToManyListener = new OneToManyListener(messageController, false);
        oneToManyListener.start();
        
        //Multicast to join network TODO in report
        ClientToMHost clientToMHost = new ClientToMHost(this);
        
        //RerouteAttempt is true when a client changes hosts
        if(!this.rerouteAttempt && !this.attemptConnection(clientToMHost, oneToManyListener)) {
            return;
        }

        this.rerouteAttempt = false;
        
        clientToHost = new ClientToHost(this);
        socketClient = clientToHost.getSocket();
        isConnected = true;

        mClientListener = new MClientListener(this);
        mClientListener.start();
        
        clientHeartBeatToHost = new ClientHeartBeatToHost(this);
        clientHeartBeatToHost.start();
        
        clientToHostAckSender = new ClientToHostAckSender(this);
        clientToHostAckSender.start();

        oneToOneListener = new OneToOneListener(socketClient, messageController, false);
        oneToOneListener.start();
        
        ackMonitor = new ReceivedAcknowledgmentsByClientMonitor(this);
        ackMonitor.start();
        
        sendFirstConnectMessageToHost();
    }
    
    /**
     * Handle every message still in the holdback queue
     */
    public void unloadHoldbackQueue() {
        this.holdbackQueueMonitor.unload();
    }
    
    private void stopConnections() {
        if(messagePresenter!=null) messagePresenter.stop();
        if(mClientListener!=null) mClientListener.stop();
        if(clientHeartBeatToHost!=null) clientHeartBeatToHost.stop();
        if(clientToHostAckSender!=null) clientToHostAckSender.stop();
        if(oneToOneListener!=null) oneToOneListener.stop();
        if(ackMonitor!=null) ackMonitor.stop();
        if(holdbackQueueMonitor!=null) holdbackQueueMonitor.stop();

    }
    /**
     * Try to establish a connection. Retry if there is no response
     * @param clientToMHost
     * @param oneToManyListener
     */
    private boolean attemptConnection(ClientToMHost clientToMHost, OneToManyListener oneToManyListener) {
        //First attempt
        clientToMHost.sendConnectRequest();
        
        //Sleep to give master time to respond
        try {
            Thread.sleep(250);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        //No response (in time) start sending request again from time to time (max 3 times)
        boolean flag = true;
        long startWaitingForConnection = new Date().getTime();
        long waitBeforeResendConnectRequest = 3000; //wait this long for resending connection request
        int connectTries = 0;
        while(flag) {
            //Check response
            Message message = messageController.queueMClientCommand.pop();
            if(message!=null && Commands.messageIsOfCommand(message, Commands.hostFound)) {
                messageController.queueSentMessagesByClient.clear();
                
                String[] messageParts = Commands.splitMessage(message);
                if(Misc.processID.equals(messageParts[1])) { //This client requested a connection
                    Config.connectToPortFromHost = Integer.parseInt(messageParts[3]);
                    System.out.println("HOST IS FOUND Connect to port: " + Config.connectToPortFromHost);
                    oneToManyListener.stop();
                    return true;
                }
            }
            long currentTime = new Date().getTime();
            //Send a new connection request after some time TODO in report
            if(currentTime-startWaitingForConnection>waitBeforeResendConnectRequest) {
                clientToMHost.sendConnectRequest();
                connectTries++;
                if(connectTries>2){
                    showErrorMessage("No available hosts. Try again later.");
                    return false;
                }
                startWaitingForConnection = new Date().getTime(); //reset the time of the start
            }
            try {
                /*
                 * It doesn't receive/pop a connection response without this Thread.sleep
                 * Because this Thread is consuming all processing power
                 */
                Thread.sleep(500); //TODO Find an explanation for that
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }
    /**
     * Used to send the first message to the host, so the host knows 
     * it has a new client and can update its client list
     */
    private void sendFirstConnectMessageToHost() {
        String command = Commands.constructCommand(Commands.initOneToOneWithHost);
        Message initMessage = new Message(MessageType.clientCommand, command);
        clientToHost.sendMessage(initMessage);
    }
    
    private void SendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendMessageButtonActionPerformed
        if(clientToHost==null) {
            this.showErrorMessage("You are not connected.");
            return;
        }
        Message message = new Message(Message.MessageType.hostChat, this.getUserName(), this.EnterTextArea.getText(), Misc.getNextMessageId());

        boolean success = clientToHost.sendMessage(message);
        
        //after sending the message we should store it in the SentMessages queue and wait for its acknowledgment
        messageController.queueSentMessagesByClient.push(message);
    	if(success) this.EnterTextArea.setText("");

    }//GEN-LAST:event_SendMessageButtonActionPerformed
    
	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
        	if(socketClient != null) {
        	    //Send a shutdown message
        	    String command = Commands.constructCommand(Commands.clientShutdown);
        	    Message shutdownMsg = new Message(MessageType.clientCommand, command);
        	    clientToHost.sendMessage(shutdownMsg);
        		
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
    
    public void closeSocket() {
    	if(socketClient != null) {
    		try {
				socketClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        setServerStatus("Connection failed..", false);

    }
    public String getUserName(){
        return this.UsernameTextField.getText();
    }
    
    
//    /**
//     * @Deprecated
//     * must be replaced by the port number received from the Master Host
//     */
//    @Deprecated
//    public int getPort(){
//        return Integer.parseInt(PortTextField.getText());
//    }
    
    public void setServerStatus(String status, boolean flag){
        this.ServerStatusLabel.setText(status);
        if(flag) 
            this.ServerStatusLabel.setForeground(Color.black);
        else
            this.ServerStatusLabel.setForeground(Color.red);
    }
    
    public void AddTextToMainPanel(String text){
        this.MainPanelTextArea.setText(this.MainPanelTextArea.getText() + text + "\n");
    }
    
    public String getTextFromMainPanel(){
    	return this.MainPanelTextArea.getText();
    }
    
    public void showErrorMessage(String msg) {
        this.stopConnections();
    	javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), msg, "Error",
    	        javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConnectToServerButton;
    private javax.swing.JTextArea EnterTextArea;
    private javax.swing.JTextField UsernameTextField;
    private javax.swing.JTextArea MainPanelTextArea;
//    private javax.swing.JTextField PortTextField;
    private javax.swing.JButton SendMessageButton;
    private javax.swing.JLabel ServerStatusLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

	public void setSocket(Socket socket) {
		this.socketClient = socket;
		
	}
}
