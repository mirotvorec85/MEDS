package org.meds.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.meds.database.entity.*;
import org.meds.database.entity.Character;
import org.meds.logging.Logging;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class DBStorage {

    public static Map<Integer, Achievement> AchievementStore;
    public static Map<Integer, Map<Integer, CreatureLoot>> CreatureLootStore;
    public static Map<Integer, Map<Integer, CreatureQuestRelation>> CreatureQuestRelationStore;
    public static Map<Integer, CreatureTemplate> CreatureTemplateStore;
    public static Map<Integer, Currency> CurrencyStore;
    public static Map<Integer, Map<Integer, GuildLesson>> GuildLessonStore;
    public static Map<Integer, Guild> GuildStore;
    public static Map<Integer, ItemTemplate> ItemTemplateStore;
    public static Map<Integer, LevelCost> LevelCostStore;
    public static Map<Integer, NewMessage> NewMessageStore;
    public static Map<Integer, QuestTemplate> QuestTemplateStore;
    public static Map<Integer, Skill> SkillStore;
    public static Map<Integer, Spell> SpellStore;

    @SuppressWarnings("unchecked")
    public static void load()
    {
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

        Session session = Hibernate.getSessionFactory().openSession();

        // Achievement
        List<Achievement> achievements = session.createCriteria(Achievement.class).list();
        for(Achievement achievement : achievements)
        {
            DBStorage.AchievementStore.put(achievement.getId(), achievement);
        }
        Logging.Info.log("Loaded " + DBStorage.AchievementStore.size() + " achievements");

        // Achievement Criteria
        List<AchievementCriterion> criterias = session.createCriteria(AchievementCriterion.class).list();
        // Adding to Achievement criteria list implemented
        // inside AchievementCriteria.setAchievementId method
        Logging.Info.log("Loaded " + criterias.size() + " achievement criterias");

        // Currency
        List<Currency> currencies = session.createCriteria(Currency.class).list();
        for(Currency currency : currencies)
        {
            DBStorage.CurrencyStore.put(currency.getId(), currency);
        }
        Logging.Info.log("Loaded " + DBStorage.CurrencyStore.size() + " currencies");

        // Guild
        List<Guild> guilds = session.createCriteria(Guild.class).list();
        for(Guild guild : guilds)
        {
            DBStorage.GuildStore.put(guild.getId(), guild);
        }
        Logging.Info.log("Loaded " + DBStorage.GuildStore.size() + " guilds");

        // GuildLesson
        List<GuildLesson> guildLessons = session.createCriteria(GuildLesson.class).list();
        counter = 0;
        for(GuildLesson guildLesson : guildLessons)
        {
            Map<Integer, GuildLesson> lessons = DBStorage.GuildLessonStore.get(guildLesson.getGuildId());
            if (lessons == null)
            {
                lessons = new HashMap<>();
                DBStorage.GuildLessonStore.put(guildLesson.getGuildId(), lessons);
            }
            ++counter;
            lessons.put(guildLesson.getLevel(), guildLesson);
        }
        Logging.Info.log("Loaded " + counter + " guild lessons (of " + DBStorage.GuildLessonStore.size() + " guilds)");

        // ItemTemplate
        List<ItemTemplate> items = session.createCriteria(ItemTemplate.class).list();
        for(ItemTemplate item : items)
        {
            DBStorage.ItemTemplateStore.put(item.getId(), item);
        }
        Logging.Info.log("Loaded " + DBStorage.ItemTemplateStore.size() + " items");

        // Level Costs
        List<LevelCost> levelCosts = session.createCriteria(LevelCost.class).list();
        for(LevelCost cost : levelCosts)
        {
            DBStorage.LevelCostStore.put(cost.getLevel(), cost);
        }
        Logging.Info.log("Loaded " + DBStorage.LevelCostStore.size() + " level costs");

        // Guild Cults
        // --is obsolete

        // Skills
        List<Skill> skills = session.createCriteria(Skill.class).list();
        DBStorage.SkillStore = new HashMap<>(skills.size());
        for(Skill skill : skills)
        {
            DBStorage.SkillStore.put(skill.getId(), skill);
        }
        Logging.Info.log("Loaded " + DBStorage.SkillStore.size() + " skills");

        // Spells
        List<Spell> spells = session.createCriteria(Spell.class).list();
        DBStorage.SpellStore = new HashMap<>(spells.size());
        for(Spell spell : spells)
        {
            DBStorage.SpellStore.put(spell.getId(), spell);
        }
        Logging.Info.log("Loaded " + DBStorage.SpellStore.size() + " spells");

        // Creature Templates
        List<CreatureTemplate> creatureTemplates = session.createCriteria(CreatureTemplate.class).list();
        for(CreatureTemplate template : creatureTemplates)
        {
            DBStorage.CreatureTemplateStore.put(template.getTemplateId(), template);
        }
        Logging.Info.log("Loaded " + DBStorage.CreatureTemplateStore.size() + " creature templates");

        // Creature Loot
        List<CreatureLoot> creatureLootItems = session.createCriteria(CreatureLoot.class).list();
        counter = 0;
        for(CreatureLoot creatureLootItem : creatureLootItems)
        {
            Map<Integer, CreatureLoot> lootItems = DBStorage.CreatureLootStore.get(creatureLootItem.getCreatureTemplateId());
            if (lootItems == null)
            {
                lootItems = new HashMap<>();
                DBStorage.CreatureLootStore.put(creatureLootItem.getCreatureTemplateId(), lootItems);
            }
            ++counter;
            lootItems.put(creatureLootItem.getItemTemplateId(), creatureLootItem);
        }
        Logging.Info.log("Loaded " + counter + " creature loot (of " + DBStorage.CreatureLootStore.size() + " creature templates)");

        // New Messages
        List<NewMessage> messages = session.createCriteria(NewMessage.class).list();
        for(NewMessage message : messages)
        {
            DBStorage.NewMessageStore.put(message.getId(), message);
        }
        Logging.Info.log("Loaded " + DBStorage.NewMessageStore.size() + " new messages");

        // Quest Templates
        List<QuestTemplate> questTemplates = session.createCriteria(QuestTemplate.class).list();
        for(QuestTemplate template : questTemplates)
        {
            DBStorage.QuestTemplateStore.put(template.getId(), template);
        }
        Logging.Info.log("Loaded " + DBStorage.QuestTemplateStore.size() + " quests");

        // Creature Quest Relations
        List<CreatureQuestRelation> relations = session.createCriteria(CreatureQuestRelation.class).list();
        counter = 0;
        for(CreatureQuestRelation relation : relations)
        {
            Map<Integer, CreatureQuestRelation> creatureQuests = DBStorage.CreatureQuestRelationStore.get(relation.getCreatureTemplateId());
            if (creatureQuests == null)
            {
                creatureQuests = new HashMap<>();
                DBStorage.CreatureQuestRelationStore.put(relation.getCreatureTemplateId(), creatureQuests);
            }
            ++counter;
            creatureQuests.put(relation.getQuestTemplateId(), relation);
        }
        Logging.Info.log("Loaded " + counter + " creature quest relations (of " + DBStorage.CreatureQuestRelationStore.size() + " creature templates)");

        session.close();
    }

    public static Character findCharacter(String login)
    {
        return (Character)Hibernate.getSessionFactory().openSession().createCriteria(Character.class).add(Restrictions.eq("login", login)).uniqueResult();
    }

    public static CharacterInfo getCharacterInfo(org.hibernate.Session session, int guid)
    {
        return (CharacterInfo)session.load(CharacterInfo.class, guid);
    }

    public static CharacterInfo getCharacterInfo(int guid)
    {
        return DBStorage.getCharacterInfo(Hibernate.getSessionFactory().openSession(), guid);
    }
}
