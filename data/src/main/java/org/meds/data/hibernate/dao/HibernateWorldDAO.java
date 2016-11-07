package org.meds.data.hibernate.dao;

import org.meds.data.dao.WorldDAO;
import org.meds.data.domain.*;

import java.util.List;

@SuppressWarnings("unchecked")
public class HibernateWorldDAO extends HibernateDAO implements WorldDAO {

    @Override
    public List<LocaleString> getLocaleStrings() {
        return openSession().createCriteria(LocaleString.class).list();
    }

    @Override
    public List<NewMessage> getNewMessages() {
        return openSession().createCriteria(NewMessage.class).list();
    }

    @Override
    public List<Achievement> getAchievements() {
        return openSession().createCriteria(Achievement.class).list();
    }

    @Override
    public List<AchievementCriterion> getAchievementCriteria() {
        return openSession().createCriteria(AchievementCriterion.class).list();
    }

    @Override
    public List<CreatureTemplate> getCreatureTemplates() {
        return openSession().createCriteria(CreatureTemplate.class).list();
    }

    @Override
    public List<CreatureLoot> getCreatureLoot() {
        return openSession().createCriteria(CreatureLoot.class).list();
    }

    @Override
    public List<Creature> getCreatures() {
        return openSession().createCriteria(Creature.class).list();
    }

    @Override
    public List<Currency> getCurrencies() {
        return openSession().createCriteria(Currency.class).list();
    }

    @Override
    public List<Guild> getGuilds() {
        return openSession().createCriteria(Guild.class).list();
    }

    @Override
    public List<GuildLesson> getGuildLessons() {
        return openSession().createCriteria(GuildLesson.class).list();
    }

    @Override
    public List<ItemTemplate> getItemTemplates() {
        return openSession().createCriteria(ItemTemplate.class).list();
    }

    @Override
    public List<LevelCost> getLevelCosts() {
        return openSession().createCriteria(LevelCost.class).list();
    }

    @Override
    public List<QuestTemplate> getQuestTemplates() {
        return openSession().createCriteria(QuestTemplate.class).list();
    }

    @Override
    public List<CreatureQuestRelation> getCreatureQuestRelations() {
        return openSession().createCriteria(CreatureQuestRelation.class).list();
    }

    @Override
    public List<Skill> getSkills() {
        return openSession().createCriteria(Skill.class).list();
    }

    @Override
    public List<Spell> getSpells() {
        return openSession().createCriteria(Spell.class).list();
    }
}
