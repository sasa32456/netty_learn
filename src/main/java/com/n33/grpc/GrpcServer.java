package com.n33.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * 服务端
 *
 * @author N33
 * @date 2019/7/7
 */
public class GrpcServer {

    private Server server;

    private void start() throws IOException {
        this.server = ServerBuilder.forPort(8899).addService(new StudentServiceImpl()).build().start();

        System.out.println("server started!");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("关闭jvm");
            GrpcServer.this.stop();
        }));

        System.out.println("执行到这里");
    }

    private void stop() {
        if (null != this.server) {
            this.server.shutdown();
        }
    }

    private void awaitTermination() throws InterruptedException {
        if (null != this.server) {
            this.server.awaitTermination();
//           this.server.awaitTermination(3000, TimeUnit.MILLISECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        GrpcServer server = new GrpcServer();

        server.start();
        server.awaitTermination();
    }

}


/**
 * 注册新的虚拟机关闭挂钩。
 *
 * <p> Java虚拟机<i>关闭</ i>以响应两种事件：
 *
 * <ul>
*
 * <li>程序<i>退出</ i>正常，当最后一个非守护程序线程退出或<tt> {@link #exit exit} </ tt>时（相当于{@link System＃exit）调用（int）System.exit}）方法，或
 *
 * <li>虚拟机<i>终止</ i>以响应用户中断，例如输入<tt> ^ C </ tt>或系统范围的事件，例如用户注销或系统关闭。
 *
 * </ ul>
 *
 * <p> <i> shutdown hook </ i>只是一个初始化但未启动的线程。当虚拟机开始其关闭序列时，它将以某种未指定的顺序启动所有已注册的关闭挂钩，
 * 并让它们同时运行。当所有挂钩都完成后，如果启用了finalization-on-exit，它将运行所有未读取的终结器。最后，虚拟机将停止。请注意，
 * 守护程序线程将在关闭序列期间继续运行，如果通过调用<tt> {@link #exit exit} </ tt>方法启动关闭，则非守护程序线程也将继续运行。
 *
 * <p>一旦关闭序列开始，只能通过调用<tt> {@link #halt halt} </ tt>方法来停止它，该方法强制终止虚拟机。
 *
 * <p>一旦关闭序列开始，就无法注册新的关闭挂钩或取消注册先前注册的挂钩。尝试这些操作中的任何一个都会导致抛出<tt> {@link IllegalStateException} </ tt>。
 *
 * <p>关闭挂钩在虚拟机的生命周期中的微妙时间运行，因此应该进行防御编码。特别是它们应该被编写为线程安全的并且尽可能避免死锁。
 * 他们也不应盲目依赖可能已经注册了自己的关机钩子的服务，因此他们自己可能正在关闭。例如，尝试使用其他基于线程的服务（例如AWT事件派发线程）可能会导致死锁。
 *
 * <p>关机挂钩也应该快速完成工作。当程序调用<tt> {@link #exit exit} </ tt>时，期望虚拟机将立即关闭并退出。
 * 当虚拟机因用户注销或系统关闭而终止时，底层操作系统可能只允许一段固定的时间来关闭和退出。因此，不建议尝试任何用户交互或在关闭钩子中执行长时间运行的计算。
 *
 * <p>通过调用线程的<tt> {@link ThreadGroup}的<tt> {@link ThreadGroup＃uncaughtException uncaughtException} </ tt>方法，
 * 在任何其他线程中都可以在shutdown钩子中处理未捕获的异常</对象。此方法的默认实现将异常的堆栈跟踪打印到<tt> {@link System＃err} </ tt>并终止该线程;
 * 它不会导致虚拟机退出或停止。
 *
 * <p>在极少数情况下，虚拟机可能会<i>中止</ i>，即停止运行而不会干净地关闭。当外部终止虚拟机时会发生这种情况，例如在Unix上使用<tt> SIGKILL </ tt>
 * 信号或在Microsoft Windows上使用<tt> TerminateProcess </ tt>调用。如果本机方法因例如破坏内部数据结构或尝试访问不存在的内存而出错，则虚拟机也可能中止。
 * 如果虚拟机中止，则无法保证是否将运行任何关闭挂钩。 <P>
 *
 * @param hook
 * 初始化但未启动的<tt> {@link Thread} </ tt>对象
 * @throws IllegalArgumentException
 * 如果已经注册了指定的挂钩，或者可以确定挂钩已在运行或已经运行
 * @throws IllegalStateException
 * 如果虚拟机已在关闭的过程中
 * @throws SecurityException
 * 如果存在安全管理器并且它拒绝<tt> {@link RuntimePermission}（“shutdownHooks”）</ tt>
 * @see #removeShutdownHook
 * @see #halt(int)
 * @see #exit(int)
 * @since 1.3
 * <p>
 * <p>
 * public void addShutdownHook(Thread hook) {
 * SecurityManager sm = System.getSecurityManager();
 * if (sm != null) {
 * sm.checkPermission(new RuntimePermission("shutdownHooks"));
 * }
 * ApplicationShutdownHooks.add(hook);
 * }
 */
