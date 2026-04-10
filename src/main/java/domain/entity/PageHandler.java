package domain.entity;

public class PageHandler {
    private int totalCnt;
    private int pageSize;
    private int naviSize = 5;
    private int totalPage;
    private int page;
    private int beginPage;
    private int endPage;
    private boolean showPrev;
    private boolean showNext;

    public PageHandler(int totalCnt, int page, int pageSize) {
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;

        totalPage = (int) Math.ceil((double) totalCnt / pageSize);
        endPage = (int) (Math.ceil(page / (double) naviSize)) * naviSize;
        beginPage = endPage - naviSize + 1;
        if (endPage > totalPage) endPage = totalPage;
        showPrev = beginPage != 1;
        showNext = endPage != totalPage;
    }

    public int getTotalCnt() { return totalCnt; }
    public int getPageSize() { return pageSize; }
    public int getTotalPage() { return totalPage; }
    public int getPage() { return page; }
    public int getBeginPage() { return beginPage; }
    public int getEndPage() { return endPage; }
    public boolean isShowPrev() { return showPrev; }
    public boolean isShowNext() { return showNext; }
}
