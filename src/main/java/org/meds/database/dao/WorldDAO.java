package org.meds.database.dao;

import org.meds.Creature;
import org.meds.database.entity.*;

import java.util.List;

public interface WorldDAO {

    List<LocaleString> getLocaleStrings();

    List<NewMessage> getNewMessages();

    List<Achievement> getAchievements();

    List<AchievementCriterion> getAchievementCriteria();

    List<CreatureTemplate> getCreatureTemplates();

    List<CreatureLoot> getCreatureLoot();

    List<Creature> getCreatures();

    List<Currency> getCurrencies();

    List<Guild> getGuilds();

    List<GuildLesson> getGuildLessons();

    List<ItemTemplate> getItemTemplates();

    List<LevelCost> getLevelCosts();

    List<QuestTemplate> getQuestTemplates();

    List<CreatureQuestRelation> getCreatureQuestRelations();

    List<Skill> getSkills();

    List<Spell> getSpells();
}
