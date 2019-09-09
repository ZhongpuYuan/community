package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 当后端数据返回时，不仅包含分页查询的数据库信息，还包括分页条信息
@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;// 分页条列表

    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    private Integer page;// 当前高亮页

    private List<Integer> pages = new ArrayList<>();

    private Integer totalPage;

    // size：每页显示的记录数，固定值2
    public void setPagination(Integer totalCount, Integer page, Integer size) {

        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        this.page = page;// 获取当前页，便于在页面上进行高亮显示

        // 分页条的展示逻辑
        pages.add(page);// page就是要展示的分页条，首先加入当前点击的页面

        for (int i = 1; i < 3; i++) {
            if (page-i > 0){
                // Inserts the specified element at the specified position in this list
                pages.add(0,page - i);// 向前插入时，插入头部。向后插入时，插入尾部
            }

            // 设totalCount=3。当i=1时，1+1=2<3，展示第二页。当i=2时，1+2=3<=3，展示第三页
            if (page + i <= totalPage){
                pages.add(page + i);
            }
        }

        // 是否展示上一页(若当前是第一页)
        if (page == 1){
            showPrevious = false;// 不显示"首页"和"上一页"(小箭头)
        }else {
            showPrevious = true;// 显示"最后一页"(小箭头)
        }

        // 是否展示下一页(若当前页是最后一页)
        if (page == totalPage){
            showNext = false;// 不显示"最后一页"(小箭头)
        }else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)){
            showFirstPage = false;// 当分页条包含页码1的时候，不展示
        }else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }
}