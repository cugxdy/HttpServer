# HttpServer
# 简易HTTP服务器
# 底层通过TCP连接传输数据----采用netty实现高并发环境(java Nio技术实现:selector->实现一个轮询器,线程进行selector.select()操作，进行多个TCP连接的处理。SocketChannel.register(selector,OP_READ,attachment)).
# 通过HTTP协议进行通信
