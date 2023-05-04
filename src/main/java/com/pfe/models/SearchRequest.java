package com.pfe.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class SearchRequest {
    private int pageNumber = 0;
    private int pageSize = 20;
    private Sort.Direction sortDirection = Sort.Direction.DESC;
    private String sortBy = "name";
    private Long idCategory;
    private String name;
    private String city;
    private String country;
}
