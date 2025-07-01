package raft.sever.sever.logs;

import com.google.gson.Gson;
import raft.sever.sever.messgeing.Messge;
import raft.sever.sever.serverbox;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;


/**
 * this is a stage one stage will have more logic added later
 */

public class nclResender implements Runnable{

    serverbox sb;

    ConcurrentHashMap<Integer,Integer> quramcount;
    ConcurrentHashMap<Long,String> nclog;
    Gson gson=new Gson();

    @Override
    public void run() {

        while (sb.statuse== serverbox.servertype.Leader){
            try {
                if(!Thread.interrupted())
                    sleep(300);
            } catch (InterruptedException e) {

            }finally {//
                sb.Broadcast(Messge.type.propuse,gson.toJson(nclog.values()));
            }
        }

    }
}
