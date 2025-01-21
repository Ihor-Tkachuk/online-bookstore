package com.example.demo.repository.book;

import com.example.demo.dto.BookSearchParameters;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationBuilder;
import com.example.demo.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.getTitles() != null && searchParameters.getTitles().length > 0) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"),
                    "%" + searchParameters.getTitles()[0] + "%"));
        }
        if (searchParameters.getAuthors() != null && searchParameters.getAuthors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(searchParameters.getAuthors()));
        }
        if (searchParameters.getIsbns() != null && searchParameters.getIsbns().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("isbn")
                    .getSpecification(searchParameters.getIsbns()));
        }
        return spec;
    }
}
