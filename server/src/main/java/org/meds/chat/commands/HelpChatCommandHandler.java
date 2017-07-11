package org.meds.chat.commands;

import org.meds.Player;
import org.meds.chat.ChatHandler;
import org.meds.net.ServerPacket;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Romman
 */
@ChatCommand("?")
public class HelpChatCommandHandler extends AbstractChatCommandHandler {

    @Autowired
    private ChatHandler chatHandler;
    
    private ServerPacket helpChatCommandResult;

    @PostConstruct
    private void init() {
        this.helpChatCommandResult = new ServerPacket()
                .add(chatHandler.constructSystemMessage("=============== GENERAL ==============="))
                .add(chatHandler.constructSystemMessage("\\info"))
                .add(chatHandler.constructSystemMessage("\\who"))
                .add(chatHandler.constructSystemMessage("\\relax"))
                .add(chatHandler.constructSystemMessage("\\notell"))
                .add(chatHandler.constructSystemMessage("\\locborn"))
                .add(chatHandler.constructSystemMessage("\\observe"))
                .add(chatHandler.constructSystemMessage("\\scan"))
                .add(chatHandler.constructSystemMessage("\\nomelee"))
                .add(chatHandler.constructSystemMessage("\\balance"))
                .add(chatHandler.constructSystemMessage("\\mail"))
                .add(chatHandler.constructSystemMessage("\\powerup"))
                .add(chatHandler.constructSystemMessage("\\replay"))
                .add(chatHandler.constructSystemMessage("\\skills"))
                .add(chatHandler.constructSystemMessage("\\noprotect"))
                .add(chatHandler.constructSystemMessage("\\total_filter"))
                // TODO: add tips for the next commands (cost, format, etc.)
                .add(chatHandler.constructSystemMessage("\\gra"))
                .add(chatHandler.constructSystemMessage("\\invisible"))
                .add(chatHandler.constructSystemMessage("\\doppel"))
                .add(chatHandler.constructSystemMessage("\\hide_eq"))
                .add(chatHandler.constructSystemMessage("\\compose"))
                .add(chatHandler.constructSystemMessage("\\wimpy"))
                .add(chatHandler.constructSystemMessage("\\sendpt"))
                .add(chatHandler.constructSystemMessage("\\sendgold"))
                .add(chatHandler.constructSystemMessage("\\roll"))
                .add(chatHandler.constructSystemMessage("\\return"))
                .add(chatHandler.constructSystemMessage("\\?"));
    }

    @Override
    public void handle(Player player, String[] args) {
        if (player.getSession() != null) {
            player.getSession().sendServerMessage(1128).send(this.helpChatCommandResult);
        }
    }
}
