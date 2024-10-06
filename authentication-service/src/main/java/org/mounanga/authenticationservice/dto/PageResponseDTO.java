package org.mounanga.authenticationservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PageResponseDTO<T> {
    private int totalPages;
    private int size;
    private long totalElements;
    private int numberOfElements;
    private int number;
    private boolean hasContent;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;
    private List<T> content;
}
