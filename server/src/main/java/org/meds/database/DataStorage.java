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

public class DataStorage {

    public static Repository<Achievement> AchievementRepository;
    public static BiRepository<CreatureLoot> CreatureLootRepository;
    public static BiRepository<CreatureQuestRelation> CreatureQuestRelationRepository;
    public static Repository<CreatureTemplate> CreatureTemplateRepository;
    public static Repository<Currency> CurrencyRepository;
    public static BiRepository<GuildLesson> GuildLessonRepository;
    public static Repository<Guild> GuildRepository;
    public static Repository<ItemTemplate> ItemTemplateRepository;
    public static Repository<LevelCost> LevelCostRepository;
    public static Repository<NewMessage> NewMessageRepository;
    public static Repository<QuestTemplate> QuestTemplateRepository;
    public static Repository<Skill> SkillRepository;
    public static Repository<Spell> SpellRepository;

    private static Map<Integer, ServerPacket> guildLessonsInfo;

    public static void load() {
        WorldDAO worldDAO = DAOFactory.getFactory().getWorldDAO();

        // Achievement
        List<Achievement> achievements = worldDAO.getAchievements();
        AchievementRepository = new MapRepository<>(achievements, Achievement::getId);
        Logging.Info.log("Loaded " + DataStorage.AchievementRepository.size() + " achievements");

        // Achievement Criteria
        List<AchievementCriterion> criteria = worldDAO.getAchievementCriteria();
        for (AchievementCriterion criterion : criteria) {
            DataStorage.AchievementRepository.get(criterion.getAchievementId()).getCriteria().add(criterion);
        }
        Logging.Info.log("Loaded " + criteria.size() + " achievement criteria");

        // Currency
        List<Currency> currencies = worldDAO.getCurrencies();
        CurrencyRepository = new MapRepository<>(currencies, Currency::getId);
        Logging.Info.log("Loaded " + DataStorage.CurrencyRepository.size() + " currencies");

        // Guild
        List<Guild> guilds = worldDAO.getGuilds();
        GuildRepository = new MapRepository<>(guilds, Guild::getId);
        Logging.Info.log("Loaded " + DataStorage.GuildRepository.size() + " guilds");

        // GuildLesson
        List<GuildLesson> guildLessons = worldDAO.getGuildLessons();
        GuildLessonRepository = new MapBiRepository<>(guildLessons, GuildLesson::getGuildId, GuildLesson::getLevel);
        Logging.Info.log("Loaded %d guild lessons (of %d guilds)",
                GuildLessonRepository.size(), GuildLessonRepository.sizeFirst());

        // Cache GuildLessonInfo packets
        DataStorage.guildLessonsInfo = new HashMap<>(DataStorage.GuildLessonRepository.size());

        // ItemTemplate
        List<ItemTemplate> items = worldDAO.getItemTemplates();
        ItemTemplateRepository = new MapRepository<>(items, ItemTemplate::getId);
        Logging.Info.log("Loaded " + DataStorage.ItemTemplateRepository.size() + " items");

        // Level Costs
        List<LevelCost> levelCosts = worldDAO.getLevelCosts();
        LevelCostRepository = new MapRepository<>(levelCosts, LevelCost::getLevel);
        Logging.Info.log("Loaded " + DataStorage.LevelCostRepository.size() + " level costs");

        // Guild Cults
        // --is obsolete

        // Skills
        List<Skill> skills = worldDAO.getSkills();
        DataStorage.SkillRepository = new MapRepository<>(skills, Skill::getId);
        Logging.Info.log("Loaded " + DataStorage.SkillRepository.size() + " skills");

        // Spells
        List<Spell> spells = worldDAO.getSpells();
        DataStorage.SpellRepository = new MapRepository<>(spells, Spell::getId);
        Logging.Info.log("Loaded " + DataStorage.SpellRepository.size() + " spells");

        // Creature Templates
        List<CreatureTemplate> creatureTemplates = worldDAO.getCreatureTemplates();
        CreatureTemplateRepository = new MapRepository<>(creatureTemplates, CreatureTemplate::getTemplateId);
        Logging.Info.log("Loaded " + DataStorage.CreatureTemplateRepository.size() + " creature templates");

        // Creature Loot
        List<CreatureLoot> creatureLootItems = worldDAO.getCreatureLoot();
        CreatureLootRepository = new MapBiRepository<>(creatureLootItems,
                CreatureLoot::getCreatureTemplateId, CreatureLoot::getItemTemplateId);
        Logging.Info.log("Loaded %d creature loot (of %d creature templates)", CreatureLootRepository.size(), CreatureLootRepository.sizeFirst());

        // New Messages
        List<NewMessage> messages = worldDAO.getNewMessages();
        NewMessageRepository = new MapRepository<>(messages, NewMessage::getId);
        Logging.Info.log("Loaded " + DataStorage.NewMessageRepository.size() + " new messages");

        // Quest Templates
        List<QuestTemplate> questTemplates = worldDAO.getQuestTemplates();
        QuestTemplateRepository = new MapRepository<>(questTemplates, QuestTemplate::getId);
        Logging.Info.log("Loaded " + DataStorage.QuestTemplateRepository.size() + " quests");

        // Creature Quest Relations
        List<CreatureQuestRelation> relations = worldDAO.getCreatureQuestRelations();
        CreatureQuestRelationRepository = new MapBiRepository<>(relations,
                CreatureQuestRelation::getCreatureTemplateId, CreatureQuestRelation::getQuestTemplateId);
        Logging.Info.log("Loaded %d creature quest relations (of %d creature templates)",
                CreatureQuestRelationRepository.size(), CreatureQuestRelationRepository.sizeFirst());
    }

    // TODO: maybe put this method into a more appropriate class
    public static ServerPacket getGuildLessonInfo(int guildId) {
        ServerPacket lessonsData = DataStorage.guildLessonsInfo.get(guildId);
        if (lessonsData == null) {
            Guild guild = DataStorage.GuildRepository.get(guildId);
            if (guild == null) {
                return null;
            }
            lessonsData = new ServerPacket(ServerCommands.GuildLessonsInfo)
                    .add(guildId)
                    .add(guild.getName());
            List<GuildLesson> guildLessons = new ArrayList<>(DataStorage.GuildLessonRepository.get(guildId));
            guildLessons.sort((o1, o2) -> o1.getLevel() - o2.getLevel());
            for (GuildLesson guildLesson : guildLessons) {
                lessonsData.add(guildLesson.getDescription());
            }

            DataStorage.guildLessonsInfo.put(guildId, lessonsData);
        }

        return lessonsData;
    }
}
