package org.meds;

import org.meds.database.DBStorage;
import org.meds.database.entity.*;
import org.meds.enums.AchievementCategories;
import org.meds.enums.AchievementCriterionTypes;
import org.meds.enums.Currencies;
import org.meds.logging.Logging;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;

import java.util.*;

public class AchievementManager {

    private Player player;

    private java.util.Map<AchievementCategories, HashSet<Achievement>> achievements;

    public AchievementManager(Player player) {
        this.player = player;
        this.achievements = new HashMap<AchievementCategories, HashSet<Achievement>>(AchievementCategories.values().length);

        // Create a collection with non-completed achievements
        for (Achievement achievement : DBStorage.AchievementStore.values()) {
            CharacterAchievement charAchieve = this.player.getAchievement(achievement.getId());
            if (charAchieve != null && charAchieve.isCompleted()) {
                continue;
            }
            HashSet<Achievement> categoryAchievements = this.achievements.get(achievement.getCategory());
            if (categoryAchievements == null) {
                categoryAchievements = new HashSet<Achievement>(DBStorage.AchievementStore.size());
                this.achievements.put(achievement.getCategory(), categoryAchievements);
            }
            categoryAchievements.add(achievement);
        }

        this.player.addKillingBlowListener(new Unit.KillingBlowListener() {
            @Override
            public void handleEvent(Unit.DamageEvent e) {

                if (e.getVictim().isPlayer())
                    updateProgress(AchievementCategories.PvP, e.getVictim());
                else
                    updateProgress(AchievementCategories.PvM, e.getVictim());
            }
        });
    }

    private void updateProgress(AchievementCategories category, Unit target) {
        if (this.achievements.get(category).size() == 0)
            return;

        // Check all criteria for all achievements
        Set<AchievementCriterionTypes> completed = new HashSet<AchievementCriterionTypes>(AchievementCriterionTypes.values().length);
        Set<AchievementCriterionTypes> failed = new HashSet<AchievementCriterionTypes>(AchievementCriterionTypes.values().length);

        Iterator<Achievement> iterator = this.achievements.get(category).iterator();
        while (iterator.hasNext()) {
            Achievement achievement = iterator.next();
            Set<AchievementCriterion> criteria = achievement.getCriteria();
            completed.clear();
            failed.clear();
            for (AchievementCriterion criterion : criteria) {
                // Do not need check
                // If an alternative criterion with the same type
                // is already passed checking successfully
                if (completed.contains(criterion.getType()))
                    continue;

                boolean isComplete = false;
                switch (criterion.getType()) {
                    case CreatureTemplate:
                        if (target.getUnitType() != UnitTypes.Creature)
                            break;
                        if (((Creature)target).getTemplateId() == criterion.getRequirement()) {
                            isComplete = true;
                        }
                        break;
                    case Kingdom:
                        if (this.player.getPosition().getRegion().getKingdomId() == criterion.getRequirement()) {
                            isComplete = true;
                        }
                        break;
                    case Region:
                        if (this.player.getPosition().getRegionId() == criterion.getRequirement()) {
                            isComplete = true;
                        }
                        break;
                    case SpecialCreature:
                        // TODO: Implemement special creatures
                        break;
                    case TargetReligion:
                        // TODO: Implemenet player and creature religion
                        break;
                    case TargetReligiousStatus:
                        // TODO: Implemenet players religion and statuses
                        break;
                    case TargetRace:
                        if (target.getRace().getValue() == criterion.getRequirement()) {
                            isComplete = true;
                        }
                        break;
                    case UnderSiege:
                        // TODO: Implement location siege and capturing
                        break;
                    case ClanWar:
                        // TODO: Implement Clan mechanics and its PvP
                        break;
                    case AchievementComplete:
                        // TODO: How to pass achievement ID here???
                        break;
                }

                if (isComplete) {
                    completed.add(criterion.getType());
                    failed.remove(criterion.getType());
                } else {
                    failed.add(criterion.getType());
                }
            }

            // At least one failed criterion
            // The achievement does not meet requirements
            if (failed.size() != 0)
                continue;

            // Update progress counter
            CharacterAchievement charAchieve = this.player.getAchievement(achievement.getId());
            if (charAchieve == null) {
                charAchieve = new CharacterAchievement();
                charAchieve.setCharacterId(this.player.getGuid());
                charAchieve.setAchievementId(achievement.getId());
                this.player.addAchievement(charAchieve);
            }
            charAchieve.setProgress(charAchieve.getProgress() + 1);

            // Complete Achievement
            if (achievement.getCount() == charAchieve.getProgress()) {
                charAchieve.setCompleteDate((int)(new Date().getTime() / 1000));
                // Add achievement points
                this.player.changeCurrency(Currencies.Achievement, achievement.getPoints());
                sendAchievementComplete(achievement, charAchieve);
                iterator.remove();
                Logging.Info.log(String.format("Player %s (%d) complete achievement %d (%s)",
                        this.player.getName(), this.player.getGuid(),
                        achievement.getId(), achievement.getTitle()));
            }
            // Update Achievement
            else {
                sendAchievementUpdate(charAchieve);
            }
        }
    }

    private void sendAchievementUpdate(CharacterAchievement charAchieve) {
        if (this.player.getSession() == null)
            return;
        ServerPacket packet = new ServerPacket(ServerOpcodes.AchievementUpdate);
        packet.add(charAchieve.getAchievementId());
        packet.add(charAchieve.getProgress());
        this.player.getSession().addData(packet);
    }

    private void sendAchievementComplete(Achievement achievement, CharacterAchievement charAchieve) {
        if (this.player.getSession() == null)
            return;
        ServerPacket packet = new ServerPacket(ServerOpcodes.AchievementList);
        packet.add(1); // Achievement Complete list

        packet.add(achievement.getId());
        packet.add(achievement.getTitle());
        packet.add(achievement.getDescription());
        packet.add(charAchieve.getProgress());
        packet.add(achievement.getCount());
        packet.add(charAchieve.getCompleteDate());
        packet.add(achievement.getCategory());
        packet.add(achievement.getPoints());

        this.player.getSession().addData(packet);
    }
}
