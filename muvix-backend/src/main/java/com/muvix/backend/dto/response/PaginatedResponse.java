package com.muvix.backend.dto.response;
import java.util.List;
public class PaginatedResponse<T> {
    private List<T> items;
    private int page,size,totalPages;
    private long totalElements;

    public PaginatedResponse(){}

    public PaginatedResponse(List<T> items,int page,int size,long totalElements,int totalPages){
        this.items=items;
        this.page=page;
        this.size=size;
        this.totalElements=totalElements;
        this.totalPages=totalPages;
    }

    public List<T> getItems(){return items;} public void setItems(List<T> v){items=v;}
    public int getPage(){return page;} public void setPage(int v){page=v;}
    public int getSize(){return size;} public void setSize(int v){size=v;}
    public long getTotalElements(){return totalElements;} public void setTotalElements(long v){totalElements=v;}
    public int getTotalPages(){return totalPages;} public void setTotalPages(int v){totalPages=v;}
}
