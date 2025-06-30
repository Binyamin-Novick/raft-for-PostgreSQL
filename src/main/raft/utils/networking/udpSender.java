package raft.utils.networking;

import raft.utils.networking.messgeing.Messge;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

public class udpSender {
    DatagramSocket dgr;
    public  udpSender(BlockingQueue<Messge> outbound, InetSocketAddress myad) throws SocketException {
        dgr =new DatagramSocket(myad.getPort(), myad.getAddress());
    }

    // have loop that will just send the stuff out;
}
