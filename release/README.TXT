1,release目录是程序发布运行目录，
	其中：lib包含了程序运行时依赖的相关jar和程序本身的jar包，依赖的jar包从dbserver/lib目录下拷贝
	程序自身的jar包由源代码打包
2，程序运行命令：
	./start.sh startserver 启动服务器端
	./start.sh startclient 启动客户端
