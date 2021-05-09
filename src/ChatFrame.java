import javax.swing.*;
import java.awt.event.*;//事件处理机制
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
public class ChatFrame extends JFrame implements ActionListener{ //继承JFrame；实现ActionListener
    JTextField tf;//文本输入框
    JTextArea ta;//文本展示
    JScrollPane sp;//滚动面板
    JButton send;
    JPanel p;//面板放在最先面承载JButton和JTextField
    int port;
    String s = "";
    String myID;
    Date date;//时间
    ServerSocket server;
    Socket mySocket;
    BufferedReader is;
    PrintWriter os;
    String line;
    public ChatFrame(String ID,String remoteID,String IP,int port,boolean isServer){
        super(ID);
        myID = ID;
        this.port = port;
        ta = new JTextArea();
        ta.setEditable(false);
        sp = new JScrollPane(ta);
        this.setSize(330,400);
        this.setResizable(false);//窗口不能改变大小
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//显示风格设置为系统的显示风格
        }catch(Exception e){
            System.out.println("UI Error");
        }
        this.getContentPane().add(sp,"Center");
        p = new JPanel();
        this.getContentPane().add(p,"South");//窗口南部
        send = new JButton("发送");
        tf = new JTextField(20);
        tf.requestFocus();//tf上面放置光标
        p.add(tf);
        p.add(send);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        send.addActionListener(this);//消息处理机制
        tf.addActionListener(this);//消息处理机制
        if(isServer){
            try{
                server = null;
                try{
                    server = new ServerSocket(port);
                }catch(Exception e){
                    System.out.println("can not listen to:"+e);
                }
                mySocket = null;
                try{
                    mySocket = server.accept();
                }catch(Exception e){
                    System.out.println("Error"+e);
                }
                is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                os = new PrintWriter(mySocket.getOutputStream());
            }catch(Exception e){
                System.out.println("Error:in server client socket"+e);
            }
        }
        else{
            //非服务端
            try{
                mySocket = new Socket(IP,port);
                os = new PrintWriter(mySocket.getOutputStream());
                is = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            }catch(Exception e){
                System.out.println("Error:in client socket"+e);
            }
        }
        while(true){
            //利用流通讯
            try{
                line = is.readLine();
                date = new Date();//日期对象，可以按照一定格式显示
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = format.format(date);
                s+=currentTime+" "+remoteID+"说：\n"+line+"\n";//s为全部聊天记录
                ta.setText(s);
            }catch(Exception e) {
                System.out.println("Error:in receive remote information "+e);
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){//本地输入的信息需要传输
        //事件处理,实现ActionListener接口
        date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = format.format(date);
        s+=currentTime+" "+myID+"说：\n"+tf.getText()+"\n";//s为全部聊天记录
        ta.setText(s);
        os.println(tf.getText());
        os.flush();
        tf.setText("");//还原
        tf.requestFocus();
    }
}
