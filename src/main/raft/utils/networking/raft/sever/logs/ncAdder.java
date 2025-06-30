package raft.utils.networking.raft.sever.logs;

import raft.utils.networking.messgeing.Messge;
import raft.utils.networking.raft.sever.serverbox;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ncAdder implements Runnable{
    ConcurrentHashMap<Integer,Integer> quramcount;
    BlockingQueue<Messge> nclogx;
    serverbox sb;
    @Override
    public void run() {
        
    }
}
