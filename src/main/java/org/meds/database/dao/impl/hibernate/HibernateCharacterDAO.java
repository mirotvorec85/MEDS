package org.meds.database.dao.impl.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.meds.database.dao.CharacterDAO;
import org.meds.database.entity.CharacterInfo;
import org.meds.database.entity.Character;

public class HibernateCharacterDAO extends HibernateDAO implements CharacterDAO {

    @Override
    public Character findCharacter(String login) {
        return (Character) openSession().createCriteria(Character.class).add(Restrictions.eq("login", login)).uniqueResult();
    }

    @Override
    public CharacterInfo getCharacterInfo(int id) {
        return (CharacterInfo) openSession().load(CharacterInfo.class, id);
    }

    @Override
    public void save(Character character) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(character);
        tx.commit();
        session.close();
    }

    @Override
    public void save(CharacterInfo info) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(info);
        tx.commit();
        session.close();
    }
}
