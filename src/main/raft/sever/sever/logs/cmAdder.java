package raft.sever.sever.logs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import raft.sever.sever.messgeing.Messge;
import raft.sever.sever.serverbox;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * this thread peupase is to take the stuf from the non comit log and put it in the comit log when aproret
 */
public class cmAdder implements Runnable{
    ConcurrentHashMap<Long,Integer> quramcount;
    ConcurrentHashMap<Long,datum> nclog;
    ConcurrentHashMap<Long,datum>clog;

    serverbox sb;
    BlockingQueue<Messge>propusemesges;
    BlockingQueue<Messge>comitmessges;
    Gson gson =new Gson();
    Type listType = new TypeToken<ArrayList<datum>>(){}.getType();

    @Override
    public void run() {
        while(true){
            try {
                leader();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                folower();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void folower() throws InterruptedException {
        Messge m;
        List<datum>tc;
        while (sb.statuse!= serverbox.servertype.Leader){
            m=comitmessges.take();
            tc=gson.fromJson(m.contents,listType);
            tc.parallelStream().peek((x)-> {
                clog.put(x.log, x);
                nclog.remove(x.log);

            }).collect(Collectors.toList());

        }

    }


    public void leader() throws InterruptedException {
        Messge m;
        List<datum>dl;



        int i=0;
        while(sb.statuse== serverbox.servertype.Leader){
             m =propusemesges.take();
             dl = gson.fromJson(m.contents,listType);
             for(datum d:dl){
                    quramcount.put(d.log, quramcount.get(d.log)+1);
                    if(d.log==sb.logL.get()+i){//this may need to be bug checked to make sure im consitnt in up tp vs last
                        i++;
                    }
             }
             long out =sb.logL.addAndGet(i);
            // becase there are some sqlite spisfect thing involved hear it is need to have that handled else were this
            // will also handle sending the send out of the comites
            sb.comiteandaply(dl.parallelStream().filter(x->x.log> out).collect(Collectors.toList()));
        }
    }
}
