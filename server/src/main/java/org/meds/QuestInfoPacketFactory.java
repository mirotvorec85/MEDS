package org.meds;

import org.meds.data.domain.CreatureTemplate;
import org.meds.data.domain.ItemTemplate;
import org.meds.data.domain.QuestTemplate;
import org.meds.database.Repository;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Romman
 */
@Component
public class QuestInfoPacketFactory {

    @Autowired
    private Repository<CreatureTemplate> creatureTemplateRepository;
    @Autowired
    private Repository<ItemTemplate> itemTemplateRepository;
    @Autowired
    private Locale locale;

    public ServerPacket create(QuestTemplate template) {
        return create(template, false);
    }

    public ServerPacket create(QuestTemplate template, boolean isForAccept) {
        ServerPacket packet = new ServerPacket(isForAccept ? ServerCommands.QuestInfoForAccept : ServerCommands.QuestInfo);

        packet.add(template.getId())
                .add(template.getTitle())
                .add(template.getType())
                .add(template.getDescription())
                .add(""); // Always empty

        switch (template.getType()) {
            case Kill:
                CreatureTemplate creatureTemplate = creatureTemplateRepository.get(template.getRequiredCreatureId());
                if (creatureTemplate == null) {
                    packet.add("").add("").add("");
                } else {
                    packet.add(template.getRequiredCount())
                            .add(creatureTemplate.getName())
                            .add("");
                }
                break;
            default:
                packet.add("").add("").add("");
                break;
        }
        int rewardCount = 0;

        if (template.getRewardExp() != 0) {
            packet.add(template.getRewardExp() + locale.getString(1));
            ++rewardCount;
        }
        if (template.getRewardGold() != 0) {
            packet.add(template.getRewardGold() + locale.getString(2));
            ++rewardCount;
        }

        ItemTemplate itemTemplate;
        if (template.getRewardItem1Count() != 0 && template.getRewardItem1Id() != 0) {
            itemTemplate = itemTemplateRepository.get(template.getRewardItem1Id());
            if (itemTemplate != null) {
                packet.add(template.getRewardItem1Count() + " x " + itemTemplate.getTitle());
                ++rewardCount;
            }
        }
        if (template.getRewardItem2Count() != 0 && template.getRewardItem2Id() != 0) {
            itemTemplate = itemTemplateRepository.get(template.getRewardItem2Id());
            if (itemTemplate != null) {
                packet.add(template.getRewardItem2Count() + " x " + itemTemplate.getTitle());
                ++rewardCount;
            }
        }

        while (rewardCount != 3) {
            packet.add("");
            ++rewardCount;
        }

        packet.add(0); // ??? Tutorial?

        return packet;
    }
}
