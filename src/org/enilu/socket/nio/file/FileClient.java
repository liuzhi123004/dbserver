package org.enilu.socket.nio.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {  
	  
    private int ServerPort = 12345;  
    private String ServerAddress = "127.0.0.1";  
    private String CMD = "list";  
    private String local_file = "";  
    private String server_file = "";  
  
    class SocketThread extends Thread {  
  
        @Override  
        public void run() {  
            try {  
                File file = new File(local_file);   
                if (!file.exists() && CMD.equals("put")) {  
                    System.out.println("本地没有这个文件，无法上传！");  
                    return;  
                }  
  
                InetAddress loalhost = InetAddress.getByName("127.0.0.1");//InetAddress.getLocalHost();  
                Socket socket = new Socket(ServerAddress, ServerPort, loalhost,  
                        44);  
                // 服务器IP地址 端口号 本机IP 本机端口号  
                DataInputStream dis = new DataInputStream(socket  
                        .getInputStream());  
                DataOutputStream dos = new DataOutputStream(socket  
                        .getOutputStream());  
  
                // dos.writeUTF(GetOrPut+" "+server_file);//服务器端如果是do的socket，writeUTF和writeUTF对接  
                dos.write((CMD + " " + server_file).getBytes());  
                dos.flush();  
  
                // String tempString = dis.writeUTF();  
                byte[] buf = new byte[1024];  
                int len = dis.read(buf);  
                String tempString = new String(buf, 0, len);// 服务器反馈的信息  
  
                // System.out.println(tempString);  
                if (tempString.equals("no such file")) {  
                    System.out.println("服务器没有这个文件，无法下载！");  
                    dos.close();  
                    dis.close();  
                    socket.close();  
                    return;  
                }   
  
                if (tempString.startsWith("开始下载")) {  
                    DataOutputStream fileOut = new DataOutputStream(  
                            new BufferedOutputStream(new FileOutputStream(file)));  
  
                    while ((len = dis.read(buf)) != -1) {  
                        fileOut.write(buf, 0, len);  
                    }  
                    System.out.println("下载完毕！");  
                    fileOut.close();  
                    dos.close();  
                    dis.close();  
                    socket.close();  
                } else if (tempString.equals("开始上传")) {  
                    System.out.println("正在上传文件.......");  
                    DataInputStream fis = new DataInputStream(  
                            new BufferedInputStream(new FileInputStream(file)));  
  
                    while ((len = fis.read(buf)) != -1) {  
                        dos.write(buf, 0, len);  
                    }  
                    dos.flush();  
                    System.out.println("上传完毕！");  
                    fis.close();  
                    dis.close();  
                    dos.close();  
                    socket.close();  
                }  
                else if(tempString.equals("list files"))  
                {  
                    len=dis.read(buf);  
                    String temp=new String(buf,0,len);  
                    String[] strs=temp.split(";");  
                    System.out.println("文件列表");  
                    for(String str:strs)  
                    {  
                        System.out.println(str);  
                    }  
                    dis.close();  
                    dos.close();  
                    socket.close();  
                }  
  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
  
    }  
  
    public boolean checkCommand(String command) {  
        if (!command.startsWith("put") && !command.startsWith("get") && !command.equals("list")) {  
            System.out.println("输入命令错误");  
            return false;  
        }  
  
        int index = -1;  
        String temp = "";  
        String[] tempStrings = null;  
  
        if ((index = command.indexOf("-h")) > 0) {  
            temp = command.substring(index + 3);  
            temp = temp.substring(0, temp.indexOf(' '));  
            ServerAddress = temp;  
        }  
        if ((index = command.indexOf("-p")) > 0) {  
            temp = command.substring(index + 3);  
            temp = temp.substring(0, temp.indexOf(' '));  
            ServerPort = Integer.valueOf(temp);  
        }  
  
        tempStrings = command.split(" ");  
        if (command.startsWith("put")) {  
            CMD = "put";  
            local_file = tempStrings[tempStrings.length - 2];  
            server_file = tempStrings[tempStrings.length - 1];  
        } else if (command.startsWith("get")) {  
            CMD = "get";  
            local_file = tempStrings[tempStrings.length - 1];  
            server_file = tempStrings[tempStrings.length - 2];  
        }  
        else  
        {  
            CMD = "list";  
        }  
  
        return true;  
    }  
  
    public static void main(String[] args) {  
        FileClient client = new FileClient();  
        Scanner sc = new Scanner(System.in);  
        String commandString = "";  
        do {  
            System.out.println("请输入命令：");  
            commandString = sc.nextLine();  
        } while (!client.checkCommand(commandString));  
  
        FileClient.SocketThread a = client.new SocketThread();  
        a.start();  
  
    }  
}  