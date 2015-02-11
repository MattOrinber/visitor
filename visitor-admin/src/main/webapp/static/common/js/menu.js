/**
 * Menu菜单函数
 */
var Menu = (function () {
    var handleMenuInit = function(){
        var current = jQuery.cookie("currentMenu");
        jQuery(".page-sidebar .sub-menu").find("#"+current).parent('').addClass('active');
        jQuery(".page-sidebar .sub-menu").find("#"+current).parent('').parent('').parent('li').addClass('active');
    };

    var handleMenuClick = function (id, url) {
        jQuery.cookie('currentMenu', id);
        window.location.href = url;
    };

    return {
        init: function () {
            handleMenuInit();
        },
        click: function (id, url) {
            handleMenuClick(id, url);
        }
    }
}());