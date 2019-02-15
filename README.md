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
        
    
> 客户端跟服务端的逻辑处理均是在启动的时候，给逻辑处理链 pipeline 添加逻辑处理器，
来编写数据的读写逻辑

> 客户端连接成功之后会回调到逻辑处理器 channelActive() 方法，不管是客户端还是服务端，
收到数据之后都会调用 channelRead() 方法

#### 数据传输载体 ByteBuf
> 首先我们要知道 Netty 的数据读写只认 ByteBuf 

ByteBuf 是一个字节容器，容器分成三个部分，第一部分就是已丢弃字节，第二部分是可读字节，
第三部分是已写字节。

以上三段被两个指针划分，从左到右依次是，一个是读指针(readerIndex)，一个是写指针(writerIndex)，
还有个是 capacity ，表示 ByteBuf 底层的内存容量

##### 容量 API
   * capacity()
   
    表示 ByteBuf 底层占用了多少字节的内存（包括丢弃的字节、可读字节、可写字节），
    不同的底层实现机制有不同的计算方式，后面我们讲 ByteBuf 的分类的时候会讲到
    
   * maxCapacity()
    
    表示 ByteBuf 底层最大能够占用多少字节的内存，当向 ByteBuf 中写数据的时候，
    如果发现容量不足，则进行扩容，直到扩容到 maxCapacity，超过这个数，就抛异常
    
   * readableBytes() 与 isReadable()
    
    readableBytes() 表示 ByteBuf 当前可读的字节数，它的值等于 
    writerIndex-readerIndex，如果两者相等，则不可读，isReadable() 
    方法返回 false
    
   * writableBytes()、 isWritable() 与 maxWritableBytes()
    
    writableBytes() 表示 ByteBuf 当前可写的字节数，它的值等于 
    capacity-writerIndex，如果两者相等，则表示不可写，
    isWritable() 返回 false，但是这个时候，并不代表不能往 ByteBuf 中写数据了，
     如果发现往 ByteBuf 中写数据写不进去的话，Netty 会自动扩容 ByteBuf，直到扩容
     到底层的内存大小为 maxCapacity，而 maxWritableBytes() 就表示可写的最大字节
     数，它的值等于 maxCapacity-writerIndex
     
##### 读写指针相关的 API
   * readerIndex() 与 readerIndex(int)
   
    前者表示返回当前的读指针 readerIndex, 后者表示设置读指针
    
   * writeIndex() 与 writeIndex(int)
 
    前者表示返回当前的写指针 writerIndex, 后者表示设置写指针
   
   * markReaderIndex() 与 resetReaderIndex()
   
    前者表示把当前的读指针保存起来，后者表示把当前的读指针恢复到之前保存的值，下面两段代码是等价的
    常用于解析自定义协议的数据包
    
```Text
// 代码片段1
int readerIndex = buffer.readerIndex();
// .. 其他操作
buffer.readerIndex(readerIndex);
    
// 代码片段2
buffer.markReaderIndex();
// .. 其他操作
buffer.resetReaderIndex();
```
    
   * markWriterIndex() 与 resetWriterIndex()
    
    这一对 API 的作用与上述一对 API 类似，这里不再 赘述
    

     
##### 读写 API

   * release() 与 retain()

    由于 Netty 使用了堆外内存，而堆外内存是不被 jvm 直接管理的，也就是说申请到的内存无法被垃圾回收器直接回收，
    所以需要我们手动回收。有点类似于c语言里面，申请到的内存必须手工释放，否则会造成内存泄漏。

    Netty 的 ByteBuf 是通过引用计数的方式管理的，如果一个 ByteBuf 没有地方被引用到，
    需要回收底层内存。默认情况下，当创建完一个 ByteBuf，它的引用为1，然后每次调用 
     retain() 方法， 它的引用就加一， release() 方法原理是将引用计数减一，减完之后如果
    发现引用计数为0，则直接回收 ByteBuf 底层的内存。


#### 客户端与服务端的通讯协议的编解码

无论是 Netty 还是原始的 Socket 编程，都是基于 TCP 通讯的数据包格式均为
二进制,但是单纯的二进制的包是没有意义的，然后就有了通讯协议。
通讯协议: 客户端与服务端事先商量好的，每一个二进制数据包中每一段字节都分别代表了什么含义的规则。


一套标准的通讯协议都会含有: 魔数，版本号，序列化算法，指令，数据长度，数据


#### 客户端登录流程

客户端发送一个 登录请求的数据包，服务端接收并校验，校验通过返回 成功的登录响应的数据包，
校验不通过返回 失败的登录响应的数据包

#### 客户端跟服务端收发消息
 * attr() 通过 Channel 绑定属性来设置某些状态，通过判断状态可以知道 Channel 的登录状态
 * 定义一对 MESSAGE 请求响应的数据包，来进行消息的收发
 * 通过控制台获取消息，并发送给服务端