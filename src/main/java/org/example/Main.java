/*
Kreirati novi Java projekt koji će sadržavati perzistentnu klasu „User” s varijablama „id” (Long), „username” (String) i „email” (String) te
odgovarajuću konfiguraciju u datoteci „persistence.xml”
Kreirati novu klasu s „main” metodom koja će sadržavati metode „persistUser”, „detachUser”, „reattachUser” i „deleteUser”
Metoda „persistUser” mora korištenjem EntityManager i EntityTransaction klasa pozivati metodu „persist” i perzistirati jedan primjer objekta klase „User”
Metoda „detachUser” mora na sličan način pozvati metodu „detach”, a metoda „reattach” mora pozivom metode „merge” ponovno povezati odvojeni entitet
Metodom „remove” na sličan način i obrisati objekt klase „User”



*/

package org.example;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.model.User;

public class Main {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("data");

    public static void main(String[] args) {


        User user1 = new User("Josip Josipović", "jo@mail.com");
        persistUser(user1);
        System.out.println("User persuisted: " + user1);

        detachUser(user1);
        System.out.println("Detached user: " + user1.getId());

        User reattachedUser = reattachUser(user1);
        System.out.println("Reattached user: " + reattachedUser);

        deleteUser(reattachedUser);
        System.out.println("Deleted user: " + user1);


    }

    private static void persistUser(User user) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
// Spremanje entiteta u bazu
            em.persist(user);
            transaction.commit();
            System.out.println("User persisted successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void detachUser(User user) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();


            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                em.detach(managedUser);
                System.out.println("User detached successfully!");


                System.out.println("User persisted successfully!");

            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static User reattachUser(User detachedUser) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        User mergedUser = null;
        try {
            transaction.begin();

            mergedUser = em.merge(detachedUser);
            System.out.println("User reattached (merged) successfully!");

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return mergedUser;
    }

    private static void deleteUser(User user) {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            User delUser = em.find(User.class, user.getId());
            if (delUser != null) {

                em.remove(delUser);
                System.out.println("User deleted successfully!");
            }

            transaction.commit();
            System.out.println("User persisted successfully!");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
