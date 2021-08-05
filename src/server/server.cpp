/*
一个由C++写成的WEB服务器，用以代替Java中服务器相关的代码，
更改seraddr.sin_port端口号以实现用户访问不同的内容
*/
#include<stdio.h>
#include<WinSock2.h>//包含网络编程头文件,引入静态库
#pragma comment(lib,"ws2_32.lib") 

int merror(int redata,int error,char* showinfo)
{
	if (redata == error)
	{
		perror(showinfo);
		getchar();
		return -1;
	}
	return 0;
}
void sendhtml(SOCKET s, char* filename)
{
  //暂时存放在本地的聊天记录
	FILE* pfile = fopen(filename, "r");
	int e = merror(pfile, NULL, "打开文件失败\n");
	if (e == 0)
	{
		char temp[1024];
		do {
			fgets(temp, 1024, pfile);
			send(s, temp, strlen(temp), 0);
		} while (!feof(pfile));
	}
	else {
		return;
	}

}
int main()
{
	printf("欢迎使用ASCSC!\n");
	WSADATA wsadata;//包含版本信息
	int isok=WSAStartup(MAKEWORD(2,2), &wsadata);//确定Socket版本信息
	merror(isok, WSAEINVAL, "申请Socket失败\n");

	SOCKET server=socket(AF_INET,SOCK_STREAM,IPPROTO_TCP);//AF_INET:使用IPV4地址（代表协议簇），SOCK_SEQPACKET:传输类型SOCK_STREAM流传输，IPPROTO_TCP:TCP协议
	merror(server, INVALID_SOCKET, "创建Socket流传输失败\n");

	struct sockaddr_in seraddr;
	seraddr.sin_family = AF_INET;
	seraddr.sin_port = htons(80);
	seraddr.sin_addr.s_addr = INADDR_ANY;
	isok = bind(server, &seraddr, sizeof(seraddr));
	merror(isok, SOCKET_ERROR, "绑定失败失败\n");

	isok = listen(server, 5);
	merror(isok, SOCKET_ERROR, "监听失败\n");
	
	struct sockaddr_in claddr;
	int cllen = sizeof(claddr);
	for(;;){
		SOCKET client = accept(server, &claddr, &cllen);
		merror(client, INVALID_SOCKET, "无效的连接，连接失败\n");

		char revdata[1024];
		recv(client, revdata, 1024, 0);//接受数据
		printf("%s共接收到%d字节数据\n", revdata, strlen(revdata));

		//char sendata[1024] = "<h1 style=\"\color:red;\"你好，我是HOL服务器</h1>";//让字体变大变红
		//send(client, sendata, strlen(sendata), 0);//连接成功就发送给客户端数据

		char* filename = "./";//文件名称
		sendheml(client, filename);
		closesocket(client);
	}
	closesocket(server);

	getchar();
	return 0;
}
