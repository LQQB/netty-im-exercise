# netty-im-exercise
#### IO 编程
传统的IO模型中，每个连接创建成功之后就需要一个线程来维护，每个线程包含一个
while 死循环， 1w 个连接对应 1w 个线程。就会有 1w 个 while 死循环。
就会带来以下的问题：
1. 线程资源受限
2. 线程切换效率低下
3. IO 编程中， 数据读写以字节流为单位
#### NIO 编程
NIO 中新来一个连接不再创建一个新线程，而是把这条连接直接绑定到某个固定的线程中，
然后这条连接所有的读写都由这个线程来负责。

1. NIO 模型通常会有两个线程，每个线程绑定一个轮询器 selector ,在我们的例子中
serverSelector 负责轮序是否为新连接， clientSelector 负责轮询连接是否有数据可读

2. 服务端检测到新的连接之后，不再创建新连接，而是直接将连接绑定到 clientSelector 上
这样就不用像 IO 模型中 1w 个 while 循环中死等。
3.  clientSelector 被一个 while 死循环包裹着，如果在某一时刻有多
条连接有数据可读，那么通过 clientSelector.select(1) 方法可以轮询出来，批量读写
4. 数据读写面向 Buffer

#### Netty 编程
Netty 是一个异步事件驱动的网络应用框架，可以快速开发可维护的高性能服务器和客户端

#### 服务端启动的方法
1. handler() 方法

    ```text
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {
            protected void initChannel(NioServerSocketChannel ch) {
                System.out.println("服务端启动中");
            }
        })
    ```
    handler()方法呢，可以和服务端搭建过程中的childHandler()方法对应起来，childHandler() 用于指
    定处理新连接数据的读写处理逻辑， handler() 用于指定在服务端启动过程中的一些逻辑，通常情况下呢，
    我们用不着这个方法。

2. attr() 方法

    ```text
       serverBootstrap.attr(AttributeKey.newInstance("serverName"), "nettyServer")
    ```
    attr() 方法可以给服务端 chanel,也就是NioServerSocketChannel 指定一些自定义
    属性，然后我们可以通过 channel.attr() 取出属性比如，上面的代码我们指定我们服务端
    channel 的一个 serverName 属性，属性值为 nettyServer ，其实说白了就是给 
    NioServerSocketChannel 维护一个 map 而已，通常情况下，我们也用不上这个方法。
    
3. childAttr() 方法
    ```text
       serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
    ```
    上面的 childAttr 可以给每一条连接指定自定义属性，后续可以通用 channel.attr() 取出该属性
    
4. childOption() 方法

    ```text
    serverBootstrap
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
    ```
    childOption() 可以给每条连接设置一些TCP底层相关的属性
        
    * ChannelOption.SO_KEEPALIVE 表示是否开启 TCP 底层心跳机制,true为开启
    * ChannelOption.TCP_NODELAY 表示是否开启 Nagle 算法， true 表示关闭， false 表示开启
    通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，
    就开启。
    * ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间，超过这个时间还是
    建立不上的话则代表连接失败
        
5. option() 方法

    除了给每个连接设置这一系列属性之外，我们还可以给服务端channel设置一些属性，最常见的就是so_backlog，如下设置
    ```text
       serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024)
    ```
    表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，
    服务器处理创建新连接较慢，可以适当调大这个参

> 服务端启动流程，创建一个引导类，然后给它设置线程模型，IO模型，连接读写处理逻辑，绑定端口后，服务端就启动了 

> 服务端的 bind() 跟客户端的 connect() 方法是异步的

> 还可以给 channel  设置 底层的 TCP 的属性
    
#### TCP底层相关的属性    
* ChannelOption.SO_KEEPALIVE 表示是否开启 TCP 底层心跳机制,true为开启
* ChannelOption.TCP_NODELAY 表示是否开启 Nagle 算法， true 表示关闭， false 表示开启
通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，
就开启。
* ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间，超过这个时间还是
建立不上的话则代表连接失败
        
    
