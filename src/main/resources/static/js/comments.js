function deletePost(id) {
    //DELETE /api/board/{id}
    $.ajax({
        url: '/api/board/' + id,
        type: 'DELETE',
        success: function(result) {
            console.log('result', result);
            alert('삭제되었습니다.')
            window.location.href = '/board/list';
        }
    });
}

$(document).ready(function () {
    const id = $('#create-comment-btn').data('id'); // 게시글 ID 가져오기
    let currentPage = 0; // 현재 페이지 번호
    const pageSize = 5; // 페이지당 댓글 수
    let totalPages = 0; // 총 페이지 수

    const socket = new SockJS('/ws'); // WebSocket 연결
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log('WebSocket 연결 성공!');
        stompClient.subscribe(`/topic/comments/${id}`, function (message) {
            const comment = JSON.parse(message.body);
            appendComment(comment);
            scrollToBottom(); // 댓글 추가 시 스크롤 자동 이동
        });
    });

    //전체 댓글 개수를 가져와 총 페이지 수 반환
    function getTotalPages() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: `/api/board/view/${id}/commentCount`,  // 총 댓글 개수 가져오는 API
                type: 'GET',
                success: function (count) {
                    resolve(Math.ceil(count / pageSize)); // 총 페이지 수 계산
                },
                error: function (xhr, status, error) {
                    console.error('총 댓글 개수 가져오기 실패:', error);
                    reject(error);
                }
            });
        });
    }

    //댓글 리스트 초기화
    function loadComments(page) {
        $.ajax({
            url: `/api/board/view/${id}/pagedComments?page=${page}&size=${pageSize}`,
            type: 'GET',
            success: function (comments) {
                console.log('댓글 데이터:', comments);
                $('#comment-list').empty();

                comments.content.forEach(comment => {
                    appendComment(comment);
                });

                currentPage = comments.number;
                totalPages = comments.totalPages;
                updatePagination();
            },
            error: function (xhr, status, error) {
                console.error('댓글 로드 실패:', error);
            }
        });
    }

    //댓글 추가 함수 (WebSocket & AJAX)
    function appendComment(comment) {
        const commentHtml = `
            <ul class="list-group list-group-flush">
                <li class="list-group-item">
                    <div class="text-muted fst-italic mb-2" style="font-weight: bold">${comment.username}</div>
                    <div>${comment.comment}</div>
                    <hr>
                </li>
            </ul>
        `;
        $('#comment-list').append(commentHtml);
    }

    //페이지네이션 버튼 업데이트
    function updatePagination() {
        const pagination = $('#pagination');
        pagination.empty();

        pagination.append(`<li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage - 1}">&laquo;</a>
        </li>`);

        for (let i = 0; i < totalPages; i++) {
            pagination.append(`<li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
            </li>`);
        }

        pagination.append(`<li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage + 1}">&raquo;</a>
        </li>`);
    }

    //페이지 버튼 클릭 시 댓글 로드
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        const page = $(this).data('page');
        if (page >= 0 && page < totalPages) {
            loadComments(page);
        }
    });

    //댓글 작성 버튼 클릭 시
    $('#create-comment-btn').on('click', function () {
        const commentData = {
            comment: $('#comment-content').val()
        };

        if (!commentData.comment.trim()) {
            alert('댓글 내용을 입력하세요.');
            return;
        }

        stompClient.send(`/app/comments/${id}`, {}, JSON.stringify(commentData));
        $('#comment-content').val('');

        // 댓글 추가 후 마지막 페이지로 이동
        getTotalPages().then(function (pages) {
            totalPages = pages;
            currentPage = totalPages - 1; // 마지막 페이지로 설정

            console.log("최신 currentPage:", currentPage);

            setTimeout(() => {
                loadComments(currentPage);
                scrollToBottom(); // 마지막 댓글로 스크롤 이동
            }, 300);
        }).catch(error => {
            console.error("getTotalPages() 호출 중 에러 발생:", error);
        });
    });

    //스크롤을 마지막 댓글로 이동
    function scrollToBottom() {
        $('#comment-list').animate({ scrollTop: $('#comment-list')[0].scrollHeight }, 500);
    }

    //페이지 로드 시 첫 번째 댓글 페이지 로드
    getTotalPages().then(function (pages) {
        totalPages = pages;
        currentPage = 0; // 첫 번째 페이지 설정
        loadComments(currentPage);
    }).catch(error => {
        console.error("초기 getTotalPages() 호출 실패:", error);
    });
});
