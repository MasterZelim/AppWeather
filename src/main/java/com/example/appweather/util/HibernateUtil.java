package com.example.appweather.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil{
    private static StandardServiceRegistry registry;
    private static volatile SessionFactory sessionFactory;
    private static final HibernateUtil INSTANCE = new HibernateUtil();
    private HibernateUtil(){

    }
    public static HibernateUtil getInstance(){
       return INSTANCE;
    }
    public  SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        // Create registry
                        registry = new StandardServiceRegistryBuilder().configure().build();

                        // Create MetadataSources
                        MetadataSources sources = new MetadataSources(registry);

                        // Create Metadata
                        Metadata metadata = sources.getMetadataBuilder().build();

                        // Create SessionFactory
                        sessionFactory = metadata.getSessionFactoryBuilder().build();

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (registry != null) {
                            registry.close();
                        }
                    }
                }
            }
        }

        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            registry.close();
        }
    }
}