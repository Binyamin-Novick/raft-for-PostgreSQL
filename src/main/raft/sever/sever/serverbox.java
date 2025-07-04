package raft.sever.sever;

import raft.sever.sever.logs.datum;
import raft.sever.sever.messgeing.Messge;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class serverbox {
     public enum servertype {Leader,Follower,looking}
    public volatile servertype statuse =servertype.looking;
     public int id;
     InetSocketAddress basic;
     HashMap<Integer,InetSocketAddress> IDtoSocket;
     HashMap<InetSocketAddress,Integer>SocketToID;
     public ConcurrentHashMap<String,Thread>threads;
     boolean on=true;
     public hartbox hb;
     public AtomicLong term;
     public AtomicLong logL;
    public AtomicLong logLnc;
     public int leader;
     public void comiteandaply(List<datum>datatc){}
     public void Send(Messge messge){}
    public void Broadcast(Messge.type type, String contents){}
    public void craftMes(Messge.type type,String contnts,int who){}

    public void StartLeding(){}



}
