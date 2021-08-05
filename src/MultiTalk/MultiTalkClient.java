//多客户端聊天
import java.io.*;
import java.net.*;
public class MultiTalkClient {
    int num;
    public static void main(String[] args){
        try{
            Socket socket = new Socket("127.0.0.1",4700);//127.0.0.1为本机地址，因为只有一台电脑，需要一台电脑启动两个虚拟机；4700为端口号，大于1024（保留端口）即可
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));//键盘输入流
            PrintWriter os = new PrintWriter(socket.getOutputStream());//有多种方法，输出流
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));//来自网络的输入流
            String readline;
            readline = sin.readLine();
            while(!readline.equals("bye"))
            {
                os.println(readline);//发送字符串
                os.flush();//清空缓存区，强制输出
                System.out.println("Client:"+readline);
                System.out.println("Server:"+is.readLine());
                readline = sin.readLine();
            }
            os.close();
            is.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Error"+e);
        }
    }
}
