package org.meds.net;

import org.meds.Player;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Implementation of {@link SessionContext} based on client-per-thread design for
 * processing of client requests and messages
 * @author Romman
 */
@Component
public class ThreadSessionContext implements SessionContext {

    private ThreadLocal<Session> sessionThreadLocal;
    private ThreadLocal<Player> playerThreadLocal;

    @PostConstruct
    private void init() {
        this.sessionThreadLocal = new ThreadLocal<>();
        this.playerThreadLocal = new ThreadLocal<>();
    }

    public void setSession(Session session) {
        this.sessionThreadLocal.set(session);
    }

    @Override
    public Session getSession() {
        return this.sessionThreadLocal.get();
    }

    public void setPlayer(Player player) {
        this.playerThreadLocal.set(player);
    }

    @Override
    public Player getPlayer() {
        return this.playerThreadLocal.get();
    }
}
