package com.lvhao.schedulejob.thrift.server;

import com.lvhao.schedulejob.thrift.protocol.HelloService;
import com.lvhao.schedulejob.thrift.serviceimpl.HelloServiceImpl;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import java.text.MessageFormat;

/**
 * Thrift服务端
 *
 * @author: lvhao
 * @since: 2016-7-8 18:09
 */
public class ServerMain {
    private static final int PORT = 7777;
    public static void main(String[] args) {
        try {
            // 设置服务端口为 7777
            TNonblockingServerSocket nonBlockingServerSocket = new TNonblockingServerSocket(PORT);

            // 关联处理器与 HelloService 服务的实现
            TMultiplexedProcessor tMultiplexedProcessor = new TMultiplexedProcessor();
            tMultiplexedProcessor.registerProcessor("HelloService",new HelloService.Processor(new HelloServiceImpl()));

            TThreadedSelectorServer.Args threadedSelectorServerArgs = new TThreadedSelectorServer.Args(nonBlockingServerSocket);
            TProcessorFactory processorFactory = new TProcessorFactory(tMultiplexedProcessor);
            threadedSelectorServerArgs.processorFactory(processorFactory);

            // 协议
            threadedSelectorServerArgs.protocolFactory(new TCompactProtocol.Factory());

            // transport
            threadedSelectorServerArgs.transportFactory(new TFramedTransport.Factory());

            TThreadedSelectorServer server = new TThreadedSelectorServer(threadedSelectorServerArgs);
            System.out.println(MessageFormat.format("server start listen on {0}...",String.valueOf(PORT)));
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
