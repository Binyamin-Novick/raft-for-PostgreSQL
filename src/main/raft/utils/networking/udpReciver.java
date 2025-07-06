package raft.utils.networking;

import com.google.gson.Gson;
import raft.sever.sever.messgeing.Messge;
import raft.sever.sever.messgeing.messgeDirecter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class udpReciver {
    messgeDirecter md;
    Gson gson=new Gson();
    InetSocketAddress address;

    public udpReciver(messgeDirecter md, InetSocketAddress sc){
        this.md=md;
        address=sc;
        Runnable g =new geter();
        Thread t = new Thread(g);
        t.setDaemon(true);
        t.start();

    }


    class geter implements Runnable {


        @Override
        public void run() {
            DatagramSocket socket;
            Messge m;
            DatagramPacket packet;
            try {
                socket= new DatagramSocket(address);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }

            while (true){
                packet=new DatagramPacket(new byte[4096],4096);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // I will eventuly replace this with a faster version; that does not need to hav 2 alices;
               m= gson.fromJson(new InputStreamReader(new ByteArrayInputStream(packet.getData()), StandardCharsets.UTF_8),Messge.class);
                md.sort(m);

            }
        }
    }



}
