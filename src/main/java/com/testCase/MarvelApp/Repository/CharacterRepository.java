package com.testCase.MarvelApp.Repository;

import com.testCase.MarvelApp.Entity.Characters;
import com.testCase.MarvelApp.Entity.Comics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Characters, Long>, JpaSpecificationExecutor<Characters> {
    @Override
    List<Characters> findAll();

    @Override
    List<Characters> findAll(Specification<Characters> specification);

    @Override
    Characters getById(Long aLong);

    @Override
    Page<Characters> findAll(Pageable pageable);

    @Override
    <S extends Characters> S save(S entity);

    @Override
    Optional<Characters> findById(Long aLong);

    @Query(value = "SELECT distinct comic_id as id, title, issue_number as issueNumber, co.description,  co.modified_time as modifiedTime," +
            " page_count as pageCount, comic_price as price, co.resourceurl \n" +
            "\tFROM public.comics AS co, public.comics_characters AS c_c\n" +
            "\tWHERE co.comic_id = c_c.comics_id AND c_c.characters_id = :id", nativeQuery = true)
    List<Object> findByCharacterId(@Param ("id") Long id);

    @Query(value = "SELECT distinct comic_id as id, title, issue_number as issueNumber, co.description,  co.modified_time as modifiedTime," +
            " page_count as pageCount, comic_price as price, co.resourceurl \n" +
            "\tFROM public.comics AS co, public.comics_characters AS c_c\n" +
            "\tWHERE co.comic_id = c_c.comics_id AND c_c.characters_id = :id", nativeQuery = true)
    List<Object> findWithCharacterId(@Param("id") Long id, Pageable pageable);
}

