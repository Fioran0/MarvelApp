package com.testCase.MarvelApp.Controller;

import com.testCase.MarvelApp.Entity.Characters;
import com.testCase.MarvelApp.Entity.Comics;
import com.testCase.MarvelApp.Service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.criteria.Order;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "public")
public class CharacterController {
    @Autowired
    private CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping (value = "/characters")
    public ResponseEntity<List<Characters>> getAllCharacters(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<Characters> list = characterService.getAllCharacters(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<Characters>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping (value = "/charactersWithFilterId")
    public ResponseEntity<List<Characters>> getAllCharactersWithIdFilter(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam("filterById") Long id)
    {
        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        List<Characters> list = characterService.findByPagingCriteria(id, paging);

        return new ResponseEntity<List<Characters>>(list, new HttpHeaders(), HttpStatus.OK);
    }

/*    @GetMapping(value = "characters/name")
    public ResponseEntity filterByName(@RequestParam("filterById") Long id) {
        Pageable paging = PageRequest.of(0, 3, Sort.by("id"));
        return ResponseEntity.status(HttpStatus.OK).body(characterService.findByPagingCriteria(id, paging));
    }*/

    @GetMapping(value = "/character/{id}")
     public ResponseEntity findOne(@PathVariable Long id){
        try{
        Optional<Characters> tmpCharacter = Optional.of(characterService.getOneById(id));
            return ResponseEntity.status(HttpStatus.OK).body(tmpCharacter);
        } catch (RuntimeException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found", ex);
        }
    }

/*    @GetMapping(value = "characters/{characterId}/comics")
    public ResponseEntity findComicsByCharacterId(@PathVariable Long characterId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(characterService.findComicsByCharacterId(characterId));
        } catch (RuntimeException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found", ex);
        }
    }*/

    @GetMapping(value = "characters/{characterId}/comics")
    public ResponseEntity findComicsWithCharacterId(@PathVariable Long characterId,
                                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "3") Integer pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(characterService.findComicsWithCharacterId(characterId, pageNo, pageSize, sortBy));
        } catch (RuntimeException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found", ex);
        }
    }


    @PostMapping
    public ResponseEntity addCharacter(@RequestBody @Validated Characters character){
        Characters tmpCharacter = characterService.add(character);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tmpCharacter.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/characters/update/{id}")
    public ResponseEntity updateCharacterById(@PathVariable("id") Long id, @Validated @RequestBody Characters character ){
        Optional<Characters> tmpCharacter = Optional.ofNullable(Optional.of(characterService.getOneById(id))
                .orElseThrow(() -> new RuntimeException("Character with ID:" + id + "not found")));
        tmpCharacter.get().setCharacterName(character.getCharacterName());
        tmpCharacter.get().setCharacterDescription(character.getCharacterDescription());
        tmpCharacter.get().setModifiedTime(character.getModifiedTime());
        tmpCharacter.get().setResourceURL(character.getResourceURL());
//        tmpCharacter.get().setComicsSet(character.getComicsSet());
        characterService.add(tmpCharacter.get());
        return ResponseEntity.status(HttpStatus.OK).body(tmpCharacter.get());
    }

/*    @GetMapping(value = "/charactersPage")
    public ResponseEntity getAllCharactersPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sort){
        try {

            List<Characters> characters = new ArrayList<>();
            Pageable pagingSort = PageRequest.of(page, size);

            Page<Characters> pageTuts;
            pageTuts = characterService.findAllCharact(pagingSort);


            characters = pageTuts.getContent();

            if (characters.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", characters);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

}

