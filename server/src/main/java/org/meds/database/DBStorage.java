package org.meds.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.meds.data.dao.DAOFactory;
import org.meds.data.dao.WorldDAO;
import org.meds.data.domain.*;
import org.meds.data.domain.LevelCost;
import org.meds.logging.Logging;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;

public class DBStorage {

    public static Map<Integer, Achievement> AchievementStore;
    public static Map<Integer, Map<Integer, CreatureLoot>> CreatureLootStore;
    public static Map<Integer, Map<Integer, CreatureQuestRelation>> CreatureQuestRelationStore;
    public static Map<Integer, CreatureTemplate> CreatureTemplateStore;
    public static Map<Integer, Currency> CurrencyStore;
    public static Map<Integer, List<GuildLesson>> GuildLessonStore;
    public static Map<Integer, Guild> GuildStore;
    public static Map<Integer, ItemTemplate> ItemTemplateStore;
    public static Map<Integer, LevelCost> LevelCostStore;
    public static Map<Integer, NewMessage> NewMessageStore;
    public static Map<Integer, QuestTemplate> QuestTemplateStore;
    public static Map<Integer, Skill> SkillStore;
    public static Map<Integer, Spell> SpellStore;

    private static Map<Integer, ServerPacket> guildLessonsInfo;

    public static void load() {
        int counter;
        DBStorage.AchievementStore = new HashMap<>();
        DBStorage.CreatureLootStore = new HashMap<>();
        DBStorage.CreatureQuestRelationStore = new HashMap<>();
        DBStorage.CreatureTemplateStore = new HashMap<>();
        DBStorage.CurrencyStore = new HashMap<>();
        DBStorage.GuildLessonStore = new HashMap<>();
        DBStorage.GuildStore = new HashMap<>();
        DBStorage.ItemTemplateStore = new HashMap<>();
        DBStorage.LevelCostStore = new HashMap<>();
        DBStorage.NewMessageStore = new HashMap<>();
        DBStorage.QuestTemplateStore = new HashMap<>();

        WorldDAO worldDAO = DAOFactory.getFactory().getWorldDAO();

        // Achievement
        List<Achievement> achievements = worldDAO.getAchievements();
        for(Achievement achievement : achievements) {
            DBStorage.AchievementStore.put(achievement.getId(), achievement);
        }
        Logging.Info.log("Loaded " + DBStorage.AchievementStore.size() + " achievements");

        // Achievement Criteria
        List<AchievementCriterion> criteria = worldDAO.getAchievementCriteria();
        for (AchievementCriterion criterion : criteria) {
            DBStorage.AchievementStore.get(criterion.getAchievementId()).getCriteria().add(criterion);
        }
        Logging.Info.log("Loaded " + criteria.size() + " achievement criteria");

        // Currency
        List<Currency> currencies = worldDAO.getCurrencies();
        for(Currency currency : currencies) {
            DBStorage.CurrencyStore.put(currency.getId(), currency);
        }
        Logging.Info.log("Loaded " + DBStorage.CurrencyStore.size() + " currencies");

        // Guild
        List<Guild> guilds = worldDAO.getGuilds();
        for(Guild guild : guilds) {
            DBStorage.GuildStore.put(guild.getId(), guild);
        }
        Logging.Info.log("Loaded " + DBStorage.GuildStore.size() + " guilds");

        // GuildLesson
        List<GuildLesson> guildLessons = worldDAO.getGuildLessons();
        counter = 0;
        for(GuildLesson guildLesson : guildLessons) {
           List<GuildLesson> lessons = DBStorage.GuildLessonStore.get(guildLesson.getGuildId());
            if (lessons == null) {
                lessons = new ArrayList<>(15);
                DBStorage.GuildLessonStore.put(guildLesson.getGuildId(), lessons);
            }
            ++counter;
            lessons.add(guildLesson);
        }
        Logging.Info.log("Loaded " + counter + " guild lessons (of " + DBStorage.GuildLessonStore.size() + " guilds)");

        // Cache GuildLessonInfo packets
        DBStorage.guildLessonsInfo = new HashMap<>(DBStorage.GuildLessonStore.size());

        // ItemTemplate
        List<ItemTemplate> items = worldDAO.getItemTemplates();
        for(ItemTemplate item : items) {
            DBStorage.ItemTemplateStore.put(item.getId(), item);
        }
        Logging.Info.log("Loaded " + DBStorage.ItemTemplateStore.size() + " items");

        // Level Costs
        List<LevelCost> levelCosts = worldDAO.getLevelCosts();
        for(LevelCost cost : levelCosts) {
            DBStorage.LevelCostStore.put(cost.getLevel(), cost);
        }
        Logging.Info.log("Loaded " + DBStorage.LevelCostStore.size() + " level costs");

        // Guild Cults
        // --is obsolete

        // Skills
        List<Skill> skills = worldDAO.getSkills();
        DBStorage.SkillStore = new HashMap<>(skills.size());
        for(Skill skill : skills) {
            DBStorage.SkillStore.put(skill.getId(), skill);
        }
        Logging.Info.log("Loaded " + DBStorage.SkillStore.size() + " skills");

        // Spells
        List<Spell> spells = worldDAO.getSpells();
        DBStorage.SpellStore = new HashMap<>(spells.size());
        for(Spell spell : spells) {
            DBStorage.SpellStore.put(spell.getId(), spell);
        }
        Logging.Info.log("Loaded " + DBStorage.SpellStore.size() + " spells");

        // Creature Templates
        List<CreatureTemplate> creatureTemplates = worldDAO.getCreatureTemplates();
        for(CreatureTemplate template : creatureTemplates) {
            DBStorage.CreatureTemplateStore.put(template.getTemplateId(), template);
        }
        Logging.Info.log("Loaded " + DBStorage.CreatureTemplateStore.size() + " creature templates");

        // Creature Loot
        List<CreatureLoot> creatureLootItems = worldDAO.getCreatureLoot();
        counter = 0;
        for(CreatureLoot creatureLootItem : creatureLootItems) {
            Map<Integer, CreatureLoot> lootItems = DBStorage.CreatureLootStore.get(creatureLootItem.getCreatureTemplateId());
            if (lootItems == null) {
                lootItems = new HashMap<>();
                DBStorage.CreatureLootStore.put(creatureLootItem.getCreatureTemplateId(), lootItems);
            }
            ++counter;
            lootItems.put(creatureLootItem.getItemTemplateId(), creatureLootItem);
        }
        Logging.Info.log("Loaded " + counter + " creature loot (of " + DBStorage.CreatureLootStore.size() + " creature templates)");

        // New Messages
        List<NewMessage> messages = worldDAO.getNewMessages();
        for(NewMessage message : messages) {
            DBStorage.NewMessageStore.put(message.getId(), message);
        }
        Logging.Info.log("Loaded " + DBStorage.NewMessageStore.size() + " new messages");

        // Quest Templates
        List<QuestTemplate> questTemplates = worldDAO.getQuestTemplates();
        for(QuestTemplate template : questTemplates) {
            DBStorage.QuestTemplateStore.put(template.getId(), template);
        }
        Logging.Info.log("Loaded " + DBStorage.QuestTemplateStore.size() + " quests");

        // Creature Quest Relations
        List<CreatureQuestRelation> relations = worldDAO.getCreatureQuestRelations();
        counter = 0;
        for(CreatureQuestRelation relation : relations) {
            Map<Integer, CreatureQuestRelation> creatureQuests = DBStorage.CreatureQuestRelationStore.get(relation.getCreatureTemplateId());
            if (creatureQuests == null) {
                creatureQuests = new HashMap<>();
                DBStorage.CreatureQuestRelationStore.put(relation.getCreatureTemplateId(), creatureQuests);
            }
            ++counter;
            creatureQuests.put(relation.getQuestTemplateId(), relation);
        }
        Logging.Info.log("Loaded " + counter + " creature quest relations (of " + DBStorage.CreatureQuestRelationStore.size() + " creature templates)");
    }

    // TODO: maybe put this method into a more appropriate class
    public static ServerPacket getGuildLessonInfo(int guildId) {
        ServerPacket lessonsData = DBStorage.guildLessonsInfo.get(guildId);
        if (lessonsData == null) {
            Guild guild = DBStorage.GuildStore.get(guildId);
            if (guild == null) {
                return null;
            }
            lessonsData = new ServerPacket(ServerCommands.GuildLessonsInfo)
                    .add(guildId)
                    .add(guild.getName());
            List<GuildLesson> guildLessons = DBStorage.GuildLessonStore.get(guildId);
            if (guildLessons != null) {
                for (int i = 1; i <= guildLessons.size(); ++i) {
                    lessonsData.add(guildLessons.get(i).getDescription());
                }
            }

            DBStorage.guildLessonsInfo.put(guildId, lessonsData);
        }

        return lessonsData;
    }
}
