package raft.utils.networking.raft.sever;

import raft.utils.networking.messgeing.Messge;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.sleep;

public class hartbox {
    serverbox sb;
    long timebetween;
    public AtomicLong timelastin;


    BlockingQueue<Messge> beats;
    BlockingQueue<Messge>leders;
    public  hartbox(serverbox sb,BlockingQueue<Messge>beatsr){
        Random random=new Random();
        timebetween=100+ random.nextLong(400);

    }


    public void election() {

    }



   class Checking implements Runnable{
        long between;

       @Override
       public void run() {
           while (sb.statuse== serverbox.servertype.Follower){
               between=System.currentTimeMillis()-timelastin.get();
               if(between<timebetween){
                   try {
                       sleep(timebetween-between);
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
               }else {
                   //make canditat start Cand sequence
               }
           }
       }
   }
   class updating implements Runnable{
    Messge m;
       @Override
       public void run() {
           while (sb.statuse== serverbox.servertype.Follower){
               try {
                   m= beats.poll(1000, TimeUnit.MILLISECONDS);
                   if(m.term==sb.term.get()){
                       timelastin.set(System.currentTimeMillis());
                   }
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
       }
   }

   class beatSender implements Runnable{

       @Override
       public void run() {
           while (sb.statuse== serverbox.servertype.Leader){
               try {
                   wait(300);
                   sb.Broadcast(Messge.type.beat,"");
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
       }
   }






}
