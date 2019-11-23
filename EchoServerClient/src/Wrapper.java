import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

class Wrapper
{
    SocketChannel client;
    ByteBuffer linkBuffer;
    SelectionKey key;

    Wrapper(SocketChannel client, ByteBuffer linkBuffer, SelectionKey key)
    {
        this.client = client;
        this.linkBuffer = linkBuffer;
        this.key = key;
    }
}
