/**
 * UI Interaction & Visual Effects Only
 * (No Mock Data, No Custom Modals)
 */

// Mobile menu toggle (네비게이션 바 토글은 유지)
function toggleMobileMenu() {
  const navLinks = document.getElementById("navLinks")
  const menuBtn = document.querySelector(".mobile-menu-btn")
  navLinks.classList.toggle("active")
  menuBtn.classList.toggle("active")
}

// Get current page from body data attribute
const currentPage = document.body.getAttribute("data-page")

// ==========================================
// 1. Index Page (애니메이션 효과만 유지)
// ==========================================
if (currentPage === "index") {
  
  function initIndexUI() {
    
    // 카드 마우스 호버 효과 (JSP가 그려준 요소에 반응)
    document.querySelectorAll(".scan-card").forEach((card) => {
      card.addEventListener("mousemove", (e) => {
        const rect = card.getBoundingClientRect()
        const x = ((e.clientX - rect.left) / rect.width) * 100
        const y = ((e.clientY - rect.top) / rect.height) * 100
        card.style.setProperty("--mouse-x", `${x}%`)
        card.style.setProperty("--mouse-y", `${y}%`)
      })
    })

    // 점(Dot) 깜빡임 애니메이션
    const dot = document.querySelectorAll('.dot');
    const onday = document.querySelector('.onday');
    
    if (dot && window.matchMedia('(min-width: 1024px)').matches) {
      dot.forEach(function (e) { e.style.opacity = '0'; });
      
      setInterval(function () {
        if (dot.length > 0) {
            if (dot[0].style.opacity == '1') {
                dot.forEach(function (e) { e.style.opacity = '0'; });
            } else {
                dot.forEach(function (e) { e.style.opacity = '1'; });
            }
        }
      }, 1000); 
    }

    // 스크롤 시 흐림(Blur) 효과 처리 (IntersectionObserver)
    const observer = new IntersectionObserver(entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          if (onday) onday.style.filter = 'blur(5px)';
        } else {
          if (onday) onday.style.filter = 'blur(1px)';
        }
      });
    }, {
      root: null,
      threshold: 0.1
    });

    const targetElement = document.querySelector('.scans-section');
    if (targetElement) {
      observer.observe(targetElement);
    }
  }

  initIndexUI()
}


// ==========================================
// 2. Dashboard Page (페이지 이동 및 기본 알림만 유지)
// ==========================================
if (currentPage === "dashboard") {

  // 상세 페이지로 이동 (단순 링크 이동)
  function viewScan(id) {
    window.location.href = `article.jsp?id=${id}`
  }

  // 삭제 버튼 클릭 시 (브라우저 기본 confirm 창 사용)
  function deleteScan(id) {
    if (confirm("정말 삭제하시겠습니까?")) {
      // 실제 구현 시: location.href = 'delete.do?id=' + id; 형태로 변경 필요
      alert("삭제 기능은 서버와 연동해야 합니다."); 
    }
  }

  // 수정 버튼 클릭 시 (모달 로직 삭제됨)
  // 대신 수정 페이지로 이동하거나, 구현 방식에 따라 채워넣으세요.
  function editScan(id) {
     // 모달 띄우는 코드 삭제됨
     console.log("수정 버튼 클릭됨: " + id);
     // 예: window.location.href = `update.jsp?id=${id}`;
  }

  // 설정 메뉴 토글 (드롭다운 UI는 모달과 다르므로 유지, 필요 없으면 삭제 가능)
  function toggleSetting(element) {
    element.classList.toggle("active")
  }

  function exportData() {
    alert("데이터 내보내기 기능 구현 필요")
  }

  function generateReport() {
    alert("보고서 생성 기능 구현 필요")
  }

  function handleLogout() {
    if (confirm("로그아웃 하시겠습니까?")) {
      // 로그아웃 서블릿으로 이동
      window.location.href = "logOut.do" 
    }
  }

  // 전역 스코프에 함수 등록 (HTML onclick에서 부르기 위함)
  window.viewScan = viewScan;
  window.editScan = editScan;     // 모달은 안 뜨지만 에러 방지용 빈 함수 연결
  window.deleteScan = deleteScan;
  window.toggleSetting = toggleSetting;
  window.exportData = exportData;
  window.generateReport = generateReport;
  window.handleLogout = handleLogout;
  
  // closeModal, saveEdit 등 모달 관련 함수 및 이벤트 리스너 모두 삭제됨
}

// ==========================================
// 3. Write Page (폼 이벤트)
// ==========================================
if (currentPage === "write") {
  // 폼 제출 시 특별한 JS 처리가 필요 없다면 이 부분도 비워도 됩니다.
  const writeForm = document.querySelector("form")
  if (writeForm) {
    writeForm.addEventListener("submit", function(e) {
        // e.preventDefault(); // JSP 서블릿 전송을 위해 주석 처리하거나 제거
        // 폼 유효성 검사 로직이 필요하면 여기에 작성
    })
  }
}

// ==========================================
// 4. Article Page
// ==========================================
if (currentPage === "article") {
  // JS로 처리할 로직 없음 (JSP가 데이터 표시함)
}