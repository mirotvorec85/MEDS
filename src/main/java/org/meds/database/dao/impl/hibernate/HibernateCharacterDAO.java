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
        Session session = openSession();
        Character character = (Character) session.createCriteria(Character.class).add(Restrictions.eq("login", login)).uniqueResult();
        session.close();
        return character;
    }

    @Override
    public CharacterInfo getCharacterInfo(int id) {
        Session session = openSession();
        CharacterInfo characterInfo = (CharacterInfo) session.get(CharacterInfo.class, id);
        session.close();
        return characterInfo;
    }

    @Override
    public void insert(Character character) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.save(character);
        tx.commit();
        session.close();
    }

    @Override
    public void update(Character character) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.update(character);
        tx.commit();
        session.close();
    }

    @Override
    public void insert(CharacterInfo info) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.save(info);
        tx.commit();
        session.close();
    }

    @Override
    public void update(CharacterInfo info) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        session.update(info);
        tx.commit();
        session.close();
    }
}
