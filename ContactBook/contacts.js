$(document).ready(function () {
    $('.info').hide();

    $('.contact').click(function () {
        $('.contact').css('color', 'black');
        $('.contact').css('background', 'none');
        $('.info').hide();

        var cid = ($(this).attr("class").split(' ')[1]);
        $(this).css('color', 'white');
        $(this).css('background', '#3c678d');
        $('.' + cid).show();
    });
});