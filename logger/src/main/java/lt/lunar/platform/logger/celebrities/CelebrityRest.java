package lt.lunar.platform.logger.celebrities;

import lt.lunar.platform.logger.CollectionResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/celebrities")
@RestController
class CelebrityRest {

    private final CelebritiesService celebritiesService;

    CelebrityRest(CelebritiesService celebritiesService) {
        this.celebritiesService = celebritiesService;
    }

    @GetMapping("/{id}")
    ResponseEntity<CelebrityResource> findOne(@PathVariable Long id) {
        return ok(CelebrityResource.toResource(celebritiesService.findOne(id)));
    }

    @GetMapping
    ResponseEntity<CollectionResource<CelebrityResource>> fetchCelebsByUrl(@RequestParam("url") String url) {
        CollectionResource<CelebrityResource> collection = new CollectionResource<>(celebritiesService.findByUrl(url)
            .stream()
            .map(CelebrityResource::toResource)
            .collect(toList()));
        return ok(collection);
    }

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CelebrityResource request) {
        CelebrityDto celebrity = celebritiesService.create(request.toDto());
        URI uri = linkTo(methodOn(CelebrityRest.class).findOne(celebrity.getId())).toUri();
        return created(uri).build();
    }

}