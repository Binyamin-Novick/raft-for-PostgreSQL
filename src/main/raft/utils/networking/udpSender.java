package raft.utils.networking;

import com.google.gson.Gson;
import raft.sever.sever.messgeing.Messge;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class udpSender {
    DatagramSocket dgr;
    BlockingQueue <Messge> out;
    Gson gson=new Gson();
    public  udpSender(BlockingQueue<Messge> outbound, InetSocketAddress myad) throws SocketException {
        dgr =new DatagramSocket(myad.getPort(), myad.getAddress());
        out=outbound;
        Thread t=new Thread(new send());
        t.setDaemon(true);
        t.start();
    }

    // have loop that will just send the stuff out;

    class send implements Runnable{
        Messge m;
        DatagramSocket socket;
        DatagramPacket packet;
        byte[] bytes;
        @Override
        public void run() {
            while (true){
                try {
                    m=out.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                     socket = new DatagramSocket();
                } catch (SocketException e) {
                    throw new RuntimeException(e);
                }
                bytes =gson.toJson(m).getBytes();
                packet =new DatagramPacket(bytes, bytes.length,m.reciver);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                socket.close();

            }
        }
    }
}
