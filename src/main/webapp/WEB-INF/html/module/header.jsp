<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SecureWeb</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&family=Inter:wght@400;500;600&family=JetBrains+Mono:wght@400;500;600&display=swap" rel="stylesheet">
    <!-- Replaced inline styles with external CSS file -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/styles.css">
    <script>
    (function() {
        var _res = null;
        function build() {
            var o = document.createElement('div');
            o.id = '_gm';
            o.style.cssText = 'display:none;position:fixed;inset:0;background:rgba(0,0,0,.75);backdrop-filter:blur(6px);z-index:99999;align-items:center;justify-content:center';
            o.innerHTML = '<div style="background:#1a1a1a;border:1px solid #2a2a2a;padding:2rem;max-width:380px;width:90%;text-align:center"><p id="_gm_msg" style="color:#e8e8e8;margin:0 0 1.5rem;font-size:.95rem;line-height:1.5;word-break:break-word"></p><div style="display:flex;gap:.75rem;justify-content:center"><button id="_gm_cancel" style="padding:.5rem 1.5rem;background:transparent;border:1px solid #2a2a2a;color:#a0a0a0;cursor:pointer;font-size:.9rem">취소</button><button id="_gm_ok" style="padding:.5rem 1.5rem;background:#00ff88;color:#000;border:none;cursor:pointer;font-weight:600;font-size:.9rem">확인</button></div></div>';
            document.body.appendChild(o);
            document.getElementById('_gm_ok').onclick = function() { o.style.display='none'; if(_res) _res(true); };
            document.getElementById('_gm_cancel').onclick = function() { o.style.display='none'; if(_res) _res(false); };
            return o;
        }
        function show(msg, isConfirm) {
            var o = document.getElementById('_gm') || build();
            document.getElementById('_gm_msg').textContent = String(msg == null ? '' : msg);
            document.getElementById('_gm_cancel').style.display = isConfirm ? '' : 'none';
            o.style.display = 'flex';
            return new Promise(function(r) { _res = r; });
        }
        window.alert = function(msg) { show(msg, false); };
        window.showConfirm = function(msg, cb) { show(msg, true).then(cb); };
    })();
    </script>
</head>