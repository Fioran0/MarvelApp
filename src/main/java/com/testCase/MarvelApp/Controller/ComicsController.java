package com.testCase.MarvelApp.Controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.testCase.MarvelApp.Entity.Characters;
import com.testCase.MarvelApp.Entity.Comics;
import com.testCase.MarvelApp.Service.ComicsService;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "public")
public class ComicsController {

    @Autowired
    private ComicsService comicsService;

    public ComicsController(ComicsService comicsService) {
        this.comicsService = comicsService;
    }



    @GetMapping(value = "/comics")
    public ResponseEntity<List<Comics>> getAllComics(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy)
    {
        List<Comics> list = comicsService.getAllComics(pageNo, pageSize, sortBy);

        return new ResponseEntity<List<Comics>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping (value = "/comicsWithFilterId")
    public ResponseEntity<List<Comics>> getAllComicsWithIdFilter(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam("filterById") Long id)
    {
        Pageable paging = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        List<Comics> list = comicsService.findByPagingCriteria(id, paging);

        return new ResponseEntity<List<Comics>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/comics/{id}")
    public ResponseEntity getOneById(@PathVariable Long id){
        try{
            Optional<Comics> tmpComics = Optional.of(comicsService.getOneById(id));
            return ResponseEntity.status(HttpStatus.OK).body(tmpComics);
        } catch (RuntimeException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comics not found", ex);
        }
    }

/*    @GetMapping(value = "comics/{comicsId}/characters")
    public ResponseEntity findCharactersByComicsId(@PathVariable Long comicsId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(comicsService.findCharactersByComicsId(comicsId));
        } catch (RuntimeException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comics not found", ex);
        }
    }*/

    @GetMapping(value = "comics/{comicsId}/characters")
    public ResponseEntity findCharactersWithComicsId(@PathVariable Long comicsId,
                                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "3") Integer pageSize,
                                                    @RequestParam(defaultValue = "name") String sortBy){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(comicsService.findCharactersWithComicsId(comicsId, pageNo, pageSize, sortBy));
        } catch (RuntimeException ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comics not found", ex);
        }
    }

    @PostMapping(value = "/comics/add")
    public ResponseEntity addComics(@RequestBody @Validated Comics comics){
        Comics tmpComics = comicsService.addComics(comics);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tmpComics.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/comics/update/{id}")
    public ResponseEntity updateComicsById(@PathVariable("id") Long id, @Validated @RequestBody Comics comics ){
        Optional<Comics> tmpComics = Optional.ofNullable(Optional.of(comicsService.getOneById(id))
                                        .orElseThrow(() -> new RuntimeException("Comics with ID:" + id + "not found")));
        tmpComics.get().setDescription(comics.getDescription());
        tmpComics.get().setIssueNumber(comics.getIssueNumber());
        tmpComics.get().setModifiedTime(comics.getModifiedTime());
        tmpComics.get().setPageCount(comics.getPageCount());
        tmpComics.get().setPrice(comics.getPrice());
        tmpComics.get().setTitle(comics.getTitle());
        tmpComics.get().setResourceURL(comics.getResourceURL());
        comicsService.addComics(tmpComics.get());
        return ResponseEntity.status(HttpStatus.OK).body(tmpComics.get());
    }

}
