import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
//Client class
public class NIOClient {
    public static void main(String[] args) {
        try{
            String[] messages = {"a", "b", "c", "exit"};
            System.out.println("Starting client");
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8100));

            for(String msg : messages) {
                System.out.println("Message" + msg);

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(msg.getBytes());
                buffer.flip();
                int bytesWritten = client.write(buffer);
                String.format("Message: %s\nbufferforBytes %d", msg, bytesWritten);
            }
            client.close();
            System.out.println("Client connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
