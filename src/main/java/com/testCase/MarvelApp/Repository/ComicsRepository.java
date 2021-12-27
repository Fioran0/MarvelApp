package com.testCase.MarvelApp.Repository;

import com.testCase.MarvelApp.Entity.Comics;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComicsRepository extends JpaRepository<Comics, Long>, JpaSpecificationExecutor<Comics> {

    @Override
    List<Comics> findAll();

    @Override
    Comics getOne(Long aLong);

    @Override
    <S extends Comics> List<S> findAll(Example<S> example, Sort sort);

    @Override
    Page<Comics> findAll(Pageable pageable);

    @Override
    <S extends Comics> S save(S entity);

    @Override
    <S extends Comics> Optional<S> findOne(Example<S> example);

    @Query(value = "SELECT character_id, description, image, name, modified_time, resourceurl\n" +
            "\tFROM public.characters AS c, public.comics_characters AS c_c\n" +
            "\tWHERE c.character_id = c_c.characters_id AND c_c.comics_id = :id", nativeQuery = true)
    List<Object> findByComicsId(@Param("id") Long id);

    @Query(value = "SELECT character_id, description, image, name, modified_time, resourceurl\n" +
            "\tFROM public.characters AS c, public.comics_characters AS c_c\n" +
            "\tWHERE c.character_id = c_c.characters_id AND c_c.comics_id = :id", nativeQuery = true)
    List<Object> findWithComicsId(@Param("id") Long id, Pageable pageable);


}
