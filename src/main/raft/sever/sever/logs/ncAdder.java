package raft.sever.sever.logs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import raft.sever.sever.messgeing.Messge;
import raft.sever.sever.serverbox;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ncAdder implements Runnable{
    ConcurrentHashMap<Long,Integer> quramcount;
    ConcurrentHashMap<Long,datum> nclog;
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
        nclog.put(m.log,gson.fromJson(m.contents, datum.class));
        respondF(m);


    }


    public void addNclogF(Messge messge){
        Type listType = new TypeToken<ArrayList<datum>>(){}.getType();
        List<datum> dl= (List<datum>) gson.fromJson(messge.contents, listType);
        List<datum>dv =dl.parallelStream().sorted(new dcom()).collect(Collectors.toList());
        List<datum>dvout=new ArrayList<>();
        boolean catchup=false;
        for (datum d :dv)
        if(sb.logLnc.get()>=d.log){
            if(sb.logLnc.get()==d.log)
                sb.logLnc.incrementAndGet();
             nclog.put(d.log, d);
            dvout.add(d);


        }else {

           catchup =true;
           break;
        }
        sb.craftMes(Messge.type.propuse, gson.toJson(dvout), sb.leader );
        if(catchup)
            catchup();


    }

    public void catchup(){}

    public  void respondF(Messge messge){/// worng
        if(sb.statuse== serverbox.servertype.Follower)

            //this needs to  send a comit responces not a propuse back
            sb.Send(messge);
        else if(sb.statuse== serverbox.servertype.Leader) {
            sb.threads.get("nclRs").interrupt();
        }
    }


    public Messge messgemaker(){
        return null;
    }


    class dcom implements Comparator<datum>{


        @Override
        public int compare(datum o1, datum o2) {
            if(o1.log>o2.log)
                return 1;
            if(o2.log>o1.log)
                return -1;
            return 0;
        }
    }
}
