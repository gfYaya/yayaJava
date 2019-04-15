package cn.netty.exmaple.http.upload;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class HttpUploadServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger logger = Logger.getLogger(HttpUploadServerHandler.class.getName());

    private HttpRequest request;

    private boolean readingChunks;

    private HttpData partialContent;

    private final StringBuilder responseContent = new StringBuilder();

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);//Disk if size exceed

    private HttpPostRequestDecoder decoder;

    static{
        DiskFileUpload.deleteOnExitTemporaryFile = true; //should delete file on exit(in normal exit)
        DiskFileUpload.baseDirectory = null; //system temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true; //should delete file on exit(in normal exit)
        DiskAttribute.baseDirectory = null; //system temp director
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("upload server handler channelInactive");
        //super.channelInactive(ctx);
        if(decoder != null){
            //Clean all HttpDatas (on Disk) for the current request.
            decoder.cleanFiles();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("upload server handler exceptionCaught");
        //super.exceptionCaught(ctx, cause);
        logger.log(Level.WARNING, responseContent.toString(), cause);
        ctx.channel().close();
    }

    //执行了多次
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        logger.info("upload server handler channelRead0");
        if(msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if (!uri.getPath().startsWith("/form")) {
                //Write menu
                writeMenu(ctx);
                return;
            }
            responseContent.setLength(0);
            responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
            responseContent.append("===================================\r\n");
            responseContent.append("VERSION: " + request.protocolVersion().text() + "\r\n");
            responseContent.append("REQUEST_URI: " + request.uri() + "\r\n\r\n");
            responseContent.append("\r\n\r\n");

            //new getMethod
            for (Map.Entry<String, String> entry : request.headers()) {
                responseContent.append("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
            }
            responseContent.append("\r\n\r\n");

            //new getMethod
            Set<io.netty.handler.codec.http.cookie.Cookie> cookies;
            String value = request.headers().get(HttpHeaderNames.COOKIE);
            if (value == null) {
                cookies = Collections.emptySet();
            } else {
                cookies = ServerCookieDecoder.STRICT.decode(value);
            }
            for (io.netty.handler.codec.http.cookie.Cookie cookie : cookies) {
                responseContent.append("COOKIE: " + cookie + "\r\n");
            }
            responseContent.append("\r\n\r\n");

            QueryStringDecoder decoderQuery = new QueryStringDecoder(request.uri());
            //Returns the decoded key-value parameter pairs of the URI.
            Map<String, List<String>> uriAttributes = decoderQuery.parameters();
            for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                for (String attrVal : attr.getValue()) {
                    responseContent.append("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
                }
            }
            responseContent.append("\r\n\r\n");

            //if GET method:should not try to create a HttpPostRequestDecoder
            if (HttpMethod.GET.equals(request.method())) {
                //Sp stop here
                responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
                //Not now: LastHttpContent will be sent writeResposne(ctx.channel())
                return;
            }
            try {
                //This decoder will decode Body and can handle POST BODY.And the factory used to create InterfaceHttpData.
                decoder = new HttpPostRequestDecoder(factory, request);
            } catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
                e.printStackTrace();
                responseContent.append(e.getMessage());
                writeResponse(ctx.channel());
                ctx.channel().close();
                return;
            }

            readingChunks = HttpUtil.isTransferEncodingChunked(request);
            responseContent.append("Is Chunked:" + readingChunks + "\r\n");
            //True if this request is a Multipart request , multipart 指的是 multipart/form-data (参考的Spring MVC的 multipart)?
            // 而不是chunked分块?  https://www.jianshu.com/p/c09efa423c2d
            responseContent.append("Is Multipart" + decoder.isMultipart() + "\r\n");
            if (readingChunks) {
                //chunk version
                responseContent.append("Chunks :");
                //ChannelPipeline是线程安全的,但是ChannelHandler不是线程安全的?不过按照徐妈所给出的答案,应该是历史优化代码的时候忘记了.
                readingChunks = true;
            }
        }

            //check if the decoder was constructed before
            //if not it handles the form get
            if(decoder != null){
                if(msg instanceof HttpContent){
                    //new chunk is received
                    HttpContent chunk = (HttpContent)msg;
                    try{
                        decoder.offer(chunk);
                    }catch(HttpPostRequestDecoder.ErrorDataDecoderException e){
                        e.printStackTrace();
                        responseContent.append(e.getMessage());
                        writeResponse(ctx.channel());
                        ctx.channel().close();
                        return;
                    }
                    responseContent.append("o");
                    //example of reading chunk by chunk (minimize memory usage due to Factory)
                    readHttpDataChunkByChunk();
                    //example of reading only if at the end
                    if(chunk instanceof LastHttpContent){
                        writeResponse(ctx.channel());
                        readingChunks = false;
                        reset();
                    }
                }
            }else{
                writeResponse(ctx.channel());
            }

    }

    /**
     * Example of reading request by chunk and getting values from chunk by chunk
     */
    private void readHttpDataChunkByChunk(){
        try{
            while(decoder.hasNext()){
                InterfaceHttpData data = decoder.next();
                if(data != null){
                    //check if current HrtpData is a FileUpload and previously set as partial
                    if(partialContent == data){
                        logger.info(" 100% (FinalSize: " + partialContent.length() + ")");
                        partialContent = null;  //why?
                    }
                    // new value
                    writeHttpData(data);
                }
            }
            //Check partial decoding for a FileUpload
            InterfaceHttpData data = decoder.currentPartialHttpData();
            if(data != null){
                StringBuilder builder = new StringBuilder();
                if(partialContent == null){
                    partialContent = (HttpData) data;
                    if(partialContent instanceof FileUpload){
                        builder.append("Start FileUpload: ").append(((FileUpload) partialContent).getFilename()).append(" ");
                    }else{
                        builder.append("Start Attribute: ").append(partialContent.getName()).append(" ");
                    }
                    builder.append("(DefinedSize: ").append(partialContent.definedLength()).append(")");
                }
                if(partialContent.definedLength() > 0){
                    builder.append(" ").append(partialContent.length() * 100 / partialContent.definedLength()).append("% ");
                    logger.info(builder.toString());
                }else {
                    builder.append(" ").append(partialContent.length()).append(" ");
                    logger.info(builder.toString());
                }
            }
        }catch(HttpPostRequestDecoder.EndOfDataDecoderException e){
            //end
            responseContent.append("\r\n\r\nEND OF CONTENT CHUNK BY CHUNK\r\n\r\n");
        }
    }

    public void writeHttpData(InterfaceHttpData data){
        if(data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute){ //非 file upload?
            Attribute attribute = (Attribute) data;
            String value;
            try{
                value = attribute.getValue();
            }catch(IOException e){
                //Error while reading data from File, only print name and error.
                e.printStackTrace();
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.getName() +
                        " Error while reading value: " + e.getMessage() + "\r\n");
                return;
            }
            if(value.length() > 100){
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute.getName() + " data too long\r\n");
            } else {
                responseContent.append("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ": " + attribute + "\r\n");
            }
        }else{
            responseContent.append("\r\nBODY FileUpload: " + data.getHttpDataType().name() + ": " + data + "\r\n");
            if(data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload){
                //FileUpload interface that could be in memory, on temporary file or any other implementations.
                //Most methods are inspired from java.io.File API.
                FileUpload fileUpload = (FileUpload)data;
                // completed  => all data are stored
                if(fileUpload.isCompleted()){
                    if(fileUpload.length() < 10000){
                        responseContent.append("\tContent of file\r\n");
                        try{
                            responseContent.append(fileUpload.getString(fileUpload.getCharset()));
                        }catch(IOException e){
                            // do nothing for the example
                            e.printStackTrace();
                        }
                        responseContent.append("\r\n");
                    }else{
                        responseContent.append("\tFile too long to be printed out:" + fileUpload.length() + "\r\n");
                    }
                    // fileUpload.isInMemory();// tells if the file is in Memory or on File
                    // fileUpload.renameTo(dest); // enable to move into another File dest
                    try {
                        File dest = new File("D:/tmp/upload_yaya.txt");
                        fileUpload.renameTo(dest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // decoder.removeFileUploadFromClean(fileUpload); //remove the File of to delete file
                }else{
                    responseContent.append("\tFile to be continued but should not!\r\n");
                }
            }
        }
    }

    private void reset(){
        request = null;

        //to destroy the decoder to release all resources
        //为什么下一行使用了 decoder = null ,还要destroy?这是一个side effect方法?因为还需要回收其他相关的操作?
        /*
         // release all data which was not yet pulled
        for (int i = bodyListHttpDataRank; i < bodyListHttpData.size(); i++) {
            bodyListHttpData.get(i).release();
        }
        */
        decoder.destroy();
        decoder = null;
    }

    private void writeResponse(Channel channel){
        //Convert the response to a ChannelBuffer
        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        responseContent.setLength(0);

        //Decide whether to close the connection or not.
        boolean close = request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE,true)
                || request.protocolVersion().equals(HttpVersion.HTTP_1_0)  //HTTP1.1中引入了持久连接
                && !request.headers().contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE,true);

        //Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        if(!close){
            //There's no need to add "Content-Length" header if this is the last response.
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        }

        Set<io.netty.handler.codec.http.cookie.Cookie> cookies;
        String value = request.headers().get(HttpHeaderNames.COOKIE);
        if(value == null){
            cookies = Collections.EMPTY_SET;
        }else{
            cookies = ServerCookieDecoder.STRICT.decode(value);
        }
        if(!cookies.isEmpty()){
            //Reset the cookies if necessary
            for(io.netty.handler.codec.http.cookie.Cookie cookie : cookies){
                response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
            }
        }
        //Write the response
        ChannelFuture future = channel.writeAndFlush(response);
        //Close the connection after the write operation is done if necessary.
        if(close){
            //Invoked when the operation associated with the {@link Future} has been completed.
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void writeMenu(ChannelHandlerContext ctx){
        //print serveral html forms
        //convert the response content to a ChannelBuffer
        responseContent.setLength(0);

        // create Pseudo Menu
        responseContent.append("<html>");
        responseContent.append("<head>");
        responseContent.append("<title>Netty Test Form</title>\r\n");
        responseContent.append("</head>\r\n");
        responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr>");
        responseContent.append("<td>");
        responseContent.append("<h1>Netty Test Form</h1>");
        responseContent.append("Choose one FORM");
        responseContent.append("</td>");
        responseContent.append("</tr>");
        responseContent.append("</table>\r\n");

        // GET
        responseContent.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
        responseContent.append("<input type=hidden name=getform value=\"GET\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        // POST
        responseContent.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
        responseContent.append("<input type=hidden name=getform value=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("<tr><td>Fill with file (only file name will be transmitted): <br>  <input type=file name=\"myfile\">");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        // POST with enctype="multipart/form-data"
        responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
        responseContent.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
        responseContent.append("<input type=hidden name=getform value=\"POST\">");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
        responseContent.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
        responseContent.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
        responseContent.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
        responseContent.append("</td></tr>");
        responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
        responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
        responseContent.append("</table></FORM>\r\n");
        responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

        responseContent.append("</body>");
        responseContent.append("</html>");

        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        //Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

        //write the response
        ctx.channel().writeAndFlush(response);
    }
}
