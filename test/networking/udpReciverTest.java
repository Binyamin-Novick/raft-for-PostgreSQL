package networking;


import org.junit.jupiter.api.Test;
import raft.sever.sever.messgeing.Messge;
import raft.sever.sever.messgeing.messgeDirecter;
import raft.utils.networking.udpReciver;
import raft.utils.networking.udpSender;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class udpReciverTest {

    BlockingQueue<Messge> mm =new ArrayBlockingQueue<>(200);

    BlockingQueue<Messge>tsend=new LinkedBlockingQueue<>();


    @Test
    public void sendandudpReciver() throws SocketException, InterruptedException {
        InetSocketAddress so =new InetSocketAddress("localhost",9010);
        InetSocketAddress se =new  InetSocketAddress("localhost",9011);
        udpSender sender =new udpSender(tsend,se);
        int i=0;
        udpReciver r=new udpReciver(new sorter(null),so);
        while (true) {
            Messge m = new Messge();
            m.reciver = so;
            m.sender = se;
            m.contents = "hello me";
            m.log = 0;
            m.Rid = 1;
            m.Sid = 2;
            m.term = 3;

            tsend.offer(m);
            Messge m2 = mm.take();
            assertEquals(m.log, m2.log);
            assertEquals(m.Rid, m2.Rid);
            assertEquals(m.Sid, m2.Sid);
            assertEquals(m.term, m2.term);
            assertTrue(m.contents.equals("hello me"));
            i++;

        }

    }

    class sorter extends messgeDirecter {


        public sorter(ConcurrentHashMap<Messge.type, BlockingQueue<Messge>> sortedIN) {
            super(sortedIN);
        }
        @Override
        public void sort(Messge m){
            mm.offer(m);


        }
    }

}