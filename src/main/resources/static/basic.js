let targetId;

$(document).ready(function () {
    if ($.cookie('token')) {
        $.ajaxSetup({
            headers:{
                'Authorization': $.cookie('token')
            }
        })
    } else {
        window.location.href = '/user/loginView';
    }

    $.ajax({
        type: "POST",
        url: `/user/userinfo`,
        contentType: "application/json",
        success: function (response) {
            const username = response.username;
            const isAdmin = !!response.admin;

            if (!username) {
                window.location.href = '/user/loginView';
            }

            $('#username').text(username);
            if (isAdmin) {
                showProduct(true);
            } else {
                showProduct();
            }
        },
        error: function() {
            window.location.href = '/user/loginView';
        }
    })

    // id 가 query 인 녀석 위에서 엔터를 누르면 execSearch() 함수를 실행하라는 뜻입니다.
    $('#query').on('keypress', function (e) {
        if (e.key == 'Enter') {
            execSearch();
        }
    });
    $('#close').on('click', function () {
        $('#container').removeClass('active');
    })

    $('.nav div.nav-see').on('click', function () {
        $('div.nav-see').addClass('active');
        $('div.nav-search').removeClass('active');
        $('div.nav-article').removeClass('active');

        $('#see-area').show();
        $('#search-area').hide();
        $('#article-area').hide();
    })
    $('.nav div.nav-search').on('click', function () {
        $('div.nav-see').removeClass('active');
        $('div.nav-search').addClass('active');
        $('div.nav-article').removeClass('active');

        $('#see-area').hide();
        $('#search-area').show();
        $('#article-area').hide();
    })

    $('.nav div.nav-search').on('click', function () {
        $('div.nav-see').removeClass('active');
        $('div.nav-search').removeclass('active');
        $('div.nav-article').addClass('active');

        $('#see-area').hide();
        $('#search-area').hide();
        $('#article-area').show();
    })
})

function showProduct(isAdmin = false) {
    // 1. GET /api/products 요청
    // 2. #product-container(관심상품 목록), #search-result-box(검색결과 목록) 비우기
    // 3. for 문 마다 addProductItem 함수 실행시키고 HTML 만들어서 #product-container 에 붙이기
    $.ajax({
        type: 'GET',
        url: isAdmin ? '/api/admin/products' : '/api/products',
        success: function (response) {
            $('#product-container').empty();
            $('#search-result-box').empty();
            for (let i = 0; i < response.length; i++) {
                let product = response[i];
                let tempHtml = addProductItem(product);
                $('#product-container').append(tempHtml);
            }
        }
    })
}

