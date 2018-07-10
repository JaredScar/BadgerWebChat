package org.thewolfbadger.badgerwebchat.api;

import org.bukkit.Bukkit;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 7/10/2018.
 */
public class ServerUnit extends WebSocketServer {
    private static int TCP_PORT = 31313;

    private Set<WebSocket> conns;

    public ServerUnit() {
        super(new InetSocketAddress(TCP_PORT));
        conns = new HashSet<WebSocket>();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("<Socket-Server> New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conns.remove(conn);
        System.out.println("<Socket-Server> Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("<Socket-Server> " + conn.getRemoteSocketAddress().getAddress().toString() + ">" + message);
        Bukkit.getServer().broadcastMessage("<Socket-Server> " + conn.getRemoteSocketAddress().getAddress().toString() + ">" + message);
        for (WebSocket sock : conns) {
            sock.send(message);
        }
    }
    public Set<WebSocket> getConnectedUsers() {
        return this.conns;
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        //ex.printStackTrace();
        if (conn != null) {
            conns.remove(conn);
            System.out.println("<Socket-Server> ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
            // do some thing if required
        }
        System.out.print("<Socket-Server> ERROR: ");
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("<Socket-Server> Server started successfully...");
    }
}
