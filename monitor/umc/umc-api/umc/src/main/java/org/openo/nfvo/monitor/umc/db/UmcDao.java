/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.nfvo.monitor.umc.db;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;


/**
 * The module performance database operations class,
 * contains open session, closing the session, insert, modify and delete.
 * query specific is implemented by its subclasses.
 *
 */
public class UmcDao<E> extends AbstractDAO<E> implements IUmcDao{
    private SessionFactory sessionFactory;
    protected Session session;

    public UmcDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    protected Session currentSession() {
        return this.session;
    }

    protected void beginTransaction() {
        this.session = this.sessionFactory.openSession();
        this.session.beginTransaction();
    }

    protected void closeTransaction() {
        this.session.getTransaction().commit();
        this.session.close();
    }

    public void save(Object object) {

        beginTransaction();
        session.save(object);
        closeTransaction();

    }

    public void save(List<Object> objects) {
        beginTransaction();
        for (Object object : objects) {
            session.save(object);
        }
        closeTransaction();
    }

    public void delete(Object object) {
        beginTransaction();
        session.delete(object);
        closeTransaction();
    }

    public void delete(List<Object> objects) {
        beginTransaction();
        for (Object object : objects) {
            session.delete(object);
        }
        closeTransaction();
    }

    public void update(Object object) {
        beginTransaction();
        session.update(object);
        closeTransaction();
    }

    public void update(List<Object> objects) {
        beginTransaction();
        for (Object object : objects) {
            session.update(object);
        }
        closeTransaction();
    }

}
