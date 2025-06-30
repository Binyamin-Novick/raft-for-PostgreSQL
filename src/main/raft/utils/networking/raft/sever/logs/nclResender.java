package raft.utils.networking.raft.sever.logs;

import raft.utils.networking.messgeing.Messge;
import raft.utils.networking.raft.sever.serverbox;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;



/**
 * this is a stage one stage will have more logic added later
 */

public class nclResender implements Runnable{

    serverbox sb;

    ConcurrentHashMap<Integer,Integer> quramcount;
    BlockingQueue<Messge> nclog;
    @Override
    public void run() {

        while (sb.statuse== serverbox.servertype.Leader){
            try {
                wait(300);
            } catch (InterruptedException e) {

            }finally {
                nclog.parallelStream().peek(x->sb.Broadcast(Messge.type.propuse,x.contents)).collect(Collectors.toList());
            }
        }

    }
}
