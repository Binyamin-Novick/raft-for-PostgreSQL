package raft.sever.sever.logs;

import com.google.gson.Gson;
import raft.sever.sever.messgeing.Messge;
import raft.sever.sever.serverbox;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ncAdder implements Runnable{
    ConcurrentHashMap<Long,Integer> quramcount;
    ConcurrentHashMap<Long,String> nclog;
    serverbox sb;
    BlockingQueue<Messge>proposed;
    Gson gson=new Gson();

    @Override
    public void run() {
        while (true){
            if(sb.statuse== serverbox.servertype.Leader)
            addlogL();
            else {
                try {
                    addNclogF(proposed.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
    public void addlogL(){
        Messge m =messgemaker();
        quramcount.put(m.log, 0);
        nclog.put(m.log,m.contents);
        respondF(m);


    }


    public void addNclogF(Messge messge){
        datum d= gson.fromJson(messge.contents, datum.class);
        if(sb.logLnc.get()>=d.log){
            if(sb.logL.get()<d.log){
                if(sb.logLnc.get()==d.log)
                    sb.logLnc.incrementAndGet();


                nclog.put(d.log, messge.contents);
            }
            respondF(messge);

        }else {
            catchup();
        }


    }

    public void catchup(){}

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
