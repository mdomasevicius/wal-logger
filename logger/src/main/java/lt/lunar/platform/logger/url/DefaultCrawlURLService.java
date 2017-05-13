package lt.lunar.platform.logger.url;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class DefaultCrawlURLService implements CrawlURLService {

    private final CrawlURLRepository crawlURLRepository;

    DefaultCrawlURLService(CrawlURLRepository crawlURLRepository) {
        this.crawlURLRepository = crawlURLRepository;
    }

    @Override
    public CrawlURLDto create(String crawlerId, CrawlURLDto crawlURL) {
        if (crawlURLRepository.findByCrawlerIdAndUrl(crawlerId, crawlURL.getUrl()) != null) {
            throw new IllegalArgumentException();
        }

        CrawlURL entity = fromDto(crawlURL);
        entity.setCrawlerId(crawlerId);
        return toDto(crawlURLRepository.save(entity));
    }

    @Override
    public CrawlURLDto findOne(Long id) {
        return Optional.ofNullable(crawlURLRepository.findOne(id))
            .map(DefaultCrawlURLService::toDto)
            .orElseThrow(IllegalArgumentException::new);
    }

    private static CrawlURLDto toDto(CrawlURL crawlURL) {
        CrawlURLDto dto = new CrawlURLDto();
        dto.setId(crawlURL.getId());
        dto.setCrawlerId(crawlURL.getCrawlerId());
        dto.setUrl(crawlURL.getUrl());
        return dto;
    }

    private static CrawlURL fromDto(CrawlURLDto dto) {
        CrawlURL url = new CrawlURL();
        url.setCrawlerId(dto.getCrawlerId());
        url.setUrl(dto.getUrl());
        return url;
    }
}
