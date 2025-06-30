package raft.utils.networking.raft.sever.logs;

import com.google.gson.Gson;
import raft.utils.networking.messgeing.Messge;
import raft.utils.networking.raft.sever.serverbox;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ncAdder implements Runnable{
    ConcurrentHashMap<Long,Integer> quramcount;
    BlockingQueue<Messge> nclog;
    serverbox sb;
    BlockingQueue<Messge>proposed;
    Gson gson=new Gson();

    @Override
    public void run() {


    }
    public void addlogL(){

    }


    public void addNclogF(Messge messge){
        datum d= gson.fromJson(messge.contents, datum.class);
        if(sb.logLnc.get()>=d.log){
            sb.logLnc.incrementAndGet();
        }


    }

    public  void respondF(Messge messge){
        if(sb.statuse== serverbox.servertype.Follower)
        sb.Send(messge);
        else if(sb.statuse== serverbox.servertype.Leader) {
            sb.threads.get("nclRs").interrupt();
        }
    }


    public Messge messgemaker(){
        return null;
    }
}
