package raft.utils.networking.raft.sever;

import raft.utils.networking.messgeing.Messge;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class serverbox {
     public enum servertype {Leader,Follower,looking}
    public volatile servertype statuse =servertype.looking;
     public int id;
     InetSocketAddress basic;
     HashMap<Integer,InetSocketAddress> IDtoSocket;
     HashMap<InetSocketAddress,Integer>SocketToID;
     boolean on=true;
     public hartbox hb;
     public AtomicLong term;
     public AtomicLong logL;
     public int leader;
     public void Send(Messge messge){}
    public void Broadcast(Messge.type type, String contents){}
    public void craftMes(Messge.type type,String contnts,int who){}



}
