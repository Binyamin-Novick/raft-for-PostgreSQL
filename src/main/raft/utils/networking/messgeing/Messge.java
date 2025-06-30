package raft.utils.networking.messgeing;

import java.net.InetSocketAddress;

public class Messge {
    public enum type{leader,propuse,comite,candidate,beat,CR,Catchup,CatchupResponces}
    public String contents;
    public int term;
    public long log;
    public InetSocketAddress reciver;
    public int Rid;
    public int Sid;
    public InetSocketAddress sender;


}
