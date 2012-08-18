/*
 * Copyright [2012] [zxkletters@gmail.com] Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.gmail.zxkletters.core;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.rtsp.RtspRequestDecoder;
import org.jboss.netty.handler.codec.rtsp.RtspRequestEncoder;

/**
 * ��IavServer.java��ʵ��������TODO ��ʵ������
 * 
 * @author zxkletters@gmail.com 2012-8-17 ����9:52:16
 */
public class IavServer {

    private ServerBootstrap server;

    public void init() {
        server = new ServerBootstrap(new NioServerSocketChannelFactory());

        server.setOption("child.tcpNoDelay", true);
        server.setOption("child.receiveBufferSize", 1048576);

        server.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline p = Channels.pipeline();
                p.addLast("encoder", new RtspRequestEncoder());
                p.addLast("decoder", new RtspRequestDecoder());
                p.addLast("myHandler", new RtspRequestHandler());
                return p;
            }

        });

        server.bind(new InetSocketAddress(17467));
    }

    public void destroy() {
        if (server != null) {
            server.releaseExternalResources();
        }
    }
}