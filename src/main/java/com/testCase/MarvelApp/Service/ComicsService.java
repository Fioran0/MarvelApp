package com.testCase.MarvelApp.Service;

import com.testCase.MarvelApp.Entity.Characters;
import com.testCase.MarvelApp.Entity.Comics;
import com.testCase.MarvelApp.Repository.ComicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComicsService {

    @Autowired
    private ComicsRepository comicsRepository;

    public List<Comics> findAll (){
        return comicsRepository.findAll();
    }

    public Comics getOneById(Long id){
        return comicsRepository.findById(id).get();
    }

    public Comics addComics(Comics comics){
        return comicsRepository.save(comics);
    }

    public List<Object> findCharactersByComicsId(Long id){
        return comicsRepository.findByComicsId(id);
    }

    public List<Object> findCharactersWithComicsId(Long id, Integer pageNo, Integer pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<Object> pagedResult = comicsRepository.findWithComicsId(id, paging);
        return pagedResult;
    }

    public List<Comics> getAllComics(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<Comics> pagedResult = comicsRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Comics>();
        }
    }


    public List<Comics> findByPagingCriteria(Long id, Pageable pageable){
        Page page = comicsRepository.findAll(new Specification<Comics>() {
            @Override
            public Predicate toPredicate(Root<Comics> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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
