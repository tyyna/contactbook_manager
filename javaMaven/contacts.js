function main() {
    $('.info').hide();

    $('tr #contact').click(function () {
        var cid = $(this).id;
        $('.info').hide();
        $('#cid .info').show();
    });

}


$(document).ready(main);
