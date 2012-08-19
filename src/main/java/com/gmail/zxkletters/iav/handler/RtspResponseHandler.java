/*
 * Copyright [2012] [zxkletters@gmail.com] Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.gmail.zxkletters.iav.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * 类RtspRequestHandler.java的实现描述：TODO 类实现描述
 * 
 * @author zxkletters@gmail.com 2012-8-17 下午10:15:25
 */
public class RtspResponseHandler extends SimpleChannelUpstreamHandler {

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("======  receive response  ====== \n" + e.getMessage());
    }
}
