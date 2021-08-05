public class MultiTalkServer {
    static int clientnum = 0;//静态成员变量统计客户端
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = null;
        boolean listening = true;
        try{
            serverSocket = new ServerSocket(4700);
        }catch(IOException e){
            System.out.println("Could not listen on port:4700");
            System.exit(-1);
        }
        while(listening)
        {
            new ServerThread(serverSocket.accept(),clientnum).start();
            clientnum++;
        }
        serverSocket.close();
    }
}

class ServerThread extends Thread{
    Socket socket = null;
    int clientnum;
    public ServerThread(Socket socket,int num){
        this.socket = socket;
        clientnum = num+1;
    }

    @Override
    public void run() {
        try{
            String line;
            //服务端角度的三个流
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Client"+clientnum+":"+is.readLine());
            line = sin.readLine();//读取键盘上的信息
            while (!line.equals("bye"))
            {
                os.println(line);
                os.flush();
                System.out.println("Server:"+line);
                System.out.println("Client"+clientnum+":"+is.readLine());
                line = sin.readLine();
            }
            os.close();
            is.close();
            socket.close();
        }catch(Exception e){
            System.out.println("Error"+e);
        }
    }
}
