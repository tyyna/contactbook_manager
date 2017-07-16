/*function main() {
    $('.info').hide();

    $('tr #contact').click(function () {
        var cid = $(this).id;
        $('.info').hide();
        $('#cid .info').show();
    });

}
*/

$(document).ready(function () {
    $('.info').hide();

    $('tr #contact').click(function () {
        var cid = $(this).id;
        $('.info').hide();
        $('#cid .info').show();
    });
});
