$('#custom-upload-back-btn').click(() => {
    changePageBottomIn($('#custom-upload-body'), $('#step1-body'));
});

$(document).on('click', 'table.custom-upload td.remove', function () {
    $(this).parent().remove();
});

const currentActiveTabClass = 'tab-current';
const currentActivePageClass = 'content-current';

$(document).on('click', 'ul.tab > li', function () {
    const $tabs = $('ul.tab > li');
    $tabs.removeClass(currentActiveTabClass);
    $(this).addClass(currentActiveTabClass);

    const idx = this.dataset.idx;
    const $pages = $('.tab > section.page');
    $pages.removeClass(currentActivePageClass);
    $(`#section-tzoid-${idx}`).addClass(currentActivePageClass);
});
