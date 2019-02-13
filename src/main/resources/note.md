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