package com.muvix.backend.dto.response;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
@Getter @Setter @NoArgsConstructor
@Schema(name="PaginatedResponse")
public class PaginatedResponse<T> {
    private List<T> items;
    private int page,size,totalPages;
    private long totalElements;

    public PaginatedResponse(List<T> items,int page,int size,long totalElements,int totalPages){
        this.items=items;
        this.page=page;
        this.size=size;
        this.totalElements=totalElements;
        this.totalPages=totalPages;
    }

}
