package org.meds.database.dao.impl.hibernate;

import org.hibernate.Session;

public abstract class HibernateDAO {

    protected Session openSession() {
        return Hibernate.getSessionFactory().openSession();
    }
}
