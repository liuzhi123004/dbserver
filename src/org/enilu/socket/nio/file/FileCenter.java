package org.enilu.socket.nio.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

/** 
 * 文件下中心，命令模式如下  
 * 显示文件：list  
 *      列出所有可下载的文件  
 * 下载文件：get server_file [local_file]  
 *      server_file服务器中的文件名，local_file本地存储路径 
 * 上传文件：put local_file [server_file]  
 *      local_file本地文件路径，server_file服务器存储文件名 
 *  
 * @author cyxl 
 *  
 */  
public class FileCenter {  
    private static Selector selector; // 选择器  
    private static final int server_port = 12345; // 服务器端口  
    private static CharsetDecoder decoder = Charset.forName("GB2312")  
            .newDecoder(); // 字节转字符  
    private static CharsetEncoder encoder = Charset.forName("GB2312")  
            .newEncoder(); // 字符转字节  
    private static ByteBuffer buffer = ByteBuffer.allocate(1024);  
    private static final String server_path = "/home/enilu/软件"; // 服务器文件路径  
  
    public static void main(String[] args) {  
        try {  
            selector = Selector.open();// 打开选择器  
            ServerSocketChannel serverChannel = ServerSocketChannel.open();  
            ServerSocket server = serverChannel.socket();  
            server.bind(new InetSocketAddress(server_port));  
            serverChannel.configureBlocking(false);  
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);  
            System.out.println("等待客户端连接……");  
            while (true) {  
                selector.select();  
                Iterator<SelectionKey> itr = selector.selectedKeys().iterator();  
                while (itr.hasNext()) {  
                    SelectionKey key = itr.next();  
                    itr.remove();  
                    process(key);  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static void process(SelectionKey key) throws IOException {  
        if (key.isAcceptable()) {  
            // 连接  
            ServerSocketChannel serverChannel = (ServerSocketChannel) key  
                    .channel();  
            SocketChannel client = serverChannel.accept();  
            client.configureBlocking(false);  
            SelectionKey sKey = client.register(selector, SelectionKey.OP_READ);  
            sKey.attach("[r_cmd]"); // 连接好后读取客户端发来的命令  
        } else if (key.isReadable()) {  
            // 读取  
            SocketChannel channel = (SocketChannel) key.channel();  
  
            String attach = key.attachment().toString();  
            if (attach.equals("[r_cmd]")) {  
                // 获取命令  
                int len = channel.read(buffer);  
                  
                if (len > 0) {  
                    buffer.flip();  
                    String cmd = "";  
                    CharBuffer charBuffer = decoder.decode(buffer);  
                    cmd = charBuffer.toString();  
                      
                    SelectionKey sKey = channel.register(selector,  
                            SelectionKey.OP_WRITE);  
                    System.out.println("cmd:" + cmd);  
                    if (cmd.trim().equals("list")) {  
                        sKey.attach("[list_file]");  
                    } else {  
                        String[] temp = cmd.split(" ");  
                        if (temp.length >= 2) {  
                            cmd = temp[0];  
                            String filename = temp[1];  
  
                            if (cmd.equals("get")) {  
                                // 下载  
                                File file = new File(server_path, filename);  
                                if (file.exists()) {  
                                    sKey.attach("[get]:" + filename);  
                                } else {  
                                    sKey.attach("[no_file]");  
                                }  
                            } else if (cmd.equals("put")) {  
                                // 上传  
                                sKey.attach("[put]:" + filename);  
                            } else {  
                                // 错误命令格式  
                                sKey.attach("[error_command]");  
                            }  
                        }  
  
                    }  
                } else {  
                    channel.close();  
                }  
            }  
            buffer.clear();  
        } else if (key.isWritable()) {  
            // 写入  
            SocketChannel channel = (SocketChannel) key.channel();  
            String attach = key.attachment().toString();  
            if (attach.startsWith("[list_file]")) {  
                channel.write(encoder.encode(CharBuffer.wrap("list files")));  
                File file = new File(server_path);  
                String[] filenames = file.list();  
                String temp = "";  
                for (String filename : filenames) {  
                    temp += filename + ";";  
                }  
                temp = temp.substring(0, temp.length() - 1);  
                // 写入所有可下载的文件  
                channel.write(ByteBuffer.wrap(temp.getBytes()));  
  
                channel.close();  
            } else if (attach.equals("[no_file]")) {  
                channel.write(ByteBuffer.wrap("no such file".getBytes()));  
  
                channel.close();  
            } else if (attach.equals("[error_command]")) {  
                channel.write(ByteBuffer.wrap("error command".getBytes()));  
  
                channel.close();  
            } else if (attach.startsWith("[get]")) {  
                channel.write(encoder.encode(CharBuffer.wrap("开始下载")));  
                File file = new File(server_path, attach.split(":")[1]);  
                DataInputStream dis = new DataInputStream(  
                        new BufferedInputStream(new FileInputStream(file)));  
  
                int len = 0;  
                byte[] buf = new byte[1024];  
                while ((len = dis.read(buf)) != -1) {  
                    channel.write(ByteBuffer.wrap(buf, 0, len));  
                }  
                dis.close();  
                System.out.println("下载完成");  
                channel.close();  
            } else if (attach.startsWith("[put]")) {  
                channel.write(encoder.encode(CharBuffer.wrap("开始上传")));  
                DataOutputStream dos = new DataOutputStream(  
                        new BufferedOutputStream(new FileOutputStream(new File(  
                                server_path, attach.split(":")[1]))));  
                int len = channel.read(buffer);  
                while (len >= 0) {  
                    if (len != 0) {  
                        buffer.flip();  
                    }  
                    dos.write(buffer.array(), 0, len);  
                    len = channel.read(buffer);  
                }  
                dos.close();  
                channel.close();  
                System.out.println("上传完毕");  
            }  
        }  
    }  
  
} 