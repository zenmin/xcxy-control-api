package com.zh.controlcenter.common.apibase;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

/**
 * @Describle This Class Is 分页实体
 * @Author ZengMin
 * @Date 2019/3/15 9:40
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "分页信息")
public class Pager<T> {

    @Schema(description = "页码", example = "1")
    private Integer num = 0;

    @Schema(description = "分页大小", example = "10")
    private Integer size = 10;

    @Schema(hidden = true)
    private List<T> data;

    @Schema(hidden = true)
    private Boolean last;

    @Schema(hidden = true)
    private Integer totalNums;

    @Schema(hidden = true)
    private Integer totalPages;

    public static Pager of(IPage iPage) {
        if (Objects.isNull(iPage)) {
            return null;
        }
        return new Pager((int)iPage.getCurrent(), (int)iPage.getSize(), iPage.getRecords(), iPage.getPages() <= iPage.getCurrent(), (int)iPage.getTotal(), (int)iPage.getPages());
    }

    public static Pager of(List<?> list, Pager pager, Integer allCount) {
        if (Objects.isNull(allCount)) {
            allCount = 0;
        }
        int pages = Math.toIntExact(allCount % pager.getSize()) == 0 ? Math.toIntExact(allCount / pager.getSize()) : Math.toIntExact(allCount / pager.getSize()) + 1;
        return new Pager(pager.getNum(), pager.getSize(), list, pages <= pager.getNum(), allCount, pages);
    }

    public static Pager ofPageInfo(IPage iPage) {
        return new Pager((int)iPage.getCurrent(), (int)iPage.getSize(), null, iPage.getPages() <= iPage.getCurrent(), (int)iPage.getTotal(), (int)iPage.getPages());
    }

    public Pager(int num, int size) {
        this.num = num;
        if (num <= 1) {
            this.num = 0;
        }
        this.size = size;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
        if (num <= 1) {
            this.num = 0;
        }
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getTotalNums() {
        return totalNums;
    }

    public void setTotalNums(Integer totalNums) {
        this.totalNums = totalNums;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
