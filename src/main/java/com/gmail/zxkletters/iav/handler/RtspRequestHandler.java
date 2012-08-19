/*
 * Copyright [2012] [zxkletters@gmail.com] Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.gmail.zxkletters.iav.handler;

import java.util.Date;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.rtsp.RtspHeaders;
import org.jboss.netty.handler.codec.rtsp.RtspMethods;
import org.jboss.netty.handler.codec.rtsp.RtspResponseStatuses;
import org.jboss.netty.handler.codec.rtsp.RtspVersions;

/**
 * 类RtspRequestHandler.java的实现描述：TODO 类实现描述
 * 
 * @author zxkletters@gmail.com 2012-8-17 下午10:15:25
 */
public class RtspRequestHandler extends SimpleChannelUpstreamHandler {

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object event = e.getMessage();
        HttpResponse httpResponse = null;

        if (event instanceof HttpRequest) {
            HttpRequest httpRequest = (DefaultHttpRequest) event;
            String methodName = httpRequest.getMethod().getName();

            // deal Rtsp Methods: DESCRIBE, ANNOUNCE, SETUP, PLAY, PAUSE, TEARDOWN
            if (RtspMethods.DESCRIBE.getName().equals(methodName)) {
                httpResponse = getDescribeResponse(httpRequest);
            } else if (RtspMethods.ANNOUNCE.getName().equals(methodName)) {
                httpResponse = getAnnounceResponse(httpRequest);
            } else if (RtspMethods.SETUP.getName().equals(methodName)) {
                httpResponse = getSetupResponse(httpRequest);
            } else if (RtspMethods.PLAY.getName().equals(methodName)) {
                httpResponse = getPlayResponse(httpRequest);
            } else if (RtspMethods.PAUSE.getName().equals(methodName)) {
                httpResponse = getPauseResponse(httpRequest);
            } else if (RtspMethods.TEARDOWN.getName().equals(methodName)) {
                httpResponse = getTeardownResponse(httpRequest);
            } else {
                httpResponse = getNotAllowedResponse(httpRequest);
            }

        }

        if (httpResponse == null) {
            httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.NOT_IMPLEMENTED);
        }

        System.out.println("=======  request ======= \n" + event);

        ChannelFuture channelFuture = ctx.getChannel().write(httpResponse);
        if (channelFuture.isSuccess()) {
            System.out.println("\n =======  response ======= \n" + httpResponse);
            channelFuture.getChannel().close();
        }
    }

    /**
     * not allow's method response
     * 
     * @param httpRequest
     * @return
     */
    private HttpResponse getNotAllowedResponse(HttpRequest httpRequest) {
        HttpResponse httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0,
                                                            RtspResponseStatuses.METHOD_NOT_ALLOWED);
        httpResponse.addHeader(RtspHeaders.Names.CSEQ, httpRequest.getHeader(RtspHeaders.Names.CSEQ));
        httpResponse.addHeader(RtspHeaders.Names.ALLOW, "DESCRIBE, ANNOUNCE, SETUP, PLAY, PAUSE, TEARDOWN");

        return httpResponse;
    }

    /**
     * DESCRIBE Method's response
     * 
     * @param httpRequest
     * @return
     */
    private HttpResponse getDescribeResponse(HttpRequest httpRequest) {
        HttpResponse httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        httpResponse.addHeader(RtspHeaders.Names.CSEQ, httpRequest.getHeader(RtspHeaders.Names.CSEQ));
        httpResponse.addHeader(RtspHeaders.Names.CONTENT_TYPE, "application/sdp");
        httpResponse.addHeader(RtspHeaders.Names.CONTENT_LENGTH, 123);

        return httpResponse;
    }

    /**
     * ANNOUNCE Method's response
     * 
     * @param httpRequest
     * @return
     */
    private HttpResponse getAnnounceResponse(HttpRequest httpRequest) {
        HttpResponse httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        httpResponse.addHeader(RtspHeaders.Names.CSEQ, httpRequest.getHeader(RtspHeaders.Names.CSEQ));

        return httpResponse;
    }

    /**
     * SETUP Method's response
     * 
     * @param httpRequest
     * @return
     */
    private HttpResponse getSetupResponse(HttpRequest httpRequest) {
        // If a SETUP request to a server includes a session
        // identifier, the server MUST bundle this setup request into the
        // existing session or return error "459 Aggregate Operation Not
        // Allowed" (see Section 11.3.10).

        // timeout parameter affect PAUSE method's performace

        HttpResponse httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        httpResponse.addHeader(RtspHeaders.Names.CSEQ, httpRequest.getHeader(RtspHeaders.Names.CSEQ));
        httpResponse.addHeader(RtspHeaders.Names.DATE, new Date());
        httpResponse.addHeader(RtspHeaders.Names.SESSION, 1234567);
        httpResponse.addHeader(RtspHeaders.Names.TRANSPORT,
                               "RTP/AVP;unicast;client_port=4588-4589;server_port=6256-6257");

        return httpResponse;
    }

    /**
     * PLAY Method's response
     * 
     * @param httpRequest
     * @return
     */
    private HttpResponse getPlayResponse(HttpRequest httpRequest) {
        // TODO　注意：play请求有一些注意点，实现时要考虑到 @see http://www.ietf.org/rfc/rfc2326.txt #10.5

        // The PLAY method tells the server to start sending data via the
        // mechanism specified in SETUP. A client MUST NOT issue a PLAY request
        // until any outstanding SETUP requests have been acknowledged as
        // successful.

        // PLAY requests may be pipelined (queued); a server
        // MUST queue PLAY requests to be executed in order. That is, a PLAY
        // request arriving while a previous PLAY request is still active is
        // delayed until the first has been completed.

        HttpResponse httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        httpResponse.addHeader(RtspHeaders.Names.CSEQ, httpRequest.getHeader(RtspHeaders.Names.CSEQ));
        httpResponse.addHeader(RtspHeaders.Names.SESSION, httpRequest.getHeader(RtspHeaders.Names.SESSION));
        httpResponse.addHeader(RtspHeaders.Names.RANGE, httpRequest.getHeader(RtspHeaders.Names.RANGE));

        return httpResponse;
    }

    /**
     * PAUSE Method's response
     * 
     * @param httpRequest
     * @return
     */
    private HttpResponse getPauseResponse(HttpRequest httpRequest) {
        // A PAUSE request discards all queued PLAY requests. However, the pause
        // point in the media stream MUST be maintained. A subsequent PLAY
        // request without Range header resumes from the pause point.

        // may contain a Range header specifying when the
        // stream or presentation is to be halted, "pause point"
        // see rfc: http://www.ietf.org/rfc/rfc2326.txt#10.6

        HttpResponse httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        httpResponse.addHeader(RtspHeaders.Names.CSEQ, httpRequest.getHeader(RtspHeaders.Names.CSEQ));
        httpResponse.addHeader(RtspHeaders.Names.DATE, new Date());

        return httpResponse;
    }

    /**
     * TEARDOWN Method's response
     * 
     * @param httpRequest
     * @return
     */
    private HttpResponse getTeardownResponse(HttpRequest httpRequest) {
        HttpResponse httpResponse = new DefaultHttpResponse(RtspVersions.RTSP_1_0, RtspResponseStatuses.OK);
        httpResponse.addHeader(RtspHeaders.Names.CSEQ, httpRequest.getHeader(RtspHeaders.Names.CSEQ));

        return httpResponse;
    }
}
