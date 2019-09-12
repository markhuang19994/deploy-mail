$(function () {
    const modal = document.getElementById("myModal");
    const closeSpan = modal.querySelector('.close');
    const modelP = document.getElementById('modal-content__p-1');

    window.showPopup = (text) => {
        modelP.innerText = text;
        modal.style.display = "block";
    };

    closeSpan.addEventListener('click', () => modal.style.display = "none");

    $(document).on('click', 'body', e => {
        if (e.target === modal) {
            modal.style.display = "none";
        }
    });
});