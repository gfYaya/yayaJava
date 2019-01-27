package cn.netty.exmaple.factorial;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.math.BigInteger;

/**
 * Encodes a {@link Number} into the binary representation prepended with
 * a magic number ('F' or 0x46) and a 32-bit length prefix.  For example, 42
 * will be encoded to { 'F', 0, 0, 0, 1, 42 }.
 */
public class NumberEncoder extends MessageToByteEncoder<Number> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Number number, ByteBuf byteBuf) throws Exception {
        // Convert to a BigInteger first for easier implementation.
        BigInteger v;
        if(number instanceof BigInteger ){
            v = (BigInteger)number;
        } else{
            v = new BigInteger(String.valueOf(number));
        }

        // Convert the number into a byte array.
        byte[] data = v.toByteArray();
        int datalength = data.length;

        // Write a message.
        byteBuf.writeByte((byte) 'F'); // magic number
        byteBuf.writeInt(datalength);  // data length
        byteBuf.writeBytes(data);      // data
    }
}
