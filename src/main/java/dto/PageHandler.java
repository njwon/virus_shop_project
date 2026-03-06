package dto;

public class PageHandler {
    private int totalCnt; // 총 게시물 개수
    private int pageSize; // 한 페이지에 보여줄 게시물 수 (보통 10개)
    private int naviSize = 5; // 페이지 네비게이션 크기 (1~10, 11~20...)
    private int totalPage; // 전체 페이지 수
    private int page;     // 현재 페이지
    private int beginPage; // 네비게이션 시작 페이지 (1, 11, 21...)
    private int endPage;   // 네비게이션 끝 페이지 (10, 20, 30...)
    private boolean showPrev; // 이전 버튼 보일지 여부
    private boolean showNext; // 다음 버튼 보일지 여부

    public PageHandler(int totalCnt, int page, int pageSize) {
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;

        // 1. 전체 페이지 수 계산 (나머지가 있으면 1페이지 추가)
        totalPage = (int) Math.ceil((double) totalCnt / pageSize);

        // 2. 현재 페이지를 기준으로 네비게이션의 끝 페이지 계산 (예: 15페이지면 끝은 20)
        endPage = (int) (Math.ceil(page / (double) naviSize)) * naviSize;

        // 3. 시작 페이지 계산
        beginPage = endPage - naviSize + 1;

        // 4. 실제 끝 페이지가 계산된 endPage보다 작으면 수정
        if (endPage > totalPage) {
            endPage = totalPage;
        }

        // 5. 이전, 다음 버튼 유무
        showPrev = beginPage != 1;
        showNext = endPage != totalPage;
    }

    // Getter 메서드들 (JSP에서 EL로 사용하기 위해 필수)
    public int getTotalCnt() { return totalCnt; }
    public int getPageSize() { return pageSize; }
    public int getTotalPage() { return totalPage; }
    public int getPage() { return page; }
    public int getBeginPage() { return beginPage; }
    public int getEndPage() { return endPage; }
    public boolean isShowPrev() { return showPrev; }
    public boolean isShowNext() { return showNext; }
}