package org.thewolfbadger.badgerwebchat.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.java_websocket.WebSocket;
import org.thewolfbadger.badgerwebchat.api.ServerUnit;

import java.io.*;

/**
 * Created by user on 6/21/2018.
 */
public class BadgerWebChat extends JavaPlugin implements Listener {
    private ServerUnit servUnit;
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.servUnit = new ServerUnit();
        this.servUnit.start();
    }

    @Override
    public void onDisable() {
        try {
            servUnit.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent evt) {
        if(!evt.isCancelled()) {
            String message = evt.getMessage();
            String playerName = evt.getPlayer().getName();
            boolean isOP = false;
            String msg = playerName;
            if(evt.getPlayer().isOp()) {
                isOP = true;
                msg += " [OP]: ";
            }
            msg += message;
            for(WebSocket users : this.servUnit.getConnectedUsers()) {
                users.send(msg);
            }
            //sendMessageThruHTTP(player, message, isOP);
            //
        }
    }
}