function addProductItem(product) {
    return `<div class="product-card" onclick="window.location.href='${product.link}'">
                <div class="card-header">
                    <img src="${product.image}"
                         alt="">
                </div>
                <div class="card-body">
                    <div class="title">
                        ${product.title}
                    </div>
                    <div class="lprice">
                        <span>${numberWithCommas(product.lprice)}</span>원
                    </div>
                    <div class="isgood ${product.lprice > product.myprice ? 'none' : ''}">
                        최저가
                    </div>
                </div>
            </div>`;
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function execSearch() {
    let query = $('#query').val();
    if (query == '') {
        alert('검색어를 입력해주세요');
        $('#query').focus();
        return;
    }
    $.ajax({
        type: 'GET',
        url: `/api/search?query=${query}`,
        success: function (response) {
            $('#search-result-box').empty();
            for (let i = 0; i < response.length; i++) {
                let itemDto = response[i];
                let tempHtml = addHTML(itemDto);
                $('#search-result-box').append(tempHtml);
            }
        }
    })
}

function addHTML(itemDto) {
    return `<div class="search-itemDto">
                <div class="search-itemDto-left">
                    <img src="${itemDto.image}" alt="">
                </div>
                <div class="search-itemDto-center">
                    <div>${itemDto.title}</div>
                    <div class="price">
                        ${numberWithCommas(itemDto.lprice)}
                        <span class="unit">원</span>
                    </div>
                </div>
                <div class="search-itemDto-right">
                    <img src="images/icon-save.png" alt="" onclick='addProduct(${JSON.stringify(itemDto)})'>
                </div>
            </div>`
}

function addProduct(itemDto) {
    $.ajax({
        type: "POST",
        url: '/api/products',
        contentType: "application/json",
        data: JSON.stringify(itemDto),
        success: function (response) {
            $('#container').addClass('active');
            targetId = response.id;
        }
    })
}

function setMyprice() {
    let myprice = $('#myprice').val();
    if (myprice == '') {
        alert('올바른 가격을 입력해주세요');
        return;
    }
    $.ajax({
        type: "PUT",
        url: `/api/products/${targetId}`,
        contentType: "application/json",
        data: JSON.stringify({myprice: myprice}),
        success: function (response) {
            $('#container').removeClass('active');
            alert('성공적으로 등록되었습니다.');
            window.location.reload();
        }
    })
}

function addProduct(itemDto) {
    $.ajax({
        type: "POST",
        url: '/api/products',
        contentType: "application/json",
        data: JSON.stringify(itemDto),
        success: function (response) {
            $('#container').addClass('active');
            targetId = response.id;
        }
    })
}

function addHTML(id, username, contents, modifiedAt) {
    // 1. HTML 태그를 만듭니다.
    let tempHtml = `<div class="card">
                                <div class="metadata">
                                    <div class="date">
                                        ${modifiedAt}
                                    </div>
                                    <div id="${id}-username" class="username">
                                        ${username}
                                    </div>
                                </div>
                                <div class="contents">
                                    <div id="${id}-contents" class="text">
                                        ${contents}
                                    </div>
                                    <div id="${id}-editarea" class="edit">
                                        <textarea id="${id}-textarea" class="te-edit" name="" id="" cols="30" rows="5"></textarea>
                                    </div>
                                </div>
                                <div class="footer">
                                    <img id="${id}-edit" class="icon-start-edit" src="images/edit.png" alt="" onclick="editPost('${id}')">
                                    <img id="${id}-delete" class="icon-delete" src="images/delete.png" alt="" onclick="deleteOne('${id}')">
                                    <img id="${id}-submit" class="icon-end-edit" src="images/done.png" alt="" onclick="submitEdit('${id}')">
                                </div>
                            </div>`;
    // 2. #cards-box 에 HTML을 붙인다.
    $('#cards-box').append(tempHtml);
}

// 메모를 생성합니다.
function writePost() {
    // 1. 작성한 메모를 불러옵니다.
    let contents = $('#contents').val();

    // 2. 작성한 메모가 올바른지 isValidContents 함수를 통해 확인합니다.
    if (isValidContents(contents) == false) {
        return;
    }
    // 3. genRandomName 함수를 통해 익명의 username을 만듭니다.
    let username = genRandomName(10);

    // 4. 전달할 data JSON으로 만듭니다.
    let data = {'username': username, 'contents': contents};

    // 5. POST /api/memos 에 data를 전달합니다.
    $.ajax({
        type: "POST",
        url: "/api/memos",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('메시지가 성공적으로 작성되었습니다.');
            window.location.reload();
        }
    });
}

// 메모를 수정합니다.
function submitEdit(id) {
    // 1. 작성 대상 메모의 username과 contents 를 확인합니다.
    let username = $(`#${id}-username`).text().trim();
    let contents = $(`#${id}-textarea`).val().trim();

    // 2. 작성한 메모가 올바른지 isValidContents 함수를 통해 확인합니다.
    if (isValidContents(contents) == false) {
        return;
    }

    // 3. 전달할 data JSON으로 만듭니다.
    let data = {'username': username, 'contents': contents};

    // 4. PUT /api/memos/{id} 에 data를 전달합니다.
    $.ajax({
        type: "PUT",
        url: `/api/memos/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert('메시지 변경에 성공하였습니다.');
            window.location.reload();
        }
    });
}

// 메모를 삭제합니다.
function deleteOne(id) {
    // 1. DELETE /api/memos/{id} 에 요청해서 메모를 삭제합니다.
    $.ajax({
        type: "DELETE",
        url: `/api/memos/${id}`,
        success: function (response) {
            alert('메시지 삭제에 성공하였습니다.');
            window.location.reload();
        }
    })
}