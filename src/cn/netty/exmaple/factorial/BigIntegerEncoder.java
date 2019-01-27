package cn.netty.exmaple.factorial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Decodes the binary representation of a {@link BigInteger} prepended
 * with a magic number ('F' or 0x46) and a 32-bit integer length prefix into a
 * {@link BigInteger} instance.  For example, { 'F', 0, 0, 0, 1, 42 } will be
 * decoded into new BigInteger("42").
 */
public class BigIntegerEncoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // Wait until the length prefix is available.
        if(byteBuf.readableBytes()<5){
            return;
        }
        /*markReaderIndex: 为何这里面没有接收,而且返回值是一个Bytebuf?
          徐妈:请求合并，其实就是加大一次 flush 的字节数,减少网络 io 次数.mark是为了reset,读到半包之后 reset 一下.
          南桥畂翊:就是把当前位置备份一下,方便链式编程.
            class AbstractByteBuf:
           public ByteBuf markReaderIndex() {
                 markedReaderIndex = readerIndex;
                return this;
           }
           而且markedReaderIndex是private变量,无法读取到.reset执行的时候 会 readerIndex = markedReaderIndex 这个操作,重新读取回去.
        */
        byteBuf.markReaderIndex();

    }
}
