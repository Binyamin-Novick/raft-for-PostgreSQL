package raft.sever.sever;

import raft.sever.sever.messgeing.Messge;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.sleep;

/**
 * this will tell me when i need to make a new election
 * it will also send out the heart beats at a regulaer interval
 */
public class hartbox {
    serverbox sb;
    long timebetween;
    public AtomicLong timelastin;


    BlockingQueue<Messge> beats;

    public  hartbox(serverbox sb,BlockingQueue<Messge>beatsr){
        Random random=new Random();
        timebetween=100+ random.nextLong();

    }





    /**
     * this is eponsible to sees if it needs to become a candidate
     */
   class Checking implements Runnable{
        long between;

       @Override
       public void run() {
           while (sb.statuse!= serverbox.servertype.Leader){
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

    /**
     * this job is to updata the bet tracker so it know it got the beat;
     */
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

    /**
     * this is the leader it will send out a bet to all the folwers in time.
     */

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
