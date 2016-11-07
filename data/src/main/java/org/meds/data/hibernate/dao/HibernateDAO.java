package org.meds.data.hibernate.dao;

import org.hibernate.Session;
import org.meds.data.hibernate.Hibernate;

public abstract class HibernateDAO {

    protected Session openSession() {
        return Hibernate.getSessionFactory().openSession();
    }
}
