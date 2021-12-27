package com.testCase.MarvelApp.Service;

import com.testCase.MarvelApp.Entity.Characters;
import com.testCase.MarvelApp.Repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public List<Characters> findAll(){
        return characterRepository.findAll();
    }

    public Characters getOneById(Long id){
        return characterRepository.findById(id).get();
    }

    public Characters add(Characters characters){
        return characterRepository.save(characters);
    }

    public Characters updateById (Long id){
        Characters characters = characterRepository.getById(id);
        return characters;
    }
/*
    public List<Object> findComicsByCharacterId(Long id){
        return characterRepository.findByCharacterId(id);
    }*/

    public List<Object> findComicsWithCharacterId(Long id, Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<Object> pagedResult = characterRepository.findWithCharacterId(id, paging);

            return pagedResult;
    }

    public List<Characters> getAllCharacters(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Characters> pagedResult = characterRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Characters>();
        }
    }

    public List<Characters> findByPagingCriteria(Long id,Pageable pageable){
        Page page = characterRepository.findAll(new Specification<Characters>() {
            @Override
            public Predicate toPredicate(Root<Characters> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(id != null) {
                    predicates.add(criteriaBuilder.equal(root.get("id"), id));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        page.getTotalElements();        // get total elements
        page.getTotalPages();           // get total pages
        return page.getContent();       // get List of Employee
    }
}
