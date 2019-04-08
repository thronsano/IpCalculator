package com.ipCalculator.MVC.services;

import com.ipCalculator.entity.exceptions.IpCalculatorException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.ipCalculator.utility.Logger.logWarning;

@Repository
public class PersistenceService<T> {

    @Autowired
    SessionFactory sessionFactory;

    void persistObject(T obj) throws IpCalculatorException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.saveOrUpdate(obj);
        } catch (Exception ex) {
            handleError(ex);
        } finally {
            finishSession(session);
        }
    }

    T getObjectById(String table, Object id) throws IpCalculatorException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from " + table + " where id=:selectedId");
            query.setParameter("selectedId", id);
            return (T) query.uniqueResult();
        } catch (Exception ex) {
            handleError(ex);
        } finally {
            finishSession(session);
        }

        return null;
    }

    List<T> getAllObjects(String table) throws IpCalculatorException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from " + table);
            return query.list();
        } catch (Exception ex) {
            handleError(ex);
        } finally {
            finishSession(session);
        }

        return new ArrayList<>();
    }

    void deleteObjectById(String table, Object selectedId) throws IpCalculatorException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("delete from " + table + " as obj where obj.id=:selectedId");
            query.setParameter("selectedId", selectedId);
            query.executeUpdate();
        } catch (Exception ex) {
            handleError(ex);
        } finally {
            finishSession(session);
        }
    }

    void handleError(Exception ex) throws IpCalculatorException {
        logWarning(ex.getMessage());
        throw new IpCalculatorException(ex.getMessage());
    }

    void finishSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }
}
