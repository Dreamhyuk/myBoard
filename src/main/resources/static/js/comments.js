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
    let currentPage = 0; //현재 페이지 번호
    const pageSize = 5; //페이지당 댓글 수
    let totalPages = 0; //총 페이지 수

    // 댓글 리스트 초기화 함수
    function loadComments(page) {
        $.ajax({
            url: `/api/board/view/${id}/pagedComments?page=${page}&size=${pageSize}`,
            type: 'GET',
            success: function (comments) {
                console.log('댓글 데이터: ', comments);

                // 댓글 리스트 초기화
                $('#comment-list').empty();

                // 댓글 리스트 추가
                comments.content.forEach(comment => {
                    const commentHtml = `
                        <ul class="list-group list-group-flush" style="height: 100px;">
                            <li class="list-group-item">
                                <div class="text-muted fst-italic mb-2" style="font-weight: bold">${comment.username}</div>
                                <div>${comment.comment}</div>
                                <hr>
                            </li>
                        </ul>
                    `;
                    $('#comment-list').append(commentHtml);
                });

                // 현재 페이지 및 총 페이지 수 업데이트
                currentPage = comments.number;
                totalPages = comments.totalPages;

                // 페이지네이션 업데이트
                updatePagination();
            },
            error: function (xhr, status, error) {
                console.error('댓글 로드 실패:', error);
                alert('댓글을 불러오는 데 실패했습니다.');
            }
        });
    }

    // 페이지네이션 버튼 업데이트 함수
    function updatePagination() {
        const pagination = $('#pagination');
        pagination.empty(); // 기존 버튼 초기화

        // 이전 버튼
        const prevDisabled = currentPage === 0 ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${prevDisabled}">
                <a class="page-link" href="#" aria-label="Previous" data-page="${currentPage - 1}">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        `);

        // 페이지 번호 버튼
        for (let i = 0; i < totalPages; i++) {
            const activeClass = i === currentPage ? 'active' : '';
            pagination.append(`
                <li class="page-item ${activeClass}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `);
        }

        // 다음 버튼
        const nextDisabled = currentPage === totalPages - 1 ? 'disabled' : '';
        pagination.append(`
            <li class="page-item ${nextDisabled}">
                <a class="page-link" href="#" aria-label="Next" data-page="${currentPage + 1}">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        `);
    }

    // 페이지네이션 버튼 클릭 이벤트 처리
    $(document).on('click', '.page-link', function (e) {
        e.preventDefault();
        const page = $(this).data('page');

        // 유효한 페이지 번호인 경우 댓글 로드
        if (page >= 0 && page < totalPages) {
            loadComments(page);
        }
    });

    // 댓글 작성 기능
    $('#create-comment-btn').on('click', function () {
        const commentData = {
            comment: $('#comment-content').val() // 댓글 내용
        };

        // 유효성 검사
        if (!commentData.comment || commentData.comment.trim() === '') {
            alert('댓글 내용을 입력하세요.');
            return;
        }

        console.log('전송할 데이터:', commentData);

        // 새 댓글 작성 요청
        $.ajax({
            url: `/api/board/view/${id}/comments`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(commentData),
            success: function (newComment) {
                alert('댓글을 작성했습니다.');

                // 현재 페이지 댓글 다시 로드
                $.ajax({
                    url: `/api/board/view/${id}/pagedComments?page=${currentPage}&size=${pageSize}`,
                    type: 'GET',
                    success: function (comments) {
                        console.log('현재 페이지 댓글: ', comments);

                        // 마지막 페이지 로드
                        loadComments(totalPages - 1);

                        // 댓글 입력창 초기화
                        $('#comment-content').val('');
                    },
                    error: function (xhr, status, error) {
                        console.error('댓글 작성 후 로드 실패:', error);
                        alert('댓글 추가 후 데이터를 갱신하는 데 실패했습니다.');
                    }
                });
            },
            error: function (xhr, status, error) {
                console.error('댓글 작성 실패:', error);
                alert('댓글 작성에 실패했습니다.');
            }
        });
    });

    // 페이지 로드 시 첫 번째 페이지의 댓글 불러오기
    loadComments(currentPage);

});


/*
    //1. 페이지 로드 시 기존 댓글 불러오기
    $.ajax({
        url: '/api/board/view/' + id + '/pagedComments',
        type: 'GET',
        success: function(comments) {
            console.log('기존 댓글: ', comments);

            //댓글 리스트
            comments.content.forEach(comment => {
                const commentHtml = `
                    <ul class="list-group list-group-flush" style="height: 100px;">
                        <li class="list-group-item">
                            <div class="text-muted fst-italic mb-2" style="font-weight: bold">${comment.username}</div>
                            <div>${comment.comment}</div>
                            <hr>
                        </li>
                    </ul>
                `;
                //댓글 리스트에 추가
                $('#comment-list').append(commentHtml);
            });
        },
        error: function (xhr, status, error) {
            console.error('기존 댓글 로드 실패:', error);
            alert('댓글을 불러오는 데 실패했습니다.');
        }
    });

    //2. 댓글 작성 기능
    $('#create-comment-btn').on('click', function() {
        const commentData = {
            comment: $('#comment-content').val() // 댓글 내용
        };
        console.log('전송할 데이터:', commentData);

        //새 댓글 작성 요청
        $.ajax({
            url: '/api/board/view/' + id + '/comments',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(commentData),
            success: function(newComment) {
                alert('댓글을 작성했습니다.');

                //새로운 댓글 DOM에 추가
                const commentHtml = `
                    <ul class="list-group list-group-flush" style="height: 100px;">
                        <li class="list-group-item">
                            <div class="text-muted fst-italic mb-2" style="font-weight: bold">${newComment.username}</div>
                            <div>${newComment.comment}</div>
                            <hr>
                        </li>
                    </ul>
                `;

                //댓글 리스트에 추가
                $('#comment-list').append(commentHtml);

                //댓글 입력창 초기화
                $('#comment-content').val('');
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                alert('댓글 작성에 실패했습니다.');
            }
        });
    });
});
*/