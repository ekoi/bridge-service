package nl.knaw.dans.dataverse.bridge.service.db.dao;

import nl.knaw.dans.dataverse.bridge.service.db.domain.ArchivingAuditLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Class ArchivingAuditlogDao
 * Created by Eko Indarto
 * <p>
 * This class is used to access data for the ArchivingAuditLog entity.
 * ArchivingAuditLog annotation allows the component scanning support to find and
 * configure the DAO wihtout any XML configuration and also provide the Spring
 * exceptiom translation.
 * Since we've setup setPackagesToScan and transaction manager on
 * DatabaseConfig, any bean method annotated with Transactional will cause
 * Spring to magically call begin() and commit() at the start/end of the
 * method. If exception occurs it will also call rollback().
 */
@Repository
@Transactional
public class ArchivingAuditlogDao {

    // An EntityManager will be automatically injected from entityManagerFactory
    // setup on DatabaseConfig class.
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save the archivingAuditLog in the database.
     */
    public void create(ArchivingAuditLog archivingAuditLog) {
        entityManager.persist(archivingAuditLog);
    }

    /**
     * Delete the archivingAuditLog from the database.
     */
    public void delete(ArchivingAuditLog archivingAuditLog) {
        if (entityManager.contains(archivingAuditLog))
            entityManager.remove(archivingAuditLog);
        else
            entityManager.remove(entityManager.merge(archivingAuditLog));
    }

    /**
     * Return all the archiveds stored in the database.
     */
    @SuppressWarnings("unchecked")
    public List<ArchivingAuditLog> getAll() {
        return entityManager.createQuery("from ArchivingAuditLog order by id").getResultList();
    }


    /**
     * Update the passed archivingAuditLog in the database.
     */
    public void update(ArchivingAuditLog archivingAuditLog) {
        entityManager.merge(archivingAuditLog);
    }

    /**
     * Return the ArchivingAuditLog having the passed name.
     */
    public ArchivingAuditLog getById(long id) {
        Query q = entityManager.createQuery(
                "from ArchivingAuditLog where id = :id")
                .setParameter("id", id);
        try {
            return (ArchivingAuditLog) q.getSingleResult();
        } catch (NoResultException nre) {
            //Ignore this because as per our logic this is ok!
        }
        return null;
    }
    /**
     * Return the ArchivingAuditLog having the passed name.
     */
    public ArchivingAuditLog getBySrcxmlSrcversionTargetiri(String srcMetadataXml, String srcMetadataVersion, String darName) {
        Query q = entityManager.createQuery(
                "from ArchivingAuditLog where srcMetadataXml = :srcMetadataXml and srcMetadataVersion = :srcMetadataVersion and darName = :darName")
                .setParameter("srcMetadataXml", srcMetadataXml)
                .setParameter("srcMetadataVersion", srcMetadataVersion)
                .setParameter("darName", darName);
        try {
            return (ArchivingAuditLog) q.getSingleResult();
        } catch (NoResultException nre) {
            //Ignore this because as per our logic this is ok!
        }
        return null;
    }

    /**
     * Return the ArchivingAuditLog having the passed name.
     */
    public ArchivingAuditLog getBySrcxmlSrcversionTargetiriState(String srcMetadataXml, String srcMetadataVersion, String darName, String state) {
        Query q = entityManager.createQuery(
                "from ArchivingAuditLog where srcMetadataXml = :srcMetadataXml and srcMetadataVersion = :srcMetadataVersion and darName = :darName")
                .setParameter("srcMetadataXml", srcMetadataXml)
                .setParameter("srcMetadataVersion", srcMetadataVersion)
                .setParameter("darName", darName)
                .setParameter("state", state);
        try {
            return (ArchivingAuditLog) q.getSingleResult();
        } catch (NoResultException nre) {
            //Ignore this because as per our logic this is ok!
        }
        return null;
    }
} 
