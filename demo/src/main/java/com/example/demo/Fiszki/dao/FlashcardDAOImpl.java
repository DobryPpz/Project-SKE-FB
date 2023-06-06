package com.example.demo.Fiszki.dao;
import com.example.demo.Fiszki.models.Flashcard;
import com.example.demo.Fiszki.models.FlashcardSet;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FlashcardDAOImpl implements FlashcardDAO{

    private EntityManager entityManager;

    @Autowired
    public FlashcardDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<Flashcard> findAll() {
        List<Flashcard> allFlashcards = entityManager.
                createQuery("FROM Flashcard",Flashcard.class).
                getResultList();
        return allFlashcards;
    }

    @Override
    public Flashcard findById(int theId) {
        Flashcard found = entityManager.find(Flashcard.class,theId);
        return found;
    }

    @Override
    public Flashcard save(Flashcard flashcard) {
        Flashcard saved = entityManager.merge(flashcard);
        return saved;
    }

    @Override
    public void deleteById(int theId) {
        Flashcard found = entityManager.find(Flashcard.class,theId);
        entityManager.remove(found);
    }
}
