package academy.prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
public class UrlController {
        private final UrlService urlService;

        public UrlController(UrlService urlService) {
            this.urlService = urlService;
        }

        @PostMapping("shorten")
        public UrlResultDTO shorten(@RequestBody UrlDTO urlDTO) { // Jackson / GSON
            long id = urlService.saveUrl(urlDTO);

            var result = new UrlResultDTO();
            result.setUrl(urlDTO.getUrl());
            result.setShortUrl(Long.toString(id));

            return result;
        }
    //Видалення посилань

    @DeleteMapping("/delete/{link}")
    public ResponseEntity<Boolean> delete(@PathVariable Long link) {
        boolean result = urlService.delUrl(link);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/my{id}")
    public ResponseEntity<Void> redirect(@PathVariable Long id) {
        String url = urlService.getUrl(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        headers.setCacheControl("no-cache, no-store, must-revalidate");

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping("/stat")
    public ResponseEntity<List<UrlStatDTO>> stat() {
        List<UrlStatDTO> stat = urlService.getStatistics();
        return ResponseEntity.ok().body(stat);
    }

}



