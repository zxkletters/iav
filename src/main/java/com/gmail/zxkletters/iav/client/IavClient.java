/*
 * Copyright [2012] [zxkletters@gmail.com] Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.gmail.zxkletters.iav.client;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.rtsp.RtspMethods;
import org.jboss.netty.handler.codec.rtsp.RtspRequestEncoder;
import org.jboss.netty.handler.codec.rtsp.RtspResponseDecoder;
import org.jboss.netty.handler.codec.rtsp.RtspVersions;

import com.gmail.zxkletters.iav.handler.RtspResponseHandler;

/**
 * 类IavClient.java的实现描述：TODO 类实现描述
 * 
 * @author zxkletters@gmail.com 2012-8-18 下午3:34:23
 */
public class IavClient {

    public static void main(String[] args) {
        ClientBootstrap clientBootstrap = new ClientBootstrap(new NioClientSocketChannelFactory());

        clientBootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline p = Channels.pipeline();
                p.addLast("encoder", new RtspRequestEncoder());
                p.addLast("decoder", new RtspResponseDecoder());
                p.addLast("responseHandler", new RtspResponseHandler());
                return p;
            }

        });

        ChannelFuture channelFutrue = clientBootstrap.connect(new InetSocketAddress(17467));
        if (channelFutrue.isSuccess()) {
            Channel channel = channelFutrue.getChannel();
            HttpRequest request = new DefaultHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.DESCRIBE,
                                                         "rtsp://localhost:17467/vedio");
            ChannelFuture cf = channel.write(request);
            if (cf.isSuccess()) {
                System.out.println("send success!");
            }
        }
    }
}
