/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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
    private static final long serialVersionUID = 5095009435533641880L;
    public ClientToHost clientToHost;
    public MessageController messageController = new MessageController();
    public boolean isConnected = false;
    public BlockingQueue<Message> holdbackQueue = new PriorityBlockingQueue<Message>();
    public long lastHostUpdate = 0;
    public boolean tryingToConnect = false;
    private String connectButtonText = "Connect";
    
    public static KnownClients knownClients = new KnownClients();
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

    	this.setTitle("MultiCom Messenger: " + Misc.processID);
        jLabel1 = new javax.swing.JLabel();
//        jLabel2 = new javax.swing.JLabel();
        ConnectToServerButton = new javax.swing.JButton();
        UsernameTextField = new javax.swing.JTextField();
//        PortTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        MainPanelTextArea = new javax.swing.JTextPane();
        this.MainPanelTextArea.setContentType("text/html");
        this.MainPanelTextArea.setEditable(false);
        this.MainPanelTextArea.setAutoscrolls(true);
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        EnterTextArea = new javax.swing.JTextField();
//        this.EnterTextArea.setLineWrap(true);
        Border border = new EmptyBorder(0,0,0,0);
        this.EnterTextArea.setBorder(border);
//        this.EnterTextArea.setWrapStyleWord(true);
        this.EnterTextArea.setBorder(BorderFactory.createCompoundBorder(
                this.EnterTextArea.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        this.EnterTextArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
				
			}
		});
        SendMessageButton = new javax.swing.JButton();
        ServerStatusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Nickname:");

        ConnectToServerButton.setText(this.connectButtonText);
        ConnectToServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectToServerButtonActionPerformed(evt);
            }
        });

        UsernameTextField.setText("user");

        jScrollPane1.setViewportView(MainPanelTextArea);

        jLabel3.setText("Enter your text here:");

        EnterTextArea.setColumns(20);
        jScrollPane2.setViewportView(EnterTextArea);

        SendMessageButton.setText("Send message");
        SendMessageButton.setEnabled(false);
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
                        .addGap(6, 6, 6)
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
                    .addComponent(ConnectToServerButton)
                    .addComponent(UsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    	if(clientToHost!=null && clientToHost.getSocket() !=null) {
    		this.showErrorMessage("You are already connected.");
    		return;
    	}
    	
    	startConnection();
        
    }//GEN-LAST:event_ConnectToServerButtonActionPerformed
 
    MessagePresenter messagePresenter = null;
    MClientListener mClientListener = null;
    ClientHeartbeatToHost clientHeartBeatToHost = null;
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
        
        //Listen for response of previous request
        OneToManyListener oneToManyListener = new OneToManyListener(messageController, false);
        oneToManyListener.start();
        
        //Multicast to join network
        ClientToMHost clientToMHost = new ClientToMHost(this);
        
        //RerouteAttempt is true when a client changes hosts
        if(!this.attemptConnection(clientToMHost, oneToManyListener)) {
        	this.ConnectToServerButton.setText(this.connectButtonText);
            return;
        }

        clientToHost = new ClientToHost(this);
        isConnected = true;
        SendMessageButton.setEnabled(true);

        mClientListener = new MClientListener(this);
        mClientListener.start();
        
        clientHeartBeatToHost = new ClientHeartbeatToHost(this);
        clientHeartBeatToHost.start();
        
        clientToHostAckSender = new ClientToHostAckSender(this);
        clientToHostAckSender.start();

        oneToOneListener = new OneToOneListener(clientToHost.getSocket(), messageController, false);
        oneToOneListener.start();
        
        ackMonitor = new ReceivedAcknowledgmentsByClientMonitor(this);
        ackMonitor.start();
        
        sendFirstConnectMessageToHost();
        this.ConnectToServerButton.setEnabled(false);
        
        this.ConnectToServerButton.setText("Connected");
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
        this.SendMessageButton.setEnabled(false);
    }
    /**
     * Try to establish a connection. Retry if there is no response
     * @param clientToMHost
     * @param oneToManyListener
     */
    private boolean attemptConnection(ClientToMHost clientToMHost, OneToManyListener oneToManyListener) {
    	this.ConnectToServerButton.setText("Connecting...");
        if(this.tryingToConnect) return false;
        if(this.holdbackQueueMonitor!=null) this.unloadHoldbackQueue();
        this.tryingToConnect = true; // multiple places to false
        //First attempt
        clientToMHost.sendConnectRequest();
        
        //Sleep to give master time to respond
        try {
            Thread.sleep(250);
        } catch (InterruptedException e1) {
        	System.out.println("##-- Stop attempt to make a connection --##");
        	this.tryingToConnect = false;
        	return false;
        }
        
        //No response (in time) start sending request again from time to time (max 3 times)
        boolean flag = true;
        long startWaitingForConnection = new Date().getTime();
        long waitBeforeResendConnectRequest = 4000; //wait this long for resending connection request
        int connectTries = 0;
        while(flag) {
            //Check response
            Message message = messageController.queueMClientCommand.pop();
            if(message!=null && Commands.messageIsOfCommand(message, Commands.hostFound)) {
                messageController.queueSentMessagesByClient.clear();
                
                String[] messageParts = Commands.splitMessage(message);
                if(Misc.processID.equals(messageParts[1])) { //This client requested a connection
                    Config.connectToPortFromHost = Integer.parseInt(messageParts[3]);
                    System.out.println("##-- Connecting to Host: " + Config.connectToPortFromHost + " --##");
                    oneToManyListener.stop();
                    this.tryingToConnect = false;
                    return true;
                }
            }
            long currentTime = new Date().getTime();
            //Send a new connection request after some time
            if(currentTime-startWaitingForConnection>waitBeforeResendConnectRequest) {
                clientToMHost.sendConnectRequest();
                connectTries++;
                if(connectTries>4){
                    showErrorMessage("No available hosts. Try again later.");
                    this.tryingToConnect = false;
                    this.ConnectToServerButton.setEnabled(true);
                    return false;
                }
                startWaitingForConnection = new Date().getTime(); //reset the time of the start
            }
            try {
                /*
                 * It doesn't receive/pop a connection response without this Thread.sleep
                 * Because this Thread is consuming all processing power
                 */
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.tryingToConnect = false;
        return false;
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
        this.sendMessage();

    }//GEN-LAST:event_SendMessageButtonActionPerformed
    
    private void sendMessage() {
    	if(clientToHost==null || clientToHost.getSocket()==null) {
            this.showErrorMessage("You are not connected.");
            return;
        }
        
        if(this.EnterTextArea.getText().getBytes().length > 300){
        	this.showErrorMessage("Message cannot be sent. Reduce the size.");
        	return;
        }
        
        Message message = new Message(Message.MessageType.hostChat, this.getUserName(), this.EnterTextArea.getText(), Misc.getNextMessageId());        
        boolean success = clientToHost.sendMessage(message);
        
        //after sending the message we should store it in the SentMessages queue and wait for its acknowledgment
        message.setTimeSent(new Date().getTime());
        messageController.queueSentMessagesByClient.push(message);
    	if(success) this.EnterTextArea.setText("");
    }
	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
        	if(clientToHost!=null && clientToHost.getSocket() != null) {
        	    //Send a shutdown message
        	    String command = Commands.constructCommand(Commands.clientShutdown);
        	    Message shutdownMsg = new Message(MessageType.clientCommand, command);
        	    clientToHost.sendMessage(shutdownMsg);
        		
        	    clientToHost.getSocket().close();
        	}
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println("############# Messenger #############");
        System.out.println("##### Process ID: " + Misc.processID);
        System.out.println("#####################################\n");
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
    	if(clientToHost!=null && clientToHost.getSocket() != null) {
    		try {
    		    clientToHost.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
        setServerStatus("Connection failed..", false);

    }
    public String getUserName(){
        return UsernameTextField.getText();
    }
    
    public void setServerStatus(String status, boolean flag){
        this.ServerStatusLabel.setText(status);
        if(flag) 
            this.ServerStatusLabel.setForeground(Color.black);
        else
            this.ServerStatusLabel.setForeground(Color.red);
    }
    
    public void AddTextToMainPanel(String text){
    	StringBuilder s = new StringBuilder("<html><body>");
    	if(this.MainPanelTextArea.getText().contains("</p>"))
    		s.append(this.MainPanelTextArea.getText().substring(this.MainPanelTextArea.getText().indexOf("</p>"), this.MainPanelTextArea.getText().lastIndexOf("</body>")));
    	else
    		s.append(this.MainPanelTextArea.getText().substring(0, this.MainPanelTextArea.getText().lastIndexOf("</body>")));

    	s.append(text + "<br></body></html>");
        
    	//adding text in the main panel area
    	this.MainPanelTextArea.setText(s.toString());
        //moving the cursor at the end of the last message
    	this.MainPanelTextArea.setCaretPosition(this.MainPanelTextArea.getDocument().getLength());
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
    private javax.swing.JTextField EnterTextArea;
    private static javax.swing.JTextField UsernameTextField;
    private JTextPane MainPanelTextArea;
    private javax.swing.JButton SendMessageButton;
    private javax.swing.JLabel ServerStatusLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
