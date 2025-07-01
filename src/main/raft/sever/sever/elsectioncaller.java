package raft.sever.sever;

import raft.sever.sever.messgeing.Messge;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class elsectioncaller {
    serverbox sb;
    BlockingQueue<Messge> candidates;
    BlockingQueue<Messge>CRs;
    HashMap<Integer,Boolean>CRCont;



    public void sendeletionResponces() throws InterruptedException {
        Messge m;
        while(true){
             m = candidates.take();
             if(m.term>=sb.term.get()){
                 if(sb.logL.get()>=m.log){
                     sb.craftMes(Messge.type.CR,"no",m.Sid);
                 }else {
                     sb.statuse= serverbox.servertype.Follower;
                     sb.term.incrementAndGet();
                     sb.craftMes(Messge.type.CR,"yes",m.Sid);
                     sb.hb.timelastin.set(System.currentTimeMillis());
                 }
             }else {
                 sb.craftMes(Messge.type.CR,"no",m.Sid);
             }
        }
    }


    public void sendcandidcy() throws InterruptedException {
        sb.Broadcast(Messge.type.candidate,"");
        Messge m;
        long yesV=0;
        long noV=0;

        while (sb.statuse== serverbox.servertype.looking){
            m=CRs.take();
            if(m.term== sb.term.get()){
                if(m.contents.equals("no")){
                    noV++;
                    if(noV>sb.IDtoSocket.size())/// will need to be fixed
                    return;
                }else {
                    yesV++;
                    if(yesV>sb.IDtoSocket.size()/2){// will need to be fixe so that it is posible to have new mebers
                        sb.statuse= serverbox.servertype.Leader;
                        sb.term.incrementAndGet();
                        sb.StartLeding();
                    }
                }
            }
        }

    }




}
