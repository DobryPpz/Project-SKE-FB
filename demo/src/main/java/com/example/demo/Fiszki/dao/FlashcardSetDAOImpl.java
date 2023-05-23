package com.example.demo.Fiszki.dao;
import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FlashcardSetDAOImpl implements FlashcardSetDAO {

    private EntityManager entityManager;

    @Autowired
    public FlashcardSetDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<FlashcardSet> findAll() {
        List<FlashcardSet> allFlashcardSets = entityManager.
                createQuery("FROM FlashcardSet",FlashcardSet.class).
                getResultList();
        return allFlashcardSets;
    }

    @Override
    public List<FlashcardSet> findAllByUser(String username) {
        TypedQuery<FlashcardSet> allSets = entityManager.createQuery(
                "FROM FlashcardSet WHERE username=:username",FlashcardSet.class);
        allSets.setParameter("username",username);
        List<FlashcardSet> results = allSets.getResultList();
        return results;
    }

    @Override
    public FlashcardSet findById(int theId) {
        FlashcardSet found = entityManager.find(FlashcardSet.class,theId);
        return found;
    }

    @Override
    public FlashcardSet save(FlashcardSet flashcardSet) {
        FlashcardSet saved = entityManager.merge(flashcardSet);
        return saved;
    }

    @Override
    public void deleteById(int theId) {
        FlashcardSet found = entityManager.find(FlashcardSet.class,theId);
        entityManager.remove(found);
    }
}
